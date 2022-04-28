package za.co.spsi.mdms.common.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import za.co.spsi.mdms.processor.ano.SummaryField;
import za.co.spsi.toolkit.reflect.RefFields;
import za.co.spsi.toolkit.util.Assert;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static za.co.spsi.mdms.common.dao.MeterResultData.getNonNull;

/**
 * Created by jaspervdbijl on 2017/01/06.
 * lf = kwh / (kva * hours )
 * <p>
 * Water meter no 63760613
 * Checkers 18452495
 */

@Data
public class MeterResultSummaryRecord {

    private static RefFields refFields = new RefFields(MeterResultData.class);

    private String name;

    @JsonIgnore
    private Double lastValue;
    private double average = 0d;
    public Double total;
    private Double minValue = null, maxValue = null;
    private Timestamp minDate, maxDate;
    private Double count = 0.0;
    private Double kva = 0.0;

    public static final String RATES[] = new String[]{"P","O","S"}, RATES_MAP[] = new String[]{"P","OP","STD"};

    public MeterResultSummaryRecord() {
    }

    public MeterResultSummaryRecord(String name,MeterResultSummaryRecord p,MeterResultSummaryRecord n) {
        this.name = name;
        this.count = p.count;
        this.total = getNonNull(p.total) - getNonNull(n.total);
        this.average = this.total / p.count;
        this.minValue = getNonNull(n.minValue) * -1;
        this.maxValue = getNonNull(p.maxValue);
        this.minDate = n.minDate;
        this.maxDate = p.maxDate;
        this.lastValue = p.lastValue;
    }

    public MeterResultSummaryRecord(String name) {
        this.name = name;
    }

    public MeterResultSummaryRecord(String name, Double total, double average, Double count, Double minValue, Double maxValue, Double lastValue, Double kva, Timestamp minDate, Timestamp maxDate) {
        this.name = name;
        this.average = average;
        this.total = total;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.minDate = minDate;
        this.maxDate = maxDate;
        this.lastValue = lastValue;
        this.count = count;
        this.kva = kva;
    }

    public MeterResultSummaryRecord(MeterResultData meterResultData, Field f, String fields[], MeterResultSummaryRecords records) throws IllegalAccessException {
        name = f.getName();
        update(meterResultData, f, fields, records);
    }

    public MeterResultSummaryRecord adjustTime(int mins) {
        minDate = minDate != null?new Timestamp(minDate.getTime()+ TimeUnit.MINUTES.toMillis(mins)):null;
        maxDate = maxDate != null?new Timestamp(maxDate.getTime()+ TimeUnit.MINUTES.toMillis(mins)):null;
        return this;
    }

    public void update(MeterResultData meterResultData, Field f, String fields[],MeterResultSummaryRecords records) {

        try {

            SummaryField sField = f.getAnnotation(SummaryField.class);
            Assert.notNull(sField, "Field %s must be a SummaryField", f.getName());

            Field minField = sField.negField().length() > 0 ? refFields.get(sField.negField()) : f;
            Field maxField = sField.posField().length() > 0 ? refFields.get(sField.posField()) : f;

            if(f.getName().contains("Usage")) {
                total = ( total == null ? 0.0 : MeterResultData.getNonNull((Double) f.get(meterResultData)) ) + MeterResultData.getNonNull(total);
            } else {
                total = MeterResultData.getNonNull((Double) f.get(meterResultData)) + MeterResultData.getNonNull(total);
            }

            count = count + 1;
            average = MeterResultData.getNonNull(total) / (count * 1d);

            if (f == minField) {
                if (minValue == null || getNonNull((Double) minField.get(meterResultData)) < getNonNull(minValue) ) {
                    minValue = (Double) minField.get(meterResultData);
                    minDate = meterResultData.getEntryTime();
                }
            } else {
                if (minValue == null || Math.abs(getNonNull((Double) minField.get(meterResultData))) * -1 < getNonNull(minValue) ) {
                    minValue = Math.abs(getNonNull((Double) minField.get(meterResultData))) * -1;
                    minDate = meterResultData.getEntryTime();
                }
            }
            if (maxValue == null || getNonNull((Double) maxField.get(meterResultData)) > getNonNull(maxValue) ) {
                maxValue = (Double) maxField.get(meterResultData);
                maxDate = meterResultData.getEntryTime();
            }
            if (sField.tou() && records != null) {
                // add the tou references
                for (int i =0;i < RATES.length;i++) {
                    String fName = f.getName() + RATES[i];
                    if (fields == null || fields.length == 0 || Arrays.stream(fields).anyMatch(fname -> fname.equals(fName))) {
                        if (!records.getByName(fName).isPresent()) {
                            records.add(new MeterResultSummaryRecord(fName));
                        }
                        // TODO: DESK-1148 - IED-3483
                        if (RATES_MAP[i].equals(meterResultData.getTou1())) {
                            records.getByName(fName).get().update(meterResultData, f, fields, null);
                        }
                    }
                }

            }
        } catch (IllegalAccessException ie) {
            throw new RuntimeException(ie);
        }
    }


    public MeterResultSummaryRecord updateNulls() {
        Optional<Field> mField  = refFields.stream().filter(f -> f.getName().equals(name)).findFirst();
        if (mField.isPresent() && !mField.get().getAnnotation(SummaryField.class).exportTotal()) {
            total = null;
        }
        return this;
    }


    protected boolean canEqual(final Object other) {
        return other instanceof MeterResultSummaryRecord;
    }

}
