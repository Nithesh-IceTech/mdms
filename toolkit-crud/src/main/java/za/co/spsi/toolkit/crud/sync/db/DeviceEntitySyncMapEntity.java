/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.spsi.toolkit.crud.sync.db;

import org.json.JSONArray;
import org.json.JSONObject;
import za.co.spsi.toolkit.crud.sync.SyncableEntity;
import za.co.spsi.toolkit.dao.DeviceEntityMapReq;
import za.co.spsi.toolkit.dao.RetrieveDeviceEntityMapReq;
import za.co.spsi.toolkit.dao.ToolkitConstants;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.db.drivers.DriverFactory;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.util.ObjectUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;


/**
 * @author jaspervdb
 * <p>
 * CREATE TABLE t1 (c1 NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY,c2 VARCHAR2(10));
 */
@Table(version = 1)
public class DeviceEntitySyncMapEntity extends EntityDB {

    public static final Logger TAG = Logger.getLogger(DeviceEntitySyncMapEntity.class.getName());

    @Id()
    @Column(name = "DEVICE_ENTITY_SYNC_MAP_ID", size = 15)
    public Field<Integer> deviceEntitySyncMapId = new Field<>(this);
    @Column(name = "DEVICE_ID", size = 36)
    public Field<String> deviceId = new Field<>(this);

    @Column(name = "ENTITY_ID", size = 36)
    public Field<String> entityId = new Field<>(this);
    @Column(name = "ENTITY_CLASS", size = 250)
    public Field<String> entityClass = new Field<>(this);

    @Column(name = "DELIVERED", defaultValue = "N", size = 1)
    public Field<Character> delivered = new Field<>(this);
    @Column(name = "DELIVERED_TIME", defaultValue = "now")
    public Field<Timestamp> deliveredTime = new Field<>(this);
    @Column(name = "ERROR_MESSAGE", size = 4000)
    public Field<String> errorMessage = new Field<>(this);

    @Column(name = "ON_TABLET", defaultValue = "N", size = 1)
    public Field<Character> onTablet = new Field<>(this);

    public DeviceEntitySyncMapEntity() {
        super("DEVICE_ENTITY_SYNC_MAP");
    }

    public DeviceEntitySyncMapEntity(EntityDB entity, DeviceEntity deviceEntity) {
        this();
        deviceId.set(deviceEntity.deviceId.get());
        entityId.set(entity.getSingleId().getAsString());
        entityClass.set(entity.getClass().getName());
        delivered.set('N');
        onTablet.set('N');
    }

    public static void recall(Connection connection, Class type, String id) throws SQLException {
        DataSourceDB.executeUpdate(connection, "update device_entity_sync_map set delivered = 'R' where " +
                "delivered = 'Y' and entity_class = ? and entity_id = ? ", type.getName(), id);
    }

    public static void cancelUndelivered(Connection connection, Class type, String id) throws SQLException {
        DataSourceDB.executeUpdate(connection, "update device_entity_sync_map set delivered = 'C' where " +
                "delivered = 'N' and entity_class = ? and entity_id = ? ", type.getName(), id);
    }

    public EntityDB getRefEntity() {
        try {
            EntityDB entityDB = (EntityDB) Class.forName(entityClass.get()).newInstance();
            entityDB.getSingleId().set(entityId.get());
            return entityDB;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public EntityDB getRefEntity(Connection dataSource) {
        return DataSourceDB.loadFromId(dataSource, getRefEntity());
    }

    private void processOnOnTabletMultiCast(Connection connection, EntityDB entity, Integer entityStatusCd) throws SQLException {
        if (ToolkitConstants.ENTITY_STATUS_BACK_OFFICE_PROCESSING.equals(entityStatusCd) && onTablet.get() == 'Y') {
            // broadcast to all other devices
            DataSourceDB.executeUpdate(connection, "update device_entity_sync_map set delivered = 'Y' " +
                            "where entity_id = ? and on_tablet = 'N' and delivered = 'N' and device_id <> ? ",
                    entity.getSingleId().get(), deviceId.get());

            DataSourceDB.executeUpdate(connection, "update device_entity_sync_map set delivered = 'N', on_tablet = 'F' " +
                    "where entity_id = ? and on_tablet = 'Y' and device_id <> ? ", entity.getSingleId().get(), deviceId.get());
        }
        onTablet.setSerial(ToolkitConstants.ENTITY_STATUS_TABLET_PROCESSING.equals(entityStatusCd) ? "Y" : "N");
    }

    /**
     *
     */
    public void processOnOnTabletMultiCast(Connection connection, EntityDB entity) throws SQLException {
        if (entity instanceof SyncableEntity) {
            processOnOnTabletMultiCast(connection, entity, ((SyncableEntity) entity).getBaseSharedSyncEntity().entityStatusCd.get());
        } else {
            onTablet.setSerial("N");
        }
    }

    public void process(DataSource dataSource, DeviceEntityMapReq entityMapReq) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            this.delivered.setSerial(entityMapReq.getDelivered());
            this.errorMessage.set(entityMapReq.getError());
            this.deliveredTime.set(new Timestamp(System.currentTimeMillis()));
            if (entityMapReq.getDelivered().equals("Y") && !"R".equals(entityMapReq.getOnTablet())) {
                processOnOnTabletMultiCast(connection, getRefEntity(connection));
            }
            DataSourceDB.set(connection, this);
        }
    }

    /**
     * retrieve record and convert to json
     *
     * @param connection
     * @param map
     * @return
     * @throws Exception
     */
    private static void add(Map<String, List<JSONObject>> jsonMap, Connection connection, DeviceEntitySyncMapEntity map,
                            int index) throws Exception {

        EntityDB entity = DataSourceDB.loadFromId(connection, (EntityDB) Class.forName(map.entityClass.get()).newInstance(), map.entityId.get());
        if (entity != null) {
            Object[] jsonStruct = entity.getPathToParentAsJson(connection, index);

            if (!jsonMap.containsKey(jsonStruct[0])) {
                jsonMap.put((String) jsonStruct[0], new ArrayList<JSONObject>());
            }

            jsonMap.get(jsonStruct[0]).add((JSONObject) jsonStruct[1]);

        } else {
            TAG.warning(String.format("Could not locate entity %s record %s", map.entityClass.get(), map.entityId.get()));
        }
    }

    /**
     * retrieve all the data to be published to the device refreenced from the sync map
     *
     * @param dataSource
     * @param device
     * @return
     * @throws Exception
     */
    public static JSONObject getSyncDataForDeviceOldVersion(DataSource dataSource, DeviceEntity device) throws Exception {
        JSONObject jsonData = new JSONObject();
        try (DataSourceDB<DeviceEntitySyncMapEntity> ds = new DataSourceDB<>(DeviceEntitySyncMapEntity.class)) {
            List<RetrieveDeviceEntityMapReq> records = new ArrayList<RetrieveDeviceEntityMapReq>();

            LinkedHashMap<String, List<JSONObject>> jsonMap = new LinkedHashMap<>();
            try (Connection connection = dataSource.getConnection()) {

                int index = 0;
                for (DeviceEntitySyncMapEntity syncMapEntity :
                        ds.getAll(connection, true,
                                DriverFactory.getDriver().limitSql("select * from device_entity_sync_map where device_id = ? and " +
                                        "delivered = 'N' order by device_entity_sync_map_id asc", 50), device.deviceId.get())) {

                    add(jsonMap, connection, syncMapEntity, index);
                    records.add(new RetrieveDeviceEntityMapReq(
                            syncMapEntity.deviceEntitySyncMapId.get(),
                            syncMapEntity.deviceId.getAsString(),
                            syncMapEntity.entityId.getAsString(),
                            syncMapEntity.onTablet.getAsString(),
                            index));

                    index++;
                }
            }

            if (records.size() > 0) {
                for (String key : jsonMap.keySet()) {
                    jsonData.put(key, new JSONArray(jsonMap.get(key)));
                }

                jsonData.put("deviceEntitySyncMap", records);
                return jsonData;
            }

            return null;
        }
    }

    public static JSONObject getSyncDataForDevice(DataSource dataSource, DeviceEntity device) throws Exception {
        return getSyncDataForDevice(dataSource, device, 0);
    }

    /**
     * retrieve all the data to be published to the device refreenced from the sync map
     *
     * @param dataSource
     * @param device
     * @param version    - temp hack to introduce new version
     * @return
     * @throws Exception
     */
    public static JSONObject getSyncDataForDevice(DataSource dataSource, DeviceEntity device, int version) throws Exception {
        JSONObject jsonData = new JSONObject();
        try (DataSourceDB<DeviceEntitySyncMapEntity> ds = new DataSourceDB<>(DeviceEntitySyncMapEntity.class)) {
            LinkedHashMap<String, List<JSONObject>> jsonMap = new LinkedHashMap<>();


            try (Connection connection = dataSource.getConnection()) {

                int index = 0;
                for (DeviceEntitySyncMapEntity syncMapEntity :
                        ds.getAll(connection, true,
                                DriverFactory.getDriver().limitSql(
                                        "select * from device_entity_sync_map " +
                                                " where device_id = ? and delivered in ('R','N')" +
                                                " order by device_entity_sync_map_id asc", 3), device.deviceId.get())) {

                    try {
                        RetrieveDeviceEntityMapReq retrieveDeviceEntityMapReq =
                                new RetrieveDeviceEntityMapReq(
                                        syncMapEntity.deviceEntitySyncMapId.get(),
                                        syncMapEntity.deviceId.getAsString(),
                                        syncMapEntity.entityId.getAsString(),
                                        syncMapEntity.onTablet.getAsString(),
                                        index);

                        if (syncMapEntity.delivered.get().equals('N')) {
                            if (version == 0) {
                                add(jsonMap, connection, syncMapEntity, index);
                            } else {
                                EntityDB entity = DataSourceDB.loadFromId(connection, (EntityDB) Class.forName(syncMapEntity.entityClass.get()).newInstance(),
                                        syncMapEntity.entityId.get());
                                retrieveDeviceEntityMapReq.setEntity(entity.exportAsJson(connection).toString());
                            }
                        }
                        if (syncMapEntity.delivered.get().equals('R')) {
                            retrieveDeviceEntityMapReq.setEntity(syncMapEntity.entityClass.getAsString().substring(
                                    syncMapEntity.entityClass.getAsString().lastIndexOf('.') + 1));
                            retrieveDeviceEntityMapReq.setRecall(true);
                        } else {
                            retrieveDeviceEntityMapReq.setRecall(false);
                        }

                        index++;

                        if (version == 0) {
                            JSONObject j = new JSONObject(retrieveDeviceEntityMapReq);
                            for (String key : jsonMap.keySet()) {
                                j.put(key, jsonMap.get(key));
                            }
                            jsonData.accumulate("deviceEntitySyncMap", j);
                        } else {
                            jsonData.accumulate("deviceEntitySyncMap", new JSONObject(retrieveDeviceEntityMapReq));
                        }
                    } catch (Exception ex) {
                        syncMapEntity.errorMessage.set(ObjectUtils.convertStackTraceToString(ex, 200));
                        syncMapEntity.delivered.set('E');
                        DataSourceDB.set(dataSource, syncMapEntity);
                        throw ex;
                    }
                }
            }
        }

        return jsonData.length() == 0 ? null : jsonData;
    }

    public static void pushData(DataSource dataSource, List<EntityDB> entities, List<DeviceEntity> devices) {
        devices.stream().forEach(d -> {
            entities.stream().forEach(e -> DataSourceDB.set(dataSource, new DeviceEntitySyncMapEntity(e, d)));
        });
    }
}
