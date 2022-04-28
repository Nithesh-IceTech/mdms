package za.co.spsi.mdms.kamstrup.services;

import za.co.spsi.mdms.util.GenericDateAdapter;

/**
 * Created by jaspervdb on 2016/10/17.
 * 2016-10-09T06:01:00Z
 */
public class ShortFormatDateAdapter extends GenericDateAdapter {

    public ShortFormatDateAdapter() {
        super("yyyy-MM-dd'T'HH:mm:ss'Z'");
    }

}
