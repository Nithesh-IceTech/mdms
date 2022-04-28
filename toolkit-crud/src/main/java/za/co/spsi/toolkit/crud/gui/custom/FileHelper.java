package za.co.spsi.toolkit.crud.gui.custom;

import com.vaadin.ui.Notification;
import com.vaadin.ui.Upload;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import static za.co.spsi.toolkit.crud.gui.render.AbstractView.getLocaleValue;

/**
 * Created by jaspervdb on 2016/08/26.
 */
public class FileHelper implements Upload.Receiver {

    private String filename;
    private File tmpFile;

    public FileHelper showDialog(String caption, final Callback callback) {
        final Upload upload = new Upload(caption, this);

        MessageBox box = MessageBox.create().withCaption(caption).withMessage(upload).
                withCancelButton(() -> callback.uploadCancelled(), ButtonOption.caption(getLocaleValue(ToolkitLocaleId.CANCEL).toUpperCase()), ButtonOption.closeOnClick(true));

        upload.addFailedListener((Upload.FailedListener) event -> {
            callback.uploadFailed(event.getReason());
            box.close();
        });
        upload.addSucceededListener((Upload.SucceededListener) event -> {
                    callback.uploadSucceeded(event.getFilename(),tmpFile);
                    box.close();
            Notification.show(AbstractView.getLocaleValue(ToolkitLocaleId.UPLOAD_SUCCESSFUL), Notification.Type.TRAY_NOTIFICATION);
                }
        );
        box.open();
        return this;
    }

    public static FileHelper open(String caption, final Callback callback) {
        return new FileHelper().showDialog(caption, callback);
    }

    @Override
    public OutputStream receiveUpload(String filename, String mimeType) {
        this.filename = filename;
        try {
            tmpFile = File.createTempFile("import", ".dat");
            tmpFile.deleteOnExit();
            return new FileOutputStream(tmpFile);
        } catch (IOException ex) {
            Logger.getLogger(FileDialog.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }


    public static interface Callback {
        void uploadSucceeded(String filename,File file);

        void uploadFailed(Exception reason);

        void uploadCancelled();

    }

}
