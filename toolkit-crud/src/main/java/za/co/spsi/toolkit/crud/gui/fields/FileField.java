package za.co.spsi.toolkit.crud.gui.fields;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.custom.FileHelper;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.io.IOUtil;
import za.co.spsi.toolkit.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: jaspervdb
 * Date: 2013/11/01
 * Time: 4:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileField extends LField<byte[]> {

    private Field fileNameField;
    private LField fileNameLField;
    private Button upload = new Button(AbstractView.getLocaleValue(ToolkitLocaleId.UPLOAD), FontAwesome.UPLOAD),
            open = new Button(AbstractView.getLocaleValue(ToolkitLocaleId.OPEN), FontAwesome.FOLDER_OPEN);
    private TextField filename = new TextField();
    private File file;
    private FileHelper.Callback callback;

    public FileField(Field<byte[]> field, Field<String> filenameField, String captionId, Layout model) {
        super(field, captionId, model);
        this.fileNameField = filenameField;
        init();
    }

    public FileField(Field<byte[]> field, LField<String> fileNameLField, String captionId, Layout model) {
        super(field, captionId, model);
        this.fileNameLField = fileNameLField;
        this.fileNameField = fileNameLField.getField();
        init();
    }

    public FileField setCallback(FileHelper.Callback callback) {
        this.callback = callback;
        return this;
    }

    private void init() {
        upload.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                FileHelper.open(getCaption(), new FileHelper.Callback() {
                    @Override
                    public void uploadSucceeded(String filename, File file) {
                        FileField.this.filename.setValue(filename);
                        fileNameField.set(filename);
                        if (fileNameLField != null) {
                            fileNameLField.set(filename);
                            fileNameLField.intoControl();
                        }
                        FileField.this.file = file;
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
            }
        });
    }

    public File getFile() {
        return file;
    }

    public File getOrCreateFile() throws IOException {
        if (file == null && get() != null && fileNameField.get() != null) {
            file = File.createTempFile(fileNameField.getSerial(), "");
            file.deleteOnExit();
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(get());
            }
        }
        return file;
    }

    @Override
    public void beforeOnScreenEvent() {
        super.beforeOnScreenEvent();
        new FileDownloader(new StreamResource((StreamResource.StreamSource) () -> new ByteArrayInputStream(get()), fileNameField.getAsString())).
                extend(open);
    }

    @Override
    public void applyProperties() {
        filename.setEnabled(false);
        open.setEnabled(!StringUtils.isEmpty(fileNameField.getAsString()));
        upload.setEnabled(getProperties().isEnabled());

    }

    @Override
    public void intoControl() {
        this.filename.setValue(fileNameField.getAsString());
        applyProperties();
    }

    @Override
    public void intoBindings() {
        // set the file data
        if (file != null) {
            getField().set(IOUtil.readFullyHandleException(file));
            fileNameField.set(filename.getValue());
        }
        super.intoBindings();
    }

    @Override
    public Component buildComponent() {
        MHorizontalLayout root = new MHorizontalLayout(filename, upload, open).withWidth("-1px").withSpacing(true);
        root.setCaption(getCaption());
        return root;
    }


}
