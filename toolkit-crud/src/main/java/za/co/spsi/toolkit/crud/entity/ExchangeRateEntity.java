package za.co.spsi.toolkit.crud.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.entity.Field;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;


@Table(version = 0)
public class ExchangeRateEntity extends EntityDB {

    private static boolean iceEngineDateIsString = false;
    private static String dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    @Id(uuid = true)
    @Column(name = "EXCHANGE_RATE_ID", size = 50, notNull = true)
    public Field<String> exchangeRateId = new Field<>(this);

    @Column(name = "AGENCY_ID", size = 10)
    public Field<Integer> agencyId = new Field<>(this);

    @Column(name = "BASE_CURRENCY_CD", size = 5)
    public Field<String> baseCurrencyCd = new Field<>(this);

    @Column(name = "TARGET_CURRENCY_CD", size = 5)
    public Field<String> targetCurrencyCd = new Field<>(this);

    @Column(name = "EXCHANGE_RATE")
    public Field<Double> exchangeRate = new Field<>(this);

    @Column(name = "FROM_D")
    public Field<Timestamp> fromD = new Field<Timestamp>(this);

    @Column(name = "TO_D")
    public Field<Timestamp> toD = new Field<Timestamp>(this);

    public ExchangeRateEntity() {
        super("EXCHANGE_RATE");
    }

    @JsonProperty("EXCHANGE_RATE_ID")
    public void setExchangeRateId(String exchangeRateId) {
        this.exchangeRateId.set(exchangeRateId);
    }

    @JsonProperty("AGENCY_ID")
    public void setAgencyId(String agencyId) {
        this.agencyId.set(new Integer(agencyId));
    }

    @JsonProperty("BASE_CURRENCY_CD")
    public void setBaseCurrencyCd(String baseCurrencyCd) {
        this.baseCurrencyCd.set(baseCurrencyCd);
    }

    @JsonProperty("TARGET_CURRENCY_CD")
    public void setTargetCurrencyCd(String targetCurrencyCd) {
        this.targetCurrencyCd.set(targetCurrencyCd);
    }

    @JsonProperty("EXCHANGE_RATE")
    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate.set(new Double(exchangeRate));
    }

    public static void setDateIsString(boolean dateIsString) {
        iceEngineDateIsString = dateIsString;
    }

    public static void setDateFormat(String format) {
        dateFormat = format;
    }

    private Timestamp parseDateFromISO8601String(String dateStr) throws ParseException {
        DateFormat df = new SimpleDateFormat(dateFormat);
        return new Timestamp(df.parse(dateStr).getTime());
    }

    @JsonProperty("FROM_D")
    public void setFromD(String fromD) throws ParseException {
        if (fromD != null) {
            this.fromD.set(iceEngineDateIsString ? parseDateFromISO8601String(fromD) : new Timestamp(new Long(fromD)));
        }
    }

    @JsonProperty("TO_D")
    public void setToD(String toD) throws ParseException {
        if (toD != null) {
            this.toD.set(iceEngineDateIsString ? parseDateFromISO8601String(toD) : new Timestamp(new Long(toD)));
        }
    }
}
