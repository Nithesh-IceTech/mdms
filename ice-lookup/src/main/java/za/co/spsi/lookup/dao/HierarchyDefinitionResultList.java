package za.co.spsi.lookup.dao;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by jaspervdb on 2/3/16.
 */
public class HierarchyDefinitionResultList extends ArrayList<HierarchyDefinitionResult> {

    public HierarchyDefinitionResultList(HierarchyDefinitionResult[] values) {
        if (values != null) {
            Collections.addAll(this, values);
        }
    }

}
