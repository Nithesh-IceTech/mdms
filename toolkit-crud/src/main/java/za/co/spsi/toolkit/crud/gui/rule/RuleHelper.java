package za.co.spsi.toolkit.crud.gui.rule;


import com.vaadin.ui.Notification;
import za.co.spsi.toolkit.ano.AgencyUIQualifier;
import za.co.spsi.toolkit.ano.UIField;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.crud.gui.render.VaadinNotification;
import za.co.spsi.toolkit.util.AnoUtil;

import javax.enterprise.context.Dependent;
import java.util.List;

/**
 * Created by jaspervdb on 4/6/16.
 */
@Dependent
public class RuleHelper {

    public boolean validate(List<Rule> rules) {
        for (Rule rule : rules) {

            // Check if rule applied to agency
            AgencyUIQualifier agencyUIQualifier = rule.getLayout().getField(rule).getAnnotation(AgencyUIQualifier.class);
            UIField uiField =
                    rule.getLayout().getField(rule).getAnnotation(UIField.class) != null ?
                            rule.getLayout().getField(rule).getAnnotation(UIField.class) :
                            AnoUtil.getUIFieldAno(agencyUIQualifier, ToolkitCrudConstants.getChildAgencyId(), ToolkitCrudConstants.getParentAgencyId());

            if (agencyUIQualifier == null || uiField != null) {
                String msg = rule.validateRule();
                if (msg != null) {
                    // show in a dialog
                    VaadinNotification.show(msg, Notification.Type.ERROR_MESSAGE);
                    return false;
                }
            }
        }
        return true;
    }

}
