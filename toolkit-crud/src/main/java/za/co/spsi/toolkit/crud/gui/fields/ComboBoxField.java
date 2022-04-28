package za.co.spsi.toolkit.crud.gui.fields;

import com.vaadin.ui.ComboBox;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import za.co.spsi.toolkit.entity.Field;

/**
 * Created with IntelliJ IDEA.
 * User: jaspervdb
 * Date: 2013/11/01
 * Time: 8:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class ComboBoxField<T> extends LField<T> {

    private String options[];
    private Object values[];

    public ComboBoxField(Field<T> field, String captionId,String options[], Object values[], Layout layout) {
        super(field, captionId,layout);
        this.options = options;
        this.values = values;
    }

    private ComboBox getComboBoxField() {
        ComboBox comboBox = new ComboBox();
        int i = 0;
        for (String option : options) {
            comboBox.addItem(values[i]);
            comboBox.setItemCaption(values[i++], AbstractView.getLocaleValue(option));
        }
        return comboBox;
    }

    public String[] getOptions() {
        return options;
    }

    protected void setOptions(String[] options) {
        this.options = options;
    }

    public Object[] getValues() {
        return values;
    }

    public void setValues(Object[] values) {
        this.values = values;
    }

    @Override
    public com.vaadin.ui.Field buildVaadinField() {
        ComboBox comboBox = getComboBoxField();
        return comboBox;
    }
}
