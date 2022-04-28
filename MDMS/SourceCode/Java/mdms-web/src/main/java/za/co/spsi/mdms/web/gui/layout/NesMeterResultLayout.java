package za.co.spsi.mdms.web.gui.layout;

import com.vaadin.server.FontAwesome;
import za.co.spsi.mdms.nes.db.NESMeterResultEntity;
import za.co.spsi.mdms.nes.util.NESUtil;
import za.co.spsi.toolkit.crud.gui.*;
import za.co.spsi.toolkit.crud.gui.ano.EntityRef;
import za.co.spsi.toolkit.crud.gui.ano.PlaceOnToolbar;
import za.co.spsi.toolkit.crud.gui.ano.UIGroup;
import za.co.spsi.toolkit.crud.gui.custom.DownloadField;
import za.co.spsi.toolkit.crud.gui.fields.TextAreaField;
import za.co.spsi.toolkit.crud.util.DownloaderHelper;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.io.IOUtil;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import static za.co.spsi.mdms.nes.db.NESMeterResultEntity.Status.IGNORED;

/**
 * Created by jaspervdb on 2016/04/19.
 */

public class NesMeterResultLayout extends Layout<NESMeterResultEntity> implements DownloaderHelper.Callback {

    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @EntityRef(main = true)
    private NESMeterResultEntity result = new NESMeterResultEntity();

    @UIGroup(column = 0)
    public Group detailGroup = new Group("Details",this);

    public LField resultDateTime = new LField(result.resultDateTime,"Entry time",this);

    public LField dateTimeStamp = new LField(result.dateTimeStamp,"Update time",this);

    public LField entitySerialNumber = new LField(result.entitySerialNumber,"Comparison serial number",this);

    public LField routingEntityName = new LField(result.routingEntityName,"Routing number",this);

    @UIGroup(column = 0)
    public Group statusGroup = new Group("Status",this);

    public LField status = new LField(result.status,"Status",this);

    public TextAreaField error = new TextAreaField(result.error,"Error",this);

    @PlaceOnToolbar
    public DownloadField openData = new DownloadField("Download", FontAwesome.DOWNLOAD,this,"Order Data.xml",this);

    public Group nameGroup = new Group("",this,status,dateTimeStamp,entitySerialNumber).setNameGroup();

    public Pane detailPane = new Pane("",this, detailGroup,statusGroup);


    public NesMeterResultLayout() {
        super("Meter Order Detail");
        setPermission(new Permission(0));
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public String getMainSql() {
        return "select * from nes_meter_result order by date_time_stamp desc";
    }

    @Override
    public File getFile() throws Exception {
        // load order
        NESMeterResultEntity tmp = DataSourceDB.loadFromId(getDataSource(),new NESMeterResultEntity(), result.meterResultId.get());
        File tmpFile = File.createTempFile("nes","data");
        try (FileOutputStream fos = new FileOutputStream(tmpFile)) {
            fos.write(NESUtil.Decompress(tmp.resultData.getInflatedString()).getBytes());
            fos.flush();
            return tmpFile;
        }
    }

    @Override
    public String[] getActions() {
        return super.getActions();
    }

    @Override
    public String[][] getFilters() {
        return new String[][]{{"Failed","Filter all failed orders","select * from nes_meter_result where status = " + IGNORED.code + " order by created desc"}};
    }
}
