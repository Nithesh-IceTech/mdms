package za.co.spsi.mdms.kamstrup.services.utility;

import lombok.Synchronized;
import lombok.extern.java.Log;
//import org.apache.commons.collections.ListUtils;
import org.apache.commons.collections.ListUtils;
import org.idempiere.webservice.client.base.WebServiceResponse;
import za.co.spsi.mdms.common.db.survey.*;
import za.co.spsi.mdms.common.db.utility.IceApprovedMeterReadingsView;
import za.co.spsi.mdms.common.db.utility.IceMeterReadingList;
import za.co.spsi.mdms.common.properties.config.PropertiesConfig;
import za.co.spsi.mdms.utility.MDMSUtilityHelper;
import za.co.spsi.toolkit.crud.sync.db.DeviceEntitySyncMapEntity;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.ee.properties.ConfValue;
import za.co.spsi.toolkit.ee.properties.TextFile;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static za.co.spsi.mdms.common.db.survey.PecMeterReadingListEntity.Status;
import static za.co.spsi.mdms.common.db.survey.PecUtilityMeterReadingListEntity.ReadingListLinkEntity.getMeterReadingLists;

/**
 * Created by jaspervdbijl on 2017/03/30.
 */
@Singleton
@DependsOn({"PropertiesConfig"})
@Startup
@TransactionManagement(value = TransactionManagementType.BEAN)
@Log
public class UtilityMeterReadingSyncService {

    public static final Logger TAG = Logger.getLogger(UtilityMeterReadingSyncService.class.getName());

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");

    @Resource(mappedName = "java:/jdbc/mdms")
    private javax.sql.DataSource dataSource;

    @Resource(mappedName = "java:/jdbc/IceUtil")
    private javax.sql.DataSource iceDataSource;

    @Inject
    private PropertiesConfig propertiesConfig;

    @Inject
    protected MDMSUtilityHelper helper;

    @Inject
    @ConfValue(value = "utility.meter_reading_sync.auto_delete", folder = "server")
    private boolean autoDelete;

    @Inject
    @ConfValue(value = "utility.meter_reading_sync.timeout_days", folder = "server")
    private int timeoutDays;

    @Inject
    @ConfValue(value = "utility.meter_reading_sync.tmz_offset", folder = "server")
    private int tmzOffset;

    @Inject
    @ConfValue(value = "utility.meter_reading_sync.auto_push", folder = "server")
    private boolean autoPush;

    @Inject
    private MeterRegisterUpdateService service;

    @Inject
    @TextFile("sql/smart_meter_reading.sql")
    private String smartMeterReadingQuery;

    public void getReadingLists() {
        List<String> registerFilter = new ArrayList<>();
        List<PecUtilityMeterReadingListEntity> lists = new ArrayList<>();
        DataSourceDB.executeInTx(iceDataSource, ice -> {
            DataSourceDB.executeInTx(dataSource, connection -> {
                PecMeterReadingListEntity lastList = null;
                AtomicInteger splitCount = new AtomicInteger(0);

                for (IceApprovedMeterReadingsView view : new IceApprovedMeterReadingsView().getDataSource(ice)) {
                    // check if the view has been processed
                    PecUtilityMeterReadingListEntity list = DataSourceDB.getFromSet(connection,
                            (PecUtilityMeterReadingListEntity) new PecUtilityMeterReadingListEntity().reference_id.set(view.approved.ice_meterreadinglist_id.get()));

                    if (list == null) {
                        lastList = null;
                        splitCount.set(0);
                        list = new PecUtilityMeterReadingListEntity();
                        registerFilter.clear();
                        DataSourceDB.set(connection, list.initList(view, timeoutDays));
                        lists.add(list);
                    }

                    if (!lists.isEmpty() && lists.get(lists.size() - 1).utilityMeterReadingListId.get().equals(list.utilityMeterReadingListId.get())) {
                        lastList = lists.get(lists.size() - 1).update(connection, view, lastList, splitCount,registerFilter);
                    }
                }
                lists.stream().forEach(l -> l.linkToSmartMeters(connection));
            });
        });
    }

    private static void recall(Connection connection, PecUtilityMeterReadingListEntity list) {
        try {
            for (PecMeterReadingListEntity l : list.meterReadingList.get(connection)) {
                DeviceEntitySyncMapEntity.cancelUndelivered(connection, PecMeterReadingListEntity.class, l.meterReadingListId.get());
                DeviceEntitySyncMapEntity.recall(connection, PecMeterReadingListEntity.class, l.meterReadingListId.get());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * check if there is a list outstanding
     *
     * @return
     */
    public static List<String> verifySplitLists(Connection connection, PecMeterReadingListEntity list) {
        final List<String> messages = new ArrayList<>();
        Optional<Timestamp> maxDate = list.getMaxConventionalReadingDate(connection);
        if (maxDate.isPresent()) {
            for (PecMeterReadingListEntity l : PecUtilityMeterReadingListEntity.ReadingListLinkEntity.getMeterReadingLists(connection, list.meterReadingListId.get())) {
                Optional<Timestamp> refDate = l.getMaxConventionalReadingDate(connection);
                if (!list.meterReadingListId.get().equalsIgnoreCase(l.meterReadingListId.get())) {
                    if (l.status.getNonNull() == Status.Started.getCode() || l.status.getNonNull() == Status.Received.getCode()) {
                        messages.add(String.format("Sub Meter list %s is still in a processing state", l.name.get()));
                    }
                    if (refDate.isPresent() && refDate.get().toLocalDateTime().truncatedTo(ChronoUnit.DAYS).
                            compareTo(maxDate.get().toLocalDateTime()) != 0) {
                        messages.add(String.format("Reading date mismatch. Sub List %s [%s] does not match [%s]", l.name.get(), dateFormat.format(refDate.get()), dateFormat.format(maxDate.get())));
                    }
                    if (l.status.getNonNull() == Status.Completed.getCode() && !refDate.isPresent()) {
                        messages.add(String.format("Sub Meter list %s has no readings", l.name.get()));
                    }
                }
            }
        } else {

            if(list.smartReadings == null) {
                messages.add(String.format("Smart Meter Reading list has no readings"));
            }

            if(list.conventionalReadings == null) {
                messages.add(String.format("Conventional Meter Reading list has no readings"));
            }

        }
        return messages;
    }

    public static boolean proceedWithCompleted(Connection connection, PecMeterReadingListEntity list,String user) {
        // update the pec meter reading list date and status
        // if all the meter lists are completed
        // list.meterReadingListId.get()
        PecUtilityMeterReadingListEntity uList = PecUtilityMeterReadingListEntity.ReadingListLinkEntity.getUtilityMeterReadingLists(
                connection, list.meterReadingListId.get());
        if (uList.getMeterReadingLists(connection).stream().
                filter(f -> f.status.getNonNull() < Status.Completed.getCode()).count() == 0) {
            list.log(connection, Status.RequestClose, "REQUESTED LIST CLOSE", String.format("USER [%s] requested meter reading list to be completed",user), null);
            uList.readingDate.set(list.readingDate.get());
            uList.status.set(Status.Completed.getCode());
            DataSourceDB.set(connection, uList);
            recall(connection, uList);
            return true;
        } else {
            return false;
        }
    }

    /**
     * when the reading time has expired
     */
    public void processCompleted() {
        DataSourceDB.executeInTx(dataSource, connection -> {
            if (autoPush) {
                for (PecMeterReadingListEntity list : PecMeterReadingListEntity.getCompleted(connection)) {
                    proceedWithCompleted(connection, list,"SERVER");
                }
            }
            for (PecUtilityMeterReadingListEntity list : PecUtilityMeterReadingListEntity.getExpired(connection)) {
                for (PecMeterReadingListEntity l : list.meterReadingList.get(connection)) {
                    DeviceEntitySyncMapEntity.cancelUndelivered(connection, PecUtilityMeterReadingListEntity.class, l.meterReadingListId.get());
                    DeviceEntitySyncMapEntity.recall(connection, PecUtilityMeterReadingListEntity.class, l.meterReadingListId.get());
                    list.status.set(PecMeterReadingListEntity.Status.Completed.getCode());
                    list.updateStatus(connection, Status.Completed, Status.Expired, "READINGS EXPIRED. RECALLED", "", null);
                }
            }
            // update smart meter readings
            for (PecUtilityMeterReadingListEntity list : PecUtilityMeterReadingListEntity.getByStatus(connection, Status.Completed)) {
                try {
                    for (PecMeterRegisterEntity register : list.smartMeterRegisterList.get(connection)) {
                        service.update(connection, register.meter.getOne(connection), register, list.cycleStartDate.get(), list.readingDate.get(), tmzOffset, true);
                    }
                    list.updateStatus(connection, Status.RequestClose, Status.RequestClose, "READ SMART METER READING DATA", "", null);
                } catch (Exception ex) {
                    log.log(Level.WARNING,ex.getMessage(),ex);
                    list.updateStatus(connection, Status.Error, Status.Error, "ERROR UPDATING READINGS", ex.getMessage(), ex);
                }
            }
        });
    }

    /**
     * push the data back to ice utility
     */
    public void pushData() {

        DataSourceDB.executeInTx(dataSource, connection -> {

            for (PecUtilityMeterReadingListEntity list : PecUtilityMeterReadingListEntity.getByStatus(connection, Status.RequestClose)) {
                // send to ice utility
                StringBuilder request = new StringBuilder();
                try {

                    PecMeterReadingView.PecMeterReadingViewList readingViewList = new PecMeterReadingView()
                            .init(list.utilityMeterReadingListId.get())
                            .getGroupsByReadingReference(dataSource);

                    WebServiceResponse response = helper.sendCreateUpdateMeterReadingMultipleRequest(readingViewList, request);
                    WebServiceResponse updateListResponse = helper.sendListUpdate(list.reference_id.get());
                    list.updateStatus(connection, Status.Closed, Status.Closed,
                            "SENT TO UTILITY. STATUS CLOSED",
                            response.getStatus().toString() + "\n\n" + request.toString() + "\n\n" +
                                    "LIST UPDATE RESPONSE: " + updateListResponse.getStatus().toString(),
                            null);
                } catch (Exception ex) {
                    TAG.log(Level.WARNING, ex.getMessage(), ex);
                    list.updateStatus(connection, Status.Error, Status.Error, "ERROR SENDING TO UTILITY", request.toString(), ex);
                    connection.commit();
                }
            }

        });
    }

    private void copyUtilMeterListIdData() {
        DataSourceDB.execute(dataSource, "delete from pec_meter_reading_list_copy");
        DataSourceDB.execute(dataSource, "delete from pec_meter_reading_list_delete");
        DataSourceDB.executeInTx(dataSource, c -> {
            try (PreparedStatement ps = c.prepareStatement("insert into pec_meter_reading_list_copy(ice_meterreadinglist_id) values (?)")) {
                DataSourceDB.executeInTx(iceDataSource, iceC -> {
                    for (IceMeterReadingList list : new DataSourceDB<>(IceMeterReadingList.class).getAllWhere(iceC, "ICE_MeterReadingListStatus = 'AP'")) {
                        ps.setObject(1, list.meterReadingListId.get());
                        ps.execute();
                    }
                });
            }
            c.commit();
        });
    }


    @Synchronized
    public void deleteRemoved() {
        copyUtilMeterListIdData();
        DataSourceDB.executeInTx(dataSource, c -> {

            String whereSQL = String.format("to_number(reference_id,'9999999999') not in (select * from pec_meter_reading_list_copy)");
            for (PecUtilityMeterReadingListEntity uList : new DataSourceDB<>(PecUtilityMeterReadingListEntity.class).getAllWhere(c, whereSQL)) {
                recall(c, uList);
            }
        });
        DataSourceDB.execute(dataSource, "insert into pec_meter_reading_list_delete(meter_reading_list_id) " +
                "select meter_reading_list_id from pec_utility_meter_reading_list where to_number(reference_id,'9999999999') not in (select * from pec_meter_reading_list_copy)");
        DataSourceDB.execute(dataSource, "delete from pec_utility_meter_reading_list where to_number(reference_id,'9999999999') not in (select * from pec_meter_reading_list_copy)");
        DataSourceDB.execute(dataSource, "delete from pec_meter_reading_list where meter_reading_list_id in (select meter_reading_list_id from pec_meter_reading_list_delete)");
    }

    /**
     * meter
     */
    public static void verifySplitMeterReadingDates(Connection connection, PecMeterReadingListEntity list) {
        List<PecMeterReadingListEntity> lists = getMeterReadingLists(connection, list.meterReadingListId.get()).stream().
                filter(v -> v.status.getNonNull() == Status.Completed.getCode()).collect(Collectors.toCollection(ArrayList::new));
        // get all the max dates
        Optional<Timestamp> max =
                lists.stream().map(l -> l.getMaxConventionalReadingDate(connection)).filter(l -> l.isPresent()).map(l -> l.get()).max(Timestamp::compareTo);
        if (max.isPresent()) {
            PecMeterReadingListEntity maxList = lists.stream().filter(p -> p.getMaxConventionalReadingDate(connection).isPresent()).sorted(Comparator.comparing(a -> a.readingDate.get())).findFirst().get();
            lists.stream().filter(l -> l.getMaxConventionalReadingDate(connection).get().toLocalDateTime().truncatedTo(ChronoUnit.DAYS).
                    compareTo(max.get().toLocalDateTime().truncatedTo(ChronoUnit.DAYS)) < 0).forEach(l -> {
                // clear the readings and add a message
                l.log(connection, "CLEARED READINGS", "Received newer meter reading data from " + maxList.name.get(), null);
                l.readings.getAllAsList(connection).stream().forEach(r -> {
                    DataSourceDB.set(connection, (EntityDB) r.reading.set(null));
                });
                DataSourceDB.set(connection, (EntityDB) l.status.set(Status.Started.getCode()));
            });
        }
    }

    public static void getSmartMeterReadingDatesInSyncWithManualReadings(Connection connection, PecMeterReadingListEntity list) {
        Optional<PecMeterReadingEntity> max = list.conventionalReadings.getAllAsList(connection).stream().
                filter(r -> r.reading.get() != null && r.readingDate.get() != null).
                sorted((a, b) -> a.readingDate.get().compareTo(a.readingDate.get())).findFirst();
        if (max.isPresent()) {
            // update all the smart meters
            list.smartReadings.getAllAsList(connection).stream().forEach(r ->
                    DataSourceDB.set(connection, (EntityDB) r.readingDate.set(max.get().readingDate.get())));
        }
    }

//    @Lock(LockType.READ)
    @Schedule(hour = "*", minute = "*", second = "*/120", persistent = false)
    public void startServices() {
        if (propertiesConfig.getUtility_meter_reading_sync_enabled()) {
            getReadingLists(); // Sync Reading Lists
            processCompleted(); // Processing Reading Lists
            pushData(); // Push Meter Reading Lists to ICE Utility
            if (autoDelete) {
                deleteRemoved();
            }
        }
    }

}
