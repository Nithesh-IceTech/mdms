package za.co.spsi.mdms.nes.util;

/**
 * Created by johan on 2016/12/14.
 */
public class NESConstants {
    public class DeviceCommands {
        public static final String CONNECT_CONTROL_RELAY = "971dc6e881a14317a5ae4936fdf2734f";
        public static final String DISCONNECT_CONTROL_RELAY = "5137fd076f6c4be69da39c1928d1251d";
        public static final String CONNECT_LOAD = "8ff7f8145166477a86afebd1052682ab";
        public static final String DISCONNECT_LOAD = "55c9a443124e42308681eb8ebe17c067";
        public static final int DEVICE_DISCONNECT_PRIORITY_LEVEL_LOW = 0;
        public static final int DEVICE_DISCONNECT_PRIORITY_LEVEL_HIGH = 1;

    }

    public class GatewayCommunicationRequestTypes {
        public static final String SERVER_INITIATED_HIGH_PRIORITY = "8262edfc771547a2ad4467e97b2c5479";
        public static final String SERVER_INITIATED_NORMAL_PRIORITY = "ebcec8adecb142c48fb3951818db1fc4";
        public static final String GATEWAY_INITIATED = "a6fbe085fba047a3ad7a1cfcbcd86227";

    }

    public class CommandHistoryStatus
    {
        public static final String SUCCESS = "b43637dc8ddb487aabbcf9752556762f";
        public static final String FAILURE = "28b70caf8360420982f4d1ebac66a39c";
        public static final String WAITING = "3dc39216a0a64a19974826f3fec44368";
        public static final String DELETED = "a796f572761a4ea6b3a6cab751a4e8e7";
        public static final String IN_PROGRESS = "7f31e3d98cfb478f8155d3851053a607";
        public static final String CANCELLED = "FA74ABC8031F4ffb9F0C37A9720BB249";
        public static final String ORPHANED = "45e31fee41cb48d6b615fd99935db48b";
    }

    public class ControlRelayStatus
    {
        public static final String BROADCAST_OPEN = "d7ff0b7cdb6d491c90afd700cda4971b";
        public static final String BROADCAST_CLOSED = "15080352c0264373a97501ba9e40517f";
        public static final String OPEN = "6eebd8aceb784b1f826a6cdfaf4f69ae";
        public static final String CLOSED = "f8e560fe460142f996e8374a890161b4";
    }

    public class DeviceLoadVoltageStatusTypes
    {
        public static final String PRESENT     = "ff22a845507f467ca65555d52927b94c";
        public static final String NOT_PRESENT = "a8b06b115d704a64b586b19fdd1cc8b4";
        public static final String UNKNOWN     = "2349e315a1bc400b97f873901550d593";
    }

    public class StandardAPIOptions
    {
        public static final String YES = "5af8a532523c4c6e9f9344c0827391ee";
        public static final String NO = "fe6a84d285d74e06bc52413ffb81151a";
        public static final String DISCONNECT = "7d6b389f36044740bc0e1babb6340b4f";
        public static final String CONNECT = "30bd98ee3bf641798418ee0222d235b6";
        public static final String EXACT = "de334f6f1654471d99e18df1b7c29fdd";
        public static final String INEXACT = "78c2a568a6a04a9e9ccf08329043657f";
        public static final String SUCCESS = "e19cc05385004100ac7dbde7baa2a2ef";
        public static final String FAILURE = "1bf17985fcfb44f19f00799677b526bf";
    }

    public class TaskPriorities
    {
        public static final int LOW = 10;
        public static final int MEDIUM = 5;
        public static final int MEDIUM_TO_MEDIUM_HIGH = 4;
        public static final int MEDIUM_HIGH = 3;
        public static final int HIGH = 1;
    }

}
