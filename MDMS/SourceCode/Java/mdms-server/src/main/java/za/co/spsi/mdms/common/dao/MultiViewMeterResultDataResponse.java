package za.co.spsi.mdms.common.dao;

import lombok.Data;

import java.util.List;

/**
 * Created by jaspervdbijl on 2017/03/05.
 */
@Data
public class MultiViewMeterResultDataResponse {

    private List<MultiViewMeterResultData> meterResultDataList;

    public MultiViewMeterResultDataResponse() {

    }

    public MultiViewMeterResultDataResponse(List<MultiViewMeterResultData> dataSet) {
        this.meterResultDataList = dataSet;
    }
}
