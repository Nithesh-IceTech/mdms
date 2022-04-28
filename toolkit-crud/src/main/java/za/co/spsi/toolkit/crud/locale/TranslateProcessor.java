package za.co.spsi.toolkit.crud.locale;

import com.vaadin.ui.UI;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Locale;

/**
 * Created by jaspervdb on 2015/08/17.
 */
@Dependent
public class TranslateProcessor implements Serializable {


    @Inject
    Event<TranslationCompleteEvent> translateEvent;

    public void translatePage(@Observes TranslationRequestEvent event) {

        ToolkitCrudConstants.setLocale(event.getEvent().getNewLocale());
        ToolkitCrudConstants.persistLocale(event.getEvent().getNewLocale());
        Locale l = ToolkitCrudConstants.getAsLocale();
        UI.getCurrent().setLocale(l);
        UI.getCurrent().getSession().setLocale(l);
        VaadinLocaleHelper.translatePage(UI.getCurrent().getContent(), event.getEvent().getOldLocale(), event.getEvent().getNewLocale());
        //send translation completed event
        translateEvent.fire(new TranslationCompleteEvent(event.getEvent()));
    }

    public static class TranslateEvent {
        private boolean reloadPage = false;
        private String oldLocale,newLocale;
        private Object guiSource;

        public TranslateEvent(String oldLocale,String newLocale,Object guiSource) {
            this.oldLocale = oldLocale;
            this.newLocale = newLocale;
            this.guiSource = guiSource;
        }

        public TranslateEvent(String oldLocale,String newLocale) {
            this(oldLocale,newLocale,null);
        }

        public boolean isReloadPage() {
            return reloadPage;
        }

        public String getOldLocale() {
            return oldLocale;
        }

        public String getNewLocale() {
            return newLocale;
        }

        public Object getGuiSource() {
            return guiSource;
        }
    }

    public static class TranslationRequestEvent {
        private TranslateEvent event;

        public TranslationRequestEvent(TranslateEvent event) {
            this.event = event;
        }

        public TranslateEvent getEvent() {
            return event;
        }
    }

    public static class TranslationCompleteEvent {
        private TranslateEvent event;

        public TranslationCompleteEvent(TranslateEvent event) {
            this.event = event;
        }

        public TranslateEvent getEvent() {
            return event;
        }
    }

}
