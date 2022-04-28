package za.co.spsi.toolkit.crud.gui;


import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.entity.FieldList;
import za.co.spsi.toolkit.util.Assert;
import za.co.spsi.toolkit.util.StringList;

import java.lang.annotation.Annotation;
import java.sql.Connection;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * Created by jaspervdb on 2/11/16.
 */
public class LFieldList extends ViewList<LField> {

    public LFieldList() {
    }

    public LFieldList(Collection<LField> fields) {
        super(fields);
    }

    public LFieldList(Iterator<LField> iterator) {
        while (iterator.hasNext()) {
            add(iterator.next());
        }
    }

    public LFieldList(LField... fields) {
        super(fields);
    }

    public static Integer test(Function<Integer, Integer> mapper, Integer test) {
        return mapper.apply(test);
    }

    public FieldList getFields() {
        return this.stream().filter(field -> field.getField() != null).map(LField::getField).collect(Collectors.toCollection(FieldList::new));
    }

    public LFieldList getVisible() {
        return this.stream().filter(field -> field.getProperties().isVisible()).collect(Collectors.toCollection(LFieldList::new));
    }

    public StringList getColNames() {
        return stream().map(field -> field.getColName()).collect(Collectors.toCollection(StringList::new));
    }

    public LField getByName(String name) {
        LFieldList fields = stream().filter(field -> field.getName().equals(name)).collect(Collectors.toCollection(LFieldList::new));
        return fields.size() > 0?fields.get(0):null;
    }

    public StringList getFullColNames() {
        return stream().map(field -> field.getFullColName()).collect(Collectors.toCollection(StringList::new));
    }

    public Field getByColumnNameStrict(String fieldName) {
        Iterator<LField> iterator = stream().filter(field -> field.getField().getName().equalsIgnoreCase(fieldName)).iterator();
        Assert.isTrue(iterator.hasNext(), "Unable to find column field in layout by name %s", fieldName);
        return iterator.next().getField();
    }

    public FieldList getByColumnNameStrict(List<String> fieldNames) {
        return fieldNames.stream().map(fieldName -> getByColumnNameStrict(fieldName)).collect(Collectors.toCollection(FieldList::new));
    }

    public LFieldList getFieldsOfType(Class aClass, boolean inverse) {
        return stream().filter(field -> aClass.isAssignableFrom(field.getClass()) != inverse).collect(Collectors.toCollection(LFieldList::new));
    }

    public LFieldList getFieldsOfType(Class aClass) {
        return getFieldsOfType(aClass, false);
    }

    public LFieldList getFieldsOfColumnType(Class aClass, boolean inverse) {
        return stream().filter(field -> field.getFieldType() != null && aClass.isAssignableFrom(field.getFieldType()) != inverse).collect(Collectors.toCollection(LFieldList::new));
    }

    public LFieldList getFieldsOfColumnType(Class aClass) {
        return getFieldsOfColumnType(aClass, false);
    }


    @Override
    public void saveEvent(Connection connection) {
        stream().forEach(lField -> lField.saveEvent(connection));
    }

    @Override
    public void beforeOnScreenEvent() {
        stream().forEach(lField -> lField.beforeOnScreenEvent());
    }

    public void apply(Action<LField> function) {
        stream().forEach(field -> function.apply(field));
    }

    public void refreshAuditEvent() {
        apply((field)->field.refreshAuditEvent());
    }

    public LFieldList intoBindingsWithNoValidation() {
        stream().forEach(lField -> lField.intoBindingsWithNoValidation());
        return this;
    }


    @FunctionalInterface
    public static interface Action<T> {
        void apply(T t);
    }

    public boolean isChanged() {
        intoBindingsWithNoValidation();
        return getFields().isChanged();
    }

    public LFieldList getFieldsWithAnnotation(Class annotation) {
        return (LFieldList) stream().filter(field -> field.getMyField() != null && field.getMyField().isAnnotationPresent(annotation)).collect(Collectors.toCollection(LFieldList::new));
    }

    /**
     * Retrieve a list of fields who are annotated with annotation and
     * the annotation method methodName will yield the specified result
     * For example entity.getFieldsWithAnnotationWithMethodSignature(entity,Column.class,id,true)
     * will return the field who is annotated by Column and who's id() method will return true
     *
     * @param annotation the required annotation
     * @param methodName the annotation's method
     * @param result     the required annoation method's result
     * @return List of fields who matches the anotation and method signature
     * @throws
     */
    public LFieldList getFieldsWithAnnotationWithMethodSignature(Class annotation, String methodName, Object result) {
        try {
            LFieldList list = new LFieldList();
            for (LField field : this) {
                if (field.getMyField() != null &&
                        za.co.spsi.toolkit.util.Util.isAnnotationSetWithMethodSignature(field.getMyField().getAnnotation(annotation),methodName,result)) {
                    list.add(field);
                }
            }
            return list;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }


}
