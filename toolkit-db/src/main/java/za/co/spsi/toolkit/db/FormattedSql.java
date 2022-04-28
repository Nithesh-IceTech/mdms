package za.co.spsi.toolkit.db;

import za.co.spsi.toolkit.util.StringList;
import za.co.spsi.toolkit.util.StructuredText;

import java.nio.file.FileVisitOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jaspervdb on 2016/04/27.
 */
public class FormattedSql extends StructuredText {
    private static StringList DIV_LIST = new StringList(new String[]{"select "," from "," where "," group by "," order by "});
    private static List<Boolean> DIV_LIST_ORDER = new ArrayList<>();//boolean[]{true,true,true,false,false};

    static {
        Collections.addAll(DIV_LIST_ORDER,new Boolean[]{true,true,true,false,false});
    }

    public FormattedSql(String sql) {
        super(DIV_LIST,DIV_LIST_ORDER,sql);
    }

    private String getString(int index) {
        return values.size() > index?values.get(index):null;
    }

    public String getSelect() {
        return getString(0);
    }

    public void setSelect(String select) {
        set(0,select);
    }

    public String getFrom() {
        return getString(1);
    }

    public void setFrom(String from) {
        set(1,from);
    }

    private void set(int index,String value) {
        while (values.size() < index + 1) {
            values.add(null);
        }
        values.set(index,value);
    }

    public String getWhere() {
        return getString(2);
    }

    public void setWhere(String where) {
        set(2,where);
    }

    public String getOrder() {
        return getString(4);
    }

    public void setOrder(String order) {
        set(4,order);
    }

    public String getGroup() {
        return values.get(3);
    }

    public void setGroup(String group) {
        set(3,group);
    }

    public FormattedSql addWhere(String where) {
        setWhere(getWhere() == null ? where : String.format("((%s) and (%s))", where, getWhere()));
        return this;
    }

    public StringList getSelectColumns(boolean includeTableName) {
        String select = getSelect();
        if (select.contains("(")) {
            StringList sl = new StringList(select,"\\(");
            for (int i =0;i < sl.size();i++) {
                if (sl.get(i).contains(")")) {
                    sl.set(i, sl.get(i).substring(sl.get(i).indexOf(")")+1));
                }
            }
            select = sl.toString("");
        }
        StringList sl = new StringList(select,",");
        for (int i = 0;i < sl.size();i++) {
            String value = sl.get(i);
            if (value.toLowerCase().contains(" as ")) {
                value = value.substring(value.toLowerCase().indexOf(" as ")+" as ".length());
            }
            if (value.contains(".")&& !includeTableName) {
                value = value.substring(value.indexOf(".")+1);
            }
            sl.set(i,value);
        }
        return sl;
    }

    /**
     * Generates a string to be uses in a where cluase with in values
     * @param items
     * @return ?,?,? as item to be used in clause
     */
    public static String getInListParm(Object... items) {
        StringBuilder stringBuilder = new StringBuilder();
        for(Object item : items) {
            stringBuilder.append("?,");
        }

        return stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(",")).toString();
    }

    static String sql = "select LEGAL_ENTITY.NUIT,LEGAL_ENTITY.NAME,LEGAL_ENTITY.SURNAME,LEGAL_ENTITY.REGISTERED_NAME,LEGAL_ENTITY.REGISTRATION_N,LEGAL_ENTITY.TRADING_NAME,case(Legal_Entity.legal_Entity_Type_Cd) when (1) then nuit || '-' || name || ' ' || surname when (2) then nuit || '-' || registered_Name || ' ' || trading_Name end as legalEntityDetail,LEGAL_ENTITY.LEGAL_ENTITY_ID, A1.DESCRIPTION as TITLE_CD , A7.DESCRIPTION as ENTITY_STATUS_CD , A9.DESCRIPTION as REVIEW_STATUS_CD ,TO_CHAR (LEGAL_ENTITY.ENTITY_STATUS_CHANGE_D,'YYYY-MM-DD HH24:MI') as ENTITY_STATUS_CHANGE_D,TO_CHAR (LEGAL_ENTITY.REVIEW_STATUS_CHANGE_D,'YYYY-MM-DD HH24:MI') as REVIEW_STATUS_CHANGE_D,TO_CHAR (LEGAL_ENTITY.CAPTURED_D,'YYYY-MM-DD HH24:MI') as CAPTURED_D from legal_entity  left join lookups A1 on A1.code = LEGAL_ENTITY.TITLE_CD and A1.lookup_def = 'TITLE' and A1.agency_id = '1000056' and A1.lang = 'en'   left join lookups A7 on A7.code = LEGAL_ENTITY.ENTITY_STATUS_CD and A7.lookup_def = 'ENTITYSTAT' and A7.agency_id = '1000056' and A7.lang = 'en'   left join lookups A9 on A9.code = LEGAL_ENTITY.REVIEW_STATUS_CD and A9.lookup_def = 'REVIEWSTATUS' and A9.agency_id = '1000056' and A9.lang = 'en'  where entity_status_cd <> 5 and legal_entity.agency_id = 3";

    public static void main(String args[]) throws Exception {
        System.out.println(new FormattedSql(sql).getSelectColumns(false).toString());
    }


}
