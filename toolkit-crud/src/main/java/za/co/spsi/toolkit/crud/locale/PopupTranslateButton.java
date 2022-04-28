package za.co.spsi.toolkit.crud.locale;

import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import org.vaadin.hene.popupbutton.PopupButton;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;

import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

/**
 * Created by jaspervdb on 2015/08/17.
 */
@Dependent
public class PopupTranslateButton extends PopupButton implements Button.ClickListener {

    @Inject
    private TranslateProcessor processor;

    @Inject
    javax.enterprise.event.Event<TranslateProcessor.TranslationRequestEvent> translateEvent;

    public PopupTranslateButton() {
        setIcon(com.vaadin.server.FontAwesome.LANGUAGE);
        addClickListener(this);
    }

    public void init() {
        setCaption(ToolkitCrudConstants.getLocale());

        HorizontalLayout popupLayout = new HorizontalLayout();

        this.setComponent(popupLayout); // Set popup content

        final String oldLocale = getCaption();

        for(String locale: ToolkitCrudConstants.getLocales()){
            Button but = new Button(locale);
            but.setIcon(com.vaadin.server.FontAwesome.LANGUAGE);
            but.addClickListener((ClickListener) event -> {
                setCaption(locale);
                processor.translatePage(new TranslateProcessor.TranslationRequestEvent(new TranslateProcessor.TranslateEvent(oldLocale, locale)));
                // check if its ok to change tx
                translateEvent.fire(new TranslateProcessor.TranslationRequestEvent(new TranslateProcessor.TranslateEvent(oldLocale, locale)));
            });
            popupLayout.addComponent(but);
        }
    }

    @Override
    public void buttonClick(ClickEvent event) {
    }

    public void handleTranslationCompletedEvent(@Observes TranslateProcessor.TranslationCompleteEvent event) {
        setCaption(event.getEvent().getNewLocale());
    }

}
