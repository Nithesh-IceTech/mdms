package za.co.spsi.toolkit.crud.gui.fields;

/**
 * Created by jaspervdb on 2016/06/17.
 */
public interface LeftJoinable {
    String getJoinColName();
    String getLeftJoinSql();

    default boolean isLeftJoin() {
        return true;
    }
}
