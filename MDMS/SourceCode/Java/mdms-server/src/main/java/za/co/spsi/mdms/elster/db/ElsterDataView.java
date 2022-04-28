package za.co.spsi.mdms.elster.db;

import za.co.spsi.mdms.common.db.MeterReadingEntity;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.entity.Field;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;

public class ElsterDataView extends EntityDB {

    private static final SimpleDateFormat TS_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat dayFormat = new SimpleDateFormat(MeterReadingEntity.ENTRY_DAY_FORMAT);

    public Field<Long> id = new Field<>(this);
    public Field<String> SerialNumber = new Field<>(this);
    public Field<String> entrytime = new Field<>(this);
    //    public Field<Date> date = new Field<>(this);
//    public Field<java.sql.Time> time = new Field<>(this);
    public Field<Double> Import_kWh = new Field<>(this);
    public Field<Double> Export_kWh = new Field<>(this);
    public Field<Double> Q1_kvarh = new Field<>(this);
    public Field<Double> Q2_kvarh = new Field<>(this);
    public Field<Double> Q3_kvarh = new Field<>(this);
    public Field<Double> Q4_kvarh = new Field<>(this);
    public Field<Double> VAh_kVAh = new Field<>(this);
    public Field<Double> Frequency = new Field<>(this);
    public Field<Double> PFtot = new Field<>(this);
    public Field<Double> PFa = new Field<>(this);
    public Field<Double> PFb = new Field<>(this);
    public Field<Double> PFc = new Field<>(this);
    public Field<Double> Ptot_kW = new Field<>(this);
    public Field<Double> Pa_kW = new Field<>(this);
    public Field<Double> Pb_kW = new Field<>(this);
    public Field<Double> Pc_kW = new Field<>(this);
    public Field<Double> Qtot_kvar = new Field<>(this);
    public Field<Double> Qa_kvar = new Field<>(this);
    public Field<Double> Qb_kvar = new Field<>(this);
    public Field<Double> Qc_kvar = new Field<>(this);
    public Field<Double> Stot_kVA = new Field<>(this);
    public Field<Double> Sa_kVA = new Field<>(this);
    public Field<Double> Sb_kVA = new Field<>(this);
    public Field<Double> Sc_kVA = new Field<>(this);
    public Field<Double> Va = new Field<>(this);
    public Field<Double> Vb = new Field<>(this);
    public Field<Double> Vc = new Field<>(this);
    public Field<Double> Ia = new Field<>(this);
    public Field<Double> Ib = new Field<>(this);
    public Field<Double> Ic = new Field<>(this);
    public Field<Double> IaAng = new Field<>(this);
    public Field<Double> IbAng = new Field<>(this);
    public Field<Double> IcAng = new Field<>(this);
    public Field<Double> VaAng = new Field<>(this);
    public Field<Double> VbAng = new Field<>(this);
    public Field<Double> VcAng = new Field<>(this);

    public ElsterDataView() {
        super("pec_tech_office_periodic");
    }

    /**
     * totalKwhP = Import_kWh = t1KwhP (Grid Power)  or t2KwhP (Gen Power)
     * totalKwhN = Export_kWh = t1KwhN (Grid Power) or t2KwhN (Gen Power)
     * totalKVarP = Q1_kvarh + Q4_kvarh = t1KVarP (Grid Power)  or t2KVarP (Gen Power)
     * totalKVarN = Q2_kvarh + Q3_kvarh = t1KVarN (Grid Power)  or t2KVarN (Gen Power)
     * <p>
     * RMS Voltage:
     * rmsL1V = Va
     * rmsL2V = Vb
     * rmsL3V = Vc
     * <p>
     * RMS Current:
     * rmsL1C = Ia
     * rmsL2C = Ib
     * rmsL3C = Ic
     *
     * @param reading
     * @return
     */
    public MeterReadingEntity update(MeterReadingEntity reading) {
        try {
            reading.elsterRefId.set(id.get());
            reading.totalKwhP.set(Import_kWh.get());
            reading.totalKwhN.set(Export_kWh.get());
            if (Q1_kvarh.get() != null && Q2_kvarh.get() != null) {
                reading.totalKVarP.set(Q1_kvarh.get() + Q2_kvarh.get());
            }
            if (Q3_kvarh.get() != null && Q4_kvarh.get() != null) {
                reading.totalKVarN.set(Q3_kvarh.get() + Q4_kvarh.get());
            }
            // map total to t1
            reading.getFields().stream().filter(f -> f.getName().startsWith("total") && !f.getName().startsWith("totalMax")).
                    forEach(f -> reading.getFields().getByName("t1" + f.getName().substring("total".length())).set(f.get()));

            reading.rmsL1V.set(Va.get());
            reading.rmsL2V.set(Vb.get());
            reading.rmsL3V.set(Vc.get());

            reading.rmsL1C.set(Ia.get());
            reading.rmsL2C.set(Ib.get());
            reading.rmsL3C.set(Ic.get());

            reading.entryTime.set(entrytime.get() != null?new Timestamp(TS_FORMAT.parse(entrytime.get()).getTime()):null);
            reading.entryDay.set(Integer.parseInt(dayFormat.format(reading.entryTime.get())));

            return reading;
        } catch (ParseException pe) {
            throw new RuntimeException(pe);
        }
    }

    public static DataSourceDB<ElsterDataView> getData(Connection connection, Long maxId, Integer batchSize) {
        return new DataSourceDB<>(ElsterDataView.class).getAll(connection,
                "select timestamp(date,time) as entrytime,pec_tech_office_periodic.* from pec_tech_office_periodic where id > ? order by date desc, time desc limit ?",
                maxId,
                batchSize);
    }

}
