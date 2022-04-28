package za.co.spsi.mdms.util;

import za.co.spsi.toolkit.io.IOUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Base64;

/**
 * Created by jaspervdb on 2016/11/24.
 */
public class XmlHelper {

    public static <E> E unmarshall(Class<E> type,String data) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(type);

        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        return (E) jaxbUnmarshaller.unmarshal(new ByteArrayInputStream(data.getBytes()));
    }

    public static <E> String marshallToString(Class<E> type,E object) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(type);

        Marshaller m = jaxbContext.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        StringWriter sw = new StringWriter();
        m.marshal(object, sw);

        return sw.toString();
    }

    public static <E> String marshallToStringHandleException(Class<E> type,E object) {
        try {
            return marshallToString(type,object);
        } catch (JAXBException je) {
            throw new RuntimeException(je);
        }

    }


    public static <E> E unmarshallBase64Zip(Class<E> type,String data) throws JAXBException, IOException {
        return unmarshall(type, new String(IOUtil.unzip(Base64.getDecoder().decode(data))));
    }
}
