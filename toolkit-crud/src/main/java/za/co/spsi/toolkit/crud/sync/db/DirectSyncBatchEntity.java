/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.spsi.toolkit.crud.sync.db;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.db.fields.FieldTimestamp;
import za.co.spsi.toolkit.entity.Field;


@Table(version = 2)
public class DirectSyncBatchEntity extends EntityDB {

    @Id(uuid = true)
    @Column(name = "BATCH_ID")
    public Field<String> batchId = new Field<>(this);

    @Column(name = "DATA", clob = true)
    public Field<String> data = new Field<>(this);

    @Column(name = "BATCH_STATUS_CD")
    public Field<Integer> batchStatusCd = new Field<>(this);

    @Column(name = "DEVICE_ID")
    public Field<String> deviceId = new Field<>(this);

    @Column(name = "CREATE_T")
    public FieldTimestamp createT = new FieldTimestamp(this);

    @Column(name = "ENTITY")
    public Field<String> entity = new Field<>(this);

    @Column(name = "ERROR", size = 1024)
    public Field<String> error = new Field<>(this);

    @Column(name = "ACTION_CD", defaultValue = "1")
    public Field<Integer> actionCd = new Field<>(this);


    public DirectSyncBatchEntity() {
        super("DIRECT_SYNC_BATCH");
    }
}



