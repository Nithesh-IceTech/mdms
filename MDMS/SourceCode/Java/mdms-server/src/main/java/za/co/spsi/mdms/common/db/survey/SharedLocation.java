package za.co.spsi.mdms.common.db.survey;

import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.entity.Entity;
import za.co.spsi.toolkit.entity.Field;

/**
 * Created by jaspervdbijl on 2017/03/23.
 */
public class SharedLocation extends Entity {

    @Column(name = "COUNTRY_CD", size = 10)
    public Field<String> countryCd = new Field<>(this);

    @Column(name = "PROVINCE_CD", size = 10)
    public Field<Integer> provinceCd = new Field<>(this);

    @Column(name = "CITY_CD", size = 10)
    public Field<Integer> cityCd = new Field<>(this);

    @Column(name = "SUBURB_CD")
    public Field<String> suburbCd = new Field<>(this);

    @Column(name = "STAND_NUMBER", size = 30)
    public Field<String> standNumber = new Field<>(this);

    @Column(name = "STREET_NAME", size = 254)
    public Field<String> streetName = new Field<>(this);

    @Column(name = "LAT", size = 10, decimalPlaces = 6)
    public Field<Double> lat = new Field<>(this);

    @Column(name = "LON", size = 10, decimalPlaces = 6)
    public Field<Double> lon = new Field<>(this);

    public SharedLocation() {
    }

    public SharedLocation(Entity entity) {
        initEntity(entity);
    }
}
