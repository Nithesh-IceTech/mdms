package za.co.spsi.mdms.common.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import za.co.spsi.mdms.common.services.MeterDataService;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static za.co.spsi.mdms.common.services.MeterDataService.DEFAULT_TOUC;

/**
 * Created by jaspervdbijl on 2017/01/06.
 */
@Data
@JsonIgnoreProperties
public class MeterResultRequest {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");

    private String serialN[];
    private Integer tmzOffset = 120;
    private Integer interval = 0;
    private Integer period;
    private String fields[];
    private String touc = DEFAULT_TOUC;
    private Boolean removeTmz = false;

    private String from,to;

    public MeterDataService.Interval getInterval() {
        return MeterDataService.Interval.fromCode(interval != null?interval:0);
    }

    @JsonIgnore
    public Timestamp getTime(String time) {
        try {
            return new Timestamp(dateFormat.parse(time).getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @JsonIgnore
    public Timestamp getFromTime() {
        return getTime(from);
    }

    @JsonIgnore
    public Timestamp getToTime() {
        return getTime(to);
    }

    public Boolean getRemoveTmz() {
        return removeTmz != null?removeTmz:false;
    }
}
