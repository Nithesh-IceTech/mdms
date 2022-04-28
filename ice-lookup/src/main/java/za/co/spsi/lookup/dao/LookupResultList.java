package za.co.spsi.lookup.dao;

import za.co.spsi.toolkit.util.Assert;
import za.co.spsi.toolkit.util.StringList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by jaspervdb on 1/20/16.
 */
public class LookupResultList extends ArrayList<LookupResult> implements Serializable {

    public static final long serialVersionUID = 42L;

    public LookupResultList() {}

    public LookupResultList(LookupResult[] values) {
        if (values != null) {
            Collections.addAll(this, values);
        }
    }

    public LookupResult getForCode(String code) {
        for (LookupResult lookupResult : this) {
            if (code.equals(lookupResult.getLookupCode())) {
                return lookupResult;
            }
        }
        return null;
    }

    public LookupResult getForDesc(String desc) {
        for (LookupResult lookupResult : this) {
            if (desc.equals(lookupResult.getDescription())) {
                return lookupResult;
            }
        }
        return null;
    }

    public String  getDescForCode(String code) {
        LookupResult lookupResult = getForCode(code);
        return lookupResult != null?lookupResult.getDisplayValue():null;
    }

    public LookupResultList getForCodes(String ... codes) {
        LookupResultList lookupResults = new LookupResultList();
        for (String code : codes) {
            LookupResult lookupResult = getForCode(code);
            Assert.notNull(lookupResult,"Could not locate LookupResult for code %s",code);
            lookupResults.add(lookupResult);
        }
        return lookupResults;
    }


}
