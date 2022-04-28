package za.co.spsi.lookup.db;

import za.co.spsi.lookup.dao.HierarchyDefinitionResult;
import za.co.spsi.lookup.dao.LookupResult;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.entity.Field;


/**
 * Created by jaspervdb on 2/3/16.
 */
@Table(version = 2)
public class LookupEntity extends EntityDB {

    @Column(name = "parent_code")
    public Field<String> parentCode = new Field<String>(this);
    public Field<String> code = new Field<String>(this);
    @Column(name = "lookup_def")
    public Field<String> lookupDef = new Field<String>(this);
    public Field<String> lang = new Field<String>(this);
    public Field<String> description = new Field<String>(this);
    @Column(name = "agency_id")
    public Field<Integer> agencyId = new Field<Integer>(this);


    public LookupEntity() {
        super("lookups");
    }

    public void init(LookupResult lookupResult,String lookupDef,String parentLookup,Integer agencyId) {
        getFields().clearFields();
        this.code.set(lookupResult.getLookupCode());
        this.description.set(lookupResult.getDescription());
        this.lang.set(lookupResult.getLanguage());
        this.lookupDef.set(lookupDef);
        this.parentCode.set(parentLookup);
        this.agencyId.set(agencyId);
    }

    public void init(HierarchyDefinitionResult lookupResult, String lang, Integer agencyId) {
        getFields().clearFields();
        this.code.set(lookupResult.getChildLookupCode());
        this.description.set(lookupResult.getChildLookupDescription());
        this.lang.set(lang);
        this.parentCode.set(lookupResult.getParentLookupCode());
        this.lookupDef.set(lookupResult.getChildLookupDefinitionId());
        this.agencyId.set(agencyId);
    }

}
