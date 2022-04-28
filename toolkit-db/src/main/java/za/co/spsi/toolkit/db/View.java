/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
*/
package za.co.spsi.toolkit.db;

import za.co.spsi.toolkit.db.drivers.DriverFactory;
import za.co.spsi.toolkit.entity.Entity;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.entity.FieldList;
import za.co.spsi.toolkit.util.StringList;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jaspervdb
 */
public class View<E extends View> extends EntityDB {

    private String sql;
    private Object data[] = new Object[]{};
    private FieldList myFields = null;
    private Integer limit;

    public View() {
        this(null);
    }

    public View(String sql) {
        super("");
        this.sql = sql;
    }

    public View(String sql, Object data[]) {
        this(sql);
        this.data = data;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public void setSql(String sql, Object... data) {
        this.sql = sql;
        this.data = data;
    }

    public void setData(Object... data) {
        this.data = data;
    }

    public String getSql() {
        return sql;
    }

    public Object[] getData() {
        return data;
    }

    public View setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }

    public void setFields(FieldList vField) {
        this.myFields = vField;
    }

    public View aliasNames() {
        ArrayList<EntityDB> entites = getEntities();
        for (Field field : getFields()) {
            Entity entity = field.getEntity();
            if (getEntityIndexOnName(entites, entity) == -1) {
                entity = entity.getParentEntity();
            }
            String altName = "A" + getEntityIndexOnName(entites, entity) + "_" + field.getName();
            field.setAlternateName(altName.length() > 30 ?
                    "A" + getEntityIndexOnName(entites, entity) + "_" + getFields().indexOf(field) : altName
            );
        }
        return this;
    }

    private int getEntityIndexOnName(ArrayList<EntityDB> entites, Entity entity) {
        for (int i = 0; i < entites.size(); i++) {
            if (entites.get(i).getName().equals(entity.getName())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public FieldList getFields() {
        if (myFields == null) {
            myFields = new FieldList();
            for (EntityDB entityDB : getEntities()) {
                myFields.addAll(entityDB.getFields());
            }
        }
        return myFields;
    }

    public ArrayList<EntityDB> getEntities() {
        try {
            ArrayList<EntityDB> vEntity = new ArrayList<EntityDB>();
            for (java.lang.reflect.Field f : this.getClass().getFields()) {
                if (f.get(this) instanceof EntityDB) {
                    vEntity.add((EntityDB) f.get(this));
                }
            }
            return vEntity;
        } catch (IllegalAccessException iae) {
            throw new RuntimeException(iae);
        }
    }

    /* SQL Implementation */
    public DataSourceDB<E> getDataSource(Connection dbcon) {
        FormattedSql formattedSql = new FormattedSql(sql);

        if (formattedSql.getSelect().indexOf("*") != -1) {
            StringList names = new StringList();
            for (Field field : getFields()) {
                names.add(getFullColumnName(field, false) + " " + (field.getAlternateName() != null ? field.getAlternateName() : ""));
            }
            formattedSql.setSelect(names.toString(","));
            sql = formattedSql.toString();
        }
        return new DataSourceDB(getClass()).getAll(dbcon,
                limit != null ? DriverFactory.getDriver().limitSql(sql, limit) : sql, data);
    }

    public List<E> getAsList(DataSource dataSource) {
        return DataSourceDB.executeResultInTx(dataSource, new DataSourceDB.Callback<List<E>>() {
            @Override
            public <T> T run(Connection connection) throws Exception {
                return (T) getDataSource(connection).getAllAsList();
            }
        });
    }


    @Override
    public void setInDatabase(boolean inDatabase) {
        super.setInDatabase(inDatabase);
        for (EntityDB entityDB : getEntities()) {
            entityDB.setInDatabase(inDatabase);
        }
    }

    public static boolean decimalPlacesOk(String value, String seperator, int decimalPlaces) {
        return (value.indexOf(seperator) == -1 || (value.indexOf(seperator) >= (value.length() - decimalPlaces - 1)));
    }

    public static void main(String args[]) throws Exception {
        System.out.println(decimalPlacesOk("1312.123", ".", 2));
    }


}
