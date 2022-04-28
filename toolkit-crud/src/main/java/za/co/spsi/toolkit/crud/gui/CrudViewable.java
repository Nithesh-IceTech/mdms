package za.co.spsi.toolkit.crud.gui;

import com.vaadin.navigator.View;
import com.vaadin.ui.Component;

/**
 * Created by jaspervdb on 2016/09/06.
 */
public interface CrudViewable extends View {

    public void releaseAllTx();

    public Component getRoot();

    public String getViewCaption();
}
