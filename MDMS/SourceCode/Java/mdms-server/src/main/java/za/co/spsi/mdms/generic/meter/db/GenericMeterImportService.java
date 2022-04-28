package za.co.spsi.mdms.generic.meter.db;

import lombok.extern.java.Log;
import za.co.spsi.mdms.common.properties.config.PropertiesConfig;
import za.co.spsi.mdms.common.services.ReadingGenericGapIdentifier;
import za.co.spsi.mdms.common.services.ReadingGenericGapProcessor;
import za.co.spsi.mdms.util.DBToDBUtil;
import za.co.spsi.mdms.util.DBUtil;
import za.co.spsi.mdms.util.PrepaidMeterFilterService;
import za.co.spsi.toolkit.db.DSDB;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.drivers.DriverFactory;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;

@Log
@Singleton
@TransactionManagement(value = TransactionManagementType.BEAN)
@DependsOn(value = "MDMSUpgradeService")
public class GenericMeterImportService {

    @Resource(mappedName = "java:/jdbc/mdms")
    private javax.sql.DataSource dataSource;

    @Inject
    private PropertiesConfig propertiesConfig;

    @Inject
    private PrepaidMeterFilterService prepaidFilter;

    @Inject
    private ReadingGenericGapIdentifier gapIdentifier;

    @PostConstruct
    public void postConstruct() {
        // Clear all manual import at startup
        String updateQuery = String.format("update DB_TO_DB_MAPPING_DETAIL set MAN_IMPORT_RUNNING = %s, BATCH_IMPORT_RUNNING = %s",
                DriverFactory.getDriver().boolToNumber(false),
                DriverFactory.getDriver().boolToNumber(false));
        DataSourceDB.executeUpdate(dataSource, updateQuery, null);
    }

    private void process() {

        try (Connection connection = dataSource.getConnection()) {

            String query = String.format("select * from DB_TO_DB_MAPPING where active = %s",
                    DriverFactory.getDriver().boolToNumber(true));

            for (DbToDbMappingEntity dbToDbMappingEntity :
                    new DataSourceDB<>(DbToDbMappingEntity.class).getAll(connection, query)) {

                String manImportQuery = String.format("select * from DB_TO_DB_MAPPING_DETAIL where MAN_IMPORT_RUNNING = %s and DB_TO_DB_MAPPING_ID = ?",
                        DriverFactory.getDriver().boolToNumber(true));

                // Check if manual import is running
                DbToDbMappingDetailEntity manualImport =
                        DataSourceDB.get(DbToDbMappingDetailEntity.class, dataSource, manImportQuery,
                                dbToDbMappingEntity.dbToDbMappingId.get());

                if (manualImport != null) {
                    return;
                }

                for (DbToDbMappingDetailEntity dbToDbMappingDetailEntity :
                        dbToDbMappingEntity.dbToDbMappingDetailEntityEntityRef.getAllAsList(dataSource)) {

                    if (dbToDbMappingDetailEntity.batchImportRunning.getNonNull() == true) {
                        break;
                    }

                    log.info("Processing");

                    String sql = DBToDBUtil.buildMappingSql(dbToDbMappingEntity, dbToDbMappingDetailEntity, false);
                    try {

                        dbToDbMappingDetailEntity.batchImportRunning.set(true);
                        DSDB.setUpdate(dataSource, dbToDbMappingDetailEntity);
                        log.info("Generic Meter Batch Process Started");

                        Timestamp lastSyncTime =
                                DBToDBUtil.persistMapping(gapIdentifier,sql,
                                        DBUtil.createDataSource(
                                                dbToDbMappingEntity.driver.get(),
                                                dbToDbMappingEntity.serverAddress.get(),
                                                dbToDbMappingEntity.serviceName.get(),
                                                dbToDbMappingEntity.portNumber.get(),
                                                dbToDbMappingEntity.dbName.get(),
                                                dbToDbMappingEntity.userName.get(),
                                                dbToDbMappingEntity.password.get()
                                        ), dataSource, dbToDbMappingEntity, dbToDbMappingDetailEntity, prepaidFilter);

                        dbToDbMappingDetailEntity.lastSyncTime.set(lastSyncTime);
                        DSDB.setUpdate(dataSource, dbToDbMappingDetailEntity);

                    } catch (SQLException e) {
                        log.log(Level.SEVERE, e.getMessage(), e);
                    } finally {
                        dbToDbMappingDetailEntity.batchImportRunning.set(false);
                        DSDB.setUpdate(dataSource, dbToDbMappingDetailEntity);
                        log.info("Generic Meter Batch Process Stopped");
                    }
                }
            }

        } catch (SQLException ex) {
            log.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    @Schedule(hour = "*", minute = "*", persistent = false)
    public void atSchedule() {
        if (propertiesConfig.getGeneric_meter_processing_enabled()) {
            process();
        }
    }

}
