package za.co.spsi.toolkit.crud.gui.custom;

import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.util.DownloaderHelper;

public class DownloadField extends ActionField implements ActionField.Callback {

    private DownloaderHelper.Callback callback;
    private String filename;

    public DownloadField(String captionId, Resource toolbarIcon, Layout model, String filename, DownloaderHelper.Callback callback) {
        super(captionId, toolbarIcon,model,null);
        setCallback(this);
        this.filename = filename;
        this.callback = callback;
    }

    @Override
    public Component buildComponent() {
        Button btn = (Button) super.buildComponent();
        DownloaderHelper.extend(btn, filename, callback);
        return btn;
    }

    @Override
    public void callback(ActionField source) {

    }

}

