package za.co.spsi.toolkit.crud.sync.gui.fields;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.lookup.Constants;
import za.co.spsi.lookup.dao.LookupResultList;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.custom.Toolbar;
import za.co.spsi.toolkit.crud.gui.fields.MLCSLookupField;
import za.co.spsi.toolkit.crud.gui.fields.VirtualField;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.dao.ToolkitConstants;

/**
 * Created by jaspervdb on 2015/09/14.
 */
public class EntityStatusActionField extends VirtualField implements EntityStatusActionFieldListener {

    private MLCSLookupField entitySyncCd;
    private Toolbar.ToolbarButton btnTablet, btnBilling, btnRecall;
    private EntityStatusActionFieldListener listener;
    private boolean billingEnabled = false, billingVisible = true, recallEnabled = true, tabletEnabled = true;

    public EntityStatusActionField(MLCSLookupField entitySyncCd, EntityStatusActionFieldListener listener, Layout model) {
        super(ToolkitLocaleId.SYNC_STATUS, model);
        this.listener = listener;
        this.entitySyncCd = entitySyncCd;
    }

    public void setBillingEnabled(boolean billingEnabled) {
        this.billingEnabled = billingEnabled;
    }

    public void setBillingVisible(boolean billingVisible) {
        this.billingVisible = billingVisible;
    }

    public void setRecallEnabled(boolean recallEnabled) {
        this.recallEnabled = recallEnabled;
    }

    public void setBtnTablet(Toolbar.ToolbarButton btnTablet) {
        this.btnTablet = btnTablet;
    }

    public void setTabletEnabled(boolean tabletEnabled) {
        this.tabletEnabled = tabletEnabled;
    }

    @Override
    public Component buildComponent() {
        // get the names of the status
        LookupResultList lookupResultList = getLayout().getLookupServiceHelper().executeLookupRequest(
                Constants.ENTITYSTAT, ToolkitCrudConstants.getLocale(), ToolkitCrudConstants.getAgencyId());
        btnTablet = new Toolbar.ToolbarButton(FontAwesome.MOBILE);
        btnBilling = new Toolbar.ToolbarButton(FontAwesome.DOLLAR);
        btnRecall = new Toolbar.ToolbarButton(FontAwesome.UNDO);

        btnTablet.setDescription(lookupResultList.getForCode(ToolkitConstants.ENTITY_STATUS_TABLET_PROCESSING.toString()).getDescription());
        btnBilling.setDescription(lookupResultList.getForCode(ToolkitConstants.ENTITY_STATUS_BILLING_PROCESSING.toString()).getDescription());
        btnRecall.setDescription("RECALL SURVEY");

        Button.ClickListener clickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                ToolkitConstants.EntitySyncStatus entitySyncStatus =
                        event.getButton() == btnTablet ? ToolkitConstants.EntitySyncStatus.TABLET :
                                event.getButton() == btnBilling ? ToolkitConstants.EntitySyncStatus.BILLING :
                                        ToolkitConstants.EntitySyncStatus.BACK_OFFICE;

                listener.action(entitySyncStatus, EntityStatusActionField.this);
            }
        };

        btnTablet.addClickListener(clickListener);
        btnBilling.addClickListener(clickListener);
        btnRecall.addClickListener(clickListener);

        MHorizontalLayout layout = new MHorizontalLayout(btnTablet, btnBilling, btnRecall).
                withMargin(new MarginInfo(false, true, false, true)).withSpacing(true).withMargin(false);

        return new MVerticalLayout(layout).alignAll(Alignment.MIDDLE_CENTER).withMargin(new MarginInfo(true, false, true, false));
    }

    @Override
    public void action(ToolkitConstants.EntitySyncStatus entitySyncStatus, EntityStatusActionFieldListener listener) {
        // adjust the status cd
        if(!(entitySyncCd.get().equals(ToolkitConstants.ENTITY_STATUS_BILLING_PROCESSING)
                && (entitySyncStatus.getCode().equals(ToolkitConstants.ENTITY_STATUS_TABLET_PROCESSING)))){
            entitySyncCd.set(entitySyncStatus.getCode());
        }
        getLayout().save();
        getLayout().intoControl();
    }

    @Override
    public void intoBindings() {
    }

    @Override
    public void applyProperties() {
        super.applyProperties();
        btnTablet.setEnabled(getProperties().isEnabled());
        btnBilling.setEnabled(getProperties().isEnabled());
        btnRecall.setEnabled(getProperties().isEnabled());
    }

    public void setBillingIcon(Resource resource) {
        this.btnBilling.setIcon(resource);
    }

    @Override
    public void intoControl() {
        if (ToolkitConstants.ENTITY_STATUS_TABLET_PROCESSING.equals(entitySyncCd.get())) {
            // all should be read only
            btnTablet.setEnabled(false);
            btnBilling.setEnabled(false);
            btnRecall.setEnabled(true);
        } else if (ToolkitConstants.ENTITY_STATUS_BACK_OFFICE_PROCESSING.equals(entitySyncCd.get())) {
            btnTablet.setEnabled(true);
            btnBilling.setEnabled(true);
            btnRecall.setEnabled(false);
        } else if (ToolkitConstants.ENTITY_STATUS_BILLING_PROCESSING.equals(entitySyncCd.get())) {
            btnTablet.setEnabled(true);
            btnBilling.setEnabled(false);
            btnRecall.setEnabled(false);
        } else {
            btnTablet.setEnabled(false);
            btnBilling.setEnabled(false);
            btnRecall.setEnabled(false);
        }

        if (!billingEnabled) {
            btnBilling.setEnabled(false);
        }

        if (!billingVisible) {
            btnBilling.setVisible(false);
        }

        if (!tabletEnabled) {
            btnTablet.setEnabled(false);
        }

        if (!recallEnabled) {
            btnRecall.setVisible(false);
            btnRecall.setEnabled(false);
        }
    }
}

