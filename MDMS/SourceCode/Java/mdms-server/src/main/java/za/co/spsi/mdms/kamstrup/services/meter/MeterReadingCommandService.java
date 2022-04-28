package za.co.spsi.mdms.kamstrup.services.meter;

import za.co.spsi.mdms.common.properties.config.PropertiesConfig;
import za.co.spsi.mdms.io.kamstrup.RestHelper;
import za.co.spsi.mdms.kamstrup.db.KamstrupReadingCommandEntity;
import za.co.spsi.mdms.kamstrup.db.KamstrupReadingCommandRegistersEntity;
import za.co.spsi.mdms.kamstrup.services.meter.domain.Register;
import za.co.spsi.mdms.kamstrup.services.meter.domain.Registers;
import za.co.spsi.mdms.kamstrup.services.order.domain.Commands;
import za.co.spsi.mdms.kamstrup.services.order.domain.Group;
import za.co.spsi.mdms.kamstrup.services.order.domain.OrderDetailCommand;
import za.co.spsi.mdms.kamstrup.services.order.domain.Subjects;
import za.co.spsi.mdms.kamstrup.services.order.domain.commands.LoggerCommand;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.ee.properties.ConfValue;
import za.co.spsi.toolkit.service.ProcessorService;
import za.co.spsi.toolkit.util.Processor;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by johan on 2017/01/03.
 */

@Singleton
@Startup
@DependsOn({"PropertiesConfig"})
@TransactionManagement(value = TransactionManagementType.BEAN)
public class MeterReadingCommandService extends ProcessorService {

    @Inject
    private PropertiesConfig propertiesConfig;

    @Resource(mappedName = "java:/jdbc/mdms")
    private javax.sql.DataSource dataSource;

    @Inject
    RestHelper restHelper;

    Processor processor = getProcessor();

    private List<KamstrupReadingCommandEntity> getScheduled() {
        return KamstrupReadingCommandEntity.getByStatus(dataSource, KamstrupReadingCommandEntity.Status.PROCESSING);

    }

    private OrderDetailCommand createNewMeterReadingOrder(String loggerID, String groupRef, String[] registers) {
        OrderDetailCommand ord = new OrderDetailCommand();
        ord.priority = "High";

        ord.subjects = new Subjects();
        ord.subjects.group = new Group();
        ord.subjects.group.ref = groupRef;

        ord.commands = new Commands();
        ord.commands.loggerCommands = new LoggerCommand[1];

        ord.commands.loggerCommands[0] = new LoggerCommand();
        ord.commands.loggerCommands[0].action = "read";
        ord.commands.loggerCommands[0].logger = new LoggerCommand.Logger();
        ord.commands.loggerCommands[0].logger.id = loggerID;
        ord.commands.loggerCommands[0].logger.registers = new Registers();
        ord.commands.loggerCommands[0].logger.registers.registers = new Register[registers.length];
        for (int i = 0; i < registers.length; i++) {
            ord.commands.loggerCommands[0].logger.registers.registers[i] = new Register();
            ord.commands.loggerCommands[0].logger.registers.registers[i].id = registers[i];

        }

        //NB!! change time info to previous hour
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Timestamp time = new Timestamp(cal.getTimeInMillis());

        ord.commands.loggerCommands[0].logger.fromDate = time;

        cal.add(Calendar.DAY_OF_YEAR, 1);
        time = new Timestamp(cal.getTimeInMillis());
        ord.commands.loggerCommands[0].logger.toDate = time;

        return ord;

    }

    void addItemTOList(String item, ArrayList<String> list) {
        if (item == null)
            return;
        if (item.equals(""))
            return;
        list.add(item);
    }

    String[] getRegistersFromEntAsArray(KamstrupReadingCommandEntity entity) {
        ArrayList<String> list = new ArrayList<>();
        DataSourceDB<KamstrupReadingCommandRegistersEntity> registers = null;
        try (Connection conn = dataSource.getConnection()) {
            registers = entity.registers.get(conn);
            for (KamstrupReadingCommandRegistersEntity ent : registers) {
                list.add(ent.registerId.get());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list.toArray(new String[list.size()]);
    }

    public void processScheduled() {

        if (!propertiesConfig.getKamstrup_processing_enabled()) return;

        KamstrupReadingCommandEntity.scheduleCreated(dataSource);

        List<KamstrupReadingCommandEntity> brokerCommandsByStatus = getScheduled();

        for (KamstrupReadingCommandEntity currentEntity : brokerCommandsByStatus ) {

            OrderDetailCommand commandObject = createNewMeterReadingOrder(currentEntity.logId.get(), currentEntity.groupRef.get(), getRegistersFromEntAsArray(currentEntity));

            Response response = restHelper.restPost(commandObject,"/orders/");

            //TODO: do something if statys returned is not 201
            // Assert.assertTrue(response.getStatus() == 201);
            String createdOrderPath = response.getHeaderString("Location");

            //NB!! save location
            currentEntity.orderURL.set(createdOrderPath);
            currentEntity.status.set(KamstrupReadingCommandEntity.Status.SUBMITED.getCode());
            //currentEntity.setStatus(KamstrupBrokerCommandEntity.Status.SUBMITED);
            DataSourceDB.set(dataSource, currentEntity);
        }
    }

    @PostConstruct
    public void startServices() {
        processor.delay(5).minutes(1).repeat(() -> {
            processScheduled();
        });
    }

}
