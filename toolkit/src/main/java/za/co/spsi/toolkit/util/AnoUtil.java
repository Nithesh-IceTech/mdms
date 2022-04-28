package za.co.spsi.toolkit.util;

import za.co.spsi.toolkit.ano.AgencyUIQualifier;
import za.co.spsi.toolkit.ano.UI;
import za.co.spsi.toolkit.ano.UIField;


/**
 * Created by jaspervdb on 2016/04/29.
 */
public class AnoUtil {

    public static boolean agencyMatch(int[] agencies, Integer agency) {
        for (int i : agencies) {
            if (agency.equals(i)) {
                return true;
            }
        }
        return false;
    }

    public static UIField getUIFieldAno(AgencyUIQualifier agencyUIQualifier, Integer agency, Integer parentAgency) {

        if (agencyUIQualifier == null) {
            return null;
        }

        // Check if my agency has been defined
        for (UIField uiField : agencyUIQualifier.uiField()) {
            if (agencyMatch(uiField.agency(), agency)) {
                return uiField;
            }
        }

        // Check if my parent agency has been defined
        for (UIField uiField : agencyUIQualifier.uiField()) {
            if (agencyMatch(uiField.agency(), parentAgency)) {
                return uiField;
            }
        }

        // Check if default agency was defined
        for (UIField uiField : agencyUIQualifier.uiField()) {
            for (int i : uiField.agency()) {
                if (0 == i) {
                    return uiField;
                }
            }
        }
        return null;
    }

    public static UI getUIAno(AgencyUIQualifier agencyUIQualifier, Integer agency, Integer parentAgency) {

        if (agencyUIQualifier == null) {
            return null;
        }

        // Check if my agency has been defined
        for (UI ui : agencyUIQualifier.ui()) {
            if (agencyMatch(ui.agency(), agency)) {
                return ui;
            }
        }

        // Check if my parent agency has been defined
        for (UI ui : agencyUIQualifier.ui()) {
            if (agencyMatch(ui.agency(), parentAgency)) {
                return ui;
            }
        }

        // Check if default agency was defined
        for (UI ui : agencyUIQualifier.ui()) {
            for (int i : ui.agency()) {
                if (0 == i) {
                    return ui;
                }
            }
        }
        return null;
    }
}
