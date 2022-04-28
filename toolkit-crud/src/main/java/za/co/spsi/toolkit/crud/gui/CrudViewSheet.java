package za.co.spsi.toolkit.crud.gui;

import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import org.vaadin.viritin.MSize;
import org.vaadin.viritin.layouts.MVerticalLayout;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jaspervdb on 2016/09/06.
 */
public class CrudViewSheet extends TabSheet implements CrudViewable {

    private List<CrudView> views = new ArrayList<>();

    public CrudViewSheet() {
        setSizeFull();
    }

    public CrudViewSheet(CrudView ... views) {
        this();
        for (CrudView view : views) {
            add(view);
        }
    }

    public CrudViewSheet add(CrudView  view) {
        views.add(view);
        view.setSizeFull();
        MVerticalLayout main = new MVerticalLayout(view).withMargin(true).withSize(MSize.FULL_SIZE);
        addTab(main, AbstractView.getLocaleValue(view.getViewCaption()));
        return this;
    }

    @Override
    public void releaseAllTx() {
        for (CrudView view : views) {
            view.releaseAllTx();
        }
    }

    @Override
    public Component getRoot() {
        return this;
    }

    @Override
    public String getViewCaption() {
        return null;
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        for (CrudView view : views) {
            view.enter(event);
        }
    }
}
