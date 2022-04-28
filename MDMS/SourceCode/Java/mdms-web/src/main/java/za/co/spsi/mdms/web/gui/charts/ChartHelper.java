//package za.co.spsi.mdms.web.gui.charts;
//
//import za.co.spsi.locale.annotation.MdmsLocaleId;
//import za.co.spsi.mdms.web.auth.view.KamstrupMeterView;
//import za.co.spsi.mdms.web.auth.view.NesMeterView;
//import za.co.spsi.toolkit.crud.gui.CrudView;
//import za.co.spsi.toolkit.crud.gui.render.AbstractView;
//
///**
// * Created by jaspervdb on 2016/09/07.
// */
//public class ChartHelper {
//
//    public enum MeterType {
//        Kamstrup(MdmsLocaleId.KAMSTRUP, "KAMSTRUP_METER", KamstrupMeterView.class, "STATE", "TYPE_DESC", "KAM_METER_ID"),
//        NES(MdmsLocaleId.NES, "NES_METER", NesMeterView.class, "STATE", "TYPE_DESC", "NES_METER_ID");
//
//        public String displayName,tableName,stateLookup, modelLookup, foreignKey;
//        public Class<? extends CrudView> viewClass;
//
//        MeterType(String displayName,String tableName,Class<? extends CrudView> viewClass, String stateLookup, String modelLookup, String foreignKey) {
//            this.displayName = displayName;
//            this.tableName = tableName;
//            this.viewClass = viewClass;
//            this.stateLookup = stateLookup;
//            this.modelLookup = modelLookup;
//            this.foreignKey = foreignKey;
//        }
//
//        public static MeterType getByName(String localeName) {
//            for (MeterType type : MeterType.values()) {
//                if (AbstractView.getLocaleValue(type.displayName).equals(localeName)) {
//                    return type;
//                }
//            }
//            throw new RuntimeException((String.format("Can not find entity type by local name %s",localeName)));
//        }
//    }
//}
