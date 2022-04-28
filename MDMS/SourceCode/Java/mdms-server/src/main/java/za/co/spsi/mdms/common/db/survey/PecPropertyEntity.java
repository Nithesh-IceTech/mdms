/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.spsi.mdms.common.db.survey;

import org.json.JSONObject;
import za.co.spsi.mdms.common.db.utility.IceApprovedMeterReadingsView;
import za.co.spsi.mdms.common.db.utility.IceProperty;
import za.co.spsi.toolkit.crud.db.fields.UserIdField;
import za.co.spsi.toolkit.crud.db.gis.ImageGeoEntity;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.EntityRef;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.entity.ExportPathObject;
import za.co.spsi.toolkit.entity.Exportable;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.entity.ano.AlwaysExport;
import za.co.spsi.toolkit.entity.ano.Audit;
import za.co.spsi.toolkit.entity.ano.ExportableEntity;

import java.sql.Connection;
import java.sql.DriverManager;

@Audit
@Table(version = 16, allowFkDrop = true)
public class PecPropertyEntity extends SyncEntity implements ExportableEntity {

    @Override
    public ExportPathObject getExportObject() {
        ExportPathObject property = new ExportPathObject(PecPropertyEntity.class).
                set(PecPropertyPhotoEntity.class, ImageGeoEntity.class, PecLocationSurveyEntity.class, PecMeterEntity.class, PecUnitEntity.class);

        property.addSub(PecLocationSurveyEntity.class).set(PecLocationSurveyPhotoEntity.class, ImageGeoEntity.class);

        property.addSub(PecMeterEntity.class).set(ImageGeoEntity.class, PecMeterPhotoEntity.class, PecPropertyEntity.class, PecUnitEntity.class, PecMeterRegisterEntity.class);

        ExportPathObject unit = property.addSub(PecUnitEntity.class).set(PecUnitPhotoEntity.class, PecMeterEntity.class, PecPropertyEntity.class);
        unit.addSub(PecMeterEntity.class).set(ImageGeoEntity.class, PecMeterPhotoEntity.class, PecPropertyEntity.class, PecUnitEntity.class, PecMeterRegisterEntity.class);
        return property;
    }

    @Id(uuid = true)
    @Column(name = "PROPERTY_ID", size = 50)
    public Field<String> propertyId = new Field<>(this);

    @Column(name = "AGENCY_ID", size = 8)
    public Field<Integer> agencyId = new Field<>(this);

    @Column(name = "USER_ID", size = 50)
    public UserIdField userId = new UserIdField(this);

    @Column(name = "PROPERTY_NAME", size = 250)
    public Field<String> propertyName = new Field<>(this);

    @Column(name = "PROPERTY_CODE")
    public Field<String> propertyCode = new Field<>(this);

    @Column(name = "EXTERNAL_REF", size = 50)
    public Field<String> externalRef = new Field<>(this);

    @Column(name = "PROPERTY_TYPE_CD", size = 10)
    public Field<Integer> propertyTypeCd = new Field<>(this);

    @Column(name = "COMPANY", size = 250)
    public Field<String> company = new Field<>(this);

    @Column(name = "AREA", size = 50)
    public Field<String> area = new Field<>(this);

    public SharedLocation sharedLocation = new SharedLocation(this);

    @Column(name = "PROPERTY_ENTITY_TYPE_CD", size = 10)
    public Field<Integer> propertyEntityTypeCd = new Field<>(this);

    @AlwaysExport
    @Exportable(name = "pecPropertyPhotos", deleteAllReferences = true, forceExport = true)
    public EntityRef<PecPropertyPhotoEntity> propertyPhotos = new EntityRef<>(this);

    @Exportable(forceExport = true,name = "pecUnitList")
    public EntityRef<PecUnitEntity> unitList = new EntityRef<>(this);

    @Exportable(forceExport = true,name = "pecMeterList")
    public EntityRef<PecMeterEntity> meterList = new EntityRef<>(this);


    public PecPropertyEntity() {
        super("PEC_PROPERTY");
    }

    public PecPropertyEntity initProperty(IceProperty property) {
        this.propertyName.set(property.propertyName.get());
        sharedEntity.reference_id.set(property.icePropertyID.get().toString());
        propertyCode.set(property.icePropertyCode.get());
        externalRef.set(property.icePropertyExternalRef.get());
        propertyTypeCd.set(property.icePropertyTypeID.get());
//        area.set(property.area)
        sharedLocation.streetName.set(property.iceStreetName.get());
        sharedLocation.cityCd.set(property.cCityID.get());
        sharedLocation.suburbCd.set(property.iceDistrictID.getNonNull()+"");
        sharedLocation.countryCd.set(property.cCountryID.getNonNull()+"");
        sharedLocation.provinceCd.set(property.cRegionID.get());
        sharedLocation.standNumber.set(property.iceStreetNumber.get());
        sharedLocation.lat.set(property.iceLatitude.get());
        sharedLocation.lon.set(property.iceLongitude.get());

        propertyTypeCd.set(property.icePropertyTypeID.get());

        return this;
    }

    public static PecPropertyEntity getOrCreate(Connection connection, IceApprovedMeterReadingsView view) {
        PecPropertyEntity property = new PecPropertyEntity();
        property.sharedEntity.reference_id.set(view.property.icePropertyID.get().toString());
        property = DataSourceDB.getFromSet(connection,property);
        property = property == null?new PecPropertyEntity():property;
        property.initProperty(view.property);
        DataSourceDB.set(connection,property);
        return property;
    }

    public static void main(String args[]) throws Exception {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection connection = DriverManager.getConnection("jdbc:sqlserver://105.255.128.202:1433;DatabaseName=NES_Core","PECDB","PeCDB.2015@");

        PecPropertyEntity prop = (PecPropertyEntity) DataSourceDB.getFromSet(connection,(EntityDB) new PecPropertyEntity().propertyId.set("000768f3-99ae-4396-bd5d-a54dd268786d"));
        JSONObject json = prop.exportAsJson(connection);
        System.out.println("" + json);
    }
}
