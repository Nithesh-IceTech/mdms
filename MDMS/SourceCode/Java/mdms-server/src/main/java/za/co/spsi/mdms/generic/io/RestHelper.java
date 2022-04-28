package za.co.spsi.mdms.generic.io;

import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.enterprise.context.Dependent;
import java.io.IOException;
import java.nio.charset.Charset;

@Dependent
public class RestHelper<T> {

    public static final String TAG = RestHelper.class.getName();

    private static String userToken = null;

    public ResponseEntity<T> doGet(Class<T> type, String url, String token) throws IOException, HttpClientErrorException {
        return doGetOrPost(type, HttpMethod.GET, url, null, token);
    }

    public ResponseEntity<T> doPost(Class<T> type, String url, Object postObject, String token) throws HttpClientErrorException {
        return doGetOrPost(type, HttpMethod.POST, url, postObject, token);
    }

    public ResponseEntity<T> doGetOrPost(Class<T> type, HttpMethod httpMethod, String url, Object postObject, String token)
            throws HttpClientErrorException {

        HttpHeaders requestHeaders = null;
        HttpEntity<Object> entity = null;
        RestTemplate restTemplate = null;

        // Add the gzip Accept-Encoding header
        requestHeaders = createHeaders(token);
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);

        // set the timeouts
        HttpComponentsClientHttpRequestFactory rf = new HttpComponentsClientHttpRequestFactory();
        rf.setReadTimeout(90 * 1000);
        rf.setConnectTimeout(30 * 1000);

        // Create a new RestTemplate instance
        restTemplate = new RestTemplate(rf);

        if (String.class.isAssignableFrom(type)) {
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        } else if (Resource.class.isAssignableFrom(type)) {
            restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
        } else {
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        }

        entity = new HttpEntity<>(postObject, requestHeaders);
        return restTemplate.exchange(url, httpMethod, entity, type);

    }

    public static void setUserToken(String userTokenSession) {
        userToken = userTokenSession;
    }

    private HttpHeaders createHeaders(final String token) {
        if (token != null) {
            return new HttpHeaders() {{
                set("Authorization", "Bearer " + token);
                set("Connection", "Close");
            }};
        } else {
            return new HttpHeaders() {{
                set("Connection", "Close");
            }};
        }
    }
}
