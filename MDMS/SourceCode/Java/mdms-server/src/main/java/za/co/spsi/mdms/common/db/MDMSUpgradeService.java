package za.co.spsi.mdms.common.db;

import lombok.SneakyThrows;
import za.co.spsi.mdms.common.db.survey.PecMeterRegisterEntity;
import za.co.spsi.mdms.common.db.survey.PecUtilityMeterReadingListEntity;
import za.co.spsi.mdms.common.services.MdmsSettingsService;
import za.co.spsi.mdms.kamstrup.db.KamstrupMeterEntity;
import za.co.spsi.mdms.kamstrup.services.utility.MeterRegisterUpdateService;
import za.co.spsi.mdms.nes.db.NESMeterEntity;
import za.co.spsi.mdms.nes.services.meter.NESMeterSyncService;
import za.co.spsi.mdms.nes.services.result.NESMeterResultSyncService;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.ee.properties.ConfValue;
import za.co.spsi.toolkit.ee.properties.TextFile;
import za.co.spsi.toolkit.ee.upgrade.DeployService;
import za.co.spsi.toolkit.service.ProcessorService;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.logging.Logger;

/**
 * Created by jaspervdb on 2016/10/19.
 */
@Singleton
@Startup()
@TransactionManagement(value = TransactionManagementType.BEAN)
@DependsOn({"MdmsDatasource"})
public class MDMSUpgradeService extends DeployService {

    @Inject
    @TextFile("sql/duplicate_kam_meters.sql")
    private String duplicateKamSql;

    public static final Logger TAG = Logger.getLogger(MDMSUpgradeService.class.getName());

    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @Inject
    private MdmsSettingsService mdmsSettingsService;

    @Override
    protected String[] getUpgradePaths() {
        return new String[]{
                "za.co.spsi.toolkit.crud.db.gis",
                "za.co.spsi.toolkit.db",
                "za.co.spsi.toolkit.db.maintenance",
                "za.co.spsi.toolkit.crud.gis.db",
                "za.co.spsi.toolkit.crud.sync.db",
                "za.co.spsi.toolkit.crud.login",
                "za.co.spsi.mdms.kamstrup.db",
                "za.co.spsi.mdms.elster.db",
                "za.co.spsi.mdms.nes.db",
                "za.co.spsi.mdms.generic.meter.db",
                "za.co.spsi.mdms.common.db.generator",
                "za.co.spsi.toolkit.crud.entity",
                "za.co.spsi.mdms.common.db",
                "za.co.spsi.mdms.common.db.survey",
                "za.co.spsi.toolkit.db.audit",
                "za.co.spsi.mdms.common.db.utility",
                "za.co.spsi.mdms.generic.meter.db",
                "za.co.spsi.toolkit.db.upgrade"};
    }

    @Override
    protected Class[] getExclusions() {
        return new Class[]{};
    }


    @Override
    protected DataSource getDataSource() {
        return dataSource;
    }

    private void mergeDuplicateTable(Connection connection, NESMeterEntity meter, String tableName, String meterIdName) throws SQLException {
        DataSourceDB.executeUpdate(connection,
                String.format("update %s set %s = ? where %s is not null and " +
                        "%s in (select meter_id from nes_meter where serial_n = ? and not meter_id = ?)", tableName, meterIdName, meterIdName, meterIdName),
                meter.meterId.get(), meter.serialN.get(), meter.meterId.get());
    }

    private void mergeNesDuplicate(DataSource dataSource, NESMeterEntity meter) {
        TAG.info("MERGE " + meter.serialN.get());
        DataSourceDB.executeInTx(dataSource, connection -> {
            mergeDuplicateTable(connection, meter, "meter_reading", "nes_meter_id");
            mergeDuplicateTable(connection, meter, "nes_meter_result", "meter_id");
            mergeDuplicateTable(connection, meter, "pec_meter", "nes_meter_id");

            DataSourceDB.executeUpdate(connection,
                    "delete from nes_meter where meter_id in (select meter_id from nes_meter where serial_n = ? and not meter_id = ? )",
                    meter.serialN.get(), meter.meterId.get());
        });
    }

    private void mergeDuplicateTable(Connection connection, KamstrupMeterEntity meter, String tableName, String meterIdName) throws SQLException {
        DataSourceDB.executeUpdate(connection,
                String.format("update %s set %s = ? where %s is not null and " +
                                "%s in (select meter_id from kamstrup_meter where ref = ? and not meter_id = ?)",
                        tableName, meterIdName, meterIdName, meterIdName),
                meter.meterId.get(), meter.ref.get(), meter.meterId.get());
    }

    private void mergeKamDuplicate(DataSource dataSource, KamstrupMeterEntity meter) {
        TAG.info("MERGE " + meter.ref.get());
        String meterR = meter.ref.get();
        meterR = meterR.endsWith("/") ? meterR.substring(0, meterR.length() - 1) : meterR;
        final String ref = meterR.substring(meterR.lastIndexOf("/") + 1);

        DataSourceDB.executeInTx(dataSource, connection -> {
            mergeDuplicateTable(connection, meter, "meter_reading", "kam_meter_id");
            mergeDuplicateTable(connection, meter, "gen_meter_link", "kam_meter_id");
            mergeDuplicateTable(connection, meter, "pec_meter", "kam_meter_id");
            mergeDuplicateTable(connection, meter, "broker_command", "meter_id");
            mergeDuplicateTable(connection, meter, "KAMSTRUP_GROUP_METER", "meter_id");
            mergeDuplicateTable(connection, meter, "KAMSTRUP_METER_ORDER_HISTORY", "meter_id");
            mergeDuplicateTable(connection, meter, "KAM_METER_READ_FAILED_LOG", "meter_id");
            mergeDuplicateTable(connection, meter, "KAMSTRUP_METER_READING", "meter_id");
            mergeDuplicateTable(connection, meter, "KAMSTRUP_METER_REGISTER", "meter_id");

            DataSourceDB.executeUpdate(connection,
                    "delete from kamstrup_meter where meter_id in (select meter_id from kamstrup_meter where ref like '%"
                            + ref + "%' and not meter_id = ? )",
                    meter.meterId.get());
        });
    }

    public void dbFix() {
        // tmp fix for new environment
        // temp fix for duplicate
        DataSourceDB.executeQuery(dataSource, new Class[]{String.class}, duplicateKamSql).forEach(s ->
                mergeKamDuplicate(dataSource
                        , DataSourceDB.get(KamstrupMeterEntity.class, dataSource
                                , "select * from kamstrup_meter where ref like '%" + s.get(0).toString().replace("/", "") + "%'")));

        DataSourceDB.executeUpdate(dataSource, "update KAMSTRUP_METER set ref = replace (ref,'utilidriver','UtiliDriver')");
        DataSourceDB.executeUpdate(dataSource, "update KAMSTRUP_METER set ref = replace (ref,'172.31.91.228','192.1.0.181')");
        DataSourceDB.executeUpdate(dataSource, "update KAMSTRUP_METER set ref = replace (ref,'196.44.197.67','192.1.0.181')");

    }

    @PostConstruct
    @Override
    public void upgrade() throws SQLException {

        if (getFrwEngineeringEnabled()) {
            testProxyConnection();
            // FE upgrade starts
            super.upgrade();
        }

    }

    @Inject
    private MeterRegisterUpdateService service;

    @Inject
    @ConfValue(value = "utility.meter_reading_sync.tmz_offset", folder = "server")
    private int tmzOffset;

    @SneakyThrows
    public void test() {
        try (Connection connection = dataSource.getConnection()) {
            PecUtilityMeterReadingListEntity uList = DataSourceDB.getFromSet(connection,
                    new PecUtilityMeterReadingListEntity().utilityMeterReadingListId.set("2a0d4730-939c-4583-a393-a00272d60870"));
            for (PecMeterRegisterEntity register : uList.smartMeterRegisterList.get(connection)) {
                service.update(connection, register.meter.getOne(connection), register, uList.cycleStartDate.get()
                        , Timestamp.from(LocalDate.of(2019,01,18).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), tmzOffset,true);
            }
        }
    }


}