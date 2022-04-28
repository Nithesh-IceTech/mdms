package za.co.spsi.mdms.common.db.utility;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.entity.Field;

/**
 * Created by jaspervdbijl on 2017/03/29.
 */
public class IceOUM extends EntityDB {

    @Column(name = "C_UOM_ID")
    public Field<String> oumId = new Field<>(this);

    @Column(name = "X12DE355")
    public Field<String> oumLVal = new Field<>(this);

    public IceOUM() {
        super("C_UOM");
    }
}
