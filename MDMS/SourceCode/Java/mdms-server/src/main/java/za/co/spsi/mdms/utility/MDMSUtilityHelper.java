package za.co.spsi.mdms.utility;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.idempiere.webservice.client.base.*;
import org.idempiere.webservice.client.exceptions.WebServiceException;
import org.idempiere.webservice.client.net.WebServiceConnection;
import org.idempiere.webservice.client.request.CompositeOperationRequest;
import org.idempiere.webservice.client.response.CompositeResponse;
import org.idempiere.webservice.client.response.RunProcessResponse;
import org.idempiere.webservice.client.response.StandardResponse;
import org.idempiere.webservice.client.response.WindowTabDataResponse;
import za.co.spsi.mdms.common.db.IceBrokerCommandStatusUpdateEntity;
import za.co.spsi.mdms.common.db.PrepaidMeterMeterReadingsView;
import za.co.spsi.mdms.common.db.survey.PecMeterEntity;
import za.co.spsi.mdms.common.db.survey.PecMeterReadingView;
import za.co.spsi.mdms.common.db.survey.PecMeterRegisterEntity;
import za.co.spsi.mdms.common.db.survey.PecPropertyEntity;
import za.co.spsi.mdms.common.db.utility.IceTOURow;
import za.co.spsi.mdms.common.db.utility.IceTimeOfUseHistRow;
import za.co.spsi.mdms.common.properties.config.PropertiesConfig;
import za.co.spsi.mdms.common.services.broker.BrokerService;
import za.co.spsi.mdms.util.IceMeterCacheService;
import za.co.spsi.mdms.utility.dao.ApprovedMeterReadingListItemEntity;
import za.co.spsi.mdms.utility.dao.CreateUpdateMeterReadingEntity;
import za.co.spsi.toolkit.crud.idempiere.AgencyBillingProperties;
import za.co.spsi.toolkit.crud.idempiere.BaseIceUtilityHelper;
import za.co.spsi.toolkit.crud.idempiere.BillingProperties;
import za.co.spsi.toolkit.crud.sync.gui.SyncLayout;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;

import javax.annotation.Resource;
import javax.ejb.AccessTimeout;
import javax.ejb.DependsOn;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by johan on 2017/03/10.
 */
@Slf4j
@Dependent
@Singleton
@AccessTimeout(value=1800000)
@DependsOn({"PropertiesConfig"})
public class MDMSUtilityHelper extends BaseIceUtilityHelper {

    @Resource(mappedName = "java:/jdbc/IceUtil")
    public javax.sql.DataSource iceDataSource;

    @Resource(mappedName = "java:/jdbc/mdms")
    public javax.sql.DataSource mdmsDataSource;

    @Inject
    public AgencyBillingProperties billingProperties;

    @Inject
    private IceMeterCacheService iceMeterCacheService;

    @Inject
    private BrokerService brokerService;

    @Inject
    private PropertiesConfig propertiesConfig;

    public static Logger TAG = Logger.getLogger(MDMSUtilityHelper.class.getName());

    @Inject
    public BillingDataWrapper billingWrapper;

    @SneakyThrows
    public List<IceTimeOfUseHistRow> sendGetTOUTimeSlotDetailRequest(Integer priceListId, Integer priceListVersionId) {
        WebServiceResponse response = getClient().sendRequest( billingWrapper.getTOUTimeSlotDetailRequest( getLogin(), priceListId, priceListVersionId ) );
        DataSet dataset = ((WindowTabDataResponse) response).getDataSet();
        List<IceTimeOfUseHistRow> iceTimeOfUseHistRowList = new ArrayList<>();
        for (DataRow row : dataset.getRows()) {
            iceTimeOfUseHistRowList.add( billingWrapper.mapTimeOfUseHistRow(row) );
        }
        return iceTimeOfUseHistRowList;
    }

    /**
     * IED-3483: MDMS & AMI Portal: TOU Comparison Profile
     * @param iceTarrifScheduleId
     * @param dayOfWeekName
     * @return
     * @throws WebServiceException
     */
    @SneakyThrows
    public List<IceTOURow> sendGetTOUComparrisonTimeSlotDetailRequest(Integer iceTarrifScheduleId, String dayOfWeekName) throws WebServiceException {
            WebServiceResponse response = null;
        try{
            response = getClient().sendRequest( billingWrapper.getTOUComparisonTimeSlotDetailRequest( getLogin(), iceTarrifScheduleId, dayOfWeekName ) );
        } catch (Exception e) {
                log.debug("billingWrapper.getTOUComparisonTimeSlotDetailRequest() has failed with error : " + e);
        }

        DataSet dataset = ((WindowTabDataResponse) response).getDataSet();
        List<IceTOURow> IceTOURowList= new ArrayList<>();
        for (DataRow row : dataset.getRows()) {
            IceTOURowList.add( billingWrapper.mapTimeOfUseComparisonHistRow(row) );
        }
        return IceTOURowList;
    }

    @SneakyThrows
    public ArrayList<ApprovedMeterReadingListItemEntity> sendGetApprovedMeterListRequest() {
        return billingWrapper.mapGetApprovedMeterListResponse(getClient()
                .sendRequest(billingWrapper.getApproveMeterListRequest(getLogin())));
    }

    @SneakyThrows
    public Map<String, PecPropertyEntity> sendGetPropertyDetailsRequest() {
        return billingWrapper.mapPropertyDetailListResponseAsMap(getClient().sendRequest(
                billingWrapper.getPropertyDetailsRequest(getLogin())));
    }

    public Map<String, PecMeterRegisterEntity> sendGetMeterRegisterDetailsRequest() {
        WebServiceConnection client = getClient();
        WebServiceResponse response = null;
        getLogin().setWarehouseID(null);
        ModelCRUDRequest req = billingWrapper.getMeterRegisterDetailsRequest(getLogin());
        try {
            response = client.sendRequest(req);
        } catch (WebServiceException e) {
            throw new RuntimeException(e);
        }
        return billingWrapper.mapMeterRegisterDetailListResponse(response);
    }

    public String getAD_PInstance_ID(String meterNumber) {
        WebServiceConnection client = getClient();
        WebServiceResponse response = null;
        getLogin().setWarehouseID(null);
        ModelRunProcessRequest req = billingWrapper.getMeterVMDetailsRequest(getLogin(), meterNumber);
        try {
            response = client.sendRequest(req);
        } catch (WebServiceException e) {
            throw new RuntimeException(e);
        }
        return ((RunProcessResponse)response).getAD_PInstance_ID();
    }

    public Map<String, String> getMeterHierarchy(String meterNumber) {
        WebServiceConnection client = getClient();
        WebServiceResponse response;
        getLogin().setWarehouseID(null);
        ModelCRUDRequest req = billingWrapper.queryMeterHierarchyFromAdpInstanceId(getLogin(), getAD_PInstance_ID(meterNumber));
        try {
            response = client.sendRequest(req);
        } catch (WebServiceException e) {
            throw new RuntimeException(e);
        }
        DataSet dataset = ((WindowTabDataResponse) response).getDataSet();
        HashMap<String, String> list = new HashMap<>();
        for (DataRow row : dataset.getRows()) {
            list.put(row.getField("ICE_Meter_Number").getStringValue(), row.getField("ICE_OperationType").getStringValue());
        }
        return list;
    }

    public WebServiceResponse sendListUpdate(String listNumber) {
        WebServiceConnection client = getClient();
        getLogin().setWarehouseID(null);
        ModelCRUDRequest req = billingWrapper.updateList(getLogin(), listNumber);
        try {
            return client.sendRequest(req);
        } catch (WebServiceException e) {
            throw new RuntimeException(e);
        }
    }

    public void queryVirtualMeter(String serialN) {
        billingWrapper.queryVirtualMeter(getLogin(),serialN);
    }


    public Map<String, PecMeterEntity> sendGetMeterDetailsRequest() {
        WebServiceConnection client = getClient();
        WebServiceResponse response = null;
        getLogin().setWarehouseID(null);
        ModelCRUDRequest req = billingWrapper.getMeterDetailsRequest(getLogin());
        try {
            response = client.sendRequest(req);
        } catch (WebServiceException e) {
            throw new RuntimeException(e);
        }
        return billingWrapper.mapMeterDetailListResponseAsMap(response);

    }

    public int sendCreateUpdateMeterReadingRequest(CreateUpdateMeterReadingEntity createUpdateMeterReadingEntity) {
        WebServiceConnection client = getClient();
        getLogin().setWarehouseID(null);
        ModelCRUDRequest req = billingWrapper.getCreateUpdateMeterReadingRequest(getLogin(), createUpdateMeterReadingEntity);
        try {
            WebServiceResponse response = client.sendRequest(req);
            return ((StandardResponse) response).getRecordID();
        } catch (WebServiceException e) {
            throw new RuntimeException(e);
        }
    }

    public WebServiceResponse sendCreateUpdateMeterReadingMultipleRequest(
            PecMeterReadingView.PecMeterReadingViewList readingViewList, StringBuilder sb) {
        WebServiceConnection client = getClient();
        CompositeOperationRequest compReq = billingWrapper.getCreateUpdateMeterReadingCompositeRequest(
                getLogin(), readingViewList, sb);
        try {
            WebServiceResponse response = client.sendRequest(compReq);
            if (!Enums.WebServiceResponseStatus.Successful.equals(response.getStatus())) {
                throw new IceUtilityException(client, getLogin(), response);
            }
            return response;
        } catch (WebServiceException e) {
            throw new RuntimeException(e);
        }
    }

    public WebServiceResponse sendPrepaidCreateUpdateMeterReadingMultipleRequest(
            DataSourceDB<PrepaidMeterMeterReadingsView> createUpdateMeterReadingEntityList, StringBuilder sb) {

        WebServiceConnection client = getClient();
        CompositeOperationRequest compReq = billingWrapper.getCreateUpdateMeterReadingCompositeRequest(
                iceDataSource, getLogin(), createUpdateMeterReadingEntityList, iceMeterCacheService, sb);
        try {
            if(compReq.getOperationsCount() > 0) {
                WebServiceResponse response = client.sendRequest(compReq);
                if (!Enums.WebServiceResponseStatus.Successful.equals(response.getStatus())) {
                    throw new IceUtilityException(client, getLogin(), response);
                }
                return response;
            } else {
                WebServiceResponse response = new CompositeResponse();
                response.setStatus(Enums.WebServiceResponseStatus.Successful);
                return response;
            }

        } catch (WebServiceException e) {
            throw new RuntimeException(e);
        }
    }

    public void setUser(String user) {
        getLogin().setUser(user);
    }

    public void setPassword(String password) {
        getLogin().setPass(password);
    }

    public void setLang(String lang) {
        getLogin().setLang(lang);
    }

    public void setClientID(int clientID) {
        getLogin().setClientID(clientID);
    }

    public void setRoleID(int roleID) {
        getLogin().setRoleID(roleID);
    }

    public void setOrgID(int orgID) {
        getLogin().setOrgID(orgID);
    }

    public void linkDetailToApprovedList(ArrayList<ApprovedMeterReadingListItemEntity> approvedMeterList,
                                         Map<String, PecMeterEntity> meterDetailsList,
                                         Map<String, PecPropertyEntity> propertyList,
                                         Map<String, PecMeterRegisterEntity> meterRegisterList) {
        for (ApprovedMeterReadingListItemEntity item : approvedMeterList) {
            item.setPropertyEntity(propertyList.get(item.getIcePropertyID()));
            item.setMeterEntity(meterDetailsList.get(item.getIceMeterID()));
            item.setMeterRegisterEntity(meterRegisterList.get(item.getIceMeterRegisterID()));
        }
    }

    public ArrayList<ApprovedMeterReadingListItemEntity> getApprovedMeterList() {
        ArrayList<ApprovedMeterReadingListItemEntity> approvedMeterList = sendGetApprovedMeterListRequest();
        Map<String, PecMeterEntity> meterDetailsList = sendGetMeterDetailsRequest();
        Map<String, PecPropertyEntity> propertyList = sendGetPropertyDetailsRequest();
        Map<String, PecMeterRegisterEntity> meterRegisterList = sendGetMeterRegisterDetailsRequest();
        linkDetailToApprovedList(approvedMeterList, meterDetailsList, propertyList, meterRegisterList);
        return approvedMeterList;
    }

    @Override
    public BillingProperties getBillingProperties() {
        return billingProperties;
    }

    @Override
    public WebServiceResponse processSyncEntity(Connection dbcon, SyncLayout syncLayout, EntityDB entity) {
        return null;
    }

    public WebServiceResponse sendBrokerCommandStatusUpdate(IceBrokerCommandStatusUpdateEntity iceBrokerCommandStatusUpdateEntity) throws Exception {

        WebServiceConnection client = getClient();

        getLogin().setWarehouseID(null);

        ModelCRUDRequest req = billingWrapper.getUpdateMeterReadingStatus(
                getLogin(), iceBrokerCommandStatusUpdateEntity);

        try {
            WebServiceResponse response = client.sendRequest(req);
            if (!Enums.WebServiceResponseStatus.Successful.equals(response.getStatus())) {
                throw new IceUtilityException(client, getLogin(), response);
            }
            return response;
        } catch (WebServiceException e) {
            throw new RuntimeException(e);
        }

    }

    public WebServiceResponse sendBrokerCommandHistoryUpdate(IceBrokerCommandStatusUpdateEntity iceBrokerCommandStatusUpdateEntity) {

        WebServiceConnection client = getClient();

        getLogin().setWarehouseID(null);

        ModelCRUDRequest req = billingWrapper.getCreateMeterReadingStatusRecord(
                getLogin(), iceBrokerCommandStatusUpdateEntity);

        try {
            WebServiceResponse response = client.sendRequest(req);
            if (!Enums.WebServiceResponseStatus.Successful.equals(response.getStatus())) {
                throw new IceUtilityException(client, getLogin(), response);
            }
            return response;
        } catch (WebServiceException e) {
            throw new RuntimeException(e);
        }

    }

    @Schedule(hour = "*", minute = "*", second = "*/15", persistent = false)
    public void brokerStatusUpdateProcess() {

        MDMSUtilityHelper.TAG.info("Utility Broker Command Status Updates Process Started");

        Timestamp currentTS = null;
        WebServiceResponse statusUpdateResponse = null;
        WebServiceResponse historyUpdateResponse = null;
        IceBrokerCommandStatusUpdateEntity statusUpdateEntityCopy = new IceBrokerCommandStatusUpdateEntity();

        try {

            if(propertiesConfig.getUtility_meter_broker_service_enabled()) {

                brokerService.syncBrokerCommandStatusFromHES();

                for(IceBrokerCommandStatusUpdateEntity statusUpdateEntity:
                        IceBrokerCommandStatusUpdateEntity.getByIceUpdateStatus(mdmsDataSource,IceBrokerCommandStatusUpdateEntity.ICEUpdated.NO.ordinal())) {

                    currentTS = Timestamp.valueOf(LocalDateTime.now());
                    statusUpdateEntity.iceStatusUpdateDate.set(currentTS);

                    statusUpdateEntityCopy.copyStrict(statusUpdateEntity, true);

                    statusUpdateResponse = this.sendBrokerCommandStatusUpdate(statusUpdateEntityCopy);
                    statusUpdateEntityCopy.iceUpdated.set(IceBrokerCommandStatusUpdateEntity.ICEUpdated.YES.ordinal());
                    MDMSUtilityHelper.TAG.info( String.format("Broker Command Status Update Response: %s",
                            statusUpdateResponse.getStatus().name() ) );

                    historyUpdateResponse = this.sendBrokerCommandHistoryUpdate(statusUpdateEntityCopy);
                    statusUpdateEntityCopy.iceUpdated.set(IceBrokerCommandStatusUpdateEntity.ICEUpdated.YES.ordinal());
                    MDMSUtilityHelper.TAG.info( String.format("Broker Command History Update Response: %s",
                            historyUpdateResponse.getStatus().name() ) );

                    brokerService.saveBrokerCommandStatusUpdate(statusUpdateEntityCopy);
                }

            } else {
                MDMSUtilityHelper.TAG.warning("Utility Broker Command Status Updates Service Disabled !");
            }

        } catch(Exception ex) {
            MDMSUtilityHelper.TAG.severe(ex.getMessage());
            ex.printStackTrace();
            statusUpdateEntityCopy.iceUpdated.set(IceBrokerCommandStatusUpdateEntity.ICEUpdated.ERROR.ordinal());
            statusUpdateEntityCopy.error.set(ex.getMessage());
            brokerService.saveBrokerCommandStatusUpdate(statusUpdateEntityCopy);
        }

        MDMSUtilityHelper.TAG.info("Utility Broker Command Status Updates Process Finished");
    }

}
