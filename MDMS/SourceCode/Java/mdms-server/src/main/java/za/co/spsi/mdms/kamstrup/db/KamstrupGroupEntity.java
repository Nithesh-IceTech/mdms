package za.co.spsi.mdms.kamstrup.db;

import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.EntityRef;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.db.drivers.Driver;
import za.co.spsi.toolkit.db.drivers.DriverFactory;
import za.co.spsi.toolkit.db.fields.FieldError;
import za.co.spsi.toolkit.db.fields.FieldTimestamp;
import za.co.spsi.toolkit.db.fields.Index;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.entity.ano.Audit;
import za.co.spsi.toolkit.util.StringList;

import javax.sql.DataSource;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.Timestamp;
import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by jaspervdb on 2016/10/12.
 * All groups will schedule every 6 hours
 */
@Table(version = 0)
@Audit(services = false)
public class KamstrupGroupEntity extends EntityDB {

    public static final Logger TAG = Logger.getLogger(KamstrupGroupEntity.class.getName());

    public enum FrequencyType {
        MINUTE,HOUR,DAY,MONTH;
    }

    public static String[] FREQUENCY_TYPE_OPTION = Arrays.stream(FrequencyType.values()).map(f -> f.name()).
            collect(Collectors.toCollection(ArrayList::new)).toArray(new String[]{});

    @Id(uuid = true)
    @Column(name = "GROUP_ID", size = 36)
    public Field<String> groupId = new Field<>(this);

    @Column(name = "NAME")
    public Field<String> name = new Field<>(this);

    @Column(name = "REF")
    public Field<String> ref = new Field<>(this);

    @Column(name = "LOGGER_ID")
    public Field<String> loggerId = new Field<>(this);

    @Column(name = "FREQUENCY_TYPE",notNull = true)
    public Field<String> frequencyType = new Field<>(this);

    @Column(name = "FREQUENCY", notNull = true)
    public Field<Short> frequency = new Field<>(this);

    @Column(name = "DESCRIPTION")
    public Field<String> description = new Field<>(this);

    @Column(name = "ENABLED", defaultValue = "0")
    public Field<Boolean> enabled = new Field<>(this);

    @Column(name = "LATEST_ORDER_ID")
    @ForeignKey(table = KamstrupMeterOrderEntity.class, onDeleteAction = ForeignKey.Action.SetNull,name = "KG_LATEST_ORDER_ID_FK")
    public Field<String> lastOrderId = new Field<>(this);

    public EntityRef<KamstrupGroupRegisterEntity> registries = new EntityRef<>(this);

    public EntityRef<KamstrupMeterOrderEntity> lastOrder = new EntityRef<>(lastOrderId,this);

    public Index idxName = new Index("IDX_KG_NAME",this,name).setUnique();

    public Index idxRef = new Index("IDX_KG_REF",this,ref).setUnique();

    public KamstrupGroupEntity() {
        super("KAMSTRUP_GROUP");
    }

    public static Timestamp[] scheduleFrom(FrequencyType type,int frequency,Date lastTo) {
        LocalDateTime from,to;
        // TODO IED-4558: Requested by PEC to change to 2 days ago order backlog
        Boolean isOld = isOldMeterOrder(lastTo != null ? new Timestamp(lastTo.getTime()) : Timestamp.from(Instant.now()) );
        switch (type) {
            case MINUTE: {
                if(lastTo == null || isOld) {
                    from = LocalDateTime.now(Clock.systemUTC()).withMinute(0).withSecond(0).minusDays(2);
                } else {
                    from = new Timestamp(lastTo.getTime()).toLocalDateTime().withMinute(0).withSecond(0);
                }
                to = from.plusMinutes(frequency);
                break;
            }
            case HOUR  : {
                if(lastTo == null || isOld) {
                    from = LocalDateTime.now(Clock.systemUTC()).withMinute(0).withSecond(0).minusDays(2);
                } else {
                    from = new Timestamp(lastTo.getTime()).toLocalDateTime().withMinute(0).withSecond(0);
                }
                to = from.plusHours(frequency);
                break;
            }
            case DAY  : {
                if(lastTo == null || isOld) {
                    from = LocalDateTime.now(Clock.systemUTC()).withHour(0).withMinute(0).withSecond(0).minusDays(2);
                } else {
                    from = new Timestamp(lastTo.getTime()).toLocalDateTime().withHour(0).withMinute(0).withSecond(0);
                }
                to = from.plusDays(frequency);
                break;
            }
            case MONTH: {
                if(lastTo == null) {
                    from = LocalDateTime.now(Clock.systemUTC()).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).minusMonths(2);
                } else {
                    from = new Timestamp(lastTo.getTime()).toLocalDateTime().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
                }
                to = from.plusMonths(frequency);
                break;
            }
            default: throw new UnsupportedOperationException("Unsupported type " + type);
        }

        return new Timestamp[]{Timestamp.valueOf(from),Timestamp.valueOf(to)};
    }

    public Timestamp[] scheduleTime(DataSource dataSource) {
        KamstrupMeterOrderEntity order = lastOrder.getOne(dataSource);
        return scheduleFrom(FrequencyType.valueOf(frequencyType.get()),frequency.get(),order != null?order.toDate.get():null);
    }

    public static boolean shouldSchedule(LocalDateTime currentTime,Timestamp lastRun,String type,int frequency) {
        if (lastRun != null) {
            LocalDateTime then = lastRun.toLocalDateTime();
            LocalDateTime currentTimeUTC = currentTime.minusHours(2); // Timezone offset (UTC)
            LocalDateTime currentTimeWithDelay = currentTimeUTC.minusHours(4); // Delay order with 4 hours to allow UtiliDriver to autocollect data from meters.
            switch (type) {
                case "MINUTE" : then = then.plusMinutes(frequency);break;
                case "HOUR"   : then = then.plusHours(frequency);break;
                case "DAY"    : then = then.plusDays(frequency);break;
                case "MONTH"  : then = then.plusMonths(frequency);break;
            }
            return currentTimeWithDelay.compareTo(then) > 0;
        } else {
            return true;
        }
    }

    public static boolean isOldMeterOrder(Timestamp lastRun) {

        // TODO IED-4558: Requested by PEC to change to 2 days ago order backlog
        LocalDateTime twoDaysAgo = LocalDateTime.now()
                .minusDays(2)
                .withHour(0)
                .withMinute(0)
                .withSecond(0);

        return lastRun.toLocalDateTime().isBefore(twoDaysAgo);
    }

    public static List<KamstrupGroupEntity> getReadyGroups(DataSource dataSource, int minDelay) {

        List<KamstrupGroupEntity> kamstrupGroupList = new ArrayList<>();

        String query = String.format("select * from kamstrup_group where enabled = %s",
                DriverFactory.getDriver().boolToNumber(true));

        try {
            try (Connection connection = dataSource.getConnection()) {
                kamstrupGroupList.addAll(new DataSourceDB<>(KamstrupGroupEntity.class)
                        .getAllAsList(connection, query)
                );
            }
        } catch (Exception ex) {
            TAG.info(String.format("%s", ex.getMessage()));
        }

        return kamstrupGroupList;
    }

    @Table(version = 1)
    public static class GroupMeter extends EntityDB {

        @Id(uuid = true)
        @Column(name = "GROUP_METER_ID", size = 36)
        public Field<String> groupMeterId = new Field<>(this);

        @Column(name = "GROUP_ID")
        @ForeignKey(table = KamstrupGroupEntity.class, onDeleteAction = ForeignKey.Action.Cascade,name = "KGM_GROUP_ID_FK")
        public Field<String> groupId = new Field<>(this);

        @Column(name = "METER_ID")
        @ForeignKey(table = KamstrupMeterEntity.class, onDeleteAction = ForeignKey.Action.Cascade,name = "KGM_METER_ID_FK")
        public Field<String> meterId = new Field<>(this);

        public EntityRef<KamstrupGroupEntity> group = new EntityRef<>(groupId,this);

        public GroupMeter() {
            super("KAMSTRUP_GROUP_METER");
        }

        public GroupMeter(String groupId,String meterId) {
            this();
            this.groupId.set(groupId);
            this.meterId.set(meterId);
        }
    }


    @Table(version = 1)
    public static class LogEntity extends EntityDB {

        @Id(uuid = true)
        @Column(name = "LOG_ID", size = 36)
        public Field<String> logId = new Field<>(this);

        @Column(name = "GROUP_ID")
        @ForeignKey(table = KamstrupGroupEntity.class, onDeleteAction = ForeignKey.Action.Cascade,name = "KGL_GROUP_ID_FK")
        public Field<String> groupId = new Field<>(this);

        @Column(name = "ORDER_ID")
        @ForeignKey(table = KamstrupMeterOrderEntity.class, onDeleteAction = ForeignKey.Action.Cascade,name = "KGL_ORDER_ID_FK")
        public Field<String> orderId = new Field<>(this);

        @Column(name = "STATUS")
        public Field<Integer> status = new Field<>(this);

        @Column(name = "ENTRY_TIME", size = 1)
        public FieldTimestamp entryTime = new FieldTimestamp(this);

        @Column(name = "FROM_DATE")
        public Field<Timestamp> fromDate = new Field<Timestamp>(this);

        @Column(name = "TO_DATE")
        public Field<Timestamp> toDate = new Field<Timestamp>(this);

        @Column(name = "ERROR", defaultValue = "0", size = 4000)
        public FieldError error = new FieldError(this);

        public LogEntity() {
            super("KAMSTRUP_GROUP_LOG");
        }

        public static LogEntity create(Connection connection, KamstrupGroupEntity group, KamstrupMeterOrderEntity order, Response response) {
            LogEntity log = new LogEntity();
            log.groupId.set(group.groupId.get());
            if (order != null) {
                log.orderId.set(order.meterOrderId.get());
                log.fromDate.set(order.fromDate.get());
                log.toDate.set(order.toDate.get());
            }
            log.status.set(response.getStatus());
            if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
                try {
                    // read the response
                    StringList values = new StringList("HEADERS");
                    for (String key : response.getStringHeaders().keySet()) {
                        values.add(String.format("%s: %s", key, response.getStringHeaders().get(key)));
                    }
                    values.addValue("").addValue("BODY");
                    if (response.getLength() > 0) {
                        values.addValue(response.readEntity(String.class));
                    }
                    log.error.set(values.toString("\n"));
                } catch (Exception ex) {
                    TAG.log(Level.WARNING,ex.getMessage(),ex);
                    log.error.set(ex);
                }
            }
            DataSourceDB.set(connection,log);
            return log;
        }

        public static LogEntity findBy(Connection connection,String groupId,String orderId) {
            LogEntity log = new LogEntity();
            log.groupId.set(groupId);
            log.orderId.set(orderId);
            return DataSourceDB.getFromSet(connection,log);
        }
    }


    public static void main(String args[]) throws Exception {

//        Timestamp lastRun = null;
//        int frequency = 3;
//        for (LocalDateTime run = LocalDateTime.now();run.compareTo(LocalDateTime.now().plusDays(2))< 1;run = run.plusHours(1)) {
//            if (shouldSchedule(run,lastRun,"HOUR",frequency)) {
//                Timestamp[] r = scheduleFrom(FrequencyType.HOUR,frequency,lastRun);
//                lastRun = r[1];
//                System.out.println("RUN " + run + " -> " + r[0] + " to " + r[1]);
//            }
//        }

        int frequency = 3;
        Timestamp lastRun = Timestamp.from( LocalDateTime.of(2020, 05 , 01, 0, 0).toInstant(ZoneOffset.ofHours(2)) );
        Timestamp[] r = scheduleFrom(FrequencyType.HOUR,frequency,lastRun);

    }

}
