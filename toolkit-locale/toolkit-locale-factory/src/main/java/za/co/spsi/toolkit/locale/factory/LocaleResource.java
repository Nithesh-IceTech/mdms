package za.co.spsi.toolkit.locale.factory;


import za.co.spsi.toolkit.util.StringList;

/**
 * Created by jaspervdb on 15/06/17.
 */
public class LocaleResource {
    private String name;

    private StringList localeList = new StringList(), valueList = new StringList(), contextList = new StringList();

    public LocaleResource(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addLocale(String locale, String context, String value) {
        for (int i = 0;i < localeList.size();i++) {
            if (locale.equals(localeList.get(i)) && context.equals(contextList.get(i))) {
                valueList.set(i,value);
                return;
            }
        }
        localeList.add(locale);
        contextList.add(context);
        valueList.add(value);
    }

    public String getValue(String locale, String context) {
        for (int i = 0; i < localeList.size(); i++) {
            if (localeList.get(i).equals(locale) && (context == null || contextList.get(i).equals(context))) {
                return valueList.get(i);
            }
        }
        return null;
    }

    public StringList getLocaleList() {
        return localeList;
    }

    public StringList getContextList() {
        return contextList;
    }

    public StringList getValueList() {
        return valueList;
    }

    public String getFieldDeclaration() {
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("\tpublic static final String %s = \"%s\";\n", name.toUpperCase(), name));
        sb.append(String.format("\tpublic static LocaleResource %s_RESOURCE = new LocaleResource(\"%s\");", name.toUpperCase(), name));
        return sb.toString();
    }

    public String getStaticInit() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < localeList.size(); i++) {
            sb.append(String.format("\t\t%s_RESOURCE.addLocale(\"%s\",\"%s\",\"%s\");\n", name.toUpperCase(),
                    localeList.get(i), contextList.get(i), valueList.get(i)));
            if (i == 0) {
                sb.append(String.format("\t\tLocaleHelper.add(%s_RESOURCE);\n", name.toUpperCase()));
            }
        }
        return sb.toString();
    }


}
