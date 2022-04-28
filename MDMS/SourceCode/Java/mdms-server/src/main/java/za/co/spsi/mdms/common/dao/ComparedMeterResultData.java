package za.co.spsi.mdms.common.dao;

import lombok.Data;
import za.co.spsi.toolkit.util.ObjectUtils;

/**
 */
@Data
public class ComparedMeterResultData extends MeterResultData {

    private Double totalKwhVar,totalKVarVar,totalKwhVarPer,totalKVarVarPer;

    public ComparedMeterResultData(MeterResultData data) {
        ObjectUtils.copy(this,data);
    }

    public ComparedMeterResultData init(MeterResultData primary,MeterResultData compared) {
        totalKwhVar = getNonNull(primary.getTotalKwhUsage()) - getNonNull(compared.getTotalKwhUsage());
        totalKVarVar = getNonNull(primary.getTotalKVarUsage()) - getNonNull(compared.getTotalKVarUsage());
        totalKwhVarPer = primary.getTotalKwhUsage() != null?totalKwhVar / primary.getTotalKwhUsage() * 100.0:null;
        totalKVarVarPer = primary.getTotalKVarUsage() != null?totalKVarVar / primary.getTotalKVarUsage() * 100.0:null;
        return this;
    }

}
