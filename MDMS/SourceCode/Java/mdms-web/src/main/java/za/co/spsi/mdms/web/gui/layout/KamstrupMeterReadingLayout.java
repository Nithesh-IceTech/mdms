package za.co.spsi.mdms.web.gui.layout;

import za.co.spsi.mdms.kamstrup.db.KamstrupMeterReadingEntity;
import za.co.spsi.toolkit.crud.gui.*;
import za.co.spsi.toolkit.crud.gui.ano.EntityRef;
import za.co.spsi.toolkit.crud.gui.ano.UIGroup;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.ano.Id;

import javax.annotation.Resource;
import javax.sql.DataSource;

/**
 * Created by jaspervdb on 2016/04/19.
 */

public class KamstrupMeterReadingLayout extends Layout<KamstrupMeterReadingEntity> {

    @Resource(mappedName= "java:/jdbc/mdms")
    private DataSource dataSource;

    @EntityRef(main = true)
    private KamstrupMeterReadingEntity meter = new KamstrupMeterReadingEntity();

    @UIGroup(column = 0)
    public Group detail = new Group("Meter Reading Detail",this);

    public LFields fields = new LFields(meter,this).addAllExcluding(Id.class, ForeignKey.class);

    public Group nameGroup = new Group(fields.toArray(new LField[]{}),"",this).setNameGroup();

    public Pane detailPane = new Pane("",this, detail);

    public KamstrupMeterReadingLayout() {
        super("Meter Reading Detail");
        setPermission(new Permission(0));
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public String getMainSql() {
        return "select * from KAMSTRUP_METER_READING where 1 = 1";
    }

}
