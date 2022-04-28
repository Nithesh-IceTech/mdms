package za.co.spsi.toolkit.crud.gis.db;

import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.entity.Entity;
import za.co.spsi.toolkit.entity.Field;

public class DeviceTrackedEntity extends Entity {

    @Column(name = "MODEL_NUMBER")
    public Field<String> modelNumber = new Field<String>(this);

    @Column(name = "SERIAL_NUMBER")
    public Field<String> serialNumber = new Field<String>(this);

    @Column(name = "DEVICE_TYPE")
    public Field<Integer> deviceType = new Field<Integer>(this);

    @Column(name = "SCREEN_WIDTH")
    public Field<Integer> screenWidth = new Field<Integer>(this);

    @Column(name = "SCREEN_HEIGHT")
    public Field<Integer> screenHeight = new Field<Integer>(this);


    public DeviceTrackedEntity() {
        super("DEVICE");
    }

    public DeviceTrackedEntity(Entity entity) {
        this();
        initEntity(entity);
    }

}
