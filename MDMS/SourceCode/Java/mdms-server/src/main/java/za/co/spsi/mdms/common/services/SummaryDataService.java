package za.co.spsi.mdms.common.services;

import lombok.Data;
import za.co.spsi.mdms.common.dao.MeterResultSummaryRecord;
import za.co.spsi.mdms.common.dao.MeterResultSummaryRecords;
import za.co.spsi.mdms.common.properties.config.PropertiesConfig;

import javax.ejb.DependsOn;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;
import static za.co.spsi.mdms.common.dao.MeterResultData.getNonNull;

/**
 * Created by jaspervdbijl on 2017/07/07.
 */
@Path("summary")
@DependsOn("MDMSUpgradeService")
public class SummaryDataService {

    @Inject
    private SummaryService summaryService;

    @Inject
    private PropertiesConfig propertiesConfig;

    @Produces(MediaType.APPLICATION_JSON)
    @Path("meterStats")
    @GET
    public Response getSummaryData(@Context HttpServletRequest request,
                                        @NotNull @QueryParam("serialN") String serialN,
                                        @QueryParam("tmzOffset") @DefaultValue("120") Integer tmzOffset) {

        Container container = new Container();

        List<String> periods = new ArrayList<>();
        periods.addAll(Arrays.asList("Today","Last 7 Days","Month to Date","Last 30 Days","Last 365 Days"));
        LocalDateTime from = LocalDateTime.now().truncatedTo(DAYS);
        LocalDateTime to = LocalDateTime.now();

        List<String> fields = Arrays.asList("totalKwhP","totalKwhN","t1KwhP","t1KwhN","totalKVarP","totalKVarN","t1KVarP","t1KVarN","totalMaxDemandPoKva");

        Arrays.asList(from,from.minusDays(7),from.withDayOfMonth(1),from.minusDays(30),from.minusDays(365)).stream().forEach( d -> {

            MeterResultSummaryRecords records = summaryService.getSummaryData(
                    fields,fields.stream().map(f -> f.startsWith("t1") || f.startsWith("t2") ? "total"+f.substring(2):f).collect(Collectors.toList())
                    ,d,to,serialN,
                    tmzOffset,true,null,null);

            if (!records.isEmpty()) {
                MeterResultSummaryRecord kw[] = records.getByName("totalKwhP").get().getTotal() > records.getByName("t1KwhP").get().getTotal() ?
                        new MeterResultSummaryRecord[]{records.getByName("totalKwhP").get(), records.getByName("totalKwhN").get()} :
                        new MeterResultSummaryRecord[]{records.getByName("t1KwhP").get(), records.getByName("t1KwhN").get()};
                MeterResultSummaryRecord kv[] = records.getByName("totalKVarP").get().getTotal() > records.getByName("t1KVarP").get().getTotal() ?
                        new MeterResultSummaryRecord[]{records.getByName("totalKVarP").get(), records.getByName("totalKVarN").get()} :
                        new MeterResultSummaryRecord[]{records.getByName("t1KVarP").get(), records.getByName("t1KVarN").get()};
                MeterResultSummaryRecord kva[] = new MeterResultSummaryRecord[]{records.getByName("totalMaxDemandPoKva").get(), null};

                Long numberOfDays = ( Timestamp.valueOf( to ).getTime()/1000 - Timestamp.valueOf( d ).getTime()/1000 ) / (24 * 3600);
                numberOfDays = numberOfDays > 0 ? numberOfDays : 1;
                Timestamp kvaMaxTimestamp = kva[0].getMaxDate() == null ? null : Timestamp.valueOf( kva[0].getMaxDate().toLocalDateTime().plusMinutes(propertiesConfig.getMdms_global_timezone_offset()) );

                container.getMeterResultDataList().add(new SummaryData(periods.remove(0), getNonNull(kw[0].getTotal()) - getNonNull(kw[1].getTotal()),
                        ( getNonNull(kw[0].getTotal()) - getNonNull(kw[1].getTotal()) ) / numberOfDays.doubleValue(),
                        getNonNull(kva[0].getMaxValue()), kvaMaxTimestamp ));
            }

        });
        return Response.ok().entity(container).build();
    }

    @Data
    public static class Request {
        private String serialN;
        private Integer tmzOffset = -120;
    }

    @Data
    public static class SummaryData {
        private String period;
        private double totalkWh,avgkWh,maxDemandKVA;
        private Timestamp maxDemandKVATime;

        public SummaryData() {}

        public SummaryData(String period, double totalkWh, double avgkWh, double maxDemandKVA, Timestamp maxDemandKVATime) {
            this.period = period;
            this.totalkWh = totalkWh;
            this.avgkWh = avgkWh;
            this.maxDemandKVA = maxDemandKVA;
            this.maxDemandKVATime = maxDemandKVATime;
        }
    }

    @Data
    public static class Container {
        private List<SummaryData> meterResultDataList = new ArrayList<>();
    }

}
