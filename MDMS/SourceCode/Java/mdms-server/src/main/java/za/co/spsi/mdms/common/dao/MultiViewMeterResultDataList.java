package za.co.spsi.mdms.common.dao;

import lombok.Data;

import java.util.List;

@Data
public class MultiViewMeterResultDataList {

    private List<MultiViewMeterResultData> meterResultDataList;

    public MultiViewMeterResultDataList() {}

    public MultiViewMeterResultDataList(List<MultiViewMeterResultData> dataSet) {
        this.meterResultDataList = dataSet;
    }
}
