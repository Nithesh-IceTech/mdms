package za.co.spsi.lookup.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import za.co.spsi.lookup.Constants;
import za.co.spsi.lookup.dao.*;
import za.co.spsi.toolkit.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by jaspervdb on 1/20/16.
 */
@Service
public class LookupServiceHelper {

    public static final Logger LOG = Logger.getLogger(LookupServiceHelper.class.getName());

    @Value("${ice_lookup_url}")
    private String iceLookupUrl;

    public String getIceLookupUrl() {
        return iceLookupUrl;
    }

    public void setIceLookupUrl(String iceLookupUrl) {
        this.iceLookupUrl = iceLookupUrl;
    }

    private String toString(String url,Object ...params) {
        List values = new ArrayList();
        if (params != null) {
            Collections.addAll(values,params);
        }
        while (!values.isEmpty()) {
            Assert.isTrue(url.indexOf("{") != -1,"Invalid URL and parameter count " +url + " : " + Arrays.deepToString(params));
            String tmp = url.substring(0,url.indexOf("{"));
            url = tmp + values.remove(0) + url.substring(url.indexOf("}")+1);
        }
        return url;
    }

    public <T> T executeGET(String url, Class<T> typeClass, Object... params) {
        long time = System.currentTimeMillis();
        try {

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<T> responseEntity = restTemplate.getForEntity(url, typeClass, params);
            time = System.currentTimeMillis() - time;
            LOG.info("Response took " + time + " " + toString(url,params));
            return responseEntity.getBody();
        } catch (HttpClientErrorException he) {
            LOG.log(Level.WARNING, "Lookup GET failed with status code {0}. Query {1}. Params {2}", new Object[]{
                    he.getStatusCode(), toString(url,params)});
            return null;
        }
    }


    public LookupResultList executeLookupRequest(String lookupDefinitionId, String language, String agencyId) {
        String url = String.format("%s/rest/lookupCodes/findAllActiveByLookupDefinitionId?lookupDefinitionId={lookupDefinitionId}&language={language}&agencyId={agencyId}",
                iceLookupUrl);
        return new LookupResultList(executeGET(url, LookupResult[].class, lookupDefinitionId, language, agencyId));
    }

    public LookupResultList getAllAgencies(String language, String agencyId) {
        return executeLookupRequest("AGENCY", language, agencyId);
    }

    public LookupResultList executeLookupMappingRequest(String hierarchyDefId, String language, String agencyId, Object parentId) {
        String url = String.format("%s/rest/hierarchyMappings/findChildrenByParent?language={language}&hierarchyDefinitionId={hierarchyDefId}&parentLookupCode={parentId}&agencyId={agencyId}",
                iceLookupUrl);
        return new LookupResultList(executeGET(url, LookupResult[].class, language, hierarchyDefId, parentId, agencyId));
    }

    public boolean executeLookupMappingRequestCodeExist(String hierarchyDefId, Object parentId, String language, String agencyId, Object code) {
        String url = String.format("%s/rest/hierarchyMappings/isLookupCodeInHierarchyMapping?language={language}&hierarchyDefinitionId={hierarchyDefId}&parentLookupCode={parentId}&agencyId={agencyId}&childLookupCode={childLookupCode}",
                iceLookupUrl);
        return Boolean.parseBoolean(executeGET(url, ResultString.class, language, hierarchyDefId, parentId, agencyId, code).getResult());
    }


    public HierarchyDefinitionResultList findAllByHierarchyDefinitionId(String hierarchyDefId, String language, String agencyId) {
        String url = String.format("%s/rest/hierarchyMappings/findAllByHierarchyDefinitionId?language={language}&hierarchyDefinitionId={hierarchyDefId}&agencyId={agencyId}",
                iceLookupUrl);
        return new HierarchyDefinitionResultList(executeGET(url, HierarchyDefinitionResult[].class, language, hierarchyDefId, agencyId));
    }

    public LookupCodeResult executeLookupCodeRequest(String lookupDefinitionId, String lookupCode, String language, Object agencyId) {
        String url = String.format("%s/rest/lookupCodes/findByLookupCode?language={language}&lookupCode={lookupCode}&lookupDefinitionId={lookupDefinitionId}&agencyId={agency_id}",
                iceLookupUrl);
        return executeGET(url, LookupCodeResult.class, language, lookupCode, lookupDefinitionId, agencyId);
    }

    @Cacheable("lookups")
    public LookupCodeResult executeCachedLookupCodeRequest(String lookupDefinitionId, String lookupCode, String language, Object agencyId) {
        String url = String.format("%s/rest/lookupCodes/findByLookupCode?language={language}&lookupCode={lookupCode}&lookupDefinitionId={lookupDefinitionId}&agencyId={agency_id}",
                iceLookupUrl);
        return executeGET(url, LookupCodeResult.class, language, lookupCode, lookupDefinitionId, agencyId);
    }

    public boolean isCodeInLookup(String lookupDefinitionId, String lookupCode, String language, Object agencyId) {
        LookupCodeResult lookupCodeResult = executeLookupCodeRequest(lookupDefinitionId, lookupCode, language, agencyId);
        return lookupCodeResult != null && lookupCodeResult.getDescription() != null;
    }

    public boolean isCodeInMappingLookup(String hierarchyDefId, String parentId, String language, String agencyId, Object code) {
        return executeLookupMappingRequestCodeExist(hierarchyDefId, parentId, language, agencyId, code);

    }

    public LookupCodeResult executeLookupCodeRequest(Constants.LookupCodeDefinition lookupDefinitionId, String lookupCode, String language, Object agencyId) {
        return executeLookupCodeRequest(lookupDefinitionId.name(), lookupCode, language, agencyId);
    }

    public MessageRuleSuggestionResult executeMessageRequest(String lookupCode, String language, Object agencyId) {
        return new MessageRuleSuggestionResult(
                executeLookupCodeRequest(Constants.LookupCodeDefinition.MESSAGE, lookupCode, language, agencyId),
                executeLookupCodeRequest(Constants.LookupCodeDefinition.RULE, lookupCode, language, agencyId),
                executeLookupCodeRequest(Constants.LookupCodeDefinition.SUGGESTION, lookupCode, language, agencyId)
        );
    }

    public String executeSubmitMessageRequest(String lookupCode, String language, Object agencyId) {
        LookupCodeResult lookupCodeResult =
                executeLookupCodeRequest(Constants.LookupCodeDefinition.SUBMITMESSAGE, lookupCode, language, agencyId);

        return lookupCodeResult == null ? null : lookupCodeResult.getDescription();
    }


    public String getDescription(LookupCodeResult lookupCodeResult) {
        if (lookupCodeResult == null) {
            return "";
        } else {
            return lookupCodeResult.getDescription();
        }
    }


    public static class ResultString {
        private String result;

        public ResultString() {
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }
    }


    public String getAgencyHierarchyUp(String agencyId) {

        String agency = executeGET(String.format("%s/rest/callOutService/getAgencyHierarchyUp?agencyId={agency_id}",
                iceLookupUrl), ResultString.class, agencyId).getResult();

        if (agency == null || agency.isEmpty()) {
            throw new RuntimeException("Agency list empty");
        }

        return agency;
    }

    public List<String> getAgencyHierarchyDown(String agencyId) {
        ResultString resultString = executeGET(String.format("%s/rest/callOutService/getAgencyHierarchyDown?agencyId={agency_id}",
                iceLookupUrl), ResultString.class, agencyId);
        return Arrays.asList(resultString != null && !StringUtils.isEmpty(resultString.getResult()) ? resultString.getResult().split(",") : new String[]{});
    }

    public HierarchyDefinitionModuleList getAllHierarchyDefinitionModuleByModuleCd(String moduleCode) {
        return new HierarchyDefinitionModuleList(
                executeGET(
                        String.format("%s/rest/hierarchyDefinitionModule/findAllByModuleCd?moduleCd={moduleCd}",
                                iceLookupUrl), HierarchyDefinitionModule[].class, moduleCode));

    }


    public LookupDefinitionModuleList getAllLookupDefinitionModuleByModuleCd(String moduleCode) {
        return new LookupDefinitionModuleList(
                executeGET(
                        String.format("%s/rest/lookupDefinitionModule/findAllByModuleCd?moduleCd={moduleCd}",
                                iceLookupUrl), LookupDefinitionModule[].class, moduleCode));

    }
}
