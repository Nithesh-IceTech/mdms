package za.co.spsi.toolkit.ee.properties;

import lombok.SneakyThrows;
import za.co.spsi.toolkit.util.Assert;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import java.io.InputStream;

/**
 * Created by jaspervdb on 2016/05/03.
 */
@Dependent
public class FileReader {

    @Produces
    @TextFile
    public String readFile(InjectionPoint ip) {
        //get filename from annotation
        String filePath = ip.getAnnotated().getAnnotation(TextFile.class).value();
        return readResource(filePath);
    }

    @SneakyThrows
    public static String readResource(String filePath) {
        InputStream fis = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        Assert.notNull(fis != null,"Unable to find resource " + filePath);
        java.util.Scanner s = new java.util.Scanner(Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath)).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}
