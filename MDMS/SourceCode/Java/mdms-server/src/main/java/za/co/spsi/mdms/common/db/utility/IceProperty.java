package za.co.spsi.mdms.common.db.utility;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.entity.Field;

import java.sql.Timestamp;

/**
 * Created by johan on 2017/03/30.
 */
public class IceProperty extends EntityDB {

    @Column(name = "ICE_PROPERTY_ID")
    public Field<Integer> icePropertyID = new Field<>(this);

//    @Column(name = "AD_CLIENT_ID")
//    public Field<Integer> adClientID = new Field<>(this);
//
    @Column(name = "AD_ORG_ID")
    public Field<Integer> adOrgID = new Field<>(this);

    @Column(name = "CREATED")
    public Field<Timestamp> created = new Field<>(this);

//    @Column(name = "CREATEDBY")
//    public Field<Integer> createdBy = new Field<>(this);
//
//    @Column(name = "ISACTIVE")
//    public Field<String> isActive = new Field<>(this);

    @Column(name = "UPDATED")
    public Field<Timestamp> updated = new Field<>(this);

//    @Column(name = "UPDATEDBY")
//    public Field<Integer> updatedBy = new Field<>(this);
//
//    @Column(name = "ICE_STAND_NO")
//    public Field<String> iceStandNo = new Field<>(this);

    @Column(name = "ICE_STREET_NAME")
    public Field<String> iceStreetName = new Field<>(this);

//    @Column(name = "ICE_PORTION")
//    public Field<String> icePortion = new Field<>(this);
//
//    @Column(name = "ICE_NO_OF_PORTIONS")
//    public Field<String> iceNoOfPortion = new Field<>(this);
//
//    @Column(name = "ICE_TOWNSHIP")
//    public Field<String> iceTownship = new Field<>(this);
//
//    @Column(name = "IMPROVEMENTS")
//    public Field<String> improvements = new Field<>(this);
//
//    @Column(name = "ICE_VALUATION")
//    public Field<Double> iceValuation = new Field<>(this);

    @Column(name = "ICE_LATITUDE")
    public Field<Double> iceLatitude = new Field<>(this);

    @Column(name = "ICE_LONGITUDE")
    public Field<Double> iceLongitude = new Field<>(this);

    @Column(name = "ICE_PROPERTY_TYPE_ID")
    public Field<Integer> icePropertyTypeID = new Field<>(this);

//    @Column(name = "LOAD_DEFAULT_SERVICES")
//    public Field<String> loadDefaultServices = new Field<>(this);

    @Column(name = "NAME")
    public Field<String> propertyName = new Field<>(this);

//    @Column(name = "ICE_PROPERTY_ZONING")
//    public Field<String> icePropertyZoning = new Field<>(this);
//
//    @Column(name = "ICE_MAP")
//    public Field<String> iceMap = new Field<>(this);
//
//    @Column(name = "ICE_STATUS")
//    public Field<String> iceStatus = new Field<>(this);
//
//    @Column(name = "ICE_INTRODUCE_PROPERTY")
//    public Field<String> iceIntroduceProperty = new Field<>(this);
//
//    @Column(name = "ICE_UNIT_NO")
//    public Field<String> iceUnitNo = new Field<>(this);
//
//    @Column(name = "ICE_NUMBER_OF_FLOORS")
//    public Field<Integer> iceNumberOfFloors = new Field<>(this);
//
//    @Column(name = "ICE_FLOOR_NUMBER")
//    public Field<String> iceFloorNumber = new Field<>(this);
//
    @Column(name = "ICE_BUILDING_COMPLEX_NAME")
    public Field<String> iceBuildingComplexName = new Field<>(this);

//    @Column(name = "ICE_LAND_REGISTER_NUMBER")
//    public Field<String> iceLandRegisterNumber = new Field<>(this);
//
//    @Column(name = "ICE_BUILDING_CONSTRUCTION_DATE")
//    public Field<Timestamp> iceBuildingConstructionDate = new Field<>(this);
//
//    @Column(name = "ICE_IS_EXEMPT")
//    public Field<String> iceIsExempt = new Field<>(this);
//
//    @Column(name = "ICE_EXEMPTION_REASON")
//    public Field<String> iceExemptionReason = new Field<>(this);
//
//    @Column(name = "ICE_SIZE")
//    public Field<Double> iceSize = new Field<>(this);

    @Column(name = "ICE_DISTRICT_ID")
    public Field<Integer> iceDistrictID = new Field<>(this);

//    @Column(name = "ICE_NEIGHBORHOOD_ID")
//    public Field<Integer> iceNeighbourhoodID = new Field<>(this);
//
//    @Column(name = "ICE_IS_HIGH_RISE_BUILDING")
//    public Field<String> iceHighRiseBuilding = new Field<>(this);
//
//    @Column(name = "ICE_IS_EXEMPT_TRANSFERTAX")
//    public Field<String> iceIsExemptTransferTax = new Field<>(this);
//
    @Column(name = "C_CITY_ID")
    public Field<Integer> cCityID = new Field<>(this);
//
//    @Column(name = "ICE_PROPTRANSEXEMPTREASON")
//    public Field<String> icePropTranExemptReason = new Field<>(this);
//
//    @Column(name = "ICE_PROPERTY_UU")
//    public Field<String> icePropertyUU = new Field<>(this);
//
//    @Column(name = "ICE_LAND_AREA")
//    public Field<Double> iceLandArea = new Field<>(this);
//
    @Column(name = "ICE_OCCUPIED_OR_COVERED_AREA")
    public Field<Double> iceOccupiedOrCoveredArea = new Field<>(this);
//
//    @Column(name = "ICE_YEAR_OF_CONSTRUCTION")
//    public Field<Integer> iceYearOfConstruction = new Field<>(this);

//    @Column(name = "VALUE")
//    public Field<String> value = new Field<>(this);

//    @Column(name = "ICE_PARCEL")
//    public Field<String> iceParcel = new Field<>(this);
//
//    @Column(name = "ICE_UNBUILT_AREA")
//    public Field<Double> iceUnBuildArea = new Field<>(this);
//
//    @Column(name = "ICE_VALUATION_OVERRIDE")
//    public Field<Double> iceValuationOverride = new Field<>(this);
//
//    @Column(name = "ICE_AREA_TYPE")
//    public Field<String> iceAreaType = new Field<>(this);
//
//    @Column(name = "ICE_RESIDENTIAL_DENSITY")
//    public Field<String> iceResidentialDensity = new Field<>(this);
//
//    @Column(name = "ICE_BUSINESS_LOCATION")
//    public Field<String> iceBusinessLocation = new Field<>(this);
//
//    @Column(name = "ICE_GLA")
//    public Field<Double> iceGlaNumber = new Field<>(this);
//
//    @Column(name = "DESCRIPTION")
//    public Field<String> iceDescription = new Field<>(this);

    @Column(name = "C_REGION_ID")
    public Field<Integer> cRegionID = new Field<>(this);

    @Column(name = "C_COUNTRY_ID")
    public Field<Integer> cCountryID = new Field<>(this);
//
//    @Column(name = "POSTAL")
//    public Field<String> postal = new Field<>(this);

    @Column(name = "ICE_PROPERTY_EXTERNAL_REF")
    public Field<String> icePropertyExternalRef = new Field<>(this);

    @Column(name = "ICE_PROPERTY_CODE")
    public Field<String> icePropertyCode = new Field<>(this);

//    @Column(name = "ICE_LAND_USAGE_ID")
//    public Field<Integer> iceLandUsageID = new Field<>(this);
//
//    @Column(name = "ISSUMMARY")
//    public Field<String> isSummary = new Field<>(this);
//
//    @Column(name = "ICE_PROPERTYENTITYTYPE_ID")
//    public Field<Integer> icePropertyEntityTypeID = new Field<>(this);
//
//    @Column(name = "ICE_PROPERTYUNITTYPE_ID")
//    public Field<Integer> icePropertyUnitTypeID = new Field<>(this);
//
//    @Column(name = "ICE_CREATE_BILLING")
//    public Field<String> iceCreateBilling = new Field<>(this);
//
//    @Column(name = "ICE_PROCPROPAGATETEMPLATES")
//    public Field<String> iceProcPropAgateTemplates = new Field<>(this);
//
//    @Column(name = "ICE_PROPAGATETEMPLATES")
//    public Field<String> icePropAgateTemplates = new Field<>(this);
//
//    @Column(name = "M_PRICELIST_ID")
//    public Field<Integer> mPriceListID = new Field<>(this);
//
//    @Column(name = "ICE_CONTRACT_CLASSIFICATION_ID")
//    public Field<Integer> iceContractClassificationID = new Field<>(this);
//
//    @Column(name = "ICE_ACC_NO")
//    public Field<String> iceAccNo = new Field<>(this);

    @Column(name = "ICE_STREET_NUMBER")
    public Field<String> iceStreetNumber = new Field<>(this);

//    @Column(name = "ICE_ACC_NO_OVRD")
//    public Field<String> iceAccNoOVRD = new Field<>(this);

    public IceProperty() {
        super("ICE_PROPERTY");
    }

}
