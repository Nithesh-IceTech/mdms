package za.co.spsi.toolkit.db.audit;

/**
 * Created by jaspervdb on 2016/10/31.
 * Interface to enable user admin integration
 */
public class AuditHelper {

    private static UIDCallback uidCallback;
    private static UIDRoleCallback uidRoleCallback;

    public static void setUIDCallback(UIDCallback p_uidCallback) {
        uidCallback = p_uidCallback;
    }

    public static void setUIDRoleCallback(UIDRoleCallback p_uidRoleCallback) {
        uidRoleCallback = p_uidRoleCallback;
    }

    public static String getUID() {
        return uidCallback != null?uidCallback.getUID():null;
    }

    public static boolean isSupervisor() {
        return uidRoleCallback != null?uidRoleCallback.isAdmin():false;
    }

    public static interface UIDCallback {
        String getUID();
    }

    public static interface UIDRoleCallback {
        boolean isAdmin();
    }

}
