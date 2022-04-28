package za.co.spsi.mdms.common.db.interfaces;

import java.sql.Timestamp;

public interface MeterEntity {

    public enum Type {
        ELSTER,KAMSTRUP,NES,GENERIC
    }

    public String getName();

    public String getMeterId();

    public String getMeterSerialN();

    public Timestamp getInstallationDate();

    public String getMeterType();

//    public Boolean getWater();
//
//    public Boolean getLive();

    public Timestamp getLastCommsD();

    public Timestamp getMaxEntryTime();
}
