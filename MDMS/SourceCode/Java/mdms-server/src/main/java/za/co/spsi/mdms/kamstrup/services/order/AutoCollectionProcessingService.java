package za.co.spsi.mdms.kamstrup.services.order;

import org.springframework.util.CollectionUtils;
import za.co.spsi.mdms.common.db.MeterReadingEntity;
import za.co.spsi.mdms.common.db.generator.GeneratorTransactionEntity;
import za.co.spsi.mdms.common.properties.config.PropertiesConfig;
import za.co.spsi.mdms.io.kamstrup.RestHelper;
import za.co.spsi.mdms.kamstrup.db.KamstrupGapProcessorJobEntity;
import za.co.spsi.mdms.kamstrup.db.KamstrupMeterEntity;
import za.co.spsi.mdms.kamstrup.processor.KamstrupProcessor;
import za.co.spsi.mdms.kamstrup.services.order.domain.*;
import za.co.spsi.mdms.util.MeterFilterService;
import za.co.spsi.mdms.util.PrepaidMeterFilterService;
import za.co.spsi.mdms.util.XmlHelper;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.ee.properties.ConfValue;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import javax.xml.bind.JAXBException;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static za.co.spsi.mdms.kamstrup.db.KamstrupMeterEntity.findMeterBySerialN;
import static za.co.spsi.mdms.kamstrup.processor.KamstrupProcessor.*;

@Singleton
@Startup
@DependsOn({"PropertiesConfig"})
@TransactionManagement(value = TransactionManagementType.BEAN)
public class AutoCollectionProcessingService {

    @Resource(mappedName = "java:/jdbc/mdms")
    private javax.sql.DataSource dataSource;

    @Inject
    private RestHelper restHelper;

    @Inject
    private MeterFilterService meterFilterService;

    @Inject
    private KamstrupProcessor kamstrupProcessor;

    @Inject
    private PrepaidMeterFilterService prepaidFilter;

    @Inject
    @ConfValue(folder = "server",value = "utilitydriver.uri")
    private String utilityDriverUri;

    @Inject
    private PropertiesConfig propertiesConfig;

    public static final Logger TAG = Logger.getLogger(AutoCollectionProcessingService.class.getName());

    private List<KamstrupMeterEntity> kamMeterEntityList;
    private Timestamp meterListExpiryTS;
    private boolean metersLoaded;

    @PostConstruct
    private void init() {
        kamMeterEntityList = new ArrayList<>();
        metersLoaded = false;
        meterListExpiryTS = Timestamp.valueOf(LocalDateTime.now().minusMinutes(5));
    }

    private void processAutoCollection() {
        TAG.info("START");
        List<KamstrupMeterEntity> kamstrupMeterEntityList = getFilteredMeterEntityList();
        for(KamstrupMeterEntity kamMeter : kamstrupMeterEntityList) {
            if(kamMeter != null) {
                Readings meterReadings = null;
                if(kamMeter.ref.get() != null)
                    meterReadings = getMeterReadings(kamMeter.ref.get());
                if(meterReadings != null) {
                    if (meterReadings.reading != null) {
                        TAG.info(String.format("Kamstrup Meter: %s -> Meter Readings:",kamMeter.serialN.get()));
                        if(kamMeter.isWater()) {
                            if(kamMeter.lastCommsD.get() == null)
                                kamMeter.lastCommsD.set(timestampToUtc(Timestamp.valueOf(LocalDateTime.now().minusHours(3))));
                            processWaterReadings(meterReadings, kamMeter);
                        } else {
                            if(kamMeter.lastTotalEnergyUpdateTimestamp.get() == null)
                                kamMeter.lastTotalEnergyUpdateTimestamp.set(timestampToUtc(Timestamp.valueOf(LocalDateTime.now().minusHours(3))));
                            processEnergyReadings(meterReadings, kamMeter);
                        }
                    }
                }
            }
        }
        TAG.info("DONE");
    }

    private void loadKamstrupMeters() {

        Timestamp nowTS = Timestamp.valueOf(LocalDateTime.now());

        String kamMeterQuery = "select * from KAMSTRUP_METER where STATE = 'InUse' " +
                " and TYPE_DESC not like '%162M%' " +
                " and TYPE_DESC not like '%351C%' " +
                " and TYPE_DESC not like '%382M%' " +
                " order by LAST_COMMS_D asc";

        List<KamstrupMeterEntity> meters = null;

        if(!metersLoaded) {
            TAG.info("Load Kamstrup Meter List Into Cache.");
            try (Connection connection = dataSource.getConnection()) {
                meters = new DataSourceDB<>(KamstrupMeterEntity.class).getAllAsList(connection, kamMeterQuery);
            } catch (SQLException throwables) {
                TAG.severe(throwables.getMessage());
            }
            if(!CollectionUtils.isEmpty(meters)) {
                kamMeterEntityList.clear();
                kamMeterEntityList.addAll(meters);
                meterListExpiryTS = Timestamp.valueOf(LocalDateTime.now().plusDays(1));
                metersLoaded = true;
                TAG.info(String.format("Kamstrup Meter List [%d] Loaded Into Cache.", kamMeterEntityList.size()));
            } else {
                metersLoaded = false;
            }
        } else if( nowTS.getTime() > meterListExpiryTS.getTime() ) {
            TAG.info("Kamstrup Meter List Cache Expired.");
            metersLoaded = false;
        } else {
            TAG.info(String.format("Kamstrup Meter List [%d] Cache Valid.", kamMeterEntityList.size()));
        }

    }

    private Readings getMeterReadings(String meterRef) {
        String path = String.format("/collection/meters/%s/readings/", meterRef.split("/")[6]);
        String results = restHelper.restGet(utilityDriverUri,String.class, path);
        Readings meterReadings;
        try {
            meterReadings = XmlHelper.unmarshall(Readings.class, results);
        } catch (JAXBException ex) {
            ex.printStackTrace();
            meterReadings = new Readings();
        }
        return meterReadings;
    }

    private List<KamstrupMeterEntity> getFilteredMeterEntityList() {

        List<KamstrupMeterEntity> meterEntityList = new ArrayList<>();

        if(metersLoaded && !propertiesConfig.getKamstrup_autocollection_filtered_meters_enabled()) {
            return kamMeterEntityList;
        } else {
            List<String> filteredSerialNList = meterFilterService.getFilteredMeters();
            for(String serialN : filteredSerialNList) {
                KamstrupMeterEntity kamMeter = findMeterBySerialN(dataSource,serialN);
                meterEntityList.add(kamMeter);
            }
        }

        return meterEntityList;
    }

    private void processWaterReadings(Readings meterReadings, KamstrupMeterEntity kamMeter) {

        for (Reading r : meterReadings.reading) {
            try {
                if (r.registerReading != null) {
                    if (r.registerReading.register.id.matches("[8,9].1.1.0.0.255")) { // Only allow Cold/Hot Water Registers
                        if (r.registerReading.readouttime.getTime() > kamMeter.lastCommsD.get().getTime()) {
                            MeterReadingEntity meterReadingEntity = new MeterReadingEntity();
                            meterReadingEntity.kamMeterId.set(kamMeter.meterId.get());
                            DataSourceDB.executeInTx(dataSource, connection -> {
                                if (updateWaterEntryRegisterValues(connection, r, meterReadingEntity)) {

                                    if (shouldScheduleGapProcessor(kamMeter.lastCommsD.get(), meterReadingEntity.entryTime.get())) {
                                        KamstrupGapProcessorJobEntity.create(connection, kamMeter
                                                , KamstrupGapProcessorJobEntity.JobTypes.WATER.name()
                                                , kamMeter.groupId.get()
                                                , Timestamp.valueOf(kamMeter.lastCommsD.get().toLocalDateTime().minusMinutes(120))
                                                , Timestamp.valueOf(meterReadingEntity.entryTime.get().toLocalDateTime().plusMinutes(120))
                                                , Timestamp.valueOf(LocalDateTime.now().plusMinutes(60))
                                                , 60);
                                    }

                                    meterReadingEntity.prepaidMeter.set(meterReadingEntity.prepaidMeter.get() == null ?
                                            prepaidFilter.isPrepaid(kamMeter.serialN.get()) : meterReadingEntity.prepaidMeter.get());
                                    kamMeter.lastCommsD.set(meterReadingEntity.entryTime.get());
                                    kamMeter.failedNumber.set(0);

                                    TAG.log(Level.FINER, String.format("Add Meter Reading:\n%s\n", meterReadingEntity.toString()));
                                    DataSourceDB.set(connection, kamMeter);
                                    DataSourceDB.set(connection, meterReadingEntity);
                                    connection.commit();
                                }

                            });
                        }
                    }
                }
            } catch (Exception ex) {
                TAG.info(ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private void processEnergyReadings(Readings meterReadings, KamstrupMeterEntity kamMeter) {

        for (Reading r : meterReadings.reading) {
            if (r.loggerReading != null) {
                for (za.co.spsi.mdms.kamstrup.services.order.domain.Logger l : r.loggerReading.loggers) {
                    if (l.loggerId.equals("0.1.99.1.0.255")) { // Only allow Electricity Load Profile Logger
                        for (Entry e : l.entries.entry) {
                            try {
                                if(e != null) {
                                    if(e.timestamp != null) {
                                        if(e.timestamp.getTime() > kamMeter.lastTotalEnergyUpdateTimestamp.get().getTime()) {
                                            MeterReadingEntity meterReadingEntity = new MeterReadingEntity();
                                            meterReadingEntity.kamMeterId.set(kamMeter.meterId.get());
                                            DataSourceDB.executeInTx(dataSource, connection -> {
                                                if(updateEnergyEntryRegisterValues(connection, e, meterReadingEntity)) {
                                                    kamstrupProcessor.individualKamstrupRegisterGapIdentifier( connection, meterReadingEntity.entryTime.get(), kamMeter, e.registers.registers );
                                                    meterReadingEntity.prepaidMeter.set(meterReadingEntity.prepaidMeter.get() == null ?
                                                            prepaidFilter.isPrepaid(kamMeter.serialN.get()) : meterReadingEntity.prepaidMeter.get());
                                                    GeneratorTransactionEntity.updateForTx(connection, meterReadingEntity);
                                                    kamMeter.lastTotalEnergyUpdateTimestamp.set(meterReadingEntity.entryTime.get());
                                                    kamMeter.lastCommsD.set(meterReadingEntity.entryTime.get());
                                                    kamMeter.failedNumber.set(0);
                                                    TAG.log(Level.FINER, String.format("Add Meter Reading:\n%s\n",meterReadingEntity.toString()));
                                                    DataSourceDB.set(connection, kamMeter);
                                                    DataSourceDB.set(connection, meterReadingEntity);
                                                    connection.commit();
                                                }
                                            });
                                        }
                                    }
                                }
                            } catch (Exception ex) {
                                TAG.info(ex.getMessage());
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    private Double scaleWaterVolumeRegister(Register register) {
        return register.value * Math.pow(10, register.scale );
    }

    private boolean shouldScheduleGapProcessor(Timestamp lastCommsD,Timestamp entryTime) {
        return (lastCommsD != null && Duration.between(lastCommsD.toLocalDateTime(),entryTime.toLocalDateTime()).toMinutes() >= 60);
    }

    private boolean updateWaterEntryRegisterValues(Connection connection, Reading reading, MeterReadingEntity readingEntity) {
        boolean readingExist = false;
        boolean volumeRegistersNull = false;
        Timestamp nowUtcTS = KamstrupProcessor.timestampToUtc(Timestamp.valueOf(LocalDateTime.now()));
        readingEntity.entryTime.set(readingEntity.adjustTimestamp(60, reading.registerReading.readouttime));
        readingEntity.entryDay.set(Integer.parseInt(dayFormat.format(reading.registerReading.readouttime)));
        MeterReadingEntity meterReadingTmp = DataSourceDB.getFromSet(connection, readingEntity);
        readingExist = meterReadingTmp != null;
        if(readingExist) {
            volumeRegistersNull = meterReadingTmp.volume1.getNonNull() == 0.0;
            if(volumeRegistersNull) {
                readingEntity.copyStrict(meterReadingTmp);
            }
        } else {
            readingEntity.createTime.set(nowUtcTS);
            readingEntity.volume1.set( scaleWaterVolumeRegister(reading.registerReading.register) );
        }
        return !readingExist || volumeRegistersNull;
    }

    private boolean updateEnergyEntryRegisterValues(Connection connection, Entry entry, MeterReadingEntity readingEntity) {
        boolean readingExist = false;
        boolean totalRegistersNull = false;
        Timestamp nowUtcTS = KamstrupProcessor.timestampToUtc(Timestamp.valueOf(LocalDateTime.now()));
        readingEntity.entryTime.set(readingEntity.adjustTimestamp(30, entry.timestamp));
        readingEntity.entryDay.set(Integer.parseInt(dayFormat.format(entry.timestamp)));
        MeterReadingEntity meterReadingTmp = DataSourceDB.getFromSet(connection, readingEntity);
        readingExist = meterReadingTmp != null;
        if(readingExist) {
            totalRegistersNull = meterReadingTmp.totalKwhP.getNonNull() == 0.0;
            if(totalRegistersNull) {
                readingEntity.copyStrict(meterReadingTmp);
            }
        } else {
            readingEntity.createTime.set(nowUtcTS);
        }
        readingEntity.update(entry.registers.registers);
        return !readingExist || totalRegistersNull;
    }

    @Lock(LockType.WRITE)
    @Schedule(hour = "*", minute = "*/5", second = "0", persistent = false)
    public void loadMetersService() {
        if (propertiesConfig.getKamstrup_autocollection_enabled()) {
            loadKamstrupMeters();
        }
    }

    @Lock(LockType.WRITE)
    @Schedule(hour = "*/3", minute = "0", second = "0", persistent = false)
    public void scheduleAutoCollectService() {
        if (propertiesConfig.getKamstrup_autocollection_enabled()) {
            processAutoCollection();
        }
    }

}
