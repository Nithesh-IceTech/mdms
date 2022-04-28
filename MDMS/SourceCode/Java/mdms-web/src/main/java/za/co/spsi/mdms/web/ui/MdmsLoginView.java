package za.co.spsi.mdms.web.ui;

import com.vaadin.cdi.UIScoped;
import za.co.spsi.locale.annotation.MdmsLocaleId;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import za.co.spsi.toolkit.crud.login.LoginView;
import za.co.spsi.toolkit.ee.properties.ConfValue;

import javax.inject.Inject;


/**
 * Created by jaspervdb on 2015/08/17.
 */
@UIScoped
public class MdmsLoginView extends LoginView {

    @Inject
    @ConfValue("parent_agency")
    private String parentAgency;

    @Inject
    @ConfValue("agency_map")
    private String agencyMap;

    public MdmsLoginView() {
    }

    @Override
    public void buildUI() {
        setHtmlHeader(AbstractView.getLocaleValue(MdmsLocaleId.LOGIN_TITLE));
        setLogoImage("img/background/logo.png");

        super.buildUI();
    }


    @Override
    public String getParentAgency() {
        return parentAgency;
    }
}
