package za.co.spsi.toolkit.db.ano;

import java.sql.Connection;

/**
 * Created by jaspervdb on 2016/09/26.
 */
public interface VirtuallyDeleted {
    /**
     * overload to implement
     * if this is implemented then it will not perform a hard delete
     */
    public void virtualDelete(Connection connection) ;

}
