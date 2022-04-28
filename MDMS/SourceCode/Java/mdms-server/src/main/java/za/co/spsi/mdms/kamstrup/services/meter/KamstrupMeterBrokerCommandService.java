package za.co.spsi.mdms.kamstrup.services.meter;

import za.co.spsi.mdms.common.MdmsConstants;
import za.co.spsi.mdms.common.properties.config.PropertiesConfig;
import za.co.spsi.mdms.common.services.MeterDataService;
import za.co.spsi.mdms.io.kamstrup.RestHelper;
import za.co.spsi.mdms.kamstrup.db.KamstrupBrokerCommandEntity;
import za.co.spsi.mdms.kamstrup.db.KamstrupMeterEntity;
import za.co.spsi.mdms.kamstrup.services.meter.domain.Meter;
import za.co.spsi.mdms.kamstrup.services.meter.domain.Meters;
import za.co.spsi.mdms.kamstrup.services.order.domain.*;
import za.co.spsi.mdms.kamstrup.services.order.domain.commands.BreakerCommand;
import za.co.spsi.mdms.util.kamstrup.KamstrupRestException;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.ee.properties.ConfValue;
import za.co.spsi.toolkit.service.ProcessorService;
import za.co.spsi.toolkit.util.Processor;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static za.co.spsi.mdms.common.MdmsConstants.Status;
import static za.co.spsi.mdms.kamstrup.services.order.domain.OrderDetailCommand.Priority.High;
import static za.co.spsi.mdms.kamstrup.services.order.domain.commands.BreakerCommand.Cmd.*;

/**
 * Created by johan on 2016/11/17.
 */
@Singleton
@Startup
@DependsOn({"PropertiesConfig"})
@TransactionManagement(value = TransactionManagementType.BEAN)
public class KamstrupMeterBrokerCommandService extends ProcessorService {

    @Inject
    private PropertiesConfig propertiesConfig;

    @Resource(mappedName = "java:/jdbc/mdms")
    private javax.sql.DataSource dataSource;

    private Processor processor = getProcessor();

    @Inject
    RestHelper restHelper;

    private static final String[] BROKER_COMMANDS = {read.name(), cutoff.name(), release.name(), connect.name()};

    private static final long TIME_OUT_IN_MILLIS = 1 * 60 * 1000;

    public static final Logger TAG = Logger.getLogger(KamstrupMeterBrokerCommandService.class.getName());

    public static OrderDetailCommand createNewBrokerOrder(String commandString, String ... meterIDURL) {
        Meter[] meters = Arrays.stream(meterIDURL).map(s -> new Meter(s))
                .collect(Collectors.toCollection(ArrayList::new)).toArray(new Meter[]{});
        return new OrderDetailCommand(High,new Subjects(new Meters(meters)),
                        new Commands(new BreakerCommand[]{new BreakerCommand(commandString)}));
    }

    private List<KamstrupBrokerCommandEntity> getScheduled() {
        return KamstrupBrokerCommandEntity.getByStatus(dataSource, Status.PROCESSING);

    }

    private List<KamstrupBrokerCommandEntity> getSubmitted() {
        return KamstrupBrokerCommandEntity.getByStatus(dataSource, Status.SUBMITED);

    }

    private List<KamstrupBrokerCommandEntity> getwaiting() {
        return KamstrupBrokerCommandEntity.getByStatus(dataSource, Status.WAITING);

    }

    public void processScheduled() {
        if (!propertiesConfig.getKamstrup_broker_processing_enabled()) return;
        KamstrupBrokerCommandEntity.scheduleCreated(dataSource);
        List<KamstrupBrokerCommandEntity> brokerCommandsByStatus = getScheduled();
        for (KamstrupBrokerCommandEntity currentEntity : brokerCommandsByStatus) {
            DataSourceDB.executeInTx(dataSource, true, currentEntity, connection -> {
                String commandString = BROKER_COMMANDS[currentEntity.command.get()];
                String meterIDURL = currentEntity.meterIDURL.get();
                OrderDetailCommand commandObject = createNewBrokerOrder(commandString, meterIDURL);

                Response response = restHelper.restPost(commandObject, "/orders/");
                if (response.getStatus() == 201) {
                    String createdOrderPath = response.getHeaderString("Location");
                    currentEntity.orderURL.set(createdOrderPath);
                    currentEntity.setStatus(MdmsConstants.Status.SUBMITED);
                    DataSourceDB.set(connection, currentEntity);
                } else {
                    throw new KamstrupRestException("Failed to create breaker command",response);
                }
            }, callback -> currentEntity.setStatus(Status.FAILED_WITH_REASON));

        }
    }

    public void processSubmitted() {
        if(!propertiesConfig.getKamstrup_broker_processing_enabled()) return;
        for (KamstrupBrokerCommandEntity currentEntity : getSubmitted()) {
            DataSourceDB.executeInTx(dataSource, true, currentEntity, connection -> {
                OrderDetail ord = restHelper.restGet(currentEntity.orderURL.get(), OrderDetail.class);
                OrderExecutions ordEx = restHelper.restGet(ord.executions.ref + "/", OrderExecutions.class);
                currentEntity.orderStatusURL.set(ordEx.orderExecution.refStatus);
                currentEntity.orderCompletedURL.set(ordEx.orderExecution.refCompleted);
                currentEntity.setStatus(Status.WAITING);
                DataSourceDB.set(connection, currentEntity);
            }, callback -> {
                currentEntity.setStatus(Status.ERROR);
            });
        }
    }

    public void processWaiting() {
        if(!propertiesConfig.getKamstrup_broker_processing_enabled()) return;
        List<KamstrupBrokerCommandEntity> brokerCommandsByStatus = getwaiting();
        for (KamstrupBrokerCommandEntity currentEntity : brokerCommandsByStatus) {
            DataSourceDB.executeInTx(dataSource, true, currentEntity, connection -> {
                OrderExecutionStatus ordStatus = restHelper.restGet(currentEntity.orderStatusURL.get(), OrderExecutionStatus.class);
                if (ordStatus.isCompleted()) {
                    if (ordStatus.hasFailed()) {
                        currentEntity.failedWithReason(restHelper);
                    } else {
                        //DataSourceDB.executeInTx(dataSource, );
                        KamstrupMeterEntity kamMeter = currentEntity.meterRef.getOne(dataSource, null);
                        currentEntity.setStatus(Status.SUCCESSFUL);
                        if (currentEntity.command.get().equals(KamstrupBrokerCommandEntity.Command.CONNECT.getCode()) || currentEntity.command.get().equals(KamstrupBrokerCommandEntity.Command.RELEASE.getCode())) {
                            kamMeter.statusOn.set(true);
                        } else if (currentEntity.command.get().equals(KamstrupBrokerCommandEntity.Command.CUT.getCode())) {
                            kamMeter.statusOn.set(false);
                        }
                        DataSourceDB.set(connection, kamMeter);
                    }
                    DataSourceDB.set(connection, currentEntity);
                }
            }, callback -> currentEntity.setStatus(Status.ERROR));
        }
    }

    private List<KamstrupBrokerCommandEntity> gettimedOut() {
        return KamstrupBrokerCommandEntity.getTimedOutCommands(dataSource, TIME_OUT_IN_MILLIS);
    }

    public void processTimedOut() {
        if(!propertiesConfig.getKamstrup_broker_processing_enabled()) return;
        List<KamstrupBrokerCommandEntity> timedOutBrokerCommands = gettimedOut();
        for (KamstrupBrokerCommandEntity currentTimedOutEntity : timedOutBrokerCommands) {
            int timedOutCount = currentTimedOutEntity.commandTimeOutCount.get();
            if (timedOutCount >= 3) {
                currentTimedOutEntity.setStatus(Status.FAILED_TIME_OUT);
            } else {
                //Set timed out command back to created so that it follows cycle again
                currentTimedOutEntity.setStatus(Status.CREATED);
                timedOutCount++;
                currentTimedOutEntity.commandTimeOutCount.set(timedOutCount);
            }
            DataSourceDB.set(dataSource, currentTimedOutEntity);
        }

    }

    @PostConstruct
    public void startServices() {
        processor.delay(5).seconds(5).repeat(() -> {
            processScheduled();
            processSubmitted();
            processWaiting();
            processTimedOut();
        });
    }

}
