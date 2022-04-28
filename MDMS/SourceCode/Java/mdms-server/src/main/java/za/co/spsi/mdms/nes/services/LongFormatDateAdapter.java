package za.co.spsi.mdms.nes.services;

import za.co.spsi.mdms.util.GenericDateAdapter;

/**
 * Created by jaspervdb on 2016/10/17.
 * 2016-10-09T06:01:00Z
 */
public class LongFormatDateAdapter extends GenericDateAdapter {

    // 2016-11-23 10:00:00.000
    public LongFormatDateAdapter() {
        super("yyyy-MM-dd HH:mm:ss.SSS");
    }

}
