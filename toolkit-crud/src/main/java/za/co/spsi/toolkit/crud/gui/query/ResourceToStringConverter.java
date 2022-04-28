package za.co.spsi.toolkit.crud.gui.query;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;

import java.util.Locale;

public class ResourceToStringConverter implements Converter<Resource, String> {
    @Override
    public String convertToModel(Resource resource, Class<? extends String> aClass, Locale locale) throws ConversionException {
        return null;
    }

    @Override
    public Resource convertToPresentation(String s, Class<? extends Resource> aClass, Locale locale) throws ConversionException {
        return new ThemeResource(s);
    }

    @Override
    public Class<String> getModelType() {
        return String.class;
    }

    @Override
    public Class<Resource> getPresentationType() {
        return Resource.class;
    }

}