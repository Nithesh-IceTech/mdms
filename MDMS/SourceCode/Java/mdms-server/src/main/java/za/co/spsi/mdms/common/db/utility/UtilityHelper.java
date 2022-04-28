package za.co.spsi.mdms.common.db.utility;

/**
 * Created by jaspervdbijl on 2017/03/31.
 */
public class UtilityHelper {

    public static String getStringFromInt(Integer number) {
        return number == null?"":""+number;
    }

    public static Integer getIntFromString(String number) {
        try {
            return number != null?Integer.parseInt(number):null;
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public static Long getLongFromString(String number) {
        try {
            return number != null?Long.parseLong(number):null;
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public static Integer parseBoolean(String option) {
        if (option == null)
            return null;
        return option.equalsIgnoreCase("Y") ? 1 : 2;
    }

}
