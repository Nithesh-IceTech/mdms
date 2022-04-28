package za.co.spsi.toolkit.crud.sync.gui;


import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import de.steinwedel.messagebox.ButtonOption;
import de.steinwedel.messagebox.ButtonType;
import de.steinwedel.messagebox.MessageBox;
import org.idempiere.webservice.client.base.Enums;
import org.idempiere.webservice.client.base.WebServiceResponse;
import rx.Observable;
import rx.schedulers.Schedulers;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.crud.entity.BulkBillingResultEntity;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.crud.gui.ToolkitUI;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.crud.gui.render.VaadinNotification;
import za.co.spsi.toolkit.crud.idempiere.BaseIceUtilityHelper;
import za.co.spsi.toolkit.crud.idempiere.BillingProperties;
import za.co.spsi.toolkit.crud.service.NumberService;
import za.co.spsi.toolkit.crud.sync.SyncableEntity;
import za.co.spsi.toolkit.crud.sync.db.SharedEntity;
import za.co.spsi.toolkit.crud.sync.gui.fields.EntityStatusActionFieldListener;
import za.co.spsi.toolkit.crud.sync.util.SyncHelper;
import za.co.spsi.toolkit.dao.ToolkitConstants;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import static za.co.spsi.toolkit.crud.gui.render.AbstractView.getLocaleValue;

public abstract class SyncLayout<E extends EntityDB> extends Layout<E> implements EntityStatusActionFieldListener {

    private static final Logger LOG = Logger.getLogger(SyncLayout.class.getName());

    @Inject
    protected BeanManager beanManager;

    @Inject
    protected SyncHelper syncHelper;

    @Inject
    protected NumberService numberService;

    public abstract BillingProperties getBillingProperties();

    public SyncLayout() {
        getPermission().setMayCreate(false);
    }

    public SyncHelper getSyncHelper() {
        return syncHelper;
    }

    public void setSyncHelper(SyncHelper syncHelper) {
        this.syncHelper = syncHelper;
    }

    public SyncLayout(String captionId) {
        super(captionId);
        getPermission().setMayCreate(false);
    }

    public abstract SharedEntity getSharedEntity();

    public abstract BaseIceUtilityHelper getIceUtilityHelper();

    @Override
    public void intoControl() {
        // tablet processing - set read only
        if (ToolkitConstants.EntitySyncStatus.TABLET.getCode().equals(getSharedEntity().entityStatusCd.get()) ||
                ToolkitConstants.EntitySyncStatus.BILLING.getCode().equals(getSharedEntity().entityStatusCd.get())) {
            getPermission().setReadOnly(true);
        }
        super.intoControl();
    }

    @Override
    public void beforeOnScreenEvent() {
        super.beforeOnScreenEvent();
        // set the correct edit state
        getPermission().updateMayUpdate(ToolkitConstants.EntitySyncStatus.BACK_OFFICE.getCode().equals(getSharedEntity().entityStatusCd.get()));
    }

    @Override
    public void action(ToolkitConstants.EntitySyncStatus entitySyncStatus, EntityStatusActionFieldListener listener) {

        if (save()) {
            if (entitySyncStatus == ToolkitConstants.EntitySyncStatus.TABLET) {
                if (validatePushEntities((SyncableEntity) getMainEntity())) {

                    syncHelper.pushToDevice(getDataSource(), (deviceAliases, notes) -> {
                        if (deviceAliases != null && !deviceAliases.isEmpty()) {
                            listener.action(entitySyncStatus, null);
                            pushToDeviceEvent(deviceAliases.stream().map(a -> a.getDisplayName()).reduce((a1, a2) -> a1 + "," + a2).toString());
                            intoControl();
                            listener.action(entitySyncStatus, null);
                        } else {
                            VaadinNotification.show(AbstractView.getLocaleValue(ToolkitLocaleId.NO_DEVICE_SELECTED));
                        }
                    }, allowMultiple(null), getMainEntity());
                }
            } else if (entitySyncStatus == ToolkitConstants.EntitySyncStatus.BILLING) {
                if (validateBillingEntities((SyncableEntity) getMainEntity())) {

                    MessageBox billingMessageBox = MessageBox.createInfo();
                    billingMessageBox.withCaption(getLocaleValue(ToolkitLocaleId.CONFIRM_FOR_BILLING_CAPTION))
                            .withMessage(getLocaleValue(ToolkitLocaleId.CONFIRM_FOR_BILLING_MESSAGE))
                            .withCancelButton(ButtonOption.caption(getLocaleValue(ToolkitLocaleId.CANCEL).toUpperCase()), ButtonOption.closeOnClick(true))
                            .withOkButton(() -> {
                                        billingMessageBox.getButton(ButtonType.OK).setEnabled(false);
                                        billingMessageBox.getButton(ButtonType.CANCEL).setEnabled(false);
                                        Observable.fromCallable(confirmForBillingRequest()).
                                                subscribeOn(Schedulers.newThread()).
                                                doOnError(throwable -> {
                                                    if (billingMessageBox != null) {
                                                        MessageBox.createError().withMessage(throwable.getMessage()).open();
                                                        UI.getCurrent().access(() -> billingMessageBox.close());
                                                    }
                                                    throw new RuntimeException(throwable);

                                                }).subscribe(aBoolean -> {
                                            UI.getCurrent().access(() -> billingMessageBox.close());
                                            DataSourceDB.set(getDataSource(), getMainEntity());
                                        });
                                    },
                                    ButtonOption.caption(getLocaleValue(ToolkitLocaleId.OK).toUpperCase()), ButtonOption.closeOnClick(false)).open();
                }
            } else if (entitySyncStatus == ToolkitConstants.EntitySyncStatus.BACK_OFFICE) {
                syncHelper.recallFromDevice(getDataSource(), () -> {
                    DataSourceDB.loadFromId(getDataSource(), getMainEntity());
                    // reset the state - assuming that you must have had upadte rights
                    SyncLayout.this.getPermission().setMayUpdate(true);
                    intoControl();
                    executeAuditingLogic();
                }, getMainEntity());
            }
        }

    }

    private Callable<Boolean> confirmForBillingRequest() {
        return () -> {
            confirmForBilling();
            return new Boolean(true);
        };
    }

    /**
     * override to implement logic
     */
    public void pushToDeviceEvent(String deviceId) {
    }

    /**
     * overload to chaneg behavior
     *
     * @param connection
     */
    public void confirmForBilling(Connection connection, EntityDB... entities) {
        // Check if entity has legal entity linked check that legal entity has been sent for billing
        String bulkId = entities.length > 0 ? numberService.getHexString(13) : null;
        final AtomicInteger i = new AtomicInteger(0);
        if (entities.length == 0) {
            entities = new EntityDB[1];
            entities[0] = SyncLayout.this.getMainEntity();
        }
        for (EntityDB entityDB : entities) {
            Runnable billingAction = () -> {
                try {
                    try (Connection dbcon = getDataSource().getConnection()) {
                        WebServiceResponse response = getIceUtilityHelper().processSyncEntity(dbcon, SyncLayout.this, entityDB);
                        if (response != null && response.getStatus() == Enums.WebServiceResponseStatus.Successful) {
                            if (bulkId == null) {
                                VaadinNotification.show(AbstractView.getLocaleValue(ToolkitLocaleId.CONFIRM_FOR_BILLING_SUCCESS),
                                        Notification.Type.HUMANIZED_MESSAGE);
                                intoControl();
                            } else {
                                DataSourceDB.set(connection, new BulkBillingResultEntity(entityDB, getLocaleValue(
                                        AbstractView.getLocaleValue(ToolkitLocaleId.CONFIRM_FOR_BILLING_SUCCESS)),
                                        bulkId));
                            }
                            i.getAndAdd(1);
                        } else {
                            if (response != null) {
                                if (bulkId == null) {
                                    MessageBox.createWarning().withCaption(getLocaleValue(
                                            ToolkitLocaleId.ICE_UTIL_ERROR)).withWidth("600")
                                            .withMessage(response.getErrorMessage())
                                            .withOkButton(ButtonOption.caption(getLocaleValue(ToolkitLocaleId.OK).toUpperCase()),
                                                    ButtonOption.closeOnClick(true)).open();
                                } else {
                                    DataSourceDB.set(connection, new BulkBillingResultEntity(entityDB, getLocaleValue(
                                            ToolkitLocaleId.ICE_UTIL_ERROR) + ":" + response.getErrorMessage(),
                                            bulkId));
                                }
                                LOG.info(response.getErrorMessage());
                            } else {
                                if (bulkId == null) {
                                    VaadinNotification.show(AbstractView.getLocaleValue(
                                            ToolkitLocaleId.ERROR),
                                            AbstractView.getLocaleValue(ToolkitLocaleId.ICE_UTIL_NO_RESPONSE),
                                            Notification.Type.ERROR_MESSAGE);
                                } else {
                                    DataSourceDB.set(connection, new BulkBillingResultEntity(entityDB,
                                            AbstractView.getLocaleValue(ToolkitLocaleId.ICE_UTIL_NO_RESPONSE),
                                            bulkId));
                                }
                            }
                        }
                    }
                } catch (SQLException sqle) {
                    throw new RuntimeException(sqle);
                }
            };
            validateSyncEntityProcessBilling(connection, billingAction, bulkId, entityDB);
        }
        if (bulkId != null) {
            MessageBox.createInfo().withCaption(getLocaleValue(
                    ToolkitLocaleId.NOTE)).withWidth("600")
                    .withMessage(String.format(AbstractView.getLocaleValue
                            (ToolkitLocaleId.BULK_SESSION_COMPLETED), i, entities.length, bulkId))
                    .withOkButton(ButtonOption.caption(getLocaleValue(ToolkitLocaleId.OK).toUpperCase()),
                            ButtonOption.closeOnClick(true)).open();
        }
    }

    protected void confirmForBilling() {
        try {
            try (Connection connection = getDataSource().getConnection()) {
                confirmForBilling(connection);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected void confirmForBilling(EntityDB... entityDBS) {
        try {
            try (Connection connection = getDataSource().getConnection()) {
                confirmForBilling(connection, entityDBS);
            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }


    /**
     * override to change behavior
     *
     * @param connection
     * @param billingAction
     */
    protected void validateSyncEntityProcessBilling(Connection connection, Runnable billingAction, String bulkId, EntityDB entityDB) {

    }

    /**
     * implement logic to reload entity
     */
    public void reloadAfterMerge() {
    }

    @Override
    public String[] getActions() {

        if (ToolkitUI.getToolkitUI().getUserRoles().containsIgnoreCase(ToolkitCrudConstants.ROLE_SUPERVISOR) &&
                getBillingProperties().getBillingEnabled()) {
            return new String[]{ToolkitLocaleId.SEND_TO_DEVICE, ToolkitLocaleId.RECALL_SURVEY, ToolkitLocaleId.SEND_TO_BILLING};
        } else if (ToolkitUI.getToolkitUI().getUserRoles().containsIgnoreCase(ToolkitCrudConstants.ROLE_SUPERVISOR) && !getBillingProperties().getBillingEnabled()) {
            return new String[]{ToolkitLocaleId.SEND_TO_DEVICE, ToolkitLocaleId.RECALL_SURVEY};
        } else {
            return new String[]{ToolkitLocaleId.SEND_TO_DEVICE};
        }
    }

    public boolean allowMultiple(String action) {
        return true;
    }

    protected boolean validatePushEntities(SyncableEntity syncableEntity) {
        return true;
    }

    protected boolean validateBillingEntities(SyncableEntity syncableEntity) {
        if (!ToolkitConstants.ENTITY_STATUS_BACK_OFFICE_PROCESSING.equals(syncableEntity.getBaseSharedSyncEntity().entityStatusCd.get())) {
            VaadinNotification.show(AbstractView.getLocaleValue(ToolkitLocaleId.SEND_TO_DEVICE_ONLY_BACK_OFFICE_STATE),
                    Notification.Type.ERROR_MESSAGE);
            return false;
        }
        if (!ToolkitConstants.REVIEW_STATUS_APPROVED.equals(syncableEntity.getBaseSharedSyncEntity().reviewStatusCd.get())) {

            VaadinNotification.show(AbstractView.getLocaleValue(ToolkitLocaleId.INVALID_REVIEW_STATUS_FOR_BILLING),
                    Notification.Type.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validateRecallEntities(SyncableEntity syncableEntity) {
        if (!ToolkitConstants.ENTITY_STATUS_TABLET_PROCESSING.equals(syncableEntity.getBaseSharedSyncEntity().entityStatusCd.get())) {
            VaadinNotification.show(AbstractView.getLocaleValue(ToolkitLocaleId.RECALL_FROM_DEVICE_ONLY_TABLET_PROCESSING_STATE),
                    Notification.Type.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean validatePushEntities(List<EntityDB> entities) {
        for (EntityDB entity : entities) {
            if (entity instanceof SyncableEntity) {
                if (!validatePushEntities((SyncableEntity) entity)) {
                    return false;
                }
            } else {
                throw new RuntimeException("Unexpected entity type for push");
            }
        }
        return true;
    }

    private boolean validateRecallEntities(List<EntityDB> entities) {
        for (EntityDB entity : entities) {
            if (entity instanceof SyncableEntity) {
                if (!validateRecallEntities((SyncableEntity) entity)) {
                    return false;
                }
            } else {
                throw new RuntimeException("Unexpected entity type for recall");
            }
        }
        return true;
    }

    protected boolean validateBillingEntities(List<EntityDB> entities) {
        for (EntityDB entity : entities) {
            if (entity instanceof SyncableEntity) {
                if (!validateBillingEntities((SyncableEntity) entity)) {
                    return false;
                }
            } else {
                throw new RuntimeException("Unexpected entity type for billing");
            }
        }
        return true;
    }

    @Override
    public void action(String action, List<EntityDB> entities) {
        if (action.equals(ToolkitLocaleId.SEND_TO_DEVICE)) {
            // validate that all entities are marked as BO processing
            if (validatePushEntities(entities)) {
                syncHelper.pushToDevice(getDataSource(), (deviceAliases, note) -> {
                    refresh();
                }, allowMultiple(action), entities.toArray(new EntityDB[]{}));
            }
        } else if (action.equals(ToolkitLocaleId.RECALL_SURVEY)) {
            // validate that all entities are marked as BO processing
            if (validateRecallEntities(entities)) {
                syncHelper.recallFromDevice(getDataSource(), () -> {
                    refresh();
                }, entities.toArray(new EntityDB[]{}));
            }
        } else if (action.equals(ToolkitLocaleId.SEND_TO_BILLING)) {

            // validate that all entities are marked as BO processing and that all are APPROVED review status
            if (validateBillingEntities(entities)) {
                if (entities.size() > 0) {
                    confirmForBilling(entities.toArray(new EntityDB[]{}));
                } else {
                    VaadinNotification.show(AbstractView.getLocaleValue(
                            ToolkitLocaleId.ERROR),
                            AbstractView.getLocaleValue(ToolkitLocaleId.NO_RECORDS_SELECTED),
                            Notification.Type.ERROR_MESSAGE);
                }
            }
        }
    }

    @Override
    public void newEvent() {
        super.newEvent();
        getSharedEntity().capturedD.set(new Timestamp(System.currentTimeMillis()));
    }

    @Override
    public boolean save() {

        if (getSharedEntity().entityStatusCd.isChanged()) {
            getSharedEntity().entityStatusChgD.set(new Timestamp(System.currentTimeMillis()));
        }

        return super.save();
    }
}