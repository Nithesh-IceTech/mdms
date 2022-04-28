package za.co.spsi.toolkit.crud.gui.render;

import com.vaadin.server.Page;
import com.vaadin.ui.UI;
import org.apache.commons.lang3.time.DateUtils;
import za.co.spsi.toolkit.crud.gui.ToolkitUI;
import za.co.spsi.toolkit.crud.locale.VaadinLocaleHelper;
import za.co.spsi.toolkit.util.Assert;

import javax.servlet.http.Cookie;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: jaspervdbijl
 * Date: 2013/07/01
 * Time: 10:23 AM
 * ToolkitCrudConstants relevant to the web app
 */
public class ToolkitCrudConstants {

   public static final String ROLE_SYS_ADMIN = "SysAdmin",FARM_CAPTURER = "FarmCapturer",
            SURVEY_VERIFIER = "SurveyVerifier",PROP_VERIFIER = "PropVerifier",SYS_ADMIN= "SysAdmin";


    public static SimpleDateFormat TABLE_TIME_FORMAT = new SimpleDateFormat("HH:mm"),
            TRIP_TABLE_TIMESTAMP_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public static final SimpleDateFormat DASHBOARD_REPORT_DATE_FORMAT = new SimpleDateFormat("MMM-yy");

    public static final int REPORT_MONTHS_TO_START_FROM = 12;

    public static final String LOCALE = "LOCALE", CONTEXT = "CONTEXT", AGENCY = "AGENCY",AGENCY_CHILDREN = "AGENCY_CHILDREN",
            DECIMAL_SEPARATOR="DECIMAL_SEPARATOR", GROUPING_SEPARATOR="GROUPING_SEPARATOR",
            DECIMAL_FORMAT="DECIMAL_FORMAT",ROLE_SUPERVISOR = "Supervisor", ECO_CASH_URL = "ECO_CASH_URL", REALM = "REALM",
            ECO_CASH_POLLING_RETRY="ECO_CASH_POLLING_RETRY";

    private static List<String> locales;

    public static void setLocales(String... available_locales) {
        locales = Arrays.asList(available_locales);
    }

    public static List<String> getLocales() {
        return locales;
    }

    public static String getLocale() {
        Object locale = ToolkitUI.getToolkitUI() != null ? ToolkitUI.getToolkitUI().getAttribute(LOCALE) : "en";
        Assert.notNull(locale != null, "Locale has not been set. call za.co.spsi.vaadin.ToolkitCrudConstants.setLocale");
        return locale != null ? locale.toString() : "";
    }

    public static String getContext() {
        Object context = ToolkitUI.getToolkitUI().getAttribute(CONTEXT);
        Assert.notNull(context != null, "Context has not been set. call za.co.spsi.vaadin.ToolkitCrudConstants.setLocale");
        return context != null ? context.toString() : "";
    }

    public static Locale getAsLocale() {
        return getLocale().equals("pt") ? new Locale(getLocale(), "MZ") :
                new Locale(getLocale());
    }

    public static void setDecimalFormat(String decimalFormat){
        ToolkitUI.getToolkitUI().setAttribute(DECIMAL_FORMAT, decimalFormat);
    }

    public static String getDecimalFormat(){
        return ToolkitUI.getToolkitUI().getAttribute(DECIMAL_FORMAT) == null ? "#,##0.00" :
                ToolkitUI.getToolkitUI().getAttribute(DECIMAL_FORMAT).toString();
    }

    public static void setGroupingSeparator(char groupingSeperator){
        ToolkitUI.getToolkitUI().setAttribute(GROUPING_SEPARATOR, groupingSeperator);
    }

    public static char getGroupingSeparator(){
        return ToolkitUI.getToolkitUI().getAttribute(GROUPING_SEPARATOR) == null ? ',' :
                ToolkitUI.getToolkitUI().getAttribute(GROUPING_SEPARATOR).toString().charAt(0);
    }

    public static void setDecimalSeparator(char decimalSeperator){
        ToolkitUI.getToolkitUI().setAttribute(DECIMAL_SEPARATOR, decimalSeperator);
    }

    public static char getDecimalSeparator(){
        return ToolkitUI.getToolkitUI().getAttribute(DECIMAL_SEPARATOR) == null ? '.' :
                ToolkitUI.getToolkitUI().getAttribute(DECIMAL_SEPARATOR).toString().charAt(0);
    }

    public static boolean isAgencySet() {
        return ToolkitUI.getToolkitUI() != null &&
                ToolkitUI.getToolkitUI().getAttribute(AGENCY) != null;
    }

    public static String getAgencyId() {
        Assert.notNull(ToolkitUI.getToolkitUI().getAttribute(AGENCY), "Agency has not been defined");
        return ToolkitUI.getToolkitUI().getAttribute(AGENCY).toString();
    }

    public static Integer getChildAgencyId() {
        String[] agencyId = getAgencyId() != null ? getAgencyId().split(",") : null;
        return Integer.parseInt(agencyId[0]);
    }

    public static Integer getParentAgencyId() {
        String[] agencyId = getAgencyId() != null ? getAgencyId().split(",") : null;
        //Ignore the ICE_MODULES Agency
        return Integer.parseInt(agencyId[agencyId.length - 1]) != 1001
                ?Integer.parseInt(agencyId[agencyId.length - 1])
                :Integer.parseInt(agencyId[agencyId.length - 2]);
    }

    public static Integer[] getAgenciesAsInt() {
        List<Integer> agencies = new ArrayList<>();
        Arrays.stream(getAgencyId().split(",")).forEach(s -> agencies.add(Integer.parseInt(s)));
        return agencies.toArray(new Integer[]{});
    }

    public static void setLocale(String locale) {
        ToolkitUI.getToolkitUI().setAttribute(LOCALE, locale);
    }

    public static void setContext(String context) {
        ToolkitUI.getToolkitUI().setAttribute(CONTEXT, context);
    }

    public static void setAgencyId(String agencyId) {
        ToolkitUI.getToolkitUI().setAttribute(AGENCY, agencyId);
    }

    public static void setEcoCashUrl(String url) {
        ToolkitUI.getToolkitUI().setAttribute(ECO_CASH_URL, url);
    }

    public static String getEcoCashUrl() {
        Assert.notNull(ToolkitUI.getToolkitUI().getAttribute(ECO_CASH_URL), "EcoCash url has not been defined");
        return ToolkitUI.getToolkitUI().getAttribute(ECO_CASH_URL).toString();
    }

    public static void setEcoCashPollingRetry(Integer polls) {
        ToolkitUI.getToolkitUI().setAttribute(ECO_CASH_POLLING_RETRY, polls);
    }

    public static Integer getEcoCashPollingRetry() {
        Assert.notNull(ToolkitUI.getToolkitUI().getAttribute(ECO_CASH_POLLING_RETRY), "EcoCash retries has not been defined");
        return Integer.parseInt(ToolkitUI.getToolkitUI().getAttribute(ECO_CASH_POLLING_RETRY).toString());
    }

    public static void setRealm(String realm) {
        ToolkitUI.getToolkitUI().setAttribute(REALM, realm);
    }

    public static String getRealm() {
        Assert.notNull(ToolkitUI.getToolkitUI().getAttribute(REALM), "Realm has not been defined");
        return ToolkitUI.getToolkitUI().getAttribute(REALM).toString();
    }

    public static void setChildrenAgencyIds(List<String> agencyId) {
        ToolkitUI.getToolkitUI().setAttribute(AGENCY_CHILDREN, agencyId);
    }

    public static List<String> getChildrenAgencyIds() {
        return (List<String>) ToolkitUI.getToolkitUI().getAttribute(AGENCY_CHILDREN);
    }

    private static final String COOKIE_NAME = "VAADIN_TOOLKIT_LOCALE";

    public static void persistLocale(String locale) {
        String cookieName = (String) UI.getCurrent().getSession().getAttribute(COOKIE_NAME);
        Page.getCurrent().getJavaScript().execute(String.format("document.cookie = '%s=%s;';", cookieName, locale));
    }

    public static String loadLocale(String cookieName, String defaultLocale) {
        UI.getCurrent().getSession().setAttribute(COOKIE_NAME, cookieName);
        Cookie language = VaadinLocaleHelper.getCookie(cookieName);
        if (language == null) {
            persistLocale(defaultLocale);
            return defaultLocale;
        } else {
            return language.getValue();
        }
    }

    public static String loadLocale(String defaultLocale) {
        return loadLocale(UI.getCurrent().getClass().getName(), defaultLocale);
    }



    private static List<Field> listAllFields(Object obj) {
        List<Field> fieldList = new ArrayList<Field>();

        fieldList.addAll(Arrays.asList(obj.getClass().getDeclaredFields()));
        fieldList.addAll(Arrays.asList(obj.getClass().getSuperclass().getDeclaredFields()));
        return fieldList;
    }

    public static void main(String args[]) throws Exception {
        System.out.println(DateUtils.addMilliseconds(DateUtils.ceiling(new Date(), Calendar.DATE), -1));
    }

}
