package za.co.spsi.toolkit.crud.gui.render;

import com.vaadin.ui.Component;
import za.co.spsi.toolkit.crud.gui.UIProperties;

import java.lang.annotation.Annotation;
import java.sql.Connection;

/**
 * Created with IntelliJ IDEA.
 * User: jaspervdbijl
 * Date: 2013/10/18
 * Time: 9:29 AM
 * To change this template use File | Settings | File Templates.
 */
public interface Viewable {

    public boolean isBuilt();
    /**
     * build and get the vaadin component
     * @return
     */
    public Component getComponent();

    public UIProperties getUI();

    /**
     * update the entity properties
     */
    public void intoBindings();

    /**
     * update the vaadin field from the properties
     */
    public void intoControl();

    /**
     * update the field properties again
     */
    public void applyProperties();


    /**
     * set the focus on the first enabled field
     */
    public boolean setFocus();


    public void newEvent() ;

    public void saveEvent(Connection connection) ;

    public void beforeOnScreenEvent();

    public void afterOnScreenEvent() ;

    public <T extends Annotation> T getAnnotation(Class<T> type);
}
