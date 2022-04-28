package za.co.spsi.toolkit.crud.gui.custom;

import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.addons.comboboxmultiselect.ComboBoxMultiselect;
import za.co.spsi.toolkit.crud.gui.ToolkitUI;
import za.co.spsi.toolkit.crud.gui.lookup.ToolkitLookupServiceHelper;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.util.StringList;
import za.co.spsi.uaa.util.dto.AgencyRoleMap;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.*;


/**
 * Created by jaspervdbijl on 2017/03/07.
 */
public class AgencySelectorComboBox extends ComboBoxMultiselect {

    @Inject
    private javax.enterprise.event.Event<AgencySelectedEvent> agencySelectedEventEvent;

    @Inject
    ToolkitLookupServiceHelper lookupServiceHelper;

    public AgencySelectorComboBox() {
        setImmediate(true);
    }

    @PostConstruct
    private void init() {
        addStyleName(ValoTheme.COMBOBOX_TINY);
        // display all the agencies that have the survey viewer role
        AgencyRoleMap map = ToolkitUI.getToolkitUI().getAgencyRoleMap();
        map.keySet().stream().forEach(k -> {
            if (getRoleFilter() == null || Arrays.stream(getRoleFilter()).anyMatch(role -> map.get(k).contains(role.toUpperCase()))) {
                addItem(k);
                setItemCaption(k, lookupServiceHelper.getAllAgencies(ToolkitCrudConstants.getLocale(), k).getDescForCode(k));
            }
        });
        setValue(new HashSet(Arrays.asList(ToolkitCrudConstants.getChildAgencyId().toString())));
        addValueChangeListener((ValueChangeListener) event -> {
            Collection selection = (Collection) event.getProperty().getValue();
            if (!selection.isEmpty()) {
                agencySelectedEventEvent.fire(new AgencySelectedEvent(getParent(),new StringList(selection)));
            }
        });
        setVisible(size() > 1 );
    }
    public StringList getAgencies() {
        return new StringList((Collection) getValue());
    }

    public String[] getRoleFilter() {
        return null;
    }

    public static class AgencySelectedEvent {
        private StringList agencies;
        private Object source;

        public AgencySelectedEvent(Object source,StringList agencies) {
            this.source = source;
            this.agencies = agencies;
        }

        public StringList getAgencies() {
            return agencies;
        }

        public Object getSource() {
            return source;
        }
    }
}
