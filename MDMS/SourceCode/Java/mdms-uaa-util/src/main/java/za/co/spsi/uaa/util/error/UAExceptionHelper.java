package za.co.spsi.uaa.util.error;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.reflections.Reflections;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by jaspervdb on 2016/09/28.
 */
public class UAExceptionHelper {

    private static ObjectMapper objectMapper = new ObjectMapper();

    private static Class<? extends UAException> UA_CLASSES[] = new Class[]{
            AccountLockedException.class,
            AuthorisationException.class,
            BadRequestException.class,
            ChangePasswordRequiredException.class,
            InvalidCredentialsException.class,
            InvalidGrantException.class,
            InvalidPasswordException.class,
            InvalidTokenException.class,
            NoMailAddressException.class,
            NotFoundUsedException.class,
            PasswordAboutToExpireException.class,
            PasswordPreviouslyUsedException.class,
            PasswordResetRequestNotFoundException.class,
            SecurityException.class,
            ServiceUnavailableException.class,
            SystemException.class,
            UAException.class,
            UserAlreadyExistsException.class,
            UserNotFoundException.class};

    private static Map<String, Class<? extends UAException>> typeMap = new HashMap<>();

    static {
        for (Class<? extends UAException> type : UA_CLASSES) {
            try {
                typeMap.put(type.getConstructor().newInstance().getError(), type);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static <T> List<Class<? extends T>> getSubTypesOf(String path, Class<T> type) {
        List<Class<? extends T>> values = new ArrayList<>();
        values.addAll(new Reflections(path).getSubTypesOf(type));
        return values;
    }


    @SneakyThrows
    public static UAException getInstance(UAException ua) {
        if (typeMap.containsKey(ua.getError())) {
            UAException uae = typeMap.get(ua.getError()).getConstructor().newInstance();
            uae.setError(ua.getError());
            uae.setErrorCode(ua.getErrorCode());
            uae.setErrorDetail(ua.getErrorDetail());
            uae.setErrorDescription(ua.getErrorDescription());
            uae.setHttpStatus(ua.getHttpStatus());
            return uae;
        } else {
            return ua;
        }
    }

    @SneakyThrows
    @JsonIgnore
    public static UAException fromJson(String json) {
        try {
            return getInstance(objectMapper.readValue(json, ErrorMessage.class).getAsException());
        } catch (Exception ex) {
            return new SystemException("Invalid json response",ex);
        }
    }

    @SneakyThrows
    public static String toJson(UAException uae) {
        return objectMapper.writeValueAsString(new ErrorMessage(uae));
    }
}


