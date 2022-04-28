package academy.learningprogramming;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType( propOrder = { "name", "capital", "foundation", "continent" , "population"} )
@XmlRootElement( name = "Entry" )
public class Entry {

    public String population;
    public String name;
    public String capital;
    public String importance;

    @XmlElement(name = "Country_Population")
    public void setPopulation( String population )
    {
        this.population = population;
    }

    @XmlElement( name = "Country_Name" )
    public void setName( String name )
    {
        this.name = name;
    }

    @XmlElement( name = "Country_Capital" )
    public void setCapital( String capital )
    {
        this.capital = capital;
    }
    @XmlAttribute( name = "importance", required = true )
    public void setImportance( String importance )
    {
        this.importance = importance;
    }

}
