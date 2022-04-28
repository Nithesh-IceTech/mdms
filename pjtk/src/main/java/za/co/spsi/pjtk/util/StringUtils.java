package za.co.spsi.pjtk.util;

/**
 * Created by jaspervdb on 2/16/16.
 */
public class StringUtils {
    public static final String EMPTY = "";

    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isNotEmpty(String value) {
        return !isEmpty(value);
    }

    public static String defaultIfEmpty(String value, String defaultValue) {
        return value == null || value.trim().isEmpty() ? defaultValue : value;
    }

    public static boolean isNotBlank(String value) {
        return !isEmpty(value);
    }

    /**
     * add a space char before every case
     *
     * @param fieldName
     * @return
     */
    public static String addSpaceToCase(String fieldName) {
        StringBuilder sb = new StringBuilder();
        for (char c : fieldName.toCharArray()) {
            if (Character.isUpperCase(c)) {
                sb.append(" ");
            }
            sb.append(sb.length() == 0 ? Character.toUpperCase(c) : c);
        }
        return sb.toString();
    }

    public static String substringBetween(String value, String start, String end) {
        Assert.isTrue(value.indexOf(start) < value.lastIndexOf(end),
                "%s does not contain %s and %s in the right order", value, start, end);
        return value.substring(value.indexOf(start) + 1,
                value.substring(value.indexOf(start)).indexOf(end) + value.indexOf(start));
    }

}