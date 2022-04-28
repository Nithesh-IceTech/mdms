package za.co.spsi.toolkit.crud.gui.custom;

import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import de.steinwedel.messagebox.MessageBox;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: jaspervdbijl
 * Date: 2013/04/06
 * Time: 11:43 AM
 * To change this template use File | Settings | File Templates.
 * Basic confirm dialog that displays two options
 */
public class ErrorHandlerDialog extends DefaultErrorHandler {


    public static final Logger LOG = Logger.getLogger(ErrorHandlerDialog.class.getName());
    private String remoteAddress;
    private ErrorListener errorListener;

    public ErrorHandlerDialog(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public ErrorHandlerDialog(String remoteAddress, ErrorListener errorListener) {
        this(remoteAddress);
        this.errorListener = errorListener;
    }


    @Override
    public void error(com.vaadin.server.ErrorEvent event) {
        long errorTime = System.currentTimeMillis();
        String hostName;

        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            hostName = "Unknown";
        }

        LOG.log(Level.WARNING,event.getThrowable().getMessage() + " Reference number [" + errorTime + "], Remote Host [" + remoteAddress + "]", event.getThrowable());
        if (errorListener != null) {
            errorListener.error(event);
        }
        VaadinService.getCurrentRequest().getRemoteAddr();
        MessageBox.createError().withCaption("A system error has occurred")
                .withMessage(new Label(
                        "<p>Please contact your system administrator</p>" +
                        "<p>Reference [" + errorTime + "]</p>" +
                        "<p>Server [" + hostName + "]</p>" +
                        "<p><a href=\"mailto:?" +
                        "Subject=DM%20Dashboard%20System%20Error&" +
                        "body=An%20error%20has%20occurred:%0AServer%20[" + hostName + "]%0ARemote Host%20[" +
                        remoteAddress + "]%0AReference%20no%20[" + errorTime +
                        "]\" target=\"_blank\">Email</a> this to system administrator</p>", ContentMode.HTML)).open();
    }

    public static interface ErrorListener {
        void error(com.vaadin.server.ErrorEvent event);
    }

}
