package za.co.spsi.mdms.common.db.generator;

import za.co.spsi.mdms.common.generator.WinSmsHelper;
import za.co.spsi.mdms.common.generator.msg.GeneratorMessage;
import za.co.spsi.mdms.common.generator.msg.MessageFactory;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.db.fields.FieldError;
import za.co.spsi.toolkit.db.fields.FieldTimestamp;
import za.co.spsi.toolkit.entity.Field;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static za.co.spsi.mdms.common.db.generator.CommunicationLogEntity.Status.Failed;

/**
 * Created by jaspervdbijl on 2017/07/26.
 */
@Table(version = 0)
public class CommunicationLogEntity extends EntityDB {

    public static final Logger TAG = Logger.getLogger(CommunicationLogEntity.class.getName());

    public enum Status {
        Received(0),Processed(1),Failed(2),NoProcessorFound(3),;

        public int code;
        Status(int code) {
            this.code = code;
        }
    }

    @Id(uuid = true)
    public Field<String> id = new Field<>(this);

    public Field<String> sc = new Field<>(this);
    public Field<String> cli = new Field<>(this);

    public Field<String> data = new Field<>(this);

    @Column(name = "date_time")
    public Field<Timestamp> dateTime = new Field<Timestamp>(this);
    public Field<Timestamp> received = new Field<Timestamp>(this);
    public Field<Timestamp> sent = new Field<Timestamp>(this);

    @Column()
    public Field<Character> direction = new Field<>(this);

    @Column(defaultValue = "0")
    public Field<Integer> status = new Field<>(this);

    @Column(size = 3096)
    public FieldError error = new FieldError(this);

    @Column(size = 1024)
    public Field<String> message = new Field<>(this);

    @Column(size = 1024, name = "original_data")
    public Field<String> originalData = new Field<>(this);

    public FieldTimestamp created = new FieldTimestamp(this);

    @Column(name = "gen_id", size = 50)
    @ForeignKey(table= GeneratorEntity.class, onDeleteAction = ForeignKey.Action.Cascade)
    public Field<String> generatorId= new Field<>(this);

    public CommunicationLogEntity() {
        super("gen_comms_log");
    }

    public CommunicationLogEntity(WinSmsHelper.Sms sms) {
        this();
        this.sc.set(sms.getSc());
        this.cli.set(sms.getCli());
        this.direction.set('I');
        this.received.set(sms.getReceived() != null?new Timestamp(sms.getReceived().getTime()):null);
        this.status.set(CommunicationLogEntity.Status.Received.code);
        this.data.set(sms.getMessage());
        this.originalData.set(sms.getOriginalData());
        // set the date and time form the message
        try {
            Optional<GeneratorMessage> msg = MessageFactory.getMessage(sms.getMessage());
            this.dateTime.set(msg.isPresent() ? msg.get().getReceiveTime(this.received.get()) : this.received.get());
        } catch (Exception ex) {
            this.status.set(Failed.code);
            TAG.log(Level.WARNING,ex.getMessage(),ex);
        }
    }

    public static CommunicationLogEntity send(DataSource dataSource, GeneratorEntity generatorEntity, WinSmsHelper.Sms sms) {
        CommunicationLogEntity log = new CommunicationLogEntity(sms);
        log.direction.set('O');
        log.generatorId.set(generatorEntity.id.get());
        DataSourceDB.set(dataSource,log);
        return log;
    }

    public static List<CommunicationLogEntity> getReceived(DataSource dataSource) {
        return new DataSourceDB<>(CommunicationLogEntity.class).getAllAsList(dataSource,
                "select * from gen_comms_log where status = 0 and direction = 'I' order by date_time asc");
    }

    public static List<CommunicationLogEntity> getSend(DataSource dataSource) {
        return new DataSourceDB<>(CommunicationLogEntity.class).getAllAsList(dataSource,
                "select * from gen_comms_log where status = 0 and direction = 'O' order by created asc");
    }

    public static void main(String args[]) throws Exception {
        List<WinSmsHelper.Sms> sms = WinSmsHelper.getSms("CLI=27715563783;DateReceived=201708251604;Message=25/08/2017-16:07:38  Sig=100%  AT=0  FW=7.47A  SN=170101-30183  PWR=On  BT=14.3V  IN=0  OUT=0  ;SentMessageID=729");
        System.out.println(new CommunicationLogEntity(sms.get(0)));

        Optional<GeneratorMessage> msg = MessageFactory.getMessage(sms.get(0).getMessage());
//        this.dateTime.set(msg.isPresent()?msg.get().getReceiveTime(this.received.get()):this.received.get());

    }
}
