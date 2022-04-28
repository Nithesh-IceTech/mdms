package za.co.spsi.toolkit.io;

import org.apache.commons.codec.binary.Base64;
import za.co.spsi.toolkit.util.Assert;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by jaspervdb on 2016/04/29.
 */
public class IOUtil {


    public static byte[] readFully(InputStream is) throws IOException {
        return readFully(is, null);
    }

    public static byte[] readFully(InputStream is, Integer length) throws IOException {
        try {
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            for (int r = is.read(buffer); r > -1 && (length == null || length == -1 || bos.size() < length); r = is.read(buffer)) {
                bos.write(buffer, 0, r);
            }
            if(length != null && length != -1 && bos.size() != length) {
                throw new EOFException("Length to read: " + length + " actual: " + bos.size());
            }
            return bos.toByteArray();
        } finally {
            is.close();
        }
    }

    public static byte[] readFullyHandleException(File file) {
        try {
            return readFully(new FileInputStream(file));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void copy(InputStream is, OutputStream os) throws IOException {
        byte buffer[] = new byte[1024];
        while (is.available() > 0) {
            int r = is.read(buffer);
            os.write(buffer, 0, r);
        }
    }

    public static void copy(InputStream is, OutputStream os,int length) throws IOException {
        for (int i = 0;i < length || length == -1;i++) {
            int r = is.read();
            if (r != -1) {
                os.write(r);
            } else if (length== -1) {
                break;
            } else {
                throw new IOException("Stream closed");
            }
        }
    }

    public static File downloadFile(String url) throws IOException {
        URL u = new URL(url);
        HttpURLConnection httpConn = (HttpURLConnection) u.openConnection();
        int responseCode = httpConn.getResponseCode();
        Assert.isTrue(responseCode == 200, "Failed to read file. Code " + responseCode);
        File ioFile = File.createTempFile("toolkit", ".dat");
        ioFile.deleteOnExit();
        try (FileOutputStream fos = new FileOutputStream(ioFile)) {
            IOUtil.copy(httpConn.getInputStream(), fos, httpConn.getContentLength());
        }
        return ioFile;
    }

    public static void deleteRecursively(File file) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                deleteRecursively(f);
            }
        } else {
            file.delete();
        }
    }

    public static byte[] zip(InputStream is) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            try (GZIPOutputStream gos = new GZIPOutputStream(bos)) {
                gos.write(readFully(is));
                gos.close();
                return bos.toByteArray();
            }
        }
    }

    public static byte[] zip(String data) throws IOException {
        return zip(new ByteArrayInputStream(data.getBytes()));
    }

    public static byte[] unzip(InputStream zip) throws IOException {
        try (GZIPInputStream fis = new GZIPInputStream(zip)) {
            return readFully(fis);
        }
    }

    public static File unzipFile(File file, boolean skipHidden) throws IOException {
        File tmpFolder = File.createTempFile("shape_import", "");
        tmpFolder.delete();
        tmpFolder.mkdir();
        tmpFolder.deleteOnExit();

        List<File> files = new ArrayList<>();
        ZipFile zipFile = new ZipFile(file);
        for (Enumeration<? extends ZipEntry> entries = zipFile.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = entries.nextElement();
            if (!skipHidden || !(entry.getName().startsWith(".") || entry.getName().indexOf("/") != -1)) {
                files.add(new File(tmpFolder.getAbsolutePath() + "/" + entry.getName()));
                files.get(files.size() - 1).deleteOnExit();
                files.get(files.size() - 1).getParentFile().mkdirs();
                if (entry.isDirectory()) {
                    files.get(files.size() - 1).mkdir();
                } else {
                    try (FileOutputStream fos = new FileOutputStream(files.get(files.size() - 1))) {
                        try (InputStream zis = zipFile.getInputStream(entry)) {
                            copy(zis, fos);
                        }
                    }
                }
            }
        }
        return tmpFolder;
    }


    public static byte[] unzip(byte data[]) throws IOException {
        return unzip(new ByteArrayInputStream(data));
    }

    public static void main(String args[]) throws Exception {

        System.out.println(new String(Base64.encodeBase64(zip("Hello World"))));

    }

}
