package za.co.spsi.toolkit.crud.sync.service;

import org.json.JSONObject;
import za.co.spsi.toolkit.crud.sync.db.BatchEntity;
import za.co.spsi.toolkit.crud.sync.db.DeviceEntity;
import za.co.spsi.toolkit.crud.sync.db.DeviceEntitySyncMapEntity;
import za.co.spsi.toolkit.dao.DeviceEntityMapReq;
import za.co.spsi.toolkit.dao.RegisterDeviceReq;
import za.co.spsi.toolkit.dao.RegisterDeviceResp;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.ee.properties.ConfValue;
import za.co.spsi.toolkit.ee.security.Secured;
import za.co.spsi.toolkit.io.IOUtil;

import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jaspervdbijl on 2017/04/07.
 */
public abstract class DataSyncController {

    private static final Logger LOG = Logger.getLogger(DataSyncController.class.getName());

    @Inject
    private BeanManager beanManager;

    @Inject
    @ConfValue("enc_key")
    private String encKey = "";

    @Inject
    @ConfValue("system_user_password")
    private String systemUserPassword = "";


    public abstract DataSource getDataSource();


    @Path(value = "register")
    @POST
    @Consumes("application/json")
    public RegisterDeviceResp register(RegisterDeviceReq registerDeviceReq) {
        DeviceEntity.registerDevice(registerDeviceReq, getDataSource());
        DeviceEntity.updateLastCommsForDevice(registerDeviceReq.getDeviceId(), getDataSource());
        return new RegisterDeviceResp();
    }

    @Path("upload/{batchId}")
    @POST
    @Secured
    public Response upload(@Context HttpServletRequest request, @PathParam("batchId") String batchId) {
        return uploadBatchData(request, null, batchId);
    }

    @Path("upload/{deviceId}/{batchId}")
    @POST
    @Secured
    public Response upload(@Context HttpServletRequest request, @PathParam("deviceId") String deviceId, @PathParam("batchId") String batchId,
                           Class<? extends BatchProcessor.BatchProcessWorker> processor) {

        DeviceEntity.updateLastCommsForDevice(deviceId, getDataSource());
        DeviceEntity deviceEntity = DataSourceDB.get(DeviceEntity.class, getDataSource(), "select * from device where DEVICE_ID = ?", deviceId);
        if (deviceEntity != null) {
            DataSourceDB.set(getDataSource(), (EntityDB) deviceEntity.apkVersion.set(request.getHeader("ApkVersion")));
        }

        LOG.info("DATA - Data Received for " + batchId);
        BatchEntity batchEntity = new BatchEntity(processor == null ? BatchProcessor.DefaultProcessor.class : processor);
        batchEntity.batchId.set(batchId);

        if (DataSourceDB.loadFromId(getDataSource(), batchEntity) == null) {
            try {
                batchEntity.data.set(new String(IOUtil.readFully(request.getInputStream(), request.getContentLength())));
                batchEntity.deviceId.set(deviceId);
                batchEntity.batchStatusCd.set(BatchProcessor.STATUS_UNPROCESSED);
                batchEntity.syncStatusCd.set(BatchProcessor.STATUS_UNPROCESSED);
                DataSourceDB.set(getDataSource(), batchEntity);
                return Response.status(Response.Status.OK).build();

            } catch (IOException e) {
                LOG.severe(e.getMessage());
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            LOG.warning("Duplicate key found " + batchId);
            return Response.status(Response.Status.CONFLICT).build();
        }
    }

    @Path("uploadData/{batchId}")
    @POST
    @Secured
    public Response uploadData(@Context HttpServletRequest request, @PathParam("batchId") String batchId) {
        return uploadBatchData(request, null, batchId);
    }

    @Path("uploadBatchData/{deviceId}/{batchId}")
    @POST
    @Secured(anonymous = true)
    public Response uploadBatchData(@Context HttpServletRequest request, @PathParam("deviceId") String deviceId, @PathParam("batchId") String batchId) {
        return upload(request, deviceId, batchId, null);
    }


    /**
     * Get data that was pushed to the device
     *
     * @param deviceId
     * @return
     */
    @Path("download/{deviceId}")
    @GET
    @Secured
    public Response retrieveAndRecallData(@Context HttpServletRequest request, @PathParam("deviceId") String deviceId) {
        try {
            DeviceEntity device = DataSourceDB.loadFromId(getDataSource(), new DeviceEntity(), deviceId);
            if (device != null) {
                DeviceEntity.updateLastCommsForDevice(deviceId, getDataSource());
                DataSourceDB.set(getDataSource(), (EntityDB) device.apkVersion.set(request.getHeader("ApkVersion")));

                JSONObject jsonObject = DeviceEntitySyncMapEntity.getSyncDataForDevice(getDataSource(), device, 1);
                return jsonObject != null ? Response.status(Response.Status.OK).entity(jsonObject.toString()).build() :
                        Response.status(Response.Status.NO_CONTENT).build();

            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (Exception e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Data delivery was successful, mark all indices as delivered
     *
     * @return
     */
    @Path("downloadOK")
    @POST
    @Consumes("application/json")
    @Secured
    public Response retrieveDataOk(List<DeviceEntityMapReq> deviceEntityMapReqList) {
        try {
            for (DeviceEntityMapReq deviceEntityMapReq : deviceEntityMapReqList) {
                // process multi cast logic
                DeviceEntitySyncMapEntity syncEntity = DataSourceDB.loadFromId(getDataSource(), new DeviceEntitySyncMapEntity(),
                        deviceEntityMapReq.getDeviceEntitySyncMapId());
                if (syncEntity != null) {
                    syncEntity.process(getDataSource(), deviceEntityMapReq);
                }
            }

            return Response.status(Response.Status.OK).build();
        } catch (Exception e) {
            LOG.log(Level.WARNING, e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }


    // TODO implement ICEFSS_TRACKING user logic
    @Secured(anonymous = true)
    @Path("getSecureKey")
    @GET
    public Response getSecureKey(@Context HttpServletRequest request) {
        return Response.status(Response.Status.OK).entity(encKey).build();
    }

    @Secured(anonymous = true)
    @Path("getSystemUserPassword")
    @GET
    public Response getSystemUserPassword(@Context HttpServletRequest request) {
        return Response.status(Response.Status.OK).entity(systemUserPassword).build();
    }


    /*
    Username: ICEFSS_TRACKINGPassword: nkt25mjf
     */
}
