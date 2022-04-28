package za.co.spsi.toolkit.crud.gui.fields;

import com.vaadin.data.Property;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.ComboBox;
import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.crud.gui.Layout;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.FormattedSql;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.util.StringList;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jaspervdb
 * Date: 2013/10/31
 * Time: 10:11 PM
 * This field will be visualized by a ComboBox
 */
public class LookupField<T> extends LField<T> implements LeftJoinable {

    public static final String PARENT_ID = "_PARENT_ID_";

    private GetDataSource dataSource;
    private Map<String, String> itemCaption = null;
    private String sql;
    private boolean nullAllowed = true;
    private boolean isLeftJoin = true;
    private LookupField parentReference;
    private String intoControlValue = null;
    private List<LookupField> childReferences = new ArrayList<>();

    public LookupField(Field field, String captionId, String sql, Layout model,LookupField parentReference) {
        super(field, captionId, model);
        this.sql = sql;
        this.parentReference = parentReference;
        if (this.parentReference != null) {
            parentReference.addChildReference(this);
        }
    }

    public LookupField(Field field, String captionId, String sql, Layout model) {
        this(field,captionId,sql,model,null);
    }

    public LookupField<T> setLeftJoin(boolean leftJoin) {
        this.isLeftJoin = leftJoin;
        return this;
    }

    @Override
    public boolean isLeftJoin() {
        return isLeftJoin;
    }

    protected void addChildReference(LookupField lookupField) {
        childReferences.add(lookupField);
    }

    public LookupField setSql(String sql) {
        this.sql = sql;
        return this;
    }

    public LookupField setDataSource(GetDataSource dataSource) {
        this.dataSource = dataSource;
        return this;
    }

    public static interface GetDataSource {
        DataSource getDataSource();
    }

    public GetDataSource getDataSource() {
        return dataSource;
    }

    public Map<String, String> getItemCaption() {
        return itemCaption;
    }

    /**
     * @param entity
     * @param sql
     * @return [0] formatted sql [1] List of all the field values
     */
    public Object[] formatSqlReplaceEntityColumnsWithValues(EntityDB entity, String sql) {
        StringList names = entity.getFullColumnNames();
        List values = new ArrayList<>();
        while (names.indexInsideOfIgnoreCase(sql) > -1) {
            int index = names.indexInsideOfIgnoreCase(sql);
            String name = names.get(index);
            sql = sql.substring(0, sql.toLowerCase().indexOf(name.toLowerCase())) +
                    "?" +
                    sql.substring(sql.toLowerCase().indexOf(name.toLowerCase()) + name.length());
            values.add(getLayout().getMainEntity().getFields().get(names.indexOf(name)).get());
        }
        if (sql.contains(PARENT_ID)) {
            sql = sql.replace(PARENT_ID,"?");
            values.add(parentReference != null ? parentReference.get():null);
        }
        return new Object[]{sql, values};
    }

    public Map<String, String> getItemCaptionMap() {
        if (itemCaption == null) {
            // execute sql
            Object formatted[] = formatSqlReplaceEntityColumnsWithValues(getLayout().getMainEntity(), sql);
            List<List> set = DataSourceDB.executeQuery(getDataSource() == null?getLayout().getDataSource():getDataSource().getDataSource(), (String) formatted[0],
                    ((List) formatted[1]).toArray());
            itemCaption = new LinkedHashMap<>();
            for (List values : set) {
                itemCaption.put(values.get(0).toString(), values.get(1) != null ? values.get(1).toString() : "");
            }
        }
        return itemCaption;
    }

    public void populateLookupFieldComboBox() {
        itemCaption = null;
        ComboBox comboBox = (ComboBox) getVaadinField();
        comboBox.setFilteringMode(FilteringMode.CONTAINS);
        comboBox.removeAllItems();
        for (Object key : getItemCaptionMap().keySet()) {
            comboBox.addItem(key);
            comboBox.setItemCaption(key, getItemCaptionMap().get(key));
        }
        if (intoControlValue != null) {
            comboBox.setValue(intoControlValue);
            intoControlValue = null;
        }
    }

    public void setNullAllowed(boolean nullAllowed) {
        this.nullAllowed = nullAllowed;
    }

    @Override
    public com.vaadin.ui.Field buildVaadinField() {
        final ComboBox comboBox = new ComboBox();
        comboBox.setScrollToSelectedItem(true);
        comboBox.setImmediate(true);
        setComponent(comboBox);
        populateLookupFieldComboBox();
        comboBox.addValueChangeListener((Property.ValueChangeListener) event -> {
            LookupField.this.intoBindingsWithNoValidation();
            for (LookupField child : LookupField.this.childReferences) {
                child.populateLookupFieldComboBox();
            }
        });

        comboBox.setNullSelectionAllowed(nullAllowed);
        setComponent(comboBox);
        return comboBox;
    }

    @Override
    public void intoControl() {
        super.intoControl();
        this.intoControlValue = getAsString();
    }

    public String getJoinTabName() {
        return String.format("A%d", getLayout().getGroups().getNameGroup().getFields().indexOf(this));
    }

    public String getJoinColName() {
        FormattedSql querySql = new FormattedSql(sql);
        StringList names = new StringList(querySql.getSelect().replace(querySql.getFrom() + ".", getJoinTabName() + ".").split(","));
        String colname = names.size() > 1 ? names.get(1) : names.get(0);
        if (colname.toLowerCase().indexOf("as ") != -1) {
            colname = colname.substring(0, colname.toLowerCase().indexOf(" as "));
        }
        return String.format("%s as %s ", colname, EntityDB.getColumnName(getField()));
    }

    @Override
    public String getLeftJoinSql() {
        try {
            ForeignKey foreignKey = (ForeignKey) getField().getAnnotation(ForeignKey.class);
            EntityDB entityDB = (EntityDB) foreignKey.table().newInstance();
            FormattedSql querySql = new FormattedSql(sql);
            String where = String.format("%s.%s = %s", getJoinTabName(), EntityDB.getColumnName(entityDB.getSingleId()),
                    EntityDB.getFullColumnName(getField()));
            where = querySql.getWhere() != null ?
                    String.format("%s and %s", where, querySql.getWhere().replace(querySql.getFrom() + ".", getJoinTabName() + ".")) : where;

            return String.format("left join %s %s on %s", querySql.getFrom(), getJoinTabName(), where);
        } catch (IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
