package za.co.spsi.mdms.elster.db;

import za.co.spsi.toolkit.entity.Entity;
import za.co.spsi.toolkit.entity.Field;

public class ElsterProperties extends Entity {

    public Field<Long> maxId = new Field<>(this);
}
