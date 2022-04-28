package za.co.spsi.mdms.common.dao;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MeterResultTouSummarizedResponse {

    private List<MeterResultTouSummarizedRecord> meterResultTouSummarizedRecordList = new ArrayList<>();

    public void updateDescriptions() {
        for (MeterResultTouSummarizedRecord aMeterResultTouSummarizedRecord: meterResultTouSummarizedRecordList) {
            aMeterResultTouSummarizedRecord.setAttribute(
                    TouGroup.getDescription(
                            aMeterResultTouSummarizedRecord.getAttributeCode()));

        }

    }
    public void updatePercentages() {

        double t1kWhPGrandTotal = 0d;
        double t1kWhNGrandTotal = 0d;
        double t2kWhPGrandTotal = 0d;
        double t2kWhNGrandTotal = 0d;

        double t1kWhPRunningPercentage = 0d;
        double t1kWhNRunningPercentage = 0d;
        double t2kWhPRunningPercentage = 0d;
        double t2kWhNRunningPercentage = 0d;

        // Add totals
        for (MeterResultTouSummarizedRecord aMeterResultTouSummarizedRecord: meterResultTouSummarizedRecordList) {

            switch (aMeterResultTouSummarizedRecord.getTariff1ImportExportGroup()) {
                case T1P:
                    t1kWhPGrandTotal = t1kWhPGrandTotal + aMeterResultTouSummarizedRecord.getTouTariff1kWhTotal();
                    break;
                case T1N:
                    t1kWhNGrandTotal = t1kWhNGrandTotal + aMeterResultTouSummarizedRecord.getTouTariff1kWhTotal();
                    break;
                default:
                    break;

            }
            switch (aMeterResultTouSummarizedRecord.getTariff2ImportExportGroup()) {
                case T2P:
                    t2kWhPGrandTotal = t2kWhPGrandTotal + aMeterResultTouSummarizedRecord.getTouTariff2kWhTotal();
                    break;
                case T2N:
                    t2kWhNGrandTotal = t2kWhNGrandTotal + aMeterResultTouSummarizedRecord.getTouTariff2kWhTotal();
                    break;
                default:
                    break;

            }
        }

        // Update percentages
        for (MeterResultTouSummarizedRecord aMeterResultTouSummarizedRecord: meterResultTouSummarizedRecordList) {

            switch (aMeterResultTouSummarizedRecord.getTariff1ImportExportGroup()) {
                case T1P:
                    aMeterResultTouSummarizedRecord.
                            setTouTariff1Percentage(t1kWhPGrandTotal != 0 ?
                                    aMeterResultTouSummarizedRecord.getTouTariff1kWhTotal() /
                                            t1kWhPGrandTotal * 100: 0);
                    t1kWhPRunningPercentage = t1kWhPRunningPercentage + aMeterResultTouSummarizedRecord.getTouTariff1Percentage();
                    break;
                case T1N:
                    aMeterResultTouSummarizedRecord.
                            setTouTariff1Percentage(t1kWhNGrandTotal != 0 ?
                                    aMeterResultTouSummarizedRecord.getTouTariff1kWhTotal() /
                                            t1kWhNGrandTotal * 100: 0);
                    t1kWhNRunningPercentage = t1kWhNRunningPercentage + aMeterResultTouSummarizedRecord.getTouTariff1Percentage();
                    break;

                default:
            }
            switch (aMeterResultTouSummarizedRecord.getTariff2ImportExportGroup()) {
                case T2P:
                    aMeterResultTouSummarizedRecord.
                            setTouTariff2Percentage(t2kWhPGrandTotal != 0 ?
                                    aMeterResultTouSummarizedRecord.getTouTariff2kWhTotal()/
                                            t2kWhPGrandTotal * 100: 0);
                    t2kWhPRunningPercentage = t2kWhPRunningPercentage + aMeterResultTouSummarizedRecord.getTouTariff2Percentage();
                    break;
                case T2N:
                    aMeterResultTouSummarizedRecord.
                            setTouTariff2Percentage(t2kWhNGrandTotal != 0 ?
                                    aMeterResultTouSummarizedRecord.getTouTariff2kWhTotal()/
                                            t2kWhNGrandTotal * 100: 0);
                    t2kWhNRunningPercentage = t2kWhNRunningPercentage + aMeterResultTouSummarizedRecord.getTouTariff2Percentage();
                    break;
                default:
            }

        }

        // P Total
        MeterResultTouSummarizedRecord recordPTot = new MeterResultTouSummarizedRecord();
        recordPTot.setAttributeCode(TouGroup.TOT_P.code);
        recordPTot.setTouTariff1kWhTotal(t1kWhPGrandTotal);
        recordPTot.setTouTariff2kWhTotal(t2kWhPGrandTotal);
        recordPTot.setTariff1ImportExportGroup(TierGroup.T1P);
        recordPTot.setTariff2ImportExportGroup(TierGroup.T2N);
        recordPTot.setTouTariff1Percentage(t1kWhPRunningPercentage);
        recordPTot.setTouTariff2Percentage(t2kWhPRunningPercentage);
        meterResultTouSummarizedRecordList.add(recordPTot);

        // N Total
        MeterResultTouSummarizedRecord recordTotN = new MeterResultTouSummarizedRecord();
        recordTotN.setAttributeCode(TouGroup.TOT_N.code);
        recordTotN.setTouTariff1kWhTotal(t1kWhNGrandTotal);
        recordTotN.setTouTariff2kWhTotal(t2kWhPGrandTotal);
        recordTotN.setTouTariff1Percentage(t1kWhNRunningPercentage);
        recordTotN.setTouTariff2Percentage(t2kWhNRunningPercentage);
        recordTotN.setTariff1ImportExportGroup(TierGroup.T1N);
        recordTotN.setTariff2ImportExportGroup(TierGroup.T2N);
        meterResultTouSummarizedRecordList.add(recordTotN);

    }

}
