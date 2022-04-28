package za.co.spsi.toolkit.crud.gui.query;

import com.vaadin.data.util.converter.Converter;
import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.crud.gui.fields.ComboBoxField;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import za.co.spsi.toolkit.util.StringList;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Locale;

/**
 * Created by jaspervdb on 2016/08/23.
 */
public class StringToBigDecimal implements Converter<String, BigDecimal> {

    private String trueString,falseString;

    public StringToBigDecimal(String trueString, String falseString) {
        this.trueString = trueString;
        this.falseString = falseString;
    }

    public BigDecimal convertToModel(String value, Class<? extends BigDecimal> targetType, Locale locale) throws ConversionException {
        if(value != null && !value.isEmpty()) {
            value = value.trim();
            if(this.getTrueString().equals(value)) {
                return new BigDecimal(1.0);
            } else if(this.getFalseString().equals(value)) {
                return new BigDecimal(0);
            } else {
                throw new ConversionException("Cannot convert " + value + " to " + this.getModelType().getName());
            }
        } else {
            return null;
        }
    }

    protected String getTrueString() {
        return this.trueString;
    }

    protected String getFalseString() {
        return this.falseString;
    }

    public String convertToPresentation(BigDecimal value, Class<? extends String> targetType, Locale locale) throws ConversionException {
        return value == null?null:(value.doubleValue()==1?this.getTrueString(locale):this.getFalseString(locale));
    }

    protected String getFalseString(Locale locale) {
        return this.getFalseString();
    }

    protected String getTrueString(Locale locale) {
        return this.getTrueString();
    }

    public Class<BigDecimal> getModelType() {
        return BigDecimal.class;
    }

    public Class<String> getPresentationType() {
        return String.class;
    }

}
