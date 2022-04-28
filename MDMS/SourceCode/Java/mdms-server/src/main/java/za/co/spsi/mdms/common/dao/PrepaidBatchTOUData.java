package za.co.spsi.mdms.common.dao;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PrepaidBatchTOUData {

    private Timestamp nextEntryTime;
    private Timestamp nextEntryTimeLocal;
    private Double nextReading;

    private Timestamp prevEntryTime;
    private Timestamp prevEntryTimeLocal;
    private Double prevReading;

    private Double accumulatedConsumption;

    private Boolean isPPTOUMeterAndReg;

}
