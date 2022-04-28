package za.co.spsi.mdms.common.db;

import lombok.SneakyThrows;
import za.co.spsi.mdms.common.dao.MeterResultData;
import za.co.spsi.mdms.common.dao.ano.MeterRegister;
import za.co.spsi.mdms.common.db.survey.PecMeterEntity;
import za.co.spsi.mdms.common.db.survey.PecMeterRegisterEntity;
import za.co.spsi.mdms.elster.db.ElsterMeterEntity;
import za.co.spsi.mdms.generic.meter.db.GenericMeterEntity;
import za.co.spsi.mdms.kamstrup.db.KamstrupMeterEntity;
import za.co.spsi.mdms.kamstrup.db.KamstrupMeterOrderEntity;
import za.co.spsi.mdms.kamstrup.services.order.domain.Meter;
import za.co.spsi.mdms.kamstrup.services.order.domain.Register;
import za.co.spsi.mdms.nes.db.NESMeterEntity;
import za.co.spsi.mdms.nes.db.NESMeterResultEntity;
import za.co.spsi.mdms.nes.services.order.domain.Channel;
import za.co.spsi.mdms.nes.services.order.domain.Interval;
import za.co.spsi.mdms.util.PrepaidMeterFilterService;
import za.co.spsi.pjtk.util.StringUtils;
import za.co.spsi.toolkit.db.DSDB;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.db.drivers.DriverFactory;
import za.co.spsi.toolkit.db.fields.FieldTimestamp;
import za.co.spsi.toolkit.db.fields.Index;
import za.co.spsi.toolkit.ee.properties.ConfValue;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.entity.FieldLocalDate;
import za.co.spsi.toolkit.reflect.RefFields;
import za.co.spsi.toolkit.util.Assert;

import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import static za.co.spsi.mdms.common.services.MeterDataService.Interval.HALF_HOURLY;
import static za.co.spsi.mdms.common.services.MeterDataService.Interval.HOURLY;
import static za.co.spsi.mdms.kamstrup.processor.KamstrupProcessor.dayFormat;
import static za.co.spsi.toolkit.util.Util.handle;

/**
 * Created by jaspervdb on 2016/11/22.
 */
@Table(version = 18, deleteOldRecords = false, deleteRecordTimeField = "ENTRY_TIME")
public class MeterReadingEntity extends EntityDB {

    public static RefFields METER_ENTRY_FIELDS = new RefFields(MeterReadingEntity.class);
    public static final Logger TAG = Logger.getLogger(MeterReadingEntity.class.getName());
    public static String ENTRY_DAY_FORMAT = "yyMMdd";

    @Inject
    @ConfValue(value = "utility.meter_reading_sync.tmz_offset", folder = "server")
    private String tmzOffset;

    public enum MeterType {
        MeterType, Prepaid, Postpaid;
    }

    @Id(uuid = true)
    @Column(name = "METER_READING_ID", size = 50, notNull = true)
    public Field<String> meterReadingId = new Field<>(this);

    @ForeignKey(table = KamstrupMeterEntity.class, name = "MR_KAM_METER_ID", onDeleteAction = ForeignKey.Action.Cascade)
    @Column(name = "KAM_METER_ID", size = 50)
    public Field<String> kamMeterId = new Field<>(this);

    @ForeignKey(table = GenericMeterEntity.class, name = "GENERIC_METER_ID", onDeleteAction = ForeignKey.Action.Cascade)
    @Column(name = "GENERIC_METER_ID", size = 50)
    public Field<String> genericMeterId = new Field<>(this);

    @ForeignKey(table = NESMeterEntity.class, name = "MR_NES_METER_ID", onDeleteAction = ForeignKey.Action.Cascade)
    @Column(name = "NES_METER_ID", size = 50)
    public Field<String> nesMeterId = new Field<>(this);

    @ForeignKey(table = ElsterMeterEntity.class, name = "MR_ELS_METER_ID", onDeleteAction = ForeignKey.Action.Cascade)
    @Column(name = "ELS_METER_ID", size = 50)
    public Field<String> elsterMeterId = new Field<>(this);

    @Column(name = "ELS_REF_ID")
    public Field<Long> elsterRefId = new Field<>(this);

    @Column(name = "USER_ID", size = 50)
    public Field<String> userId = new Field<String>(this);

    @Column(name = "KAM_METER_ORDER_ID")
    @ForeignKey(table = KamstrupMeterOrderEntity.class, name = "MR_KAM_ORDER_ID", onDeleteAction = ForeignKey.Action.SetNull)
    public Field<String> kamMeterOrderId = new Field<>(this);

    @Column(name = "NES_METER_RESULT_ID")
    @ForeignKey(table = NESMeterResultEntity.class, name = "MR_NES_RESULT_ID", onDeleteAction = ForeignKey.Action.SetNull)
    public Field<String> nesMeterResultId = new Field<>(this);

    @Column(name = "ENTRY_TIME", notNull = true)
    public FieldLocalDate<Timestamp> entryTime = new FieldLocalDate<Timestamp>(this)
            .truncateTo(ChronoUnit.MINUTES);

    @Column(name = "ENTRY_DAY", notNull = true)
    public Field<Integer> entryDay = new Field<>(this);

    @Column(name = "CREATE_TIME")
    public FieldTimestamp createTime = new FieldTimestamp(this);

    @Column(name = "gen_tx_id", size = 50)
    public Field<String> genTxId = new Field<>(this);

    @Column(name = "PREPAID_METER", toNumber = true)
    public Field<Boolean> prepaidMeter = new Field<>(this);

    @Column(name = "GENERATED", toNumber = true)
    public Field<Boolean> generated = new Field<>(this);

    @Column(name = "PREPAID_METER_READING_BATCH_ID")
    @ForeignKey(table = PrepaidMeterReadingBatch.class, name = "PMRB_ID", onDeleteAction = ForeignKey.Action.SetNull)
    public Field<String> prepaidMeterReadingBatchId = new Field<>(this);

    // Values
    @MeterRegister(value = {"1.1.1.8.0.255", "0"}, scale = {0, 3, 0, 0},exportScale = {0,3,0,0})
    @Column(name = "TOTAL_KWHP")
    public Field<Double> totalKwhP = new Field<>(this);

    @MeterRegister(value = {"1.1.2.8.0.255", "1"}, scale = {0, 3, 0, 0},exportScale = {0,3,0,0})
    @Column(name = "TOTAL_KWHN")
    public Field<Double> totalKwhN = new Field<>(this);

    @MeterRegister(value = {"1.1.3.8.0.255", "2"}, scale = {0, 3, 0, 0},exportScale = {0,3,0,0})
    @Column(name = "TOTAL_KVARP")
    public Field<Double> totalKVarP = new Field<>(this);

    @MeterRegister(value = {"1.1.4.8.0.255", "3"}, scale = {0, 3, 0, 0},exportScale = {0,3,0,0})
    @Column(name = "TOTAL_KVARN")
    public Field<Double> totalKVarN = new Field<>(this);

    @MeterRegister(value = {"1.1.1.8.1.255", "29"}, scale = {0, 3, 0, 0},exportScale = {0,3,0,0})
    @Column(name = "T1_KWHP")
    public Field<Double> t1KwhP = new Field<>(this);

    @MeterRegister(value = {"1.1.2.8.1.255", "30"}, scale = {0, 3, 0, 0},exportScale = {0,3,0,0})
    @Column(name = "T1_KWHN")
    public Field<Double> t1KwhN = new Field<>(this);

    @MeterRegister(value = {"1.1.3.8.1.255", "31"}, scale = {0, 3, 0, 0},exportScale = {0,3,0,0})
    @Column(name = "T1_KVARP")
    public Field<Double> t1KVarP = new Field<>(this);

    @MeterRegister(value = {"1.1.4.8.1.255", "32"}, scale = {0, 3, 0, 0},exportScale = {0,3,0,0})
    @Column(name = "T1_KVARN")
    public Field<Double> t1KVarN = new Field<>(this);

    @MeterRegister(value = {"1.1.1.8.2.255", "40"}, scale = {0, 3, 0, 0},exportScale = {0,3,0,0})
    @Column(name = "T2_KWHP")
    public Field<Double> t2KwhP = new Field<>(this);

    @MeterRegister(value = {"1.1.2.8.2.255", ""}, scale = {0, 3, 0, 0},exportScale = {0,3,0,0})
    @Column(name = "T2_KWHN")
    public Field<Double> t2KwhN = new Field<>(this);

    @MeterRegister(value = {"1.1.3.8.2.255", "42"}, scale = {0, 3, 0, 0},exportScale = {0,3,0,0})
    @Column(name = "T2_KVARP")
    public Field<Double> t2KVarP = new Field<>(this);

    @MeterRegister(value = {"1.1.4.8.2.255", ""}, scale = {0, 3, 0, 0},exportScale = {0,3,0,0})
    @Column(name = "T2_KVARN")
    public Field<Double> t2KVarN = new Field<>(this);

    // Max Demand
    @MeterRegister(value = {"1.1.9.6.0.255", ""}, scale = {0, 3, 0, 0})
    @Column(name = "TOTAL_MAX_DEMAND_PO_KVA")
    public Field<Double> totalMaxDemandPoKva = new Field<>(this);

    @MeterRegister(value = {"0.2.1.130.0.255", ""}, scale = {0, 3, 0, 0})
    @Column(name = "TOTAL_MAX_DEMAND_PO_RTC")
    public Field<Double> totalMaxDemandPoRtc = new Field<>(this);

    @MeterRegister(value = {"1.1.9.6.0.255", ""}, scale = {0, 3, 0, 0})
    @Column(name = "TOTAL_MAX_DEMAND_PR_KVA")
    public Field<Double> totalMaxDemandPrKva = new Field<>(this);

    @MeterRegister(value = {"0.2.1.130.0.255", ""}, scale = {0, 3, 0, 0})
    @Column(name = "TOTAL_MAX_DEMAND_PR_RTC")
    public Field<Double> totalMaxDemandPrRtc = new Field<>(this);

    @MeterRegister(value = {"1.1.9.6.1.255", ""}, scale = {0, 3, 0, 0})
    @Column(name = "T1_MAX_DEMAND_PO_KVA")
    public Field<Double> t1MaxDemandPoKva = new Field<>(this);

    @MeterRegister(value = {"1.1.9.6.1.255", ""}, scale = {0, 3, 0, 0})
    @Column(name = "T1_MAX_DEMAND_PR_KVA")
    public Field<Double> t1MaxDemandPrKva = new Field<>(this);

    @MeterRegister(value = {"1.1.9.6.2.255", ""}, scale = {0, 3, 0, 0})
    @Column(name = "T2_MAX_DEMAND_PO_KVA")
    public Field<Double> t2MaxDemandPoKva = new Field<>(this);

    @MeterRegister(value = {"1.1.9.6.2.255", ""}, scale = {0, 3, 0, 0})
    @Column(name = "T2_MAX_DEMAND_PR_KVA")
    public Field<Double> t2MaxDemandPrKva = new Field<>(this);

    // RMS Voltage
    @MeterRegister(value = {"1.1.32.25.0.255", "15"}, scale = {0, 3, 0, 0},exportScale = {0,3,0,0})
    @Column(name = "RMS_L1_V")
    public Field<Double> rmsL1V = new Field<>(this);

    @MeterRegister(value = {"1.1.52.25.0.255", "16"}, scale = {0, 3, 0, 0},exportScale = {0,3,0,0})
    @Column(name = "RMS_L2_V")
    public Field<Double> rmsL2V = new Field<>(this);

    @MeterRegister(value = {"1.1.72.25.0.255", "17"}, scale = {0, 3, 0, 0},exportScale = {0,3,0,0})
    @Column(name = "RMS_L3_V")
    public Field<Double> rmsL3V = new Field<>(this);

    // RMS Current
    @MeterRegister(value = {"1.1.31.25.0.255", "197"}, scale = {0, 3, 0, 0},exportScale = {0,3,0,0})
    @Column(name = "RMS_L1_C")
    public Field<Double> rmsL1C = new Field<>(this);

    @MeterRegister(value = {"1.1.51.25.0.255", "198"}, scale = {0, 3, 0, 0},exportScale = {0,3,0,0})
    @Column(name = "RMS_L2_C")
    public Field<Double> rmsL2C = new Field<>(this);

    @MeterRegister(value = {"1.1.71.25.0.255", "199"}, scale = {0, 3, 0, 0},exportScale = {0,3,0,0})
    @Column(name = "RMS_L3_C")
    public Field<Double> rmsL3C = new Field<>(this);

    @Column(name = "ALARM")
    public Field<Short> alarm = new Field<>(this);

    // WATER
    @MeterRegister(value = {"8.1.1.0.0.255", "9.1.1.0.0.255"}, scale = {0, 0, 0, 0}, exportScale = {0,0,0,0})
    @Column(name = "VOLUME_1")
    public Field<Double> volume1 = new Field<>(this);

    // COS
    @Column(name = "PF")
    public Field<Double> pf = new Field<>(this);

    private Index idxKamEntryDay = new Index("idx_MR_KAM_ENTRY_DAY", this, kamMeterId, entryDay);
    private Index idxNesEntryDay = new Index("idx_MR_NES_ENTRY_DAY", this, nesMeterId, entryDay);
    private Index idxKamMeterId = new Index("idx_MR_KAM_METER_ID", this, kamMeterId);
    private Index idxKamOrderId = new Index("idx_MR_KAM_ORDER_ID", this, kamMeterOrderId);
    private Index idxNesMeterId = new Index("idx_MR_NES_METER_ID", this, nesMeterId);
    private Index idxNesResultId = new Index("idx_MR_NES_RESULT_ID", this, nesMeterResultId);
    private Index idxElsMeterId = new Index("idx_MR_ELS_METER_ID", this, elsterMeterId);
    private Index idxPrepaid1 = new Index("idx_PREPAID1", this, prepaidMeter, prepaidMeterReadingBatchId);
    private Index idxPrepaid = new Index("idx_PREPAID", this, prepaidMeterReadingBatchId);
    private Index idxGenericMeterId = new Index("idx_GENERIC_METER_ID", this, genericMeterId);
    private Index idxPrepaidMeterReadingBatchId = new Index("idx_PREP_METER_READING_BATCH", this, prepaidMeterReadingBatchId);
    private Index idxEntryTime = new Index("idx_MR_ET", this, entryTime);

    public MeterReadingEntity() {
        super("METER_READING");
    }

    private void update(String registerId, Double value) {
        Map<Field, MeterRegister> map = getFields().getFieldsWithAnnotation(MeterRegister.class);
        for (Field field : map.keySet()) {
            if (Arrays.asList(map.get(field).value()).contains(registerId)) {
                field.set(value);
            }
        }
    }

    /**
     * determine the index of the export scale
     * @return
     */
    public int getExportScaleIndex() {
        return kamMeterId.get() != null ? 0
                : nesMeterId.get() != null ? 1
                : elsterMeterId.get() != null ? 2
                : 3;
    }

    private void update(Register register) {
        update(register.id, register.value);
    }

    private void update(Channel channel) {
        update(channel.id.toString(), channel.value);
    }

    public void update(Register registers[]) {
        Arrays.asList(registers).stream().forEach(r -> update(r));
    }

    public MeterReadingEntity mapTotalToXAndClearY(String genTxId, String x, String y) {
        this.genTxId.set(genTxId);
        for (Field field : getFields()) {
            if (field.getName().startsWith(x)) {
                Field xField = field;
                Field yField = getFields().getByName(y + field.getName().substring(x.length()));
                Field totalField = getFields().getByName("total" + field.getName().substring(x.length()));
                if (totalField.get() != null) {
                    xField.set(totalField.get());
                    yField.set(null);
                }
            }
        }
        return this;
    }

    public void update(Interval entry) {
        Arrays.asList(entry.channels).stream().forEach(r -> update(r));
    }

    public String getMeterId() {
        return kamMeterId.get() != null ? kamMeterId.get() :
                nesMeterId.get() != null ? nesMeterId.get() :
                        elsterMeterId.get();
    }

    public Field getRegField(String registerId) {
        Optional<Field> field = getFieldsWithAnnotation(MeterRegister.class).stream().filter(f ->
                Arrays.stream(((MeterRegister) f.getAnnotation(MeterRegister.class)).value()).
                        filter(r -> r.equals(registerId)).findAny().isPresent()).findAny();
        return field.isPresent() ? field.get() : null;
    }

    public static String getSqlAdjustedFieldSelect() {
        String fields = METER_ENTRY_FIELDS.filter(Column.class).stream().filter(f -> !f.getName().equals("meterReadingId"))
                .map(f -> {
                            MeterRegister register = f.getAnnotation(MeterRegister.class);
                            String colName = f.getAnnotation(Column.class).name();
                            colName = "meter_reading."+(colName != null ? colName : f.getName());

                            return register != null ? String.format(
                                    "(case when meter_reading.KAM_METER_ID is not null then %s / power(10,%s) / power(10,%s) " +
                                            "when meter_reading.NES_METER_ID is not null then %s / power(10,%s) / power(10,%s) " +
                                            "when meter_reading.ELS_METER_ID is not null then %s / power(10,%s) / power(10,%s) " +
                                            "when meter_reading.GENERIC_METER_ID is not null then %s / power(10,%s) / power(10,%s) end) as %s",
                                    colName, register.scale()[0], "0",
                                    colName, "0", register.exportScale()[1],
                                    colName, register.scale()[2], "0",
                                    colName, register.scale()[3], "0",
                                    colName) : colName;
                        }
                ).reduce((s1, s2) -> s1 + ",\n" + s2).get();
        return fields.replace("as meter_reading.","as ");
    }

    public static int getGenerateScaleIndex(PecMeterEntity pecMeter) {
        if (pecMeter == null)
            return 3;

        if (pecMeter.kamMeterId.get() != null && !pecMeter.kamMeterId.get().equals(""))
            return 0;

        if (pecMeter.nesMeterId.get() != null && !pecMeter.nesMeterId.get().equals(""))
            return 1;

        if (pecMeter.elsMeterId.get() != null && !pecMeter.elsMeterId.get().equals(""))
            return 2;

        return 3;

    }

    /**
     *
     * @param connection
     * @param data
     * @param kamMeterId
     * @param elsMeterId
     * @param nesMeterId
     * @param genericMeterId
     * @param is_Prepaid
     * @param scaleIndex
     * @return
     */
    @SneakyThrows
    public static MeterReadingEntity generate(Connection connection, MeterResultData data
            , String kamMeterId,String elsMeterId,String nesMeterId, String genericMeterId
            , String meterReadingGroupName
            , boolean is_Prepaid, int scaleIndex) {

        boolean existingMeterReading = false;

        MeterReadingEntity reading = new MeterReadingEntity();
        reading.entryTime.set(data.getEntryTime());

        if( !StringUtils.isEmpty( kamMeterId ) ) {
            reading.kamMeterId.set(kamMeterId);
        } else if( !StringUtils.isEmpty( nesMeterId ) ) {
            reading.nesMeterId.set(nesMeterId);
        } else if( !StringUtils.isEmpty( elsMeterId ) ) {
            reading.elsterMeterId.set(elsMeterId);
        } else if( !StringUtils.isEmpty( genericMeterId ) ) {
            reading.genericMeterId.set(genericMeterId);
        } else {
            return reading;
        }

        MeterReadingEntity tempReading = DataSourceDB.getFromSet(connection, reading);

        if( tempReading != null ) {

            existingMeterReading = true;

            reading.meterReadingId.set( tempReading.meterReadingId.get() );
            reading.entryDay.set( tempReading.entryDay.get() );

            String fieldName = meterReadingGroupName.contains("V") ? "volume1" : String.format("%skwhp", meterReadingGroupName.toLowerCase());

            Field existingReading = tempReading.getFields().getByName(fieldName,true);

            if( !existingReading.getNonNull().equals(0.0) ) {
                return tempReading;
            }

        }

        if( !StringUtils.isEmpty( kamMeterId ) ) {

            String fieldNameRegexFilter = getMeterReadingFieldNameFilter(meterReadingGroupName);

            // map specific registers
            new RefFields(data.getClass()).stream()
                    .filter(f -> reading.getFields().getByName(f.getName()) != null)
                    .filter(f -> f.getName().matches(fieldNameRegexFilter))
                    .forEach(f -> handle(() -> {
                        if (f.get(data) != null) {
                            Field field = reading.getFields().getByName(f.getName());
                            MeterRegister register = (MeterRegister) field.getAnnotation(MeterRegister.class);
                            if (register != null) {
                                field.set( (Double) f.get(data) * Math.pow(10, (double) register.scale()[scaleIndex]) ) ;
                            } else {
                                field.set( f.get(data)  );
                            }
                        }
                    }));

        } else {

            // map all the registers
            new RefFields(data.getClass()).stream()
                    .filter(f -> reading.getFields().getByName(f.getName()) != null)
                    .forEach(f -> handle(() -> {
                        if (f.get(data) != null) {
                            Field field = reading.getFields().getByName(f.getName());
                            MeterRegister register = (MeterRegister) field.getAnnotation(MeterRegister.class);
                            if (register != null) {
                                field.set( (Double) f.get(data) * Math.pow(10, (double) register.scale()[scaleIndex]) ) ;
                            } else {
                                field.set( f.get(data)  );
                            }
                        }
                    }));

        }

        reading.generated.set(true);
        reading.entryDay.set(Integer.parseInt(dayFormat.format(data.getEntryTime())));
        reading.prepaidMeter.set(is_Prepaid);

        if(existingMeterReading) {
            DataSourceDB.setUpdate(connection, reading);
        } else {
            DataSourceDB.setInsert(connection, reading);
        }

        return reading;
    }

    public static MeterReadingEntity generate(Connection connection, MeterResultData data, PecMeterEntity meter,
                                              PecMeterRegisterEntity register, PrepaidMeterFilterService filterService) {

        String meterReadingGroupName = getMeterReadingGroupName(register.getMappedName());

        return generate(connection
                ,data,meter.kamMeterId.get(),meter.elsMeterId.get(),meter.nesMeterId.get(),meter.genericMeterId.get(), meterReadingGroupName
                ,filterService.isPrepaid(meter.meterN.get()),getGenerateScaleIndex(meter));
    }

    private static String getMeterReadingGroupName(String registerMappedName) {
        String groupName;

        if( registerMappedName.contains("volume") ) {
            groupName = "VOLUME";
        } else {
            groupName = "TOTAL";
        }

        return groupName;
    }

    private static String getMeterReadingColumnNameFilter(String groupName) {
        String columnName;

        switch(groupName) {
            case "TOTAL":
                columnName = "TOTAL_KWHP";
                break;
            case "T1":
                columnName = "T1_KWHP";
                break;
            case "T2":
                columnName = "T2_KWHP";
                break;
            case "CURRENT":
                columnName = "RMS_L1_C";
                break;
            case "VOLTAGE":
                columnName = "RMS_L1_V";
                break;
            case "VOLUME":
                columnName = "VOLUME_1";
                break;
            default:
                columnName = "TOTAL_KWHP";
        }

        return columnName;
    }

    private static String getMeterReadingFieldNameFilter(String groupName) {
        String fieldNameFilter;

        switch(groupName) {
            case "TOTAL":
                fieldNameFilter = "total.*";
                break;
            case "T1":
                fieldNameFilter = "t1.*";
                break;
            case "T2":
                fieldNameFilter = "t2.*";
                break;
            case "CURRENT":
                fieldNameFilter = "rmsL.*C";
                break;
            case "VOLTAGE":
                fieldNameFilter = "rmsL.*V";
                break;
            case "VOLUME":
                fieldNameFilter = "volume.*";
                break;
            default:
                fieldNameFilter = "total.*";
        }

        return fieldNameFilter;
    }

    /**
     *
     * @param field
     * @return the export value converted by scale
     */
    public static Double getExportValue(Field<Double> field,int scaleIndex) {
        MeterRegister register = field.getAnnotation(MeterRegister.class);
        return field.get() != null?field.get()/(Math.pow(10,register.exportScale()[scaleIndex])):0.0;
    }

    public static double getExportScale(PecMeterRegisterEntity registerEntity,int scaleIndex) {
        String fieldName = registerEntity.getMappedName(registerEntity.registerId.get());
        Optional<java.lang.reflect.Field> field = Arrays.stream(MeterReadingEntity.class.getFields())
                .filter(f -> f.getName().equals(fieldName))
                .findFirst();
        Assert.isTrue(field.isPresent(),"Could not locate field %s in %s",fieldName,MeterReadingEntity.class.getName());
        return Math.pow(10,field.get().getAnnotation(MeterRegister.class).exportScale()[scaleIndex]);
    }

    public static void adjustForExport(PecMeterRegisterEntity registerEntity,Field<Double> field,int scaleIndex) {
        if (field.get() != null) {
            field.set(field.get() / getExportScale(registerEntity,scaleIndex));
        }
        else {
            field.set(0.0);
        }
    }

    public Timestamp adjustTimestamp( Integer nearestMin ) {

        long adjustedTimeEpochMilli = 0;
        int adjustMin = 0;
        long milliSecondAdj = 0;
        int min = this.entryTime.get().toLocalDateTime().getMinute();
        int mode = min % nearestMin;

        if( mode > ( nearestMin / 2 ) ) {
            adjustMin = nearestMin - mode;
        } else {
            adjustMin = 0 - mode;
        }

        milliSecondAdj = adjustMin * 60 * 1000;

        adjustedTimeEpochMilli = this.entryTime.get().getTime() + milliSecondAdj;

        LocalDateTime adjustedTime = LocalDateTime.ofEpochSecond(adjustedTimeEpochMilli/1000, 0,
                ZoneOffset.ofHours(Integer.parseInt(StringUtils.isEmpty(tmzOffset) ? "120":tmzOffset)/60) )
                .truncatedTo(ChronoUnit.MINUTES);

        return  Timestamp.valueOf( adjustedTime );

    }

    public Timestamp adjustTimestamp( Integer nearestMin, Timestamp unadjustedTS ) {

        long adjustedTimeEpochMilli = 0;
        int adjustMin = 0;
        long milliSecondAdj = 0;
        int min = unadjustedTS.toLocalDateTime().getMinute();
        int mode = min % nearestMin;

        if( mode > ( nearestMin / 2 ) ) {
            adjustMin = nearestMin - mode;
        } else {
            adjustMin = 0 - mode;
        }

        milliSecondAdj = adjustMin * 60 * 1000;

        adjustedTimeEpochMilli = unadjustedTS.getTime() + milliSecondAdj;

        LocalDateTime adjustedTime = LocalDateTime.ofEpochSecond(adjustedTimeEpochMilli/1000, 0,
                ZoneOffset.ofHours(Integer.parseInt(StringUtils.isEmpty(tmzOffset) ? "120":tmzOffset)/60) )
                .truncatedTo(ChronoUnit.MINUTES);

        return Timestamp.valueOf( adjustedTime );
    }

    public static MeterReadingEntity convertFromTsdb(MeterReadingTsdbEntity tsdbEntity) {
        MeterReadingEntity meterReadingEntity = new MeterReadingEntity();

        meterReadingEntity.entryTime.set(tsdbEntity.entry_time.get());

        meterReadingEntity.totalKwhP.set(tsdbEntity.total_kwhp.get());
        meterReadingEntity.totalKwhN.set(tsdbEntity.total_kwhn.get());
        meterReadingEntity.totalKVarP.set(tsdbEntity.total_kvarp.get());
        meterReadingEntity.totalKVarN.set(tsdbEntity.total_kvarn.get());

        meterReadingEntity.t1KwhP.set(tsdbEntity.t1_kwhp.get());
        meterReadingEntity.t1KwhN.set(tsdbEntity.t1_kwhn.get());
        meterReadingEntity.t1KVarP.set(tsdbEntity.t1_kvarp.get());
        meterReadingEntity.t1KVarN.set(tsdbEntity.t1_kvarn.get());

        meterReadingEntity.t2KwhP.set(tsdbEntity.t2_kwhp.get());
        meterReadingEntity.t2KwhN.set(tsdbEntity.t2_kwhn.get());
        meterReadingEntity.t2KVarP.set(tsdbEntity.t2_kvarp.get());
        meterReadingEntity.t2KVarN.set(tsdbEntity.t2_kvarn.get());

        meterReadingEntity.rmsL1V.set(tsdbEntity.rms_l1_v.get());
        meterReadingEntity.rmsL2V.set(tsdbEntity.rms_l2_v.get());
        meterReadingEntity.rmsL3V.set(tsdbEntity.rms_l3_v.get());

        meterReadingEntity.rmsL1C.set(tsdbEntity.rms_l1_c.get());
        meterReadingEntity.rmsL2C.set(tsdbEntity.rms_l2_c.get());
        meterReadingEntity.rmsL3C.set(tsdbEntity.rms_l3_c.get());

        meterReadingEntity.volume1.set(tsdbEntity.volume_1.get());

        Boolean generated = tsdbEntity.total_kwhp_orig.get() == null && tsdbEntity.total_kwhp.get() != null;
        generated |= tsdbEntity.t1_kwhp_orig.get() == null && tsdbEntity.t1_kwhp.get() != null;
        generated |= tsdbEntity.t2_kwhp_orig.get() == null && tsdbEntity.t2_kwhp.get() != null;
        generated |= tsdbEntity.rms_l1_v_orig.get() == null && tsdbEntity.rms_l1_v.get() != null;
        generated |= tsdbEntity.rms_l1_c_orig.get() == null && tsdbEntity.rms_l1_c.get() != null;
        generated |= tsdbEntity.volume_1_orig.get() == null && tsdbEntity.volume_1.get() != null;

        meterReadingEntity.generated.set(generated);

        return meterReadingEntity;
    }

}
