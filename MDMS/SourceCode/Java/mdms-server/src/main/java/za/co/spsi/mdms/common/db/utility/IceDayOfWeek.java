package za.co.spsi.mdms.common.db.utility;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.entity.Field;

/**
 * Created by jaspervdbijl on 2017/03/29.
 */
public class IceDayOfWeek extends EntityDB {

    @Column(name = "VALUE")
    public Field<String> value = new Field<>(this);

    @Column(name = "NAME")
    public Field<String> name = new Field<>(this);

    @Column(name = "ISACTIVE")
    public Field<Character> isActive = new Field<>(this);

    public IceDayOfWeek() {
        super("ICE_DAYOFWEEK");
    }
}
