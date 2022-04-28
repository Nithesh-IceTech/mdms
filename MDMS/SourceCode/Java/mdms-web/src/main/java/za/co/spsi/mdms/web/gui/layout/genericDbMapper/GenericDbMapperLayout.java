package za.co.spsi.mdms.web.gui.layout.genericDbMapper;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Notification;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.ButtonType;
import de.steinwedel.messagebox.MessageBox;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.mdms.generic.meter.db.DbToDbMappingDetailEntity;
import za.co.spsi.mdms.generic.meter.db.DbToDbMappingEntity;
import za.co.spsi.mdms.util.DBUtil;
import za.co.spsi.mdms.web.gui.fields.MeterPlatformTypeCdField;
import za.co.spsi.mdms.web.gui.fields.MeterTypeCdField;
import za.co.spsi.toolkit.ano.Qualifier;
import za.co.spsi.toolkit.ano.Role;
import za.co.spsi.toolkit.ano.UI;
import za.co.spsi.toolkit.ano.UIField;
import za.co.spsi.toolkit.crud.gui.Group;
import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.Pane;
import za.co.spsi.toolkit.crud.gui.ano.EntityRef;
import za.co.spsi.toolkit.crud.gui.ano.UIGroup;
import za.co.spsi.toolkit.crud.gui.custom.ActionField;
import za.co.spsi.toolkit.crud.gui.custom.SwitchField;
import za.co.spsi.toolkit.crud.gui.fields.*;
import za.co.spsi.toolkit.crud.gui.render.VaadinNotification;
import za.co.spsi.toolkit.db.DSDB;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.drivers.Driver;
import za.co.spsi.toolkit.db.drivers.DriverFactory;
import za.co.spsi.toolkit.util.MaskId;
import za.co.spsi.toolkit.util.StringUtils;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.logging.Logger;

import static za.co.spsi.toolkit.crud.gui.render.AbstractView.getLocaleValue;

@Qualifier(roles = {@Role(value = "Supervisor")})
public class GenericDbMapperLayout extends Layout<DbToDbMappingEntity> implements ActionField.Callback, DynamicLookupField.Callback {

    public static final Logger TAG = Logger.getLogger(GenericDbMapperLayout.class.getName());

    ArrayList<String> vendorList = new ArrayList<String>(Arrays.asList("AAA",
            "ABB", "ABN", "ABR", "ACA", "ACB", "ACC", "ACE", "ACG", "OH", "ACL", "ACN", "ACS", "ACT", "ACW", "ADA", "ADE", "ADD", "ADF", "ADR", "ADX", "ADN", "ADU", "AEA", "AEC", "AEE", "AEG", "AEI", "AEL", "AEM", "AER", "AFX",
            "AGE", "AGT", "AHV", "ALC", "ALF", "ALG", "ALLES", "ALR", "OLD", "ALV", "AMB", "AMC", "AMD", "AME", "AMH", "AMI", "AML", "AMP", "AMR", "AMS", "AMT", "AMX", "ANA", "AND", "AOM", "AEON", "APA", "APL", "April", "APS",
            "APT", "APX", "AQL", "AQM", "AQT", "AQU", "ERA", "ARC", "ARD", "ARF", "POOR", "ARS", "ART", "ARW", "ASA", "ASC", "ASM", "ASR", "AST", "ATF", "ATI", "ATL", "ATM", "ATS", "AUR", "AUX", "AVA", "AXI", "AXS", "AYS", "AZE",
            "BAM", "BAR", "BAS", "BBE", "BBS", "BCE", "BCR", "BEE", "BEF", "BEG", "BER", "BFW", "BHG", "BJY", "BKB", "BKT", "BKO", "BLU", "BME", "BMI", "BMP", "BMT", "BNR", "BRA", "BSC", "BSE", "BSM", "BSP", "BSS", "BST", "BSX",
            "BTL", "BTR", "BTS", "BUR", "BYD", "BYL", "BYW", "BXC", "BZR", "CAH", "CAL", "CAR", "CAT", "CAV", "CBI", "CBS", "CCS", "CDL", "CEB", "CEL", "CEM", "CET", "CGC", "CGO", "CHC", "CHE", "CHM", "CIR", "CLA", "CLB", "CLE",
            "CLO", "CLT", "CLY", "CMC", "CMP", "CMT", "CMV", "CNM", "COH", "COM", "CON", "CPG", "CPL", "CPO", "CPS", "CQC", "CRD", "CRT", "CRW", "CRY", "CSC", "CSL", "CSP", "CTE", "CTL", "CTQ", "CTR", "CTT", "CTX", "CUC", "CUR",
            "CWA", "CWI", "CWV", "CYE", "CYI", "CYN", "CZA", "CZM", "DAE", "DAF", "DAN", "DBE", "DCD", "DDE", "DDI", "DDL", "DEA", "DEC", "DEL", "OF", "DEV", "DFE", "DFS", "DGC", "DGM", "DGY", "DHA", "DHQ", "THE", "DIG", "DJA",
            "DKY", "DIL", "DMC", "DME", "DMP", "DMS", "DNB", "DNO", "DNT", "DNV", "DOM", "DOS", "DPP", "DRT", "DSE", "DST", "DUA", "DVL", "DWZ", "DZG", "EAA", "EAH", "EAS", "SFBC", "EBV", "EBZ", "ECA", "ECC", "ECH", "ECL", "ECM",
            "ECO", "ECS", "EDI", "EDM", "EEC", "EEE", "EEO", "EEP", "EFA", "EFE", "EFI", "EFN", "ERA", "EFS", "EGA", "EGC", "EGD", "EGM", "EGW", "EGY", "EHL", "EIE", "EIT", "EKA", "EKO", "EKT", "ELD", "ELE", "ELG", "ELM", "ELO",
            "ELQ", "ELR", "ELS", "ELT", "ELV", "EMB", "EMC", "EME", "EMF", "EMH", "EMI", "EML", "EMM", "EMO", "EMS", "EMR", "EMT", "EMU", "END", "ENE", "CLOSELY", "ENI", "ENL", "ENM", "ENO", "ENP", "ENR", "ENS", "ENT", "ENX",
            "EPI", "EPL", "ERE", "ERI", "ERL", "ERN", "ERS", "ESA", "ESC", "ESE", "IT", "IT", "ESM", "ESO", "ESS", "ESY", "ETO", "EUE", "EUR", "EUS", "EVD", "EVK", "EVL", "EWA", "EEC", "EWT", "EXS", "EXT", "EYE", "EYT", "FAE",
            "FAN", "FAR", "FAS", "FED", "FFD", "FID", "FIM", "FIN", "FIO", "FLA", "FLD", "FLE", "FLG", "FLO", "FLS", "FLU", "FLW", "FLX", "FMG", "FML", "FMM", "FNX", "FPL", "FPR", "FRE", "FRU", "FSP", "FST", "FSY", "FTL", "FUS",
            "FUT", "FWS", "FZK", "GAV", "GBJ", "GCE", "GDS", "GEC", "GEE", "GEL", "GENE", "GEO", "GET", "GEX", "GFM", "GIL", "GIN", "GIO", "GLM", "GLX", "GMC", "GME", "GMM", "GMT", "GNY", "GOE", "GPM", "GRA", "GRE", "GRI", "GRS",
            "GRX", "GSP", "GSS", "GST", "GTE", "GTM", "GTR", "GTS", "GUH", "GWF", "GWI", "HAG", "HBY", "HCE", "HDX", "HDY", "HEG", "HEI", "HEK", "HEL", "HEM", "HER", "HEX", "HFI", "HFR", "HGM", "HIE", "HKK", "HLY", "HMI", "HML",
            "HMS", "HMU", "HND", "HOE", "HOL", "HON", "HOY", "HPL", "HPM", "HRM", "HRS", "HSD", "HST", "HTC", "HTI", "HTL", "HTS", "HUK", "HVT", "HWC", "HWM", "HWT", "HXD", "HXE", "HXW", "HYD", "HYE", "HYG", "HZC", "HZI", "HZZ",
            "IAC", "IBE", "ICB", "ICM", "ICP", "ICS", "ICT", "ICU", "IDE", "IDS", "IEC", "IEE", "IEI", "IES", "IFC", "IFX", "IGR", "IGS", "HIM", "IJE", "IJK", "IKE", "IKM", "IKS", "IMC", "IME", "IMS", "IMT", "INC", "IND",
            "INE", "INF", "INI", "INM", "INN", "IN O", "INP", "INS", "INT", "INV", "INX", "IOT", "IPD", "IRISHMAN", "ISA", "ISE", "ISF", "ISI", "ISK", "ISO", "ISS", "IS", "ITA", "ITB", "ITC", "ITE", "ITG", "ITH", "ITF", "ITI",
            "ICT", "ITP", "ITR", "ITS", "ITU", "ITW", "ITX", "ITZ", "IUS", "IWK", "IYI", "IZE", "JAC", "JAN", "JCE", "JED", "JGD", "JGF", "JHA", "JHM", "JJN", "JKW", "JMT", "JNC", "JNJ", "JOY", "JSM", "JSO", "JUM", "JWH", "JWR",
            "JWS", "JYS", "KAA", "CAME", "KAR", "CHEESE", "KAT", "KAW", "KBH", "KBK", "KBN", "KDS", "KEE", "KEL", "KER", "KES", "KFM", "KGE", "KHL", "KIG", "KKE", "KLE", "KLK", "KMB", "KMT", "KNI", "KNX", "KRO", "KRT", "KST",
            "KSY", "KTC", "LAC", "LAN", "READ", "LCG", "LCR", "LDE", "LEC", "LEM", "LFS", "LGB", "LGF", "LGC", "LGD", "LGG", "LGS", "LGU", "LGZ", "LHA", "LIT", "LJP", "LLM", "LMC", "LMG", "LML", "LNC", "LNK", "LNT", "LOG",
            "LOV", "LSC", "LSE", "LSK", "LSP", "LSZ", "LUG", "LUN", "LWT", "LYE", "MAC", "MAD", "MAE", "MAN", "MAT", "MAX", "MBS", "MCR", "MCS", "MDA", "MDE", "MDX", "MEC", "MED", "MEE", "MEH", "MEI", "MEL", "MEM", "MET",
            "MHT", "MIC", "MID", "MII", "MIJ", "MIK", "MIM", "ME", "MIS", "MKE", "WITH", "MKL", "MKS", "MLQ", "MMC", "MMI", "MMS", "MMT", "MNS", "MNW", "MOS", "MOT", "MPA", "MPR", "MPS", "MRI", "MSB", "MSE", "MSM", "MSO",
            "MST", "MSY", "MTD", "MTC", "MTH", "MTI", "MTL", "MTM", "MTN", "MTP", "MTR", "MTS", "MTX", "MUK", "MWU", "MXM", "MYS", "NAE", "NAR", "NAT", "NCK", "NDF", "NDM", "NEE", "NES", "NET", "NIK", "NIS", "NJC", "NKS",
            "NLI", "NMG", "NMS", "NMT", "NNT", "NOQ", "November", "NPS", "NPT", "NRM", "NRN", "NSE", "NTC", "NTM", "NVD", "NVN", "NWM", "NXP", "NYG", "NYN", "NZR", "OAS", "OBR", "OBC", "OBE", "ODI", "ODK", "OEE", "OLI", "OMS",
            "ONR", "ONS", "OPT", "ORB", "ORM", "OSA", "OSK", "OYK", "OZK", "PAD", "PAF", "PAK", "PAN", "PCE", "PCR", "PDE", "PDX", "PEA", "PEE", "PEL", "PEP", "PGP", "PHL", "PII", "PIK", "PIL", "PIM", "PIP", "PLN", "PLO", "PLU",
            "PMG", "PMP", "PMS", "PMX", "PNC", "POD", "POLE", "POW", "POZ", "PPC", "PRE", "PPS", "PRI", "PRG", "PRM", "PER", "PRY", "PSE", "PST", "PTI", "PUK", "PVT", "PWB", "PWR", "PXC", "PYU", "QDR", "QDS", "QFP", "QTS", "RAC",
            "WHEEL", "R.A.M.", "RAS", "RAY", "RBM", "RCE", "REC", "REF", "REI", "REL", "REM", "RES", "RIC", "RIL", "RIM", "RIT", "RIX", "RIZ", "RKE", "RMA", "RMG", "RML", "RMR", "RMT", "RNW", "RSA", "RSM", "RSW", "SAA", "SAC",
            "SAE", "SAY", "SAM", "SAN", "SAP", "SAT", "SBC", "SCA", "SCE", "SCH", "SCM", "SCR", "SCT", "SCW", "SDC", "SDM", "SDS", "SEC", "LAKE", "SEH", "BE", "SEL", "SEN", "SEO", "SET", "SFI", "SFT", "SGA", "SGM", "SGN", "SGX",
            "SHD", "SHE", "SHM", "SHT", "SIC", "YOU", "SIG", "SIJ", "SIL", "SIM", "SIN", "SIT", "SIV", "SIX", "SKI", "SKK", "SKT", "SLB", "SLP", "SLV", "SLW", "SLX", "SMA", "SMC", "SME", "SMG", "SMI", "SML", "SMM", "SMN", "SMP",
            "SMQ", "SMS", "SMT", "SMX", "SNM", "SNR", "SNS", "SOC", "SOF", "SOG", "SOL", "SOM", "SON", "SOS", "SOT", "SPE", "SPL", "SPX", "SPZ", "SRE", "SRF", "SRN", "SRV", "SSI", "SSM", "SSN", "SST", "STA", "STC", "HOURS", "STF",
            "STM", "STO", "STR", "STS", "STV", "STZ", "SUN", "SVM", "SVT", "SWI", "SWM", "SWS", "SWT", "SYC", "SYN", "SYS", "SYX", "SZS", "DAY", "TAS", "DID", "TAY", "TBN", "TBS", "TCE", "TCH", "TCO", "TCT", "TCX", "TCZ", "TDC", "TEA",
            "TEC", "TEI", "TEK", "TEO", "TEP", "TEU", "TFC", "TGX", "THE", "TIC", "TIG", "TII", "TIL", "TIP", "TIS", "TIX", "TKS", "TLC", "TLM", "TLR", "TLS", "TLT", "TMK", "TMS", "TOP", "TPB", "TPC", "TPJ", "TPL", "TPI", "TRC", "TRI",
            "TRJ", "TRL", "TRN", "TRU", "TRV", "TRX", "TSD", "TSG", "TTM", "TTR", "TTT", "DOOR", "TWM", "TWO", "TXL", "UAG", "UBI", "UBY", "UEI", "UGI", "UGT", "UHM", "UNIVERSITY", "URM", "USC", "UTF", "UTI", "UTL", "UTT", "VAL", "VDP",
            "VEC", "VEL", "VER", "VES", "VGO", "VIE", "VIK", "VIN", "VIP", "VLT", "VMP", "VNE", "CPI", "VSE", "VTC", "VTK", "YTL", "VTZ", "WAD", "WAH", "WAI", "WHALE", "WDN", "WEB", "PATH", "SORE", "WEL", "WEP", "WFT", "WGP", "TIG", "WIN",
            "WKL", "WKX", "WMO", "WNC", "WNW", "WOW", "WSD", "WSE", "WSR", "WTI", "WTL", "WTM", "WTT", "WUR", "WZG", "WZT", "XAO", "XEM", "XJM", "XMA", "XMX", "XTM", "XTR", "XTY", "YDD", "YDS", "YFC", "YGM", "YHE", "YNP", "YPP", "YSS", "YTE",
            "YYW", "YZR", "ZAG", "ZAP", "ZAR", "ZEL", "ZEN", "ZFY", "ZGX", "ZIP", "ZIV", "ZJY", "ZPA", "ZRI", "ZRM", "ZTE", "ZTX", "ZTY", "ZYX", "ZYB", "ZZZ"));


    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @EntityRef(main = true)
    private DbToDbMappingEntity dbToDbMappingEntity = new DbToDbMappingEntity();

    @UIGroup(column = 0)
    public Group statusDetail = new Group("Status Detail", this);

    @UIField(setDefault = "0")
    @UI(width = "-1px")
    public SwitchField active = new SwitchField(dbToDbMappingEntity.active, "Active", this);

    @UIField(setDefault = "0")
    @UI(width = "-1px")
    public SwitchField live = new SwitchField(dbToDbMappingEntity.live, "Live", this);

    @UIField(setDefault = "0", enabled = false)
    @UI(width = "-1px")
    public SwitchField dbActive = new SwitchField(dbToDbMappingEntity.dbActive, "DB Active", this);

    @UIField(enabled = false, visible = false)
    public ImageField statusGridImage = new ImageField("Active", this, "STATUS_IMAGE",
            new ImageField.Callback() {
                @Override
                public com.vaadin.server.Resource getResource() {
                    return new ThemeResource(!active.getNonNull() ? "../toolkit/img/red_dot_16.png" : "../toolkit/img/green_dot_16.png");
                }

                @Override
                public Integer getItemId() {
                    return !active.getNonNull() ? 0 : 1;
                }
            });

    @UIField(enabled = false, visible = false)
    public ImageField liveGridImage = new ImageField("Live", this, "LIVE_IMAGE",
            new ImageField.Callback() {
                @Override
                public com.vaadin.server.Resource getResource() {
                    return new ThemeResource(!live.getNonNull() ? "../toolkit/img/red_dot_16.png" : "../toolkit/img/green_dot_16.png");
                }

                @Override
                public Integer getItemId() {
                    return !live.getNonNull() ? 0 : 1;
                }
            });

    @UIField(enabled = false, visible = false)
    public ImageField dbActiveGridImage = new ImageField("DB Active", this, "DB_ACTIVE_IMAGE",
            new ImageField.Callback() {
                @Override
                public com.vaadin.server.Resource getResource() {
                    return new ThemeResource(!dbActive.getNonNull() ? "../toolkit/img/red_dot_16.png" : "../toolkit/img/green_dot_16.png");
                }

                @Override
                public Integer getItemId() {
                    return !dbActive.getNonNull() ? 0 : 1;
                }
            });

    @UIGroup(column = 0)
    public Group dbConnectionDetail = new Group("DB connection Detail", this);

    @UIField(mandatory = true)
    public LField serverAddress = new LField(dbToDbMappingEntity.serverAddress, "Server Address", this);

    @UIField(mandatory = true)
    public LField portNumber = new LField(dbToDbMappingEntity.portNumber, "Port Number", this);

    @UIField(uppercase = false)
    public LField dbName = new LField(dbToDbMappingEntity.dbName, "Database Name", this);

    @UIField(uppercase = false)
    public LField serviceName = new LField(dbToDbMappingEntity.serviceName, "Service Name", this);

    @UIField(mandatory = true)
    public DriverLookupField driver = new DriverLookupField(dbToDbMappingEntity.driver, "Driver", this);

    @UIField(mandatory = true, uppercase = false)
    public LField userName = new LField(dbToDbMappingEntity.userName, "Username", this);

    @UIField(mandatory = true, mask = MaskId.ANY, min = 6, max = 20, uppercase = false)
    public PasswordField password = new PasswordField(dbToDbMappingEntity.password, "Password", this);

    @UIField()
    public ActionField testConnection = new ActionField("Test Connection", FontAwesome.WRENCH, this, this);

    @UIGroup(column = 1)
    public Group sqlDetail = new Group("SQL Query", this);

    @UIField(mandatory = true, rows = 5, enabled = false)
    public TextAreaField sqlSelect = new TextAreaField(dbToDbMappingEntity.sqlSelect, "SQL Select Columns", this);

    @UIField(mandatory = true, rows = 5, enabled = false)
    public TextAreaField sqlFrom = new TextAreaField(dbToDbMappingEntity.sqlFrom, "SQL Select From", this);

    @UIField()
    public ActionField testSql = new ActionField("Test Sql", FontAwesome.WRENCH, this, this);

    @UIField(enabled = false, rows = 15, uppercase = false)
    public TextAreaField columns = new TextAreaField(dbToDbMappingEntity.columnFields, "Columns", this);

    @UIGroup(column = 0)
    public Group db2DbMapping = new Group("DB-to-DB Mapping Detail", this);

    @UIField(mandatory = true)
    public LField mapName = new LField(dbToDbMappingEntity.mapName, "Map Name", this);

    @UIGroup(column = 0)
    public Group meterDetail = new Group("Meter Detail", this);

    @UIField()
    public DynamicLookupField meterId = new DynamicLookupField(dbToDbMappingEntity.meterId, "Meter ID", this, this);

    @UIField()
    public DynamicLookupField meterReadingId = new DynamicLookupField(dbToDbMappingEntity.meterReadingId, "Meter Reading ID", this, this);

    @UIField(mandatory = true)
    public DynamicLookupField meterSerialN = new DynamicLookupField(dbToDbMappingEntity.meterSerialN, "Meter Serial Number", this, this);

    @UIField(mandatory = true)
    public DynamicLookupField timestamp = new DynamicLookupField(dbToDbMappingEntity.timestamp, "Timestamp", this, this);

    @UIField(mandatory = true)
    public LField<Integer> timeZoneOffsetToUtc = new LField(dbToDbMappingEntity.timeZoneOffsetToUtc, "Timezone Offset", this);

    @UIField()
    public DynamicLookupField meterManId = new DynamicLookupField(dbToDbMappingEntity.meterManId, "Meter ManID", this, this);

    @UIField(mandatory = true)
    public DynamicLookupField vendorPrefix = new DynamicLookupField(dbToDbMappingEntity.vendorPrefix, "Vendor Prefix", this,
            () -> {
                Map<String, String> map = new TreeMap<>();
                vendorList.forEach(i -> map.put(i, i));
                return map;
            });

    @UIField(mandatory = true)
    public MeterTypeCdField meterType = new MeterTypeCdField(dbToDbMappingEntity.meterType, this);

    @UIField(mandatory = true)
    public MeterPlatformTypeCdField meterPlatformTypeCdField = new MeterPlatformTypeCdField(dbToDbMappingEntity.meterPlatformType, this);

    public Pane mappingDetail = new Pane("Mapping Details",
            " select * " +
                    " from DB_TO_DB_MAPPING_DETAIL " +
                    " where DB_TO_DB_MAPPING_DETAIL.DB_TO_DB_MAPPING_ID = ?",
            GenericDbMapperDetailLayout.class, this);

    public Group nameGroup = new Group("", this, statusGridImage, liveGridImage, dbActiveGridImage, mapName,
            meterPlatformTypeCdField, serverAddress, portNumber, dbName, driver, userName).setNameGroup();

    public Pane detailPane = new Pane("", this, statusDetail, dbConnectionDetail, sqlDetail, db2DbMapping, meterDetail);

    private Boolean originalActiveValue = false;

    public GenericDbMapperLayout() {
        super("Generic Connection Detail");
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public String getMainSql() {
        Driver driver = DriverFactory.getDriver();
        return String.format("select *, " +
                "CASE WHEN active is null or ACTIVE = %s " +
                "THEN '../toolkit/img/red_dot_16.png' " +
                "ELSE '../toolkit/img/green_dot_16.png' END as STATUS_IMAGE, " +

                "CASE WHEN db_active is null or db_active = %s " +
                "THEN '../toolkit/img/red_dot_16.png' " +
                "ELSE '../toolkit/img/green_dot_16.png' END as DB_ACTIVE_IMAGE, " +

                "CASE WHEN LIVE is null or LIVE = %s " +
                "THEN '../toolkit/img/red_dot_16.png' " +
                "ELSE '../toolkit/img/green_dot_16.png' END as LIVE_IMAGE " +
                "FROM DB_TO_DB_MAPPING ",
                driver.boolToNumber(false),
                driver.boolToNumber(false),
                driver.boolToNumber(false));
    }

    @Override
    public void callback(ActionField source) {
        if (source == testConnection) {
            testDbConnection();
        } else if (source == testSql) {
            runSqlQuery();
        }
    }

    private void runSqlQuery() {

        try {

            DataSource dataSource = DBUtil.createDataSource(driver.getVaadinField().getValue().toString(),
                    serverAddress.getVaadinField().getValue().toString(), serviceName.getVaadinField().getValue().toString(),
                    Integer.parseInt(portNumber.getVaadinField().getValue().toString()),
                    dbName.getVaadinField().getValue().toString(), userName.getVaadinField().getValue().toString(),
                    password.getVaadinField().getValue().toString());

            try( Connection connection = dataSource.getConnection() ) {

                String query = DBUtil.returnFirstRows(driver.getVaadinField().getValue().toString(),
                        "Select " + sqlSelect.getVaadinField().getValue().toString() +
                                " from " + sqlFrom.getVaadinField().getValue().toString(), 1);

                try( Statement stmt = connection.createStatement() ) {

                    ResultSet rs = stmt.executeQuery(query);

                    connection.close();
                    columns.set(DBUtil.trimSql(sqlSelect.getVaadinField().getValue().toString().trim()));
                    columns.intoControl();
                    populateLookupFields();

                } catch (SQLException e) {
                    throw e;
                }

            }

        } catch (Exception e) {
            VaadinNotification.show("Connection failed", e.getMessage(), Notification.Type.ERROR_MESSAGE);
        }
    }

    private void testDbConnection() {

        try {
            DataSource remoteDataSource = DBUtil.createDataSource(
                    driver.getVaadinField().getValue().toString(),
                    serverAddress.getVaadinField().getValue().toString(),
                    serviceName.getVaadinField().getValue().toString(),
                    Integer.parseInt(portNumber.getVaadinField().getValue().toString()),
                    dbName.getVaadinField().getValue().toString(),
                    userName.getVaadinField().getValue().toString(),
                    password.getVaadinField().getValue().toString());

            // test connection
            remoteDataSource.getConnection().close();
            statusGridImage.buildVaadinField();
            statusGridImage.intoControl();
            dbActive.set(true);
            // Show tables
            Notification.show("Connection established", "The connection was successful", Notification.Type.TRAY_NOTIFICATION);
        } catch (Exception e) {
            dbActive.set(false);
            statusGridImage.intoControl();
            VaadinNotification.show("Connection failed", e.getMessage(), Notification.Type.ERROR_MESSAGE);
        }
    }

    @Override
    public Map<String, String> getLookupCation() {
        Map<String, String> returnedMap = new TreeMap<>();

        if (!StringUtils.isEmpty(columns.get())) {
            List<String> columnsList = new ArrayList<String>(Arrays.asList(columns.getNonNull().split(",")));
            for (String value : columnsList) {
                returnedMap.put(value, value);
            }
        }
        return returnedMap;
    }

    private void populateLookupFields() {
        meterId.populateLookupFieldComboBox();
        meterId.intoControl();

        meterId.populateLookupFieldComboBox();
        meterId.intoControl();

        meterReadingId.populateLookupFieldComboBox();
        meterReadingId.intoControl();

        meterSerialN.populateLookupFieldComboBox();
        meterSerialN.intoControl();

        timestamp.populateLookupFieldComboBox();
        timestamp.intoControl();

        meterManId.populateLookupFieldComboBox();
        meterManId.intoControl();

        vendorPrefix.populateLookupFieldComboBox();
        vendorPrefix.intoControl();
    }

    @Override
    public boolean save() {
        if (active.getVaadinField().getValue().toString().equals("false") &&
                live.getVaadinField().getValue().toString().equals("true")) {

            VaadinNotification.show("Save failed",
                    "Cant set live status to true if active is not set to true.",
                    Notification.Type.ERROR_MESSAGE);
            return false;
        }

        if (live.getVaadinField().getValue().toString().equals("true")) {

            MessageBox.createQuestion().asModal(true).withCaption("Import Data").
                    withMessage("Live indicator has been set, once set it can't be unset. Continue?").
                    withButton(ButtonType.OK, () -> {
                        doSave();
                    }).withCancelButton(ButtonOption.closeOnClick(true)).open();
            return false;

        } else {
            return doSave();
        }
    }

    private boolean doSave() {
        originalActiveValue = active.get();
        if (super.save()) {
            if ((Boolean) active.getVaadinField().getValue()) {
                setReadonlyFields();

                if (!originalActiveValue) {
                    for (DbToDbMappingDetailEntity dbToDbMappingDetailEntity :
                            dbToDbMappingEntity.dbToDbMappingDetailEntityEntityRef.getAllAsList(dataSource)) {

                        GenericDbMapperDetailLayout.deleteDataForMapping(dataSource, dbToDbMappingDetailEntity);
                        dbToDbMappingDetailEntity.lastSyncTime.set(new Timestamp(0));

                        try {
                            DSDB.setUpdate(dataSource, dbToDbMappingDetailEntity);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }

            if ((Boolean) live.getVaadinField().getValue()) {

                live.getProperties().setReadOnly(true);
                live.applyProperties();

                active.getProperties().setReadOnly(true);
                active.applyProperties();

                // Update all records to live via mapping table for this mapping
                Driver driver = DriverFactory.getDriver();
                DataSourceDB.execute(dataSource,
                        String.format("update GENERIC_METER " +
                                        " set live = %s " +
                                        " where GENERIC_METER_ID in " +
                                        "  (select distinct (METER_ID) from DB_TO_DB_MAP_METER where DB_TO_DB_MAPPING_ID = '%s')",
                                driver.boolToNumber(true),
                                dbToDbMappingEntity.dbToDbMappingId.get()));
            }
            return true;
        } else {
            return false;
        }
    }

    private void setReadonlyFields() {

        sqlDetail.getFields().forEach(field -> {
            field.getProperties().setReadOnly(true);
            field.applyProperties();
        });

        meterDetail.getFields().forEach(field -> {
            field.getProperties().setReadOnly(true);
            field.applyProperties();
        });

        db2DbMapping.getFields().forEach(field -> {
            field.getProperties().setReadOnly(true);
            field.applyProperties();
        });
    }

    private void setNotReadonlyFields() {

        sqlDetail.getFields().forEach(field -> {
            field.getProperties().setReadOnly(false);
            field.applyProperties();
        });

        meterDetail.getFields().forEach(field -> {
            field.getProperties().setReadOnly(false);
            field.applyProperties();
        });


        db2DbMapping.getFields().forEach(field -> {
            field.getProperties().setReadOnly(false);
            field.applyProperties();
        });

    }

    @Override
    public void beforeOnScreenEvent() {
        super.beforeOnScreenEvent();

        if (live.get()) {

            live.getProperties().setReadOnly(true);
            live.applyProperties();

            active.getProperties().setReadOnly(true);
            active.applyProperties();
        }

        if (active.getNonNull()) {
            setReadonlyFields();
        } else {
            setNotReadonlyFields();
        }

        columns.getProperties().setReadOnly(true);
        columns.applyProperties();

    }

    @Override
    public boolean delete(EntityDB entityDB) {

        DbToDbMappingEntity dbToDbMappingEntity = (DbToDbMappingEntity) entityDB;

        if (dbToDbMappingEntity.active.get() || dbToDbMappingEntity.live.get()) {

            VaadinNotification.show("Delete failed",
                    "Cant delete record thats been set as active or live.",
                    Notification.Type.ERROR_MESSAGE);
            return false;
        } else {

            deleteDataForMapping(dbToDbMappingEntity);
            DataSourceDB.delete(getDataSource(), entityDB);
            // refresh
            layoutViewGrid.getSqlContainer().refresh();
            VaadinNotification.show(String.format(getLocaleValue(ToolkitLocaleId.ENTITY_DELETED), getCaption()),
                    Notification.Type.TRAY_NOTIFICATION);
            return true;
        }
    }

    public void deleteDataForMapping(DbToDbMappingEntity dbToDbMappingEntity) {
        try (Connection localConnection = dataSource.getConnection()) {
            localConnection.setAutoCommit(false);
            try {

                DataSourceDB.execute(localConnection,
                        String.format(
                                "delete from GENERIC_METER " +
                                        " where GENERIC_METER_ID in (select DB_TO_DB_MAP_METER.METER_ID from DB_TO_DB_MAP_METER" +
                                        "   where DB_TO_DB_MAPPING_ID = '%s')" +
                                        " and GENERIC_METER_ID NOT in(select DB_TO_DB_MAP_METER.METER_ID from DB_TO_DB_MAP_METER " +
                                        "   where DB_TO_DB_MAPPING_ID != '%s')",
                                dbToDbMappingEntity.dbToDbMappingId.get(), dbToDbMappingEntity.dbToDbMappingId.get()));

                DataSourceDB.execute(localConnection,
                        String.format("delete from DB_TO_DB_MAP_METER  where DB_TO_DB_MAPPING_ID = '%s'",
                                dbToDbMappingEntity.dbToDbMappingId.get()));

                localConnection.commit();
            } catch (Exception e) {
                localConnection.rollback();
                throw new RuntimeException(e);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
