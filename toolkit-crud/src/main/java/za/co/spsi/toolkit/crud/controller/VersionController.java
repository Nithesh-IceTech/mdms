package za.co.spsi.toolkit.crud.controller;

import za.co.spsi.toolkit.crud.util.VaadinVersionUtil;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;

@Path("versionController")
public class VersionController {

    @Path("getVersion")
    @GET
    public String getVersion(@Context HttpServletRequest request) {
        String version = VaadinVersionUtil.getVersion(request.getServletContext(), "/META-INF/");
        return "Version : " + (version == null ? "UNKNOWN" : version);
    }
}
