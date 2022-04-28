package za.co.spsi.lookup.dao;

/**
 * Created by jaspervdb on 2/3/16.
 */
public class HierarchyDefinitionResult {

    /*

    "childLookupCode" : "1",
  "childLookupDescription" : "BISHOP GAUL AVENUE",
  "childLookupDefinitionId" : "STREETNAMENUM",
  "parentLookupCode" : "84",
  "parentLookupDescription" : "MILTON PARK",
  "parentLookupDefinitionId" : "NEIGHBORHOOD"

     */

    private String childLookupCode,childLookupDescription,childLookupDefinitionId,parentLookupCode,parentLookupDescription,parentLookupDefinitionId;

    public String getChildLookupCode() {
        return childLookupCode;
    }

    public void setChildLookupCode(String childLookupCode) {
        this.childLookupCode = childLookupCode;
    }

    public String getChildLookupDescription() {
        return childLookupDescription;
    }

    public void setChildLookupDescription(String childLookupDescription) {
        this.childLookupDescription = childLookupDescription;
    }

    public String getChildLookupDefinitionId() {
        return childLookupDefinitionId;
    }

    public void setChildLookupDefinitionId(String childLookupDefinitionId) {
        this.childLookupDefinitionId = childLookupDefinitionId;
    }

    public String getParentLookupCode() {
        return parentLookupCode;
    }

    public void setParentLookupCode(String parentLookupCode) {
        this.parentLookupCode = parentLookupCode;
    }

    public String getParentLookupDescription() {
        return parentLookupDescription;
    }

    public void setParentLookupDescription(String parentLookupDescription) {
        this.parentLookupDescription = parentLookupDescription;
    }

    public String getParentLookupDefinitionId() {
        return parentLookupDefinitionId;
    }

    public void setParentLookupDefinitionId(String parentLookupDefinitionId) {
        this.parentLookupDefinitionId = parentLookupDefinitionId;
    }
}
