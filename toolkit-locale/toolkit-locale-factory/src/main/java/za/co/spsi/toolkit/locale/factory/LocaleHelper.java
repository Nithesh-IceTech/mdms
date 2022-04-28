package za.co.spsi.toolkit.locale.factory;


import za.co.spsi.toolkit.util.StringList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaspervdb on 15/06/18.
 */
public class LocaleHelper {

    private static StringList txNames = new StringList();

    private static List<LocaleResource> localeResourceList = new ArrayList<LocaleResource>();

    public static void add(LocaleResource localeResource) {
        localeResourceList.add(localeResource);
    }

    public static List<LocaleResource> getLocaleResourceList() {
        return localeResourceList;
    }

    private static boolean isNonWordCharacter(char value) {
        return (value < 'A' || value > 'z') && !(value == '<' || value == '>');
    }

    public static String[] getKey(String value, String locale) {
        // filter out any non applicable characters
        if (value != null && !value.isEmpty()) {
            for (; value.startsWith(" "); value = value.substring(1)) ;
            value = value.trim();
            if (!value.isEmpty()) {
                // filter out any non applicable characters
                for (LocaleResource localeResource : localeResourceList) {
                    if (localeResource.getValueList().containsIgnoreCase(value)) {
                        return new String[]{localeResource.getName()};
                    }
                    // check with all the tx names
                    for (String txName : txNames) {
                        if (value.length() > (txName + " - ").length()) {
                            StringList sl = localeResource.getValueList().getIgnoreCaseAsList(value.substring((txName + " - ").length()));
                            for (int i = 0; i < sl.size(); i++) {
                                if (value.equals(txName + " - " + sl.get(i))) {
                                    return new String[]{localeResource.getName(), txName};
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public static void addTxName(String txName) {
        if (!txNames.contains(txName)) {
            txNames.add(txName);
        }
    }

    public static String getValueNoException(String name, String locale, String context) {
        for (LocaleResource localeResource : localeResourceList) {
            if (localeResource.getName().equals(name)) {
                return localeResource.getValue(locale, context);
            }
        }
        return null;
    }

    public static String getValue(String name, String locale, String context) {
        String value = getValueNoException(name, locale, context);
        if (value == null) {
//            throw new RuntimeException(String.format("Unable to locate name %s on locale %s context %s", name, locale,context));
            return name;
        }
        return value;
    }

    public static List<String> getLocales() {
        List<String> locales = new ArrayList<>();
        for (LocaleResource localeResource : localeResourceList) {
            for (String l : localeResource.getLocaleList()) {
                if (!locales.contains(l)) {
                    locales.add(l);
                }
            }
        }
        return locales;
    }
//
//    public static String getValueNoException(String name) {
//        return getValueNoException(name, ToolkitConstants.getLocale(), ToolkitConstants.getContext());
//    }
//
//    public static String getValue(String name) {
//        return getValue(name, ToolkitConstants.getLocale(), ToolkitConstants.getContext());
//    }
//
//    public static String getValueNoException(String name, String locale) {
//        return getValueNoException(name, locale, ToolkitConstants.getContext());
//    }
//
//    public static String getValue(String name, String locale) {
//        return getValue(name, locale, ToolkitConstants.getContext());
//    }

//    public static String getTranslatedCaption(String name) {
//        String value = getValueNoException(name);
//        return value != null?value:name;
//    }


    public static String getDefaultLocale() {
        for (LocaleResource localeResource : localeResourceList) {
            if (!localeResource.getLocaleList().isEmpty()) {
                return localeResource.getLocaleList().get(0);
            }
        }
        return null;
    }
}
