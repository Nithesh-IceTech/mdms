package za.co.spsi.lookup.dao;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by jaspervdb on 2/4/16.
 */
public class HierarchyDefinitionModuleList extends ArrayList<HierarchyDefinitionModule> {

    public HierarchyDefinitionModuleList(HierarchyDefinitionModule[] values) {
        if (values != null) {
            Collections.addAll(this, values);
        }
    }
}
