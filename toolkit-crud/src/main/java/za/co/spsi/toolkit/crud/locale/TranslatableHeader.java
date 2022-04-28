package za.co.spsi.toolkit.crud.locale;

import org.vaadin.viritin.label.Header;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;

/**
 * Created by jaspervdb on 15/09/25.
 */
public class TranslatableHeader extends Header implements Translatable {

    private String captionId;

    public TranslatableHeader() {
        super("");
    }

    public TranslatableHeader setCaptionId(String captionId) {
        this.captionId = captionId;
        setText(AbstractView.getLocaleValue(captionId));
        return this;
    }

    @Override
    public TranslatableHeader setHeaderLevel(int headerLevel) {
        return (TranslatableHeader) super.setHeaderLevel(headerLevel);
    }

    @Override
    public void translate(String oldLocale, String newLocale) {
        setText(VaadinLocaleHelper.getTranslation(captionId,oldLocale,newLocale));
    }
}
