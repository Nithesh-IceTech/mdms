package za.co.spsi.toolkit.crud.webframe;

import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import za.co.spsi.toolkit.crud.webframe.menu.MenuFrame;

/**
 *
 * Created by jaspervdb on 2016/06/02.
 */
public class WebFrame extends VerticalLayout {

    private MVerticalLayout main = new MVerticalLayout().withMargin(false).withFullHeight();
    private HeaderFrame header = new HeaderFrame();
    private MHorizontalLayout south = new MHorizontalLayout().withMargin(false).withSpacing(false);
    private MenuFrame menuFrame = new MenuFrame();


    public WebFrame() {
        init();
    }

    private void init() {
        addStyleName("webframe");
        south.setSizeFull();
        setSizeFull();
        addComponents(header,south);
        setExpandRatio(south,2f);

        main.addStyleName("main");
        setMargin(false);
        setSpacing(false);

        initSouth();
    }

    private void initSouth() {
        south.add(menuFrame);
        south.add(main);
        south.setExpandRatio(main,2f);
    }

    public void setMenu(MenuFrame menuFrame) {
        south.removeAllComponents();
        this.menuFrame = menuFrame;
        initSouth();
    }

    public HeaderFrame getHeader() {
        return header;
    }

    public MenuFrame getMenuFrame() {
        return menuFrame;
    }

    public MVerticalLayout getMain() {
        return main;
    }
}
