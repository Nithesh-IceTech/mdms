package za.co.spsi.toolkit.crud.locale;

import com.vaadin.ui.ComboBox;
import za.co.spsi.toolkit.crud.gui.fields.MLCSLookupField;

/**
 * Created by jaspervdb on 15/09/25.
 */
public class TranslatableComboBox extends ComboBox implements Translatable {

    private Translatable field;

    public TranslatableComboBox(Translatable field) {
        this.field = field;
    }

    public Translatable getField() {
        return field;
    }

    @Override
    public void translate(String oldLocale, String newLocale) {
        field.translate(oldLocale,newLocale);
    }

    @Override
    public void setCaption(String caption) {
        super.setCaption(caption);
    }
}
