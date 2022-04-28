package za.co.spsi.toolkit.crud.db.audit;

import za.co.spsi.toolkit.crud.db.fields.UserIdField;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.entity.Field;

/**
 * Created by jaspervdbijl on 2017/06/28.
 */
public class AuditEntityDB extends EntityDB {

    @Column(name = "AGENCY_ID")
    public Field<Integer> agencyId = new Field<>(this);

    @Column(name = "USER_ID")
    public UserIdField userId= new UserIdField(this);

    public AuditEntityDB(String name) {
        super(name);
    }
}
