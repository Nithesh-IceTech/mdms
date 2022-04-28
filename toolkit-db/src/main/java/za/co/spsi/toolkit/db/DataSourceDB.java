package za.co.spsi.toolkit.db;

import javassist.bytecode.ByteArray;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.VirtuallyDeleted;
import za.co.spsi.toolkit.db.audit.AuditEntity;
import za.co.spsi.toolkit.db.fields.FieldError;
import za.co.spsi.toolkit.entity.DataSource;
import za.co.spsi.toolkit.entity.Entity;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.entity.FieldList;
import za.co.spsi.toolkit.io.IOUtil;
import za.co.spsi.toolkit.util.Assert;
import za.co.spsi.toolkit.util.StringList;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static za.co.spsi.toolkit.util.Util.handle;

/**
 * Created by jaspervdb on 2/3/16.
 */
public class DataSourceDB<E extends EntityDB> implements DataSource<E>, Closeable {

    public static final Logger TAG = Logger.getLogger(DataSourceDB.class.getName());

    private Class<E> entityClass;
    private E entity;
    private ResultSet rs;
    private boolean keepHistory = false, skipGet = false, skipHasNext = false;
    private int scrolledAhead = -1;
    private boolean closed = false;

    public static final List<SQLEventCallback> SQL_EVENT_CALLBACKS = new ArrayList<>();

    public static SQLEventCallback registerEvent(SQLEventCallback eventCallback) {
        SQL_EVENT_CALLBACKS.add(eventCallback);
        return eventCallback;
    }

    public static void deRegisterEvent(SQLEventCallback eventCallback) {
        SQL_EVENT_CALLBACKS.remove(eventCallback);
    }

    public static interface SQLEventCallback {
        void callback(String sql);
    }

    public DataSourceDB(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    public DataSourceDB(E entity) {
        this.entity = entity;
        this.entityClass = (Class<E>) entity.getClass();
    }

    public DataSourceDB() {
        this.entityClass = getType();
    }

    public DataSourceDB(Class<E> entityClass, ResultSet rs) {
        this(entityClass, rs, false);
    }

    public DataSourceDB(E entity, ResultSet rs, boolean keepHistory) {
        this.entity = entity;
        this.entityClass = (Class<E>) entity.getClass();
        this.rs = rs;
        this.keepHistory = keepHistory;
    }

    public DataSourceDB(Class<E> entityClass, ResultSet rs, boolean keepHistory) {
        this.entityClass = entityClass;
        this.rs = rs;
        this.keepHistory = keepHistory;
    }

    public void setKeepHistory(boolean keepHistory) {
        this.keepHistory = keepHistory;
    }

    public DataSourceDB(ResultSet rs) {
        this.entityClass = getType();
        this.rs = rs;
    }

    public E getEntityInstance() {
        try {
            return entityClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T> Class<T> getType() {
        Class type = (Class<T>) ((ParameterizedType) entity.get(this).getGenericType()).getActualTypeArguments()[0];
        return type;
    }

    private static <T> T getFromRs(ResultSet rs, Class<T> type, String colName) throws SQLException {
        try {
            T value;
            if (String.class.equals(type)) {
                value = (T) rs.getString(colName);
            } else if (Short.class.equals(type)) {
                value = (T) new Short(rs.getShort(colName));
            } else if (Integer.class.equals(type)) {
                value = (T) new Integer(rs.getInt(colName));
            } else if (Long.class.equals(type)) {
                value = (T) new Long(rs.getLong(colName));
            } else if (Boolean.class.equals(type)) {
                value = (T) new Boolean(rs.getBoolean(colName));
            } else if (Character.class.equals(type)) {
                value = rs.getString(colName) == null ? (T) null : (T) new Character(rs.getString(colName).charAt(0));
            } else if (Double.class.equals(type)) {
                value = (T) new Double(rs.getDouble(colName));
            } else if (Float.class.equals(type)) {
                value = (T) new Float(rs.getFloat(colName));
            } else if (byte[].class.equals(type)) {
                value = (T) rs.getBytes(colName);
//                Blob blob = rs.getBlob(colName);
//                value = (T) (blob != null ? IOUtil.readFully(blob.getBinaryStream()) : null);
            } else if (java.sql.Date.class.equals(type)) {
                value = (T) rs.getDate(colName);
            } else if (java.sql.Time.class.equals(type)) {
                value = (T) rs.getTime(colName);
            } else if (java.sql.Timestamp.class.equals(type)) {
                value = (T) rs.getTimestamp(colName);
            } else if (BigDecimal.class.equals(type)) {
                value = (T) rs.getBigDecimal(colName);
            } else if (Object.class.equals(type)) {
                value = (T) rs.getObject(colName);
            } else {
                throw new UnsupportedOperationException(String.format("Unsupported field type %s", type));
            }
            return rs.wasNull() ? null : value;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void initFieldFromRs(EntityDB entity, Field field, ResultSet rs) throws SQLException, IOException {
        try {
            field.set(getFromRs(rs, field.getType(), entity.getColumnName(field, true)));
            field.reset();
        } catch (Exception ex) {
            if (!(ex instanceof UnsupportedOperationException)) {
                TAG.log(Level.SEVERE, ex.getMessage(), ex);
                throw new SQLException(String.format("Error setting field %s.%s. Error", entity.getName(), field.getName(), ex.getMessage()), ex);
            } else {
                throw ex;
            }
        }
    }

    public static EntityDB initEntityFromRs(EntityDB entity, ResultSet rs) throws SQLException {
        try {
            entity.getFields().clearFields();
            for (Field field : entity.getFields()) {
                initFieldFromRs(entity, field, rs);
            }
            entity.setInDatabase(true);
            return entity;
        } catch (IOException ioe) {
            throw new SQLException(ioe.getMessage());
        }
    }

    public static int executeUpdate(javax.sql.DataSource dataSource, String sql, Object... params) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement ps = prepareStatement(connection, log(sql), params)) {
                return ps.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static int executeUpdate(Connection connection, String sql, Object... params) throws SQLException {
        try (PreparedStatement ps = prepareStatement(connection, sql, params)) {
            return ps.executeUpdate();
        }
    }

    public E get() {
        return rs != null ? get(rs) : null;
    }

    public void close() {
        if (!closed) {
            closed = true;
            if (rs != null) {
                try {
                    Statement smt = rs.getStatement();
                    rs.close();
                    smt.close();
                } catch (Exception ex) {}
            }

        }
    }

    public E get(ResultSet rs) {
        try {
            if (entity == null || keepHistory) {
                try {
                    entity = entityClass.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            if (skipGet || stepNext(rs)) {
                skipGet = false;
                initEntityFromRs(entity, rs);
                return entity;
            } else {
                entity = null;
                close();
                return null;
            }
        } catch (SQLException e) {
            // ensure that you close the connection
            close();
            throw new RuntimeException(e);
        }
    }

    private static String getSetValueInput(int size) {
        StringList strings = new StringList();
        for (int i = 0; i < size; i++) {
            strings.add("?");
        }
        return strings.toString(",");
    }

    public static String log(String sql) {
        TAG.log(Level.FINE, sql);
        for (SQLEventCallback eventCallback : SQL_EVENT_CALLBACKS) {
            eventCallback.callback(sql);
        }
        return sql;
    }

    public static PreparedStatement getUpdateStatement(Connection connection, EntityDB entity) throws SQLException {
        FieldList ids = entity.getId();
        Assert.isTrue(!ids.isEmpty(), String.format("Entity %s does not have any @Id fields", entity.getName()));

        PreparedStatement ps =
            connection.prepareStatement(
                log(String.format("update %s set %s where %s",
                    entity.getName(),
                    entity.getColumnNames(entity.getFields().getSet()).append(" = ?").toString(","),
                    entity.getColumnNames(ids).append(" = ?").toString(" and "))));
        return ps;
    }

    public static PreparedStatement getLoadFromIdRs(Connection connection, EntityDB entity) throws SQLException {
        Assert.isTrue(
            entity.getId().size() == 1,
            String.format("EntityDB %s with more than one id not supported yet", entity.getClass().getName()));
        
        PreparedStatement ps =
            connection.prepareStatement(
                log(String.format("select * from %s where %s",
                    entity.getName(),
                    entity.getColumnNames(entity.getId()).append(" = ?").toString(" and "))));
        
        entity.setPreparedStatementWhereValues(ps, 1);
        return ps;
    }

    public static PreparedStatement getRsFromSet(Connection connection, EntityDB entity) throws SQLException {
        PreparedStatement ps =
            connection.prepareStatement(
                log(String.format("select * from %s where %s",
                    entity.getName(),
                    entity.getColumnNames(entity.getFields().getSet()).append(" = ?").toString(" and "))));
        
        entity.setPreparedStatementWhereValues(ps, entity.getFields().getSet(), 1);
        return ps;
    }

    public static <E extends EntityDB> E loadFromId(Connection connection, E entity) {
        try (PreparedStatement ps = getLoadFromIdRs(connection, entity)) {
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return (E) initEntityFromRs(entity, rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static <E extends EntityDB> E loadFromId(javax.sql.DataSource dataSource, Entity entity) {
        return loadFromId(dataSource, (EntityDB) entity);
    }

    public static <E extends EntityDB> E loadFromId(javax.sql.DataSource dataSource, EntityDB entity) {
        try (Connection con = dataSource.getConnection()) {
            return (E) loadFromId(con, entity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static <E extends EntityDB> E loadFromId(Connection connection, EntityDB entity, Object idValue) {
        entity.getSingleId().set(idValue);
        return (E) loadFromId(connection, entity);
    }

    public static <E extends EntityDB> E loadFromId(javax.sql.DataSource dataSource, EntityDB entity, Object idValue) {
        entity.getSingleId().set(idValue);
        return loadFromId(dataSource, entity);
    }

    public static <E extends EntityDB> DataSourceDB<E> getAllFromSet(Connection connection, E entity) {
        try {
            PreparedStatement ps = getRsFromSet(connection, entity);
            ResultSet rs = ps.executeQuery();
            return new DataSourceDB<>(entity, rs, false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static <E extends EntityDB> DataSourceDB getAllFromSet(Connection connection, Entity entity) {
        return getAllFromSet(connection, (E) entity);
    }

    public static <E extends EntityDB> List<E> getAllFromSet(javax.sql.DataSource dataSource, E entity) {
        try {
            try (Connection connection = dataSource.getConnection()) {
                return getAllFromSet(connection, entity).getAllAsList();
            }
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
    }

    public static <E extends EntityDB> List<E> getAllFromSet(javax.sql.DataSource dataSource, Class<E> type, Entity entity) {
        return getAllFromSet(dataSource, (E) entity);
    }

    public static <T extends EntityDB> T getFromSet(Connection connection, T entity) {
        try {
            try (PreparedStatement ps = getRsFromSet(connection, entity)) {
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return (T) initEntityFromRs(entity, rs);
                    } else {
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends EntityDB> T getFromSet(javax.sql.DataSource dataSource, T entity) {
        try {
            try (Connection connection = dataSource.getConnection()) {
                return getFromSet(connection, entity);
            }
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
    }

    public static <T extends EntityDB> T getFromSet(javax.sql.DataSource dataSource, Entity entity) {
        return getFromSet(dataSource, (T) entity);
    }

    public static <T extends EntityDB> T getFromSet(Connection connection, Entity entity) {
        return getFromSet(connection, (T) entity);
    }

    public static PreparedStatement getInsertStatement(Connection connection, EntityDB entity) throws SQLException {
        PreparedStatement ps =
            connection.prepareStatement(
                log(String.format("insert into %s (%s) values (%s)",
                    entity.getName(),
                    entity.getColumnNames().toString(","), getSetValueInput(entity.getFields().size()))));
        return ps;
    }

    public static boolean setInsert(PreparedStatement ps, EntityDB entity) throws SQLException {
        entity.prepareFieldsForInsert();
        setPs(ps, entity.getFields());
        entity.setInDatabase(true);
        boolean result = ps.execute();
        FieldList idField = entity.getFields().getFieldsWithAnnotationWithMethodSignature(Id.class, "autoIncrement", true);
        if (!idField.isEmpty()) {
            try (ResultSet rs = ps.getGeneratedKeys()) {
                for (Field field : idField) {
                    if (rs.next()) {
                        field.set(getFromRs(rs, field.getType(), entity.getColumnName(field)));
                    } else {
                        throw new RuntimeException("Could not load auto increment from ud " + EntityDB.getFullColumnName(field));
                    }
                }
            }
        }
        return result;
    }

    public static boolean setInsert(Connection connection, EntityDB entity) throws SQLException {
        if (entity.beforeInsertEvent(connection)) {
            try (PreparedStatement ps = getInsertStatement(connection, entity)) {
                boolean inserted = setInsert(ps, entity);
                entity.afterInsertEvent(connection);
                // process auditing
                if (entity.isAuditable()) {
                    AuditEntity.createAuditLog(ps.getConnection(), entity, AuditEntity.Type.CREATE);
                }
                entity.getFields().reset();
                return inserted;
            }
        }
        return false;
    }

    public static <T> T executeResultInTx(javax.sql.DataSource dataSource, boolean startTx, Callback<T> callback) {
        try {
            try (Connection connection = dataSource.getConnection()) {
                if (startTx) {
                    connection.setAutoCommit(false);
                }
                try {
                    T value = callback.run(connection);
                    if (startTx) {
                        connection.commit();
                    }
                    return value;
                } catch (Exception ex) {
                    if (startTx) {
                        connection.rollback();
                    }
                    throw new RuntimeException(ex);
                }
            }
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
    }

    public static <T> T executeResultInTx(javax.sql.DataSource dataSource, Callback<T> callback) {
        return executeResultInTx(dataSource, true, callback);
    }

    public static void executeInTx(javax.sql.DataSource dataSource, boolean startTx, final EmptyCallback callback) {
        executeResultInTx(dataSource, startTx, new Callback<Object>() {
            @Override
            public <T> T run(Connection connection) throws Exception {
                callback.run(connection);
                return null;
            }
        });
    }

    public static void executeInTx(javax.sql.DataSource dataSource, final EmptyCallback callback) {
        executeInTx(dataSource, true, callback);
    }

    public static <T extends EntityDB> void executeInTx(javax.sql.DataSource dataSource, boolean startTx, T entity
            , final EmptyCallback callback, final EntityCallback<T> failedCallback) {
        try {
            try (Connection connection = dataSource.getConnection()) {
                if (startTx) {
                    connection.setAutoCommit(false);
                }
                try {
                    callback.run(connection);
                    DataSourceDB.set(connection, entity);
                    if (startTx) {
                        connection.commit();
                    }
                } catch (Exception ex) {
                    if (startTx) {
                        connection.rollback();
                    }
                    for (Field field : entity.getFields().getFieldsOfInstance(FieldError.class)) {
                        ((FieldError) field).set(ex);
                    }
                    if (failedCallback != null) {
                        failedCallback.run(entity);
                    }
                    DataSourceDB.set(connection, entity);
                    if (startTx) {
                        connection.commit();
                    }
                }
            }
        } catch (SQLException sqle) {
            TAG.log(Level.WARNING, sqle.getMessage(), sqle);
            throw new RuntimeException(sqle);
        }
    }

    public static interface Callback<T> {
        <T> T run(Connection connection) throws Exception;
    }

    public static interface EmptyCallback<T> {
        void run(Connection connection) throws Exception;
    }

    public static interface EntityCallback<T> {
        void run(T entity);
    }

    public static interface CallbackResult {
        void callback(List result);
    }

    public static void setPs(PreparedStatement ps, FieldList set) throws SQLException {

        int i = 0;
        for (Field field : set) {
            try {
                Column column = (Column) field.getAnnotation(Column.class);
                if (column != null && column.clob()) {
                    Assert.isTrue(
                        String.class.equals(field.getType()),
                        "Clob fields %s must be Strings", ((EntityDB) field.getEntity()).getFullColumnName(field));
                    if (field.get() != null) {
                        ps.setCharacterStream(
                            i + 1,
                            new InputStreamReader(new ByteArrayInputStream(field.getAsString().getBytes())));
                    } else {
                        ps.setObject(i + 1, null);
                    }
                } else if (column != null && column.toNumber()) {
                    Assert.isTrue(Boolean.class.equals(field.getType()), "@Column toNumber attribute can only be used for boolean fields: %s", ((EntityDB) field.getEntity()).getFullColumnName(field));
                    if (field.get() != null) {
                        ps.setInt(i + 1, field.getBoolAsNumber());
                    } else {
                        ps.setObject(i + 1, null);
                    }
                } else if (byte[].class.equals(field.getType())) {
                    if (field.get() != null) {
                        ps.setBinaryStream(i + 1, new ByteArrayInputStream((byte[]) field.get()));
                    } else {
                        ps.setObject(i + 1, null);
                    }
                } else if (Character.class.equals(field.getType())) {
                    ps.setString(i + 1, field.getAsString());
                } else if (String.class.equals(field.getType()) && column != null && column.autoCrop() && field.get() != null &&
                        field.getAsString().length() > column.size()) {

                    ps.setObject(i + 1, field.getAsString().substring(0, column.size() != -1 ? column.size() : 200));
                } else {
                    ps.setObject(i + 1, field.get());
                }
                i++;
            } catch (SQLException sqle) {
                throw new SQLException(String.format("SQL Error. Field %s: %s", field.getName(), sqle.getMessage()), sqle);
            }
        }
    }

    public static boolean setUpdate(PreparedStatement ps, EntityDB entity) throws SQLException {
        try {
            FieldList set = entity.getFields().getSet();
            if (!set.isEmpty()) {
                setPs(ps, set);
                FieldList ids = entity.getId();
                for (int i = 0; i < ids.size(); i++) {
                    ps.setObject(i + 1 + set.size(), ids.get(i).get());
                }
                entity.setInDatabase(true);
                boolean updated = ps.execute();
                // process auditing
                if (entity.isAuditable()) {
                    AuditEntity.createAuditLog(ps.getConnection(), entity, AuditEntity.Type.UPDATE);
                }
            }
            return true;
        } finally {
            entity.getFields().reset();
        }
    }

    public static boolean setUpdate(Connection connection, EntityDB entity) throws SQLException {
        if (entity.beforeUpdateEvent(connection)) {
            try (PreparedStatement ps = getUpdateStatement(connection, entity)) {
                boolean updated = setUpdate(ps, entity);
                entity.afterUpdateEvent(connection);
                return updated;
            }
        }
        return false;
    }

    public static boolean setUpdate(javax.sql.DataSource dataSource, EntityDB entity) throws SQLException {
        try {
            try (Connection connection = dataSource.getConnection()) {
                return setUpdate(connection, entity);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * This will do an insert if it has not been loaded from the entity
     *
     * @param connection
     * @param entityDB
     * @return
     */
    public static <E extends EntityDB> E set(Connection connection, E entityDB) {
        try {
            if (entityDB.isInDatabase()) {
                setUpdate(connection, entityDB);
            } else {
                setInsert(connection, entityDB);
            }
            for (EntityDB txRefEntity : entityDB.getTxRefEntities()) {
                set(connection, txRefEntity);
            }
            return entityDB;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static <E extends EntityDB> E set(Connection connection, Entity entityDB) {
        return set(connection, (E) entityDB);
    }

    public static <E extends EntityDB> E set(javax.sql.DataSource dataSource, final E entityDB) {
        return executeResultInTx(dataSource, false, new Callback<E>() {
            @Override
            public <T> T run(Connection connection) throws Exception {
                return (T) set(connection, entityDB);
            }
        });
    }

    public static <T extends EntityDB> T setIfChanged(Connection connection, final T entityDB) {
        EntityDB entity = DataSourceDB.loadFromId(connection, entityDB.clone());
        if (entity == null || !entity.equalsEntity(entityDB)) {
            entityDB.setInDatabase(entity != null);
            if (entity != null) {
                entityDB.getFields().getNotEquals(entity.getFields()).setSet();
            }
            DataSourceDB.set(connection, entityDB);
        }
        return (T) entityDB;
    }

    public static <T extends EntityDB> T setIfChanged(javax.sql.DataSource dataSource, final T entityDB) {
        return (T) executeResultInTx(dataSource, false, new Callback<EntityDB>() {
            @Override
            public <T> T run(Connection connection) throws Exception {
                return (T) setIfChanged(connection, entityDB);
            }
        });
    }

    public static <T extends EntityDB> T createIfNotExists(Connection connection, final T entityDB) {
        EntityDB entity = DataSourceDB.loadFromId(connection, entityDB.clone());
        if (entity == null) {
            DataSourceDB.set(connection, entityDB);
        }
        return (T) entityDB;
    }

    public static <T extends EntityDB> T setIfNotExists(Connection connection, final T entityDB) {
        if (DSDB.getFromSet(connection, entityDB.clone()) == null) {
            DSDB.set(connection, entityDB);
        }
        return entityDB;
    }

    public static <T extends EntityDB> T createOrUpdate(Connection connection, final T entityDB) throws SQLException {
        entityDB.setInDatabase(DataSourceDB.loadFromId(connection, entityDB.clone()) != null);
        return (T) DataSourceDB.set(connection, entityDB);
    }

    public static void delete(Connection connection, EntityDB entity) throws SQLException {
        if (entity.beforeDeleteEvent(connection)) {
            try {
                if (entity.isVirtuallyDeleted()) {
                    ((VirtuallyDeleted) entity).virtualDelete(connection);
                } else {
                    try (PreparedStatement ps =
                             connection.prepareStatement(
                                log(String.format("delete from %s where %s",
                                    entity.getName(),
                                    entity.getColumnNames(entity.getId()).append(" = ?").toString(" and "))))){
                        
                        entity.setPreparedStatementWhereValues(ps, 1);
                        ps.execute();
                        AuditEntity.createAuditLog(connection, entity, AuditEntity.Type.DELETE);
                    }
                }
            } finally {
                entity.afterDeleteEvent(connection);
            }
            for (EntityDB txRefEntity : entity.getTxRefEntities()) {
                delete(connection, txRefEntity);
            }
        }
    }

    public static void deleteAllFrom(Connection connection, String tableName) throws SQLException {

        try (PreparedStatement ps = connection.prepareStatement(log(String.format("delete from %s", tableName)))) {
            ps.execute();
        }
    }

    public static void deleteFromSet(Connection connection, EntityDB entity) throws SQLException {
        try (PreparedStatement ps =
                 connection.prepareStatement(
                    log(String.format("delete from %s where %s",
                        entity.getName(),
                        entity.getColumnNames(entity.getFields().getSet()).append(" = ?").toString(" and "))))) {
            int index = 1;
            for (Field field : entity.getFields().getSet()) {
                ps.setObject(index++, field.get());
            }
            ps.execute();
        }
    }

    public static void deleteFromSet(javax.sql.DataSource dataSource, final EntityDB entity) throws SQLException {
        executeInTx(dataSource, false, new EmptyCallback() {
            @Override
            public void run(Connection connection) throws Exception {
                deleteFromSet(connection, entity);
            }
        });
    }

    public static void deleteHandleException(Connection connection, EntityDB entity) {
        try {
            delete(connection, entity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void execute(Connection connection, String sql, Object... params) {
        try (PreparedStatement ps = prepareStatement(connection, sql, params)) {
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void execute(javax.sql.DataSource dataSource, final String where, final Object... params) {
        executeInTx(dataSource, false, new EmptyCallback() {
            @Override
            public void run(Connection connection) throws Exception {
                execute(connection, where, params);
            }
        });
    }

    public static void delete(javax.sql.DataSource dataSource, final EntityDB entityDB) {
        executeInTx(dataSource, false, new EmptyCallback() {
            @Override
            public void run(Connection connection) throws Exception {
                delete(connection, entityDB);
            }
        });
    }

    public static PreparedStatement prepareStatement(
        Connection connection, String sql, Object... params) throws SQLException {
        
        PreparedStatement ps = connection.prepareStatement(log(sql));

        if (params != null) {
            // Index in statement
            int index = 1;
            for (int i = 0; i < params.length; i++) {
                if (params[i] instanceof Object[]) {
                    Object[] objArray = ((Object[]) params[i]);
                    for (int j = 0; j < objArray.length; j++) {
                        ps.setObject(index++, objArray[j]);
                    }
                } else {
                    ps.setObject(index++, params[i]);
                }
            }
        }
        return ps;
    }

    /**
     * build the query and return its values
     *
     * @param connection
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    public static void executeQuery(
        Connection connection, CallbackResult callback,
        Class types[], String sql, Object... params) throws SQLException {
        
        try (PreparedStatement ps = prepareStatement(connection, sql, params)) {
            try (ResultSet rs = ps.executeQuery()) {
                ResultSetMetaData metaData = rs.getMetaData();
                while (rs.next()) {
                    List set = new ArrayList<>();
                    if (types != null) {
                        Assert.isTrue(metaData.getColumnCount() == types.length, String.format("Column count is %d. Supplied types %d", metaData.getColumnCount(), types.length));
                    }
                    for (int i = 0; i < metaData.getColumnCount(); i++) {
                        set.add(getFromRs(rs, types == null ? Object.class : types[i], metaData.getColumnName(i + 1)));
                    }
                    callback.callback(set);
                }
            }
        }
    }

    public static List<List> executeQuery(Connection connection, Class types[], String sql, Object... params) throws SQLException {
        final List<List> values = new ArrayList<>();
        executeQuery(connection, new CallbackResult() {
            @Override
            public void callback(List result) {
                values.add(result);
            }
        }, types, sql, params);
        return values;
    }

    public static void executeQuery(javax.sql.DataSource dataSource, CallbackResult callbackResult, Class types[], String sql, Object... params) {
        try {
            try (Connection connection = dataSource.getConnection()) {
                executeQuery(connection, callbackResult, types, sql, params);
            }
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
    }

    public static List<List> executeQuery(javax.sql.DataSource dataSource, Class types[], String sql, Object... params) {
        try {
            try (Connection connection = dataSource.getConnection()) {
                return executeQuery(connection, types, sql, params);
            }
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
    }

    public static <T> T executeQuery(javax.sql.DataSource dataSource, Class<T> type, String sql, Object... params) {
        List<List> lists = executeQuery(dataSource, new Class[]{type}, sql, params);
        return !lists.isEmpty() && !lists.get(0).isEmpty() ? (T) lists.get(0).get(0) : null;
    }

    public static List<List> executeQuery(javax.sql.DataSource dataSource, String sql, Object... params) {
        return executeQuery(dataSource, (Class[]) null, sql, params);
    }

    public static <E extends EntityDB> E get(Class<E> eClass, javax.sql.DataSource dataSource, String query, Object... params) {
        try {
            try (Connection connection = dataSource.getConnection()) {
                return get(eClass, connection, query, params);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static <E extends EntityDB> E get(
        Class<E> eClass, Connection connection, String query, Object... params) throws SQLException {
        
        try (PreparedStatement ps = prepareStatement(connection, query, params)) {
            try (ResultSet rs = ps.executeQuery()) {
                return (E) new DataSourceDB<E>(eClass).get(rs);
            }
        }
    }

    public Iterator<E> iterator() {
        get();
        skipHasNext = true;
        skipGet = true;
        return this;
    }

    public DataSourceDB<E> getAll(Connection connection, boolean keepHistory, String query, Object... params) {
        try {
            PreparedStatement ps = prepareStatement(connection, query, params);
            this.keepHistory = keepHistory;
            rs = ps.executeQuery();
            return this;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public DataSourceDB<E> getAllWhere(Connection connection, String where, Object... params) {
        E entity = getEntityInstance();
        return getAll(connection, String.format("select %s from %s %s",
                EntityDB.getColumnNames(entity.getFields()).toString(","),
                entity.getName(), where != null ? "where " + where : ""), params != null && params.length == 1 && params[0] == null ? null : params);
    }

    /**
     * will not keep history by default
     *
     * @param connection
     * @param query
     * @param params
     * @return
     */
    public DataSourceDB<E> getAll(Connection connection, String query, Object... params) {
        return getAll(connection, false, query, params);
    }

    public List<E> getAllAsList(Connection connection, String query, Object... params) {
        List<E> values = new ArrayList<>();
        keepHistory = true;
        try {
            for (E entity : getAll(connection, true, query, params)) {
                values.add(entity);
            }
            return values;
        } finally {
            // close it in case of an exception occurrence
            close();
        }
    }

    public List<E> getAllAsList(javax.sql.DataSource dataSource, String query, Object... params) {
        try {
            try (Connection connection = dataSource.getConnection()) {
                return getAllAsList(connection, query, params);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static <E extends EntityDB> List<E> getAllAsList(Class<E> type, javax.sql.DataSource dataSource, String query, Object... params) {
        return new DataSourceDB(type).getAllAsList(dataSource, query, params);
    }

    public List<E> getAllAsList(javax.sql.DataSource dataSource) {
        try {
            try (Connection connection = dataSource.getConnection()) {
                return getAllAsList(connection, "select * from " + entityClass.newInstance().getName());
            }
        } catch (InstantiationException | IllegalAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<E> getAllAsList() {
        keepHistory = true;
        List<E> list = new ArrayList<>();
        for (E entity : this) {
            list.add(entity);
        }
        return list;
    }

    public static <E> List<E> getAllAsList(Class<E> entityClass, ResultSet rs) {
        try {
            List<E> list = new ArrayList<>();
            while (rs.next()) {
                list.add((E) initEntityFromRs((EntityDB) entityClass.newInstance(), rs));
            }
            return list;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


    private boolean stepNext(ResultSet rs) throws SQLException {
        if (rs != null && !rs.isClosed()) {
            boolean next = rs.next();
            return next;
        }
        return false;
    }

    public boolean hasNext() {
        try {
            if (skipHasNext) {
                skipHasNext = false;
                return entity != null;
            } else if (stepNext(rs)) {
                skipGet = true;
                return true;
            } else {
                close();
                return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public E next() {
        return get();
    }

    public void remove() {
    }

}

