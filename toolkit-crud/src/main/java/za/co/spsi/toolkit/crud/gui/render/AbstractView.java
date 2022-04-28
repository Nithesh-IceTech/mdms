package za.co.spsi.toolkit.crud.gui.render;

import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import za.co.spsi.toolkit.ano.AgencyUIQualifier;
import za.co.spsi.toolkit.ano.UI;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.UIProperties;
import za.co.spsi.toolkit.crud.gui.ViewList;
import za.co.spsi.toolkit.crud.locale.VaadinLocaleHelper;
import za.co.spsi.toolkit.crud.util.AgencyHelper;
import za.co.spsi.toolkit.crud.util.Util;
import za.co.spsi.toolkit.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: jaspervdbijl
 * Date: 2013/10/18
 * Time: 9:33 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractView implements Viewable {

    private String captionId;
    private static Util util = Util.getInstance();

    public static final Logger TAG = Logger.getLogger(AbstractView.class.getName());

    private ViewList views = new ViewList();

    private Component component;
    private Field myField;
    private Layout layout;

    private UIProperties uiProperties;

    public AbstractView(Layout layout) {
        this.layout = layout;
    }

    public AbstractView(String captionId, Layout layout) {
        this(layout);
        setCaptionId(captionId);
    }


    public void addView(Viewable viewable) {
        views.add(viewable);
    }

    public void addViews(Viewable viewables[]) {
        for (Viewable view : viewables) {
            addView(view);
        }
    }

    public ViewList<Viewable> getViews() {
        return views.apply();
    }

    public <T> List<T> getViews(Class<T> type) {
        List<T> list = new ArrayList<T>();
        for (Object view : views) {
            if (type.isAssignableFrom(view.getClass())) {
                list.add((T) view);
            }
        }
        return views;
    }

    /**
     * @return the components defined caption
     */
    public String getCaption() {
        return !StringUtils.isEmpty(getUI().getCaption()) ? getLocaleValue(getUI().getCaption()) : getLocaleValue(captionId);
    }

    public String getCaptionId() {
        return captionId;
    }

    public static String getLocaleValue(String resourceName) {
        return VaadinLocaleHelper.getValue(resourceName, ToolkitCrudConstants.getLocale());
    }

    /**
     * set the components custom caption
     *
     * @param captionId
     */
    public void setCaptionId(String captionId) {
        this.captionId = captionId;
    }


    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    // TODO implement - check is attached to a active window
    public boolean isVisible() {
        return getComponent().isVisible();
    }

    /**
     * get and don;t' build it if its not built
     *
     * @return
     */
    public boolean isBuilt() {
        return component != null;
    }

    @Override
    public synchronized Component getComponent() {
        if (component == null) {
            component = buildComponent();
            // apply ui and roles
            if (layout != null) {
                if (layout.getReflectionField(this) != null) {
                    util.applyUI(component, getUI());
                    util.applyWritePermissions(layout.getReflectionField(this), layout.getRoleProvider(), component);
                }
            }
            applyProperties();
        }
        return component;
    }

    public void resetComponent() {
        this.component = null;
    }


    /**
     * @param component
     */
    protected void setComponent(Component component) {
        this.component = component;
    }

    public abstract Component buildComponent();

    /**
     * Overload method to return a toolbar
     *
     * @return the top toolbar that contains the name and the navigation buttons
     */
    public HorizontalLayout getToolbar() {
        return null;
    }

    @Override
    public void intoBindings() {
        views.intoBindings();
    }

    @Override
    public void intoControl() {
        views.intoControl();
    }

    @Override
    public void applyProperties() {
        views.applyProperties();
    }


    private boolean setFocus(Component component) {
        if (component instanceof ComponentContainer) {
            Iterator<Component> iterator = ((ComponentContainer) component).iterator();
            while (iterator.hasNext()) {
                if (setFocus(iterator.next())) {
                    return true;
                }
            }
            return false;
        } else if (component instanceof AbstractField && component.isEnabled()) {
            ((AbstractField) component).focus();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean setFocus() {
        return setFocus(getComponent());
    }


    public <T extends Annotation> T getAnnotation(Class<T> anoClass) {
        Field field = getLayout().getReflectionField(this);
        return field != null ? field.getAnnotation(anoClass) : null;
    }

    /**
     * @return UI if the object is annotated by one
     * @throws IllegalAccessException
     */
    public UIProperties getUI() {
        if (uiProperties == null) {
            Layout layout = getLayout();
            if (layout != null) {
                AgencyUIQualifier qualifier = getAnnotation(AgencyUIQualifier.class);
                List<UI> uis = qualifier != null ? AgencyHelper.getUIs(qualifier) : new ArrayList<>();

                uiProperties = !uis.isEmpty() ? new UIProperties(uis.get(0)) :
                        getAnnotation(UI.class) != null ? new UIProperties(getAnnotation(UI.class)) : null;
            }
        }
        uiProperties = uiProperties == null ? new UIProperties() : uiProperties;
        return uiProperties;
    }


    // Refelection properties


    /**
     * recursively search for my reference field
     *
     * @param modelClass
     * @return
     * @throws IllegalAccessException
     */
    private java.lang.reflect.Field getMyField(Class modelClass) throws IllegalAccessException {
        for (java.lang.reflect.Field field : modelClass.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.get(getLayout()) == this) {
                return field;
            }
        }
        if (!modelClass.getSuperclass().equals(Object.class)) {
            // look in parent
            return getMyField(modelClass.getSuperclass());
        }
        return null;
    }

    /**
     * @return your own referenced field by the model
     */
    public java.lang.reflect.Field getMyField() {
        if (myField == null) {
            try {
                myField = getMyField(layout.getClass());
            } catch (IllegalAccessException iae) {
                throw new RuntimeException(iae);
            }
        }
        return myField;
    }

    public boolean hasGenericType() {
        return getMyField().getGenericType() instanceof ParameterizedType;
    }

    private Class getMyFieldType(Type type) {
        if (type instanceof ParameterizedType) {
            return (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
        } else if (type instanceof Class) {
            return getMyFieldType(((Class) type).getGenericSuperclass());
        } else {
            return String.class;
        }
    }

    public Class getFieldType() {
        return getMyFieldType(getMyField().getGenericType());
    }


    public void newEvent() {
    }

    public void saveEvent(Connection connection) {
    }

    public void beforeOnScreenEvent() {
    }

    public void afterOnScreenEvent() {
    }
}
