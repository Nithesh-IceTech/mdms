package za.co.spsi.uaa.ee.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import za.co.spsi.pjtk.util.CloseWrapper;
import za.co.spsi.uaa.util.BaseUAHelper;
import za.co.spsi.uaa.util.Constants;
import za.co.spsi.uaa.util.dto.*;
import za.co.spsi.uaa.util.error.PasswordAboutToExpireException;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaspervdb on 2016/05/30.
 */
@Slf4j
public class UAHelper extends BaseUAHelper {

    public static final String TOKEN = "Y2xpZW50OnBhc3N3b3Jk";

    public UAHelper() {
    }

    public static String formatUrl(String url) {
        return url.replace("//", "/").replace("http:/", "http://").replace("https:/", "https://");
    }

    private Client client = ClientBuilder.newClient();

    public Invocation.Builder getHttpHeaders(Invocation.Builder builder, String realm) {
        builder.header("realm", realm);
        return builder;
    }

    public Invocation.Builder getHttpHeaders(Invocation.Builder builder, String realm, String token, String locale) {
        Invocation.Builder httpHeaders = getHttpHeaders(builder, realm);
        httpHeaders.header("authorization", "Bearer " + token);
        httpHeaders.header("locale", locale);
        httpHeaders.header(Constants.COMPRESS_TOKEN, "true");
        return httpHeaders;
    }

    public Invocation.Builder getInvocationBuilder(String url, String realm, String token, String locale) {
        return getHttpHeaders(
                client.target(formatUrl(url)).request(
                        MediaType.APPLICATION_JSON), realm, token, locale);
    }

    void assertOk(Response response) {
        if (response.getStatus() != HttpServletResponse.SC_OK) {
            throw ExceptionHandlerFactory.buildException(response);
        }
    }

    /**
     * login and get the oauth token
     *
     * @param realm
     * @param url
     * @param username
     * @param password
     * @return
     */
    @SneakyThrows
    public TokenResponseDao login(String realm, String url, String username, String password) {
        try {
            Invocation.Builder builder = getInvocationBuilder(url + "/oauth/token", realm, TOKEN, "en");
            try (CloseWrapper<Response> response = new CloseWrapper(builder
                    .accept(MediaType.APPLICATION_JSON_TYPE)
                    .post(Entity.entity(String.format("grant_type=password&client_id=client&client_secret=password&username=%s&password=%s", username, password),
                            MediaType.APPLICATION_FORM_URLENCODED)))) { // MediaType.APPLICATION_FORM_URLENCODED

                assertOk(response.get());

                String responseStr = response.get().readEntity(String.class);

//                log.info(String.format("MDMS LOGIN RESPONSE STRING: %s", responseStr));

                ObjectMapper objectMapper = new ObjectMapper();
                TokenResponseDao tokenResponseDao = objectMapper.readValue(responseStr, TokenResponseDao.class);

                if (isPasswordDueToExpire(tokenResponseDao.getAttributes())) {
                    throw new PasswordAboutToExpireException(tokenResponseDao, getDaysBeforeExpiry(tokenResponseDao.getAttributes()));
                }

                return tokenResponseDao;
            }

        } catch (ClientErrorException ce) {
            throw ExceptionHandlerFactory.buildException(ce.getResponse());
        }
    }

    @SneakyThrows
    public void deleteUser(String realm, String url, String token, String uid) {

        Invocation.Builder builder = getInvocationBuilder(url + "/api/users/" + uid, realm, token, "en");

        try (CloseWrapper<Response> response = new CloseWrapper(
                builder.accept(MediaType.APPLICATION_JSON_TYPE).delete())) {
            assertOk(response.get());
            log.info("Deleted user " + uid);
        }
    }

    @SneakyThrows
    public User createUser(String realm, String url, String token, CreateOrUpdateUser createUser) {

        Invocation.Builder builder = getInvocationBuilder(url + "/api/users/", realm, token, createUser.getPreferredLanguage());

        try (CloseWrapper<Response> response = new CloseWrapper(
                builder.accept(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(
                        createUser, MediaType.APPLICATION_JSON_TYPE)))) {
            assertOk(response.get());
            log.info("Created user " + createUser.getUid());
            return response.get().readEntity(User.class);
        }
    }

    @SneakyThrows
    public void updateUser(String realm, String url, String token, CreateOrUpdateUser createUser) {

        Invocation.Builder builder = getInvocationBuilder(url + "/api/users/", realm, token, createUser.getPreferredLanguage());

        try (CloseWrapper<Response> response = new CloseWrapper(
                builder.accept(MediaType.APPLICATION_JSON_TYPE).put(Entity.entity(
                        createUser, MediaType.APPLICATION_JSON_TYPE)))) {
            assertOk(response.get());
            log.info("Created user " + createUser.getUid());
        }
    }

    public void changePassword(String realm, String url, String uid, String curPassword, String newPassword) {
        changePassword(realm, url, uid, curPassword, newPassword, "en");
    }

    public void changePassword(String realm, String url, String uid, String curPassword, String newPassword, String locale) {

        Response response = null;
        try {
            Client client = ClientBuilder.newClient();
            Invocation.Builder builder = getHttpHeaders(
                    client.target(formatUrl(String.format("%s/api/users/%s/password/update", url, uid))).request(
                            MediaType.APPLICATION_JSON), realm, "Y2xpZW50OnBhc3N3b3Jk", locale);

            response = builder.accept(MediaType.APPLICATION_JSON_TYPE).post(Entity.entity(
                    new ChangePasswordRequest(curPassword, newPassword), MediaType.APPLICATION_JSON_TYPE));

            if (response.getStatus() != HttpServletResponse.SC_OK) {
                throw ExceptionHandlerFactory.buildException(response);
            }

        } catch (ClientErrorException ce) {
            throw ExceptionHandlerFactory.buildException(ce.getResponse());
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    public String resetPassword(String realm, String url, String token, String uid) {
        return resetPassword(realm, url, token, uid, "en");
    }

    public String resetPassword(String realm, String url, String token, String uid, String locale) {
        Client client = ClientBuilder.newClient();
        Invocation.Builder builder = getHttpHeaders(
                client.target(formatUrl(String.format("%s/api/users/%s/password/reset", url, uid))).request(
                        MediaType.APPLICATION_JSON), realm, token, locale);
        return builder.post(Entity.json(null), String.class).replace("\"", "");
    }

    public Boolean isChangePasswordRequired(String realm, String url, String uid, String password) {
        Client client = ClientBuilder.newClient();
        Invocation.Builder builder = getHttpHeaders(
                client.target(
                        formatUrl(String.format("%s/api/users/%s/isPasswordChangeRequired", url, uid))).request(
                        MediaType.APPLICATION_JSON), realm);
        return builder.post(Entity.json(new UsernamePassword(uid, password)), Boolean.class);
    }

    public void resetPassword(String realm, String url, String token, User user) {
        resetPassword(realm, url, token, user, "en");
    }

    public void resetPassword(String realm, String url, String token, User user, String locale) {
        user.setUserPassword(resetPassword(realm, url, token, user.getUid(), locale));
    }

    public List<SimpleUser> getUserForAgency(String realm, String url, String token, ArrayList<String> agencies) {
        return getUserForAgency(realm, url, token, agencies, "en");
    }

    public List<SimpleUser> getUserForAgency(String realm, String url, String token, ArrayList<String> agencies, String locale) {
        Response response = null;
        try {
            Client client = ClientBuilder.newClient();
            Invocation.Builder builder = getHttpHeaders(
                    client.target(formatUrl(String.format("%s/api/users/getSimpleUsersForAgencies/%s", url
                            , String.join(",", agencies)))).
                            request(MediaType.APPLICATION_JSON), realm, token, locale);

            response = builder.accept(MediaType.APPLICATION_JSON_TYPE).get();

            if (response.getStatus() != HttpServletResponse.SC_OK) {
                throw ExceptionHandlerFactory.buildException(response);
            }
            return response.readEntity(new GenericType<List<SimpleUser>>() {
            });

        } finally {
            response.close();
        }

    }

    public List<User> getUserWithRoles(String realm, String url, String token, ArrayList<String> roles) {
        return getUserWithRoles(realm, url, token, roles, "en");
    }

    public List<User> getUserWithRoles(String realm, String url, String token, ArrayList<String> roles, String locale) {
        Response response = null;
        try {
            Client client = ClientBuilder.newClient();
            Invocation.Builder builder = getHttpHeaders(
                    client.target(
                            formatUrl(
                                    String.format("%s/api/users/getUsersWithRoles/%s", url
                                            , String.join(",", roles)))).
                            request(MediaType.APPLICATION_JSON), realm, token, locale);

            response = builder.accept(MediaType.APPLICATION_JSON_TYPE).get();

            if (response.getStatus() != HttpServletResponse.SC_OK) {
                throw ExceptionHandlerFactory.buildException(response);
            }
            return response.readEntity(new GenericType<List<User>>() {
            });

        } finally {
            if (response != null) {
                response.close();
            }
        }

    }

    public User getUserDetail(String realm, String url, String username, String token) {
        return getUserDetail(realm, url, username, token, "en");
    }

    public User getUserDetail(String realm, String url, String username, String token, String locale) {
        Client client = ClientBuilder.newClient();
        Invocation.Builder builder = getHttpHeaders(
                client.target(formatUrl(String.format("%s/api/users/%s", url, username))).request(
                        MediaType.APPLICATION_JSON), realm, token, locale);

        Response response = builder.accept(MediaType.APPLICATION_JSON_TYPE).get();
        try {
            if (response.getStatus() != HttpServletResponse.SC_OK) {
                throw ExceptionHandlerFactory.buildException(response);
            }
            return response.readEntity(new GenericType<List<User>>() {
            }).get(0);
        } finally {
            response.close();
        }
    }


}
