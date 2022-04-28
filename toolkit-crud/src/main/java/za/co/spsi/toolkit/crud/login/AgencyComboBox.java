package za.co.spsi.toolkit.crud.login;

import com.vaadin.cdi.CDIView;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.UI;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.lookup.dao.LookupResult;
import za.co.spsi.toolkit.crud.gui.ToolkitUI;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.crud.util.AgencyHelper;
import za.co.spsi.toolkit.util.StringList;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import static za.co.spsi.toolkit.crud.gui.render.AbstractView.getLocaleValue;

/**
 * Created by jaspervdbijl on 2017/05/29.
 */
@Dependent
public class AgencyComboBox extends ComboBox {

    @Inject
    private javax.enterprise.event.Event<LogoutEventProcessor.LogoutEvent> logoutEventEvent;

    @Inject
    private AgencyHelper agencyHelper;

    private boolean activated = false;

    public AgencyComboBox() {

    }

    @PostConstruct
    protected void init() {

        removeAllItems();
        StringList agencies = ToolkitUI.getToolkitUI().getAgencyRoleMap() != null?new StringList(ToolkitUI.getToolkitUI().getAgencyRoleMap().keySet()):new StringList();
        agencies.remove("0");
        if (!agencies.isEmpty()) {
            for (String agency : agencies) {
                LookupResult lookupResult = agencyHelper.getLookupResults().getForCode(agency);
                if (lookupResult != null && lookupResult.getDisplayValue() != null) {
                    addItem(agency);
                    setItemCaption(agency, lookupResult.getDisplayValue());

                    if (getValue() == null) {
                        setValue(agency);
                    }
                }
            }
        }
    }

    private String getTxName() {
        CDIView view = UI.getCurrent().getNavigator().getCurrentView() != null?
                UI.getCurrent().getNavigator().getCurrentView().getClass().getAnnotation(CDIView.class):null;
        return view != null?view.value():"home";
    }

    public void activate() {
        if (!activated) {
            setValue(""+ ToolkitCrudConstants.getChildAgencyId());
            activated = true;
            addValueChangeListener((ValueChangeListener) event -> {
                MessageBox.createInfo().withCaption(getLocaleValue(ToolkitLocaleId.SS_SWITCH_AGENCY))
                        .withMessage(getLocaleValue(ToolkitLocaleId.SS_SWITCH_AGENCY_CONFIRM))
                        .withCancelButton(ButtonOption.caption(getLocaleValue(ToolkitLocaleId.CANCEL).toUpperCase()), ButtonOption.closeOnClick(true))
                        .withOkButton(() -> {
                            logoutEventEvent.fire(new LogoutEventProcessor.LogoutEvent(
                                    "UI?"+String.format("token=%s&agency=%s&txName=%s&txName=%s&requestN=%d",
                                            ToolkitUI.getToolkitUI().getToken(), getValue(),getTxName(),"",SSOProcessor.getNewRequest())
                            ));
                        }, ButtonOption.caption(getLocaleValue(ToolkitLocaleId.OK).toUpperCase()), ButtonOption.closeOnClick(true))
                        .open();
            });
        }
    }

}
