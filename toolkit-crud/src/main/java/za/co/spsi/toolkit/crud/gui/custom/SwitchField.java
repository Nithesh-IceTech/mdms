package za.co.spsi.toolkit.crud.gui.custom;

import com.vaadin.data.Property;
import org.vaadin.teemu.switchui.Switch;
import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import za.co.spsi.toolkit.entity.Field;

public class SwitchField extends LField<Boolean> {

    private Switch aSwitch;

    public SwitchField(Field field, String captionId, Layout model) {
        super(field, captionId,model);
    }

    @Override
    public com.vaadin.ui.Field buildVaadinField() {
        aSwitch = new Switch();
        aSwitch.setCaption(AbstractView.getLocaleValue(getCaptionId()));
        aSwitch.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                System.out.println("changed");
            }
        });
        aSwitch.setImmediate(true);

        return aSwitch;
    }

    @Override
    public void intoBindings() {
        super.intoBindings();
    }

    @Override
    public void intoControl() {
        aSwitch.setValue((Boolean) field.get());
    }

    @Override
    public Boolean getNonNull() {
        return get() != null && get();
    }
}

