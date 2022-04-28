package za.co.spsi.mdms.common.dao;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class PrepaidBatchData {

    private Timestamp entryTime;
    private Double registerReading;
    private Double registerConsumption;

}
