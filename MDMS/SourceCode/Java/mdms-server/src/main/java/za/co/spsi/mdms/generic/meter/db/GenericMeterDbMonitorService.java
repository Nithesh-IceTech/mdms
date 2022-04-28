package za.co.spsi.mdms.generic.meter.db;

import lombok.SneakyThrows;
import za.co.spsi.mdms.util.DBUtil;
import za.co.spsi.mdms.util.PrepaidMeterFilterService;
import za.co.spsi.toolkit.db.DSDB;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.service.ProcessorService;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Connection;
import java.util.logging.Logger;

@Singleton
@TransactionManagement(value = TransactionManagementType.BEAN)
@DependsOn(value = "MDMSUpgradeService")
public class GenericMeterDbMonitorService extends ProcessorService {

    public static final Logger TAG = Logger.getLogger(GenericMeterDbMonitorService.class.getName());

    @Resource(mappedName = "java:/jdbc/mdms")
    private javax.sql.DataSource dataSource;

    @Inject
    private PrepaidMeterFilterService prepaidFilter;

    @Lock(LockType.WRITE)
    @Schedule(minute = "*", hour = "*", second = "*/120", persistent = false)
    public void atSchedule() {
        process();
    }

    @SneakyThrows
    private void process() {

        if (!ProcessorService.isGlobalDelay()) {
            try (Connection connection = dataSource.getConnection()) {

                for (DbToDbMappingEntity dbToDbMappingEntity :
                        new DataSourceDB<>(DbToDbMappingEntity.class).getAll(connection, "select * from DB_TO_DB_MAPPING")) {

                    try {
                        DataSource remoteDataSource = DBUtil.createDataSource(
                                dbToDbMappingEntity.driver.get(),
                                dbToDbMappingEntity.serverAddress.get(),
                                dbToDbMappingEntity.serviceName.get(),
                                dbToDbMappingEntity.portNumber.get(),
                                dbToDbMappingEntity.dbName.get(),
                                dbToDbMappingEntity.userName.get(),
                                dbToDbMappingEntity.password.get());

                        // test connection
                        remoteDataSource.getConnection().close();
                        dbToDbMappingEntity.dbActive.set(true);
                        DSDB.setUpdate(dataSource, dbToDbMappingEntity);

                    } catch (Exception e) {
                        dbToDbMappingEntity.dbActive.set(false);
                        DSDB.setUpdate(dataSource, dbToDbMappingEntity);
                    }
                }

            }
        }
    }
}
