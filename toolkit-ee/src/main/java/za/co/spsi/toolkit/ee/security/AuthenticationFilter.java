package za.co.spsi.toolkit.ee.security;

import za.co.spsi.toolkit.ee.properties.ConfValue;
import za.co.spsi.toolkit.util.Assert;
import za.co.spsi.uaa.ee.util.UAHelper;
import za.co.spsi.uaa.util.dto.AgencyRoleMap;
import za.co.spsi.uaa.util.error.AuthorisationException;

import javax.annotation.PostConstruct;
import javax.annotation.Priority;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static za.co.spsi.toolkit.ee.util.BeanUtil.getBean;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    public static final String AUTHENTICATION_SCHEME = "Bearer";

    public static final Logger TAG = Logger.getLogger(AuthenticationFilter.class.getName());

    private DefaultSecurityRoleProvider provider;

    @Inject
    @ConfValue(value = "security.disabled")
    private boolean securityDisabled;

    @Context
    private ResourceInfo resourceInfo;

    private UAHelper uaHelper = new UAHelper();

    @Inject
    private BeanManager beanManager;

    @PostConstruct
    private void init() {
        provider = getBean(beanManager,DefaultSecurityRoleProvider.class);
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (!securityDisabled) {
            String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
            // Validate the Authorization header
            if (!isTokenBasedAuthentication(authorizationHeader)) {
                abortWithUnauthorized(requestContext);
            } else {
                // Extract the token from the Authorization header
                String token = authorizationHeader.substring(AUTHENTICATION_SCHEME.length()).trim();
                try {
                    validateToken(token);
                } catch (Exception e) {
                    abortWithUnauthorized(requestContext);
                }
            }
        }
    }

    public static boolean isTokenBasedAuthentication(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.toLowerCase()
                .startsWith(AUTHENTICATION_SCHEME.toLowerCase() + " ");
    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext) {
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                        .header(HttpHeaders.WWW_AUTHENTICATE, AUTHENTICATION_SCHEME)
                        .build());
    }

    private String[] getRoles(Secured secured) {
        return secured.value().length == 0?
                provider != null?provider.getDefaultRoles():new String[]{}:
                secured.value();
    }


    /**
     * check that the token has the roles needed
     * @param token
     * @throws Exception
     */
    private void validateToken(String token) throws Exception {
        AgencyRoleMap map = uaHelper.getAgencyRoleMapFromToken(token);
        Secured secured = resourceInfo.getResourceMethod().getAnnotation(Secured.class);
        if (!secured.anonymous()) {
            if (!secured.any()) {
                Arrays.stream(getRoles(secured)).forEach(role ->
                        Assert.isTrue(AuthorisationException.class,
                                map.keySet().stream().filter(agency -> map.get(agency).stream().
                                        map(String::toUpperCase).collect(Collectors.toList()).
                                        contains(role.toUpperCase())).findAny().isPresent(),
                                String.format("Role %s not present", role)));
            } else if (getRoles(secured).length > 0) {
                Assert.isTrue(
                        Arrays.stream(getRoles(secured)).filter(role ->
                                map.keySet().stream().filter(
                                        agency -> map.get(agency).contains(role)).findAny().isPresent()).findAny().isPresent(),
                        String.format("None of the expected Roles [%s] are present",
                                Arrays.stream(getRoles(secured)).reduce((s1, s2) -> s1 + "," + s2).get()));
            }
        }
    }
}