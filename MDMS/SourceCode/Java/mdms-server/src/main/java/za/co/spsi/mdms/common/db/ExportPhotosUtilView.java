package za.co.spsi.mdms.common.db;

import za.co.spsi.mdms.common.db.utility.AdOrg;
import za.co.spsi.mdms.common.db.utility.IceCBpartner;
import za.co.spsi.mdms.common.db.utility.IceCInvoice;
import za.co.spsi.mdms.common.db.utility.IceProperty;
import za.co.spsi.toolkit.db.View;

/**
 * Created by ettienne on 2017/05/25.
 */
public class ExportPhotosUtilView extends View<ExportPhotosUtilView> {

    public IceCBpartner iceCBpartner = new IceCBpartner();
    public IceCInvoice iceCInvoice = new IceCInvoice();
    public IceProperty iceProperty = new IceProperty();
    public AdOrg adOrg = new AdOrg();

    public ExportPhotosUtilView init(String meterReadingId) {

        setSql("SELECT\n" +
                "  *\n" +
                "FROM\n" +
                "  C_Invoice,\n" +
                "  C_Bpartner,\n" +
                "  C_Invoiceline,\n" +
                "  ICE_PROPERTY,\n" +
                "  ad_org,\n" +
                "  ICE_METERREADINGS\n" +
                "WHERE\n" +
                "  ICE_METERREADINGS.ICE_METERREADINGS_ID = ? and\n" +
                "  C_Invoiceline.C_INVOICELINE_ID = ICE_METERREADINGS.C_INVOICELINE_ID and\n" +
                "  C_Invoice.C_Invoice_ID = C_Invoiceline.C_Invoice_ID AND\n" +
                "  C_Invoice.C_Bpartner_ID = C_Bpartner.C_Bpartner_ID AND\n" +
                "  ICE_PROPERTY.ICE_PROPERTY_ID = C_Invoiceline.ICE_PROPERTY_ID AND\n" +
                "  ad_org.ad_org_id = ICE_PROPERTY.ad_org_id", meterReadingId);

        return this;
    }

}