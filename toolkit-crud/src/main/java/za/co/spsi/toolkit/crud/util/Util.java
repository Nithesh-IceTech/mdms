package za.co.spsi.toolkit.crud.util;

import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.Resource;
import com.vaadin.ui.*;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.vaadin.csvalidation.CSValidator;
import za.co.spsi.toolkit.ano.Permission;
import za.co.spsi.toolkit.ano.Qualifier;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.UIProperties;
import za.co.spsi.toolkit.crud.gui.auth.RoleProvider;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import za.co.spsi.toolkit.crud.gui.render.AgencyThreadLocal;
import za.co.spsi.toolkit.crud.locale.VaadinLocaleHelper;
import za.co.spsi.toolkit.util.Call1;

import javax.enterprise.inject.spi.BeanManager;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static za.co.spsi.toolkit.ee.util.BeanUtil.getBean;

/**
 * Created with IntelliJ IDEA.
 * User: jaspervdbijl
 * Date: 2013/10/15
 * Time: 6:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class Util {

    private static Util util = new Util();

    private Util() {
    }

    public static Util getInstance() {
        return util;
    }


    /**
     * @param component
     * @param ui
     */
    public void applyUI(Component component, UIProperties ui) {
        if (ui != null) {
            if (!StringUtils.isEmpty(ui.getCaption())) {
                component.setCaption(VaadinLocaleHelper.getTranslatedCaption(ui.getCaption()));
            }
            if (!StringUtils.isEmpty(ui.getWidth())) {
                component.setWidth(ui.getWidth());
            }
            if (!StringUtils.isEmpty(ui.getHeight())) {
                component.setHeight(ui.getHeight());
            }
            if (component instanceof TextField && ui.getColumns() > 0) {
                ((TextField) component).setMaxLength(ui.getColumns());
            }
            if (ui.getWidth().length() > 0) {
                component.setWidth(ui.getWidth());
            }
            component.setEnabled(ui.isEnabled());
        } else {
            // default to 100%
            component.setWidth("100%");
        }
    }

    /**
     * apply write permissions
     *
     * @param refField
     * @param roleProvider
     * @param component
     */
    public void applyWritePermissions(Field refField, RoleProvider roleProvider, Component component) {
        Permission permission = refField.getAnnotation(Permission.class);
        if (permission != null && !permission.write()) {
            component.setEnabled(false);
        }
    }

    public String getGenericStringLengthValidatorMessage(String fieldName, Integer min, Integer max) {
        if (min != null && max != null) {
            return fieldName + " length of " + min + " to " + max + " required";
        } else {
            return (min != null ? "Min" : "Max") + " " + fieldName + " length";
        }
    }

    /**
     * @param min
     * @param max
     * @return
     */
    public String getJavaScriptForRegularExpression(String regex, String regexMsg, Integer min, Integer max, boolean forceUpper) {
        min = min == null ? -1 : min;
        max = max == null ? -1 : max;
        regex = regex != null ? regex.contains("\\d") ? regex.replace("\\d", Matcher.quoteReplacement("\\d")) : regex : null;
        regex = regex != null ? regex.endsWith("$") ? regex : regex + "$" : null;
        String regexScript =
                regex != null ?
                        "if (value.length == 0 || value." + (forceUpper ? "toUpperCase()." : "") + "match(\"" + regex + "\")) null; else \"" + regexMsg + "\";" :
                        "null;";
        return "if (" + min + " > 0 && value.length > 0 && value.length < " + min + ") \"partial\"; \n" +
                "else \n" +
                "if (" + max + " > 0 && value.length > " + max + ") \"Max field length " + max + "\"; \n" +
                "else " + (regex != null ? regexScript : " null;");
    }


    public void placeWindowMiddleTop(Window window) {
        window.setPositionY(0);
        window.setPositionX((com.vaadin.ui.UI.getCurrent().getPage().getBrowserWindowWidth() - (int) window.getWidth()) / 2);
    }


    public void placeWindowMiddleRight(Window window) {
        window.setPositionY((com.vaadin.ui.UI.getCurrent().getPage().getBrowserWindowHeight() - (int) window.getHeight()) / 2);
        window.setPositionX(com.vaadin.ui.UI.getCurrent().getPage().getBrowserWindowWidth() - (int) window.getWidth());
    }

    public void showResourceInNewWindow(String heading, Resource resource) {
        BrowserFrame browserFrame = new BrowserFrame(heading, resource);
        browserFrame.setSizeFull();
        Window windowDialog = new Window(heading, browserFrame);
        windowDialog.setWidth("1024px");
        windowDialog.setHeight("800px");
        windowDialog.center();
        com.vaadin.ui.UI.getCurrent().addWindow(windowDialog);
    }

    /**
     * @param regex             regular expression for validation
     * @param errorMessage      the error message that will be set on validation error
     * @param fieldErrorMessage the message displayed next to the field
     * @param abstractTextField
     * @param min
     * @param max
     * @param setCaps
     * @param mandatory
     */
    public void applyValidationMask(
            String fieldName,
            String regex, String errorMessage, String fieldErrorMessage, AbstractTextField abstractTextField, Integer min, Integer max,
            boolean setCaps, boolean mandatory) {
        CSValidator validator = new CSValidator();
        validator.extend(abstractTextField);
        validator.setPreventInvalidTyping(false);
        validator.setErrorMessage(errorMessage == null ? "NO ERROR MESSAGE" : errorMessage);
        validator.setJavaScript(Util.getInstance().getJavaScriptForRegularExpression(
                regex, fieldErrorMessage, min, max, setCaps));
        abstractTextField.addValidator(new RegexpValidator(regex, errorMessage));
        if (min != null || max != null) {
            abstractTextField.addValidator(
                    new StringLengthValidator(util.getGenericStringLengthValidatorMessage(fieldName, min, max), min, max, mandatory)
            );
        }
    }

    public static <T extends Layout> T getLayout(final BeanManager manager, final Class<T> type) {
        Layout layout = getBean(manager, type);
        layout.getPermission().initFrom(layout.getClass().getAnnotation(Qualifier.class));
        return (T) layout;
    }



    public static synchronized  <T> T getBeanForAgency(final BeanManager manager, final Class<T> type, String agency) {
        AgencyThreadLocal.setAgency(agency);
        try {
            return getBean(manager, type);
        } finally {
            AgencyThreadLocal.removeAgency();
        }
    }


    public static <T> T getComponentParent(Class<T> parentType, Component source) {
        if (source != null && parentType.isAssignableFrom(source.getClass())) {
            return (T) source;
        }
        if (source.getParent() != null) {
            return getComponentParent(parentType, source.getParent());
        } else {
            return null;
        }
    }

    /**
     * extract all the components of type from a container
     * @param list
     * @param type
     * @param component
     * @param <T>
     * @return
     */
    public static <T> List<T> extractComponents(List<T> list,Class<T> type,Component component) {
        list = list == null?new ArrayList<T>():list;
        if (component instanceof AbstractOrderedLayout) {
            AbstractOrderedLayout layout = (AbstractOrderedLayout) component;
            for (int i =0;i < layout.getComponentCount();i++) {
                extractComponents(list,type,layout.getComponent(i));
            }
        } else {
            if (type.isAssignableFrom(component.getClass())) {
                list.add((T) component);
            }
        }
        return list;
    }


    public static Object addMethodExecutionResult(List source, List values, String methodName) {
        try {
            Method m = null;
            for (Object value : source) {
                m = m == null ? value.getClass().getMethod(methodName, null) : m;
                values.add(m.invoke(value, null));
            }
            return values;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Window showInWindow(String caption, Component component, String width, String height) {
        Window window = new Window(caption);
        window.setWidth(width);
        window.setHeight(height);
        window.setContent(component);
        window.center();
        UI.getCurrent().addWindow(window);
        return window;
    }

    public static <T> List<Class<? extends T>> getSubTypesOf(String path,Class<T> type) {
        return new Reflections(path).getSubTypesOf(type).stream().collect(Collectors.toCollection(
                ArrayList<Class<? extends T>>::new));
    }

    public static <T> List<Class<? extends T>> getSubTypesOf(Class<T> type) {
        return getSubTypesOf("",type);
    }

    public static <T extends Annotation> Set<? extends Class> getTypesAnnotatedWith(String path,Class<T> type) {
        return new Reflections(path).getTypesAnnotatedWith(type);
    }

    public static <T> List<Class<? extends T>> getNonAbstractSubTypesOf(Class<T> type) {
        return getSubTypesOf(type).stream().filter(aClass -> !Modifier.isAbstract(aClass.getModifiers())).
                collect(Collectors.toCollection(
                        ArrayList<Class<? extends T>>::new));
    }

    public static <T> List<T> getInstances(Class<T> type,boolean filterDefault) {
        try {
            List<T> values = new ArrayList<T>();
            for (Class aClass : getNonAbstractSubTypesOf(type)) {
                if (!filterDefault || Stream.of(aClass.getConstructors()).anyMatch((c) -> c.getParameterCount() == 0)) {
                    values.add((T) aClass.newInstance());
                }
            }
            return values;
        } catch (InstantiationException | IllegalAccessException ie) {
            throw new RuntimeException(ie);
        }
    }

    public static <T> List<T> getInstances(Class<T> type) {
        return getInstances(type,false);
    }

    private static MessageBox addCancel(MessageBox box,Runnable cancel) {
        if (cancel != null) {
            box.withCancelButton(cancel,ButtonOption.closeOnClick(true));
        }
        return box;
    }

    public static void showError(String caption,String message,Runnable ok,Runnable cancel) {
        addCancel(MessageBox.createError().withCaption(AbstractView.getLocaleValue(caption)).withMessage(AbstractView.getLocaleValue(message)).
                withOkButton(ok,ButtonOption.closeOnClick(true)),cancel).open();
    }

    public static void showError(String caption,String message) {
        showError(caption,message,null,null);
    }

    public static void showInfo(String caption,String message,Runnable ok,Runnable cancel) {
        addCancel(MessageBox.createInfo().withCaption(AbstractView.getLocaleValue(caption)).withMessage(AbstractView.getLocaleValue(message)).withOkButton(ok,ButtonOption.closeOnClick(true)),cancel).open();
    }

    public static void showInput(String caption, String message, Call1<String,Void> ok, Runnable cancel) {
        TextField textField = new TextField(AbstractView.getLocaleValue(message));
        addCancel(MessageBox.createInfo().withCaption(AbstractView.getLocaleValue(caption)).withMessage(textField).
                withOkButton(() -> ok.call(textField.getValue()),ButtonOption.closeOnClick(true)),cancel).open();
    }

    public static void showDateInput(String caption, String message, Call1<Date,Void> ok, Runnable cancel) {
        DateField dateField = new DateField(AbstractView.getLocaleValue(message));
        addCancel(MessageBox.createInfo().withCaption(AbstractView.getLocaleValue(caption)).withMessage(dateField).
                withOkButton(() -> ok.call(dateField.getValue()),ButtonOption.closeOnClick(true)),cancel).open();
    }

    public static void showTextAreaInput(String caption, String message, Call1<String,Void> ok, Runnable cancel) {
        TextArea textField = new TextArea(AbstractView.getLocaleValue(message));
        textField.focus();
        MessageBox msgBox = MessageBox.createInfo().withCaption(AbstractView.getLocaleValue(caption)).withMessage(textField);
        addCancel(msgBox.withOkButton(() ->
                {
                  msgBox.close();
                  ok.call(textField.getValue());
                },
                ButtonOption.closeOnClick(false)),cancel).open();
    }


    public static void showNumberInput(String caption, String message, Call1<String,Void> ok, Runnable cancel) {
        TextField textField = new TextField(AbstractView.getLocaleValue(message));
        textField.focus();
        MessageBox msgBox = MessageBox.createInfo().withCaption(AbstractView.getLocaleValue(caption)).withMessage(textField);
        addCancel(msgBox.withOkButton(() ->
          {
              String valueString = textField.getValue();
              Double parsedValue = null;
              String error = "";
              try {
                  parsedValue = Double.parseDouble(valueString);
              } catch (NumberFormatException e) {
                  error = "Invalid number entered. Only use digits (0-9) and the decimal point (.)";
              }

              if (error.equals("")) {
                msgBox.close();
                ok.call(textField.getValue());
              }
              else {
                showError("Invalid Number", error);
              }


          },
        ButtonOption.closeOnClick(false)),cancel).open();
    }


    public static void showInfo(String caption,String message) {
        showInfo(caption,message,null,null);
    }

    public static void showQuestion(String caption,String message,Runnable ok,Runnable cancel) {
        addCancel(MessageBox.createQuestion().withCaption(AbstractView.getLocaleValue(caption)).withMessage(AbstractView.getLocaleValue(message)).
                withOkButton(ok,ButtonOption.closeOnClick(true)),cancel).open();
    }

    /**
     * split a flat list into seperate lists of size
     * @param values
     * @param size
     * @param <T>
     * @return
     */
    public static <T> List<List<T>> splitSet(Set<T> values,int size) {
        List<List<T>> list = new ArrayList<>();
        List<T> clone = new ArrayList<>();
        clone.addAll(values);
        while (!clone.isEmpty()) {
            if (list.isEmpty() || list.get(list.size()-1).size() >= size) {
                list.add(new ArrayList<>());
            }
            list.get(list.size()-1).add(clone.remove(0));
        }
        return list;
    }

    public static <T> List<List<T>> splitSet(List<T> values,int size) {
        return splitSet(new HashSet<>(values),size);
    }

}
