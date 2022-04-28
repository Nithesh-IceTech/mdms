package za.co.spsi.toolkit.crud.payment.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.steinwedel.messagebox.MessageBox;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.crud.gui.ToolkitUI;
import za.co.spsi.toolkit.crud.gui.render.AbstractView;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.crud.payment.util.RestClient;
import za.co.spsi.toolkit.dao.ToolkitConstants;

/**
 * Created by francoism on 2017/10/03.
 */

public class EcoCashPaymentService {


    public EcoCashPaymentService() {
    }

    public static Integer makePayment(EcoCashRequest req, String feeType) {
        try {
            int pollingRetryCount = 0;
            String token = ToolkitUI.getToolkitUI().getToken();

            System.out.println("ECO-Cash TransactionGUID was: " + req.getTxGuid());
            System.out.println("ECO-Cash Cell number was: " + req.getIdentifier());
            System.out.println("ECO-Cash token: " + token);
            System.out.println("ECO-Cash realm is: " + ToolkitCrudConstants.getRealm());

            ResponseEntity result = RestClient.makeRequest(ToolkitCrudConstants.getEcoCashUrl() + "/paymentRequest",
                    new ObjectMapper().writeValueAsString(req), getRestProperties());
            if (result.getStatusCode() == HttpStatus.OK) {
                EcoCashResponse resp = new ObjectMapper().readValue(result.getBody().toString(), EcoCashResponse.class);

                if ((resp.getStatusCd().equals("3") && resp.getResultCd().equals("1"))
                        || resp.getStatusCd().equals("8")) {
                    EcoCashPollResponse pollResp = new EcoCashPollResponse("8", "1");
                    while (pollResp.getPaymentStatusCd().equals("8") &&
                            pollResp.getPaymentResultCd().equals("1")) {
                        ResponseEntity pollResult = RestClient.makeRequest(ToolkitCrudConstants.getEcoCashUrl() + "/paymentPoll",
                                new ObjectMapper().writeValueAsString(new EcoCashPollRequest("1.0.0",
                                        resp.getTxGuid(), resp.getMachineName(), new Date(System.currentTimeMillis()))), getRestProperties());
                        if (pollResult.getStatusCode() == HttpStatus.OK && pollingRetryCount < ToolkitCrudConstants.getEcoCashPollingRetry()) {
                            pollingRetryCount++;
                            pollResp = new ObjectMapper().readValue(pollResult.getBody().toString(), EcoCashPollResponse.class);
                            if (pollResp.getStatusCd().equals("3") && pollResp.getResultCd().equals("1")) {
                                if (pollResp.getPaymentStatusCd().equals("3") && pollResp.getPaymentResultCd().equals("1")) {
//                                    respond(AbstractView.getLocaleValue(ToolkitLocaleId.ECO_SUCCESS));
                                    return ToolkitConstants.PAID_STATUS_PAID;
                                } else if (pollResp.getPaymentStatusCd().equals("8") && pollResp.getPaymentResultCd().equals("1")) {
                                    Thread.sleep(10000);
                                } else {
                                    respond(AbstractView.getLocaleValue(ToolkitLocaleId.ECO_FAILURE) + " " + pollResp.getMessage() + "\n" +
                                            AbstractView.getLocaleValue(ToolkitLocaleId.STATUS_CODE) + " " + pollResp.getStatusCd() + "\n" +
                                            AbstractView.getLocaleValue(ToolkitLocaleId.RESULT_CODE) + " " + pollResp.getResultCd() + "\n" +
                                            AbstractView.getLocaleValue(ToolkitLocaleId.PAYMENT_STATUS_CODE) + " " + pollResp.getPaymentStatusCd() + "\n" +
                                            AbstractView.getLocaleValue(ToolkitLocaleId.PAYMENT_RESULT_CODE) + " " + pollResp.getPaymentResultCd(), feeType);
                                    return ToolkitConstants.PAID_STATUS_UNPAID;
                                }
                            } else {
                                //FAIL because poll request is invalid
                            }
                        } else if(pollResult.getStatusCode() != HttpStatus.OK){
                            //if invalid response on polling, the request was already sent, so retry in 5 seconds for 5 times.
                            pollingRetryCount++;
                            if (pollingRetryCount < 5) {
                                Thread.sleep(5000);
                            } else {
                                //TODO write this to a DB table to cancel request to eco-cash?
                                respond(AbstractView.getLocaleValue(ToolkitLocaleId.ERROR_ON_ECO_PAY) + " " +
                                        AbstractView.getLocaleValue(ToolkitLocaleId.PAYMENT_MIGHT_HAVE_COMPLETED), feeType);
                                return ToolkitConstants.PAID_STATUS_UNPAID;
                            }
                        } else if(pollResult.getStatusCode() == HttpStatus.OK && pollingRetryCount >= ToolkitCrudConstants.getEcoCashPollingRetry()){
                            respond(AbstractView.getLocaleValue(ToolkitLocaleId.NO_RESPONSE_FROM_ECO) + " " +
                                    AbstractView.getLocaleValue(ToolkitLocaleId.PAYMENT_MIGHT_HAVE_COMPLETED), feeType);
                            return ToolkitConstants.PAID_STATUS_PENDING;
                        }
                    }
                } else if (resp.getStatusCd().equals("6") && resp.getResultCd().equals("0")) {
                    //POLL a couple of times, unlikely to happen
                    //TODO Implement polling here
                } else {
                    //Cannot poll, invalid polling status returned
                    respond(AbstractView.getLocaleValue(ToolkitLocaleId.ECO_FAILURE) + " " + resp.getMessage() + "\n" +
                            AbstractView.getLocaleValue(ToolkitLocaleId.STATUS_CODE) + " " + resp.getStatusCd() + "\n" +
                            AbstractView.getLocaleValue(ToolkitLocaleId.RESULT_CODE) + " " + resp.getResultCd(), feeType);
                    return ToolkitConstants.PAID_STATUS_UNPAID;
                }
            } else if (result.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                respond(AbstractView.getLocaleValue(ToolkitLocaleId.ERROR_ON_ECO_PAY)
                        + " " + AbstractView.getLocaleValue(ToolkitLocaleId.INVALID_TOKEN_FOR_REALM), feeType);
                return ToolkitConstants.PAID_STATUS_UNPAID;
            } else {
                respond(AbstractView.getLocaleValue(ToolkitLocaleId.ERROR_ON_ECO_PAY)
                        + " " + AbstractView.getLocaleValue(ToolkitLocaleId.INVALID_RESPONSE_FROM_ECO)
                        + " " + result.getStatusCode(), feeType);
                return ToolkitConstants.PAID_STATUS_UNPAID;
            }
        } catch (Exception e) {
            e.printStackTrace();
            respond(ToolkitLocaleId.ERROR_ON_ECO_PAY, feeType);
            return ToolkitConstants.PAID_STATUS_UNPAID;
        }
        respond(AbstractView.getLocaleValue(ToolkitLocaleId.ERROR_ON_ECO_PAY)
                + " " + AbstractView.getLocaleValue(ToolkitLocaleId.UNKNOWN), feeType);
        return ToolkitConstants.PAID_STATUS_UNPAID;
    }

    private static Map<String, String> getRestProperties() {
        Map<String, String> map = new HashMap<>();
        map.put("Content-Type", "application/json");
        map.put("Accept", "application/json");
        map.put("bearer_token", ToolkitUI.getToolkitUI().getToken());
        map.put("realm", ToolkitCrudConstants.getRealm());
        return map;
    }

    public static void respond(String message, String feeType) {
        MessageBox.createInfo().withCaption(AbstractView.getLocaleValue(ToolkitLocaleId.ECO_CASH_RESULT) + " - " + feeType).withMessage(message).open();

    }

    public static class EcoCashRequest implements Serializable {
        private String txVersion, txGuid, machineName, location, iceCashTxCode, paymentMethod, identifierType, identifier, amount;
        private Date createT;

        public EcoCashRequest() {
        }

        public EcoCashRequest(String txVersion, String txGuid, String machineName, String location,
                              String iceCashTxCode, String paymentMethod, String identifierType, Date createT,
                              String identifier, String amount) {
            this.txVersion = txVersion;
            this.txGuid = txGuid;
            this.machineName = machineName;
            this.location = location;
            this.iceCashTxCode = iceCashTxCode;
            this.paymentMethod = paymentMethod;
            this.identifierType = identifierType;
            this.createT = createT;
            this.identifier = identifier;
            this.amount = amount;
        }

        public String getTxVersion() {
            return txVersion;
        }

        public void setTxVersion(String txVersion) {
            this.txVersion = txVersion;
        }

        public String getTxGuid() {
            return txGuid;
        }

        public void setTxGuid(String txGuid) {
            this.txGuid = txGuid;
        }

        public String getMachineName() {
            return machineName;
        }

        public void setMachineName(String machineName) {
            this.machineName = machineName;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getIceCashTxCode() {
            return iceCashTxCode;
        }

        public void setIceCashTxCode(String iceCashTxCode) {
            this.iceCashTxCode = iceCashTxCode;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        public String getIdentifierType() {
            return identifierType;
        }

        public void setIdentifierType(String identifierType) {
            this.identifierType = identifierType;
        }

        public Date getCreateT() {
            return createT;
        }

        public void setCreateT(Date createT) {
            this.createT = createT;
        }

        public String getIdentifier() {
            return identifier;
        }

        public void setIdentifier(String identifier) {
            this.identifier = identifier;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }
    }


    public static class EcoCashResponse implements Serializable {
        private String txVersion, txGuid, machineName, message, statusCd, resultCd;

        public EcoCashResponse() {
        }

        public EcoCashResponse(String txVersion, String txGuid, String machineName, String message,
                               String statusCd, String resultCd) {
            this.txVersion = txVersion;
            this.txGuid = txGuid;
            this.machineName = machineName;
            this.message = message;
            this.statusCd = statusCd;
            this.resultCd = resultCd;
        }

        public String getTxVersion() {
            return txVersion;
        }

        public void setTxVersion(String txVersion) {
            this.txVersion = txVersion;
        }

        public String getTxGuid() {
            return txGuid;
        }

        public void setTxGuid(String txGuid) {
            this.txGuid = txGuid;
        }

        public String getMachineName() {
            return machineName;
        }

        public void setMachineName(String machineName) {
            this.machineName = machineName;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getStatusCd() {
            return statusCd;
        }

        public void setStatusCd(String statusCd) {
            this.statusCd = statusCd;
        }

        public String getResultCd() {
            return resultCd;
        }

        public void setResultCd(String resultCd) {
            this.resultCd = resultCd;
        }
    }

    public static class EcoCashPollRequest implements Serializable {
        private String txVersion, txGuid, machineName;
        private Date createT;

        public EcoCashPollRequest() {
        }

        public EcoCashPollRequest(String txVersion, String txGuid, String machineName, Date createT) {
            this.txVersion = txVersion;
            this.txGuid = txGuid;
            this.machineName = machineName;
            this.createT = createT;
        }

        public String getTxVersion() {
            return txVersion;
        }

        public void setTxVersion(String txVersion) {
            this.txVersion = txVersion;
        }

        public String getTxGuid() {
            return txGuid;
        }

        public void setTxGuid(String txGuid) {
            this.txGuid = txGuid;
        }

        public String getMachineName() {
            return machineName;
        }

        public void setMachineName(String machineName) {
            this.machineName = machineName;
        }

        public Date getCreateT() {
            return createT;
        }

        public void setCreateT(Date createT) {
            this.createT = createT;
        }
    }

    public static class EcoCashPollResponse implements Serializable {
        private String txVersion, txGuid, machineName, message, statusCd, resultCd, paymentStatusCd, paymentResultCd;

        public EcoCashPollResponse() {
        }

        public EcoCashPollResponse(String paymentStatusCd, String paymentResultCd) {
            this.paymentStatusCd = paymentStatusCd;
            this.paymentResultCd = paymentResultCd;
        }

        public EcoCashPollResponse(String txVersion, String txGuid, String machineName, String message,
                                   String statusCd, String resultCd, String paymentStatusCd, String paymentResultCd) {
            this.txVersion = txVersion;
            this.txGuid = txGuid;
            this.machineName = machineName;
            this.message = message;
            this.statusCd = statusCd;
            this.resultCd = resultCd;
            this.paymentStatusCd = paymentStatusCd;
            this.paymentResultCd = paymentResultCd;
        }

        public String getTxVersion() {
            return txVersion;
        }

        public void setTxVersion(String txVersion) {
            this.txVersion = txVersion;
        }

        public String getTxGuid() {
            return txGuid;
        }

        public void setTxGuid(String txGuid) {
            this.txGuid = txGuid;
        }

        public String getMachineName() {
            return machineName;
        }

        public void setMachineName(String machineName) {
            this.machineName = machineName;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getStatusCd() {
            return statusCd;
        }

        public void setStatusCd(String statusCd) {
            this.statusCd = statusCd;
        }

        public String getResultCd() {
            return resultCd;
        }

        public void setResultCd(String resultCd) {
            this.resultCd = resultCd;
        }

        public String getPaymentStatusCd() {
            return paymentStatusCd;
        }

        public void setPaymentStatusCd(String paymentStatusCd) {
            this.paymentStatusCd = paymentStatusCd;
        }

        public String getPaymentResultCd() {
            return paymentResultCd;
        }

        public void setPaymentResultCd(String paymentResultCd) {
            this.paymentResultCd = paymentResultCd;
        }
    }
}