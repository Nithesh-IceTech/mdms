package za.co.spsi.mdms.common;

public class MdmsConstants {

    public enum Status {
        CREATED(0),
        PROCESSING(1),
        SUBMITED(2),
        WAITING(3),
        FAILED_WITH_REASON(4),
        FAILED_TIME_OUT(5),
        SUCCESSFUL(6),
        ERROR(7);

        int code;
        Status(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

    }

    public static final Integer ENTITY_STATUS_TABLET_PROCESSING = 1;
    public static final Integer ENTITY_STATUS_BACK_OFFICE_PROCESSING = 2;
    public static final Integer ENTITY_STATUS_BILLING_PROCESSING = 3;
    public static final Integer ENTITY_STATUS_BILLING_FAILED = 4;
    public static final Integer ENTITY_STATUS_DELETED = 5;

    public static final Integer REVIEW_STATUS_TO_BE_REVIEWED = 1;
    public static final Integer REVIEW_STATUS_REVIEWED = 2;
    public static final Integer REVIEW_STATUS_APPROVED = 3;

//    public static final String COUNTRY = "UCOUNTRY",
//            REGIONCITY = "UCITY",
//            CITYSUBURB = "UDISTRICT",
//            PROP_ENTITY_TYPE = "UPROPENTTYPE",PROP_TYPE = "UPROPTYPE",
//            PROVINCE = "UREGION";


    public static final String PROCESS_STATUS = "PROCESS_STATUS";
    public static final String SERVICE_GROUP = "SERVICEGROUP";
    public static final String SUPPLY_TYPE = "SUPPLYTYPE";
    public static final String METER_TYPE_LSTOPT="METERTYPELSTOPT";
    public static final String METERPLATFRMTYP="METERPLATFRMTYP";
    public static final String METER_MAKE = "METERMAKE";
    public static final String METER_MODEL = "METERMODEL";
    public static final String METER_CONFIG = "METERCONFIG";
    public static final String METER_OWNER = "METEROWNER";
    public static final String UNIT_TYPE = "PROPUNITTYPE";
    public static final String PROPERTY_TYPE = "PROPTYPE";
    public static final String PROPERTY_ENTITY_TYPE = "PROPENTITYTYPE";
    public static final String METER_REG_TYPE = "METERREGTYPE";

    public static final String DAY_OF_MONTH = "DAYOFMONTH";
    public static final String READING_TYPE ="READINGTYPE";
    public static final String UNIT_OF_MEASURE ="UNITOFMEASURE";

    public static final String NO_READING_REASON_CD = "NOREADINGREASON";

//
//    public enum GeoType {
//
//        LAND(1,"land_caption","green"),AREA(2,"area_caption","blue"),FARM_AREA(3,"farm_area_caption","Brown"),
//        CROP(4,"crop_caption","BurlyWood");
//
//        private Integer code;
//        private String name,color;
//
//
//        GeoType(Integer code,String name,String color) {
//            this.code = code;
//            this.name = name;
//            this.color = color;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public Integer getCode() {
//            return code;
//        }
//
//        public String getColor() {
//            return color;
//        }
//
//        public static GeoType getByCode(Integer code) {
//            for (GeoType geoType : values()) {
//                if (code.equals(geoType.getCode())) {
//                    return geoType;
//                }
//            }
//            throw new RuntimeException(String.format("Unable to locate Geo Type for Code %s",code));
//        }
//    }
//
//    public enum EntitySyncStatus {
//
//        TABLET(ENTITY_STATUS_TABLET_PROCESSING),BACK_OFFICE(ENTITY_STATUS_BACK_OFFICE_PROCESSING),
//        BILLING(ENTITY_STATUS_BILLING_PROCESSING),BILLING_FAILED(ENTITY_STATUS_BILLING_FAILED);
//
//        private Integer code;
//
//        EntitySyncStatus(Integer code) {
//            this.code = code;
//        }
//
//        public Integer getCode() {
//            return code;
//        }
//
//        public static EntitySyncStatus getByCode(Integer code) {
//            for (EntitySyncStatus status : values()) {
//                if (code.equals(status.getCode())) {
//                    return status;
//                }
//            }
//            throw new RuntimeException(String.format("Unable to locate Geo Type for Code %s",code));
//        }
//    }

}
