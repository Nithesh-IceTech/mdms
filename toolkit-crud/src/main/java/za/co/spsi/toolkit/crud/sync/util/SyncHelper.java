package za.co.spsi.toolkit.crud.sync.util;

import com.vaadin.ui.TextArea;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.MessageBox;
import org.vaadin.viritin.layouts.MVerticalLayout;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.crud.gui.render.VaadinNotification;
import za.co.spsi.toolkit.crud.service.gui.DeviceComboBox;
import za.co.spsi.toolkit.crud.sync.SyncableEntity;
import za.co.spsi.toolkit.crud.sync.db.BaseSharedSyncEntity;
import za.co.spsi.toolkit.crud.sync.db.DeviceEntity;
import za.co.spsi.toolkit.crud.sync.db.DeviceEntitySyncMapEntity;
import za.co.spsi.toolkit.dao.ToolkitConstants;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.EntityRef;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static za.co.spsi.toolkit.crud.gui.render.AbstractView.getLocaleValue;

/**
 * Created by jaspervdb on 2016/08/18.
 */
public class SyncHelper {

    public static interface PushCallback {

        public void callback(List<DeviceEntity> deviceAliases, String note);
    }

    public static interface RecallCallback {
        public void callback();
    }

    /**
     * overload to split to different devices
     * @param dataSource
     * @param devices
     * @param notes
     * @param callback
     * @param entities
     */
    public void okPressedPushToDevices(DataSource dataSource, List<DeviceEntity> devices,TextArea notes,PushCallback callback,EntityDB... entities) {
        for (EntityDB entity : entities) {
            DeviceEntitySyncMapEntity.pushData(dataSource, Arrays.asList(entity), devices);

            if (entity instanceof SyncableEntity) {
                SyncableEntity syncEntity = (SyncableEntity) entity;
                updateSharedEntity(((SyncableEntity)syncEntity).getBaseSharedSyncEntity(), notes.getValue());
                pushToDeviceEvent(dataSource,notes,entity);
                DataSourceDB.set(dataSource, (EntityDB) syncEntity);
            }
        }
        if (callback != null) {
            callback.callback(devices, notes.getCaption());
        }

    }

    public void pushToDevice(DataSource dataSource, PushCallback callback,boolean allowMultple,EntityDB... entities) {
        DeviceComboBox deviceComboBox = new DeviceComboBox();
        deviceComboBox.init(dataSource,getLocaleValue(ToolkitLocaleId.SELECT_DEVICE), null,allowMultple);

        final TextArea notes = new TextArea();

        notes.setRows(5);
        notes.setInputPrompt(getLocaleValue(ToolkitLocaleId.SET_NOTE));
        notes.setWidth("100%");
        notes.setMaxLength(500);
        notes.addStyleName("uppercase");

        MessageBox.createInfo().withCaption(getLocaleValue(ToolkitLocaleId.SELECT_DEVICE))
                .withMessage(new MVerticalLayout(deviceComboBox.getComboBox()))
                .withCancelButton(ButtonOption.caption(getLocaleValue(ToolkitLocaleId.CANCEL).toUpperCase()), ButtonOption.closeOnClick(true)).withWidth("700px")
                .withOkButton(() -> {
                    List<DeviceEntity> devices = (deviceComboBox.getComboBox().getValue() != null?
                            new ArrayList(deviceComboBox.getValues()):null);
                    if (devices != null && !devices.isEmpty()) {
                        MessageBox.createInfo().withCaption(getLocaleValue(ToolkitLocaleId.NOTE))
                                .withMessage(new MVerticalLayout(notes))
                                .withCancelButton(ButtonOption.closeOnClick(true)).withWidth("700px")
                                .withOkButton(() -> {
                                    okPressedPushToDevices(dataSource,devices,notes,callback,entities);
                                }, ButtonOption.closeOnClick(true))
                                .open();
                    } else {
                        VaadinNotification.show(AbstractView.getLocaleValue(ToolkitLocaleId.NO_DEVICE_SELECTED));
                    }
                }, ButtonOption.caption(getLocaleValue(ToolkitLocaleId.OK).toUpperCase()), ButtonOption.closeOnClick(true))
                .open();
    }


    public void pushToDeviceEvent(DataSource dataSource,TextArea notes,EntityDB entity) {}

    public void recallFromDevice(DataSource dataSource, RecallCallback callback, EntityDB... entities) {

        MessageBox.createInfo().withCaption(getLocaleValue(ToolkitLocaleId.SELECT_DEVICE))
                .withMessage("RECALL SURVEY")
                .withCancelButton(ButtonOption.caption(getLocaleValue(ToolkitLocaleId.CANCEL).toUpperCase()), ButtonOption.closeOnClick(true)).withWidth("700px")
                .withOkButton(() -> {

                    for (EntityDB entityDB : entities) {

                        boolean insertDeviceEntitySyncMapEntity = false;

                        if (entityDB instanceof SyncableEntity) {
                            ((SyncableEntity) entityDB).getBaseSharedSyncEntity().entityStatusCd.set(ToolkitConstants.ENTITY_STATUS_BACK_OFFICE_PROCESSING);
                            ((SyncableEntity) entityDB).getBaseSharedSyncEntity().entityStatusChgD.set(new Timestamp(System.currentTimeMillis()));
                            recallToDeviceEvent(dataSource,entityDB);
                            insertDeviceEntitySyncMapEntity = true;

                        }

                        if (insertDeviceEntitySyncMapEntity) {
                            DataSourceDB.set(dataSource, entityDB);

                            try {
                                try (Connection nesConnection = dataSource.getConnection()) {
                                    for (DeviceEntity deviceEntity : new DataSourceDB<>(DeviceEntity.class).getAllWhere(nesConnection, "agency_id = ?",
                                            ToolkitCrudConstants.getChildAgencyId().toString())) {

                                        DeviceEntitySyncMapEntity deviceEntitySyncMapEntity =
                                                new DeviceEntitySyncMapEntity(entityDB, deviceEntity);

                                        deviceEntitySyncMapEntity.delivered.set('R');
                                        deviceEntitySyncMapEntity.onTablet.set('R');
                                        DataSourceDB.set(dataSource, deviceEntitySyncMapEntity);
                                    }
                                }
                            } catch (SQLException sqle) {
                                throw new RuntimeException(sqle);
                            }
                        }

                        if (callback != null) {
                            callback.callback();
                        }

                    }
                }, ButtonOption.closeOnClick(true)).open();
    }

    public void recallToDeviceEvent(DataSource dataSource,EntityDB entity) {}

    private void updateSharedEntity(BaseSharedSyncEntity sharedEntity, String note) {
        if (sharedEntity != null) {
            if(!sharedEntity.entityStatusCd.get().equals(ToolkitConstants.ENTITY_STATUS_BILLING_PROCESSING)) {
                sharedEntity.entityStatusCd.set(ToolkitConstants.ENTITY_STATUS_TABLET_PROCESSING);
            }
            sharedEntity.entityStatusChgD.set(new Timestamp(System.currentTimeMillis()));
            sharedEntity.pushDownNote.set(note);
        }
    }



    public void updateState(DataSource dataSource, SyncableEntity entity, String note,Integer newState) {
        if (entity != null) {
            updateSharedEntity(entity.getBaseSharedSyncEntity(),note);
            DataSourceDB.set(dataSource, (EntityDB) entity);
        }
    }

    public void updateRecursive(DataSource dataSource, SyncableEntity entity, String note,
                                Integer oldState, Integer newState) {
        updateState(dataSource,entity,note,newState);
        updateRecursive(dataSource, entity, note, oldState, newState, new ArrayList<Class>());
    }

    public void updateRecursive(DataSource dataSource, SyncableEntity entity, String note,
                                Integer oldState, Integer newState, List<Class> updated) {

        for (EntityRef entityRef : ((EntityDB) entity).getEntityRefs()) {
            if (SyncableEntity.class.isAssignableFrom(entityRef.getType()) && !updated.contains(entityRef.getType())) {
                for (Object item : entityRef.getAllAsList(dataSource, "ENTITY_STATUS_CD = " + oldState)) {
                    if (item instanceof SyncableEntity) {
                        updated.add(item.getClass());
                        updateState(dataSource, (SyncableEntity) item, note, newState);
                        updateRecursive(dataSource, (SyncableEntity) item, note, oldState, newState, updated);
                    }
                }
            }
        }
    }

}
