package za.co.spsi.mdms.common.services;

import za.co.spsi.mdms.common.db.survey.PecLocationSurveyEntity;
import za.co.spsi.mdms.common.db.survey.PecMeterReadingListEntity;
import za.co.spsi.toolkit.crud.sync.db.DeviceEntity;
import za.co.spsi.toolkit.crud.sync.service.BatchProcessor;
import za.co.spsi.toolkit.ee.properties.ConfValue;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.util.HashMap;

@Singleton
@TransactionManagement(value = TransactionManagementType.BEAN)
@DependsOn(value = "MDMSUpgradeService")
public class MDMSBatchProcessor extends BatchProcessor {

    static {
        registeredEntities = new HashMap<>();
        registeredEntities.put("pecLocationSurvey", PecLocationSurveyEntity.class);
        registeredEntities.put("pecMeterReadingList", PecMeterReadingListEntity.class);
        registeredEntities.put("meterReadingList", PecMeterReadingListEntity.class);
        registeredEntities.put("deviceTracked", DeviceEntity.class);
    }

    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;


    @Inject
    @ConfValue(value = "batchProcessorEnabled",folder = "server")
    private boolean processEnabled = true;

    @Lock(LockType.WRITE)
    @Schedule(minute = "*", hour = "*", second = "*/120", persistent = false)
    public void atSchedule() {
        if (processEnabled) {
            process(dataSource);
        }
    }

}
