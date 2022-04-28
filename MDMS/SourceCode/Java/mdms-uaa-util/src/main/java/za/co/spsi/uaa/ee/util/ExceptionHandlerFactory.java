package za.co.spsi.uaa.ee.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import za.co.spsi.uaa.util.error.ErrorMessage;
import za.co.spsi.uaa.util.error.UAException;
import za.co.spsi.uaa.util.error.UAExceptionHelper;

import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jaspervdb on 2016/05/30.
 */
public class ExceptionHandlerFactory {

    public static Logger TAG = Logger.getLogger(ExceptionHandlerFactory.class.getName());

    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * build a UAException from the response received. Expected body to be in ErrorMessage json format
     *
     * @param response
     * @return
     */
    public static UAException buildException(Response response) {


        String responseStr = response.readEntity(String.class);
        if (responseStr.startsWith("\"")) {
            responseStr = responseStr.substring(1, responseStr.length() - 1).replace("\\", "");
        }

        try {
            return UAExceptionHelper.fromJson(responseStr.replace("\\\"", "\""));
        } catch (Exception ex) {
            TAG.log(Level.SEVERE, ex.getMessage(), ex);
            return new UAException().init(responseStr, "", "", 500);
        }
    }


    public static void main(String args[]) throws Exception {
        String value = "{\"error\":\"invalid_credentials\",\"error_description\":\"There is no client authentication. Try adding an appropriate authentication filter.\"}";
        objectMapper.readValue(value, ErrorMessage.class);
    }


}
