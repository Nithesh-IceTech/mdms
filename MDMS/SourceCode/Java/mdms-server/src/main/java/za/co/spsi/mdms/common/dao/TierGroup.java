package za.co.spsi.mdms.common.dao;

public enum TierGroup {

    T1P("T1P", "T1 Import"),
    T1N("T1N", "T1 Export"),
    T2P("T2P", "T2 Import"),
    T2N("T2N", "T2 Export"),
    UNKNOWN("UN", "Unknown");

    TierGroup(String code, String description) {
        this.code = code;
        this.desscription = description;
    }

    public static TierGroup getDescription(String code) {
        for(TierGroup e : values()) {
            if(e.code.equals(code))
                return e;
        }
        return UNKNOWN;
    }

    public String code, desscription;
}
