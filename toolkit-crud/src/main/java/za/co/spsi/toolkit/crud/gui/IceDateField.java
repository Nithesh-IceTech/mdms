package za.co.spsi.toolkit.crud.gui;

import com.vaadin.ui.DateField;
import za.co.spsi.toolkit.util.StringList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jaspervdb on 2016/08/24.
 */
public class IceDateField extends DateField {

    public static final Logger TAG = Logger.getLogger(IceDateField.class.getName());

    public IceDateField() {
        super();
    }

    public IceDateField(String caption) {
        super(caption);
    }

    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        String newDateString = (String) variables.get("dateString");
        if (newDateString != null && getDateFormat().indexOf("/") != -1 && newDateString.indexOf("/") == -1) {
            try {
                StringList sl = new StringList();
                // insert the required /
                for (String value : getDateFormat().split("/")) {
                    sl.add(newDateString.substring(0, value.length()));
                    newDateString = newDateString.substring(value.length());
                }
                setValue(new SimpleDateFormat(getDateFormat()).parse(sl.toString("/")));
            } catch (Exception e) {
                TAG.log(Level.INFO, e.getMessage(), e);
                setValue(null);
            }
        } else {
            super.changeVariables(source, variables);
        }
    }
}
