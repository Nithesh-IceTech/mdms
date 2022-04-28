package za.co.spsi.pjtk.reflect;

import lombok.SneakyThrows;
import lombok.Synchronized;
import za.co.spsi.pjtk.util.ObjectUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static za.co.spsi.pjtk.util.Util.handle;

public class Reflect<T> {
    private static Map<Class, RefFields> FIELD_MAP = new HashMap<>();
    private static Map<Class, RefMethods> METHOD_MAP = new HashMap<>();

    private RefFields fields;
    private RefMethods methods;
    private T entity;

    @Synchronized
    public static RefFields getFields(Class type) {
        if (!FIELD_MAP.containsKey(type)) {
            FIELD_MAP.put(type, new RefFields(type));
        }
        return FIELD_MAP.get(type);
    }

    public static RefFields getFields(Class type, boolean ignoreMap) {
        if (ignoreMap) {
            return new RefFields(type);
        } else {
            return getFields(type);
        }
    }


    @Synchronized
    public static RefMethods getMethods(Class type) {
        if (!METHOD_MAP.containsKey(type)) {
            METHOD_MAP.put(type, new RefMethods(type));
        }
        return METHOD_MAP.get(type);
    }

    public Reflect(T entity) {
        this.entity = entity;
        fields = getFields(entity.getClass());
        methods = getMethods(entity.getClass());
    }

    public RefFields getSet() {
        return fields.getSet(entity);
    }

    public RefFields getFields() {
        return fields;
    }

    public RefMethods getMethods() {
        return methods;
    }

    public List<String> getNames() {
        return fields.getNames();
    }

    public static <T, E> T copyFields(T dest, E source) {
        RefFields src = getFields(source.getClass());
        RefFields dst = getFields(dest.getClass());
        dst.getByNameAndTypes(src).foreach(f -> handle(() -> f.set(dest, (src.get(f.getName()).get(source)))));
        return dest;
    }

    public static <T, E> T copyNonNullFields(T dest, E source) {
        RefFields src = getFields(source.getClass());
        RefFields dst = getFields(dest.getClass());
        dst.getByNameAndTypes(src).filter(f -> f.get(source) != null)
                .foreach(f -> handle(() -> f.set(dest, (src.get(f.getName()).get(source)))));
        return dest;
    }


    @SneakyThrows
    public static <T> Map<String, Object> difference(T o1, T o2) {
        Map<String, Object> diff = new HashMap<>();
        for (Field f1 : Reflect.getFields(o1.getClass())) {
            Field f2 = Reflect.getFields(o2.getClass()).get(f1.getName());
            if (!(ObjectUtils.equals(f1.get(o1), f2.get(o2)))) {
                diff.put(f1.getName(),f1.get(o1));
            }
        }
        return diff;
    }

    public static boolean equals(Object source, Object compare) {
        return source != null && compare != null
                ? Reflect.getFields(source.getClass()).equals(source, compare)
                : source == null && compare == null;
    }


}
