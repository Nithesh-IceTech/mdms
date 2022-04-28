package za.co.spsi.toolkit.reflect;

import lombok.SneakyThrows;
import za.co.spsi.toolkit.util.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by jaspervdb on 2016/06/08.
 */
public class RefFields extends ArrayList<Field> {

    public RefFields() {
    }


    public RefFields(Class type,boolean addRecursive) {
        if (addRecursive) {
            for (; !Object.class.equals(type) && type != null; type = type.getSuperclass()) {
                addAllFields(type);
            }
        } else {
            addAllFields(type);
        }
    }

    public RefFields(Class type) {
        this(type,true);
    }

    public void addAllFields(Class type) {
        for (Field field : type.getDeclaredFields()) {
            field.setAccessible(true);
            add(field);
        }
    }

    @SneakyThrows
    public void foreach(Call0Ex<Field> call) {
        for (Field field : this) {
            call.call(field);
        }
    }

    /**
     *
     * @return all fields that have values
     */
    public RefFields getSet(Object ref) {
        return stream().filter(f -> Util.call(field -> field.get(ref) != null,f))
                .collect(Collectors.toCollection(RefFields::new));
    }

    /**
     * copy fields's values into my repo
     *
     * @param mySource
     * @param fieldSource
     * @param fields
     * @return
     * @throws IllegalAccessException
     */
    public RefFields copyValues(Object mySource, Object fieldSource, RefFields fields, Set<Class<?>> primitiveTypes) throws IllegalAccessException {
        for (Field field : this) {
            if (!((field.getModifiers() & Modifier.FINAL) == Modifier.FINAL) && (primitiveTypes == null || primitiveTypes.contains(field.getType()))) {
                Field refField = fields.get(field.getName());
                if (refField != null) {
                    Assert.isTrue(refField.getType().equals(field.getType()) ||
                            // test conversion to primitive
                                    refField.getType().getName().replace("java.lang.","").equalsIgnoreCase(field.getType().getName().replace("java.lang.","")) &&
                                            refField.get(fieldSource) != null
                            ,
                            "Field type mismatch Field1: Name %s Type %s, Field2: Name %s Type %s", field.getName(), field.getType().toString(),
                            refField.getName(), refField.getType().toString());
                    field.set(mySource, refField.get(fieldSource));
                }
            }
        }
        return this;
    }

    public RefFields copyValues(Object mySource, Object fieldSource, RefFields fields) throws IllegalAccessException {
        return copyValues(mySource, fieldSource, fields, null);
    }

    public Field get(String name) {
        for (Field field : this) {
            if (field.getName().equals(name)) {
                return field;
            }
        }
        return null;
    }

    public RefFields remove(String name) {
        remove(get(name));
        return this;
    }

    public RefFields getByNames(String... names) {
        RefFields fields = new RefFields();
        for (String name : names) {
            if (get(name) != null) {
                fields.add(get(name));
            }
        }
        return fields;
    }

    public RefFields getByNames(RefMethods methods) {
        RefFields fields = new RefFields();
        for (String name : getNames()) {
            if (methods.getByName("get"+name,true) != null) {
                fields.add(get(name));
            }
        }
        return fields;
    }

    public Field getByName(String name) {
        RefFields fields = getByNames(name);
        return !fields.isEmpty()?fields.get(0):null;
    }

    public RefFields intersect(RefFields fields) {
        return getByNames(fields.getNames().toArray(new String[]{}));
    }

    public RefFields filter(Call1<Field,Boolean> filter) {
        RefFields list = new RefFields();
        for (Field field : this) {
            if (filter.call(field)) {
                list.add(field);
            }
        }
        return list;
    }

    public List<String> getNames() {
        List<String> names = new ArrayList<>();
        for (int i =0;i < size();i++) {
            names.add(get(i).getName());
        }
        return names;
    }

    /**
     * return field names, optionally getting value from annotation
     *
     * @param aType
     * @param methodName
     * @return
     */
    @SneakyThrows
    public List<String> getNames(Class<? extends Annotation> aType, String methodName) {
        Assert.isTrue(aType.getMethod(methodName) != null, "Method not found: " + methodName);
        return stream().map(f -> Util.call(f1 -> {
            Annotation a = f1.getAnnotation(aType);
            String value = a != null?aType.getMethod(methodName).invoke(a).toString():null;
            return !StringUtils.isEmpty(value) ? value : f.getName();
        },f)).collect(Collectors.toList());
    }

    public RefFields removeNull() {
        for (int i = 0; i < size(); i++) {
            if (get(i) == null) {
                remove(i--);
            }
        }
        return this;
    }

    public List get(Object entity) {
        return stream().map(f -> Util.call(field -> field.get(entity),f)).collect(Collectors.toList());
    }

    public void set(Object dest, Object source) {
        try {
            for (int i = 0; i < size(); i++) {
                get(i).set(dest, get(i).get(source));
            }
        } catch (IllegalAccessException ie) {
            throw new RuntimeException(ie);
        }
    }

    public RefFields getNull(Object source) {
        try {
            RefFields fields = new RefFields();
            for (int i = 0; i < size(); i++) {
                if (get(i).get(source) == null) {
                    fields.add(get(i));
                }
            }
            return fields;
        } catch (IllegalAccessException ie) {
            throw new RuntimeException(ie);
        }
    }

    public RefFields removeNull(Object source) {
        try {
            for (int i = 0; i < size(); i++) {
                if (get(i).get(source) == null) {
                    remove(i--);
                }
            }
            return this;
        } catch (IllegalAccessException ie) {
            throw new RuntimeException(ie);
        }
    }

    public RefFields removeAll(RefFields fields) {
        removeAll(fields);
        return this;
    }

    public boolean contains(String name) {
        return get(name) != null;
    }

    public boolean filterName(Field field) {
        return get(field.getName()) != null;
    }

    public RefFields setAccessible(boolean accessible) {
        for (Field field : this) {
            field.setAccessible(accessible);
        }
        return this;
    }

    public RefFields filter(Class<? extends Annotation> aClass,boolean inverse) {
        RefFields fields = new RefFields();
        for (Field field : this) {
            if (inverse != field.isAnnotationPresent(aClass)) {
                fields.add(field);
            }
        }
        return fields;
    }

    public RefFields filter(Class<? extends Annotation> aClass) {
        return filter(aClass,false);
    }

    public Field filterOne(Class<? extends Annotation> aClass) {
        RefFields fields = filter(aClass);
        Assert.isTrue(fields.size()==1,"Located other then just one annotation " + aClass);
        return fields.get(0);
    }

    public RefFields filterType(Class filterClass,boolean inverse) {
        RefFields fields = new RefFields();
        for (Field field : this) {
            if (inverse != filterClass.isAssignableFrom(field.getType())) {
                fields.add(field);
            }
        }
        return fields;
    }

    public RefFields filterType(Class filterClass) {
        return filterType(filterClass,false);
    }

    public RefFields getNonNull(Object ref) {
        try {
            RefFields fields = new RefFields();
            for (Field field : this) {
                if (field.get(ref) != null) {
                    fields.add(field);
                }
            }
            return fields;
        } catch (IllegalAccessException ie) {
            throw new RuntimeException(ie);
        }
    }

    public <T> List<T> getInstances(Class<T> type, Object source) {
        try {
            List<T> instances = new ArrayList<>();
            for (Field field : filterType(type)) {
                instances.add((T) field.get(source));
            }
            return instances;
        } catch (IllegalAccessException ie) {
            throw new RuntimeException(ie);
        }
    }

}
