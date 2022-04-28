package za.co.spsi.toolkit.crud.locale;

import org.vaadin.viritin.util.BrowserCookie;
import za.co.spsi.toolkit.entity.Entity;

/**
 * Created by jaspervdbijl on 2017/02/06.
 */
public class CookieEntity extends Entity {

    public CookieEntity(String name) {
        super(name);
    }

    public CookieEntity load() {
        BrowserCookie.detectCookieValue(getName(), s -> {
            if (s != null) {
                initFromJson(s);
            }
        });
//        Cookie cookie = VaadinLocaleHelper.getCookie(getName());
//        if (cookie != null) {
//            initFromJson(cookie.getValue());
//        }
        return this;
    }

    public void persist() {
        BrowserCookie.setCookie(getName(), getAsJson().toString());
    }
}
