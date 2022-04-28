package za.co.spsi.toolkit.db;

import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.entity.Entity;
import za.co.spsi.toolkit.entity.Field;

import java.sql.Connection;

@Table(version = 0)
public class DBSettings extends EntityDB {

    @Id
    public Field<String> name = new Field(this);
    // json clob value
    @Column(size = 4000)
    public Field<String> value = new Field(this);

    public DBSettings() {
        super("db_settings");
    }

    public static DBSettings getRecord(Connection connection, Class<? extends Entity> eType) {
        return  DataSourceDB.getFromSet(connection,new DBSettings().name.set(eType.getName()));
    }

    public static <T extends Entity> T set(Connection connection, T entity) {
        DBSettings record = getRecord(connection,entity.getClass());
        record = record == null? (DBSettings) new DBSettings().name.set(entity.getClass().getName()):record;
        record.value.set(entity.getAsJson().toString());
        DataSourceDB.set(connection,record);
        return entity;
    }

    public static <T extends Entity> T get(Connection connection, Class<? extends Entity> eType) {
        try {
            DBSettings record = getRecord(connection, eType);
            T entity = (T) eType.newInstance();
            if (record != null) {
                entity.initFromJson(record.value.get());
            }
            return entity;
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

}
