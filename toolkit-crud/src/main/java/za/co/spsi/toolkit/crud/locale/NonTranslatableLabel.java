package za.co.spsi.toolkit.crud.locale;

import com.vaadin.ui.Label;

/**
 * Created by jaspervdb on 2016/06/10.
 */
public class NonTranslatableLabel extends Label implements Translatable {

    @Override
    public void translate(String oldLocale, String newLocale) {
        // do nothing
    }
}
