package za.co.spsi.mdms.common.properties.config;

import com.mysql.cj.core.util.StringUtils;
import lombok.Data;

import za.co.spsi.mdms.common.properties.service.PropertiesStoreService;
import za.co.spsi.mdms.common.services.MdmsSettingsService;
import za.co.spsi.pjtk.util.Assert;
import za.co.spsi.toolkit.ee.properties.ConfValue;

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.inject.Inject;

import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Arno Combrinck
 */
@Data
@Startup
@Singleton
@AccessTimeout(value = 600000)
@TransactionManagement(value = TransactionManagementType.BEAN)
@DependsOn({"MDMSUpgradeService"})
public class PropertiesConfig {

    private static Logger logger = Logger.getLogger(PropertiesConfig.class.getName());

    private static final String mdms_global_timezone_offset_key                     = "mdms.global.timezone.offset";
    private static final String utility_prepaid_push_enabled_key                    = "utility.prepaid.push.enabled";
    private static final String utility_meter_broker_service_enabled_key            = "utility.meter_broker_service.enabled";
    private static final String utility_generator_meter_sync_enabled_key            = "utility.generator_meter_sync.enabled";
    private static final String utility_meter_reading_sync_enabled_key              = "utility.meter_reading_sync.enabled";
    private static final String generator_service_enabled_key                       = "generator.service.enabled";
    private static final String generator_status_msg_enabled_key                    = "generator.status.msg.enabled";
    private static final String winsms_service_enabled_key                          = "winsms.service.enabled";
    private static final String prepaid_batch_processing_enabled_key                = "prepaid.batch.processing.enabled";
    private static final String prepaid_batch_processing_interval_key               = "prepaid.batch.processing.interval";
    private static final String prepaid_batch_processing_backlog_time_window_key    = "prepaid.batch.processing.backlog.time.window";
    private static final String prepaid_batch_latest_reading_only_key               = "prepaid.batch.latest.reading.only";
    private static final String prepaid_batch_30min_meter_filter_key                = "prepaid.batch.30min.meter.filter";
    private static final String kamstrup_processing_enabled_key                     = "kamstrup.processing.enabled";
    private static final String kamstrup_processing_failed_orders_enabled_key       = "kamstrup.processing.failed.orders.enabled";
    private static final String kamstrup_processing_failed_orders_days_key          = "kamstrup.processing.failed.orders.days";
    private static final String kamstrup_processing_failed_orders_retry_key         = "kamstrup.processing.failed.orders.retry";
    private static final String kamstrup_processing_order_schedule_delay_key        = "kamstrup.processing.order.schedule.delay";
    private static final String kamstrup_gap_processing_enabled_key                 = "kamstrup.gap.processing.enabled";
    private static final String kamstrup_gap_processing_debug_enabled_key           = "kamstrup.gap.processing.debug.enabled";
    private static final String kamstrup_gap_processing_batch_size_key              = "kamstrup.gap.processing.batch.size";
    private static final String kamstrup_gap_processing_delay_key                   = "kamstrup.gap.processing.delay";
    private static final String kamstrup_gap_processing_water_delay_key             = "kamstrup.gap.processing.water.delay";
    private static final String kamstrup_gap_processing_minutes_key                 = "kamstrup.gap.processing.mins";
    private static final String kamstrup_gap_processing_water_minutes_key           = "kamstrup.gap.processing.water.mins";
    private static final String kamstrup_autocollection_enabled_key                 = "kamstrup.autocollection.enabled";
    private static final String kamstrup_autocollection_filtered_meters_enabled_key = "kamstrup.autocollection.filtered.meters.enabled";
    private static final String kamstrup_broker_processing_enabled_key              = "kamstrup.broker.processing.enabled";
    private static final String kamstrup_broker_filter_meters_key                   = "kamstrup.broker.filter.meters";
    private static final String nes_processing_enabled_key                          = "nes.processing.enabled";
    private static final String nes_processing_batch_size_key                       = "nes.processing.batch.size";
    private static final String nes_broker_processing_enabled_key                   = "nes.broker.processing.enabled";
    private static final String nes_broker_filter_meters_key                        = "nes.broker.filter.meters";
    private static final String generic_meter_processing_enabled_key                = "generic.meter.processing.enabled";
    private static final String generic_broker_processing_enabled_key               = "generic.broker.processing.enabled";
    private static final String generic_broker_filter_meters_key                    = "generic.broker.filter.meters";
    private static final String elster_processing_enabled_key                       = "elster.processing.enabled";
    private static final String elster_processing_batch_size_key                    = "elster.processing.batch.size";
    private static final String meter_reading_gap_processor_enabled_key             = "meter.reading.gap.processor.enabled";
    private static final String meter_reading_gap_processor_debug_enabled_key       = "meter.reading.gap.processor.debug.enabled";
    private static final String meter_reading_gap_processor_batch_size_key          = "meter.reading.gap.processor.batch.size";
    private static final String filtered_meters_key                                 = "filtered_meters";

    @Inject
    @ConfValue(value = "public.key", folder = "server", defaultValue = "TO BE ADDED")
    private String public_key;

    @Inject
    @ConfValue(value = "mdms.app.instance", folder = "server", defaultValue = "1")
    private Integer mdms_app_instance;

    @Inject
    @ConfValue(value = "mdms.file.based.properties.enabled", folder = "server", defaultValue = "false")
    public Boolean mdms_file_based_properties_enabled;

    @Inject
    @ConfValue(value = "mdms.wildfly.deployment.mode", folder = "server", defaultValue = "false")
    public Boolean mdms_wildfly_deployment_mode;

    @Inject
    @ConfValue(value = "mdms.global.timezone.offset", folder = "server", defaultValue = "120")
    public Integer mdms_global_timezone_offset;

    @Inject
    @ConfValue(value = "generator.service.enabled", folder = "server", defaultValue = "true")
    public Boolean generator_service_enabled;

    @Inject
    @ConfValue(value = "generator.status.msg.enabled", folder = "server", defaultValue = "false")
    public Boolean generator_status_msg_enabled;

    @Inject
    @ConfValue(value = "winsms.service.enabled", folder = "server", defaultValue = "true")
    public Boolean winsms_service_enabled;

    @Inject
    @ConfValue(value = "utility.prepaid.push.enabled", folder = "server", defaultValue = "true")
    public Boolean utility_prepaid_push_enabled;

    @Inject
    @ConfValue(value = "utility.meter_broker_service.enabled", folder = "server", defaultValue = "true")
    public Boolean utility_meter_broker_service_enabled;

    @Inject
    @ConfValue(value = "utility.generator_meter_sync.enabled", folder = "server", defaultValue = "true")
    public Boolean utility_generator_meter_sync_enabled;

    @Inject
    @ConfValue(value = "utility.meter_reading_sync.enabled", folder = "server", defaultValue = "true")
    public Boolean utility_meter_reading_sync_enabled;

    @Inject
    @ConfValue(value = "prepaid.batch.processing.enabled", folder = "server", defaultValue = "true")
    public Boolean prepaid_batch_processing_enabled;

    @Inject
    @ConfValue(value = "prepaid.batch.processing.interval", folder = "server", defaultValue = "2")
    public Integer prepaid_batch_processing_interval;

    @Inject
    @ConfValue(value = "prepaid.batch.processing.backlog.time.window", folder = "server", defaultValue = "7")
    public Integer prepaid_batch_processing_backlog_time_window;

    @Inject
    @ConfValue(value = "prepaid.batch.latest.reading.only", folder = "server", defaultValue = "true")
    public Boolean prepaid_batch_latest_reading_only;

    @Inject
    @ConfValue(value = "prepaid.batch.30min.meter.filter", folder = "server")
    public String prepaid_batch_30min_meter_filter;

    @Inject
    @ConfValue(value = "kamstrup.processing.enabled", folder = "server", defaultValue = "true")
    public Boolean kamstrup_processing_enabled;

    @Inject
    @ConfValue(value = "kamstrup.processing.order.schedule.delay", folder = "server", defaultValue = "120")
    public Integer kamstrup_processing_order_schedule_delay;

    @Inject
    @ConfValue(value = "kamstrup.processing.failed.orders.enabled", folder = "server", defaultValue = "true")
    public Boolean kamstrup_processing_failed_orders_enabled;

    @Inject
    @ConfValue(value = "kamstrup.processing.failed.orders.days", folder = "server", defaultValue = "2")
    public Integer kamstrup_processing_failed_orders_days;

    @Inject
    @ConfValue(value = "kamstrup.processing.failed.orders.retry", folder = "server", defaultValue = "5")
    public Integer kamstrup_processing_failed_orders_retry;

    @Inject
    @ConfValue(value = "kamstrup.gap.processing.enabled", folder = "server", defaultValue = "true")
    public Boolean kamstrup_gap_processing_enabled;

    @Inject
    @ConfValue(value = "kamstrup.gap.processing.debug.enabled", folder = "server", defaultValue = "false")
    public Boolean kamstrup_gap_processing_debug_enabled;

    @Inject
    @ConfValue(value = "kamstrup.gap.processing.batch.size", folder = "server", defaultValue = "300")
    public Integer kamstrup_gap_processing_batch_size;

    @Inject
    @ConfValue(value = "kamstrup.gap.processing.delay", folder = "server", defaultValue = "1140")
    public Integer kamstrup_gap_processing_delay;

    @Inject
    @ConfValue(value = "kamstrup.gap.processing.water.delay", folder = "server", defaultValue = "60")
    public Integer kamstrup_gap_processing_water_delay;

    @Inject
    @ConfValue(value = "kamstrup.gap.processing.mins", folder = "server", defaultValue = "40")
    public Integer kamstrup_gap_processing_minutes;

    @Inject
    @ConfValue(value = "kamstrup.gap.processing.water.mins", folder = "server", defaultValue = "120")
    public Integer kamstrup_gap_processing_water_minutes;

    @Inject
    @ConfValue(value = "kamstrup.autocollection.enabled", folder = "server", defaultValue = "true")
    public Boolean kamstrup_autocollection_enabled;

    @Inject
    @ConfValue(value = "kamstrup.autocollection.filtered.meters.enabled", folder = "server", defaultValue = "true")
    public Boolean kamstrup_autocollection_filtered_meters_enabled;

    @Inject
    @ConfValue(value = "kamstrup.broker.processing.enabled", folder = "server", defaultValue = "true")
    public Boolean kamstrup_broker_processing_enabled;

    @Inject
    @ConfValue(value = "kamstrup.broker.filter.meters", folder = "server")
    public String kamstrup_broker_filter_meters;

    @Inject
    @ConfValue(value = "nes.processing.enabled", folder = "server", defaultValue = "true")
    public Boolean nes_processing_enabled;

    @Inject
    @ConfValue(value = "nes.processing.batch.size", folder = "server", defaultValue = "5000")
    public Integer nes_processing_batch_size;

    @Inject
    @ConfValue(value = "nes.broker.processing.enabled", folder = "server", defaultValue = "true")
    public Boolean nes_broker_processing_enabled;

    @Inject
    @ConfValue(value = "nes.broker.filter.meters", folder = "server")
    public String nes_broker_filter_meters;

    @Inject
    @ConfValue(value = "generic.meter.processing.enabled", folder = "server", defaultValue = "true")
    public Boolean generic_meter_processing_enabled;

    @Inject
    @ConfValue(value = "generic.broker.processing.enabled", folder = "server", defaultValue = "true")
    public Boolean generic_broker_processing_enabled;

    @Inject
    @ConfValue(value = "generic.broker.filter.meters", folder = "server")
    public String generic_broker_filter_meters;

    @Inject
    @ConfValue(value = "elster.processing.enabled", folder = "server", defaultValue = "true")
    public Boolean elster_processing_enabled;

    @Inject
    @ConfValue(value = "elster.processing.batch.size", folder = "server", defaultValue = "1000")
    public Integer elster_processing_batch_size;

    @Inject
    @ConfValue(value = "meter.reading.gap.processor.enabled", folder = "server", defaultValue = "true")
    public Boolean meter_reading_gap_processor_enabled;

    @Inject
    @ConfValue(value = "meter.reading.gap.processor.debug.enabled", folder = "server", defaultValue = "false")
    public Boolean meter_reading_gap_processor_debug_enabled;

    @Inject
    @ConfValue(value = "meter.reading.gap.processor.batch.size", folder = "server", defaultValue = "100")
    public Integer meter_reading_gap_processor_batch_size;

    @Inject
    @ConfValue(value = "db.frw.engineering.enabled", folder = "server", defaultValue = "true")
    public Boolean db_frw_engineering_enabled;

    @Inject
    @ConfValue(value = "db.frw.engineering.liquibase.enabled", folder = "server", defaultValue = "true")
    public Boolean db_frw_engineering_liquibase_enabled;

    @Inject
    @ConfValue(value = "filtered_meters", folder = "server")
    public String filtered_meters;

    @Inject
    private MdmsSettingsService mdmsSettingsService;

    @Inject
    private PropertiesStoreService propertiesStoreService;

    public Map<String,String> mdmsDatabasePropertiesStore;

    public Properties mdmsFileBasedPropertiesStore;

    public PropertiesConfig() {

    }

    @PostConstruct
    void init() {
        logger.log(Level.INFO, "Initialize MDMS Properties Store.");
        this.updatePropertyValues();
    }

    public void updatePropertyValues() {

        Map<String, String> propertiesStore;

        if(this.getMdms_file_based_properties_enabled()) {
            mdmsFileBasedPropertiesStore = propertiesStoreService.loadPropertiesFromFile();
            propertiesStore = propertiesStoreService.convertPropertiesToMap(mdmsFileBasedPropertiesStore);
        } else {
            mdmsDatabasePropertiesStore = mdmsSettingsService.loadPropertiesFromDatabase();
            propertiesStore = mdmsDatabasePropertiesStore;
        }

        Assert.notNull(propertiesStore,"Could not update property values, the properties store is null.");

        if(propertiesStore.isEmpty()) {
            logger.warning("Could not update property values, the properties store is empty.");
            logger.warning("MDMS will use default property values to start up.");
            return;
        }

        propertiesStore.forEach( (prop_key, prop_val) -> {

            Assert.notNull(prop_key,"Property key was null.");

            try {
                switch(prop_key.toLowerCase()) {

                    case mdms_global_timezone_offset_key:
                        logger.log(Level.INFO, String.format("BEFORE -> Property %s, Value %s", prop_key, this.getMdms_global_timezone_offset() ));
                        if(StringUtils.isNullOrEmpty(prop_val)) {
                            prop_val = String.valueOf(120);
                        }
                        if(this.getMdms_global_timezone_offset() == null) {
                            this.setMdms_global_timezone_offset(120);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getMdms_global_timezone_offset() ));
                        }
                        if(this.getMdms_global_timezone_offset() != Integer.parseInt(prop_val)) {
                            this.setMdms_global_timezone_offset(Integer.parseInt(prop_val));
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getMdms_global_timezone_offset() ));
                        }
                        break;
                    case generator_service_enabled_key:
                        logger.log(Level.INFO, String.format("BEFORE -> Property %s, Value %s", prop_key, this.getGenerator_service_enabled() ));
                        if(StringUtils.isNullOrEmpty(prop_val)) {
                            prop_val = String.valueOf(true);
                        }
                        if(this.getGenerator_service_enabled() == null) {
                            this.setGenerator_service_enabled(true);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getGenerator_service_enabled() ));
                        }
                        if(this.getGenerator_service_enabled() != Boolean.parseBoolean(prop_val)) {
                            this.setGenerator_service_enabled(Boolean.parseBoolean(prop_val));
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getGenerator_service_enabled() ));
                        }
                        break;
                    case generator_status_msg_enabled_key:
                        logger.log(Level.INFO, String.format("BEFORE -> Property %s, Value %s", prop_key, this.getGenerator_status_msg_enabled() ));
                        if(StringUtils.isNullOrEmpty(prop_val)) {
                            prop_val = String.valueOf(true);
                        }
                        if(this.getGenerator_status_msg_enabled() == null) {
                            this.setGenerator_status_msg_enabled(true);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getGenerator_status_msg_enabled() ));
                        }
                        if(this.getGenerator_status_msg_enabled() != Boolean.parseBoolean(prop_val)) {
                            this.setGenerator_status_msg_enabled(Boolean.parseBoolean(prop_val));
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getGenerator_status_msg_enabled() ));
                        }
                        break;
                    case winsms_service_enabled_key:
                        logger.log(Level.INFO, String.format("BEFORE -> Property %s, Value %s", prop_key, this.getWinsms_service_enabled() ));
                        if(StringUtils.isNullOrEmpty(prop_val)) {
                            prop_val = String.valueOf(true);
                        }
                        if(this.getWinsms_service_enabled() == null) {
                            this.setWinsms_service_enabled(true);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getWinsms_service_enabled() ));
                        }
                        if(this.getWinsms_service_enabled() != Boolean.parseBoolean(prop_val)) {
                            this.setWinsms_service_enabled(Boolean.parseBoolean(prop_val));
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getWinsms_service_enabled() ));
                        }
                        break;
                    case utility_prepaid_push_enabled_key:
                        logger.log(Level.INFO, String.format("BEFORE -> Property %s, Value %s", prop_key, this.getUtility_prepaid_push_enabled() ));
                        if(StringUtils.isNullOrEmpty(prop_val)) {
                            prop_val = String.valueOf(true);
                        }
                        if(this.getUtility_prepaid_push_enabled() == null) {
                            this.setUtility_prepaid_push_enabled(true);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getUtility_prepaid_push_enabled() ));
                        }
                        if(this.getUtility_prepaid_push_enabled() != Boolean.parseBoolean(prop_val)) {
                            this.setUtility_prepaid_push_enabled(Boolean.parseBoolean(prop_val));
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getUtility_prepaid_push_enabled() ));
                        }
                        break;
                    case utility_meter_broker_service_enabled_key:
                        logger.log(Level.INFO, String.format("BEFORE -> Property %s, Value %s", prop_key, this.getUtility_meter_broker_service_enabled() ));
                        if(StringUtils.isNullOrEmpty(prop_val)) {
                            prop_val = String.valueOf(true);
                        }
                        if(this.getUtility_meter_broker_service_enabled() == null) {
                            this.setUtility_meter_broker_service_enabled(true);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getUtility_meter_broker_service_enabled() ));
                        }
                        if(this.getUtility_meter_broker_service_enabled() != Boolean.parseBoolean(prop_val)) {
                            this.setUtility_meter_broker_service_enabled(Boolean.parseBoolean(prop_val));
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getUtility_meter_broker_service_enabled() ));
                        }
                        break;
                    case utility_generator_meter_sync_enabled_key:
                        logger.log(Level.INFO, String.format("BEFORE -> Property %s, Value %s", prop_key, this.getUtility_generator_meter_sync_enabled() ));
                        if(StringUtils.isNullOrEmpty(prop_val)) {
                            prop_val = String.valueOf(true);
                        }
                        if(this.getUtility_generator_meter_sync_enabled() == null) {
                            this.setUtility_generator_meter_sync_enabled(true);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getUtility_generator_meter_sync_enabled() ));
                        }
                        if(this.getUtility_generator_meter_sync_enabled() != Boolean.parseBoolean(prop_val)) {
                            this.setUtility_generator_meter_sync_enabled(Boolean.parseBoolean(prop_val));
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getUtility_generator_meter_sync_enabled() ));
                        }
                        break;
                    case utility_meter_reading_sync_enabled_key:
                        logger.log(Level.INFO, String.format("BEFORE -> Property %s, Value %s", prop_key, this.getUtility_meter_reading_sync_enabled() ));
                        if(StringUtils.isNullOrEmpty(prop_val)) {
                            prop_val = String.valueOf(true);
                        }
                        if(this.getUtility_meter_reading_sync_enabled() == null) {
                            this.setUtility_meter_reading_sync_enabled(true);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getUtility_meter_reading_sync_enabled() ));
                        }
                        if(this.getUtility_meter_reading_sync_enabled() != Boolean.parseBoolean(prop_val)) {
                            this.setUtility_meter_reading_sync_enabled(Boolean.parseBoolean(prop_val));
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getUtility_meter_reading_sync_enabled() ));
                        }
                        break;
                    case prepaid_batch_processing_enabled_key:
                        logger.log(Level.INFO, String.format("BEFORE -> Property %s, Value %s", prop_key, this.getPrepaid_batch_processing_enabled() ));
                        if(StringUtils.isNullOrEmpty(prop_val)) {
                            prop_val = String.valueOf(true);
                        }
                        if(this.getPrepaid_batch_processing_enabled() == null) {
                            this.setPrepaid_batch_processing_enabled(true);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getPrepaid_batch_processing_enabled() ));
                        }
                        if(this.getPrepaid_batch_processing_enabled() != Boolean.parseBoolean(prop_val)) {
                            this.setPrepaid_batch_processing_enabled(Boolean.parseBoolean(prop_val));
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getPrepaid_batch_processing_enabled() ));
                        }
                        break;
                    case prepaid_batch_processing_interval_key:
                        if(StringUtils.isNullOrEmpty(prop_val)) {
                            prop_val = String.valueOf(2); // 2 hours
                        }
                        logger.log(Level.INFO, String.format("BEFORE -> Property %s, Value %s", prop_key, this.getPrepaid_batch_processing_interval() ));
                        if(this.getPrepaid_batch_processing_interval() == null) {
                            this.setPrepaid_batch_processing_interval(2); // 2 hours
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getPrepaid_batch_processing_interval() ));
                        }
                        if(this.getPrepaid_batch_processing_interval() != Integer.parseInt(prop_val)) {
                            this.setPrepaid_batch_processing_interval (Integer.parseInt(prop_val));
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getPrepaid_batch_processing_interval() ));
                        }
                        break;
                    case prepaid_batch_processing_backlog_time_window_key:
                        if(StringUtils.isNullOrEmpty(prop_val)) {
                            prop_val = String.valueOf(7); // 7 days
                        }
                        logger.log(Level.INFO, String.format("BEFORE -> Property %s, Value %s", prop_key, this.getPrepaid_batch_processing_backlog_time_window() ));
                        if(this.getPrepaid_batch_processing_backlog_time_window() == null) {
                            this.setPrepaid_batch_processing_backlog_time_window(7); // 7 days
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getPrepaid_batch_processing_backlog_time_window() ));
                        }
                        if(this.getPrepaid_batch_processing_backlog_time_window() != Integer.parseInt(prop_val)) {
                            this.setPrepaid_batch_processing_backlog_time_window(Integer.parseInt(prop_val));
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getPrepaid_batch_processing_backlog_time_window() ));
                        }
                        break;
                    case prepaid_batch_latest_reading_only_key:
                        logger.log(Level.INFO, String.format("BEFORE -> Property %s, Value %s", prop_key, this.getPrepaid_batch_latest_reading_only() ));
                        if(StringUtils.isNullOrEmpty(prop_val)) {
                            prop_val = String.valueOf(true);
                        }
                        if(this.getPrepaid_batch_latest_reading_only() == null) {
                            this.setPrepaid_batch_latest_reading_only(true);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getPrepaid_batch_latest_reading_only() ));
                        }
                        if(this.getPrepaid_batch_latest_reading_only() != Boolean.parseBoolean(prop_val)) {
                            this.setPrepaid_batch_latest_reading_only(Boolean.parseBoolean(prop_val));
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getPrepaid_batch_latest_reading_only() ));
                        }
                        break;
                    case prepaid_batch_30min_meter_filter_key:
                        logger.log(Level.INFO, String.format("BEFORE -> Property %s, Value %s", prop_key, this.getPrepaid_batch_30min_meter_filter() ));
                        if(StringUtils.isNullOrEmpty(prop_val)) {
                            prop_val = "";
                        }
                        if(this.getPrepaid_batch_30min_meter_filter() == null) {
                            this.setPrepaid_batch_30min_meter_filter("");
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getPrepaid_batch_30min_meter_filter() ));
                        }
                        if( !this.getPrepaid_batch_30min_meter_filter().equalsIgnoreCase(prop_val) ) {
                            this.setPrepaid_batch_30min_meter_filter(prop_val);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getPrepaid_batch_30min_meter_filter() ));
                        }
                        break;
                    case kamstrup_processing_enabled_key:
                        logger.log(Level.INFO, String.format("BEFORE -> Property %s, Value %s", prop_key, this.getKamstrup_processing_enabled() ));
                        if(StringUtils.isNullOrEmpty(prop_val)) {
                            prop_val = String.valueOf(true);
                        }
                        if(this.getKamstrup_processing_enabled() == null) {
                            this.setKamstrup_processing_enabled(true);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getKamstrup_processing_enabled() ));
                        }
                        if(this.getKamstrup_processing_enabled() != Boolean.parseBoolean(prop_val)) {
                            this.setKamstrup_processing_enabled(Boolean.parseBoolean(prop_val));
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getKamstrup_processing_enabled() ));
                        }
                        break;
                    case kamstrup_processing_order_schedule_delay_key:
                        logger.log(Level.INFO, String.format("BEFORE -> Property %s, Value %s", prop_key, this.getKamstrup_processing_order_schedule_delay() ));
                        if(StringUtils.isNullOrEmpty(prop_val)) {
                            prop_val = String.valueOf(120);
                        }
                        if(this.getKamstrup_processing_order_schedule_delay() == null) {
                            this.setKamstrup_processing_order_schedule_delay(120);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getKamstrup_processing_order_schedule_delay() ));
                        }
                        if(this.getKamstrup_processing_order_schedule_delay() != Integer.parseInt(prop_val)) {
                            this.setKamstrup_processing_order_schedule_delay(Integer.parseInt(prop_val));
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getKamstrup_processing_order_schedule_delay() ));
                        }
                        break;
                    case kamstrup_processing_failed_orders_enabled_key:
                        logger.log(Level.INFO, String.format("BEFORE -> Property %s, Value %s", prop_key, this.getKamstrup_processing_failed_orders_enabled() ));
                        if(StringUtils.isNullOrEmpty(prop_val)) {
                            prop_val = String.valueOf(true);
                        }
                        if(this.getKamstrup_processing_failed_orders_enabled() == null) {
                            this.setKamstrup_processing_failed_orders_enabled(true);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getKamstrup_processing_failed_orders_enabled() ));
                        }
                        if(this.getKamstrup_processing_failed_orders_enabled() != Boolean.parseBoolean(prop_val)) {
                            this.setKamstrup_processing_failed_orders_enabled(Boolean.parseBoolean(prop_val));
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getKamstrup_processing_failed_orders_enabled() ));
                        }
                        break;
                    case kamstrup_processing_failed_orders_days_key:
                        logger.log(Level.INFO, String.format("BEFORE -> Property %s, Value %s", prop_key, this.getKamstrup_processing_failed_orders_days() ));
                        if(StringUtils.isNullOrEmpty(prop_val)) {
                            prop_val = String.valueOf(10);
                        }
                        if(this.getKamstrup_processing_failed_orders_days() == null) {
                            this.setKamstrup_processing_failed_orders_days(10);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getKamstrup_processing_failed_orders_days() ));
                        }
                        if(this.getKamstrup_processing_failed_orders_days() != Integer.parseInt(prop_val)) {
                            this.setKamstrup_processing_failed_orders_days(Integer.parseInt(prop_val));
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getKamstrup_processing_failed_orders_days() ));
                        }
                        break;
                    case kamstrup_processing_failed_orders_retry_key:
                        logger.log(Level.INFO, String.format("BEFORE -> Property %s, Value %s", prop_key, this.getKamstrup_processing_failed_orders_retry() ));
                        if(StringUtils.isNullOrEmpty(prop_val)) {
                            prop_val = String.valueOf(5);
                        }
                        if(this.getKamstrup_processing_failed_orders_retry() == null) {
                            this.setKamstrup_processing_failed_orders_retry(5);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getKamstrup_processing_failed_orders_retry() ));
                        }
                        if(this.getKamstrup_processing_failed_orders_retry() != Integer.parseInt(prop_val)) {
                            this.setKamstrup_processing_failed_orders_retry(Integer.parseInt(prop_val));
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getKamstrup_processing_failed_orders_retry() ));
                        }
                        break;
                    case kamstrup_gap_processing_enabled_key:
                        logger.log(Level.INFO, String.format("BEFORE -> Property %s, Value %s", prop_key, this.getKamstrup_gap_processing_enabled() ));
                        if(StringUtils.isNullOrEmpty(prop_val)) {
                            prop_val = String.valueOf(true);
                        }
                        if(this.getKamstrup_gap_processing_enabled() == null) {
                            this.setKamstrup_gap_processing_enabled(true);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getKamstrup_gap_processing_enabled() ));
                        }
                        if(this.getKamstrup_gap_processing_enabled() != Boolean.parseBoolean(prop_val)) {
                            this.setKamstrup_gap_processing_enabled(Boolean.parseBoolean(prop_val));
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getKamstrup_gap_processing_enabled() ));
                        }
                        break;
                    case kamstrup_gap_processing_debug_enabled_key:
                        logger.log(Level.INFO, String.format("BEFORE -> Property %s, Value %s", prop_key, this.getKamstrup_gap_processing_debug_enabled() ));
                        if(StringUtils.isNullOrEmpty(prop_val)) {
                            prop_val = String.valueOf(false);
                        }
                        if(this.getKamstrup_gap_processing_debug_enabled() == null) {
                            this.setKamstrup_gap_processing_debug_enabled(false);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getKamstrup_gap_processing_debug_enabled() ));
                        }
                        if(this.getKamstrup_gap_processing_debug_enabled() != Boolean.parseBoolean(prop_val)) {
                            this.setKamstrup_gap_processing_debug_enabled(Boolean.parseBoolean(prop_val));
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getKamstrup_gap_processing_debug_enabled() ));
                        }
                        break;
                    case kamstrup_gap_processing_batch_size_key:
                        logger.log(Level.INFO, String.format("BEFORE -> Property %s, Value %s", prop_key, this.getKamstrup_gap_processing_batch_size() ));
                        if(StringUtils.isNullOrEmpty(prop_val)) {
                            prop_val = String.valueOf(10);
                        }
                        if(this.getKamstrup_gap_processing_batch_size() == null) {
                            this.setKamstrup_gap_processing_batch_size(10);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getKamstrup_gap_processing_batch_size() ));
                        }
                        if(this.getKamstrup_gap_processing_batch_size() != Integer.parseInt(prop_val)) {
                            this.setKamstrup_gap_processing_batch_size(Integer.parseInt(prop_val));
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getKamstrup_gap_processing_batch_size() ));
                        }
                        break;
                    case kamstrup_gap_processing_delay_key:
                        logger.log(Level.INFO, String.format("BEFORE -> Property %s, Value %s", prop_key, this.getKamstrup_gap_processing_delay()));
                        if(StringUtils.isNullOrEmpty(prop_val)) {
                            prop_val = String.valueOf(1140);
                        }
                        if(this.getKamstrup_gap_processing_delay() == null) {
                            this.setKamstrup_gap_processing_delay(1140);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getKamstrup_gap_processing_delay() ));
                        }
                        if(this.getKamstrup_gap_processing_delay() != Integer.parseInt(prop_val)) {
                            this.setKamstrup_gap_processing_delay(Integer.parseInt(prop_val));
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getKamstrup_gap_processing_delay() ));
                        }
                        break;
                    case kamstrup_gap_processing_water_delay_key:
                        logger.log(Level.INFO, String.format("BEFORE -> Property %s, Value %s", prop_key, this.getKamstrup_gap_processing_water_delay() ));
                        if(StringUtils.isNullOrEmpty(prop_val)) {
                            prop_val = String.valueOf(60);
                        }
                        if(this.getKamstrup_gap_processing_water_delay() == null) {
                            this.setKamstrup_gap_processing_water_delay(60);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getKamstrup_gap_processing_water_delay() ));
                        }
                        if(this.getKamstrup_gap_processing_water_delay() != Integer.parseInt(prop_val)) {
                            this.setKamstrup_gap_processing_water_delay(Integer.parseInt(prop_val));
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getKamstrup_gap_processing_water_delay() ));
                        }
                        break;
                    case kamstrup_gap_processing_minutes_key:
                        logger.log(Level.INFO, String.format("BEFORE -> Property %s, Value %s", prop_key, this.getKamstrup_gap_processing_minutes() ));
                        if(StringUtils.isNullOrEmpty(prop_val)) {
                            prop_val = String.valueOf(40);
                        }
                        if(this.getKamstrup_gap_processing_minutes() == null) {
                            this.setKamstrup_gap_processing_minutes(40);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getKamstrup_gap_processing_minutes() ));
                        }
                        if(this.getKamstrup_gap_processing_minutes() != Integer.parseInt(prop_val)) {
                            this.setKamstrup_gap_processing_minutes(Integer.parseInt(prop_val));
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getKamstrup_gap_processing_minutes() ));
                        }
                        break;
                    case kamstrup_gap_processing_water_minutes_key:
                        logger.log(Level.INFO, String.format("BEFORE -> Property %s, Value %s", prop_key, this.getKamstrup_gap_processing_water_minutes() ));
                        if(StringUtils.isNullOrEmpty(prop_val)) {
                            prop_val = String.valueOf(120);
                        }
                        if(this.getKamstrup_gap_processing_water_minutes() == null) {
                            this.setKamstrup_gap_processing_water_minutes(120);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getKamstrup_gap_processing_water_minutes() ));
                        }
                        if(this.getKamstrup_gap_processing_water_minutes() != Integer.parseInt(prop_val)) {
                            this.setKamstrup_gap_processing_water_minutes(Integer.parseInt(prop_val));
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getKamstrup_gap_processing_water_minutes() ));
                        }
                        break;
                    case kamstrup_autocollection_enabled_key:
                        logger.log(Level.INFO, String.format("BEFORE -> Property %s, Value %s", prop_key, this.getKamstrup_autocollection_enabled() ));
                        if(StringUtils.isNullOrEmpty(prop_val)) {
                            prop_val = String.valueOf(true);
                        }
                        if(this.getKamstrup_autocollection_enabled() == null) {
                            this.setKamstrup_autocollection_enabled(true);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getKamstrup_autocollection_enabled() ));
                        }
                        if(this.getKamstrup_autocollection_enabled() != Boolean.parseBoolean(prop_val)) {
                            this.setKamstrup_autocollection_enabled(Boolean.parseBoolean(prop_val));
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getKamstrup_autocollection_enabled() ));
                        }
                        break;
                    case kamstrup_autocollection_filtered_meters_enabled_key:
                        logger.log(Level.INFO, String.format("BEFORE -> Property %s, Value %s", prop_key, this.getKamstrup_autocollection_filtered_meters_enabled() ));
                        if(StringUtils.isNullOrEmpty(prop_val)) {
                            prop_val = String.valueOf(false);
                        }
                        if(this.getKamstrup_autocollection_filtered_meters_enabled() == null) {
                            this.setKamstrup_autocollection_filtered_meters_enabled(false);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getKamstrup_autocollection_filtered_meters_enabled() ));
                        }
                        if(this.getKamstrup_autocollection_filtered_meters_enabled() != Boolean.parseBoolean(prop_val)) {
                            this.setKamstrup_autocollection_filtered_meters_enabled(Boolean.parseBoolean(prop_val));
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getKamstrup_autocollection_filtered_meters_enabled() ));
                        }
                        break;
                    case kamstrup_broker_processing_enabled_key:
                        logger.log(Level.INFO, String.format("BEFORE -> Property %s, Value %s", prop_key, this.getKamstrup_broker_processing_enabled() ));
                        if(StringUtils.isNullOrEmpty(prop_val)) {
                            prop_val = String.valueOf(true);
                        }
                        if(this.getKamstrup_broker_processing_enabled() == null) {
                            this.setKamstrup_broker_processing_enabled(true);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getKamstrup_broker_processing_enabled() ));
                        }
                        if(this.getKamstrup_broker_processing_enabled() != Boolean.parseBoolean(prop_val)) {
                            this.setKamstrup_broker_processing_enabled(Boolean.parseBoolean(prop_val));
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getKamstrup_broker_processing_enabled() ));
                        }
                        break;
                    case kamstrup_broker_filter_meters_key:
                        logger.log(Level.INFO, String.format("BEFORE -> Property %s, Value %s", prop_key, this.getKamstrup_broker_filter_meters() ));
                        if(StringUtils.isNullOrEmpty(prop_val)) {
                            prop_val = "";
                        }
                        if(this.getKamstrup_broker_filter_meters() == null) {
                            this.setKamstrup_broker_filter_meters(prop_val);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getKamstrup_broker_filter_meters() ));
                        }
                        if( !this.getKamstrup_broker_filter_meters().equalsIgnoreCase(prop_val) ) {
                            this.setKamstrup_broker_filter_meters(prop_val);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getKamstrup_broker_filter_meters() ));
                        }
                        break;
                    case nes_processing_enabled_key:
                        logger.log(Level.INFO, String.format("BEFORE -> Property %s, Value %s", prop_key, this.getNes_processing_enabled() ));
                        if(StringUtils.isNullOrEmpty(prop_val)) {
                            prop_val = String.valueOf(true);
                        }
                        if(this.getNes_processing_enabled() == null) {
                            this.setNes_processing_enabled(true);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getNes_processing_enabled() ));
                        }
                        if(this.getNes_processing_enabled() != Boolean.parseBoolean(prop_val)) {
                            this.setNes_processing_enabled(Boolean.parseBoolean(prop_val));
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getNes_processing_enabled() ));
                        }
                        break;
                    case nes_processing_batch_size_key:
                        logger.log(Level.INFO, String.format("BEFORE -> Property %s, Value %s", prop_key, this.getNes_processing_batch_size() ));
                        if(StringUtils.isNullOrEmpty(prop_val)) {
                            prop_val = String.valueOf(10);
                        }
                        if(this.getNes_processing_batch_size() == null) {
                            this.setNes_processing_batch_size(10);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getNes_processing_batch_size() ));
                        }
                        if(this.getNes_processing_batch_size() != Integer.parseInt(prop_val)) {
                            this.setNes_processing_batch_size(Integer.parseInt(prop_val));
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getNes_processing_batch_size() ));
                        }
                        break;
                    case nes_broker_processing_enabled_key:
                        logger.log(Level.INFO, String.format("BEFORE -> Property %s, Value %s", prop_key, this.getNes_broker_processing_enabled() ));
                        if(StringUtils.isNullOrEmpty(prop_val)) {
                            prop_val = String.valueOf(true);
                        }
                        if(this.getNes_broker_processing_enabled() == null) {
                            this.setNes_broker_processing_enabled(true);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getNes_broker_processing_enabled() ));
                        }
                        if(this.getNes_broker_processing_enabled() != Boolean.parseBoolean(prop_val)) {
                            this.setNes_broker_processing_enabled(Boolean.parseBoolean(prop_val));
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getNes_broker_processing_enabled() ));
                        }
                        break;
                    case nes_broker_filter_meters_key:
                        logger.log(Level.INFO, String.format("BEFORE -> Property %s, Value %s", prop_key, this.getNes_broker_filter_meters() ));
                        if(StringUtils.isNullOrEmpty(prop_val)) {
                            prop_val = "";
                        }
                        if(this.getNes_broker_filter_meters() == null) {
                            this.setNes_broker_filter_meters(prop_val);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getNes_broker_filter_meters()));
                        }
                        if( !this.getNes_broker_filter_meters().equalsIgnoreCase(prop_val) ) {
                            this.setNes_broker_filter_meters(prop_val);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getNes_broker_filter_meters()));
                        }
                        break;
                    case generic_meter_processing_enabled_key:
                        logger.log(Level.INFO, String.format("BEFORE -> Property %s, Value %s", prop_key, this.getGeneric_meter_processing_enabled() ));
                        if(StringUtils.isNullOrEmpty(prop_val)) {
                            prop_val = String.valueOf(true);
                        }
                        if(this.getGeneric_meter_processing_enabled() == null) {
                            this.setGeneric_meter_processing_enabled(true);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getGeneric_meter_processing_enabled() ));
                        }
                        if(this.getGeneric_meter_processing_enabled() != Boolean.parseBoolean(prop_val)) {
                            this.setGeneric_meter_processing_enabled(Boolean.parseBoolean(prop_val));
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getGeneric_meter_processing_enabled() ));
                        }
                        break;
                    case generic_broker_processing_enabled_key:
                        logger.log(Level.INFO, String.format("BEFORE -> Property %s, Value %s", prop_key, this.getGeneric_broker_processing_enabled() ));
                        if(StringUtils.isNullOrEmpty(prop_val)) {
                            prop_val = String.valueOf(true);
                        }
                        if(this.getGeneric_broker_processing_enabled() == null) {
                            this.setGeneric_broker_processing_enabled(true);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getGeneric_broker_processing_enabled() ));
                        }
                        if(this.getGeneric_broker_processing_enabled() != Boolean.parseBoolean(prop_val)) {
                            this.setGeneric_broker_processing_enabled(Boolean.parseBoolean(prop_val));
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getGeneric_broker_processing_enabled() ));
                        }
                        break;
                    case generic_broker_filter_meters_key:
                        logger.log(Level.INFO, String.format("BEFORE -> Property %s, Value %s", prop_key, this.getGeneric_broker_filter_meters() ));
                        if(StringUtils.isNullOrEmpty(prop_val)) {
                            prop_val = "";
                        }
                        if(this.getGeneric_broker_filter_meters() == null) {
                            this.setGeneric_broker_filter_meters(prop_val);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getGeneric_broker_filter_meters() ));
                        }
                        if( !this.getGeneric_broker_filter_meters().equalsIgnoreCase(prop_val) ) {
                            this.setGeneric_broker_filter_meters(prop_val);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getGeneric_broker_filter_meters() ));
                        }
                        break;
                    case elster_processing_enabled_key:
                        logger.log(Level.INFO, String.format("BEFORE -> Property %s, Value %s", prop_key, this.getElster_processing_enabled() ));
                        if(StringUtils.isNullOrEmpty(prop_val)) {
                            prop_val = String.valueOf(true);
                        }
                        if(this.getElster_processing_enabled() == null) {
                            this.setElster_processing_enabled(true);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getElster_processing_enabled() ));
                        }
                        if(this.getElster_processing_enabled() != Boolean.parseBoolean(prop_val)) {
                            this.setElster_processing_enabled(Boolean.parseBoolean(prop_val));
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getElster_processing_enabled() ));
                        }
                        break;
                    case elster_processing_batch_size_key:
                        logger.log(Level.INFO, String.format("BEFORE -> Property %s, Value %s", prop_key, this.getElster_processing_batch_size() ));
                        if(StringUtils.isNullOrEmpty(prop_val)) {
                            prop_val = String.valueOf(1000);
                        }
                        if(this.getElster_processing_batch_size() == null) {
                            this.setElster_processing_batch_size(1000);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getElster_processing_batch_size() ));
                        }
                        if(this.getElster_processing_batch_size() != Integer.parseInt(prop_val)) {
                            this.setElster_processing_batch_size(Integer.parseInt(prop_val));
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getElster_processing_batch_size() ));
                        }
                        break;
                    case meter_reading_gap_processor_enabled_key:
                        logger.log(Level.INFO, String.format("BEFORE -> Property %s, Value %s", prop_key, this.getMeter_reading_gap_processor_enabled() ));
                        if(StringUtils.isNullOrEmpty(prop_val)) {
                            prop_val = String.valueOf(true);
                        }
                        if(this.getMeter_reading_gap_processor_enabled() == null) {
                            this.setMeter_reading_gap_processor_enabled(true);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getMeter_reading_gap_processor_enabled() ));
                        }
                        if(this.getMeter_reading_gap_processor_enabled() != Boolean.parseBoolean(prop_val)) {
                            this.setMeter_reading_gap_processor_enabled(Boolean.parseBoolean(prop_val));
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getMeter_reading_gap_processor_enabled() ));
                        }
                        break;
                    case meter_reading_gap_processor_debug_enabled_key:
                        logger.log(Level.INFO, String.format("BEFORE -> Property %s, Value %s", prop_key, this.getMeter_reading_gap_processor_debug_enabled() ));
                        if(StringUtils.isNullOrEmpty(prop_val)) {
                            prop_val = String.valueOf(false);
                        }
                        if(this.getMeter_reading_gap_processor_debug_enabled() == null) {
                            this.setMeter_reading_gap_processor_debug_enabled(false);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getMeter_reading_gap_processor_debug_enabled() ));
                        }
                        if(this.getMeter_reading_gap_processor_debug_enabled() != Boolean.parseBoolean(prop_val)) {
                            this.setMeter_reading_gap_processor_debug_enabled(Boolean.parseBoolean(prop_val));
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getMeter_reading_gap_processor_debug_enabled() ));
                        }
                        break;
                    case meter_reading_gap_processor_batch_size_key:
                        logger.log(Level.INFO, String.format("BEFORE -> Property %s, Value %s", prop_key, this.getMeter_reading_gap_processor_batch_size() ));
                        if(StringUtils.isNullOrEmpty(prop_val)) {
                            prop_val = String.valueOf(10);
                        }
                        if(this.getMeter_reading_gap_processor_batch_size() == null) {
                            this.setMeter_reading_gap_processor_batch_size(10);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getMeter_reading_gap_processor_batch_size() ));
                        }
                        if(this.getMeter_reading_gap_processor_batch_size() != Integer.parseInt(prop_val)) {
                            this.setMeter_reading_gap_processor_batch_size(Integer.parseInt(prop_val));
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getMeter_reading_gap_processor_batch_size() ));
                        }
                        break;
                    case filtered_meters_key:
                        logger.log(Level.INFO, String.format("BEFORE -> Property %s, Value %s", prop_key, this.getFiltered_meters() ));
                        if(StringUtils.isNullOrEmpty(prop_val)) {
                            prop_val = "";
                        }
                        if(this.getFiltered_meters() == null) {
                            this.setFiltered_meters(prop_val);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getFiltered_meters() ));
                        }
                        if( !this.getFiltered_meters().equalsIgnoreCase(prop_val) ) {
                            this.setFiltered_meters(prop_val);
                            logger.log(Level.INFO, String.format("AFTER -> Property %s, Value %s", prop_key, this.getFiltered_meters() ));
                        }
                        break;
                    default:
                        logger.log(Level.WARNING, String.format("Property %s doesn't exist", prop_key ));
                }
            } catch(Exception ex) {
                ex.printStackTrace();
            }

        });

    }

}
