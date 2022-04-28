package za.co.spsi.mdms.common.controllers;

import za.co.spsi.toolkit.crud.controller.LookupController;
import za.co.spsi.toolkit.crud.sync.service.BatchProcessor;
import za.co.spsi.toolkit.crud.sync.service.DataSyncController;
import za.co.spsi.toolkit.dao.DeviceEntityMapReq;
import za.co.spsi.toolkit.dao.RegisterDeviceReq;
import za.co.spsi.toolkit.dao.RegisterDeviceResp;

import javax.annotation.Resource;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by jaspervdb on 15/06/09.
 */
@Path("mdmsController")
public class MDMSController extends DataSyncController implements LookupController {

    private static final Logger LOG = Logger.getLogger(MDMSController.class.getName());

    @Resource(mappedName = "java:/jdbc/mdms")
    private javax.sql.DataSource dataSource;

    @Inject
    private BeanManager beanManager;

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }

    @Path("download/{deviceId}")
    @GET
    public Response retrieveAndRecallData(@Context HttpServletRequest request, @PathParam("deviceId") String deviceId) {
        return super.retrieveAndRecallData(request, deviceId);
    }

    @Path(value = "register")
    @POST
    @Consumes("application/json")
    @Override
    public RegisterDeviceResp register(RegisterDeviceReq registerDeviceReq) {
        return super.register(registerDeviceReq);
    }

    @Path("upload/{batchId}")
    @POST
    @Override
    public Response upload(@Context HttpServletRequest request, @PathParam("batchId") String batchId) {
        return super.upload(request, batchId);
    }

    @Path("upload/{deviceId}/{batchId}")
    @POST
    @Override
    public Response upload(@Context HttpServletRequest request, @PathParam("deviceId") String deviceId, @PathParam("batchId") String batchId,
                           Class<? extends BatchProcessor.BatchProcessWorker> processor) {
        return super.upload(request, deviceId, batchId, processor);
    }

    @Path("uploadData/{batchId}")
    @POST
    @Override
    public Response uploadData(@Context HttpServletRequest request, @PathParam("batchId") String batchId) {
        return super.uploadData(request, batchId);
    }

    @Path("uploadBatchData/{deviceId}/{batchId}")
    @POST
    @Override
    public Response uploadBatchData(@Context HttpServletRequest request, @PathParam("deviceId") String deviceId, @PathParam("batchId") String batchId) {
        return super.uploadBatchData(request, deviceId, batchId);
    }

    @Path("downloadOK")
    @POST
    @Consumes("application/json")
    @Override
    public Response retrieveDataOk(List<DeviceEntityMapReq> deviceEntityMapReqList) {
        return super.retrieveDataOk(deviceEntityMapReqList);
    }
}
