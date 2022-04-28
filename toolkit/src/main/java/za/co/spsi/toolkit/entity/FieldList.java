package za.co.spsi.toolkit.entity;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import za.co.spsi.toolkit.entity.ano.Audit;
import za.co.spsi.toolkit.util.ObjectUtils;
import za.co.spsi.toolkit.util.StringList;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by jaspervdb on 2/3/16.
 */
@Slf4j
public class FieldList extends ArrayList<Field> {

    public static final Logger TAG = Logger.getLogger(FieldList.class.getName());

    public FieldList() {
    }

    public FieldList(Field... fields) {
        add(fields);
    }

    public boolean containsSuper(Field field) {
        for (Field f : this) {
            if (field.equalsSuper(f)) {
                return true;
            }
        }
        return false;
    }

    public FieldList(Collection<Field> collection) {
        addAll(collection);
    }

    public void add(Field fields[]) {
        Collections.addAll(this, fields);
    }

    public FieldList(Iterator<Field> iterator) {
        while (iterator.hasNext()) {
            add(iterator.next());
        }
    }

    public <T extends Annotation> Map<Field, T> getFieldsWithAnnotation(Class<T> aClass) {
        List<T> fields = new ArrayList<T>();
        Map<Field, T> map = new LinkedHashMap<Field, T>();
        for (Field field : this) {
            if (field.getAnnotation(aClass) != null) {
                map.put(field, (T) field.getAnnotation(aClass));
            }
        }
        return map;
    }

    private FieldList filter(Filter filter) {
        FieldList fields = new FieldList();
        for (Field field : this) {
            if (filter.filter(field)) {
                fields.add(field);
            }
        }
        return fields;
    }

    interface Filter {
        boolean filter(Field field);
    }

    public FieldList getSet() {
        return filter(new Filter() {
            public boolean filter(Field field) {
                return field.isSet();
            }
        });
    }

    public FieldList setSet() {
        for (Field field : this) {
            field.setSet();
        }
        return this;
    }


    public FieldList getChanged() {
        return filter(new Filter() {
            public boolean filter(Field field) {
                return field.isChanged();
            }
        });

    }


    public FieldList setAudit(boolean audit) {
        for (Field field : this) {
            field.setAudit(audit);
        }
        return this;
    }

    public void setAudit() {
        FieldList fields = copy();
        fields.removeAll(getFieldsWithAnnotationWithMethodSignature(Audit.class, "export", true));
        fields.setAudit(true);
    }

    public FieldList getAudit() {
        return filter(new Filter() {
            public boolean filter(Field field) {
                return field.isAudit();
            }
        });
    }

    public void reset() {
        for (Field field : this) {
            field.reset();
        }
    }

    public void clearFields() {
        for (Field field : this) {
            field.clear();
        }
    }

    public boolean isSet() {
        for (Field field : this) {
            if (field.isSet()) {
                return true;
            }
        }
        return false;
    }

    public boolean isChanged() {
        for (Field field : this) {
            if (field.isChanged()) {
                return true;
            }
        }
        return false;
    }

    public FieldList copy() {
        FieldList fields = new FieldList();
        for (Field field : this) {
            fields.add(field);
        }
        return fields;
    }

    public void copy(FieldList fields) {
        for (Field field : this) {
            if (fields.getByName(field.getName()) != null) {
                field.set(fields.getByName(field.getName()).get());
            }
        }
    }

    public StringList getNames() {
        StringList strings = new StringList();
        for (Field field : this) {
            strings.add(field.getName());
        }
        return strings;
    }

    public StringList getExportNames() {
        StringList strings = new StringList();
        for (Field field : this) {
            strings.add(field.getExportName());
        }
        return strings;
    }

    public Field getByName(String name) {
        return getByName(name,false);
    }

    public Field getByName(String name,boolean ignoreCase) {
        for (Field field : this) {
            if (ignoreCase?field.getName().equalsIgnoreCase(name):field.getName().equals(name)) {
                return field;
            }
        }
        return null;
    }

    public FieldList filterByName(FieldList fields) {
        FieldList filtered = new FieldList();
        for (Field field : this) {
            if (fields.getByName(field.getName()) != null) {
                filtered.add(field);
            }
        }
        return filtered;
    }

    public boolean equalsFields(FieldList fields) {
        return fields.size() == size() && getNotEquals(fields).isEmpty();
    }

    public FieldList getNotEquals(FieldList fields) {
        FieldList notEquals = new FieldList();
        if (fields.size() == size()) {
            for (int i = 0; i < size(); i++) {
                if (!fields.get(i).equalsField(get(i))) {
                    notEquals.add(get(i));
                }
            }
        }
        return notEquals;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof FieldList) {
            return equalsFields((FieldList) o);
        } else {
            return super.equals(o);
        }
    }

    /**
     * Find all the fields who are annotated the annotation
     *
     * @param annotation the required annotation
     * @return A list of all the fields who are annotated by annotation
     */
    public FieldList getAnnotation(Class annotation) {
        FieldList list = new FieldList();
        for (Field field : this) {
            if (field.getAnnotation(annotation) != null) {
                list.add(field);
            }
        }
        return list;
    }

    public FieldList getFieldsOfFieldType(Class type) {
        FieldList fields = new FieldList();
        for (Field field : this) {
            if (type.equals(field.getType())) {
                fields.add(field);
            }
        }
        return fields;
    }

    public FieldList getFieldsOfInstance(Class type) {
        FieldList fields = new FieldList();
        for (Field field : this) {
            if (type.isAssignableFrom(field.getClass())) {
                fields.add(field);
            }
        }
        return fields;
    }

    /**
     * Retrieve a list of fields who are annotated with annotation and
     * the annotation method methodName will yield the specified result
     * For example persistance.getFieldsWithAnnotationWithMethodSignature(persistance,Column.class,id,true)
     * will return the field who is annotated by Column and who's id() method will return true
     *
     * @param annotation the required annotation
     * @param methodName the annotation's method
     * @return List of fields who matches the anotation and method signature
     * @throws
     */
    public FieldList getFieldsWithAnnotationWithMethodSignatureWithFilter(Class annotation, String methodName, za.co.spsi.toolkit.util.Util.Filter filter) {
        try {
            FieldList list = new FieldList();
            for (Field field : this) {
                if (field.isAnnotationSetWithMethodSignatureUseFilter(annotation, methodName, filter)) {
                    list.add(field);
                }
            }
            return list;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public FieldList getFieldsWithAnnotationWithMethodSignature(Class annotation, String methodName, final Object result) {
        return getFieldsWithAnnotationWithMethodSignatureWithFilter(annotation,methodName,new za.co.spsi.toolkit.util.Util.Filter() {
            @Override
            public boolean filter(Object value) {
                return ObjectUtils.equals(value, result);
            }
        });
    }

    public void initFromJson(JSONObject jsonObject) {
        for (Field field : this) {
            if (!jsonObject.has(field.getName())) {
                log.warn(String.format("Field %s not found in jsonObject", field.getName()));
            } else {
                Object value = jsonObject.get(field.getName());
                field.setSerial(value != null ? value.toString() : "", true);
            }
        }
    }

    public String getNameValueDesc() {
        StringList strings = new StringList();
        for (Field field : this) {
            strings.add(String.format("%s:%s", field.getName(), field.getAsString()));
        }
        return strings.toString(", ");
    }

    @Override
    public String toString() {
        return getNameValueDesc();
    }

}
