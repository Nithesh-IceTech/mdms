package za.co.spsi.mdms.utility;

import lombok.extern.slf4j.Slf4j;
import org.idempiere.webservice.client.base.*;
import org.idempiere.webservice.client.exceptions.RequestFactoryException;
import org.idempiere.webservice.client.request.CompositeOperationRequest;
import org.idempiere.webservice.client.request.RunProcessRequest;
import org.idempiere.webservice.client.response.WindowTabDataResponse;
import za.co.spsi.mdms.common.dao.MeterResultDataArray;
import za.co.spsi.mdms.common.dao.PrepaidBatchData;
import za.co.spsi.mdms.common.dao.PrepaidBatchTOUData;
import za.co.spsi.mdms.common.db.IceBrokerCommandStatusUpdateEntity;
import za.co.spsi.mdms.common.db.PrepaidMeterMeterReadingsView;
import za.co.spsi.mdms.common.db.survey.PecMeterEntity;
import za.co.spsi.mdms.common.db.survey.PecMeterReadingView;
import za.co.spsi.mdms.common.db.survey.PecMeterRegisterEntity;
import za.co.spsi.mdms.common.db.survey.PecPropertyEntity;
import za.co.spsi.mdms.common.db.utility.*;
import za.co.spsi.mdms.common.properties.config.PropertiesConfig;
import za.co.spsi.mdms.common.services.MDMSModelCRUDRequest;
import za.co.spsi.mdms.common.services.PrepaidProcessingHelper;
import za.co.spsi.mdms.util.IceMeterCacheService;
import za.co.spsi.mdms.utility.dao.ApprovedMeterReadingListItemEntity;
import za.co.spsi.mdms.utility.dao.CreateUpdateMeterReadingEntity;
import za.co.spsi.toolkit.crud.idempiere.CreateUpdateCompositeRequest;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.util.Assert;

import javax.ejb.AccessTimeout;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by johan on 2017/03/13.
 */
@Slf4j
@Singleton
@AccessTimeout(value=1800000)
public class BillingDataWrapper {

    public static Logger TAG = Logger.getLogger(BillingDataWrapper.class.getName());

    @Inject
    private PrepaidProcessingHelper prepaidProcessingHelper;

    @Inject
    private PropertiesConfig propertiesConfig;

    private LocalDateTime CurrentDateTime() {
        return LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    /**
     * This method starts building the Read request values to be used in the webservice call.
     * @param loginReq - LoginRequest object.
     * @param tariffScheduleId - the value to match the column : ICE_TARRIFSCHEDULE_ID in the ICE_TARIFF_SCHEDULE_V view.
     * @param dayOfWeekName - allowed values : Sunday, Saturday or Weekdays
     * @return Returns the MDMSModelCRUDRequest object
     */
    public MDMSModelCRUDRequest getTOUComparisonTimeSlotDetailRequest(LoginRequest loginReq, Integer tariffScheduleId, String dayOfWeekName) {

        MDMSModelCRUDRequest req = null;

        try {
            req = new MDMSModelCRUDRequest(Enums.WebServiceResponseModel.WindowTabDataResponse, Enums.WebServiceMethod.queryData);
            req.setLogin(loginReq);
            req.setWebServiceType("QueryTimeOfUseTariffSchedule");
            req.setTableName("ICE_TARIFF_SCHEDULE_V");
            req.setAction(Enums.ModelCRUDAction.Read);
        } catch (Exception e) {
            log.debug("MDMSModelCRUDRequest in method : getTOUComparisonTimeSlotDetailRequest() failed : " + e);
        }

        DataRow dataRow = new DataRow();
        try {
            dataRow.addField(new Field("ICE_TariffSchedule_ID", tariffScheduleId));
            dataRow.addField(new Field("DAYOFWEEK", dayOfWeekName));
        } catch (Exception e) {
            log.debug("DataRow creation in method getTOUComparisonTimeSlotDetailRequest() failed : " + e);
        }
        req.setDataRow(dataRow);

        return req;
    }

    public MDMSModelCRUDRequest getTOUTimeSlotDetailRequest(LoginRequest loginReq, Integer priceListId, Integer priceListVersion) {
        MDMSModelCRUDRequest req = new MDMSModelCRUDRequest(Enums.WebServiceResponseModel.WindowTabDataResponse, Enums.WebServiceMethod.queryData);

        req.setLogin(loginReq);
        req.setWebServiceType("QueryTOUHistory");
        req.setTableName("ICE_TOU_HIST_MV");
        req.setAction(Enums.ModelCRUDAction.Read);

        DataRow dataRow = new DataRow();
        dataRow.addField(new Field("M_PriceList_ID", priceListId));
        dataRow.addField(new Field("M_PriceList_Version_ID", priceListVersion));
        req.setDataRow(dataRow);

        return req;
    }

    public ModelCRUDRequest getApproveMeterListRequest(LoginRequest loginReq) {
        MDMSModelCRUDRequest req = new MDMSModelCRUDRequest(Enums.WebServiceResponseModel.WindowTabDataResponse, Enums.WebServiceMethod.queryData);

        req.setLogin(loginReq);
        req.setWebServiceType("GetApprovedMeterReadings");
        req.setTableName("ICE_ApprovedMeterReadings_v");
        req.setAction(Enums.ModelCRUDAction.Read);
        return req;
    }

    public ModelCRUDRequest getPropertyDetailsRequest(LoginRequest loginReq) {
        MDMSModelCRUDRequest req = new MDMSModelCRUDRequest(Enums.WebServiceResponseModel.WindowTabDataResponse, Enums.WebServiceMethod.queryData);

        req.setLogin(loginReq);
        req.setWebServiceType("QueryPropertyDetails");
        req.setTableName("ICE_PropertyDetails");
        req.setAction(Enums.ModelCRUDAction.Read);
        return req;
    }

    public ModelCRUDRequest getMeterRegisterDetailsRequest(LoginRequest loginReq) {
        MDMSModelCRUDRequest req = new MDMSModelCRUDRequest(Enums.WebServiceResponseModel.WindowTabDataResponse, Enums.WebServiceMethod.queryData);

        req.setLogin(loginReq);
        req.setWebServiceType("QueryMeterRegisterDetails");
        req.setTableName("ICE_MeterRegisterDetails");
        req.setAction(Enums.ModelCRUDAction.Read);
        return req;
    }

    public ModelRunProcessRequest getMeterVMDetailsRequest(LoginRequest loginReq, String meterNumber) {
        ModelRunProcessRequest req = new RunProcessRequest();
        req.setLogin(loginReq);
        req.setWebServiceType("ProcessMeterHierarchyPerMeter");
        ParamValues paramValues = new ParamValues();
        paramValues.addField("ICE_METER_NUMBER", meterNumber);
        req.setParamValues(paramValues);
        return req;
    }

    public ModelCRUDRequest queryMeterHierarchyFromAdpInstanceId(LoginRequest loginReq, String adpInstanceId) {
        MDMSModelCRUDRequest req = new MDMSModelCRUDRequest(Enums.WebServiceResponseModel.WindowTabDataResponse, Enums.WebServiceMethod.queryData);

        req.setLogin(loginReq);
        req.setWebServiceType("QueryMeterHierarchy");
        DataRow dataRow = new DataRow();
        dataRow.addField(new Field("AD_PInstance_ID", adpInstanceId));
        req.setDataRow(dataRow);
        return req;
    }

    public ModelCRUDRequest updateList(LoginRequest loginReq, String listNumber) {
        MDMSModelCRUDRequest req = new MDMSModelCRUDRequest(Enums.WebServiceResponseModel.WindowTabDataResponse, Enums.WebServiceMethod.updateData);

        req.setLogin(loginReq);
        req.setWebServiceType("UpdateMeterReadingListStatus");
        req.setTableName("ICE_MeterReadingList");
        req.setAction(Enums.ModelCRUDAction.Update);
        req.setRecordID(Integer.parseInt(listNumber));
        DataRow dataRow = new DataRow();
        dataRow.addField(new Field("ICE_IsProcessed", "Y"));
        req.setDataRow(dataRow);
        return req;
    }


    public ModelCRUDRequest getMeterDetailsRequest(LoginRequest loginReq) {
        MDMSModelCRUDRequest req = new MDMSModelCRUDRequest(Enums.WebServiceResponseModel.WindowTabDataResponse, Enums.WebServiceMethod.queryData);

        req.setLogin(loginReq);
        req.setWebServiceType("QueryMeterDetails");
        req.setTableName("ICE_MeterDetails");
        req.setAction(Enums.ModelCRUDAction.Read);
        return req;
    }

    public ModelCRUDRequest queryVirtualMeter(LoginRequest loginReq, String serialN) {
        MDMSModelCRUDRequest req = new MDMSModelCRUDRequest(Enums.WebServiceResponseModel.WindowTabDataResponse, Enums.WebServiceMethod.queryData);

        req.setLogin(loginReq);
        req.setWebServiceType("QueryVirtualMeter");
        req.setTableName("ICE_Virtual_Meter_v");
        req.setAction(Enums.ModelCRUDAction.Read);

        DataRow dataRow = new DataRow();
        dataRow.addField("ICE_Meter_ID", serialN);
        req.setDataRow(dataRow);

        return req;
    }


    public ModelCRUDRequest getCreateUpdateMeterReadingRequest(LoginRequest loginReq, CreateUpdateMeterReadingEntity createUpdateMeterReadingEntity) {
        MDMSModelCRUDRequest req = new MDMSModelCRUDRequest(Enums.WebServiceResponseModel.StandardResponse, Enums.WebServiceMethod.createUpdateData);

        req.setLogin(loginReq);
        req.setWebServiceType("CreateUpdateMeterReading");
        req.setTableName("ICE_MeterReadings");
        req.setAction(Enums.ModelCRUDAction.CreateUpdate);

        DataRow dataRow = new DataRow();

        dataRow.addField("ICE_MeterReadings_ID", createUpdateMeterReadingEntity.getIceMeterReadingsID());
        dataRow.addField("ICE_Meter_ID", createUpdateMeterReadingEntity.getIceMeterID());
        Assert.isTrue(createUpdateMeterReadingEntity.getQty() != null || createUpdateMeterReadingEntity.getIceMeterReading() != null,
                "Either Qty or reading must be defined ");
        if (createUpdateMeterReadingEntity.getIceMeterReading() != null) {
            dataRow.addField("ICE_Meter_Reading", createUpdateMeterReadingEntity.getIceMeterReading());
        } else {
            dataRow.addField("Qty", createUpdateMeterReadingEntity.getQty());
        }

        req.setDataRow(dataRow);

        return req;
    }

    public za.co.spsi.toolkit.crud.idempiere.DataRow getMeterReadingRow(PecMeterReadingView ent, StringBuilder sb) {
        za.co.spsi.toolkit.crud.idempiere.DataRow dataRow = new za.co.spsi.toolkit.crud.idempiere.DataRow();

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        dataRow.addField("ICE_MeterNotAvailable", "N");
        dataRow.addField("ICE_MeterReadingList_ID", ent.listReferenceId.get());
        dataRow.addField("ICE_MeterReadings_ID", ent.readingReferenceId.get());
        dataRow.addField("ICE_IsProcessed", "Y");
        dataRow.addField("ICE_Meter_ID", ent.meterReference.get());
        dataRow.addField("ICE_Meter_Reading", ent.reading.get());
        dataRow.addField("Qty", ent.smartReading.get());
        dataRow.addField("ICE_MeterReadingDate", sf.format(ent.readingDate.get()));
        dataRow.addField("ICE_ReasonNotCaptured_ID", ent.noReadingReasonCd.get());

        dataRow.getFields().stream().map(r -> r.getColumn() + ": " + r.getValue() + "\n").forEach(s -> sb.append(s));
        return dataRow;
    }

    public za.co.spsi.toolkit.crud.idempiere.DataRow getMeterRowForGPSUpdate(PecMeterReadingView ent, StringBuilder sb) {
        za.co.spsi.toolkit.crud.idempiere.DataRow dataRow = new za.co.spsi.toolkit.crud.idempiere.DataRow();

        dataRow.addField("ICE_Meter_ID", ent.pecMeter.meterReference.getAsString());
        dataRow.addField("ICE_Longitude", ent.pecMeter.lon.getAsString());
        dataRow.addField("ICE_Latitude", ent.pecMeter.lat.getAsString());

        dataRow.getFields().stream().map(r -> r.getColumn() + ": " + r.getValue() + "\n").forEach(s -> sb.append(s));
        return dataRow;
    }

    public za.co.spsi.toolkit.crud.idempiere.DataRow getMeterReadingRow(
            IceMeter meter, IcePrepaidMeter prepaidMeter, IceMeterRegister register, PrepaidMeterMeterReadingsView ent, StringBuilder sb) {
        za.co.spsi.toolkit.crud.idempiere.DataRow dataRow = new za.co.spsi.toolkit.crud.idempiere.DataRow();

        dataRow.addField("ICE_MeterNotAvailable", "N");
        dataRow.addField("ICE_MeterReadings_ID", "0");
        dataRow.addField("ICE_IsProcessed", "Y");
        dataRow.addField("ICE_Meter_ID", meter.iceMeterID.get());
        dataRow.addField("ICE_Meter_Register_ID", prepaidMeter.iceMeterRegisterId.get());

        Assert.notNull(ent.meterReadingEntity.getRegField(register.meterRegister.get()), "Could not map register [%s]", register.meterRegister.get());

        dataRow.addField("ICE_Meter_Reading", ent.meterReadingEntity.getRegField(register.meterRegister.get()).get() );
        dataRow.addField("Qty", "1");
        dataRow.addField("ICE_MeterReadingDate", ent.meterReadingEntity.entryTime.getLocal() );

        dataRow.getFields().stream().map(r -> r.getColumn() + ": " + r.getValue() + "\n").forEach(s -> sb.append(s));
        return dataRow;
    }

    public CompositeOperationRequest getCreateUpdateMeterReadingCompositeRequest(
            LoginRequest loginReq, PecMeterReadingView.PecMeterReadingViewList readingsList, StringBuilder sb) {

        CreateUpdateCompositeRequest.Builder builder = new CreateUpdateCompositeRequest.Builder().
                compositeRequest("CompositeCreateUpdateMeterReading", sb);

        for (PecMeterReadingView ent : readingsList) {
            za.co.spsi.toolkit.crud.idempiere.DataRow row = getMeterReadingRow(ent, sb);
            builder.addRequest("CreateUpdateMeterReading", row, sb);
            row = getMeterRowForGPSUpdate(ent, sb);
            builder.addRequest("UpdateMeter", row, sb);
        }

        TAG.info(String.format( "\n\nICE METER READING LIST PUSH: \n%s\n\n", readingsList.toString() ));

        sb.append("\n\n");

        CompositeOperationRequest req = builder.build();
        req.setLogin(loginReq);

        return req;
    }

    public CompositeOperationRequest getCreateUpdateMeterReadingCompositeRequest(
            DataSource iceDataSource,
            LoginRequest loginReq, DataSourceDB<PrepaidMeterMeterReadingsView> readingsView,
            IceMeterCacheService iceMeterCacheService,
            StringBuilder sb) {

        CreateUpdateCompositeRequest.Builder builder = new CreateUpdateCompositeRequest.Builder().
                compositeRequest("CompositeCreateUpdateMeterReading", sb);

        List<PrepaidMeterMeterReadingsView> readingsViewList = readingsView.getAllAsList();
        Map<String,PrepaidBatchTOUData> ppBatchTouRegisterCacheMap = new HashMap<>();

        for (PrepaidMeterMeterReadingsView ent : readingsViewList) {

            Timestamp ppBatchReadingEntryTime = ent.meterReadingEntity.entryTime.get();

            String serialNo =
                    ent.kam.meterId.get() != null ? ent.kam.serialN.get() :
                            ent.els.meterId.get() != null ? ent.els.serialN.get() :
                                    ent.genericMeterEntity.genericMeterId.get() != null ? ent.genericMeterEntity.meterSerialN.get() : ent.nes.serialN.get();

            Assert.notNull(ppBatchReadingEntryTime, "Null entry time detected for Prepaid Batch ID: %s, ICE Meter Number: %s, Meter Reading ID: %s.",
                    ent.meterReadingEntity.prepaidMeterReadingBatchId.get(),
                    serialNo,
                    ent.meterReadingEntity.meterReadingId.get());

            String meter30MinFilter = propertiesConfig.getPrepaid_batch_30min_meter_filter() == null ? "" : propertiesConfig.getPrepaid_batch_30min_meter_filter();
            Boolean latestReadingOnly = Arrays.stream(meter30MinFilter.split(",")).noneMatch(mtr -> mtr.equalsIgnoreCase(serialNo));

            for (IceMeter iceMeter : iceMeterCacheService.get(serialNo)) {
                if (iceMeter != null) {

                    for (IcePrepaidMeter prepaidMeter :
                            DataSourceDB.getAllFromSet(iceDataSource, IcePrepaidMeter.class, new IcePrepaidMeter().
                                    iceMeterID.set(iceMeter.iceMeterID.get()))) {

                        MeterResultDataArray dataSet = prepaidProcessingHelper.getPrepaidBatchMeterReadings(
                                ent.meterReadingEntity.prepaidMeterReadingBatchId.get(),
                                iceMeter.iceMeterNumber.get());

                        Assert.isTrue(dataSet != null, String.format("Meter readings dataset is null for Prepaid Batch ID: %s, ICE Meter Number: %s",
                                ent.meterReadingEntity.prepaidMeterReadingBatchId.get(),
                                iceMeter.iceMeterNumber.get()));

                        Assert.isTrue(!dataSet.isEmpty(), String.format("Meter readings dataset is empty for Prepaid Batch ID: %s, ICE Meter Number: %s",
                                ent.meterReadingEntity.prepaidMeterReadingBatchId.get(),
                                iceMeter.iceMeterNumber.get()));

                        for( IceMeterRegister register : DataSourceDB.getAllFromSet(iceDataSource, IceMeterRegister.class, new IceMeterRegister().
                                meterRegisterId.set(prepaidMeter.iceMeterRegisterId.get())) ) {

                            if (register != null) {
                                String iceMeterNumber      = iceMeter.iceMeterNumber.get();
                                String iceMeterRegisterId  = register.meterRegisterId.get();
                                String mdmsMeterRegisterId = register.meterRegister.get();

                                PrepaidBatchData prepaidBatchData =
                                        prepaidProcessingHelper.processPrepaidRegisters(dataSet, ppBatchReadingEntryTime,
                                                mdmsMeterRegisterId, latestReadingOnly);

                                PrepaidBatchTOUData prepaidBatchTOUData =
                                        prepaidProcessingHelper.processPrepaidTOURegisters(dataSet, ppBatchTouRegisterCacheMap,
                                                prepaidBatchData, iceMeterNumber, iceMeterRegisterId, mdmsMeterRegisterId,
                                                latestReadingOnly);

                                prepaidProcessingHelper.updateMeterReadingsViewPrepaidRegisterValue(ent,prepaidBatchData,
                                        prepaidBatchTOUData, mdmsMeterRegisterId);

                                za.co.spsi.toolkit.entity.Field valueField = ent.meterReadingEntity.getRegField(mdmsMeterRegisterId);
                                if (valueField != null) {
                                    if(valueField.get() != null) {
                                        if(!valueField.get().equals(0.0)) {
                                            za.co.spsi.toolkit.crud.idempiere.DataRow row = getMeterReadingRow(iceMeter, prepaidMeter, register, ent, sb);
                                            builder.addRequest("CreateUpdateMeterReading", row, sb);
                                        }
                                    }
                                } else {
                                    sb.append(String.format("Register: %s, field value is null for ICE Meter ID: %s, ICE Meter Number %s\n",
                                            prepaidMeter.iceMeterRegisterId.get(),
                                            prepaidMeter.iceMeterID.getAsString(),
                                            serialNo));
                                }

                            } else {
                                sb.append(String.format("Register: %s not found in ICE_METER_REGISTER table for ICE Meter ID %s, ICE Meter Number %s\n",
                                        prepaidMeter.iceMeterRegisterId.get(),
                                        prepaidMeter.iceMeterID.getAsString(),
                                        serialNo));
                            }

                        }

                    }
                } else {
                    sb.append(String.format("Could not locate utility meter by serial number (%s) in MDMS\n", serialNo));
                    TAG.warning(String.format("Could not locate utility meter by serial number (%s) in MDMS\n", serialNo));
                }
            }

            if(latestReadingOnly) break;
        }

        sb.append("\n\n");

        CompositeOperationRequest req = builder.build();
        req.setLogin(loginReq);

        return req;
    }

    public ModelCRUDRequest getUpdateMeterReadingStatus(
            LoginRequest loginReq, IceBrokerCommandStatusUpdateEntity iceBrokerCommandStatusUpdateEntity) throws RequestFactoryException {

        MDMSModelCRUDRequest req = new MDMSModelCRUDRequest(Enums.WebServiceResponseModel.StandardResponse,
                Enums.WebServiceMethod.updateData);

        req.setLogin(loginReq);
        req.setWebServiceType("UpdateMeterReadingStatus");
        req.setRecordID( Integer.parseInt(iceBrokerCommandStatusUpdateEntity.getIceMeterId().get())  );
        req.setAction(Enums.ModelCRUDAction.Update);

        DataRow dataRow = new DataRow();

        if (iceBrokerCommandStatusUpdateEntity != null) {
            dataRow.addField("ICE_Meter_Status_Date", iceBrokerCommandStatusUpdateEntity.getIce_meter_status_date_formatted());
            dataRow.addField("ICE_Meter_Status", iceBrokerCommandStatusUpdateEntity.getIceMeterStatus().get());
            TAG.info(String.format("UpdateMeterReadingStatus Request: %s", iceBrokerCommandStatusUpdateEntity.getIceMeterStatus().get()));
        }

        req.setDataRow(dataRow);

        return req;
    }

    public ModelCRUDRequest getCreateMeterReadingStatusRecord(
            LoginRequest loginReq, IceBrokerCommandStatusUpdateEntity iceBrokerCommandStatusUpdateEntity) {

        MDMSModelCRUDRequest req = new MDMSModelCRUDRequest(Enums.WebServiceResponseModel.StandardResponse,
                Enums.WebServiceMethod.createData);

        req.setLogin(loginReq);
        req.setWebServiceType("CreateMeterReadingStatusRecord");
        req.setAction(Enums.ModelCRUDAction.Create);

        DataRow dataRow = new DataRow();

        if (iceBrokerCommandStatusUpdateEntity != null) {
            dataRow.addField("ICE_Message", iceBrokerCommandStatusUpdateEntity.getIceMessage().get());
            dataRow.addField("ICE_Meter_Status", iceBrokerCommandStatusUpdateEntity.getIceMeterStatus().get());
            dataRow.addField("ICE_Meter_ID", iceBrokerCommandStatusUpdateEntity.getIceMeterId().get());
            TAG.info(String.format("CreateMeterReadingStatusRecord Request: %s ", iceBrokerCommandStatusUpdateEntity.getIceMessage().get()));
        }

        req.setDataRow(dataRow);

        return req;
    }

    private boolean isFieldEmpty(Field field) {
        return (field == null) || (field.getStringValue().equals(""));
    }

    private PecMeterEntity mapMeterRow(DataRow row) {
        PecMeterEntity ent = new PecMeterEntity();

        Field field = row.getField("IsActive");
        if (field.getStringValue() != null) {
            ent.activeCd.set(field.getStringValue().equalsIgnoreCase("Y") ? 1 : 0);
        }

        field = row.getField("ICE_Meter_Number");
        if (field.getStringValue() != null) {
            ent.meterN.set(field.getStringValue());
        }

        field = row.getField("ICE_Meter_Actual_Size");
        if (!isFieldEmpty(field)) {
            ent.actualSizeWater.set(field.getIntValue());
        }

        field = row.getField("ICE_Meter_Breaker_Size_Rel");
        if (!isFieldEmpty(field)) {
            ent.relevantSizeWater.set(field.getIntValue());
        }

        field = row.getField("ICE_Meter_Location");
        if (field.getStringValue() != null) {
            ent.meterLocation.set(field.getStringValue());
        }

        field = row.getField("ICE_Longitude");
        if (!isFieldEmpty(field)) {
            ent.lon.set(field.getDoubleValue());
        }

        field = row.getField("ICE_Latitude");
        if (!isFieldEmpty(field)) {
            ent.lat.set(field.getDoubleValue());
        }

        field = row.getField("ICE_Meter_ID");
        if (field.getStringValue() != null) {
            ent.meterReference.set(field.getStringValue());
        }

        //TODO
        field = row.getField("ICE_Meter_Configuration");
        if (field.getStringValue() != null) {
            ent.meterConfigurationCd.set(field.getStringValue());
        }

        field = row.getField("ICE_Meter_Prepaid");
        if (field.getStringValue() != null) {
            ent.prepaidCd.set(field.getStringValue().equalsIgnoreCase("Y") ? 1 : 0);
        }

        field = row.getField("ICE_Meter_Make_ID");
        if (!isFieldEmpty(field)) {
            ent.meterMakeCd.set(field.getIntValue());
        }

        field = row.getField("ICE_Meter_Model_ID");
        if (!isFieldEmpty(field)) {
            ent.meterModelCd.set(field.getIntValue());
        }
        return ent;
    }

    private PecMeterRegisterEntity mapMeterRegisterRow(DataRow row) {
        PecMeterRegisterEntity ent = new PecMeterRegisterEntity();

        Field field = row.getField("ICE_Meter_ChannelID");
        if (!isFieldEmpty(field)) {
            ent.channelId.set(field.getIntValue());
        }

        field = row.getField("ICE_Meter_Register_ID");
        if (field.getStringValue() != null) {
            ent.registerId.set(field.getStringValue());
        }

        //TODO: meter mapping!!

        field = row.getField("ICE_LoadProfileNo");
        if (!isFieldEmpty(field)) {
            ent.loadProfileNumber.set(field.getIntValue());
        }

        field = row.getField("ICE_Meter_Digits");
        if (!isFieldEmpty(field)) {
            ent.digits.set(field.getIntValue());
        }

        field = row.getField("ICE_MeterRegisterType_ID");
        if (field.getStringValue() != null) {
            ent.meterRegisterTypeCd.set(field.getStringValue());
        }

        return ent;
    }

    private PecPropertyEntity mapPropertyRow(DataRow row) {
        PecPropertyEntity ent = new PecPropertyEntity();

        Field field = row.getField("ICE_Property_ID");
        if (field.getStringValue() != null) {
            ent.getBaseSharedSyncEntity().reference_id.set(field.getStringValue());
        }

        field = row.getField("ICE_Street_Name");
        if (field.getStringValue() != null) {
            ent.sharedLocation.streetName.set(field.getStringValue());
        }

        field = row.getField("ICE_Property_Type_ID");
        if (!isFieldEmpty(field)) {
            ent.propertyTypeCd.set(field.getIntValue());
        }

        field = row.getField("Name");
        if (field.getStringValue() != null) {
            ent.propertyName.set(field.getStringValue());
        }

        field = row.getField("ICE_District_ID");
        if (!isFieldEmpty(field)) {
            ent.sharedLocation.cityCd.set(field.getIntValue());
        }

        field = row.getField("C_Country_ID");
        if (field.getStringValue() != null) {
            ent.sharedLocation.countryCd.set("ZA");
        }

        field = row.getField("C_Region_ID");
        if (!isFieldEmpty(field)) {
            ent.sharedLocation.provinceCd.set(field.getIntValue());
        }

        field = row.getField("ICE_Property_External_Ref");
        if (field.getStringValue() != null) {
            ent.externalRef.set(field.getStringValue());
        }

        field = row.getField("ICE_PropertyEntityType_ID");
        if (!isFieldEmpty(field)) {
            ent.propertyEntityTypeCd.set(field.getIntValue());
        }

        field = row.getField("ICE_Street_Number");
        if (field.getStringValue() != null) {
            ent.sharedLocation.standNumber.set(field.getStringValue());
        }

        field = row.getField("ICE_Latitude");
        if (!isFieldEmpty(field)) {
            ent.sharedLocation.lat.set(field.getDoubleValue());
        }

        field = row.getField("ICE_Longitude");
        if (!isFieldEmpty(field)) {
            ent.sharedLocation.lon.set(field.getDoubleValue());
        }

        return ent;
    }


    private ApprovedMeterReadingListItemEntity mapRow(DataRow row) {
        ApprovedMeterReadingListItemEntity ent = new ApprovedMeterReadingListItemEntity();

        Field field = row.getField("AD_Client_ID");
        if (field.getIntValue() != null) {
            ent.setAdClientID(field.getIntValue());
        }

        field = row.getField("AD_Org_ID");
        if (field.getIntValue() != null) {
            ent.setAdOrgID(field.getIntValue());
        }

        field = row.getField("Created");
        if (!isFieldEmpty(field)) {
            ent.setCreated(field.getDateValue());
        }

        field = row.getField("Updated");
        if (!isFieldEmpty(field)) {
            ent.setUpdated(field.getDateValue());
        }

        field = row.getField("ICE_MeterReadingDate");
        if (!isFieldEmpty(field)) {
            ent.setIceMeterReadingDate(field.getDateValue());
        }

        field = row.getField("ICE_MeterReadingList_ID");
        if (field.getIntValue() != null) {
            ent.setIceMeterReadingListID(field.getIntValue());
        }

        field = row.getField("ICE_MeterReadings_ID");
        if (field.getIntValue() != null) {
            ent.setIceMeterReadingsID(field.getIntValue());
        }

        field = row.getField("ICE_Meter_ID");
        if (field.getStringValue() != null) {
            ent.setIceMeterID(field.getStringValue());
        }

        field = row.getField("ICE_Meter_Register_ID");
        if (field.getStringValue() != null) {
            ent.setIceMeterRegisterID(field.getStringValue());
        }

        field = row.getField("ICE_Property_ID");
        if (field.getStringValue() != null) {
            ent.setIcePropertyID(field.getStringValue());
        }

        return ent;

    }

    public ArrayList<ApprovedMeterReadingListItemEntity> mapGetApprovedMeterListResponse(WebServiceResponse response) {
        DataSet dataset = ((WindowTabDataResponse) response).getDataSet();
        ArrayList<ApprovedMeterReadingListItemEntity> list = new ArrayList<ApprovedMeterReadingListItemEntity>();
        for (DataRow row : dataset.getRows()) {
            ApprovedMeterReadingListItemEntity ent = mapRow(row);
            list.add(ent);
        }

        return list;
    }

    public ArrayList<PecMeterEntity> mapMeterDetailListResponse(WebServiceResponse response) {
        DataSet dataset = ((WindowTabDataResponse) response).getDataSet();
        ArrayList<PecMeterEntity> list = new ArrayList<PecMeterEntity>();
        for (DataRow row : dataset.getRows()) {
            PecMeterEntity ent = mapMeterRow(row);
            list.add(ent);
        }
        return list;
    }

    public Map<String, PecMeterEntity> mapMeterDetailListResponseAsMap(WebServiceResponse response) {
        DataSet dataset = ((WindowTabDataResponse) response).getDataSet();
        HashMap<String, PecMeterEntity> list = new HashMap();
        for (DataRow row : dataset.getRows()) {
            PecMeterEntity ent = mapMeterRow(row);
            list.put(ent.meterReference.get(), ent);
        }
        return list;
    }


    public ArrayList<PecPropertyEntity> mapPropertyDetailListResponse(WebServiceResponse response) {
        DataSet dataset = ((WindowTabDataResponse) response).getDataSet();
        ArrayList<PecPropertyEntity> list = new ArrayList<PecPropertyEntity>();
        for (DataRow row : dataset.getRows()) {
            PecPropertyEntity ent = mapPropertyRow(row);
            list.add(ent);
        }
        return list;

    }

    public Map<String, PecPropertyEntity> mapPropertyDetailListResponseAsMap(WebServiceResponse response) {
        DataSet dataset = ((WindowTabDataResponse) response).getDataSet();
        HashMap<String, PecPropertyEntity> list = new HashMap();
        for (DataRow row : dataset.getRows()) {
            PecPropertyEntity ent = mapPropertyRow(row);
            list.put(ent.sharedEntity.reference_id.get(), ent);
        }
        return list;

    }

    public Map<String, PecMeterRegisterEntity> mapMeterRegisterDetailListResponse(WebServiceResponse response) {
        DataSet dataset = ((WindowTabDataResponse) response).getDataSet();
        HashMap<String, PecMeterRegisterEntity> list = new HashMap<>();
        for (DataRow row : dataset.getRows()) {
            PecMeterRegisterEntity ent = mapMeterRegisterRow(row);
            list.put(ent.registerId.get(), ent);
        }
        return list;

    }

    public IceTimeOfUseHistRow mapTimeOfUseHistRow(DataRow row) {
        IceTimeOfUseHistRow ent = new IceTimeOfUseHistRow();

        Field field = row.getField("M_PriceList_ID");
        if (field.getIntValue() != null) {
            ent.setMPriceListId(field.getIntValue());
        }

        field = row.getField("M_PriceList_Version_ID");
        if (field.getIntValue() != null) {
            ent.setMPriceListVersionId(field.getIntValue());
        }

        field = row.getField("PLV_NAME");
        if (!isFieldEmpty(field)) {
            ent.setPlvName(field.getStringValue());
        }

        field = row.getField("PLV_VALID_FROM");
        if (!isFieldEmpty(field)) {
            ent.setPlvValidFrom(field.getDateValue());
        }

        field = row.getField("StartTime");
        if (!isFieldEmpty(field)) {
            ent.setStartTime(field.getDateValue());
        }

        field = row.getField("EndTime");
        if (!isFieldEmpty(field)) {
            ent.setEndTime(field.getDateValue());
        }

        field = row.getField("DOW_NAME");
        if (!isFieldEmpty(field)) {
            ent.setDowName(field.getStringValue());
        }

        return ent;
    }

    public IceTOURow mapTimeOfUseComparisonHistRow(DataRow row) {
        IceTOURow ent = new IceTOURow();

        Field field = row.getField("ICE_TariffSchedule_ID");
        if (field.getIntValue() != null) {
            ent.setICE_TARRIFFSCHEDULE_ID(field.getIntValue());
        }

        field = row.getField("AD_Client_ID");
        if (field.getIntValue() != null) {
            ent.setAD_CLIENT_ID(field.getIntValue());
        }

        field = row.getField("AD_Org_ID");
        if (!isFieldEmpty(field)) {
            ent.setAD_ORG_ID(field.getIntValue());
        }

        field = row.getField("SCHEDULENAME");
        if (!isFieldEmpty(field)) {
            ent.setSCHEDULENAME(field.getStringValue());
        }

        field = row.getField("VALID_FROM");
        if (!isFieldEmpty(field)) {
            ent.setVALID_FROM(field.getDateValue());
        }

        field = row.getField("TIMEOFUSE");
        if (!isFieldEmpty(field)) {
            ent.setTIMEOFUSE(field.getStringValue());
        }

        field = row.getField("DAYOFWEEK");
        if (!isFieldEmpty(field)) {
            ent.setDAYOFWEEK(field.getStringValue());
        }

        field = row.getField("StartTime");
        if (!isFieldEmpty(field)) {
            ent.setSTARTTIME(field.getDateValue());
        }

        field = row.getField("EndTime");
        if (!isFieldEmpty(field)) {
            ent.setENDTIME(field.getDateValue());
        }

        field = row.getField("DAYOFWEEK");
        if (!isFieldEmpty(field)) {
            ent.setDAYOFWEEK(field.getStringValue());
        }

        return ent;
    }

}
