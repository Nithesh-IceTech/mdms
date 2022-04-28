package za.co.spsi.toolkit.crud.gui.query;

import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.db.FormattedSql;
import za.co.spsi.toolkit.util.StringList;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Created by jaspervdb on 2016/05/03.
 */
public class FormattedQuerySql extends FormattedSql {

    private Layout layout;

    public FormattedQuerySql(Layout layout,String sql) {
        super(sql);
        this.layout = layout;
    }

    /**
     * split out any functions in the field names
     * @return
     */
    public StringList getSelectFunctions() {
        return
        Arrays.stream(getSelect().split(",")).
                map(value -> value.indexOf(" ")!=-1?value.substring(0,value.indexOf(" ")):"").
                collect(Collectors.toCollection(StringList::new));
    }


}
