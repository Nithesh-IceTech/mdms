package za.co.spsi.toolkit.entity;

import za.co.spsi.toolkit.entity.ano.AlwaysExport;
import za.co.spsi.toolkit.reflect.RefFields;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaspervdbijl on 2017/04/10.
 */
public class ExportPathObject {

    private Class type;
    private ExportPathObject parentLink;
    private List<ExportPathObject> subs = new ArrayList<>();

    public ExportPathObject(Class type) {
        this.type = type;
        initAlwaysExportable();
    }

    public Class getType() {
        return type;
    }

    public ExportPathObject(Class type, ExportPathObject parentLink) {
        this(type);
        this.parentLink = parentLink;
    }

    private void initAlwaysExportable() {
        try {
            for (java.lang.reflect.Field field : new RefFields(type).filter(AlwaysExport.class)) {
                // add the generic type
                if (field.getGenericType() instanceof ParameterizedType) {
                    set((Class) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]);
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public ExportPathObject set(Class ... children) {
        for (Class child : children) {
            if (getSub(child) == null) {
                addSub(child);
            }
        }
        return this;
    }

    public ExportPathObject getSub(Class type) {
        for (ExportPathObject epo : subs) {
            if (epo.getType().equals(type)) {
                return epo;
            }
        }
        return null;
    }

    public ExportPathObject addSub(Class type) {
        // type must be a child
        if (getSub(type) != null) {
            return getSub(type);
        } else {
            ExportPathObject exportPathObject = new ExportPathObject(type, this);
            subs.add(exportPathObject);
            return exportPathObject;
        }
    }

    public ExportPathObject getSubReverse(Class type) {
        if (type.equals(type)) {
            return this;
        } else if (parentLink != null) {
            return parentLink.getSubReverse(type);
        } else {
            return null;
        }
    }

}
