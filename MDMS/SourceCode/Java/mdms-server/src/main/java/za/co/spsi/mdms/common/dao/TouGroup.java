package za.co.spsi.mdms.common.dao;

public enum TouGroup {
    P_P("P_P", "kWh Import Peak"),
    STD_P("STD_P", "kWh Import Standard"),
    OP_P("OP_P", "kWh Import Off-Peak"),
    TOT_P("TOT_P", "kWh Import Total"),

    P_N("P_N", "kWh Export Peak"),
    STD_N("STD_N", "kWh Export Standard"),
    OP_N("OP_N", "kWh Export Off-Peak"),
    TOT_N("TOT_N", "kWh Export Total"),

    P("_P", "Import"),
    N("_N", "Export"),

    UNKNOWN("UN", "Unknown");

    TouGroup(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static String getDescription(String code) {
        for(TouGroup e : values()) {
            if(e.code.equals(code))
                return e.description;
        }
        return UNKNOWN.description;
    }

    public String code, description;

}
