package za.co.spsi.mdms.util;

import za.co.spsi.mdms.common.db.MeterReadingEntity;
import za.co.spsi.mdms.common.services.ReadingGenericGapIdentifier;
import za.co.spsi.mdms.common.services.ReadingGenericGapProcessor;
import za.co.spsi.mdms.generic.meter.db.DbToDbMapMeterEntity;
import za.co.spsi.mdms.generic.meter.db.DbToDbMappingDetailEntity;
import za.co.spsi.mdms.generic.meter.db.DbToDbMappingEntity;
import za.co.spsi.mdms.generic.meter.db.GenericMeterEntity;
import za.co.spsi.toolkit.db.DSDB;
import za.co.spsi.toolkit.util.StringUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Logger;

import static za.co.spsi.mdms.util.MeterFilterService.getMax;

public class DBToDBUtil {

    public static final Logger TAG = Logger.getLogger(DBToDBUtil.class.getName());
    private static SimpleDateFormat dayFormat = new SimpleDateFormat(MeterReadingEntity.ENTRY_DAY_FORMAT);

    public static Timestamp persistMapping(ReadingGenericGapIdentifier gapIdentifier, String sql, DataSource remoteDataSource, DataSource localDs,
                                           DbToDbMappingEntity db2DbMappingEntity,
                                           DbToDbMappingDetailEntity dbToDbMappingDetailEntity,
                                           PrepaidMeterFilterService prepaidFilter) throws SQLException {
        GenericMeterEntity genericMeterEntity = null;
        MeterReadingEntity meterReadingEntity = null;
        Timestamp lastSyncTime = null;

        TAG.info("Remote query : " + sql);

        try (Connection remoteConnection = remoteDataSource.getConnection()) {

            try (Connection localConnection = localDs.getConnection()) {

                localConnection.setAutoCommit(false);

                try (Statement smt = remoteConnection.createStatement()) {

                    TAG.info("Doing remote query");

                    ResultSet rs = smt.executeQuery(sql);

                    TAG.info("Remote query executed.");

                    while (rs.next()) {

                        genericMeterEntity = new GenericMeterEntity();
                        initMeterFromRemoteDb(rs, genericMeterEntity, db2DbMappingEntity, dbToDbMappingDetailEntity);

                        genericMeterEntity.live.set(db2DbMappingEntity.live.getNonNull());

                        // Test if meter exist save if it does not
                        GenericMeterEntity tmpGenericMeterEntity =
                                DSDB.getFromSet(localConnection,
                                        new GenericMeterEntity().meterSerialN.set(genericMeterEntity.meterSerialN.get()));

                        if (tmpGenericMeterEntity == null) {
                            genericMeterEntity.meterId.set(genericMeterEntity.genericMeterId.get());
                            genericMeterEntity = DSDB.set(localConnection, genericMeterEntity);
                            TAG.info("ELR : " + genericMeterEntity.toString());
                        } else {
                            genericMeterEntity.copyStrict(tmpGenericMeterEntity);
                            genericMeterEntity.meterId.set(genericMeterEntity.genericMeterId.get());
                            DSDB.setUpdate(localConnection, genericMeterEntity);
                        }

                        // Persist meter Mapping
                        DbToDbMapMeterEntity dbToDbMapMeterEntity = new DbToDbMapMeterEntity();
                        dbToDbMapMeterEntity.meterId.set(genericMeterEntity.genericMeterId.get());
                        dbToDbMapMeterEntity.dbToDbMapping.set(db2DbMappingEntity.dbToDbMappingId.get());
                        dbToDbMapMeterEntity.dbToDbMappingDetailId.set(dbToDbMappingDetailEntity.dbToDbMappingDetailId.get());

                        // Test if meter exist save if it does not
                        DbToDbMapMeterEntity tmpDbToDbMapMeterEntity = DSDB.getFromSet(localConnection, dbToDbMapMeterEntity);

                        if (tmpDbToDbMapMeterEntity == null) {
                            DSDB.set(localConnection, dbToDbMapMeterEntity);
                        }

                        // Persist meterReading
                        meterReadingEntity = initMeterReadingFromRemoteDb(rs, genericMeterEntity, db2DbMappingEntity);

                        Timestamp mdmsEntryTime;
                        Timestamp genericEntryTime;

                        genericEntryTime  = meterReadingEntity.adjustTimestamp( genericMeterEntity.isWater() ? 60 : 30 );
                        meterReadingEntity.entryTime.set(genericEntryTime);
                        gapIdentifier.identifyMeterReadingGaps(localConnection,genericMeterEntity,meterReadingEntity,genericEntryTime,genericMeterEntity.isWater());

                        // Save prepaid
                        meterReadingEntity.prepaidMeter.set(
                                meterReadingEntity.prepaidMeter.get() == null ?
                                        prepaidFilter.isPrepaid(genericMeterEntity.meterSerialN.get()) : meterReadingEntity.prepaidMeter.get());

                        LocalDateTime lastSixHours = LocalDateTime.now().minusHours(6);

                        if( meterReadingEntity.entryTime.get().getTime() >= Timestamp.valueOf(lastSixHours).getTime() ) {
                            lastSyncTime = meterReadingEntity.entryTime.get();
                        } else {
                            lastSyncTime = Timestamp.valueOf(lastSixHours);
                        }

                        MeterReadingEntity dBMeterReadingEntity = new MeterReadingEntity();
                        dBMeterReadingEntity.genericMeterId.set(genericMeterEntity.genericMeterId.get());
                        dBMeterReadingEntity.entryTime.set(meterReadingEntity.entryTime.get());
                        dBMeterReadingEntity = DSDB.getFromSet(localConnection, dBMeterReadingEntity);

                        if (dBMeterReadingEntity == null) {
                            DSDB.set(localConnection, meterReadingEntity);
                            genericMeterEntity.lastCommsD.set(meterReadingEntity.entryTime.get());
                            genericMeterEntity.maxEntryTime.set(getMax(genericMeterEntity.maxEntryTime.get(),meterReadingEntity.entryTime.get()) );
                        } else {
                            // Merge Db instance and new instance
                            mergeMeterReadingEntity(dBMeterReadingEntity, meterReadingEntity);
                            DSDB.set(localConnection, dBMeterReadingEntity);
                            genericMeterEntity.lastCommsD.set(dBMeterReadingEntity.entryTime.get());
                            genericMeterEntity.maxEntryTime.set(getMax(genericMeterEntity.maxEntryTime.get(),meterReadingEntity.entryTime.get()) );
                        }

                        DSDB.setUpdate(localConnection, genericMeterEntity);

                    }

                    localConnection.commit();
                    return lastSyncTime;

                } catch (Exception e) {
                    localConnection.rollback();
                    TAG.severe(e.getMessage());
                    if (genericMeterEntity != null) {
                        TAG.severe(genericMeterEntity.toString());
                    }
                    if (meterReadingEntity != null) {
                        TAG.severe(meterReadingEntity.toString());
                    }
                    throw e;
                }
            }
        }
    }

    private static void mergeMeterReadingEntity(MeterReadingEntity destMeterReadingEntity,
                                                MeterReadingEntity sourceMeterReadingEntity) {

        if (sourceMeterReadingEntity.totalKwhP.get() != null) {
            destMeterReadingEntity.totalKwhP.set(sourceMeterReadingEntity.totalKwhP.get());
        }

        if (sourceMeterReadingEntity.totalKwhN.get() != null) {
            destMeterReadingEntity.totalKwhN.set(sourceMeterReadingEntity.totalKwhN.get());
        }

        if (sourceMeterReadingEntity.totalKVarP.get() != null) {
            destMeterReadingEntity.totalKVarP.set(sourceMeterReadingEntity.totalKVarP.get());
        }

        if (sourceMeterReadingEntity.totalKVarN.get() != null) {
            destMeterReadingEntity.totalKVarN.set(sourceMeterReadingEntity.totalKVarN.get());
        }

        if (sourceMeterReadingEntity.rmsL1V.get() != null) {
            destMeterReadingEntity.rmsL1V.set(sourceMeterReadingEntity.rmsL1V.get());
        }

        if (sourceMeterReadingEntity.rmsL2V.get() != null) {
            destMeterReadingEntity.rmsL2V.set(sourceMeterReadingEntity.rmsL2V.get());
        }

        if (sourceMeterReadingEntity.rmsL3V.get() != null) {
            destMeterReadingEntity.rmsL3V.set(sourceMeterReadingEntity.rmsL3V.get());
        }

        if (sourceMeterReadingEntity.rmsL1C.get() != null) {
            destMeterReadingEntity.rmsL1C.set(sourceMeterReadingEntity.rmsL1C.get());
        }

        if (sourceMeterReadingEntity.rmsL2C.get() != null) {
            destMeterReadingEntity.rmsL2C.set(sourceMeterReadingEntity.rmsL2C.get());
        }

        if (sourceMeterReadingEntity.rmsL2C.get() != null) {
            destMeterReadingEntity.rmsL3C.set(sourceMeterReadingEntity.rmsL2C.get());
        }

        if (sourceMeterReadingEntity.volume1.get() != null) {
            destMeterReadingEntity.volume1.set(sourceMeterReadingEntity.volume1.get());
        }
    }

    public static void initMeterFromRemoteDb(ResultSet rs, GenericMeterEntity genericMeterEntity,
                                             DbToDbMappingEntity db2DbMappingEntity,
                                             DbToDbMappingDetailEntity dbToDbMappingDetailEntity) {
        try {
            genericMeterEntity.meterId.set(rs.getString("meterId"));
        } catch (SQLException e) {
        }

        try {
            genericMeterEntity.meterSerialN.set(db2DbMappingEntity.vendorPrefix.get() + rs.getString("meterSerialN"));
        } catch (SQLException e) {
        }

        try {
            genericMeterEntity.meterReadingId.set(rs.getString("meterReadingId"));
        } catch (SQLException e) {
        }

        try {
            genericMeterEntity.meterManId.set(rs.getString("meterManId"));
        } catch (SQLException e) {
        }

        genericMeterEntity.meterType.set(db2DbMappingEntity.meterType.get());
    }

    public static MeterReadingEntity initMeterReadingFromRemoteDb(ResultSet rs,
                                                                  GenericMeterEntity genericMeterEntity,
                                                                  DbToDbMappingEntity db2DbMappingEntity) {

        MeterReadingEntity meterReadingEntity = new MeterReadingEntity();
        meterReadingEntity.genericMeterId.set(genericMeterEntity.genericMeterId.get());
        try {

            // Handle timezone
            Timestamp timestamp = rs.getTimestamp("timestamp");
            timestamp.setTime(timestamp.getTime() + (Integer.parseInt(db2DbMappingEntity.timeZoneOffsetToUtc.getAsString()) *
                    60 * 60 * 1000));
            meterReadingEntity.entryTime.set(timestamp);

        } catch (SQLException e) {
        }

        try {
            meterReadingEntity.totalKwhP.set(rs.getDouble("importKwh"));
        } catch (SQLException e) {
        }

        try {
            meterReadingEntity.totalKwhN.set(rs.getDouble("exportKwh"));
        } catch (SQLException e) {
        }

        try {
            meterReadingEntity.totalKVarP.set(rs.getDouble("inductiveKvarh"));
        } catch (SQLException e) {
        }

        try {
            meterReadingEntity.totalKVarN.set(rs.getDouble("capacitiveKvarh"));
        } catch (SQLException e) {
        }

        try {
            meterReadingEntity.rmsL1V.set(rs.getDouble("voltageL1"));
        } catch (SQLException e) {
        }

        try {
            meterReadingEntity.rmsL2V.set(rs.getDouble("voltageL2"));
        } catch (SQLException e) {
        }

        try {
            meterReadingEntity.rmsL3V.set(rs.getDouble("voltageL3"));
        } catch (SQLException e) {
        }

        try {
            meterReadingEntity.rmsL1C.set(rs.getDouble("currentL1"));
        } catch (SQLException e) {
        }

        try {
            meterReadingEntity.rmsL2C.set(rs.getDouble("currentL2"));
        } catch (SQLException e) {
        }

        try {
            meterReadingEntity.rmsL3C.set(rs.getDouble("currentL3"));
        } catch (SQLException e) {
        }

        try {
            meterReadingEntity.volume1.set(rs.getDouble("volumeWater"));
        } catch (SQLException e) {
            try {
                meterReadingEntity.volume1.set(rs.getDouble("volumeGas"));
            } catch (SQLException e1) {
            }
        }

        meterReadingEntity.entryDay.set(Integer.parseInt(dayFormat.format(meterReadingEntity.entryTime.getLocal())));
        return meterReadingEntity;
    }

    public static String buildMappingSql(DbToDbMappingEntity db2DbMappingEntity,
                                         DbToDbMappingDetailEntity dbToDbMappingDetailEntity,
                                         boolean getLatest) {

        // Build columns
        StringBuilder columns = new StringBuilder();
        if (!StringUtils.isEmpty(db2DbMappingEntity.meterId.getNonNull())) {
            columns = columns.append(db2DbMappingEntity.meterId.getSerial()).
                    append(" as ").
                    append(db2DbMappingEntity.meterId.getColumnName()).append(",");
        }

        if (!StringUtils.isEmpty(db2DbMappingEntity.meterSerialN.getNonNull())) {
            columns = columns.append(db2DbMappingEntity.meterSerialN.getSerial()).
                    append(" as ").
                    append(db2DbMappingEntity.meterSerialN.getColumnName()).append(",");
        }

        if (!StringUtils.isEmpty(db2DbMappingEntity.meterReadingId.getNonNull())) {
            columns = columns.append(db2DbMappingEntity.meterReadingId.getSerial()).
                    append(" as ").
                    append(db2DbMappingEntity.meterReadingId.getColumnName()).append(",");
        }

        if (!StringUtils.isEmpty(db2DbMappingEntity.timestamp.getNonNull())) {
            columns = columns.append(db2DbMappingEntity.timestamp.getSerial()).
                    append(" as ").
                    append(db2DbMappingEntity.timestamp.getColumnName()).append(",");
        }

        if (!StringUtils.isEmpty(db2DbMappingEntity.meterManId.getNonNull())) {
            columns = columns.append(db2DbMappingEntity.meterManId.getSerial()).
                    append(" as ").
                    append(db2DbMappingEntity.meterManId.getSerial()).append(",");
        }

        if (!StringUtils.isEmpty(dbToDbMappingDetailEntity.importKWH.getNonNull())) {
            columns = columns.append(dbToDbMappingDetailEntity.importKWH.getSerial()).
                    append(" * ").append(dbToDbMappingDetailEntity.importKWHScalingFactor.getSerial()).
                    append(" as ").
                    append(dbToDbMappingDetailEntity.importKWH.getColumnName()).append(",");
        }

        if (!StringUtils.isEmpty(dbToDbMappingDetailEntity.exportKWH.getNonNull())) {
            columns = columns.append(dbToDbMappingDetailEntity.exportKWH.getSerial()).
                    append(" * ").append(dbToDbMappingDetailEntity.exportKWHScalingFactor.getSerial()).
                    append(" as ").
                    append(dbToDbMappingDetailEntity.exportKWH.getColumnName()).append(",");
        }

        if (!StringUtils.isEmpty(dbToDbMappingDetailEntity.inductiveKVARH.getNonNull())) {
            columns = columns.append(dbToDbMappingDetailEntity.inductiveKVARH.getSerial()).
                    append(" * ").append(dbToDbMappingDetailEntity.inductiveKVARHScalingFactor.getSerial()).
                    append(" as ").
                    append(dbToDbMappingDetailEntity.inductiveKVARH.getColumnName()).append(",");
        }

        if (!StringUtils.isEmpty(dbToDbMappingDetailEntity.capacitiveKVARH.getNonNull())) {
            columns = columns.append(dbToDbMappingDetailEntity.capacitiveKVARH.getSerial()).
                    append(" * ").append(dbToDbMappingDetailEntity.capacitiveKVARHScalingFactor.getSerial()).
                    append(" as ").
                    append(dbToDbMappingDetailEntity.capacitiveKVARH.getColumnName()).append(",");
        }

        if (!StringUtils.isEmpty(dbToDbMappingDetailEntity.voltageL1.getNonNull())) {
            columns = columns.append(dbToDbMappingDetailEntity.voltageL1.getSerial()).
                    append(" * ").append(dbToDbMappingDetailEntity.voltageL1ScalingFactor.getSerial()).
                    append(" as ").
                    append(dbToDbMappingDetailEntity.voltageL1.getColumnName()).append(",");
        }

        if (!StringUtils.isEmpty(dbToDbMappingDetailEntity.voltageL2.getNonNull())) {
            columns = columns.append(dbToDbMappingDetailEntity.voltageL2.getSerial()).
                    append(" * ").append(dbToDbMappingDetailEntity.voltageL2ScalingFactor.getSerial()).
                    append(" as ").
                    append(dbToDbMappingDetailEntity.voltageL2.getColumnName()).append(",");
        }

        if (!StringUtils.isEmpty(dbToDbMappingDetailEntity.voltageL3.getNonNull())) {
            columns = columns.append(dbToDbMappingDetailEntity.voltageL3.getSerial()).
                    append(" * ").append(dbToDbMappingDetailEntity.voltageL3ScalingFactor.getSerial()).
                    append(" as ").
                    append(dbToDbMappingDetailEntity.voltageL3.getColumnName()).append(",");
        }

        if (!StringUtils.isEmpty(dbToDbMappingDetailEntity.currentL1.getNonNull())) {
            columns = columns.append(dbToDbMappingDetailEntity.currentL1.getSerial()).
                    append(" * ").append(dbToDbMappingDetailEntity.currentL1ScalingFactor.getSerial()).
                    append(" as ").
                    append(dbToDbMappingDetailEntity.currentL1.getColumnName()).append(",");
        }

        if (!StringUtils.isEmpty(dbToDbMappingDetailEntity.currentL2.getNonNull())) {
            columns = columns.append(dbToDbMappingDetailEntity.currentL2.getSerial()).
                    append(" * ").append(dbToDbMappingDetailEntity.currentL2ScalingFactor.getSerial()).
                    append(" as ").
                    append(dbToDbMappingDetailEntity.currentL2.getColumnName()).append(",");
        }

        if (!StringUtils.isEmpty(dbToDbMappingDetailEntity.currentL3.getNonNull())) {
            columns = columns.append(dbToDbMappingDetailEntity.currentL3.getSerial()).
                    append(" * ").append(dbToDbMappingDetailEntity.currentL3ScalingFactor.getSerial()).
                    append(" as ").
                    append(dbToDbMappingDetailEntity.currentL3.getColumnName()).append(",");
        }

        if (!StringUtils.isEmpty(dbToDbMappingDetailEntity.volumeWater.getNonNull())) {
            columns = columns.append(dbToDbMappingDetailEntity.volumeWater.getSerial()).
                    append(" * ").append(dbToDbMappingDetailEntity.volumeWaterScalingFactor.getSerial()).
                    append(" as ").
                    append(dbToDbMappingDetailEntity.volumeWater.getColumnName()).append(",");
        }

        if (!StringUtils.isEmpty(dbToDbMappingDetailEntity.volumeGas.getNonNull())) {
            columns = columns.append(dbToDbMappingDetailEntity.volumeGas.getSerial()).
                    append(" * ").append(dbToDbMappingDetailEntity.volumeGasScalingFactor.getSerial()).
                    append(" as ").
                    append(dbToDbMappingDetailEntity.volumeGas.getColumnName()).append(",");
        }

        int index = columns.lastIndexOf(",");
        if (index >= 0) {
            columns = columns.replace(index, index + 1, "");
        }

        String mergeFrom = DBUtil.trimSql(db2DbMappingEntity.sqlFrom.get()) + "," +
                DBUtil.trimSql(dbToDbMappingDetailEntity.sqlFrom.get());

        String[] from = mergeFrom.split(",");
        ArrayList<String> fromList = new ArrayList<>();
        for (String item : from) {
            if (!fromList.contains(item)) {
                fromList.add(item);
            }
        }

        mergeFrom = " from " + String.join(",", fromList) +
                (dbToDbMappingDetailEntity.sqlWhere.get() != null ? " where " +
                        dbToDbMappingDetailEntity.sqlWhere.get() : " ");

        return DBUtil.returnFirstRows(db2DbMappingEntity.driver.get(),
                "select " + columns.toString() +
                        (!getLatest ?
                                appendLastSyncTime(mergeFrom, db2DbMappingEntity.timestamp.getSerial(), dbToDbMappingDetailEntity.lastSyncTime.get()) :
                                mergeFrom)

                        + " order by " + db2DbMappingEntity.timestamp.getSerial() +
                        (!getLatest ? " asc " : " desc"), 2000);

    }

    private static String appendLastSyncTime(String sql, String timestampField, Timestamp lastSync) {

        if (lastSync == null) {
            Timestamp now = new Timestamp(System.currentTimeMillis());
            lastSync = new Timestamp(now.toInstant().minusSeconds(3600 * 6).getEpochSecond() * 1000);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime lastSyncDT = lastSync.toLocalDateTime();

        if (!sql.toLowerCase().contains("where")) {
            return sql + " where " + timestampField + " >= '" + formatter.format(lastSyncDT) + "'";
        } else {
            return sql + " and " + timestampField + " >= '" + formatter.format(lastSyncDT) + "'";
        }
    }

}
