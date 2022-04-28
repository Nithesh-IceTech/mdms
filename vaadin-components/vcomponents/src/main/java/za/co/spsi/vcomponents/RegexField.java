package za.co.spsi.vcomponents;

import com.vaadin.server.AbstractClientConnector;
import com.vaadin.server.AbstractExtension;
import com.vaadin.ui.TextField;
import za.co.spsi.vcomponents.client.RegexFieldState;

public class RegexField extends AbstractExtension {

    public static RegexField extend(TextField field) {
        RegexField regexField = new RegexField();
        regexField.extend((AbstractClientConnector) field);
        return regexField;
    }

    @Override
    protected RegexFieldState getState() {
        return (RegexFieldState) super.getState();
    }

    public void setRegEx(String regex) {
        getState().regex = regex;
    }
}
