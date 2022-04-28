package za.co.spsi.mdms.nes;

import java.text.SimpleDateFormat;

/**
 * Created by jaspervdb on 2016/11/21.
 */
public class NesConstants {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss.mmm");

    public enum CommandHistoryStatus {

        SUCCESS("b43637dc8ddb487aabbcf9752556762f"),
        FAILURE("28b70caf8360420982f4d1ebac66a39c"),
        WAITING("3dc39216a0a64a19974826f3fec44368"),
        DELETED("a796f572761a4ea6b3a6cab751a4e8e7"),
        IN_PROGRESS("7f31e3d98cfb478f8155d3851053a607"),
        CANCELLED("FA74ABC8031F4ffb9F0C37A9720BB249");

        String value;
        CommandHistoryStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum GatewayStatus {
        ENABLED("6F9EFDF055C04031947FAE0C65814C78"),
        DISABLED("D1F878A4F5594dd0886B31C9CDAF62CC");

        String value;
        GatewayStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum ControlRelayStatus {
        BROADCAST_OPEN("d7ff0b7cdb6d491c90afd700cda4971b"),
        BROADCAST_CLOSED("15080352c0264373a97501ba9e40517f"),
        OPEN("6eebd8aceb784b1f826a6cdfaf4f69ae"),
        CLOSED("f8e560fe460142f996e8374a890161b4");

        String value;
        ControlRelayStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

}
