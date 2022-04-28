package za.co.spsi.mdms.web.gui.layout.generator;

import za.co.spsi.locale.annotation.MdmsLocaleId;
import za.co.spsi.mdms.common.db.generator.CommunicationLogEntity;
import za.co.spsi.mdms.common.db.generator.GeneratorTransactionEntity;
import za.co.spsi.toolkit.ano.UIField;
import za.co.spsi.toolkit.crud.gui.*;
import za.co.spsi.toolkit.crud.gui.ano.EntityRef;
import za.co.spsi.toolkit.crud.gui.ano.UIGroup;
import za.co.spsi.toolkit.crud.gui.fields.ComboBoxField;
import za.co.spsi.toolkit.crud.gui.fields.LocalTimestampField;
import za.co.spsi.toolkit.crud.gui.fields.TextAreaField;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;

import javax.annotation.Resource;
import javax.sql.DataSource;

import static za.co.spsi.mdms.common.db.generator.GeneratorTransactionEntity.Status.Active;
import static za.co.spsi.mdms.common.db.generator.GeneratorTransactionEntity.Status.Cancelled;

/**
 * Created by jaspervdb on 2016/04/19.
 */

public class GeneratorTransactionLayout extends Layout {

    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @EntityRef(main = true)
    private GeneratorTransactionEntity tx = new GeneratorTransactionEntity();

//    @EntityRef()
    private CommunicationLogEntity startComms = new CommunicationLogEntity();

//    @EntityRef()
    private CommunicationLogEntity endComms = new CommunicationLogEntity();

    @UIGroup(column = 0)
    public Group detailGroup = new Group("Detail", this).setNameGroup();

    public LocalTimestampField start = new LocalTimestampField(tx.txStart,"Start",this);
    public LocalTimestampField end = new LocalTimestampField(tx.txEnd,"Ended",this);

    public ComboBoxField<Integer> status = new ComboBoxField<>(tx.status, MdmsLocaleId.PROCESSING_STATUS,
            new String[]{MdmsLocaleId.STATUS_ACTIVE, MdmsLocaleId.STATUS_CANCELLED},
            new String[]{Active.code+"", Cancelled.code+""}, this);

    @UIField(rows = 5)
    public TextAreaField cancelMessage = new TextAreaField(tx.cancelMessage,MdmsLocaleId.CANCEL_MESSAGE,this);

    @UIGroup(column = 0)
    public Group startCommsGroup = new Group("Start Comms", this);

    public LField startData = new LField(startComms.data,"Data",this);
    public LField startReceived = new LField(startComms.received,"Received",this);

    @UIGroup(column = 0)
    public Group endCommsGroup = new Group("End Comms", this);

    public LField endData = new LField(endComms.data,"Data",this);
    public LField endReceived = new LField(endComms.received,"Received",this);

    public Pane detailPane = new Pane("Details", this, detailGroup,startCommsGroup,endCommsGroup);

    public Pane linesPane = new Pane("Detail Lines",GeneratorTransactionDetailLayout.getSQL() + " where gen_tx_detail.gen_tx_id = ?",
            GeneratorTransactionDetailLayout.class,new Permission(0),this);


    public GeneratorTransactionLayout() {
        super("Generator Transactions");
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public void beforeOnScreenEvent() {
        super.beforeOnScreenEvent();
        DataSourceDB.loadFromId(dataSource,(EntityDB) startComms.id.set(tx.startCommsId.get()));
        if (tx.endCommsId.get() != null) {
            DataSourceDB.loadFromId(dataSource,(EntityDB) endComms.id.set(tx.endCommsId.get()));
        }
    }
}
