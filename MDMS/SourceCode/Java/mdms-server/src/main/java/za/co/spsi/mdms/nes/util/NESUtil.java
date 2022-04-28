package za.co.spsi.mdms.nes.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.InflaterOutputStream;

/**
 * Created by jaspervdbijl on 2017/01/03.
 */
public class NESUtil {
    private static byte[] HexToBytes(String hex) {
        if (hex.startsWith("Z8")) {
            hex = "7" + hex.substring(1);
        }
        return javax.xml.bind.DatatypeConverter.parseHexBinary(hex);
    }

    public static String Decompress(String resultCompressed) {
        try {
            if (resultCompressed.startsWith("Z8") || // Z8DA
                    resultCompressed.startsWith("78")) { // 78DA
                byte[] resultBytes = HexToBytes(resultCompressed);
                try (ByteArrayInputStream bais = new ByteArrayInputStream(resultBytes)) {
                    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                        try (InflaterOutputStream inflate = new InflaterOutputStream(baos)) {
                            int len;
                            byte[] buffer = new byte[1000];
                            while ((len = bais.read(buffer)) > 0) {
                                inflate.write(buffer, 0, len);
                            }
                            String resultDecompressed = new String(baos.toByteArray(), "UTF-8");
                            return resultDecompressed;
                        }
                    }
                }
            }
            return resultCompressed;
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    public static void main(String args[]) throws Exception {
        String data = "Z8DABD986B6E9C3010C7AFD20BA478FC766421B9E0B648AC5981899AAF55B3F73F42CD3E697615D7C96C2484ACC1FE6B1EE6C718DB0FAEDD8EC3F7AEF7B59DA28BF35483AD8E23DBBAE8261F6BDBB58B39DD6D75B6ADD6C6E7AD5F26BC36D96608D175616A7D1FDD93EB673FD5C456B7CCD6873669FBD86D7C4D09A807A21EA8FA02FA9190747D2524AD5C4FB25D887E4CCBDB7974B11B42CDD28C2BA30DF3E69B4F5E353F5D08BE4F11525B5D19EDB05DA6A7C1DEA11AA4AD0E23EBC2D435C366DB772E34FE909C25BE948FDD4E0B2A8451E24573F647FCD6E645EC88019046519372F2C6E21440D3CFAD9F5254A3776D177EECD373CB6C373EC5D5B8BE99FB9480D7197DE3A9DD8EDDC68DCF4B814E435B9DA33DE56B35DC17FEBA10F2915D0A71A9C2F5B639E674BF6DC861DBF85F3145E3DBE394A592FF5A4E5937C099E497DC5767B1B52A2D53E59A6A033A232ADFE3AA50195528F455B19C9FC00A15A5C929F24245C8462DCA14296344B39CA82C15955C646357A5A2E985CF794A59A928CF291696884256B1B44482DC50AC2ED0C8E043AD397E477C70C2193A3E0C01898F0F4E0447C6875EBE39A8F8D05A23E34313818E0F30021F1F8253868E0F4E15C7C647F6DB568E0F838D0F453F888FCFE93E3897F8DD87012AEE800FAEB0BB0F608A20F30338086480407A89D00942C0707482302EF109429996D804E1804D1041B00962D88708A23FAB0131E20E04A1D993C67B0862243A41D261189B2092316C82886C3FF70E820802F804F98F129513440A403FC2106C8230F41E44670852ADFE86ACFF66D57F0178C92792";
        System.out.println(Decompress(data));
    }

}
