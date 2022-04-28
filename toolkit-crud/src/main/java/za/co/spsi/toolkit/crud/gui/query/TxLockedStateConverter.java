package za.co.spsi.toolkit.crud.gui.query;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.data.util.converter.StringToBooleanConverter;
import com.vaadin.server.FontAwesome;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.ToolkitUI;

import java.util.Locale;

/**
 * Created by jaspervdb on 2016/08/23.
 */
public class TxLockedStateConverter implements Converter<String, String> {

    private Layout layout;

    public TxLockedStateConverter(Layout layout) {
        this.layout = layout;
    }

    @Override
    public String convertToModel(String value, Class<? extends String> targetType, Locale locale) throws ConversionException {
        return value;
    }

    @Override
    public String convertToPresentation(String value, Class<? extends String> targetType, Locale locale) throws ConversionException {
        String row = value != null?value.substring(0,value.indexOf("|")):null;
        String id = value != null?value.substring(value.indexOf("|")+1):null;
        return ToolkitUI.isExclusiveTx(layout,id)?FontAwesome.UNLOCK.getHtml():
                ToolkitUI.isTxLocked(layout,id)?FontAwesome.LOCK.getHtml():row;
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
