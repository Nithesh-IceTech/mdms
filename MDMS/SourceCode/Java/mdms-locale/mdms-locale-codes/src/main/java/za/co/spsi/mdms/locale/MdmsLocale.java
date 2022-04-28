package za.co.spsi.mdms.locale;


import lombok.Data;
import za.co.spsi.locale.annotation.ResourceLocale;
import za.co.spsi.locale.annotation.ResourceSet;

/**
 * Created by jaspervdb on 15/06/18.
 */
@ResourceLocale(locales = {"en", "pt", "fr"}, context = {"mdms"},
        resources = {
                @ResourceSet(resources = {"toolkit.messages.properties", "messages.properties"}),
                @ResourceSet(resources = {"toolkit.messages_pt.properties", "messages_pt.properties"}),
                @ResourceSet(resources = {"toolkit.messages_fr.properties", "messages_fr.properties"})
        }
)
@Data
public class MdmsLocale {
}
