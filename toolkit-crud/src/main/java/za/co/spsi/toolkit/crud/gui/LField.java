package za.co.spsi.toolkit.crud.gui;

import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.*;
import com.vaadin.ui.DateField;
import org.apache.commons.lang3.StringUtils;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.ano.AgencyUIQualifier;
import za.co.spsi.toolkit.ano.UIField;
import za.co.spsi.toolkit.crud.gui.custom.IntField;
import za.co.spsi.toolkit.crud.gui.custom.ReviewFieldWrapper;
import za.co.spsi.toolkit.crud.gui.custom.Reviewable;
import za.co.spsi.toolkit.crud.gui.fields.*;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import za.co.spsi.toolkit.crud.gui.render.GridColumnRenderer;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.crud.gui.render.VaadinNotification;
import za.co.spsi.toolkit.crud.locale.VaadinLocaleHelper;
import za.co.spsi.toolkit.crud.util.Util;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.util.AnoUtil;
import za.co.spsi.toolkit.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static za.co.spsi.toolkit.util.Util.getConvertedValue;

/**
 * Created by jaspervdb on 4/12/16.
 */
public class LField<T> extends AbstractView {

    public static final Logger LOG = Logger.getLogger(LField.class.getName());
    protected Field field;
    private T virtualValue;
    // colname for virtual fields
    private String colName;
    private java.lang.reflect.Field myField = null;
    private FieldProperty fieldProperty = null;
    private String dateFormat = "dd/MM/yyyy HH:mm";

    private List<ValueChangeListener> valueChangeListenerList = new ArrayList<ValueChangeListener>(),
            dynamicFieldBehaviours = new ArrayList<ValueChangeListener>();

    // callback before permission is applied
    private FieldProperty.BeforeApplyCallback beforeApplyPermissionCallback;

    public List<ValueChangeListener> getDynamicFieldBehaviours() {
        return dynamicFieldBehaviours;
    }

    private GridColumnRenderer gridColumnRenderer = null;

    public static Util util = Util.getInstance();

    public LField(Field field, String captionId, Layout layout) {
        super(layout);
        setCaptionId(captionId);
        this.field = field;
        layout.add(this);
    }

    public LField(String captionId, String colName, Layout layout) {
        this((Field) null, captionId, layout);
        this.colName = colName;
    }

    public FieldProperty.BeforeApplyCallback getBeforeApplyPermissionCallback() {
        return beforeApplyPermissionCallback;
    }

    public void setBeforeApplyPermissionCallback(FieldProperty.BeforeApplyCallback beforeApplyPermissionCallback) {
        this.beforeApplyPermissionCallback = beforeApplyPermissionCallback;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public String getColName() {
        return colName != null ? colName : EntityDB.getColumnName(field);
    }

    public String getName() {
        return getLayout().getField(this).getName();
    }

    public String getFullColName() {
        return colName != null ? colName : EntityDB.getFullColumnName(field);
    }

    public FieldProperty getProperties() {
        if (fieldProperty == null) {
            fieldProperty = getMyUIField() != null ?
                    new FieldProperty(getMyUIField()) : new FieldProperty();
        }
        fieldProperty.setPermission(getLayout().getPermission());
        return fieldProperty;
    }

    public GridColumnRenderer getGridColumnRenderer() {
        return gridColumnRenderer;
    }

    public void setGridColumnRenderer(GridColumnRenderer gridColumnRenderer) {
        this.gridColumnRenderer = gridColumnRenderer;
    }

    public void setProperties(FieldProperty fieldProperty) {
        this.fieldProperty = fieldProperty;
        fieldProperty.setPermission(getLayout().getPermission());
    }

    private UIField getMyUIField() {
        return getMyField() != null && getMyField().getAnnotation(UIField.class) != null ?
                getMyField().getAnnotation(UIField.class) :
                getMyField() != null ? AnoUtil.getUIFieldAno(getMyField().getAnnotation(AgencyUIQualifier.class),
                        ToolkitCrudConstants.getChildAgencyId(), ToolkitCrudConstants.getParentAgencyId()) : null;

    }

    public boolean isAuditable() {
        return getLayout().isAuditable() && getField() != null && getField().isAudit();
    }

    /**
     * @return the value converter to a string
     */
    public String getAsString() {
        T value = get();
        return value == null ? null : "" + value;
    }

    /**
     * @return the actual value displayed in the vaadin field
     */
    public String getDisplayValue() {
        return getAsString();
    }

    /**
     * get the fields value
     *
     * @return
     */
    public T get() {
        try {
            return field == null ? virtualValue : (T) getConvertedValue(field.get(), getFieldType(), field.get());
        } catch (RuntimeException e) {
            TAG.warning(String.format("Error occurred on Layout %s Field %s ", getLayout().getClass(), getColName()));
            throw e;
        }
    }

    public T getNonNull() {
        Assert.notNull(field, "Can not be called to virtual fields");
        return (T) (T) getConvertedValue(field.get(), getFieldType(), field.getNonNull());
    }

    @Override
    public Class getFieldType() {
        try {
            return getMyField() != null && hasGenericType() ? super.getFieldType() : field != null ? field.getType() : String.class;
        } catch (RuntimeException ce) {
            throw new RuntimeException("Error occurred on field " + getCaption(), ce);
        }

    }

    /**
     * set the reference value
     *
     * @param value
     */
    public void set(T value) {
        if (field != null) {
            field.set(getConvertedValue(value, field.getType(), value));
        } else {
            virtualValue = value;
        }
        intoControl();
    }

    public void setSerial(String value) {
        setSerial(value, false);
    }

    public void setSerial(String value, boolean setOldValue) {
        field.set(getConvertedValue(value, field.getType(), value), setOldValue);
        getLayout().dataChangeEvent();
    }

    /**
     * add dynamic field behaviour class on this field
     * Note that you must still set the behaviour on the class
     *
     * @return
     */
    public DynamicFieldBehaviour addDynamicFieldBehaviour() {
        DynamicFieldBehaviour dynamicFieldBehaviour = new DynamicFieldBehaviour(this);
        dynamicFieldBehaviours.add(dynamicFieldBehaviour);
        valueChangeListenerList.add(dynamicFieldBehaviour);
        return dynamicFieldBehaviour;
    }

    public void addValueChangeListener(ValueChangeListener valueChangeListener) {
        getProperties().setImmediate(true);
        if (!valueChangeListenerList.contains(valueChangeListener)) {
            valueChangeListenerList.add(valueChangeListener);
        }
    }

    private com.vaadin.ui.Field getRawVaadinField() {
        if (getComponent() instanceof com.vaadin.ui.Field) {
            return (com.vaadin.ui.Field) getComponent();
        }
        return null;
    }

    public com.vaadin.ui.Field getVaadinField() {
        return getRawVaadinField() != null && getRawVaadinField() instanceof ReviewFieldWrapper ?
                ((ReviewFieldWrapper) getRawVaadinField()).getVaadinField() : getRawVaadinField();
    }

    @Override
    public Component buildComponent() {
        final com.vaadin.ui.Field vaadinField = buildVaadinField();
        if (getCaption() != null) {
            vaadinField.setCaption(getCaption());
        }
        util.applyUI(vaadinField, getUI());
        // add value change listener
        if (!valueChangeListenerList.isEmpty()) {
            getLayout().dataChangeEvent();
            vaadinField.addValueChangeListener((Property.ValueChangeListener) event -> {
                try {
                    intoBindings();
                    for (ValueChangeListener listener : valueChangeListenerList) {
                        listener.valueChanged(LField.this, vaadinField, getLayout().isBusyConstructing(),
                                vaadinField.getValue() == null || StringUtils.isEmpty(vaadinField.getValue().toString()));
                    }
                } catch (InvalidFieldValueException e) {
                    if (!getLayout().isBusyConstructing()) {
                        VaadinNotification.show(e.getMessage(), Notification.Type.ERROR_MESSAGE);
                    }
                }
            });
        } else {
            if (getLayout().isDataChangeEventListener()) {
                vaadinField.addValueChangeListener( event -> getLayout().dataChangeEvent());
            }
        }
        return vaadinField;
    }


    public void markState() {
        if (getField() != null) {
            getField().markState();
        }
    }

    public void resetState() {
        if (getField() != null) {
            getField().resetState();
        }
    }

    /**
     * overload this method to return a different gui component
     * build a generic Vaadin Field component based on its property type
     *
     * @return
     */
    public com.vaadin.ui.Field buildVaadinField() {
        com.vaadin.ui.Field field;
        if (getFieldType().equals(String.class) && getProperties().shouldSetCaps()) {
            field = new UppercaseTextField();
        } else if (getFieldType().equals(Long.class) || getFieldType().equals(Integer.class)) {
            field = new IntField();
        } else if (getFieldType().equals(Double.class) ||
                getFieldType().equals(Float.class) || getFieldType().equals(BigDecimal.class)) {
            field = new DecimalTextField(getProperties().getFormat());
        } else if (getFieldType().equals(Boolean.class)) {
            field = new CheckBox();
        } else {
            field = DefaultFieldFactory.createFieldByPropertyType(getFieldType());
        }
        if (field instanceof DateField) {
            field = new IceDateField();

            String dateFormat =
                    !StringUtils.isEmpty(getProperties().getFormat()) ?
                            getProperties().getFormat() : getDateFormat();

            ((DateField) field).setDateFormat(dateFormat);
            ((DateField) field).setResolution(
                    dateFormat.indexOf("ss") != -1 ? Resolution.SECOND :
                            dateFormat.indexOf("mm") != -1 ? Resolution.MINUTE :
                                    dateFormat.toUpperCase().indexOf("HH") != -1 ? Resolution.HOUR :
                                            Resolution.DAY
            );

        }
        if (dynamicFieldBehaviours.size() > 0) {
            getProperties().setImmediate(true);
        }
        if (field instanceof AbstractTextField) {
            ((AbstractTextField) field).setNullRepresentation("");
        }
        return getLayout().isAuditable() ? wrapInReviewField(field) : field;
    }

    /**
     * apply the field's properties, ie mask etc.
     */
    public void applyProperties() {
        getProperties().applyProperties(getVaadinField() != null ? getVaadinField() : getComponent(), this);
        if (getRawVaadinField() instanceof ReviewFieldWrapper) {
            ((ReviewFieldWrapper) getRawVaadinField()).applyProperties();
        }
    }

    protected com.vaadin.ui.Field intoBindingsWithNoValidation() {
        return intoBindingsWithNoValidation(false);
    }

    protected com.vaadin.ui.Field intoBindingsWithNoValidation(boolean setOldValue) {
        com.vaadin.ui.Field vaadinField = getVaadinField();
        if (vaadinField != null) {
            if (vaadinField instanceof AbstractTextField) {
                ((AbstractTextField) vaadinField).setComponentError(null);
                AbstractTextField textField = (AbstractTextField) vaadinField;
                try {
                    if (textField.getConverter() != null) {
                        field.set(textField.getConvertedValue(), setOldValue);
                    } else {
                        setSerial(getProperties().shouldSetCaps() ? textField.getValue().toUpperCase() :
                                textField.getValue(), setOldValue);
                    }
                } catch (RuntimeException re) {
                    LOG.log(Level.WARNING, re.getMessage());
                }
            } else {
                try {
                    set((T) vaadinField.getValue());
                } catch (RuntimeException re) {
                    LOG.log(Level.WARNING, re.getMessage());
                }
            }
        }
        return vaadinField;
    }

    private boolean isEmpty(Object value) {
        return value instanceof String ? StringUtils.isEmpty((String) value) : value == null;
    }

    public void intoBindings() {
        try {
            com.vaadin.ui.Field vaadinField = getVaadinField();
            try {
                if (vaadinField != null) {
                    if (vaadinField.isEnabled() && (getProperties().isMandatory() || !isEmpty(vaadinField.getValue()))) {
                        vaadinField.validate();
                        vaadinField.commit();
                    }
                } else {
                    if (getProperties().isMandatory() && get() == null) {
                        throw new InvalidFieldValueException(VaadinLocaleHelper.getValue(ToolkitLocaleId.VALUE_IS_REQUIRED), null);
                    }
                }
                intoBindingsWithNoValidation(true);
            } catch (Validator.InvalidValueException e) {
                String msg;
                if (e.getCauses().length > 1) {
                    msg = e.getCauses()[0].getMessage() + " [" + getCaption() + "]";
                } else {
                    msg = e.getMessage() + " [" + getCaption() + "]";
                }
                LOG.log(Level.WARNING, e.getMessage(), e.getCause());
                if (vaadinField != null) {
                    vaadinField.focus();
                }
                throw new InvalidFieldValueException(msg, vaadinField, e.getCauses());
            }
        } catch (RuntimeException ex) {
            if (!(ex instanceof InvalidFieldValueException)) {
                throw new RuntimeException(String.format("FIELD %s: CLASS: %s : %s", getColName(), getClass().getName(), ex.getMessage(), ex));
            } else {
                throw ex;
            }
        }
    }

    @Override
    public void intoControl() {
        try {
            com.vaadin.ui.Field vaadinField = getVaadinField();
            if (vaadinField != null) {
                // ensure that no value change events are fired from this
                if (get() == null && vaadinField instanceof AbstractTextField) {
                    vaadinField.setValue("");
                } else if (vaadinField instanceof NumberField) {
                    vaadinField.setValue(get() == null ? null : new BigDecimal(getAsString()));
                } else if (vaadinField instanceof CheckBox) {
                    vaadinField.setValue(get());
                } else if (vaadinField instanceof DecimalTextField) {
                    ((DecimalTextField) vaadinField).setConvertedValue(get());
                } else if (this instanceof FieldTwinColSelect) {
                    vaadinField.setValue(get());
                } else if (vaadinField instanceof DateField) {
                    Object value = get();
                    vaadinField.setValue(value != null ? value instanceof Long ? new Date((Long) value) : value : null);
                } else {
                    vaadinField.setValue(getAsString());
                }
            }
//            getProperties().applyProperties(vaadinField, this);
            applyProperties();
        } catch (Exception ex) {
            LOG.log(Level.WARNING, String.format("Error occurred on intoControl for %s: %s", getLayout().getClass().getName(), getCaption()));
            if (ex instanceof RuntimeException) {
                throw ex;
            } else {
                throw new RuntimeException(ex.getMessage(), ex);
            }
        }
    }

    public com.vaadin.ui.Field wrapInReviewField(com.vaadin.ui.Field field) {
        return isAuditable() ? new ReviewFieldWrapper(this, field) : field;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public LField setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
        return this;
    }

    public LField setDateTimeFormat() {
        return setDateFormat("dd/MM/yyyy HH:mm");
    }

    void refreshAuditEvent() {
        if (getRawVaadinField() instanceof Reviewable) {
            ((Reviewable) getRawVaadinField()).setReviewable(getLayout().getReviewFields().containsIgnoreCase(field.getName()));
        }
    }

    public void beforeOnScreenEvent() {
        refreshAuditEvent();
    }


    public static class Spacer extends LField {

        public Spacer(Layout layout) {
            super((Field) null, "", layout);
        }

        @Override
        public void intoControl() {
        }

        @Override
        public void intoBindings() {
        }
    }

}
