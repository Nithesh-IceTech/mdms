package za.co.spsi.mdms.common.services.broker;

import org.springframework.util.CollectionUtils;
import za.co.spsi.mdms.common.MdmsConstants;
import za.co.spsi.mdms.common.db.utility.IceMeter;
import za.co.spsi.mdms.common.properties.config.PropertiesConfig;
import za.co.spsi.mdms.generic.meter.db.GenericBrokerCommandEntity;
import za.co.spsi.mdms.generic.meter.db.GenericMeterEntity;
import za.co.spsi.mdms.kamstrup.db.KamstrupBrokerCommandEntity;
import za.co.spsi.mdms.kamstrup.db.KamstrupMeterEntity;
import za.co.spsi.mdms.nes.db.NESBrokerCommandEntity;
import za.co.spsi.mdms.nes.db.NESMeterEntity;
import za.co.spsi.mdms.common.db.IceBrokerCommandStatusUpdateEntity;
import za.co.spsi.mdms.util.IceMeterCacheService;
import za.co.spsi.toolkit.db.DSDB;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.ee.properties.ConfValue;
import za.co.spsi.toolkit.util.Assert;
import za.co.spsi.toolkit.util.ExpiringCacheMap;
import za.co.spsi.toolkit.util.StringUtils;

import javax.annotation.Resource;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static za.co.spsi.mdms.common.services.broker.BrokerRequest.Command.CUT;
import static za.co.spsi.mdms.common.services.broker.BrokerRequest.Command.RELEASE;
import static za.co.spsi.mdms.common.services.broker.BrokerRequestResponse.Status.*;

@Dependent
public class BrokerService {

    @Resource(mappedName = "java:/jdbc/mdms")
    public javax.sql.DataSource mdmsDataSource;

    @Resource(mappedName = "java:/jdbc/IceUtil")
    public javax.sql.DataSource iceDataSource;

    @Inject
    @ConfValue(value = "global.broker.env", folder = "server")
    private String brokerServiceEnv;

    @Inject
    private PropertiesConfig propertiesConfig;

    @Inject
    public IceMeterCacheService iceMeterCacheService;

    private ExpiringCacheMap<String, String> meterSerialNumberCacheMap = new ExpiringCacheMap<>(TimeUnit.HOURS.toMillis(1));

    private final Logger TAG = Logger.getLogger(BrokerService.class.getName());

    public BrokerRequestResponse processKamstrupCommand(
            KamstrupMeterEntity kamMeter, KamstrupBrokerCommandEntity.Command command, Timestamp effectedDate) {

        if(propertiesConfig.getKamstrup_broker_processing_enabled()) {

            String meterIDURL = kamMeter.ref.get();

            KamstrupBrokerCommandEntity ent = new KamstrupBrokerCommandEntity();
            ent.setAsCreated();
            ent.meterId.set(kamMeter.meterId.get());
            ent.meterIDURL.set(meterIDURL);
            ent.commandTimeOutCount.set(0);
            ent.command.set(command.getCode());
            ent.createdDate.set(new Timestamp(System.currentTimeMillis()));
            ent.effectedDate.set(effectedDate);

            if(brokerServiceEnv.equalsIgnoreCase("prod")) {
                DataSourceDB.set(mdmsDataSource, ent);
            } else {
                // filter meters
                if (StringUtils.isEmpty(propertiesConfig.getKamstrup_broker_filter_meters()) ||
                        Arrays.stream(propertiesConfig.getKamstrup_broker_filter_meters().split(","))
                                .filter(f -> kamMeter.serialN.get().equals(f)).count() >= 1) {
                    DataSourceDB.set(mdmsDataSource, ent);
                }
            }

            return new BrokerRequestResponse(command == KamstrupBrokerCommandEntity.Command.CUT ? CUT : RELEASE, ent.brokerCommandId.get());

        } else {
            TAG.log(Level.WARNING, "Kamstrup Broker Command Processing Disabled !");
        }

        return new BrokerRequestResponse();
    }

    public BrokerRequestResponse processNesCommand(
            NESMeterEntity meter, NESBrokerCommandEntity.Command command, Timestamp effectedDate) {

        if(propertiesConfig.getNes_broker_processing_enabled()) {

            NESBrokerCommandEntity ent = new NESBrokerCommandEntity();
            ent.meterSerialNumber.set(meter.serialN.get());
            ent.meterId.set(meter.meterId.get());
            ent.command.set(command.getCode());
            ent.submitDate.set(new Timestamp(System.currentTimeMillis()));
            ent.createdDate.set(new Timestamp(System.currentTimeMillis()));
            ent.effectedDate.set(effectedDate);
            ent.commandStatus.set(MdmsConstants.Status.CREATED.getCode());

            if(brokerServiceEnv.equalsIgnoreCase("prod")) {
                DataSourceDB.set(mdmsDataSource, ent);
            } else {
                // filter meters
                if (StringUtils.isEmpty(propertiesConfig.getNes_broker_filter_meters())
                        || Arrays.stream(propertiesConfig.getNes_broker_filter_meters().split(","))
                        .filter(f -> meter.serialN.get().equals(f)).count() >= 1) {
                    DataSourceDB.set(mdmsDataSource, ent);
                }
            }

            return new BrokerRequestResponse(command == NESBrokerCommandEntity.Command.DISCONNECT ? CUT : RELEASE, ent.nesBrokerCommandId.get());

        } else {
            TAG.log(Level.WARNING, "NES Broker Command Processing Disabled !");
        }

        return new BrokerRequestResponse();
    }

    public BrokerRequestResponse processGenericCommand(GenericMeterEntity meter, GenericBrokerCommandEntity.Command command, Timestamp effectedDate) {

        if(propertiesConfig.getGeneric_broker_processing_enabled()) {

            GenericBrokerCommandEntity ent = new GenericBrokerCommandEntity();
            ent.setAsCreated();
            ent.meterId.set(meter.genericMeterId.get());
            ent.command.set(command.getCode());
            ent.createdDate.set(new Timestamp(System.currentTimeMillis()));
            ent.effectedDate.set(effectedDate);

            if(brokerServiceEnv.equalsIgnoreCase("prod")) {
                DataSourceDB.set(mdmsDataSource, ent);
            } else {
                // filter meters
                if (StringUtils.isEmpty(propertiesConfig.getGeneric_broker_filter_meters())
                        || Arrays.stream(propertiesConfig.getGeneric_broker_filter_meters().split(","))
                        .filter(f -> meter.meterSerialN.get().equals(f)).count() >= 1) {
                    DataSourceDB.set(mdmsDataSource, ent);
                }
            }

            return new BrokerRequestResponse(command == GenericBrokerCommandEntity.Command.DISCONNECT ? CUT : RELEASE, ent.genericBrokerCommandId.get());

        } else {
            TAG.log(Level.WARNING, "Generic Broker Command Processing Disabled !");
        }

        return new BrokerRequestResponse();
    }

    private synchronized String getAMIMeterSerialN(String nes_mtr_id, String kam_mtr_id, String generic_mtr_id) {

        String amiMeterQuery;
        String amiMeterSerialN;
        String meterId = nes_mtr_id != null ? nes_mtr_id : kam_mtr_id != null ? kam_mtr_id : generic_mtr_id;

        if (!meterSerialNumberCacheMap.containsKey(meterId)) {
            if( !StringUtils.isEmpty( nes_mtr_id ) ) {
                amiMeterQuery = String.format("SELECT SERIAL_N FROM NES_METER WHERE METER_ID = '%s'", nes_mtr_id);
            } else if( !StringUtils.isEmpty( kam_mtr_id ) ) {
                amiMeterQuery = String.format("SELECT SERIAL_N FROM KAMSTRUP_METER WHERE METER_ID = '%s'", kam_mtr_id);
            } else if( !StringUtils.isEmpty( generic_mtr_id ) ) {
                amiMeterQuery = String.format("SELECT METER_SERIAL_N AS SERIAL_N FROM GENERIC_METER WHERE GENERIC_METER_ID = '%s'", generic_mtr_id);
            } else {
                amiMeterQuery = String.format("SELECT SERIAL_N FROM NES_METER WHERE METER_ID = '%s'", nes_mtr_id);
            }
            amiMeterSerialN = DSDB.executeQuery(mdmsDataSource,String.class,amiMeterQuery);
            meterSerialNumberCacheMap.put(meterId, amiMeterSerialN);
        } else {
            amiMeterSerialN = meterSerialNumberCacheMap.get(meterId);
        }

        return StringUtils.isEmpty(amiMeterSerialN) ? "UNKNOWN" : amiMeterSerialN;
    }

    public IceMeter getIceMeter(String serialN) {
        List<IceMeter> iceMeterList = iceMeterCacheService.get(serialN);
        if(!CollectionUtils.isEmpty(iceMeterList)) {
            return iceMeterList.get(0);
        } else {
            return null;
        }
    }

    private void logBrokerQueryResponse(String ref, Integer command, Timestamp statusUpdateDate ) throws Exception {

        if( !StringUtils.isEmpty(ref) ) {

            KamstrupBrokerCommandEntity kBroker = DSDB.getFromSet(mdmsDataSource, new KamstrupBrokerCommandEntity().brokerCommandId.set(ref));

            NESBrokerCommandEntity nBroker = kBroker == null ?
                    DSDB.getFromSet(mdmsDataSource, new NESBrokerCommandEntity().nesBrokerCommandId.set(ref)) : null;

            GenericBrokerCommandEntity genericBrokerCommandEntity = kBroker == null && nBroker == null ?
                    DSDB.getFromSet(mdmsDataSource, new GenericBrokerCommandEntity().genericBrokerCommandId.set(ref)) : null;

            if (kBroker != null || nBroker != null || genericBrokerCommandEntity != null) {

                BrokerQueryResponse responseUpdate =
                        kBroker != null ?
                                new BrokerQueryResponse(ref, kBroker.status.getNonNull() < 4 ? PENDING : kBroker.status.getNonNull() == 6 ? COMPLETED : FAILED,
                                        kBroker.failedReason.get()) :

                                nBroker != null ?
                                        new BrokerQueryResponse(ref, nBroker.commandStatus.getNonNull() < 4 ? PENDING : nBroker.commandStatus.getNonNull() == 6 ? COMPLETED : FAILED,
                                                nBroker.error.get()) :

                                        new BrokerQueryResponse(ref, genericBrokerCommandEntity.status.getNonNull() < 4 ? PENDING : genericBrokerCommandEntity.status.getNonNull() == 6 ? COMPLETED : FAILED,
                                                genericBrokerCommandEntity.error.get());

                String serialN = this.getAMIMeterSerialN(
                        nBroker != null ? nBroker.meterId.get() : null,
                        kBroker != null ? kBroker.meterId.get() : null,
                        genericBrokerCommandEntity != null ? genericBrokerCommandEntity.meterId.get() : null);

                Assert.isTrue( !serialN.equalsIgnoreCase("unknown"),
                        String.format("MDMS could not find the meter serial number for meter_id: %s",
                                nBroker != null ? nBroker.meterId.get() :
                                kBroker != null ? kBroker.meterId.get() :
                                        genericBrokerCommandEntity.meterId.get() ) );

                IceMeter iceMeterEntity = this.getIceMeter(serialN);
                Assert.notNull(iceMeterEntity, String.format("MDMS could not find an ICE_METER entity for ICE_METER_NUMBER: %s from the ICE Utilities Database.", serialN));
                Assert.notNull(iceMeterEntity.iceMeterID.get(), String.format("ICE_METER_ID is null for ICE_METER_NUMBER: %s", serialN));

                Timestamp brokerCommandCreatedDate = nBroker != null ? nBroker.createdDate.get() :
                kBroker != null ? kBroker.createdDate.get() : genericBrokerCommandEntity != null ?
                                genericBrokerCommandEntity.createdDate.get() : Timestamp.valueOf(LocalDateTime.now());

                IceBrokerCommandStatusUpdateEntity newBrokerCommandStatusUpdateEntity =
                        IceBrokerCommandStatusUpdateEntity.create(
                        responseUpdate.getRef(),
                        iceMeterEntity.iceMeterID.get(),
                        statusUpdateDate,
                        responseUpdate.getIceBrokerCommandStatus(command),
                        responseUpdate.getIceBrokerCommandMessage(command),
                        brokerCommandCreatedDate,
                        Timestamp.valueOf(LocalDateTime.now()),
                        null);

                if(!newBrokerCommandStatusUpdateEntity.isSameStatus(mdmsDataSource)) {
                    newBrokerCommandStatusUpdateEntity.iceUpdated.set(IceBrokerCommandStatusUpdateEntity.ICEUpdated.NO.ordinal());
                    saveBrokerCommandStatusUpdate(newBrokerCommandStatusUpdateEntity);
                    TAG.info(String.format("Save IceBrokerCommandStatusUpdateEntity: %s",
                            newBrokerCommandStatusUpdateEntity.toString()));
                }

            }

        }

    }

    public void saveBrokerCommandStatusUpdate(IceBrokerCommandStatusUpdateEntity brokerCommandStatusUpdateEntity) {
        try(Connection connection = mdmsDataSource.getConnection()) {
            brokerCommandStatusUpdateEntity.save(connection);
        } catch(Exception ex) {
            TAG.severe(ex.getMessage());
        }
    }

    public void syncBrokerCommandStatusFromHES() {

        Timestamp timeThreshold = Timestamp.valueOf(LocalDateTime.now().minusHours(1));

        try {

            for( KamstrupBrokerCommandEntity brokerCommandEntity : KamstrupBrokerCommandEntity.getLatest(mdmsDataSource, timeThreshold) ) {

                this.logBrokerQueryResponse(
                        brokerCommandEntity.brokerCommandId.get(),
                        brokerCommandEntity.command.get(),
                        brokerCommandEntity.statusUpdateDate.get());
            }

        } catch(Exception ex) {
            TAG.severe(ex.getMessage());
        }

        try {

            for( NESBrokerCommandEntity brokerCommandEntity : NESBrokerCommandEntity.getLatest(mdmsDataSource, timeThreshold) ) {
                this.logBrokerQueryResponse(
                        brokerCommandEntity.nesBrokerCommandId.get(),
                        brokerCommandEntity.command.get(),
                        brokerCommandEntity.submitDate.get());
            }

        } catch(Exception ex) {
            TAG.severe(ex.getMessage());
        }

        try {

            for( GenericBrokerCommandEntity brokerCommandEntity : GenericBrokerCommandEntity.getLatest(mdmsDataSource, timeThreshold) ) {
                this.logBrokerQueryResponse(
                        brokerCommandEntity.genericBrokerCommandId.get(),
                        brokerCommandEntity.command.get(),
                        brokerCommandEntity.statusUpdateDate.get());
            }

        } catch(Exception ex) {
            TAG.severe(ex.getMessage());
        }

    }

}
