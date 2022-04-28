package za.co.spsi.toolkit.crud.gui.query;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.FontAwesome;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.ToolkitUI;
import za.co.spsi.toolkit.crud.gui.fields.ComboBoxField;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import za.co.spsi.toolkit.util.StringList;

import java.util.*;

/**
 * Created by jaspervdb on 2016/08/23.
 */
public class ComboBoxConverter implements Converter<String, Object> {

    private StringList options,values = new StringList();

    public ComboBoxConverter(ComboBoxField field) {
        options = new StringList(field.getOptions());
        Arrays.stream(field.getValues()).forEach(o -> values.add(o.toString()));
    }

    @Override
    public String convertToModel(String value, Class<? extends Object> targetType, Locale locale) throws ConversionException {
        return value;
    }

    @Override
    public String convertToPresentation(Object value, Class<? extends String> targetType, Locale locale) throws ConversionException {
        if (value != null && values.contains(value.toString())) {
            return AbstractView.getLocaleValue(options.get(values.indexOf(value.toString())));
        } else {
            return null;
        }
    }

    @Override
    public Class<Object> getModelType() {
        return Object.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }
}
