package za.co.spsi.toolkit.crud.audit.gui;

import com.vaadin.ui.renderers.HtmlRenderer;
import za.co.spsi.locale.annotation.ToolkitLocaleId;
import za.co.spsi.toolkit.ano.Qualifier;
import za.co.spsi.toolkit.ano.Role;
import za.co.spsi.toolkit.crud.gui.*;
import za.co.spsi.toolkit.crud.gui.ano.EntityRef;
import za.co.spsi.toolkit.crud.gui.ano.UIGroup;
import za.co.spsi.toolkit.crud.gui.fields.ComboBoxField;
import za.co.spsi.toolkit.crud.gui.fields.MLCSLookupField;
import za.co.spsi.toolkit.crud.gui.fields.VirtualField;
import za.co.spsi.toolkit.crud.gui.query.MLCSLookupFieldConverter;
import za.co.spsi.toolkit.crud.gui.render.GridColumnRenderer;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.audit.AuditDetailEntity;
import za.co.spsi.toolkit.db.audit.AuditEntity;
import za.co.spsi.toolkit.entity.Field;

@Qualifier(roles = {@Role(value = "Supervisor",write = false,create = false)})
public class AuditLayout extends Layout {

    @EntityRef(main = true)
    private AuditEntity audit = new AuditEntity();

    public VirtualField fieldName = new VirtualField(ToolkitLocaleId.AUDIT_FIELD, "field_name",this);

    @UIGroup(column = 0)
    public Group detail = new Group(ToolkitLocaleId.AUDIT_CAPTION, this);

    public ComboBoxField<String> auditType = new ComboBoxField(audit.auditType, ToolkitLocaleId.AUDIT_TYPE,
            new String[]{ToolkitLocaleId.AUDIT_TYPE_CREATE, ToolkitLocaleId.AUDIT_TYPE_UPDATE, ToolkitLocaleId.AUDIT_TYPE_DELETE},
            new String[]{"0", "1", "2"}, this);

    public LField userId = new LField(audit.userId, ToolkitLocaleId.AUDIT_UID, this);

    public LField createTime = new LField(audit.createTime, ToolkitLocaleId.AUDIT_TIME, this);

    public Group nameGroup = new Group("", this, auditType, userId, createTime, fieldName).setNameGroup();

    public Pane detailPane = new Pane(ToolkitLocaleId.AUDIT_DETAIL_CAPTION, this, detail);

    public Pane surveyPane = new Pane(ToolkitLocaleId.AUDIT_DETAIL_CAPTION,
            "select * from audit_log_detail where audit_log.audit_log_id = audit_log_detail.audit_log_id",
            AuditDetailLayout.class, new Permission(0), this);

    public AuditLayout() {
        super(ToolkitLocaleId.AUDIT_CAPTION);
        getPermission().setPermissionFlag(0);
    }

    public static String getSql(String where,String orderBy) {
        return String.format("select audit_log.audit_log_id,audit_type,user_id,create_time," +
                "   (CASE audit_type WHEN 1 THEN" +
                "   (listagg(field_name, ',') WITHIN GROUP ( ORDER BY field_name))\n" +
                "   else '' end) AS field_name, \n" +
                "   CASE audit_type WHEN 0 THEN 'Create' " +
                "       WHEN 1 THEN 'Update' " +
                "       ELSE 'Delete' END as auditType from audit_log,audit_log_detail %s %s", where, orderBy);
    }


    public static String getSqlFor(String entityName, String idFieldName) {
        return getSql(String.format(" where " +
                        "upper(entity_name) = upper('%s') and entity_id = %s.%s and " +
                        "audit_log_detail.audit_log_id = audit_log.audit_log_id ", entityName, entityName, idFieldName),
                " group by audit_log.audit_log_id,audit_type,user_id,create_time " +
                        "order by create_time desc");
    }

    public static String getSqlFor(String entityName) {
        return getSqlFor(entityName, entityName + "_id");
    }

    @Override
    public boolean shouldAddFilterOnPaneList() {
        return true;
    }

    public String getMainSql() {
        return AuditConfig.getSql("", "");
    }

    public static class ReviewDetailView extends Layout {

        @EntityRef(main = true)
        private AuditDetailEntity auditDetail = new AuditDetailEntity();

        @EntityRef()
        private AuditEntity audit = new AuditEntity();

        @UIGroup(column = 0)
        public Group detail = new Group(ToolkitLocaleId.AUDIT_CAPTION, this);

        public ComboBoxField<String> auditType = new ComboBoxField(audit.auditType, ToolkitLocaleId.AUDIT_TYPE,
                new String[]{ToolkitLocaleId.AUDIT_TYPE_CREATE, ToolkitLocaleId.AUDIT_TYPE_UPDATE, ToolkitLocaleId.AUDIT_TYPE_DELETE},
                new String[]{"0", "1", "2"}, this);

        public LField userId = new LField(audit.userId, ToolkitLocaleId.AUDIT_UID, this);
        public ComboBoxField<String> supervisor = new ComboBoxField(audit.supervisor, ToolkitLocaleId.AUDIT_SUPERVISOR,
                new String[]{ToolkitLocaleId.YES, ToolkitLocaleId.NO}, new String[]{"0", "1"}, this);

        public LField createTime = new LField(audit.createTime, ToolkitLocaleId.AUDIT_TIME, this);

        public LField oldValue = new LField(auditDetail.oldValue, ToolkitLocaleId.AUDIT_OLD_VALUE, this);
        public LField newValue = new LField(auditDetail.newValue, ToolkitLocaleId.AUDIT_NEW_VALUE, this);


        public Group nameGroup = new Group("", this, auditType, userId, supervisor, createTime, oldValue, newValue).setNameGroup();

        public Pane detailPane = new Pane(ToolkitLocaleId.AUDIT_DETAIL_CAPTION, this, detail);

        private Field auditField;


        public void setAuditField(LField auditField) {
            this.auditField = auditField.getField();
            if (auditField instanceof MLCSLookupField) {
                GridColumnRenderer gridColumnRenderer = column ->
                        column.setRenderer(new HtmlRenderer(), new MLCSLookupFieldConverter((MLCSLookupField) auditField));
                oldValue.setGridColumnRenderer(gridColumnRenderer);
                newValue.setGridColumnRenderer(gridColumnRenderer);
            }

        }

        public String getMainSql() {
            return ("select * from AUDIT_LOG,AUDIT_LOG_DETAIL " +
                    "where" +
                    "    AUDIT_LOG.AUDIT_LOG_ID = AUDIT_LOG_DETAIL.AUDIT_LOG_ID AND " +
                    "    AUDIT_LOG.ENTITY_ID = '_ENTITY_ID_' AND " +
                    "    AUDIT_LOG.ENTITY_NAME = '_ENTITY_NAME_' and  " +
                    "    AUDIT_LOG_DETAIL.FIELD_NAME = '_FIELD_NAME_' " +
                    "order by CREATE_TIME desc").replace("_ENTITY_ID_",
                    ((EntityDB) auditField.getParentEntity()).getSingleId().getSerial())
                    .replace("_ENTITY_NAME_", (auditField.getEntity()).getName())
                    .replace("_FIELD_NAME_", auditField.getName());
        }
    }


}
