package za.co.spsi.mdms.common.db.survey;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.entity.Field;


/**
 * Created by jaspervdb on 2/3/16.
 * Structure used to build the table
 */
@Table(version = 0)
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
        super("LOOKUPS");
    }


}
