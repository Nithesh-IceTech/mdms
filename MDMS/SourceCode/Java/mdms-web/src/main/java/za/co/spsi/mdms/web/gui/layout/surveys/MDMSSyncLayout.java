package za.co.spsi.mdms.web.gui.layout.surveys;


import com.vaadin.ui.Notification;
import za.co.spsi.locale.annotation.MdmsLocaleId;
import za.co.spsi.mdms.common.db.survey.PecMeterReadingListEntity;
import za.co.spsi.mdms.utility.MDMSUtilityHelper;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import za.co.spsi.toolkit.crud.gui.render.VaadinNotification;
import za.co.spsi.toolkit.crud.idempiere.AgencyBillingProperties;
import za.co.spsi.toolkit.crud.idempiere.BaseIceUtilityHelper;
import za.co.spsi.toolkit.crud.idempiere.BillingProperties;
import za.co.spsi.toolkit.crud.sync.SyncableEntity;
import za.co.spsi.toolkit.crud.sync.gui.SyncLayout;
import za.co.spsi.toolkit.crud.sync.gui.fields.EntityStatusActionFieldListener;
import za.co.spsi.toolkit.dao.ToolkitConstants;
import za.co.spsi.toolkit.db.EntityDB;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.util.logging.Logger;

import static za.co.spsi.mdms.common.db.survey.PecMeterReadingListEntity.Status.Completed;

public abstract class MDMSSyncLayout<E extends EntityDB> extends SyncLayout<E> implements EntityStatusActionFieldListener {

    private static final Logger LOG = Logger.getLogger(MDMSSyncLayout.class.getName());

    @Resource(mappedName = "java:/jdbc/mdms")
    private javax.sql.DataSource dataSource;

    @Inject
    protected MDMSUtilityHelper iceUtilityHelper;

    @Inject
    protected AgencyBillingProperties agencyBillingProperties;

    public MDMSSyncLayout() {
        getPermission().setMayCreate(false);
    }

    public MDMSSyncLayout(String captionId) {
        super(captionId);
    }

    @Override
    public BillingProperties getBillingProperties() {
        return agencyBillingProperties;
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    public BaseIceUtilityHelper getIceUtilityHelper() {
        return iceUtilityHelper;
    }

    @Override
    public void pushToDeviceEvent(String deviceId) {
    }

    /**
     * implement logic to reload entity
     */
    public void reloadAfterMerge() {
    }

    public boolean allowMultiple(String action) {
        return false;
    }

    @Override
    protected boolean validateBillingEntities(SyncableEntity syncableEntity) {
        if (!ToolkitConstants.ENTITY_STATUS_BACK_OFFICE_PROCESSING.equals(syncableEntity.getBaseSharedSyncEntity().entityStatusCd.get())) {
            VaadinNotification.show(AbstractView.getLocaleValue(MdmsLocaleId.SEND_TO_BILLING_BACK_OFFICE_ONLY),
                    Notification.Type.ERROR_MESSAGE);
            return false;
        }
        if (syncableEntity instanceof PecMeterReadingListEntity &&
                !PecMeterReadingListLayout.isInEditableState((PecMeterReadingListEntity) syncableEntity)) {
            VaadinNotification.show(AbstractView.getLocaleValue(MdmsLocaleId.SEND_TO_DEVICE_INCORRECT_STATE),
                    Notification.Type.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    @Override
    protected boolean validatePushEntities(SyncableEntity syncableEntity) {
        if (syncableEntity instanceof PecMeterReadingListEntity &&
                ((PecMeterReadingListEntity) syncableEntity).status.get() > Completed.getCode()) {
            VaadinNotification.show(AbstractView.getLocaleValue(MdmsLocaleId.SEND_TO_DEVICE_INCORRECT_STATE),
                    Notification.Type.ERROR_MESSAGE);
            return false;
        } else {
            return super.validatePushEntities(syncableEntity);
        }
    }

}