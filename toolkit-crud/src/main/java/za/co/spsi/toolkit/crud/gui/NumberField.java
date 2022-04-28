package za.co.spsi.toolkit.crud.gui;


import com.vaadin.data.util.converter.StringToBigDecimalConverter;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.TextField;
import za.co.spsi.toolkit.crud.gui.custom.TextFieldRetrievable;

import java.math.BigDecimal;

public class NumberField extends CustomField<BigDecimal> implements TextFieldRetrievable {

    private TextField textField;

    public NumberField() {
        textField = new TextField();
        textField.setNullRepresentation("");
        textField.setConverter(new StringToBigDecimalConverter());
        textField.setImmediate(true);
    }

    @Override
    protected Component initContent() {
        return textField;
    }

    @Override
    public Class<BigDecimal> getType() {
        return BigDecimal.class;
    }

    @Override
    public void setValue(BigDecimal newFieldValue) {
        textField.setConvertedValue(newFieldValue);
//        textField.setValue(formatValue(newFieldValue));
    }

    @Override
    public void setWidth(float width, Unit unit) {
        if (textField != null) {
            textField.setWidth(width, unit);
        }

        super.setWidth(width, unit);
    }

    @Override
    public void addValueChangeListener(ValueChangeListener listener) {
//        textField.addValueChangeListener(listener);
    }

    @Override
    public BigDecimal getValue() {
        return (BigDecimal) textField.getConvertedValue();
//
//        if (textField.getValue() == null || textField.getValue().isEmpty()) {
//            return null;
//        }
//        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
//        otherSymbols.setDecimalSeparator(ToolkitCrudConstants.getDecimalSeparator());
//        otherSymbols.setGroupingSeparator(ToolkitCrudConstants.getGroupingSeparator());
//        DecimalFormat df = new DecimalFormat(ToolkitCrudConstants.getDecimalFormat(), otherSymbols);
//
//        try {
//            df.setParseBigDecimal(true);
//            return (BigDecimal) df.parse(textField.getValue());
//        } catch (ParseException e) {
//            throw new InvalidFieldValueException(VaadinLocaleHelper.getValue(ToolkitLocaleId.INVALID_VALUE),null);
//        }
    }

    protected BigDecimal getInternalValue() {
        return getValue();
    }

//    public String formatValue(BigDecimal bigDecimal) {
////
////        if (bigDecimal == null) {
////            return null;
////        } else {
////            DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
////            char decimal_seperator = ToolkitCrudConstants.getDecimalSeparator();
////            char grouping_seperator = ToolkitCrudConstants.getGroupingSeparator();
////            otherSymbols.setDecimalSeparator(decimal_seperator);
////            otherSymbols.setGroupingSeparator(grouping_seperator);
////            DecimalFormat df = new DecimalFormat(ToolkitCrudConstants.getDecimalFormat(), otherSymbols);
////            return df.format(bigDecimal);
////        }
//    }

    @Override
    public TextField getTextField() {
        return textField;
    }

    public void setTextField(TextField textField) {
        this.textField = textField;
    }
}



