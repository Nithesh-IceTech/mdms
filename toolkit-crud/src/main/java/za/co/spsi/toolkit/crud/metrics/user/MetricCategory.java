package za.co.spsi.toolkit.crud.metrics.user;

import za.co.spsi.toolkit.crud.db.audit.AuditEntityDB;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.db.audit.AuditEntity;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.util.Util;

import java.sql.Connection;

/**
 * Created by jaspervdbijl on 2017/06/22.
 */
@Table(version = 0)
public class MetricCategory extends EntityDB {

    @Id(uuid=true)
    @Column(name = "ID", size = 50)
    public Field<String> id = new Field<>(this);

    @Column(name = "ENTITY_NAME")
    public Field<String> entityName = new Field<>(this);

    @Column(name = "CLASS_NAME")
    public Field<String> className= new Field<>(this);

    @Column(name = "CREATE_POINTS",defaultValue = "5")
    public Field<Integer> createPoints = new Field<>(this);

    @Column(name = "UPDATE_POINTS",defaultValue = "2")
    public Field<Integer> updatePoints = new Field<>(this);

    @Column(name = "DELETE_POINTS",defaultValue = "2")
    public Field<Integer> deletePoints = new Field<>(this);

    @Column(size = 1024)
    public Field<String> description = new Field<>(this);


    public MetricCategory() {
        super("METRIC_CATEGORY");
    }

    public MetricCategory initDefaults(String entityName,String className) {
        this.className.set(className);
        this.entityName.set(entityName);
        return this;
    }

    public Integer getPoint(AuditEntity.Type type) {
        return type.equals(AuditEntity.Type.CREATE)?createPoints.getNonNull():
                type.equals(AuditEntity.Type.UPDATE)?updatePoints.getNonNull():
                        deletePoints.getNonNull();
    }

    @Override
    public void onTableCreate(Connection connection) {
        // load the default data
        Util.getSubTypesOf("za.co.spsi", AuditEntityDB.class).stream().forEach(e -> {
            DataSourceDB.set(connection,new MetricCategory().initDefaults(e.getSimpleName(),e.getName()));
        });

    }

}
