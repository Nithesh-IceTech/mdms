package za.co.spsi.toolkit.dao;

/**
 * Created with IntelliJ IDEA.
 * User: jaspervdbijl
 * Date: 2013/07/01
 * Time: 10:23 AM
 * ToolkitConstants relevant to the web app
 */
public class ToolkitConstants {

    public static final Integer ENTITY_STATUS_TABLET_PROCESSING = 1;
    public static final Integer ENTITY_STATUS_BACK_OFFICE_PROCESSING = 2;
    public static final Integer ENTITY_STATUS_BILLING_PROCESSING = 3;
    public static final Integer ENTITY_STATUS_BILLING_FAILED = 4;
    public static final Integer ENTITY_STATUS_DELETED = 5;

    public static final Integer REVIEW_STATUS_TO_BE_REVIEWED = 1;
    public static final Integer REVIEW_STATUS_REVIEWED = 2;
    public static final Integer REVIEW_STATUS_APPROVED = 3;

    public static final String TO_BE_REVIEWED_STATUS_COLOR = "yellow";
    public static final String REVIEWED_STATUS_COLOR = "orange";
    public static final String COMPLETED_STATUS_COLOR = "pink";

    public static final String CURRENCY_USD = "USD";
    public static final String CURRENCY_ZAR = "ZAR";
    public static final String CURRENCY_GBP = "GBP";
    public static final String CURRENCY_EUR = "EUR";
    public static final String CURRENCY_BWP = "BWP";
    public static final String ZERO_MONEY = "0.00";

    public static final Integer PAID_STATUS_PAID = 1;
    public static final Integer PAID_STATUS_UNPAID = 2;
    public static final Integer PAID_STATUS_PENDING = 3;

    //Languages
    public static final String ENGLISH = "en";
    public static final String PORTUGUESE = "pt";

    public enum GeoType {

        LAND(1, "land_caption", "land", "green"),
        LOCATION(1, "location", "location", "green"),
        BUILDING(2, "building", "building", "blue"),
        FARM_AREA(3, "farm_area_caption", "location", "Brown"),
        CROP(4, "crop_caption", "crop_caption", "BurlyWood");

        private Integer code;
        private String backofficeCaption, androidCaption, color;

        GeoType(Integer code, String backofficeCaption, String androidCaption, String color) {
            this.code = code;
            this.backofficeCaption = backofficeCaption;
            this.androidCaption = androidCaption;
            this.color = color;
        }

        public String getBackofficeCaption() {
            return backofficeCaption;
        }

        public String getAndroidCaption() {
            return androidCaption;
        }

        public Integer getCode() {
            return code;
        }

        public String getColor() {
            return color;
        }

        public static GeoType getByCode(Integer code) {
            for (GeoType geoType : values()) {
                if (code.equals(geoType.getCode())) {
                    return geoType;
                }
            }
            throw new RuntimeException(String.format("Unable to locate Geo Type for Code %s", code));
        }
    }

    public enum EntitySyncStatus {

        TABLET(ENTITY_STATUS_TABLET_PROCESSING), BACK_OFFICE(ENTITY_STATUS_BACK_OFFICE_PROCESSING),
        BILLING(ENTITY_STATUS_BILLING_PROCESSING), BILLING_FAILED(ENTITY_STATUS_BILLING_FAILED);

        private Integer code;

        EntitySyncStatus(Integer code) {
            this.code = code;
        }

        public Integer getCode() {
            return code;
        }

        public static EntitySyncStatus getByCode(Integer code) {
            for (EntitySyncStatus status : values()) {
                if (code.equals(status.getCode())) {
                    return status;
                }
            }
            throw new RuntimeException(String.format("Unable to locate Geo Type for Code %s", code));
        }
    }
}
