package za.co.spsi.toolkit.crud.gui;

import com.vaadin.server.Sizeable;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalSplitPanel;
import org.vaadin.viritin.layouts.MVerticalLayout;
import za.co.spsi.toolkit.crud.gui.query.LayoutViewGrid;
import za.co.spsi.toolkit.crud.util.Util;
import za.co.spsi.toolkit.util.Assert;

import javax.enterprise.inject.spi.BeanManager;
import javax.sql.DataSource;

/**
 * Created by jaspervdb on 4/20/16.
 */
public class GroupList extends ViewList<Group> {

    public Group getNameGroup() {
        for (Group group : this) {
            if (group.isNameGroup()) {
                return group;
            }
        }
        return null;
    }

    public Group getShortNameGroup() {
        for (Group group : this) {
            if (group.isShortNameGroup()) {
                return group;
            }
        }
        return null;
    }

    public GroupList getVisibleGroups() {
        GroupList groups = new GroupList();
        for (Group group : groups) {
            if (group.getComponent().isVisible()) {
                groups.add(group);
            }
        }
        return groups;
    }

}
