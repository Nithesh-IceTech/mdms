package za.co.spsi.mdms.kamstrup.services.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import za.co.spsi.mdms.common.dao.MeterResultDataArray;
import za.co.spsi.mdms.common.db.MeterReadingEntity;
import za.co.spsi.mdms.common.db.survey.PecMeterEntity;
import za.co.spsi.mdms.common.db.survey.PecMeterReadingEntity;
import za.co.spsi.mdms.common.db.survey.PecMeterRegisterEntity;
import za.co.spsi.mdms.common.services.MeterDataService;
import za.co.spsi.mdms.elster.db.ElsterMeterEntity;
import za.co.spsi.mdms.generic.meter.db.GenericMeterEntity;
import za.co.spsi.mdms.kamstrup.db.KamstrupMeterEntity;
import za.co.spsi.mdms.nes.db.NESMeterEntity;
import za.co.spsi.mdms.util.PrepaidMeterFilterService;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.drivers.DriverFactory;
import za.co.spsi.toolkit.util.Assert;

import javax.annotation.Resource;
import javax.ejb.AccessTimeout;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.OptionalDouble;
import java.util.concurrent.TimeUnit;

import static za.co.spsi.toolkit.util.Util.getLocalDate;

@Data
@Log
@Singleton
@AccessTimeout(value=1800000)
public class MeterRegisterUpdateService {

    @Resource(mappedName = "java:/jdbc/mdms")
    private javax.sql.DataSource dataSource;

    @Inject
    private MeterDataService service;

    @Inject
    private PrepaidMeterFilterService filterService;

    private LocalDateTime today() {
        return LocalDateTime.now().truncatedTo(ChronoUnit.DAYS);
    }

    public MeterRegisterUpdateService() {
    }

    private Timestamp getToDate(Timestamp date, int sec) {
        Timestamp toDate = Timestamp.valueOf(date.toLocalDateTime().plusSeconds(sec));
        return toDate.toLocalDateTime().compareTo(today()) > 0 ? Timestamp.valueOf(today()) : toDate;
    }

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    @SneakyThrows
    public void update(Connection connection, PecMeterEntity meter, PecMeterRegisterEntity register, Timestamp lStart, Timestamp date, int tmz
            , boolean persistCalculated) {
        try {
            // assert that the meter is the same
            log.info(String.format("METER %s REGISTER %s. Start %s End %s", meter.meterN.get(), register.registerId.get()
                    , lStart.toString(), date.toString()));

//            registerId = 1.1.1.8.0.255 (Total kwhp)
            PecMeterReadingEntity reading = register.meterReading.getOne(dataSource);

            if (reading != null) {

                // TODO: DESK-1690 test push
                // Cycle Start Date:q:w
//                lStart = reading.prevReadingDate1.get();
                Timestamp to = getToDate(date,2); // Reading Date

                // IED-5241: MDMS - TOU Post-Paid Reading List Investigation
                Timestamp from = lStart != null ? lStart : // Cycle Start Date
                        Timestamp.valueOf(to.toLocalDateTime().minusDays(1));

                Assert.isTrue(service.isDataRequestWithinTimeScope(from, to, MeterDataService.Interval.HALF_HOURLY)
                        , "Meter data request for meter %s and register %s exceeds allowable time frame (90 days)"
                        , meter.meterN.get(), register.registerId.get());

                MeterResultDataArray dataSet = service.getDetailData(meter.meterN.get(), from, to, tmz
                        , MeterDataService.Interval.HALF_HOURLY, "series1", null, null, false);


                // persist the data set
                if (persistCalculated && !dataSet.isPersisted()) {
                    ObjectMapper mapper = new ObjectMapper();
                    FileOutputStream fos = new FileOutputStream(dateFormat.format(from) + "_" + dateFormat.format(to) + "_data.json");
                    mapper.writeValue(fos, dataSet);
                    fos.close();

                    dataSet.setPersisted(true);
                    dataSet.adjustTime((int) TimeUnit.MINUTES.toMillis(-tmz)).getCalculated()
                            .forEach(d -> MeterReadingEntity.generate(connection, d, meter, register, filterService));
                }

                dataSet = dataSet.filterTOU( register.getTouFrom() , register.getTouTo() , register.getTouDayOfWeek()
                        , getLocalDate(register.dateRx.get()), tmz);

                String mapped = register.getMappedName();

                if (!dataSet.isEmpty()) {
                    // filter for TOU
                    // either max kva or consumption
                    OptionalDouble maxValue = dataSet.getMaxValue(mapped);

                    reading.actualReading.set(maxValue.isPresent() ? maxValue.getAsDouble() : 0.0);

                    if (register.isMaxReading()) {

                        // could have been populated by the meter readings
                        reading.smartReading.set(maxValue.isPresent() ? maxValue.getAsDouble() : 0.0);

                    } else if (register.isKva()) {

                        reading.smartReading.set(dataSet.getMaxTotalKVA());

                    } else if (register.hasTimeOfUse() || register.isGridRegister()  || register.isGeneratorRegister()) {

                        reading.smartReading.set(dataSet.calculateConsumption(mapped + "Usage"));
                    }

                    if (register.isMaxReading()) {

                        reading.reading.set(reading.smartReading.get());

                    } else if (register.hasTimeOfUse() || register.isKva() || register.isGridRegister() || register.isGeneratorRegister()) {

                        reading.reading.set(reading.prevReading1.getNonNull() + reading.smartReading.get());

                    } else {

                        reading.reading.set(maxValue.isPresent() ? maxValue.getAsDouble() : 0.0);

                    }

                    //Set date with 1 second before save
                    Timestamp to_1 = getToDate(date,0);
                    reading.readingDate.set(to_1);

                    DataSourceDB.set(connection, reading);
                }

            }
        } catch (Exception ex) {
            log.warning(String.format("Error ON METER %s REGISTER %s. Start %s End %s"
                    , meter.meterN.get(), register.registerId.get(), lStart.toString(), date.toString()));
            throw new RuntimeException(ex);
        }
    }

    public static void main(String args[]) throws Exception {
        BigInteger i = BigInteger.valueOf(System.nanoTime()).multiply(BigInteger.valueOf((long) (Math.random() * Math.pow(10, 8)) + 1));
        System.out.println(i);
        String value = new String(java.util.Base64.getEncoder().encode(i.toByteArray()));
        System.out.println(value.substring(0, 12));
    }

    //    @PostConstruct
//    public void testTOU() {
//
//
//        log.info("Gateway Manager: ");
//
//    }

}


