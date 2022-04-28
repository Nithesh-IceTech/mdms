package za.co.spsi.toolkit.crud.locale;

import com.vaadin.ui.UI;
import za.co.spsi.toolkit.entity.Field;

/**
 * Created by jaspervdbijl on 2017/02/06.
 */
public class LocaleCookieEntity extends CookieEntity {

    public static Class<? extends LocaleCookieEntity> LOCALE_COOKIE = LocaleCookieEntity.class;

    public static Class<? extends LocaleCookieEntity> getLocaleCookie() {
        return LOCALE_COOKIE;
    }

    public static void setLocaleCookie(Class<? extends LocaleCookieEntity> localeCookie) {
        LOCALE_COOKIE = localeCookie;
    }

    public static <T extends LocaleCookieEntity> T get() {
        try {
            T entity = UI.getCurrent().getSession().getAttribute((Class<T>) LOCALE_COOKIE);
            if (entity == null) {
                entity = (T) LOCALE_COOKIE.newInstance();
                entity.load();
                UI.getCurrent().getSession().setAttribute((Class<T>) LOCALE_COOKIE, entity);
            }
            return entity;
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    public static void set(Callback callback) {
        callback.callback(get());
        set();
    }

    public static interface Callback {
        void callback(LocaleCookieEntity entity);
    }

    public static void set() {
        get().persist();
    }

    // MAP Component
    public Field<Boolean> mapFilterVisible = new Field<>(this);
    public Field<Boolean> mapLegendVisible = new Field<>(this);
    public Field<Integer> mapLayer = new Field<>(this);

    public LocaleCookieEntity() {
        super(LocaleCookieEntity.class.getName());
    }

    private void init() {
        mapFilterVisible.set(true);
        mapLayer.set(2);
    }


}
