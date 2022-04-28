package za.co.spsi.toolkit.crud.gui.query;

import com.vaadin.data.util.converter.Converter;
import za.co.spsi.lookup.service.LookupServiceHelper;
import za.co.spsi.toolkit.crud.gui.fields.ComboBoxField;
import za.co.spsi.toolkit.crud.gui.fields.MLCSLookupField;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import za.co.spsi.toolkit.util.StringList;

import java.util.Arrays;
import java.util.Locale;

/**
 * Created by jaspervdb on 2016/08/23.
 */
public class MLCSLookupFieldConverter implements Converter<String, String> {

    private MLCSLookupField field;

    public MLCSLookupFieldConverter(MLCSLookupField field) {
        this.field =field;
    }

    @Override
    public String convertToModel(String value, Class<? extends String> targetType, Locale locale) throws ConversionException {
        return value;
    }

    @Override
    public String convertToPresentation(String value, Class<? extends String> targetType, Locale locale) throws ConversionException {
        return field.getDisplayValue(value);
    }

    @Override
    public Class<String> getModelType() {
        return String.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
}
