package za.co.spsi.toolkit.crud.controller;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import za.co.spsi.toolkit.crud.entity.ErrorLogEntity;
import za.co.spsi.toolkit.dao.DeviceErrors;
import za.co.spsi.toolkit.db.DataSourceDB;

import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.sql.Connection;

public interface ErrorLogController {

    DataSource getDataSource();

    @Path(value = "logErrors")
    @POST
    @Consumes("application/json")
    @SneakyThrows
    public default void postError(DeviceErrors deviceErrors) {

        try (Connection connection = getDataSource().getConnection()) {

            final ErrorLogEntity errorLogEntity = new ErrorLogEntity();
            errorLogEntity.timestamp.set(deviceErrors.getTimestamp());
            errorLogEntity.imei.set(deviceErrors.getImei());
            errorLogEntity.apkVersion.set(deviceErrors.getApkVersion());
            errorLogEntity.error.set(deviceErrors.getError());
            errorLogEntity.msg.set(deviceErrors.msg);
            DataSourceDB.createOrUpdate(connection, errorLogEntity);
        }
    }

    @Slf4j
    final class LogHolder {
    }
}