package za.co.spsi.toolkit.locale.factory;

import java.util.ArrayList;

/**
 * Created by jaspervdb on 15/06/18.
 */
public class LocaleResourceList extends ArrayList<LocaleResource> {

    public LocaleResource getByName(String name) {
        for (LocaleResource localeResource : this) {
            if (localeResource.getName().equals(name)) {
                return localeResource;
            }
        }
        return null;
    }

    public boolean contains(String name) {
        return getByName(name) != null;
    }

    public String getFieldDeclaration() {
        StringBuilder sb =new StringBuilder();
        for (LocaleResource localeResource : this) {
            sb.append(localeResource.getFieldDeclaration());
            sb.append("\n");
        }
        return sb.toString();
    }

    public String getStaticInit() {
        StringBuilder sb =new StringBuilder();
        for (LocaleResource localeResource : this) {
            sb.append(localeResource.getStaticInit());
            sb.append("\n");
        }
        return sb.toString();
    }
}
