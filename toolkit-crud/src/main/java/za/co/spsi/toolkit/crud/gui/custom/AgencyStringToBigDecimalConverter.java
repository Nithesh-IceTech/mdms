package za.co.spsi.toolkit.crud.gui.custom;

import com.vaadin.data.util.converter.StringToBigDecimalConverter;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by jaspervdb on 2016/11/14.
 */
public class AgencyStringToBigDecimalConverter extends StringToBigDecimalConverter {

    private String decimalFormat = null;

    public AgencyStringToBigDecimalConverter(String decimalFormat) {
        this.decimalFormat = decimalFormat;
    }

    @Override
    protected NumberFormat getFormat(Locale locale) {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
        otherSymbols.setDecimalSeparator(ToolkitCrudConstants.getDecimalSeparator());
        otherSymbols.setGroupingSeparator(ToolkitCrudConstants.getGroupingSeparator());
        DecimalFormat dc = new DecimalFormat(decimalFormat != null ? decimalFormat :
                ToolkitCrudConstants.getDecimalFormat(), otherSymbols);

        dc.setParseBigDecimal(true);
        return dc;
    }

    @Override
    public BigDecimal convertToModel(String value,
                                     Class<? extends BigDecimal> targetType, Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        return (BigDecimal) convertToNumber(value, BigDecimal.class, locale);
    }
}
