package za.co.spsi.toolkit.crud.gui.custom;

import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Window;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jaspervdb on 2016/08/25.
 */
public class FileDialog extends Window implements Upload.Receiver {

    private Upload upload;
    private Callback callback;
    private String filename = null;
    private File tmpFile = null;
    private FileOutputStream fos = null;
    private boolean completed = false;
    //private VaadinUIInterface ui;

    public FileDialog(String title, Callback callback) {
        super(title);
        this.callback = callback;
        init(title);
    }

    public final void init(String title) {
        this.setClosable(true);
        this.setModal(true);
        this.setWidth("400px");
        this.setHeight("190px");
        this.center();
        Panel pane = new Panel();
        pane.setCaption(title);
        upload = new Upload(title, this);
        upload.addFailedListener((Upload.FailedListener) event -> callback.uploadFailed(event.getReason()));
        upload.addSucceededListener((Upload.SucceededListener) event -> callback.uploadSucceeded(tmpFile));
        pane.setContent(upload);
        this.addCloseListener((CloseListener) e -> {
            if (tmpFile == null || !tmpFile.exists()) {
                callback.uploadCancelled();
            }
        });
        this.setContent(pane);
    }

    public static void open(String name, Callback callback) {
        UI.getCurrent().addWindow(new FileDialog(name, callback));
    }

    public OutputStream receiveUpload(String filename, String mimeType) {
        this.filename = filename;
        try {
            tmpFile = File.createTempFile("import", ".dat");
            tmpFile.deleteOnExit();
            this.fos = new FileOutputStream(tmpFile);
            return fos;
        } catch (IOException ex) {
            Logger.getLogger(FileDialog.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    public static interface Callback {
        void uploadSucceeded(File file);
        void uploadFailed(Exception reason);
        void uploadCancelled();

    }
}
