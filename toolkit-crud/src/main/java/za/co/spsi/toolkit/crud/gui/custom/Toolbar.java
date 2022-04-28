package za.co.spsi.toolkit.crud.gui.custom;

import com.vaadin.server.Resource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import za.co.spsi.toolkit.crud.util.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jaspervdb on 2016/07/11.
 */
public class Toolbar extends MHorizontalLayout {

    private List<Component> components = new ArrayList<>();

    public Toolbar() {
        addStyleName("panel-toolbar");
        setMargin(new MarginInfo(true,false,false,false));
        setSpacing(false);
    }

    public Toolbar(Component ... components) {
        this();
        for (Component component : components) {
            add(component);
        }
        redraw();
    }

    /**
     *
     * @param component
     */
    public void add(Component component) {
        // if the component is a container, then break it up
        add(component,components.size());
    }

    public void add(Component component,int index) {
        if (component instanceof AbstractLayout) {
            for (Button btn : Util.extractComponents(null,Button.class,component)) {
                components.add(index,formatButton(btn));
            }
        } else {
            components.add(index,formatComponent(component));
        }
        redraw();
    }

    public void addAll(Component... components) {
        for (Component component : components) {
            add(component);
        }
    }

    public void redraw() {
        removeAllComponents();
        for (Component component : components) {
            if (component.isVisible()) {
                super.add(component);
                if (component != components.get(components.size() - 1)) {
                    super.addComponent(new Separator());
                }
            }
        }
    }

    private Component formatComponent(Component c) {
        if (c instanceof Button) {
            formatButton((Button) c);
        }
        return c;
    }

    private Button formatButton(Button button) {
        button.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        button.addStyleName(ValoTheme.BUTTON_LARGE);
        if (button.getIcon() != null) {
            button.setCaption(null);
        }
        return button;
    }

    public Button addButton(Resource resource) {
        Button button = formatButton(new Button(resource));
        addComponent(button);
        return button;
    }

    public static class Separator extends VerticalLayout {
        public Separator() {
            setWidth("2px");
            setHeight("100%");
            addStyleName("seperator");
        }
    }


    public static class ToolbarButton extends Button {

        public ToolbarButton() {}

        public ToolbarButton(Resource icon) {
            super(icon);
        }

        @Override
        public void setVisible(boolean visible) {
            super.setVisible(visible);
            if (Util.getComponentParent(Toolbar.class,this) != null) {
                Util.getComponentParent(Toolbar.class, this).redraw();
            }
        }
    }
}
