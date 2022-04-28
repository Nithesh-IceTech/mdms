package za.co.spsi.toolkit.crud.db.gis;

import za.co.spsi.toolkit.crud.db.audit.AuditEntityDB;
import za.co.spsi.toolkit.db.EntityRef;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.entity.Exportable;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.entity.ano.AlwaysExport;


/**
 * Created by ettienne on 2015/09/18.
 */
@Table(version = 1)
public class ImageGeoEntity extends AuditEntityDB {

    @za.co.spsi.toolkit.db.ano.Id(uuid=true)
    @za.co.spsi.toolkit.db.ano.Column(name = "IMAGE_ID")
    public Field<String> imageId= new Field<>(this);

    @Exportable(dontExport = true)
    public Field<byte[]> image= new Field<>(this);
    @Exportable(dontExport = true)
    public Field<String> notes= new Field<>(this);

    @AlwaysExport
    @Exportable(name = "imageGeoLocations", deleteAllReferences = false, forceExport = true)
    public EntityRef<ImageGeoLocationEntity> imageGeoLocations = new EntityRef<>(this);

    public ImageGeoEntity() {
        super("IMAGE");
    }
}
