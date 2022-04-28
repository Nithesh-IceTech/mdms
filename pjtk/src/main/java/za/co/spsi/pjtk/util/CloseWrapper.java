package za.co.spsi.pjtk.util;

import lombok.SneakyThrows;
import za.co.spsi.pjtk.io.IOUtil;
import za.co.spsi.pjtk.reflect.RefMethods;
import za.co.spsi.pjtk.reflect.Reflect;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * wrapper expects a close of delete method
 *
 *
 */
public class CloseWrapper<T> implements Closeable {

    private T object;
    private Method close;

    public CloseWrapper(T object) {
        this.object = object;
        RefMethods closeMethod = Reflect.getMethods(object.getClass()).filter("close").filterParams();
        Assert.isTrue(object instanceof File || closeMethod.size() > 0,"Not a closeable" + object.getClass());
        close = closeMethod.size() > 0 ? closeMethod.get(0) : null;
    }

    public T get() {
        return object;
    }

    private void closeFile(File file) {
        if (file.isDirectory()) {
            IOUtil.deleteRecursively(file);
            file.delete();
        } else {
            file.delete();
        }
    }

    @SneakyThrows
    public void close() throws IOException {
        if (object instanceof File) {
            closeFile((File)object);
        } else {
            close.invoke(object);
        }
    }

}
