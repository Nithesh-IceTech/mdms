package za.co.spsi.toolkit.crud.sync.service;

import org.json.JSONObject;
import za.co.spsi.toolkit.crud.sync.db.DirectSyncBatchEntity;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.drivers.DriverFactory;
import za.co.spsi.toolkit.service.ProcessorService;
import za.co.spsi.toolkit.util.Util;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class DirectSyncBatchProcessor extends ProcessorService {

    public static final int STATUS_UNPROCESSED = 1, STATUS_FAILED = 4, STATUS_OK = 3;
    public static final Logger TAG = Logger.getLogger(DirectSyncBatchProcessor.class.getName());

    enum ACTION {
        INSERT_UPDATE(1), DELETE(2);
        private Integer code;

        ACTION(int code) {
            this.code = code;
        }
    }

    public void process(Connection connection, DirectSyncBatchEntity directSyncBatchEntity) throws Exception {

        Class c = Class.forName(directSyncBatchEntity.entity.get());
        EntityDB entity = (EntityDB) c.newInstance();
        entity.initFromJson(new JSONObject(directSyncBatchEntity.data.get()));

        if (!ACTION.DELETE.code.equals(directSyncBatchEntity.actionCd.get())) {
            DataSourceDB.createOrUpdate(connection, entity);
        } else {
            DataSourceDB.delete(connection, entity);
        }
    }

    /**
     * step through the batch data and process
     */
    public void process(Connection connection) throws SQLException {
        DataSourceDB<DirectSyncBatchEntity> ds = new DataSourceDB<>(DirectSyncBatchEntity.class).getAll(connection,
                DriverFactory.getDriver().limitSql("select * from DIRECT_SYNC_BATCH where batch_status_cd = ? " +
                        "order by create_t asc", 150), STATUS_UNPROCESSED);

        for (DirectSyncBatchEntity directSyncBatchEntity : ds) {
            TAG.info("PARK - Processing batch row " + directSyncBatchEntity.batchId.get() + " " + Thread.currentThread().getId());
            try {
                process(connection, directSyncBatchEntity);
                directSyncBatchEntity.batchStatusCd.set(STATUS_OK);
                DataSourceDB.set(connection, directSyncBatchEntity);
                connection.commit();
            } catch (Throwable ex) {
                TAG.log(Level.WARNING, ex.getMessage(), ex);
                ex.printStackTrace();
                connection.rollback();
                directSyncBatchEntity.batchStatusCd.set(STATUS_FAILED);
                directSyncBatchEntity.error.set(Util.getExceptionAsString(ex, 1024));
                DataSourceDB.set(connection, directSyncBatchEntity);
                connection.commit();
            }
        }
    }

    /**
     * step through the batch data and process
     */
    public void process(DataSource dataSource) {
        try {
            try (Connection connection = dataSource.getConnection()) {
                connection.setAutoCommit(false);
                process(connection);
            }
        } catch (SQLException e) {
            TAG.severe(e.getMessage());
        }
    }

}
