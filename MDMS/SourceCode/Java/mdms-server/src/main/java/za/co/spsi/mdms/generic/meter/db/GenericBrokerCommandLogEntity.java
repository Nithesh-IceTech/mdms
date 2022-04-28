package za.co.spsi.mdms.generic.meter.db;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.db.fields.FieldError;
import za.co.spsi.toolkit.entity.Field;

import java.sql.Timestamp;
import java.util.logging.Logger;

@Table(version = 1)
public class GenericBrokerCommandLogEntity extends EntityDB {

    public static final Logger TAG = Logger.getLogger(GenericBrokerCommandLogEntity.class.getName());

    @Id(uuid = true)
    @Column(name = "GENERIC_BROKER_COMMAND_LOG_ID")
    public Field<String> genericBrokerCommandLogId = new Field<>(this);

    @Column(name = "GENERIC_BROKER_COMMAND_ID")
    @ForeignKey(table = GenericBrokerCommandEntity.class, onDeleteAction = ForeignKey.Action.Cascade)
    public Field<String> genericBrokerCommandId = new Field<>(this);

    @Column(name = "CREATED_DATE")
    public Field<Timestamp> createdDate = new Field<>(this);

    @Column(size = 1024)
    public FieldError error = new FieldError(this);

    public GenericBrokerCommandLogEntity() {
        super("GENERIC_BROKER_COMMAND_LOG");
    }

}
