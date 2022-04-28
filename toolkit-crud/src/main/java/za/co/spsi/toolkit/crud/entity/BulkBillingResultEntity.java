/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.spsi.toolkit.crud.entity;

import za.co.spsi.toolkit.crud.db.audit.AuditEntityDB;
import za.co.spsi.toolkit.crud.gui.render.ToolkitCrudConstants;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.entity.Field;

import java.sql.Timestamp;

@Table(version = 8)
public class BulkBillingResultEntity extends AuditEntityDB {

    @Id(uuid = true)
    @Column(name = "BULK_BILLING_RESULT_ID",size = 50, notNull = true)
    public Field<String> dataExportId = new Field<>(this);

    @Column(name = "BULK_BATCH_ID")
    public Field<String> bulkBatchId= new Field<>(this);

    @Column(name = "ENTITY")
    public Field<String> entity = new Field<>(this);

    @Column(name = "ENTITY_ID", size = 1000)
    public Field<String> entityId = new Field<>(this);

    @Column(name = "MESSAGE", size = 1000)
    public Field<String> message = new Field<>(this);

    @Column(name = "CREATE_T")
    public Field<Timestamp> createT= new Field<>(this);

    public BulkBillingResultEntity() {
        super("BULK_BILLING_RESULT");
    }

    public BulkBillingResultEntity(EntityDB entity, String message, String bulkId){
        super("BULK_BILLING_RESULT");
        agencyId.set(Integer.parseInt(ToolkitCrudConstants.getChildAgencyId().toString()));
        this.entity.set(entity.getClass().getSimpleName());
        this.entityId.set(entity.getId().getNameValueDesc() + " " + entity.getIdentifier().getNameValueDesc());
        this.message.set(message);
        this.createT.set(new Timestamp(System.currentTimeMillis()));
        this.bulkBatchId.set(bulkId);
    }


}
