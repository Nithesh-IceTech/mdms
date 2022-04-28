package za.co.spsi.toolkit.crud.gui.custom;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaspervdb on 2015/11/04.
 */
public class ToggleButtonGroup extends HorizontalLayout {

    private List<Boolean> toggleBtn = new ArrayList<>();

    public ToggleButtonGroup(Component... children) {
        for (Component c : children) {
            addComponent(c);
        }
    }

    @Override
    public void addComponent(Component c) {
        if (c instanceof Button) {
            addButton((Button)c,true);
        } else {
            super.addComponent(c);
        }
    }

    public void addButton(Button btn, boolean toggle) {
        super.addComponent(btn);
        toggleBtn.add(toggle);
        if (toggle) {
            btn.addClickListener(new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent clickEvent) {
                    setEnabled(true);
                    clickEvent.getButton().setEnabled(false);
                }
            });
        }
    }

    private List<Button> getButtons() {
        List<Button> buttons = new ArrayList<>();
        for (int i = 0; i < getComponentCount(); i++) {
            if (getComponent(i) instanceof Button) {
                buttons.add((Button) getComponent(i));
            }
        }
        return buttons;
    }


    public void setEnabled(boolean enabled) {
        int cnt = 0;
        for (Button btn : getButtons()) {
            if (toggleBtn.get(cnt++)) {
                btn.setEnabled(enabled);
            }
        }
    }

    public void setSelected(Button btn) {
        setEnabled(true);
        btn.setEnabled(false);
    }
}
