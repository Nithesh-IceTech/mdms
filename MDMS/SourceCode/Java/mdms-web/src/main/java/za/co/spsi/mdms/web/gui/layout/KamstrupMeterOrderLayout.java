package za.co.spsi.mdms.web.gui.layout;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Notification;
import lombok.SneakyThrows;
import za.co.spsi.mdms.kamstrup.db.KamstrupMeterOrderEntity;
import za.co.spsi.mdms.nes.util.NESUtil;
import za.co.spsi.mdms.web.gui.fields.StatusCdField;
import za.co.spsi.toolkit.ano.UIField;
import za.co.spsi.toolkit.crud.gui.*;
import za.co.spsi.toolkit.crud.gui.ano.EntityRef;
import za.co.spsi.toolkit.crud.gui.ano.PlaceOnToolbar;
import za.co.spsi.toolkit.crud.gui.ano.UIGroup;
import za.co.spsi.toolkit.crud.gui.custom.ActionField;
import za.co.spsi.toolkit.crud.gui.custom.DownloadField;
import za.co.spsi.toolkit.crud.gui.fields.LocalTimestampField;
import za.co.spsi.toolkit.crud.gui.fields.TextAreaField;
import za.co.spsi.toolkit.crud.util.DownloaderHelper;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.io.IOUtil;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Base64;

/**
 * Created by jaspervdb on 2016/04/19.
 */

public class KamstrupMeterOrderLayout extends Layout<KamstrupMeterOrderEntity> implements DownloaderHelper.Callback,ActionField.Callback {

    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @EntityRef(main = true)
    private KamstrupMeterOrderEntity order = new KamstrupMeterOrderEntity();

    @UIGroup(column = 0)
    public Group refGroup = new Group("References",this);

    @UIField(uppercase = false)
    public LField ref = new LField(order.ref,"Ref",this);
    @UIField(uppercase = false)
    public LField statusRef = new LField(order.statusRef,"Status Ref",this);
    @UIField(uppercase = false)
    public LField completedRef = new LField(order.completedRef,"Completed Ref",this);

    @UIGroup(column = 0)
    public Group statusGroup = new Group("Status",this);

    public StatusCdField status = new StatusCdField(order.status,this);

    public LocalTimestampField fromDate = new LocalTimestampField(order.fromDate,"From date",this);
    public LocalTimestampField toDate = new LocalTimestampField(order.toDate,"To date",this);

    public LField succeeded = new LField(order.succeeded,"Succeeded",this);
    public LField failed = new LField(order.failed,"Failed",this);
    public LField aborted = new LField(order.aborted,"Aborted",this);
    public LField waiting = new LField(order.waiting,"Waiting",this);

    public LField commands = new LField(order.commands,"Commands",this);

    @UIGroup(column = 0)
    public Group processingTimeGroup = new Group("Processing time",this);

    public LocalTimestampField created = new LocalTimestampField(order.created,"Created",this);
    public LField updated = new LField(order.updated,"Updated",this).setDateFormat("dd/MM/yyyy HH:mm");

    @UIField(rows = 5,uppercase = false)
    public TextAreaField error = new TextAreaField(order.error,"Error",this);

    public LField errorRetry = new LField(order.errorRetry,"Error retry",this);

    @PlaceOnToolbar
    public DownloadField openData = new DownloadField("Download", FontAwesome.DOWNLOAD,this,"Order Data.xml",this);
    @PlaceOnToolbar
    public ActionField reprocess = new ActionField("Re Process", FontAwesome.WRENCH,this,this);

    public Group nameGroup = new Group("",this,created,ref, status,succeeded,failed).setNameGroup();

    public Pane detailPane = new Pane("",this, refGroup,statusGroup,processingTimeGroup);

    public Pane failedLogs = new Pane("Failed Meter Re-Orders",
            KamstrupMeterReadFailedLogLayout.getMainSQL("kam_meter_read_failed_log.order_id = ?"),KamstrupMeterReadFailedLogLayout.class,new Permission(0),this);

    public KamstrupMeterOrderLayout() {
        super("Meter Order Detail");
        setPermission(new Permission(0));
    }

    @PostConstruct
    private void init() {
        replaceField(status,order.status);
        replaceField(error,order.error);
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public void beforeOnScreenEvent() {
        super.beforeOnScreenEvent();
        openData.getComponent().setEnabled(true);
    }

    @Override
    public void intoControl() {
        super.intoControl();
        openData.getComponent().setEnabled(true);
    }

    @Override
    public String getMainSql() {
        return "select * from KAMSTRUP_METER_ORDER order by created desc";
    }

    @SneakyThrows
    private File getAsFile(byte data[]) {
        File tmpFile = File.createTempFile("kamstrup","data");
        try (FileOutputStream fos = new FileOutputStream(tmpFile)) {
            fos.write(data);
            fos.flush();
            return tmpFile;
        }

    }
    @Override
    public File getFile() throws Exception {
        // load order
        KamstrupMeterOrderEntity tmp = DataSourceDB.loadFromId(getDataSource(),new KamstrupMeterOrderEntity(), order.meterOrderId.get());
        // TODO - Remove support for response
        if (tmp.data.get() != null) {
            return getAsFile(tmp.data.getInflated());
        } else if (tmp.response.get() != null) {
            return getAsFile(IOUtil.unzip(Base64.getDecoder().decode(tmp.response.get())));
        }
        return null;
    }

    @Override
    public String[] getActions() {
        return super.getActions();
    }

    @Override
    public void callback(ActionField source) {
        if (status.get().equals(KamstrupMeterOrderEntity.Status.FAILED.getCode())) {
            order.status.set(KamstrupMeterOrderEntity.Status.RECEIVED.getCode());
            order.error.set((String) null);
            DataSourceDB.set(getDataSource(),order);
            intoControl();
            com.vaadin.ui.Notification.show("Re processing scheduled");
        } else {
            com.vaadin.ui.Notification.show("Only Error states can be re processed", Notification.Type.ERROR_MESSAGE);
        }
    }

    @Override
    public String[][] getFilters() {
        return new String[][]{{"Accepted","Filter all accepted orders","select * from kamstrup_meter_order_layout where status <> 5 order by created desc"},
                {"Rejected","Filter all rejected orders","select * from kamstrup_meter_order_layout where status = 5 order by created desc"}};
    }
}
