package za.co.spsi.toolkit.crud.gui.custom;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.crud.audit.gui.AuditLayout;
import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import za.co.spsi.toolkit.crud.locale.VaadinLocaleHelper;
import za.co.spsi.toolkit.crud.util.Util;
import za.co.spsi.toolkit.db.audit.AuditDetailEntity;

import static za.co.spsi.toolkit.crud.gui.render.AbstractView.getLocaleValue;
import static za.co.spsi.toolkit.ee.util.BeanUtil.getBean;

/**
 * Created by jaspervdb on 2016/11/02.
 */
public class ReviewFieldWrapper extends CustomField<Object> implements Reviewable {

    private LField refField;
    //    private TextField textField = new TextField();
    private Button reviewBtn = new Button(FontAwesome.STAR);
    private com.vaadin.ui.Field vField;
    private Window window = null;

    public ReviewFieldWrapper(LField refField, com.vaadin.ui.Field vField) {
        this.refField = refField;
        this.vField = vField;
        init();
    }

    public void applyProperties() {
        vField.setCaption(null);
        setCaption(VaadinLocaleHelper.getTranslatedCaption(refField.getCaption()));
    }

    private void init() {
        reviewBtn.addStyleName(ValoTheme.BUTTON_TINY);
        reviewBtn.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
        reviewBtn.setTabIndex(refField.getLayout().getFields().size()+refField.getLayout().getFields().indexOf(refField));
        vField.setWidth("100%");

        reviewBtn.addClickListener((Button.ClickListener) event -> {
            // open review window

            AuditLayout.ReviewDetailView layout = getBean(refField.getLayout().getBeanManager(), AuditLayout.ReviewDetailView.class);
            layout.getPermission().setPermissionFlag(0);
            layout.setAuditField(refField);

            window= Util.showInWindow(AbstractView.getLocaleValue(ToolkitLocaleId.AUDIT_CAPTION)+ " " + refField.getCaption(),
                    layout.buildList(refField.getLayout().getDataSource(),
                            (layout1, source,newEvent, entityDB) -> MessageBox.createQuestion().withCaption(AbstractView.getLocaleValue(ToolkitLocaleId.AUDIT_REVIEW_CAPTION))
                            .withMessage(AbstractView.getLocaleValue(ToolkitLocaleId.AUDIT_REVIEW_CONFIRMATION))
                            .withYesButton(() -> {
                                refField.setSerial(((AuditDetailEntity) entityDB).newValue.getSerial());
                                refField.getLayout().intoControl();
                                // force an update
                                refField.getField().setChanged(true);
                                setReviewable(false);
                                window.close();
                            }, ButtonOption.caption(AbstractView.getLocaleValue(ToolkitLocaleId.YES).toUpperCase()),
                                    ButtonOption.closeOnClick(true)).
                                            withCancelButton(ButtonOption.caption(getLocaleValue(ToolkitLocaleId.CANCEL).toUpperCase()),
                                                    ButtonOption.closeOnClick(true)).
                            open(), false) , "90%", "50%");

            window.setModal(true);
        });
    }

    public Field getVaadinField() {
        return vField;
    }

    public void setReviewable(boolean value) {
        reviewBtn.addStyleName(value?ValoTheme.BUTTON_BORDERLESS_COLORED:ToolkitTheme.BUTTON_BORDERLESS_LIGHT_GREAY);
        reviewBtn.removeStyleName(!value?ValoTheme.BUTTON_BORDERLESS_COLORED:ToolkitTheme.BUTTON_BORDERLESS_LIGHT_GREAY);

    }

    @Override
    protected Component initContent() {
        return new MHorizontalLayout(vField, reviewBtn).withExpand(vField, 2f).withSpacing(true).withFullWidth();
    }

    @Override
    public Class<? extends String> getType() {
        return String.class;
    }

    @Override
    public void setValue(Object newFieldValue)
            throws com.vaadin.data.Property.ReadOnlyException,
            Converter.ConversionException {
        vField.setValue(newFieldValue);
        super.setValue(newFieldValue);
    }

    @Override
    public Object getValue() {
        return vField.getValue();
    }

    @Override
    public void addValueChangeListener(ValueChangeListener listener) {
        vField.addValueChangeListener(listener);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        vField.setEnabled(enabled);
        reviewBtn.setEnabled(enabled);
    }
}
