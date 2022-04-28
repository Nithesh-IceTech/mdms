package za.co.spsi.toolkit.crud.gui;

/**
 * Created by jaspervdb on 2014/05/15.
 */
public interface ValueChangeListener {

    /**
     *
     * @param srcField
     * @param field
     * @param inConstruction if the layout is currently being constructed
     * @param valueIsNull
     */
    void valueChanged(LField srcField,com.vaadin.ui.Field field, boolean inConstruction,boolean valueIsNull);

}