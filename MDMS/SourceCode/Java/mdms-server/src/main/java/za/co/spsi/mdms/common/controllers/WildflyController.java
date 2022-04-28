package za.co.spsi.mdms.common.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.java.Log;
import za.co.spsi.mdms.common.db.MdmsSettingsEntity;
import za.co.spsi.mdms.common.properties.config.PropertiesConfig;
import za.co.spsi.toolkit.ee.properties.ConfValue;
import za.co.spsi.toolkit.ee.security.Secured;

import javax.inject.Inject;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.lang.management.ManagementFactory;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Arno Combrinck
 */
@Log
@Path("wildfly")
public class WildflyController {

    @Inject
    private PropertiesConfig propertiesConfig;

    @POST
    @Secured("DeployAdmin")
    @Consumes("application/json")
    @Produces("application/json")
    @Path(value = "deployment")
    public Response setWildflyDeploymentMode(@QueryParam("mode") Boolean mode) {
        MdmsSettingsEntity responseEntity;
        try {
            responseEntity = new MdmsSettingsEntity();
            responseEntity.appInstance.set( propertiesConfig.getMdms_app_instance() );
            responseEntity.propertyKey.set("mdms.wildfly.deployment.mode");
            responseEntity.propertyValue.set(String.format("%s", mode?"true":"false" ));
            responseEntity.lastChangeTime.set(Timestamp.valueOf(LocalDateTime.now()));
            propertiesConfig.setMdms_wildfly_deployment_mode(mode);
            return Response.status(Response.Status.OK).entity(settingsUpdateResponse(responseEntity)).build();
        } catch(Exception exception) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(exception.getMessage()).build();
        } finally {
            checkDeploymentMode();
        }
    }

    @Path(value = "deployment")
    @GET
    @Produces("application/json")
    public Response getWildflyDeploymentMode() {
        MdmsSettingsEntity responseEntity;
        try {
            Boolean mode = propertiesConfig.getMdms_wildfly_deployment_mode();
            responseEntity = new MdmsSettingsEntity();
            responseEntity.appInstance.set( propertiesConfig.getMdms_app_instance() );
            responseEntity.propertyKey.set("mdms.wildfly.deployment.mode");
            responseEntity.propertyValue.set(String.format("%s", mode?"true":"false" ));
            responseEntity.lastChangeTime.set(Timestamp.valueOf(LocalDateTime.now()));
            return Response.status(Response.Status.OK).entity(settingsUpdateResponse(responseEntity)).build();
        } catch(Exception exception) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(exception.getMessage()).build();
        }
    }

    private ObjectNode settingsUpdateResponse(MdmsSettingsEntity mdmsSettingsEntity) {
        mdmsSettingsEntity = mdmsSettingsEntity == null ? new MdmsSettingsEntity() : mdmsSettingsEntity;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode responseNode = objectMapper.createObjectNode();
        responseNode.put("appInstance", mdmsSettingsEntity.appInstance.get() );
        responseNode.put("propertyKey", mdmsSettingsEntity.propertyKey.get() );
        responseNode.put("propertyValue", mdmsSettingsEntity.propertyValue.get() );
        responseNode.put("lastChangeTime", dateTimeFormatter.format( mdmsSettingsEntity.lastChangeTime.get().toLocalDateTime() ) );
        return responseNode;
    }

    private void checkDeploymentMode() {
        if(propertiesConfig.getMdms_wildfly_deployment_mode()) {
            try {
                log.warning("Wildfly deployment mode activated, shutting down services...");
                MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
                ObjectName objectName = new ObjectName("jboss.as:management-root=server");
                mBeanServer.invoke( objectName, "shutdown", null, null);
            } catch(Exception exception) {
                exception.printStackTrace();
            }
        }
    }

}
