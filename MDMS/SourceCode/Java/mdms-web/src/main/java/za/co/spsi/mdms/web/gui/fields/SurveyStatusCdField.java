package za.co.spsi.mdms.web.gui.fields;

import za.co.spsi.locale.annotation.MdmsLocaleId;
import za.co.spsi.lookup.Constants;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.fields.MLCSLookupField;
import za.co.spsi.toolkit.entity.Field;

/**
 * Created by jaspervdb on 2015/09/14.
 */
public class SurveyStatusCdField extends MLCSLookupField<Integer> {

    public SurveyStatusCdField(Field field, Layout model) {
        super(field, MdmsLocaleId.LOCATION_SURVEY_STATUS, model, Constants.SURVEYSTAT);
        getProperties().setMandatory(true);
    }


}
