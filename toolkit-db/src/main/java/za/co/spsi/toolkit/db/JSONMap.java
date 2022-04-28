package za.co.spsi.toolkit.db;

import org.json.JSONArray;
import org.json.JSONObject;
import za.co.spsi.toolkit.util.Assert;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static za.co.spsi.toolkit.db.ExportHelper.entities;

/**
 * Created by jaspervdbijl on 2017/04/07.
 */
public class JSONMap extends HashMap<Class,List<EntityDB>> {


    public JSONMap addEntity(EntityDB entity) {
        if (!containsKey(entity.getClass())) {
            put(entity.getClass(),new ArrayList<EntityDB>());
        }
        if (!inList(get(entity.getClass()),entity)) {
            get(entity.getClass()).add(entity);
        }
        return this;
    }

    public boolean inList(List<EntityDB> list, final EntityDB entity) {
        for (EntityDB e : list) {
            if (e.getId().getNameValueDesc().equals(entity.getId().getNameValueDesc())) {
                return true;
            }
        }
        return false;
    }

    public JSONObject toJSONObject() {
        JSONObject jMap = new JSONObject();
        for (Class type : keySet()) {
            List<JSONObject> list = new ArrayList<>();
            for (EntityDB entity : get(type)) {
                list.add(entity.getAsJson());
            }
            jMap.put(type.getSimpleName(),new JSONArray(list));
        }
        return jMap;
    }

    public static List<EntityDB> fromJSONMap(Connection connection, JSONObject jsonObject) throws IllegalAccessException, InstantiationException {
        List<EntityDB> values = new ArrayList<>();
        Assert.notEmpty(entities,"ExportHelper not initialised");
        for (String key : jsonObject.keySet()) {
            for (Object object : jsonObject.getJSONArray(key)) {
                EntityDB entity = ExportHelper.getByName(key).newInstance();
                entity.getFields().reset();
                entity.initFromJson((String) object);
                DataSourceDB.set(connection,entity);
                values.add(entity);
            }
        }
        return values;
    }
}
