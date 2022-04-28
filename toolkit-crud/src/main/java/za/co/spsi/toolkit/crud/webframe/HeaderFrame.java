package za.co.spsi.toolkit.crud.webframe;

import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MMarginInfo;
import org.vaadin.viritin.layouts.MVerticalLayout;
import za.co.spsi.toolkit.crud.locale.NonTranslatableLabel;

/**
 * Created by jaspervdb on 2016/06/02.
 */
public class HeaderFrame extends MVerticalLayout {

    private MHorizontalLayout top = new MHorizontalLayout();
    private CssLayout bottom = new CssLayout();
    private MHorizontalLayout leftBtnLayout = new MHorizontalLayout(),rightBtnLayout = new MHorizontalLayout();
    private NonTranslatableLabel contextLabel = new NonTranslatableLabel();

    public HeaderFrame() {
        init();
    }

    private void init() {
        addStyleName("header");
        top.withFullHeight().withFullWidth();
        bottom.addComponents(leftBtnLayout.withStyleName("left"),rightBtnLayout.withStyleName("right"));
        with(top,bottom).withMargin(false).withSpacing(false).withExpand(bottom,2f);
        top.addStyleName("top");
        bottom.addStyleName("bottom");
        top.setHeight("90px");
        Image logo =new Image(null, new ThemeResource("webframe/header_logo.png"));
        top.with(logo).withMargin(new MMarginInfo(false,true,true,false)).with(contextLabel).
                withAlign(contextLabel, Alignment.TOP_RIGHT);
        contextLabel.addStyleName(ValoTheme.LABEL_SMALL);
        contextLabel.setWidthUndefined();
    }

    public void setContextLabel(String label) {
        contextLabel.setValue(label);
        contextLabel.setContentMode(ContentMode.HTML);
    }

    private Button format(Button btn) {
        btn.addStyleName(ValoTheme.BUTTON_BORDERLESS);
        btn.addStyleName(ValoTheme.BUTTON_LARGE);
        return btn;
    }

    public HeaderFrame addLeftAction(Button btn) {
        leftBtnLayout.add(format(btn));
        return this;
    }

    public HeaderFrame addRightAction(Component btn) {
        rightBtnLayout.add(btn instanceof Button?format((Button)btn):btn);
        return this;
    }

    public MHorizontalLayout getRightBtnLayout() {
        return rightBtnLayout;
    }
}
