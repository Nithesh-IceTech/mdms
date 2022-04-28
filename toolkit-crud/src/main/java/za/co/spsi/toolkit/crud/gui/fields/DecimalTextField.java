package za.co.spsi.toolkit.crud.gui.fields;

import com.vaadin.ui.TextField;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.crud.gui.custom.AgencyStringToBigDecimalConverter;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.crud.locale.Translatable;
import za.co.spsi.toolkit.util.StringUtils;
import za.co.spsi.vcomponents.DecimalField;

import java.math.BigDecimal;

/**
 * Created by jaspervdb on 2016/11/14.
 */
public class DecimalTextField extends TextField implements Translatable {

    private DecimalField decimalField;
    private String decimalFormat;

    public DecimalTextField(String decimalFormat) {
        super();
        setConverter(new AgencyStringToBigDecimalConverter(decimalFormat));
        setImmediate(true);
        setLocale(ToolkitCrudConstants.getAsLocale());
        setConversionError(AbstractView.getLocaleValue(ToolkitLocaleId.INVALID_VALUE));
        this.decimalFormat = decimalFormat;

        init();
    }

    private void init() {
        decimalField = DecimalField.extend(this);
        decimalField.setMaxValueExceededError(AbstractView.getLocaleValue(ToolkitLocaleId.ERROR_MESSAGE_DECIMAL_FIELD_MAX_VALUE));
        decimalField.setDecimalSeparator(ToolkitCrudConstants.getDecimalSeparator() + "");
        decimalField.setGroupSeparator(ToolkitCrudConstants.getGroupingSeparator() + "");

        if (!StringUtils.isEmpty(decimalFormat)) {
            int dotIndex = decimalFormat.lastIndexOf(".");
            int commaIndex = decimalFormat.lastIndexOf(",");

            int decimalPlaces = 0;
            if (dotIndex == -1 && commaIndex == -1) {
                decimalPlaces = 0;
            } else if (dotIndex > commaIndex) {
                decimalPlaces = decimalFormat.substring(dotIndex).length() - 1;
            } else {
                decimalPlaces = decimalFormat.substring(commaIndex).length() - 1;
            }
            decimalField.setDecimalPlaces(decimalPlaces);
        }
    }

    public DecimalField getDecimalField() {
        return decimalField;
    }

    @Override
    public void setValue(String newValue) throws ReadOnlyException {
        super.setValue(newValue);
    }

    @Override
    public void setConvertedValue(Object value) {
        if (value instanceof BigDecimal) {
            super.setConvertedValue(value);
        } else if (value instanceof Double) {
            super.setConvertedValue(new BigDecimal((Double) value));
        } else if (value instanceof Float) {
            super.setConvertedValue(new BigDecimal((Float) value));
        } else {
            throw new UnsupportedOperationException("Unsupported type " + value);
        }
    }


    public Object getConvertedValue() {
        return super.getConvertedValue();
    }

    @Override
    public void translate(String oldLocale, String newLocale) {
        decimalField.remove();
        double max = decimalField.getMax();
        init();
        decimalField.setMax(max);
    }
}
