package za.co.spsi.mdms.web.gui.layout;

import za.co.spsi.locale.annotation.MdmsLocaleId;
import za.co.spsi.mdms.common.db.MeterReadingEntity;
import za.co.spsi.toolkit.crud.gui.*;
import za.co.spsi.toolkit.crud.gui.ano.EntityRef;
import za.co.spsi.toolkit.crud.gui.ano.UIGroup;
import za.co.spsi.toolkit.crud.gui.fields.LocalTimestampField;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.time.format.DateTimeFormatter;

import static za.co.spsi.toolkit.ee.properties.FileReader.readResource;

/**
 * Created by jaspervdb on 2016/04/19.
 */

public class MeterReadingLayout extends Layout<MeterReadingEntity> {

    private static String METER_MAKE_SERIAL_CASE,METER_JOIN;

    static {
        METER_MAKE_SERIAL_CASE = readResource("sql/meter_reading_meter_make_serial_n_case.sql");
        METER_JOIN = readResource("sql/meter_reading_join_meters.sql");
    }

    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @EntityRef(main = true)
    private MeterReadingEntity reading = new MeterReadingEntity();

    public LField<String> meterMake = new LField<>("Meter Make","METER_MAKE", this);
    public LField<String> meterSerial = new LField<>("Meter Serial Number","METER_SERIAL_N", this);

    @UIGroup(column = 0)
    public Group registers = new Group("Registers", this);

    public static DateTimeFormatter entryDayFormat = DateTimeFormatter.ofPattern("yyMMdd");

    public LocalTimestampField entryTime = new LocalTimestampField(reading.entryTime, "Time", this);
    public LField<Boolean> generated = new LField<>(reading.generated, "Generated", this);
    public LField<Double> totalKwhP = new LField<>(reading.totalKwhP, MdmsLocaleId.TOTAL_KWH_POSITIVE, this);
    public LField<Double> totalKwhN = new LField<>(reading.totalKwhN, MdmsLocaleId.TOTAL_KWH_NEGATIVE, this);
    public LField<Double> totalKVarP = new LField<>(reading.totalKVarP, MdmsLocaleId.TOTAL_KVAR_POSITIVE, this);
    public LField<Double> totalKVarN = new LField<>(reading.totalKVarN, MdmsLocaleId.TOTAL_KVAR_NEGATIVE, this);
    public LField<Double> t1KwhP = new LField<>(reading.t1KwhP, MdmsLocaleId.T1_KWH_POSITIVE, this);
    public LField<Double> t1KwhN = new LField<>(reading.t1KwhN, MdmsLocaleId.T1_KWH_NEGATIVE, this);
    public LField<Double> t1KVarP = new LField<>(reading.t1KVarP, MdmsLocaleId.T1_KVAR_POSITIVE, this);
    public LField<Double> t1KVarN = new LField<>(reading.t1KVarN, MdmsLocaleId.T1_KVAR_NEGATIVE, this);
    public LField<Double> t2KwhP = new LField<>(reading.t2KwhP, MdmsLocaleId.T2_KWH_POSITIVE, this);
    public LField<Double> t2KwhN = new LField<>(reading.t2KwhN, MdmsLocaleId.T2_KWH_NEGATIVE, this);
    public LField<Double> t2KVarP = new LField<>(reading.t2KVarP, MdmsLocaleId.T2_KVAR_POSITIVE, this);
    public LField<Double> t2KVarN = new LField<>(reading.t2KVarN, MdmsLocaleId.T2_KVAR_NEGATIVE, this);
    public TimestampField createTime = new TimestampField(reading.createTime, "Created Time", this);

//     Max Demand
//    @UIGroup(column = 0)
//    public Group maxDemand = new Group(MdmsLocaleId.TOTAL_MAX_DEMAND, this);
//
//    public LField<Double> totalMaxDemandPoKva = new LField<>(reading.totalMaxDemandPoKva, MdmsLocaleId.PO_KVA, this);
//    public LField<Double> totalMaxDemandPoRtc = new LField<>(reading.totalMaxDemandPoRtc, MdmsLocaleId.PO_RTC, this);
//    public LField<Double> totalMaxDemandPrKva = new LField<>(reading.totalMaxDemandPrKva, MdmsLocaleId.PR_KVA, this);
//    public LField<Double> totalMaxDemandPrRtc = new LField<>(reading.totalMaxDemandPrRtc, MdmsLocaleId.PR_RTC, this);
//    public LField<Double> t1MaxDemandPoKva    = new LField<>(reading.t1MaxDemandPoKva, MdmsLocaleId.PO_KVA, this);
//    public LField<Double> t1MaxDemandPrKva    = new LField<>(reading.t1MaxDemandPrKva, MdmsLocaleId.PR_KVA, this);
//    public LField<Double> t2MaxDemandPoKva    = new LField<>(reading.t2MaxDemandPoKva, MdmsLocaleId.PO_KVA, this);
//    public LField<Double> t2MaxDemandPrKva    = new LField<>(reading.t2MaxDemandPrKva, MdmsLocaleId.PR_KVA, this);

    @UIGroup(column = 0)
    public Group voltage = new Group(MdmsLocaleId.VOLTAGE, this);

    public LField<Double> rmsL1V = new LField<>(reading.rmsL1V, MdmsLocaleId.RMS_L1V, this);
    public LField<Double> rmsL2V = new LField<>(reading.rmsL2V, MdmsLocaleId.RMS_L2V, this);
    public LField<Double> rmsL3V = new LField<>(reading.rmsL3V, MdmsLocaleId.RMS_L3V, this);

    @UIGroup(column = 0)
    public Group current = new Group("Current", this);

    public LField<Double> rmsL1C = new LField<>(reading.rmsL1C, MdmsLocaleId.RMS_L1C, this);
    public LField<Double> rmsL2C = new LField<>(reading.rmsL2C, MdmsLocaleId.RMS_L2C, this);
    public LField<Double> rmsL3C = new LField<>(reading.rmsL3C, MdmsLocaleId.RMS_L3C, this);


    @UIGroup(column = 0)
    public Group water = new Group(MdmsLocaleId.WATER, this);

    public LField<Double> volume1 = new LField<>(reading.volume1, MdmsLocaleId.VOLUME, this);

    public Group nameGroup = new Group(getFields().toArray(new LField[]{}), "", this).setNameGroup();

    public Pane detailPane = new Pane("", this, registers, voltage, current, water);

    public Pane kamstrupPane = new Pane("Kamstrup Meter", "select * from kamstrup_meter where kamstrup_meter.meter_id = meter_reading.kam_meter_id", KamstrupMeterLayout.class,this);
    public Pane nesPane = new Pane("Echelon Meter", "select * from nes_meter where nes_meter.meter_id = meter_reading.nes_meter_id", NesMeterLayout.class,this);
    public Pane elsPane = new Pane("Elster Meter", "select * from elster_meter where elster_meter.meter_id = meter_reading.els_meter_id", ElsterMeterLayout.class,this);
    public Pane genericPane = new Pane("Generic Meter", "select * from generic_meter where generic_meter.generic_meter_id = meter_reading.generic_meter_id", GenericMeterLayout.class,this);

    public Pane kamstrupOrderPane = new Pane("Kamstrup Order"
            , "select * from KAMSTRUP_METER_ORDER where KAMSTRUP_METER_ORDER.METER_ORDER_ID = meter_reading.KAM_METER_ORDER_ID"
            , KamstrupMeterOrderLayout.class,this);

    public Pane nesOrderPane = new Pane("Nes Order"
            , "select * from NES_METER_RESULT where NES_METER_RESULT.METER_RESULT_ID = meter_reading.NES_METER_RESULT_ID"
            , NesMeterResultLayout.class,this);

    public Pane prepaidPane = new Pane("Prepaid Batch"
            , "select * from prepaid_meter_reading_batch,meter_reading where prepaid_meter_reading_batch.PREPAID_METER_READING_BATCH_ID = meter_reading.PREPAID_METER_READING_BATCH_ID " +
            "and meter_reading.METER_READING_ID = ?"
            , PrepaidMeterReadingBatchLayout.class,this);

    public MeterReadingLayout() {
        super(MdmsLocaleId.METER_READING_DETAIL);
        setPermission(new Permission(0));
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    public static String getSql() {
        return String.format("select %s,%s from meter_reading %s"
                , METER_MAKE_SERIAL_CASE,MeterReadingEntity.getSqlAdjustedFieldSelect(),METER_JOIN);
    }

    @Override
    public String getMainSql() {
        return getSql() + " where 1 = 1";
    }

    @Override
    public String getExportSheetName() {
        if (getParentLayout().getExportSheetName() != null) {
            return getParentLayout().getExportSheetName();
        } else {
            return super.getExportSheetName();
        }
    }

}
