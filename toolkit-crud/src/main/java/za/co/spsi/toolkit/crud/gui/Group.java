package za.co.spsi.toolkit.crud.gui;


import com.vaadin.ui.Component;
import za.co.spsi.toolkit.ano.Qualifier;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import za.co.spsi.toolkit.crud.gui.render.FormPanel;

import static za.co.spsi.toolkit.crud.gui.ToolkitUI.applyPermission;

/**
 * Created by jaspervdb on 2/11/16.
 */
public class Group extends AbstractView {

    protected LFieldList fields = new LFieldList();
    protected String faIcon;
    private String labelWidth = "30%";
    private boolean nameGroup,shortNameGroup;
    private Class<? extends FormPanel> customForm;

    public Group(String captionId, String faIcon, boolean nameGroup,Layout layout) {
        super(captionId,layout);
        this.faIcon = faIcon;
        layout.add(this);
    }
    public Group(String captionId, String faIcon, Layout layout) {
        this(captionId,faIcon,false,layout);
    }

    public Group(String captionId, Layout layout) {
        this(captionId,(String)null,layout);
    }

    public Group(String captionId, boolean nameGroup,Layout layout) {
        this(captionId,null,nameGroup,layout);
    }

    public Group(LField fields[],String captionId, Layout layout) {
        this(captionId,layout);
        addViews(fields);
        this.fields = new LFieldList(fields);
    }

    public void setCustomForm(Class<? extends FormPanel> customForm) {
        this.customForm = customForm;
    }

    /**
     *  init this a
     * @param layout
     * @param fields
     */
    public Group(String captionId, Layout layout, LField ... fields) {
        this(fields,captionId, layout);
    }

    public Group setNameGroup() {
        nameGroup = true;
        // remove yourself from any Panes
        for (Pane pane : getLayout().getPanes()) {
            pane.getViews().remove(this);
        }
        return this;
    }

    public Group setShortNameGroup() {
        shortNameGroup = true;
        return this;
    }

    public Group setFaIcon(String faIcon) {
        this.faIcon = faIcon;
        return this;
    }

    private FormPanel getForm() {
        try {
            UIProperties ui = getUI();
            FormPanel formPanel =  customForm != null?customForm.newInstance():new FormPanel();
            formPanel.setCaption(getCaption());
            formPanel.setCols(ui.getColumns());
            formPanel.setDescription(ui.getDescription());
            formPanel.initForm();
            return formPanel;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Component buildComponent() {
        FormPanel formPanel = getForm();
        applyPermission(fields,getLayout().getField(this).getAnnotation(Qualifier.class));
        formPanel.setFields(this,fields.toArray(new LField[]{}));
        applyPermission(formPanel,getLayout().getField(this).getAnnotation(Qualifier.class));
        return formPanel;
    }

    public boolean isNameGroup() {
        return nameGroup;
    }

    public boolean isShortNameGroup() {
        return shortNameGroup;
    }

    public void addField(LField field) {
        fields.add(field);
    }

    public LFieldList getFields() {
        // ensure that fields have been wired
        getLayout().getMainEntity().getFields();
        return fields.apply();
    }

    @Override
    public void intoBindings() {
        fields.intoBindings();
    }

    public void intoControl() {
        fields.intoControl();
    }

    @Override
    public void applyProperties() {}

    public static class Disable extends Group {

        public Disable(Layout layout) {
            super(null, layout);
        }

        @Override
        public Component buildComponent() {
            return null;
        }

        @Override
        public void applyProperties() {

        }
    }
}
