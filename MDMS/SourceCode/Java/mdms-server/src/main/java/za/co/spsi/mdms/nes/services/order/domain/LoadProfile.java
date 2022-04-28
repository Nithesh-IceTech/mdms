package za.co.spsi.mdms.nes.services.order.domain;

import za.co.spsi.mdms.nes.services.LongFormatDateAdapter;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.sql.Timestamp;

/**
 * Created by jaspervdb on 2016/11/25.
 */
@XmlRootElement(name = "LOADPROFILE")
public class LoadProfile {

    @XmlElement(name = "STATUS")
    public Integer status;

    @XmlElement(name = "DATASET")
    public DataSet dataSet;

    @XmlElement(name = "LOADPROFILETYPE")
    public Integer loadProfileType;

    @XmlElement(name = "CONTAINSDELTAVALUES")
    public Integer containsDeltaValues;

    @XmlJavaTypeAdapter(LongFormatDateAdapter.class)
    @XmlElement(name = "ENDDATETIME")
    public Timestamp endTime;

    @XmlElement(name = "INTERVALDURATION")
    public Integer intervalDuration;

    @XmlElement(name = "NUMBEROFCHANNELS")
    public Integer numberOfChannels;

    @XmlElement(name = "OPTIONS")
    public Options options;

    @XmlElement(name = "INTERVALS")
    public Intervals intervals;

    


}
