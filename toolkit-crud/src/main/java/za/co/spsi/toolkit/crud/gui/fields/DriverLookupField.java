package za.co.spsi.toolkit.crud.gui.fields;

import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;
import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.util.Util;

import java.sql.Driver;
import java.util.LinkedHashMap;
import java.util.Map;

public class DriverLookupField<T> extends LField<T> {

    private Map<String, String> itemCaption = null;
    private String intoControlValue = null;
    private boolean nullAllowed = true;

    public DriverLookupField(Field field, String captionId, Layout layout) {
        super(field, captionId, layout);
    }

    public Map<String, String> getItemCaptionMap() {
        if (itemCaption == null) {
            itemCaption = new LinkedHashMap<>();

            // Load db drivers
            Util.getSubTypesOf(Driver.class).stream().forEach(e -> itemCaption.put(e.getName(), e.getCanonicalName()));
        }
        return itemCaption;
    }

    public void populateLookupFieldComboBox() {
        ComboBox comboBox = (ComboBox) getVaadinField();
        comboBox.setFilteringMode(FilteringMode.CONTAINS);
        comboBox.removeAllItems();
        for (Object key : getItemCaptionMap().keySet()) {
            comboBox.addItem(key);
            comboBox.setItemCaption(key, getItemCaptionMap().get(key));
        }
        if (intoControlValue != null) {
            comboBox.setValue(intoControlValue);
            intoControlValue = null;
        }
    }

    @Override
    public com.vaadin.ui.Field buildVaadinField() {
        final ComboBox comboBox = new ComboBox();
        comboBox.setScrollToSelectedItem(true);
        comboBox.setImmediate(true);
        setComponent(comboBox);
        populateLookupFieldComboBox();
        comboBox.setNullSelectionAllowed(nullAllowed);
        setComponent(comboBox);
        return comboBox;
    }
}
