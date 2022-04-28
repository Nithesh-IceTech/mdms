package za.co.spsi.toolkit.db;


import org.json.JSONObject;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.entity.Exportable;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.entity.FieldList;
import za.co.spsi.toolkit.util.Assert;
import za.co.spsi.toolkit.util.StringList;

import javax.sql.DataSource;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jaspervdbijl
 * Date: 2013/02/14
 * Time: 7:18 AM
 * This class is used to define entity relational constraints
 */
public class EntityRef<E extends EntityDB> {
    private EntityDB entity;
    // if you point to the parent
    private Field fKey;
    private String sql;
    private FieldList sqlValues;

    public EntityRef(EntityDB entity) {
        this.entity = entity;
        entity.add(this);
    }

    public EntityRef(String sql, EntityDB entity) {
        this(entity);
        this.sql = sql;
    }

    /**
     * if you point to the parent
     *
     * @param fKey
     * @param entity
     */
    public EntityRef(Field fKey, EntityDB entity) {
        this(entity);
        this.fKey = fKey;
        Assert.isTrue(fKey.getAnnotation(ForeignKey.class) != null, "Field %s must be a Foreign Key", fKey.getName());
    }

    private String getFormattedSql() {
        if (sqlValues == null) {
            sqlValues = new FieldList();
            sql = sql.replace("?", EntityDB.getFullColumnName(getEntity().getSingleId()));
            StringList names = EntityDB.getFullColumnNames(entity.getFields());
            for (int index = names.indexInsideOfIgnoreCase(sql); index != -1; index = names.indexInsideOfIgnoreCase(sql)) {
                sql = sql.substring(0, sql.toLowerCase().indexOf(names.get(index).toLowerCase())) + "?" + sql.substring(sql.toLowerCase().indexOf(names.get(index).toLowerCase())+names.get(index).length());
                sqlValues.add(entity.getFields().get(index));
            }
        }
        return sql;
    }


    public EntityDB getEntity() {
        return entity;
    }

    public Field getfKey() {
        return fKey;
    }

    public boolean hasPotentialValue() {
        return fKey != null && fKey.get() != null || sql != null;
    }

    /**
     * @return the declared annotation type of this class
     */
    public Class<E> getType() {
        return (Class<E>) ((ParameterizedType) entity.get(this)
                .getGenericType()).getActualTypeArguments()[0];
    }

    public Exportable getExportable() {
        java.lang.reflect.Field field = entity.get(this);
        return field.getAnnotation(Exportable.class);
    }

    public String getExportName() {
        return getExportNames().get(0);
    }

    public StringList getExportNames() {
        Exportable exportable = entity.get(this).getAnnotation(Exportable.class);
        StringList names = new StringList(exportable != null && !exportable.name().isEmpty() ? exportable.name() : entity.get(this).getName());
        if (exportable != null) {
            names.addAll(Arrays.asList(exportable.otherNames()));
        }
        return names;
    }

    public boolean isContainedIn(JSONObject object) {
        for (String name : getExportNames()) {
            if (object.has(name)) {
                return true;
            }
        }
        return false;
    }

    public Object getFrom(JSONObject object) {
        for (String name : getExportNames()) {
            if (object.has(name)) {
                return object.get(name);
            }
        }
        throw new RuntimeException("Could not get object from JsonObject from entityRef " + getExportNames());
    }

    public Field getForeignField() {
        try {

            if (fKey == null) {
                E foreignEntity = getType().newInstance();
                FieldList list = foreignEntity.getFields().getAnnotation(ForeignKey.class);
                for (Field field : list) {
                    if (((ForeignKey) field.getAnnotation(ForeignKey.class)).table().equals(entity.getClass())) {
                        return field;
                    }
                }
                // could not locate a matching reference
                throw new RuntimeException("Could not find a matching relational reference for Class " + foreignEntity.getClass().getName() + " to " + entity.getName());

            } else {
                return fKey;
            }

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * @return the database lookup for this relation
     */
    public DataSourceDB<E> get(Connection database) {
        return get(database, null);
    }

    /**
     * @param whereFilter Optional extra where filter
     * @return the database lookup for this relation
     */
    public DataSourceDB<E> get(Connection database, String whereFilter) {
        try {
            if (sql != null) {
                // replace all instances of own fields
                PreparedStatement ps = database.prepareStatement(getFormattedSql());
                for (int i = 1;i <= sqlValues.size();i++) {
                    ps.setObject(i, sqlValues.get(i-1).get());
                }
                return new DataSourceDB<E>(getType(), ps.executeQuery(), true);
            } else if (fKey == null) {
                Field field = getForeignField();
                E foreignEntity = (E) field.getEntity();
                String sql =
                    this.sql == null
                        ? String.format("select * from %s where (%s) and (%s) order by %s",
                            foreignEntity.getName(),
                            foreignEntity.getColumnName(field) + " = ?",
                            whereFilter != null ? whereFilter : "1=1",
                            foreignEntity.getColumnNames(foreignEntity.getId()).toString(","))
                        : this.sql;

                PreparedStatement ps = database.prepareStatement(sql);
                ps.setObject(1, entity.getSingleId().get());
                return new DataSourceDB<E>(getType(), ps.executeQuery(), true);
            } else {
                ForeignKey foreignKey = (ForeignKey) fKey.getAnnotation(ForeignKey.class);
                EntityDB parent = (EntityDB) foreignKey.table().getDeclaredConstructor().newInstance();
                if (fKey.get() != null) {
                    parent.getSingleId().set(fKey.get());
                    return new DataSourceDB(
                        foreignKey.table(),
                        DataSourceDB.getLoadFromIdRs(database, parent).executeQuery());
                } else {
                    // empty rs
                    return new DataSourceDB<>(foreignKey.table());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public E getOne(Connection database, String whereFilter) {
        try (DataSourceDB<E> ds = get(database, whereFilter)){
            return ds != null ? ds.get() : null;
        }
    }

    public E getOne(Connection database) {
        return getOne(database, null);
    }

    public E getOne(DataSource dataSource) {
        return getOne(dataSource, null);
    }

    public E getOne(DataSource dataSource, String whereFilter) {
        try {
            try (Connection connection = dataSource.getConnection()) {
                return getOne(connection, whereFilter);
            }
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
    }

    public List<E> getAllAsList(Connection database, String whereFilter) {
        try (DataSourceDB<E> ds = get(database, whereFilter);){
            return ds.getAllAsList();
        }
    }

    public List<E> getAllAsList(Connection database) {
        return getAllAsList(database, null);
    }

    public List<E> getAllAsList(DataSource dataSource, String whereFilter) {
        try (Connection connection = dataSource.getConnection()) {
            return getAllAsList(connection, whereFilter);
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle);
        }
    }

    public List<E> getAllAsList(DataSource dataSource) {
        return getAllAsList(dataSource, null);
    }

    public E getNew() {
        try {
            E fEntity = getType().getDeclaredConstructor().newInstance();
            if (fKey == null) {
                Assert.isTrue(entity.updateForeignKeyFieldValues(fEntity) || fEntity.updateForeignKeyFieldValues(entity),
                        "No Foreign keys mapped from %s to %s", entity.getName(), fEntity.getName());
            }
            return fEntity;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
