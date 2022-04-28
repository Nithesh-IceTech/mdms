package za.co.spsi.mdms.common.db;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.db.fields.FieldTimestamp;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.entity.FieldLocalDate;

import java.sql.Timestamp;

@Table(version = 5)
public class MdmsSettingsEntity extends EntityDB {

    @Id(uuid = true)
    @Column(name = "PROPERTY_ID", size = 50, notNull = true)
    public Field<String> propertyId = new Field<>(this);

    @Column(name = "APP_INSTANCE", defaultValue = "1", notNull = true)
    public Field<Integer> appInstance = new Field<>(this);

    @Column(name = "PROPERTY_KEY")
    public Field<String> propertyKey = new Field<>(this);

    @Column(name = "PROPERTY_VALUE", size = 4000, autoCrop = true)
    public Field<String> propertyValue = new Field<>(this);

    @Column(name = "PROPERTY_TYPE", defaultValue = "STRING")
    public Field<String> propertyType = new Field<>(this);

    @Column(name = "LAST_CHANGE_TIME")
    public FieldLocalDate<Timestamp> lastChangeTime = new FieldLocalDate<Timestamp>(this);

    @Column(name = "CREATE_TIME")
    public FieldTimestamp createTime = new FieldTimestamp(this);

    public MdmsSettingsEntity() {
        super("MDMS_SETTINGS");
    }

    @Override
    public String toString() {
        return "MdmsSettingsEntity{" +
                "propertyKey=" + propertyKey +
                ", propertyValue=" + propertyValue +
                ", propertyType=" + propertyType +
                ", lastChangeTime=" + lastChangeTime +
                '}';
    }
}
