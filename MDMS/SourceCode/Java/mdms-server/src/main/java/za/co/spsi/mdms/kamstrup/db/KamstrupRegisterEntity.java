package za.co.spsi.mdms.kamstrup.db;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.entity.ano.Audit;

/**
 * Created by jaspervdb on 2016/10/12.
 * All groups will schedule every 6 hours
 */
@Table(version = 0)
@Audit(services = false)
public class KamstrupRegisterEntity extends EntityDB {

    @Id(uuid = true)
    @Column(name = "REGISTER_ID", size = 36)
    public Field<String> registerId = new Field<>(this);

    @Column(name = "GROUP_ID")
    @ForeignKey(table = KamstrupGroupEntity.class, onDeleteAction = ForeignKey.Action.Cascade)
    public Field<String> groupId = new Field<>(this);

    @Column(name = "REG_ID")
    public Field<String> regId = new Field<>(this);

    @Column(name = "DESCRIPTION")
    public Field<String> description = new Field<>(this);

    public KamstrupRegisterEntity() {
        super("KAMSTRUP_REGSTER");
    }


}
