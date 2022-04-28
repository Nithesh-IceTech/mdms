package za.co.spsi.pjtk.reflect;

import lombok.SneakyThrows;
import za.co.spsi.pjtk.util.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static za.co.spsi.pjtk.util.Util.call;

/**
 * Created by jaspervdb on 2016/06/08.
 */
public class RefFields extends ArrayList<Field> {

    public RefFields() {
    }


    public RefFields(Class type,boolean traverseUp) {
        for (; !Object.class.equals(type); type = type.getSuperclass()) {
            addAllFields(type);
            if (!traverseUp) break;
        }
    }

    public RefFields(Class type) {
        this(type,true);
    }


    public void addAllFields(Class type) {
        for (Field field : type.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers()) && !Modifier.isFinal(field.getModifiers())) {
                field.setAccessible(true);
                add(field);
            }
        }
    }

    /**
     * only does one level
     * @return
     */
    public boolean equals(Object o1,Object o2) {
        RefFields cField = Reflect.getFields(o2.getClass());
        Optional<Field> failed = stream().filter(f -> call((v) -> cField.get(v.getName()) != null &&
                !ObjectUtils.equals(v.get(o1),cField.get(v.getName()).get(o2)),f)).findAny();
        return !failed.isPresent();
    }

    @SneakyThrows
    public void foreach(Call0Ex<Field> call) {
        for (Field field : this) {
            call.call(field);
        }
    }

    /**
     * @return all fields that have values
     */
    public RefFields getSet(Object ref) {
        return stream().filter(f -> call(field -> field.get(ref) != null, f))
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
    public RefFields copyValues(Object mySource, Object fieldSource, RefFields fields, Set<Class<?>> primitiveTypes,boolean requireFieldMatch) throws IllegalAccessException {
        for (Field field : this) {
            if (!((field.getModifiers() & Modifier.FINAL) == Modifier.FINAL) && (primitiveTypes == null || primitiveTypes.contains(field.getType()))) {
                Field refField = fields.get(field.getName());
                Assert.isTrue(refField == null || !requireFieldMatch,"Field %s not found in class % for copy"
                        ,refField != null?refField.getName():"Null ref Field",fieldSource.getClass());
                if (refField != null) {
                    Assert.isTrue(refField.getType().equals(field.getType()),
                            "Field type mismatch Field1: Name %s Type %s, Field2: Name %s Type %s", field.getName(), field.getType().toString(),
                            refField.getName(), refField.getType().toString());
                    field.set(mySource, refField.get(fieldSource));
                }
            }
        }
        return this;
    }

    public RefFields copyValues(Object mySource, Object fieldSource, RefFields fields, Set<Class<?>> primitiveTypes) throws IllegalAccessException {
        return copyValues(mySource,fieldSource,fields,primitiveTypes,false);
    }

    @SneakyThrows
    public RefFields copyValues(Object mySource, Object fieldSource, RefFields fields) {
        return copyValues(mySource, fieldSource, fields, null);
    }

    @SneakyThrows
    public RefFields copyValues(Object mySource, Object fieldSource,boolean requireFieldMatch) {
        return copyValues(mySource, fieldSource, Reflect.getFields(fieldSource.getClass()), null,requireFieldMatch);
    }

    public RefFields copyValues(Object mySource, Object fieldSource) {
        return copyValues(mySource,fieldSource,false);
    }

    public Field get(String name) {
        for (Field field : this) {
            if (field.getName().equals(name)) {
                return field;
            }
        }
        return null;
    }

    @SneakyThrows
    public Object getInstance(Object source,String name) {
        return get(name).get(source);
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

    public RefFields getByNameAndTypes(RefFields fields) {
        return stream().filter(f -> fields.get(f.getName()) != null && f.getType()
                .equals(fields.getByName(f.getName()).getType())).collect(Collectors.toCollection(RefFields::new));
    }

    public RefFields getByNames(RefMethods methods) {
        RefFields fields = new RefFields();
        for (String name : getNames()) {
            if (methods.getByName("get" + name, true) != null) {
                fields.add(get(name));
            }
        }
        return fields;
    }

    public Field getByName(String name) {
        RefFields fields = getByNames(name);
        return !fields.isEmpty() ? fields.get(0) : null;
    }

    public RefFields intersect(RefFields fields) {
        return getByNames(fields.getNames().toArray(new String[]{}));
    }

    @SneakyThrows
    public RefFields filter(Call1Ex<Field, Boolean> filter) {
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
        for (int i = 0; i < size(); i++) {
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
        return stream().map(f -> call(f1 -> {
            Annotation a = f1.getAnnotation(aType);
            String value = a != null ? aType.getMethod(methodName).invoke(a).toString() : null;
            return !StringUtils.isEmpty(value) ? value : f.getName();
        }, f)).collect(Collectors.toList());
    }

    public RefFields removeNull() {
        remove(f -> f == null);
        return this;
    }

    public RefFields remove(Predicate p) {
        this.removeIf(p);
        return this;
    }

    public RefFields removeTransient() {
        this.removeIf(f -> Modifier.isTransient(f.getModifiers()));
        return this;
    }


    public List get(Object entity) {
        return stream().map(f -> call(field -> field.get(entity), f)).collect(Collectors.toList());
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

    private RefFields getNull(Object source,boolean inverse) {
        try {
            RefFields fields = new RefFields();
            for (int i = 0; i < size(); i++) {
                if (get(i).get(source) == null|| inverse) {
                    fields.add(get(i));
                }
            }
            return fields;
        } catch (IllegalAccessException ie) {
            throw new RuntimeException(ie);
        }
    }

    public RefFields getNull(Object source) {
        return getNull(source,false);
    }

    public RefFields getNonNull(Object source) {
        return getNull(source,true);
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

    public RefFields setAccessible(boolean accessible) {
        for (Field field : this) {
            field.setAccessible(accessible);
        }
        return this;
    }

    public RefFields filter(boolean inverse,final Class<? extends Annotation> ... aClass) {
        return stream().filter(f -> inverse != Arrays.asList(aClass).stream().filter(a -> f.isAnnotationPresent(a)).findAny().isPresent())
                .collect(Collectors.toCollection(RefFields::new));
    }

    public RefFields filter(Class<? extends Annotation> aClass) {
        return filter(false,aClass);
    }

    public RefFields assertSize(int size,String msg) {
        Assert.isTrue(size() == size,msg);
        return this;
    }

    public RefFields filter(Class<? extends Annotation> aClass,String method,Object value) {
        return filter(false,aClass).filter((f -> call(c ->
                value.equals(aClass.getMethod(method).invoke(f.getAnnotation(aClass))),f)));
    }

    public Field filterOne(Class<? extends Annotation> aClass) {
        RefFields fields = filter(aClass);
        Assert.isTrue(fields.size() >= 1, "Located other then just one annotation " + aClass);
        return fields.get(0);
    }

    public RefFields filterType(Class filterClass, boolean inverse) {
        RefFields fields = new RefFields();
        for (Field field : this) {
            if (inverse != filterClass.isAssignableFrom(field.getType())) {
                fields.add(field);
            }
        }
        return fields;
    }

    public RefFields filterType(Class filterClass) {
        return filterType(filterClass, false);
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
