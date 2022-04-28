package za.co.spsi.toolkit.db;

import za.co.spsi.toolkit.util.Assert;
import za.co.spsi.toolkit.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaspervdbijl on 2017/04/07.
 */
public class ExportHelper {

    public static List<Class<? extends EntityDB>> entities = new ArrayList<>();

    public static void initPaths(String paths[]) {
        for (String path : paths) {
            for (Class type : Util.getSubTypesOf(path, EntityDB.class)) {
                entities.add(type);
            }
        }
    }

    public static Class<? extends EntityDB> getByName(String name) {
        List<Class<? extends EntityDB>> values = new ArrayList<>();
        for (Class<? extends EntityDB> type : entities) {
            if (type.getSimpleName().equals(name)) {
                values.add(type);
            }
        }
        Assert.notEmpty(values,"Could not locate class from simple name %s",name);
        Assert.isTrue(values.size() < 2,"More than 1 match found for simple name %s",name);
        return values.get(0);
    }

}
