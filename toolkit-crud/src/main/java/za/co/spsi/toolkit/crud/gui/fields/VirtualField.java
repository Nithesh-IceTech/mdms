package za.co.spsi.toolkit.crud.gui.fields;

import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.crud.gui.Layout;

/**
 * Created by jaspervdb on 4/12/16.
 */
public class VirtualField<T> extends LField<T> {

    private String colmnSql;

    public VirtualField(String captionId, Layout layout) {
        this(captionId, "", layout);
    }

    public VirtualField(String captionId, String colmnName,String colmnSql,Layout layout) {
        super(captionId, colmnName, layout);
        this.colmnSql = colmnSql;
    }

    public VirtualField(String captionId, String colmnName,Layout layout) {
        this(captionId,colmnName,null,layout);
    }

    public String getColmnSql() {
        return colmnSql;
    }

    @Override
    public String getFullColName() {
        return colmnSql != null?colmnSql:super.getFullColName();
    }


    @Override
    protected com.vaadin.ui.Field intoBindingsWithNoValidation(boolean update) {
        return null;
    }
//
//    @Override
//    public void intoBindings() {
//    }
//
//    @Override
//    public void intoControl() {
//    }
}
