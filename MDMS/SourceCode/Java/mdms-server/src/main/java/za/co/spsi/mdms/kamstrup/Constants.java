package za.co.spsi.mdms.kamstrup;

/**
 * Created by jaspervdb on 2016/11/24.
 */
public class Constants {

    public enum LOGGER_PROFILE {
        TOTAL_4_QUADRANT_LOAD("0.1.99.1.0.255"),
        T1_ENERGY("1.1.99.1.1.255"),
        T2_ENERGY("1.1.99.1.1.255"),
        MAX_DEMAND_TOTAL_PO("0.0.21.0.5.255"),
        MAX_DEMAND_TOTAL_PR("0.0.21.0.5.255"),
        RMS_VOLTAGE("1.1.99.1.1.255"),
        RMS_CURRENT("1.1.99.1.1.255");

        public String id;

        LOGGER_PROFILE(String id) {
            this.id = id;
        }
    }

    public enum REGISTER {
        TOTAL_4_QUADRANT_LOAD_KWHP("1.1.1.8.0.255"),
        TOTAL_4_QUADRANT_LOAD_KWHN("1.1.2.8.0.255"),

        TOTAL_4_QUADRANT_LOAD_KVARP("1.1.3.8.0.255"),
        TOTAL_4_QUADRANT_LOAD_KVARN("1.1.4.8.0.255"),

        T1_ENERGY_KWHP("1.1.1.8.1.255"),
        T1_ENERGY_KWHN("1.1.2.8.1.255"),

        T1_ENERGY_KVARP("1.1.3.8.1.255"),
        T1_ENERGY_KVARN("1.1.4.8.1.255"),

        T2_ENERGY_KWHP("1.1.1.8.2.255"),
        T2_ENERGY_KWHN("1.1.2.8.2.255"),

        T2_ENERGY_KVARP("1.1.3.8.2.255"),
        T2_ENERGY_KVARN("1.1.4.8.2.255"),

        MAX_DEMAND_TOTAL_PO_KVA("1.1.9.6.0.255"),
        MAX_DEMAND_TOTAL_PO_RTC("0.2.1.130.0.255"),

        MAX_DEMAND_TOTAL_PR_KVA("1.1.9.6.0.255"),
        MAX_DEMAND_TOTAL_PR_RTC("0.2.1.130.0.255"),

        RMS_VOLTAGE_L1_V("1.1.32.25.0.255"),
        RMS_VOLTAGE_L2_V("1.1.52.25.0.255"),
        RMS_VOLTAGE_L3_V("1.1.72.25.0.255"),

        RMS_CURRENT_RMS_L1_I("1.1.99.1.1.255"),
        RMS_CURRENT_RMS_L2_I("1.1.51.25.0.255"),
        RMS_CURRENT_RMS_L3_I("1.1.71.25.0.255");

        public String id;

        REGISTER(String id) {
            this.id = id;
        }
    }
}
