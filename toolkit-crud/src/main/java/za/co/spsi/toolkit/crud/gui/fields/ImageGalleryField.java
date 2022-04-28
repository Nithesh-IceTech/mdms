package za.co.spsi.toolkit.crud.gui.fields;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.hene.popupbutton.PopupButton;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.lookup.service.LookupServiceHelper;
import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.custom.FileHelper;
import za.co.spsi.toolkit.crud.gui.custom.ImageGalleryCrud;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import za.co.spsi.toolkit.crud.gui.render.VaadinNotification;
import za.co.spsi.toolkit.crud.util.Util;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.EntityRef;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.io.IOUtil;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class ImageGalleryField extends LField {

    private Class<? extends ImageGalleryCrud> galleryClass;
    private Resource toolbarIcon;
    private EntityRef entityRef;
    private Button btn = new Button(getCaption()), upload;
    private LookupServiceHelper lookupServiceHelper;
    private boolean allowUpload = true;


    public ImageGalleryField(String captionId, Resource toolbarIcon, Class<? extends ImageGalleryCrud> galleryClass, EntityRef entityRef, Layout model) {
        super(captionId, "", model);
        this.entityRef = entityRef;
        this.toolbarIcon = toolbarIcon;
        this.galleryClass = galleryClass;
    }

    public ImageGalleryField(String captionId, Class<? extends ImageGalleryCrud> galleryClass, EntityRef entityRef, Layout model) {
        this(captionId, FontAwesome.IMAGE, galleryClass, entityRef, model);
    }

    public ImageGalleryField disableImageUpload() {
        this.allowUpload = true;
        return this;
    }

    @Override
    public void applyProperties() {
        if (upload != null) {
            upload.setEnabled(getProperties().isEnabled());
        }
    }

    private Button getUpload() {
        upload = new Button(FontAwesome.UPLOAD);
        upload.setDescription(AbstractView.getLocaleValue(ToolkitLocaleId.UPLOAD));

        upload.addClickListener((Button.ClickListener) event -> {
            if (!entityRef.getEntity().isInDatabase()) {
                Util.showError(ToolkitLocaleId.UPLOAD_NOT_ALLOWED, ToolkitLocaleId.UPLOAD_PLEASE_SAVE);
                //entityRef.
                return;
            }

            FileHelper.open(getCaption(), new FileHelper.Callback() {
                @Override
                public void uploadSucceeded(String filename, File file) {

                    // Validate as image
                    try {
                        if (ImageIO.read(file) != null) {
                            // save the image
                            EntityDB photo = entityRef.getNew();
                            // set the gency
                            ImageGalleryCrud.getPhotoField(photo).set(IOUtil.readFullyHandleException(file));
                            DataSourceDB.set(getLayout().getDataSource(), photo);
                            Util.showInfo(ToolkitLocaleId.UPLOAD, ToolkitLocaleId.UPLOAD_SUCCESSFUL, null, null);
                            btn.setEnabled(true);
                        } else {
                            throw new IOException("Could not read image");
                        }
                    } catch (IOException e) {
                        Util.showError(ToolkitLocaleId.UPLOAD_ERROR, ToolkitLocaleId.UPLOAD_INCORRECT_FILE_TYPE);
                    }
                }

                @Override
                public void uploadFailed(Exception reason) {
                    Notification.show(AbstractView.getLocaleValue(ToolkitLocaleId.UPLOAD_FAILED), reason.getMessage(), Notification.Type.ERROR_MESSAGE);
                }

                @Override
                public void uploadCancelled() {
                    Notification.show(AbstractView.getLocaleValue(ToolkitLocaleId.UPLOAD_CANCELLED), Notification.Type.HUMANIZED_MESSAGE);
                }
            });
        });
        upload.setEnabled(getProperties().isEnabled());
        return upload;
    }

    @Override
    public Component buildComponent() {
        btn.setIcon(toolbarIcon);
        btn.addClickListener((Button.ClickListener) event -> {
            // init the class
            try {
                ImageGalleryCrud imageGallery = galleryClass.newInstance();
                imageGallery.setLookupServiceHelper(lookupServiceHelper);
                imageGallery.setLayout(getLayout());
                imageGallery.initImages(getLayout().getDataSource(), entityRef);
                if (!imageGallery.getResources().isEmpty()) {
                    imageGallery.showInTranslucentWindow(getCaption());
                    imageGallery.setEditable(!imageGallery.getResources().isEmpty() && !getProperties().isReadOnly());
                } else {
                    VaadinNotification.show(String.format(AbstractView.getLocaleValue(ToolkitLocaleId.NO_IMAGES), getCaption()),
                            Notification.Type.HUMANIZED_MESSAGE);
                }

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        btn.setCaption(getCaption());
        btn.setDescription(getCaption());

        if (allowUpload && getProperties().isEnabled()) {
            PopupButton popup = new PopupButton();
            popup.setIcon(FontAwesome.IMAGE);
            popup.setContent(new VerticalLayout(btn, getUpload()));
            btn.setIcon(FontAwesome.EYE);
            btn.setCaption(null);
            return popup;
        } else {
            return btn;
        }
    }

    @Override
    public void beforeOnScreenEvent() {
        if (getEntityRef().getOne(getLayout().getDataSource(), null) != null) {
            enableButton();
        } else {
            disableButton();
        }
    }


    @Override
    public void intoControl() {
    }

    @Override
    protected com.vaadin.ui.Field intoBindingsWithNoValidation(boolean update) {
        return null;
    }

    @Override
    public void intoBindings() {
    }

    public EntityRef getEntityRef() {
        return entityRef;
    }

    public void enableButton() {
        btn.setEnabled(true);
    }

    public void disableButton() {
        btn.setEnabled(false);
    }

    /**
     * optional interface that will enable features on display
     */
    public static interface DisplayableImage {

        Field<byte[]> getPhotoField();

        Field<String> getNotesField();

        String getCaption(LookupServiceHelper lookupServiceHelper);
    }

    public LookupServiceHelper getLookupServiceHelper() {
        return lookupServiceHelper;
    }

    public void setLookupServiceHelper(LookupServiceHelper lookupServiceHelper) {
        this.lookupServiceHelper = lookupServiceHelper;
    }
}

