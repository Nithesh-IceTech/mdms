package za.co.spsi.mdms.common.dao;

import lombok.Data;
import za.co.spsi.toolkit.util.Assert;
import za.co.spsi.toolkit.util.ObjectUtils;

import java.sql.Timestamp;

@Data
public class MultiViewMeterResultData {


    private Timestamp entryTime,plotEntryTime;

    private Integer weekDay;

    private String series,tou;

    private Double data;

    public MultiViewMeterResultData() {
    }

    public MultiViewMeterResultData(MeterResultData data,String field) {
        try {
            Assert.notNull(MeterResultData.refFields.get(field) != null,"Field %s does not exist",field);
            ObjectUtils.copy(this, data);
            this.data = (Double) MeterResultData.refFields.get(field).get(data);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
