package za.co.spsi.toolkit.util;

import za.co.spsi.toolkit.io.IOUtil;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jaspervdbijl on 2017/07/24.
 */
public class ByteClassLoader extends ClassLoader {

    private static Map<String,byte[]> CLASS_MAP = new HashMap<>();

    public Class findClass(String name) {
        byte[] b = loadClassData(name);
        return defineClass(name, b, 0, b.length);
    }

    public static void mapClass(String name,byte data[]) {
        CLASS_MAP.put(name,data);
    }

    private byte[] loadClassData(String name) {
        if (CLASS_MAP.containsKey(name)) {
            return CLASS_MAP.get(name);
        } else {
            throw new RuntimeException("Could not locate class by name " +name);
        }
    }

    public static void main(String args[]) throws Exception {
        ByteClassLoader.mapClass("za.co.spsi.toolkit.service.Tester",
                IOUtil.readFully(new FileInputStream(new
                File("/Users/jaspervdbijl/Documents/Work/Core/toolkit/target/classes/za/co/spsi/toolkit/service/Tester.class"))));
        Object main = new ByteClassLoader().loadClass("za.co.spsi.toolkit.util.Tester", true).newInstance();
        System.out.println("Tester " + main);
    }

}
