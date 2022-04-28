package za.co.spsi.mdms.common.db.utility;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.entity.Field;

public class IceCBpartner extends EntityDB {

    @Column(name = "C_BPARTNER_ID")
    public Field<Integer> CBPartnerId = new Field<>(this);

    @Column(name = "NAME")
    public Field<String> name = new Field<>(this);

    @Column(name = "VALUE")
    public Field<String> value = new Field<>(this);

    @Column(name = "AD_ORG_ID")
    public Field<Integer> adOrgId = new Field<>(this);

    public IceCBpartner() {
        super("C_BPARTNER");
    }
}
