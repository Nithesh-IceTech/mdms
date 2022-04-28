package za.co.spsi.toolkit.crud.util;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import lombok.AllArgsConstructor;
import za.co.spsi.toolkit.crud.gui.render.VaadinNotification;
import za.co.spsi.toolkit.util.Call0;

import java.io.File;
import java.io.InputStream;

/**
 * Created by jaspervdb on 2016/08/19.
 */
public class DownloaderHelper {

    public static Downloader extend(Button btn, String filename, final Callback callback,final Call0<Boolean> streamListener) {
        return new Downloader(filename,callback,streamListener).init(btn);
    }

    public static Downloader extend(Button btn, String filename, final Callback callback) {
        return extend(btn, filename,callback,null);
    }

    public static class Downloader {
        private String filename;
        private Callback callback;
        private Call0<Boolean> streamListener;

        public Downloader(String filename, Callback callback,Call0<Boolean> streamListener) {
            this.filename = filename;
            this.callback = callback;
            this.streamListener= streamListener;
        }

        public Downloader init(Button btn) {
            btn.addClickListener((Button.ClickListener) event -> {
                try {
                    try {
                        Page.getCurrent().open(new FileResourceWithName(callback.getFile(),filename), null, false);
                    } finally {
                        if (streamListener != null) {
                            streamListener.call(true);
                        }
                    }
                } catch (Exception ex) {
                    VaadinNotification.show(ex.getMessage(), Notification.Type.ERROR_MESSAGE);
                }
            });
            return this;
        }
    }

    public interface Callback {
        File getFile() throws Exception;
    }

    public static class FileResourceWithName extends FileResource {
        private String filename;

        /**
         * Creates a new file resource for providing given file for client
         * terminals.
         *
         * @param sourceFile the file that should be served.
         */
        public FileResourceWithName(File sourceFile,String filename) {
            super(sourceFile);
            this.filename = filename;
        }

        @Override
        public String getFilename() {
            return filename;
        }
    }
}
