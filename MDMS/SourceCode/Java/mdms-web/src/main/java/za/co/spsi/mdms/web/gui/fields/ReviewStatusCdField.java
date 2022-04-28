package za.co.spsi.mdms.web.gui.fields;

import za.co.spsi.locale.annotation.MdmsLocaleId;
import za.co.spsi.lookup.Constants;
import za.co.spsi.mdms.common.MdmsConstants;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.fields.MLCSLookupField;
import za.co.spsi.toolkit.entity.Field;

/**
 * Created by jaspervdb on 2015/09/14.
 */
public class ReviewStatusCdField extends MLCSLookupField<Integer> {

    public ReviewStatusCdField(Field field, Layout model) {
        super(field, MdmsLocaleId.REVIEW_STATUS, model, Constants.REVIEWSTAT);
        getProperties().setMandatory(true);
        getProperties().setDefault(MdmsConstants.REVIEW_STATUS_TO_BE_REVIEWED.toString());
    }

}

