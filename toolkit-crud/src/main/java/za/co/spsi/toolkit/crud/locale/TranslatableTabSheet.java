package za.co.spsi.toolkit.crud.locale;

import com.vaadin.ui.Component;
import com.vaadin.ui.TabSheet;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaspervdb on 2016/06/14.
 */
public class TranslatableTabSheet extends TabSheet implements Translatable {

    private List<String> captions = new ArrayList<>(), detailCaption = new ArrayList<>();
    private List<Tab> tabs = new ArrayList<>();

    public TranslatableTabSheet() {
        this.isResponsive();
    }

    private String getCaption(String captionId, String detailCaptionId, String oldLocale, String newLocale) {
        return String.format("%s - %s",
                AbstractView.getLocaleValue(VaadinLocaleHelper.getTranslation(captionId, oldLocale, newLocale)).toUpperCase(),
                ToolkitLocaleId.DETAILS.equals(detailCaptionId) ?
                        AbstractView.getLocaleValue(VaadinLocaleHelper.getTranslation(detailCaptionId, oldLocale, newLocale)).toUpperCase() :
                        detailCaptionId
        );
    }

    @Override
    public void removeTab(Tab tab) {
        super.removeTab(tab);
        if (tabs.contains(tab)) {
            captions.remove(tabs.indexOf(tab));
            tabs.remove(tab);
        }
    }

    public Tab addDetailTab(Component c, String captionId, String detailCaptionId) {
        Tab tab = super.addTab(c, getCaption(captionId, detailCaptionId, ToolkitCrudConstants.getLocale(), ToolkitCrudConstants.getLocale()));
        captions.add(captionId);
        detailCaption.add(detailCaptionId);
        tabs.add(tab);
        return tab;
    }

    public Tab addDetailTab(Component c, String captionId) {
        return addDetailTab(c,captionId,ToolkitLocaleId.DETAILS);
    }

    @Override
    public void translate(String oldLocale, String newLocale) {
        for (int i = 0; i < captions.size(); i++) {
            tabs.get(i).setCaption(getCaption(captions.get(i), detailCaption.get(i), oldLocale, newLocale));
        }
        // translate the rest
        for (int i = 0; i < getComponentCount(); i++) {
            if (!tabs.contains(getTab(i))) {
                getTab(i).setCaption(VaadinLocaleHelper.getTranslation(getTab(i).getCaption(), oldLocale, newLocale));
            }
        }
    }
}
