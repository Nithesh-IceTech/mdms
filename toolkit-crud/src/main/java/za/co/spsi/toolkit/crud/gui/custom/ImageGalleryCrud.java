package za.co.spsi.toolkit.crud.gui.custom;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.lookup.service.LookupServiceHelper;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.fields.ImageGalleryField;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.EntityRef;
import za.co.spsi.toolkit.entity.Entity;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.entity.FieldList;
import za.co.spsi.toolkit.util.Assert;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static za.co.spsi.toolkit.crud.gui.render.AbstractView.getLocaleValue;

/**
 * Created by jaspervdb on 2016/05/04.
 */
public class ImageGalleryCrud extends ImageGallery {

    private EntityRef entityRef;
    protected List<EntityDB> entities = new ArrayList<>();
    private Button deleteBtn;
    private Layout layout;
    private LookupServiceHelper lookupServiceHelper;


    public ImageGalleryCrud() {
        super();
        deleteBtn = addAction(FontAwesome.TRASH_O, new Runnable() {
            @Override
            public void run() {
                if (getSelected() != null) {
                    // confirm the execute
                    MessageBox.createInfo().withCaption(getLocaleValue(ToolkitLocaleId.DELETE_PHOTO))
                            .withMessage(getLocaleValue(ToolkitLocaleId.CONFIRM_DELETE))
                            .withCancelButton(ButtonOption.caption(getLocaleValue(ToolkitLocaleId.CANCEL).toUpperCase()), ButtonOption.closeOnClick(true))
                            .withOkButton(new Runnable() {
                                @Override
                                public void run() {
                                    removeCurrentImage();
                                }
                            }, ButtonOption.caption(getLocaleValue(ToolkitLocaleId.OK).toUpperCase()), ButtonOption.closeOnClick(true))
                            .open();

                }
            }
        });
    }

    public List<EntityDB> getEntities() {
        return entities;
    }

    public void setEditable(boolean editable) {
        deleteBtn.setEnabled(editable);
    }

    public Layout getLayout() {
        return layout;
    }

    public void setLayout(Layout layout) {
        this.layout = layout;
    }

    @Override
    public void removeCurrentImage() {
        int index = getSelected();
        super.removeCurrentImage();
        DataSourceDB.delete(getLayout().getDataSource(), entities.remove(index));
        setEditable(!getResources().isEmpty());
    }

    public EntityDB getSelectedEntity() {
        if (getSelected() != null && !entities.isEmpty()) {
            return entities.get(getSelected());
        }
        return null;
    }

    @Override
    public void setImages(List<Resource> resources) {
        super.setImages(resources);
        setEditable(!resources.isEmpty());
    }

    /**
     * find field with type of byte[]
     *
     * @param entityDB
     * @return
     */
    public static Field getPhotoField(EntityDB entityDB) {
        FieldList photoField = entityDB.getFields().getFieldsOfFieldType(byte[].class);
        Assert.isTrue(photoField.size() == 1, "Entity %s must have one field of type byte[]. Found %s", entityDB.getClass(), photoField.toString());
        return photoField.get(0);
    }

    /**
     * overload to implement loading logic
     */
    public void initImages(DataSource dataSource, EntityRef entityRef) {
        this.entityRef = entityRef;
        // load the images
        List<byte[]> imageData = new ArrayList<>();
        try (Connection connection = dataSource.getConnection()) {
            for (Object entity : entityRef.get(connection)) {
                entities.add((EntityDB) entity);
                if (entity instanceof ImageGalleryField.DisplayableImage) {
                    ImageGalleryField.DisplayableImage displayable = (ImageGalleryField.DisplayableImage) entity;
                    imageData.add(displayable.getPhotoField().get());
                } else {
                    imageData.add((byte[]) getPhotoField((EntityDB) entity).get());
                }
                if (imageData.get(imageData.size()-1)==null) {
                    imageData.remove(imageData.size()-1);
                    entities.remove(entities.size()-1);
                }
            }
            setImagesAsBytes(imageData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void imageSelected(Integer selected) {
        super.imageSelected(selected);
        Entity entity = entities.get(selected);
        if (entity instanceof ImageGalleryField.DisplayableImage) {
            ImageGalleryField.DisplayableImage image = (ImageGalleryField.DisplayableImage) entity;
            setHeaderCaption(image.getCaption(lookupServiceHelper));
        } else {
            setHeaderCaption(null);
        }
    }

    public void reloadImages(DataSource dataSource) {
        int index = getSelected();
        initImages(dataSource, entityRef);
        getImageViewer().setTabIndex(index);

    }

    public void setLookupServiceHelper(LookupServiceHelper lookupServiceHelper) {
        this.lookupServiceHelper = lookupServiceHelper;
    }

}
