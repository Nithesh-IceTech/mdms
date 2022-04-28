/*
 * FieldMetaData.java
 *
 * Created on November 20, 2006, 5:15 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package za.co.spsi.toolkit.db.meta;

import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.entity.Entity;
import za.co.spsi.toolkit.entity.Field;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jaspervdb
 */
public class FieldMetaData {
//
//    public String name = null;
//    public int size = 0;
//    //public integrate type = 0;
//    public Class type = null;
//    public String defaultValue = null;
//    public boolean nullable = true;
//    public boolean autoIncrement = false;
//
//    @Override
//    public String toString() {
//        return name + "; size " + size+ "; type " + type +"; default " + defaultValue + "; nullable " + nullable + "; autoinc " + autoIncrement;
//    }
//    /*
//     * @return null if all fields match, message with unmatched fields
//     */
//    public boolean match(Entity entity, Field field, boolean exact) throws IllegalAccessException {
//        Column column = (Column) field.getAnnotation(Column.class);
//        String df = column != null && !column.defaultValue().isEmpty()?column.defaultValue():null;
//        boolean notNullableMatch = (field.hasAnnotation(Id.class) || (column != null && column.notNull())&exact) == nullable;
//        boolean sizeMatch = column == null || (!field.getTypeClass().equals(String.class) || (column.size() == size && exact));
//        boolean typeMatch =
//        boolean checks[] = new boolean[]{
//            (field.isAnnotationPresent(FieldId.class) || field.isAnnotationPresent(FieldNotNull.class)) == nullable && exact,
//            //(this.isAutoIncrement != (field.isAnnotationPresent(FieldId.class))) && !(field instanceof _FieldIdUnique),
//            (this.autoIncrement != (field.isAnnotationPresent(FieldId.class))),
//            field.getDefaultClass().equals(String.class) && (!field.isAnnotationPresent(TypeBlob.class) && size != field.getSize() && exact),
//            field.isAnnotationPresent(SetDefault.class)?
//                    !(df!=null&&(df.equals(defaultValue)||df.equals("'"+defaultValue+"'")))&exact:false,
////                    (!(((SetDefault)field.getAnnotation(SetDefault.class)).value()+"").equals(defaultValue))&&exact:false,
//            !DBUtil.matchField(entity, type, field, exact)
//        };
//
//        StringList slist = new StringList();
//        slist.add(field.getDefaultClass().getName());
//        /*
//        for (integrate t : DBUtil.fieldTypeMap.get(field.getDefault().getClass())) {
//            slist.add(""+t);
//        }*/
//        String msg[] = new String[]{
//            "Allow null", "Autoincrement", "size mismatched","Set Default",
//            "Default Type [" + field.getDefaultClass() + "]. DB Type ["+ type + "]"
//        };
//        boolean pass = true;
//        for (int i =0;i < checks.length;i++) {
//            pass = pass && !checks[i];
//            if (checks[i]) {
//                Logger.getLogger(MetaDataField.class.getName()).log(Level.WARNING, "Field [{0}]. {1}. Default {2}", new Object[]{field.getConicalName(), msg[i],df});
//            }
//        }
//        return pass;
//        /*
//        String msg = "";
//        boolean nullMatch = field.isAnnotationPresent(FieldId.class) || (isNull == !field.isAnnotationPresent(FieldNotNull.class));
//        boolean pass = true;
//        //boolean keyMatch = !field.isAnnotationPresent(FieldId.class) || field.isAnnotationPresent(FieldId.class) == isAutoIncrement;
//        if (!nullMatch) {
//            Logger.getLogger(MetaDataField.class.getName()).warning("Field [" + field.getConicalName() + "]. Allow null - mismatch");
//            pass = exact;
//            //msg += "Field [" + field.getConicalName() + "]. Allow null - mismatch\n";
//        }
//        if (this.isAutoIncrement != field.isAnnotationPresent(FieldId.class) && !(field instanceof Unique)) {
//            Logger.getLogger(MetaDataField.class.getName()).warning("Field [" + field.getConicalName() + "]. Autoincrement mismatch");
//            // pass
//            //msg += "Field [" + field.getConicalName() + "]. Autoincrement mismatch\n";
//            pass = false;
//        }
//        if (field.getDefault() instanceof String && size != field.getSize()) {
//            msg += "Field [" + field.getConicalName() + "] size mismatched";
//            pass = pass || exact;
//        }
//        if (!DBUtil.matchField(dao, type, field, exact)) {
//            StringBuilder sb = new StringBuilder();
//            for (integrate t : DBUtil.fieldTypeMap.get(field.getDefault().getClass())) {
//                sb.append(t + ",");
//            }
//            return "Field ["
//                    + field.getConicalName() + "] mismatched. Default Type [" + field.getDefault().getClass() + "]. DB Type ["
//                    + sb.toString() + "]\n";
//        }
//        if (msg.length() > 0) {
//            Logger.getLogger(MetaDataField.class.getName()).warning("DB Update: " + msg);
//        }
//        return exact ? (msg.length() == 0 ? null : msg) : null;*/
//    }
}
