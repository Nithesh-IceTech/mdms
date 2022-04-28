package za.co.spsi.toolkit.crud.gui;

import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.entity.Entity;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.util.StringUtils;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by jaspervdb on 4/12/16.
 */
public class LFields extends ArrayList<LField> {

    private Entity entity;
    private Layout layout;

    public LFields(Entity entity, Layout layout) {
        this.entity = entity;
        this.layout = layout;
    }


    public LFields addAll() {
        return addAllExcluding(null);
    }

    public LFields addAllVisual() {
        return addAllExcluding(Id.class, ForeignKey.class);
    }

    public LFields addAllExcluding(Class<? extends Annotation>... aTypes) {
        for (Field field : entity.getFields()) {
            if (aTypes == null || Arrays.stream(aTypes).noneMatch(t -> field.getAnnotation(t) != null)) {
                LField lf = new LField(field, StringUtils.addSpaceToCase(field.getName()), layout);
                add(lf);
            }
        }
        return this;
    }

    public LField getField(Field field) {
        for (LField f : this) {
            if (f.getField() == field) {
                return f;
            }
        }
        throw new RuntimeException("Could not locate LField for field " + field.getName());
    }

    public LFields removeFields(Field... fields) {
        Group group = layout.getGroups().isEmpty() ? null : layout.getGroups().get(layout.getGroups().size() - 1);
        for (Field field : fields) {
            if (group != null) {
                group.getFields().remove(getField(field));
            }
            layout.getFields().remove(getField(field));
            remove(getField(field));
        }
        return this;
    }

    public LFields remove(Class<? extends Annotation> aType) {
        for (Field field : entity.getFields()) {
            if (field.getAnnotation(aType) != null) {
                removeFields(field);
            }
        }
        return this;
    }

    public LFields removeId() {
        return remove(Id.class);
    }

    public LFields removeForeignKey() {
        return remove(ForeignKey.class);
    }

    public LFields clone() {
        LFields fields = new LFields(entity, layout);
        fields.addAll(this);
        return this;
    }

    public LFields remove(Field... fields) {
        Arrays.stream(fields).forEach(field -> remove(getField(field)));
        return this;
    }

    public LField[] getAsArray() {
        return toArray(new LField[]{});
    }

}
