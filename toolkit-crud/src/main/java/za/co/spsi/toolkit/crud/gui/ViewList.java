package za.co.spsi.toolkit.crud.gui;


import za.co.spsi.toolkit.ano.AgencyUIQualifier;
import za.co.spsi.toolkit.ano.UI;
import za.co.spsi.toolkit.crud.gui.render.Viewable;
import za.co.spsi.toolkit.crud.util.AgencyHelper;
import za.co.spsi.toolkit.util.AnoUtil;
import za.co.spsi.toolkit.util.StringUtils;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jaspervdb on 3/10/16.
 */
public class ViewList<T extends Viewable> extends ArrayList<T> {

    private boolean applied = false;

    public ViewList() {
    }

    public ViewList(Collection<T> fields) {
        addAll(fields);
    }

    public ViewList(T... fields) {
        if (fields != null) {
            for (T field : fields) {
                add(field);
            }
        }
    }

    public void intoControl() {
        forEach((k) -> {
            k.intoControl();
        });
    }

    public void intoBindings() {
        forEach((k) -> {
            k.intoBindings();
        });
    }

    public void applyProperties() {
        forEach((k) -> {
            k.applyProperties();
        });
    }


    public void newEvent() {
        forEach((k) -> {
            k.newEvent();
        });
    }

    public void saveEvent(Connection connection) {
        forEach((k) -> {
            k.saveEvent(connection);
        });
    }

    public void beforeOnScreenEvent() {
        forEach((k) -> {
            k.beforeOnScreenEvent();
        });
    }

    public void afterOnScreenEvent() {
        forEach((k) -> {
            k.afterOnScreenEvent();
        });
    }

    public ViewList<T> subtract(ViewList<T> views) {
        ViewList subtracted = new ViewList();
        for (T view : this) {
            if (!views.contains(view)) {
                subtracted.add(view);
            }
        }
        return subtracted;
    }

    private synchronized <E extends ViewList<T>> E filter() {
        List remove = new ArrayList<>();
        stream().filter(v -> v.getAnnotation(AgencyUIQualifier.class) != null &&
                AgencyHelper.removed(v.getAnnotation(AgencyUIQualifier.class))).forEach(v -> remove.add(v));
        removeAll(remove);
        return (E) this;
    }

    private void apply(T v) {
        AgencyHelper.getUIs(v.getAnnotation(AgencyUIQualifier.class)).forEach(ui -> v.getUI().init(ui));
    }

    public synchronized <E extends ViewList<T>> E  apply() {
        if (!applied) {
            applied = true;
            filter();
        }
        return (E) this;
    }
}
