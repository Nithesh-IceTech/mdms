package za.co.spsi.toolkit.reflect;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by jaspervdb on 2016/06/08.
 */
public class RefMethods extends ArrayList<Method> {

    public RefMethods() {
    }


    public RefMethods(Class type) {
        for (; !Object.class.equals(type) && type != null; type = type.getSuperclass()) {
            addAllFields(type);
        }
    }

    public RefMethods filter(boolean inverse, String ... names) {
        return stream().filter(f -> !inverse == Arrays.stream(names)
                .filter(n -> f.getName().equalsIgnoreCase(n)).findFirst().isPresent())
                .collect(Collectors.toCollection(RefMethods::new));
    }

    public RefMethods filter(String ... names) {
        return filter(false,names);
    }

    public void addAllFields(Class type) {
        for (Method method : type.getMethods()) {
            method.setAccessible(true);
            add(method);
        }
    }

    public Method get(boolean ignoreCase,String name) {
        for (Method method : this) {
            if (ignoreCase?method.getName().equalsIgnoreCase(name):method.getName().equals(name)) {
                return method;
            }
        }
        return null;
    }

    public RefMethods getByNames(boolean ignoreCase, String... names) {
        RefMethods fields = new RefMethods();
        for (String name : names) {
            if (get(ignoreCase,name) != null) {
                fields.add(get(ignoreCase,name));
            }
        }
        return fields;
    }

    public RefMethods filterByType(Class type) {
        return stream().filter(m -> type.isAssignableFrom(m.getReturnType()))
                .collect(Collectors.toCollection(RefMethods::new));
    }

    public RefMethods filterParams(Class ... params) {
        return stream().filter(m ->
                Arrays.stream(m.getParameters()).map(p -> p.getType()).collect(Collectors.toList())
                        .equals(Arrays.asList(params==null?new Class[]{}:params)))
                .collect(Collectors.toCollection(RefMethods::new));
    }

    public RefMethods filterStatic(boolean inverse) {
        return stream().filter(m -> Modifier.isStatic(m.getModifiers()) != inverse).collect(Collectors.toCollection(RefMethods::new));
    }

    public RefMethods filterStatic() {
        return filterStatic(false);
    }


    public Optional<Method> getByName(String name, boolean ignoreCase) {
        RefMethods fields = getByNames(ignoreCase,name);
        return Optional.ofNullable(!fields.isEmpty()?fields.get(0):null);
    }

    public List<String> getNames() {
        List<String> names = new ArrayList<>();
        for (int i =0;i < size();i++) {
            names.add(get(i).getName());
        }
        return names;
    }

    public boolean contains(boolean ignoreCase,String name) {
        return get(ignoreCase,name) != null;
    }

    public RefMethods setAccessible(boolean accessible) {
        for (Method method : this) {
            method.setAccessible(accessible);
        }
        return this;
    }

}
