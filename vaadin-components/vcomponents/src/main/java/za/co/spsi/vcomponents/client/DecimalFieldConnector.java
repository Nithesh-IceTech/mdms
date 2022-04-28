package za.co.spsi.vcomponents.client;

import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.ui.VTextField;
import com.vaadin.shared.ui.Connect;
import za.co.spsi.vcomponents.DecimalField;

@Connect(DecimalField.class)
public class DecimalFieldConnector extends AbstractFieldConnector {
    private static final long serialVersionUID = -737765038361894693L;

    private void maxOk(String value) {
        try {
            value = value.replace(getState().groupSeparator, "").replace(getState().decimalSeparator, ".");
            double decimal = Double.valueOf(value);
            if (decimal >= getState().max) {
                throw new RuntimeException(getState().maxValueExceededError.replace("_VALUE_", "" + getState().max));
            }
        } catch (NumberFormatException ex) {
            throw new RuntimeException(getState().invalidCharacterError);
        }
    }

    private boolean commaOk(String value) {
        return value.indexOf(getState().groupSeparator) == -1 ||
                value.indexOf(getState().decimalSeparator) == -1 ||
                value.indexOf(getState().decimalSeparator) >= value.indexOf(getState().groupSeparator);
    }

    public boolean decimalPlacesOk(String value) {
        return (value.indexOf(getState().decimalSeparator) == -1 ||
                (value.indexOf(getState().decimalSeparator) >= (value.length() - getState().decimalPlaces -1)));
    }

    @Override
    boolean isValueValid(String value) {
        maxOk(value);
        return commaOk(value) && decimalPlacesOk(value);
    }

    @Override
    public DecimalFieldState getState() {
        return (DecimalFieldState) super.getState();
    }

    public static void main(String args[]) throws Exception {
        System.out.println(new DecimalFieldConnector().decimalPlacesOk("1312.123"));
    }

}