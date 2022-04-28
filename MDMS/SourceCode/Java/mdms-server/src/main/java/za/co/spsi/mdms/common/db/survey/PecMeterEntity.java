/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.spsi.mdms.common.db.survey;

import org.json.JSONObject;
import za.co.spsi.mdms.common.db.utility.IceApprovedMeterReadingsView;
import za.co.spsi.mdms.common.db.utility.IceMeter;
import za.co.spsi.mdms.elster.db.ElsterMeterEntity;
import za.co.spsi.mdms.generic.meter.db.GenericMeterEntity;
import za.co.spsi.mdms.kamstrup.db.KamstrupMeterEntity;
import za.co.spsi.mdms.nes.db.NESMeterEntity;
import za.co.spsi.toolkit.crud.db.fields.UserIdField;
import za.co.spsi.toolkit.crud.db.gis.ImageGeoEntity;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.EntityRef;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.db.fields.Index;
import za.co.spsi.toolkit.entity.Exportable;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.entity.ano.AlwaysExport;
import za.co.spsi.toolkit.entity.ano.Audit;

import java.sql.Connection;

import static za.co.spsi.mdms.common.db.utility.UtilityHelper.*;
import static za.co.spsi.toolkit.db.ano.ForeignKey.Deferrability.InitiallyDeferred;

/**
 * @author francoism
 */

@Exportable(name = "pecMeter")
@Table(version = 8)
@Audit
public class PecMeterEntity extends EntityDB {
    @Id(uuid = true)
    @Column(name = "METER_ID", size = 50)
    public Field<String> meterId = new Field<>(this);
    @Column(name = "AGENCY_ID", size = 8)
    public Field<Integer> agencyId = new Field<>(this);
    @Column(name = "USER_ID", size = 50)
    public UserIdField userId = new UserIdField(this);
    @Column(name = "METER_REFERENCE", size = 10)
    public Field<String> meterReference = new Field<>(this);
    @Column(name = "METER_N", size = 50)
    public Field<String> meterN = new Field<>(this);
    @Column(name = "EXTERNAL_REF", size = 20)
    public Field<String> externalRef = new Field<>(this);
    @Column(name = "DESCRIPTION", size = 255)
    public Field<String> description = new Field<>(this);
    @Column(name = "SERVICE_GROUP_CD", size = 50)
    public Field<String> serviceGroupCd = new Field<>(this);
    @Column(name = "SUPPLY_TYPE_CD", size = 10)
    public Field<Integer> supplyTypeCd = new Field<>(this);
    @Column(name = "METER_MAKE_CD", size = 10)
    public Field<Integer> meterMakeCd = new Field<>(this);
    @Column(name = "METER_MODEL_CD", size = 10)
    public Field<Integer> meterModelCd = new Field<>(this);
    @Column(name = "METER_CONFIGURATION_CD", size = 100)
    public Field<String> meterConfigurationCd = new Field<>(this);
    @Column(name = "PREPAID_CD", size = 10)
    public Field<Integer> prepaidCd = new Field<>(this);
    @Column(name = "AUTO_AVERAGE_CD", size = 10)
    public Field<Integer> autoAverageCd = new Field<>(this);
    @Column(name = "METER_READ_CD", size = 10)
    public Field<Integer> meterReadCd = new Field<>(this);
    @Column(name = "METER_LOCATION", size = 50)
    public Field<String> meterLocation = new Field<>(this);
    @Column(name = "LAT", size = 10, decimalPlaces = 6)
    public Field<Double> lat = new Field<>(this);
    @Column(name = "LON", size = 10, decimalPlaces = 6)
    public Field<Double> lon = new Field<>(this);
    @Column(name = "METER_OWNER_CD", size = 10)
    public Field<Integer> meterOwnerCd = new Field<>(this);
    @Column(name = "ACTIVE_CD", size = 10)
    public Field<Integer> activeCd = new Field<>(this);
    @Column(name = "ACTUAL_SIZE_WATER", size = 4)
    public Field<Integer> actualSizeWater = new Field<>(this);
    @Column(name = "RELEVANT_SIZE_WATER", size = 4)
    public Field<Integer> relevantSizeWater = new Field<>(this);

    @ForeignKey(table = KamstrupMeterEntity.class, name = "PM_KAM_METER_ID", onDeleteAction = ForeignKey.Action.Cascade)
    @Column(name = "KAM_METER_ID", size = 50)
    public Field<String> kamMeterId = new Field<>(this);

    @ForeignKey(table = NESMeterEntity.class, name = "PM_NES_METER_ID", onDeleteAction = ForeignKey.Action.Cascade)
    @Column(name = "NES_METER_ID", size = 50)
    public Field<String> nesMeterId = new Field<>(this);

    @ForeignKey(table = za.co.spsi.mdms.generic.meter.db.GenericMeterEntity.class, name = "PM_GENERIC_METER_ID", onDeleteAction = ForeignKey.Action.Cascade)
    @Column(name = "GENERIC_METER_ID", size = 50)
    public Field<String> genericMeterId = new Field<>(this);

    @ForeignKey(table = ElsterMeterEntity.class, name = "PM_ELS_METER_ID", onDeleteAction = ForeignKey.Action.Cascade)
    @Column(name = "ELS_METER_ID", size = 50)
    public Field<String> elsMeterId = new Field<>(this);

    //PROPERTY
    @Column(name = "PROPERTY_ID", size = 50)
    @ForeignKey(table = PecPropertyEntity.class, onDeleteAction = ForeignKey.Action.Cascade)
    public Field<String> propertyId = new Field<>(this);

    public Field<String> reference_id = new Field<>(this);

    @Column(name = "NOTES", size = 2048, autoCrop = true)
    public Field<String> notes = new Field<>(this);

    @Exportable(name = "pecProperty", parent = true, forceExport = true)
    public EntityRef<PecPropertyEntity> propertyEntity = new EntityRef<>(propertyId, this);

    //UNIT
    @Column(name = "UNIT_ID", size = 50)
    @ForeignKey(table = PecUnitEntity.class, onDeleteAction = ForeignKey.Action.Cascade)
    public Field<String> unitId = new Field<>(this);

    @Exportable(name = "pecUnit", parent = true)
    public EntityRef<PecUnitEntity> unitEntity = new EntityRef<>(unitId, this);

    //MAPS IMAGES/DRAWINGS
    @Column(name = "IMAGE_ID", size = 50)
    @ForeignKey(table = ImageGeoEntity.class, deferrable = InitiallyDeferred, onDeleteAction = ForeignKey.Action.SetNull)
    @Exportable(name = "imageId")
    public Field<String> imageId = new Field<>(this);

    @Exportable(name = "imageGeo", deleteAllReferences = true, forceExport = true)
    public EntityRef<ImageGeoEntity> geoImage = new EntityRef<>(imageId, this);

    //PHOTOS
    @AlwaysExport
    @Exportable(name = "pecMeterPhotos", deleteAllReferences = true, forceExport = true)
    public EntityRef<PecMeterPhotoEntity> meterPhotos = new EntityRef<>(this);

    @Exportable(name = "property")
    public EntityRef<PecPropertyEntity> property = new EntityRef<>(propertyId, this);

    @Exportable(name = "pecMeterRegisterList")
    public EntityRef<PecMeterRegisterEntity> registers = new EntityRef<>(this);

    public EntityRef<PecMeterReadingListEntity> meterList = new EntityRef<>("select pec_meter_reading_list.* from pec_meter_reading_list where pec_meter_reading_list.METER_READING_LIST_ID in (" +
            "  select METER_READING_LIST_ID from PEC_METER_REGISTER where PEC_METER_REGISTER.METER_READING_LIST_ID =  pec_meter_reading_list.METER_READING_LIST_ID and " +
            "    PEC_METER_REGISTER.METER_ID = pec_meter.METER_ID" +
            ")", this);

    public EntityRef<NESMeterEntity> nesMeter = new EntityRef<>(nesMeterId, this);
    public EntityRef<KamstrupMeterEntity> kamMeter = new EntityRef<>(kamMeterId, this);
    public EntityRef<ElsterMeterEntity> elsMeter = new EntityRef<>(elsMeterId, this);
    public EntityRef<GenericMeterEntity> genericMeter = new EntityRef<>(genericMeterId, this);

    public Index idxMe = new Index("PEC_ME_PHOTO_EXP",this,meterId,propertyId);

    public PecMeterEntity() {
        super("PEC_METER");
    }

    public PecMeterEntity initMeter(IceMeter meter) {
        // TODO COMPLETE REST
        reference_id.set(meter.iceMeterID.get().toString());
        meterReference.set(meter.iceMeterID.get());
        meterN.set(meter.iceMeterNumber.get());
        externalRef.set(meter.iceExternalReference.get());
        description.set(meter.description.get());
        serviceGroupCd.set(getStringFromInt(meter.iceServiceGroupID.get()));
        supplyTypeCd.set(getIntFromString(meter.iceMeterSupplyType.get()));
        meterMakeCd.set(meter.iceMeterMakeID.get());
        meterModelCd.set(meter.iceMeterModelID.get());
        meterConfigurationCd.set(meter.iceMeterConfiguration.get());
        //TODO: check configuration
        prepaidCd.set(parseBoolean(meter.iceMeterPrepaid.get()));
        autoAverageCd.set(parseBoolean(meter.iceMeterAutoAverage.get()));
        meterReadCd.set(parseBoolean(meter.iceMeterRead.get()));
        meterLocation.set(meter.iceMeterLocation.get());
        //TODO: Check meter owner
        //meterOwnerCd.set(meter.iceMeterOwner.get());
        activeCd.set(parseBoolean(meter.isActive.get()));
        //TODO: check actual size water
        //actualSizeWater.set(meter.act);
        //TODO: check relavvantsize water
        //TODO unit id

        return this;
    }

    /**
     * determine the index of the export scale
     * @return
     */
    public int getExportScaleIndex() {
        return kamMeterId.get() != null ? 0
                : nesMeterId.get() != null ? 1
                : elsMeterId.get() != null ? 2
                : 3;
    }


    public static PecMeterEntity getOrCreate(Connection connection, PecPropertyEntity property, IceApprovedMeterReadingsView view) {
        PecMeterEntity meter = DataSourceDB.getFromSet(connection,
                (PecMeterEntity) new PecMeterEntity().reference_id.set(view.meter.iceMeterID.get()));
        meter = meter == null ? new PecMeterEntity() : meter;
        meter.initMeter(view.meter);
        meter.propertyId.set(property.propertyId.get());
        DataSourceDB.set(connection, meter);
        return meter;
    }

    public boolean isSmartMeter() {
        return meterConfigurationCd.getAsString().equals("AMI");
    }

    @Override
    public JSONObject exportAsJson(Connection connection) {
        return super.exportAsJson(connection);
    }

    public boolean linkToSmartMeters(Connection connection) {
        KamstrupMeterEntity kamMeter = (KamstrupMeterEntity) DataSourceDB.getFromSet(connection, (EntityDB) new KamstrupMeterEntity().serialN.set(meterN.get()));
        NESMeterEntity nesMeter = (NESMeterEntity) DataSourceDB.getFromSet(connection, (EntityDB) new NESMeterEntity().serialN.set(meterN.get()));
        ElsterMeterEntity elsMeter = (ElsterMeterEntity) DataSourceDB.getFromSet(connection, (EntityDB) new ElsterMeterEntity().serialN.set(meterN.get()));
        GenericMeterEntity genericMeterEntity = (GenericMeterEntity) DataSourceDB.getFromSet(connection, (EntityDB) new GenericMeterEntity().meterSerialN.set(meterN.get()));
        nesMeterId.set(nesMeter != null ? nesMeter.meterId.get() : null);
        kamMeterId.set(kamMeter != null ? kamMeter.meterId.get() : null);
        elsMeterId.set(elsMeter != null ? elsMeter.meterId.get() : null);
        genericMeterId.set(genericMeterEntity != null ? genericMeterEntity.genericMeterId.get() : null);
        return isSmartMeter();
    }


}
