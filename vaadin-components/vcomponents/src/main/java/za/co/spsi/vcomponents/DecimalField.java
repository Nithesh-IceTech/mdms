package za.co.spsi.vcomponents;

import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.AbstractExtension;
import com.vaadin.ui.TextField;
import za.co.spsi.vcomponents.client.DecimalFieldState;

public class DecimalField extends AbstractExtension {
    
    public static DecimalField extend(TextField field) {
        DecimalField decimalField = new DecimalField();
        decimalField.extend((AbstractClientConnector) field);
        return decimalField;
    }

    @Override
    protected DecimalFieldState getState() {
        return (DecimalFieldState) super.getState();
    }

    public String getGroupSeparator() {
        return getState().groupSeparator;
    }

    public void setGroupSeparator(String groupSeparator) {
        getState().groupSeparator = groupSeparator;
    }

    public String getDecimalSeparator() {
        return getState().decimalSeparator;
    }

    public void setDecimalSeparator(String decimalSeparator) {
        getState().decimalSeparator = decimalSeparator;
    }

    public String getMaxValueExceededError() {
        return getState().maxValueExceededError;
    }

    public void setMaxValueExceededError(String maxValueExceededError) {
        getState().maxValueExceededError = maxValueExceededError;
    }

    public String getInvalidCharacterError() {
        return getState().invalidCharacterError;
    }

    public void setInvalidCharacterError(String invalidCharacterError) {
        getState().invalidCharacterError = invalidCharacterError;
    }

    public double getMax() {
        return getState().max;
    }

    public void setMax(double max) {
        getState().max = max;
    }

    public int getDecimalPlaces() {
        return getState().decimalPlaces;
    }

    public void setDecimalPlaces(int decimalPlaces) {
        getState().decimalPlaces = decimalPlaces;
    }
}
