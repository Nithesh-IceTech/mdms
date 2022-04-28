package za.co.spsi.mdms.web.gui.fields;

import za.co.spsi.lookup.Constants;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.fields.MLCSLookupField;
import za.co.spsi.toolkit.entity.Field;

/**
 * Created by jaspervdb on 2015/09/14.
 */

public class YesNoLookupField extends MLCSLookupField<Integer> {
    public YesNoLookupField(Field field, String captionId, Layout model, MLCSLookupField parentReference) {
        super(field, captionId, model, Constants.YESNO, parentReference);
        setNullAllowed(false);
    }

    public YesNoLookupField(Field field, String captionId, Layout model) {
        this(field,captionId,model,null);
    }
}
