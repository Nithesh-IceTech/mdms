package za.co.spsi.mdms.kamstrup.services.meter.domain;

import za.co.spsi.mdms.kamstrup.services.LongFormatDateAdapter;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Timestamp;

/**
 * Created by jaspervdb on 2016/10/13.
 * http://172.31.91.228/utilidriver/api/meters/4B414D00000003C954C8/
 */
@XmlRootElement(name = "Meter")
public class MeterDetail {

    @XmlAttribute(name = "ref")
    public String ref;
    @XmlElement(name = "SerialNumber")
    public String serialNumber;
    @XmlElement(name = "MeterNumber")
    public String meterNumber;
    @XmlElement(name = "State")
    public String state;
    @XmlElement(name = "VendorId")
    public String vendorId;
    @XmlElement(name = "Firmware")
    public String firmware;

    @XmlJavaTypeAdapter(LongFormatDateAdapter.class)
    @XmlElement(name = "ConfigurationUpdated")
    public Timestamp configurationUpdated;

    @XmlElement(name = "Gateway")
    public Gateway gateway;
    @XmlElement(name = "Profile")
    public Profile profileRef;
    @XmlElement(name = "TypeDescription")
    public String typeDescription;
    @XmlElement(name = "ConsumptionType")
    public String consumptionType;
    @XmlElement(name = "Routes")
    public Routes routesRef;

    public static class Routes {
        @XmlAttribute(name = "ref")
        public String ref;
    }

    public static class Gateway {
        @XmlElement(name = "VendorId")
        public String vendorId;

        @XmlElement(name = "TypeDescription")
        public String typeDescription;

    }

    public static class Profile {
        @XmlAttribute(name = "ref")
        public String ref;
    }


}