package za.co.spsi.toolkit.util;

public class ByteUtil {

    public static byte[] copy(byte data[], int len) {
        byte copy[] = new byte[len];
        System.arraycopy(data,0,copy,0,data.length > len?len:data.length);
        return copy;
    }
}
