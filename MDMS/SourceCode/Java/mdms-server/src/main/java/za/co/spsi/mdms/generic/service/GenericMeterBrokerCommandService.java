package za.co.spsi.mdms.generic.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import za.co.spsi.mdms.common.properties.config.PropertiesConfig;
import za.co.spsi.mdms.generic.broker.*;
import za.co.spsi.mdms.generic.io.RestHelper;
import za.co.spsi.mdms.generic.meter.db.*;
import za.co.spsi.toolkit.db.DSDB;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.ee.properties.ConfValue;
import za.co.spsi.toolkit.service.ProcessorService;
import za.co.spsi.toolkit.util.Processor;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import static za.co.spsi.mdms.common.MdmsConstants.Status;

@Singleton
@Startup
@DependsOn({"PropertiesConfig"})
@TransactionManagement(value = TransactionManagementType.BEAN)
public class GenericMeterBrokerCommandService extends ProcessorService {

    @Inject
    @ConfValue(value = "generic.broker.processing.url", folder = "server")
    private String processingUrl;

    @Resource(mappedName = "java:/jdbc/mdms")
    private javax.sql.DataSource dataSource;

    @Inject
    private PropertiesConfig propertiesConfig;

    private Processor processor = getProcessor();

    public static final Logger TAG = Logger.getLogger(GenericMeterBrokerCommandService.class.getName());

    private List<GenericBrokerCommandEntity> getScheduled() {
        return GenericBrokerCommandEntity.getByStatus(dataSource, Status.PROCESSING);
    }

    private List<GenericBrokerCommandEntity> getSubmitted() {
        return GenericBrokerCommandEntity.getByStatus(dataSource, Status.SUBMITED);
    }

    private String getMeterPlatformType(GenericMeterEntity genericMeterEntity) {

        DbToDbMapMeterEntity dbToDbMapMeterEntity =
                DSDB.getFromSet(dataSource, new DbToDbMapMeterEntity().meterId.set(genericMeterEntity.genericMeterId.get()));

        if (dbToDbMapMeterEntity == null) {
            throw new RuntimeException("dbToDbMapMeterEntity not found");
        }

        DbToDbMappingDetailEntity dbToDbMappingDetailEntity =
                DSDB.getFromSet(dataSource, new DbToDbMappingDetailEntity().dbToDbMappingDetailId.set(
                        dbToDbMapMeterEntity.dbToDbMappingDetailId.get()));

        if (dbToDbMappingDetailEntity == null) {
            throw new RuntimeException("dbToDbMappingDetailEntity not found");
        }

        DbToDbMappingEntity dbToDbMappingEntity =
                DSDB.getFromSet(dataSource, new DbToDbMappingEntity().dbToDbMappingId.set(dbToDbMappingDetailEntity.dbToDbMappingId.get()));

        if (dbToDbMappingEntity == null) {
            throw new RuntimeException("dbToDbMappingEntity not found");
        }

        return dbToDbMappingEntity.meterPlatformType.get().toLowerCase();
    }

    public void processScheduled() {
        if(!propertiesConfig.getGeneric_broker_processing_enabled()) return;
        GenericBrokerCommandEntity.scheduleCreated(dataSource);
        for (GenericBrokerCommandEntity genericBrokerCommandEntity : getScheduled()) {
            DataSourceDB.executeInTx(dataSource, true, genericBrokerCommandEntity, connection -> {

                // Get GenericMeterEntity to determine platformType
                GenericMeterEntity genericMeterEntity =
                        DSDB.getFromSet(dataSource, new GenericMeterEntity().genericMeterId.set(genericBrokerCommandEntity.meterId.get()));
                String platformType = getMeterPlatformType(genericMeterEntity);

                String uri = null;
                if (platformType.toLowerCase().equals("mbus")) {
                    uri = String.format(processingUrl + "/%s/water/control", platformType);
                } else {
                    uri = String.format(processingUrl + "/%s/energy/control", platformType);
                }

                // Setup broker command
                GenericBrokerBreaker genericBrokerBreaker = new GenericBrokerBreaker();
                genericBrokerBreaker.setPriority("best-effort");
                genericBrokerBreaker.setValue(
                        genericBrokerCommandEntity.command.get() == GenericBrokerCommandEntity.Command.DISCONNECT.getCode() ?
                                "open" : "close");

                genericBrokerBreaker.setOutputNumber("1");

                GenericBrokerResources genericBrokerResources = new GenericBrokerResources();
                genericBrokerResources.setBreakerCommand(genericBrokerBreaker);
                GenericBrokerPayload genericBrokerPayload = new GenericBrokerPayload();
                genericBrokerPayload.setMeterSerialNumber(genericMeterEntity.meterSerialN.getSerial());
                genericBrokerPayload.setResources(Arrays.asList(genericBrokerResources));

                // Send post command to Head system
                try {
                    RestHelper<GenericBrokerPayloadResponse> restClient = new RestHelper<>();
                    ResponseEntity<GenericBrokerPayloadResponse> responseEntity =
                            restClient.doPost(GenericBrokerPayloadResponse.class, uri, genericBrokerPayload, null);

                    if (responseEntity.getStatusCode() == HttpStatus.OK) {
                        genericBrokerCommandEntity.setStatus(Status.SUBMITED);
                        genericBrokerCommandEntity.ref.set(responseEntity.getBody().getRefID());
                        DataSourceDB.set(connection, genericBrokerCommandEntity);
                    } else {

                        Integer scheduledRetryCount = genericBrokerCommandEntity.scheduledRetryCount.getNonNull();

                        createGenericBrokerCommandLogEntity("Http status = " + responseEntity.getStatusCode(),
                                genericBrokerCommandEntity.genericBrokerCommandId.get(), connection);

                        // Try and submit records
                        if (scheduledRetryCount > 3) {
                            genericBrokerCommandEntity.setStatus(Status.FAILED_WITH_REASON);
                            genericBrokerCommandEntity.error.set("Schedule retry count exceeded");
                            DataSourceDB.set(connection, genericBrokerCommandEntity);
                        } else {
                            genericBrokerCommandEntity.scheduledRetryCount.set(scheduledRetryCount + 1);
                            DataSourceDB.set(connection, genericBrokerCommandEntity);
                        }
                    }
                } catch (Exception ex) {
                    Integer scheduledRetryCount = genericBrokerCommandEntity.scheduledRetryCount.getNonNull();
                    TAG.severe(ex.getMessage());

                    createGenericBrokerCommandLogEntity(ex.getMessage(),
                            genericBrokerCommandEntity.genericBrokerCommandId.get(), connection);


                    // Try and submit records
                    if (scheduledRetryCount > 3) {
                        genericBrokerCommandEntity.setStatus(Status.FAILED_WITH_REASON);
                        genericBrokerCommandEntity.error.set(ex);
                        DataSourceDB.set(connection, genericBrokerCommandEntity);
                    } else {
                        genericBrokerCommandEntity.scheduledRetryCount.set(scheduledRetryCount + 1);
                        DataSourceDB.set(connection, genericBrokerCommandEntity);
                    }
                }
            }, callback -> genericBrokerCommandEntity.setStatus(Status.FAILED_WITH_REASON));

        }
    }

    public void processSubmitted() {
        if(!propertiesConfig.getGeneric_broker_processing_enabled()) return;
        for (GenericBrokerCommandEntity genericBrokerCommandEntity : getSubmitted()) {
            DataSourceDB.executeInTx(dataSource, true, genericBrokerCommandEntity, connection -> {

                // Get GenericMeterEntity to determine platformType
                GenericMeterEntity genericMeterEntity =
                        DSDB.getFromSet(dataSource, new GenericMeterEntity().genericMeterId.set(genericBrokerCommandEntity.meterId.get()));

                String platformType = getMeterPlatformType(genericMeterEntity);
                String uri = null;

                if (platformType.toLowerCase().equals("mbus")) {
                    uri = String.format(processingUrl + "%s/water/control?refID=%s", platformType, genericBrokerCommandEntity.ref.get());
                } else {
                    uri = String.format(processingUrl + "%s/energy/control?refID=%s", platformType, genericBrokerCommandEntity.ref.get());
                }

                // Poll to see if command was executed
                RestHelper<GenericBrokerPollResponse> restClient = new RestHelper<>();
                try {
                    ResponseEntity<GenericBrokerPollResponse> responseEntity =
                            restClient.doGet(GenericBrokerPollResponse.class, uri, null);

                    if (responseEntity.getStatusCode() == HttpStatus.OK) {

                        if (responseEntity.getBody().getStatus().toLowerCase().equals("success")) {
                            genericBrokerCommandEntity.setStatus(Status.SUCCESSFUL);
                        }

                        if (responseEntity.getBody().getStatus().toLowerCase().equals("error") ||
                                responseEntity.getBody().getStatus().toLowerCase().equals("failure")) {
                            genericBrokerCommandEntity.setStatus(Status.FAILED_WITH_REASON);
                            genericBrokerCommandEntity.error.set(responseEntity.getBody().getMessage());
                        }

                        DataSourceDB.set(connection, genericBrokerCommandEntity);
                    } else {
                        Integer submitRetryCount = genericBrokerCommandEntity.submitRetryCount.getNonNull();

                        createGenericBrokerCommandLogEntity("Http status = " + responseEntity.getStatusCode(),
                                genericBrokerCommandEntity.genericBrokerCommandId.get(), connection);

                        // Try and submit records
                        if (submitRetryCount > 3) {
                            genericBrokerCommandEntity.setStatus(Status.FAILED_WITH_REASON);
                            genericBrokerCommandEntity.error.set("Submit retry count exceeded");
                            DataSourceDB.set(connection, genericBrokerCommandEntity);
                        } else {
                            genericBrokerCommandEntity.submitRetryCount.set(submitRetryCount + 1);
                            DataSourceDB.set(connection, genericBrokerCommandEntity);
                        }
                    }
                } catch (Exception ex) {
                    Integer submitRetryCount = genericBrokerCommandEntity.submitRetryCount.getNonNull();
                    TAG.severe(ex.getMessage());

                    createGenericBrokerCommandLogEntity(ex.getMessage(),
                            genericBrokerCommandEntity.genericBrokerCommandId.get(), connection);

                    // Try and submit records
                    if (submitRetryCount > 3) {
                        genericBrokerCommandEntity.setStatus(Status.FAILED_WITH_REASON);
                        genericBrokerCommandEntity.error.set(ex);
                        DataSourceDB.set(connection, genericBrokerCommandEntity);
                    } else {
                        genericBrokerCommandEntity.submitRetryCount.set(submitRetryCount + 1);
                        DataSourceDB.set(connection, genericBrokerCommandEntity);
                    }
                }
            }, callback -> {
                genericBrokerCommandEntity.setStatus(Status.ERROR);
            });
        }
    }

    private void createGenericBrokerCommandLogEntity(String error, String genericBrokerCommandId, Connection connection) {
        GenericBrokerCommandLogEntity genericBrokerCommandLogEntity = new GenericBrokerCommandLogEntity();
        genericBrokerCommandLogEntity.createdDate.set(new Timestamp(System.currentTimeMillis()));
        genericBrokerCommandLogEntity.genericBrokerCommandId.set(genericBrokerCommandId);
        genericBrokerCommandLogEntity.error.set(error);
        DataSourceDB.set(connection, genericBrokerCommandLogEntity);
    }


    @PostConstruct
    public void startServices() {
        processor.delay(5).seconds(10).repeat(() -> {
            processScheduled();
            processSubmitted();
        });
    }
}
