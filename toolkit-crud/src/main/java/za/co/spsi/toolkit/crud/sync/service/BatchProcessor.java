package za.co.spsi.toolkit.crud.sync.service;

import org.json.JSONArray;
import org.json.JSONObject;
import za.co.spsi.toolkit.crud.sync.db.BatchEntity;
import za.co.spsi.toolkit.crud.sync.db.DeviceEntity;
import za.co.spsi.toolkit.crud.sync.db.DeviceEntitySyncMapEntity;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.JSONMap;
import za.co.spsi.toolkit.db.drivers.DriverFactory;
import za.co.spsi.toolkit.service.ProcessorService;
import za.co.spsi.toolkit.util.Assert;
import za.co.spsi.toolkit.util.Util;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class BatchProcessor extends ProcessorService {

    public static Map<String, Class<? extends EntityDB>> registeredEntities = new HashMap<>();

    static {
        registeredEntities.put("deviceTracked", DeviceEntity.class);
    }

    public static final int STATUS_UNPROCESSED = 1, STATUS_FAILED = 4, STATUS_OK = 3;

    public static final Logger TAG = Logger.getLogger(BatchProcessor.class.getName());

    private void processMultiCastLogic(Connection connection, BatchEntity batchEntity, EntityDB entity) throws SQLException {
        DeviceEntitySyncMapEntity map = new DeviceEntitySyncMapEntity();
        map.entityClass.set(entity.getClass().getName());
        map.entityId.set(entity.getSingleId().getAsString());
        map.deviceId.set(batchEntity.deviceId.get());
        map.onTablet.set('Y');
        map = DataSourceDB.getFromSet(connection, map);

        if (map != null) {
            map.processOnOnTabletMultiCast(connection, entity);
            DataSourceDB.set(connection, map);
        }
    }

    public void process(Connection connection, BatchEntity batchEntity) throws Exception {
        List<EntityDB> initEntities = batchEntity.getProcessor().process(connection, batchEntity);
        if (batchEntity.deviceId.get() != null) {
            for (EntityDB entity : initEntities) {
                processMultiCastLogic(connection, batchEntity, entity);
            }
        }
    }

    public static interface BatchProcessWorker {
        List<EntityDB> process(Connection connection, BatchEntity batchEntity) throws Exception;
    }

    public static class DefaultProcessor implements BatchProcessWorker {

        @Override
        public List<EntityDB> process(Connection connection, BatchEntity batchEntity) throws Exception {
            JSONObject jObject = new JSONObject(batchEntity.data.get().startsWith("[") ? batchEntity.data.get().substring(1, batchEntity.data.get().length() - 1) : batchEntity.data.get());
            //
            List<EntityDB> initEntities = new ArrayList<>();
            for (String key : jObject.keySet()) {
                if (registeredEntities.containsKey(key)) {
                    if (jObject.get(key) instanceof JSONArray) {
                        for (Object obj : (JSONArray) jObject.get(key)) {
                            Assert.isTrue(obj instanceof JSONObject, "Object should be of type JSONObject");
                            EntityDB.initFromJson(connection, registeredEntities.get(key), null, (JSONObject) obj, initEntities);
                        }
                    } else {
                        EntityDB.initFromJson(connection, registeredEntities.get(key), jObject.get(key).toString(), initEntities);
                    }
                } else {
                    TAG.warning(String.format("Entity %s not registered", key));
                }
            }
            return initEntities;
        }
    }

    public static class SimpleProcessor implements BatchProcessWorker {

        @Override
        public List<EntityDB> process(Connection connection, BatchEntity batchEntity) throws Exception {
            return JSONMap.fromJSONMap(connection, new JSONObject(batchEntity.data.get()));
        }
    }

    /**
     * step through the batch data and process
     */
    public void process(Connection connection) throws SQLException {
        String query = "select * from batch where batch_status_cd = ?";
        query = DriverFactory.getDriver().limitSqlAndOrderBy(query, 50, "create_t", false);
        DataSourceDB<BatchEntity> ds = new DataSourceDB<>(BatchEntity.class).getAll(connection,query,STATUS_UNPROCESSED);
        for (BatchEntity batchEntity : ds) {
            TAG.info("Processing batch row " + batchEntity.batchId.get() + " " + Thread.currentThread().getId());
            try {
                process(connection, batchEntity);
                batchEntity.batchStatusCd.set(STATUS_OK);
                DataSourceDB.set(connection, batchEntity);
                connection.commit();
            } catch (Throwable ex) {
                TAG.log(Level.WARNING, ex.getMessage(), ex);
                ex.printStackTrace();
                connection.rollback();
                batchEntity.batchStatusCd.set(STATUS_FAILED);
                batchEntity.error.set(Util.getExceptionAsString(ex, 1024));
                DataSourceDB.set(connection, batchEntity);
                connection.commit();
            }
        }
    }

    /**
     * step through the batch data and process
     */
    public void process(DataSource dataSource) {
        try {
            TAG.info("process");
            try (Connection connection = dataSource.getConnection()) {
                connection.setAutoCommit(false);
                process(connection);
            }
        } catch (SQLException e) {
            TAG.severe(e.getMessage());
        }
    }

}
