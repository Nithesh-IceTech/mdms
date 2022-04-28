package za.co.spsi.toolkit.crud.audit.gui;

import za.co.spsi.toolkit.db.drivers.Driver;
import za.co.spsi.toolkit.db.drivers.DriverFactory;

import javax.inject.Inject;
import javax.sql.DataSource;

public abstract class AuditConfig {

    public static DataSource DATA_SOURCE;
    public static Driver DRIVER;

    @Inject
    protected void setDs() {
        AuditConfig.DATA_SOURCE = getDataSource();
        DriverFactory.setDataSource(getDataSource());
        AuditConfig.DRIVER = DriverFactory.getDriver();
    }

    public static String getSql(String where, String orderBy) {
        return String.format("select audit_log.audit_log_id,audit_type,user_id,create_time," +
                        "   (CASE audit_type WHEN 1 THEN %s\n" +
                        "   else '' end) AS field_name, \n" +
                        "   CASE audit_type WHEN 0 THEN 'Create' " +
                        "       WHEN 1 THEN 'Update' " +
                        "       ELSE 'Delete' END as auditType from audit_log,audit_log_detail %s %s",
                DriverFactory.getDriver().aggregateList("field_name", ","),
                where,
                orderBy);
    }

    public static String getSqlFor(String entityName, String idFieldName) {
        return AuditConfig.getSql(String.format(" where " +
                        "upper(entity_name) = upper('%s') and entity_id = %s.%s and " +
                        "audit_log_detail.audit_log_id = audit_log.audit_log_id ", entityName, entityName, idFieldName),
                " group by audit_log.audit_log_id,audit_type,user_id,create_time " +
                        "order by create_time desc");
    }

    public static String getSqlFor(String entityName) {
        return AuditConfig.getSqlFor(entityName, entityName + "_id");
    }

    public abstract DataSource getDataSource();

    public Driver getDriver() {
        return AuditConfig.DRIVER;
    }

}
