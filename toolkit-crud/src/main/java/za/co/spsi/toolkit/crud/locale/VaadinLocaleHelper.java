package za.co.spsi.toolkit.crud.locale;

import com.vaadin.server.VaadinService;
import com.vaadin.ui.*;
import org.apache.commons.lang3.StringUtils;
import org.vaadin.addons.comboboxmultiselect.ComboBoxMultiselect;
import za.co.spsi.toolkit.crud.gui.custom.ReviewFieldWrapper;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.locale.factory.LocaleHelper;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by jaspervdb on 4/12/16.
 */
public class VaadinLocaleHelper {

    public static final Logger LOG = Logger.getLogger(VaadinLocaleHelper.class.getName());

    public static String getValueNoException(String name) {
        return LocaleHelper.getValueNoException(name, ToolkitCrudConstants.getLocale(), ToolkitCrudConstants.getContext());
    }

    public static String getValue(String name) {
        return LocaleHelper.getValue(name, ToolkitCrudConstants.getLocale(), ToolkitCrudConstants.getContext());
    }

    public static String getValueNoException(String name, String locale) {
        return LocaleHelper.getValueNoException(name, locale, ToolkitCrudConstants.getContext());
    }

    public static String getValue(String name, String locale) {
        return LocaleHelper.getValue(name, locale, ToolkitCrudConstants.getContext());
    }

    public static String getTranslatedCaption(String name) {
        String value = getValueNoException(name);
        return value != null ? value : name;
    }


    /**
     * retrieve a cookie by name
     *
     * @param name
     * @return
     */
    public static Cookie getCookie(String name) {
        if (VaadinService.getCurrentRequest() != null && VaadinService.getCurrentRequest().getCookies() != null) {
            for (Cookie cookie : VaadinService.getCurrentRequest().getCookies()) {
                if (name.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }

    /**
     * create cookie if it doesn't exist
     *
     * @param name
     * @param defaultValue
     * @return
     */
    public Cookie getOrCreateCookie(String name, String defaultValue) {
        Cookie cookie = getCookie(name);
        if (cookie == null) {
            cookie = new Cookie(name, defaultValue);
            cookie.setPath(VaadinService.getCurrentRequest().getContextPath());
            // Save cookie
            VaadinService.getCurrentResponse().addCookie(cookie);
        }
        return cookie;
    }

    private static boolean isAllUpperCase(String value) {
        for (char c : value.toCharArray()) {
            if (!Character.isUpperCase(c) && c != ' ') {
                return false;
            }
        }
        return true;
    }

    public static String getTranslation(String caption, String oldLocale, String newLocale) {
        if (!StringUtils.isEmpty(caption)) {
            String key[] = LocaleHelper.getKey(caption.toUpperCase(), oldLocale);
            if (key != null) {
                String tsValue = key.length == 2 ? (key[1] + " - " + getValue(key[0], newLocale)) : getValue(key[0], newLocale);
                return isAllUpperCase(caption) ? tsValue.toUpperCase() : tsValue;
            } else {
                LOG.info(String.format("Unable to locale translated value %s", caption));
            }
            // check if the string is subdivided by a /
            if (caption.split("/").length > 1) {
                List<String> values = new ArrayList<>();
                for (String value : caption.split("/")) {
                    String translated = getTranslation(value.replaceAll("^\\s+", "").trim(), oldLocale, newLocale);
                    if (translated != null) {
                        values.add(translated);
                    }
                }
                return StringUtils.join(values, " / ");
            }
        }
        return caption;
    }

    public static void translate(AbstractSelect abstractSelect, String oldLocale, String newLocale) {
        for (Object key : abstractSelect.getItemIds()) {
            String caption = abstractSelect.getItemCaption(key);
            String value = getTranslation(caption, oldLocale, newLocale);
            if (value != null) {
                abstractSelect.setItemCaption(key, value);
            }

            if (abstractSelect instanceof ComboBoxMultiselect) {
                ComboBoxMultiselect comboBoxMultiselect = (ComboBoxMultiselect) abstractSelect;

                if (!comboBoxMultiselect.getInputPrompt().isEmpty()) {
                    caption = getTranslation(comboBoxMultiselect.getInputPrompt(), oldLocale, newLocale);
                    if (caption != null) {
                        comboBoxMultiselect.setInputPrompt(caption);
                    }
                }

                if (!comboBoxMultiselect.getClearButtonCaption().isEmpty()) {
                    caption = getTranslation(comboBoxMultiselect.getClearButtonCaption(), oldLocale, newLocale);
                    if (caption != null) {
                        comboBoxMultiselect.setClearButtonCaption(caption);
                    }
                }
            }

            if (abstractSelect instanceof ComboBox) {
                ComboBox comboBox = (ComboBox) abstractSelect;

                if (comboBox.getInputPrompt() != null && !comboBox.getInputPrompt().isEmpty()) {
                    caption = getTranslation(comboBox.getInputPrompt(), oldLocale, newLocale);
                    if (caption != null) {
                        comboBox.setInputPrompt(caption);
                    }
                }
            }
        }
    }

    public static void translateComponent(Component cmp, String oldLocale, String newLocale) {
        if (cmp instanceof SingleComponentContainer) {
            cmp = ((SingleComponentContainer) cmp).getContent();
        }
        if (cmp instanceof Accordion) {
            Accordion accordion = (Accordion) cmp;
            for (int i = 0; i < accordion.getComponentCount(); i++) {
                String value = getTranslation(accordion.getTab(i).getCaption(), oldLocale, newLocale);
                if (value != null) {
                    accordion.getTab(i).setCaption(value);
                }
            }
        }
        if (cmp instanceof ComponentContainer || cmp instanceof SingleComponentContainer) {
            translateContainer((ComponentContainer) cmp, oldLocale, newLocale);
        }

        String caption = getTranslation(cmp.getCaption(), oldLocale, newLocale);

        if (caption != null) {
            cmp.setCaption(caption);
        }

        if (cmp instanceof Label) {
            caption = getTranslation(((Label) cmp).getValue(), oldLocale, newLocale);
            if (caption != null) {
                ((Label) cmp).setValue(caption);
            }
        }

        if (cmp instanceof Translatable) {
            ((Translatable) cmp).translate(oldLocale, newLocale);

        } else if (cmp instanceof ReviewFieldWrapper && ((ReviewFieldWrapper) cmp).getVaadinField() instanceof Translatable) {
            ((Translatable) ((ReviewFieldWrapper) cmp).getVaadinField()).translate(oldLocale, newLocale);
        } else if (cmp instanceof Table) {
            // change the columnn names
            Table table = (Table) cmp;
            for (Object key : table.getContainerPropertyIds()) {
                caption = getTranslation(table.getColumnHeader(key), oldLocale, newLocale);
                if (caption != null) {
                    table.setColumnHeader(key, caption);
                }
            }
        } else if (cmp instanceof TabSheet) {
            TabSheet tabSheet = (TabSheet) cmp;
            for (int i = 0; i < tabSheet.getComponentCount(); i++) {
                String value = getTranslation(tabSheet.getTab(i).getCaption(), oldLocale, newLocale);
                if (value != null) {
                    tabSheet.getTab(i).setCaption(value);
                }
            }
        } else if (cmp instanceof AbstractTextField) {
            AbstractTextField textField = (AbstractTextField) cmp;
            caption = textField.getInputPrompt();
            String value = getTranslation(caption, oldLocale, newLocale);
            if (value != null) {
                textField.setInputPrompt(value);
            }
        } else if (cmp instanceof AbstractSelect) {
            AbstractSelect abstractSelect = (AbstractSelect) cmp;
            translate(abstractSelect, oldLocale, newLocale);
        } else if (cmp instanceof MenuBar) {
            MenuBar menuBar = (MenuBar) cmp;
            for (MenuBar.MenuItem i : menuBar.getItems()) {
                translateMenuBar(i, oldLocale, newLocale);
            }
        }
    }

    public static void translateContainer(ComponentContainer container, String oldLocale, String newLocale) {
        container.forEach(i -> translateComponent(i, oldLocale, newLocale));
    }

    private static void translateMenuBar(MenuBar.MenuItem menuItem, String oldLocale, String newLocale) {
        String caption = menuItem.getText();
        String value = getTranslation(caption, oldLocale, newLocale);
        if (value != null) {
            menuItem.setText(value);
        }
        if (menuItem.hasChildren()) {
            for (MenuBar.MenuItem i : menuItem.getChildren()) {
                translateMenuBar(i, oldLocale, newLocale);
            }
        }
    }

    public static void translatePage(Component onScreenComponent, String oldLocale, String newLocale) {
        ComponentContainer parent = null;
        for (Component cmp = onScreenComponent; cmp != null; cmp = cmp.getParent()) {
            if (cmp instanceof ComponentContainer) {
                parent = (ComponentContainer) cmp;
            }
        }
        // now iterate over all the on screen components and translate their caption's
        if (parent != null) {
            translateContainer(parent, oldLocale, newLocale);
        }
    }
}
