package za.co.spsi.mdms.common.db.survey;

import lombok.extern.slf4j.Slf4j;
import za.co.spsi.mdms.common.db.utility.IceApprovedMeterReadingsView;
import za.co.spsi.toolkit.crud.db.fields.UserIdField;
import za.co.spsi.toolkit.db.DSDB;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.EntityRef;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.entity.Field;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static za.co.spsi.mdms.common.db.survey.PecMeterReadingListEntity.Status;
import static za.co.spsi.mdms.common.db.survey.PecMeterReadingListEntity.createList;
import static za.co.spsi.toolkit.db.ano.ForeignKey.Action.Cascade;

/**
 * Created by jaspervdbijl on 2017/03/14.
 * Meter reading list that is received from utility
 * another list will be generated from this with the manual meter readings
 */
@Table(version = 3)
@Slf4j
public class PecUtilityMeterReadingListEntity extends EntityDB {

    @Id(uuid = true, name = "PUMRL_ID")
    @Column(name = "UTILITY_METER_READING_LIST_ID", size = 50)
    public Field<String> utilityMeterReadingListId = new Field<>(this);

    @Column(name = "METER_READING_LIST_ID", size = 50)
    @ForeignKey(table = PecMeterReadingListEntity.class, name = "UMRL_MRL_ID", onDeleteAction = Cascade)
    public Field<String> meterReadingListId = new Field<>(this);

    public Field<String> name = new Field<>(this);

    @Column(name = "NOTES", size = 4096)
    public Field<String> notes = new Field<>(this);

    @Column(name = "BUILDING_NAME")
    public Field<String> buildingName = new Field<>(this);

    @Column(name = "PROPERTY_NAME")
    public Field<String> propertyName = new Field<>(this);

    @Column(name = "ROUTE_NAME")
    public Field<String> routeName = new Field<>(this);

    @Column(name = "AGENCY_ID", size = 8)
    public Field<Integer> agencyId = new Field<>(this);

    @Column(name = "USER_ID", size = 50)
    public UserIdField userId = new UserIdField(this);

    public Field<Integer> status = new Field<>(this);

    @Column(name = "READING_DATE")
    public Field<Timestamp> readingDate = new Field<>(this);

    @Column(name = "CYCLE_START_DATE")
    public Field<Timestamp> cycleStartDate = new Field<>(this);

    @Column(name = "CLOSURE_DATE")
    public Field<Timestamp> closureDate = new Field<>(this);

    // ice utility reference id
    public Field<String> reference_id = new Field<>(this);

    public EntityRef<PecMeterRegisterEntity> meterRegisterList = new EntityRef<>("select * from pec_meter_register where utility_meter_reading_list_id = ? " +
            "order by meter_id asc ", this);

    public EntityRef<PecMeterRegisterEntity> smartMeterRegisterList = new EntityRef<>("select * from pec_meter mtr " +
            "join pec_meter_register pmr on mtr.meter_id = pmr.meter_id " +
            "where pmr.utility_meter_reading_list_id = ? and mtr.meter_configuration_cd = 'AMI' " +
            "order by mtr.meter_id asc ", this);

    public EntityRef<PecMeterReadingListEntity> meterReadingList = new EntityRef<>(
            "select pec_meter_reading_list.* from pec_meter_reading_list,meter_reading_list_link where " +
                    "pec_meter_reading_list.meter_reading_list_id = meter_reading_list_link.meter_reading_list_id and " +
                    "meter_reading_list_link.utility_meter_reading_list_id = pec_utility_meter_reading_list.utility_meter_reading_list_id", this);

    public EntityRef<PecMeterReadingEntity> readingList = new EntityRef<>(this);

    public EntityRef<PecMeterEntity> meters = new EntityRef<>(
            "select pec_meter.* from pec_meter,pec_meter_register where pec_meter.meter_id = pec_meter_register.meter_id and " +
                    "pec_meter_register.utility_meter_reading_list_id = ?", this);

    public PecUtilityMeterReadingListEntity() {
        super("PEC_UTILITY_METER_READING_LIST");
    }

    public static DataSourceDB<PecUtilityMeterReadingListEntity> getExpired(Connection connection) {
        return new DataSourceDB<>(PecUtilityMeterReadingListEntity.class).getAll(connection,
                "select * from pec_utility_meter_reading_list where status in (?,?) and closure_date < current_timestamp",
                Status.Received.getCode(), Status.Started.getCode());
    }

    public static DataSourceDB<PecUtilityMeterReadingListEntity> getByStatus(Connection connection, Status status) {
        return new DataSourceDB<PecUtilityMeterReadingListEntity>(PecUtilityMeterReadingListEntity.class).getAll(connection,
                "select * from pec_utility_meter_reading_list where status = ?", status.getCode());
    }

    public DataSourceDB<PecMeterEntity> getSmartMeters(Connection connection) {
        return new DataSourceDB<PecMeterEntity>(PecMeterEntity.class).getAll(connection,
                "select pec_meter.* from pec_meter,pec_meter_register where " +
                        "pec_meter_register.utility_meter_reading_list_id = ? and " +
                        "pec_meter.meter_id = pec_meter_register.meter_id and " +
                        "(kam_meter_id is not null or nes_meter_id is not null or els_meter_id is not null or generic_meter_id is not null)", utilityMeterReadingListId.get());
    }

    public PecUtilityMeterReadingListEntity initList(IceApprovedMeterReadingsView view, int timeoutDays) {
        this.reference_id.set(view.approved.ice_meterreadinglist_id.get());
        this.status.set(PecMeterReadingListEntity.Status.Received.getCode());
        this.readingDate.set(view.list.meterReadingDate.get());
        this.cycleStartDate.set(view.list.cycleStartDate.get());
        this.name.set(view.property.iceBuildingComplexName.get() + " - " + view.list.name.get());
        this.buildingName.set(view.property.iceBuildingComplexName.get());
        this.propertyName.set(view.property.propertyName.get());
        this.routeName.set(view.list.name.get());
        this.notes.set(view.list.notes.get());
        LocalDate localDate = this.readingDate.get().toLocalDateTime().toLocalDate().plusDays(timeoutDays);
        this.closureDate.set(new Timestamp(Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()).getTime()));
        return this;
    }

    public PecMeterReadingListEntity update(Connection connection, IceApprovedMeterReadingsView view,
                                            PecMeterReadingListEntity lastList, AtomicInteger splitCount,List<String> meterFilter) {
        if (lastList == null || "Y".equalsIgnoreCase(view.approved.ice_splitlist_indicator.get())) {
            lastList = createList(connection, this, splitCount.incrementAndGet());
        }
        // update property
        PecPropertyEntity property = PecPropertyEntity.getOrCreate(connection, view);
        PecMeterEntity meter = PecMeterEntity.getOrCreate(connection, property, view);
        String registerId = view.register.meterRegister.get();
        String key = meter.reference_id.get() + "_" + registerId;
        if (meter.isSmartMeter() || !meterFilter.contains(key)) {
            PecMeterRegisterEntity register = PecMeterRegisterEntity.create(connection, meter, this, lastList, view);
            PecMeterReadingEntity reading = PecMeterReadingEntity.create(connection, register, view);
        } else {
            log.warn("Filtered duplicate meter {} register {}",meter.meterId.get(), registerId);
        }
        meterFilter.add(key);
        return lastList;
    }

    public PecUtilityMeterReadingListEntity linkToSmartMeters(Connection connection) {
        meters.getAllAsList(connection).stream().forEach(m -> {
            if (m.isSmartMeter()) { // !m.isSmartMeter()
                m.linkToSmartMeters(connection);
                DataSourceDB.set(connection, m);
            }
        });
        return this;
    }

    public void updateStatus(Connection connection, Status utilStatus, Status status, String event, String detail, Exception error) {
        this.status.set(utilStatus.getCode());
        DataSourceDB.set(connection, this);
        for (PecMeterReadingListEntity list : meterReadingList.get(connection)) {
            list.log(connection, status, event, detail, error);
        }
    }

    public boolean onlySmartMeters(javax.sql.DataSource dataSource) {
        return smartMeterRegisterList.getOne(dataSource) != null;
    }

    public boolean onlyConventionalMeters(javax.sql.DataSource dataSource) {
        return smartMeterRegisterList.getOne(dataSource) == null;
    }

    public List<PecMeterReadingListEntity> getMeterReadingLists(Connection connection) {
        return ReadingListLinkEntity.getMeterReadingListsFromUtility(connection,utilityMeterReadingListId.get());
    }

    // if there are conventional meters are a completed state, then choose that date, else use own date
    public Timestamp getMaxReadingDate(Connection connection) {
        Optional<PecMeterReadingListEntity> max =
                ReadingListLinkEntity.getMeterReadingListsFromUtility(connection, utilityMeterReadingListId.get())
                        .stream()
                        .filter(f -> f.getMaxConventionalReadingDate(connection).isPresent())
                        .sorted((b,a) ->
                                a.getMaxConventionalReadingDate(connection).get()
                                        .compareTo(b.getMaxConventionalReadingDate(connection).get())
                        ).findFirst();
        return max.isPresent()?max.get().getMaxConventionalReadingDate(connection).get():readingDate.get();
    }

    public Timestamp getMaxReadingDate(DataSource dataSource) {
        return DSDB.executeResultInTx(dataSource,new DataSourceDB.Callback<Timestamp>() {
            @Override
            public Timestamp run(Connection connection) throws Exception {
                return getMaxReadingDate(connection);
            }
        });
    }

    @Table(version = 0)
    public static class ReadingListLinkEntity extends EntityDB {

        @Id(uuid = true, name = "PEC_MRL_LINK_ID")
        @Column(size = 50)
        public Field<String> id = new Field<>(this);

        @Column(name = "UTILITY_METER_READING_LIST_ID", size = 50)
        @ForeignKey(table = PecUtilityMeterReadingListEntity.class, name = "MRLL_UMRL_ID", onDeleteAction = ForeignKey.Action.Cascade)
        public Field<String> utilMeterReadingListId = new Field<>(this);

        @Column(name = "METER_READING_LIST_ID", size = 50)
        @ForeignKey(table = PecMeterReadingListEntity.class, name = "RLL_MRL_ID", onDeleteAction = ForeignKey.Action.Cascade)
        public Field<String> meterReadingListId = new Field<>(this);

        public EntityRef<PecMeterReadingListEntity> meterReadingList = new EntityRef<>(meterReadingListId, this);

        public ReadingListLinkEntity() {
            super("METER_READING_LIST_LINK");
        }

        public static ReadingListLinkEntity link(Connection connection, PecUtilityMeterReadingListEntity utilityMeterReadingListEntity, PecMeterReadingListEntity meterReadingListEntity) {
            ReadingListLinkEntity link = new ReadingListLinkEntity();
            link.utilMeterReadingListId.set(utilityMeterReadingListEntity.utilityMeterReadingListId.get());
            link.meterReadingListId.set(meterReadingListEntity.meterReadingListId.get());
            return (ReadingListLinkEntity) DataSourceDB.set(connection, link);
        }

        public static List<PecMeterReadingListEntity> getMeterReadingLists(Connection connection, String meterReadingListId) {
            PecUtilityMeterReadingListEntity.ReadingListLinkEntity link = (PecUtilityMeterReadingListEntity.ReadingListLinkEntity) DataSourceDB.getFromSet(connection,
                    (EntityDB) new PecUtilityMeterReadingListEntity.ReadingListLinkEntity().meterReadingListId.set(meterReadingListId));
            if (link != null) {
                return getMeterReadingListsFromUtility(connection,link.utilMeterReadingListId.get());
            } else {
                return new ArrayList<>();
            }
        }

        public static List<PecMeterReadingListEntity> getMeterReadingListsFromUtility(Connection connection, String utilMeterReadingListId) {
            String sql = "select * from pec_meter_reading_list where meter_reading_list_id in (" +
                    "select meter_reading_list_id from meter_reading_list_link where meter_reading_list_link.utility_meter_reading_list_id = ?)";
            return new DataSourceDB<>(PecMeterReadingListEntity.class).getAllAsList(connection, sql, utilMeterReadingListId);
        }

        public static PecUtilityMeterReadingListEntity getUtilityMeterReadingLists(Connection connection, String meterReadingListId) {
            try {
                String sql = "select * from pec_utility_meter_reading_list where utility_meter_reading_list_id in (" +
                        "select utility_meter_reading_list_id from meter_reading_list_link where meter_reading_list_link.meter_reading_list_id = ?)";
                return DataSourceDB.get(PecUtilityMeterReadingListEntity.class, connection, sql, meterReadingListId);
            } catch (SQLException sqle) {
                throw new RuntimeException(sqle);
            }
        }

        public static PecUtilityMeterReadingListEntity getUtilityMeterReadingLists(DataSource dataSource, String meterReadingListId) {
            return DataSourceDB.executeResultInTx(dataSource, new DataSourceDB.Callback<PecUtilityMeterReadingListEntity>() {
                @Override
                public PecUtilityMeterReadingListEntity run(Connection connection) throws Exception {
                    return getUtilityMeterReadingLists(connection, meterReadingListId);
                }
            });
        }
    }

}
