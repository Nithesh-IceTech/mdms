package za.co.spsi.toolkit.crud.gui.custom;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.tepi.imageviewer.ImageViewer;
import org.vaadin.viritin.MSize;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by jaspervdb on 2016/05/04.
 */
public class ImageGallery extends MVerticalLayout implements ImageViewer.ImageSelectionListener {

    private List<Resource> resources;
    private MVerticalLayout toolbar = new MVerticalLayout().withHeight("-1px").withWidth("-1px").withMargin(true).withSpacing(true);
    private ImageViewer imageViewer = new ImageViewer();
    private Integer selected = 0;
    private Label header = new Label();

    public ImageGallery(){
        init();
    }

    private void init() {
        imageViewer.setSizeFull();

        imageViewer.setHiLiteEnabled(true);
        header.addStyleName(ValoTheme.LABEL_H1);
        header.setSizeUndefined();

        MHorizontalLayout main = new MHorizontalLayout(imageViewer,
                new MVerticalLayout(toolbar).withStyleName("primary_panel").withFullHeight().withWidth("-1px").withAlign(toolbar, Alignment.TOP_RIGHT)
        ).withSize(MSize.FULL_SIZE).withExpand(imageViewer,2f);
        addComponent(new MHorizontalLayout(header).withFullWidth().withAlign(header,Alignment.MIDDLE_CENTER));
        addComponent(main);
        setExpandRatio(main,2f);

        imageViewer.addListener(this);
        toolbar.setMargin(true);
    }

    public Button addAction(Resource icon, final Runnable runnable) {
        Button btn = new Button(icon);
        btn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                runnable.run();
            }
        });
        toolbar.addComponents(btn);
        return btn;
    }

    public void setImages(List<Resource> resources) {
        this.resources = resources;
        imageViewer.setImages(resources);
    }

    public ImageViewer getImageViewer() {
        return imageViewer;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void removeCurrentImage() {
        if (selected != null) {
            resources.remove(selected.intValue());
            setImages(resources);
        }
    }

    public Integer getSelected() {
        return selected;
    }

    public void setImagesAsBytes(List<byte[]> resources) {
        List<Resource> resourceList = new ArrayList<>();
        for (byte[] data : resources) {
            resourceList.add(new StreamResource(new StreamResource.StreamSource() {
                @Override
                public InputStream getStream() {
                    return new ByteArrayInputStream(data);
                }
            }, UUID.randomUUID()+".jpg"));
        }
        setImages(resourceList);
        if (!resourceList.isEmpty()) {
            imageSelected(0);
        }
    }

    @Override
    public void imageSelected(ImageViewer.ImageSelectedEvent imageSelectedEvent) {
        selected = imageSelectedEvent.getSelectedImageIndex();
        imageSelected(selected);
    }

    /**
     * overload to intercept logic
     * @param selected
     */
    public void imageSelected(Integer selected) {

    }

    public void setHeaderCaption(String caption) {
        header.setValue(caption);
    }

    public Window showInTranslucentWindow(String title) {
        setSizeFull();
        Window window = new Window(title);
        window.setSizeFull();
        window.addStyleName("translucent");
        window.setContent(this);
        window.setClosable(true);
        window.setResizable(true);
        UI.getCurrent().addWindow(window);
        return window;
    }
}
