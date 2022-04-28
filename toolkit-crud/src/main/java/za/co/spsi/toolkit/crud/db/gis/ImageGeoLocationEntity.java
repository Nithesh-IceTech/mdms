/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.spsi.toolkit.crud.db.gis;

import za.co.spsi.toolkit.crud.db.audit.AuditEntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.db.fields.Index;
import za.co.spsi.toolkit.entity.Field;

import static za.co.spsi.toolkit.db.ano.ForeignKey.Action.Cascade;


/**
 *
 * @author francoism
 */

@Table(version = 5, maintainStrict = false, allowFkDrop = true)
public class ImageGeoLocationEntity extends AuditEntityDB {

    @Id(uuid=true)
    @Column(name = "IMAGE_GEO_LOCATION_ID")
    public Field<String> imageGeoLocationId= new Field<>(this);
    @Column(name = "GPS_LOCATIONS",size = 4000)
    public Field<String> gpsLocations= new Field<>(this);
    @Column(name = "AREA")
    public Field<Double> area= new Field<>(this);
    @Column(name = "GEO_LOCATION_TYPE_CD")
    public Field<Integer> geoLocationTypeCd= new Field<>(this);

    @ForeignKey(table = ImageGeoEntity.class, onDeleteAction = Cascade)
    @Column(name = "IMAGE_ID")
    public Field<String>imageId= new Field<>(this);

    public Index mapPolyIdx = new Index("iGMapPolyIdx",this,imageId);

    public ImageGeoLocationEntity() {
        super("IMAGE_GEO_LOCATION");
    }

}
