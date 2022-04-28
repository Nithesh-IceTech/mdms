package za.co.spsi.mdms.web.gui.layout;

import za.co.spsi.mdms.common.db.PrepaidMeterReadingBatch;
import za.co.spsi.toolkit.ano.UIField;
import za.co.spsi.toolkit.crud.gui.*;
import za.co.spsi.toolkit.crud.gui.ano.EntityRef;
import za.co.spsi.toolkit.crud.gui.ano.UIGroup;
import za.co.spsi.toolkit.crud.gui.fields.TextAreaField;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * Created by jaspervdb on 2016/04/19.
 */

public class PrepaidMeterReadingBatchLayout extends Layout {

    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @EntityRef(main = true)
    private PrepaidMeterReadingBatch meterReadingBatch = new PrepaidMeterReadingBatch();

    @UIGroup(column = 0)
    public Group detail = new Group("Prepaid Meter Reading Batch", this);

    @UIField(enabled = false)
    public LField createdDate = new LField(meterReadingBatch.createdDate,"Created Date",this);

    @UIField(enabled = false)
    public LField updateDate = new LField(meterReadingBatch.updateDate,"Updated Date",this);

    @UIField(enabled = false)
    public LField serialN = new LField(meterReadingBatch.serialN,"Meter Serial Number",this);

    @UIField(enabled = false)
    public LField utilStatusId = new LField(meterReadingBatch.utilStatusId,"ICE Status",this);

    @UIField(enabled = false, rows = 10)
    public TextAreaField utilData = new TextAreaField(meterReadingBatch.utilData,"Data Sent",this);

    @UIField(enabled = false, rows = 10)
    public TextAreaField error= new TextAreaField(meterReadingBatch.error,"Error",this);

    public Group nameGroup = new Group("", this, createdDate, updateDate, serialN, utilStatusId).setNameGroup();

    public Pane detailPane = new Pane("Details", this, detail);

    public Pane readingPane = new Pane("Readings",
            MeterReadingLayout.getSql() +
                    " where PREPAID_METER_READING_BATCH_ID = ? order by ENTRY_TIME asc", MeterReadingLayout.class,
            new Permission(0), this);

    public PrepaidMeterReadingBatchLayout() {
        super("Prepaid Meter Reading Batch");
        setPermission(new Permission(0));
        init();
    }

    private void init() {
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public String getMainSql() {
        return "select * from PREPAID_METER_READING_BATCH where 1 = 1 order by UPDATED_DATE desc";
    }

}
