package za.co.spsi.mdms.common.dao;

import za.co.spsi.mdms.processor.ano.SummaryField;
import za.co.spsi.mdms.processor.processor.SummaryProcessor;
import za.co.spsi.toolkit.reflect.RefFields;

import java.lang.reflect.Field;
import java.util.stream.Stream;

/**
 * Created by jaspervdbijl on 2017/03/06.
 */
public class AbstractMeterResultDataSummary {

    private RefFields refFields = new RefFields(MeterResultData.class);

    public void update(MeterResultData meterResultData) {
        refFields.filter(SummaryField.class).stream().forEach(f -> {
            try {
                Field total = getClass().getDeclaredField(f.getName()+ SummaryProcessor.TOTAL);
                Field average = getClass().getDeclaredField(f.getName()+ SummaryProcessor.AVERAGE);
                Field count = getClass().getDeclaredField(f.getName()+ SummaryProcessor.COUNT);
                Field minDate = getClass().getDeclaredField(f.getName()+ SummaryProcessor.MIN_DATE);
                Field maxDate = getClass().getDeclaredField(f.getName()+ SummaryProcessor.MAX_DATE);
                Field minValue = getClass().getDeclaredField(f.getName()+ SummaryProcessor.MIN_VALUE);
                Field maxValue = getClass().getDeclaredField(f.getName()+ SummaryProcessor.MAX_VALUE);

                Stream.of(total,average,count,minDate,minValue,maxDate,maxValue).forEach(i -> i.setAccessible(true));

                total.set(this,MeterResultData.getNonNull((Double) f.get(meterResultData)) + MeterResultData.getNonNull((Double) total.get(this)));
                count.set(this,((Integer)count.get(this))+1);
                average.set(this,MeterResultData.getNonNull((Double)total.get(this)) / ((Integer)count.get(this)*1d));

                if (minValue.get(this) == null || MeterResultData.getNonNull((Double)f.get(meterResultData)) < MeterResultData.getNonNull((Double)minValue.get(this))) {
                    minValue.set(this,f.get(meterResultData));
                    minDate.set(this,meterResultData.getEntryTime());
                }
                if (MeterResultData.getNonNull((Double)f.get(meterResultData)) > MeterResultData.getNonNull((Double)maxValue.get(this))) {
                    maxValue.set(this,f.get(meterResultData));
                    maxDate.set(this,meterResultData.getEntryTime());
                }


            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
