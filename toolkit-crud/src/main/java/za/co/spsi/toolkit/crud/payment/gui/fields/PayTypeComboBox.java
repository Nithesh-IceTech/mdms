package za.co.spsi.toolkit.crud.payment.gui.fields;

import com.vaadin.ui.*;
import org.vaadin.addons.comboboxmultiselect.ComboBoxMultiselect;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import java.util.*;

/**
 * Created by jaspervdb on 2015/10/06.
 */
public class PayTypeComboBox {

    private AbstractSelect comboBox;
    public PayTypeComboBox() {

    }

    public void init(String input, boolean allowMultiple) {

        if (allowMultiple) {
            comboBox = new ComboBoxMultiselect();
            ((ComboBoxMultiselect)comboBox).setInputPrompt(input);
        } else {
            comboBox = new ComboBox();
        }
        comboBox.setWidth("100%");
        EnumSet<PayType> payTypes = EnumSet.allOf(PayType.class);
        payTypes.stream().forEach(payType -> {
            comboBox.addItem(payType);
            comboBox.setItemCaption(payType, payType.getValue());
        });
        comboBox.select(payTypes.toArray()[0]);
    }

    public AbstractSelect getComboBox() {
        return comboBox;
    }

    public PayType getValue() {
        return (PayType)comboBox.getValue();
    }

    public enum PayType {

        CASH(AbstractView.getLocaleValue(ToolkitLocaleId.CASH), 1),
        ECO_CASH(AbstractView.getLocaleValue(ToolkitLocaleId.ECO_CASH), 8);

        private String value;
        private Integer code;

        PayType(String value, Integer code) {
            this.value = value;
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public Integer getCode(){
            return code;
        }

        @Override
        public String toString() {
            return this.getValue();
        }

        public static PayType getEnum(String value) {
            if(value == null)
                throw new IllegalArgumentException();
            for(PayType v : values())
                if(value.equalsIgnoreCase(v.getValue())) return v;
            throw new IllegalArgumentException();
        }
    }

}