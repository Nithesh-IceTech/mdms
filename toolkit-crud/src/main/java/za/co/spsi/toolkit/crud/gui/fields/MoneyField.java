package za.co.spsi.toolkit.crud.gui.fields;

import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.entity.Field;

public class MoneyField extends LField<Double> {

    public MoneyField(Field field, String captionId, Layout layout) {
        super(field, captionId, layout);
    }

    @Override
    public String getDisplayValue() {
        return get() != null ? String.format("%.2f", get()) : "";
    }

    /**
     * @return the value converter to a string
     */
    public String getAsString() {
        return get() != null ? String.format("%.2f", get()) : "";
    }
}

