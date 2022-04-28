package za.co.spsi.toolkit.crud.gui.query;

import com.vaadin.data.Container;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.data.util.sqlcontainer.RowItem;
import com.vaadin.data.util.sqlcontainer.query.FreeformStatementDelegate;
import com.vaadin.data.util.sqlcontainer.query.OrderBy;
import com.vaadin.data.util.sqlcontainer.query.generator.StatementHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import za.co.spsi.toolkit.crud.gui.LField;
import za.co.spsi.toolkit.crud.gui.LFieldList;
import za.co.spsi.toolkit.crud.gui.fields.LeftJoinable;
import za.co.spsi.toolkit.crud.gui.fields.LookupField;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.FormattedSql;
import za.co.spsi.toolkit.db.drivers.Driver;
import za.co.spsi.toolkit.db.drivers.DriverFactory;
import za.co.spsi.toolkit.util.StringList;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jaspervdb on 15/01/27.
 */
public class ViewQueryDelegate implements FreeformStatementDelegate {

    private String sqlSelect, columnNames;
    private int rowLimit = 1000;

    private static final Logger LOG = LoggerFactory.getLogger(ViewQueryDelegate.class);
    private Driver driver;

    private StringList colNames;
    private List<Container.Filter> filters;
    private List<OrderBy> initialOrderBy = new ArrayList<>(), orderBys;
    private LayoutViewGrid viewGrid;
    private Runnable queryLoaded;

    public ViewQueryDelegate(Driver driver, LayoutViewGrid viewGrid, String sqlSelect, int rowLimit) {
        this.driver = driver;
        this.rowLimit = rowLimit;
        this.viewGrid = viewGrid;
        setSql(sqlSelect);
        setViewGrid(viewGrid);
    }

    public void setRowLimit(int rowLimit) {
        this.rowLimit = rowLimit;
    }

    public void setSql(String sql) {
        this.sqlSelect = formatSql(viewGrid, sql.replace("_AGENCY_ID_", ToolkitCrudConstants.getChildAgencyId().toString()));
    }

    private void removeDuplicates(StringList colNames) {
        colNames.removeDuplicates();
        StringList toBeRemoved = new StringList();
        for (String value : colNames) {
            if (value.toLowerCase().indexOf(" as ") != -1) {
                value = value.substring(value.toLowerCase().indexOf(" as ") + " as ".length());
                if (colNames.containsIgnoreCase(value)) {
                    toBeRemoved.add(value);
                }
            }
        }
        colNames.removeAllIgnoreCase(toBeRemoved);
    }

    private List<OrderBy> formatOrderBy(String orderBys) {
        List<OrderBy> orderByList = new ArrayList<>();
        for (String orderBy : orderBys.split(",")) {
            orderByList.add(new OrderBy(orderBy.split(" ")[0], orderBy.split(" ").length == 1 || "asc".equalsIgnoreCase(orderBy.split(" ")[1])));
        }
        return orderByList;
    }

    private String formatSql(LayoutViewGrid viewGrid, String sql) {
        LFieldList lookupFields = viewGrid.getLayout().getGroups().getNameGroup().getFields().getFieldsOfType(LeftJoinable.class).stream().
                filter(l -> ((LeftJoinable)l).isLeftJoin()).collect(Collectors.toCollection(LFieldList::new));
        FormattedSql formattedSql = new FormattedSql(sql);
        StringList tables = new StringList(formattedSql.getFrom(), ",");
        tables.trim(true).trim(false);

        String where = sql.toLowerCase().indexOf("where") > -1 ? sql.substring(sql.toLowerCase().indexOf("where ") + "where ".length()) : "";

        if (formattedSql.getSelect().indexOf("*") != -1) {
            formattedSql.setSelect(formattedSql.getSelect().replaceFirst("[*]", viewGrid.getLayout().getGroups().getNameGroup().getFields().stream().
                    filter(f -> !(f instanceof LeftJoinable) || !((LeftJoinable)f).isLeftJoin()).
                    collect(Collectors.toCollection(LFieldList::new)).getFullColNames().toString(",")));
        }
        colNames = new StringList(formattedSql.getSelect(), ",").trim(true).trim(false);
        // add colnams that if lefrt joinanle but not ---
        // add order by to col names
        removeDuplicates(colNames);
        colNames.addAll(EntityDB.getFullColumnNames(viewGrid.getLayout().getMainEntity().getId()));

            for (LField mlcsField : lookupFields) {
            if (!(mlcsField instanceof LookupField) || ((LookupField)mlcsField).getDataSource() == null) {
                if (colNames.indexOfIgnoreCase(mlcsField.getFullColName()) != -1) {
                    colNames.remove(colNames.indexOfIgnoreCase(mlcsField.getFullColName()));
                }
                colNames.add(((LeftJoinable) mlcsField).getJoinColName());
                int tabIndex = tables.indexOfInsideIgnoreCase(mlcsField.getField().getEntity().getName());
                tables.set(tabIndex, tables.get(tabIndex) + " " + ((LeftJoinable) mlcsField).getLeftJoinSql());
            }
        }
        for (LField timestampField : viewGrid.getLayout().getGroups().getNameGroup().getFields().getFieldsOfColumnType(Timestamp.class)) {

            String colN = colNames.containsIgnoreCase(EntityDB.getFullColumnName(timestampField.getField(),true)) ?
                    EntityDB.getFullColumnName(timestampField.getField(),true) : timestampField.getColName();

            colNames.add(DriverFactory.getDriver().formatDate(
                    timestampField.getFullColName().indexOf(" ")!=-1?timestampField.getFullColName().substring(0,timestampField.getFullColName().lastIndexOf(" ")):
                            timestampField.getFullColName()
                    , EntityDB.getColumnName(timestampField.getField())));

            if(StringUtils.isNotEmpty(colN)) {
                if(!colNames.remove(colN)) {
                    if(!colNames.remove(timestampField.getFullColName())) {
                        List<String> timeColumns = new ArrayList<>();
                        timeColumns.add(colN);
                        colNames.removeAllIgnoreCase(timeColumns);
                    }
                }
            }

        }
        colNames.removeDuplicates();

        if (formattedSql.getOrder() != null) {
            setInitialOrderBy(formatOrderBy(formattedSql.getOrder()).toArray(new OrderBy[]{}));
        }
        where = where.length() > 0 ? "where " + where : "";
        return String.format("select %s from %s %s", colNames.toString(","), tables.toString(","), where);
    }


    public LayoutViewGrid getViewGrid() {
        return viewGrid;
    }

    public void setViewGrid(LayoutViewGrid viewGrid) {
        this.viewGrid = viewGrid;
        StringList columns = new StringList();
        columns.addAll(viewGrid.getLayout().getGroups().getNameGroup().getFields().getColNames());
        columns.addAll(EntityDB.getColumnNames(viewGrid.getLayout().getMainEntity().getId()));
        columns.removeDuplicates();
        columnNames = columns.toString(",");
    }

    public void setInitialOrderBy(OrderBy... initialOrderBy) {
        this.initialOrderBy = Arrays.asList(initialOrderBy);
    }

    @Deprecated
    public String getQueryString(int offset, int limit)
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Use getQueryStatement method.");
    }

    public String getSqlSelect() {
        return sqlSelect;
    }

    /**
     * set the unformatted raw sql
     *
     * @param sqlSelect
     */
    public void setSqlSelect(String sqlSelect) {
        this.sqlSelect = sqlSelect;
    }

    public String getSql() {
        return getSql(null, null,rowLimit);
    }

    private StringList getSqlFilter() {
        return filters != null ? filters.stream().map(f -> String.format("lower(%s)", ((SimpleStringFilter) f).getPropertyId()) + " like lower('%" +
                ((SimpleStringFilter) f).getFilterString().toUpperCase() + "%')").collect(Collectors.toCollection(StringList::new)) :
                new StringList();
    }

    public String getSql(Integer offset, Integer limit, Integer rowLimit) {

        String sql;
        List<String> sqlFilter = getSqlFilter();
        String sqlSelect = offset != null ? driver.offset(getSqlSelect(), offset, limit) : getSqlSelect();
        if (!sqlFilter.isEmpty()) {
            sql = String.format("select * from (%s) sqlselect where %s ", sqlSelect, StringUtils.join(sqlFilter, " and "));
        } else {
            sql = String.format("select * from (%s) sqlselect", sqlSelect);
        }
        List<String> orderFilter = new ArrayList<>();
        if (orderBys != null) {
            for (OrderBy orderBy : orderBys) {
                orderFilter.add(String.format("\"%s\" %s", orderBy.getColumn(), orderBy.isAscending() ? "asc" : "desc"));
            }
        }
        if (orderFilter.isEmpty() && !initialOrderBy.isEmpty()) {
            for (OrderBy orderBy : initialOrderBy) {
                orderFilter.add(String.format("%s %s", orderBy.getColumn(), orderBy.isAscending() ? "asc" : "desc"));
            }
        }
        // remove the table name from the order by
        Driver driver = DriverFactory.getDriver();
        String value = orderFilter.isEmpty() ?
                viewGrid != null ? String.format("%s order by %s", sql, EntityDB.getColumnNames(viewGrid.getLayout().getMainEntity().getId()).toString(",")) : sql
                : String.format("%s order by %s", sql, orderFilter.stream().
                        map(o -> o.indexOf(".") != -1 ? o.substring(o.indexOf(".") + 1) : o).collect(Collectors.toCollection(StringList::new)).toString(","));
        return rowLimit != null ? driver.limitSql(value, rowLimit) : value;
    }

    public void setQueryLoaded(Runnable queryLoaded) {
        this.queryLoaded = queryLoaded;
    }

    public StatementHelper getQueryStatement(int offset, int limit) throws UnsupportedOperationException {
        if (queryLoaded != null) {
            queryLoaded.run();
        }
        String sql = getSql(null, null, rowLimit);
        FormattedSql formattedSql = new FormattedSql(sql);
        if (formattedSql.getSelect().indexOf("*") != -1) {
            formattedSql.setSelect(formattedSql.getSelect().replace("*", viewGrid.getLayout().getGroups().getNameGroup().
                    getFields().getColNames().toString(",")));
        }

        StringList select = formattedSql.getSelectColumns(false);
        StringList idNames = EntityDB.getColumnNames(viewGrid.getLayout().getMainEntity().getId());
        select.addAll(idNames);

        select.add(0, String.format("%s + " + offset + " || '|' || %s as %s"
                ,DriverFactory.getDriver().getRowNum()
                ,idNames.toString(" || '|' || "), "UNIQUE_ROW_ID"));

        sql = String.format("select %s from (%s) querystatement", select.toString(","), driver.offset(sql, offset, limit));
        LOG.info(String.format("Query sql: %s", sql));
        StatementHelper sh = new StatementHelper();
        sh.setQueryString(sql);
        return sh;
    }

    public void setFilters(List<Container.Filter> filters)
            throws UnsupportedOperationException {
        this.filters = filters;
    }

    public void setOrderBy(List<OrderBy> orderBys)
            throws UnsupportedOperationException {
        this.orderBys = orderBys;
    }

    @Deprecated
    public String getCountQuery() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Use getCountStatement method.");
    }

    public StatementHelper getCountStatement() throws UnsupportedOperationException {
        StatementHelper sh = new StatementHelper();
        String query = getSql(null, null,rowLimit);
        query = "select count(*)" + query.substring(query.toLowerCase().indexOf(" from "));
        sh.setQueryString(query);
        return sh;
    }

    public int storeRow(Connection conn, RowItem row) throws SQLException {
        int returnValue = 0;
        return returnValue;

    }

    @Override
    public boolean removeRow(Connection conn, RowItem row) throws UnsupportedOperationException, SQLException {
        return false;
    }


    @Deprecated
    public String getContainsRowQueryString(Object... keys)
            throws UnsupportedOperationException {
        throw new UnsupportedOperationException(
                "Please use getContainsRowQueryStatement method.");
    }

    @Override
    public StatementHelper getContainsRowQueryStatement(Object... keys)
            throws UnsupportedOperationException {
        String sql = getSql();
        String s1 = sql.substring(0, sql.lastIndexOf(")") + 1);
        String s2 = sql.substring(sql.lastIndexOf(")") + 1);
        String orderBy = s2.indexOf(" order by ") > 0 ?
                s2.substring(s2.indexOf(" order by ")) : "";

        String where = s2.indexOf(" where ") != -1 ?
                s2.substring(s2.indexOf(" where ") + " where ".length(),
                        s2.indexOf(" order by ") >0 ? s2.indexOf(" order by ") : s2.length()) : "";

        List<String> equalsStrings = new ArrayList<>();
        for (int i = 0; i < keys.length; i++) {
            equalsStrings.add(String.format(" %s = '%s' ", EntityDB.getColumnName(
                    viewGrid.getLayout().getMainEntity().getId().get(i)), keys[i]));
        }
        String query = where.isEmpty() ?
                String.format("%s %s (%s) %s", s1, filters != null && !filters.isEmpty() ? "and" : "where", StringUtils.join(equalsStrings, " and "), orderBy) :
                String.format("%s where ((%s) and %s) %s", s1, where, StringUtils.join(equalsStrings, " and "), orderBy);
        StatementHelper sh = new StatementHelper();
        sh.setQueryString(query);
        return sh;
    }
}
