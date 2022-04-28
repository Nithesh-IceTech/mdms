package za.co.spsi.toolkit.crud.webframe.ee.menu;

/**
 * Created by jaspervdb on 2016/05/13.
 */

import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.internal.Conventions;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import org.apache.commons.collections.map.HashedMap;
import za.co.spsi.toolkit.ano.Qualifier;
import za.co.spsi.toolkit.crud.gui.ToolkitUI;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import za.co.spsi.toolkit.crud.util.AgencyHelper;
import za.co.spsi.toolkit.crud.webframe.ee.AgencyViewMenuItem;
import za.co.spsi.toolkit.crud.webframe.ee.ViewMenuItem;
import za.co.spsi.toolkit.crud.webframe.menu.MenuFrame;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Any;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import java.util.*;

/**
 * A helper to automatically create a menu from available Vaadin CDI view.
 * Listed views should be annotated with ViewMenuItem annotation to be listed
 * here, there you can also set icon, caption etc.
 * <p>
 * You'll probably want something more sophisticated in your app, but this might
 * be handy prototyping small CRUD apps.
 * <p>
 * By default the menu uses Valo themes responsive layout rules, but those can
 * easily be overridden.
 */
@Dependent
public class WiredViewMenu extends MenuFrame {

    @Inject
    BeanManager beanManager;

    private Map<String, MenuGroup> menuGroupMap = new HashedMap();

    public List<Bean<?>> getAvailableViews() {
        Set<Bean<?>> all = beanManager.getBeans(View.class,
                new AnnotationLiteral<Any>() {
                });

        final ArrayList<Bean<?>> list = new ArrayList<>();
        for (Bean<?> bean : all) {

            Class<?> beanClass = bean.getBeanClass();

            ViewMenuItem annotation = beanClass.getAnnotation(ViewMenuItem.class);

            if (annotation != null && annotation.enabled() && (ToolkitUI.mayView(beanClass.getAnnotation(Qualifier.class)))) {
                list.add(bean);
            }
        }

        Collections.sort(list, new Comparator<Bean<?>>() {

            @Override
            public int compare(Bean<?> o1, Bean<?> o2) {
                ViewMenuItem a1 = o1.getBeanClass().
                        getAnnotation(ViewMenuItem.class);
                ViewMenuItem a2 = o2.getBeanClass().
                        getAnnotation(ViewMenuItem.class);
                int order1 = a1 == null ? ViewMenuItem.DEFAULT : a1.order();
                int order2 = a2 == null ? ViewMenuItem.DEFAULT : a2.order();
                if (order1 == order2) {
                    final String name1 = a1.value();
                    final String name2 = a2.value();
                    return name1.compareTo(name2); // just compare names
                } else {
                    return order1 - order2;
                }
            }
        });
        return list;
    }

    @PostConstruct
    void init() {

//        addAttachListener(new AttachListener() {
//                              @Override
//                              public void attach(AttachEvent event) {
//                                  getUI().addStyleName("valo-menu-responsive");
//                              }
//                          }
//        );
        addMenuItems(getAvailableViews());
    }

    private MenuGroup getMenuGroup(String name) {
        if (!menuGroupMap.containsKey(name)) {
            menuGroupMap.put(name, new MenuGroup(name));
        }
        super.addMenuItem(menuGroupMap.get(name));
        return menuGroupMap.get(name);
    }

    private void addMenuItems(List<Bean<?>> availableViews) {
        Collections.sort(availableViews, new Comparator<Bean<?>>() {

            @Override
            public int compare(Bean<?> o1, Bean<?> o2) {
                return 0;
            }
        });

        for (Bean<?> viewBean : availableViews) {

            Class<?> beanClass = viewBean.getBeanClass();

            ViewMenuItem annotation = beanClass.getAnnotation(ViewMenuItem.class);
            if (annotation != null && !annotation.enabled()) {
                continue;
            }

            if (beanClass.getAnnotation(CDIView.class) != null) {
                ViewMenuItem menuItem = beanClass.getAnnotation(ViewMenuItem.class);
                Button.ClickListener clickListener = (Button.ClickListener) clickEvent -> navigateTo(beanClass);
                Boolean added = false;
                if (menuItem != null && menuItem.groupName().length() > 0) {
                    for(AgencyViewMenuItem agencyViewMenuItem:menuItem.agencyCaption()) {
                        if (AgencyHelper.inAgency(agencyViewMenuItem.forAgency())) {
                            getMenuGroup(AbstractView.getLocaleValue(menuItem.groupName())).addItem(AbstractView.getLocaleValue(agencyViewMenuItem.caption()), clickListener);
                            added=true;
                        }
                    }
                    if(!added) {
                        getMenuGroup(AbstractView.getLocaleValue(menuItem.groupName())).addItem(AbstractView.getLocaleValue(menuItem.value()), clickListener);
                    }
                } else {
                    for(AgencyViewMenuItem agencyViewMenuItem:menuItem.agencyCaption()) {
                        if (AgencyHelper.inAgency(agencyViewMenuItem.forAgency())) {
                            addMenuItem(AbstractView.getLocaleValue(agencyViewMenuItem.caption()), clickListener);
                            added=true;
                        }
                    }
                    if(!added) {
                        addMenuItem(AbstractView.getLocaleValue(menuItem.value()), clickListener);
                    }
                }
            }
        }
    }

    public void navigateTo(final Class<?> viewClass) {
        CDIView cdiview = viewClass.getAnnotation(CDIView.class);
        String viewId = cdiview.value();
        if (CDIView.USE_CONVENTIONS.equals(viewId)) {
            viewId = Conventions.deriveMappingForView(viewClass);
        }
        UI.getCurrent().getNavigator().navigateTo(viewId);
    }

    protected Resource getIconFor(Class<?> viewType) {
        ViewMenuItem annotation = viewType.getAnnotation(ViewMenuItem.class);
        if (annotation == null) {
            return FontAwesome.FILE;
        }
        return annotation.icon();
    }


}
