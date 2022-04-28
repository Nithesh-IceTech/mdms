package za.co.spsi.toolkit.crud.gui.fields;

import com.vaadin.data.Property;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;
import org.vaadin.viritin.util.HtmlElementPropertySetter;
import za.co.spsi.lookup.dao.LookupResult;
import za.co.spsi.toolkit.crud.audit.gui.AuditConfig;
import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.crud.locale.Translatable;
import za.co.spsi.toolkit.crud.locale.TranslatableComboBox;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.drivers.Driver;
import za.co.spsi.toolkit.db.drivers.DriverFactory;
import za.co.spsi.toolkit.entity.Field;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jaspervdb
 * Date: 2013/10/31
 * Time: 10:11 PM
 * This field will be visualized by a ComboBox
 */
public class MLCSLookupField<T> extends LField<T> implements Translatable,LeftJoinable {

    private String lookupDefId;
    private List<MLCSLookupField> childReferences = new ArrayList<MLCSLookupField>();
    private MLCSLookupField parentReference;
    private Object customDependency = null;
    private Map<String, String> itemCaption = null;
    private boolean nullAllowed = true;
    private String intoControlValue = null;
    private boolean combineHierarchyNames = false;

    public MLCSLookupField(Field field, String captionId, Layout model,
                           String lookupDefId, MLCSLookupField parentReference) {
        super(field, captionId, model);
        this.lookupDefId = lookupDefId;
        this.parentReference = parentReference;
        // add yourself as a child to the parents
        if (this.parentReference != null) {
            parentReference.addChildReference(this);
        }
    }

    public MLCSLookupField setCombineHierarchyNames(boolean combineHierarchyNames) {
        this.combineHierarchyNames = combineHierarchyNames;
        return this;
    }

    public MLCSLookupField getParentReference() {
        return parentReference;
    }

    public MLCSLookupField(Field field, String captionId, Layout model, String lookupDefId) {
        this(field, captionId, model, lookupDefId, null);
    }

    public Map<String, String> getItemCaption() {
        return itemCaption;
    }

    protected void addChildReference(MLCSLookupField lookupField) {
        childReferences.add(lookupField);
    }

    public String getLookupDefId() {
        return lookupDefId;
    }

    public void setLookupDefId(String lookupDefId) {
        this.lookupDefId = lookupDefId;
        itemCaption = null;
    }

    /**
     * menually set the dependency values
     */
    public void setDependency(Object value) {
        customDependency = value;
        itemCaption = null;
    }

    public Object getCustomDependency() {
        return customDependency;
    }

    public Map<String, String> getItemCaptionMap() {
        if (itemCaption == null) {
            itemCaption = new LinkedHashMap<>();
            List<LookupResult> lookupResults =
                    (parentReference == null && customDependency == null) ? getLayout().getLookupServiceHelper().executeLookupRequest(
                            lookupDefId,
                            ToolkitCrudConstants.getLocale(), ToolkitCrudConstants.getAgencyId()) :
                            getLayout().getLookupServiceHelper().executeLookupMappingRequest(
                                    combineHierarchyNames?parentReference.getLookupDefId()+lookupDefId:lookupDefId,
                                    ToolkitCrudConstants.getLocale(), ToolkitCrudConstants.getAgencyId(),
                                    customDependency != null ? customDependency : parentReference.get());
            for (LookupResult lookupResult : lookupResults) {
                itemCaption.put(lookupResult.getLookupCode(), lookupResult.getDisplayValue());
            }
        }
        return itemCaption;
    }

    /**
     * @return the value displayed in the combobox
     */
    public String getDisplayValue(String value) {
        return getItemCaptionMap().get(value);
    }

    public String getDisplayValue() {
        return getDisplayValue(getAsString());
    }

    public String getDescriptionValue() {
        String value = getDisplayValue();
        return value != null && value.indexOf(" - ") != -1 ? value.substring(value.indexOf(" - ") + 3) : value;
    }

    public void populateLookupFieldComboBox() {
        itemCaption = null;
        ComboBox comboBox = (ComboBox) getVaadinField();
        comboBox.setFilteringMode(FilteringMode.CONTAINS);

        HtmlElementPropertySetter heps = new HtmlElementPropertySetter(comboBox);
        heps.setProperty("./input", "autocorrect", "off");
        heps.setProperty("./input", "autocomplete", "off");
        heps.setProperty("./input", "autocapitalize", "off");


        comboBox.removeAllItems();
        if (customDependency != null || (parentReference == null || parentReference.get() != null)) {
            for (Object key : getItemCaptionMap().keySet()) {
                comboBox.addItem(key);
                comboBox.setItemCaption(key, getItemCaptionMap().get(key));
            }
        }
        if (intoControlValue != null) {
            comboBox.setValue(intoControlValue);
            intoControlValue = null;
        }
    }

    /**
     * build the combobox
     *
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private ComboBox getLookupField() throws IllegalAccessException, InstantiationException {
        final TranslatableComboBox comboBox = new TranslatableComboBox(this);
        comboBox.setScrollToSelectedItem(true);
        comboBox.setImmediate(true);
        setComponent(comboBox);
        // first check that there are no dependencies on this field
        populateLookupFieldComboBox();
        // attach lookup dependencies
        comboBox.addValueChangeListener((Property.ValueChangeListener) event -> {
            MLCSLookupField.this.intoBindingsWithNoValidation();
            for (MLCSLookupField child : MLCSLookupField.this.childReferences) {
                child.populateLookupFieldComboBox();
            }
        });
        return comboBox;
    }

    public void setNullAllowed(boolean nullAllowed) {
        this.nullAllowed = nullAllowed;
    }

    @Override
    public com.vaadin.ui.Field buildVaadinField() {
        try {
            ComboBox comboBox = getLookupField();
            comboBox.setNullSelectionAllowed(nullAllowed);
            setComponent(comboBox);
            return wrapInReviewField(comboBox);
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void intoControl() {
        super.intoControl();
        this.intoControlValue = getAsString();
    }

    public String getLeftJoinSql() {
        String joinTabName = getJoinTabName();
        Driver driver = DriverFactory.getDriver();
        return String.format(
                " left join lookups %s on " +
                        "%s = %s and " +
                        "%s.lookup_def = '%s' and " +
                        "%s = '%s' and " +
                        "%s.lang = '%s' ",
                joinTabName,
                driver.toChar(joinTabName + ".code"),
                driver.toChar(EntityDB.getFullColumnName(getField())),
                joinTabName,
                getNormalisedLookupDefName(),
                driver.toChar(joinTabName + ".agency_id"),
                ToolkitCrudConstants.getChildAgencyId(),
                joinTabName,
                ToolkitCrudConstants.getLocale());
    }

    /**
     * for hierarchy smd dependencies are concatenated
     * @return
     */
    public String getNormalisedLookupDefName() {
        return parentReference != null?
                lookupDefId.startsWith(parentReference.getNormalisedLookupDefName())?lookupDefId.substring(parentReference.getNormalisedLookupDefName().length()):lookupDefId:
                customDependency != null && lookupDefId.indexOf("GROUP")!=-1?lookupDefId.substring(lookupDefId.indexOf("GROUP")+"GROUP".length()):lookupDefId;
    }


    public String getJoinTabName() {
        return String.format("A%d",getLayout().getGroups().getNameGroup().getFields().indexOf(this));
    }

    public String getLookupColName() {
        return String.format(" %s.DESCRIPTION ",getJoinTabName());
    }

    public String getJoinColName() {
        return String.format(" %s.DESCRIPTION as %s ",getJoinTabName(),EntityDB.getColumnName(getField()));
    }

    @Override
    public void translate(String oldLocale, String newLocale) {
        // translate all the lookups
        itemCaption = null;
        ComboBox comboBox = (ComboBox) getVaadinField();
        comboBox.setFilteringMode(FilteringMode.CONTAINS);
        if (customDependency != null || (parentReference == null || parentReference.get() != null)) {
            for (Object key : getItemCaptionMap().keySet()) {
                comboBox.setItemCaption(key, getItemCaptionMap().get(key));
            }
        }
        comboBox.markAsDirty();
    }
}
