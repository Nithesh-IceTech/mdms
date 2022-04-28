package za.co.spsi.vcomponents.client;

import com.vaadin.shared.ui.Connect;
import za.co.spsi.vcomponents.RegexField;

@Connect(RegexField.class)
public class RegexFieldConnector extends AbstractFieldConnector {


    @Override
    boolean isValueValid(String value) {
        return value.matches(getState().regex);
    }

    @Override
    public RegexFieldState getState() {
        return (RegexFieldState) super.getState();
    }

}