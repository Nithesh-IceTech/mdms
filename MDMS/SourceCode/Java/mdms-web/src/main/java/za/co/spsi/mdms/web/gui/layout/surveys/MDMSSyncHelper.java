package za.co.spsi.mdms.web.gui.layout.surveys;

import com.vaadin.ui.TextArea;
import lombok.Data;
import za.co.spsi.mdms.common.db.survey.PecMeterReadingListEntity;
import za.co.spsi.mdms.common.db.survey.PecMeterRegisterEntity;
import za.co.spsi.toolkit.crud.sync.SyncableEntity;
import za.co.spsi.toolkit.crud.sync.util.SyncHelper;
import za.co.spsi.toolkit.dao.ToolkitConstants;
import za.co.spsi.toolkit.db.EntityDB;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaspervdbijl on 2017/03/15.
 */
public class MDMSSyncHelper extends SyncHelper {

    @Override
    public void pushToDeviceEvent(DataSource dataSource, TextArea notes, EntityDB entity) {
        if (entity instanceof PecMeterReadingListEntity) {
            updateRecursive(dataSource, (SyncableEntity) entity, notes.getValue(), ToolkitConstants.ENTITY_STATUS_BACK_OFFICE_PROCESSING, ToolkitConstants.ENTITY_STATUS_TABLET_PROCESSING);
        }
    }

    public void recallFromDevice(DataSource dataSource, RecallCallback callback, EntityDB entity) {
        updateRecursive(dataSource, (SyncableEntity) entity, "", ToolkitConstants.ENTITY_STATUS_TABLET_PROCESSING, ToolkitConstants.ENTITY_STATUS_BACK_OFFICE_PROCESSING);
    }

    @Data
    private static class MeterRegisterDetail {
        private String property,meterN,register,deviceId;

        public MeterRegisterDetail() {}

        public MeterRegisterDetail(String property, String meterN, String register) {
            this.property = property;
            this.meterN = meterN;
            this.register = register;
        }

        public static List<MeterRegisterDetail> getList(DataSource dataSource, PecMeterReadingListEntity list) {
            List<MeterRegisterDetail> values = new ArrayList<>();
            for (PecMeterRegisterEntity register : list.meterRegister.getAllAsList(dataSource,null)) {
                values.add(new MeterRegisterDetail(list.buildingName.get(),register.meter.getOne(dataSource).meterN.get(),register.registerId.get()));
            }
            return values;
        }
    }

//    /**
//     * overload to split to different devices
//     * @param dataSource
//     * @param devices
//     * @param notes
//     * @param callback
//     * @param entities
//     */
//    public void okPressedPushToDevices(DataSource dataSource, List<DeviceEntity> devices, TextArea notes, PushCallback callback, EntityDB... entities) {
//        Assert.isTrue(entities.length == 1,"Can not process more than one meter list at a time");
//        if (devices.size() > 1 && entities[0] instanceof PecMeterReadingListEntity) {
//            //
//            Grid grid = new Grid(new ListContainer(MeterRegisterDetail.getList(dataSource, (PecMeterReadingListEntity) entities[0])));
//            grid.addColumn("property");
//            grid.addColumn("meterN");
//            grid.addColumn("register");
//            grid.addColumn("deviceId");
//            MessageBox.create().withMessage(grid).withCancelButton(ButtonOption.closeOnClick(true)).withOkButton(ButtonOption.closeOnClick(true));
//        } else {
//            super.okPressedPushToDevices(dataSource,devices,notes,callback,entities);
//        }
////        if (callback != null) {
////            callback.callback(devices, notes.getCaption());
////        }
//
//    }


//    @Override
//    public void recallToDeviceEvent(DataSource dataSource, EntityDB entityDB) {
//
//        if (entityDB instanceof PecMeterReadingListEntity) {
//            updateRecursive(dataSource, (SyncableEntity) entity,notes.getValue(),IceFSSDaoConstant.ENTITY_STATUS_BACK_OFFICE_PROCESSING,IceFSSDaoConstant.ENTITY_STATUS_TABLET_PROCESSING);
//
//        } else if (entityDB instanceof LocationSurveyEntity) {
//            // Loop through all ref entities and check if its an instance of syncentity then update to backoffice
//            // if its current state is tablet processing
//            for (EntityRef entityRef : entityDB.getEntityRefs()) {
//                if (SyncEntity.class.isAssignableFrom(entityRef.getType())) {
//                    for (Object item : entityRef.getAllAsList(dataSource, " ENTITY_STATUS_CD = 1")) {
//
//                        ((SyncEntity) item).sharedEntity.entityStatusCd.set(IceFSSDaoConstant.ENTITY_STATUS_BACK_OFFICE_PROCESSING);
//                        ((SyncEntity) item).sharedEntity.entityStatusChgD.set(new Timestamp(System.currentTimeMillis()));
//                        DataSourceDB.set(dataSource, (SyncEntity) item);
//                    }
//                }
//            }
//        }
//    }
}
