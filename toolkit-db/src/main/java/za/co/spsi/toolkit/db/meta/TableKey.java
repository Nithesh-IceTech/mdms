package za.co.spsi.toolkit.db.meta;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.drivers.Driver;
import za.co.spsi.toolkit.db.upgrade.UpgradeHelper;
import za.co.spsi.toolkit.entity.Field;

/**
 * Created by jaspervdbijl on 2016/12/19.
 */
public class TableKey extends EntityDB {

    public Field<String> PKTABLE_CAT = new Field<>(this);
    public Field<String> PKTABLE_SCHEM = new Field<>(this);
    public Field<String> PKTABLE_NAME = new Field<>(this);
    public Field<String> PKCOLUMN_NAME = new Field<>(this);
    public Field<String> FKTABLE_CAT = new Field<>(this);
    public Field<String> FKTABLE_SCHEM = new Field<>(this);
    public Field<String> FKTABLE_NAME = new Field<>(this);
    public Field<String> FKCOLUMN_NAME = new Field<>(this);
    public Field<String> KEY_SEQ = new Field<>(this);
    public Field<String> UPDATE_RULE = new Field<>(this);
    public Field<String> DELETE_RULE = new Field<>(this);
    public Field<String> FK_NAME = new Field<>(this);
    public Field<String> PK_NAME = new Field<>(this);
    public Field<String> DEFERRABILITY = new Field<>(this);

    public TableKey() {
        super("");
    }

}
