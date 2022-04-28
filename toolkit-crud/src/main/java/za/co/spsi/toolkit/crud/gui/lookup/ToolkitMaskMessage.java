package za.co.spsi.toolkit.crud.gui.lookup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import za.co.spsi.lookup.dao.LookupResult;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.crud.util.AgencyHelper;
import za.co.spsi.toolkit.util.StringList;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jaspervdb on 2015/09/14.
 */
@Singleton
public class ToolkitMaskMessage {

    @Inject
    public ToolkitLookupServiceHelper lookupServiceHelper;
    public static final Logger LOG = LoggerFactory.getLogger(ToolkitMaskMessage.class);
    private Map<String, Map<String, String>> messages = new HashMap<>();

    private StringList agencies = new StringList();

    public ToolkitMaskMessage() {
    }

    @PostConstruct
    public void postConstruct() {

        if (!agencies.containsIgnoreCase(ToolkitCrudConstants.getAgencyId())) {

            agencies.add(ToolkitCrudConstants.getAgencyId());

            for (String locale : ToolkitCrudConstants.getLocales()) {

                for (LookupResult lookupResult :
                        lookupServiceHelper.executeLookupRequest(za.co.spsi.lookup.Constants.LookupCodeDefinition.MESSAGE.name(), locale,
                                ToolkitCrudConstants.getAgencyId())) {

                    if (!messages.containsKey(lookupResult.getLookupCode())) {
                        messages.put(lookupResult.getLookupCode(), new HashMap<String, String>());
                    }

                    messages.get(lookupResult.getLookupCode()).put(locale, lookupResult.getDisplayValue());
                }
            }
        }
    }

    public String getMessage(String key) {
        Map msgMap = this.messages.get(key);
        Assert.notEmpty(msgMap, String.format("No message found for key %s", key));
        return (String) msgMap.get(ToolkitCrudConstants.getLocale());
    }


    public void handleLogin(@Observes AgencyHelper.AgencySetEvent agencySetEvent) {
        postConstruct();
    }

}
