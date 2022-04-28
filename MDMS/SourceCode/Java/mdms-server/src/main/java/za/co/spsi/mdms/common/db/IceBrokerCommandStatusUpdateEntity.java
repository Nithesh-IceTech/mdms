package za.co.spsi.mdms.common.db;

import lombok.SneakyThrows;
import org.springframework.util.CollectionUtils;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.db.drivers.Driver;
import za.co.spsi.toolkit.db.drivers.DriverFactory;
import za.co.spsi.toolkit.db.fields.FieldError;
import za.co.spsi.toolkit.db.fields.FieldTimestamp;
import za.co.spsi.toolkit.entity.Field;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

@Table(version = 2, deleteOldRecords = true)
public class IceBrokerCommandStatusUpdateEntity extends EntityDB {

    @Id(uuid = true)
    @Column(name = "BROKERCOMMAND_STATUSUPDATE_ID", size = 50, notNull = true)
    public Field<String> iceBrokerCommandStatusUpdateId = new Field<>(this);

    @Column(name = "BROKER_COMMAND_ID")
    public Field<String> brokerCommandId = new Field<>(this);

    @Column(name = "ICE_METER_ID")
    public Field<String> iceMeterId = new Field<>(this);

    @Column(name = "ICE_METER_STATUS_DATE")
    public Field<Timestamp> iceMeterStatusDate = new Field(this);

    @Column(name = "ICE_METER_STATUS")
    public Field<String> iceMeterStatus = new Field<>(this);

    @Column(name = "ICE_MESSAGE")
    public Field<String> iceMessage = new Field<>(this);

    @Column(name = "CREATED_DATE")
    public Field<Timestamp> createdDate = new Field(this);

    @Column(name = "ICE_STATUS_UPDATE_DATE")
    public FieldTimestamp iceStatusUpdateDate = new FieldTimestamp(this);

    @Column(name = "ERROR")
    public FieldError error = new FieldError(this);

    @Column(name = "ICE_UPDATED")
    public Field<Integer> iceUpdated = new Field<>(this);

    public IceBrokerCommandStatusUpdateEntity() {
        super("ICE_BROKERCOMMAND_STATUSUPDATE");
    }

    public enum ICEUpdated {
        YES, NO, ERROR
    }

    @SneakyThrows
    public static IceBrokerCommandStatusUpdateEntity create(String brokerCommandId, String iceMeterId,
                                                            Timestamp iceMeterStatusDate, String iceMeterStatus, String iceMessage,
                                                            Timestamp createdDate, Timestamp iceStatusUpdateDate,
                                                            Integer iceUtilUpdated) {
        IceBrokerCommandStatusUpdateEntity entity = new IceBrokerCommandStatusUpdateEntity();
        entity.brokerCommandId.set(brokerCommandId);
        entity.iceMeterId.set(iceMeterId);
        entity.iceMeterStatusDate.set(iceMeterStatusDate);
        entity.iceMeterStatus.set(iceMeterStatus);
        entity.iceMessage.set(iceMessage);
        entity.createdDate.set(createdDate);
        entity.iceStatusUpdateDate.set(iceStatusUpdateDate);
        entity.iceUpdated.set(iceUtilUpdated);

        return entity;
    }

    @SneakyThrows
    public IceBrokerCommandStatusUpdateEntity save(Connection connection) {
        return DataSourceDB.set(connection, this);
    }

    public static List<IceBrokerCommandStatusUpdateEntity> getByCommandIdAndIceUpdated(Connection connection, String brokerCommandId, Integer iceUtilUpdated) {
        return new DataSourceDB<>(IceBrokerCommandStatusUpdateEntity.class).getAllWhere(connection,
                "BROKER_COMMAND_ID = ? AND ICE_UPDATED = ?", brokerCommandId, iceUtilUpdated).getAllAsList();
    }

    public static List<IceBrokerCommandStatusUpdateEntity> getByIceUpdateStatus(javax.sql.DataSource dataSource, Integer iceUtilUpdated) {
        return new DataSourceDB<>(IceBrokerCommandStatusUpdateEntity.class).getAllAsList(dataSource,
                "select * from ICE_BROKERCOMMAND_STATUSUPDATE where ICE_UPDATED = ? ORDER BY ICE_STATUS_UPDATE_DATE DESC",iceUtilUpdated);
    }

    public Boolean isSameStatus(DataSource dataSource) {
        Boolean isSameStatus = false;
        Driver driver = DriverFactory.getDriver();
        if(DataSourceDB.getAllAsList(IceBrokerCommandStatusUpdateEntity.class, dataSource,
                driver.limitSql("select * from ice_brokercommand_statusupdate where " +
                        "broker_command_id = ? and " +
                        "ice_meter_status = ? and " +
                        "created_date = ?",1),
                this.brokerCommandId.get(),
                this.iceMeterStatus.get(),
                this.createdDate.get()).size() > 0) {
            isSameStatus = true;
        }
        return isSameStatus;
    }

    public Field<String> getIceBrokerCommandStatusUpdateId() {
        return iceBrokerCommandStatusUpdateId;
    }

    public Field<String> getBrokerCommandId() {
        return brokerCommandId;
    }

    public Field<String> getIceMeterId() {
        return iceMeterId;
    }

    public Field<Timestamp> getIceMeterStatusDate() {
        return iceMeterStatusDate;
    }

    public String getIce_meter_status_date_formatted() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatted_date = dateFormat.format( iceMeterStatusDate.get().getTime() );
        return formatted_date;
    }

    public Field<String> getIceMeterStatus() {
        return iceMeterStatus;
    }

    public Field<String> getIceMessage() {
        return iceMessage;
    }

    public Field<Timestamp> getCreatedDate() {
        return createdDate;
    }

    public FieldTimestamp getIceStatusUpdateDate() {
        return iceStatusUpdateDate;
    }

    public Field<Integer> getIceUpdated() {
        return iceUpdated;
    }

    public void setIceBrokerCommandStatusUpdateId(Field<String> iceBrokerCommandStatusUpdateId) {
        this.iceBrokerCommandStatusUpdateId = iceBrokerCommandStatusUpdateId;
    }

    public void setBrokerCommandId(Field<String> brokerCommandId) {
        this.brokerCommandId = brokerCommandId;
    }

    public void setIceMeterId(Field<String> iceMeterId) {
        this.iceMeterId = iceMeterId;
    }

    public void setIceMeterStatusDate(FieldTimestamp iceMeterStatusDate) {
        this.iceMeterStatusDate = iceMeterStatusDate;
    }

    public void setIceMeterStatus(Field<String> iceMeterStatus) {
        this.iceMeterStatus = iceMeterStatus;
    }

    public void setIceMessage(Field<String> iceMessage) {
        this.iceMessage = iceMessage;
    }

    public void setCreatedDate(FieldTimestamp createdDate) {
        this.createdDate = createdDate;
    }

    public void setIceStatusUpdateDate(FieldTimestamp iceStatusUpdateDate) {
        this.iceStatusUpdateDate = iceStatusUpdateDate;
    }

    public void setIceUpdated(Field<Integer> iceUpdated) {
        this.iceUpdated = iceUpdated;
    }
}