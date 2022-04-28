package za.co.spsi.mdms.kamstrup.services.order.domain.commands;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by jaspervdb on 2016/10/17.
 */
@XmlRootElement(name = "Fault")
public class Fault {

    @XmlElement(name = "Description")
    public String description;


    /*
    <Fault xmlns:i="http://www.w3.org/2001/XMLSchema-instance"><Description>None of the specified meters were found</Description><Message>Bad request</Message></Fault>
     */
}
