package za.co.spsi.mdms.common.services;

import lombok.extern.slf4j.Slf4j;
import org.idempiere.webservice.client.exceptions.WebServiceException;
import org.springframework.util.CollectionUtils;
import za.co.spsi.mdms.common.dao.*;
import za.co.spsi.mdms.common.db.MeterReadingEntity;
import za.co.spsi.mdms.common.db.MeterReadingTsdbEntity;
import za.co.spsi.mdms.common.db.utility.IceTimeOfUseService;
import za.co.spsi.mdms.common.db.utility.IceTimeOfUseViewSyncService;
import za.co.spsi.mdms.common.error.RestException;
import za.co.spsi.mdms.elster.db.ElsterMeterEntity;
import za.co.spsi.mdms.generic.meter.db.GenericMeterEntity;
import za.co.spsi.mdms.kamstrup.db.KamstrupMeterEntity;
import za.co.spsi.mdms.nes.db.NESMeterEntity;
import za.co.spsi.mdms.utility.MDMSUtilityHelper;
import za.co.spsi.toolkit.crud.util.VaadinVersionUtil;
import za.co.spsi.toolkit.db.DSDB;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.drivers.Driver;
import za.co.spsi.toolkit.db.drivers.DriverFactory;
import za.co.spsi.toolkit.ee.properties.ConfValue;
import za.co.spsi.toolkit.ee.properties.TextFile;
import za.co.spsi.toolkit.util.Assert;
import za.co.spsi.toolkit.util.StringUtils;

import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static javax.ws.rs.core.Response.Status.NOT_ACCEPTABLE;

/**
 * Created by jaspervdbijl on 2017/01/06.
 */
@Singleton
@Slf4j
@Path("data")
@AccessTimeout(value=1800000)
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@TransactionManagement(value = TransactionManagementType.BEAN)
@DependsOn("MDMSUpgradeService")
public class MeterDataService {

    public enum Interval {
        HALF_HOURLY(0, 30),
        HOURLY(1, 60),
        DAILY(2, (int) TimeUnit.DAYS.toMinutes(1) ),
        MONTHLY(3, (int) TimeUnit.DAYS.toMinutes(31) );

        public int code, minutes;

        Interval(int code, int minutes) {
            this.code = code;
            this.minutes = minutes;
        }

        public boolean match(LocalDateTime time) {
            switch (this) {
                case HALF_HOURLY:
                    return time.getMinute() % 30 == 0;
                case HOURLY:
                    return time.getMinute() == 0;
                case DAILY:
                    return time.getMinute() == 0 && time.getHour() == 0;
                case MONTHLY:
                    return time.getDayOfMonth() == getLastDayInMonth(time) && time.getHour() == 23 && time.getMinute() == 30;
            }
            throw new UnsupportedOperationException("Type not supported");
        }

        public static Interval fromCode(Integer code) {
            Optional<Interval> interval = Arrays.stream(values()).filter(i -> i.code == (code != null ? code : 0)).findFirst();
            Assert.isTrue(interval.isPresent(), () -> {
                throw new RestException(NOT_ACCEPTABLE, "Unsupported interval type " + code);
            });
            return interval.get();
        }

        public static Interval fromMinutes(Integer min) {
            return Arrays.stream(values()).filter(i -> i.minutes == min).findFirst().get();
        }

    }

    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyyMMddHHmm");

    public static SimpleDateFormat entryDayFormat = new SimpleDateFormat(MeterReadingEntity.ENTRY_DAY_FORMAT);

    public static final String DEFAULT_TOUC = "0";
    public static final Boolean DEFAULT_REMOVE_TMZ = false;

    private Calendar calendar = Calendar.getInstance();

    @Resource(mappedName = "java:/jdbc/mdms")
    public javax.sql.DataSource dataSource;

    @Inject
    private MDMSUtilityHelper utilityHelper;

    @Inject
    VMMeterDataService vmMeterDataService;

    @Inject
    @ConfValue(value = "meter.scale.kumstrup", folder = "server")
    private int kumstrupScale = 0;

    @Inject
    @ConfValue(value = "meter.scale.nes", folder = "server")
    private int nesScale = 3;

    @Inject
    @ConfValue(value = "meter_data_service.max_range.days", folder = "server")
    private int maxDateRequestRangeDays = 90;

    @Inject
    @ConfValue(value = "meter_data_service.max_range.months", folder = "server")
    private int maxDateRequestRangeMonths = 13;

    @Inject
    public IceTimeOfUseViewSyncService touService;

    @Inject
    public IceTimeOfUseService touServiceTest;

    private DateTimeFormatter touFormat = DateTimeFormatter.ofPattern("HH:mm"), ymdFormat = DateTimeFormatter.ofPattern("yyMMdd");

    @Inject
    @TextFile("sql/smart_meter_reading.sql")
    private String smartMeterSqlUsageReading;

    @Inject
    @TextFile("sql/tsdb_meter_reading_with_gapfill.sql")
    private String tsdbMeterReadingWithGapFillSql;

    public MeterDataService() {

    }

    public static int getLastDayInMonth(LocalDateTime localDateTime) {

        int year = localDateTime.getYear();
        int month = localDateTime.getMonthValue() - 1;
        int day = localDateTime.getDayOfMonth();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        return days;
    }

    private MeterReadingEntity getPreviousReading(
            Connection connection,
            String where, java.util.Date from, java.util.Date to) throws SQLException {
        LocalDateTime fromTime = new Timestamp(from.getTime()).toLocalDateTime();

        // first select the oldest in the group
        LocalDateTime selectFrom = fromTime.toLocalDate().atStartOfDay().minusDays(1);
        Integer fromDate = Integer.parseInt(entryDayFormat.format(java.sql.Date.valueOf(selectFrom.toLocalDate())));
        Integer toDate = Integer.parseInt(entryDayFormat.format(to));

        String sql =
                DriverFactory.getDriver().limitSql(String.format(
                        "select * from meter_reading where %s and entry_time < ? and entry_day >= ?  and entry_day <= ? " +
                                "order by entry_time desc", where), 1);

        MeterReadingEntity lastReading = DataSourceDB.get(
                MeterReadingEntity.class, connection, sql, new Timestamp(from.getTime()), fromDate, toDate);

        // if not found, then do an expensive query
        if (lastReading == null) {
            sql =
                    DriverFactory.getDriver().limitSql(String.format(
                            "select * from meter_reading where %s and entry_time < ? order by entry_time desc", where), 1);
            lastReading = DataSourceDB.get(MeterReadingEntity.class, connection, sql, new Timestamp(from.getTime()));
        }

        return lastReading;
    }

    @GET
    public String getVersion(@Context HttpServletRequest request) {
        return VaadinVersionUtil.getVersion();
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Path("meterData")
    @POST
    public Response getMeterData(@Context HttpServletRequest request, MeterResultRequest msgRequest) {
        return getMeterData(msgRequest.getSerialN(), msgRequest.getFromTime(), msgRequest.getToTime(), msgRequest.getTmzOffset(),
                msgRequest.getInterval(), msgRequest.getTouc(), "0", msgRequest.getRemoveTmz());
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Path("multiviewData")
    @POST
    public Response getMultiViewData(@Context HttpServletRequest request, MeterResultRequest msgRequest) {
        return getMultiViewData(msgRequest.getSerialN(), msgRequest.getFields(), msgRequest.getFromTime(), msgRequest.getToTime(),
                msgRequest.getTmzOffset(), msgRequest.getInterval(), msgRequest.getTouc(),"0", msgRequest.getRemoveTmz());
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Path("summaryData")
    @POST
    public Response getMeterSummaryData(@Context HttpServletRequest request, MeterResultRequest msgRequest) {
        return getSummaryData(msgRequest.getSerialN()[0], msgRequest.getFromTime(), msgRequest.getToTime(), msgRequest.getFields()
                , msgRequest.getTmzOffset(), msgRequest.getInterval(), msgRequest.getRemoveTmz());
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Path("summaryData")
    @GET
    public Response getMeterSummaryData(@Context HttpServletRequest request,
                                        @NotNull @QueryParam("serialN") String serialN,
                                        @NotNull @QueryParam("from") DateTimeParam.DateTime from,
                                        @NotNull @QueryParam("to") DateTimeParam.DateTime to,
                                        @QueryParam("fields") String fields,
                                        @QueryParam("tmzOffset") @DefaultValue("120") Integer tmzOffset,
                                        @QueryParam("interval") @DefaultValue("0") Integer interval,
                                        @QueryParam("removeTmz") @DefaultValue("false") Boolean removeTmz) {
        return getSummaryData(serialN, from.get(), to.get(), fields != null ? fields.split("\\|") : new String[]{}
                , tmzOffset, Interval.fromCode(interval), removeTmz);
    }

    //    TODO: IED-3483: MDMS & AMI Portal: TOU Comparison Profile
    @Produces(MediaType.APPLICATION_JSON)
    @Path("summaryTouData")
    @GET
    public Response getTouSummaryData(@Context HttpServletRequest request,
                                      @NotNull @QueryParam("serialN") String serialN,
                                      @NotNull @QueryParam("from") DateTimeParam.DateTime from,
                                      @NotNull @QueryParam("to") DateTimeParam.DateTime to,
                                      @NotNull @QueryParam("tou1") @DefaultValue(DEFAULT_TOUC) String tou1,
                                      @NotNull @QueryParam("tou2") @DefaultValue(DEFAULT_TOUC) String tou2,
                                      @QueryParam("tmzOffset") @DefaultValue("120") Integer tmzOffset,
                                      @QueryParam("interval") @DefaultValue("0") Integer interval,
                                      @QueryParam("removeTmz") @DefaultValue("false") Boolean removeTmz) {
        return getSummaryTouData(serialN, from.get(), to.get(), tou1, tou2, tmzOffset, Interval.fromCode(interval), removeTmz);
    }

    public Response getSummaryTouData(String serialN, Timestamp timestamp, Timestamp timestamp1, String tou1, String tou2, Integer tmzOffset, Interval fromCode, Boolean removeTmz) {
        MeterResultDataArray data = getDetailData(serialN, timestamp, timestamp1, tmzOffset, fromCode, "series1", tou1, tou2, removeTmz);
        String fields[] = new String[0];

        if (data != null) {
            data = data.filter(fromCode).adjustNegative();
            return Response.status(Response.Status.OK).entity(new MeterResultDataSummaryResponse(data,
                    fields != null ? fields : new String[]{})).build();
        } else {
            return Response.status(Response.Status.OK).entity(MeterResultDataSummaryResponse.empty(fields)).build();
        }
    };

    public Response getSummaryData(String serialN, Date from, Date to, String fields[], int tmzOffset, Interval interval, Boolean removeTmz) {
        MeterResultDataArray data = getDetailData(serialN, from, to, tmzOffset, interval, "series1", null, null, removeTmz);
        if (data != null) {
            data = data.filter(interval).adjustNegative();
            return Response.status(Response.Status.OK).entity(new MeterResultDataSummaryResponse(data,
                    fields != null ? fields : new String[]{})).build();
        } else {
            return Response.status(Response.Status.OK).entity(MeterResultDataSummaryResponse.empty(fields)).build();
        }
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Path("compareMeter")
    @POST
    public Response getCompareMeterData(@Context HttpServletRequest request, MeterResultRequest msgRequest) {
        return getComparisonMeterData(request, msgRequest.getSerialN()[0], msgRequest.getFromTime(), msgRequest.getToTime(), msgRequest.getPeriod(),
                msgRequest.getTmzOffset(), msgRequest.getInterval(), msgRequest.getTouc(), msgRequest.getRemoveTmz());
    }

    private int getOffsetInMilli(int mins) {
        return (int) (TimeUnit.MINUTES.toMillis(mins));
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Path("meterData")
    @GET
    public Response getMeterData(@Context HttpServletRequest request,
                                 @NotNull @QueryParam("serialN") String serialN,
                                 @NotNull @QueryParam("from") DateTimeParam.DateTime from,
                                 @NotNull @QueryParam("to") DateTimeParam.DateTime to,
                                 @QueryParam("tmzOffset") @DefaultValue("120") Integer tmzOffset,
                                 @QueryParam("interval") @DefaultValue("0") Integer interval,
                                 @QueryParam("touc") @DefaultValue(DEFAULT_TOUC) String touc,
                                 @QueryParam("removeTmz") @DefaultValue("false") Boolean removeTmz) {
        return getMeterData(serialN.split("\\|"), from.get(), to.get(), tmzOffset, Interval.fromCode(interval), touc, "0", removeTmz);
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Path("summarizedTouData")
    @GET
    public Response getSummarizedTouData(@Context HttpServletRequest request,
                                         @NotNull @QueryParam("serialN") String serialN,
                                         @NotNull @QueryParam("from") DateTimeParam.DateTime from,
                                         @NotNull @QueryParam("to") DateTimeParam.DateTime to,
                                         @NotNull @QueryParam("tou1") @DefaultValue(DEFAULT_TOUC) String tou1,
                                         @NotNull @QueryParam("tou2") @DefaultValue(DEFAULT_TOUC) String tou2,
                                         @QueryParam("tmzOffset") @DefaultValue("120") Integer tmzOffset,
                                         @QueryParam("interval") @DefaultValue("0") Integer interval,
                                         @QueryParam("removeTmz") @DefaultValue("false") Boolean removeTmz) {
        return getSummarizedTouData(serialN, from.get(), to.get(), tou1, tou2, tmzOffset, Interval.fromCode(interval), removeTmz);
    }

    public Response getSummarizedTouData(String serialN, Timestamp timestamp, Timestamp timestamp1, String tou1, String tou2, Integer tmzOffset, Interval fromCode, Boolean removeTmz) {
        MeterResultDataArray data = getDetailData(serialN, timestamp, timestamp1, tmzOffset, fromCode, "series1", tou1, tou2, removeTmz);

        Map<String, MeterResultTouSummarizedRecord> meterResultTouSummarizedRecordMap =
                new HashMap<>();

        if (data != null) {

            // Tou1
            data.stream().forEach(p -> {
                String attributeCode = p.getTou1();

                // Import T1
                MeterResultTouSummarizedRecord recordP =
                        meterResultTouSummarizedRecordMap.get(attributeCode + TouGroup.P.code);

                if(recordP != null) {
                    recordP.setTouTariff1kWhTotal(recordP.getTouTariff1kWhTotal() + p.getTotalKwhP());
                    recordP.setTariff1ImportExportGroup(TierGroup.T1P);

                } else {
                    recordP = new MeterResultTouSummarizedRecord();
                    recordP.setAttributeCode(attributeCode + TouGroup.P.code);
                    recordP.setTouTariff1kWhTotal(p.getTotalKwhP());
                    recordP.setTariff1ImportExportGroup(TierGroup.T1P);
                    meterResultTouSummarizedRecordMap.put(recordP.getAttributeCode(), recordP);
                }

                //Export T1
                MeterResultTouSummarizedRecord recordN =
                        meterResultTouSummarizedRecordMap.get(attributeCode + TouGroup.N.code);

                if(recordN != null) {
                    recordN.setTouTariff1kWhTotal(recordN.getTouTariff1kWhTotal() + p.getTotalKwhN());
                    recordN.setTariff1ImportExportGroup(TierGroup.T1N);

                } else {
                    recordN = new MeterResultTouSummarizedRecord();
                    recordN.setAttributeCode(attributeCode + TouGroup.N.code);
                    recordN.setTouTariff1kWhTotal(p.getTotalKwhN());
                    recordN.setTariff1ImportExportGroup(TierGroup.T1N);
                    meterResultTouSummarizedRecordMap.put(recordN.getAttributeCode(), recordN);
                }

                // Import T2
                attributeCode = p.getTou2();
                recordP = meterResultTouSummarizedRecordMap.get(attributeCode + TouGroup.P.code);

                if(recordP != null) {
                    recordP.setTouTariff2kWhTotal(recordP.getTouTariff2kWhTotal() + p.getTotalKwhP());
                    recordP.setTariff2ImportExportGroup(TierGroup.T2P);
                } else {
                    recordP = new MeterResultTouSummarizedRecord();
                    recordP.setAttributeCode(attributeCode + "_P");
                    recordP.setTouTariff2kWhTotal(p.getTotalKwhP());
                    recordP.setTariff2ImportExportGroup(TierGroup.T2P);
                    meterResultTouSummarizedRecordMap.put(recordP.getAttributeCode(), recordP);
                }

                // Export T2
                recordN = meterResultTouSummarizedRecordMap.get(attributeCode + TouGroup.N.code);

                if(recordN != null) {
                    recordN.setTouTariff2kWhTotal(recordN.getTouTariff2kWhTotal() + p.getTotalKwhN());
                    recordN.setTariff2ImportExportGroup(TierGroup.T2N);

                } else {
                    recordN = new MeterResultTouSummarizedRecord();
                    recordN.setAttributeCode(attributeCode + "_N");
                    recordN.setTariff2ImportExportGroup(TierGroup.T2N);
                    recordP.setTouTariff2kWhTotal(p.getTotalKwhN());
                    meterResultTouSummarizedRecordMap.put(recordN.getAttributeCode(), recordN);
                }
            });

            MeterResultTouSummarizedResponse response = new MeterResultTouSummarizedResponse();

            for (Map.Entry<String, MeterResultTouSummarizedRecord> entry
                    : meterResultTouSummarizedRecordMap.entrySet()) {
                response
                        .getMeterResultTouSummarizedRecordList().add(entry.getValue());
            }

            response.updatePercentages();
            response.updateDescriptions();

            return Response
                    .status(Response.Status.OK)
                    .entity(response)
                    .build();

        } else {
            return Response
                    .status(Response.Status.OK)
                    .entity(Void.class)
                    .build();
        }
    };

//    TODO: IED-3483: MDMS & AMI Portal: TOU Comparison Profile

    /**
     * IED-3483: MDMS & AMI Portal: TOU Comparison Profile
     * @param request
     * @param serialN
     * @param from
     * @param to
     * @param tmzOffset
     * @param interval
     * @param tou1
     * @param tou2
     * @param removeTmz
     * @return
     */
    @Produces(MediaType.APPLICATION_JSON)
    @Path("touCompareData")
    @GET
    public Response getTouCompareData(@Context HttpServletRequest request,
                                      @NotNull @QueryParam("serialN") String serialN,
                                      @NotNull @QueryParam("from") DateTimeParam.DateTime from,
                                      @NotNull @QueryParam("to") DateTimeParam.DateTime to,
                                      @QueryParam("tmzOffset") @DefaultValue("120") Integer tmzOffset,
                                      @QueryParam("interval") @DefaultValue("0") Integer interval,
                                      @NotNull @QueryParam("tou1") String tou1,
                                      @NotNull @QueryParam("tou2") String tou2,
                                      @QueryParam("removeTmz") @DefaultValue("false") Boolean removeTmz) {
        return getToucMeterData(serialN.split("\\|"), from.get(), to.get(), tmzOffset, Interval.fromCode(interval), tou1, tou2, removeTmz);
    }

    @Produces(MediaType.APPLICATION_JSON)
    @Path("multiviewData")
    @GET
    public Response getMultiViewData(@Context HttpServletRequest request,
                                     @NotNull @QueryParam("serialN") String serialN,
                                     @NotNull @QueryParam("fields") String field,
                                     @NotNull @QueryParam("from") DateTimeParam.DateTime from,
                                     @NotNull @QueryParam("to") DateTimeParam.DateTime to,
                                     @QueryParam("tmzOffset") @DefaultValue("120") Integer tmzOffset,
                                     @QueryParam("interval") @DefaultValue("0") Integer interval,
                                     @QueryParam("touc") @DefaultValue(DEFAULT_TOUC) String touc,
                                     @QueryParam("removeTmz") @DefaultValue("false") Boolean removeTmz) {
        return getMultiViewData(serialN.split(","), field.split(","), from.get(), to.get(), tmzOffset
                , Interval.fromCode(interval), touc, "0", removeTmz);
    }

    public Response getMultiViewData(String serialN[], String field[], Date from, Date to, Integer tmzOffset,
                                     Interval interval, String touc, String touc2, Boolean removeTmz) {
        Assert.isTrue(serialN.length == field.length, () -> {
            throw new RestException(NOT_ACCEPTABLE, "Amount of Serial numbers and field's must be the same");
        });

        List<String> serials = Arrays.asList(serialN);
        List<MultiViewMeterResultData> dataSet = getMeterDetailData(serialN, from, to, tmzOffset, interval, touc, touc2, removeTmz).stream().map(
                        d -> new MultiViewMeterResultData(d, field[serials.indexOf(d.getSerialN())])).
                collect(Collectors.toCollection(ArrayList::new));
        return dataSet.isEmpty() ?
                Response.status(Response.Status.BAD_REQUEST).entity(Void.class).build() :

                Response.status(Response.Status.OK).entity(new MultiViewMeterResultDataList(dataSet)).build();
    }

    /**
     *
     * @param request
     * @param serialN
     * @param from
     * @param to
     * @param period
     * @param tmzOffset
     * @param interval
     * @param touc
     * @param removeTmz
     * @return
     */
    @Produces(MediaType.APPLICATION_JSON)
    @Path("compareMeter")
    @GET
    public Response getComparisonMeterData(@Context HttpServletRequest request,
                                           @NotNull @QueryParam("serialN") String serialN,
                                           @NotNull @QueryParam("from") DateTimeParam.DateTime from,
                                           @NotNull @QueryParam("to") DateTimeParam.DateTime to,
                                           @NotNull @QueryParam("period") Integer period,
                                           @QueryParam("tmzOffset") @DefaultValue("120") Integer tmzOffset,
                                           @QueryParam("interval") @DefaultValue("0") Integer interval,
                                           @QueryParam("touc") @DefaultValue(DEFAULT_TOUC) String touc,
                                           @QueryParam("removeTmz") @DefaultValue("false") Boolean removeTmz) {
        return getComparisonMeterData(request, serialN, from.get(), to.get(), period, tmzOffset, Interval.fromCode(interval), touc, removeTmz);
    }

    public Response getComparisonMeterData(@Context HttpServletRequest request, String serialN, Timestamp from, Timestamp to, Integer pe,
                                           Integer tmzOffset, Interval interval, String touc, Boolean removeTmz) {

        // IFS-1389
        final Integer period = pe * -1;

        MeterResultDataArray dataSet1 = getDetailData(serialN, from, to, tmzOffset, interval, "series1", touc, null ,removeTmz);

        MeterResultDataArray dataSet2 = getDetailData(serialN,
                Timestamp.valueOf(from.toLocalDateTime().plusDays(period)),
                Timestamp.valueOf(to.toLocalDateTime().plusDays(period)), tmzOffset, interval, "series2", touc, null , removeTmz);

        if (dataSet1 != null && dataSet2 != null) {
            dataSet1.stream().forEach(d -> d.setRecordType("Primary"));
            dataSet2.stream().forEach(d -> d.setPlotEntryTime(Timestamp.valueOf(d.getEntryTime().toLocalDateTime().minusDays(period))));
            dataSet2.stream().forEach(d -> d.setRecordType("Compared"));
            // insert the blanks
            dataSet1.stream().forEach(d -> {
                Timestamp adjusted = Timestamp.valueOf(d.getEntryTime().toLocalDateTime().plusDays(period));
                if (!dataSet2.get(adjusted).isPresent()) {
                    dataSet2.add(new MeterResultData(adjusted));
                }
            });

            MeterResultDataArray compared = dataSet1.stream().map(d -> new ComparedMeterResultData(d).
                    init(d, dataSet2.get(Timestamp.valueOf(d.getEntryTime().toLocalDateTime().plusDays(period)))
                            .get())).collect(Collectors.toCollection(MeterResultDataArray::new));
            compared.addAll(dataSet2);

            return Response.status(Response.Status.OK).entity(
                    new MeterResultDataResponse(compared.filter(interval).adjustNegative())).build();
        } else {
            return Response.status(Response.Status.OK).entity(MeterResultDataResponse.emptySeries()).build();
        }
    }

    /**
     * request is received in Local time, and converted to GMT+0
     *
     * @param serialN
     * @param from
     * @param to
     * @param tmzOffset
     * @param interval
     * @return
     */
    public Response getMeterData(String serialN[], java.util.Date from, java.util.Date to, Integer tmzOffset, Interval interval
            , String touc1, String touc2, Boolean removeTmz) {
        MeterResultDataArray dataArray = getMeterDetailData(serialN, from, to, tmzOffset, interval, touc1, touc2, removeTmz);
        return dataArray.isEmpty() ?
                Response.status(Response.Status.OK).entity(MeterResultDataResponse.empty()).build() :
                Response.status(Response.Status.OK).entity(new MeterResultDataResponse(dataArray)).build();
    }

    /**
     * IED-3483: MDMS & AMI Portal: TOU Comparison Profile
     * @param serialN
     * @param from
     * @param to
     * @param tmzOffset
     * @param interval
     * @param touc1
     * @param touc2
     * @param removeTmz
     * @return
     */
    public Response getToucMeterData(String serialN[], java.util.Date from, java.util.Date to, Integer tmzOffset, Interval interval
            , String touc1, String touc2, Boolean removeTmz) {
        MeterResultDataArray dataArray = getMeterDetailData(serialN, from, to, tmzOffset, interval, touc1, touc2, removeTmz);
        return dataArray.isEmpty() ?
                Response.status(Response.Status.OK).entity(MeterResultDataResponse.empty()).build() :
                Response.status(Response.Status.OK).entity(new MeterResultDataResponse(dataArray)).build();
    }

    public MeterResultDataArray getMeterDetailData(String serialN[], java.util.Date from, java.util.Date to, Integer tmzOffset, Interval interval
            , String touc1, String touc2, boolean removeTmz) {
        MeterResultDataArray dataArray = new MeterResultDataArray();
        final AtomicInteger cnt = new AtomicInteger(0);
        Arrays.stream(serialN).forEach(s -> {
            MeterResultDataArray data = getDetailData(s, from, to, tmzOffset, interval, "series" + cnt.incrementAndGet(), touc1, touc2, removeTmz);
            if (data != null) {
                dataArray.addAll(data);
            }
        });
        return dataArray.filter(interval).adjustNegative();
    }

    public MeterResultDataArray getDetailData(String serialN, Date from, Date to, int tmzOffset, Interval interval, String series
            , String touc1, String touc2, boolean removeTmz, boolean forceRead) {
        return getDetailDataTmzMilli(serialN, from, to, getOffsetInMilli(tmzOffset), interval, series, touc1, touc2, removeTmz, forceRead);
    }

    public MeterResultDataArray getDetailData(String serialN, Date from, Date to, int tmzOffset, Interval interval, String series
            , String touc1, String touc2, boolean removeTmz) {
        return getDetailDataTmzMilli(serialN, from, to, getOffsetInMilli(tmzOffset), interval, series, touc1, touc2, removeTmz);
    }

    /**
     * is request within allowable time frame
     *
     * @param from
     * @param to
     * @return
     */
    public boolean isDataRequestWithinTimeScope(Date from, Date to, Interval interval) {
        if(interval == MeterDataService.Interval.MONTHLY) {
            return getNumberOfMonths( from, to, 0 ) <= (maxDateRequestRangeMonths);
        } else if(interval != MeterDataService.Interval.MONTHLY) {
            return to.getTime() - from.getTime() < TimeUnit.DAYS.toMillis(maxDateRequestRangeDays);
        }
        return false;
    }

    private MeterResultData toMeterResult(MeterReadingEntity entity, int tmzOffset, String serialN, String series, String touType1, String touType2) {
        return new MeterResultData(touService,
                tmzOffset, serialN,
                entity,
                entity.kamMeterId.get() != null ? 0
                        : entity.nesMeterId.get() != null ? 1
                        : entity.elsterMeterId.get() != null ? 2
                        : 3
                , series, touType1, touType2);
    }

    private String getMeterFilterSql(KamstrupMeterEntity kamMeter, GenericMeterEntity genericMeterEntity
            , ElsterMeterEntity elsMeter, NESMeterEntity nesMeter) {
        return kamMeter != null ? String.format("kam_meter_id = '%s'", kamMeter.meterId.get()) :
                genericMeterEntity != null ? String.format("generic_meter_id = '%s'", genericMeterEntity.genericMeterId.get()) :
                        elsMeter != null ? String.format("els_meter_id = '%s'", elsMeter.meterId.get()) :
                                String.format("nes_meter_id in (select meter_id from nes_meter where serial_n = '%s')", nesMeter.serialN.get());
    }


    public MeterResultDataArray getDetailDataTmzMilli(String serialN, Date from, Date to, int tmzOffset, Interval interval, String series, String touc1, String touc2, boolean removeTmz) {
        return getDetailDataTmzMilli(serialN, from, to, tmzOffset, interval, series, touc1, touc2, removeTmz, false);
    }

    //    @Lock(LockType.READ)
    public MeterResultDataArray getDetailDataTmzMilli(String serialN, Date from, Date to, int tmzOffset, Interval interval, String series, String touc1, String touc2, boolean removeTmz, boolean forceRead) {
        // adjust from and to to local time

        Driver driver = DriverFactory.getDriver();
        Boolean isOracle = driver.getClass().getName().contains("Oracle");

        MeterResultDataArray temp = null;

        if(interval == MeterDataService.Interval.MONTHLY) {

            LocalDateTime fromLocalDT = Instant.ofEpochMilli(from.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();

            int fromYear = fromLocalDT.getYear();
            int fromMonth = fromLocalDT.getMonthValue();
            int fromDay = 1;
            int fromHour = 0;
            int fromMinute = 0;

            fromLocalDT = LocalDateTime.of(fromYear, fromMonth, fromDay, fromHour, fromMinute);
            from = Date.from( fromLocalDT.atZone( ZoneId.systemDefault()).toInstant() );

            LocalDateTime toLocalDT = Instant.ofEpochMilli(to.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
            toLocalDT = toLocalDT.truncatedTo( ChronoUnit.DAYS ).minusDays( toLocalDT.getDayOfMonth() )
                    .plusHours(23).plusMinutes(59).plusSeconds(59);
            to = Date.from( toLocalDT.atZone( ZoneId.systemDefault()).toInstant() );

            from = new Date(from.getTime() - tmzOffset);
            to   = new Date(to.getTime() - tmzOffset);

        } else {

            from = new Date(from.getTime() - tmzOffset);
            to   = new Date(to.getTime() - tmzOffset);

        }

        Assert.isTrue(forceRead || isDataRequestWithinTimeScope(from, to, interval), () -> {
            if(interval == MeterDataService.Interval.MONTHLY ) {
                throw new RestException(NOT_ACCEPTABLE, "Range requested exceeds max months of " + maxDateRequestRangeMonths);
            } else if(interval != MeterDataService.Interval.MONTHLY) {
                throw new RestException(NOT_ACCEPTABLE, "Range requested exceeds max days of " + maxDateRequestRangeDays);
            }
        });

        MeterResultDataArray dataSet = new MeterResultDataArray();

        // find the meter
        KamstrupMeterEntity kamMeter = DataSourceDB.getFromSet(dataSource, (KamstrupMeterEntity) new KamstrupMeterEntity().serialN.set(serialN));

        String query = String.format("select GENERIC_METER.* from GENERIC_METER where GENERIC_METER.METER_SERIAL_N = ? and GENERIC_METER.LIVE = %s",
                DriverFactory.getDriver().boolToNumber(true));
        GenericMeterEntity genericMeterEntity = DataSourceDB.get(GenericMeterEntity.class, dataSource, query, serialN);

        ElsterMeterEntity elsMeter = kamMeter == null ? DataSourceDB.getFromSet(dataSource, (ElsterMeterEntity) new ElsterMeterEntity().serialN.set(serialN)) : null;
        NESMeterEntity nesMeter = kamMeter == null && elsMeter == null ? DataSourceDB.getFromSet(dataSource, (NESMeterEntity) new NESMeterEntity().serialN.set(serialN)) : null;

        if (kamMeter != null || nesMeter != null || elsMeter != null || genericMeterEntity != null) {
            try {
                try (Connection connection = dataSource.getConnection()) {

                    String fromDateStr = driver.toDate( new Timestamp(from.getTime()).toLocalDateTime() );
                    String toDateStr   = driver.toDate( new Timestamp(to.getTime()).toLocalDateTime() );

                    String sqlQuery = "select * from meter_reading where %s and entry_time >= %s and entry_time < %s order by entry_time asc";
                    String whereSql = getMeterFilterSql(kamMeter, genericMeterEntity, elsMeter, nesMeter);
                    String meterIdField = whereSql.split(" in ")[0].replace(" ", "");
                    meterIdField = StringUtils.isEmpty(meterIdField) ? meterIdField : meterIdField.split("=")[0];
                    String weekDayName = "";

                    if(isOracle) { // Oracle DB

                        sqlQuery = String.format(sqlQuery, whereSql, fromDateStr, toDateStr);

                        for (MeterReadingEntity reading : new DataSourceDB<>(MeterReadingEntity.class).getAll(connection, sqlQuery)) {

                            // TODO: IED-3483: MDMS & AMI Portal: TOU Comparison Profile
                            /** Calculate the day of week number (1,2,3,...7) from the meter reading as well as the name of that day (Sunday, Weekdays, Saturday)*/
                            Calendar c = Calendar.getInstance();

                            // Use the reading entry time to calculate the day of the week the reading has been taken on
                            c.setTime(reading.entryTime.get());
                            c.add(Calendar.MILLISECOND,tmzOffset);

                            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
                            weekDayName = getWeekDayName(dayOfWeek);

                            String touType1 = "0";
                            String touType2 = "0";

                            if (touc1 != null && !touc1.isEmpty()) {
                                // Eskom - Miniflex => plvID, versionID = List Box 1
                                touType1 = touServiceTest.getTouComparisonTimeSlotType(Integer.parseInt(touc1),weekDayName, reading.entryTime.get()); // OP
                            }

                            if (touc2 != null && !touc2.isEmpty()) {
                                // Tshwane - Miniflex => plvID, versionID = List Box 2
                                touType2 = touServiceTest.getTouComparisonTimeSlotType(Integer.parseInt(touc2),weekDayName, reading.entryTime.get()); // P
                            }

                            MeterResultData point = toMeterResult(reading, tmzOffset, serialN, series, touType1, touType2);

                            if (!dataSet.contain(point.getEntryTime())) {
                                dataSet.add(point);
                            }
                        }

                        if(kamMeter != null) {
                            if(!kamMeter.isWater()) {
                                dataSet = dataSet.excludeNullReadings(true, false, false, false, false, false);
                            }
                        } else if(genericMeterEntity != null) {
                            if(!genericMeterEntity.isWater()) {
                                dataSet = dataSet.excludeNullReadings(true, false, false, false, false, false);
                            }
                        } else if(nesMeter != null || elsMeter != null) {
                            dataSet = dataSet.excludeNullReadings(true, false, false, false, false, false);
                        }

                        dataSet = dataSet.normalize(interval.minutes);

                    } else { // TimescaleDB

                        sqlQuery = String.format(tsdbMeterReadingWithGapFillSql,
                                meterIdField, getTsdbTimeInterval(interval), fromDateStr, toDateStr, whereSql, fromDateStr, toDateStr, meterIdField);

                        List<MeterReadingTsdbEntity> meterReadingTsdbList =
                                new DSDB(new MeterReadingTsdbEntity()).getAll(connection,sqlQuery).getAllAsList();

                        if(!CollectionUtils.isEmpty(meterReadingTsdbList)) {

                            for (MeterReadingTsdbEntity readingTsdb : meterReadingTsdbList) {

                                String touType1 = "0";
                                String touType2 = "0";

                                // TODO: IED-3483: MDMS & AMI Portal: TOU Comparison Profile
                                if (touc1 != null && !touc1.isEmpty()) {
                                    touType1 = touServiceTest.getTouComparisonTimeSlotType(Integer.parseInt(touc1), weekDayName, readingTsdb.entry_time.get()); // OP
                                }

                                if (touc2 != null && !touc2.isEmpty()) {
                                    touType2 = touServiceTest.getTouComparisonTimeSlotType(Integer.parseInt(touc2), weekDayName, readingTsdb.entry_time.get()); // P
                                }

                                MeterResultData point = toMeterResult(MeterReadingEntity.convertFromTsdb(readingTsdb) , tmzOffset, serialN, series, touType1, touType2);

                                if (!dataSet.contain(point.getEntryTime())) {
                                    dataSet.add(point);
                                }

                            }

                        }

                    }

                }

                MeterResultDataArray updatedCalcs = dataSet.updateCalcs().subset(from, to, tmzOffset);

                MeterResultDataArray statsFilter =
                        CollectionUtils.isEmpty(updatedCalcs) ? updatedCalcs :
                                updatedCalcs.statisticalFilter( updatedCalcs,
                                        (kamMeter != null ?
                                                kamMeter.isWater() : false ||
                                                genericMeterEntity != null ?
                                                genericMeterEntity.meterType.get().equalsIgnoreCase("water") : false));
                temp = statsFilter;

            } catch (SQLException | WebServiceException sqle) {
                throw new RuntimeException(sqle);
            }

        } else if (vmMeterDataService.checkMeter(serialN)) {

            temp = vmMeterDataService.getDetailDataTmzMilli(this, serialN,
                    new Date(from.getTime() + tmzOffset), new Date(to.getTime() + tmzOffset), interval, tmzOffset, series, touc1, touc2,false);


        }

        MeterResultDataArray data = temp == null || temp.isEmpty() ?
                getZeroPad(new Timestamp(from.getTime()).toLocalDateTime(),
                        new Timestamp(to.getTime()).toLocalDateTime(),
                        interval, serialN, series, false) : temp;

        return removeTmz ? data.adjustTime(tmzOffset) : data;
    }

    public MeterResultDataArray getZeroPad(LocalDateTime from, LocalDateTime to, Interval interval, String serialN, String series, Boolean onlyToCurrent) {
        MeterResultDataArray data = new MeterResultDataArray();
        if (onlyToCurrent && to.isAfter(LocalDateTime.now()) && !from.isAfter(LocalDateTime.now())) {
            to = LocalDateTime.now();
        }
        while (from.compareTo(to) < 0) {
            data.add(new MeterResultData(Timestamp.valueOf(from)).zero());
            data.get(data.size() - 1).setSeries(series);
            data.get(data.size() - 1).setSerialN(serialN);
            from = from.plusMinutes(interval.minutes);
        }
        return data;
    }

    public int getNumberOfMonths(Date from, Date to, int tmzOffset) {

        from =  new Date( from.getTime() );
        to =  new Date( to.getTime() );

        int fromYearValue = LocalDateTime.ofEpochSecond( from.toInstant().getEpochSecond(), 0, ZoneOffset.ofHours(0)).getYear();
        int fromMonthValue = LocalDateTime.ofEpochSecond( from.toInstant().getEpochSecond(), 0, ZoneOffset.ofHours(0)).getMonthValue();
        int toYearValue = LocalDateTime.ofEpochSecond( to.toInstant().getEpochSecond(), 0, ZoneOffset.ofHours(0)).minusMonths(1).getYear();
        int toMonthValue = LocalDateTime.ofEpochSecond( to.toInstant().getEpochSecond(), 0, ZoneOffset.ofHours(0)).minusMonths(1).getMonthValue();

        int numberOfMonths = 0;

        if( fromYearValue == toYearValue ) {
            numberOfMonths = toMonthValue - fromMonthValue + 1;
        } else {
            numberOfMonths = ( 12 - fromMonthValue + 1 ) + toMonthValue;
        }

        return numberOfMonths;
    }

    /**
     * This method receives the day of week number (1-7) and returns the name of that week day number. eg 1 = Sunday, 2= Weekdays, 7= Saturday .....
     * @param dayOfWeekNumber
     * @return
     */
    public String getWeekDayName(Integer dayOfWeekNumber) {
        switch (dayOfWeekNumber)
        {
            case 1:
                return "Sunday"
                        ;
            case 2: // Monday
                return "Weekdays"
                        ;
            case 3: // Tuesday
                return "Weekdays"
                        ;
            case 4: // Wednesday
                return "Weekdays"
                        ;
            case 5: // Thursday
                return "Weekdays"
                        ;
            case 6: // Friday
                return "Weekdays"
                        ;
            case 7:
                return "Saturday"
                        ;
            default:
                return "Unknown"
                        ;
        }
    }

    /**
     * This method are just being used by the unit test call to get the new TOU comparison info.
     * @param
     * @return
     */
    public String testCallToDb() throws WebServiceException, SQLException {

        Timestamp meterReadingEntryTime = Timestamp.valueOf(LocalDateTime.of(LocalDate.of(2021, 07, 16), LocalTime.of(7, 00, 00)));

        String touTypeX = touServiceTest.getTouComparisonTimeSlotType(1000002, "Saturday", meterReadingEntryTime);

        return "Test ran " + touTypeX ;
    };

    public String getTsdbTimeInterval(Interval interval) {
        String timeInterval = null;

        Driver driver = DriverFactory.getDriver();

        switch(interval.name()) {
            case "HALF_HOURLY":
                timeInterval = driver.getInterval(30,"minute");
                break;
            case "HOURLY":
                timeInterval = driver.getInterval(60,"minute");
                break;
            case "DAILY":
                timeInterval = driver.getInterval(24,"hour");
                break;
            case "MONTHLY":
                timeInterval = driver.getInterval(1,"month");
                break;
            default:
                timeInterval = driver.getInterval(30,"minute");
        }

        return timeInterval;
    }


}
