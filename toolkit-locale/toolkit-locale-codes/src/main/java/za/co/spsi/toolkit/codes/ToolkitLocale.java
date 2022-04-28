package za.co.spsi.toolkit.codes;


import za.co.spsi.locale.annotation.ResourceLocale;
import za.co.spsi.locale.annotation.ResourceSet;

/**
 * Created by jaspervdb on 15/06/18.
 */
@ResourceLocale(locales = {"en", "pt"}, context = {"toolkit"},
        resources = {
                @ResourceSet(resources = {"toolkit.messages.properties"}),
                @ResourceSet(resources = {"toolkit.messages_pt.properties"})
        }
)
public class ToolkitLocale {
}
