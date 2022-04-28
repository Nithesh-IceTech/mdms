package za.co.spsi.mdms.common.services;

import org.idempiere.webservice.client.base.Enums;
import org.idempiere.webservice.client.base.ModelCRUDRequest;

/**
 * Created by johan on 2017/03/13.
 */
public class MDMSModelCRUDRequest extends ModelCRUDRequest{

    private Enums.WebServiceResponseModel webserviceResponseModel;
    private Enums.WebServiceMethod webServiceMethod;

    @Override
    public Enums.WebServiceMethod getWebServiceMethod() {
        return webServiceMethod;
    }

    @Override
    public Enums.WebServiceDefinition getWebServiceDefinition() {
        return Enums.WebServiceDefinition.ModelADService;
    }

    @Override
    public Enums.WebServiceResponseModel getWebServiceResponseModel() {
        return webserviceResponseModel;
    }

    public MDMSModelCRUDRequest(Enums.WebServiceResponseModel webserviceResponseModel, Enums.WebServiceMethod webServiceMethod) {
        this.webserviceResponseModel = webserviceResponseModel;
        this.webServiceMethod = webServiceMethod;
    }
}
