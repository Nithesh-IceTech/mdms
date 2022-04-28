package za.co.spsi.mdms.nes.services.result;

import lombok.extern.java.Log;
import za.co.spsi.mdms.common.properties.config.PropertiesConfig;
import za.co.spsi.mdms.nes.db.NESMeterEntity;
import za.co.spsi.mdms.nes.db.NESMeterResultEntity;
import za.co.spsi.mdms.nes.db.NESResultDeviceView;
import za.co.spsi.mdms.nes.services.meter.NESMeterService;
import za.co.spsi.mdms.nes.util.NESDataSourceHelper;
import za.co.spsi.mdms.util.MeterFilterService;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.ee.properties.ConfValue;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;


/**
 * Created by jaspervdb on 2016/10/12.
 */
@Log
@Dependent
@TransactionManagement(value = TransactionManagementType.BEAN)
public class NESMeterResultService {

    @Resource(mappedName = "java:/jdbc/mdms")
    private javax.sql.DataSource mdmsDs;

    @Resource(mappedName = "java:/jdbc/nesdb")
    private javax.sql.DataSource nesDs;

    @Inject
    NESDataSourceHelper nesDataSourceHelper;

    @Inject
    private MeterFilterService meterFilterService;

    @Inject
    @ConfValue(value = "utility.meter_reading_sync.tmz_offset", folder = "server")
    private int tmzOffset;

    private boolean active = true;

    @Inject
    private NESMeterService meterService;

    @Inject
    private PropertiesConfig propertiesConfig;

    public void syncResults() {

        if(propertiesConfig.getNes_processing_enabled()) {

            try {
                log.info("Importing NES Results ");
                try (Connection nesConnection = nesDataSourceHelper.getConnection()) {
                    Timestamp maxTime = NESResultDeviceView.getStartTime(mdmsDs);
                    for (NESResultDeviceView view : new NESResultDeviceView().init(meterFilterService,maxTime).getDataSource(nesConnection)) {
                        if (active) {
                            // check if device exists
                            NESMeterEntity nesMeterEntity = meterService.update(mdmsDs, view.device);
                            NESMeterResultEntity result = null;
                            if (view.result.dateTimeStamp.get().equals(maxTime)) {
                                result = DataSourceDB.loadFromId(mdmsDs,
                                        (NESMeterResultEntity) new NESMeterResultEntity().meterResultId.set(view.result.resultId.get()));
                            }
                            if (result == null) {
                                try {
                                    DataSourceDB.set(mdmsDs, new NESMeterResultEntity().init(nesMeterEntity, view.result));
                                } catch (Exception ex) {
                                    throw new RuntimeException(ex);
                                }
                            }

                        } else {
                            return;
                        }
                    }
                }
            } catch (SQLException sqle) {
                throw new RuntimeException(sqle);
            }

        }

    }

    @PreDestroy
    public void stopProcessing() {
        this.active = false;
    }
}