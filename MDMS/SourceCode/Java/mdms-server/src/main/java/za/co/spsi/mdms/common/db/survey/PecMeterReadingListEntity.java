package za.co.spsi.mdms.common.db.survey;

import za.co.spsi.mdms.common.MdmsConstants;
import za.co.spsi.mdms.kamstrup.services.meter.MeterConstants;
import za.co.spsi.mdms.kamstrup.services.utility.UtilityMeterReadingSyncService;
import za.co.spsi.toolkit.crud.db.fields.UserIdField;
import za.co.spsi.toolkit.crud.db.gis.ImageGeoEntity;
import za.co.spsi.toolkit.crud.service.NumberService;
import za.co.spsi.toolkit.crud.sync.SyncableEntity;
import za.co.spsi.toolkit.crud.sync.db.SharedEntity;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.EntityRef;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.db.fields.FieldError;
import za.co.spsi.toolkit.db.fields.FieldTimestamp;
import za.co.spsi.toolkit.db.fields.Index;
import za.co.spsi.toolkit.entity.ExportPathObject;
import za.co.spsi.toolkit.entity.Exportable;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.entity.ano.AlwaysExport;
import za.co.spsi.toolkit.entity.ano.Audit;
import za.co.spsi.toolkit.entity.ano.ExportableEntity;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Optional;

/**
 * Created by jaspervdbijl on 2017/03/14.
 */
@Table(version = 2)
@Audit
public class PecMeterReadingListEntity extends EntityDB implements SyncableEntity, ExportableEntity {

    @Override
    public ExportPathObject getExportObject() {
        ExportPathObject epo = new ExportPathObject(PecMeterReadingListEntity.class);
        ExportPathObject epoRegister = epo.addSub(PecMeterRegisterEntity.class).set(PecMeterEntity.class, PecMeterReadingEntity.class);
        ExportPathObject epoMeter = epoRegister.addSub(PecMeterEntity.class).set(ImageGeoEntity.class, PecMeterPhotoEntity.class, PecPropertyEntity.class, PecUnitEntity.class);
        ExportPathObject epoUnit = epoMeter.addSub(PecUnitEntity.class).set(PecUnitPhotoEntity.class, PecPropertyEntity.class);
        epoUnit.addSub(PecPropertyEntity.class).set(PecPropertyPhotoEntity.class);
        return epo;
    }

    public enum Status {
        Received(0), Started(MeterConstants.PEC_PROCESSING_STARTED), Completed(MeterConstants.PEC_PROCESSING_COMPLETED),
        RequestClose(3), Closed(4), Expired(5), Error(6);

        private int code;

        Status(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

    @Id(uuid = true)
    @Column(name = "METER_READING_LIST_ID", size = 50)
    public Field<String> meterReadingListId = new Field<>(this);

    @Column(name = "METER_READING_LIST_N", size = 20)
    public Field<String> meterReadingListN = new Field<>(this);

    public Field<String> name = new Field<>(this);

    @Column(name = "BUILDING_NAME")
    public Field<String> buildingName = new Field<>(this);

    @Column(name = "ROUTE_NAME")
    public Field<String> routeName = new Field<>(this);

    @Column(name = "READING_DATE")
    public Field<Timestamp> readingDate = new Field<>(this);

    @Column(name = "CYCLE_START_DATE")
    public Field<Timestamp> cycleStartDate = new Field<>(this);

    @Column(name = "CLOSURE_DATE")
    public Field<Timestamp> closureDate = new Field<>(this);

    @Column(name = "AGENCY_ID", size = 8)
    public Field<Integer> agencyId = new Field<>(this);

    @Column(name = "USER_ID", size = 50)
    public UserIdField userId = new UserIdField(this);

    public Field<Integer> status = new Field<>(this);

    public SharedEntity sharedEntity = new SharedEntity(this);

    public Index readingDateIdx = new Index("PEC_MRL_READ_D", this, readingDate);

    @AlwaysExport
    @Exportable(name = "pecMeterRegisterList")
    //Select All Conventional meters and smart meters that has been offline for 5 days to send to tablets.
    public EntityRef<PecMeterRegisterEntity> meterRegister = new EntityRef<>(
            "SELECT\n" +
                    "    *\n" +
                    "FROM\n" +
                    "    pec_meter_register\n" +
                    "WHERE\n" +
                    "    meter_reading_list_id = ?\n" +
                    "    AND   meter_id IN (\n" +
                    "        SELECT\n" +
                    "            meter_id\n" +
                    "        FROM\n" +
                    "            pec_meter\n" +
                    "        WHERE\n" +
                    "            pec_meter_register.meter_id = pec_meter.meter_id\n" +
                    "            AND   kam_meter_id IS NULL\n" +
                    "            AND   nes_meter_id IS NULL\n" +
                    "            AND   els_meter_id IS NULL\n" +
                    "            AND   generic_meter_id IS NULL\n" +
                    "    )\n" +
                    "    OR    meter_id IN (\n" +
                    "        SELECT\n" +
                    "            pec_meter.meter_id\n" +
                    "        FROM\n" +
                    "            pec_meter,\n" +
                    "            kamstrup_meter,\n" +
                    "            nes_meter,\n" +
                    "            elster_meter,\n" +
                    "            generic_meter\n" +
                    "        WHERE\n" +
                    "            pec_meter.meter_id = pec_meter_register.meter_id\n" +
                    "            AND   meter_reading_list_id = ?\n" +
                    "            AND   (\n" +
                    "                pec_meter.kam_meter_id = kamstrup_meter.meter_id\n" +
                    "                AND   kamstrup_meter.last_comms_d IS NOT NULL\n" +
                    "                AND   kamstrup_meter.last_comms_d < SYSDATE - 5\n" +

                    "                OR    pec_meter.nes_meter_id = nes_meter.meter_id\n" +
                    "                AND   nes_meter.last_comms_d IS NOT NULL\n" +
                    "                AND   nes_meter.last_comms_d < SYSDATE - 5\n" +

                    "                OR    pec_meter.els_meter_id = elster_meter.meter_id\n" +
                    "                AND   elster_meter.last_comms_d IS NOT NULL\n" +
                    "                AND   elster_meter.last_comms_d < SYSDATE - 5\n" +

                    "                OR    pec_meter.generic_meter_id = generic_meter.generic_meter_id\n" +
                    "                AND   generic_meter.last_comms_d IS NOT NULL\n" +
                    "                AND   generic_meter.last_comms_d < SYSDATE - 5\n" +
                    "            )\n" +
                    "    )", this);

    public EntityRef<PecMeterReadingEntity> readings = new EntityRef<>("select pec_meter_reading.* from pec_meter_reading,pec_meter_register where " +
            "pec_meter_reading.meter_register_id = pec_meter_register.meter_register_id and pec_meter_register.meter_reading_list_id = pec_meter_reading_list.meter_reading_list_id", this);

    public EntityRef<PecMeterReadingEntity> conventionalReadings = new EntityRef<>("select pec_meter_reading.* from pec_meter_reading,pec_meter_register,pec_meter where " +
            "pec_meter_reading.meter_register_id = pec_meter_register.meter_register_id and pec_meter_register.meter_reading_list_id = pec_meter_reading_list.meter_reading_list_id and " +
            "pec_meter.meter_id = pec_meter_register.meter_id and (pec_meter.kam_meter_id is null and pec_meter.nes_meter_id is null and pec_meter.els_meter_id is null )", this);

    public EntityRef<PecMeterReadingEntity> smartReadings = new EntityRef<>("select pec_meter_reading.* from pec_meter_reading,pec_meter_register,pec_meter where " +
            "pec_meter_reading.meter_register_id = pec_meter_register.meter_register_id and pec_meter_register.meter_reading_list_id = pec_meter_reading_list.meter_reading_list_id and " +
            "pec_meter.meter_id = pec_meter_register.meter_id and (pec_meter.kam_meter_id is not null or pec_meter.nes_meter_id is not null or pec_meter.els_meter_id is not null or pec_meter.generic_meter_id is not null)", this);

    public PecMeterReadingListEntity() {
        super("PEC_METER_READING_LIST");
    }

    public static PecMeterReadingListEntity createList(Connection connection, PecUtilityMeterReadingListEntity list, int splitCount) {
        PecMeterReadingListEntity pecList = new PecMeterReadingListEntity();
        pecList.agencyId.set(list.agencyId.get());
        pecList.userId.set(list.userId.get());
        pecList.readingDate.set(list.readingDate.get());
        pecList.cycleStartDate.set(list.cycleStartDate.get());
        pecList.closureDate.set(list.closureDate.get());
        pecList.status.set(Status.Received.getCode());
        pecList.name.set(list.name.get() + " #" + splitCount);
        pecList.sharedEntity.notes.set(list.notes.get());
        pecList.routeName.set(list.routeName.get());
        pecList.buildingName.set(list.buildingName.get());
        DataSourceDB.set(connection, pecList);
        pecList.log(connection, "LIST CREATED", String.format("READING DATE: %s", list.readingDate.get()), null);
        // create the link
        PecUtilityMeterReadingListEntity.ReadingListLinkEntity.link(connection, list, pecList);
        return pecList;
    }

    @Override
    public boolean beforeInsertEvent(Connection connection) {
        meterReadingListN.set(meterReadingListN.get() == null ? new NumberService().getHexString(10) : meterReadingListN.get());
        return super.beforeInsertEvent(connection);
    }

    @Override
    public void afterUpdateEvent(Connection connection) {
        if (getFields().getChanged().contains(status) &&
                MdmsConstants.ENTITY_STATUS_TABLET_PROCESSING.equals(sharedEntity.entityStatusCd.getOldValue()) &&
                MdmsConstants.ENTITY_STATUS_BACK_OFFICE_PROCESSING.equals(sharedEntity.entityStatusCd.get()) &&
                Status.Completed.getCode() == status.getNonNull()) {
            // check that there are no other linked
            UtilityMeterReadingSyncService.verifySplitMeterReadingDates(connection, this);
            if (DataSourceDB.loadFromId(connection, this).status.getNonNull() == Status.Completed.getCode()) {
                UtilityMeterReadingSyncService.getSmartMeterReadingDatesInSyncWithManualReadings(connection, this);
            }
        }
        super.afterUpdateEvent(connection);
    }

    @Override
    public SharedEntity getBaseSharedSyncEntity() {
        return sharedEntity;
    }

    public static DataSourceDB<PecMeterReadingListEntity> getCompleted(Connection connection) {
        return new DataSourceDB<>(PecMeterReadingListEntity.class).getAllWhere(connection,
                "status = ? and exists ( " +
                        "select * from meter_reading_list_link,pec_utility_meter_reading_list " +
                        "where " +
                        "meter_reading_list_link.utility_meter_reading_list_id = pec_utility_meter_reading_list.utility_meter_reading_list_id and " +
                        "meter_reading_list_link.meter_reading_list_id = pec_meter_reading_list.meter_reading_list_id and " +
                        "pec_utility_meter_reading_list.status in (?,?))", Status.Completed.getCode(), Status.Received.getCode(), Status.Started.getCode());
    }

    public Optional<Timestamp> getMaxConventionalReadingDate(Connection connection) {
        return conventionalReadings.getAllAsList(connection).stream().filter(r -> r.readingDate.get() != null && r.reading.get() != null).map(r -> r.readingDate.get())
                .max(Timestamp::compareTo);
    }

    public Log log(Connection connection, String event, String detail, Exception error) {
        return (Log) DataSourceDB.set(connection, new Log(this, event, detail, error));
    }

    public Log log(Connection connection, Status status, String event, String detail, Exception error) {
        this.status.set(status.getCode());
        DataSourceDB.set(connection, this);
        return log(connection, event, detail, error);
    }

    @Table(version = 5)
    public static class Log extends EntityDB {

        @Id(uuid = true)
        @Column(name = "METER_READING_LIST_LOG_ID", size = 50)
        public Field<String> logId = new Field<>(this);

        @Column(name = "METER_READING_LIST_ID", size = 50)
        @ForeignKey(table = PecMeterReadingListEntity.class, name = "MRLL_MRL_ID", onDeleteAction = ForeignKey.Action.Cascade)
        public Field<String> meterReadingListId = new Field<>(this);

        public Field<String> event = new Field<>(this);

        @Column(name = "DETAIL", size = 8000)
        public Field<String> detail = new Field<>(this);

        @Column(name = "ERROR", size = 8000, autoCrop = true)
        public FieldError error = new FieldError(this);

        @Column(name = "LOG_TIME")
        public FieldTimestamp logTime = new FieldTimestamp(this);

        public Log() {
            super("PEC_METER_READING_LIST_LOG");
        }

        public Log(PecMeterReadingListEntity list, String event, String detail, Exception error) {
            this();
            this.meterReadingListId.set(list.meterReadingListId.get());
            this.event.set(event);
            this.detail.set(detail);
            if (error != null) {
                this.error.set(error);
            }
        }
    }


}
