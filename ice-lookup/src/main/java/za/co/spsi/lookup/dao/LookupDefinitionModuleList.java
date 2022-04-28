package za.co.spsi.lookup.dao;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by jaspervdb on 2/4/16.
 */
public class LookupDefinitionModuleList extends ArrayList<LookupDefinitionModule> {

    public LookupDefinitionModuleList(LookupDefinitionModule[] values) {
        if (values != null) {
            Collections.addAll(this, values);
        }
    }

    public LookupDefinitionModule getByLookupDef(String lookupDef) {
        for (LookupDefinitionModule lookupDefinitionModule: this) {
            if (lookupDef.equals(lookupDefinitionModule.getLookupDefinitionModuleEntityPK().getLookupDefinitionId())) {
                return lookupDefinitionModule;
            }
        }
        return null;
    }

    public boolean containsLookupDef(String lookupDef) {
        return getByLookupDef(lookupDef) != null;
    }

    public LookupDefinitionModuleList removeGroups() {
        for (int i = 0;i < size();i++) {
            String name = get(i).getLookupDefinitionModuleEntityPK().getLookupDefinitionId();
            if (name.contains("GROUP") && containsLookupDef(name.substring(0,name.indexOf("GROUP")))) {
                remove(i--);
            }
        }
        return this;
    }
}
