package za.co.spsi.toolkit.crud;

import de.steinwedel.messagebox.MessageBox;

/**
 * Created by jaspervdbijl on 2017/06/29.
 */
public class LayoutException extends RuntimeException {

    private boolean showMsg = true;
    private String caption;

    public LayoutException() {
    }

    public LayoutException(String message, String caption) {
        super(message);
        this.caption = caption;
    }

    public LayoutException(String message) {
        super(message);
    }

    public LayoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public LayoutException(Throwable cause) {
        super(cause);
    }

    public LayoutException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public boolean shouldShowMsg() {
        return showMsg;
    }

    public LayoutException setShowMsg(boolean showMsg) {
        this.showMsg = showMsg;
        return this;
    }

    public void showMessage() {
        if (shouldShowMsg()) {
            MessageBox.createWarning().withCaption(caption).
                        withMessage(getMessage()).
                        withOkButton(() -> {
                            try {
                                this.finalize();
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        }).open();
        }
    }
}
