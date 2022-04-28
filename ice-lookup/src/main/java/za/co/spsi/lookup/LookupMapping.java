package za.co.spsi.lookup;

/**
 * Created by jaspervdb on 1/20/16.
 */
public class LookupMapping {

    private String hierarchyDefId;
    private Lookup parent,child;

    public LookupMapping(String hierarchyDefId,Lookup parent,Lookup child) {
        this.hierarchyDefId = hierarchyDefId;
        this.parent = parent;
        this.child = child;
    }

    public String getHierarchyDefId() {
        return hierarchyDefId;
    }

    public void setHierarchyDefId(String hierarchyDefId) {
        this.hierarchyDefId = hierarchyDefId;
    }

    public Lookup getParent() {
        return parent;
    }

    public void setParent(Lookup parent) {
        this.parent = parent;
    }

    public Lookup getChild() {
        return child;
    }

    public void setChild(Lookup child) {
        this.child = child;
    }
}
