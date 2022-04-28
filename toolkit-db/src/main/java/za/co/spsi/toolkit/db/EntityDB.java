package za.co.spsi.toolkit.db;

import org.json.JSONArray;
import org.json.JSONObject;
import za.co.spsi.toolkit.db.ano.*;
import za.co.spsi.toolkit.db.drivers.Driver;
import za.co.spsi.toolkit.db.drivers.DriverFactory;
import za.co.spsi.toolkit.db.fields.DBField;
import za.co.spsi.toolkit.db.fields.Index;
import za.co.spsi.toolkit.db.fields.IndexList;
import za.co.spsi.toolkit.entity.Entity;
import za.co.spsi.toolkit.entity.ExportPathObject;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.entity.FieldList;
import za.co.spsi.toolkit.entity.ano.ExportableEntity;
import za.co.spsi.toolkit.util.Assert;
import za.co.spsi.toolkit.util.StringList;
import za.co.spsi.toolkit.util.StringUtils;
import za.co.spsi.toolkit.util.Util;

import javax.sql.DataSource;
import java.lang.annotation.Annotation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by jaspervdb on 2/3/16.
 */
public class EntityDB extends Entity {

    private boolean inDatabase = false;
    private EntityRefList entityRefs = new EntityRefList();
    // entities that will be saved , deleted with this entity
    private List<EntityDB> txRefEntities = new ArrayList<>();
    private IndexList indexes = new IndexList();

    public EntityDB(String name) {
        super(name);
    }

    public void add(EntityRef entityRef) {
        entityRefs.add(entityRef);
    }

    public EntityRefList getEntityRefs() {
        return entityRefs;
    }

    /**
     * add a entity that will be saved / deleted with this entity
     * @param entityDB
     */
    public void addTxRefEntity(EntityDB entityDB) {
        txRefEntities.add(entityDB);
    }

    public void addIndex(Index index) {
        indexes.add(index);
    }

    public List<EntityDB> getTxRefEntities() {
        return txRefEntities;
    }

    public IndexList getIndexes() {
        return indexes;
    }

    public <T extends EntityDB> List<T> getTxRefEntities(Class<T> eClass) {
        List<EntityDB> entityDBs = new ArrayList<>();
        for (EntityDB entityDB : txRefEntities) {
            if (eClass.equals(entityDB.getClass())) {
                entityDBs.add(entityDB);
            }
        }
        return (List<T>) entityDBs;
    }

    @Override
    public Entity getFromSet(Object dataSource) {
        Assert.isTrue(dataSource instanceof DataSource, "Expected a javax.sql.DataSource");
        return DataSourceDB.getFromSet((DataSource) dataSource, this);
    }

    /**
     * ID could be an embedded Entity
     *
     * @return
     */
    public FieldList getId() {
        return new FieldList(getFields().getFieldsWithAnnotation(Id.class).keySet());
    }

    public FieldList getIdentifier() {
        return new FieldList(getFields().getFieldsWithAnnotation(Identifier.class).keySet());
    }

    public Field getSingleId() {
        FieldList ids = getId();
        Assert.isTrue(ids.size() == 1, "Entity %s has more than one id defined %s", getClass(), ids.getNames());
        return ids.get(0);
    }

    public String getIdWhere() {
        return getColumnNames(getId()).append(" = ? ").toString(" and ");
    }

    public PreparedStatement setPreparedStatementWhereValues(PreparedStatement ps, FieldList fields, int offsetIndex)
            throws SQLException {
        for (int i = 0; i < fields.size(); i++) {
            ps.setObject(offsetIndex + i,
                    fields.get(i).get() instanceof Character ? fields.get(i).getAsString() : fields.get(i).get());
        }
        return ps;
    }

    public PreparedStatement setPreparedStatementWhereValues(PreparedStatement ps, int offsetIndex)
            throws SQLException {
        return setPreparedStatementWhereValues(ps, getId(), offsetIndex);
    }

    public <E extends EntityDB> E setId(Class<E> typeClass, Object... idValues) {
        int i = 0;
        for (Field field : getId()) {
            field.set(idValues[i]);
        }
        return (E) this;
    }


    /**
     * init the uuid id fields
     */
    public void prepareFieldsForInsert() {
        for (Field field : getFields().getFieldsWithAnnotationWithMethodSignature(Id.class, "uuid", true)) {
            if (field.get() == null) {
                field.set(UUID.randomUUID().toString());
            }
        }
        Map<Field, Column> map = getFields().getFieldsWithAnnotation(Column.class);
        for (Field field : map.keySet()) {
            if (map.get(field).defaultValue().length() > 0 && field.get() == null) {
                setDefault(field, map.get(field));
            }
        }
    }

    private void setDefault(Field field, Column column) {
        switch (column.defaultValue()) {
            case ("now"): {
                Date date = new Date(System.currentTimeMillis());
                field.set(Util.getConvertedValue(date, field.getType(), date));
                break;
            }
            default:
                field.setSerial(column.defaultValue());
        }
    }

    public FieldList getFieldsWithAnnotation(Class aClass) {
        FieldList fields = new FieldList();
        Map<Field, Annotation> map = getFields().getFieldsWithAnnotation(aClass);
        for (Field field : map.keySet()) {
            fields.add(field);
        }
        return fields;
    }

    public static String convertCamelCase(String name) {
        char[] nameChar = name.toCharArray();
        StringBuilder sb = new StringBuilder("" + nameChar[0]);
        for (int i = 1; i < nameChar.length; i++) {
            sb.append(Character.isAlphabetic(nameChar[i - 1]) && Character.isAlphabetic(nameChar[i]) &&
                    !Character.isAlphabetic(nameChar[i - 1]) && Character.isUpperCase(nameChar[i]) ? "_" : "");
            sb.append(nameChar[i]);
        }
        return sb.toString();
    }

    public static String getColumnName(Field field, boolean allowAlternate) {
        if (allowAlternate && field.getAlternateName() != null) {
            return field.getAlternateName();
        } else {
            Column column = (Column) field.getAnnotation(Column.class);
            return column != null && column.name().length() > 0 ? column.name() : field.getName();
        }
    }

    public static String getColumnName(Field field) {
        Driver driver = DriverFactory.getDriver();
        String colName = getColumnName(field, true);
        return driver.isOracle() ? colName.toUpperCase() : colName.toLowerCase();
    }

    public static String getFullColumnName(Field field, boolean allowAlternate) {
        return String.format("%s.%s", field.getEntity().getName(), getColumnName(field, allowAlternate));
    }

    public static String getFullColumnName(Field field) {
        return getFullColumnName(field, true);
    }

    public static StringList getColumnNames(FieldList fields) {
        StringList names = new StringList();
        for (Field field : fields) {
            names.add(getColumnName(field));
        }
        return names;
    }

    public StringList getColumnNames() {
        return getColumnNames(getFields());
    }

    public static StringList getFullColumnNames(FieldList fields) {
        StringList names = new StringList();
        for (Field field : fields) {
            String colName = getFullColumnName(field);
            names.add(colName);
        }
        return names;
    }

    public StringList getFullColumnNames() {
        return getFullColumnNames(getFields());
    }

    public FieldList getForeignKeyFields(Class<? extends Entity> refEntity) {
        FieldList fields = getFields().getFieldsWithAnnotationWithMethodSignature(ForeignKey.class, "table", refEntity);
        return fields;
    }

    /**
     * update the foreign key references entity's id fields with your own id values
     *
     * @param fEntity
     */
    public boolean updateForeignKeyFieldValues(EntityDB fEntity) {

        FieldList fFields = getForeignKeyFields(fEntity.getClass());
        if (!fFields.isEmpty()) {
            for (Field fField : fFields) {
                ForeignKey fE = (ForeignKey) fField.getAnnotation(ForeignKey.class);
                Field idField = !StringUtils.isEmpty(fE.field()) ? fEntity.getFields().getByName(fE.field()) : fEntity.getSingleId();
                Assert.notNull(idField, "Unable to locate @Id Field by name %s in referenced from Foreign Entity %s field %s",
                        fE.field(), fEntity.getClass(), fField.getName());
                fField.set(idField.get());
            }
        }
        return !fFields.isEmpty();
    }

    public boolean isInDatabase() {
        return inDatabase;
    }

    public void setInDatabase(boolean inDatabase) {
        this.inDatabase = inDatabase;
    }

    /**
     * copy from entity
     * @param entity
     */
    @Override
    public void copyStrict(Entity entity,boolean strict) {
        super.copyStrict(entity,strict);
        if (entity instanceof EntityDB) {
            EntityDB entityDB = (EntityDB) entity;
            setInDatabase(entityDB.isInDatabase());
            getTxRefEntities().addAll(((EntityDB) entity).getTxRefEntities());
        }
    }

    public void initFromJson(JSONObject jsonObject) {
        getFields().initFromJson(jsonObject);
    }

    public static void initFromJsonArray(Connection connection, Class<EntityDB> eClass, JSONArray jsonArray, List<EntityDB> initEntities)
            throws IllegalAccessException, InstantiationException, SQLException {
        for (int i = 0; i < jsonArray.length(); i++) {
            initFromJson(connection, eClass, null, (JSONObject) jsonArray.get(i), initEntities);
        }
    }

    public static <E extends EntityDB> E initFromJson(
            Connection connection, Class<E> eClass, E entityDB, JSONObject jsonObject, List<EntityDB> initEntities) throws SQLException {
        try {
            entityDB = entityDB == null ? eClass.newInstance() : entityDB;
            entityDB.getId().initFromJson(jsonObject);
            entityDB = (E) DataSourceDB.loadFromId(connection, entityDB);
            entityDB = entityDB == null ? eClass.newInstance() : entityDB;
            entityDB.initFromJson(jsonObject);

            if (entityDB.getFields().isSet()) {
                DataSourceDB.set(connection, entityDB);
                initEntities.add(entityDB);
            }
            // load all the entity refs
            for (EntityRef entityRef : entityDB.getEntityRefs()) {
                if (entityRef.isContainedIn(jsonObject)) {

                    if (entityRef.getExportable() != null &&
                            entityRef.getExportable().deleteAllReferences()) {

                        if (entityRef.getfKey() != null && entityRef.getfKey().get() != null) {
                            // set Fkey to null
                            EntityDB fKtable = (EntityDB) ((ForeignKey) entityRef.getfKey().getAnnotation(ForeignKey.class)).table().newInstance();

                            DataSourceDB.execute(connection,
                                    "delete from " + fKtable.getName() +
                                            " where " + EntityDB.getColumnName(fKtable.getSingleId()) + " = ?", entityRef.getfKey().get());

                            entityDB.getFields().getByName(entityRef.getfKey().getExportName()).set(jsonObject.get(entityRef.getfKey().getExportName()));
                            DataSourceDB.set(connection, entityDB);

                        } else {
                            if (entityRef.getExportable() != null && entityRef.getExportable().deleteAllReferences()) {
                                DataSourceDB.execute(connection,
                                        "delete from " + entityRef.getForeignField().getEntity().getName() +
                                                " where " + EntityDB.getColumnName(entityRef.getForeignField()) + " = ?", entityDB.getSingleId().get());
                            }
                        }
                    }

                    // Must be a json object
                    if (entityRef.getFrom(jsonObject) instanceof JSONArray) {
                        initFromJsonArray(connection, entityRef.getType(), (JSONArray) entityRef.getFrom(jsonObject), initEntities);
                    } else {
                        initFromJson(connection, entityRef.getType(), null, (JSONObject) entityRef.getFrom(jsonObject), initEntities);
                    }
                }
            }

            // Load all additionalRegisteredEntities
            for (String registeredEntity : entityDB.getAdditionalRegisteredEntities().keySet()) {
                if (jsonObject.has(registeredEntity)) {

                    // Must be a json object
                    if (jsonObject.get(registeredEntity) instanceof JSONArray) {
                        initFromJsonArray(connection, entityDB.getAdditionalRegisteredEntities().get(registeredEntity),
                                (JSONArray) jsonObject.get(registeredEntity), initEntities);
                    } else {
                        initFromJson(connection, entityDB.getAdditionalRegisteredEntities().get(registeredEntity),
                                null, (JSONObject) jsonObject.get(registeredEntity), initEntities);
                    }
                }

            }

            return entityDB;
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    public static <E extends EntityDB> E initFromJson(Connection connection, Class<E> eClass, String json, List<EntityDB> initEntities) throws SQLException {
        return initFromJson(connection, eClass, null, new JSONObject(json), initEntities);
    }


    /**
     * map all child references
     *
     * @param connection
     * @param ids        id list to revent recursions
     * @return
     */
    public JSONObject getAsJson(Connection connection, boolean exportChild, boolean exportEntityName, List<String> ids) {

        if (ids.contains(getSingleId().getAsString())) {
            return null;
        } else {
            ids.add(getSingleId().getAsString());
        }

        JSONObject object = getAsJson();
        EntityRefList entityRefList = null;

        if (exportChild) {
            entityRefList = getEntityRefs().getExportableChildren();
        } else {
            entityRefList = getEntityRefs().getForcedExportables();
        }

        for (EntityRef entityRef : entityRefList) {
            List<JSONObject> array = new ArrayList<>();
            for (Object entity : entityRef.get(connection, null)) {

                JSONObject j = ((EntityDB) entity).getAsJson(connection, exportChild, ids);

                if (j != null) {
                    array.add(j);
                }
            }

            if (!array.isEmpty()) {
                object.put(entityRef.getExportName(), new JSONArray(array));
            }
        }

        if (exportEntityName) {
            JSONObject objectName = new JSONObject();
            objectName.put(getExportName(), object);
            return objectName;
        }

        return object;
    }

    /**
     * map all child references
     *
     * @param connection
     * @return
     */
    public JSONObject getAsJson(Connection connection, boolean getExportbaleChildren, List<String> ids) {
        return getAsJson(connection, getExportbaleChildren, false, ids);
    }

    /**
     * recursively build up parent
     *
     * @param connection
     * @param sync
     * @param childName
     * @param child
     * @return
     */
    private static Object[] getPathToParentAsJson(Connection connection, EntityDB sync, String childName, JSONObject child, int index) {
        StringList ids = new StringList();
        JSONObject obj = child == null ? sync.getAsJson(connection, true, ids) : sync.getAsJson(connection, false, ids);
        if (child != null) {
            obj.put(childName, child);
        }
        if (sync.getEntityRefs().getExportableParent() != null) {
            EntityDB parent = sync.getEntityRefs().getExportableParent().getOne(connection, null);
            if (parent != null) {
                return getPathToParentAsJson(connection, parent, sync.getExportName(), obj, index);
            }
        }

        obj = obj.put("indexInList", index);
        return new Object[]{sync.getExportName(), obj};
    }

    /**
     * @param connection
     * @return exportName, JsonObject
     */
    public Object[] getPathToParentAsJson(Connection connection, int index) {
        return getPathToParentAsJson(connection, this, null, null, index);
    }

    /**
     * notification before execute
     *
     * @return false to cancel the execute event
     */
    public boolean beforeDeleteEvent(Connection connection) throws SQLException {
        return true;
    }

    /**
     * notification before execute
     */
    public void afterDeleteEvent(Connection connection) {
    }


    /**
     * notification before execute
     *
     * @return false to cancel the execute event
     */
    public boolean beforeInsertEvent(Connection connection) {
        for (Field field : getFields().getFieldsOfInstance(DBField.class)) {
            ((DBField) field).beforeInsertEvent(connection);
        }
        return true;
    }

    /**
     * notification before execute
     */
    public void afterInsertEvent(Connection connection) {
    }


    /**
     * notification before execute
     *
     * @return false to cancel the execute event
     */
    public boolean beforeUpdateEvent(Connection connection) {
        for (Field field : getFields().getFieldsOfInstance(DBField.class)) {
            ((DBField) field).beforeUpdateEvent(connection);
        }
        return true;
    }

    /**
     * notification before execute
     */
    public void afterUpdateEvent(Connection connection) {
        // process auditing
    }

    public boolean isVirtuallyDeleted() {
        return this instanceof VirtuallyDeleted;
    }

    @Override
    public EntityDB clone() {
        return (EntityDB) super.clone();
    }

    public void exportAsJson(Connection connection, ExportPathObject epo, JSONMap map) {
        EntityDB clone = clone();
        for (Field field : clone.getFields().getAnnotation(ForeignKey.class)) {
            Class type = ((ForeignKey)field.getAnnotation(ForeignKey.class)).table();
            if (epo.getSubReverse(type) == null && epo.getSub(type) == null) {
                field.set(null);
            }
        }
        map.addEntity(clone);
        for (EntityRef entityRef : getEntityRefs()) {
            ExportPathObject epof = epo.getSub(entityRef.getType());
            if (epof != null) {
                if (entityRef.getfKey() == null) {
                    for (Object e : entityRef.get(connection)) {
                        map.addEntity((EntityDB)e);
                        ((EntityDB)e).exportAsJson(connection,epof,map);
                    }
                } else {
                    EntityDB entity = entityRef.getOne(connection);
                    if (entity != null) {
                        map.addEntity(entity);
                        entity.exportAsJson(connection,epof,map);
                    }
                }
            }
        }
    }

    public JSONObject exportAsJson(Connection connection) {
        Assert.isTrue(ExportableEntity.class.isAssignableFrom(getClass()),"Must be an ExportPathObject: " + getClass());
        JSONMap map = new JSONMap();
        ExportPathObject epo = ((ExportableEntity)this).getExportObject();
        Assert.notNull(epo,"Paths must be defined to export as json for %s",getClass().getName());
        exportAsJson(connection,epo,map);
        return map.toJSONObject();
    }


    public void onTableCreate(Connection connection) {}
}
