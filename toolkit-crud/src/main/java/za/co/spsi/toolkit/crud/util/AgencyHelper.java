package za.co.spsi.toolkit.crud.util;

import com.google.common.primitives.Ints;
import za.co.spsi.lookup.dao.LookupResultList;
import za.co.spsi.toolkit.ano.AgencyPostConstruct;
import za.co.spsi.toolkit.ano.AgencyUIQualifier;
import za.co.spsi.toolkit.ano.UI;
import za.co.spsi.toolkit.ano.UIField;
import za.co.spsi.toolkit.crud.gui.lookup.ToolkitLookupServiceHelper;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.ee.properties.ConfValue;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jaspervdb on 2016/11/09.
 */
@Dependent
@Singleton
public class AgencyHelper {

    public static boolean ENABLED = true;

    @Inject
    @ConfValue("parent_agency")
    private String parentAgency;

    @Inject
    @ConfValue("agency_map")
    private Map<String, Properties> agencyMap;

    private LookupResultList lookupResults = new LookupResultList(null);

    @Inject
    private ToolkitLookupServiceHelper lookupServiceHelper;

    public String getParentAgency() {
        return parentAgency;
    }

    public Map<String, Properties> getAgencyMap() {
        return agencyMap;
    }

    public LookupResultList getLookupResults() {
        return lookupResults;
    }

    public ToolkitLookupServiceHelper getLookupServiceHelper() {
        return lookupServiceHelper;
    }

    @PostConstruct
    protected void init() {

        if (ENABLED) {
            for (String agency : parentAgency.split("\\,")) {
                lookupResults.addAll(lookupServiceHelper.getAllAgencies(ToolkitCrudConstants.getLocale(), agency));
            }

            // filter the mapped ones
            for (int i = 0; i < lookupResults.size(); i++) {
                if (!agencyMap.keySet().contains(lookupResults.get(i).getLookupCode())) {
                    lookupResults.remove(i--);
                }
            }
        }
    }

    public static boolean inAgency(int agency) {
        return agency == 0 || Arrays.asList(ToolkitCrudConstants.getAgencyId().split(",")).stream().filter(s -> Integer.parseInt(s) == agency).count() > 0;
    }

    public static boolean inAgency(int agencies[]) {
        return Ints.asList(agencies).stream().filter(a -> inAgency(a)).count() > 0;
    }

    public static boolean inAgency(String agencies[]) {
        return Arrays.stream(ToolkitCrudConstants.getAgencyId().split(",")).anyMatch(v ->Arrays.asList(agencies).contains(v));
    }

    public static List<Method> getAgencyPostConstruct(Class type) {
        return Arrays.stream(type.getDeclaredMethods()).filter(m -> m.getAnnotation(AgencyPostConstruct.class) != null &&
                Ints.asList(m.getAnnotation(AgencyPostConstruct.class).agency()).stream().filter(a -> AgencyHelper.inAgency(a)).count() > 0).collect(
                Collectors.toCollection(ArrayList::new));
    }

    public static List<UI> getUIs(AgencyUIQualifier qualifier) {
        return Arrays.stream(qualifier.ui()).filter(ui -> inAgency(ui.agency())).collect(Collectors.toCollection(ArrayList::new));
    }

    public static List<UIField> getUIFields(AgencyUIQualifier qualifier) {
        return Arrays.stream(qualifier.uiField()).filter(ui -> inAgency(ui.agency())).collect(Collectors.toCollection(ArrayList::new));
    }

    public static boolean removed(AgencyUIQualifier qualifier) {
        return getUIs(qualifier).stream().filter(ui -> ui.visibility() == UI.REMOVED).count() > 0 ||
                getUIFields(qualifier).stream().filter(ui -> ui.visibility() == UI.REMOVED).count() > 0;
    }


    public static class AgencySetEvent {
        private String agency;

        public AgencySetEvent(String agency) {
            this.agency = agency;
        }

        public String getAgency() {
            return agency;
        }

        public void setAgency(String agency) {
            this.agency = agency;
        }
    }


}
