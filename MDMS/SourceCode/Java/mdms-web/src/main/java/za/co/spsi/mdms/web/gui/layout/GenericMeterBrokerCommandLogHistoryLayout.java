package za.co.spsi.mdms.web.gui.layout;

import za.co.spsi.locale.annotation.MdmsLocaleId;
import za.co.spsi.mdms.generic.meter.db.GenericBrokerCommandLogEntity;
import za.co.spsi.mdms.kamstrup.db.KamstrupBrokerCommandEntity;
import za.co.spsi.toolkit.ano.UIField;
import za.co.spsi.toolkit.crud.gui.*;
import za.co.spsi.toolkit.crud.gui.ano.EntityRef;
import za.co.spsi.toolkit.crud.gui.ano.UIGroup;
import za.co.spsi.toolkit.crud.gui.fields.TextAreaField;

import javax.annotation.Resource;
import javax.sql.DataSource;

public class GenericMeterBrokerCommandLogHistoryLayout extends Layout<KamstrupBrokerCommandEntity> {
    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @EntityRef(main = true)
    private GenericBrokerCommandLogEntity brokerCommandHistory = new GenericBrokerCommandLogEntity();

    @UIGroup(column = 0)
    public Group detail = new Group(MdmsLocaleId.BROKER_COMMAND_LOG_HISTORY_DETAIL, this);

    @UIField(enabled = false)
    public LField createdDate = new LField(brokerCommandHistory.createdDate, MdmsLocaleId.CREATED_DATE, this);

    @UIField(enabled = false, rows = 5)
    public TextAreaField error = new TextAreaField(brokerCommandHistory.error, MdmsLocaleId.ERROR, this);

    public Group nameGroup = new Group("", this, createdDate, error).setNameGroup();

    public Pane detailPane = new Pane("", this, detail);

    public GenericMeterBrokerCommandLogHistoryLayout() {
        super("Broker Command History");
        setPermission(new Permission(0));
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public String getMainSql() {
        return "select * from GENERIC_BROKER_COMMAND_LOG where 1 = 1";
    }
}
