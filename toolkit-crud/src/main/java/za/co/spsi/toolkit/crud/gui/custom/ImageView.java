package za.co.spsi.toolkit.crud.gui.custom;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Image;
import com.vaadin.ui.VerticalLayout;

/**
 * Created by jaspervdb on 2014/11/24.
 */
public class ImageView extends VerticalLayout {

    public ImageView(Resource resource) {
        Image image = new Image("",resource);
        addComponent(image);
        setComponentAlignment(image, Alignment.MIDDLE_CENTER);
//        addStyleName("login-dialog");
        setSizeFull();
    }

}
