package za.co.spsi.mdms.util;

import za.co.spsi.toolkit.util.StringUtils;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by jaspervdb on 2016/10/17.
 */
public class GenericDateAdapter extends XmlAdapter<String, Timestamp> {

    // 2015-04-16T12:06:36.1050492Z
    private String pattern;
    private ConvertCallback convertCallback;

    public GenericDateAdapter(String pattern) {
        this.pattern = pattern;
    }

    public GenericDateAdapter(ConvertCallback convertCallback) {
        this.convertCallback = convertCallback;
    }

    public String marshal(Timestamp date) throws Exception {
        return new SimpleDateFormat(pattern).format(date);
    }

    public Timestamp unmarshal(String dateString) throws Exception {
        return convert(dateString);
    }


    public Timestamp convert(String dateString) throws ParseException {
        return !StringUtils.isEmpty(dateString) ?
                convertCallback != null?convertCallback.convert(dateString):
                new Timestamp(new SimpleDateFormat(pattern).parse(dateString).getTime()) : null;
    }

    public interface ConvertCallback {
        Timestamp convert(String date) throws ParseException;
    }
}
