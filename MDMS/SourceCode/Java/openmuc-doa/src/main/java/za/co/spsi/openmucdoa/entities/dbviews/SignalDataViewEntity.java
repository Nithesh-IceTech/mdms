package za.co.spsi.openmucdoa.entities.dbviews;

import lombok.Data;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Data
@Entity
@Table(name = "signal_data_view")
public class SignalDataViewEntity {

    @Id
    private Timestamp entrytime;

    @Column(name = "ied_name")
    private String iedName;

    @Column(name = "channel_name")
    private String channelName;

    @Column(name = "description")
    private String channelDescription;

    @Column(name = "double_value")
    private Double doubleValue;

    public String getEntryTimeFormatted() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime entryTimeLocal = getEntrytime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return  dateTimeFormatter.format( entryTimeLocal );
    }

    public Double getDoubleValueFormatted() {
        String doubleValStr = getDoubleValue() != null ? String.format("%.2f", getDoubleValue()).replace(",", ".") : "0.0";
        return StringUtils.isEmpty(doubleValStr) ? Double.parseDouble("0.0") : Double.parseDouble(doubleValStr) ;
    }

}