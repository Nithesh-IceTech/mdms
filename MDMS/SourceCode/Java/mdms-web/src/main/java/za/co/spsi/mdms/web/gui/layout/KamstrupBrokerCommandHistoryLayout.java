package za.co.spsi.mdms.web.gui.layout;

import za.co.spsi.locale.annotation.MdmsLocaleId;
import za.co.spsi.mdms.kamstrup.db.KamstrupBrokerCommandEntity;
import za.co.spsi.toolkit.ano.UIField;
import za.co.spsi.toolkit.crud.gui.*;
import za.co.spsi.toolkit.crud.gui.ano.EntityRef;
import za.co.spsi.toolkit.crud.gui.ano.UIGroup;
import za.co.spsi.toolkit.crud.gui.fields.ComboBoxField;
import za.co.spsi.toolkit.crud.gui.fields.TextAreaField;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * Created by johan on 2016/12/05.
 */
public class KamstrupBrokerCommandHistoryLayout extends Layout<KamstrupBrokerCommandEntity> {
    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @EntityRef(main = true)
    private KamstrupBrokerCommandEntity brokerCommandHistory = new KamstrupBrokerCommandEntity();

    @UIGroup(column = 0)
    public Group detail = new Group(MdmsLocaleId.BROKER_COMMAND_HISTORY_DETAIL,this);

    @UIField(enabled= false)
    //public LField command = new LField(brokerCommandHistory.command, "Command", this);
    public ComboBoxField<Integer> command = new ComboBoxField<Integer>(brokerCommandHistory.command,MdmsLocaleId.COMMAND, KamstrupBrokerCommandEntity.getCommandOptions(), KamstrupBrokerCommandEntity.getCommandValues(),this);

    @UIField(enabled= false)
    public ComboBoxField<Integer> status = new ComboBoxField<Integer>(brokerCommandHistory.status,MdmsLocaleId.PROCESSING_STATUS, KamstrupBrokerCommandEntity.getStatusOptions(), KamstrupBrokerCommandEntity.getStatusValues(),this);

    @UIField(enabled= false)
    public LField failReason = new LField(brokerCommandHistory.failedReason, MdmsLocaleId.FAIL_REASON, this);

    @UIField(enabled= false)
    public LField createdDate = new LField(brokerCommandHistory.createdDate, MdmsLocaleId.CREATED_DATE, this);

    @UIField(enabled= false)
    public LField statusUpdateDate = new LField(brokerCommandHistory.statusUpdateDate, MdmsLocaleId.STATUS_UPDATE_DATE, this);

    @UIField(enabled= false)
    public LField commandTimeOutCount = new LField(brokerCommandHistory.commandTimeOutCount, MdmsLocaleId.TIMEOUT, this);

    @UIField(enabled= false,uppercase = false)
    public LField orderURL = new LField(brokerCommandHistory.orderURL, MdmsLocaleId.ORDER_URL, this);

    @UIField(enabled= false,uppercase = false)
    public LField orderStatusURL = new LField(brokerCommandHistory.orderStatusURL, MdmsLocaleId.ORDER_STATUS_URL, this);

    @UIField(enabled= false,uppercase = false)
    public LField orderCompletedURL = new LField(brokerCommandHistory.orderCompletedURL, MdmsLocaleId.ORDER_COMPLETED_URL, this);

    @UIField(enabled = false,rows = 5)
    public TextAreaField error = new TextAreaField(brokerCommandHistory.error, MdmsLocaleId.ERROR,this);

    public Group nameGroup = new Group("",this,command,status,createdDate,statusUpdateDate, commandTimeOutCount).setNameGroup();

    public Pane detailPane = new Pane("",this, detail);

    public KamstrupBrokerCommandHistoryLayout() {
        super("Broker Command History");
        setPermission(new Permission(0));
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public String getMainSql() {
        return "select * from BROKER_COMMAND where 1 = 1";
    }
}
