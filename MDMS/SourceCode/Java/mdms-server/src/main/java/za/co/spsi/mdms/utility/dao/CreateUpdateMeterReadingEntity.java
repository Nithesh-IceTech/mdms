package za.co.spsi.mdms.utility.dao;

/**
 * Created by johan on 2017/03/14.
 */
public class CreateUpdateMeterReadingEntity {
    private boolean iceMeterNotAvailable;
    private int iceMeterReadingsListID;
    private int iceMeterReadingsID;
    private int iceMeterRegisterID;
    private String iceReasonNotCaptured;
    //iceEstimate not required as per ICEUN-3357
    //private boolean iceIsEstimate;
    private Double qty;

    //Meter reading date not required as IECUN-3357
    // private Date iceMeterReadingDate;
    private int iceMeterID;
    private Integer iceMeterReading;

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public boolean isIceMeterNotAvailable() {
        return iceMeterNotAvailable;
    }

    public void setIceMeterNotAvailable(boolean iceMeterNotAvailable) {
        this.iceMeterNotAvailable = iceMeterNotAvailable;
    }

    public Integer getIceMeterReadingsListID() {
        return iceMeterReadingsListID;
    }

    public void setIceMeterReadingsListID(int iceMeterReadingsListID) {
        this.iceMeterReadingsListID = iceMeterReadingsListID;
    }

    public int getIceMeterReadingsID() {
        return iceMeterReadingsID;
    }

    public void setIceMeterReadingsID(int iceMeterReadingsID) {
        this.iceMeterReadingsID = iceMeterReadingsID;
    }

    public int getIceMeterRegisterID() {
        return iceMeterRegisterID;
    }

    public void setIceMeterRegisterID(int iceMeterRegisterID) {
        this.iceMeterRegisterID = iceMeterRegisterID;
    }

    public String getIceReasonNotCaptured() {
        return iceReasonNotCaptured;
    }

    public void setIceReasonNotCaptured(String iceReasonNotCaptured) {
        this.iceReasonNotCaptured = iceReasonNotCaptured;
    }

    public int getIceMeterID() {
        return iceMeterID;
    }

    public void setIceMeterID(int iceMeterID) {
        this.iceMeterID = iceMeterID;
    }

    public Integer getIceMeterReading() {
        return iceMeterReading;
    }

    public void setIceMeterReading(Integer iceMeterReading) {
        this.iceMeterReading = iceMeterReading;
    }

}
