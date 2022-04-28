package za.co.spsi.vcomponents.client;

import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.HTML;
import com.vaadin.client.ComponentConnector;
import com.vaadin.client.ServerConnector;
import com.vaadin.client.extensions.AbstractExtensionConnector;
import com.vaadin.client.ui.VOverlay;
import com.vaadin.client.ui.VTextField;

public abstract class AbstractFieldConnector extends AbstractExtensionConnector implements KeyUpHandler {
    private static final long serialVersionUID = -737765038361894693L;

    VOverlay warning;
    VTextField textField;

    @Override
    public void onKeyUp(KeyUpEvent keyUpEvent) {
        if (!textField.isReadOnly() && textField.isEnabled() && !validateValue(textField.getText())) {
            textField.setValue(textField.getValue().substring(0, textField.getText().length()-1));
        }
    }

    @Override
    protected void extend(ServerConnector target) {
        textField = (VTextField) ((ComponentConnector) target).getWidget();
        textField.addKeyPressHandler(new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent event) {
                clearMessage();
            }
        });
        textField.addKeyUpHandler(this);
        textField.addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                clearMessage();
            }
        });
    }

    private void clearMessage() {
        if (warning != null) {
            warning.removeFromParent();
        }
        warning = null;
    }

    public void showMessage(String message) {
        clearMessage();
        warning = new VOverlay();
        warning.setOwner(textField);
        warning.setStyleName("vcomponent-message");

        warning.add(new HTML(message));
        warning.showRelativeTo(textField);
    }

    boolean validateValue(String value) {
        try {
            return value.length()==0?true:isValueValid(value);
        } catch (Exception e) {
            showMessage(e.getMessage());
            return false;
        }
    }

    abstract boolean isValueValid(String value);
}