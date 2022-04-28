package za.co.spsi.mdms.kamstrup.db;

import lombok.SneakyThrows;
import za.co.spsi.mdms.kamstrup.services.PagingService;
import za.co.spsi.mdms.kamstrup.services.order.OrderDao;
import za.co.spsi.mdms.kamstrup.services.order.domain.OrderDetail;
import za.co.spsi.mdms.kamstrup.services.order.domain.OrderExecutionStatus;
import za.co.spsi.mdms.util.XmlHelper;
import za.co.spsi.toolkit.db.DSDB;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
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
import za.co.spsi.toolkit.entity.FieldZip;
import za.co.spsi.toolkit.util.StringList;

import javax.sql.DataSource;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static za.co.spsi.mdms.kamstrup.db.KamstrupMeterOrderEntity.Status.DELETED;

/**
 * Created by jaspervdb on 2016/10/17.
 */
@Table(maintainStrict = true, version = 7, deleteOldRecords = true)
public class KamstrupMeterOrderEntity extends EntityDB {

    public enum Status {
        RECEIVED(0),
        READY(1),
        STARTED(2),
        BUSY(3),
        COMPLETED(4),
        REJECTED(5),
        FAILED(6),
        DELETED(7);

        int code;

        Status(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

    public static String[] getOptions() {
        return Arrays.stream(Status.values()).map(s -> s.name()).collect(Collectors.toCollection(ArrayList::new)).toArray(new String[]{});
    }

    public static String[] getValues() {
        return Arrays.stream(Status.values()).map(s -> s.getCode() + "").collect(Collectors.toCollection(ArrayList::new)).toArray(new String[]{});
    }

    public static final String RS_PROCESSING_EXCEPTION = "'%javax.ws.rs.ProcessingException: RESTEASY004655: Unable to invoke request%'";
    public static final String CONNECTION_TIMED_OUT = "'%java.net.ConnectException: Connection timed out%'";
    public static final String XML_MARSHALL = "'%XmlHelper.unmarshall%'";
    public static final String XML_UNMARSHALL = "'%JAXBUnmarshalException%'";

    @Id(uuid = true)
    @Column(name = "METER_ORDER_ID", notNull = true, size = 50)
    public Field<String> meterOrderId = new Field<>(this);

    @Column(name = "GROUP_ID")
    @ForeignKey(table = KamstrupGroupEntity.class, onDeleteAction = ForeignKey.Action.SetNull, name = "KMO_GROUP_ID_FK")
    public Field<String> groupId = new Field<>(this);

    @Column(name = "REF", size = 250, notNull = true)
    public Field<String> ref = new Field<>(this);

    @Column(name = "REF_RESPONSE", size = 10000, notNull = false)
    public Field<String> refResponse = new Field<>(this);

    @Column(name = "STATUS_REF", size = 241)
    public Field<String> statusRef = new Field<>(this);

    @Column(name = "COMPLETED_REF", size = 250)
    public Field<String> completedRef = new Field<>(this);

    @Column(name = "STATUS")
    public Field<Integer> status = new Field<>(this);

    public Field<Integer> succeeded = new Field<>(this);
    public Field<Integer> failed = new Field<>(this);
    public Field<Integer> aborted = new Field<>(this);
    public Field<Integer> waiting = new Field<>(this);

    @Column(name = "COMMANDS", size = 4000)
    public Field<String> commands = new Field<>(this);

    @Column(name = "CREATED", notNull = true)
    public FieldTimestamp created = new FieldTimestamp(this);

    @Column(name = "UPDATED")
    public FieldTimestamp updated = new FieldTimestamp(this).onUpdate();

    @Column(name = "FROM_DATE")
    public Field<Timestamp> fromDate = new Field<Timestamp>(this);

    @Column(name = "TO_DATE")
    public Field<Timestamp> toDate = new Field<Timestamp>(this);

    @Column(name = "ERROR", defaultValue = "0", size = 4000)
    public FieldError error = new FieldError(this);

    @Column(name = "ERROR_RETRY", defaultValue = "0")
    public Field<Integer> errorRetry = new Field<>(this);

    @Column(name = "RESPONSE", size = 8192)
    public Field<String> response = new Field<>(this);

    @Column(name = "DATA")
    public FieldZip data = new FieldZip(this);

    public Index idxRef = new Index("KMO_REF_IDX", this, ref);
    public Index idxGroupId = new Index("KMO_GROUP_ID", this, groupId);
    public Index idxGroupDateId = new Index("KMO_GR_DATE_ID", this, groupId, toDate);
    public Index idxStatus = new Index("KMO_STATUS", this, status);
    public Index idxCreated = new Index("KMO_CREATED", this, created);

    public KamstrupMeterOrderEntity() {
        super("KAMSTRUP_METER_ORDER");
    }

    public KamstrupMeterOrderEntity init(OrderDao order, PagingService pagingService) {
        this.ref.set(order.getOrder().ref);
        this.statusRef.set(order.getExecutionDetail(pagingService).orderExecution.refStatus);
        this.completedRef.set(order.getExecutionDetail(pagingService).orderExecution.refCompleted);
        this.created.set(order.getDetail(pagingService).created);

        OrderDetail detail = order.getDetail(pagingService);
        initOrderDetail(detail);

        if (detail.commands.hasBreakerReadCommand() && !detail.commands.hasLoggerReadCommand()) {
            this.status.set(Status.REJECTED.code);
        } else {
            this.status.set(Status.RECEIVED.code);
        }
        return this;
    }


    public void initOrderDetail(OrderDetail detail) {
        this.refResponse.set(XmlHelper.marshallToStringHandleException(OrderDetail.class, detail));
        this.commands.set(detail.commands != null ?
                detail.commands.toString().length() > 4000 ? detail.commands.toString().substring(0, 4000) : detail.commands.toString()
                : "");

    }

    public static KamstrupMeterOrderEntity getInstance(KamstrupGroupEntity group) {
        KamstrupMeterOrderEntity order = new KamstrupMeterOrderEntity();
        order.groupId.set(group.groupId.get());
        order.status.set(Status.RECEIVED.code);
        return order;
    }

    public KamstrupMeterOrderEntity init(OrderExecutionStatus status) {
        this.succeeded.set(status.succeededCommandCount);
        this.failed.set(status.failedCommandCount);
        this.aborted.set(status.abortedCommandCount);
        this.waiting.set(status.waitingCommandCount + status.waitingMeterCount);
        return this;
    }

    public static DataSourceDB<KamstrupMeterOrderEntity> getByStatus(Connection connection, Status status, int limit) {
        Driver driver = DriverFactory.getDriver();
        String query = "select * from KAMSTRUP_METER_ORDER where STATUS = ?";
        query = driver.limitSqlAndOrderBy(query,limit,"created", false);
        return new DataSourceDB<>(KamstrupMeterOrderEntity.class).getAll(connection,query,status.code);
    }

    public static void resetProcessState(javax.sql.DataSource dataSource, Status fromStatus, Status toStatus) {
        DataSourceDB.executeUpdate(dataSource, "update KAMSTRUP_METER_ORDER SET STATUS = ?, ERROR = NULL WHERE STATUS = ?", toStatus.getCode(), fromStatus.getCode());
    }

    public KamstrupMeterOrderEntity updateState(DataSource dataSource, Status status) {
        this.status.set(status.getCode());
        DataSourceDB.set(dataSource, this);
        return this;
    }

    @SneakyThrows
    public boolean doesOrderExist() {
        URL url = new URL(ref.get());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        try {
            con.setRequestMethod("GET");
            int responseCode = con.getResponseCode();
            return responseCode != 404;
        } finally {
            con.disconnect();
        }
    }

    public static void rescheduleFailedTcpTx(DataSource dataSource,Status reset,KamstrupMeterOrderEntity orderEntity) {
        if (orderEntity.doesOrderExist()) {
            // re process
            orderEntity.status.set(reset.code);
            orderEntity.errorRetry.set(orderEntity.errorRetry.getNonNull()+1);
        } else {
            orderEntity.status.set(DELETED.code);
        }
        DSDB.set(dataSource,orderEntity);
    }

    public static void rescheduleFailedTcpTx(javax.sql.DataSource dataSource, Status reset, int maxRetry, int retryWait,
                                             String... exception) {
        LocalDateTime retryTime = LocalDateTime.now().minusMinutes(retryWait);
        List<KamstrupMeterOrderEntity> failed = DSDB.getAllAsList(KamstrupMeterOrderEntity.class, dataSource
                , String.format("select * from kamstrup_meter_order where "
                        + "status = ? AND (%s) AND ERROR_RETRY < ? and UPDATED < ?", new StringList(exception).prepend("ERROR LIKE ").toString(" or ")),
                Status.FAILED.getCode(), maxRetry,Timestamp.valueOf(retryTime));
        failed.stream().forEach(f -> rescheduleFailedTcpTx(dataSource,reset,f));
    }

    private static void rescheduleFailedMarshall(javax.sql.DataSource dataSource, int maxRetry, int retryWait) {
        rescheduleFailedTcpTx(dataSource, Status.RECEIVED, maxRetry, retryWait, XML_MARSHALL, XML_UNMARSHALL);
    }

    private static void rescheduleFailedTcpTx(javax.sql.DataSource dataSource, int maxRetry, int retryWait) {
        rescheduleFailedTcpTx(dataSource, Status.STARTED, maxRetry, retryWait, RS_PROCESSING_EXCEPTION, CONNECTION_TIMED_OUT);
    }

    public static void rescheduleFailed(javax.sql.DataSource dataSource, int maxRetry, int retryWait) {
        rescheduleFailedMarshall(dataSource, maxRetry, retryWait);
        rescheduleFailedTcpTx(dataSource, maxRetry, retryWait);
    }


    /**
     * ip address might differ
     *
     * @param dataSource
     * @param ref
     * @return
     */
    public static KamstrupMeterOrderEntity findMeterOrderByRef(javax.sql.DataSource dataSource, String ref) {
        return DataSourceDB.get(KamstrupMeterOrderEntity.class, dataSource, "select * from kamstrup_meter_order where ref = ?", ref);
    }

    @Table(version = 0)
    public static class MeterHistory extends EntityDB {

        @Id(uuid = true, name = "KMOH_MOH_ID")
        @Column(name = "METER_ORDER_HISTORY_ID", notNull = true, size = 50)
        public Field<String> meterOrderHistoryId = new Field<>(this);

        @Column(name = "METER_ID")
        @ForeignKey(table = KamstrupMeterEntity.class, onDeleteAction = ForeignKey.Action.SetNull, name = "KMOH_METER_ID_FK")
        public Field<String> meterId = new Field<>(this);

        @Column(name = "METER_ORDER_ID")
        @ForeignKey(table = KamstrupMeterOrderEntity.class, onDeleteAction = ForeignKey.Action.SetNull, name = "KMOH_ORDER_ID_FK")
        public Field<String> orderId = new Field<>(this);

        public MeterHistory() {
            super("KAMSTRUP_METER_ORDER_HISTORY");
        }

        public static MeterHistory create(Connection connection, KamstrupMeterOrderEntity order, KamstrupMeterEntity meter) {
            MeterHistory meterHistory = new MeterHistory();
            meterHistory.meterId.set(meter.meterId.get());
            meterHistory.orderId.set(order.meterOrderId.get());
            DataSourceDB.set(connection, meterHistory);
            return meterHistory;
        }
    }


}
