package za.co.spsi.mdms.kamstrup.services.meter.domain;

import za.co.spsi.toolkit.util.StringList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by jaspervdb on 2016/10/13.
 */
@XmlRootElement(name = "Registers")
public class Registers {

    @XmlElement(name = "Register")
    public Register[] registers;

    @Override
    public String toString() {
        return String.format("%s",registers != null?
                Arrays.stream(registers).filter(r -> r != null).map(r -> r.toString()).collect(Collectors.toCollection(StringList::new)):
                "NULL");
    }

}