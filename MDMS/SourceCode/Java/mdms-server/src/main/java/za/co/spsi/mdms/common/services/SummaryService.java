package za.co.spsi.mdms.common.services;

import za.co.spsi.mdms.common.dao.MeterResultSummaryRecord;
import za.co.spsi.mdms.common.dao.MeterResultSummaryRecords;
import za.co.spsi.mdms.common.db.MeterReadingEntity;
import za.co.spsi.mdms.elster.db.ElsterMeterEntity;
import za.co.spsi.mdms.generic.meter.db.GenericMeterEntity;
import za.co.spsi.mdms.kamstrup.db.KamstrupMeterEntity;
import za.co.spsi.mdms.nes.db.NESMeterEntity;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.drivers.DriverFactory;
import za.co.spsi.toolkit.ee.properties.ConfValue;
import za.co.spsi.toolkit.ee.properties.TextFile;
import za.co.spsi.toolkit.util.Assert;

import javax.annotation.Resource;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.sql.Driver;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by jaspervdbijl on 2017/07/05.
 */
@Dependent
public class SummaryService {

    @Inject
    @TextFile("sql/smart_meter_reading_sub.sql")
    private String smartMeterSqlUsageReading;

    @Inject
    @TextFile("sql/smart_meter_reading_sub_field.sql")
    private String sqlField;

    @Inject
    @TextFile("sql/smart_meter_reading_sub_field_kva.sql")
    private String sqlFieldKva;

    @Inject
    @TextFile("sql/smart_meter_reading_sub_lag.sql")
    private String sqlLag;

    @Inject
    @TextFile("sql/smart_meter_reading_sub_case.sql")
    private String sqlCase;

    @Resource(mappedName = "java:/jdbc/mdms")
    private javax.sql.DataSource dataSource;

    @Inject
    @ConfValue(value = "meter.scale.kumstrup", folder = "server")
    private int kumstrupScale = 0;

    @Inject
    @ConfValue(value = "meter.scale.nes", folder = "server")
    private int nesScale = 3;

    @Inject
    @ConfValue(value = "meter.scale.elster", folder = "server")
    private int elsterScale = 0;

    @Inject
    @ConfValue(value = "meter.scale.generic", folder = "server")
    private int genericScale = 0;

    @Inject
    @ConfValue(value = "utility.meter_reading_sync.tmz_offset", folder = "server")
    private int tmzOffset;

    private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"),
            touFormat = DateTimeFormatter.ofPattern("HH:mm"), dayFormat = DateTimeFormatter.ofPattern("yyMMdd");

    private static MeterReadingEntity entity = new MeterReadingEntity();

    public int getTmzOffset() {
        return tmzOffset;
    }

    private String getCase(List<String> fields, List<String> lagFields) {
        return IntStream.range(0, fields.size()).mapToObj(i ->
                sqlCase.replace("_FIELD_", fields.get(i)).replace("_FIELDLAG_", lagFields.get(i))).reduce((a, b) -> a + "," + b).get();
    }

    private String getQuery(List<String> fields, List<String> lagFields, LocalDateTime from, LocalDateTime to, String kamMeterId, String nesMeterId,
                            String elsterMeterId, String genericMeterId, int tmzOffset, boolean noTouFilter, LocalTime fromTou, LocalTime toTou) {
        return smartMeterSqlUsageReading.
                replace("_SELECT_", fields.stream().map(f -> !f.contains("PO_KVA") ? sqlField.replace("_FIELD_", f) : "" ).reduce((a, b) -> a + "," + b).get().concat( sqlFieldKva )).
                replace("_LAG_", fields.stream().map(f -> sqlLag.replace("_FIELD_", f)).reduce((a, b) -> a + "," + b).get()).
                replace("_CASE_", getCase(fields, lagFields)).
                replace("_START_TIME_", dateFormat.format(from.minusMinutes(tmzOffset))).
                replace("_END_TIME_", dateFormat.format(to.minusMinutes(tmzOffset))).
                replace("_START_DAY_", dayFormat.format(from.minusMinutes(tmzOffset).minusDays(1))).
                replace("_END_DAY_", dayFormat.format(to.minusMinutes(tmzOffset).plusDays(1))).
                replace("_METER_ID_FIELD_", kamMeterId != null ? "KAM_METER_ID" : nesMeterId != null ? "NES_METER_ID" :
                        genericMeterId != null ? "GENERIC_METER_ID" : "ELS_METER_ID").
                replace("_METER_ID_VALUE_", kamMeterId != null ? kamMeterId : nesMeterId != null ? nesMeterId :
                        genericMeterId != null ? genericMeterId : elsterMeterId).
                replace("_TOU_FLTER_", noTouFilter ? "1 = 1" : "0 = 1").
                replace("_FROM_TOU_", fromTou != null ? fromTou.minusMinutes(tmzOffset).format(touFormat) : "NONE").
                replace("_TO_TOU_", toTou != null ? toTou.minusMinutes(tmzOffset).format(touFormat) : "NONE").
                replace("_SCALE_", "" + (Math.pow(10, (kamMeterId != null ? kumstrupScale : nesMeterId != null ? nesScale :
                        genericMeterId != null ? genericScale : elsterScale)))).
                replace("_NA_,", "");
    }

    private double getNonNull(Object value) {
        return value == null ? 0d : ((Double) value).doubleValue();
    }

    private int getNonNullInt(Object value) {
        return value == null ? 0 : ((Integer) value).intValue();
    }

    public MeterResultSummaryRecords getSummaryData(
            List<String> fields, List<String> lagFields, LocalDateTime from, LocalDateTime to, String serialN, int tmzOffset,
            boolean noTouFilter, LocalTime fromTou, LocalTime toTou) {

        KamstrupMeterEntity kamMeter = (KamstrupMeterEntity) DataSourceDB.getFromSet(dataSource, (EntityDB) new KamstrupMeterEntity().serialN.set(serialN));
        NESMeterEntity nesMeter = kamMeter == null ? (NESMeterEntity) DataSourceDB.getFromSet(dataSource, (EntityDB) new NESMeterEntity().serialN.set(serialN)) : null;
        ElsterMeterEntity elsterMeter = kamMeter == null && nesMeter == null ? (ElsterMeterEntity) DataSourceDB.getFromSet(dataSource, (EntityDB) new ElsterMeterEntity().serialN.set(serialN)) : null;
        String query = String.format( "select GENERIC_METER.* from GENERIC_METER where GENERIC_METER.METER_SERIAL_N = ? and GENERIC_METER.LIVE = %s",
                DriverFactory.getDriver().boolToNumber(true));
        GenericMeterEntity genericMeterEntity =
                kamMeter == null && nesMeter == null && elsterMeter == null ? DataSourceDB.get(GenericMeterEntity.class, dataSource, query, serialN) : null;

        if (kamMeter != null || nesMeter != null || elsterMeter != null || genericMeterEntity != null) {
            return getSummaryData(fields, lagFields, from, to,
                    kamMeter != null ? kamMeter.meterId.get() : null,
                    nesMeter != null ? nesMeter.meterId.get() : null,
                    elsterMeter != null ? elsterMeter.meterId.get() : null,
                    genericMeterEntity != null ? genericMeterEntity.genericMeterId.get() : null,
                    tmzOffset, noTouFilter, fromTou, toTou);
        } else {
            return new MeterResultSummaryRecords();
        }
    }

    public static boolean isUsageField(String f) {
        return f.endsWith("Usage") && !(f.endsWith("PUsage") || f.endsWith("NUsage"));
    }

    public static List<String> mapFields(List<String> fields) {
        List<String> mapped = new ArrayList<>();
        fields.stream().map(f -> f.replace("PUsage", "P").replace("NUsage", "N")).forEach(f -> {
            if (isUsageField(f)) {
                mapped.add(f.replace("Usage", "P"));
                mapped.add(f.replace("Usage", "N"));
            } else {
                mapped.add(f);
            }
        });
        return mapped.stream().collect(Collectors.toCollection(ArrayList::new));
    }

    private List<String> mapToCols(List<String> mapped) {
        return mapped.stream().filter(f -> entity.getFields().getByName(f, true) != null).
                map(f -> EntityDB.getColumnName(entity.getFields().getByName(f, true))).collect(Collectors.toCollection(ArrayList::new));
    }

    public MeterResultSummaryRecords getSummaryData(
            List<String> fields, List<String> lagFields, LocalDateTime from, LocalDateTime to, String kamMeterId, String nesMeterId,
            String elsterMeterId, String genericMeterId, int tmzOffset, boolean noTouFilter, LocalTime fromTou, LocalTime toTou) {

        MeterResultSummaryRecords results = new MeterResultSummaryRecords();
        List<String> mapped = mapFields(fields);
        List<String> mappedLag = mapFields(lagFields);
        List<String> cols = mapToCols(mapped);
        List<String> colsLag = mapToCols(mappedLag);
        Assert.isTrue(mapped.size() == cols.size(), "Unable to locate some of the columns from fields");
        Assert.isTrue(mappedLag.size() == colsLag.size(), "Unable to locate some of the columns from fields");

        List<Class> types = new ArrayList<>();
        mapped.stream().forEach(f -> {
            types.addAll(Arrays.asList(new Class[]{Double.class, Double.class, Double.class, Double.class, Double.class, Double.class, Timestamp.class, Timestamp.class}));
        });

        DataSourceDB.executeQuery(dataSource, result -> mapped.stream().forEach(f -> {
            int offSet = 8;
            int i = mapped.indexOf(f);
            int maxIdx = 7 + i * offSet;
            int size = result.size();

            Assert.isTrue(maxIdx != size, String.format("result[%d] -> Index Out of Bounds, result.size() = %d", i, size));

            results.add( new MeterResultSummaryRecord( f,
                    getNonNull(result.get(0 + i * offSet)), getNonNull(result.get(1 + i * offSet)), getNonNull(result.get(2 + i * offSet)),
                    getNonNull(result.get(3 + i * offSet)), getNonNull(result.get(4 + i * offSet)), getNonNull(result.get(5 + i * offSet)), 0.0,
                    (Timestamp) result.get(6 + i * offSet), (Timestamp) result.get(7 + i * offSet)).adjustTime(tmzOffset) );
        }),
        types.toArray(new Class[]{}),
        getQuery(cols, colsLag, from, to, kamMeterId, nesMeterId, elsterMeterId, genericMeterId, tmzOffset, noTouFilter, fromTou, toTou));

        MeterResultSummaryRecords mappedResults = new MeterResultSummaryRecords();
        fields.stream().forEach(f -> {
            if (isUsageField(f)) {
                mappedResults.add(new MeterResultSummaryRecord(f, results.getByName(f.replace("Usage", "P")).get(),
                        results.getByName(f.replace("Usage", "N")).get()));
            } else {
                mappedResults.add( results.getByName(f.replace("Usage", "")).get() );
            }
        });
        return mappedResults;
    }

}
