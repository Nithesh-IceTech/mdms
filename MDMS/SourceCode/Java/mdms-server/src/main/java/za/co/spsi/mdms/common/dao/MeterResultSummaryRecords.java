package za.co.spsi.mdms.common.dao;

import za.co.spsi.mdms.processor.ano.SummaryField;
import za.co.spsi.toolkit.reflect.RefFields;
import za.co.spsi.toolkit.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static za.co.spsi.mdms.common.dao.MeterResultSummaryRecord.RATES;

/**
 * Created by jaspervdbijl on 2017/01/06.
 * lf = kwh / (kva * hours )
 * <p>
 * Water meter no 63760613
 * Checkers 18452495
 */
public class MeterResultSummaryRecords extends ArrayList<MeterResultSummaryRecord> {

    private static RefFields refFields = new RefFields(MeterResultData.class);

    public MeterResultSummaryRecords() {
    }

    public void update(MeterResultData meterResultData, String fields[]) {
        try {
            for (Field field : refFields.filter(SummaryField.class)) {
                if (fields.length == 0 || StringUtils.isEmpty(fields[0]) ||
                        Arrays.stream(fields).anyMatch(f -> f.equals(field.getName()) || Arrays.stream(RATES).anyMatch(r -> f.equals(field.getName()+r)))) {
                    Optional<MeterResultSummaryRecord> record = getByName(field.getName());
                    if (!record.isPresent()) {
                        add(new MeterResultSummaryRecord(meterResultData, field, fields, this));
                    } else {
                        record.get().update(meterResultData, field, fields, this);
                    }
                }
            }
        } catch (IllegalAccessException ie) {
            throw new RuntimeException(ie);
        }
    }

    public Optional<MeterResultSummaryRecord> getByName(final String name) {
        return stream().filter(m -> name.equals(m.getName())).findFirst();
    }

    public MeterResultSummaryRecords updateNulls() {
        stream().forEach(f -> f.updateNulls());
        return this;
    }

    public MeterResultSummaryRecords filter(List<String> fields) {
        removeAll(stream().filter(f -> !fields.isEmpty() && !fields.contains(f.getName())).collect(Collectors.toCollection(ArrayList::new)));
        return this;
    }

    public MeterResultSummaryRecords addTouSummary() {
        stream().forEach(f -> f.updateNulls());
        return this;
    }

    public static MeterResultSummaryRecords empty(String fields[]) {
        fields = fields.length == 0 || StringUtils.isEmpty(fields[0])?refFields.filter(SummaryField.class).stream().map(f -> f.getName())
                .collect(Collectors.toCollection(ArrayList::new)).toArray(new String[]{}):fields;
        MeterResultSummaryRecords records = new MeterResultSummaryRecords();
        Arrays.stream(fields).forEach(f -> records.add(new MeterResultSummaryRecord(f)));
        return records;
    }

}
