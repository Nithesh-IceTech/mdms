package za.co.spsi.mdms.web.gui.fields;


import za.co.spsi.locale.annotation.MdmsLocaleId;
import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.ValueChangeListener;
import za.co.spsi.toolkit.entity.Field;

import java.sql.Timestamp;

/**
 * Created by jaspervdb on 2015/09/14.
 */
public class ReviewStatusChdDateField extends LField<Timestamp> implements ValueChangeListener {

    public ReviewStatusChdDateField(Field field, ReviewStatusCdField reviewStatusCdField,Layout model) {
        super(field, MdmsLocaleId.REVIEW_STATUS_CHANGE_DATE, model);
        getProperties().setReadOnly(true);
        reviewStatusCdField.addValueChangeListener(this);
    }

    @Override
    public void valueChanged(LField srcField, com.vaadin.ui.Field field, boolean inConstruction,boolean valueIsNull) {
        if (!inConstruction) {
            set(new Timestamp(System.currentTimeMillis()));
            intoControl();
        }
    }

}

