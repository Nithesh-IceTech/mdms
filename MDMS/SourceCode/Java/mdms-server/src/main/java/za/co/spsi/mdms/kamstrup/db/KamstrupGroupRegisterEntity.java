package za.co.spsi.mdms.kamstrup.db;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.EntityRef;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.entity.Field;

/**
 * Created by jaspervdb on 2016/10/12.
 */
@Table(version = 1)
public class KamstrupGroupRegisterEntity extends EntityDB {

    @Id(uuid = true)
    @Column(name = "GROUP_REGISTER_ID")
    public Field<String> groupRegisterId = new Field<>(this);

    @Column(name = "GROUP_ID")
    @ForeignKey(table =  KamstrupGroupEntity.class, name = "KGR_GROUP_ID_FK", onDeleteAction = ForeignKey.Action.Cascade)
    public Field<String> groupId = new Field<>(this);

    public Field<String> description = new Field<>(this);
    @Column(name = "REGISTER_ID")
    public Field<String> registerId = new Field<>(this);

    public EntityRef<KamstrupGroupEntity> group = new EntityRef<KamstrupGroupEntity>(groupId,this);

    public KamstrupGroupRegisterEntity() {
        super("KAMSTRUP_GROUP_REGISTER");
    }


}
