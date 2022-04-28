package za.co.spsi.mdms.actuator.controller;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jboss.as.controller.client.helpers.Operations;
import org.jboss.dmr.ModelNode;
import za.co.spsi.mdms.actuator.inspectors.JmxInspectorImpl;
import za.co.spsi.toolkit.crud.util.VaadinVersionUtil;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.SQLException;

@Path("/actuator/info")
public class InfoResource {

    @Resource(mappedName = "java:/jdbc/mdms")
    private javax.sql.DataSource mdmsDs;

    @Resource(mappedName = "java:/jdbc/nesdb")
    private javax.sql.DataSource nesDs;

    @Resource(mappedName = "java:/jdbc/elsterDB")
    private javax.sql.DataSource elsterDs;

    @Resource(mappedName = "java:/jdbc/IceUtil")
    private javax.sql.DataSource iceutilDs;

    @Inject
    private JmxInspectorImpl jmxInspector;

    private String wfPort;

    @PostConstruct
    private void init() {
        this.wfPort = jmxInspector.getWildflyManagementPort();
    }

    @GET
    @Produces("application/json")
    public Response getInfo() {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("Server", getSystemStatus());
        jsonObjectBuilder.add("Application", getApplicationVersion());
        jsonObjectBuilder.add("Java", getJavaJdkVersion());
        jsonObjectBuilder.add("MDMS Database", getMdmsDbVersion());
        jsonObjectBuilder.add("NES Database", getNesDbVersion());
        jsonObjectBuilder.add("Elster Database", getElsterDbVersion());
        jsonObjectBuilder.add("ICE Utilities Database", getIceUtilDbVersion());
        return Response.ok(jsonObjectBuilder.build()).build();
    }

    private JsonObjectBuilder getApplicationVersion() {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("Build Version", VaadinVersionUtil.getVersion());
        return jsonObjectBuilder;
    }

    private JsonObjectBuilder getSystemStatus() {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        try {
            final ModelControllerClient client = ModelControllerClient.Factory.create(InetAddress.getByName("localhost"), Integer.parseInt(wfPort));
            final ModelNode address = new ModelNode().setEmptyList();
            final ModelNode op = Operations.createReadResourceOperation(address);
            final ModelNode outcome = client.execute(op);
            if (Operations.isSuccessfulOutcome(outcome)) {
                jsonObjectBuilder.add("Hostname", Operations.readResult(outcome).get("name").asString());
                jsonObjectBuilder.add("Product Name", Operations.readResult(outcome).get("product-name").asString());
                jsonObjectBuilder.add("Product Version", Operations.readResult(outcome).get("product-version").asString());
                jsonObjectBuilder.add("Release Version", Operations.readResult(outcome).get("release-version").asString());
            } else {
                jsonObjectBuilder.add("Failure Description", Operations.getFailureDescription(outcome).asString());
            }
        } catch(Exception ex) {
            jsonObjectBuilder.add("Error", ex.getMessage());
        }
        return jsonObjectBuilder;
    }

    private JsonObjectBuilder getJavaJdkVersion() {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        String jdkVersion = System.getProperty("java.version");
        jsonObjectBuilder.add("JDK Version", jdkVersion);
        return  jsonObjectBuilder;
    }

    private JsonObjectBuilder getMdmsDbVersion() {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        try(Connection connection = mdmsDs.getConnection()) {
            jsonObjectBuilder.add("Product Name", connection.getMetaData().getDatabaseProductName());
            jsonObjectBuilder.add("Product Version", connection.getMetaData().getDatabaseProductVersion());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            jsonObjectBuilder.add("Error", throwables.getMessage());
        }
        return jsonObjectBuilder;
    }

    private JsonObjectBuilder getNesDbVersion() {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        try(Connection connection = nesDs.getConnection()) {
            jsonObjectBuilder.add("Product Name", connection.getMetaData().getDatabaseProductName());
            jsonObjectBuilder.add("Product Version", connection.getMetaData().getDatabaseProductVersion());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            jsonObjectBuilder.add("Error", throwables.getMessage());
        }
        return jsonObjectBuilder;
    }

    private JsonObjectBuilder getElsterDbVersion() {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        try(Connection connection = elsterDs.getConnection()) {
            jsonObjectBuilder.add("Product Name", connection.getMetaData().getDatabaseProductName());
            jsonObjectBuilder.add("Product Version", connection.getMetaData().getDatabaseProductVersion());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            jsonObjectBuilder.add("Error", throwables.getMessage());
        }
        return jsonObjectBuilder;
    }

    private JsonObjectBuilder getIceUtilDbVersion() {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        try(Connection connection = iceutilDs.getConnection()) {
            jsonObjectBuilder.add("Product Name", connection.getMetaData().getDatabaseProductName());
            jsonObjectBuilder.add("Product Version", connection.getMetaData().getDatabaseProductVersion());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            jsonObjectBuilder.add("Error", throwables.getMessage());
        }
        return jsonObjectBuilder;
    }

}
