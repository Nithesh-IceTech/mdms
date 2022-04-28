package za.co.spsi.toolkit.crud.util;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;
import lombok.SneakyThrows;

import java.io.IOException;

/**
 * Created by ettienne on 2017/06/08.
 */
public class FileUtil {

    public static SmbFile writeSmbFile(NtlmPasswordAuthentication authentication, String path, byte[] data) throws IOException {
        SmbFile smb = null;
        try {
            smb = new SmbFile(path, authentication);
            SmbFileOutputStream sops = new SmbFileOutputStream(smb);
            sops.write(data);
            sops.flush();
            return smb;
        } catch (Exception ex) {
            smb.delete();
            throw ex;
        }
    }

    public static void deleteSmbFile(NtlmPasswordAuthentication authentication, String path) throws Exception {
        SmbFile smb = null;
        try {
            smb = new SmbFile(path, authentication);
            smb.delete();
        } catch (Exception ex) {
            throw ex;
        }
    }

    public static boolean dirExist(NtlmPasswordAuthentication authentication, String path) throws Exception {
        SmbFile smb = null;
        try {
            smb = new SmbFile(path, authentication);
            return smb.exists();
        } catch (Exception ex) {
            throw ex;
        }
    }

    @SneakyThrows
    public static void main(String[] args) {
        NtlmPasswordAuthentication authentication = new NtlmPasswordAuthentication(
                null, "DVADMIN", "D0cuV1s10n");
        System.out.println(""+dirExist(authentication,"smb://EDMS-LIVE-PROD/MeterImages TEST/MeterImages-UPT01/XML"));
    }

}