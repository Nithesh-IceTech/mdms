package za.co.spsi.toolkit.entity;

import org.apache.commons.codec.binary.Base64;
import za.co.spsi.toolkit.util.ObjectUtils;
import za.co.spsi.toolkit.util.Util;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Stack;

/**
 * Created by jaspervdb on 2/3/16.
 */
public class Field<T> implements Serializable  {

    private Entity entity;
    private T value,oldValue;
    private boolean set = false, changed = false, audit = false;
    private Stack<Boolean> stateStack = new Stack<Boolean>();
    private String name = null,alternateName;
    private SimpleDateFormat dateFormat = null;

    public Field(Entity entity) {
        this.entity = entity;
        entity.add(this);
    }

    public Field setAudit(boolean audit) {
        this.audit = audit;
        return this;
    }

    public SimpleDateFormat getDateFormat() {
        return dateFormat;
    }

    public Field setDateFormat(String dateFormat) {
        this.dateFormat = new SimpleDateFormat(dateFormat);
        return this;
    }

    public boolean isAudit() {
        return audit;
    }

    /**
     * @return the field's defined type.
     * For example Field<Integer> getType() would return Integer.class
     */
    public Class<T> getType() {
        if (entity.get(this).getGenericType() instanceof ParameterizedType) {
            return (Class<T>) ((ParameterizedType) entity.get(this).getGenericType()).getActualTypeArguments()[0];
        }
        if (entity.get(this).getType().getGenericSuperclass() instanceof ParameterizedType) {
            return (Class<T>) ((ParameterizedType) entity.get(this).getType().getGenericSuperclass()).getActualTypeArguments()[0];
        }
        throw new RuntimeException(String.format("Unable to determine generic type of Field ",getName()));
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public Entity getParentEntity() {
        return entity.getParentEntity() != null?entity.getParentEntity():entity;
    }

    public String getName() {
        return name != null ? name : entity.get(this).getName();
    }

    public String getColumnName() {
        return name != null ? name : entity.get(this).getName();
    }

    /**
     * overload the default name
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getAlternateName() {
        return alternateName;
    }

    public void setAlternateName(String alternateName) {
        this.alternateName = alternateName;
    }

    public String getExportName() {
        Exportable exportable = getAnnotation(Exportable.class);
        return exportable != null ? exportable.name() : getName();
    }

    public boolean isExportable() {
        Exportable exportable = getAnnotation(Exportable.class);
        return exportable != null && exportable.dontExport() ? false : true;
    }

    public <E extends Annotation> E getAnnotation(Class<E> aClass) {
        return (E) entity.get(this).getAnnotation(aClass);
    }

    public T get() {
        return value;
    }

    public T getOldValue() {
        return oldValue;
    }

    public T getNonNull() {
        if (get() == null) {
            if (Integer.class.equals(getType())) {
                return (T) new Integer(0);
            } else if (Short.class.equals(getType())) {
                return (T) new Short((short) 0);
            } else if (Long.class.equals(getType())) {
                return (T) new Long(0);
            } else if (Double.class.equals(getType())) {
                return (T) new Double(0);
            } else if (Float.class.equals(getType())) {
                return (T) new Float(0);
            } else if (String.class.equals(getType())) {
                return (T) "";
            } else if (Boolean.class.equals(getType())) {
                return (T) new Boolean(false);
            }  else {
                throw new UnsupportedOperationException("Unmapped type " + getType());
            }
        } else {
            return get();
        }
    }


    public Entity set(T value) {
        return set(value, false);
    }

    public Entity set(T value, boolean setOldValue) {
        changed = changed || !ObjectUtils.equals(this.value, value);
        if (changed && audit && setOldValue) {
            oldValue = this.value;
        }
        this.value = value;
        set = true;
        return entity;
    }

    public void markState() {
        stateStack.add(changed);
        stateStack.add(set);
    }

    public void resetState() {
        set = stateStack.pop();
        changed = stateStack.pop();
    }


    public void clear() {
        value = null;
    }

    public boolean isSet() {
        return set;
    }

    public void setSet() {
        set = true;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    public void reset() {
        set = false;
        changed = false;
        stateStack.clear();
        oldValue = value;
    }

    public boolean equalsField(Field field) {
        return field.getName().equals(getName()) &&
                (get() != null && get().equals(field.get()) || get() == null && field.get() == null) ||
                "".equals(get()) && field.get() == null || "".equals(field.get()) && get() == null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Field && entity != null && entity.get(this) != null) {
            return equalsField((Field) obj);
        } else {
            return super.equals(obj);
        }
    }

    public boolean equalsSuper(Field field) {
        return super.equals(field);
    }

    public boolean hasAnnotation(Class<? extends Annotation> aClass) {
        return getAnnotation(aClass) != null;
    }

    public String getAsString() {
        return get() == null ? "" : get().toString();
    }

    public Integer getBoolAsNumber() {
        return get() == null ? 0 : (Boolean.parseBoolean( get().toString() ) ? 1 : 0);
    }

    public String getSerial() {
        return getSerial(value);
    }

    public String getSerial(T value) {
        if (value == null) {
            return "";
        }
        if (Date.class.isAssignableFrom(getType())) {
            return "" + ((Date) value).getTime();
        }
        if (BigDecimal.class.isAssignableFrom(getType())) {
            return ((BigDecimal) value).toPlainString();
        }
        if (byte[].class.equals(getType())) {
            return Base64.encodeBase64String((byte[]) value);
        }
        return value.toString();
    }

    public void setSerial(String value) {
        setSerial(value, false);
    }

    public void setSerial(String value, boolean setOldValue) {
        try {
            if (value == null || value.trim().isEmpty()) {
                set(null, setOldValue);
            } else if (String.class.equals(getType())) {
                set((T) value, setOldValue);
            } else if (Character.class.equals(getType())) {
                set((T) new Character(value.charAt(0)), setOldValue);
            } else if (Date.class.isAssignableFrom(getType())) {
                if (dateFormat != null) {
                    set(getType().getConstructor(long.class).newInstance(dateFormat.parse(value).getTime()), setOldValue);
                } else {
                    set(getType().getConstructor(long.class).newInstance(Long.parseLong(value)), setOldValue);
                }
            } else if (BigDecimal.class.isAssignableFrom(getType())) {
                set((T) new BigDecimal(value), setOldValue);
            } else if (byte[].class.equals(getType())) {
                // decide base 64
                set((T) Base64.decodeBase64(value),setOldValue);
            } else {
                // assume a method of valueOf
                set((T) getType().getMethod("valueOf", String.class).invoke(getType(), value),setOldValue);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return String.format("%s.%s : %s",getEntity().getName(),getName(),""+value);
    }

    /**
     * Retrieve a list of fields who are annotated with annotation and
     * the annotation method methodName will yield the specified result
     * For example persistance.getFieldsWithAnnotationWithMethodSignature(persistance,Column.class,id,true)
     * will return the field who is annotated by Column and who's id() method will return true
     *
     * @param annotation the required annotation
     * @param methodName the annotation's method
     * @param result     the required annoation method's result
     * @return boolean
     * @throws RuntimeException
     */
    public boolean isAnnotationSetWithMethodSignature(Class annotation, String methodName, Object result) {

        return Util.isAnnotationSetWithMethodSignature(getAnnotation(annotation), methodName, result);
    }

    public boolean isAnnotationSetWithMethodSignatureUseFilter(Class annotation, String methodName, za.co.spsi.toolkit.util.Util.Filter filter) {

        return Util.isAnnotationSetWithMethodSignatureUseFilter(getAnnotation(annotation), methodName, filter);
    }

}
