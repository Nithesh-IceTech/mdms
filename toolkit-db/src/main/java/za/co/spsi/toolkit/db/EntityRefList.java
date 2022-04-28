package za.co.spsi.toolkit.db;

import java.util.ArrayList;

/**
 * Created by jaspervdb on 2016/04/29.
 */
public class EntityRefList extends ArrayList<EntityRef> {

    public EntityRefList getExportable(boolean parent) {
        EntityRefList entityRefs = new EntityRefList();
        for (EntityRef entityRef : this) {
            if (entityRef.getExportable() != null && entityRef.getExportable().parent() == parent) {
                entityRefs.add(entityRef);
            }
        }
        return entityRefs;
    }

    public EntityRef getExportableParent() {
        EntityRefList parents = getExportable(true);
        //Assert.isTrue(parents.size() < 2, "A Entity %s may not have more than one parent entity ref", size() > 0 ? get(0).getEntity().getClass() : "[No Parents]");
        for(EntityRef entityRef : parents) {
            if (entityRef.hasPotentialValue()) {
                return entityRef;
            }
        }

        return parents.isEmpty() ? null : parents.get(0);
    }

    public EntityRefList getForcedExportables() {
        EntityRefList entityRefs = new EntityRefList();
        for (EntityRef entityRef : this) {
            if (entityRef.getExportable() != null && entityRef.getExportable().forceExport()) {
                entityRefs.add(entityRef);
            }
        }
        return entityRefs;
    }

    public EntityRefList getExportableChildren() {
        return getExportable(false);
    }

}
