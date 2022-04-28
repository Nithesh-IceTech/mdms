package za.co.spsi.toolkit.entity;

import org.json.JSONObject;
import za.co.spsi.toolkit.entity.ano.Audit;
import za.co.spsi.toolkit.entity.ano.Embedded;
import za.co.spsi.toolkit.reflect.RefFields;
import za.co.spsi.toolkit.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jaspervdb on 2/3/16.
 */
public class Entity {

    private Entity parentEntity;
    private FieldList fields = new FieldList();
    private String name;
    private boolean checkEmbeddedFields = false;

    public Entity(String name) {
        this.name = name;
        // init any embedded Entities
        for (java.lang.reflect.Field entityField : new RefFields(getClass()).filter(Embedded.class)) {
            try {
                Entity entity = (Entity) entityField.getType().newInstance();
                entity.setName(getName());
                entityField.set(this, entity);
                entity.initEntity(this);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * include all the entities fields in this
     *
     * @param entity
     */
    public void initEntity(Entity entity) {
        this.parentEntity = entity;
        name = entity.name;
        for (Field field : getFields()) {
            if (!entity.getFields().contains(field)) {
                entity.add(field);
            }
        }
    }

    public Entity() {
        name = getClass().getSimpleName();
    }

    public void add(Field field) {
        if (!fields.containsSuper(field)) {
            fields.add(field);
        }
    }

    public FieldList getFields() {
        if (!checkEmbeddedFields) {
            checkEmbeddedFields = true;
            for (Entity embedded : new RefFields(getClass()).filter(Embedded.class).getInstances(Entity.class, this)) {
                for (Field field : embedded.getFields()) {
                    Assert.isTrue(getFields().getByName(field.getName()) == field, "@Embedded %s instantiated in contructor of persistance %s", embedded.getClass(), getClass());
                }
            }
            // also check for audit annotation
            if (isAuditable()) {
                fields.setAudit(true);
            }
        }
        return fields;
    }

    public boolean isAuditable() {
        return getClass().isAnnotationPresent(Audit.class);
    }

    public Entity getFromSet(Object dataSource) {
        throw new UnsupportedOperationException("Not implemented");
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExportName() {
        return getClass().getAnnotation(Exportable.class) != null ? ((Exportable) getClass().getAnnotation(Exportable.class)).name() : getName();
    }

    public java.lang.reflect.Field get(Class eClass, Object ref) {
        try {
            for (java.lang.reflect.Field field : eClass.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.get(this) == ref) {
                    return field;
                }
            }
            if (!eClass.getSuperclass().equals(Object.class)) {
                return get(eClass.getSuperclass(), ref);
            }
            return null;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public java.lang.reflect.Field get(Object ref) {
        return get(getClass(), ref);
    }

    public JSONObject getAsJson() {
        JSONObject object = new JSONObject();
        for (Field field : getFields()) {

            if (field.isExportable()) {
                object.put(field.getExportName(), field.getSerial());
            }
        }
        return object;
    }

    public void initFromJson(JSONObject object) {
        for (Field field : getFields()) {
            if (object.has(field.getExportName())) {
                field.setSerial(object.getString(field.getExportName()));
            }
        }
    }

    public void initFromJson(String json) {
        initFromJson(new JSONObject(json));
    }

    public void copyStrict(Entity entity,boolean strict) {
        for (Field field : getFields()) {
            Field refField = entity.getFields().getByName(field.getName());
            Assert.isTrue(!strict || refField != null, "Unable to locate field by name %s from persistance %s", field.getName(), entity.getName());
            if (refField != null) {
                field.set(refField.get());
                field.reset();
                if (refField.isSet()) {
                    field.setSet();
                }
                field.setChanged(refField.isChanged());
            }
        }
    }

    public void copyStrict(Entity entity) {
        copyStrict(entity,true);
    }


    public Entity getParentEntity() {
        return parentEntity;
    }

    @Override
    public String toString() {
        return String.format("%s {%s}", getName(), getFields().toString());
    }

    public Entity clone() {
        try {
            Entity entity = getClass().newInstance();
            entity.copyStrict(this);
            return entity;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && getClass().isAssignableFrom(obj.getClass()) ?
                equalsEntity((Entity) obj) : super.equals(obj);
    }

    public <T extends Entity> boolean equalsEntity(Entity entity) {
        return entity != null && entity.getFields().equals(getFields());
    }

    public Map<String, Class> getAdditionalRegisteredEntities() {
        return new HashMap<>();
    }

    public static class Test extends Entity {
        public Field<String> name = new Field<String>(this);

        public Test() {
            System.out.println("fields " + name);
        }
    }

    public static void main(String args[]) throws Exception {
        new Test();
    }
}
