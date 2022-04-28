package za.co.spsi.mdms.common.db.utility;

import za.co.spsi.mdms.common.db.generator.GeneratorEntity;
import za.co.spsi.toolkit.db.DSDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.entity.Field;

import javax.sql.DataSource;
import java.sql.Timestamp;

/**
 * Created by jaspervdbijl on 2017/03/29.
 */
public class IceGeneratorMeter extends EntityDB {

    @Column(name = "CREATED")
    public Field<Timestamp> created = new Field<>(this);

    @Column(name = "ICE_GENERATOR_UU")
    public Field<String> iceGeneratorUU = new Field<>(this);

    public Field<Character> isActive = new Field<>(this);

    @Column(name = "SERIAL_NO")
    public Field<String> serialN = new Field<>(this);

    @Column(name = "NAME")
    public Field<String> name = new Field<>(this);

    @Column(name = "CELL_NO")
    public Field<String> cellNo = new Field<>(this);

    @Column(name = "UPDATED")
    public Field<Timestamp> updated = new Field<>(this);


    @Column(name = "ICE_METER_ID")
    //@ForeignKey() IceMeter
    public Field<Integer> meterId = new Field<>(this);

    @Column(name = "ICE_METER_NUMBER")
    public Field<String> iceMeterNumber = new Field<>(this);


    public IceGeneratorMeter() {
        super("ICE_GENERATOR_METER_V");
    }

    public void init(GeneratorEntity generatorEntity) {
        generatorEntity.enabled.set("Y".equalsIgnoreCase(isActive.getSerial()));
        generatorEntity.id.set(iceGeneratorUU.get());
        generatorEntity.serialN.set(serialN.get());
        generatorEntity.msisdn.set(cellNo.get());
        generatorEntity.description.set(name.get());
    }

    public GeneratorEntity sync(DataSource dataSource, boolean update) {
        GeneratorEntity generator = DSDB.loadFromId(dataSource,(GeneratorEntity) new GeneratorEntity().id.set(iceGeneratorUU.get()));
        generator = generator == null?new GeneratorEntity():generator;
        if (!generator.isInDatabase() || update) {
            init(generator);
            DSDB.set(dataSource,generator);
        }
        // check the meter config

        return generator;
    }
}
