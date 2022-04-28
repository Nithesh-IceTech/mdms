package za.co.spsi.toolkit.crud.controller;

import org.apache.commons.io.FileUtils;
import za.co.spsi.toolkit.crud.entity.ImportAndroidLookupsEntity;
import za.co.spsi.toolkit.crud.util.MD5Util;
import za.co.spsi.toolkit.db.DataSourceDB;

import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ettienne on 2017/03/29.
 */
public interface LookupController {

    static final Logger LOG = Logger.getLogger(LookupController.class.getName());

    public abstract DataSource getDataSource();

    @GET
    @Path("getAndroidDB")
    @Produces("application/zip")
    default Response downloadFile(@QueryParam("apkVersion") String apkVersion,
                                         @QueryParam("lookupVersion") Integer lookupVersion) {
        try {

            List<List> set =
                    DataSourceDB.executeQuery(getDataSource(),
                            "select max(IMPORT_ANDROID_LOOKUPS.LOOKUP_VERSION) from IMPORT_ANDROID_LOOKUPS " +
                                    "where IMPORT_ANDROID_LOOKUPS.APK_VERSION = ?", apkVersion);
            Integer maxAvailableVersion = set.get(0).get(0) != null ? Integer.parseInt(set.get(0).get(0).toString()) : null;


            if (maxAvailableVersion == null ||
                    (maxAvailableVersion != null && lookupVersion != null && maxAvailableVersion <= lookupVersion)) {
                return Response.status(Response.Status.NO_CONTENT).type(MediaType.APPLICATION_JSON_TYPE).entity(
                        "No lookup version available for this APK version " +
                                "or latest lookup version already installed on tablet, " +
                                "or latest lookup version not uploaded yet on Backoffice").build();
            }

            ImportAndroidLookupsEntity entity = new ImportAndroidLookupsEntity();
            entity.lookupVersion.set(maxAvailableVersion);
            entity.apkVersion.set(apkVersion);
            entity = DataSourceDB.getFromSet(getDataSource(), entity);

            File file = File.createTempFile(entity.filename.get().split("\\.")[0], entity.filename.get().split("\\.")[1]);
            FileUtils.writeByteArrayToFile(file, entity.fileData.get());

            Response.ResponseBuilder response = Response.ok(file);
            response.tag(entity.lookupVersion.get() + " " + MD5Util.calculateMD5(file));
            String headerKey = "Content-Disposition";
            String headerValue = String.format("attachment; filename=\"%s\"", entity.filename.get());
            response.header(headerKey, headerValue);
            return response.build();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
