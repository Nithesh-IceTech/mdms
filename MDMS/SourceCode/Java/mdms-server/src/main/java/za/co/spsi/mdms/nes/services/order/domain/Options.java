package za.co.spsi.mdms.nes.services.order.domain;

import javax.xml.bind.annotation.XmlElement;

/**
 * Created by jaspervdb on 2016/11/25.
 *
 */
public class Options {

    @XmlElement(name = "VALUE")
    private Integer value;

    @XmlElement(name = "ANSICOMPLIANCESTATUSTYPEID")
    private String ansiComplianceStatusTypeId;

    @XmlElement(name = "INCLUDESENDREADINGS")
    private Integer includesendreadings;

    @XmlElement(name = "METERCALCULATESDELTAVALUES")
    private Boolean meterCalculatesDeltaValues;

    @XmlElement(name = "PRIMARY")
    private Integer primary;

}
