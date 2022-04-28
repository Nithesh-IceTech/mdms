package za.co.spsi.toolkit.crud.payment.gui.layout;

import com.vaadin.event.FieldEvents;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import org.vaadin.viritin.layouts.MVerticalLayout;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.lookup.service.LookupServiceHelper;
import za.co.spsi.toolkit.crud.gui.lookup.ToolkitLookupServiceHelper;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import za.co.spsi.toolkit.crud.payment.gui.fields.PayTypeComboBox;

import javax.sql.DataSource;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import static com.vaadin.ui.AbstractTextField.TextChangeEventMode.EAGER;

public class PayLayout {

    public ToolkitLookupServiceHelper lookupServiceHelper;

    public Double amountDued = 0.0;
    public static NumberFormat formatter = new DecimalFormat("###,###.00");
    public String feeType, id;
    public PayTypeComboBox payTypeComboBox;
    public DataSource dataSource;
    public TextField amountDue, changeDue, amountReceived, cellNumber;
    public Button payButton, cancelButton;
    public Boolean paid = false;
    public ArrayList<Fee> selectFeeListViewItems;
    public VerticalLayout feesLayout;
    public HorizontalLayout buttons;

    public PayLayout(DataSource dataSource, String feeType, String id, ToolkitLookupServiceHelper lookupServiceHelper) {
        this.feeType = feeType;
        this.id = id;
        this.dataSource = dataSource;
        this.lookupServiceHelper = lookupServiceHelper;
    }

    public Window buildUI() {
        
        addAllNecessaryFees();
        feesLayout = new VerticalLayout();
        String textString = "";
        TextArea text = new TextArea();
        text.setWidth("100%");
        text.setEnabled(false);
        int rows = 1;
        for(Fee fee:selectFeeListViewItems){
            text.setRows(rows++);
            textString = textString + fee.getFeeType() + "\t USD " + formatter.format(fee.getFee()) +"\n";
            text.setValue(textString);
        }
        feesLayout.addComponent(text);
        feesLayout.setMargin(new MarginInfo(true, false, true, false));
        buttons = new HorizontalLayout();
        buttons.setMargin(new MarginInfo(true, true, true, true));

        Window subWindow = new Window(AbstractView.getLocaleValue(ToolkitLocaleId.PAYMENT));
        VerticalLayout subContent = new VerticalLayout();
        payTypeComboBox = new PayTypeComboBox();
        payTypeComboBox.init("",false);
        subContent.setMargin(new MarginInfo(true, true, true, true));
        subContent.setWidth("100%");
        subWindow.setContent(subContent);

        amountDue = new TextField(AbstractView.getLocaleValue(ToolkitLocaleId.AMOUNT_DUE));
        amountDue.setEnabled(false);
        amountDue.setInputPrompt(amountDued.toString());

        changeDue = new TextField(AbstractView.getLocaleValue(ToolkitLocaleId.CHANGE_DUE));
        changeDue.setEnabled(false);
        changeDue.setTextChangeEventMode(EAGER);
        changeDue.setImmediate(true);

        cellNumber = new TextField(AbstractView.getLocaleValue(ToolkitLocaleId.CELLPHONE_NUMBER));
        cellNumber.setTextChangeEventMode(EAGER);
        cellNumber.setImmediate(true);

        amountReceived = new TextField(AbstractView.getLocaleValue(ToolkitLocaleId.AMOUNT_RECEIVED));
        amountReceived.setTextChangeEventMode(EAGER);
        amountReceived.setImmediate(true);
        amountReceived.addTextChangeListener((FieldEvents.TextChangeListener) event -> {
            try {
                Double change = new Double(amountDue.getInputPrompt()) - new Double(event.getText());
                if(change<=0) {
                    paid = true;
                    changeDue.setValue(formatter.format(
                            Math.abs(new Double(amountDue.getInputPrompt()) - new Double(event.getText()))));
                }
                else{
                    paid = false;
                    changeDue.setValue(formatter.format(new Double(0)));
                }
            }catch (Exception ex){
                amountReceived.setValue("");
                changeDue.setValue("");
            }

        });

        payButton = new Button(AbstractView.getLocaleValue(ToolkitLocaleId.PAY));
        cancelButton = new Button(AbstractView.getLocaleValue(ToolkitLocaleId.CANCEL));

        payButton.addClickListener(clickEvent ->{
                if(validate()) {
                    createPaymentEntities();
                    subWindow.close();
                }
        });
        cancelButton.addClickListener(clickEvent -> subWindow.close());
        buttons.addComponents(payButton, cancelButton);
        payTypeComboBox.getComboBox().addValueChangeListener(valueChangeEvent -> {
            if (payTypeComboBox.getValue() != null && payTypeComboBox.getValue().getCode().equals(PayTypeComboBox.PayType.CASH.getCode())){
                subContent.removeAllComponents();
                subContent.addComponent(new MVerticalLayout(payTypeComboBox.getComboBox()));
                subContent.addComponent(feesLayout);
                subContent.addComponent(amountDue);
                subContent.addComponent(amountReceived);
                subContent.addComponent(changeDue);
                subContent.addComponent(buttons);
            }else if(payTypeComboBox.getValue() != null && payTypeComboBox.getValue().getCode().equals(PayTypeComboBox.PayType.ECO_CASH.getCode())){
                subContent.removeAllComponents();
                subContent.addComponent(new MVerticalLayout(payTypeComboBox.getComboBox()));
                subContent.addComponent(feesLayout);
                subContent.addComponent(amountDue);
                subContent.addComponent(cellNumber);
                subContent.addComponent(buttons);
            }
        });
        subContent.addComponent(new MVerticalLayout(payTypeComboBox.getComboBox()));
        subContent.addComponent(feesLayout);
        subContent.addComponent(amountDue);
        subContent.addComponent(amountReceived);
        subContent.addComponent(changeDue);
        subContent.addComponent(buttons);
        subWindow.center();
        return subWindow;
    }

    public Boolean addAllNecessaryFees() {
        return null;
    }

    public Boolean createPaymentEntities() {
        return null;
    }

    public Boolean validate() {
        return null;
    }

    public static class Fee implements Serializable {
        BigDecimal fee;
        String feeType, transactionId;
        Boolean paid, mandatory;

        public Fee(BigDecimal fee, String feeType, Boolean paid, Boolean mandatory, String transactionId) {
            this.fee = fee;
            this.feeType = feeType;
            this.paid = paid;
            this.mandatory = mandatory;
            this.transactionId = transactionId;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public Boolean getMandatory() {
            return mandatory;
        }

        public void setMandatory(Boolean mandatory) {
            this.mandatory = mandatory;
        }

        public BigDecimal getFee() {
            return fee;
        }

        public void setPaid(Boolean paid) {
            this.paid = paid;
        }

        public Boolean getPaid() {
            return paid;
        }

        public String getFeeType() {
            return feeType;
        }

    }

}
