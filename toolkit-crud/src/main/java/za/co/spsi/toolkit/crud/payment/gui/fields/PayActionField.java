package za.co.spsi.toolkit.crud.payment.gui.fields;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
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

import javax.sql.DataSource;

public class PayActionField extends VirtualField implements PayActionFieldListener {

    private Toolbar.ToolbarButton btnPay;
    private PayActionFieldListener listener;
    private boolean payEnabled = false, payVisible = true;
    public String feeType;
    public MLCSLookupField payStatus;

    public PayActionField(PayActionFieldListener listener, Layout model, String feeType,  MLCSLookupField paystatus) {
        super(ToolkitLocaleId.PAYMENT, model);
        this.listener = listener;
        this.feeType = feeType;
        this.payStatus = paystatus;

    }

    public boolean isPayEnabled() {
        return payEnabled;
    }

    public void setPayEnabled(boolean payEnabled) {
        this.payEnabled = payEnabled;
    }

    public boolean isPayVisible() {
        return payVisible;
    }

    public void setPayVisible(boolean payVisible) {
        this.payVisible = payVisible;
    }

    @Override
    public Component buildComponent() {
        // get the names of the status
        LookupResultList lookupResultList = getLayout().getLookupServiceHelper().executeLookupRequest(
                Constants.ENTITYSTAT, ToolkitCrudConstants.getLocale(), ToolkitCrudConstants.getAgencyId());
        btnPay = new Toolbar.ToolbarButton(FontAwesome.DOLLAR);

        Button.ClickListener clickListener = (Button.ClickListener) event -> listener.action(PayActionField.this);

        btnPay.addClickListener(clickListener);

        MHorizontalLayout layout = new MHorizontalLayout(btnPay).
                withMargin(new MarginInfo(false, true, false, true)).withSpacing(true).withMargin(false);

        return new MVerticalLayout(layout).alignAll(Alignment.MIDDLE_CENTER).withMargin(new MarginInfo(true, false, true, false));
    }

    @Override
    public void action(PayActionFieldListener listener) {
        getLayout().save();
        getLayout().intoControl();
    }

    @Override
    public void intoBindings() {
    }

    @Override
    public void intoControl() {
        if(ToolkitConstants.PAID_STATUS_PENDING.equals(this.payStatus.get())){
            btnPay.setEnabled(false);
        }
    }

    @Override
    public void applyProperties() {
        super.applyProperties();
        btnPay.setEnabled(getProperties().isEnabled());
    }

    public void makePayment(DataSource dataSource, String entityId) {
    }

}

