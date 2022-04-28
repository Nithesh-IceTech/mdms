package za.co.spsi.mdms.kamstrup.services.utility;

import za.co.spsi.mdms.common.db.generator.GeneratorEntity;
import za.co.spsi.mdms.common.db.utility.IceGeneratorMeter;
import za.co.spsi.mdms.common.properties.config.PropertiesConfig;
import za.co.spsi.mdms.elster.db.ElsterMeterEntity;
import za.co.spsi.mdms.generic.meter.db.GenericMeterEntity;
import za.co.spsi.mdms.kamstrup.db.KamstrupMeterEntity;
import za.co.spsi.mdms.nes.db.NESMeterEntity;
import za.co.spsi.toolkit.db.DSDB;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.drivers.Driver;
import za.co.spsi.toolkit.db.drivers.DriverFactory;
import za.co.spsi.toolkit.ee.properties.ConfValue;
import za.co.spsi.toolkit.service.ProcessorService;
import za.co.spsi.toolkit.util.Container;
import za.co.spsi.toolkit.util.Processor;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by jaspervdbijl on 2017/03/30.
 */
@Singleton
@DependsOn({"PropertiesConfig"})
@Startup
@TransactionManagement(value = TransactionManagementType.BEAN)
public class UtilityGeneratorMeterSyncService extends ProcessorService {

    public static final Logger TAG = Logger.getLogger(UtilityGeneratorMeterSyncService.class.getName());

    @Resource(mappedName = "java:/jdbc/mdms")
    private javax.sql.DataSource dataSource;

    @Resource(mappedName = "java:/jdbc/IceUtil")
    private javax.sql.DataSource iceDataSource;

    private Processor processor = getProcessor();

    @Inject
    @ConfValue(value = "utility.generator_meter_autoclear.enabled", folder = "server")
    private boolean autoClear;

    @Inject
    private PropertiesConfig propertiesConfig;

    private void process() {
        Container<GeneratorEntity> lstGen = new Container<>();
        List<String> updatedGen = new ArrayList<>();
        DSDB.executeInTx(iceDataSource, utilConnection -> {
            DSDB.executeInTx(dataSource, connection -> {
                for (IceGeneratorMeter iceGen : new DSDB<>(IceGeneratorMeter.class).getAll(utilConnection,
                        "select * from ice_generator_meter_v order by ice_generator_id asc")) {
                    GeneratorEntity gen = lstGen.isPresent() && iceGen.iceGeneratorUU.get().equals(lstGen.get().id.get()) ?
                            lstGen.get() : DSDB.getFromSet(connection, new GeneratorEntity().id.set(iceGen.iceGeneratorUU.get()));
                    gen = gen == null ? new GeneratorEntity() : gen;
                    if (!updatedGen.contains(gen.id.get())) {
                        iceGen.init(gen);
                        DSDB.set(connection, gen);
                        updatedGen.add(gen.id.get());
                        if (autoClear) {
                            DSDB.execute(connection, "delete from gen_meter_link where gen_id = ?", gen.id.get());
                        }
                        lstGen.set(gen);
                    }

                    // sync meter link
                    KamstrupMeterEntity kmeter = DSDB.getFromSet(connection, new KamstrupMeterEntity().serialN.set(iceGen.iceMeterNumber.get()));
                    NESMeterEntity nmeter = DSDB.getFromSet(connection, new NESMeterEntity().serialN.set(iceGen.iceMeterNumber.get()));
                    ElsterMeterEntity emeter = DSDB.getFromSet(connection, new ElsterMeterEntity().serialN.set(iceGen.iceMeterNumber.get()));

                    Driver driver = DriverFactory.getDriver();
                    String genericQuery = String.format("select GENERIC_METER.*" +
                            " from GENERIC_METER" +
                            " where GENERIC_METER.METER_SERIAL_N = ? and GENERIC_METER.LIVE = %s",
                            driver.isOracle() ? driver.boolToNumber(true) : "true");

                    GenericMeterEntity genericMeterEntity = DataSourceDB.get(GenericMeterEntity.class, dataSource,genericQuery,iceGen.iceMeterNumber.get());

                    if (kmeter != null || nmeter != null || emeter != null || genericMeterEntity != null) {
                        GeneratorEntity.MeterLink meterLink = new GeneratorEntity.MeterLink();
                        meterLink.generatorId.set(gen.id.get());
                        meterLink.kamMeterId.set(kmeter != null ? kmeter.meterId.get() : null);
                        meterLink.nesMeterId.set(nmeter != null ? nmeter.meterId.get() : null);
                        meterLink.elsMeterId.set(emeter != null ? emeter.meterId.get() : null);
                        meterLink.genericMeterId.set(genericMeterEntity != null ? genericMeterEntity.genericMeterId.get() : null);
                        DSDB.setIfNotExists(connection, meterLink);
                    } else {
                        TAG.warning(String.format("Could not locate smart meter by serial no %s", iceGen.iceMeterNumber.get()));
                    }
                }
            });
        });
    }

    @PostConstruct
    public void startServices() {
        if (propertiesConfig.getUtility_generator_meter_sync_enabled()) {
            processor.delay(5).minutes(15).repeat(() -> process());
        }
    }

}
