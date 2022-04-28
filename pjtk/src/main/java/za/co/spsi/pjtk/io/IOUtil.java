package za.co.spsi.pjtk.io;

import lombok.SneakyThrows;
import za.co.spsi.pjtk.util.Assert;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.zip.*;

import static za.co.spsi.pjtk.util.Util.handle;

/**
 * Created by jaspervdb on 2016/04/29.
 */
public class IOUtil {


    public static byte[] readFully(InputStream is) throws IOException {
        return readFully(is, null);
    }

    public static byte[] readAvailable(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte buffer[] = new byte[1024];
        while (is.available() > 0) {
            int r = is.read(buffer);
            bos.write(buffer, 0, r);
        }
        return bos.toByteArray();
    }

    public static byte[] readFully(File file) throws IOException {
        return readFully(new FileInputStream(file), null);
    }

    public static byte[] readFully(InputStream is, Integer length) throws IOException {
        try {
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            for (int r = is.read(buffer); r > -1 && (length == null || length == -1 || bos.size() < length); r = is.read(buffer)) {
                bos.write(buffer, 0, r);
            }
            if (length != null && length != -1 && bos.size() != length) {
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

    public static Files listFilesRecursively(File file) {
        return listFilesRecursively(new Files(), file);
    }

    private static Files listFilesRecursively(Files files, File file) {
        if (file.isDirectory()) {
            Arrays.stream(file.listFiles()).forEach(f -> listFilesRecursively(files, f));
        } else {
            files.add(file);
        }
        return files;
    }

    public static void copyAndClose(InputStream is, OutputStream os) throws IOException {
        copy(is, os);
        is.close();
        os.close();
    }

    @SneakyThrows
    public static void write(File file, String data) {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(data.getBytes());
        }
    }

    @SneakyThrows
    public static void copyAndCloseHandleException(InputStream is, OutputStream os) {
        copyAndClose(is,os);
    }

    public static void copy(InputStream is, OutputStream os, long length) throws IOException {
        for (int i = 0; i < length || length == -1; i++) {
            int r = is.read();
            if (r != -1) {
                os.write(r);
            } else if (length == -1) {
                break;
            } else {
                throw new IOException("Stream closed");
            }
        }
    }


    public static void copyAndCloseOs(InputStream is, OutputStream os, long length) throws IOException {
        copy(is, os, length);
        os.close();
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
        }
        file.delete();
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

    public static void zipFolder(File sourceDir, File zipFile) throws IOException {
        Path p = zipFile.toPath();
        try (ZipOutputStream zs = new ZipOutputStream(java.nio.file.Files.newOutputStream(p))) {
            Path pp = Paths.get(sourceDir.getAbsolutePath());
            java.nio.file.Files.walk(pp)
                    .filter(path -> !java.nio.file.Files.isDirectory(path))
                    .forEach(path ->
                            handle(() -> {
                                ZipEntry zipEntry = new ZipEntry(pp.relativize(path).toString());
                                zs.putNextEntry(zipEntry);
                                java.nio.file.Files.copy(path, zs);
                                zs.closeEntry();
                            }));
        }
    }

    public static File zipFolder(File sourceDir,String ext) throws IOException {
        File file = createTmpFile(sourceDir.getName(), ext);
        zipFolder(sourceDir, file);
        return file;
    }

    public static File zipFolder(File sourceDir) throws IOException {
        return zipFolder(sourceDir,".zip");
    }

    public static byte[] zip(String data) throws IOException {
        return zip(new ByteArrayInputStream(data.getBytes()));
    }

    public static byte[] unzip(InputStream zip) throws IOException {
        try (GZIPInputStream fis = new GZIPInputStream(zip)) {
            return readFully(fis);
        }
    }

    @SneakyThrows
    public static File createTmpFolder(String pre) {
        File tmpFolder = File.createTempFile(pre, "");
        tmpFolder.delete();
        tmpFolder.mkdir();
        tmpFolder.deleteOnExit();
        return tmpFolder;
    }

    @SneakyThrows
    public static File createTmpFile(String pre, String post) {
        File file = File.createTempFile(pre, post);
        file.deleteOnExit();
        return file;
    }

    public static File unzipFile(InputStream is, boolean skipHidden) throws IOException {
        File tmpFolder = createTmpFolder("zip");
        ZipInputStream zis = new ZipInputStream(is);
        for (ZipEntry entry = zis.getNextEntry(); entry != null; entry = zis.getNextEntry()) {
            if (!skipHidden || !entry.getName().startsWith(".")) {
                File uzFile = new File(tmpFolder.getAbsolutePath() + "/" + entry.getName());
                if (entry.isDirectory()) {
                    uzFile.mkdirs();
                } else {
                    uzFile.getParentFile().mkdirs();
                    copyAndCloseOs(zis, new FileOutputStream(uzFile), entry.getSize());
                }
            }
        }
        return tmpFolder;
    }

    public static File unzipFile(File file, boolean skipHidden) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            return unzipFile(fis, skipHidden);
        }
    }

    public static byte[] unzip(byte data[]) throws IOException {
        return unzip(new ByteArrayInputStream(data));
    }

    @SneakyThrows
    public static byte[] serializeObject(Object serializable) {
        Assert.isTrue(serializable != null, "Can not serialze a null object");
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            try (ObjectOutputStream os = new ObjectOutputStream(bos)) {
                os.writeObject(serializable);
                return bos.toByteArray();
            }
        }
    }

    @SneakyThrows
    public static <T> T deserializeObject(Class<T> type, byte data[]) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data)) {
            try (ObjectInputStream is = new ObjectInputStream(bis)) {
                return (T) is.readObject();
            }
        }
    }


}
