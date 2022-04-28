package academy.learningprogramming;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement( name = "Countries" )
public class ResultEntries {

    List entries;

    @XmlElement( name = "Entry" )
    public void setEntries( List entries )
    {
        this.entries = entries;
    }

}
