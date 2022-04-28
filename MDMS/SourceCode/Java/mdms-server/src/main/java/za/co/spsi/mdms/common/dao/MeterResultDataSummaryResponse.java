package za.co.spsi.mdms.common.dao;

import lombok.Data;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Created by jaspervdbijl on 2017/03/05.
 */
@Data
public class MeterResultDataSummaryResponse {

    private MeterResultSummaryRecords meterResultDataList = new MeterResultSummaryRecords();

    public MeterResultDataSummaryResponse() {

    }

    public MeterResultDataSummaryResponse(MeterResultSummaryRecords meterResultDataList) {
        this.meterResultDataList = meterResultDataList;
    }

    private void updateRefMin(MeterResultDataArray meterResults, Optional<MeterResultSummaryRecord> record, Optional<MeterResultSummaryRecord> ref) {
        Double tempT1PF = 0.0;
        Double tempT2PF = 0.0;
        Double tempTotalPF = 0.0;
        if (record.isPresent() && ref.isPresent()) {
            Optional<MeterResultData> value = meterResults.get(ref.get().getMinDate());
            if (value.isPresent()) {

                switch ( ref.get().getName() ) {
                    case "t1KVA":
                        tempT1PF = value.get().getT1PF();
                        break;
                    case "t2KVA":
                        tempT2PF = value.get().getT2PF();
                        break;
                    case "totalKVA":
                        tempTotalPF = value.get().getTotalPF();
                        break;
                }

                switch( record.get().getName() ) {
                    case "t1PF":
                        record.get().setMinValue( tempT1PF );
                        break;
                    case "t2PF":
                        record.get().setMinValue( tempT2PF );
                        break;
                    case "totalPF":
                        record.get().setMinValue( tempTotalPF );
                        break;
                }

                record.get().setMinDate(ref.get().getMinDate());
            }
        }
    }

    private void updateRefMax(MeterResultDataArray meterResults, Optional<MeterResultSummaryRecord> record, Optional<MeterResultSummaryRecord> ref) {
        Double tempT1PF = 0.0;
        Double tempT2PF = 0.0;
        Double tempTotalPF = 0.0;
        if (record.isPresent() && ref.isPresent()) {
            Optional<MeterResultData> value = meterResults.get(ref.get().getMaxDate());
            if (value.isPresent()) {

                switch ( ref.get().getName() ) {
                    case "t1KVA":
                        tempT1PF = value.get().getT1PF();
                        break;
                    case "t2KVA":
                        tempT2PF = value.get().getT2PF();
                        break;
                    case "totalKVA":
                        tempTotalPF = value.get().getTotalPF();
                        break;
                }

                switch( record.get().getName() ) {
                    case "t1PF":
                        record.get().setMaxValue( tempT1PF );
                        break;
                    case "t2PF":
                        record.get().setMaxValue( tempT2PF );
                        break;
                    case "totalPF":
                        record.get().setMaxValue( tempTotalPF );
                        break;
                }

                record.get().setMaxDate(ref.get().getMaxDate());
            }
        }
    }

    public MeterResultDataSummaryResponse(MeterResultDataArray meterResults, String fields[]) {
        meterResults.stream().forEach(m -> meterResultDataList.update(m, fields));
        // adjust the power factor to be in line with kva
        Stream.of("t1", "t2", "total").forEach(s -> {
            updateRefMin(meterResults, meterResultDataList.getByName(s + "PF"), meterResultDataList.getByName(s + "KVA"));
            updateRefMax(meterResults, meterResultDataList.getByName(s + "PF"), meterResultDataList.getByName(s + "KVA"));
        });
        meterResultDataList.updateNulls().filter(Arrays.asList(fields));
    }

    public static MeterResultDataSummaryResponse empty(String fields[]) {
        return new MeterResultDataSummaryResponse(MeterResultSummaryRecords.empty(fields));
    }
}
