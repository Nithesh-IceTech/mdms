package za.co.spsi.toolkit.crud.gui.fields;

import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;
import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.entity.Field;

import java.util.LinkedHashMap;
import java.util.Map;

public class DynamicLookupField<T> extends LField<T> {

    private Map<String, String> itemCaption = null;
    private String intoControlValue = null;
    private boolean nullAllowed = true;
    private Callback callback;

    public DynamicLookupField(Field field, String captionId, Layout layout, Callback callback) {
        super(field, captionId, layout);
        this.callback = callback;
    }

    public Map<String, String> getItemCaptionMap() {
        itemCaption = new LinkedHashMap<>();
        itemCaption = callback.getLookupCation();
        return itemCaption;
    }

    public void populateLookupFieldComboBox() {
        ComboBox comboBox = (ComboBox) getVaadinField();
        comboBox.setFilteringMode(FilteringMode.CONTAINS);
        comboBox.removeAllItems();

        getItemCaptionMap().forEach((key, value) -> {
            comboBox.addItem(key);
            comboBox.setItemCaption(key, value);
        });

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

    public static interface Callback {
        public Map<String, String> getLookupCation();
    }
}
