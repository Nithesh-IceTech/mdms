package za.co.spsi.mdms.web.gui.layout;

import za.co.spsi.locale.annotation.MdmsLocaleId;
import za.co.spsi.mdms.generic.meter.db.GenericBrokerCommandEntity;
import za.co.spsi.mdms.kamstrup.db.KamstrupBrokerCommandEntity;
import za.co.spsi.toolkit.ano.UIField;
import za.co.spsi.toolkit.crud.gui.*;
import za.co.spsi.toolkit.crud.gui.ano.EntityRef;
import za.co.spsi.toolkit.crud.gui.ano.UIGroup;
import za.co.spsi.toolkit.crud.gui.fields.ComboBoxField;
import za.co.spsi.toolkit.crud.gui.fields.TextAreaField;

import javax.annotation.Resource;
import javax.sql.DataSource;

public class GenericMeterBrokerCommandHistoryLayout extends Layout<KamstrupBrokerCommandEntity> {
    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @EntityRef(main = true)
    private GenericBrokerCommandEntity brokerCommandHistory = new GenericBrokerCommandEntity();

    @UIGroup(column = 0)
    public Group detail = new Group(MdmsLocaleId.BROKER_COMMAND_HISTORY_DETAIL, this);

    @UIField(enabled = false)
    public ComboBoxField<Integer> command = new ComboBoxField<Integer>(brokerCommandHistory.command, MdmsLocaleId.COMMAND,
            GenericBrokerCommandEntity.getCommandOptions(), GenericBrokerCommandEntity.getCommandValues(), this);

    @UIField(enabled = false)
    public ComboBoxField<Integer> status = new ComboBoxField<Integer>(brokerCommandHistory.status, MdmsLocaleId.PROCESSING_STATUS, KamstrupBrokerCommandEntity.getStatusOptions(), KamstrupBrokerCommandEntity.getStatusValues(), this);

    @UIField(enabled = false)
    public LField failReason = new LField(brokerCommandHistory.failedReason, MdmsLocaleId.FAIL_REASON, this);

    @UIField(enabled = false)
    public LField createdDate = new LField(brokerCommandHistory.createdDate, MdmsLocaleId.CREATED_DATE, this);

    @UIField(enabled = false)
    public LField statusUpdateDate = new LField(brokerCommandHistory.statusUpdateDate, MdmsLocaleId.STATUS_UPDATE_DATE, this);

    @UIField(enabled = false, rows = 5)
    public TextAreaField error = new TextAreaField(brokerCommandHistory.error, MdmsLocaleId.ERROR, this);

    public Group nameGroup = new Group("", this, command, status, createdDate, statusUpdateDate).setNameGroup();

    public Pane detailPane = new Pane("", this, detail);

    public Pane brokerCommandLogHistoryPane = new Pane("Broker Log History",
            "select * from GENERIC_BROKER_COMMAND_LOG where GENERIC_BROKER_COMMAND_ID = ? order by created_date desc", GenericMeterBrokerCommandLogHistoryLayout.class,
            new Permission(0), this);


    public GenericMeterBrokerCommandHistoryLayout() {
        super("Broker Command History");
        setPermission(new Permission(0));
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public String getMainSql() {
        return "select * from GENERIC_BROKER_COMMAND where 1 = 1";
    }
}
