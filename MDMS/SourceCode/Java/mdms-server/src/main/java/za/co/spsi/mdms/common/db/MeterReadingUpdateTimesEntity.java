package za.co.spsi.mdms.common.db;

import lombok.SneakyThrows;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.db.fields.FieldTimestamp;
import za.co.spsi.toolkit.entity.Field;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@Table(version = 1, deleteOldRecords = true, deleteRecordTimeField = "CREATE_TIME")
public class MeterReadingUpdateTimesEntity extends EntityDB {

    @Id(uuid = true)
    @Column(name = "METER_READING_UPDATE_ID", size = 50, notNull = true)
    public Field<String> meterReadingUpdateId = new Field<>(this);

    @Column(name = "METER_ID", size = 50)
    public Field<String> meterId = new Field<>(this);

    @Column(name = "TOTAL_ENERGY_ENTRY_TIME")
    public FieldTimestamp totalEnergyEntryTime = new FieldTimestamp(this);

    @Column(name = "T1_ENERGY_ENTRY_TIME")
    public FieldTimestamp t1EnergyEntryTime = new FieldTimestamp(this);

    @Column(name = "T2_ENERGY_ENTRY_TIME")
    public FieldTimestamp t2EnergyEntryTime = new FieldTimestamp(this);

    @Column(name = "VOLTAGE_ENTRY_TIME")
    public FieldTimestamp voltageEntryTime = new FieldTimestamp(this);

    @Column(name = "CURRENT_ENTRY_TIME")
    public FieldTimestamp currentEntryTime = new FieldTimestamp(this);

    @Column(name = "VOLUME_ENTRY_TIME")
    public FieldTimestamp volumeEntryTime = new FieldTimestamp(this);

    @Column(name = "CREATE_TIME")
    public FieldTimestamp createTime = new FieldTimestamp(this);

    public MeterReadingUpdateTimesEntity() {
        super("METER_READING_UPDATE_TIMES");
    }

    @SneakyThrows
    public void updateTotalEnergyEntryTime(Connection connection, String meterId, Timestamp entryTime) {

        Statement sqlStatement = connection.createStatement();

        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//        String insertQuery = String.format("SELECT * FROM METER_READING WHERE KAM_METER_ID = '%s' AND ENTRY_TIME = TO_DATE('%s','YYYY-MM-DD HH24:MI:SS')",
//                kamMeterId, dateTimeFormat.format( data.getEntryTime() ) );

    }

}
