package za.co.spsi.mdms.common.db.utility;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.entity.Field;

import java.sql.Timestamp;

/**
 * Created by johan on 2017/03/30.
 */
public class IceMeter extends EntityDB {

//    @Column(name = "AD_CLIENT_ID")
//    public Field<Integer> adClientId = new Field<>(this);
//
//    @Column(name = "AD_ORG_ID")
//    public Field<Integer> adOrgId = new Field<>(this);

    @Column(name = "CREATED")
    public Field<Timestamp> created = new Field<>(this);

//    @Column(name = "CREATEDBY")
//    public Field<Integer> createdBy = new Field<>(this);

    @Column(name = "DESCRIPTION")
    public Field<String> description = new Field<>(this);

    @Column(name = "ICE_EXTERNAL_REFERENCE")
    public Field<String> iceExternalReference = new Field<>(this);

    @Column(name = "ICE_LATITUDE")
    public Field<Double> iceLatitude = new Field<>(this);

    @Column(name = "ICE_LONGITUDE")
    public Field<Double> iceLongitude = new Field<>(this);

//    @Column(name = "ICE_METER_ACTUAL_SIZE")
//    public Field<Integer> iceMeterActualSize = new Field<>(this);

    @Column(name = "ICE_METER_AUTO_AVERAGE")
    public Field<String> iceMeterAutoAverage = new Field<>(this);

//    @Column(name = "ICE_METER_AUTO_RESET")
//    public Field<String> iceMeterAutoreset = new Field<>(this);
//
//    @Column(name = "ICE_METER_BREAKER_SIZE_ACT")
//    public Field<Integer> iceMeterBreakerSizeAct = new Field<>(this);
//
//    @Column(name = "ICE_METER_BREAKER_SIZE_REL")
//    public Field<Integer> iceMeterBreakerSizeRel = new Field<>(this);
//
//    @Column(name = "ICE_METER_CTRATIO")
//    public Field<Integer> iceMeterCTRatio = new Field<>(this);

    @Column(name = "ICE_METER_CONFIGURATION")
    public Field<String> iceMeterConfiguration = new Field<>(this);

    @Column(name = "ICE_METER_DIGITS")
    public Field<Integer> iceMeterDigits = new Field<>(this);

    @Column(name = "ICE_METER_ID")
    public Field<String> iceMeterID = new Field<>(this);

    @Column(name = "ICE_METER_LOCATION")
    public Field<String> iceMeterLocation = new Field<>(this);

//    @Column(name = "ICE_METER_MAKE")
//    public Field<String> iceMeterMake = new Field<>(this);
//
//    @Column(name = "ICE_METER_MODEL")
//    public Field<String> iceMeterModel = new Field<>(this);

    @Column(name = "ICE_METER_NUMBER")
    public Field<String> iceMeterNumber = new Field<>(this);

//    @Column(name = "ICE_METER_OWNER")
//    public Field<String> iceMeterOwner = new Field<>(this);
//
//    @Column(name = "ICE_METER_PHASE")
//    public Field<String> iceMeterPhase = new Field<>(this);

    @Column(name = "ICE_METER_PREPAID")
    public Field<String> iceMeterPrepaid = new Field<>(this);

//    @Column(name = "ICE_METER_READINGFACTOR")
//    public Field<Integer> iceMeterReadingFactor = new Field<>(this);
//
//    @Column(name = "ICE_METER_RELEVANT_SIZE")
//    public Field<Integer> iceMeterRelevantSize = new Field<>(this);
//
//    @Column(name = "ICE_METER_STATUS")
//    public Field<String> iceMeterStatus = new Field<>(this);
//
//    @Column(name = "ICE_METER_STATUS_DATE")
//    public Field<Timestamp> iceMeterStatusDate = new Field<>(this);

    @Column(name = "ICE_METER_SUPPLYTYPE")
    public Field<String> iceMeterSupplyType = new Field<>(this);

    @Column(name = "ICE_SERVICE_GROUP_ID")
    public Field<Integer> iceServiceGroupID = new Field<>(this);

//    @Column(name = "ICE_METER_UU")
//    public Field<String> iceMeterUU = new Field<>(this);

    @Column(name = "ISACTIVE")
    public Field<String> isActive = new Field<>(this);

//    @Column(name = "ISSUMMARY")
//    public Field<String> isSummary = new Field<>(this);
//
//    @Column(name = "NAME")
//    public Field<String> meterName = new Field<>(this);

    @Column(name = "UPDATED")
    public Field<Timestamp> updated = new Field<>(this);

//    @Column(name = "UPDATEDBY")
//    public Field<Integer> updatedBy = new Field<>(this);

    @Column(name = "VALUE")
    public Field<String> value = new Field<>(this);

//    @Column(name = "ICE_METER_METERFACTOR")
//    public Field<Double> iceMeterMeterFactor = new Field<>(this);
//
//    @Column(name = "ICE_METER_VTFACTOR")
//    public Field<Double> iceMeterVTFactor = new Field<>(this);
//
//    @Column(name = "ICE_METER_VTRATIO")
//    public Field<Integer> iceMeterVTratio = new Field<>(this);
//
    @Column(name = "ICE_METER_MAKE_ID")
    public Field<Integer> iceMeterMakeID = new Field<>(this);

    @Column(name = "ICE_METER_MODEL_ID")
    public Field<Integer> iceMeterModelID = new Field<>(this);

    @Column(name = "ICE_METERREAD")
    public Field<String> iceMeterRead = new Field<>(this);

//    @Column(name = "ICE_METER_PHASE_ID")
//    public Field<Integer> iceMeterPhaseID = new Field<>(this);
//
//    @Column(name = "ICE_METER_CTRATIO2")
//    public Field<Integer> iceMeterCTRatio2 = new Field<>(this);
//
//    @Column(name = "ICE_METER_VTRATIO2")
//    public Field<Integer> iceMeterVTRatio2 = new Field<>(this);
//
//    @Column(name = "ICE_METER_VTMETERFACTOR")
//    public Field<Integer> iceMeterVTMeterFactor = new Field<>(this);
//
//    @Column(name = "ICE_METER_WATERMETERFACTOR")
//    public Field<Integer> iceMeterWaterMeterFactor = new Field<>(this);
//
//    @Column(name = "ICE_METER_CTREADINGFACTOR")
//    public Field<Integer> iceMeterCTReadingFactor = new Field<>(this);
//
//    @Column(name = "ICE_METER_CTMETERFACTOR")
//    public Field<Integer> iceMeterCTRMeterFactor = new Field<>(this);
//
//    @Column(name = "ICE_METER_VTREADINGFACTOR")
//    public Field<Double> iceMeterVTReadingFactor = new Field<>(this);

    public IceMeter() {
        super("ICE_METER");
    }

}