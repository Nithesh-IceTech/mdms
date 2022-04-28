package za.co.spsi.mdms.kamstrup.db;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.entity.Field;

/**
 * Created by johan on 2017/01/06.
 */

@Table(version = 0)
public class KamstrupReadingCommandRegistersEntity extends EntityDB {

    @Id(uuid = true)
    @Column(name = "READING_COMMAND_REGISTER_ID")
    public Field<String> readingCommandRegisterId = new Field<>(this);

    public KamstrupReadingCommandRegistersEntity() {
        super("KAM_READING_COMMAND_REGISTERS");
    }

    @Column(name = "REGISTER_ID")
    public Field<String> registerId = new Field<>(this);

    @ForeignKey(table = KamstrupReadingCommandEntity.class,name="KAM_READ_COMMAND_REGISTER_FK",onDeleteAction = ForeignKey.Action.Cascade)
    @Column(name = "REG_READING_COMMAND_ID", size = 50)
    public Field<String> kamMeterId = new Field<>(this);

}
