package za.co.spsi.mdms.web.gui.layout.generator;

import za.co.spsi.mdms.common.db.generator.CommunicationLogEntity;
import za.co.spsi.toolkit.ano.UIField;
import za.co.spsi.toolkit.crud.gui.*;
import za.co.spsi.toolkit.crud.gui.ano.EntityRef;
import za.co.spsi.toolkit.crud.gui.ano.UIGroup;
import za.co.spsi.toolkit.crud.gui.fields.ComboBoxField;
import za.co.spsi.toolkit.crud.gui.fields.TextAreaField;

import javax.annotation.Resource;
import javax.sql.DataSource;

import static za.co.spsi.locale.annotation.MdmsLocaleId.*;
import static za.co.spsi.mdms.common.db.generator.CommunicationLogEntity.Status.*;

/**
 * Created by jaspervdb on 2016/04/19.
 */

public class CommunicationLogLayout extends Layout {

    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @EntityRef(main = true)
    private CommunicationLogEntity commsLog = new CommunicationLogEntity();

    @UIGroup(column = 0)
    public Group detailGroup = new Group("Detail", this);

    public LField cli = new LField<>(commsLog.cli,"Source Msisdn",this);
    public LField sc = new LField(commsLog.sc,"Destination Msisdn",this);

    public LField data = new LField(commsLog.data,"Data",this);
    public LField dateTime = new LField(commsLog.dateTime,"Timestamp",this);
    public LField received = new LField(commsLog.received,"Received",this);
    public LField sent = new LField(commsLog.sent,"Sent",this);

    public LField direction = new LField<>(commsLog.direction,"Direction",this);

    @UIGroup(column = 0)
    public Group statusGroup = new Group("Status", this);

    public ComboBoxField status = new ComboBoxField(commsLog.status,"Status",new String[]{RECEIVED,PROCESSED,FAILED},
            new String[]{Received.code+"",Processed.code+"",Failed.code+""},this);

    @UIField(rows = 5)
    public TextAreaField message = new TextAreaField(commsLog.message,"Message",this);

    @UIField(rows = 5)
    public TextAreaField error = new TextAreaField(commsLog.error,"Error",this);

    public Group nameGroup = new Group("", this,cli,sc,data,received,status).setNameGroup();

    public Pane detailPane = new Pane("Details", this, detailGroup,statusGroup);

    public CommunicationLogLayout() {
        super("Communication Logs");
        setPermission(new Permission(0));
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }


    @Override
    public String getMainSql() {
        return super.getMainSql() + " order by received desc";
    }
}
