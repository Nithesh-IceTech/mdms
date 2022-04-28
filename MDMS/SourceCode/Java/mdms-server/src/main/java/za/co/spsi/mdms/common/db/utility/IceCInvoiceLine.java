package za.co.spsi.mdms.common.db.utility;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.entity.Field;

public class IceCInvoiceLine extends EntityDB {

    @Column(name = "C_INVOICE_ID")
    public Field<Integer> cInvoiceId = new Field<>(this);

    @Column(name = "DOCUMENTNO")
    public Field<String> documentNo = new Field<>(this);

    public IceCInvoiceLine() {
        super("C_INVOICELINE");
    }
}
