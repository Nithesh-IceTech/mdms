package za.co.spsi.toolkit.crud.idempiere;

import org.idempiere.webservice.client.base.Field;

/**
 * Created by francoism on 2017/02/13.
 */
public class DataRow extends org.idempiere.webservice.client.base.DataRow {

    @Override
    public void addField(String columnName, Object value) {
        if(value != null) {
            this.addField(new Field(columnName, value));
        }
    }
}
