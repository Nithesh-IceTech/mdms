package za.co.spsi.mdms.common.db.utility;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.entity.Field;

/**
 * Created by jaspervdbijl on 2017/03/29.
 */
public class IceMeterReadingRoute extends EntityDB {

    @Column(name = "ICE_METER_READING_ROUTE_ID")
    public Field<Integer> meterReadingRouteId = new Field<>(this);

    public Field<Integer> name = new Field<>(this);

    public IceMeterReadingRoute() {
        super("ICE_METERREADINGROUTE");
    }
}
