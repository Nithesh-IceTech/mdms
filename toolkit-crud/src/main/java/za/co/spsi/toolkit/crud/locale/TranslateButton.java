package za.co.spsi.toolkit.crud.locale;

import com.vaadin.ui.Button;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

/**
 * Created by jaspervdb on 2015/08/17.
 */
@Dependent
public class TranslateButton extends Button implements Button.ClickListener {

    @Inject
    private TranslateProcessor processor;

    @Inject
    javax.enterprise.event.Event<TranslateProcessor.TranslationRequestEvent> translateEvent;

    public TranslateButton() {
        setIcon(com.vaadin.server.FontAwesome.LANGUAGE);
        addClickListener(this);
    }

    @PostConstruct
    private void init() {
        setCaption(ToolkitCrudConstants.getLocale());
    }

    @Override
    public void buttonClick(ClickEvent event) {
        final String oldLocale = getCaption();

        final String newLocale = ToolkitCrudConstants.getLocales().get((ToolkitCrudConstants.getLocales().indexOf(getCaption()) + 1) %
                ToolkitCrudConstants.getLocales().size());
        setCaption(newLocale);

        processor.translatePage(new TranslateProcessor.TranslationRequestEvent(new TranslateProcessor.TranslateEvent(oldLocale, newLocale)));
        // check if its ok to change tx
        translateEvent.fire(new TranslateProcessor.TranslationRequestEvent(new TranslateProcessor.TranslateEvent(oldLocale, newLocale)));

    }

    public void handleTranslationCompletedEvent(@Observes TranslateProcessor.TranslationCompleteEvent event) {
        setCaption(event.getEvent().getNewLocale());
    }

}
