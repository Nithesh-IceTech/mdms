package za.co.spsi.pjtk.util;


public class NumberUtils {

    public static boolean isInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException ne) {
            return false;
        }
    }

    public static boolean isNumber(String value) {
        try {
            Double.parseDouble(value);
            return true;
        } catch (NumberFormatException ne) {
            return false;
        }
    }

}
