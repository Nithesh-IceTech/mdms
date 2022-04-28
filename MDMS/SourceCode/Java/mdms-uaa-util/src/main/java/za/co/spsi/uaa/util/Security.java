package za.co.spsi.uaa.util;


import za.co.spsi.mdms.common.properties.config.PropertiesConfig;

import javax.inject.Inject;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

/**
 * Created by jaspervdb on 2016/07/28.
 */

public class Security {

    @Inject
    private PropertiesConfig propertiesConfig;

    public PublicKey getPublicKey() {

        try {
            return Constants.PUBLIC_KEY;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
