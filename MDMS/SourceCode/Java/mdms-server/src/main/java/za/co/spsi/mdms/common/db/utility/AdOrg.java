package za.co.spsi.mdms.common.db.utility;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.entity.Field;

/**
 * Created by ettienne on 2017/06/08.
 */
public class AdOrg extends EntityDB {

    @Column(name = "AD_ORG_ID")
    public Field<Integer> adOrgId = new Field<>(this);

    @Column(name = "NAME")
    public Field<String> name = new Field<>(this);

    @Column(name = "ICE_COMPANYCODE")
    public Field<String> companyCode = new Field<>(this);

    public AdOrg() {
        super("AD_ORG");
    }
}
