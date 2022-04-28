package za.co.spsi.mdms.common.services;

import lombok.*;
import lombok.extern.java.Log;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.hsqldb.lib.StringUtil;
import za.co.spsi.mdms.common.db.utility.ICEMeterPortfolioView;
import za.co.spsi.mdms.elster.db.ElsterMeterEntity;
import za.co.spsi.mdms.generic.meter.db.GenericMeterEntity;
import za.co.spsi.mdms.kamstrup.db.KamstrupMeterEntity;
import za.co.spsi.mdms.nes.db.NESMeterEntity;
//import za.co.spsi.pjtk.util.CloseWrapper;
import za.co.spsi.toolkit.db.DSDB;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.ee.properties.ConfValue;
import za.co.spsi.toolkit.ee.properties.TextFile;
import za.co.spsi.toolkit.util.Assert;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Data @NoArgsConstructor @AllArgsConstructor
class EmailMeterGroup {

    private String MeterType;
    private Timestamp lastComms;
    private int TimeMismatch;
    private String MeterSerialNumber;
    private String EmailAddress;
    private String Body;

    public EmailMeterGroup(String meterType, String meterSerialNumber, Timestamp lastComms, int timeMismatch, String emailAddress, String body) {
        MeterType = meterType;
        this.lastComms = lastComms;
        TimeMismatch = timeMismatch;
        MeterSerialNumber = meterSerialNumber;
        EmailAddress = emailAddress;
        Body = body;
    }

    public String getMeterType() {
        return MeterType;
    }

    public void setMeterType(String meterType) {
        MeterType = meterType;
    }

    public Timestamp getLastComms() {
        return lastComms;
    }

    public void setLastComms(Timestamp lastComms) {
        this.lastComms = lastComms;
    }

    public int getTimeMismatch() {
        return TimeMismatch;
    }

    public void setTimeMismatch(int timeMismatch) {
        TimeMismatch = timeMismatch;
    }

    public String getMeterSerialNumber() {
        return MeterSerialNumber;
    }

    public void setMeterSerialNumber(String meterSerialNumber) {
        MeterSerialNumber = meterSerialNumber;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }

    public String getBody() {
        return Body;
    }

    public void setBody(String body) {
        Body = body;
    }
}

@Singleton
@TransactionManagement(value = TransactionManagementType.BEAN)
@DependsOn({"MDMSUpgradeService"})
@Log
@Startup
public class MDMSMeterTimeProcessor {

    @Inject
    @ConfValue(value = "email.meter_sync", folder = "server", defaultValue = "false")
    private boolean enabled;

    @Inject
    @TextFile("html/meter_time_out_of_sync.html")
    private String mainHtml;

    @Inject
    @TextFile("html/meter_time_out_of_sync_line.html")
    private String lineHtml;

    @Inject
    @TextFile("sql/meter_time_sync.sql")
    private String meterSyncSql;

    @Inject
    @TextFile("sql/meter_time_sync_kamstrup.sql")
    private String meterSyncKamSql;

    @Inject
    @ConfValue(value = "email.meter_sync.allowed_deviance", folder = "server", defaultValue = "5")
    private Integer deviance;

    @Inject
    @ConfValue(value = "email.meter_sync.to_address", folder = "server", defaultValue = "adele@pecgroup.co.za")
    private String toAddress;

    @Inject
    @ConfValue(value = "email.meter_sync.subject", folder = "server", defaultValue = "MDMS Meter Time Deviance Report")
    private String subject;

    private String tempBody;

    @Inject
    MailService mailService;

    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @Resource(mappedName = "java:/jdbc/IceUtil")
    private DataSource iceDataSource;

    @Lock(LockType.WRITE)
    @Schedule(hour = "3", minute = "0", second = "0", persistent = false)
    public void atSchedule() {
        if (enabled) {
            processReport();
        }
    }

    private String getMeterType(EntityDB entity) {
        String meterType =
                entity instanceof KamstrupMeterEntity?"Kamstrup":
                        entity instanceof NESMeterEntity?"Echelon":
                                entity instanceof ElsterMeterEntity?"Elster":
                                        entity instanceof GenericMeterEntity
                                                ? ""+entity.getFields().getByName("meterType").get():
                                        null;
        Assert.isTrue(meterType != null,"Unsupported Meter Type " + entity.getClass());
        return meterType;
    }

    private String getDetailLines( EmailMeterGroup emg ) {

        return lineHtml
                .replace("_meter_type_", emg.getMeterType() )
                .replace("_meter_serial_no_", "" + emg.getMeterSerialNumber() )
                .replace("_last_comms_", "" + emg.getLastComms().toString() )
                .replace("_time_mismatch_",   String.format("%d", emg.getTimeMismatch()) + "");
    }

    private String getMeterSerialNumber( EntityDB meterEntity ) {

        String serialNField = meterEntity instanceof GenericMeterEntity?"meterSerialN":"serialN";

        return meterEntity.getFields().getByName(serialNField).get().toString();
    }

    private List<String> getMeterSerialNumberList( List<EntityDB> meterList ) {

        List<String> stringList = new ArrayList<>();

        for( EntityDB mtr: meterList  ) {

            String sn = getMeterSerialNumber(mtr);

            if (!sn.equals(null))
                stringList.add( sn );
        }

        return stringList;
    }

    private String formalSql(String tableName,String sql) {
        return sql
                .replace("_TABLE_",tableName)
                .replace("_Q1_",""+deviance)
                .replace("_Q2_",""+(30 - deviance))
                .replace("_Q3_",""+(30 + deviance))
                .replace("_Q4_",""+(60 - deviance));

    }

    /**
     * Email devices that were off
     */

    @SneakyThrows
    public void processReport() {

        List<EmailMeterGroup> emailMeterGroupList = new ArrayList<>();
        List<KamstrupMeterEntity> kamstrupList = new ArrayList<>();
        List<NESMeterEntity> nesList = new ArrayList<>();
        List<ElsterMeterEntity> elsterList = new ArrayList<>();
        List<GenericMeterEntity> genericList = new ArrayList<>();

        DSDB.executeInTx(dataSource,c -> {

            kamstrupList.addAll( new DSDB(new KamstrupMeterEntity()).getAll(c,formalSql("KAMSTRUP_METER",meterSyncKamSql)).getAllAsList() );
            kamstrupList.forEach( m -> {

                EmailMeterGroup emailMeterGroup =
                        new EmailMeterGroup( getMeterType(m),
                                             getMeterSerialNumber(m),
                                             m.lastCommsD.getLocal(),
                                             LocalDateTime.ofInstant(m.lastCommsD.getLocal().toInstant(), ZoneId.systemDefault()).getMinute(),
                                             null,
                                             null);

                emailMeterGroupList.add( emailMeterGroup );

            } );

            nesList.addAll( new DSDB(new NESMeterEntity()).getAll(c,formalSql("NES_METER",meterSyncSql)).getAllAsList() );
            nesList.forEach( m -> {

                EmailMeterGroup emailMeterGroup =
                        new EmailMeterGroup( getMeterType(m),
                                getMeterSerialNumber(m),
                                m.lastCommsD.getLocal(),
                                LocalDateTime.ofInstant(m.lastCommsD.getLocal().toInstant(), ZoneId.systemDefault()).getMinute(),
                                null,
                                null);

                emailMeterGroupList.add( emailMeterGroup );

            } );

            elsterList.addAll( new DSDB(new ElsterMeterEntity()).getAll(c,formalSql("ELSTER_METER",meterSyncSql)).getAllAsList() );
            elsterList.forEach( m -> {

                EmailMeterGroup emailMeterGroup =
                        new EmailMeterGroup( getMeterType(m),
                                getMeterSerialNumber(m),
                                m.lastCommsD.getLocal(),
                                LocalDateTime.ofInstant(m.lastCommsD.getLocal().toInstant(), ZoneId.systemDefault()).getMinute(),
                                null,
                                null);

                emailMeterGroupList.add( emailMeterGroup );

            } );

            genericList.addAll( new DSDB(new GenericMeterEntity()).getAll(c,formalSql("GENERIC_METER",meterSyncSql)).getAllAsList() );
            genericList.forEach( m -> {

                EmailMeterGroup emailMeterGroup =
                        new EmailMeterGroup( getMeterType(m),
                                getMeterSerialNumber(m),
                                m.lastCommsD.getLocal(),
                                LocalDateTime.ofInstant(m.lastCommsD.getLocal().toInstant(), ZoneId.systemDefault()).getMinute(),
                                null,
                                null);

                emailMeterGroupList.add( emailMeterGroup );

            } );

            emailMeterGroupList.forEach( mtr -> {

                try (Connection connection = iceDataSource.getConnection()) {
                    connection.setAutoCommit(false);
                    String emailAdd = this.toAddress;
                    String query = String.format("select * from ICE_Meter_Portfolio_V where upper(trim(ice_meter_number)) = '%s'", mtr.getMeterSerialNumber());

                    for ( ICEMeterPortfolioView iceMeterPortfolioView: new DataSourceDB<>(ICEMeterPortfolioView.class).getAll(connection, query, null) ) {

                        emailAdd = iceMeterPortfolioView.portfolioManagerEmail.toString().split(":")[1];

                    }

                    mtr.setEmailAddress( emailAdd );

                } catch (SQLException sqle) {

                   log.info( sqle.getStackTrace().toString() );

                }

            } );

        });

        Map<String,List<EmailMeterGroup>> emailMeterGroupMap = emailMeterGroupList.stream().collect(Collectors.groupingBy(EmailMeterGroup::getEmailAddress));

        emailMeterGroupMap.forEach( (pfmEmail, pfmMetersList) -> {

            Comparator<EmailMeterGroup> emailMeterGroupLastCommsSorting = Comparator.comparing((EmailMeterGroup emailMeterGroup) -> emailMeterGroup.getLastComms());

            Collections.sort( pfmMetersList , emailMeterGroupLastCommsSorting );

            pfmMetersList.forEach( emg -> {

                this.tempBody += this.getDetailLines( emg );

            } );

            String html = mainHtml
                    .replace("_DEVIANCE_", deviance + "")
                    .replace("_LINES_", StringUtils.isNotBlank( this.tempBody ) ? this.tempBody : "");

            mailService.sendHtml(pfmEmail, mailService.getFrom(), subject, html);

            this.tempBody = "";

        } );

        log.info("Meter Time Deviation Report Sent !");

    }

}