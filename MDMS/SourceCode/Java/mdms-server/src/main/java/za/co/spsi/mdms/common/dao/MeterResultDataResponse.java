package za.co.spsi.mdms.common.dao;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * Created by jaspervdbijl on 2017/03/05.
 */
@Data
public class MeterResultDataResponse {

    private List<MeterResultData> meterResultDataList;

    public MeterResultDataResponse() {

    }

    public MeterResultDataResponse(List<MeterResultData> dataSet) {
        this.meterResultDataList = dataSet;
    }

    public static MeterResultDataResponse empty() {
        return new MeterResultDataResponse(Arrays.asList(new MeterResultData()));
    }

    public static MeterResultDataResponse emptySeries() {
        return new MeterResultDataResponse(Arrays.asList(new MeterResultData("series1"),new MeterResultData("series2")));
    }

}
