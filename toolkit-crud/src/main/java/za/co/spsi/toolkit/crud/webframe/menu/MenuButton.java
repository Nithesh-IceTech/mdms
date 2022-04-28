package za.co.spsi.toolkit.crud.webframe.menu;

import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.HasComponents;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by jaspervdb on 2016/06/03.
 */
public class MenuButton extends VerticalLayout implements Button.ClickListener {

    private Button btn;

    public MenuButton(Resource icon, String caption) {
        btn = new Button(caption,icon);
        setSizeFull();
        setMargin(false);
        setSpacing(false);
        addComponent(btn);
        btn.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        btn.addClickListener(this);
        addStyleName("hline");
    }

    public Button getBtn() {
        return btn;
    }

    public void addClickListener(Button.ClickListener clickListener) {
        btn.addClickListener(clickListener);
    }

    public void selected(boolean selected) {
        if (selected) {
            addStyleName("selected");
        } else {
            removeStyleName("selected");
        }
    }

    private MenuButton removeSelected(HasComponents parent) {
        parent.forEach(component -> {
            if (component instanceof MenuFrame.MenuGroup) {
                ((MenuFrame.MenuGroup)component).selected(false);
            } else if (component instanceof MenuButton) {
                ((MenuButton)component).selected(false);
            }
        });
        return this;
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        // remove all selected from parents
        removeSelected(getParent()).removeSelected(getParent().getParent());
        selected(true);
    }
}
