package za.co.spsi.toolkit.crud.webframe.menu;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * Created by jaspervdb on 2016/06/02.
 */
public class MenuFrame extends MHorizontalLayout {

    private MVerticalLayout menuLayout = new MVerticalLayout();
    private MVerticalLayout menuBar = new MVerticalLayout();

    private Label crestLabel;

    public MenuFrame() {
        init();
    }

    private void init() {

        addStyleName("menu");
        setWidth("230px");
        setHeight("100%");
        setMargin(false);
        setSpacing(false);

        menuLayout.addStyleName("lens");
        menuLayout.setSizeFull();
        menuLayout.setMargin(new MarginInfo(true,false,true,false));
        addComponent(menuLayout);
        setExpandRatio(menuLayout,2f);

        initMain();

    }

    public void addMenuItem(MenuButton component) {
        menuBar.add(component);
    }

    public void addMenuItem(String caption, Button.ClickListener clickListener) {
        MenuButton menuButton = new MenuButton(FontAwesome.CHEVRON_RIGHT,caption);
        if (clickListener != null) {
            menuButton.addClickListener(clickListener);
        }
        menuBar.add(menuButton);
    }

    public void addMenuItem(MenuGroup component) {
        menuBar.add(component);
    }

    public static class MenuGroup extends MVerticalLayout {
        private MVerticalLayout btnGroup = new MVerticalLayout().withMargin(false).withSpacing(false);
        private MenuButton btn;

        public MenuGroup(String caption) {

            setSizeFull();
            withMargin(false).withSpacing(false);
            btn = new MenuButton(FontAwesome.CHEVRON_RIGHT,caption);
            with(btn).with(btnGroup).withExpand(btnGroup,2f);
            btnGroup.setVisible(false);

            btn.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
                    // toggle the button group
                    btnGroup.setVisible(!btnGroup.isVisible());
                    btn.selected(btnGroup.isVisible());
                }
            });
        }

        public void selected(boolean selected) {
            btnGroup.setVisible(selected);
            btn.selected(selected);
            if (!selected) {
                btnGroup.forEach(component -> {
                    if (component instanceof MenuButton) {
                        ((MenuButton) component).selected(false);
                    }
                });
            }
        }

        public MVerticalLayout getBtnGroup() {
            return btnGroup;
        }

        public MenuButton getBtn() {
            return btn;
        }

        public MenuGroup addItem(String caption, Button.ClickListener clickListener) {
            final MenuButton btn = new MenuButton(FontAwesome.ARROW_RIGHT,caption);
            btn.setSpacing(false);
            btn.setMargin(new MarginInfo(false,false,false,true));
            if (clickListener != null) {
                btn.addClickListener(clickListener);
            }
            btnGroup.with(btn);
            return this;
        }
    }

    private void initMain() {
        Label space = new Label((String)null);
        space.setSizeFull();
        menuBar.setHeightUndefined();
        menuLayout.with(menuBar).with(space).with(new MenuLogo());
        menuLayout.setExpandRatio(space,2f);


        menuBar.setMargin(false);
        menuBar.setSpacing(false);
    }

    public static Component getHr() {
        CssLayout hr = new CssLayout();
        hr.addStyleName("hline");
        return hr;
    }

    class MenuLogo extends MVerticalLayout {
        public MenuLogo() {
            init();
        }

        private void init() {
            setMargin(false);
            setSpacing(false);
            with(getHr());
            Image logo = new Image(null,new ThemeResource("webframe/menu-logo.png"));
            crestLabel = new Label(
                    "<h1>MUNICIPAILITY OF MATOLA</h1><p>Welcome to the municipality of Matola. Onde trabalhamos divertido</p>",
                    ContentMode.HTML);
            with(logo).withAlign(logo, Alignment.TOP_CENTER).with(crestLabel).withAlign(crestLabel, Alignment.BOTTOM_CENTER);
        }
    }

    public void setCrestLabel(String crestText) {
        crestLabel.setValue(crestText);
        crestLabel.setContentMode(ContentMode.HTML);
    }
}
