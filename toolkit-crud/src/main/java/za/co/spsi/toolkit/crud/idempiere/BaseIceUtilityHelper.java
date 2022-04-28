package za.co.spsi.toolkit.crud.idempiere;

import org.idempiere.webservice.client.base.Enums;
import org.idempiere.webservice.client.base.LoginRequest;
import org.idempiere.webservice.client.base.WebServiceResponse;
import org.idempiere.webservice.client.exceptions.WebServiceException;
import org.idempiere.webservice.client.net.WebServiceConnection;
import org.idempiere.webservice.client.request.CompositeOperationRequest;
import org.idempiere.webservice.client.request.CreateUpdateDataRequest;
import org.idempiere.webservice.client.request.QueryDataRequest;
import org.idempiere.webservice.client.response.CompositeResponse;
import org.idempiere.webservice.client.response.StandardResponse;
import org.idempiere.webservice.client.response.WindowTabDataResponse;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.crud.sync.gui.SyncLayout;
import za.co.spsi.toolkit.dao.ToolkitConstants;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.entity.Field;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by jaspervdb on 5/12/16.
 */
public abstract class BaseIceUtilityHelper {

    private static final Logger LOG = Logger.getLogger(BaseIceUtilityHelper.class.getName());

    public abstract BillingProperties getBillingProperties();

    private LoginRequest login;


    public WebServiceConnection getClient() {
        WebServiceConnection client = new WebServiceConnection();
        client.setAttempts(1);
        client.setTimeout(600000);
        client.setAttemptsTimeout(5000);
        client.setUrl(getBillingProperties().getIceBaseUrl());
        return client;
    }

    private Integer format(Integer value) {return value.intValue() == -1?null:value;}

    public LoginRequest getLogin() {
        if (login == null) {
            login = new LoginRequest();
            login.setUser(getBillingProperties().getIceUser());
            login.setPass(getBillingProperties().getIcePassword());
            login.setClientID(format(getBillingProperties().getIceClientId()));
            login.setRoleID(format(getBillingProperties().getIceRoleId()));
            login.setOrgID(format(getBillingProperties().getIceOrgId()));
            login.setWarehouseID(format(getBillingProperties().getIceWarehouseId()));
            login.setLang(getLanguage(ToolkitCrudConstants.getLocale()));
        }
        return login;
    }

    protected String getLanguage(String language) {
        if (language.equals(ToolkitConstants.PORTUGUESE)) {
            return "pt_MZ";
        } else if (language.equals(ToolkitConstants.ENGLISH)) {
            return "en_ZA";
        }
        throw new UnsupportedOperationException("Language not supported or not set");

    }

    public WindowTabDataResponse query(QueryDataRequest queryDataRequest) throws WebServiceException {

        WebServiceConnection client = getClient();
        queryDataRequest.setLogin(getLogin());

        WindowTabDataResponse response = client.sendRequest(queryDataRequest);
        if (response.getStatus() == Enums.WebServiceResponseStatus.Error) {
            LOG.warning(response.getErrorMessage());
            throw new WebServiceException(response.getErrorMessage());
        }

        return response;
    }

    public abstract WebServiceResponse processSyncEntity(Connection dbcon, SyncLayout syncLayout, EntityDB entity);

    public StandardResponse sendRequest(CreateUpdateDataRequest compositeOperationRequest, Connection connection,
                                        EntityDB entityDB, Field refField, Boolean updateEntityStatus) {
        WebServiceConnection client = getClient();
        compositeOperationRequest.setLogin(getLogin());

        try {
            StandardResponse response = client.sendRequest(compositeOperationRequest);
            if (response != null && response.getStatus() == Enums.WebServiceResponseStatus.Successful) {
                refField.set(response.getRecordID());
                if (updateEntityStatus) {
                    // update the status
                    entityDB.getFields().getByName("entityStatusCd").set(ToolkitConstants.ENTITY_STATUS_BILLING_PROCESSING);
                }
                DataSourceDB.set(connection, entityDB);
            } else {
                LOG.warning(response.getErrorMessage());
                LOG.warning(compositeOperationRequest.toString());
            }

            return response;
        } catch (Exception e) {
            LOG.warning(e.getMessage());
            return null;
        }
    }

    public CompositeResponse sendRequest(CompositeOperationRequest compositeOperationRequest,
                                         Connection connection,
                                         List<CompositeReqObj> compositeReqObjList) {

        WebServiceConnection client = getClient();
        compositeOperationRequest.setLogin(getLogin());

        try {

            CompositeResponse response = client.sendRequest(compositeOperationRequest);

            if (response != null && response.getStatus() == Enums.WebServiceResponseStatus.Successful) {

                for (WebServiceResponse webServiceResponse : response.getResponses()) {
                    for (CompositeReqObj compositeReqObj : compositeReqObjList) {
                        if (compositeReqObj.webserviceRequest.equals(webServiceResponse.getWebServiceType())) {
                            compositeReqObj.refField.set(((StandardResponse) webServiceResponse).getRecordID());
                            if (compositeReqObj.entityStatusToBeSet) {

                                compositeReqObj.entity.getFields().getByName("entityStatusCd").
                                        set(ToolkitConstants.ENTITY_STATUS_BILLING_PROCESSING);

                                compositeReqObj.entity.getFields().getByName("entityStatusChgD").
                                        set(new Timestamp(System.currentTimeMillis()));
                            }
                            compositeReqObjList.remove(compositeReqObj);
                            DataSourceDB.set(connection, compositeReqObj.entity);
                            break;
                        }
                    }
                }
            }

            return response;

        } catch (Exception e) {
            LOG.severe(e.getMessage());
            return null;
        }
    }

    public class CompositeReqObj {

        protected String webserviceRequest;
        protected EntityDB entity;
        protected Field refField;
        protected boolean entityStatusToBeSet;

        public CompositeReqObj(String webserviceRequest, EntityDB entity, Field refField, boolean entityStatusToBeSet) {
            this.webserviceRequest = webserviceRequest;
            this.entity = entity;
            this.refField = refField;
            this.entityStatusToBeSet = entityStatusToBeSet;
        }
    }

}
