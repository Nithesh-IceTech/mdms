package za.co.spsi.toolkit.reflect;

import lombok.Synchronized;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

}
