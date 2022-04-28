package za.co.spsi.mdms.web.gui.layout;

import com.vaadin.ui.Notification;
import za.co.spsi.mdms.kamstrup.db.KamstrupGroupEntity;
import za.co.spsi.mdms.kamstrup.services.group.KamstrupRestService;
import za.co.spsi.toolkit.ano.UIField;
import za.co.spsi.toolkit.crud.gui.*;
import za.co.spsi.toolkit.crud.gui.ano.EntityRef;
import za.co.spsi.toolkit.crud.gui.ano.UIGroup;
import za.co.spsi.toolkit.crud.gui.fields.ComboBoxField;
import za.co.spsi.toolkit.crud.gui.fields.TextAreaField;
import za.co.spsi.toolkit.db.EntityDB;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;
import javax.ws.rs.core.Response;
import java.util.Arrays;

/**
 * Created by jaspervdb on 2016/04/19.
 */

public class KamstrupGroupLayout extends Layout {

    @Resource(mappedName = "java:/jdbc/mdms")
    private DataSource dataSource;

    @Inject
    private KamstrupRestService groupService;

    @EntityRef(main = true)
    private KamstrupGroupEntity group = new KamstrupGroupEntity();

    @UIGroup(column = 0)
    public Group refGroup = new Group("Group Detail", this).setNameGroup();

    public LField enabled = new LField(group.enabled, "Enabled", this);

    @UIField(mandatory = true, writeOnce = true)
    public LField<String> name = new LField(group.name, "Name", this);
    @UIField(writeOnce = true)//,regex = UIConstants.LOGGER_ID_REGEX)
    public LField loggerId = new LField(group.loggerId, "Logger Id", this);

    @UIField(mandatory = true,writeOnce = true)
    public ComboBoxField<String> frequencyType = new ComboBoxField<String>(group.frequencyType, "Frequency Type",
            KamstrupGroupEntity.FREQUENCY_TYPE_OPTION,KamstrupGroupEntity.FREQUENCY_TYPE_OPTION,this);

    @UIField(mandatory = true,writeOnce = true)
    public LField frequency = new LField(group.frequency, "Frequency", this);

    @UIField(enabled = false,uppercase = false)
    public LField ref = new LField(group.ref, "Reference", this);

    public TextAreaField description = new TextAreaField(group.description, "Description", this);

    public Pane detailPane = new Pane("Group Details", this, refGroup);

    public Pane registersPane = new Pane("Registers","select * from kamstrup_group_register where kamstrup_group_register.group_id = ? order by register_id desc",KamstrupGroupRegisterLayout.class,this);

    public Pane orderPane = new Pane("Orders", "select * from kamstrup_meter_order where kamstrup_meter_order.group_id = ? order by created desc", KamstrupMeterOrderLayout.class,new Permission(0), this);

    public Pane meterPane = new Pane("Meters", "select * from kamstrup_meter,kamstrup_group_meter where kamstrup_meter.meter_id = kamstrup_group_meter.meter_id and kamstrup_group_meter.group_id = ?", KamstrupMeterLayout.class,new Permission(Permission.PERMISSION_WRITE), this);

    public Pane logPane = new Pane("Logs", "select * from kamstrup_group_log where kamstrup_group_log.group_id = ? order by entry_time desc", KamstrupGroupLogLayout.class,new Permission(0), this);

    public KamstrupGroupLayout() {
        super("Group");
    }

    @Override
    public String getMainSql() {
        return "select * from KAMSTRUP_GROUP order by name asc";
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Override
    public boolean save() {
        // validate logger id
        if (!group.isInDatabase()) {
            intoBindings();
            // check if group already exists
            if (groupService.getGroups().stream().filter(g -> g.groups != null).flatMap(g -> Arrays.stream(g.groups)).
                    anyMatch(g -> name.get().equalsIgnoreCase(g.name))) {
                Notification.show(String.format("Group by name %s already exists", name.get()), Notification.Type.ERROR_MESSAGE);
                return false;
            }
            if (groupService.executeRequest(Response.Status.CREATED, () ->
                            groupService.createGroup(getDataSource(), group),"Create group")) {
                ref.intoControl();
            } else {
                return false;
            }
        }
        return super.save();
    }

    @Override
    public boolean delete(EntityDB entityDB) {
        // first delete the group by ref
        Response response = groupService.deleteGroup((KamstrupGroupEntity) entityDB);
        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            Notification.show("Could not delete group. Http status " + response.getStatus(), Notification.Type.ERROR_MESSAGE);
            return false;
        }
        return super.delete(entityDB);
    }
}
