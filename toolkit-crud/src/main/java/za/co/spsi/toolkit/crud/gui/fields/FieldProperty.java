package za.co.spsi.toolkit.crud.gui.fields;

import com.vaadin.data.Validator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.csvalidation.CSValidator;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.ano.UIField;
import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.crud.gui.Permission;
import za.co.spsi.toolkit.crud.gui.UIProperties;
import za.co.spsi.toolkit.crud.gui.custom.TextFieldRetrievable;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import za.co.spsi.toolkit.crud.locale.VaadinLocaleHelper;
import za.co.spsi.toolkit.crud.util.Util;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.util.Assert;
import za.co.spsi.toolkit.util.BeanUtils;
import za.co.spsi.toolkit.util.MaskId;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created with IntelliJ IDEA.
 * User: jaspervdb
 * Date: 2013/10/31
 * Time: 9:54 PM
 * All field's properties are stored in this class
 */
public class FieldProperty {

    public static Boolean DEFAULT_UPPERCASE = null;

    public static final int
            NONE = 0x00,
            MANDATORY = 0x02,
            READ_ONLY = 0x04,
            SET_DEFAULT = 0x08,
            SET_CAPS = 0x10,
            VISIBLE = 0x20,
            WRITE_CONCE = 0x40;

    private int option = SET_CAPS | VISIBLE;

    private Util util = Util.getInstance();
    private Permission permission;

    private MaskId maskId = MaskId.ANY;

    private String caption, customRegEx;
    private String defaultValue;
    private Integer min = null, max = null, rows = 5;
    private Long maxValue;
    private String errorMessage;
    private boolean immediate = true;
    private List<Field> defaultSetList = new ArrayList<>();
    private String format;

    private Stack<FieldProperty> states = new Stack<>();

    public FieldProperty() {
    }

    public FieldProperty(FieldProperty state) {
        init(state);
    }

    public FieldProperty(int option, Integer min, Integer max, String customRegEx, MaskId maskId) {
        this.option = option;
        this.min = min;
        this.max = max;
        this.customRegEx = customRegEx;
        this.maskId = maskId;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    private void init(FieldProperty defaultValue) {
        try {
            BeanUtils.getInstance().copyProperties(this, defaultValue);
            maskId = defaultValue.getMaskId();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public FieldProperty(UIField uiField) {
        if(uiField.captionStr() != null && !uiField.captionStr().equals("")){
            setCaption(uiField.captionStr());
        }
        setMandatory(uiField.mandatory());
        setWriteOnce(uiField.writeOnce());
        setReadOnly(!uiField.enabled());
        if (uiField.setDefault().length() > 0) {
            setDefault(uiField.setDefault());
        }
        setVisible(uiField.visible());
        setCaps(DEFAULT_UPPERCASE == null?uiField.uppercase():DEFAULT_UPPERCASE && uiField.uppercase());
        setImmediate(uiField.immediate());

        this.min = uiField.min() > -1 ? uiField.min() : null;
        this.max = uiField.max() > -1 ? uiField.max() : null;
        this.maxValue = uiField.maxValue() > -1 ? uiField.maxValue() : null;
        this.rows = uiField.rows();
        this.maskId = uiField.mask();
        this.customRegEx = !StringUtils.isEmpty(uiField.regex()) ? uiField.regex() : null;
        format = uiField.format();
    }

    /**
     * this will mark and store the current state, such that it can be reverted
     */
    public void markState() {
        states.add(new FieldProperty(this));
    }

    public void revertState() {
        Assert.isTrue(!states.isEmpty(), "No states have been recorded");
        FieldProperty fieldProperty = states.pop();
        init(fieldProperty);

    }

    public void applyProperties(Component component, LField field) {
        if (field.getField() != null && field.getField().getEntity() instanceof EntityDB) {
            if (((EntityDB) field.getField().getEntity()).isInDatabase() && isWriteOnce()) {
                setReadOnly(isReadOnly() || field.get() != null);
            }
        }

        if (component instanceof com.vaadin.ui.Field) {
            applyProperties((com.vaadin.ui.Field) component, field);
        } else {
            if (caption != null) {
                field.setCaptionId(caption);
                component.setCaption(VaadinLocaleHelper.getTranslatedCaption(caption));
            }
            component.setVisible(isVisible());
            component.setEnabled(isEnabled());
        }
    }

    private void applyPropertiesToTextField(AbstractTextField textField, LField field) {
        if (max != null) {
            textField.setMaxLength(max);
        }
        textField.setImmediate(immediate);
        applyMask(textField, field);

    }

    public void applyProperties(com.vaadin.ui.Field vaadinField, LField field) {
        if (field.getBeforeApplyPermissionCallback() != null) {
            field.getBeforeApplyPermissionCallback().onBeforeApply(this,vaadinField,field);
        }
        vaadinField.setRequired(isMandatory() && !isReadOnly());
        if (isMandatory() && !isReadOnly()) {
            vaadinField.setRequiredError(VaadinLocaleHelper.getValue(ToolkitLocaleId.VALUE_IS_REQUIRED));
        }
        if (vaadinField instanceof AbstractTextField) {
            applyPropertiesToTextField((AbstractTextField) vaadinField, field);
        }
        if (vaadinField instanceof TextFieldRetrievable) {
            applyPropertiesToTextField(((TextFieldRetrievable) vaadinField).getTextField(), field);
        }
        if (vaadinField instanceof ComboBox && immediate) {
            ((ComboBox) vaadinField).setImmediate(immediate);
        }
        if (vaadinField instanceof DecimalTextField) {
            ((DecimalTextField) vaadinField).getDecimalField().setMax(maxValue != null?maxValue:Double.MAX_VALUE);
        }
        // apply the mask
        vaadinField.setEnabled(isEnabled());
        field.getComponent().setEnabled(isEnabled());
        if (shouldSetCaps()) {
            vaadinField.addStyleName("uppercase");
        }
        if (shouldSetDefault() && !defaultSetList.contains(vaadinField)) {
            defaultSetList.add(vaadinField);
            if ((field.get() == null)) {
                field.setSerial(defaultValue);
                // check that the type is the same
                if (vaadinField instanceof ComboBox) {
                    ComboBox comboBox = (ComboBox) vaadinField;
                    if (!comboBox.getItemIds().isEmpty()) {
                        Class valueClass = ((ComboBox) vaadinField).getItemIds().iterator().next().getClass();
                        Assert.isTrue(defaultValue.getClass().equals(valueClass),
                                String.format("Default value for [%s: %s %s] is not the same type as the populated values. Default class %s, Lookup ConfValue Class %s",
                                        field.getLayout().getClass().getSimpleName(), field.getCaption(), field.getMyField().getName(),
                                        defaultValue.getClass(), valueClass));
                    }
                }
            }
        }
        if (caption != null) {
            field.setCaptionId(caption);
            vaadinField.setCaption(VaadinLocaleHelper.getTranslatedCaption(caption));
        }
        vaadinField.setVisible(isVisible());
        field.getComponent().setVisible(isVisible());
    }


    public String getCaption() {
        return caption;
    }

    public FieldProperty setCaption(String caption) {
        this.caption = caption;
        return this;
    }

    public boolean isImmediate() {
        return immediate;
    }

    public FieldProperty setImmediate(boolean immediate) {
        this.immediate = immediate;
        return this;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public FieldProperty setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public MaskId getMaskId() {
        return maskId;
    }

    public String getFormat() {
        return format;
    }

    public FieldProperty setMaskId(MaskId maskId) {
        this.maskId = maskId;
        return this;
    }

    public String getCustomRegEx() {
        return customRegEx;
    }

    public FieldProperty setCustomRegEx(String customRegEx) {
        this.customRegEx = customRegEx;
        return this;
    }

    public boolean isMandatory() {
        return (option & MANDATORY) == MANDATORY;
    }

    public boolean isWriteOnce() {
        return (option & WRITE_CONCE) == WRITE_CONCE;
    }

    private void setFlag(int flag, boolean enable) {
        if (enable != ((option & flag) == flag)) {
            this.option = option ^ flag;
        }
    }

    public FieldProperty setMandatory(boolean mandatory) {
        setFlag(MANDATORY, mandatory);
        if (mandatory) {
            setFlag(READ_ONLY, false);
        }
        return this;
    }

    public FieldProperty setWriteOnce(boolean writeOnce) {
        setFlag(WRITE_CONCE, writeOnce);
        return this;
    }

    public FieldProperty setVisible(boolean visible) {
        setFlag(VISIBLE, visible);
        // reset the model
        return this;
    }

    public boolean isVisible() {
        return (option & VISIBLE) == VISIBLE;
    }

    public boolean isReadOnly() {
        return ((option & READ_ONLY) == READ_ONLY || (permission != null && !permission.mayUpdate()));
    }

    public FieldProperty setReadOnly(boolean readOnly) {
        setFlag(READ_ONLY, readOnly);
        return this;
    }


    public boolean isEnabled() {
        return !isReadOnly();
    }

    public FieldProperty setEnabled(boolean enabled) {
        return setReadOnly(!enabled);
    }

    public boolean shouldSetDefault() {
        return (option & SET_DEFAULT) == SET_DEFAULT;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public FieldProperty setDefault(String defaultValue) {
        setFlag(SET_DEFAULT, defaultValue != null);
        this.defaultValue = defaultValue;
        return this;
    }

    public boolean shouldSetCaps() {
        return ((option & SET_CAPS) == SET_CAPS) && (DEFAULT_UPPERCASE == null || DEFAULT_UPPERCASE);
    }

    public FieldProperty setCaps(boolean caps) {
        setFlag(SET_CAPS, caps);
        return this;
    }

    public Integer getMin() {
        return min;
    }

    public FieldProperty setMin(Integer min) {
        this.min = min;
        return this;
    }

    public Integer getMax() {
        return max;
    }

    public Long getMaxValue() {
        return maxValue;
    }

    public FieldProperty setMax(Integer max) {
        this.max = max;
        return this;
    }

    public Integer getRows() {
        return rows;
    }

    public FieldProperty setRows(Integer rows) {
        this.rows = rows;
        return this;
    }

    /**
     * build a generic regular expression based
     *
     * @param type
     * @return
     */
    public String getGenericRegularExpressionFromType(Class type) {
        if (type.equals(Integer.class)) {
            setErrorMessage("Enter valid number");
            return "-?[0-9]{1,7}";
        }
        if (type.equals(Long.class)) {
            setErrorMessage("Enter valid number");
            return "-?[0-9]{1,10}";
        }
        // have special developed field to handle these cases
//        if (type.equals(Double.class) || type.equals(Float.class) || type.equals(BigDecimal.class)) {
//            setErrorMessage("Enter valid number");
//            return "^[+-]?(\\d*\\.)?\\d+$";
//        }
        if (type.equals(Short.class)) {
            setErrorMessage("Enter valid number");
            return "-?[0-9]{1,3}";
        }
        return null;
    }

    public void clear() {
        defaultSetList.clear();
    }


    /**
     * apply the regular expression mask
     *
     * @param abstractTextField
     */
    private void applyMask(AbstractTextField abstractTextField, LField field) {
        String regex = null;
        if (maskId != null && maskId != MaskId.ANY) {
            regex = maskId.getRegExp();
            String errorMsg = field.getLayout().getMaskMessage().getMessage(maskId.getMsgCode());
            setErrorMessage(errorMsg == null ? "ERROR MSG LOOKUP NOT FOUND" : errorMsg);
        } else {
            regex = customRegEx == null ?
                    getGenericRegularExpressionFromType(field.getFieldType()) : customRegEx;
        }
        CSValidator validator = new CSValidator();
        if (!field.isAuditable()) {
            validator.extend(abstractTextField);
            validator.setPreventInvalidTyping(false);
            validator.setErrorMessage(getErrorMessage() == null ? "ERROR MSG LOOKUP NOT FOUND" : getErrorMessage());
            validator.setJavaScript(Util.getInstance().getJavaScriptForRegularExpression(
                    regex, getErrorMessage(), min, max, shouldSetCaps()));
        }
        UIProperties ui = field.getUI();
        abstractTextField.removeAllValidators();
        if (regex != null) {
            abstractTextField.addValidator(new RegexpValidator(
                    shouldSetCaps() ? regex.replace("A-Z", "A-z") : regex, getErrorMessage()));

        }
        if (min != null || max != null) {
            abstractTextField.addValidator(
                    new LengthValidator(min, max, isMandatory(), field.getCaption())
            );
        }

    }


    public static class LengthValidator implements Validator {

        private Integer min, max;
        private boolean required = false;
        private String fieldCaption;

        public LengthValidator(Integer min, Integer max, boolean required, String fieldCaption) {
            this.min = min;
            this.max = max;
            this.required = required;
            this.fieldCaption = fieldCaption;
        }

        @Override
        public void validate(Object value) throws InvalidValueException {
            if (value == null || value.toString().isEmpty()) {
                if (required) {
                    throw new InvalidValueException(String.format(AbstractView.getLocaleValue(ToolkitLocaleId.VALUE_IS_REQUIRED) + " %s", fieldCaption));
                } else {
                    return;
                }
            }
            if (min != null && value.toString().length() < min) {
                throw new InvalidValueException(String.format(AbstractView.getLocaleValue(ToolkitLocaleId.ERROR_MESSAGE_MIN_LENGTH), fieldCaption, min));
            }
            if (max != null && value.toString().length() > max) {
                throw new InvalidValueException(String.format(AbstractView.getLocaleValue(ToolkitLocaleId.ERROR_MESSAGE_MAX_LENGTH), fieldCaption, max));
            }
        }
    }

    public static interface BeforeApplyCallback {
        void onBeforeApply(FieldProperty fieldProperty, com.vaadin.ui.Field vaadinField, LField field);
    }
}
