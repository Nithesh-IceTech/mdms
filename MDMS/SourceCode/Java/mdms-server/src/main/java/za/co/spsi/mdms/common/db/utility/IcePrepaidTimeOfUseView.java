package za.co.spsi.mdms.common.db.utility;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.entity.Field;

import java.sql.Timestamp;

/**
 * Created by Arno Combrinck 2020-10-23
 */
public class IcePrepaidTimeOfUseView extends EntityDB {

    @Column(name = "ICE_METER_ID")
    public Field<String> icemeterId = new Field<>(this);

    @Column(name = "ICE_METER_NUMBER")
    public Field<String> iceMeterNumber = new Field<>(this);

    @Column(name = "ICE_METER_REGISTER_ID")
    public Field<String> iceMeterRegisterId = new Field<>(this);

    @Column(name = "ICE_METER_REGISTERID")
    public Field<String> mdmsMeterRegisterId = new Field<>(this);

    @Column(name = "ICE_METERREADINGS_ID")
    public Field<String> iceMeterReadingsId = new Field<>(this);

    @Column(name = "ICE_METER_READING")
    public Field<Double> iceMeterReading = new Field<>(this);

    @Column(name = "MTR_REG_TYPE")
    public Field<String> mtrRegType = new Field<>(this);

    @Column(name = "ICE_LINKED_SERVICES_ID")
    public Field<String> iceLinkedServicesId = new Field<>(this);

    @Column(name = "M_PRICELIST_ID")
    public Field<String> mPriceListId = new Field<>(this);

    @Column(name = "M_PRICELIST_VERSION_ID")
    public Field<String> mPriceListVersionId = new Field<>(this);

    @Column(name = "PLV_NAME")
    public Field<String> plvName = new Field<>(this);

    @Column(name = "PLV_VALID_FROM")
    public Field<Timestamp> plvValidFrom = new Field<>(this);

    @Column(name = "STARTTIME")
    public Field<Timestamp> startTime = new Field<>(this);

    @Column(name = "ENDTIME")
    public Field<Timestamp> endTime = new Field<>(this);

    @Column(name = "DOW_NAME")
    public Field<String> dowName = new Field<>(this);

    @Column(name = "RNK")
    public Field<Character> rnk = new Field<>(this);

    @Column(name = "RNK2")
    public Field<Character> rnk2 = new Field<>(this);

    public IcePrepaidTimeOfUseView() {
        super("ICE_PREPAID_TOU_V");
    }

}
