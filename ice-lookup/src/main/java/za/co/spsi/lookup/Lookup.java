package za.co.spsi.lookup;

/**
 * Created by jaspervdb on 1/20/16.
 */
public class Lookup {

    /**
     * name can either represent the lookupDefId or the hierarchyDefId
     */
    private String name;

    public Lookup(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
