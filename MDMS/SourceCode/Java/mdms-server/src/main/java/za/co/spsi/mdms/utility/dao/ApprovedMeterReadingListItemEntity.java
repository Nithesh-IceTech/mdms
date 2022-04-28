package za.co.spsi.mdms.utility.dao;

import za.co.spsi.mdms.common.db.survey.PecMeterEntity;
import za.co.spsi.mdms.common.db.survey.PecMeterRegisterEntity;
import za.co.spsi.mdms.common.db.survey.PecPropertyEntity;

import java.util.Date;

/**
 * Created by johan on 2017/03/13.
 */
public class ApprovedMeterReadingListItemEntity {
    private int adClientID;
    private int adOrgID;
    private Date created;
    private Date updated;
    private Date iceMeterReadingDate;
    private int iceMeterReadingListID;
    private int iceMeterReadingsID;
    private PecMeterEntity meterEntity;
    private String iceMeterID;
    private String iceMeterRegisterID;
    private String icePropertyID;
    private PecPropertyEntity propertyEntity;
    private PecMeterRegisterEntity meterRegisterEntity;

    public PecMeterEntity getMeterEntity() {
        return meterEntity;
    }

    public void setMeterEntity(PecMeterEntity meterEntity) {
        this.meterEntity = meterEntity;
    }

    public PecPropertyEntity getPropertyEntity() {
        return propertyEntity;
    }

    public void setPropertyEntity(PecPropertyEntity propertyEntity) {
        this.propertyEntity = propertyEntity;
    }

    public int getAdClientID() {
        return adClientID;
    }

    public PecMeterRegisterEntity getMeterRegisterEntity() {
        return meterRegisterEntity;
    }

    public  void setMeterRegisterEntity(PecMeterRegisterEntity meterRegisterEntity) {
        this.meterRegisterEntity = meterRegisterEntity;
    }

    public void setAdClientID(int adClientID) {
        this.adClientID = adClientID;
    }

    public int getAdOrgID() {
        return adOrgID;
    }

    public void setAdOrgID(int adOrgID) {
        this.adOrgID = adOrgID;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Date getIceMeterReadingDate() {
        return iceMeterReadingDate;
    }

    public void setIceMeterReadingDate(Date iceMeterReadingDate) {
        this.iceMeterReadingDate = iceMeterReadingDate;
    }

    public int getIceMeterReadingListID() {
        return iceMeterReadingListID;
    }

    public void setIceMeterReadingListID(int iceMeterReadingListID) {
        this.iceMeterReadingListID = iceMeterReadingListID;
    }

    public int getIceMeterReadingsID() {
        return iceMeterReadingsID;
    }

    public void setIceMeterReadingsID(int iceMeterReadingsID) {
        this.iceMeterReadingsID = iceMeterReadingsID;
    }

    public String getIceMeterID() {
        return iceMeterID;
    }

    public void setIceMeterID(String iceMeterID) {
        this.iceMeterID = iceMeterID;
    }

    public String getIceMeterRegisterID() {
        return iceMeterRegisterID;
    }

    public void setIceMeterRegisterID(String iceMeterRegisterID) {
        this.iceMeterRegisterID = iceMeterRegisterID;
    }

    public String getIcePropertyID() {
        return icePropertyID;
    }

    public void setIcePropertyID(String icePropertyID) {
        this.icePropertyID = icePropertyID;
    }
}
