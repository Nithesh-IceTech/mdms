package za.co.spsi.lookup.dao;

/**
 * Created by jaspervdb on 1/21/16.
 */
public class MessageRuleSuggestionResult {

    private LookupCodeResult message,rule,suggestion;

    public MessageRuleSuggestionResult(LookupCodeResult message, LookupCodeResult rule, LookupCodeResult suggestion) {
        this.message = message;
        this.rule = rule;
        this.suggestion = suggestion;
    }

    public LookupCodeResult getMessage() {
        return message;
    }

    public LookupCodeResult getRule() {
        return rule;
    }

    public LookupCodeResult getSuggestion() {
        return suggestion;
    }
}
