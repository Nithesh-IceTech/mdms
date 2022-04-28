package za.co.spsi.toolkit.crud.gui.fields;

import com.vaadin.ui.TextField;

/**
 * Created by jaspervdb on 15/02/05.
 */
public class UppercaseTextField extends TextField {
    /**
     * Constructs an empty <code>TextField</code> with no caption.
     */
    public UppercaseTextField() {
        super();
        addStyleName("uppercase");
    }

    /**
     * Constructs an empty <code>TextField</code> with given caption.
     *
     * @param caption
     *            the caption <code>String</code> for the editor.
     */
    public UppercaseTextField(String caption) {
        super(caption);
        addStyleName("uppercase");
    }

    @Override
    protected void setInternalValue(String newValue) {
        super.setInternalValue(newValue!=null?newValue.toUpperCase():null);
    }

    @Override
    public String getValue() {
        String value = super.getValue();
        return value != null?value.toUpperCase():null;
    }
}
