package za.co.spsi.mdms.kamstrup.services;

import za.co.spsi.mdms.util.GenericDateAdapter;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Created by jaspervdb on 2016/10/17.
 */
public class LongFormatDateAdapter extends GenericDateAdapter {

    // 2015-04-16T12:06:36.1050492Z
    public static String pattern = "yyy-MM-dd'T'HH:mm:ss";

    public LongFormatDateAdapter() {
        super(date -> new Timestamp(new SimpleDateFormat(pattern).parse(
                date.substring(0,date.length()-9)).getTime()));
    }

    public String marshal(Timestamp date) throws Exception {
        return date != null?(new SimpleDateFormat(pattern).format(date)+"50492Z"):"";
    }


}
