package za.co.spsi.pjtk.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Created by jaspervdb on 2016/06/08.
 */
public class RefMethods extends ArrayList<Method> {

    public RefMethods() {
    }

    public RefMethods(Class type) {
        for (;type != null &&  !Object.class.equals(type) ; type = type.getSuperclass()) {
            addAllFields(type);
        }
    }

    public RefMethods filter(boolean inverse, String... names) {

        RefMethods refMethods = new RefMethods();

        for (Method method : this) {
            boolean found = false;
            for (String name : names) {
                if (method.getName().equalsIgnoreCase(name)) {
                    found = true;
                    break;
                }
            }
            if (!inverse == found) {
                refMethods.add(method);
            }
        }

        return refMethods;

    }

    public RefMethods filter(String... names) {
        return filter(false, names);
    }

    public RefMethods filter(Class<? extends Annotation> aType) {
        RefMethods refMethods = new RefMethods();
        for (Method method : this) {
            if (method.getAnnotation(aType) != null) {
                refMethods.add(method);
            }
        }
        return refMethods;
    }

    public void addAllFields(Class type) {
        for (Method method : type.getMethods()) {
            if (method != null) {
                method.setAccessible(true);
                add(method);
            }
        }
    }

    public Method get(boolean ignoreCase, String name) {
        for (Method method : this) {
            if (ignoreCase ? method.getName().equalsIgnoreCase(name) : method.getName().equals(name)) {
                return method;
            }
        }
        return null;
    }

    public RefMethods getByNames(boolean ignoreCase, String... names) {
        RefMethods fields = new RefMethods();
        for (String name : names) {
            if (get(ignoreCase, name) != null) {
                fields.add(get(ignoreCase, name));
            }
        }
        return fields;
    }

    public RefMethods getByNames(String... names) {
        return getByNames(false, names);
    }

    public RefMethods filterByReturnType(Class type) {

        RefMethods refMethods = new RefMethods();
        for (Method method : this) {
            if (type.isAssignableFrom(method.getReturnType())) {
                refMethods.add(method);
            }
        }

        return refMethods;
    }

    private boolean paramEquals(List<Class> l1, List<Class> l2) {
        if (l1.size() == l2.size()) {
            for (int i = 0;i < l1.size();i++) {
                if (!l1.get(i).getSimpleName().equalsIgnoreCase(l2.get(i).getSimpleName())) {
                    return false;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public RefMethods filterStatic(boolean inverse) {

        RefMethods refMethods = new RefMethods();
        for (Method method : this) {
            if (Modifier.isStatic(method.getModifiers()) != inverse) {
                refMethods.add(method);
            }
        }
        return refMethods;
    }

    public RefMethods filterStatic() {
        return filterStatic(false);
    }

    public RefMethods filterParams(Class... params) {

        RefMethods refMethods = new RefMethods();
        for (Method method : this) {

            List list = new ArrayList();
            for (Parameter parameter : method.getParameters()) {
                list.add(parameter.getType());
            }

            if (paramEquals(list, Arrays.asList((params == null ? new Class[]{} : params)))) {
                refMethods.add(method);
            }
        }
        return refMethods;
    }

    public Optional<Method> getByName(String name, boolean ignoreCase) {
        RefMethods fields = getByNames(ignoreCase, name);
        return Optional.ofNullable(!fields.isEmpty() ? fields.get(0) : null);
    }

    public List<String> getNames() {
        List<String> names = new ArrayList<>();
        for (int i = 0; i < size(); i++) {
            names.add(get(i).getName());
        }
        return names;
    }

    public boolean contains(boolean ignoreCase, String name) {
        return get(ignoreCase, name) != null;
    }

    public RefMethods setAccessible(boolean accessible) {
        for (Method method : this) {
            method.setAccessible(accessible);
        }
        return this;
    }

}
