package za.co.spsi.toolkit.crud.gui;

import com.vaadin.server.VaadinService;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import za.co.spsi.toolkit.ano.AgencyUIQualifier;
import za.co.spsi.toolkit.ano.Qualifier;
import za.co.spsi.toolkit.ano.Role;
import za.co.spsi.toolkit.ano.UIField;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.util.AnoUtil;
import za.co.spsi.toolkit.util.StringList;
import za.co.spsi.uaa.util.dto.AgencyRoleMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jaspervdb on 2015/11/02.
 */
public abstract class ToolkitUI extends UI {

    private static Map<String, List<UI>> TX_SEMAPHORE_MAP = new HashMap<>();

    private String token, lastTxUid;
    private AgencyRoleMap agencyRoleMap;
    private String username;
    private StringList userRoles = new StringList();

    private Map attrMap = new HashMap<String, Object>();

    private static List<UI> getUIList(String tx) {
        if (!TX_SEMAPHORE_MAP.containsKey(tx)) {
            TX_SEMAPHORE_MAP.put(tx, new ArrayList<>());
        }
        return TX_SEMAPHORE_MAP.get(tx);
    }

    public AgencyRoleMap getAgencyRoleMap() {
        return agencyRoleMap;
    }

    public void setAgencyRoleMap(String token, AgencyRoleMap agencyRoleMap) {
        this.token = token;
        this.agencyRoleMap = agencyRoleMap;
    }

    public String getToken() {
        return token;
    }

    public String getLastTxUid() {
        return lastTxUid;
    }

    public void setLastTxUid(String lastTxUid) {
        this.lastTxUid = lastTxUid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public StringList getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(StringList userRoles) {
        this.userRoles = userRoles;
    }

    public boolean hasRole(String role) {
        return "*".equals(role) || userRoles.containsIgnoreCase(role);
    }

    public Role getRole(Qualifier qualifier) {
        for (Role role : qualifier.roles()) {
            if (hasRole(role.value())) {
                return role;
            }
        }
        return null;
    }

    public static boolean mayView(Qualifier qualifier) {
        if (qualifier != null) {
            Role role = ((ToolkitUI) getCurrent()).getRole(qualifier);
            return role != null && role.read();
        }
        return true;
    }

    public static boolean mayView(AgencyUIQualifier agencyUIQualifier) {
        UIField uiField = AnoUtil.getUIFieldAno(agencyUIQualifier, ToolkitCrudConstants.getChildAgencyId(), ToolkitCrudConstants.getParentAgencyId());
        if (agencyUIQualifier == null || uiField == null || (uiField != null && uiField.visible())) {
            return true;
        }
        return false;
    }

    public static boolean mayCreate(Qualifier qualifier) {
        if (qualifier != null) {
            Role role = ((ToolkitUI) getCurrent()).getRole(qualifier);
            return role != null && role.create();
        }
        return true;
    }

    public static boolean mayUpdate(Qualifier qualifier) {
        if (qualifier != null) {
            Role role = ((ToolkitUI) getCurrent()).getRole(qualifier);
            return role != null && role.write();
        }
        return true;
    }

    public static boolean mayDelete(Qualifier qualifier) {
        if (qualifier != null) {
            Role role = ((ToolkitUI) getCurrent()).getRole(qualifier);
            return role != null && role.delete();
        }
        return true;
    }

    public static void applyPermission(LFieldList fields, Qualifier qualifier) {
        Role role = qualifier != null ? ((ToolkitUI) getCurrent()).getRole(qualifier) : null;
        if (role != null) {
            fields.stream().forEach(f -> {
                f.getProperties().setVisible(role.read() && f.getProperties().isVisible());
                f.getProperties().setEnabled(role.write() && f.getProperties().isEnabled());
            });
        }
    }

    public static void applyPermission(Component component, Qualifier qualifier) {
        Role role = qualifier != null ? ((ToolkitUI) getCurrent()).getRole(qualifier) : null;
        if (role != null) {
            component.setVisible(role.read());
            component.setEnabled(role.write());
        }
    }

    public static ToolkitUI getToolkitUI() {
        return (ToolkitUI) getCurrent();
    }

    private static List<UI> getUIList(Layout tx) {
        return getUIList(tx.getTxUID());
    }

    public static String getLockedTxMachineName(Layout tx) {
        try {
            removeStaleTxs();
            if (!getUIList(tx).isEmpty()) {
                return String.format("USER: %S, IP: %s", ((ToolkitUI) getUIList(tx).get(0)).getUsername(),
                        getUIList(tx).get(0).getPage().getWebBrowser().getAddress());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            ;
        }
        return "none";
    }

    public static String getLockedUserName(Layout tx) {
        try {
            removeStaleTxs();
            if (!getUIList(tx).isEmpty()) {
                return ((ToolkitUI) getUIList(tx).get(0)).getUsername();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            ;
        }
        return "none";
    }

    public static boolean isExclusiveTx(Layout layout, String id) {
        List<UI> uis = TX_SEMAPHORE_MAP.get(Layout.getTxUID(layout.getMainEntity(), id));
        return uis != null && uis.contains(getCurrent());
    }

    public static boolean isTxLocked(Layout layout, String id) {
        return TX_SEMAPHORE_MAP.containsKey(Layout.getTxUID(layout.getMainEntity(), id)) &&
                !TX_SEMAPHORE_MAP.get(Layout.getTxUID(layout.getMainEntity(), id)).isEmpty();
    }

    public static boolean isTxExclusive(Layout tx) {
        // remove dead UI's
        removeStaleTxs();
        if (getUIList(tx).isEmpty() || getUIList(tx).get(0).equals(UI.getCurrent())) {
            TX_SEMAPHORE_MAP.get(tx.getTxUID()).add(UI.getCurrent());
            return true;
        }
        return false;
    }

    private static boolean isUIActive(UI ui) {
        if (ui.isClosing() || ui.getPage() == null || ui.getSession() == null || ui.getPage().getWebBrowser() == null) {
            return false;
        } else {
            long now = System.currentTimeMillis();
            // considered to be dead if three heartbeats were missed
            int timeout = 1000 * VaadinService.getCurrent().getDeploymentConfiguration().getHeartbeatInterval();
            return now - ui.getLastHeartbeatTimestamp() < Math.round(timeout * 2.5);
        }
    }

    public static void removeExclusiveTx(Layout tx) {
        if (!getUIList(tx).isEmpty()) {
            getUIList(tx).remove(0);
        }
    }

    public static void removeStaleTxs() {
        List<String> toBeRemoved = new ArrayList<>();
        for (String key : TX_SEMAPHORE_MAP.keySet()) {
            if (!getUIList(key).isEmpty() && !isUIActive(getUIList(key).get(0))) {
                // remove from map in case detach does not fire
                toBeRemoved.add(key);
            }
        }
        // has to be in a seperate collection to avoid exception
        for (String key : toBeRemoved) {
            TX_SEMAPHORE_MAP.remove(key);
        }
    }

    public static void removeExclusiveSession(UI ui) {
        List<String> cleanup = new ArrayList<>();
        for (String key : TX_SEMAPHORE_MAP.keySet()) {
            if (!TX_SEMAPHORE_MAP.get(key).isEmpty() && ui.equals(TX_SEMAPHORE_MAP.get(key).get(0))) {
                cleanup.add(key);
            }
        }
        for (String key : cleanup) {
            TX_SEMAPHORE_MAP.remove(key);
        }
    }


    @Override
    public void detach() {
        removeExclusiveSession(UI.getCurrent());
        super.detach();
    }

    public Object getAttribute(String name) {
        return attrMap.get(name);
    }

    public void setAttribute(String name, Object value) {
        attrMap.put(name, value);
    }
}
