package za.co.spsi.mdms.common.db;

import za.co.spsi.mdms.common.db.survey.*;
import za.co.spsi.toolkit.db.View;

/**
 * Created by ettienne on 2017/05/25.
 */
public class ExportPhotosMDMSView extends View<ExportPhotosMDMSView> {

    public PecMeterReadingPhotoEntity pecMeterReadingPhotoEntity = new PecMeterReadingPhotoEntity();
    public PecMeterReadingEntity pecMeterReadingEntity = new PecMeterReadingEntity();
    public PecMeterRegisterEntity pecMeterRegisterEntity = new PecMeterRegisterEntity();
    public PecMeterEntity pecMeterEntity = new PecMeterEntity();
    public PecPropertyEntity pecPropertyEntity = new PecPropertyEntity();

    public ExportPhotosMDMSView() {
        super();
        setSql("SELECT *\n" +
                "FROM\n" +
                "  PEC_METER_READING_PHOTO,\n" +
                "  PEC_METER_READING,\n" +
                "  PEC_METER_REGISTER,\n" +
                "  PEC_METER,\n" +
                "  PEC_PROPERTY\n" +
                "WHERE\n" +
                "  (PEC_METER_READING_PHOTO.PHOTO_EXPORTED = 'N' OR PEC_METER_READING_PHOTO.PHOTO_EXPORTED IS NULL) AND\n" +
                "  PEC_METER_READING.METER_READING_ID = PEC_METER_READING_PHOTO.METER_READING_ID AND\n" +
                "  PEC_METER_READING.METER_REGISTER_ID = PEC_METER_REGISTER.METER_REGISTER_ID AND\n" +
                "  PEC_METER_REGISTER.METER_ID = PEC_METER.METER_ID AND\n" +
                "  PEC_PROPERTY.PROPERTY_ID = PEC_METER.PROPERTY_ID");

    }
}