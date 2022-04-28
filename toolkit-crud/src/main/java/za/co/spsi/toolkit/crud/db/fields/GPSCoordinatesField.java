package za.co.spsi.toolkit.crud.db.fields;

import za.co.spsi.toolkit.entity.Entity;
import za.co.spsi.toolkit.entity.Field;

import java.text.DecimalFormat;

/**
 * Created by jaspervdbijl on 2016/12/08.
 */
public class GPSCoordinatesField extends Field<String> {


    public GPSCoordinatesField(Entity entity) {
        super(entity);
    }

    /**
     *
     * @return the lat and then the lon
     */
    public double[] getCoordinates() {
        return get() != null && get().indexOf(",")!=-1?new double[]{
                Double.parseDouble(get().split(",")[0]),Double.parseDouble(get().split(",")[1])}:null;
    }

}
