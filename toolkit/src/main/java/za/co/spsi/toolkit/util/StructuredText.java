package za.co.spsi.toolkit.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaspervdb on 2016/04/27.
 */
public class StructuredText {
    protected StringList divList;
    protected List<Boolean> orderList  = null;
    protected StringList values = new StringList();

    public StructuredText(StringList divList,List<Boolean> orderList ,String sql) {
        this.divList = divList;
        this.orderList  = orderList ;
        Assert.isTrue(orderList  == null || orderList .size() == divList.size(),"Invalid divider order specified");
        init(sql);
    }

    public StructuredText(StringList divList,String sql) {
        this(divList,generateOrder(divList.size(),null),sql);
    }

    private static List<Boolean> generateOrder(int size,List<Boolean> source) {
        List orderList = new ArrayList<>();
        for (int i =0;i < size;i++) {
            orderList.add(source != null?source.get(i):true);
        }
        return orderList;
    }

    protected void init(String sql) {
        format(sql,divList.clone(),generateOrder(divList.size(),orderList),values);
    }

    private static int indexOf(String s1,String s2,boolean first) {
        return first?s1.indexOf(s2):s1.lastIndexOf(s2);
    }

    public static void format(String sql,StringList div,List<Boolean> oLst,StringList values) {
        String lower = sql.toLowerCase().replace("\n"," ");
        if (!lower.contains(div.get(0))) {
            oLst.remove(0);
            div.remove(0);
            values.add(null);
            format(sql,div,oLst,values);
        } else if (div.getIndexOfAnyPartOf(lower) > -1 && div.size() > 1) {
            String start = div.remove(0);
            boolean order = oLst.remove(0);
            int end = div.getIndexOfAnyPartOf(lower)>-1?indexOf(lower,div.get(div.getIndexOfAnyPartOf(lower)),order):sql.length();
            values.add(sql.substring(indexOf(lower,start,order)+start.length(),end));
            sql = sql.substring(end);
            if (!sql.trim().isEmpty()) {
                format(sql, div, oLst,values);
            }
        } else {
            values.add(sql.substring(lower.indexOf(div.get(0))+div.get(0).length()));
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i  = 0;i < values.size();i++) {
            if (values.get(i) != null) {
                sb.append(divList.get(i)).append(values.get(i));
            }
        }
        return sb.toString();
    }

}
