package za.co.spsi.mdms.common.dao;

import lombok.Data;

@Data
public class MeterResultTouSummarizedRecord {

    private String attributeCode;
    private String attribute;
    private TierGroup tariff1ImportExportGroup;
    private TierGroup tariff2ImportExportGroup;

    private double touTariff1kWhTotal;
    private double touTariff1Percentage;

    private double touTariff2kWhTotal;
    private double touTariff2Percentage;

}
