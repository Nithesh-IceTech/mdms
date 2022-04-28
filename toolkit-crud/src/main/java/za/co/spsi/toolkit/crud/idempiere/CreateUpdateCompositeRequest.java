package za.co.spsi.toolkit.crud.idempiere;

import org.idempiere.webservice.client.base.Operation;
import org.idempiere.webservice.client.request.CompositeOperationRequest;
import org.idempiere.webservice.client.request.CreateUpdateDataRequest;
import org.idempiere.webservice.client.request.RunProcessRequest;

import java.util.List;

/**
 * Created by ettienne on 2016/05/30.
 */
public class CreateUpdateCompositeRequest {
    private CompositeOperationRequest compositeRequest;

    private CreateUpdateCompositeRequest(CreateUpdateCompositeRequest.Builder builder) {
        this.compositeRequest = builder.compositeRequest;
    }

    public static class Builder {
        private CompositeOperationRequest compositeRequest;

        public Builder() {
        }

        public CreateUpdateCompositeRequest.Builder compositeRequest(String name,StringBuilder sb) {
            sb.append(name).append("\n");
            this.compositeRequest = new CompositeOperationRequest();
            this.compositeRequest.setWebServiceType(name);
            return this;
        }

        public CreateUpdateCompositeRequest.Builder addRequest(String name, DataRow data,StringBuilder sb) {
            sb.append(name).append("\n");
            CreateUpdateDataRequest request = new CreateUpdateDataRequest();
            request.setWebServiceType(name);
                request.setDataRow(data);

            Operation operation = new Operation();
            operation.setWebService(request);
            compositeRequest.addOperation(operation);

            return this;
        }

        public CreateUpdateCompositeRequest.Builder addRequest(String name, List<DataRow> dataRows,StringBuilder sb) {
            for(DataRow dataRow:dataRows){
                addRequest(name, dataRow,sb);
            }
            return this;
        }

        public CreateUpdateCompositeRequest.Builder addRunProcessRequest(String name) {
            RunProcessRequest request = new RunProcessRequest();
            request.setWebServiceType(name);

            Operation operation = new Operation();
            operation.setWebService(request);
            compositeRequest.addOperation(operation);

            return this;
        }

        public CompositeOperationRequest build() {
            return (new CreateUpdateCompositeRequest(this)).compositeRequest;
        }
    }
}