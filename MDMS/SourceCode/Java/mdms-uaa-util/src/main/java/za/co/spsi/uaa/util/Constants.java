package za.co.spsi.uaa.util;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import za.co.spsi.mdms.common.properties.config.PropertiesConfig;
import za.co.spsi.pjtk.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by jaspervdb on 2016/07/28.
 */
@Slf4j
public class Constants {

    public static final String COMPRESS_TOKEN = "compress_token";
    public static final String COMPRESSED_PREFIX = "COMPRESSED_";

    public static PublicKey PUBLIC_KEY;

    @Inject
    private PropertiesConfig propertiesConfig;

    @PostConstruct
    public void setKey() {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PUBLIC_KEY = keyFactory.generatePublic(new X509EncodedKeySpec(propertiesConfig.getPublic_key().getBytes()));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
