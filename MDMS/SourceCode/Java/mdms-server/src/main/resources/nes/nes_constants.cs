/*
Copyright 2002-2014, Echelon Corporation, USA. All rights reserved

Echelon ESP
NAME: constants.cs

DESCRIPTION:
	This file contains all the common constants.

 * */

using System;

namespace Echelon.ASPG.Constants
{
	//Contains the list of types of Logs entries for the solution.
	public class LogType
	{
        public const string ERROR = "f27e59fb29f140c2a62b0167e016bac1";
        public const string USERACTIVITY = "396d29884c8449b0bca4e54c3c31d177";
        public const string DEBUG = "04866276652c4ee8a5573195f0d34939";
        public const string SQL_TRACE = "c1e81dbe173742b5b230420bf85f1f24";
        public const string DC1000_ADAPTER_ENHANCED_DEBUG = "6d57a96ffc1f4fd3b3bcb5985af09128";
        public const string INTERNAL_TESTING = "28f3422ebfbc417bb75a2214368cad9d";
        public const string PERFORMANCE = "f3a7374299fb4cdcad829bde99781499";
        public const string DIAGNOSTIC = "d296bb876f244dcf9d6afbb8fa076a79";
        public const string ENGINE_MESSAGE = "2db39ef3186146b7a4d45df5025e5bfc";
        public const string INFORMATIONAL = "9d6b63734e4845a082b8fd6352e37783";

		[Obsolete("v5.0 - use ENGINE_MESSAGE instead")]
		public const string TCPIP_TRACE = "2db39ef3186146b7a4d45df5025e5bfc";
	}

	//Contains the list of databases supported.
	public class DatabaseTypeID
	{
        public const string CORE = "dd5141bd0f754256b6a51c1eb934841b";
        public const string DISTRIBUTION = "b596af7a0c4d455fa1e77294557dc561";
	}

	//Contains the types of data supported by DataPoints, Functions, SoapCalls, etc...
	public class DataTypes
	{
		public const string NUMERIC = "3616f4c224084b958047f8ac8a4ab0bc";
		public const string STRING = "309c5d9a62454707b6bd74ed973583fd";
	}

	//Contains the list of statuses for DataPointValues.
	public class DataPointValueStatus
	{
		public const string COMPLETE = "86FF3E549A5C4cecAFADC739246686DA";
		public const string FAILURE = "4E312B96EDA447f29F74E1FC1F9F06DA";
	}

	// Contains the list of statuses for data points
	public class DataPointStatus
	{
		public const string ENABLED = "65610EB0B08F41399F2D5BC236DA23A3";
		public const string DISABLED = "5F3F591C8667467b967CF68CAC6252C4";
		public const string STOPPED = "168C025616D94db7A6D63257BF67F02C";
	}

	// Contains the list of categories of Types
	public class Categories
	{
		public const string DATAPOINTVALUE_STATUS_TYPE = "DataPointValues.StatusType";
		public const string DATAPOINT_STATUS_TYPE = "DataPoints.StatusType";
		public const string DATAPOINT_CALCULATION_TYPE = "DataPoints.CalculationType";
		public const string DATAPOINT_SOURCE_TYPE = "DataPoints.SourceType";
		public const string TASK_STATUS_TYPE = "Tasks.StatusType";
		public const string TASK_TASK_TYPE = "Tasks.TaskType";
		public const string ATTRIBUTE_TYPE = "Attributes.Type";
		public const string MESSAGE_LOG_STATUS_TYPE = "MessageLog.StatusType";
		public const string EXTERNAL_RETURN_CODES = "ExternalServiceReturnCodes";
		public const string DATA_TYPES = "DataTypes";
		public const string SCHEDULE_RECURRENCE_TYPE = "Schedules.RecurrenceType";
		public const string SCHEDULE_STATUS_TYPE = "Schedules.StatusType";
		public const string SCHEDULEDTASKSPENDING_STATUS_TYPE = "ScheduledTasksPending.StatusType";
		public const string DATABASE_TYPE = "DataManager.DatabaseTypeID";
		public const string DATALOG_RETRIEVAL_METHOD = "GatewaysILon100.DataLogRetrievalMethodType";
		public const string GATEWAY_TYPE = "Gateways.Types";
		public const string GATEWAY_STATUS = "Gateways.StatusTypeID";
		public const string SETTING_VALUE_TYPES = "SolutionSetting.ValueTypes";
		public const string SCHEDULE_TIMEOUTINTERVAL_TYPE = "Schedules.TimeoutIntervalType";
		public const string EXPRESSION_TYPE = "DataPoints.ExpressionType";
		public const string RESTRICTION_TYPE = "DataPointManager.RestrictionType";
		public const string HIERARCHY_RESTRICTION_TYPE = "Hierarchy.RetrieveRestrictions";
		public const string HIERARCHY_LEVEL_MEMBER_DELETE_TYPE = "HierarchyLevelMember.DeleteTypes";
		public const string GATEWAY_COMMUNICATION_TYPE = "Gateways.CommunicationTypes";
        public const string GATEWAY_TEMPLATE_TYPE = "Gateways.TemplateTypes";
		public const string ID_TYPE = "IDTypes";
		public const string DEVICE_STATUS = "Devices.StatusTypes";
		public const string DEVICE_TYPE = "Devices.Types";
		public const string ENTITY_TYPE = "EntityTypes";
		public const string TASK_PROCESSOR_TYPES = "TaskProcessorTypes";
		public const string COMMAND_HISTORY_STATUS_TYPE = "CommandHistory.StatusTypes";
		public const string METER_LONTALK_KEY_STATUS = "Meters.AuthenticationStatusTypes";
		public const string GATEWAY_COMMUNICATION_REQUEST_TYPE = "GatewayCommunicationHistory.RequestTypeID";
		public const string GATEWAY_COMMUNICATION_STATUS_TYPE = "GatewayCommunicationHistory.StatusTypeID";
		public const string DEVICE_SERVICE_STATUS_TYPE = "Devices.ServiceStatusTypes";
		public const string PHASE_TYPES = "PhaseTypes";
		public const string RESULT_TYPES = "Results.Types";
		public const string EVENT_DEFINITION_STATUS_TYPES = "EventDefinition.StatusType";
		public const string EVENT_DEFINITION_DELIVERY_TYPE = "Event.DeliveryType";
		public const string FIRMWARE_TYPES = "Firmware.Types";
		public const string CHANGE_TYPE = "PendingChanges.ChangeTypeID";
		public const string EXPIRED_INTERVAL_TYPE = "ArchiveSetting.ExpiredIntervalType";
		public const string SCHEDULE_ASSIGNMENT_STATUS_TYPE = "ScheduleAssignment.StatusType";
		public const string SCHEDULE_TYPE = "Schedules.Types";
		public const string SETTING_TYPE = "SettingTypes";
		public const string DATA_POINT_VALUE_SORT_TYPES = "DataPointValue.SortTypes";
		public const string SORT_ORDER_TYPES = "SortOrderTypes";
		public const string INFORMATION_RETURN_TYPES = "InformationReturnTypes";
		public const string METER_DISPLAY_CATEGORY_TYPES = "MeterDisplayCategoryTypes";
		public const string PROPERTY_DATA_TYPES = "Properties.DataTypes";
		public const string TOUCALENDARTYPES = "TOUCalendarTypes";
		public const string CONTROL_RELAY_STATUS = "SecondaryControlOutputRelayStatus";
		public const string MAXIMUM_POWER_LEVEL_ENABLE_TYPES = "MaximumPowerEnableTypes";
		public const string LAST_GATEWAY_TO_DEVICE_COMMUNICATION_STATUS_TYPES = "GatewayToDeviceCommunicationStatusTypes";
		public const string REPEATER_PATH_STATUS = "RepeaterPathStatus";
		public const string ADD_METER_FAILURE_TYPES = "AddMeterFailureTypes";
		public const string DEVICE_DISCOVERED_TYPES = "DiscoveredTypes";
		public const string BILLING_DATA_STRUCTURE_TYPES = "BillingDataStructureTypes";
		public const string IP_ADDRESS_TYPE = "IPAddressTypes";
		public const string APPLICATION_LEVEL_AUTHENTICATION_TYPES = "ApplicationLevelAuthenticationStatusTypes";
		public const string GATEWAY_ENCRYPTION_STATUS_TYPES = "Gateway.EncryptionStatusTypes";
		public const string GATEWAY_WAN_CONFIGURATION_STATUS_TYPES = "Gateway.WANConfigurationStatusTypes";
		public const string GATEWAY_OUTBOUND_CHAP_STATUS_TYPES = "Gateway.OutboundCHAPStatusTypes";
		public const string GATEWAY_INBOUND_CHAP_STATUS_TYPES = "Gateway.InboundCHAPStatusTypes";
		public const string GATEWAY_PAP_STATUS_TYPES = "Gateway.PAPStatusTypes";
		public const string GATEWAY_PORT_SPEED_TYPES = "Gateway.PortSpeedTypes";
		public const string GATEWAY_MODEM_COMMAND_TYPES = "Gateway.ModemCommandTypes";
		public const string GATEWAY_MODEM_CONNECT_TYPES = "Gateway.ModemConnectTypes";
		public const string GATEWAY_PPP_CONNECTION_TYPES = "Gateway.PPPConnectionTypes";
		public const string GATEWAY_MODEM_ANSWER_TYPES = "Gateway.ModemAnswerTypes";
		public const string GATEWAY_AUTHENTICATION_TYPES = "Gateway.AuthenticationTypes";
		public const string PING_GATEWAY_STATUS_TYPE = "PingGatewayStatusTypes";
		public const string MBUS_STATUS_TYPES = "MBusStatusTypes";
		public const string RESERVED_IDS = "ReservedIDs";
		public const string DEVICE_MBUS_AUTO_DISCOVERY_STATUS_TYPE = "Device.MBusAutoDiscoveryStatusTypes";
		public const string CONNECTION_FAILURE_TYPES = "ConnectionFailureTypes";
		public const string PERFORMANCE_LOG_TYPES = "PerformanceLogTypes";
		public const string APPLICATION_LEVEL_AUTHENTICATION_STATUS_TYPES = "ApplicationLevelAuthenticationStatusTypes";
		public const string COMMAND_FAILURE_TYPES = "Command.FailureTypes";
		public const string COMMUNICATION_FAILURE_STATUS_TYPES = "CommunicationFailureStatusTypes";
		public const string DATA_ORDER_TYPES = "DataOrderTypes";
        public const string DEVICE_NACK_CODES = "DC1000DeviceNackCodes";
        public const string DATA_CONCENTRATOR_HARDWARE_FAILURE_CODES = "DC1000HardwareDiagnosticCodes";
        public const string DATA_CONCENTRATOR_CAUSE_CODES = "DC1000NackCauseCodes";
        public const string DATA_CONCENTRATOR_REBOOT_CODES = "DC1000RebootCauses";
		public const string DC1000_RESOURCE_ENTRY_PRIORITIES = "DC1000ResourceEntryPriorities";
		public const string DC1000_RESOURCES = "DC1000Resources";
        public const string DATA_CONCENTRATOR_SECURITY_EXCEPTION_CODES = "DC1000SecurityExceptions";
        public const string DATA_CONCENTRATOR_SOFTWARE_FAILURE_CODES = "DC1000SoftwareDiagnosticCodes";
		public const string EVENT_DELIVERY_STATUS_TYPES = "Event.DeliveryStatusType";
		public const string EVENT_SECURITY_ALERT_TYPES = "Events.SecurityAlertTypes";
		public const string EVENT_STATE_TYPE = "EventStateType";
		public const string GATEWAY_DATA_AVAILABLE_TYPES = "Gateway.DataAvailableTypes";
		public const string GATEWAY_INITIAL_COMMUNICATION_STATUS_TYPES = "Gateways.InitialCommunicationStatusType";
		public const string GRAMMAR_STATUS = "Grammar.Status";
		public const string KEY_AVAILABILITY_TYPES = "KeyAvailabilityTypes";
		public const string MBUS_SECURITY_STATUS_TYPES = "MBus.SecurityStatusTypes";
		public const string MBUS_AUTHENTICATION_TYPES = "MBusAuthenticationTypes";
		public const string MBUS_BILLING_SCHEDULE_FREQUENCY_TYPES = "MBusBillingScheduleFrequencyTypes";
        public const string MBUS_ALARMS = "MeterDetectedMBusAlarms";
		public const string REPEATER_TYPES = "RepeaterTypes";
		public const string SERVER_TO_GATEWAY_PROTOCOL_TYPES = "ServerToGatewayProtocolTypes";
		public const string STANDARD_API_OPTIONS = "StandardAPIOptions";
		public const string TIMEZONE_DST_TYPES = "Timezones.DstTypes";
		public const string PREPAY_ADD_CREDIT_OPTION_TYPES = "PrepayAddCreditOptionTypes";
		public const string PREPAY_CLEAR_CREDIT_OPTION_TYPES = "PrepayClearCreditOptionTypes";
		public const string PREPAY_STATUS_TYPES = "PrepayStatusTypes";
		public const string PREPAY_EMERGENCY_CREDIT_STATUS_TYPES = "PrepayEmergencyCreditStatusTypes";
		public const string PREPAY_REVERSE_POWER_DEDUCTION_STATUS_TYPES = "PrepayReversePowerDeductionStatusTypes";
		public const string PREPAY_MAXIMUM_POWER_STATUS_TYPE_ID = "PrepayMaximumPowerStatusTypeID";
		public const string PREPAY_AUDIBLE_ALARM_STATUS_TYPES = "PrepayAudibleAlarmStatusTypes";
		public const string PREPAY_CREDIT_TYPES = "PrepayCreditTypes";
		public const string ENGINE_IP_ADDRESS_ASSIGNMENT_TYPE_ID = "Engine.IPAddressAssignmentTypes";
		public const string DEVICE_TEST_POINT_STATUS_TYPES = "Device.TestPointStatusTypes";
		public const string GATEWAY_MESH_DETECTION_STATUS_TYPES = "Gateway.MeshDetectionStatusTypes";
		public const string READ_TOU_CALENDAR_OPTION_TYPES = "Device.ReadTOUCalendarOptionTypes";
		public const string CONNECT_REQUEST_SOURCE_TYPES = "ConnectRequestSourceTypes";
		public const string DEVICE_LOAD_VOLTAGE_STATUS_TYPES = "Device.LoadVoltageStatusTypes";
		public const string TEST_TCPIP_PORT_STATUS_TYPES = "WanConfigurationSetting.TestTcpIpPortStatusTypes";
		public const string EVENT_HISTORY_SORT_BY_TYPES = "EventHistory.SortByTypes";
        public const string PROCESSED_STATUS_TYPES = "ProcessedStatusTypes";
        public const string PASSIVE_FTP_IP_ADDRESS_SOURCE_TYPES = "WanConfigurationSetting.PassiveFtpIpAddressSourceTypes";
        public const string CONTINUOUS_EVENT_LOG_CONFIGURATION_STATUS_TYPES = "ContinuousEventLogConfigurationStatusTypes";
        public const string CONTINUOUS_EVENT_LOG_CONFIGURATION_PRIORITY_LEVEL_TYPES = "ContinuousEventLogConfigurationPriorityLevelTypes";
        public const string METER_ALARM_STATUS_TYPES = "MeterAlarmStatusTypes";
        public const string MEP_ALARM_STATUS_TYPES = "MEPAlarmStatusTypes";
        public const string METER_ALARM_EVENT_LOG_STATUS_TYPES = "MeterAlarmEventLogStatusTypes";
        public const string GATEWAY_EVENT_STATUS_TYPES = "GatewayEventStatusTypes";
        public const string GATEWAY_EVENT_ON_CLEAR_STATUS_TYPES = "GatewayEventOnClearStatusTypes";
        public const string COMMAND_TYPES = "Command.Types";
        public const string INTERFACE_READ_STATUS_TYPES = "Device.InterfaceReadStatusTypes";
        public const string BILLING_INTERFACE_MISMATCH_TYPES = "Device.BillingInterfaceMismatchTypes";
        public const string GATEWAY_PLC_STATUS_TYPES = "Gateway.PLCStatusTypes";
        public const string SELF_READ_RETRIEVAL_STATUS_TYPES = "Devices.SelfReadRetrievalStatusTypes";
        public const string ONE_TIME_READ_REQUEST_TYPES = "Devices.OneTimeReadRequestTypes";
        public const string CONTINUOUS_DELTA_LOAD_PROFILE_COLLECTION_STATUS_TYPES = "ContinuousDeltaLoadProfileCollectionStatusTypes";
        public const string CONTINUOUS_DELTA_LOAD_PROFILE_COLLECTION_STOPPED_REASON_TYPES = "ContinuousDeltaLoadProfileCollectionStoppedReasonTypes";
        public const string CUMULATIVE_DEMAND_STATUS_TYPES = "Devices.CumulativeDemandStatusTypes";
        public const string CONTINUOUS_CUMULATIVE_DEMAND_STATUS_TYPES = "Devices.ContinuousCumulativeDemandStatusTypes";
        public const string PRESENT_DEMAND_CALCULATION_TYPES = "Devices.PresentDemandCalculationTypes";
        public const string DEMAND_CONFIGURATION_FAILURE_TYPES = "DemandConfigurationFailureTypes";
        public const string PHASE_STATUS_TYPES = "PhaseStatusTypes";
        public const string LOAD_PROFILE_CONFIGURATION_ANSI_COMPLIANCE_STATUS_TYPES = "Devices.LoadProfileConfigurationAnsiComplianceStatusTypes";
        public const string LOAD_PROFILE_CONFIGURATION_SKIPPED_INTERVAL_VALUES_TYPES = "Devices.LoadProfileConfigurationSkippedIntervalValuesTypes";
        public const string AGENT_TYPES = "AgentTypes";
        public const string DISCONNECT_SWITCH_LED_BEHAVIOR = "Device.DisconnectSwitchLEDBehavior";
        public const string DISCONNECT_SWITCH_LED_STATUS = "Device.DisconnectSwitchLEDStatus";
        public const string MANUAL_DISCONNECT_STATUS = "Device.ManualDisconnectStatus";
        public const string REMOTE_RECONNECT_STATUS = "Device.RemoteReconnectStatus";
        public const string POWER_QUALITY_STATISTIC_TYPES = "PowerQualityStatisticTypes";
        public const string COMMAND_FAILURE_REASONS = "TaskProcessor.CommandFailureReasons";
        public const string LOAD_PROFILE_UPLOAD_TYPES = "LoadProfile.UploadTypes";
        public const string PENDING_CHANGE_ENTITY_TYPES = "PendingChangeEntityTypes";
        public const string DEMAND_CONFIGURATION_STATUS_TYPES = "DemandConfigurationStatusTypes";
        public const string GATEWAY_POWER_LINE_CARRIER_COMMUNICATION_STATUS_TYPES = "GatewayPowerLineCarrierCommunicationStatusTypes";
        public const string USER_AUTHENTICATION_TYPES = "UserAuthenticationTypes";
        public const string IMPORT_DATA_TYPES = "ImportDataTypes";
        public const string IMPORT_PROCESSING_COMPLETION_STATUS_TYPES = "ImportProcessingCompletionStatusTypes";

		//-----added in V4------
		public const string DEVICE_KEY_CATEGORIES = "DeviceKeyCategories";
		public const string DATA_CONTENT_RETURN_TYPES = "DataContentReturnTypes";
		public const string GATEWAY_KEY_CATEGORIES = "GatewayKeyCategories";
		public const string LOGICAL_RESTRICTION_TYPES = "LogicalRestrictionTypes";
        public const string GATEWAY_RETRIEVE_LIST_SORT_BY_TYPES = "GatewayRetrieveListSortByTypes";
        public const string VALUE_MATCH_TYPES = "ValueMatchTypes";
        public const string GATEWAY_ATM_MODE_TYPES = "GatewayAtmModeTypes";
        public const string DEVICE_ATM_MODE_TYPES = "DeviceAtmModeTypes";
        public const string COMMAND_HISTORY_SORT_BY_TYPES = "CommandHistorySortByTypes";
        public const string GATEWAY_RETRIEVE_COMMUNICATION_HISTORY_SORT_BY_TYPES = "GatewayRetrieveCommunicationHistorySortByTypes";
        public const string DEVICE_RETRIEVE_LIST_SORT_BY_TYPES = "DeviceRetrieveListSortByTypes";
		public const string GATEWAY_EVENT_RETRIEVE_TYPES = "GatewayEventRetrieveTypes";
		public const string EVENT_TYPES = "EventTypes";
		public const string HIERARCHY_RETRIEVE_TYPES = "HierarchyRetrieveTypes";
		public const string ATTRIBUTE_RETRIEVE_TYPES = "AttributeRetrieveTypes";
        public const string COMMAND_FAILURE_REASON_TYPES = "CommandFailureReasonTypes";
        public const string DATA_CONCENTRATOR_LOAD_PROFILE_SOURCE_TYPES = "DataConcentratorLoadProfileSourceTypes";
		public const string MEP_READ_SCHEDULE_FREQUENCY_TYPES = "MepReadScheduleFrequencyTypes";
        public const string DATA_URGENCY_TYPES = "DataUrgencyTypes";
        public const string DISCONNECT_RECONNECT_WITH_LSV_STATUS_TYPES = "Devices.DisconnectReconnectWithLSVStatusTypes";
        public const string DISCONNECT_RECONNECT_WITH_LS_STATUS_TYPES = "Devices.DisconnectReconnectWithLSStatusTypes";

		//------added in V4.1 ------
		public const string FILE_RETRIEVE_LIST_SORT_BY_TYPES = "FileRetrieveListSortByTypes";
		public const string FILE_TYPES = "FileTypes";
        public const string DEVICE_DISCONNECT_LOCKED_OPEN_STATUS_TYPES = "Device.DisconnectLockedOpenStatusTypes";

        //------added in V5.0 ------
        public const string LOAD_PROFILE_CONFIGURATION_READ_TYPES = "LoadProfileConfigurationReadTypes";
        public const string LOAD_PROFILE_CONFIGURATION_TYPES = "LoadProfileConfigurationTypes";
        public const string TRUSTED_SOURCE_TYPES = "TrustedSourceTypes";
        public const string GATEWAY_EXPORT_DATA_TYPES = "GatewayExportDataTypes";
        public const string DEVICE_EXPORT_DATA_TYPES = "DeviceExportDataTypes";
        public const string EXPORT_PROCESSING_COMPLETION_STATUS_TYPES = "ExportProcessingCompletionStatusTypes";
        public const string IMPORT_FAILURE_REASON_TYPES = "ImportFailureReasonTypes ";
		public const string EXPORT_FAILURE_REASON_TYPES = "ExportFailureReasonTypes";
		public const string CONNECT_FAILURE_REASONS = "ConnectFailureReasons";
		public const string DATA_COLLECTION_TYPES = "DataCollectionTypes";
		public const string ASYMMETRIC_KEY_TYPES = "AsymmetricKeyTypes";
		public const string COMMAND_CANCELLED_TYPES = "Command.CancelledTypes";
        public const string AUTO_APPLY_STATUS_TYPES = "AutoApplyStatusTypes";

		//------added in V5.1----------
		public const string ESS_CREDENTIALS_BYPASS_SSL_TYPES = "EssCredentialsBypassSslTypes";
        public const string BATCH_TYPES = "BatchTypes";
        public const string PROPERTY_DATA_TYPE = "PropertyDataTypes";
        public const string COMMISSION_STATUS_TYPES = "CommissionStatusType";

        public const string REGISTERED_OBJECT_EVENT_TYPE = "RegisteredObject.EventType";
        public const string METER_DOMAIN_ID_STATUS_TYPE = "MeterDomainIDStatusTypes";
        public const string DEVICE_METER_SOURCE_TYPES = "Devices.MeterSourceTypes";
        public const string INTERNAL_EVENT_TYPES = "InternalEventTypes";

		//---5.2------
		public const string CLEAR_CREDITS_BEFORE_ADD_OPTION_TYPES = "ClearCreditsBeforeAddOptionTypes";

		//---5.3------
		public const string EVENT_LOG_TYPES = "EventLogTypes";
		public const string METER_DISPLAY_LIST_TYPES = "MeterDisplayListTypes";
		public const string AVERAGE_POWER_CONFIGURATION_TYPES = "AveragePowerConfigurationTypes";

		////////////////////////////////////////////////////////////////////////////////////////
		//The following are deprecated - the version in which they were deprecated and the    //
		//constant replacing them (if any) is listed to the right of the constant.            //
		////////////////////////////////////////////////////////////////////////////////////////

		[Obsolete("v2.1 - use ID_TYPE")]
		public const string RETRIEVE_BY_PARAMETER_ID_TYPE = "IDTypes";	//v2.1 - use ID_TYPE

		[Obsolete("v2.1 - use CONTROL_RELAY_STATUS")]
		public const string SECONDARY_CONTROL_OUTPUT_RELAY_STATUS = "SecondaryControlOutputRelayStatus"; //v2.1 - use CONTROL_RELAY_STATUS

		[Obsolete("v2.2 - duplicate of SCHEDULE_TIMEOUTINTERVAL_TYPE")]
		public const string TIMEOUT_INTERVAL_TYPE = "Schedules.TimeoutIntervalType";	//v2.2 - duplicate of SCHEDULE_TIMEOUTINTERVAL_TYPE

		[Obsolete("v2.2 - duplicate of TASK_PROCESSOR_TYPES")]
		public const string PROCESSOR_TYPE = "TaskProcessorTypes";	//v2.2 - duplicate of TASK_PROCESSOR_TYPES

		[Obsolete("v2.2 - no longer used")]
		public const string PROCESSOR_PROCESSING_TYPE = "TaskProcessor.ProcessingType";	//v2.2 - no longer used

		[Obsolete("v2.2 - no longer used")]
		public const string SCHEDULE_PROCESS_TYPE = "Schedules.ProcessType";	//v2.2 - no longer used

		[Obsolete("v2.2 - no longer used")]
		public const string MESSAGE_LOG_LOG_TYPE = "MessageLog.LogType";	//v2.2 - no longer used

		[Obsolete("v2.2 - no longer used")]
		public const string SETTING_RETURN_STATUSES = "SolutionSetting.ReturnStatuses";	//v2.2 - no longer used

		[Obsolete("v2.2 - no longer used")]
		public const string DEVICE_COMMANDS = "Device.CommandTypes";	//v2.2 - no longer used

		[Obsolete("v2.2 - no longer used")]
		public const string GATEWAY_COMMANDS = "Gateway.CommandTypes";	//v2.2 - no longer used

		[Obsolete("v2.2 - no longer used")]
		public const string INTERNAL_COMMANDS = "InternalCommandTypes";	//v2.2 - no longer used

		[Obsolete("v2.3 use METER_LONTALK_KEY_STATUS")]
		public const string METER_AUTHENTICATION_STATUS = "Meters.AuthenticationStatusTypes";	//v2.3 use METER_LONTALK_KEY_STATUS

        [Obsolete("v3.0 - replaced by DC1000_DEVICE_NACK_CODES")]
        public const string DC1000_DEVICE_KNACK_CODES = "DC1000DeviceNackCodes"; //v3.0 - replaced by DC1000_DEVICE_NACK_CODES

        [Obsolete("v3.0 - no longer used")]
        public const string DC1000_EVENT_TYPES = "Event.DC1000EventTypes";  //v3.0 - no longer used

        [Obsolete("v3.0 - no longer used")]
        public const string DC1000_DEVICE_ALARMS = "DC1000DeviceAlarms";    //v3.0 - no longer used

        [Obsolete("v3.0 - replaced with DATA_CONCENTRATOR_REBOOT_CODES")]
        public const string DC1000_REBOOT_CAUSES = "DC1000RebootCauses";    //v3.0 - replaced with DATA_CONCENTRATOR_REBOOT_CODES

        [Obsolete("v3.0 - replaced with DATA_CONCENTRATOR_SECURITY_EXCEPTION_CODES")]
        public const string DC1000_SECURITY_EXCEPTIONS = "DC1000SecurityExceptions";    //v3.0 - replaced with DATA_CONCENTRATOR_SECURITY_EXCEPTION_CODES

        [Obsolete("v3.0 - no longer used")]
        public const string DC1000_RSA_KEY_STATES = "DC1000RsaKeyStates";   //v3.0 - no longer used

        [Obsolete("v3.0 - replaced with DATA_CONCENTRATOR_HARDWARE_FAILURE_CODES")]
        public const string DC1000_HARDWARE_DIAGNOSTIC_CODES = "DC1000HardwareDiagnosticCodes"; //v3.0 - replaced with DATA_CONCENTRATOR_HARDWARE_FAILURE_CODES

        [Obsolete("v3.0 - replaced with DATA_CONCENTRATOR_SOFTWARE_FAILURE_CODES")]
        public const string DC1000_SOFTWARE_DIAGNOSTIC_CODES = "DC1000SoftwareDiagnosticCodes"; //v3.0 - replaced with DATA_CONCENTRATOR_SOFTWARE_FAILURE_CODES

        [Obsolete("v3.0 - replaced with DEVICE_NACK_CODES")]
        public const string DC1000_DEVICE_NACK_CODES = "DC1000DeviceNackCodes"; //v3.0 - replaced with DEVICE_NACK_CODES

        [Obsolete("v3.0 - replaced with DATA_CONCENTRATOR_CAUSE_CODES")]
        public const string DC1000_NACK_CAUSE_CODES = "DC1000NackCauseCodes";   //v3.0 - replaced with DATA_CONCENTRATOR_CAUSE_CODES

        [Obsolete("v3.0 - replaced with MBUS_ALARMS")]
        public const string METER_DETECTED_MBUS_ALARMS = "MeterDetectedMBusAlarms"; //v3.0 - replaced with MBUS_ALARMS

		[Obsolete("v5.2.000 - no longer used")]
		public const string MAXIMUM_POWER_TYPES = "Device.MaximumPowerTypes";
		/////////////////////////////////////////////////////////////////////////////////////////
	}

	public class EventLogTypes
	{
		public const string LOG_1 = "1a769784a2b14f5292f9c7a212512808";
		public const string LOG_2 = "95a65fad57734d71bdaa849543b9079f";
	}
	// Contains the list of database servers supported
	public class DatabaseServerType
	{
		public const string SQL_SERVER = "e8c8d576339849fda9637181e51efaeb";
	}

	// Contains the list of types supported in the Task Distribution Framework
	public class TaskProcessorTypes
	{
		public const string ARCHIVE = "6bbe851a0a15451fba5e189d8a8ac06c";
		public const string SCHEDULE_CONTROLLER = "c1c921f6cc40461d8f431ee4e02b932e";
		public const string SCHEDULE_PROCESSOR = "5C259D292C5C4651BC63BD17C72C0DAE";
		public const string GLOBAL_TASK_MANAGER = "F27EC3EDCC174e66B2C21C45FC49FB7E";
		public const string LOCAL_TASK_MANAGER = "7175289DB7C24f84A0A13D08A64C9F6E";
		public const string TASK_TIMEOUT = "3136f5dbd4a949aba98bbf2e9a3aa6e4";
		public const string DC1000_ADAPTER = "2ec6f270e9bf495da852a3b535ec41a8";
		public const string EVENT = "ee4f5401daad4746acfeff144bf9102f";
	    public const string ALWAYS_ON_IP_ADAPTER = "98fc7821d7144d02a04b5e1be0c8f0c8";
        public const string ATM = "99ef24aa74be421cb5b07dd0c8a4fb31";
        public const string NES_SYSTEM_SOFTWARE = "67685b8ae84549019948988166037791";

        [Obsolete("v4 - no longer used")]
        public const string CISCO_ACCESS_SERVER_AUTHENTICATION_ENGINE = "A8E15AB4EA23485cB7BBADDCDCBFC67F";   // v4 -- no longer used

        [Obsolete("v4 - no longer used")]
        public const string CISCO_ACCESS_SERVER_ADAPTER = "6a85fb5e63f045c7b71cc7ede96f0559";  // v4 -- no longer used.

        [Obsolete("v4.1 - no longer used")]
        public const string RAS_ADAPTER = "8be59f6536584f709b0625198905a906";

        [Obsolete("v5.2 - no longer used")]
        public const string RECALC = "485939F521C64e029CA139B733C26636";

        [Obsolete("v5.2 - no longer used")]
        public const string AGGREGATION = "3237604F96A24fbf974D05C3749BE098";

        [Obsolete("v5.2 - no longer used")]
        public const string ILON_100_ADAPTER = "D088035E1BF54D109CFB9A7B4BA96DE2";
	}

	// Contains the list of statuses used by the Task Distribution Framework
	public class TaskStatusTypes
	{
		public const string WAITING = "3BC28FD1B008402b808C3FAD58D1EF02";
		public const string IN_PROGRESS = "2850E772024F418295571C71F9E8F93C";
		public const string SLEEPING = "094B25FA426A41dc82968D48F6F87127";
		public const string ERROR = "EEA3A69129524261B2873B7E77736B5D";
	}

	// Contains the list of types of attributes supported
	public class AttributeTypes
	{
		public const string DEFINED = "40074C80216D4effB0347EBBA6A09A90";
		public const string STRING = "E884BDCCF0374a1380D6543FEFD6D85C";
		public const string NUMERIC = "9479C1CCAA9A40ebA1167981A315EAA5";
	}

	// Contains the list of external service return codes
	public class ExternalServiceReturnCodes
	{
		public const string SUCCEEDED = "A92A7EBEE897499fA8B06D5FE94B8A30";
        public const string FAILURE = "5720769475E544c0A24A0C34CFF35A54";
		public const string INVALID_XML_PARAMETERS_FORMAT = "8a27cb8afd464686a3893f4f8db26540";
		public const string INVALID_PARAMETERS_NODE_MISSING = "C74C9E88E7904920B8164E15D363115C";

		public const string NO_DATA_TO_SET = "16748a3688b74d62bfbeb84522a97d15";
		public const string INVALID_EVENT_LOG_TYPE_ID = "c5fad651abed4f3f9200b049298b97fe";
		public const string DUPLICATE_EVENT_LOG_TYPE_ID = "d658dfd1b44d4ec6a21b9fa86727518b";

		public const string INVALID_NONRECURRING_DATE_ACTION = "d453b84b6e5a43e2b1c320535a50e758";
		public const string INVALID_NONRECURRING_DATE_DATE = "d3f8bee5346b45fb9107bb2473afcaf5";
		public const string INVALID_NONRECURRING_DATE_COUNT = "e53d528211b6451e92e9ba77c794c267";
		public const string INVALID_NONRECURRING_DATE_PERFORM_BILLING_READ = "2654d847c4444b768e6a95dfcca1070e";
		public const string INVALID_NONRECURRING_DATE_PERFORM_DEMAND_RESET = "f913d53e59f54764940a03c45c586f8b";
		public const string INVALID_DAY_SCHEDULES_SWITCH_COUNT = "ca98911b80bb4c18a7d18cc2c78178af";

		//added in v5.1
		public const string DEVICE_MCN_CONFIGURATION_UNSUPPORTED = "48e11226aaec4814abcb7b84e0e447e0";
		public const string HARDWARE_CONFIGURATION_UNKNOWN = "2e55e57d197148d984d6e9c0ac68eaba";
		public const string INVALID_POWER_LINE_CARRIER_MODE_TRANSITION = "c4b02a5ea57742a3af258b89793cfe67";
		public const string INVALID_POWER_LINE_CARRIER_MODE = "babb263a492f48f098340aa5d6195bee";
		public const string INVALID_SYNCHRONIZED_TRANSITION_TIMEOUT = "346fb1aeca4a420a9cc72cd3fa793854";
		public const string INVALID_WAN_ADAPTER_STATUS_DURING_EXTERNAL_MODE = "4e5273a14fdc4259a6cdb0f7bd8cb49c";
		public const string INVALID_COS_APP_DELTA_LP_SETS = "13528ea11eee42a98ed4d9146e5d1800";

        //added in v5
		public const string INVALID_MOVE_LOCAL_METER = "293a805c582b49da8f2d6820022e904a";
        public const string INVALID_NODE_MISSING = "0f4931eefd0442928c061709d3f905ba";
		public const string INVALID_RETURN_HARDWARE_CONFIGURATION_OPTION_BIT_OFFSET = "4d42b06d27e34c28ac5975b42c2d1a61";
		public const string INVALID_RETURN_HARDWARE_CONFIGURATION_OPTION_BIT_LENGTH = "0b2ed6bfa66f4f66b8fe5c9c36d90c8e";
		public const string INVALID_DEVICE_ID = "404CD23E49F245e8BDCB7009A8D4F912";
        public const string INVALID_MBUS_INDEX = "1f82f39409954fdc94be31c01e203e82";
        public const string INVALID_GROUP_ACTION = "54223df71b1f429e89043ca64d36d265";
		public const string INVALID_TYPE_OF_ENTITY_TYPE_ID = "ac633307cde34118b009cdffaf220634";

        public const string UNSUPPORTED_DATA_SET_ID = "05984d8b895b4ac9bda2cbcf7fa3e54d";

		public const string INVALID_DEVICE_DATAPOINT_ASSIGNMENT = "61c29309a4b442b4afdbdd67f21473e4";
		public const string INVALID_DEVICE_ID_ASSIGNMENT = "030061a6e1154989affb016cde162f89";

		public const string INVALID_HIERARCHY_LEVEL_MEMBER_ID_NODE_MISSING = "A64B4C43AD6348a9891AE1294D58743B";
		public const string INVALID_NAME_NODE_MISSING = "D80245C7516141a182FA563E103DD45C";
		public const string HIERARCHY_LEVEL_MEMBER_ID_BLANK = "CAA4811A74724d3c98476AC18671A8F2";
		public const string INVALID_DEVICE_NAME_BLANK = "BA079E1BDDD347b2BA7091896A81F1CC";
		public const string DUPLICATE_DEVICE_NAME = "9bf9671d1e8440849a0fb2151ffa3087";
		public const string INVALID_ATTRIBUTE_ID = "897961760A134a738395C10806C5A6C6";
		public const string INVALID_ATTRIBUTE_VALUE = "20878FC91EC347288053ACE7AC85CBDF";
		public const string INVALID_NUMERIC_ATTRIBUTE_VALUE = "079a5b64224545ce834b78e12fd96f68";
		public const string INVALID_ATTRIBUTE_TYPE_ID = "2dc9a71395a74fa7895a4c941f3abd0d";
		public const string INVALID_ATTRIBUTE_DEFINEDVALUE_ID = "3ac41fafd1aa4f9f86a822d1c1092e33";
		public const string INVALID_ATTRIBUTE_NAME_DUPLICATE = "cebbadeadbce45cd8efe0383c94fbe6b";
		public const string INVALID_ATTRIBUTE_DEFINEDVALUE_NAME_DUPLICATE = "d697841dcb9549b7874046a6f3c9c180";
		public const string INVALID_ATTRIBUTE_NAME_BLANK = "55906f87e5a34fb9bebed0715a89b7bc";
		public const string INVALID_ATTRIBUTE_DEFINEDVALUE_NAME_BLANK = "9be3f52fbfd94200bc06a8e91dafc16b";
		public const string INVALID_ATTRIBUTE_ID_NOT_DEFINED = "fa1d9b6f08ba405fa48263f64259250b";
		public const string INVALID_ATTRIBUTE_ID_NOT_STRING = "6c821e86e00c4a148e1986e288fe3b83";
		public const string INVALID_ATTRIBUTE_ID_NOT_NUMERIC = "5a74dc403b8842a5aa800d6d03be66d1";
		public const string DUPLICATE_HIERARCHY_ASSIGNMENT = "DEFB4EC0F91B4daf9F72C71122353556";
		public const string INVALID_DATA_POINT_ID = "28050E0C73624def90918FDE03F11D8D";
		public const string INVALID_HIERARCHY_LEVEL_MEMBER_ID = "BB6CB61FBF2B486dB43F00410AD60A05";
		public const string INVALID_VARIABLE_TYPES_XML = "40153C219C1D43f69B4994168A38DF38";
		public const string INVALID_VARIABLE_TYPES_XML_EMPTY = "B08C2B32C9E142c089F0CC74960E937D";
		public const string BLANK_VARIABLE_NAME = "40830F08C7DC4aafA76436228BD38D01";
		public const string VARIABLE_TYPE_IN_USE = "3066126399EF40d2BF882D8CADA35178";
		public const string INVALID_SECURITY_KEY = "1EE016C8AA2C4f709FFA73655CEFD9FB";
		public const string INVALID_TYPE_CATEGORY = "1D2B8582C2C545f48963719914949A1D";
		public const string INVALID_TYPE_ID = "ADC4E0656DA446b58F71F4F04A046905";
		public const string INVALID_DATA_TYPE_ID = "E77B79D1D84A4f7996EF1A82679F42E9";
		public const string INVALID_USER_AUTHENTICATION_TYPE_ID = "5C5B36E9EAAF4234988D9CB1B4D9B784";
		public const string INVALID_NUMERIC_RANGE = "C3E8BBBE4FC7415aA4BD78A8D1197BF8";
		public const string INVALID_NUMERIC_MIN = "4c5d09f9f4ef4346beaf23e731ed6ac6";
		public const string INVALID_NUMERIC_MAX = "d318c7277ab841ee963df4a05a1d0cd6";
		public const string INVALID_DELETE_DATA_POINTS_NODE_MISSING = "368B2F3EC6AD42deB2603EBB73B64F1B";
		public const string INVALID_DELETE_DATA_POINTS_ID = "DD548AFB4D71457982C0A544897D2233";

		public const string PARTIAL_SUCCESS = "7651E04B049E4dddAFA408370D0E80D0";
		public const string INVALID_HIERARCHY_LEVEL_ID = "e36cf2b913e94595bb183e734b6b6044";
		public const string INVALID_HIERARCHY_ID = "d7c91ca786e34586a0a56e0fb074cf65";
        public const string INVALID_HIERARCHY_NAME_DUPLICATE = "42f0450cf5a8432b9a1a07ab43f1e271";
		public const string INVALID_HIERARCHY_LEVEL_NAME_DUPLICATE = "3fcccf0501804b6ea53d1db55b73661d";
		public const string INVALID_HIERARCHY_LEVEL_MEMBER_NAME_DUPLICATE = "d0bf846170664a3c9b36f03e87fbf329";
		public const string INVALID_HIERARCHY_LEVEL_MULTIPLE_CHILDREN = "9afc0d0cd7184689ab9a87af6c4f01e7";
		public const string INVALID_HIERARCHY_NAME_BLANK = "c4e2fd9a15ba48d9868bafb93495e6b9";
		public const string INVALID_HIERARCHY_LEVEL_NAME_BLANK = "e16215e9575e47b3b7e8357203d14ffe";
		public const string INVALID_HIERARCHY_LEVEL_MEMBER_NAME_BLANK = "052ea988303f4514921ed7614d9c5c9c";
		public const string INVALID_HIERARCHY_LEVEL_PARENT_ID = "dfae316306dc48c7ab572e25bc5ba25d";
		public const string INVALID_HIERARCHY_LEVEL_MEMBER_PARENT_ID = "2a8626be8fa9443db5dc06d503122bf9";
		// Level specified for the Hierarchy Level Member as the parent is not valid for this member.
		// May be because the Level Member specified as the parent is lower in the hierarchy tree than the
		// member or because the level member specified is not in the same hierarchy.
		public const string INVALID_HIERARCHY_LEVEL_MEMBER_PARENT_LEVEL = "8707055784ed4750a7608139567befd9";
		public const string INVALID_HIERARCHY_RESTRICTION_TYPE_ID = "82e1045be1634e099a25870869223bb7";
		public const string INVALID_HIERARCHY_LEVEL_MEMBER_NAME_CHARACTERS = "7b739c7ec3bc49f7b15a6071a2accc59";
		public const string INVALID_HIERARCHY_DELETION_TYPE_ID = "f34baa69f29143cca9e07a67d2bd9913";
		public const string INVALID_HIERARCHY_LEVEL_NAME_CHARACTERS = "188beda317f2433fa10ae56bc0ca829c";
		public const string INVALID_HIERARCHY_LEVEL_STRUCTURE = "23ebfa15e548474f907627b1def3aced";

		public const string INVALID_RECURRENCE_TYPE_ID = "13f78ae7507d4e76b5c040ddeac989ef";
		public const string INVALID_STATUS_TYPE_ID = "629C307080974772BB803A49CB7A2CBF";
		public const string INVALID_SOURCE_TYPE_ID = "67DD454B0AB74bc49AB3F5EC5968EB8C";
		public const string INVALID_CALCULATION_TYPE_ID = "DC945387F85F43df8911B35F2FDEF2B8";
		public const string INVALID_VARIABLE_TYPE_ID = "A831975F83EC42b9BEAF8143FF768B93";
		public const string INVALID_VARIABLE_TYPE_COMPONENT_ID = "dd630af14f8f4434bd2eca8147fc9777";
		public const string INVALID_TIMEZONE_ID = "C277E2D657D2457a8FE07FFE1A28A7D9";
		public const string INVALID_TIMEZONE_ID_NODE_MISSING = "0ba026b846094194b289815df57a8b8d";
		public const string INVALID_STARTDATE = "06c3a04fd0a649b9a5b4e9e10523315f";
		public const string INVALID_DEVICE_ID_DEVICE_NOT_TIED_TO_GATEWAY_ID = "A8C8417A75A04223BC89110ACB260A4E";
		public const string INVALID_DATAPOINT_MINMAX_RANGE = "093C0AC65ED74e7390D4DF152D487CF7";
		public const string INVALID_ENDDATE = "52e68e4510bb44209482cdcdf29afe11";
		public const string INVALID_PRIORITY = "aa71aac765f042fa984c80d7fd505295";
		public const string INVALID_PRIORITY_NOT_SUPPORTED = "8f01896567e8421e973fd31ade64af1c";
		public const string COMPONENT_NOT_FOUND = "628255DA087245a98575F949F7646BD3";
		public const string INVALID_STOPPED_DATE = "8a09e6008380482ea8829dc5fa32c0ee";
		public const string INVALID_DATAPOINT_RESTRICTION_TYPE_ID = "665f22df55d54c97a20590609672c11f";
		public const string INVALID_DATAPOINTVALUE_STATUS_TYPE_ID = "eac58c72de6a49f59cf7c9ea8227e3a3";
		public const string INVALID_DATAPOINT_VALUE = "46170a98b734415f9cd3e57b2f296d45";
		public const string INVALID_GATEWAY_COMBINATION = "3601D0EF74BF4abb9C2B26521E830642";
		public const string INVALID_DATAPOINT_SCHEDULE_ASSIGNMENT = "8a53448446e949e4a9d54ea257e1af2d";
		public const string INVALID_DATAPOINT_SCHEDULE_DELETE = "426a668af29348c2aadad749f0eda7ec";
		public const string INVALID_METER_SOFTWARE_VERSION_NOT_SUPPORTED = "d733335439e64ac8b4808a2b7e63cecb";
		public const string INVALID_TRACKING_ID = "a323e3cb19754ee7a32ae82313fe4463";
		public const string INVALID_TRACKING_ID_NODE_MISSING = "3f4e5309fc1d4cf59de87aca5354b992";
		public const string INVALID_KEY_CATEGORY_TYPE_ID_NODE_MISSING = "fe4fd8b284bb43ffb311ec72ab294965";
		public const string INVALID_KEY_CATEGORY_TYPE_ID = "d86013733447416eae3c8e7c48cc5322";
		public const string INVALID_KEY_CATEGORIES_NODE_MISSING = "b225b89ac9d746af85d778f8f2a079c4";
		public const string INVALID_KEY_CATEGORY_NODE_MISSING = "78c1a1b10f7e43cc9dab91254426f90e";
		public const string INVALID_DATA_CONTENT_RETURN_TYPE_ID_NODE_MISSING = "447f53c9f8d64c19bb3493a5a960186f";
		public const string INVALID_DATA_CONTENT_RETURN_TYPE_ID = "7970a954dd584e2faf188b440d87e98b";
		public const string UNSUPPORTED_DATA_CONTENT_RETURN_TYPE_ID = "06e0462acb394dbb8db03ff34562670f";
		public const string NO_DISCOVERY_DATA_FOUND = "66ccd40244544474996dd15e83363449";
		public const string INVALID_VERSION_ID_MATCH_TYPE_ID = "c05cd239cdcf413f88639acd509bd16a";

        //Attribute Manager
        public const string INVALID_ATTRIBUTE_NAME_MATCH_TYPE_ID = "380ca03a08f240888ab81e2a84557572";

        // Gateway Manager
		public const string INVALID_GATEWAY_ID = "94f7645740554fcfaca7dd9816545e6f";
		public const string INVALID_GATEWAY_TYPE_ID = "49988335ee81476c87e66db8145f6841";
		public const string INVALID_GATEWAY_TYPE_ID_NODE_MISSING = "22f616a10b234c5ba8cfa38cb3d77f75";
		public const string INVALID_GATEWAY_TEMPLATE_PARAMETER_ID = "6c36061b2afb45ac99670de92195aef4";
		public const string INVALID_GATEWAY_TEMPLATE_NAME = "750321b796794c339906ab8bec177817";
		public const string DUPLICATE_GATEWAY_TEMPLATE_NAME = "8638fbd3afd549e6b14f70e1c33b317c";
		public const string INVALID_GATEWAY_STATUS_TYPE_ID = "908e6efc342744e3bf48a36dce4b837b";
		public const string INVALID_GATEWAY_STATUS_TYPE_ID_NODE_MISSING = "59f6483b144f461ea6681b7ba87733c6";
		public const string INVALID_ILON100_DATA_LOG_RETRIEVAL_TYPE_ID = "bc3dae66b7f1478087713ceba6d6791c";
		public const string INVALID_ILON100_DATA_LOG_USED_PARAMETER_ID = "35fb3d1275b84de0aa782225fe390943";

		public const string INVALID_GATEWAY_NAME = "5e0d7db37d854c518c1afd195fad8ebd";
		public const string INVALID_GATEWAY_VARIABLE_NAME = "2bd91ef3c612457ab54eb8e104165ba0";
		public const string INVALID_GATEWAY_VARIABLE_TYPE_COMPONENT_ID = "42879ed14a674dd48bce28c897f12e95";
		public const string INVALID_TAKE_HIERARCHIES_PARAMETER_ID = "980b61c3666447efbc941ac72769da96";

		public const string INVALID_ATTRIBUTE_VALUE_NODE_MISSING = "6ce8a02f4c5d40d5a213e7f6efbca018";
		public const string INVALID_ATTRIBUTE_ID_NODE_MISSING = "31b76abe7a264b14810adb18d0648d23";
		public const string INVALID_DATETIME = "8dd2304a495f4a46b1b875bc54299f64";
		public const string INVALID_GATEWAY_TEMPLATE_ID = "07cad359cc1142e5a2b97f0e6bf7aee9";
		public const string REGISTRATION_LIMIT_REACHED = "b3e649a5dfdb49ac9bbfa2c573192400";
		public const string INVALID_GATEWAY_NAME_BLANK = "ff46053a6ea74f4392fdc2317ee83e8e";
		public const string DEVICES_IN_USE = "6573c9ae3d2a4797807b9ad26910bd81";
		public const string UNSUPPORTED_GATEWAY_TYPE_ID = "03cd80ebc0014e59a08fea9abae59695";
		public const string INVALID_PPP_USERNAME = "2bd9054a5be8434785446ebbc6a10460";
		public const string INVALID_PPP_USERNAME_BLANK = "7659b5b48e0d41159321ba012f3e33f6";
		public const string INVALID_PPP_USERNAME_DUPLICATE = "ec945e116bb04a92a5435be263fac195";
		public const string INVALID_PPP_PASSWORD = "9c3cd24fe69c4bc084a5e3650d2a8f41";
		public const string INVALID_PPP_PASSWORD_BLANK = "4765fa6ea06944469ad2b1a23e5740a2";
        public const string INVALID_GATEWAY_COMMUNICATION_HISTORY_ID = "8ee098fc32d0443ba3d4770eeeed60a7";
		public const string INVALID_GATEWAY_COMMUNICATION_HISTORY_INITIATED_START_DATETIME = "66bb1eb1223545ed99ffc155a666ff02";
        public const string INVALID_GATEWAY_COMMUNICATION_HISTORY_INITIATED_END_DATETIME = "1e7f6ba5799849078d2f811c9a07ed7d";
		public const string INVALID_GATEWAY_COMMUNICATION_HISTORY_INITIATED_DATETIME_RANGE = "84d2eedd579345148033942d0057ac50";
		public const string INVALID_GATEWAY_COMMUNICATION_HISTORY_REQUESTED_START_DATETIME = "b8307e406e764a59a7f376aeccf2aea0";
		public const string INVALID_GATEWAY_COMMUNICATION_HISTORY_REQUESTED_END_DATETIME = "f6c5ce1c9e9148c1b2a3729f252a1b8e";
		public const string INVALID_GATEWAY_COMMUNICATION_HISTORY_REQUESTED_DATETIME_RANGE = "1dbae26dc8cd4f9983177b130b6fb632";
		public const string INVALID_GATEWAY_COMMUNICATION_HISTORY_STATUS_TYPE_ID = "48148d9463784af283bb9db387024847";
		public const string INVALID_COMMUNICATION_REQUEST_TYPE_ID = "9eac04040e4c4119acd50e91bdfa5181";
		public const string INVALID_COMMUNICATION_REQUEST_TYPE_ID_NODE_MISSING = "cd2f47c9d18a469899062dd76d97ef68";
        public const string INVALID_GATEWAY_COMMUNICATION_TYPE_ID = "5AE782E09FB44a67AE12F504AFED62A5";
		public const string INVALID_GATEWAY_COMMUNICATION_TYPE_ID_NODE_MISSING = "4ceb7ba5e4ad4ba398acb8807774a259";
		public const string GATEWAY_INITIATED_COMMUNICATION_IN_PROGRESS = "04be1b61d83d41da80271eb04f48d759";
		public const string INVALID_ACTIVATION_DATETIME = "398748e1cb9546a1876aa797569b8809";
		public const string GATEWAY_NOT_ENABLED = "57f395ab1b2c47bbb149d73a604c1784";
		public const string INVALID_IP_ADDRESS = "c7d9858b109d403f9dc64597555c3f57";
		public const string INVALID_MODEM_TYPE = "76c4a668b589429087141cc7a4c865cc";
		public const string INVALID_MODEM_INIT_STRING = "716012bd9a2b47099c0e61efd72b87bf";
		public const string INVALID_SEARCH_CRITERIA = "36f5a11454d9406a85a952fc30a50cef";
		public const string INVALID_ATTRIBUTE_NODE_MISSING = "8fe91c67cc6e48dab1b46fa06e70cc01";
		public const string INVALID_HIERARCHY_LEVEL_MEMBER_NODE_MISSING = "f9b4827652b346b9abe84d9e5f8d0518";
		public const string INVALID_DC1000_IP_ADDRESS = "a0d9a331d5174af083eb4016b09f6a1f";
		public const string INVALID_IP_ADDRESS_DUPLICATE = "1a54b6481ed84309b85dc664b265eb87";
		public const string INVALID_IP_ADDRESS_OUT_OF_RANGE = "8ed705cba33c487ebc661f18732cf9ba";
		public const string INVALID_GATEWAY_TO_SERVER_PHONE_NUMBER_1 = "4db0bb244d3143fd8fe3e24a320f2fed";
		public const string INVALID_GATEWAY_TO_SERVER_PHONE_NUMBER_2 = "e1603cb62dc543ce811fd5eb6974f650";
		public const string INVALID_ENABLE_TOTAL_ENERGY_VALUE = "643e8bb2baf74cd68d0d84c8cc13eebd";
		public const string INVALID_APPLICATION_LEVEL_AUTHENTICATION_VALUE = "51f12cc5c21347948f8f01f72295bcc8";
		public const string INVALID_SCHEDULE_NODE_MISSING = "f2fb555a43c54fae93c71b8dfdbb813a";
		public const string INVALID_SCHEDULE_ID_NODE_MISSING = "1d6f7a9a0adc4496b7cd7d4e426579b9";
		public const string INVALID_GATEWAY_DISCONNECT_COMMAND = "e241911a6f3d40a7a29be480e6d10ef6";
		public const string INVALID_GATEWAY_DISCONNECT_IN_PROGRESS = "2caccf187b814d0fb93b0699389deb63";
		public const string UNSUPPORTED_GATEWAY_COMMUNICATION_REQUEST_TYPE_ID = "2eccc753de4240cda1f511b65b3daeef";
		public const string INVALID_GATEWAY_SOFTWARE_VERSION_NOT_SUPPORTED = "4de82e8205854a6bb064f2914042208e";
		public const string INVALID_SECURITY_OPTIONS_NODE_MISSING = "c636f6b9b6f94966b3a960253c016848";
		public const string INVALID_PPP_USERNAME_AND_PASSWORD_MISSING = "a00bf7180912475593f8dcb1fa78d1f6";
		public const string INVALID_NEURON_ID_MISSING = "cec69c1ef6b34e22a986a42367b621ee";
		public const string INVALID_PPP_USERNAME_NODE_MISSING = "bd4e3987ef92475e84248d5d58b2adcb";
		public const string INVALID_PPP_PASSWORD_NODE_MISSING = "dbcae2ef87184e65b09cd31c9db098c0";
		public const string GATEWAY_DISABLED = "EC327A569B0F4680AF59AF17ED01BAAD";
		public const string INVALID_CONTROL_RELAY_STATUS_TYPE_ID = "37381b497105406993fdaa657393ac13";
		public const string INVALID_MAXIMUM_POWER_LEVEL_ENABLE_STATUS_ID = "d4038663555a4165ab0c93da4d4ba37c";
		public const string INVALID_ENABLE_MAXIMUM_POWER_NODE_MISSING = "41ae2f65d9de46338f6e5518505bd02c";
		public const string INITIAL_GATEWAY_COMMUNICATION_NOT_COMPLETE = "29DE5712AE354b8cB855DCFF84AD0B08";
		public const string INVALID_LAST_GATEWAY_TO_DEVICE_COMMUNICATION_STATUS_TYPE_ID = "ac2afd7bc52b4817a0c165b0a97e3af4";
		public const string MULTIPLE_GATEWAYS_WITH_SERIAL_NUMBER = "3464dc8a88f9429899119602d1d78672";
		public const string INVALID_PRIORITY_NODE_MISSING = "39b9bde7b5b446e39b8ff770efb19722";
		public const string UNSUPPORTED_GATEWAY_COMMUNICATION_TYPE_ID = "9d4928b8aacc44488328714977ca481c";
		public const string INVALID_SERVER_ROUTABLE_IP_ADDRESS_NODE_MISSING = "6bb35fc1e6cb4d3487c26780be39a8c8";
		public const string WAN_CONFIGURATION_ASSIGNMENT_LIMIT_REACHED = "a2e793927f824f86b92af3637c3179db";
		public const string INVALID_PRIORITY_DUPLICATE = "75c795ad3f1f4f91a339ee62d05a14ad";
		public const string INVALID_ASSIGNMENT_NAME_NODE_MISSING = "57e4a1a3e7b4490c8783cd3775b9bb51";
		public const string INVALID_ASSIGNMENT_NAME = "d9b5367df22c4e0698b00720dd690d34";
		public const string INVALID_ASSIGNMENT_NAME_DUPLICATE = "2b589c04e35e4f24afbd7a99fad242b2";
		public const string INVALID_SERVER_ROUTABLE_IP_ADDRESS = "9cead96db1de40afaf1f93bc88ca9c03";
		public const string INVALID_SERVER_ROUTABLE_IP_ADDRESS_DUPLICATE = "9485e37b95db497d899b7d0c17e83849";
		public const string INVALID_DEFAULT_ASSIGNMENT_NAME_NOT_FOUND = "fe14d16c565648199e128f286ea53beb";
		public const string INVALID_WAN_CONFIGURATION_ASSIGNMENT = "1262019dc5aa45e3a84088b76b630660";
		public const string UNSUPPORTED_WAN_CONFIGURATION_COMMUNICATION_TYPE_ID = "2ddccbd83999407ab69c6a5992c8cabf";
		public const string INVALID_IP_ADDRESS_NODE_MISSING = "62ae26893b5d4fbba9851558c71fb545";
        //same value as INVALID_IP_ADDRESS_NODE_MISSING, used for register.
        public const string INVALID_WAN_CONFIGURATION_ASSIGNMENTS_NODE_MISSING = "62ae26893b5d4fbba9851558c71fb545";
        //
		public const string INVALID_DEFAULT_WAN_CONFIGURATION_NOT_FOUND = "2c3e5a2530f6454eb575a36bff4453a0";
		public const string INVALID_WAN_CONFIGURATION_ASSIGNMENT_NODE_MISSING = "32012e51200542428605453791e1b4c9";
		public const string INVALID_WAN_CONFIGURATION_ID_NODE_MISSING = "e56bd3c319bb4443b9660721f8abdc03";
		public const string INVALID_NUMBER_OF_GATEWAY_WAN_CONFIGURATIONS = "C1F1422069534daaA56AC66642F0E1EF";
		public const string INVALID_WAN_CONFIGURATION_NODE_MISSING = "C943D2DCD52744e29D7D21508A92C7C5";
		public const string INVALID_CURRENT_WAN_NAME_NODE_MISSING = "6ABC17FB2ECE439dBABC667444D3ED7F";
		public const string INVALID_CURRENT_WAN_NAME = "386C97F587C2437588A74C8DFA150E2A";
		public const string INVALID_WAN_CONFIGURATION_NAME_NODE_MISSING = "50EF37AD28E949179AEB5038B67F144D";
		public const string INVALID_ENCRYPTION_STATUS_TYPE_ID_NODE_MISSING = "11C2DF647BD04448BAA1E8CEC56BE610";
		public const string INVALID_ENCRYPTION_STATUS_TYPE_ID = "c46190a5c8d4487f98fa8d097c5fda45";
		public const string INVALID_APPLICATION_LEVEL_AUTHENTICATION_STATUS_TYPE_ID_NODE_MISSING = "F1B074957DEC49319D2B19055A6782D9";
		public const string INVALID_APPLICATION_LEVEL_AUTHENTICATION_STATUS_TYPE_ID = "01E4541008F94b7fA0F523B15FD3C16E";
		public const string INVALID_GATEWAY_WAN_CONFIGURATION_STATUS_TYPE_ID_NODE_MISSING = "D7E19F3E71E34aeaB7572BECC8DCF5F1";
		public const string INVALID_GATEWAY_WAN_CONFIGURATION_STATUS_TYPE_ID = "67050E258831441cA2402B63C665125C";
		public const string ENABLED_GATEWAY_WAN_CONFIGURATION_MISSING = "C2AD813B0E374f17BAEB56648A534ED3";
		public const string INVALID_MODEM_INITIALIZATION_STRING_NODE_MISSING = "41D9065AF6BD40df8A42BA09BF344B6C";
		public const string INVALID_GATEWAY_WAN_CONFIGURATION_PRIORITY_NODE_MISSING = "4FA7D2852A884adbA944E84C258E521D";
		public const string INVALID_GATEWAY_WAN_CONFIGURATION_PRIORITY = "68EC23C37B644dd5B0DBDC89CAC3D7AA";
		public const string DUPLICATE_GATEWAY_WAN_CONFIGURATION_PRIORITY = "7D36DFEB18604348A80B7D29A5386301";
		public const string INVALID_GATEWAY_WAN_CONFIGURATION_PHONE_NUMBER_NODE_MISSING = "A78576F4A0414b6d90EC83113D614823";
		public const string INVALID_GATEWAY_WAN_CONFIGURATION_PHONE_NUMBER = "8D5819255CBC4b71848BC1D337BE8747";
		public const string INVALID_GATEWAY_WAN_CONFIGURATION_IP_ADDRESS_NODE_MISSING = "89891A79DEF54f4eB3D3FB9823CE643C";
		public const string INVALID_GATEWAY_WAN_CONFIGURATION_IP_ADDRESS = "C470ECD68E1B4e37BB95655F72EE7CCA";
		public const string INVALID_OUTBOUND_CHAP_STATUS_TYPE_ID_NODE_MISSING = "9B96AF763849430e89589F1A17E6565E";
		public const string INVALID_OUTBOUND_CHAP_STATUS_TYPE_ID = "282A5E34EFCC45f6BD2C18BF6740EB34";
		public const string INVALID_INBOUND_CHAP_STATUS_TYPE_ID_NODE_MISSING = "5AB7FD909D654ac58FFBFEF79837AC71";
		public const string INVALID_INBOUND_CHAP_STATUS_TYPE_ID = "1639C493D5A84193B863F0E9A340F7CC";
		public const string INVALID_PAP_STATUS_TYPE_ID_NODE_MISSING = "45EF1126DB0D4f8cBAC8B10EED738D46";
		public const string INVALID_PAP_STATUS_TYPE_ID = "C9A2A094E7F34b40837543CFF0345727";
		public const string INVALID_MODEM_MONITOR_INTERVAL_NODE_MISSING = "63635B0FCDFA4240A7B1FC036912809A";
		public const string INVALID_MODEM_MONITOR_INTERVAL = "ECCF3020777D48ffB925792EC982A972";
		public const string INVALID_CALL_RETRY_WAIT_NODE_MISSING = "1FF7193F1B3E42c3BF9EC6B66AF57C0E";
		public const string INVALID_CALL_RETRY_WAIT = "F6F0E86B1E794da8A80F3AEC4122FCD0";
		public const string INVALID_SWITCH_LIMIT_NODE_MISSING = "2577B40570C4444185DBF1CD7078D01C";
		public const string INVALID_SWITCH_LIMIT = "60D8ED34E61E4bd486BAA45A503DDF72";
		public const string INVALID_PING_NODE_MISSING = "488E9C9655C44a85ADB9849FB47DCE91";
		public const string INVALID_PING_IP_ADDRESS_NODE_MISSING = "5B64A208856C48b38ABBF93A159B245F";
		public const string INVALID_PING_IP_ADDRESS = "3721D37348904c648BD93BBEDA4C1FD5";
		public const string INVALID_PING_INTERVAL_NODE_MISSING = "50871F2AB8DE440bAD8EDCCC1AEF1DF7";
		public const string INVALID_PING_INTERVAL = "D810DA96725B4fe998E56A637BE31BE2";
		public const string INVALID_MODEM_CONFIGURATION_NODE_MISSING = "2E1368F6E20349c998363F9FF1F05C2A";
		public const string INVALID_PORT_SPEED_TYPE_ID_NODE_MISSING = "8D7695620BB746fa971171497BB02DDB";
		public const string INVALID_PORT_SPEED_TYPE_ID = "1CCA6932852C4a908C4278DCDE7CC20C";
		public const string INVALID_MODEM_COMMAND_TYPE_ID_NODE_MISSING = "24CAE1497C8E470f8680493924810ACA";
		public const string INVALID_MODEM_COMMAND_TYPE_ID = "C90286A20BA74ca694B6B22E459D5E03";
		public const string INVALID_MODEM_CONNECT_TYPE_ID_NODE_MISSING = "5BF79CF314814111B16BCA4ECFB0377D";
		public const string INVALID_MODEM_CONNECT_TYPE_ID = "20E4401592614481BF93E54D4AD2B55B";
		public const string INVALID_PPP_CONNECTION_TYPE_ID_NODE_MISSING = "94AA692E4806418485D23E9D6E7AE859";
		public const string INVALID_PPP_CONNECTION_TYPE_ID = "FDBB67C6F7DA4c00A2758BFAE38730BF";
		public const string INVALID_MODEM_ANSWER_TYPE_ID_NODE_MISSING = "70AA1E65C6A143c0B232767F977C5179";
		public const string INVALID_MODEM_ANSWER_TYPE_ID = "F8F286063AE04b489042B66855570F05";
		public const string INVALID_ISP_NODE_MISSING = "2EE9B56C6C4F4ee3AAF7538E479FFE50";
		public const string INVALID_GATEWAY_AUTHENTICATION_TYPE_ID_NODE_MISSING = "3183577DAEC6453cAB9AE3EDB8066825";
		public const string INVALID_GATEWAY_AUTHENTICATION_TYPE_ID = "BFE14A12B2264f22B3A1D950AACB09F2";
		public const string INVALID_ISP_USERNAME_NODE_MISSING = "9FA85CD930344934B2EB4642D11719DB";
		public const string INVALID_ISP_USERNAME = "7777AA7E43A04381944D1DBF45F71EBD";
		public const string INVALID_ISP_PASSWORD_NODE_MISSING = "5B2691BB7E83422c94F7B4A113E9EF23";
		public const string INVALID_ISP_PASSWORD = "08CC7699AB314dd3AE2ADFB1E45623E9";
        public const string GATEWAY_DEVICE_LIMIT_KEY_NOT_FOUND = "2b6d870b580642da85104741a8c951b6";
		public const string INVALID_FORCE_DISCONNECT = "9cec9b7b54ba4bf1a6249cd5637f700a";
		public const string INVALID_FORCE_DISCONNECT_COMMAND = "9e7acbfa28c14dbfa7e1f4779421d53e";
		public const string INVALID_NEURON_ID_NOT_FOUND = "fec693fb44c34aad985c3a5a3dfad2bd";
		public const string INVALID_SERIAL_NUMBER_NOT_FOUND = "f46485aa64834a40a4ed264210dfa043";
        public const string INVALID_LAST_CONNECTION_START_DATE_TIME = "318b646ff4e44d138fc08f6234401fd4";
		public const string INVALID_LAST_CONNECTION_END_DATE_TIME = "a2e8f4fb965b40b49604a41c3a29e8ba";
		public const string INVALID_LAST_CONNECTION_DATE_TIME_RANGE = "ca15df6251b14e9192d7adf474f9aaf3";
		public const string INVALID_DELETE_CRITERIA = "15849de4ab864381a6d437fef2bd1f7b";
		public const string INVALID_CONNECT_NO_WAN_CONFIGURATION_ASSIGNED = "6942116e7f224fda814df1becb1c2cca";
        public const string INVALID_RESOURCES_NODE_MISSING = "c41abdd276ed49899d95f5602d0ddc37";
        public const string INVALID_RESOURCE_NODE_MISSING = "9b088f79d7094b4690cc9ec5267e95be";
        public const string INVALID_RESOURCE_ID_NODE_MISSING = "04e128b9645442afa0d3b8653a2fc7d6";
        public const string INVALID_RESOURCE_ID = "c76a203754d54276800bf596d65cfc56";
        public const string DUPLICATE_RESOURCE_ID = "0b2a12dc76aa4e468f9ffbf4bb29c853";
		public const string UNSUPPORTED_ID_TYPE_ID = "77e91203b518492cb705297ce754ecf6";
		public const string INVALID_DISCOVERING_GATEWAY_NODE_MISSING = "b36da7f355db4056b11ec113482f0762";

        public const string INVALID_EVENT_CONFIGURATION_NODE_MISSING = "84245a4b7b2c4927847315affee23fc1";
        public const string INVALID_EVENT_CONFIGURATION_CHILD_NODE_MISSING = "0529238e204145b296c2201352490487";
        public const string INVALID_GATEWAY_EVENT_NODE_MISSING = "3f228b9b3cc1448696fa5dda9482cae8";
        public const string INVALID_METER_ALARM_NODE_MISSING = "edab41fad39f49769eff8842b4cfe3b5";
        public const string INVALID_GATEWAY_EVENT_ID_NODE_MISSING = "8617a2c89cab4d14b369509049780d17";
        public const string INVALID_GATEWAY_EVENT_ID = "db9d8f4ad6a248629628dcef435bcf91";
        public const string UNSUPPORTED_GATEWAY_EVENT_ID = "a1073f988f5c4526bca534d192a6097b";
        public const string INVALID_GATEWAY_EVENT_STATUS_TYPE_ID = "220139e70ed548d69bc586a5f21ce382";
        public const string INVALID_GATEWAY_EVENT_URGENT_COUNT = "4d97c11ef57e41489f55e8ebf370110a";
        public const string INVALID_GATEWAY_EVENT_LIMIT = "53d6317a6f704aa581107572950618fc";
        public const string INVALID_GATEWAY_EVENT_ON_CLEAR_STATUS_TYPE_ID = "893dfb9f1fd143ae96fc552ba541cdaf";
        public const string INVALID_GATEWAY_EVENT_CHILD_NODE_MISSING = "81547a0764f746279c8b5dae7fe66e09";
        public const string DUPLICATE_GATEWAY_EVENT_ID = "e8966aad17d24f4c974fff7727ff1795";
        public const string INVALID_METER_ALARM_ID_NODE_MISSING = "78a0db68bac94fe1a96b9898598c769d";
        public const string INVALID_METER_ALARM_ID = "976e6405e9de4698b8050a73c47ca5b0";
        public const string INVALID_METER_ALARM_STATUS_CHILD_NODE_MISSING = "a949983e63f243ed8435b00c65273c22";
        public const string INVALID_METER_ALARM_STATUS_TYPE_ID = "419691c6cd7348b4b026dccb6265d5f2";
        public const string UNSUPPORTED_METER_ALARM_CHILD_NODE = "7484df4aad4749ab877c28960660d701";
        public const string INVALID_METER_ALARM_EVENT_LOG_STATUS_TYPE_ID = "46f3c217f53b4439b8e2499a99a46407";
        public const string DUPLICATE_METER_ALARM_ID = "74fdbddda3d8490b854a7654412d0f69";
        public const string INVALID_POWER_LINE_CARRIER_COMMUNICATION_CONFIGURATION_NODE_MISSING = "5b102dbe71f14ef8aaefbf77bbd9c3ad";
        public const string INVALID_SCHEDULES_NODE_MISSING = "0011c84e3f9d44739b97b66dad237a2f";
        public const string INVALID_INTERVALS_NODE_MISSING = "52cc0d1d3cca421592c588b99af55ce4";
        public const string INVALID_INTERVAL_NODE_MISSING = "f3e908ca260740389c9345cf23d4169c";
        public const string INVALID_POWER_LINE_CARRIER_COMMUNICATION_STATUS_TYPE_ID = "d18820111f2b439981e717efb2818565";
        public const string INVALID_START_TIME_NODE_MISSING = "33c6a374d32248ba938fb64025d6e533";
        public const string INVALID_START_TIME_FORMAT = "d1fdfaa854f44221b2e488df2c273fd3";
        public const string INVALID_END_TIME_FORMAT = "0775b8f93bf64f5b896cf2b623607b9e";
        public const string INVALID_START_TIME_HOUR = "fe7da9a3b9ed43daa213d0623fe9013f";
        public const string INVALID_START_TIME_MINUTE = "c8780af22d6e4360b801a91229358e61";
        public const string INVALID_END_TIME_NODE_MISSING = "a82a52dd5edf4487a6636b656ac44368";
        public const string INVALID_END_TIME_HOUR = "d5f8c3bc29d64b0b9189ee2ed6c98e96";
        public const string INVALID_END_TIME_MINUTE = "6cd1e6927a124f73bbbb172c59d18a3d";
        public const string INVALID_TIME_RANGE = "65659b203afa4f0680a134bafb92f05b";
        public const string INVALID_POWER_LINE_CARRIER_COMMUNICATION_SCHEDULE_INTERVAL_OVERLAP = "b555982896fb45a9be9dd0d8c70061cf";
        public const string INVALID_POWER_LINE_CARRIER_COMMUNICATION_STATUS_TYPE_ID_NODE_MISSING = "96d85d52ad2d46a8a9bfe59af20a30fe";
		public const string INVALID_DATASET_NEXT_COMMUNICATION_HISTORY_REQUESTED_DATE_TIME_NODE_MISSING = "40b2791cd04642bd8ba7c30b1b9a96f6";
		public const string INVALID_DATASET_NEXT_COMMUNICATION_HISTORY_GATEWAY_ID_NODE_MISSING = "bb0be7d8436c4ad0bedbf175edc2992f";
		public const string INVALID_DATASET_NEXT_COMMUNICATION_HISTORY_ID_NODE_MISSING = "09d0a3f8569f468fa2456a7cda9b3fde";
		public const string INVALID_DATASET_NEXT_COMMUNICATION_HISTORY_REQUESTED_DATE_TIME = "8cae8bcb497a44ad9ff1356eb4d4eb44";
		public const string INVALID_DATASET_NEXT_COMMUNICATION_HISTORY_REQUESTED_DATE_TIME_RANGE = "e66daa6a3a4c421b80a295c4e936a4b2";
		public const string INVALID_DATASET_NEXT_DISCOVERED_GATEWAY_GATEWAY_ID_NODE_MISSING = "7e4a4bc053634ce8900a4425a498f3e0";
		public const string INVALID_DATASET_NEXT_DISCOVERED_GATEWAY_NEURON_ID_NODE_MISSING = "0ce23d9b4f624cdfae99eb4a27e22546";
		public const string INVALID_DATASET_NEXT_DISCOVERED_GATEWAY_NEURON_ID = "ef52b730231f4ef592ed2311dcc7f529";
        public const string INVALID_DATASET_NEXT_DISCOVERED_GATEWAY_LAST_UPDATE_DATE_TIME_NODE_MISSING = "1a87e21d4a844ca89b1c92cfee4c6e6b";
        public const string INVALID_DATASET_NEXT_DISCOVERED_GATEWAY_LAST_UPDATE_DATE_TIME = "ef7a2cbc7aa04f5890addb5ab855e9c2";
		public const string INVALID_DATASET_NEXT_GATEWAY_NAME_NODE_MISSING = "a35e528cd1a14ef58637e9e9b56cbf19";
		public const string INVALID_DATASET_NEXT_GATEWAY_ID_NODE_MISSING = "d88bd1e66a1b42d6b07a2f0599814abc";
		public const string INVALID_DATASET_NEXT_RESULT_RECORDED_FROM_GATEWAY_DATE_TIME = "1cbee8af897c4842bde85ff4cbd58b21";
		public const string INVALID_DATASET_NEXT_RESULT_RECORDED_FROM_GATEWAY_DATE_TIME_RANGE = "51637cd5c6ad444abef4138f943590cb";
		public const string INVALID_DATASET_NEXT_RESULT_RECORDED_FROM_GATEWAY_DATE_TIME_NODE_MISSING = "d5dfe78da1bb47f88a3a15018cbef20e";
		public const string INVALID_DATASET_NEXT_GATEWAY_NEURON_ID_NODE_MISSING = "658c5f46eb76440db82da4f1622c56db";
		public const string INVALID_DATASET_NEXT_GATEWAY_SERIAL_NUMBER_NODE_MISSING = "db6a8e147fd64746a2a7149c35cd7bc8";
		public const string INVALID_DATASET_NEXT_GATEWAY_LAST_CONNECTION_DATE_TIME = "087a20bc64aa4e5484232299cf50adbd";
		public const string INVALID_DATASET_NEXT_GATEWAY_SERIAL_NUMBER = "368b2129ce7f45b3a2316330df264a29";
		public const string INVALID_DATASET_NEXT_GATEWAY_LAST_CONNECTION_DATE_TIME_RANGE = "ca8bc58c3913456898f027432268afac";
		public const string INVALID_DATASET_NEXT_GATEWAY_LAST_CONNECTION_DATE_TIME_NODE_MISSING = "eea475f8344e4b86a24d13246ff63290";
		public const string INVALID_DATASET_NEXT_GATEWAY_NEURON_ID = "8e282d1531784688823ce3fb95628795";
		public const string INVALID_DATASET_NEXT_GATEWAY_NAME = "17cc90f1e01a4fe1ab6e50532d192278";
		public const string INVALID_DATASET_NEXT_GATEWAY_ID = "d8b22cf3ac4f4df684c54ec47151d196";
        public const string INVALID_REINITIALIZE_TYPE_NODE_MISSING = "fade3d250e2c409db13d2573b09fece3";
        public const string INVALID_RESOURCE_REINITIALIZE_TYPE = "63b7c49f56884124816ab5fac2c1e4f8";
        public const string INVALID_RESOURCE_REINITIALIZE_TYPE_ID = "b2f83367cc9e4f959a8af316c3f7a5a3";  //To be deleted when CO 2081 code releases.
        public const string INVALID_SET_WAN_CONFIGURATION_COMMAND = "9f7850b83c944611b1718f2feabfa6f7";
        public const string INVALID_INACTIVE_LIMIT = "9994613b158d4e459c4893a9a8190e72";
        public const string INVALID_MODEM_DOWN_LIMIT = "754cf3ddeb6b40c9b535e8203edbb83d";
		public const string INVALID_HIERARCHY_NODE_MISSING = "9b8d97efb5214d95898fc8c3bc42d5f1";
		public const string INVALID_HIERARCHY_ID_NODE_MISSING = "8886f47a69364d0084a27215a782b6ae";
		public const string INVALID_HIERARCHY_RETRIEVE_TYPE_ID_NODE_MISSING = "cc1d075208d24415bf39469d1759a91d";
		public const string INVALID_HIERARCHY_RETRIEVE_TYPE_ID = "f1edb2a1b1884d7c98fa3cec4f822800";
		public const string INVALID_ATTRIBUTE_RETRIEVE_TYPE_ID = "793ea5b15e6e45da9b9577e5289f90b3";
        public const string UPDATE_GATEWAY_FIRMWARE_COMMAND_EXISTS = "4def773064c148d7bf77a8f83d77f544";
        public const string UNSUPPORTED_FIRMWARE_VERSION_ID = "ff8d02289cc7454dbc42663c497ab845";
        public const string INVALID_LAST_COMMUNICATION_STATUS_TYPE_NODE_MISSING = "2c92cf246f104d16bd4d6d860a99ec3a";
        public const string INVALID_LAST_COMMUNICATION_STATUS_TYPE_ID_NODE_MISSING = "34709e0172ec4ef5b433d0237042199c";
        public const string INVALID_LAST_COMMUNICATION_STATUS_TYPE_ID = "3a8757e1921846b784f3b657995f640e";
        public const string INVALID_GATEWAY_TO_REPLACE_NODE_MISSING = "7f087bec2b8d40dcaa34fe564e48768a";
        public const string INVALID_GATEWAY_TO_REPLACE_CHILD_NODE_MISSING = "9dc965b612ae436c95f7900f90fca9d6";
        public const string INVALID_NUMBER_OF_GATEWAY_TO_REPLACE_CHILD_NODES = "dd031b0c78884346ad33510c8dca3826";
        public const string UNSUPPORTED_GATEWAY_TO_REPLACE_TYPE_ID = "fe16ead3e1ac4ebd87fee6d5b07e5449";
        public const string INVALID_GATEWAY_TO_REPLACE_NOT_FOUND = "40837a4f6d074eabb2ba0c33ae7f3e37";
        public const string DUPLICATE_GATEWAY_TO_REPLACE_NAME = "a0ea56e606724894b061f6f239644982";
        public const string DUPLICATE_GATEWAY_TO_REPLACE_SERIAL_NUMBER = "7d13c033e352475eaed51d78357b5b6b";
        public const string INVALID_REPLACEMENT_GATEWAY_NODE_MISSING = "3ca8688380cb4cd6a03268926cb0afd6";
        public const string INVALID_REPLACEMENT_GATEWAY_NEURON_ID_MISSING = "77ddd4ece45e4330bf39d7af23c46ace";
        public const string DUPLICATE_REPLACEMENT_GATEWAY_SERIAL_NUMBER = "d9f8f8e53fc940dd978c0e296af6dcba";
        public const string INVALID_REPLACEMENT_GATEWAY_DATA_CONCENTRATOR_CHILD_NODE_MISSING = "de1b13ee0d5445f081d58f4503980322";
        public const string INVALID_REPLACEMENT_GATEWAY_SERIAL_NUMBER_NODE_MISSING = "8a2f01dfc7bc43a8b2862789c330d918";
        public const string INVALID_REPLACEMENT_GATEWAY_SERIAL_NUMBER = "6d02ec033a8642d28654701ba31034b4";
        public const string INVALID_REPLACEMENT_GATEWAY_NEURON_ID = "58fe143602b84d60acc2ba744a39424f";
        public const string DUPLICATE_REPLACEMENT_GATEWAY_NEURON_ID = "897e8cace1ee49e8a1ff82f1276a809a";
        public const string INVALID_REPLACEMENT_GATEWAY_PPP_USERNAME_AND_PPP_PASSWORD_MISSING = "80b4d5a481134219869bf3f7ff186db7";
        public const string INVALID_REPLACEMENT_GATEWAY_SERIAL_NUMBER_NEURON_ID_MISMATCH = "9442cc60f92747c4b9caa1290beec352";
        public const string INVALID_SERVER_TO_GATEWAY_COMMUNICATION_CHILD_NODE_MISSING = "c0ca5d4255294357bfe716091e37f903";
        public const string INVALID_REPLACEMENT_GATEWAY_PPP_USER_NAME = "2ab0704fd90540059da0323c96176dc5";
        public const string INVALID_REPLACEMENT_GATEWAY_PPP_PASSWORD = "69709c42ba8f493294803052db39aba6";
        public const string INVALID_REPLACEMENT_GATEWAY_PPP_PASSWORD_NODE_MISSING = "51da6c755485436fb8e90b1713f609fd";
        public const string INVALID_REPLACEMENT_GATEWAY_PPP_USERNAME_NODE_MISSING = "a24d144df3f3461e8ccd6996b9b49e3c";
        public const string INVALID_REPLACEMENT_GATEWAY_INSTALLATION_DATE_TIME = "1558bfaa938d47f38d3244f0d26c312c";
		public const string INVALID_TRANSFORMER_NODE_MISSING = "b820afcf8ce84dc69a9d6176cdf12976";
        public const string TRANSACTION_CLOSED = "bb227298f78c4ca8aa21e747f52b2cc6";
        public const string DUPLICATE_GATEWAY_SERIAL_NUMBER = "5e7530a5b76844efb79838bcb903d818";

        //v5 return codes
		public const string INVALID_LOCAL_DATA_ACCESS_CONFIGURATION_NODE_MISSING = "dd978472057d4f99bdd6287ade4b89e2";
		public const string INVALID_LOCAL_DATA_ACCESS_CONFIGURATION_READ_ONLY_PORT_STATUS_NODE_MISSING = "c89b8bbb77b84b4b8de1a4630dd7c1d7";
		public const string INVALID_LOCAL_DATA_ACCESS_CONFIGURATION_READ_ONLY_PORT_STATUS = "3aa4d45349654977af3550a29ed20142";


		//v5.1 return codes
		public const string INVALID_ESS_CREDENTIALS_BYPASS_SSL = "325c58f8f36b48478e5dd156dbdca1bd";
		public const string INVALID_ESS_CREDENTIALS_PUBLIC_KEY = "d5ce81e443264437abcb42d32d3db9e9";

		//v5.3 return codes
		public const string INVALID_CO_LOCATION_THRESHOLD = "c308c59368e946449dbdd561fb816dd9";
		public const string INVALID_DEFAULT_CROSS_PHASE_ADJUSTMENT = "ab6dc128c8b34978b7c27afe6de6478d";
		public const string INVALID_INACTIVE_PHASE_TYPE_ID = "413c5600052e45c985db495b75cca753";
		public const string INVALID_GENERATION_LIMIT = "78d56cd35aee4402a71e068de4bdc4dc";
		public const string INVALID_GPS_ANTENNA_POWER = "cd18b8a0e4b04964b9f0732ce6ea60fc";
		public const string INVALID_POWER_OFF_CALL_DELAY = "3a4191a255664327a7887f58d35962da";
		public const string DUPLCIATE_INACTVE_PHASE_TYPE_ID = "5eaacbfe27d44b589ed50d806df6a8f0";
        public const string INVALID_OPTICAL_PORT_CONFIGURATION_OPTICAL_SESSION_TIMEOUT = "d96c9869148b43bb9fa330f9eb0fee1d";
        public const string INVALID_OPTICAL_PORT_CONFIGURATION_OPTICAL_DOS_TIMER_STATUS = "fded41225640442a9d84e740304708e0";
        public const string INVALID_OPTICAL_PORT_CONFIGURATION_OPTICAL_SESSION_HOLDOFF = "9eb599e950414b9492af1c94d743b04b";

		// End Gateway Manager

        public const string INVALID_DATABASE_TYPE_ID = "10a7e3ca60cc475e913c61661587b7c8";
		public const string INVALID_DATABASE_TYPE_ID_NODE_MISSING = "0e7a83abc7eb4f7c9dc8ee5ebb513405";
		public const string INVALID_TRANSFORMER_ID = "65b5b9e110564bd8bfbbf233f13a9c46";
		public const string INVALID_TRANSFORMER_ID_NODE_MISSING = "8fcd23a524c046af94b63ecdeb4a2649";
		public const string INVALID_INSTALLATION_DATE_TIME = "4268ffa7fb9a4f5eaec366af10011893";
		public const string INVALID_NEURON_ID = "ae7663061d8d4607aa8e39d02ab04f65";
		public const string DUPLICATE_NEURON_ID = "27d1f8d25d01441b876b2ddfe9142fcb";
		public const string DUPLICATE_TRANSFORMER_ID = "a93a286c4f6649c289c13d574674db4d";
		public const string INVALID_TEMPLATE_TYPE_ID = "b7101e9a240342fd95efbb7e1e6ae636";
		public const string RESULTS_PENDING = "4a5ac18e4f8a47d9bc1a1a98d810c1f1";
		public const string CURRENTLY_COMMUNICATING = "15d2a24c0db341778580f71742f4f9be";
		public const string INVALID_ID = "5745f6be0dd74d918ea07aea2d21d627";
		public const string INVALID_ID_TYPE = "55da2d57a75f44f0a2e747fbea184d5a";
		public const string INVALID_START_INSTALLATION_DATETIME = "736e9fd35361464b803e7ffc641879e7";
		public const string INVALID_END_INSTALLATION_DATETIME = "941659468b0c4b36998b75092de8ed21";
		public const string INVALID_INSTALLATION_DATETIME_RANGE = "aba65e949aaf4b23a436fe6bb1cffc87";
		public const string INVALID_IP_ADDRESS_CONFLICTING_SPECIFICATION = "d2984a35c4654187915ae501e3b4a07a";

		// Expression Manager
		public const string INVALID_SOAP_NAME = "F81AC19F451446ae83F0415DB615EAAE";
		public const string INVALID_FUNCTION_NAME = "F3E8A95668254b05BAE4825316C52312";
		public const string INVALID_SOAP_URI = "6B559F99D72F47b28B6EF58C33AA6E90";
		public const string INVALID_SOAP_NAMESPACE = "398A7BF6CD234e108357467A90A9FA30";
		public const string INVALID_EXPRESSION = "3DB8906E1152470b9B1941471EB304B3";
		public const string INVALID_DEFINITION_ARGUMENT = "BE55938B7421474b9FF1BB61693454FC";
		public const string INVALID_SOAPCALL_NAME_BLANK = "39984D08A60848289BE40E4610469092";
		public const string INVALID_SOAPCALL_PARAMETER_NAME_BLANK = "428E8440EFE0476eBACF26F46EE99376";
		public const string INVALID_FUNCTIONCALL_NAME_BLANK = "5859F5C3FAC14476B1AC6571074986CA";
		public const string INVALID_FUNCTIONCALL_PARAMETER_NAME_BLANK = "F3817C24ECFD44b09DC392C8181864C1";
		public const string INVALID_FUNCTIONCALL_ID = "f47fda4310964a2dabb1740d329638f9";
		public const string INVALID_SOAPCALL_ID = "e15cd59753f84d67a35502a725c7672c";
		public const string INVALID_SOAPCALL_PARAMETER_NAME = "649FCE67CB6D4ada80389EBEC40B63CC";
		public const string INVALID_FUNCTIONCALL_PARAMETER_NAME = "19C4534E8AB944f8AD50920EA13CAA19";
		public const string DUPLICATE_SOAPCALL_PARAMETER_NAME = "DB59340182A845898879119B766E2DEA";
		public const string DUPLICATE_FUNCTIONCALL_PARAMETER_NAME = "A4FB87D191444299A87CE661D2CACDDB";
		public const string DUPLICATE_FUNCTIONCALL_NAME = "94D43BDFA4D84b359AD8E8DB2C8A6599";
		public const string DUPLICATE_SOAPCALL_NAME = "7DFF7CBBFB9D4829965B33614FF2AEEB";
		public const string INVALID_SOAPCALL_PARAMETER_INDEX = "0375F4B6B37B450c86B257E5937128E3";
		public const string INVALID_SOAPCALL_PARAMETER_TYPE_ID = "27A48FD841F84f1cBBBCCA4858F872E0";
		public const string INVALID_FUNCTIONCALL_PARAMETER_INDEX = "3A5910618CD645d1B0CB9F491E30CEB9";
		public const string INVALID_FUNCTIONCALL_PARAMETER_TYPE_ID = "32F3B81ECAE94d57BD3B4E029B0D967A";
		public const string INVALID_SOAPCALL_PARAMETER_ID = "0280B14D971944efA9D589DEDF9DE372";
		public const string INVALID_FUNCTIONCALL_PARAMETER_ID = "2E76A5446A4A496dA9C66F7FFEA58D85";

		// Schedule
		public const string INVALID_TIMEOUTINTERVAL_TYPE_ID = "C63DF78E6462474fA8BC0744185ADE7D";
		public const string INVALID_TIMEOUT_INTERVAL_TYPE_ID_NODE_MISSING = "1a734fc9308a460bb6bacae560a3595e";
		public const string INVALID_SCHEDULE_ID = "1c5a2235875543a99e1138b8e3ae62a3";
		public const string INVALID_TIMEOUTINTERVAL = "8d02544ae15a4fa986f534646b63becc";
		public const string INVALID_SCHEDULE_INTERVAL = "f0b4a01d133740af8ecd4803f2219a9a";
		public const string INVALID_SCHEDULE_MINUTE_INTERVAL = "fb923b337a2d4a99bb58ccf69abc77c3";
		public const string INVALID_TASK_PROCESSOR_TYPE_ID = "2e26ff75a60343f8aafdfbca569a3c74";
		public const string INVALID_SCHEDULE_STATUS_TYPEID = "b8250ad0a936408db123fa2caff93cf8";
		public const string INVALID_SCHEDULE_TYPE_ID = "dbfdea626cc14f1788e6be1fce0d172f";
		public const string UNSUPPORTED_SCHEDULE_TYPE_ID = "58b2755689e24451a8e74cc644ae2ca9";
		public const string INVALID_SCHEDULE_ALREADY_ASSIGNED = "3b78c56a68004affb1df968e11b1f970";
		public const string INVALID_TIMEOUT_INTERVAL_MINUTE = "f4eca5891ea24ab498b110df797f9ff6";
		public const string INVALID_TIMEOUT_INTERVAL_NODE_MISSING = "0aca8a1f6c59444fb5b80de776ee637d";
		public const string INVALID_SCHEDULE_NAME_BLANK = "D6CBD1601F074c94B1B07B93BEF22F25";
		public const string INVALID_SCHEDULE_NAME_DUPLICATE = "5f69557c94df4149a102789d18c3571a";
		public const string INVALID_SCHEDULE_OCCURRENCE_LIST = "a72499b064de48bb9ea0bbab8bed7cd6";
		public const string INVALID_SCHEDULE_TYPE_ID_NODE_MISSING = "3145d0946898479fa69cf02bf69d06db";
		public const string INVALID_START_SCHEDULE_START_DATETIME = "89cf343677994110874b9b62991508e8";
		public const string INVALID_END_SCHEDULE_START_DATETIME = "46120f3818744311b38d754b5695075b";
		public const string INVALID_START_SCHEDULE_END_DATETIME = "44264ad2b6f54b52a1df8403f4f7ec02";
		public const string INVALID_END_SCHEDULE_END_DATETIME = "0ad536a40d3342b29a514d9d78bb8c1f";
		public const string INVALID_SCHEDULE_START_AND_END_DATETIME_RANGE = "fe62ae8e4c8e4eafb22f260ef3f5ddf5";
		public const string INVALID_SCHEDULE_START_DATETIME_RANGE = "971d393bcdf64e83b9f322aef5d7ee8b";
		public const string INVALID_SCHEDULE_END_DATETIME_RANGE = "231d5f00da11465ca293de73705ac065";
		public const string INVALID_SCHEDULE_REMOVAL = "059defcccb044438901e5d1e542988b5";
        public const string INVALID_SCHEDULE_STATUS_UPDATE = "f2683e1900344217a2fa82c74c11fc1e";

		// Data Access Manager
		public const string INVALID_DATABASE_LOCATION_BLANK = "70daf229c6ae42d5928d7c9d844a2c8b";
		public const string INVALID_DATABASE_LOCATION_TOO_LONG = "5e573b5d832344acb8e965666d8ba042";
		public const string INVALID_DATABASE_NAME_BLANK = "5c0358efe5354b7cba6c42569d25ca26";
		public const string INVALID_DATABASE_LOGIN_BLANK = "5460ac0344f54dde8bc741abb95b6c98";
		public const string INVALID_DATABASE_LOGIN_TOO_LONG = "39933cb6c56f42bf8be0c70985aab1ad";
		public const string INVALID_DATABASE_LOGIN_WITHOUT_PASSWORD = "9462e5e3b3924cabb13fb91c6a454585";
		public const string INVALID_DATABASE_PASSWORD_TOO_LONG = "cc3adfa9dd354533aba3d6ca30bd3841";
		public const string FAILED_SENDING_UPDATE_TO_GLOBAL_TASK_MANAGER = "a87674d13fb74af191e6eb51efa924f5";
		public const string FAILED_DATABASE_CONNECTION = "0ef8823e9da84618b44f78038512b4dd";
		public const string FAILED_GLOBAL_TASK_MANAGER_NOT_FOUND = "a82412d0aa694ef8977a448b6d54d7b4";
		public const string INVALID_PORT = "6fd34f95edff4f4688942e3efd5b9b28";
		public const string INVALID_DATABASE_TIMEOUT_SECONDS = "aaa17d31b17743e2967c6d938bed8997";
		public const string INVALID_DATABASE_NAME_MASTER = "d668de66d3464c4e8bbf16fda8107cef";
		public const string INVALID_DATABASE_NAME_TOO_LONG = "aa77ac94ae4c438692b2096c0235075b";

		// MessageLog
		public const string INVALID_EMAIL_ADDRESS_FORMAT = "b7dfe11ab8ad4c6c9f5880f9573a45ba";
		public const string INVALID_MESSAGELOGDEFINITIONID = "57ec4684a73b4993b4672b1e2d61e176";
		public const string INVALID_MESSAGELOG_STATUSID = "45d8969daeed4d51b51b4f083b0df43b";
		public const string INVALID_MESSAGELOG_TYPE_ID = "5079c159380d4a3fb1bd90b23721de27";
		public const string INVALID_MESSAGELOG_USERID = "9bdd09cf9dcf48dd8f60488312c4efd0";
		public const string INVALID_MESSAGELOG_TEXT = "ef40f62d68664328810560605a13c9b6";
		public const string INVALID_MESSAGELOG_LOCATION = "cc0ed8e028b4438a966239fb4e167bbb";
        public const string INVALID_LOGTYPE_ENABLE = "ff01eab9ab014bf59ce99f5d1cdcb4fe";
		public const string INVALID_LOGTYPE_DISABLE = "e4ff2416af2941f99971416a9859d4bf";
		public const string INVALID_EMAIL_ADDRESS_MUST_BE_BLANK = "855a9e43fee24c32a19b942ea813021c";
		public const string INVALID_MESSAGELOG_START_DATETIME = "d8fa00a3421f463094f61a69826d9a41";
		public const string INVALID_MESSAGELOG_END_DATETIME = "68fc3484e1b84dffa243e5cd1f550764";
		public const string INVALID_MESSAGELOG_DATETIME_RANGE = "890eb5bf9bc840e089339166bf31705c";
		public const string INVALID_MESSAGELOG_SOURCEIPADDRESS = "ea6c0b45c19f4caca0ecb5f45f19899a";
		public const string INVALID_SMTP_SERVER_LOCATION = "974278d617f34cbaac677206ba84b994";
		public const string INVALID_DATASET_NEXT_MESSAGELOG_DATE_TIME_STAMP = "4e7e71420f1447ceb2178c2f7496e716";
		public const string INVALID_DATASET_NEXT_MESSAGELOG_DATE_TIME_STAMP_NODE_MISSING = "0eb544e4c4ec4cf391cca6f5ce13f11e";
		public const string INVALID_DATASET_NEXT_MESSAGELOG_ID_NODE_MISSING = "39ebd3ad3c4f4467b18567c2d23dbf25";
		public const string INVALID_DATASET_NEXT_NODE_MISSING = "8b0bd000bf844d72abee6c965827d6dd";
		public const string INVALID_DATASET_NEXT_MESSAGELOG_DATE_TIME_STAMP_RANGE = "d5958e49a84945689af7bd2d3b44f88d";
		public const string INVALID_DATASET_NEXT_MESSAGELOG_ID = "ceaf7f782f7448c8bcc4bdd1e28501d4";


		// DataPointManager XMLParameter nodes
		public const string INVALID_DATAPOINT_NAME_BLANK = "88fd875abad147808290329a2f56066c";
		public const string INVALID_DATAPOINT_NUMERICMIN_VALUE = "941700369a2049a7a26cc3d5063084c7";
		public const string INVALID_DATAPOINT_NUMERICMAX_VALUE = "c98327903a4d40b49217d756a63faa46";
		public const string INVALID_MAX_COUNT = "59b5fc09247344b8a24b88270c50b567";
		public const string INVALID_DATAPOINT_NAME = "7bc17205edd441a4badac565eddb0f16";
		public const string INVALID_UNIT_OF_MEASURE = "2386fcf105404a7b826f22968635167f";
		public const string INVALID_DATAPOINT_NODE_MISSING = "e63a69bd48cd4ba2ae021fdb60cbd708";
		public const string DATAPOINT_NOT_ENABLED = "480f1ebba03d47079ee83c7b9ea43b32";
		public const string INVALID_SORT_BY_TYPE_ID = "000fecad76a14382ba3648801bc02897";
		public const string INVALID_SORT_BY_ORDER_TYPE_ID = "11f7cb72ce184593b8a9a7267e0830eb";
		public const string INVALID_SORT_OPTION_NODE_MISSING = "368de52ee4f34032ae847b77f4a4a6a6";
		public const string INVALID_SORT_BY_TYPE_ID_NODE_MISSING = "5ec581850b154a64a9fe2b832d44a1a3";
		public const string INVALID_SORT_BY_ORDER_TYPE_ID_NODE_MISSING = "737050d8ddb449b5a1389acf0a1deddf";
		public const string INVALID_SORT_ORDER_TYPE_ID_NODE_MISSING = "2395bad9e780472bafe4d54e5f37fb28";
		public const string INVALID_SORT_ORDER_TYPE_ID = "0adcf78aa7104f829851f77e0af88109";

		// DataPointValueManager codes
		public const string INVALID_DATAPOINTVALUE_ACTUALDATETIME = "b659e10386e44f4b8444c715497e517f";
		public const string INVALID_DATAPOINTVALUE_ACTUALDATETIME_NODE_MISSING = "1a48be0085c64f26a0080e2b44bd5f03";
		public const string INVALID_DATAPOINTVALUE_ACTUALTIMEZONEDATETIME = "556b5566c79c4b65adb2771ae613538c";
		public const string INVALID_DATAPOINTVALUE_ACTUALTIMEZONEDATETIME_NODE_MISSING = "33adcb5f697d4d05919bb94105c56e91";
		public const string INVALID_DATAPOINTVALUE_CALCULATIONEXECUTIONTIMESPAN = "1f8746da7daa4c97a5932d338f9c16ff";
		public const string INVALID_DATAPOINTVALUE_DATAAVAILABLE = "c98dd11e6fc14a1faf8c041bb1aa7cab";
		public const string INVALID_DATAPOINTVALUE_DATAAVAILABLE_NODE_MISSING = "ca508a793b304df0adcccd1273c0bec2";
		public const string INVALID_DATAPOINT_ID_NODE_MISSING = "2622cf8e23cd48b6973f2acb25b38d18";
		public const string INVALID_DATAPOINTVALUE_EXPECTEDDATETIME = "5c7198b37a284ad5b5f98d8b369d5bac";
		public const string INVALID_DATAPOINTVALUE_EXPECTEDDATETIME_NODE_MISSING = "ecdf296fd2e545d4a836b83d2f80797b";
		public const string INVALID_DATAPOINTVALUE_EXPECTEDTIMEZONEDATETIME = "a66aeb7e34164ce5a522e8d6aa63ee03";
		public const string INVALID_DATAPOINTVALUE_EXPECTEDTIMEZONEDATETIME_NODE_MISSING = "846322e8d7844f179f439c3174638282";
		public const string INVALID_DATAPOINTVALUE_MANUALLYENTERED_NODE_MISSING = "036726f6433941189b7d565a112803a0";
		public const string INVALID_DATAPOINTVALUE_MANUALLYENTERED = "5451837566ba41d58b805924d8fc5a9d";
		public const string INVALID_DATAPOINTVALUE_STATUSTYPEID_NODE_MISSING = "6c19110304da487ea3240ec93c203ef0";
		public const string INVALID_DATAPOINTVALUE_VALUE_TYPE_MISMATCH = "dfa88fb98c014d6b877ed0e76a126017";

		// DeviceManager
		public const string INVALID_HIERARCHY_LEVEL_MEMBER_RETRIEVAL_TYPE_ID = "8c5c4d532c594b75849e01e21c0afb21";
		public const string INVALID_DEVICE_TYPE_ID = "b240c1d77a7640bb82541e03e4457df0";
		public const string INVALID_LONTALK_KEY = "a87498cd5e1d481eadaa4f6f1c1e432e";
		public const string INVALID_SERIAL_NUMBER = "66de160c38f64e3eab61e8d4ff2a9891";
		public const string INVALID_SERIAL_NUMBER_NODE_MISSING = "0787da274cbb4ae4ae07b629543b29bc";
		public const string INVALID_PHASE_TYPE_ID = "b8f1f1c627184ed89a63b3c1f86c55e8";
		public const string INVALID_RESULT_ID = "7f12d45fb10b4ad189207e808d27f1aa";
		public const string INVALID_RESULT_TYPE_ID = "06bcb2437dc9498cb2a32f01e5f902b0";
		public const string INVALID_START_DATETIME = "c7bde659dd4f4be9b02256e0f4f03cc2";
		public const string INVALID_END_DATETIME = "c357faff7ae147759831444b65a7dc60";
		public const string INVALID_DATETIME_RANGE = "05721023453b4f2c911b41114db77fce";
		public const string INVALID_TIMEOUT_DATETIME = "aad4ba108fdf4c37bfe019a499c3a011";
		public const string INVALID_TIMEOUT_DATETIME_NODE_MISSING = "d967f4360a3d477eba63a93ebf5300bd";
        public const string LOAD_PROFILE_IN_PROGRESS = "0dc815344c964ae8ad5a421221a21fc7";
		public const string UNSUPPORTED_DEVICE_TYPE_ID = "0532ada7a8284094bafda987c0e5c1bb";
		public const string INVALID_MAXIMUM_POWER_LEVEL = "1ef237fba0a14f6797439b59b31a1cbb";
		public const string INVALID_MAXIMUM_POWER_LEVEL_DURATION = "bebb9587f75048cfbf1fab1f081b810c";
		public const string UNSUPPORTED_MAXIMUM_POWER_LEVEL_DURATION = "56CA0C57A44947099317F3CC531750E9";
		public const string INVALID_ASSOCIATED_WITH_GATEWAY = "5b910e9594dd47d3b1d992d3ad41b127";
		public const string DEVICE_NOT_ENABLED = "953b40534277401a8bd9c187401ab5c5";
		public const string INVALID_ENABLE_MAXIMUM_POWER = "af16a9545a0640ddabd50aeb36ad4e26";
		public const string PERFORM_SELF_BILLING_READ_IN_PROGRESS = "49685f8d36b44a43a9f5da304e5079f2";
		public const string INVALID_ENABLE_CONTROL_RELAY_TIERS_VALUE = "9fe600ec48e94bac9d29b0957155e9f9";
		public const string INVALID_LOAD_PROFILE_CHANNEL_SOURCE_ID = "ed98589734a04abda4fafc778dba1621";
        public const string INVALID_LOAD_PROFILE_CHANNEL_SOURCES_COUNT = "187194c55041423b8321aa1435be99e3";
		public const string INVALID_LOAD_PROFILE_CHANNEL_SOURCE_ID_NODE_MISSING = "d2b1a04fdde54f55a8317b4ebed528b7";
		public const string INVALID_LOAD_PROFILE_INTERVAL_PERIOD = "92f0bc4fcdab47a287a04ad07d7b9dd0";
		public const string INVALID_LOAD_PROFILE_INTERVAL_NODE_MISSING = "02bec8733c7046ea8b09013802c93e28";
		public const string INVALID_DEVICE_ID_NODE_MISSING = "3829ead36e344e27b0f1bef2783a00d9";
		public const string INVALID_DEVICE_NODE_MISSING = "7b9bc07aa29347a09e831f1f6185d1c7";
		public const string INVALID_DEVICES_NODE_MISSING = "F2CFE341A9A64f309B9F85D028742DAF";
		public const string INVALID_GATEWAY_ID_NODE_MISSING = "825fdf4bb2ab424185ad274ee89e229b";
		public const string INVALID_GATEWAY_NODE_MISSING = "ef4d9f24e4e64a6eabe42b4377cf8318";
		public const string INVALID_POWER_QUALITY_THRESHOLD = "7C15BBD1B01F47259F47469311F729D1";
		public const string INVALID_PROGRAM_ID = "2285A867C8144545A503B78D028FC507";
		public const string UPDATE_METER_FIRMWARE_IN_PROGRESS = "0707FF0AAEC5450d925490129AB46D78";
		public const string INVALID_FORCE_DELETE = "456ddbc0a51847fa907e353e408aec0c";
		public const string INVALID_ALARM_TYPE_NODE_MISSING = "962BD6F73A9D42b38C2A91ACE17F4A73";
		public const string INVALID_SET_ALARM_DISPLAY_CONFIGURATION_COMMAND = "0525705B841244718736C5CDC331E831";
		public const string INVALID_ALARM_INDEX = "3D579899383D4dcf863B52971A5EB92F";
		public const string INVALID_ALARM_DISPLAY_OPTION = "23FEF76074644dfcA19E22040FB53D39";
		public const string DUPLICATE_ALARM_INDEX = "4D346AFD201244aaA561818D9F062671";
		public const string INVALID_CAUTION_ID_INDEX_NODE_MISSING = "273E1AA474284b8bA9EBB566F4206C4C";
		public const string UNSUPPORTED_CAUTION_ID_INDEX = "0EF54EEAB97D479c8D75CDB1DE6D5BA3";
		public const string DUPLICATE_CAUTION_ID_INDEX = "FF29A459AD884cae9939079ED8AB7303";
		public const string INVALID_ERROR_ID_INDEX_NODE_MISSING = "BE4F5AEBCBDC4d10B8B02AE9D32C8B4F";
		public const string UNSUPPORTED_ERROR_ID_INDEX = "E3DC44FA07BB4e4aB6C4AA3554A58A2D";
		public const string DUPLICATE_ERROR_ID_INDEX = "6055E65723864cee95635184B0FE47EE";
		public const string INVALID_ENABLE_ALL_SEGMENTS_LIT = "F7AB557435C748549051F9C4EA9587EF";
		public const string INVALID_ENABLE_ALL_SEGMENTS_LIT_NODE_MISSING = "815180f3cd814e5f8dcd61ee0fa97cf7";
		public const string INVALID_SECONDS_TO_DISPLAY = "4D7BFF77408A4c12BF1386C61137159D";
		public const string INVALID_SECONDS_TO_DISPLAY_NODE_MISSING = "c07c481096cc4202b7abd94b4c4a38db";
		public const string INVALID_METER_DISPLAY_CATEGORY_TYPE_ID = "6F3F217AB8B44c27A69A956ADEFB551F";
		public const string INVALID_METER_DISPLAY_CATEGORY_TYPE_ID_NODE_MISSING = "e3a43739adf64cb78167e70bb1afd3d9";
		public const string INVALID_METER_DISPLAY_CATEGORY_INDEX = "7872AEC47CD24e61B2FCB86DE6D36A12";
		public const string INVALID_METER_DISPLAY_CATEGORY_INDEX_NODE_MISSING = "b0e83ca5ed7540fb91a39c5758974e52";
		public const string INVALID_METER_DISPLAY_SOURCE_CODE_ID = "0AA222FFD1CB47f3BA82D4CAE37E3FAE";
		public const string INVALID_METER_DISPLAY_SOURCE_CODE_ID_NODE_MISSING = "3219c5b9d2984a54abae9fef2826d410";
		public const string INVALID_METER_DISPLAY_ID_TEXT = "8B8AC43F1DDF4da8B18EB2727212A6C1";
		public const string INVALID_METER_DISPLAY_ID_TEXT_NODE_MISSING = "8c1a4f9f1b424a7b9663471003f00755";

		public const string INVALID_FIELDS_AFTER_DECIMAL_POINT = "3673CE5182FA4eb4A3410F8D2F9D126C";
		public const string INVALID_FIELDS_AFTER_DECIMAL_POINT_NODE_MISSING = "4fd17cb6fa76459887b905800c71641d";
		public const string INVALID_FIELDS_BEFORE_DECIMAL_POINT = "CFB83168E9EA4e4f8AEED7147D3FCDB9";
		public const string INVALID_FIELDS_BEFORE_DECIMAL_POINT_NODE_MISSING = "72d38a09be044294981b0f3903156892";
		public const string INVALID_DECIMAL_POINT_FIELDS = "41066AC114EC42ab8330B381CD24392A";
		public const string INVALID_SUPPRESS_ZEROS = "B81595F5131B4971A836F519A6CD5E1B";
		public const string INVALID_SUPPRESS_ZEROS_NODE_MISSING = "9191f9f7dbab449cb54d1856041bb762";
		public const string INVALID_NUMBER_OF_METER_DISPLAY_ITEMS = "FA3D59817F9542228EFC039AB592288B";
		public const string INVALID_SERIAL_NUMBER_NEURON_ID_MISMATCH = "c71aa21844774accbb190740c4f225ba";
		public const string INVALID_DISPLAY_ITEMS_NODE_MISSING = "D4230DB02A1B4f77886CE436BB1FFC88";
		public const string INVALID_DISPLAY_ITEM_NODE_MISSING = "CC8DE323A05F4f5880F6F60A6527F772";
		public const string INVALID_METER_DISPLAY_SOURCE_NODE_MISSING = "83EE9E132755461b9BBFE884A30557A6";
		public const string INVALID_METER_DISPLAY_ID_NODE_MISSING = "3E1AD42F04A14aab855CD81B18FF8AD5";
		public const string INVALID_METER_DISPLAY_VALUE_NODE_MISSING = "B06164EC472047289EE5D6A9C253C588";
		public const string INVALID_METER_DISPLAY_CONFIGURATION_NODE_MISSING = "A0A6D9F41FE843ffB1006AD7638CCC03";
		public const string INVALID_CHANNEL_INDEX_NODE_MISSING = "c8fff0fa8dd54c22bcecc8ef5e079215";
		public const string INVALID_PULSE_INPUT_CONFIGURATION_NODE_MISSING = "ca3ffa44f2074176b069ed0180a2076f";
		public const string INVALID_PULSE_INPUT_CONFIGURATION_CHANNEL_NODE_MISSING = "a78353a38f864ac08a826913481e363f";
		public const string INVALID_PULSE_INPUT_CONFIGURATION_COMMAND = "c827e84fe5004b70916d9092f7c7289b";
		public const string INVALID_CHANNEL_INDEX = "4b7b50f72635464bbe9a28de00bb62d8";
		public const string INVALID_PULSE_INPUT_CHANNEL_STATUS = "22a98b9aee644a35acf52e1c1fe88e15";
		public const string INVALID_PULSE_INPUT_IDLE_STATE = "a46d11fdc3f94d6c9e11aa26811b5a8d";
		public const string INVALID_PULSE_INPUT_CHANNEL_STATUS_NODE_MISSING = "45d43636ae7e4f8b87cd5c123cd43aaf";
		public const string INVALID_PULSE_INPUT_TAMPER_URGENT_ALARM_STATUS = "ef6657d6de6640f7939bbe7783c7fa91";
		public const string INVALID_PULSE_INPUT_TAMPER_REGULAR_ALARM_STATUS = "79a2676e8fb14d7aa91e07b47fd02d1e";
		public const string INVALID_CHANNEL_MINIMUM_PULSE_WIDTH = "022d69ccf478487f95fc9e08402ceec7";
        public const string INVALID_CHANNEL_ADC_LOW_LIMIT_VALUE = "59f007abe660474cb8c53bc65a10c438";
        public const string INVALID_CHANNEL_ADC_HIGH_LIMIT_VALUE = "b05d24d7d5594b77b0403df18bcee7bd";
		public const string INVALID_PULSE_INPUT_DIGITAL_INPUT_TAMPER_STATUS = "a1f2145f11a64166a294210e76b523ba";
        public const string INVALID_CHANNEL_ADC_LIMIT_RANGE = "43932543f6d34924af7f808d3038ccf4";
        public const string INVALID_CHANNEL_ADC_LOW_LIMIT_MISSING = "8d5c2cff1da546299d74d21b9bd8512c";
        public const string INVALID_CHANNEL_ADC_HIGH_LIMIT_MISSING = "382b3ee6f9be45feaf6d63d61ffe52dd";
        public const string INVALID_PULSE_INPUT_NODE_FOR_METER_VERSION = "b9dfbc032be54e35a38aabd55e517c8e";
		public const string INVALID_REPEATER_COUNT = "3dfa1baa81eb494b980ac188571d143c";
		public const string INVALID_DISCOVERED_TYPE_NODE_MISSING = "1956ddcc3c584556bf0fa738559547b1";
		public const string INVALID_DISCOVERED_TYPE_ID_NODE_MISSING = "a4a6eb34fdf94167af8519c6e9f6157a";
		public const string INVALID_DISCOVERED_TYPE_ID = "989a141fad424f249c2bcd13a8963812";
		public const string INVALID_LAST_CONTACT_START_DATETIME = "bd30ec88d8264f6e914b4107651ef849";
		public const string INVALID_LAST_CONTACT_END_DATETIME = "1fc6da61138b48f6a38b90665543a6a0";
		public const string INVALID_LAST_CONTACT_DATETIME_RANGE = "57688eea0c12422c9e10dbc065a718ce";
		public const string INVALID_INFORMATION_RETURN_TYPE_ID = "4829A5C12DE24345A93A173B7CE26D72";
		public const string INVALID_SERVICE_STATUS_TYPE_ID = "6abd62163e6640edac24b3da9ddea886";
		public const string INVALID_DOWN_LIMIT = "1ce3320257d241f8901361bfd57fe22c";
		public const string INVALID_DOWN_LIMIT_NODE_MISSING = "7d5eccbec7574405bb8bd1ea4cdcada5";
		public const string UNSUPPORTED_DEVICE_STATUS_TYPE_ID = "0AE1890A33724ab0A6909F4693B48C95";
		public const string UNSUPPORTED_CHILD_DEVICE_STATUS_TYPE_ID = "EF1DE7EAADDD4622B72AA7D33628E1CB";
		public const string INVALID_GATEWAY_NO_TRANSFORMER_ID = "3F0D004C8868403f9A57B503BB5BA4FF";
		public const string INVALID_DEVICE_TYPE_NODE_MISSING = "99529019eda14a69bb3f9dd202cdec68";
		public const string INVALID_DEVICE_TYPE_ID_NODE_MISSING = "ebfe9ff0384b406185b7420e3a1dd436";
		public const string INVALID_MBUS_DEVICE_TYPE_NODE_MISSING = "468170276fa446c7a1bf9122f1c47246";
		public const string INVALID_MBUS_DEVICE_TYPE_VALUE_NODE_MISSING = "3ccc9b9a39d84427a70f6459927f0f49";
		public const string INVALID_MBUS_DEVICE_TYPE_VALUE = "a35c9075c9c04d04b46c567ab75c5f86";
        public const string SERIAL_NUMBER_NOT_SUPPORTED = "1215a759be454245b188338731e50b72";
		public const string MULTIPLE_DEVICES_WITH_SERIAL_NUMBER = "60efff85a4b2423e8355ced5e030a7ea";
		public const string CHILD_DEVICES_EXIST = "3f5e017f1993493f84f3a5cb56e6b6d2";
        public const string INVALID_ALARM_POLLING_RATE_NODE_MISSING = "0dbf4f4170444eabac5b386a837e59b5";
		public const string INVALID_ALARM_POLLING_RATE = "13cbdbc7b5494604b3159e55eb6dc355";
		public const string BILLING_SCHEDULE_NODE_MISSING = "48BADA8EF7134d3b89356E877D022BB8";
		public const string FREQUENCY_TYPE_ID_NODE_MISSING = "38D2F059EE0A4492AD3269E4464023EB";
		public const string INVALID_FREQUENCY_TYPE_ID = "1D3BFDB345FB4b75B6D1E7BE94757A56";
		public const string DAY_NODE_MISSING = "0A1D98817FFF4ae4B6E34BF6E6EB4834";
		public const string INVALID_DAY = "4B921E6EE3744ba891788BC3EFC4613E";
		public const string HOUR_NODE_MISSING = "72C44D45D2EA40428259BD694E743349";
		public const string INVALID_HOUR = "99742BE10C3D46018E590201CC1DC9EB";
		public const string MINUTE_NODE_MISSING = "858CE1EE10554b3dB5520E2FC67C2EA8";
		public const string INVALID_MINUTE = "5929E220F3D84994A37A3B4684D5A31D";
		public const string DEVICE_NOT_ASSOCIATED_WITH_GATEWAY = "2EB3DC1DC69C4eb59897C8BD5A882D0D";
		public const string PARENT_DEVICE_NOT_ENABLED = "26bbfbf4cbf24e32847c949e3e13c6b1";
		public const string INVALID_TIERS_NODE_MISSING = "f39b817fd0bf432dbcd5ba2b556b8283";
		public const string INVALID_TIER_STATE_NODE_MISSING = "8e208f66a47c4462bb7e748c9ec61fb2";
		public const string INVALID_TIER_STATE = "34fdc28d9d044f2e80e079b993f5ea66";
		public const string INVALID_TIER_NODE_COUNT = "04aa2ff5f4204ec2b80612d6b8df19e0";
		public const string INVALID_LOAD_VOLTAGE_STATUS_TYPE_ID = "797ba760b47b4050a1a383407a36203b";
        public const string INVALID_MAXIMUM_POWER_NODE_MISSING = "8C399E8FBE0E44ad9F65088342F576A4";
        public const string INVALID_MAXIMUM_POWER_CHILD_NODE_MISSING = "DA0AB8BC563A4fd8B87DDEE447F0EC57";
		public const string INVALID_NEGATIVE_PREPAY_CREDIT_STATUS = "d6290a8e488d463fa66aa1e8fb9533c3";
		public const string UNSUPPORTED_NEGATIVE_PREPAY_CREDIT_STATUS = "2199e0598a224597a5aa46d85f1bd3b1";

        public const string INVALID_LOAD_PROFILE_CHANNEL_SOURCE_ID_ORDER = "7ca7243a21c0455393a1cfc096fb7827";
        public const string INVALID_LOAD_PROFILE_MBUS_BYTES_TO_READ_NODE_MISSING = "e18d838e98d541ada3cefa920d7f828d";
        public const string INVALID_LOAD_PROFILE_MBUS_BYTES_TO_READ_VALUE = "873f1b4d806c45bfbacf455192063c9e";
        public const string UNSUPPORTED_LOAD_PROFILE_MBUS_BYTES_TO_READ_VALUE = "f26d2605992142d6ac716c22a1fe54b9";
        public const string DUPLICATE_LOAD_PROFILE_CHANNEL_SOURCE_ID = "44d460a7b26e44f5924ae6f99a5d42c6";
        public const string INVALID_LOAD_PROFILE_CONFIGURATION_NODE_MISSING = "678fee6f5ac544c6b08dee33de40ff9f";
        public const string UNSUPPORTED_LOAD_PROFILE_MBUS_BYTES_TO_READ = "7d33cb8f9f6a4404be697d98d7e3218b";

        public const string UNSUPPORTED_RESULT_TYPE_ID = "9b0f317cbc9e4532b05c3de2ecf982c7";
        public const string INVALID_END_RECORDED_FROM_DEVICE_DATE_TIME = "a08b983268b74158aebd40fcece671f7";
        public const string INVALID_START_RECORDED_FROM_DEVICE_DATE_TIME = "47a4142055084f2497093c923906ab9a";
        public const string INVALID_RECORDED_FROM_DEVICE_DATE_TIME_RANGE = "a2e7a331de08406c9b7c62af9f132f59";
        public const string INVALID_START_SAVED_IN_SYSTEM_DATE_TIME = "dd803641eafc4d8db6dae10b8084b2d3";
        public const string INVALID_END_SAVED_IN_SYSTEM_DATE_TIME = "095d8c4ef9c44cee8ef120772c2739e7";
        public const string INVALID_SAVED_IN_SYSTEM_DATE_TIME_RANGE = "7cdd3a47a8aa4ac2a122e928a24062a6";
        public const string INVALID_BATCH_NODE_MISSING = "7fadd220348f40a2883e1581390e4b77";
        public const string INVALID_BATCH_ID_NODE_MISSING = "652a350b8122425faedff6d2d0b22a7c";
        public const string INVALID_BATCH_ID = "ba338130e5824e05bd755eb37701887f";
        public const string UNSUPPORTED_BATCH_TYPE_ID = "dc0e0bebbb654b79acd8bea363c45630";
        public const string INVALID_PROCESSED_STATUS_RESTRICTION_NODE_MISSING = "ee55cf7bc3524e1eb38fa6439cc50ffc";
        public const string INVALID_PROCESSED_STATUS_TYPE_ID_NODE_MISSING = "edf0038662144582bb21286e1d0aa8e5";
        public const string INVALID_PROCESSED_STATUS_TYPE_ID = "e90147f8648e46c79a6696285ddf4592";
        public const string INVALID_RESULTS_NODE_MISSING = "98df680bc4184bbead648953b12b2882";
        public const string INVALID_RESULT_ID_NOT_IN_A_BATCH = "e48eee677a0c45298c6def3caf9f708b";
        public const string NO_RESULTS_FOUND = "2517a78d94c04d1e9801caf7aa2a6715";

        public const string INVALID_RETRIEVAL_STATUS_TYPE_ID = "3ad866104002440481d59765058246c0";
        public const string INVALID_PRIORITY_LEVELS_NODE_MISSING = "16fdadefa7884c60ae68d6a52e9bee2b";
        public const string INVALID_CONTINUOUS_EVENT_LOG_CONFIGURATION_NODE_MISSING = "98117301c3fa4657a89c7e44178f56a8";
        public const string INVALID_PRIORITY_LEVEL_NODE_MISSING = "4953934171ab4188bca7d837a2f42b33";
        public const string INVALID_PRIORITY_LEVEL_TYPE_ID = "b566c5eaee2042a4a1454bb16e303a8e";
        public const string INVALID_RETRIEVAL_STATUS_TYPE_ID_NODE_MISSING = "cabd1941f004486ca1fa6121a42d549f";
        public const string DUPLICATE_PRIORITY_LEVEL_TYPE_ID = "57fa43fac4334c97b55cdd1e7381072b";
        public const string INVALID_PRIORITY_LEVEL_TYPE_ID_NODE_MISSING = "68ff914c22a347899542099be49d4ede";
        public const string INVALID_MAXIMUM_POWER_STATUS_TYPE_ID_NODE_MISSING = "0836669154664812BC1374034BB82BCF";

        public const string INVALID_STATISTICS_NODE_MISSING = "c6d8243d72de4b26803032ad91799552";
        public const string INVALID_STATISTIC_NODE_MISSING = "7c3dedcc1a204ed88b543c1929653c24";
        public const string INVALID_POWER_QUALITY_STATISTIC_TYPE_ID = "b05c9dca033b461dad63975419779346";
        public const string INVALID_TYPE_ID_NODE_MISSING = "2dfe1c9babc24ae796eb7ad78aec77a6";

        public const string INVALID_SELF_READ_RETRIEVAL_CONFIGURATION_NODE_MISSING = "8ca372a8637b4cafa9c50d320e89a368";
        public const string INVALID_SELF_READ_RETRIEVAL_CONFIGURATION_CHILD_NODE_MISSING = "8c363ae58b874e9ba6173a7783787f33";
        public const string INVALID_DEMAND_RETRIEVAL_STATUS_TYPE_ID_NODE_MISSING = "551aaaee0e9245c7b33af3d4e1169399";
        public const string INVALID_DEMAND_RETRIEVAL_STATUS_TYPE_ID = "bc495089d0a04da49c91482b21ec9567";
        public const string INVALID_SUM_OF_TIERS_RETRIEVAL_STATUS_TYPE_ID_NODE_MISSING = "a6819e005f0344a49c252bb51ef4d49f";
        public const string INVALID_SUM_OF_TIERS_RETRIEVAL_STATUS_TYPE_ID = "6e4a9d022ec641438f9895b76df73b12";
        public const string INVALID_DEMAND_RETRIEVAL_STATUS_TYPE_ID_SET_TO_INCLUDE = "d0c2c0850ff8487384d568fccb0fbfaf";
        public const string DUPLICATE_INDEX = "316bdabb3104455ab259ef3b1e5161db";
        public const string INVALID_TIERS_CHILD_NODE_MISSING = "75a3ace0889d4a37812c33ba58b8eb5e";
        public const string CONFLICTING_DELTA_LOAD_PROFILE_SYSTEM_CONFIGURATION = "cb8b8963d42b42d086dcc9c6b89696b0";
		public const string INVALID_CONTINUOUS_DELTA_LOAD_PROFILE_CONFIGURATION_NODE_MISSING = "c33a0c0b2aff40309e273889cabc9ecd";
		public const string INVALID_DELTA_LOAD_PROFILE_PROCESS_MODE = "0d2f54606763413f85495b9f1747a350";
        public const string INVALID_BLOCK_INTERVAL_COUNT = "bb5fc32d7fd143909a3f657c1cf295b8";
        public const string INVALID_BLOCK_START_TIME = "8bb841716aae452f864b6c7e3a07cc87";
        public const string INVALID_ANSI_COMPLIANCE_STATUS_TYPE_ID = "6d8ced411c224baa9af1b8492f3454db";
        public const string INVALID_SKIPPED_INTERVAL_VALUES_TYPE_ID = "ed223fc8d2504d3cbdd1fd862862b9df";
        public const string UNSUPPORTED_ANSI_COMPLIANCE_STATUS_TYPE_ID = "27349b0e98524e3b8bf524aeced849b2";

		public const string INVALID_LOAD_PROFILE_START_DATE_TIME_NODE_MISSING = "e3b1b6ae79ff4f0fb42208a67c163dfb";
		public const string INVALID_LOAD_PROFILE_START_DATE_TIME = "0ecfaf8607ad42f6a65391ecd9eb1535";
		public const string INVALID_LOAD_PROFILE_END_DATE_TIME = "bd88a81e13a94ec5a124ca8d45705849";
		public const string INVALID_LOAD_PROFILE_DATE_TIME_RANGE = "6a32e786ed9e4c958e373f38313f1113";

        public const string INVALID_CHANNEL_SOURCE_ID_CHILD_NODE_INCLUDED = "25222ac6a96840dd98bc63d03e42c683";
        public const string UNSUPPORTED_CHANNEL_SOURCE_ID_CHILD_NODE = "335d9eb7eef74ebaae90967ef0caceae";
        public const string INVALID_LOAD_PROFILE_CONFIGURATION_COMMAND = "abcc4c21c9824a959f1dca6ec1aa62b8";
        public const string UNSUPPORTED_CHANNEL_SOURCE = "f31dc98b9bbf4397b18d61dc11a2cfc1";
        public const string INVALID_CHANNEL_SOURCE_ID_CHILD_INDEX = "51a20a62af8742a5a9504feeec2bf254";
        public const string INVALID_CHANNEL_SOURCE_ID_CHILD_INDEX_NODE_MISSING = "c8e3074c011d40869b4b65a61d1ac23d";
        public const string INVALID_CHILD_LOAD_PROFILE_CONFIGURATION_CHILD_INDEX = "81ca40e405ea46a1859e0583ccd09b53";
        public const string INVALID_CHILD_LOAD_PROFILE_CONFIGURATION_CHILD_INDEX_NODE_MISSING = "83bd266d0b204f6490d9a65506114b57";
        public const string INVALID_CHANNEL_SOURCE_CONFIGURATION = "49d09c0a5a2c41c49a0c58770e65e59d";
        public const string INVALID_INTERVAL_NODE_INCLUDED = "3ed93146896a497c88f9537564372491";
        public const string INVALID_BLOCK_INTERVAL_COUNT_NODE_INCLUDED = "8eb58e272042450188970638eb38f327";
        public const string INVALID_BLOCK_START_TIME_NODE_INCLUDED = "b4f33552a0bc48e393f556cd00077f39";
        public const string INVALID_ANSI_COMPLIANCE_STATUS_TYPE_ID_NODE_INCLUDED = "eed5aeb06e3d47e4a2de6c724f2dbc4b";
        public const string INVALID_SKIPPED_INTERVAL_VALUES_TYPE_ID_NODE_INCLUDED = "4bbcf34d921a4471a8a4064093716474";
        public const string UNSUPPORTED_CHILD_LOAD_PROFILE_CONFIGURATIONS_NODE = "9de540b223804bc0abb1740a0e786d8a";
        public const string INVALID_CHILD_LOAD_PROFILE_CONFIGURATION_NODE_MISSING = "22f129f9955040d0b71fdf38e424f367";
        public const string INVALID_CHILD_LOAD_PROFILE_CONFIGURATION_COMMAND = "ae6f5888ffa5489c9160e03a6c93d70f";
        public const string INVALID_POLL_RATE = "3ba2d64f821c4171ba6bdd515d984f72";
        public const string INVALID_POLL_WAIT = "4b79e4e01a264d33a2b3c47cbce193a1";
        public const string INVALID_POLL_ATTEMPTS = "130d990e752c4dbb9abf22f822e7e37c";
        public const string INVALID_POLL_RETRY_WAIT = "c41c77e3e4ef4b96ba90487b62665024";
        public const string DUPLICATE_CHILD_LOAD_PROFILE_CHILD_INDEX = "39f8b802b1d44f019659d9ee62c5613e";
		public const string INVALID_PRIMARY_NODE_NOT_INCLUDED = "6f253cf457824247a9f420c2b097e061";
		public const string INVALID_DATA_SET_ID = "97c00f67111b4b7aaae10aeeda603653";
		public const string INVALID_NUMBER_OF_BLOCKS_NODE_INCLUDED = "c2ad668be8854b28b88463b1a6faffcb";
		public const string INVALID_NUMBER_OF_BLOCKS = "15f84256720a4e60a42accaa01dd304e";
		public const string INVALID_LOAD_PROFILE_DATA_SET_COLLECTION_TYPE = "4322a7c23cf049d495f55073820d974c";
		public const string INVALID_LOAD_PROFILE_DATA_SET_ID = "e904088ad8fb4cc9a6ad10b54b9b692c";
		public const string INVALID_LOAD_PROFILE_DATA_SET_STATUS = "a41cb6e6ddbb479eab47f4ddda3a30c8";
		public const string DUPLICATE_LOAD_PROFILE_DATA_SET = "33c8e0e0a8a344679cafc7ac0f1538bc";

        public const string INVALID_GATEWAY_SERIAL_NUMBER = "d42b4391306a474abaaf49a25e5a1fae";
        public const string INVALID_MOVE_DEVICE_COMMAND = "839cb55105af4c5e9818613df73c29cd";
        public const string MULTIPLE_GATEWAYS_WITH_NAME = "a09e42d949aa4eb1ba0ce66878fddfd7";
        public const string DEVICE_ALREADY_ASSOCIATED_WITH_GATEWAY = "8534b74d6cbe40d2bf0a197603df0909";

		public const string INVALID_GATEWAY_ATTRIBUTE_NODE_MISSING = "19fd1b62d305434bbc6cd858f92e796b";
		public const string INVALID_GATEWAY_ATTRIBUTE_ID_NODE_MISSING = "6680bc2124d64045aa6667c21645ab8a";
		public const string INVALID_GATEWAY_ATTRIBUTE_VALUE_NODE_MISSING = "d6fd80aed0ce4b628efd6821affb037e";
		public const string INVALID_GATEWAY_HIERARCHY_LEVEL_MEMBER_NODE_MISSING = "b5706c49dc1746c5b3cbd5e8b6b7ba03";
		public const string INVALID_GATEWAY_HIERARCHY_LEVEL_MEMBER_ID_NODE_MISSING = "36c77e40f81f480c8382d1a2844e0197";
		public const string INVALID_GATEWAY_ATTRIBUTE_ID = "497925edfdd548b2ac559c1c86bb8245";
		public const string INVALID_GATEWAY_ATTRIBUTE_VALUE = "f8dcf5e09640480bb2b9b01bd8d1a6df";
		public const string INVALID_GATEWAY_ATTRIBUTES_LOGICAL_RESTRICTION_TYPE_ID = "351778009e96408eb129a63cf31ae1c0";
		public const string INVALID_GATEWAY_ATTRIBUTE_SEARCH_CRITERIA = "b454e27321bf4ca9bc71dfe80ad937ec";
		public const string INVALID_GATEWAY_HIERARCHY_LEVEL_MEMBER_ID = "5cf910c96b2645489fc7d0690e5c64f8";
		public const string INVALID_GATEWAY_HIERARCHY_LEVEL_MEMBER_RETRIEVE_TYPE_ID = "b330a62474ee47e49596c211a88f296a";
		public const string INVALID_GATEWAY_HIERARCHY_LEVEL_MEMBERS_LOGICAL_RESTRICTION_TYPE_ID = "3388f90825764cc7b423ffb1dc97da4b";
		public const string INVALID_GATEWAY_HIERARCHY_LEVEL_MEMBER_SEARCH_CRITERIA = "f3a7746634e74ceab50056b10fd2b525";
		public const string INVALID_DEVICE_SEARCH_CRITERIA = "a96a67b60ae141f69a6c777272722436";
		public const string INVALID_UPDATE_HISTORY = "9208663921034704b8d0e430194b09a0";
        public const string UNSUPPORTED_GATEWAY_VERSION_FOR_CHILD_DEVICE = "9bb7042ab6f04e958db2de4b4cf9c1c3";
        public const string INVALID_PROVISIONING_IDENTIFIERS_NODE_MISSING = "2194e686684247818da9ccd898be0e04";
        public const string INVALID_PROGRAM_NUMBER_NODE_MISSING = "b518748678a349ab88df97b3c882f288";
        public const string INVALID_PROGRAM_NUMBER = "51be95b1bb1f444f97fb4865636c333f";
        public const string INVALID_DATA_URGENCY_TYPE_ID_NODE_MISSING = "8434e72c5c8b4ab98f82ad7e11e717c7";
        public const string INVALID_DATA_URGENCY_TYPE_ID = "a182fd4f5f4442c39ebc13079f3fc424";
        public const string INVALID_RETURN_GENERAL_INFORMATION_NODE_MISSING = "587134d0e9ef41ca8ca8b686af82cf91";
        public const string INVALID_GATEWAY_TIMEOUT_DATETIME = "23a30cf5225f438c921ad5681fdda74d";

		public const string INVALID_SET_MEASUREMENT_RATIO_CONFIGURATION_COMMAND = "8873eebb9d474cebad3486ddfef27364";
		public const string DEVICE_SUPPORT_FOR_CURRENT_TRANSFORMER_UNKNOWN = "93ee595b2fd0491e93d41623cc46508d";
		public const string DEVICE_CURRENT_TRANSFORMER_UNSUPPORTED = "e4be5dd6064d4e6cbccdf8060d094b7f";
		public const string CURRENT_TRANSFORMER_PRIMARY_NODE_MISSING = "647b10aa0ea94e2597d6c81d0b8eef0c";
		public const string INVALID_CURRENT_TRANSFORMER_PRIMARY = "da926c6945aa411b8bd8ccd9d1fc6153";
		public const string CURRENT_TRANSFORMER_SECONDARY_NODE_MISSING = "4737ec3b443a444192aaff4f36ea7006";
		public const string INVALID_CURRENT_TRANSFORMER_SECONDARY = "2a7ea6f090a044fd97baea79bb209d9d";
		public const string INVALID_CURRENT_TRANSFORMER_RATIO = "f11a7104f41244c0ac4e6edbfc984e91";
		public const string VOLTAGE_TRANSFORMER_PRIMARY_NODE_MISSING = "585a5d8c867e44a5b4d308ac3207f18f";
		public const string INVALID_VOLTAGE_TRANSFORMER_PRIMARY = "7871f92c94814dfc937fd19ca632c9ec";
		public const string VOLTAGE_TRANSFORMER_SECONDARY_NODE_MISSING = "a62f155f885a4bc8a7a523fb52122799";
		public const string INVALID_VOLTAGE_TRANSFORMER_SECONDARY = "e14dffbfde84440babf63da884c0d6e4";
		public const string INVALID_VOLTAGE_TRANSFORMER_RATIO = "9c8167c9761d4fbfb3cecfc6fd604034";
		public const string INVALID_APPLIED = "fba5debbf67c4b86892065c6bb736287";
		public const string DEVICE_SUPPORT_FOR_CURRENT_TRANSFORMER_READ_FAILURE = "a98a3dc6bd824c44b89920b688ab2e45";
		public const string INVALID_SET_MEASUREMENT_RATIO_CONFIGURATION_COMMAND_NODE_MISSING = "f1207cdcc80548de8d059e9539684f19";
		public const string INVALID_DISPLAY_FORMAT_NODE_MISSING = "fe9056c67126403f83bdbc8ba22b1039";
		//Prepay
		public const string INVALID_PREPAY_NODE_MISSING = "ED13CBC5EEC54853A9B9939A3F8156BB";
		public const string INVALID_ADD_CREDIT_OPTION_TYPES_NODE_MISSING = "70F8E9E7961F4b00A9EA720A9023D343";
		public const string INVALID_ADD_CREDIT_OPTION_TYPE_NODE_MISSING = "76B45232FB15476eB8C296F12612CD5B";
		public const string INVALID_ADD_CREDIT_OPTION_TYPE_ID_NODE_MISSING = "679B26AD9B564fb59C004A10177FAC3C";
		public const string INVALID_ADD_CREDIT_OPTION_TYPE_ID = "90B9761A670D4f54ACB5419F73B1C1F7";
		public const string INVALID_CREDIT_VALUE_NODE_MISSING = "D4BF3EE807964c629C4307054EE25F68";
		public const string INVALID_CREDIT_VALUE = "265D0505FDAF41449A5A2A533F345E80";
		public const string INVALID_CLEAR_CREDIT_OPTION_TYPE_ID_NODE_MISSING = "FE2852B48F9E4a248241CB9E16E8C28B";
		public const string INVALID_CLEAR_CREDIT_OPTION_TYPE_ID = "109BF61F9A874092B7D320A7336B8B8B";
		public const string INVALID_PREPAY_CONFIGURATION_NODE_MISSING = "998844EC86544968B900879DD70C903D";
		public const string INVALID_PREPAY_STATUS_TYPE_ID = "DB2E3DB311E94cd2AEE28C1FC781A53E";
		public const string INVALID_EMERGENCY_CREDIT_STATUS_TYPE_ID = "9CEED3091C8A4726BBBD3AD75E8E6091";
		public const string INVALID_MAXIMUM_EMERGENCY_CREDIT = "DD6FD62BCE284fb0A9DF2A7F8EA6CC0C";
		public const string INVALID_REVERSE_POWER_DEDUCTION_STATUS_TYPE_ID = "49C194B7C6DA4669B48703E1F1136428";
		public const string INVALID_MAXIMUM_POWER_STATUS_TYPE_ID = "E1B8BC2925594e69BB8AC4A4A7844E49";
		public const string INVALID_AUDIBLE_ALARM_STATUS_TYPE_ID = "E8AB336112F5454795DDA7C272FD0935";
		public const string INVALID_LOW_CREDIT_ALARM_LEVEL = "953AE6EED99D43c99825E3B1C4DD0B23";
		public const string INVALID_TIER_NODE_MISSING = "37EF9F0582C64de3A34C1F02568959D8";
		public const string INVALID_TIER_COUNT = "9E6065731DD94dcfAF088A5AF7750CF0";
		public const string INVALID_TIER_INDEX_NODE_MISSING = "36B0DDDAD83242a4A62710D939F08463";
		public const string INVALID_TIER_INDEX = "4EEEDBA606C948ffB73DB7547E1C67BE";
        public const string DUPLICATE_TIER_INDEX = "A4DCBE6BF7A54caeB5AD1983F5F1FFB5";
		public const string INVALID_TIER_RATE_NODE_MISSING = "BF42229EE6D94293BCD63A0431A934F2";
		public const string INVALID_TIER_RATE = "61888BE8D39640579EE7B414F3F7F561";
		public const string INVALID_SET_PREPAY_CONFIGURATION_COMMAND = "3FFE9A735F8C49a584CD47B7B8879DA2";
		public const string INVALID_CLEAR_CREDITS_BEFORE_ADD_OPTION_TYPE_ID = "0fc5d19e8445434991635c172ef05295";

		//2.0 tou stuff
		public const string INVALID_CALENDAR_ID_NODE_MISSING = "B349AAABA85B4C98B1E3FAD02038D901";
		public const string INVALID_TOU_RECURRING_DATES_NODE_MISSING = "C3D9F5FB91AA4D94B41D3241F467CE45";
		public const string INVALID_RECURRING_DATES_SCHEDULE_NODE_MISSING = "9F5A1086DAD04B4E87F800DECFAF76A1";
		public const string INVALID_RECURRING_DATES_INDEX_NODE_MISSING = "FABAEF2277184F2B9F2086BF4D2A94E9";
		public const string INVALID_RECURRING_DATE_NODE_MISSING = "99E5116612E640C09FDB356A604A680D";
		public const string DUPLICATE_RECURRING_DATES_SCHEDULE_INDEX = "C8D4FDCBEBD54E71A66EDC89A6515FB4";
		public const string INVALID_RECURRING_DATE_MONTH_NODE_MISSING = "4E8FBE68D20C4108ABC878483325ECD0";
		public const string INVALID_RECURRING_DATE_OFFSET_NODE_MISSING = "9A234F571E2248069F0A2416516971D8";
		public const string INVALID_RECURRING_DATE_WEEKDAY_NODE_MISSING = "76DF2EED08DE4303B4E20E2D6D0A8B69";
		public const string INVALID_RECURRING_DATE_PERIOD_NODE_MISSING = "B1E495758E254D85A91607D0195A25A8";
		public const string INVALID_RECURRING_DATE_DAY_NODE_MISSING = "9C21DD13410A4B53BC80FA19B96944F3";
		public const string INVALID_RECURRING_DATE_DELTA_NODE_MISSING = "3E9A459C848042CC91C167490E6A9870";
		public const string INVALID_RECURRING_DATE_ACTION_NODE_MISSING = "00DA47B028174C13B03A7E9ACC64B4C5";
		public const string INVALID_ACTIVATE_PENDING_TOU_CALENDAR_DATE_TIME_NODE_MISSING = "19BD20C134F2418F85DDDD6AC19260C7";
		public const string INVALID_RECURRING_DATE_PERFORM_BILLING_READ_NODE_MISSING = "5321DE9169E84A28899390DFBE50BEF4";
		public const string INVALID_RECURRING_DATES_SCHEDULE_INDEX_NODE_MISSING = "BDA1D72F898E4DC3B58151FE3560CABC";
		public const string INVALID_RECURRING_DATES_DST_ACTION = "5A3850C3A4454CE78F8B522A4FFEFB62";
		public const string INVALID_RECURRING_DATES_SPECIAL_SCHEDULE_ACTION = "AC578BC952194867B8A468B772FAA8BF";
		public const string INVALID_RECURRING_DATES_SCHEDULE_INDEX = "386FD20DBB9D4CF9BB11AF834B14C095";
		public const string INVALID_RECURRING_DATES_MONTH = "05C8BFB1D5F747f5882634487D4995F1";
		public const string INVALID_RECURRING_DATES_OFFSET = "13C87067CC61436c8037F121D908BFB1";
		public const string INVALID_RECURRING_DATES_WEEKDAY = "42AE7A9EA0434e14922DFAA47F44FECF";
		public const string INVALID_RECURRING_DATES_DAY = "AB84C78514AE40a4A7760050D48D7A37";
		public const string INVALID_RECURRING_DATES_PERIOD = "504FA84F478249e9AD716DF7330BB8C4";
		public const string INVALID_RECURRING_DATES_DELTA = "F74119C11A0649499F7FDD04D4FD5086";
		public const string DUPLICATE_RECURRING_DATES_ACTION = "F35D430F6EF347F1B16208BEA90D2269";
		public const string INVALID_RECURRING_DATES_ACTION = "CEA8B3233B14469090DD881D4842175C";
		public const string INVALID_RECURRING_DATES_MONTH_ORDER = "82FBB76CF5F24828826BEE78F7AFCEBD";
		public const string INVALID_RECURRING_DATES_PERFORM_BILLING_READ = "E3DC115A9A044224BE292C5D4E2EA27D";
		public const string INVALID_ACTIVATE_PENDING_TOU_CALENDAR_DATE_TIME = "6C65F1244F8F45BDB9BDB2F1FC773E21";
		public const string INVALID_DAY_SCHEDULES_NODE_MISSING = "50BDC636BB564CF8B3F7616B6FCB8342";
		public const string INVALID_DAY_SCHEDULES_SCHEDULE_NODE_MISSING = "1483C31C7290417ABC10F0674C61416E";
		public const string INVALID_DAY_SCHEDULE_INDEX_NODE_MISSING = "8ABF297B88D9461A994945DA35216E08";
		public const string INVALID_DAY_SCHEDULE_SWITCH_NODE_MISSING = "1AEEE8FB6AEF4E9F893FCDDB193ACD25";
		public const string INVALID_DAY_SCHEDULE_SWITCH_INDEX_NODE_MISSING = "793EE7B65EC54BEA88DF1405D5D9C5CC";
		public const string INVALID_DAY_SCHEDULE_TIER_NODE_MISSING = "A8718F4EB34E4489A9DF6D98D8944F85";
		public const string INVALID_DAY_SCHEDULE_START_HOUR_NODE_MISSING = "7A9EB66D4959407A8C47D5982B0A2643";
		public const string INVALID_DAY_SCHEDULE_START_MINUTE_NODE_MISSING = "8C98168594D54B16A37B722E5A6A8DCE";
		public const string DUPLICATE_DAY_SCHEDULE_SWITCH_INDEX = "35FFED4B20034E7D93A4C0996BB0839F";
		public const string INVALID_CALENDAR_ID = "229890661FED4DFDB95F08357E9FB64E";
		public const string DUPLICATE_DAY_SCHEDULE_INDEX = "5B0185B7730C45B7B33C805231068058";
		public const string INVALID_DAY_SCHEDULE_INDEX = "C930FE4908EF4d9590D01247FDCA16D1";
		public const string INVALID_DAY_SCHEDULE_SWITCH_INDEX = "7C14A0BE198248e68FAF644068F27714";
		public const string INVALID_DAY_SCHEDULE_START_HOUR_START_MINUTE = "ED55BBEF2FFB427BB05CCA341D2929E9";
		public const string INVALID_DAY_SCHEDULE_START_HOUR_START_MINUTE_NON_ZERO = "C06E0D6F52214737A7D3FF169788C84F";
		public const string INVALID_DAY_SCHEDULE_TIER = "DEAF2D4059CE445e94BD228478201DAE";
		public const string INVALID_DAY_SCHEDULE_START_HOUR = "C64BF0CBA1B84a389DA4562ED31539E4";
		public const string INVALID_DAY_SCHEDULE_START_MINUTE = "DC94811B66E84e07923CB7B2EF3F8CED";
		public const string INVALID_SEASON_SCHEDULES_NODE_MISSING = "17A69BB8A8D541ED9C8604BC12665103";
		public const string INVALID_SEASON_SCHEDULES_SCHEDULE_NODE_MISSING = "B6C51E860B8645D1BB43D7E3780826FF";
		public const string INVALID_SEASON_SCHEDULE_INDEX_NODE_MISSING = "D04C10895FF54118875213D2B29AEA13";
		public const string INVALID_SEASON_SCHEDULE_SATURDAY_NODE_MISSING = "9301478647F2464BA2A279BE02468122";
		public const string INVALID_SEASON_SCHEDULE_SUNDAY_NODE_MISSING = "5589B02AFF4E42EA9C1065F1FA7E0181";
		public const string INVALID_SEASON_SCHEDULE_WEEKDAY_NODE_MISSING = "C03DFC541F12483FBCAC415DF1CBE454";
		public const string INVALID_SEASON_SCHEDULE_SPECIAL_0_NODE_MISSING = "A7C30497715D4017A570EF0DA566C68F";
		public const string INVALID_SEASON_SCHEDULE_SPECIAL_1_NODE_MISSING = "04BF80E8D7CC46C3B405CBA5968DB776";
		public const string INVALID_SEASON_SCHEDULE_INDEX = "920388D6BB154286A44A3686F0CACDFF";
		public const string INVALID_SEASON_SCHEDULE_SATURDAY = "8D05A3F0216744aa978A6DFE0875D559";
		public const string INVALID_SEASON_SCHEDULE_SUNDAY = "33A8DDD671154e0590BCFE9A86AA58A5";
		public const string INVALID_SEASON_SCHEDULE_WEEKDAY = "41eecd391c8e42b0af5f5d297ecd5b08";
		public const string INVALID_SEASON_SCHEDULE_SPECIAL_0 = "CA6A5AF5C20C40d4B75986A785D14D53";
		public const string INVALID_SEASON_SCHEDULE_SPECIAL_1 = "5D21A9FC959B4c63A8F33E73E699D012";
		public const string INVALID_CALENDAR_PERFORM_SELF_READ_VALUE = "e1db2f18363847568e7018e91938f5f8";
		public const string INVALID_READ_SCHEDULE_NODE_MISSING = "376f9aac50704359b8a55956ef8e5c6c";

		//2.3 tou stuff
		public const string INVALID_READ_TOU_CALENDAR_OPTION_TYPE_ID = "AAABD09920144b46BAA47124229B7DB6";

        //3.0 tou stuff
        public const string INVALID_TOU_CALENDAR_NODE_MISSING = "509D675F00AD4EC0A3F0D44A6FF0EE7D";
        public const string INVALID_TOU_RECURRING_DATES_PERFORM_DEMAND_RESET = "b15c10871be04e23bf86bf19631ee642";
        public const string INVALID_TOU_CALENDAR_PERFORM_DEMAND_RESET = "1445d2865f1741158e044b5bb4878305";

        //v5.0 LDA Configuration
        public const string INVALID_LOCAL_DATA_ACCESS_CONFIGURATION_SOURCES_NODE_MISSING = "50594496c8ea4da0853437031e69510e";
        public const string INVALID_LOCAL_DATA_ACCESS_CONFIGURATION_SOURCES_SOURCE_ID = "3680535dcb6148b292f4f8c202edaa6f";
        public const string INVALID_LOCAL_DATA_ACCESS_CONFIGURATION_SOURCES_SOURCE_COLLECTION_RATE = "962bc2a6b1c746aeb472a3ba06ec8ea5";
        public const string INVALID_LOCAL_DATA_ACCESS_CONFIGURATION_SOURCES_SOURCE_COLLECTION_RATE_NODE_MISSING = "c9bb5848efb1411b8615ae128921dbca";
        public const string INVALID_LOCAL_DATA_ACCESS_CONFIGURATION_SOURCES_SOURCE_ID_NODE_MISSING = "4da2a44050114491a8a003eba9075316";

		// Retrieve List Enhancements
		public const string INVALID_DATASET_NEXT_COMMAND_HISTORY_REQUEST_DATE_TIME_NODE_MISSING = "a8578d0252514b94808e672a0b46e4c0";
		public const string INVALID_DATASET_NEXT_COMMAND_HISTORY_COMMAND_ID_NODE_MISSING = "e4e1c988a61c46b0bde74404b6eed3a0";
		public const string INVALID_DATASET_NEXT_COMMAND_HISTORY_ID_NODE_MISSING = "714d271afe32440485d436cd01704a29";
		public const string INVALID_DATASET_NEXT_COMMAND_HISTORY_COMMAND_ID = "1e2fbb73e88942d9b5cadd58573b764d";
		public const string INVALID_DATASET_NEXT_COMMAND_HISTORY_REQUEST_DATE_TIME = "81846fd911e1437ea418af1d2ee9385d";

		public const string INVALID_DATASET_NEXT_DEVICE_ID_NODE_MISSING = "fb1ebcb10d8f4ddea9e015c7fe10b674";
		public const string INVALID_DATASET_NEXT_DEVICE_NEURON_ID_NODE_MISSING = "2a8e9eb11dda463594a26695b15aee17";
		public const string INVALID_DATASET_NEXT_DEVICE_NEURON_ID = "0b58594eaeaa46859d33b33014b94107";
        public const string INVALID_DATASET_NEXT_DEVICE_LAST_UPDATE_DATE_TIME = "c025148a53ae4f8aa710fca973f902ef";
        public const string INVALID_DATASET_NEXT_DEVICE_LAST_UPDATE_DATE_TIME_NODE_MISSING = "9ed6281075d5471d82e90fc48271228b";
		public const string INVALID_DATASET_NEXT_DEVICE_NAME_NODE_MISSING = "2d1322f2d67240ff88e0f73682bf5f7b";
		public const string INVALID_DATASET_NEXT_RESULT_ID_NODE_MISSING = "362258af5e5f42bd886761cbe9990248";
		public const string INVALID_DATASET_NEXT_RESULT_RECORDED_FROM_DEVICE_DATE_TIME_NODE_MISSING = "a02f7593ab3d4f91822b4fd1488413f5";
		public const string INVALID_DATASET_NEXT_RESULT_RECORDED_FROM_DEVICE_DATE_TIME = "9c9ce572154848e4975235a2e5ec1c9f";
		public const string INVALID_DATASET_NEXT_RESULT_RECORDED_FROM_DEVICE_DATE_TIME_RANGE = "73d556a518de4d8e9d6e0441611b5ef5";
		public const string INVALID_DATASET_NEXT_RESULT_ID = "f258c11c8261418c964e4a8faf86b2b9";
		public const string INVALID_DATASET_NEXT_DEVICE_ID = "4087360488624e17a9976e4a89df7b38";
		public const string INVALID_DATASET_NEXT_DEVICE_NAME = "6d02a36dafed4048a06dcf739e243a15";

        public const string INVALID_GEOGRAPHIC_COORDINATES_CHILD_NODE_MISSING = "0b5af98a5cf349979496f0dc5555fd52";
        public const string INVALID_GEOGRAPHIC_COORDINATES_LONGITUDE_NODE_MISSING = "a7b51fc955e44d6bb8097cd69a6a8a42";
        public const string INVALID_GEOGRAPHIC_COORDINATES_LATITUDE_NODE_MISSING = "77c69b0be6a7402eacb86607a7dc90c9";
        public const string INVALID_GEOGRAPHIC_COORDINATES_LONGITUDE = "1aeba5614f464124bbcf1e0f6f6b39f4";
        public const string INVALID_GEOGRAPHIC_COORDINATES_LATITUDE = "a942699901c14436a6dedadfa5b20782";

		public const string INVALID_DISCONNECT_FEEDBACK_STATUS_NODE_MISSING = "a4dc0f31f4f3422b86a6cf6cee5a8ae1";
		public const string INVALID_DISCONNECT_FEEDBACK_STATUS_TYPE_NODE_MISSING = "221725e2bd234c7b82e744b09614cce6";
        public const string INVALID_DISCONNECT_FEEDBACK_STATUS_TYPE = "96065b70a5604221a14c69a84c72023b";
		public const string INVALID_DISCONNECT_LOCKED_OPEN_STATUS_TYPE_NODE_MISSING = "63300653cefa41f0b0b3e4c45344b52a";
		public const string INVALID_DISCONNECT_LOCKED_OPEN_STATUS_TYPE_ID_NODE_MISSING = "ee31557a31114a9d9e320041b10b881c";
        public const string INVALID_DISCONNECT_LOCKED_OPEN_STATUS_TYPE_ID = "4a4c1704367c46c881252e1cd7e51387";

		// Expression Builder Internal API (saving of expression)
		public const string INVALID_EXPRESSION_DATA_POINT_PARAMETER = "85225d55bb764eca9fd5f33dfc4725e7";
		public const string INVALID_EXPRESSION_LENGTH = "b0e000b9c564409084f0de359277b4e6";
		public const string INVALID_FUNCTION_CALL_EXPRESSION_SYNTAX = "05f4b0994eeb4a4cb81599c9ccccec62";
		public const string INVALID_SOAP_CALL_EXPRESSION_SYNTAX = "1d8ec4a57ec941a1ac82218a3c6c72da";
		public const string INVALID_EXPRESSION_DATA_POINT_PARAMETER_NO_RESTRICTIONS = "FD46B5EA53FD44b49A9166E64EE94A04";
		public const string INVALID_SYNTAX = "6511FCF4AE614d87B8F947B56E1F5147";
		public const string INVALID_EXPRESSION_CALL_PARAMETER = "0C07350D3C404e4b880E3A9C9B3D3F97";
		public const string INVALID_DATETIME_SYNTAX = "B43EB1D478D541a896B464448578875F";

		// SettingManager codes
		public const string INVALID_SETTING_NOT_NUMERIC = "8ec661f1063d4644a46b9bdf88fd7c0b";
		public const string INVALID_SETTING_OUT_OF_RANGE = "b94892f54e67476fa1dff9e20d39e2d0";
		public const string INVALID_SETTING_EMAIL = "b3b5262ae651435987ef6a75179110ed";
		public const string INVALID_SETTING_VALUE_LENGTH = "ba014de02b454724b35f4ead2178d0a9";
		public const string INVALID_SETTING_ID = "28bdfbb073e24cc0b43f7520a75323e2";
		public const string INVALID_SETTING_TYPE_ID = "E9EE95D8EAD143d183541147F574B10E";
		public const string INVALID_SETTING_TYPE_ID_NODE_MISSING = "7be41ad0123b47bf9f48c6aa27f23c0b";
		public const string INVALID_SETTING_VALUE = "28CD31D77623401d90D7772BFB71ED36";
		public const string INVALID_SOLUTION_SETTING_VALUE_TYPE_ID = "017209EC20A4468d91596367C56C92BC";
		public const string UNSUPPORTED_SOLUTION_SETTING_VALUE_TYPE_ID = "A69938DD1AD544119A1E10B56B3EC9BB";
		public const string INVALID_TABLE_NAME = "5836599954C94e4dB19706D3381997C8";
		public const string INVALID_TABLE_NAME_NODE_MISSING = "914e38a33f934bd498e657f3eeb7c973";
		public const string INVALID_COLUMN_NAME = "F1D95AA741A54e11AEBD3C68531C7D59";
		public const string INVALID_COLUMN_NAME_NODE_MISSING = "1f115aee26b54c80a0cef6c757a57a44";
		public const string INVALID_EXPIRED_INTERVAL_TYPE_ID = "8B03750B3D6341faAA2B5E02D6095EAE";
		public const string INVALID_EXPIRED_INTERVAL_TYPE_ID_NODE_MISSING = "4d5aaf3876714c20bc12cf85e151cc2b";
		public const string INVALID_EXPIRED_INTERVAL = "FB30EB1C877D4c33A206D40F1C36E604";
		public const string INVALID_EXPIRED_INTERVAL_NODE_MISSING = "f23cceb8f2a2422f814440b6072f23e2";
		public const string UNSUPPORTED_SETTING_TYPE_ID = "04571597BF814620BFA6256E440F593A";
		public const string WAN_CONFIGURATION_NOT_SET_UP = "b40f7e5d97a14d39a6709d64ea012cd0";
		public const string WAN_CONFIGURATION_IN_USE = "0795e486c48e4de58bf1f15cce6353b9";
		public const string INVALID_WAN_CONFIGURATION_ID = "5823e0e6ff3547f484a04ea9afe1c179";
		public const string INVALID_OUTBOUND_CONNECTIONS = "b0a639b67b95468aa170a2fb83208473";
		public const string INVALID_SCHEDULE_ASSIGNMENT_ID = "C8D9D0A1CED949b090685069C6A1A408";
		public const string INVALID_SCHEDULE_NULL = "63199B9508A2413984B1982B76AB73D2";
		public const string INVALID_RETRIEVE_ENCRYPTED_VALUE = "f325722b70cd48019193a7f815591b8b";
		public const string INVALID_WAN_CONFIGURATION_TYPE = "d7583461d0934588b4dfdf07dd76435c";
		public const string INVALID_SETTING_NODE_MISSING = "5c542febb54a4ff983fa22868ed0e163";
		public const string INVALID_SETTING_ID_NODE_MISSING = "bf5b9c9aff59418e8672a5d966579306";
		public const string INVALID_WAN_CONFIGURATION_GATEWAY_COMM_TYPE_MISMATCH = "7589733f21f0498ba3fd2e30e71aa0a4";
		public const string INVALID_SETTING_VALUE_RANGE = "e79111001a9d44e6acc293f11f7c12c9";
		public const string INVALID_CONNECTION_GUARD_TIME = "68772ed056414112add88ddb6ac821de";
		public const string INVALID_NUMBER_OF_RETRY_ATTEMPTS = "454d1bc6523f4cc9b75e38f1776624bb";
		public const string INVALID_RETRY_ATTEMPTS_INTERVAL = "21d5e58a3315420ca60836254deaefdc";
		public const string INVALID_MAXIMUM_TIME_TO_CONNECT = "8f1f5ccb4a7142ee9cda23613df95009";
        public const string INVALID_SEND_HELLO = "c13851288e324f68ae62c5162db35461";
		public const string INVALID_USE_AS_DEFAULT = "947c73dd72274947884c10af67e997f6";
		public const string INVALID_USE_AS_DEFAULT_MULTIPLE = "8aafb5fb028c4e03b5e70bb3216abf10";
		public const string INVALID_DEFAULT_ASSIGNMENT_NAME = "0e9b8f663440477b847fcbe986167d78";
		public const string INVALID_DEFAULT_ASSIGNMENT_NAME_NODE_MISSING = "966d475c907a49d0b8d14afc0bc87f65";
		public const string INVALID_IP_ADDRESS_TYPE_ID = "5d8fee62e3a04f8dace07c2303410ad8";
		public const string INVALID_WAN_CONFIGURATION_NAME = "507a0c9aee984be688eece26bcee9395";
		public const string INVALID_WAN_CONFIGURATION_NAME_DUPLICATE = "9eac8641046d4ee9af7736b8c7182ac5";
		public const string INVALID_PING_GATEWAY_STATUS_TYPE_ID = "2bc1fb07bf3d4acc9c307a8b7540d818";
		public const string INVALID_SOLUTION_SETTING_NAME = "f5c613a32549436dad30c01ddd21d29b";
		public const string INVALID_SOLUTION_SETTING_NAME_DUPLICATE = "09405518d15c48bcb97adda304796b5d";
		public const string INVALID_UNSUCCESSFUL_PING_RESPONSE_GUARD_TIME = "01fb37f31bbc4ccca655e26954f9b704";
		public const string INVALID_TEST_TCPIP_PORT_STATUS_TYPE_ID = "872eff9f3bc744c8bf21e2c38417360a";
		public const string CONFLICTING_PING_AND_TEST_TCPIP_STATUS_TYPES = "c622802b5bf341c393951a9bb4c8321d";
		public const string CONFLICTING_UNSUCCESSFUL_PING_RESPONSE_GUARD_TIME_AND_MAXIMUM_TIME_TO_CONNECT = "a07e3bf600ae49c7b08975b5c72a11d0";
        public const string INVALID_PASSIVE_FTP_IP_ADDRESS_SOURCE_TYPE_ID = "da687b271ff54e66ad89071901c99aff";
        public const string INVALID_TIER1_RETRIEVAL_STATUS_TYPE_ID_NODE_MISSING = "8fad4173762a46a59e1db811c3078aff";
        public const string INVALID_TIER2_RETRIEVAL_STATUS_TYPE_ID_NODE_MISSING = "8030a18ee5eb4726b0c221cb6f84c0d2";
        public const string INVALID_TIER3_RETRIEVAL_STATUS_TYPE_ID_NODE_MISSING = "c0b9d01e31124a5a8c31b9fd1d1493f9";
        public const string INVALID_TIER4_RETRIEVAL_STATUS_TYPE_ID_NODE_MISSING = "79fcb64148914baca59bffed36f0976a";
        public const string INVALID_TIER1_RETRIEVAL_STATUS_TYPE_ID = "ae411554db02457fae9ec3aa205cae8a";
        public const string INVALID_TIER2_RETRIEVAL_STATUS_TYPE_ID = "2f82b1854be34489887810240e602d99";
        public const string INVALID_TIER3_RETRIEVAL_STATUS_TYPE_ID = "bbbbcaea52b044d58681429673c2afad";
        public const string INVALID_TIER4_RETRIEVAL_STATUS_TYPE_ID = "b2d69c1e09794fe79621e0eb584d477c";
        public const string INVALID_MAXIMUM_BYTE_QUEUE_SIZE = "fb8675982cbe423d803ccbdedfdddd82";
        public const string INVALID_BYTE_QUEUE_INACTIVITY_TIMEOUT = "319fd2da2e374e358e9f088300dce37f";
        public const string INVALID_ADDITIONAL_CRITERIA = "d30da602a9f34549bb8e296ff37ae59d";
        public const string CONFLICTING_ADDITIONAL_CRITERIA_PARAMETERS = "88bb0b17421741ba8a7982810cbd810a";
		public const string INVALID_TEST_TCPIP_PORT = "54d49ce1a96e4af29409c0c70e8047df";

		//TypeManager Return Codes
		public const string CATEGORY_NODE_NOT_FOUND = "277E2167DF264d9bAC3BA09D67F46841";

		//FirmwareManager Return Codes
        public const string INVALID_FIRMWARE_IMAGE = "32639d7a350b41e4a823839b561cc85c";
        public const string INVALID_VERSION_NUMBER = "633bf2758d2b44c58ceb00c1058947c1";
		public const string INVALID_ENTITY_TYPE_ID = "c89247794c394284b04065fd6d5c0351";
		public const string INVALID_ENTITY_TYPE_TYPE_ID = "35d1506667a54d58a2173877fb0c1561";
		public const string INVALID_BUILD_DATETIME = "0288a11ec44d469698862c00d7332125";
        public const string INVALID_FIRMWARE_VERSION_ID = "f8305306d5394540a7843285de7f8a4c";
        public const string INVALID_FIRMWARE_VERSION_ID_NODE_MISSING = "250786ad1d5e4f81ad19f253d04bab41";
        public const string INVALID_FIRMWARE_VERSION_NUMBER_NOT_DEFINED = "113743FE24134138895A9FEAEAF377D4";
		public const string INVALID_VERSION_NUMBER_DUPLICATE = "3f22500c2e5d4c0ebe30e00e18d394d7";

		// Engine Manager return codes
		public const string INVALID_TASK_PROCESSOR_PORT = "999d30e15a89464ba5a6818d70672a79";
		public const string INVALID_TASK_PROCESSOR_PORT_DUPLICATE = "3d7d8cfa0d9a449ca687ea7e6be72891";
		public const string INVALID_HEARTBEAT_INTERVAL = "d6e3972830d749c399476acb55ad1fce";
		public const string INVALID_LOCAL_TASK_MANAGER_ID = "6e6eea719bf74dcb94f05177f3bf0897";
		public const string INVALID_LOCAL_TASK_MANAGER_ID_BLANK = "6c8673d65a344b12ac0b8533486166c1";
		public const string INVALID_LOCAL_TASK_MANAGER_ID_NODE_MISSING = "5651b0322f1a4e8999ce5441a40eb96d";
		public const string INVALID_ENGINE_RECEIVER_PORT = "129393b49c3e43de8465908afd791636";
		public const string INVALID_ENGINE_RECEIVER_PORT_DUPLICATE = "25bbb60213224f5080cf8f162f7f8ff4";
		public const string INVALID_TASK_PROCESSOR_ID = "90cca5554c9c4492ad0a455c4709d5b7";
		public const string INVALID_DELETE_TASK_PROCESSOR_RUNNING = "9c41f123447941af90b477aeac86e0b9";
		public const string INVALID_TASK_PROCESSOR_COMMAND_ID = "964d8deca24d43eaad748e8da5a5aaf6";
		public const string INVALID_TASK_PROCESSOR_COMMAND_ID_NODE_MISSING = "263ae98e8e2c4aeea3696abfc5ec2d33";
		public const string INVALID_TASK_PROCESSOR_COMMAND = "3452cfe8cf1c49b19d15d9d1b52d9f76";
		public const string INVALID_TASK_PROCESSOR_ID_NODE_MISSING = "9ee904168c6c4cd88df004e1f533a87f";
		public const string INVALID_TASK_PROCESSOR_STOP_COMMAND = "a34c6d94c55648bcb5a4eb5be7dbf111";
		public const string INVALID_TASK_PROCESSOR_START_COMMAND = "df274a2923b244b9a1a27a320747bf0c";
		public const string INVALID_COMMAND_HISTORY_ID = "f7d6fa8e502b48d48a50375b84b055db";
		public const string INVALID_ROUTING_ENTITY_TYPE_ID = "2e257e00d9c444fe8f6730720e372677";
		public const string INVALID_START_REQUEST_DATETIME = "53210a258bcf48ef8694b64a81700880";
		public const string INVALID_END_REQUEST_DATETIME = "1ef6fdeca979480ba2ca06cb705d0a6a";
		public const string INVALID_REQUEST_DATETIME_RANGE = "4a1e1c6745514e86ad1e06bd473d2285";
		public const string INVALID_START_COMPLETE_DATETIME = "edda6db491e74ff5a99c2592cf0bee4a";
		public const string INVALID_END_COMPLETE_DATETIME = "dc6af89458504bda8fe57032b05501cc";
		public const string INVALID_COMPLETE_DATETIME_RANGE = "aec50606ba754525a2793267ba20b6f0";
		public const string INVALID_TASK_PROCESSOR_TYPE_ID_DUPLICATE = "f22c965467ac4e0eab3eff36f3f34b0e";
		public const string INVALID_TASK_PROCESSOR_NAME_DUPLICATE = "4006e3a9edc242d282fafdbb7cd79f7b";
		public const string INVALID_TASK_PROCESSOR_NAME_BLANK = "1a100bea43c648f79857bbb9e7c6049f";
		public const string INVALID_MAX_CONCURRENT_TASKS = "e4c256f93d5e4f2996c384ab57af39b6";
        public const string INVALID_ENGINE_CREATION_NOT_ALLOWED = "296ad52f355d4f0593613cc7b5fff5c7";
		public const string UNSUPPORTED_TASK_PROCESSOR_TYPE_ID = "52c2b335e33d40e4a974f5f8b755dc5a";
		public const string INVALID_TASK_PROCESSOR_RUNNING = "09fd1df659b14cfdb51b19a2c744920e";
		public const string INVALID_ENGINE_IP_ADDRESS_ASSIGNMENT_TYPE_ID_NODE_MISSING = "ab84b69849cd47748ac81796cf85a1d8";
		public const string INVALID_ENGINE_IP_ADDRESS_ASSIGNMENT_TYPE_ID = "f5b63298ffe344e99e129e3855397583";
		public const string INVALID_DELETE_TASK_PROCESSOR_TYPE = "b8e5c1e1ab0343b8a1bb0e400c052022";
		public const string INVALID_TASK_PROCESSOR_TYPE_ID_NODE_MISSING = "74ab07d993bc42c4a1ea04e3dc4c0965";
		// New in v5.0
		public const string INVALID_DATABASE_POLLING_THREADS_MAXIMUM = "42636a3130c0460b8f523ddab17ab7c6";
		public const string INVALID_DATABASE_POLLING_THREADS_MINIMUM = "28e1b974a2924d2096bfcb9c4884ec74";
		public const string INVALID_DATABASE_POLLING_THREADS_CREATION_DELAY = "14f8274d60334a778b2e57ca09faf59b";
		public const string INVALID_DATABASE_POLLING_THREADS_DESTRUCTION_DELAY = "5172aab58ba34f698344235c176a30ca";
		public const string INVALID_DATABASE_POLLING_THREADS_RANGE = "e0c1f8eb3867467ba5cf962573aa06df";


		// Command History return codes
		public const string INVALID_COMMAND_HISTORY_TYPE = "B0112BC481C5456e82264689C31CDF6E";
		public const string INVALID_COMMAND_ID = "9e4f50d0d293475c85d551090b9c85da";
		public const string INVALID_COMMAND_NODE_MISSING = "5281ddfe6e1a41e5b24a6d35d2afd3d0";
		public const string INVALID_COMMAND_ID_NODE_MISSING = "3b99c1f425724d17abfe395384b2bfb1";
		public const string INVALID_ROUTING_ENTITY_NODE_MISSING = "ff0a283a789b474b85f0a02a013826b3";
		public const string INVALID_STATUS_TYPE_NODE_MISSING = "39ffc5a54a93419088228ef4d2ed77b0";
		public const string INVALID_ROUTING_ENTITY_TYPE_ID_NODE_MISSING = "950810a52f07450eae0af041a48499a6";
		public const string INVALID_ROUTING_ENTITY_ID_NODE_MISSING = "4aa587f58aa94d019555e812cd565915";
		public const string INVALID_STATUS_TYPE_ID_NODE_MISSING = "2d88c36f230e450a9c8884be630786a2";
		public const string INVALID_RESULT_TYPE_NODE_MISSING = "587a1932421c4674883785758e39fb0d";
		public const string INVALID_RESULT_TYPE_ID_NODE_MISSING = "adbd977d90794c3bb52102c445902596";
		public const string INVALID_COMMAND_HISTORY_NODE_MISSING = "acc162301c6547be9a67d36b0bafd08b";
		public const string INVALID_COMMAND_HISTORY_ID_NODE_MISSING = "06a5308202ad47d9b39ea8c29fa5c340";

		// EventManager return codes
		public const string INVALID_EVENT_DEFINITION_DELIVERY_TYPE_ID = "dc9c25f554024350940d4e30fe43062d";
		public const string INVALID_EVENT_DEFINITION_ID = "c80ac4b4910144439cfa478dea8d30ef";
		public const string INVALID_EVENT_DEFINITION_NAME_BLANK = "984f76135ebf40028272e1f0a0c619c5";
        public const string INVALID_EVENT_DEFINITION_NAME_DUPLICATE = "424020ab1d6c4294b16c73252ef832ab";
		public const string INVALID_EVENT_DEFINITION_PRIORITY = "3bc3a15c131c41aab5fc810095b0db90";
        public const string INVALID_EVENT_DEFINITION_STATUS_TYPE_ID = "2dfc2d30126e4478a30dcddf3dc761a3";
		public const string INVALID_EVENT_HISTORY_DATETIME_RANGE = "7c859066209c400d91d2d301c8257df2";
		public const string INVALID_EVENT_HISTORY_END_DATETIME = "079240dd067b45f4923fc3e717dfda9e";
		public const string INVALID_EVENT_HISTORY_ID = "362fba7a5b0f406bb8ca69994aa11142";
		public const string INVALID_EVENT_HISTORY_START_DATETIME = "9fe3a91d64964783b9615678f4c79086";
		public const string INVALID_HISTORY_NODE_MISSING = "8e96849bba5b4f8fba81368c170e3409";
		public const string INVALID_HISTORY_ID_NODE_MISSING = "8638987abc414e8394345f0bf766799a";
		public const string INVALID_EVENT_HISTORY_LIST_NODE_MISSING = "d1bc17d69c4c44a7ae6444af03dea289";
		public const string INVALID_EVENT_DELIVERY_STATUS_TYPE_ID = "ff77d228b71c418c95166488af5f2aad";
		public const string INVALID_DATASET_NEXT_EVENT_HISTORY_ID_NODE_MISSING = "b388b95a164641acb30439b209c7a0b5";
		public const string INVALID_DATASET_NEXT_EVENT_HISTORY_EVENT_DATE_TIME_NODE_MISSING = "0c5704d391674313a6e7e1846cb62a31";
		public const string INVALID_DATASET_NEXT_EVENT_HISTORY_EVENT_DATE_TIME = "775b20dd259a4d9b8684a8f350532044";
		public const string INVALID_DATASET_NEXT_EVENT_HISTORY_EVENT_DATE_TIME_RANGE = "f72b28f09eac43e6bac738b01755bfcd";
		public const string INVALID_DATASET_NEXT_EVENT_HISTORY_ID = "17f8973d502a40de9d4041da82189fcb";
        public const string INVALID_EVENT_HISTORY_NODE_MISSING = "18a84d1aa8324d75833c9ca3abc58a14";

		public const string INVALID_EVENT_TYPE_ID = "c18cbebc962c453d8a19c04b0695cc49";
		public const string INVALID_EVENT_TYPE_ID_NODE_MISSING = "c215da063f6b499b9f3d3f9f2bebc8ab";
		public const string INVALID_EVENT_TYPE_NODE_MISSING = "ea2dfaa4ef954a269d87f04072ff5e45";
		public const string INVALID_EVENT_DEFINITION_NODE_MISSING = "e09bdd8884d842b9b99a0e62d263f913";
		public const string INVALID_EVENT_DEFINITION_ID_NODE_MISSING = "f5ca42afc9ed4996974ff8ace3856af0";
		public const string INVALID_GATEWAY_EVENT_RETRIEVE_TYPE_ID = "73382a5e19bd4890a658c62954e4a3d1";

		public const string INVALID_DEVICE_ATTRIBUTE_SEARCH_CRITERIA = "c35f009d72b444e8a88f7ea0702c8fb9";
		public const string INVALID_DEVICE_ATTRIBUTES_LOGICAL_RESTRICTION_TYPE_ID = "19d843dd60cf4c0c8d5eede14da5f9ca";
		public const string INVALID_DEVICE_ATTRIBUTE_VALUE = "7b7b6d7d247e4ebd8c58dd2f10e7d8d1";
		public const string INVALID_DEVICE_ATTRIBUTE_VALUE_NODE_MISSING = "f6cefa76c67948cbb1f300d3af8e01cf";
		public const string INVALID_DEVICE_ATTRIBUTE_ID = "29c32e89edbb40589d9cdb58015243d4";
		public const string INVALID_DEVICE_ATTRIBUTE_ID_NODE_MISSING = "4cf1fd7455584b8f9447ae53d3eafd7e";
		public const string INVALID_DEVICE_ATTRIBUTE_NODE_MISSING = "21b4d0af77c049aeaaadbef06e869a52";

        public const string INVALID_RECEIVED_DATE_TIME_SEARCH_CRITERIA = "4a85e9576ce44065aac1005dc69dc6f7";
        public const string INVALID_RECEIVED_DATE_TIME_START_DATE_TIME = "c2701d4d3649489f99831a4f45c8f094";
        public const string INVALID_RECEIVED_DATE_TIME_END_DATE_TIME = "bd1365f4fb4e4f76ab88ac4fd74d13ad";
        public const string INVALID_RECEIVED_DATE_TIME_RANGE = "6cbfaa603f5246899d933bde0337cbec";
        public const string NO_EVENT_HISTORY_FOUND = "651130e703924c14a074ca39fb4ca29d";
        public const string INVALID_EVENT_HISTORY_ID_NOT_IN_A_BATCH = "3585b04259774597b868f0e3d58edc54";

		public const string INVALID_DEVICE_HIERARCHY_LEVEL_MEMBER_SEARCH_CRITERIA = "b00d56c866454818bb1322c9b5a39d3c";
		public const string INVALID_DEVICE_HIERARCHY_LEVEL_MEMBERS_LOGICAL_RESTRICTION_TYPE_ID = "ad4a0a5ef60a4edfa924f061731782bd";
		public const string INVALID_DEVICE_HIERARCHY_LEVEL_MEMBER_RETRIEVE_TYPE_ID = "fca9b7d87e0345e2a92c099a72a8e4b6";
		public const string INVALID_DEVICE_HIERARCHY_LEVEL_MEMBER_ID = "be66c976079c468489f5a684fddf808e";
		public const string INVALID_DEVICE_HIERARCHY_LEVEL_MEMBER_ID_NODE_MISSING = "026d36a3d47e4373a01bea72aaf81ae3";
		public const string INVALID_DEVICE_HIERARCHY_LEVEL_MEMBER_NODE_MISSING = "6e765dad7e9d43028bc08c243328e22d";

		// TaskManager return codes
		public const string INVALID_TASK_ID = "74daaf153c224d20b89c71017cd88644";
		public const string INVALID_TASK_PRIORITY = "e7b364a912fd45218e03bfe8588d59d2";
		public const string INVALID_TASK_STATUS_TYPE_ID = "800c9699c97a48b68ef4199e96ade146";
		public const string INVALID_TASK_TIMEOUT_DATETIME = "b011edcbd39f4fbebdd40db1af5f28e0";
		public const string INVALID_TASK_TO_DELETE = "ce2d082edbed416dab6d57e597356b51";
		public const string INVALID_TASK_TO_REQUEUE = "3b407d4982f54a5e929c9fa1dc11aade";
		public const string INVALID_TASK_TYPE_ID = "ac11725d88594d7db38ec678bc68bb70";
		public const string INVALID_CREATION_DATETIME_RANGE = "eb713728437c4460842a7e481ab3be4c";
		public const string INVALID_CREATION_END_DATETIME = "b8035e0d421f4603957b8a931cf5bc32";
		public const string INVALID_CREATION_START_DATETIME = "0d0335bb90a64503b4bf91223e7a05d6";
		public const string INVALID_EXECUTION_DATETIME_RANGE = "cf28397e4a9c43eaad15a84df13ab220";
		public const string INVALID_EXECUTION_END_DATETIME = "5b752d91d486479e8d8134adae552dc6";
		public const string INVALID_EXECUTION_START_DATETIME = "7e4c212e59f84f18b84c85b6f49ab094";
		public const string INVALID_TASK_TYPE_ID_NODE_MISSING = "4da3bbed01b34475bd4fd331bc5fccfd";
		public const string INVALID_ENTITY_NODE_MISSING = "682a12eb39cb4ef2a98437e0a923f79a";
		public const string INVALID_ENTITY_TYPE_ID_NODE_MISSING = "557abce06368464bacf3745676cdf94e";
		public const string INVALID_ENTITY_ID_NODE_MISSING = "47e7fcdff1fb4c8481ce8a460b41e24b";

		// General Core service return codes
		public const string INVALID_PARAMETER_NULL = "711fc7c2ef4d4e4581e1d932d7dd8cf1";
		public const string INVALID_GATEWAY_NAME_NODE_MISSING = "c3e96f30955b4e50895e553be1fe5b62";
		public const string INVALID_DATA_POINT_NAME_NODE_MISSING = "98a7bf0ec4a24c52b1aeacf5a5b441b9";
		public const string INVALID_DEVICE_NAME_NODE_MISSING = "deda944a1f5846a4bef71fc1a08a581e";
		public const string INVALID_NAME_SEARCH_TYPE_ID = "fb149df976074e118efd623470c41a8f";
		public const string INVALID_RESULT_NODE_MISSING = "4e0e909360de4a9aa1990344c60a3f0c";
		public const string INVALID_RESULT_ID_NODE_MISSING = "f8ef6fe360c1457eb21843c7fcc75e7b";
		public const string INVALID_EXECUTION_DATETIME = "672032d0d782410b98194d54f1228601";

		//RETURN CODES FOR SetGatewayProcessConfiguration
		public const string INVALID_PROCESS_CONFIGURATION_NODE_MISSING = "f21c4390e57b4c4896ae9821bd369fb1";
		public const string INVALID_PROCESS_CONFIGURATION_ID_NODE_MISSING = "a2e2ba6970d7442ab8d5840dada642ff";
		public const string INVALID_PROCESS_CONFIGURATION_ID = "65001ddd12ff407b86fbbaae5738ed54";
		public const string INVALID_PROCESS_CONFIGURATION_COMMAND = "067308d54714419baa2a118c8697dd79";
		public const string INVALID_STATUS = "0c2f16a513314a73b1b90a085e11af2d";
		public const string INVALID_RUN_DATE_TIME = "ecbbb55c9a6d495cb4b7a75ec981c4df";
		public const string INVALID_MINIMUM_ACTIVATION_INTERVAL = "7a852c793ca34c718174d1013cefa271";
		public const string INVALID_MAXIMUM_ACTIVATION_INTERVAL = "fb6f6c62d1da4d349b1c5d810ec689e3";
        //New return codes for Set Gateway Process Configuration
        public const string UNSUPPORTED_PROCESS_CONFIGURATION_ID = "76642276d59243419b4eaa843b9debf8";
        public const string INVALID_RUN_TIME_WINDOW = "ee6f32b2961c4e83b499811f97ca541e";

		//Return codes for On Demand Billing
		public const string INVALID_BILLING_DATA_TIERS_NODE_MISSING = "8624fe19725c4916a1d26042265c614e";
		public const string INVALID_BILLING_DATA_TIERS_INDEX_NODE_MISSING = "e10a95b305c64ef09fded72718fdc835";
		public const string INVALID_BILLING_DATA_TIER_COUNT = "f5fe6dfc89584e1a892c80fcbc91ca29";
		public const string INVALID_BILLING_DATA_TIER = "4061f4a6585045c299cae297fc077924";
		public const string DUPLICATE_BILLING_DATA_TIERS = "b026fb9f8fec428bb51943c4d502f54b";
        public const string READ_BILLING_DATA_ON_DEMAND_IN_PROGRESS = "7DBADD43795C442b80A9095D8559DB02";
        public const string READ_DATA_ON_DEMAND_IN_PROGRESS = "e80eae4348594cc88a4dc865e8e3582b";

		//Return codes for Set Device Auto Discovery Configuration command
		public const string INVALID_AUTO_DISCOVERY_CONFIGURATION_NODE_MISSING = "2366bee08ea14be7af36beed6e457b76";
		public const string INVALID_AUTO_DISCOVERY_CONFIGURATION_MBUS_NODE_MISSING = "972cfd797ee945c285ff91ed71ed3dd9";
		public const string INVALID_AUTO_DISCOVERY_CONFIGURATION_MBUS_STATUS_TYPE_ID_NODE_MISSING = "888fb3b68ba34e3685aaab440641af96";
		public const string INVALID_AUTO_DISCOVERY_CONFIGURATION_MBUS_STATUS_TYPE_ID = "ffae8ec356364b6ba168e3ff51ea099a";
        public const string INVALID_AUTO_DISCOVERY_CONFIGURATION_COMMAND = "b43e40be29d04bf2b2f9badb72ff29e0";
        public const string INVALID_CHILD_DEVICE_NODE_MISSING = "944eecd651164148ab22bfbac98d6cfe";
        public const string INVALID_DEVICE_TYPE = "d4ad7b8128e3413eb8f954ebbac7bc1b";

		//Return codes for Set Device Utility Information command
		public const string INVALID_UTILITY_INFORMATION_NODE_MISSING = "97eb13deab9246ad99723a24f00b64ae";
		public const string INVALID_UTILITY_INFORMATION_COMMAND = "6eff73ed29df43fe9c9d167b37c0a9b2";
		public const string INVALID_UTILITY_INFORMATION_OWNER_NAME = "2aba9732888a45dc87676abe33b6d42b";
		public const string INVALID_UTILITY_INFORMATION_OWNER_NAME_LENGTH = "f73b444bad3b4b5ea644ac0d4e297e6a";
		public const string INVALID_UTILITY_INFORMATION_UTILITY_DIVISION = "9d8978e82271492fb8dafbe30c50b886";
		public const string INVALID_UTILITY_INFORMATION_UTILITY_DIVISION_LENGTH = "2412224f153249119539a03c1768bdad";
		public const string INVALID_UTILITY_INFORMATION_MISCELLANEOUS_IDENTIFICATION = "d823154b8253404f9801e9ae376b143e";
		public const string INVALID_UTILITY_INFORMATION_MISCELLANEOUS_IDENTIFICATION_LENGTH = "bea9d6b8336b4d6fbeff2578d50208b1";
        public const string INVALID_UTILITY_INFORMATION_POINT_IDENTIFIER = "2d73e774ff9d4ecb94fa1ba3f9be55cd";
        public const string INVALID_UTILITY_INFORMATION_POINT_IDENTIFIER_LENGTH = "ce634b33af5d4b13aef3fb62fe172b81";

		//Return codes for Set Device Time Zone Configuration command
		public const string INVALID_TIME_ZONE_CONFIGURATION_NODE_MISSING = "c358c9f59bba4f9d98b0257b7cbd23ae";
		public const string INVALID_TIME_ZONE_CONFIGURATION_COMMAND = "ea998d46946041769f3e9cc5836a3415";
		public const string INVALID_DAYLIGHT_SAVING_TIME = "1fe0525ab5dd4547a0f7a81da40cd91f";
		public const string INVALID_DAYLIGHT_SAVING_TIME_END_TIME_NODE_MISSING = "2fb6de2bddac4140b540151c9eab31a7";
		public const string INVALID_DAYLIGHT_SAVING_TIME_START_TIME_NODE_MISSING = "a4c8691cd4bc45f4ad94b72d5e19693d";
		public const string INVALID_DAYLIGHT_SAVING_TIME_START_TIME = "656756cb79f544c095b32eeff8447fc5";
		public const string INVALID_DAYLIGHT_SAVING_TIME_END_TIME = "df2e09280db34c5bb5d17c87b4b1e4c5";
		public const string INVALID_DAYLIGHT_SAVING_TIME_ADJUSTMENT_OFFSET = "338a51c58897466fa110cb3b30a91869";
		public const string INVALID_DAYLIGHT_SAVING_TIME_TIME_ZONE_OFFSET = "a04cefeb1ea243aa9314fe435759c937";

		//Return codes for Connect and Disconnect Load Commands
		public const string UNSUPPORTED_DISCONNECT_PRIORITY_LEVEL_NODE = "ba01640ab2d64e5090599c1ff6fb77ca";
		public const string INVALID_DISCONNECT_PRIORITY_LEVEL = "9541f37a46bb4a04831b5c098cb78e4f";
		public const string UNSUPPORTED_FORCE_CONNECT_NODE = "03e71d2550c144228766d5cb89b55fbb";
		public const string INVALID_FORCE_CONNECT = "cad56b73253f4af98867e7d4a40c3800";
        public const string INVALID_READ_LOAD_STATUS = "158419f1806a4a0eb18ca2fa121c97c8";
        public const string DISCONNECT_DEVICE_LOAD_COMMAND_TASK_LIMIT_EXCEEDED = "d8feb86261644b03b1811d69781ed829";
        public const string CONNECT_DEVICE_LOAD_COMMAND_TASK_LIMIT_EXCEEDED = "dfaf938193654472a9499e27b69333f0";

		//Return codes for Read and Set Device Disconnect Configuration Commands
		public const string INVALID_DISCONNECT_CONFIGURATION_NODE_MISSING = "bf5598c633ff4aeeb88d16cde9216b8d";
		public const string UNSUPPORTED_MANUAL_DISCONNECT_STATUS_TYPE_ID_NODE = "257cf11ef10c499dacceaf0c91da3748";
		public const string INVALID_MANUAL_DISCONNECT_STATUS_TYPE_ID = "595d349b74c8405a8acb589c011fc494";
		public const string UNSUPPORTED_REMOTE_RECONNECT_STATUS_TYPE_ID_NODE = "a3bab2976dfb488bbde519080509ce9e";
		public const string INVALID_REMOTE_RECONNECT_STATUS_TYPE_ID = "06e9219d25fa493ca3a64c33ff8b6583";
		public const string INVALID_LED_CONFIGURATION = "1dc479ce443f46ca9c158a6a15b6fd38";
		public const string INVALID_LED_STATUS_TYPE_ID = "c4d5da0e9dce49ae8d3d80b2dd82c745";
		public const string UNSUPPORTED_LED_CONFIGURATION_SWITCH_STATUS_NODE = "4ea1d28bf8734e11956317c06ae5eea3";
		public const string INVALID_LED_CONFIGURATION_SWITCH_STATUS_MISSING = "cb38f12db2654aa9ad7d4dcc1363884c";
        public const string INVALID_SWITCH_STATUS_DISCONNECTED_LED_BEHAVIOR_TYPE_ID_NODE_MISSING = "9f3a99a1446844f5965c1b77767b1397";
        public const string INVALID_SWITCH_STATUS_DISCONNECTED_LED_BEHAVIOR_TYPE_ID = "78191be80f6c458ea3a11b7b786101a7";
        public const string INVALID_SWITCH_STATUS_CONNECTED_LED_BEHAVIOR_TYPE_ID_NODE_MISSING = "52de69d4ed114deca5f2784c78c4e072";
        public const string INVALID_SWITCH_STATUS_CONNECTED_LED_BEHAVIOR_TYPE_ID = "5dba356cbe494594862c9a89870654e3";
        public const string INVALID_SWITCH_STATUS_LOCKED_DISCONNECTED_LED_BEHAVIOR_TYPE_ID_NODE_MISSING = "e63202b9f6cf4584a5215fba4a3766ae";
        public const string INVALID_SWITCH_STATUS_LOCKED_DISCONNECTED_LED_BEHAVIOR_TYPE_ID = "b29fafd51c9c4c12959148b0132d96b8";
		public const string INVALID_DISCONNECT_CONFIGURATION_COMMAND = "368d6c53dbc34830bd69e5d9c55a5bfc";
		public const string UNSUPPORTED_AUTO_RECONNECT_STATUS_NODE = "7ff457ea6ac54397a0d8d65cc772bd22";
		public const string INVALID_AUTO_RECONNECT_STATUS = "a7a731b80bee4fa489ff2d5af5ef5c24";
		public const string UNSUPPORTED_RECONNECT_TIME_THRESHOLD_NODE = "9cf4aaa045c64f64b8fe9b03bb98ca93";
		public const string INVALID_RECONNECT_TIME_THRESHOLD = "801ff2976eb04a47bb313ebc0961c887";
		public const string UNSUPPORTED_PUSH_BUTTON_CLOSE_BEFORE_RECONNECT_TIME_THRESHOLD_STATUS = "cebf9ddc0daa4364a5e6fe7be39ed27f";
		public const string INVALID_PUSH_BUTTON_CLOSE_BEFORE_RECONNECT_TIME_THRESHOLD_STATUS = "4a9d404a49f0433bb60c46948d3f4565";

        public const string UNSUPPORTED_FIRMWARE_IMAGE = "7581cac8f4e34c49860ff9683b1a9fb8";

		//Return codes for Write Device Data Command
		public const string INVALID_RAW_DATA_NODE_MISSING = "80d0d021734b4375be61a78b35f28c7b";
		public const string INVALID_RAW_DATA_NUMBER_OF_BYTES = "1a9aa646b64b46bead5977b372902d3f";
		public const string INVALID_RAW_DATA_NOT_HEXADECIMAL = "0688b1c77ee94fca91a9f2f2ffcccfe9";
		public const string CONFLICTING_TIMEOUT_INTERVAL = "8c0d9c3428d647e59d4076970e23cd6e";

        //Return codes for Set Stop Mode Configuration
        public const string INVALID_STOP_MODE_CONFIGURATION_NODE_MISSING = "c677384f0b134eafb42dfa0ebb13f7c5";
        public const string INVALID_NUMBER_OF_DAYS_NODE_MISSING = "f7d6d604f3e44aec8c4cc574dae2ad89";
        public const string INVALID_NUMBER_OF_DAYS = "f56ba06fd78e4d918d1de25283ccf88b";

        // Return Codes for Power Quality Configuration
        public const string INVALID_POWER_QUALITY_CONFIGURATION_NODE_MISSING = "55e0345187d84bfb95afd228c5011fa1";
        public const string INVALID_OUTAGE_DURATION_THRESHOLD = "70276b6f9ea64163b6e07e6296c5b55b";
        public const string INVALID_SAG_SURGE_DURATION_THRESHOLD = "9a43efa3244c4d9ca7a49097be83f7dd";
        public const string INVALID_SET_POWER_QUALITY_CONFIGURATION_COMMAND = "13a0ead04383416190951be4f5302bb4";
        public const string INVALID_POWER_QUALITY_DURATION_THRESHOLDS_MISMATCH = "b8dfe090fb2e4c00836cc26678c998bd";
        public const string INVALID_SAG_VOLTAGE_PERCENT_THRESHOLD = "313ebf7762724f70930585f1018c3601";
        public const string INVALID_SURGE_VOLTAGE_PERCENT_THRESHOLD = "9e7a402be4b0499f93db14cead2920ec";
        public const string INVALID_OVER_CURRENT_PERCENT_THRESHOLD = "bd93ed2407204562875f961985234f29";
        public const string INVALID_MAX_CURRENT = "2b1fc9454a6d43dab19c9d51aebb1196";
        public const string INVALID_PHASE_LOSS_VOLTAGE_PERCENT_THRESHOLD = "64f18008c75a4b55a0e4f00dff044db7";
        public const string INVALID_POWER_QUALITY_CONFIGURATION_COMMAND = "48641c776f614537aa7f134fc50461e7";
        public const string INVALID_REVERSE_POWER_ALARM_CURRENT_THRESHOLD = "fe6aaf04cb414307862e462fad03b4e7";
        public const string INVALID_OVER_CURRENT_DURATION_THRESHOLD = "4a1a33f437d04583bb149fdc2bd8632e";
        public const string INVALID_TOTAL_HARMONIC_DISTORTION_CHILD_NODE_MISSING = "79df4f6b610d4d9daa79fc40d52a5359";
        public const string INVALID_TOTAL_HARMONIC_DISTORTION_CALCULATION_METHOD = "fc69fef1524546d2bac0310720d9f9d7";
        public const string INVALID_TOTAL_HARMONIC_DISTORTION_VOLTAGE_DURATION_THRESHOLD = "9e6a1db6a546459da77960cedd846d8b";
        public const string INVALID_TOTAL_HARMONIC_DISTORTION_VOLTAGE_PERCENT_THRESHOLD = "25c3c200f7f845d582ac8a7fe26a2e25";
        public const string INVALID_TOTAL_HARMONIC_DISTORTION_CURRENT_DURATION_THRESHOLD = "5fb42997f8bd4d2c8f278b8d21f7ebca";
        public const string INVALID_TOTAL_HARMONIC_DISTORTION_CURRENT_PERCENT_THRESHOLD = "29bd45a6c2814d6194fe02cc997248e2";
        public const string INVALID_TOTAL_HARMONIC_DISTORTION_APPARENT_POWER_DURATION_THRESHOLD = "faf42365b12e4dd59413e4f1fa2493e8";
        public const string INVALID_TOTAL_HARMONIC_DISTORTION_APPARENT_POWER_PERCENT_THRESHOLD = "8adc6c5f44e444c89e10385da8f9f7fc";
        //4.1 addition
        public const string INVALID_POWER_UP_HOLD_DURATION = "4370ff54fa7f4cd3a85334462f918903";

        // Return codes for Add Device One-time Read Request command
        public const string INVALID_ONE_TIME_READ_REQUEST_DATE_TIME_NODE_MISSING = "20bf779a024b48c8865e533dd7fe3216";
        public const string INVALID_ONE_TIME_READ_REQUEST_DATE_TIME = "d649c7c6648d4318819a35162d0a7824";

        // Return codes for Clear Device One-time Read Requests command
        public const string INVALID_ONE_TIME_READ_REQUEST_TYPE_ID_NODE_MISSING = "24997b0811174b97bc02085bd14c2bc2";
        public const string INVALID_ONE_TIME_READ_REQUEST_TYPE_ID = "e6f4a797e5e24510806bf9e219eb831c";

        //Return codes for Event Log Configuration
        public const string INVALID_EVENT_LOG_CONFIGURATION_NODE_MISSING = "1b223cfd9bea45b7a4096e6b53729726";
        public const string INVALID_EVENT_NODE_MISSING = "af7edb8b237b4df19e50fb857577cadd";
        public const string INVALID_EVENT_ID_NODE_MISSING = "b3b5755871cc4a329b84de7cc01b6d07";
        public const string INVALID_EVENT_ID = "0c79f637dfff44cf9248d8e0ae122052";
        public const string INVALID_EVENT_LOG_STATUS_NODE_MISSING = "81127c541f184bbaa48f69f6ebedd437";
        public const string INVALID_EVENT_LOG_STATUS = "dfc72cca2cd64d5289fbc59ff6885f16";
        public const string DUPLICATE_EVENT_ID = "b77e200c3a5146d6aefda5ed9a1d6576";
        public const string INVALID_TABLE_NODE_MISSING = "ef2caa7409ac4176a30a03689563d19c";
        public const string INVALID_TABLE_ID_NODE_MISSING = "ddf882882dca4bf1b23431598ad73042";
        public const string INVALID_TABLE_ID = "2ed400e09dde401eb412540e6f1e5e85";
        public const string INVALID_TABLE_LOG_STATUS_NODE_MISSING = "e6933a59acac401d93a756b5bce77f63";
        public const string DUPLICATE_TABLE_ID = "ab3a6e5138504a628a188e1b2f40b18c";
        public const string INVALID_TABLE_LOG_STATUS = "e7c0fc0b60314929822d6515b1020ebf";
        public const string INVALID_PROCEDURE_NODE_MISSING = "6449a27e40de40d1b9c9890cb0c1b7c7";
        public const string INVALID_PROCEDURE_ID_NODE_MISSING = "300c5d43bbf841f59d56c54e8be4d214";
        public const string INVALID_PROCEDURE_ID = "940df0e21f2a453eb1c550636e88c6b7";
        public const string INVALID_PROCEDURE_LOG_STATUS_NODE_MISSING = "4e06b69538a34a0bb4390cd8cafe9c5f";
        public const string INVALID_PROCEDURE_LOG_STATUS = "126d18bd72de4b7d841fb8f1f24b6414";
        public const string DUPLICATE_PROCEDURE_ID = "627efc87748e4eb580cb9754a5f54b56";
        public const string INVALID_EVENT_LOG_CONFIGURATION_COMMAND = "bd3269d84e434e759e0bcd94b388584c";

        //Return codes for Demand Configuration
        public const string SET_DEMAND_CONFIGURATION_IN_PROGRESS = "60F3C710793046ebB28DE3823ED6AD7E";
        public const string FEATURE_ACTIVATION_KEY_MISSING = "C9F9B61DBE184b59AC0C51448CA97814";
        public const string INVALID_DEMAND_CONFIGURATION_NODE_MISSING = "B4B2F89D091E4b40867A50400EEB002C";
        public const string INVALID_SOURCE_NODE_MISSING = "230DB38FACB84dddB77D3DAAB2C3CBD5";
        public const string INVALID_SOURCE_INDEX_NODE_MISSING = "DC8CEEFE0E8E49e596146403894C63AA";
        public const string DUPLICATE_SOURCE_INDEX = "B2E09FA1D0104afcA3BA0531F0320DBE";
        public const string INVALID_SOURCE_ID = "CF18BD64CC1E41efBBC8E9A3882548C0";
        public const string INVALID_COINCIDENT_SOURCE_NODE_MISSING = "93CB9F9CAB094016ADF7E4C81C43F9ED";
        public const string INVALID_COINCIDENT_SOURCE_INDEX_NODE_MISSING = "6A39DD58B6E546b1B7A61C14FA2FDA2A";
        public const string INVALID_COINCIDENT_SOURCE_INDEX = "22502D4B442F4cd4BC405475D91D7960";
        public const string DUPLICATE_COINCIDENT_SOURCE_INDEX = "53B1BA5E72994f89A2E30574AA970EE1";
        public const string INVALID_COINCIDENT_SOURCE_ID_NODE_MISSING = "94C21CE86AEA4d97B8E8E325EB11EC80";
        public const string INVALID_COINCIDENT_SOURCE_ID = "C0054304AEA145468A62931A35B43F2E";
        public const string INVALID_INTERVAL = "4C99EE7373DC4aecBEF9316D5FB9E8A4";
        public const string INVALID_SUBINTERVAL = "8F450276A56741bbBBA040ACEC8090CE";
        public const string INVALID_INTERVAL_SUBINTERVAL_COMBINATION = "65EDE4309D5240d49D9E24CE82693093";
        public const string INVALID_RESET_EXCLUSION = "EABB1D9F66C44486B5D2082FA44FC0A9";
        public const string INVALID_POWER_FAIL_EXCLUSION = "270E3B3877C24ad6901544B855066D74";
        public const string INVALID_PRESENT_CALCULATION_TYPE_ID = "2E2703C1905248178D486DA1E66A922D";
        public const string INVALID_RESET_TIME_FORMAT = "7F0E510EAB9A430aB8EDC7212C2ED428";
        public const string INVALID_RESET_TIME_HOUR = "CE26AC361CA8412c92C4A774D4B84225";
        public const string INVALID_RESET_TIME_MINUTE = "03A2A3BE61F6430a91DB3812981A2AF9";
        public const string INVALID_LCD_END_OF_INTERVAL_DURATION = "F1AA35B0BCCF44e990C0EC8909F6E546";
        public const string INVALID_SOURCE_INDEX = "21EB0DE9B53F4494A2A000FDDD81F6D7";
        public const string INVALID_SOURCE_ID_NODE_MISSING = "8D8684740BDC4266AE05C76046C814A7";
        public const string INVALID_COINCIDENT_SOURCES_NODE_MISSING = "FF42925E767C4474B79AE27552A9AC36";
        public const string INVALID_NUMBER_OF_COINCIDENT_SOURCES = "6215347B680B4541B687DB86DAB1777F";
        public const string INVALID_SOURCES_NODE_MISSING = "D62C557C0DAF489b94E50C7052848CB1";
        public const string INVALID_HISTORICAL_RESET_ENTRY_COUNT_NODE_MISSING = "5658A0924025429a82CC737D9CAE9EA2";
        public const string INVALID_HISTORICAL_RESET_ENTRY_COUNT = "ABB4B647B8874fc1BAD98CD98C05515F";
        public const string INVALID_CUMULATIVE_STATUS_TYPE_ID_NODE_MISSING = "63DF2F7B87A34247ADF72A859F9A5530";
        public const string INVALID_CUMULATIVE_STATUS_TYPE_ID = "185459C5F2F348a8B939DFFC677FDA61";
        public const string INVALID_CONTINUOUS_CUMULATIVE_STATUS_TYPE_ID_NODE_MISSING = "0233B45B33614743BE0300331754CA23";
        public const string INVALID_CONTINUOUS_CUMULATIVE_STATUS_TYPE_ID = "099F022436614b1683F70D5AA2816E2E";
        public const string INVALID_SUBINTERVAL_NODE_MISSING = "2B982B3F2BFC49518461C2BA62EE5745";
        public const string UNSUPPORTED_PERFORM_SELF_READ = "A1D27CF2B90E414cA1E972F796E1C918";
        public const string UNSUPPORTED_HISTORICAL_RESET_ENTRY_COUNT = "99804F7EEBF64ae989392D4B1E1B9CF3";
        public const string UNSUPPORTED_CUMULATIVE_STATUS_TYPE_ID = "3D8395CDBBB748228D9747A778F42F6A";
        public const string UNSUPPORTED_CONTINUOUS_CUMULATIVE_STATUS_TYPE_ID = "4AB12E6168CE41b59D85F98E19484DDE";
        public const string INVALID_SET_DEMAND_CONFIGURATION_COMMAND = "59B8697A297449c0BB1FDD77E88665EB";
        public const string INVALID_DEMAND_CONFIGURATION_CHILD_NODE_MISSING = "3DC86872DB3043cb822B8000B76F39DB";
        public const string INVALID_SOURCE_CHILD_NODE_MISSING = "90958534939F469396C846A8FB111462";
        public const string UNSUPPORTED_COINCIDENT_SOURCES = "71D10485E09B47b586F64F9E54B6B4BC";
        public const string INVALID_PERFORM_SELF_READ = "C095B2F0C3BC4c729AA062C52B1C0756";
        public const string UNSUPPORTED_DEVICE_DEMAND_CONFIGURED = "2269024E990E40289FB32E04F5E6E32D"; //Used during move meter validation

        //Return codes for Interface
        public const string INTERFACE_READ_STATUS_NOT_COMPLETE = "D79C92F737C5468dB2E9ECA0D766EF05";

        public const string INVALID_GATEWAY_NAME_MATCH_TYPE_ID = "b725ff910caf4fb292b9fbc1875eb7a8";
        public const string INVALID_LAST_SUCCESSFUL_COMMUNICATION_NODE_MISSING = "80086de981a34c8eb4c24a5da80210db";
        public const string INVALID_LAST_SUCCESSFUL_COMMUNICATION_REQUEST_TYPE_ID_NODE_MISSING = "7818464e427a44358e72ed6785aef967";
        public const string INVALID_LAST_SUCCESSFUL_COMMUNICATION_REQUEST_TYPE_ID = "e283944fcbeb4f799f0d214e2e2e69a8";
        public const string INVALID_LAST_SUCCESSFUL_COMMUNICATION_DATE_TIME_MISSING = "ca7eaedeba2544858d266f549d556d5e";
        public const string INVALID_LAST_SUCCESSFUL_COMMUNICATION_START_DATE_TIME = "49e3302417e647f49f69e9ce14183811";
        public const string INVALID_LAST_SUCCESSFUL_COMMUNICATION_END_DATE_TIME = "759a2ccd4a1f4bd29e0f1d073e4f4918";
        public const string INVALID_LAST_SUCCESSFUL_COMMUNICATION_DATE_TIME_RANGE = "1f24127bfe0a49e28f334edcc60b7e7d";
        public const string INVALID_GATEWAY_SERIAL_NUMBER_VALUE_NODE_MISSING = "413840a9bdcc45e39fcbd2c88f084c57";
        public const string INVALID_GATEWAY_SERIAL_NUMBER_MATCH_TYPE_ID = "5ab7fe6f7b104bca840b7dd6b8eec788";
        public const string INVALID_HIERARCHY_LEVEL_MEMBERS_LOGICAL_RESTRICTION_TYPE_ID = "2536a762a75e4a27987401692e27bf17";
        public const string INVALID_ATTRIBUTES_LOGICAL_RESTRICTION_TYPE_ID = "d48d5cd92a7843f49f8e769a8a08bb85";
        public const string INVALID_OPERATIONAL_PROFILE_NODE_MISSING = "3af11bffcac44837b7e7ebaec639124e";
        public const string INVALID_OPERATIONAL_PROFILE_ID_NODE_MISSING = "28c0de416789442089cb6ffba8c01424";
        public const string INVALID_OPERATIONAL_PROFILE_ID = "b09f1e19cb95437888cc99cf778d277c";
        public const string INVALID_ATM_CONFIGURATION_NODE_MISSING = "4bd6403d7fdb481985fbac4108272559";
        public const string INVALID_ATM_MODE_NODE_MISSING = "c0eabfbf5ad54afbaff69183b478bce6";
        public const string INVALID_ATM_MODE_TYPE_ID_NODE_MISSING = "a153b162e5de464791007620c5223ef9";
        public const string INVALID_ATM_MODE_TYPE_ID = "078d4ce30c9b491f8659232a72ec27fd";
        public const string INVALID_FIRMWARE_VERSION_NODE_MISSING = "a7e3eb3d13fe40448a6a4c3adf0e2243";
        public const string INVALID_SERVER_WAN_CONFIGURATION_NODE_MISSING = "04ca2b0227864776bfa298d5a6cfcd3e";
        public const string INVALID_SERVER_WAN_CONFIGURATION_ID = "dc2554d0f6d24395b796c2816fd22a50";
        public const string INVALID_DATASET_NEXT_LAST_SERVER_INITIATED_HIGH_PRIORITY_COMMUNICATION_DATE_TIME_NODE_MISSING = "ab15aa8783a849fc8b79bf550a0e4dea";
        public const string INVALID_DATASET_NEXT_LAST_SERVER_INITIATED_HIGH_PRIORITY_COMMUNICATION_DATE_TIME = "273c3018523d4bf4ad10776a27ca4f9d";
        public const string INVALID_DATASET_NEXT_LAST_SERVER_INITIATED_NORMAL_PRIORITY_COMMUNICATION_DATE_TIME_NODE_MISSING = "bb216b61ce3a4f9492e765e590cfc43f";
        public const string INVALID_DATASET_NEXT_LAST_SERVER_INITIATED_NORMAL_PRIORITY_COMMUNICATION_DATE_TIME = "bc20472cc68747f4900c67463d2efddc";
        public const string INVALID_DATASET_NEXT_LAST_GATEWAY_INITIATED_COMMUNICATION_DATE_TIME_NODE_MISSING = "bbf74eef79b741e097f10f77ab82de44";
        public const string INVALID_DATASET_NEXT_LAST_GATEWAY_INITIATED_COMMUNICATION_DATE_TIME = "ee016aa6cbc74474b6e13a6007d9b77b";
        public const string INVALID_FIRMWARE_VERSION_NAME_NODE_MISSING = "15efd1b48c5b4ed0b69fba5fd2167886";
        public const string INVALID_GATEWAY_SEARCH_CRITERIA = "0d4f519d82f24e928c71a6569bf7b90c";
        public const string INVALID_GATEWAY_NAME_VALUE_NODE_MISSING = "6430ed69e41840f7ba5fae6445e14e6c";
        public const string INVALID_SERVER_WAN_CONFIGURATION_ID_NODE_MISSING = "a9859a7e6bfc49d9a2308bed5016fe0d";

        public const string MULTIPLE_DEVICES_WITH_NAME = "3397468c556544faa180341420e33eb9";
        public const string INVALID_CONTINUOUS_DELTA_LOAD_PROFILE_COLLECTION_STATUS_NODE_MISSING = "beae690faaee4fe5871f0b552e0878e2";
        public const string INVALID_CONTINUOUS_DELTA_LOAD_PROFILE_COLLECTION_STATUS_TYPE_ID = "ee4a3720809d4b97b4304c46d0f0fc7a";
        public const string INVALID_CONTINUOUS_DELTA_LOAD_PROFILE_COLLECTION_STATUS_TYPE_ID_NODE_MISSING = "17e652e6ac404461ab9f22cdaa67f5c8";
        public const string INVALID_DATASET_NEXT_LAST_RECEIVED_RESULT_DATE_TIME = "531b9151bc194e4eb0b64767c769f1f4";
        public const string INVALID_DATASET_NEXT_LAST_RECEIVED_RESULT_RESULT_TYPE_ID = "81b887bbd9bf448e84e7ab75c22d1c75";
        public const string INVALID_DATASET_NEXT_LAST_RECEIVED_RESULT_RESULT_TYPE_ID_NODE_MISSING = "9d8533d1c7994f7291bb5c8801be8ebf";
        public const string INVALID_DEMAND_ACTIVATION_STATUS_NODE_MISSING = "e6ef1d44eec7431ab11fc2e2a80809b0";
        public const string INVALID_DEMAND_ACTIVATION_STATUS_VALUE = "4c130b9e489a44efad4d724977c84bbd";
        public const string INVALID_DEMAND_ACTIVATION_STATUS_VALUE_NODE_MISSING = "0e6a54e4a1b4448d8fea0f8960135d6a";
        public const string INVALID_DEMAND_CONFIGURATION_STATUS_NODE_MISSING = "7143203b5d084c628236b75884e9a118";
        public const string INVALID_DEMAND_CONFIGURATION_STATUS_VALUE = "a0653290ff9d4da9b5bd1674d6fe5279";
        public const string INVALID_DEMAND_CONFIGURATION_STATUS_VALUE_NODE_MISSING = "611bdb3c4a694b7395dfe0cfbcc29ce5";
        public const string INVALID_DEVICE_NAME_MATCH_TYPE_ID = "6cfb88f95b0b4bdfa2403537ed83dcbe";
        public const string INVALID_DEVICE_NAME_VALUE_NODE_MISSING = "0ea3e84613a7416fbd626f52854e753f";
        public const string INVALID_DEVICE_SERIAL_NUMBER_VALUE_NODE_MISSING = "e38b3a6931834a6fa8d140ca7a396b74";
        public const string INVALID_LAST_RECEIVED_RESULT_DATE_TIME_MISSING = "68f10ff1523a41ca9ad12fef14cd65e9";
        public const string INVALID_LAST_RECEIVED_RESULT_DATE_TIME_RANGE = "8fc7c7cd3877428b92fced9150dc9131";
        public const string INVALID_LAST_RECEIVED_RESULT_END_DATE_TIME = "1bb27f0ed7d0406ba81bdb6a183bc624";
        public const string INVALID_LAST_RECEIVED_RESULT_NODE_MISSING = "664dc6bb65034d94917c1134269b37a2";
        public const string INVALID_LAST_RECEIVED_RESULT_RESULT_TYPE_ID = "1df3b2b09cc24eba97b7e52dd019e4f7";
        public const string INVALID_LAST_RECEIVED_RESULT_RESULT_TYPE_ID_NODE_MISSING = "783c476fb448473a9373856892855e8e";
        public const string INVALID_LAST_RECEIVED_RESULT_START_DATE_TIME = "350ba4cf52e84c07a30e2e960b02552c";
        public const string UNSUPPORTED_LAST_RECEIVED_RESULT_RESULT_TYPE_ID = "c4ebac4aac84479189a0844a6efdb711";
        public const string INVALID_DEVICE_SERIAL_NUMBER_MATCH_TYPE_ID = "f4203f989dbf426ca3f982c5f3e3751a";
        public const string INVALID_TOTAL_SIGNAL_STRENGTH_VALUE_MISSING = "32ae88d440934762a9619c224976a67e";
        public const string INVALID_TOTAL_SIGNAL_STRENGTH_START_VALUE = "59803d26b4bc4f7cbe3ee4f7ad91679d";
        public const string INVALID_TOTAL_SIGNAL_STRENGTH_END_VALUE = "8dd8735c427e44a6a387959ae283ca11";
        public const string INVALID_TOTAL_SIGNAL_STRENGTH_VALUE_RANGE = "86469a2a985b42d9ae7badb3cb0c63f1";
        public const string INVALID_REPEATER_COUNT_VALUE_MISSING = "5d40f6701c924cd19e0c8e3f8f7cf289";
        public const string INVALID_REPEATER_COUNT_START_VALUE = "e5a5df38bf4742ee81676b8554befa6d";
        public const string INVALID_REPEATER_COUNT_END_VALUE = "5b60f04893244314bd104a6146d7a463";
        public const string INVALID_REPEATER_COUNT_VALUE_RANGE = "9922ada1a6a84372915d864ee55842c3";
        public const string INVALID_REPEATER_PATH_LOWEST_CARRIER_MARGIN_END_VALUE = "a5684f3aa42847f09b22f6361a9b68f6";
        public const string INVALID_REPEATER_PATH_LOWEST_CARRIER_MARGIN_START_VALUE = "f1d254fbd4e0454a9c2c5ce76a8919fd";
        public const string INVALID_REPEATER_PATH_LOWEST_CARRIER_MARGIN_VALUE_MISSING = "7a3894fb9059454588407bafbbc538c1";
        public const string INVALID_REPEATER_PATH_LOWEST_CARRIER_MARGIN_VALUE_RANGE = "12a51151597c41d3b1927fe6c6e220f2";
        public const string INVALID_DATASET_NEXT_LAST_RECEIVED_RESULT_NODE_MISSING = "9ee719dc8d8e41eba824952d8192b5f8";
        public const string INVALID_DATASET_NEXT_LAST_RECEIVED_RESULT_DATE_TIME_NODE_MISSING = "3f732861d9264b8c844e1aeea4571959";

        //Return codes for Assign Gateway Operational Profile command
        public const string UNSUPPORTED_OPERATIONAL_PROFILE_ID = "4042781519fa43419b7afdf811b86e18";
        public const string INVALID_PROCESS_CONFIGURATIONS_NODE_MISSING = "39722decd545421190093f0f4722d419";

        //Return codes for LSV and LS support in Disconnect Configuration feature
        public const string UNSUPPORTED_DISCONNECT_RECONNECT_WITH_LSV_STATUS_TYPE_ID_NODE = "62a011f0c0b1454ca4bf21e776af8e75";
        public const string INVALID_DISCONNECT_RECONNECT_WITH_LSV_STATUS_TYPE_ID = "4c5f4b7aedcf403caf348a577136ea34";
        public const string UNSUPPORTED_DISCONNECT_RECONNECT_WITH_LS_STATUS_TYPE_ID_NODE = "73c48cec05964c0a988cdc01ddb63863";
        public const string INVALID_DISCONNECT_RECONNECT_WITH_LS_STATUS_TYPE_ID = "3f365d50ad274677a13a10056f2c3f4c";
        public const string UNSUPPORTED_SEPARATE_DISCONNECT_AND_LSV_ICON_NODE = "80bfbc28462846c18e464806441bab36";
        public const string INVALID_SEPARATE_DISCONNECT_AND_LSV_ICON = "f7c46142503c46aea28820823bfd9b00";
        public const string UNSUPPORTED_USE_LSV_ALARM_NODE = "0caadf9ad8ad46ddb5e2ec92c41db710";
        public const string INVALID_USE_LSV_ALARM = "2514d54365404c40b5eac2eded233d3b";
        public const string INVALID_DISCONNECT_RECONNECT_WITH_LS_STATUS_TYPE_ID_NODE_MISSING = "9bab9f780125491da4c0d3d49dd23130";
        public const string INVALID_DISCONNECT_RECONNECT_WITH_LSV_STATUS_TYPE_ID_NODE_MISSING = "d96043b9af7b4571aaed29bd717d72b9";
        public const string INVALID_DISCONNECT_RECONNECT_WITH_LSV_AND_LS_STATUS_TYPE_ID_CONFIGURATION = "5bcaf3a590f04c90a993d95b10f3e011";


        //Return codes for MEP - Gateway Event Configuration feature
        public const string INVALID_MBUS_AND_MEP_ALARM_NODE_MISSING = "c9fa4abdf7e24cb8abe6201fdfbe3a06";
        public const string INVALID_MBUS_AND_MEP_ALARM_ID_NODE_MISSING = "1d3319a5deca41deac1d97fcb8f8019a";
        public const string INVALID_MBUS_AND_MEP_ALARM_ID = "844e6595717042ffa482a22d8d3accf2";
        public const string INVALID_MBUS_AND_MEP_ALARM_STATUS_TYPE_ID_NODE_MISSING = "88a92c620a5e42f182e17eed06af5d37";
        public const string INVALID_MBUS_AND_MEP_ALARM_STATUS_TYPE_ID = "a790054b85644ac2a1256bbc95ba3df2";
        public const string DUPLICATE_MBUS_AND_MEP_ALARM_ID = "d75e6dc395534eadaf6aa1581899e5d0";

        //Return codes for Tier Control Configuration
        public const string INVALID_TIER_CONTROL_CONFIGURATION_NODE_MISSING = "7dd2c5c219a34c41a13220e69389f7a1";
        public const string INVALID_CONSUMPTION_BASED_CONTROL_NODE_MISSING = "42541b55782144cdb8af6c68aa65f0c4";
        public const string INVALID_CONSUMPTION_BASED_CONTROL_STATUS = "e5770d79a2114cdfbe9f90e085a66e33";
        public const string INVALID_CONSUMPTION_BASED_CONTROL_POWER_THRESHOLD = "493413edebc94451a31c224f20359de1";
        public const string INVALID_CONSUMPTION_BASED_CONTROL_DURATION_THRESHOLD = "de1c0af7de4446d58ccf084833d16a8f";
        public const string INVALID_CONSUMPTION_BASED_CONTROL_EFFECTIVE_TIER = "39b9b0a1bcf94899b7f20b208ded5c6d";
        public const string INVALID_CONSUMPTION_BASED_CONTROL_POWER_THRESHOLD_SOURCE = "f3438a1b44ab4060a896984a6c825d8d";
        public const string INVALID_CONSUMPTION_BASED_CONTROL_CHILD_NODE_MISSING = "016969df928249189b28762c3110f263";

        //Return codes for Gateway Debug Logs
        public const string INVALID_MESSAGE_LOG_NODE_MISSING = "d0b8068f5b8f467d9b5dc6fe96cc992b";
        public const string INVALID_MESSAGE_LOG_TYPE_ID_NODE_MISSING = "c6b8caf7e3fe44f98ae6391990c1657a";
        public const string INVALID_MESSAGE_LOG_TYPE_ID = "e4fb4bf82acb4ad1b18fa7eee8fe3f8f";
        public const string UNSUPPORTED_MESSAGE_LOG_TYPE_ID = "88b589610b5b433bb6afad677769a5c1";
        public const string INVALID_MESSAGE_LOG_STATUS_TYPE_ID_NODE_MISSING = "e04ee0fe53f64fcc960aa6b92a8a4417";
        public const string INVALID_MESSAGE_LOG_STATUS_TYPE_ID = "bbb03ee8086642778426ae3ee4cbc458";
        public const string INVALID_MESSAGE_LOGS_END_DATE_TIME_NODE_MISSING = "699c0b382ff44fc584cb6bfcfdbc40d6";
        public const string INVALID_MESSAGE_LOGS_END_DATE_TIME = "aba162dd8a824687a509aa3b437b1021";

        //Return codes for Data Record Header Configuration
        public const string INVALID_DATA_RECORD_HEADER_CONFIGURATIONS_NODE_MISSING = "2c747c5379d44184a008080831027dca";
        public const string INVALID_DATA_RECORD_HEADER_CONFIGURATION_NODE_MISSING = "43d7d240e2024345a36a3bdee6d1f654";
        public const string INVALID_INDEX_NODE_MISSING = "e81f286bf92847a5b091f40d58fea5bd";
        public const string INVALID_INDEX = "7885a36bb9be4b18ad417921ecd2e641";
        public const string INVALID_ENTRY_TYPE_NODE_MISSING = "b114aa76d9394471b7f8dc0972e00564";
        public const string INVALID_ENTRY_TYPE = "c3f348947c394185a6605ebf217e2b1c";
        public const string INVALID_MAPPING_DATA_TYPE_NODE_MISSING = "1eea02038b444a469fc3f09ebb06ca17";
        public const string INVALID_MAPPING_DATA_TYPE = "707b823092294298b89a8e8b93c12945";
        public const string INVALID_DATA_RECORD_HEADER_NODE_MISSING = "825e2daf67ef4065873ba65f0ed82f37";
        public const string INVALID_DATA_RECORD_HEADER = "91287f775950468687438ce65337be05";
        public const string INVALID_FLAGGED_NODE_MISSING = "26276aae3a004783b31c386442fcaf53";
        public const string INVALID_FLAGGED_VALUE = "223ed89dee4a4612a9966d1fb6fc3cc3";

        //Return codes for Data Record Header Mapping
        public const string INVALID_DATA_RECORD_HEADER_MAPPING_NODE_MISSING = "83fffeb8164045f281ad50096bae8531";
        public const string INVALID_CONTROL_MAPPING_DATA_TYPE = "8362bdae030e412e802cbf5ed1e8720f";
        public const string INVALID_COMMAND_MONITOR_MAPPING_DATA_TYPE = "1ebd959701b543219b886b51ef49f9dc";
        public const string INVALID_TIME_STAMP_MAPPING_DATA_TYPE = "783cbe94231d4ff094906eb33ed4c67f";
        public const string INVALID_HOUR_LOCATOR_STARTING_BIT_NODE_MISSING = "2750ff1446aa475aa702b786747760c4";
        public const string INVALID_HOUR_LOCATOR_STARTING_BIT = "e89005d0a0e04233974050442df14dc2";
        public const string INVALID_HOUR_LOCATOR_BIT_COUNT_NODE_MISSING = "d9c30eeba83845998648ea5bfc49040c";
        public const string INVALID_HOUR_LOCATOR_BIT_COUNT = "54584082be8b4861aac979db099fc4e6";
        public const string NON_ZERO_HOUR_LOCATOR_BIT_COUNT = "b681bc6a66a24045a23f6aadd8d292c6";
        public const string INVALID_MINUTE_LOCATOR_STARTING_BIT_NODE_MISSING = "0356d86f6d22470f9d99d0978fee227c";
        public const string INVALID_MINUTE_LOCATOR_STARTING_BIT = "989c56f79c6c4250bd88b6e5dad601ad";
        public const string INVALID_MINUTE_LOCATOR_BIT_COUNT_NODE_MISSING = "30852403131942adbec40936867dbf1d";
        public const string INVALID_MINUTE_LOCATOR_BIT_COUNT = "aebe2ca0b7d84af3b33eb6014ac844e0";
        public const string NON_ZERO_MINUTE_LOCATOR_BIT_COUNT = "f9f5abdf023b439f8a2b7b2ecde03ea7";
        public const string INVALID_DATA_RECORD_HEADER_MAPPING_COMMAND = "b4cfd844eb4a41ef924535c297ba359f";

        //Set LP Config - new codes added for v5.0
        public const string UNSUPPORTED_CONFIGURATION_TYPE_ID = "bfe42bf9e5394349850adc1c75a44cc9";
        public const string UNSUPPORTED_DISPLAY_FORMAT_NODE = "6e1c6e4956fa4962a8db0808dadb50fa";
        public const string INVALID_LOAD_PROFILE_CONFIGURATION_ID = "85026283815e41498b8b0ba67373b68c";
        public const string UNSUPPORTED_CONFIGURATION_TYPE_NODE = "39bd89c9582b46e9a851a8aed2e78b10";
        public const string INVALID_CONFIGURATION_TYPE_ID = "202b8bc4f1364f9c87aa9277e40fb9b1";
        public const string INVALID_CHANNEL_SOURCE_NOT_CONFIGURED = "41c8520e094a47b4a7e5d723db4dfaaf";
        public const string INVALID_DISPLAY_FORMAT_ID_NODE_MISSING = "42ab743eea01463594583f63fbecf5ef";
        public const string INVALID_DISPLAY_FORMAT_ID_TEXT_NODE_MISSING = "a0dc6ccd3a13444498852599a95d06b0";
        public const string INVALID_DISPLAY_FORMAT_ID_TEXT = "ea055dbe6ab04159b83587838467631e";
        public const string INVALID_DISPLAY_FORMAT_VALUE_NODE_MISSING = "42ab743eea01463594583f63fbecf5ef";
        public const string INVALID_LOAD_PROFILE_CONFIGURATION_ID_NODE_INCLUDED = "08c5e24e90a04193953c97196db40155";
        public const string INVALID_DISPLAY_LIST_OPTIONS_LIST_STATUS = "3bdb11f845e14135a05b520566fff1ed";
        public const string INVALID_DISPLAY_LIST_OPTIONS_TIME_FORMAT = "c3ffe025d1694e089b8ad71eb96dcde9";
        public const string INVALID_DISPLAY_LIST_OPTIONS_LIST_NAME_FORMAT_TEXT_NODE_MISSING = "13075a316cf14bffaa379cff20f63828";
        public const string INVALID_DISPLAY_LIST_OPTIONS_LIST_NAME_FORMAT_TEXT = "5c4670cd424e45e084db0ba500c18bc1";
        public const string INVALID_DISPLAY_LIST_OPTIONS_DATE_TIME_FORMAT_TEXT_NODE_MISSING = "735f604b9e0646e48df4da6c7be69b61";
        public const string INVALID_DISPLAY_LIST_OPTIONS_DATE_TIME_FORMAT_TEXT = "78946c6b6beb4553a9465111e0dd2d42";
        public const string INVALID_DISPLAY_LIST_OPTIONS_INTERVAL_TIME_FORMAT_TEXT_NODE_MISSING = "715563695fbe4807be50298663d4d016";
        public const string INVALID_DISPLAY_LIST_OPTIONS_INTERVAL_TIME_FORMAT_TEXT = "9c0ef61f08f045bca133132b082a5470";
        public const string INVALID_EXTENDED_STATUS_FORMAT_DISPLAY_CHANNEL_NUMBER_NODE_MISSING = "c9fa541ac1af4913850daaaad4e2567f";
        public const string INVALID_EXTENDED_STATUS_FORMAT_DISPLAY_CHANNEL_NUMBER = "d688855186734252b80649ef4bb94542";
        public const string INVALID_EXTENDED_STATUS_FORMAT_TEXT_NODE_MISSING = "509708720fb94785809ae4fb7210b321";
        public const string INVALID_EXTENDED_STATUS_FORMAT_TEXT = "8a2fa13315b14e369154161a1be3ecfb";
        public const string INVALID_DISPLAY_LIST_OPTIONS_CHILD_NODES_MISSING = "b07d93f6207b459e9891fe8e95bff171";
        public const string DUPLICATE_CHANNEL_SOURCE_ID = "456beba8fda04f42bf0b6981a8db1647";

        //Read LP Config - new codes added for v5.0
        public const string INVALID_LOAD_PROFILE_CONFIGURATION_READ_TYPE = "b0359208c4bf4da3beee1f2e08eaea5b";

        public const string INVALID_BROADCAST_WRITE_DEVICE_DATA_COMMAND = "be92d322d6f44809a46da6ba56bcee06";
        public const string INVALID_GROUP_ID = "d3fc45890f254ef5afeffc10f8bcab2f";
        public const string INVALID_SUBGROUP_ID = "9309981084904570925ea6b39a110525";
        public const string INVALID_GROUP_ID_SUBGROUP_ID = "7bda2f95dba34886b1efb7f424a29753";
        public const string INVALID_NUMBER_OF_GROUPS = "294a8ceebbd6405e947972892e73bef1";
        public const string DUPLICATE_GROUP_ID_SUBGROUP_ID = "e16cffd9bfc644498b8521abcc01b646";

        public const string INVALID_BROADCAST_GROUP_MEMBERSHIP_CONFIGURATION_NUMBER_OF_GROUPS = "06c6efc4e58e472c9c84f7237cef8409";
        public const string INVALID_BROADCAST_GROUP_MEMBERSHIP_CONFIGURATION_GROUP_ACTION = "67eac3d27aa14f49b34c2bf650de2140";
        public const string INVALID_BROADCAST_GROUP_MEMBERSHIP_CONFIGURATION_GROUP_ID = "88d62855e2f54d54943b3f22f38cd5a1";
        public const string INVALID_BROADCAST_GROUP_MEMBERSHIP_CONFIGURATION_SUBGROUP_ID = "8a13afda2b1c4cff8915706e9afff028";
        public const string INVALID_BROADCAST_GROUP_MEMBERSHIP_CONFIGURATION_GROUP_ID_SUBGROUP_ID = "4616daf62f574eb482dd7f276c52d49b";
        public const string DUPLICATE_BROADCAST_GROUP_MEMBERSHIP_CONFIGURATION_GROUP_ID_SUBGROUP_ID = "edccad56be594fb6b599f03083503624";

		///////////////////////////////////////////////////
		// Return codes for CONTROL RELAY CONFIGURATION
		///////////////////////////////////////////////////
        // GENERAL RETURN CODES
		public const string INVALID_CONTROL_RELAY_CONFIGURATION_NODE_MISSING = "e79c8a72b9d24ccc8dfca23068e2f92e";
		public const string INVALID_CONTROL_RELAY_CONFIGURATION_CHILD_NODE_MISSING = "92305249cd6e40a4afef6d71bfe4251f";

		// TIERCONFIGURATION
		public const string INVALID_TIER_CONFIGURATION_CHILD_NODE_MISSING = "c8e3fcaa88a241d6a986dd15e50141ff";
		public const string INVALID_TIER_CONFIGURATION_STATUS = "0c6458b6cdf94a2e8088fc95874cd2d7";
		public const string INVALID_TIERS_TIER_NODE_MISSING = "91bfb5b16c8d4dd1ab6b800014fa4851";
		public const string INVALID_TIER_RELAY_STATE_NODE_MISSING = "9a8bab51f4444e27b197a43042fae4de";
		public const string INVALID_TIER_RELAY_STATE = "01aa73c1af274cf385528522430da235";

		// RANDOMIZATIONCONFIGURATION
		public const string INVALID_RANDOMIZATION_CONFIGURATION_CHILD_NODE_MISSING = "2378216dd5404b36b2ba5d8a9f9d8110";
		public const string INVALID_RANDOMIZATION_CONFIGURATION_MAXIMUM_TIME = "8512347456fd4f2b88185866013a0cff";
		public const string INVALID_COMMAND_TYPES_COMMAND_TYPE_NODE_MISSING = "ff88423cf992406da9b66cd65f9baa05";
		public const string INVALID_COMMAND_TYPE_COMMAND_NODE_MISSING = "960b69e6547e4abaa0c0866bd0126d1c";
		public const string INVALID_COMMAND_TYPE_RANDOMIZATION_STATUS_NODE_MISSING = "e6eff665a5a44815b8c8fb5927b3ac0d";
		public const string INVALID_COMMAND_TYPE_COMMAND = "21668cc5b84147b5a3c40cd65a6d4248";
		public const string INVALID_COMMAND_TYPE_RANDOMIZATION_STATUS = "06efea893b3d4ccabd1c8b4a61764739";
		public const string DUPLICATE_COMMAND_TYPE_COMMAND = "79eb71c831ac4bdc9e26c8df08e6a99b";

		// TIMECONFIGURATION
		public const string INVALID_TIER_CONFIGURATION_STATUS_NODE_MISSING = "fcffb5e757cb49d6b40e520c352d0ded";
		public const string INVALID_TIME_CONFIGURATION_CHILD_NODE_MISSING = "a86ceb8703234c759d7875c739ccf65a";
		public const string INVALID_TIME_CONFIGURATION_MODE = "402e9175d20d46f5a0c9e168674ac3a5";
		public const string INVALID_TIME_CONFIGURATION_MODE_NODE_MISSING = "4e90e3f553564673924f77189f962faa";
		public const string DUPLICATE_SCHEDULED_OCCURRENCE_INDEX = "264cf2d8ebf34e379773d14d93207593";
		public const string INVALID_SCHEDULED_OCCURRENCE_INDEX = "bd3f491d403e45d0814a1dabf60bfcd3";
		public const string INVALID_SCHEDULED_OCCURRENCE_INDEX_MISSING = "ef69068ca8e2422292be5e193cc3cd0c";
		public const string INVALID_SCHEDULED_OCCURRENCE_INDEX_NODE_MISSING = "b56c92f469e34fd0a2c660437dccd9a1";
		public const string INVALID_SCHEDULED_OCCURRENCE_SWITCHES_NODE_MISSING = "e4f4ed05df2f4620b396a8d97edaf55d";

		public const string INVALID_TIME_CONFIGURATION_SCHEDULED_OCCURRENCES_NODE_MISSING = "b1b3e3f66bd54464a61f2d300a6c5e9e";
		public const string INVALID_SWITCHES_SWITCH_NODE_MISSING = "d7e892c562a1427aacfa9b091db3e5af";
		public const string INVALID_SWITCHES_SWITCH_COUNT = "c69f2ba382c840099f67d542c6c13e0c";
		public const string INVALID_SWITCH_TIME_NODE_MISSING = "1b280657ce4a4918ac3beea9b7accd50";
		public const string INVALID_SWITCH_RELAY_STATE_NODE_MISSING = "618c56607b314d12ad608e2315d0e084";
		public const string INVALID_SWITCH_RELAY_STATE = "a26e7bed2f764926be4c2c9ec56a9eca";
		public const string INVALID_FIRST_SWITCH_TIME = "9ad430349191425b8c32b074c5a297a3";
		public const string INVALID_SWITCH_TIME = "c916b69d7a8a40d6bba11d991b0e2a56";
		public const string DUPLICATE_SWITCH_TIME = "a1ecc642cb6a4cf7a8b27ea838f57a92";
		public const string INVALID_SWITCH_TIME_ORDER = "09b9468b75844d0fa6d6efe414674742";
		public const string INVALID_TIME_CONFIGURATION_RELAY_CLOCK_ERROR_STATE = "5ea8788ca6ea465ab57bea06235c1bd1";

		// CONTROLRELAYID
		public const string INVALID_CONTROL_RELAY_ID = "cc86e478adf649638dcbd7dd3b105594";
		public const string INVALID_CONTROL_RELAY_ID_NODE_MISSING = "c2fd8967026d4a8ea310bd0ee5bb8576";
        ///////////////////////////////////////////////////
        // End of return codes for CONTROL RELAY CONFIGURATION
        ///////////////////////////////////////////////////

		//Return codes for MEMORY CONFIGURATION
		public const string INVALID_MEMORY_TYPE_ID = "b2ef05056de7492289fce18287f925ba";
		public const string DUPLICATE_MEMORY_TYPE_ID = "b10159da23bc4e4aa98d643e45d42f1a";
		public const string INVALID_MEMORY_TYPE_SIZE = "f2e140a8a2694002a55975969fb9c459";
		public const string INCOMPATIBLE_MEMORY_TYPE_SIZE = "c4ed1aa66e774bfdad4d0a9dfe78ca61";
		public const string UNABLE_TO_VALIDATE_MEMORY_TYPE_SIZE = "016d08e924d145719014ed73640c211a";


		public const string INVALID_FILE_ID_NODE_MISSING = "04da923abcb543b195ff1c9b2edc43fb";
		public const string INVALID_FILE_ID = "bf08895cc1724fe8bb39589cc7d51880";
		public const string INVALID_FILE_SIZE = "6b0e93217c0248de8afe49c5dde47a31";
		public const string INVALID_INTERPACKET_DELAY_NODE_MISSING = "619a6aeb2d0f4d6b937142e5af503af3";
		public const string INVALID_INTERPACKET_DELAY = "e116afdde7db4dbba21ce037070dcd1d";
		public const string INVALID_FILE_NODE_MISSING = "159c0f16e4e14eda8c0def8ff1169bf8";
		public const string INVALID_FILE_NAME_NODE_MISSING = "956b736620304b9c8416e8d3f7b106c9";
		public const string INVALID_FILE_NAME = "52589cac908f40248b8aa1dab52d6439";
		public const string DUPLICATE_FILE_NAME = "bf82b74ba10d4c42ba3020b1777dd431";
		public const string INVALID_FILE_TYPE_NODE_MISSING = "081ad8c7b3ad4158911bae5e0e594050";
		public const string INVALID_FILE_TYPE = "df5556fbd3b74dba96f63085590501fb";
		public const string INVALID_FILE_EMPTY = "54463c85c6954bfea181d505dd4debad";
		public const string INVALID_FILES_NODE_MISSING = "9c928821605d4f68ac9485fd044e8cb6";
		public const string INVALID_FILE_NAME_VALUE_NODE_MISSING = "99fbfbfad4824800b945fd51ed0e9295";
		public const string INVALID_FILE_NAME_MATCH_TYPE_ID = "23eeac689f4c473197b55b73d83050d8";
		public const string INVALID_DATASET_NEXT_FILE_NAME_NODE_MISSING = "e8ba054766444029a81af2f8113a8f3e";

		public const string INVALID_HARDWARE_CONFIGURATION_OPTIONS_LOGICAL_RESTRICTION_TYPE_ID = "2c5aae9f46434a6580051fd8d007df23";
		public const string INVALID_HARDWARE_CONFIGURATION_OPTION_BIT_OFFSET = "00ad025e95994b66a1a6c9c8f07654a7";
		public const string INVALID_HARDWARE_CONFIGURATION_OPTION_BIT_LENGTH = "aa86a31c7cbd47ffa5ec2275c6cb8367";
		public const string INVALID_HARDWARE_CONFIGURATION_OPTION_VALUE = "6a1a3d7486be43ea8bfd4516dde204dc";
		public const string INVALID_REPORTED_HARDWARE_VERSION_RANGE_START = "ee4c5acf2efc4b2d84dafeb1d33a1823";
		public const string INVALID_REPORTED_HARDWARE_VERSION_RANGE_END = "06838828e17e4c73a74fa859b546b724";
		public const string INVALID_REPORTED_HARDWARE_VERSION_RANGE = "0125ac78458241a3ae5b77cd67673a6a";


		// Batching return codes
		public const string INVALID_IDENTITY = "e5bd6b49f45b4e11a6d6b2134369b220";
		public const string INVALID_RELEASE_RESULT_CRITERIA = "d0d30c26253b4da694333c73538d34bc";
		public const string INVALID_RELEASE_EVENT_CRITERIA = "1fb2b5085d104db39b9247e70359abf9";

        public const string INVALID_START_RECORDED_FROM_GATEWAY_DATE_TIME = "b2f65cee1ad844018ec2ee1a48975ce3";
        public const string INVALID_END_RECORDED_FROM_GATEWAY_DATE_TIME = "dc1826cf39804c5a8717476e91222d22";
        public const string INVALID_RECORDED_FROM_GATEWAY_DATE_TIME_RANGE = "813b226a35ad4d1aacffc871c707d586";

		public const string INVALID_USE_ENCRYPTION = "fe1deab47be54739b1934e5ffa5ea0cc";

        //Import Key return codes
        public const string INVALID_IMPORT_DATA_TYPE_ID = "dc8a3ffda12b4f5fa415355e5dcf14af";
        public const string INVALID_IMPORT_DATA_TYPE_ID_NODE_MISSING = "29064a64924f4f7daff1a95a18406de0";
        public const string AUTO_APPLY_NOT_SUPPORTED = "23b62062ac2443728a477d9f8de106c4";
        public const string INVALID_AUTO_APPLY = "4b980b5ccfba488d996fc476e62b88f3";
        public const string INVALID_DATA_EMPTY = "279bb339e6884693becaaa0455e3d125";
        public const string INVALID_DATA_ENCRYPTION_FAILURE = "1fed7cbdf7964cdcb0dc409bc90337f9";
        public const string INVALID_FEATURE_NODE_MISSING = "952ee08bfec141ada45d33d870019dfb";
        public const string INVALID_FEATURE_ID_NODE_MISSING = "f291af754377487fb5cd93e071e92733";
        public const string INVALID_FEATURE_ID = "f989c972aafe4a92a0014a27655aec1c";
        public const string INVALID_FEATURE_ACTIVATED_NODE_MISSING = "bb29ad6122534265a0ce27ed3435f811";
        public const string INVALID_FEATURE_ACTIVATED = "c93ca19d7cb74c0589afdd0bd7693b7b";
        public const string INVALID_GATEWAY_FEATURE_ACTIVATION_COMMAND = "749ac40e6174431289c7e0c1fb753df3";
        public const string INVALID_GATEWAY_FEATURE_ACTIVATION_KEY_NOT_FOUND = "c66bd9831efe40d78b92311cbe06ba43";
        public const string INVALID_GATEWAY_FEATURE_ACTIVATION_KEY = "9ef0951925b34b71a3a068049147670b";
        public const string INVALID_FEATURE_ACTIVATION_FEATURE_CHILD_NODE_MISSING = "3c28ecf56aa54389bb339f5a48add027";
        public const string INVALID_FEATURE_ACTIVATION_NODE_MISSING = "1270e6f5a7ac46c19bff75daf6830c83";
        public const string INVALID_FEATURE_ACTIVATION_FEATURE_NODE_MISSING = "db2589b6881a4bf0873e08325166e27d";
        public const string INVALID_GATEWAY_FEATURE_ACTIVATION_ID = "15bc1ecc2c89488cb4582e6b7d280019";



		// Set Gateway FTP Configuration
		public const string INVALID_FTP_CONFIGURATION_NODE_MISSING = "5b00d2a955d34d469c646ae66228b2d7";
		public const string INVALID_PORT_NODE_MISSING = "9aedcd3365c74f5299acd2576c477b0b";

        //System Manager Key Management APIs
        public const string DUPLICATE_ASYMMETRIC_KEY_NAME = "b6bf87417d0a4505905e4e465bb5d4f4";
        public const string INVALID_ASYMMETRIC_KEY_NAME = "f101bbbafb7b4ceda54c8a6aa78af01e";
		public const string INVALID_ASYMMETRIC_KEY_TYPE_ID = "854a54007e774610a63ada6ec98a53f6";
        public const string INVALID_PUBLIC_KEY = "78233dcb4f9d4362b17786bbd99d6174";
        public const string INVALID_TRUSTED_KEY_VALUE = "f80e9e05e7f14e6bb7a7c189758ee357";
        public const string DUPLICATE_TRUSTED_KEY_VALUE = "c6dfb4c4899e4a8f93e6f4309b38a5fb";
        public const string INVALID_TRUSTED_KEY_SOURCE_TYPE_ID = "c8443f1d3ea94a01ad07a7f6f3e42b8b";
        public const string INVALID_TRUSTED_KEY_NAME = "eb9a97ad1ff54703b8e01fefdf8d92fd";
        public const string DUPLICATE_TRUSTED_KEY_NAME = "420d09bf1bb1445191564d2da2f996f3";

        //System Manager Export
        public const string INVALID_ORIGINATION_PUBLIC_KEY = "920c9905786c4d01abaf1d2390d5938a";
        public const string INVALID_DESTINATION_PUBLIC_KEY = "c5c5bcd7c88941bcafbbca663df58967";
        public const string INVALID_INCLUDE_SUMMARY_DETAIL = "cd0ab67f75fc47fb810e78ff61dcf49e";
        public const string INVALID_EXPORT_DATA_TYPE_ID = "04fb9825229b496794bae1119c15701f";
        public const string INVALID_GATEWAY_EXPORT_CRITERIA = "e3af508a210946f4a7142dc13b75c91a";
        public const string INVALID_DEVICE_EXPORT_CRITERIA = "99b28964bc9f4a7e805dd98683e89e89";

		//Import and System Manager Export
		public const string UNSUPPORTED_PLATFORM = "a3c8e80351db46cebbb7c5b9e51f3b1d";

        //System Manager Retrieve Result List
        public const string INVALID_DATASET_NEXT_RESULT_SAVED_IN_SYSTEM_DATE_TIME = "17503a72db8f47e5946f5471be5c276c";

		// General return codes added in V5.0
		public const string UNSUPPORTED_FILE_TYPE = "85ab2f1175ab48789f5542338bdb84a4";
		public const string INVALID_FILE_TYPE_ID = "b5886ba0add9464692ccabc8fe6cfa8f";

		//Gateway WAN Configuration
        public const string INVALID_GATEWAY_WAN_DATA_SIGNALS_STATUS = "0f74f4e7c5e449e4acdcabaae3ba1721";
        public const string INVALID_GATEWAY_WAN_INTERFACE_CLASS = "70c7bd0409454f95a9f65b44c3258d93";
        public const string INVALID_GATEWAY_WAN_SUBNET_PREFIX_LENGTH = "62250d0ce1774cc4b5438bf26c6120c4";
        public const string INVALID_GATEWAY_WAN_DEFAULT_GATEWAY_IPV4_ADDRESS = "bac1f35a53ee4757994ec93aa80c6c5e";
        public const string INVALID_GATEWAY_WAN_INTERFACE_NAME = "079e0d9c98b84391a59a72dc22d1dcca";
        public const string INVALID_GATEWAY_WAN_DEVICE_PATH = "48be7c947b274aeda449687e0b4ca5ac";
        public const string INVALID_GATEWAY_WAN_SUB_PRIORITY = "736a19578e634bba96d8a416090b7de0";
        public const string DUPLICATE_GATEWAY_WAN_CONFIGURATION_PRIORITY_SUB_PRIORITY = "cca3a3cb124a42049fc0154c557fc4f9";
        public const string INVALID_GATEWAY_WAN_SIM_METHOD = "481044a431254dbda1ff5c6c663296eb";
        public const string INVALID_GATEWAY_WAN_ANTENNA_METHOD = "245a54aadcc04d3aa34c4529c638e652";
        public const string INVALID_GATEWAY_WAN_RSSI_THRESHOLD = "5f28004436f048eebe093e73a71dad32";
        public const string INVALID_GATEWAY_WAN_OTA_UPDATE_METHOD = "620d725d6e51430ca93450dab20a6656";
        public const string INVALID_GATEWAY_WAN_UPDATE_FILENAME = "fbda3d1d6f924e929249c778d124d5ef";
        public const string INVALID_GATEWAY_WAN_UPDATE_ACTIVATION_PHONE_NUMBER = "fc4b74f1899340ce8250262ff71184ca";
        public const string INVALID_GATEWAY_WAN_UPDATE_PRL_PHONE_NUMBER = "345c104aa7154d9b8ff939295fef8070";
        public const string INVALID_GATEWAY_WAN_UPDATE_ACTION = "d325524b8a6345cd90a4d8f9775313f7";
        public const string INVALID_GATEWAY_WAN_UPDATE_PRL_INTERVAL = "184c3f0ccca346119444cee7fe60f69d";
        public const string INVALID_GATEWAY_WAN_RECONNECTION_INTERVAL = "05fa744cc10a4913a75f0edca3f80bce";
        public const string INVALID_GATEWAY_WAN_MODEM_POWER_UP_DELAY = "cf9bac28d726493c855d0b24cc3be5a7";
		public const string INVALID_DNS_CONFIGURATION_OPTION_INDEX = "19dcba12ec954f88a4799c6233b5e712";
		public const string DUPLICATE_DNS_CONFIGURATION_OPTION_INDEX = "78a13505317149c29ebe63c68ffaac0f";
		public const string INVALID_DNS_CONFIGURATION_OPTION_STATUS = "70ac39d7f1724e5b85a693e52eba72fa";
		public const string INVALID_DNS_CONFIGURATION_SUFFIX = "ff908221ed9d44f5ab2ed535c1f0d256";
		public const string INVALID_DNS_CONFIGURATION_TTL = "f27475ada2604c8297284d56268270a9";
		public const string INVALID_DNS_CONFIGURATION_PREFERRED_SERVER_IP_ADDRESS = "3b9aaa5d0c464851a07f4b1f19ec7d8a";
		public const string INVALID_DNS_CONFIGURATION_ALTERNATE_SERVER_IP_ADDRESS = "cfbf23cc79ef4e008310fbbf7cb95d7d";

        public const string BATCH_CREATED_UNABLE_TO_RETRIEVE_DETAILS = "6f7b002387534ff8b6763679e1b8c665";
		public const string INVALID_LOCAL_METER_OPTIONS = "c2dde4ee1a7d427c912cebf66eb6494a";
		public const string INVALID_DELETE_LOCAL_METER = "0427cb73938c4c318e258cfc3ed2074c";
		public const string INVALID_ADD_LOCAL_METER = "a0867733613144e08e42b3e8b0212eaf";
		public const string INVALID_METER_MANUFACTURING_INFORMATION_MISSING = "62e7bad7a69c4dfcb09db7d7046c0ae5";
		public const string INVALID_GATEWAY_MANUFACTURING_INFORMATION_MISSING = "a0cc2666272d4606a2c8b52635db74fd";

        public const string INVALID_OPTION = "c3a1239e83494a5aa57685989711c9c2";
        public const string INVALID_OPTIONS_LOGICAL_RESTRICTION_TYPE_ID = "0ec62274afeb40bbaf34a52a170c1141";
        public const string INVALID_RESULT_TYPE_OPTION = "11c7994a660549279d48ebb2f374969a";

		public const string INVALID_RESOURCE_FOR_FIRMWARE_VERSION = "33b1fccfd832402886f4f75a40703bea";
		public const string INVALID_KEY_DATA = "64cf67f46c3b48edb623344876c7a88b";
		public const string INVALID_RESOURCE_FOR_ENTITY_TYPE = "d0bfcc47e33b4c949410cecdee508767";
		public const string INVALID_PROPERTY_FOR_FIRMWARE_VERSION = "510483841de843eaaa8e2229071b3cb3";
		public const string INVALID_ACTION_FOR_PROPERTY = "2602ebb8189a4392a28b045cbf86e5e6";
		public const string INVALID_PROPERTY_VALUE = "8f4f1ca476c74f5995d75cbbc12e929c";
		public const string INVALID_MASK_VALUE = "3d89839b5fbf4598bad9f622e4b305cb";
		public const string INVALID_SYSSW_PRIVS = "4528ed9b8d894483aa6193e6221f0f5e";
		public const string INVALID_COS_APP_PRIVS = "ae31025d6a8841f1b0f891d4b791d169";
		public const string INVALID_BITMASK_NOT_PERMITTED = "508e51bc41df42f599fe67e2a1cb5148";
		public const string DUPLICATE_PROPERTY = "cf52b535b6134d73b7ad61456eed36e5";

        //Phase Rotation return codes
        public const string INVALID_READ_OPTIONS = "00b0b66ec6cb4264bca6d746876e0d29";

		public const string INVALID_INVALID_PLC_PASSWORD_THRESHOLD = "d1dd2b3c30994a9a8c92e71b9f74f24e";

		public const string INVALID_DISCONNECT_THRESHOLDS_CONFIGURATION = "581ab86932814f0baf4733ce5c34bf5c";
		public const string INVALID_ENABLE = "b1f3d20ee54d4901ae6f3720ee6558ff";
		public const string DISCONNECT_CONTROL_TYPE_NODE_NOT_SUPPORTED = "b17d2744ae404d9e8c907df48eb17291";
		public const string INVALID_DISCONNECT_CONTROL_TYPE = "6639f07391eb4807839dc150f0af44a0";
		public const string INVALID_DISCONNECT_TRIP_VALUE_SELECT = "e4ea5a475f1b4f27956b4f91f24283dc";
		public const string CURRENT_CONTROL_TYPE_NODE_NOT_SUPPORTED = "e25bb4c6ccc0458391bb613589ca5d1f";
		public const string INVALID_CURRENT_CONTROL_TYPE = "8a41347e628d4f939df735789d7f69f9";
		public const string INVALID_DISCONNECT_THRESHOLDS_CONFIGURATION_PRIMARY = "40d0f5d482a84f6895a6106b42d854d3";
		public const string INVALID_PRIMARY_POWER_THRESHOLD = "cd7463895e83430a9751f22fe84689f8";
		public const string PRIMARY_CURRENT_THRESHOLD_NODE_NOT_SUPPORTED = "efede987dd6943b1b5b6b5046e12fa4e";
		public const string INVALID_PRIMARY_CURRENT_THRESHOLD = "61041c08161b429dbcf1682eeae3d283";
		public const string INVALID_PRIMARY_TIME_THRESHOLD = "ad5646c6561747f7b5dfc71fbc7a204b";
		public const string INVALID_AVERAGE_POWER_CONFIGURATION_AVERAGE_POWER_STATUS = "a151ff541e3b4e4dbd9ab99cf96a8f8f";
		public const string INVALID_AVERAGE_POWER_CONFIGURATION_SUBINTERVAL = "5a21bbb537274d948e78ba1c59efa190";
		public const string INVALID_AVERAGE_POWER_CONFIGURATION_INTERVAL_MULTIPLIER = "f5798e51c7b24586806862fe14b3ca06";
		public const string INVALID_AVERAGE_POWER_CONFIGURATION_DISCONNECT_THRESHOLD = "7938a69370de451ebd48e3b0e2e958d2";
		public const string AVERAGE_POWER_CONFIGURATIONS_NOT_SUPPORTED = "d1bc7a413f2f4dc690d990bb18f7fcb2";
		public const string INVALID_AVERAGE_POWER_CONFIGURATION_TYPE_ID = "02dc1da2a9e84bb7b4811b948cf9b460";
		public const string UNSUPPORTED_AVERAGE_POWER_CONFIGURATION_TYPE_ID = "dfdda5e172e54b1e87d217dfdd705449";

        //------added in V5.3----------
        public const string UNSUPPORTED_GRACE_PERIOD_NODE = "5D2BB85285AD475DB5A5BE6D175CFF8B";
        public const string INVALID_TIME_AFTER = "0D4F3AEDF0BC4922A5EF4C84E335C77A";
        public const string INVALID_TIME_BEFORE = "f8037a898cb64d87937a444ede5c92b3";
        public const string INVALID_WEEKEND = "ADEE6DE843EE4F969578C180FEDBAE2F";
        public const string INVALID_SPECIAL_SCHEDULE_0 = "13FF27AA31634CFE84BED6BDD88CEF88";
        public const string INVALID_SPECIAL_SCHEDULE_1 = "7C4BF10AC74E4DA88B2DC865795A3A9F";
		public const string INVALID_TIME_BEFORE_AND_TIME_AFTER_COMBINATION = "378ecf20d2704fa087abf080e54c23e6";

		public const string INVALID_LIST_TYPE_ID = "0205071d81a54a34be7ac1ef93ceb5a8";
		public const string LIST_TYPE_ID_NOT_SUPPORTED = "4715a979748c45b088df3bcf9cd2ca33";
		public const string SECONDS_TO_DISPLAY_NODE_NOT_SUPPORTED = "1580dac5374b442b84210d18097103d5";
		public const string LIST_TIMEOUT_NODE_NOT_SUPPORTED = "fb181d9cf126455587694733390d32d0";
		public const string INVALID_LIST_TIMEOUT = "39fdc336d71942a5a2500db5b7e51638";
		public const string ENABLE_LIST_NODE_NOT_SUPPORTED = "d0aaeba919114e58bc33385b6d30f160";
		public const string INVALID_ENABLE_LIST = "b1fb3aa99d65466db2878da32ce6b7b1";
		public const string NO_DATA_TO_READ = "f18a2131271842108fd6e9cb52b7e35a";
		public const string DUPLICATE_LIST_TYPE_ID = "c4a058ac8d36488fab73eb4c21cbc095";

		public const string INVALID_TAMPER_CONFIGURATION_DURATION = "de784e79b8f44942973575fc36324bf6";
		public const string INVALID_TAMPER_CONFIGURATION_POWER_QUALITY_DURATION = "315acb3b8ff94888913a2542a5b2d597";
		public const string INVALID_POWER_QUALITY_QUALIFICATION_EVENT_ID = "6f607d75724a4727bed17e896bd2e086";
		public const string INVALID_POWER_QUALITY_QUALIFICATION_EVENT_STATUS = "ee6e1430ac9d45beb36116b56fba58e6";
		public const string DUPLICATE_POWER_QUALITY_QUALIFICATION_EVENT_ID = "de2e4ab15231466fbb605c674c9a72bd";

		public const string INVALID_SSH_FINGERPRINT = "d90c94b88bd14ce397802aa63f8acca8";
		public const string INVALID_KEY_TYPE = "4159addd72194d669d2c793bbc98e5e3";
		public const string INVALID_KEY_NEW_VALUE = "2bf138aa158e4083b61d7615e3a06c90";
        public const string INVALID_HAN_INTERFACE_STATUS = "086d43629ba842cd9f888b0a2555dac2";

        public const string INVALID_RMS_VOLTAGE_CONTINUOUS_CYCLES = "7434ae9a7547409ba40af69e412c3636";
		public const string INVALID_RMS_VOLTAGE_AVERAGING_INTERVAL = "3b2f87eecdb64d47a0d82b2895661082";

		public const string UNSUPPORTED_HARDWARE_CONFIGURATION = "fe9c3f94275949b7862749a26be19e23";

        ////////////////////////////////////////////////////////////////////////////////////////
		//The following are deprecated - the version in which they were deprecated and the    //
		//constant replacing them (if any) is listed to the right of the constant.            //
		////////////////////////////////////////////////////////////////////////////////////////

        [Obsolete("v1.1 - use DUPLICATE_DEVICE_NAME")]
        public const string DUPLICATE_DEVICE = "9bf9671d1e8440849a0fb2151ffa3087";                  //v1.1 - use DUPLICATE_DEVICE_NAME

        [Obsolete("v2.0 - use INVALID_DEVICE_NODE_MISSING")]
        public const string INVALID_DEVICE_MISSING = "7b9bc07aa29347a09e831f1f6185d1c7";			//v2.0 - use INVALID_DEVICE_NODE_MISSING

		[Obsolete("v2.1 - use INVALID_DEVICE_ID_NODE_MISSING")]
		public const string INVALID_DEVICE_ID_MISSING = "3829ead36e344e27b0f1bef2783a00d9";			//v2.1 - use INVALID_DEVICE_ID_NODE_MISSING

		[Obsolete("v2.1 - use INVALID_GATEWAY_NODE_MISSING")]
		public const string INVALID_GATEWAY_MISSING = "ef4d9f24e4e64a6eabe42b4377cf8318";			//v2.1 - use INVALID_GATEWAY_NODE_MISSING

		[Obsolete("v2.1 - use INVALID_GATEWAY_ID_NODE_MISSING")]
		public const string INVALID_GATEWAY_ID_MISSING = "825fdf4bb2ab424185ad274ee89e229b";		//v2.1 - use INVALID_GATEWAY_ID_NODE_MISSING

		[Obsolete("v2.1 - no replacement - not used")]
		public const string INVALID_RETURN_CODE = "af230356d61b4e2b950b440a8040de34";				//v2.1 - no replacement - not used

		[Obsolete("v2.0 - use INVALID_RECURRING_DATES_MONTH")]
		public const string INVALID_TOU_RECURRING_DATE_MONTH = "05C8BFB1D5F747f5882634487D4995F1";	//v2.0 - use INVALID_RECURRING_DATES_MONTH

		[Obsolete("v2.0 - use INVALID_RECURRING_DATES_OFFSET")]
		public const string INVALID_TOU_RECURRING_DATE_OFFSET = "13C87067CC61436c8037F121D908BFB1";	//v2.0 - use INVALID_RECURRING_DATES_OFFSET

		[Obsolete("v2.0 - use INVALID_RECURRING_DATES_DAY")]
		public const string INVALID_TOU_RECURRING_DATE_DAY = "AB84C78514AE40a4A7760050D48D7A37";	//v2.0 - use INVALID_RECURRING_DATES_DAY

		[Obsolete("v2.0 - use INVALID_RECURRING_DATES_WEEKDAY")]
		public const string INVALID_TOU_RECURRING_DATE_WEEKDAY = "42AE7A9EA0434e14922DFAA47F44FECF";//v2.0 - use INVALID_RECURRING_DATES_WEEKDAY

		[Obsolete("v2.0 - use INVALID_RECURRING_DATES_PERIOD")]
		public const string INVALID_TOU_RECURRING_DATE_PERIOD = "504FA84F478249e9AD716DF7330BB8C4";	//v2.0 - use INVALID_RECURRING_DATES_PERIOD

		[Obsolete("v2.0 - use INVALID_RECURRING_DATES_DELTA")]
		public const string INVALID_TOU_RECURRING_DATE_DELTA = "F74119C11A0649499F7FDD04D4FD5086";	//v2.0 - use INVALID_RECURRING_DATES_DELTA

		[Obsolete("v2.0 - use INVALID_RECURRING_DATES_ACTION")]
		public const string INVALID_TOU_ACTION = "CEA8B3233B14469090DD881D4842175C";				//v2.0 - use INVALID_RECURRING_DATES_ACTION

		[Obsolete("v2.0 - use INVALID_RECURRING_DATES_PERFORM_BILLING_READ")]
		public const string INVALID_TOU_PERFORM_BILLING_READ = "E3DC115A9A044224BE292C5D4E2EA27D";	//v2.0 - use INVALID_RECURRING_DATES_PERFORM_BILLING_READ

		[Obsolete("v2.0 - use INVALID_DAY_SCHEDULE_INDEX")]
		public const string INVALID_TOU_SCHEDULE_INDEX = "C930FE4908EF4d9590D01247FDCA16D1";		//v2.0 - use INVALID_DAY_SCHEDULE_INDEX

		[Obsolete("v2.0 - use INVALID_DAY_SCHEDULE_SWITCH_INDEX")]
		public const string INVALID_TOU_SWITCH_INDEX = "7C14A0BE198248e68FAF644068F27714";			//v2.0 - use INVALID_DAY_SCHEDULE_SWITCH_INDEX

		[Obsolete("v2.0 - use INVALID_DAY_SCHEDULE_TIER")]
		public const string INVALID_TOU_TIER = "DEAF2D4059CE445e94BD228478201DAE";					//v2.0 - use INVALID_DAY_SCHEDULE_TIER

		[Obsolete("v2.0 - use INVALID_DAY_SCHEDULE_START_HOUR")]
		public const string INVALID_TOU_START_HOUR = "C64BF0CBA1B84a389DA4562ED31539E4";			//v2.0 - use INVALID_DAY_SCHEDULE_START_HOUR

		[Obsolete("v2.0 - use INVALID_DAY_SCHEDULE_START_MINUTE")]
		public const string INVALID_TOU_START_MINUTE = "DC94811B66E84e07923CB7B2EF3F8CED";			//v2.0 - use INVALID_DAY_SCHEDULE_START_MINUTE

		[Obsolete("v2.0 - use INVALID_SEASON_SCHEDULE_SATURDAY")]
		public const string INVALID_TOU_SATURDAY = "8D05A3F0216744aa978A6DFE0875D559";				//v2.0 - use INVALID_SEASON_SCHEDULE_SATURDAY

		[Obsolete("v2.0 - use INVALID_SEASON_SCHEDULE_SUNDAY")]
		public const string INVALID_TOU_SUNDAY = "33A8DDD671154e0590BCFE9A86AA58A5";				//v2.0 - use INVALID_SEASON_SCHEDULE_SUNDAY

		[Obsolete("v2.0 - use INVALID_SEASON_SCHEDULE_SPECIAL_0")]
		public const string INVALID_TOU_SPECIAL0 = "CA6A5AF5C20C40d4B75986A785D14D53";				//v2.0 - use INVALID_SEASON_SCHEDULE_SPECIAL_0

		[Obsolete("v2.0 - use INVALID_SEASON_SCHEDULE_SPECIAL_1")]
		public const string INVALID_TOU_SPECIAL1 = "5D21A9FC959B4c63A8F33E73E699D012";				//v2.0 - use INVALID_SEASON_SCHEDULE_SPECIAL_1

		[Obsolete("v2.0 - use INVALID_SEASON_SCHEDULE_WEEKDAY")]
		public const string INVALID_TOU_WEEKDAY = "41eecd391c8e42b0af5f5d297ecd5b08";				//v2.0 - use INVALID_SEASON_SCHEDULE_WEEKDAY

		[Obsolete("v2.1 - use DUPLICATE_NEURON_ID")]
		public const string DUPLICATE_ILON100_NEURON_ID = "27d1f8d25d01441b876b2ddfe9142fcb";		//v2.1 - use DUPLICATE_NEURON_ID

		[Obsolete("v2.1 - use INVALID_NEURON_ID")]
		public const string INVALID_ILON100_NEURON_ID = "ae7663061d8d4607aa8e39d02ab04f65";			//v2.1 - use INVALID_NEURON_ID

		[Obsolete("v2.1 - use INVALID_PPP_USERNAME")]
		public const string INVALID_PPP_LOGIN = "2bd9054a5be8434785446ebbc6a10460";					//v2.1 - use INVALID_PPP_USERNAME

		[Obsolete("v2.1 - use INVALID_PPP_USERNAME_BLANK")]
		public const string INVALID_PPP_LOGIN_BLANK = "7659b5b48e0d41159321ba012f3e33f6";			//v2.1 - use INVALID_PPP_USERNAME_BLANK

		[Obsolete("v2.1 - use INVALID_PPP_USERNAME_DUPLICATE")]
		public const string INVALID_PPP_LOGIN_DUPLICATE = "ec945e116bb04a92a5435be263fac195";		//v2.1 - use INVALID_PPP_USERNAME_DUPLICATE

		[Obsolete("v2.1 - use INVALID_PPP_USERNAME_AND_PASSWORD_MISSING")]
		public const string INVALID_PPP_LOGIN_AND_PASSWORD_MISSING = "a00bf7180912475593f8dcb1fa78d1f6";	//v2.1 - use INVALID_PPP_USERNAME_AND_PASSWORD_MISSING

		[Obsolete("v2.1 - use INVALID_PPP_USERNAME_NODE_MISSING")]
		public const string INVALID_PPP_LOGIN_MISSING = "bd4e3987ef92475e84248d5d58b2adcb";	//v2.1 - use INVALID_PPP_USERNAME_NODE_MISSING

		[Obsolete("v2.1 - use INVALID_ID_TYPE")]
		public const string INVALID_RETRIEVE_BY_PARAMETER_ID_TYPE = "55da2d57a75f44f0a2e747fbea184d5a";	//v2.1 - use INVALID_ID_TYPE

        [Obsolete("v2.1 - no longer used")]
		public const string INVALID_SECONDARY_CONTROL_OUTPUT_TIER_VALUE = "2b1eb3f5528b4c24b073bda6c4ee57be";

		[Obsolete("v2.1 - use INVALID_ENABLE_CONTROL_RELAY_TIERS_VALUE")]
		public const string INVALID_ENABLE_SECONDARY_CONTROL_OUTPUT_TIERS_VALUE = "9fe600ec48e94bac9d29b0957155e9f9"; //v2.1 - use INVALID_ENABLE_CONTROL_RELAY_TIERS_VALUE

		[Obsolete("v2.1 - use INVALID_DATABASE_LOCATION_BLANK")]
		public const string INVALID_DATABASE_LOCATION_EMPTY = "70daf229c6ae42d5928d7c9d844a2c8b";	//v2.1 - use INVALID_DATABASE_LOCATION_BLANK

		[Obsolete("v2.1 - use INVALID_DATABASE_LOGIN_BLANK")]
		public const string INVALID_DATABASE_LOGIN_EMPTY = "5460ac0344f54dde8bc741abb95b6c98";		//v2.1 - use INVALID_DATABASE_LOGIN_BLANK

		[Obsolete("v2.1 - use INVALID_DATABASE_NAME_BLANK")]
		public const string INVALID_DATABASE_NAME_EMPTY = "5c0358efe5354b7cba6c42569d25ca26";		//v2.1 - use INVALID_DATABASE_NAME_BLANK

		[Obsolete("v2.0 - use INVALID_ATTRIBUTE_NODE_MISSING")]
		public const string INVALID_ATTRIBUTE_MISSING = "8fe91c67cc6e48dab1b46fa06e70cc01";			//v2.0 - use INVALID_ATTRIBUTE_NODE_MISSING

		[Obsolete("v2.0 - use INVALID_HIERARCHY_LEVEL_MEMBER_NODE_MISSING")]
		public const string INVALID_HIERARCHY_LEVEL_MEMBER_MISSING = "f9b4827652b346b9abe84d9e5f8d0518";	//v2.0 - use INVALID_HIERARCHY_LEVEL_MEMBER_NODE_MISSING

		[Obsolete("v2.1 - Use INVALID_CONTROL_RELAY_STATUS_TYPE_ID")]
		public const string INVALID_SECONDARY_CONTROL_OUTPUT_RELAY_STATUS_TYPE_ID = "37381b497105406993fdaa657393ac13"; //v2.1 - Use INVALID_CONTROL_RELAY_STATUS_TYPE_ID

		[Obsolete("v2.1 - use INVALID_LOCAL_TASK_MANAGER_ID_BLANK")]
		public const string INVALID_LOCAL_TASK_MANAGER_ID_MISSING = "6c8673d65a344b12ac0b8533486166c1";	//v2.1 - use INVALID_LOCAL_TASK_MANAGER_ID_BLANK

		[Obsolete("v2.1 - use INVALID_PPP_PASSWORD_NODE_MISSING")]
		public const string INVALID_PPP_PASSWORD_MISSING = "dbcae2ef87184e65b09cd31c9db098c0";		//v2.1 - use INVALID_PPP_PASSWORD_NODE_MISSING

		[Obsolete("v2.1 - use WAN_CONFIGURATION_ROUTE_NOT_AVAILABLE")]
		public const string COMMUNICATION_SETTING_ROUTE_NOT_AVAILABLE = "5c88c8dc907b4e33afa5bdbdb97b4775";	//v2.1 - use WAN_CONFIGURATION_ROUTE_NOT_AVAILABLE

		[Obsolete("v2.1 - use WAN_CONFIGURATION_IN_USE")]
		public const string COMMUNICATION_SETTING_IN_USE = "0795e486c48e4de58bf1f15cce6353b9";	//v2.1 - use WAN_CONFIGURATION_IN_USE

		[Obsolete("v2.1 - use INVALID_WAN_CONFIGURATION_DUPLICATE")]
		public const string INVALID_COMMUNICATION_SETTING_DUPLICATE = "714e628b316946389f41ea8d9775d471";	//v2.1 - use INVALID_WAN_CONFIGURATION_DUPLICATE

		[Obsolete("v2.1 - use INVALID_GATEWAY_WAN_CONFIGURATION_ROUTE_ID")]
		public const string INVALID_GATEWAY_COMMUNICATION_ROUTE_SETTING_ID = "facace4ded534412ac770e1b5eedeb57";	//v2.1 - use INVALID_GATEWAY_WAN_CONFIGURATION_ROUTE_ID

		[Obsolete("v2.1 - use INVALID_WAN_CONFIGURATION_GATEWAY_COMM_TYPE_MISMATCH")]
		public const string INVALID_COMM_SETTING_GATEWAY_COMM_TYPE_MISMATCH = "7589733f21f0498ba3fd2e30e71aa0a4";	//v2.1 - use INVALID_WAN_CONFIGURATION_GATEWAY_COMM_TYPE_MISMATCH

		[Obsolete("v2.1 - use INVALID_WAN_CONFIGURATION_ID")]
		public const string INVALID_COMMUNICATION_SETTING = "5823e0e6ff3547f484a04ea9afe1c179";	//v2.1 - use INVALID_WAN_CONFIGURATION_ID

		[Obsolete("v2.1 - use INVALID_IP_ADDRESS_IN_CONFLICTING_WAN_CONFIGURATION_ROUTE")]
		public const string INVALID_IP_ADDRESS_IN_CONFLICTING_COMMUNICATION_ROUTE = "dddbb7439b0240b293b068c40d5f98fd"; //v2.1 - use INVALID_IP_ADDRESS_IN_CONFLICTING_WAN_CONFIGURATION_ROUTE

		[Obsolete("v2.1 - use WAN_CONFIGURATION_ROUTE_NOT_SET_UP")]
		public const string COMMUNICATION_SETTING_ROUTE_NOT_SET_UP = "ae2da0c5e75d43b08d2dc061c3bef7f3";	//v2.1 - use WAN_CONFIGURATION_ROUTE_NOT_SET_UP

		[Obsolete("v2.1 - use WAN_CONFIGURATION_NOT_SET_UP")]
		public const string COMMUNICATION_SETTING_NOT_SET_UP = "b40f7e5d97a14d39a6709d64ea012cd0";	//v2.1 - use WAN_CONFIGURATION_NOT_SET_UP

		[Obsolete("v2.1 - use INVALID_WAN_CONFIGURATION_TYPE")]
		public const string INVALID_COMMUNICATION_SETTING_TYPE = "d7583461d0934588b4dfdf07dd76435c";	//v2.1 - use INVALID_WAN_CONFIGURATION_TYPE

		[Obsolete("v2.1 - use INVALID_NEW_WAN_CONFIGURATION_ROUTE_ID")]
		public const string INVALID_NEW_COMMUNCATION_ROUTE_SETTING_ID = "3e2b1120b77546f3b5586ac2464374f4";	//v2.1 - use INVALID_NEW_WAN_CONFIGURATION_ROUTE_ID

		[Obsolete("v2.1 - use INVALID_RECURRING_DATES_SCHEDULE_INDEX_NODE_MISSING")]
		public const string INVALID_RECURRING_DATES_INDEX_MISSING = "BDA1D72F898E4DC3B58151FE3560CABC";	//v2.1 - use INVALID_RECURRING_DATES_SCHEDULE_INDEX_NODE_MISSING

		[Obsolete("v2.2 - not replaced since this no longer can be returned")]
		public const string INVALID_PREPAY_MAXIMUM_POWER_STATUS_TYPE_ID_NODE_MISSING = "f270a84b294440698d5e69759c5ae26c";  //v2.2 - not replaced since this no longer can be returned

		[Obsolete("v2.2, not replaced since this no longer can be returned")]
		public const string INVALID_PREPAY_MAXIMUM_POWER_STATUS_TYPE_ID = "be3fed7edac94922960d35a80bf82921";  //v2.2, not replaced since this no longer can be returned

		[Obsolete("v2.2, not replaced since this no longer can be returned")]
		public const string INVALID_PREPAY_MAXIMUM_POWER_LEVEL_NODE_MISSING = "f7a01bac1a414a05944362b931428dd9";  //v2.2, not replaced since this no longer can be returned

		[Obsolete("v2.2, not replaced since this no longer can be returned")]
		public const string INVALID_PREPAY_MAXIMUM_POWER_LEVEL = "fd74d21ba4a94171b4c613b1201abd5b"; //v2.2, not replaced since this no longer can be returned

		[Obsolete("v2.3, use INVALID_SEASON_SCHEDULES_NODE_MISSING")]
		public const string INVALID_SEASON_SCHEDULE_NODE_MISSING = "17A69BB8A8D541ED9C8604BC12665103";	//v2.3, use INVALID_SEASON_SCHEDULES_NODE_MISSING

		[Obsolete("v2.3, use INVALID_SEASON_SCHEDULES_SCHEDULE_NODE_MISSING")]
		public const string INVALID_SEASON_SCHEDULE_SCHEDULE_NODE_MISSING = "B6C51E860B8645D1BB43D7E3780826FF";	//v2.3, use INVALID_SEASON_SCHEDULES_SCHEDULE_NODE_MISSING

		[Obsolete("v2.3, use INVALID_PULSE_INPUT_TAMPER_REGULAR_ALARM_STATUS")]
		public const string INVALID_PULSE_INPUT_REGULAR_ALARM_STATUS = "79a2676e8fb14d7aa91e07b47fd02d1e";	//v2.3, use INVALID_PULSE_INPUT_TAMPER_REGULAR_ALARM_STATUS

		[Obsolete("v2.3, use INVALID_PULSE_INPUT_IDLE_STATE")]
		public const string INVALID_PULSE_INPUT_IDLE_STATE_STATUS = "a46d11fdc3f94d6c9e11aa26811b5a8d";	//v2.3, use INVALID_PULSE_INPUT_IDLE_STATE

		[Obsolete("v2.3, use INVALID_COMMUNICATION_REQUEST_TYPE_ID")]
		public const string INVALID_GATEWAY_COMMUNICATION_HISTORY_REQUEST_TYPE_ID = "9eac04040e4c4119acd50e91bdfa5181";	//v2.3, use INVALID_COMMUNICATION_REQUEST_TYPE_ID

		[Obsolete("v2.3, use INVALID_UPDATE_GATEWAY_FIRMWARE_COMMAND")]
		public const string INVALID_UPDATE_DC_1000_FIRMWARE_COMMAND = "62C11ECEDF76441a8CBA14B333BB1CF1";	//v2.3, use INVALID_UPDATE_GATEWAY_FIRMWARE_COMMAND

		[Obsolete("v2.3, retired")]
		public const string INVALID_TASK_PROCESSOR_PATH = "27ff02cb3b8648269527e6857ac2f33a";	//v2.3, retired

		[Obsolete("v2.3, retired")]
		public const string INVALID_TASK_PROCESSOR_PATH_MISSING = "c28a8b0897764fc581e38761c94644c5";	//v2.3, retired

		[Obsolete("v2.3, use INVALID_LONTALK_KEY")]
		public const string INVALID_AUTHENTICATION_KEY = "a87498cd5e1d481eadaa4f6f1c1e432e";	//v2.3, use INVALID_LONTALK_KEY

		[Obsolete("v3.0 use INVALID_PULSE_INPUT_CHANNEL_STATUS_NODE_MISSING")]
		public const string INVALID_PULSE_INPUT_CHANNEL_STATUS_MISSING = "45d43636ae7e4f8b87cd5c123cd43aaf";	//v3.0 use INVALID_PULSE_INPUT_CHANNEL_STATUS_NODE_MISSING

		[Obsolete("v3.0 use INVALID_TASK_PROCESSOR_COMMAND_ID_NODE_MISSING")]
		public const string INVALID_TASK_PROCESSOR_COMMAND_ID_MISSING = "263ae98e8e2c4aeea3696abfc5ec2d33";		//v3.0 use INVALID_TASK_PROCESSOR_COMMAND_ID_NODE_MISSING

		[Obsolete("v3.0 use INVALID_TASK_PROCESSOR_ID_NODE_MISSING")]
		public const string INVALID_TASK_PROCESSOR_ID_MISSING = "9ee904168c6c4cd88df004e1f533a87f";	//v3.0 use INVALID_TASK_PROCESSOR_ID_NODE_MISSING

		[Obsolete("v3.0 use INVALID_ROUTING_ENTITY_TYPE_ID_NODE_MISSING")]
		public const string INVALID_ROUTING_ENTITY_TYPE_NODE_MISSING = "950810a52f07450eae0af041a48499a6";	//v3.0 use INVALID_ROUTING_ENTITY_TYPE_ID_NODE_MISSING

		[Obsolete("v3.0 - no longer used")]
		public const string INVALID_DEVICE_NAME = "c6f37c20012b4b00b7a0ad938d6978e0";		//v3.0 - no longer used

        [Obsolete("v3.0 - no longer used")]
        public const string INVALID_LOAD_PROFILE_CHANNEL_SOURCE = "ed98589734a04abda4fafc778dba1621"; //v3.0 - no longer user

        [Obsolete("v3.0 - no longer used")]
        public const string INVALID_LOAD_PROFILE_CHANNEL_SOURCE_NODE_MISSING = "d2b1a04fdde54f55a8317b4ebed528b7"; //v3.0 - no longer used

        [Obsolete("v3.0 - use READ_BILLING_DATA_ON_DEMAND_IN_PROGRESS")]
        public const string READ_BILLING_DATA_ON_DEMAND_IN_PROCESS = "7DBADD43795C442b80A9095D8559DB02";    //v3.0 - use READ_BILLING_DATA_ON_DEMAND_IN_PROGRESS

        [Obsolete("v3.0 - use LOAD_PROFILE_IN_PROGRESS")]
        public const string LOAD_PROFILE_IN_PROCESS = "0dc815344c964ae8ad5a421221a21fc7";   //v3.0 - use LOAD_PROFILE_IN_PROGRESS

        [Obsolete("v3.0 - use PERFORM_SELF_BILLING_READ_IN_PROGRESS")]
        public const string PERFORM_SELF_BILLING_READ_IN_PROCESS = "49685f8d36b44a43a9f5da304e5079f2";  //v3.0 - use PERFORM_SELF_BILLING_READ_IN_PROGRESS

        [Obsolete("v3.0 - no longer used")]
        public const string ENABLE_MAXIMUM_POWER_IN_PROCESS = "78e18517b2484c8eab9b77abb3839875";   //v3.0 - no longer used

        [Obsolete("v3.0 - use INVALID_METER_SOFTWARE_VERSION_NOT_SUPPORTED")]
        public const string INVALID_METER_SOFTWARE_VERSION = "d733335439e64ac8b4808a2b7e63cecb"; //v3.0 - use INVALID_METER_SOFTWARE_VERSION_NOT_SUPPORTED

        [Obsolete("v3.0 - no replacement - not used")]
        public const string INVALID_POWER_QUALITY_THRESHOLD_COMMAND = "4B1756C208BE49b4818ADE3FA20F3E4C"; //v3.0 - no replacement - not used

        [Obsolete("v3.0 - no replacement - not used")]
        public const string INVALID_POWER_QUALITY_THRESHOLD_VALUE_NODE_MISSING = "BA450BCC555C43bf84E9F750EDB9ABCF"; //v3.0 - no replacement - not used

        [Obsolete("v3.0 - no replacement - not used")]
        public const string POWER_QUALITY_THRESHOLD_VALUE_IN_SECONDS_NODE_MISSING = "E51F9252317043f69AB07C829DD3358D"; //v3.0 - no replacement - not used

        [Obsolete("v4 - no replacement, no longer used")]
        public const string INVALID_DATASET_NEXT_COMMAND_HISTORY_REQUEST_DATE_TIME_RANGE = "61e1341f9ba348699c28731fdef9e6cc"; //v4 - no replacement, no longer used

        [Obsolete("v4 - no replacement, no longer used")]
        public const string INVALID_NEW_WAN_CONFIGURATION_ROUTE_ID = "3e2b1120b77546f3b5586ac2464374f4"; //v4 - no replacement, no longer used

        [Obsolete("v4 - no replacement, no longer used")]
        public const string INVALID_IP_ADDRESS_IN_CONFLICTING_WAN_CONFIGURATION_ROUTE = "dddbb7439b0240b293b068c40d5f98fd"; //v4 - no replacement, no longer used

        [Obsolete("v4 - no replacement, no longer used")]
        public const string INVALID_NEW_WAN_CONFIGURATION_ROUTE_ID_NODE_MISSING = "a535cf7454c54953bab0abd6f139f78b"; //v4 - no replacement, no longer used

        [Obsolete("v4 - no replacement, no longer used")]
        public const string WAN_CONFIGURATION_ROUTE_NOT_AVAILABLE = "5c88c8dc907b4e33afa5bdbdb97b4775"; //v4 - no replacement, no longer used

        [Obsolete("v4 - no replacement, no longer used")]
        public const string WAN_CONFIGURATION_ROUTE_NOT_SET_UP = "ae2da0c5e75d43b08d2dc061c3bef7f3"; //v4 - no replacement, no longer used

        [Obsolete("v4 - no replacement, no longer used")]
        public const string INVALID_GATEWAY_WAN_CONFIGURATION_ROUTE_ID = "facace4ded534412ac770e1b5eedeb57"; //v4 - no replacement, no longer used

        [Obsolete("v4 - no replacement, no longer used")]
        public const string INVALID_IP_ADDRESS_RANGE_NOT_AVAILABLE = "58883233c48f4457836cfe7dd5048a9b"; //v4 - no replacement, no longer used

        [Obsolete("v4 - no replacement, no longer used")]
        public const string INVALID_IP_ADDRESS_RANGE_OVERLAP = "7e0b43bcc8a848269efe0c541ebae058"; //v4 - no replacement, no longer used

        [Obsolete("v4 - no replacement, no longer used")]
        public const string INVALID_NETMASK = "6b90ff2bfd2b424dbc33297c9c34c714"; //v4 - no replacement, no longer used

        [Obsolete("v4 - no replacement, no longer used")]
        public const string INVALID_NETMASK_NODE_MISSING = "f2fa31bf7e66459c8ad5dbe498072ec7"; //v4 - no replacement, no longer used

        [Obsolete("v4 - no replacement, no longer used")]
        public const string INVALID_SERVER_HOSTNAME = "597fdea72c4c480c872e51c1f703d5cb"; //v4 - no replacement, no longer used

        [Obsolete("v4 - no replacement, no longer used")]
        public const string INVALID_SERVER_HOSTNAME_NODE_MISSING = "09e42eb529f84ab99f3447d1cac05d36"; //v4 - no replacement, no longer used

        [Obsolete("v4 - no replacement, no longer used")]
        public const string INVALID_SERVER_HOSTNAME_DUPLICATE = "94f3e7b696db4d14bd0cd53299e1aead"; //v4 - no replacement, no longer used

        [Obsolete("v4 - no replacement, no longer used")]
        public const string GATEWAY_IN_IP_ADDRESS_RANGE = "EB5EB70F8E1E484893EC885C48A61A18"; //v4 - no replacement, no longer used

        [Obsolete("v4 - no replacement")]
        public const string INVALID_MBUS_ALARM_NODE_MISSING = "120c411ae3f44ca786cffa6ed4529255"; //v4 - no replacement, no longer used

        [Obsolete("v4 - no replacement")]
        public const string INVALID_MBUS_ALARM_ID_NODE_MISSING = "61e41e5cec394768bf4d6b7a3a44498a"; //v4 - no replacement, no longer used

        [Obsolete("v4 - no replacement")]
        public const string INVALID_MBUS_ALARM_ID = "c780518ed91b4b18a36882bed79884b9"; //v4 - no replacement, no longer used

        [Obsolete("v4 - no replacement")]
        public const string INVALID_MBUS_ALARM_STATUS_TYPE_ID_NODE_MISSING = "1957f623a5f84fd7a306172734af9058"; //v4 - no replacement, no longer used

        [Obsolete("v4 - no replacement")]
        public const string INVALID_MBUS_ALARM_STATUS_TYPE_ID = "f6f260722f3a47c0b5b86a8ab9a4b4d6"; //v4 - no replacement, no longer used

        [Obsolete("v4 - no replacement")]
        public const string DUPLICATE_MBUS_ALARM_ID = "f1433e1260d440af83388a35dff024f0"; //v4 - no replacement, no longer used

        [Obsolete("v4 - no replacement, no longer used")]
        public const string INVALID_STATUS_CHANGE = "0677FAC893734e26BB026F20CCC197B6";

        [Obsolete("v4 - no replacement, no longer used")]
        public const string INVALID_HIGH_PRIORITY_CONNECTION_REQUEST = "4E66B732B94E42d1825269AD5B091763";

        [Obsolete("v4 - no replacement, no longer used")]
        public const string UNSUPPORTED_GATEWAY_STATUS_TYPE_ID = "012C3F93AEE74f61B8048403E6B60432";

        [Obsolete("v4 - no replacement, no longer used")]
        public const string INVALID_UPDATE_GATEWAY_FIRMWARE_COMMAND = "62C11ECEDF76441a8CBA14B333BB1CF1";

        [Obsolete("v4 - no replacement, no longer used")]
        public const string INVALID_UPDATE_METER_FIRMWARE_COMMAND = "CD42CD0ED62249728954C29200A793D5";

        [Obsolete("v4 - no replacement, no longer used")]
        public const string NO_GATEWAY_IP_ADDRESS_AVAILABLE = "b866d54e8f8d4aeba2bdbcdd622cadba"; //v4 - no replacement, no longer used

        [Obsolete("v4 - no replacement, no longer used")]
        public const string INVALID_DATASET_NEXT_COMMAND_HISTORY_ID = "7a5ffa4e4fdd4df4a1a79981148c72c4"; //v4 - no replacement, no longer used

        [Obsolete("v4.1 - no replacement, no longer used")]
        public const string INVALID_PHONE_NUMBER = "01b97727a7344a72b0cc1bf9781bff9c";

        [Obsolete("v4.1 - no replacement, no longer used")]
        public const string INVALID_PHONE_NUMBER_DUPLICATE = "765ef23301134d2e8231ee0d93f95fa9";

        [Obsolete("v4.1 - no replacement, no longer used")]
        public const string INVALID_PHONE_NUMBER_BLANK = "b337587725294d08bd211a1823c4ca9e";

        [Obsolete("v3.0 - no replacement, no longer used")]
        public const string INVALID_CONTROL_RELAY_TIER_VALUE = "2b1eb3f5528b4c24b073bda6c4ee57be";

        [Obsolete("v4.1 - no replacement, no longer used")]
        public const string INVALID_WAN_CONFIGURATION_DUPLICATE = "714e628b316946389f41ea8d9775d471";

		[Obsolete("v5.1 - use INVALID_DEVICE_ID_ASSIGNMENT")]
		public const string INVALID_DEVICE_ID_ASSOCIATION_INVALID = "030061a6e1154989affb016cde162f89";

		[Obsolete("v5.1 - use INVALID_DELETE_DATA_POINTS_NODE_MISSING")]
		public const string DELETE_DATA_POINTS_NODE_MISSING = "368B2F3EC6AD42deB2603EBB73B64F1B";

		[Obsolete("v5.1 - use INVALID_DELETE_DATA_POINTS_ID")]
		public const string DELETE_DATA_POINTS_INVALID = "DD548AFB4D71457982C0A544897D2233";

		[Obsolete("v5.1 - use INVALID_ATTRIBUTE_VALUE_NODE_MISSING")]
		public const string INVALID_ATTRIBUTE_PARAMETER_VALUE_NODE_MISSING = "6ce8a02f4c5d40d5a213e7f6efbca018";

		[Obsolete("v5.1 - use INVALID_ATTRIBUTE_ID_NODE_MISSING")]
		public const string INVALID_ATTRIBUTE_PARAMETER_ID_NODE_MISSING = "31b76abe7a264b14810adb18d0648d23";

		[Obsolete("v5.1 - use INVALID_FILE_EMPTY")]
		public const string INVALID_FILE_EMTPY = "54463c85c6954bfea181d505dd4debad";

		[Obsolete("v5.2 - no replacement, no longer used")]
		public const string TYPE_MISMATCH = "AF5D081732DF4e08976D54FDA102EFF8";

		[Obsolete("v5.2 - no longer used")]
		public const string DUPLICATE_METER_DISPLAY_ID_TEXT = "A654EED3F11A465691CA574B67587704";

		[Obsolete("v5.2.000 - no longer used")]
		public const string INVALID_MAXIMUM_POWER_LEVEL_NODE_MISSING = "46b865cf335e4995b074b8cc3e5c281e";
		[Obsolete("v5.2.000 - no longer used")]
		public const string INVALID_MAXIMUM_POWER_TYPE_ID = "2710EB81268A4c4cA24D2C7768765D89";
		[Obsolete("v5.2.000 - no longer used")]
		public const string UNSUPPORTED_MAXIMUM_POWER_TYPE_ID = "C732E9F0EE204517B8C1EBDD39F27EB8";
		[Obsolete("v5.2.000 - no longer used")]
		public const string INVALID_MAXIMUM_POWER_LEVEL_DURATION_MISMATCH = "B620017C76EB4f11AD81F87217C95215";
		[Obsolete("v5.2.000 - no longer used")]
		public const string INVALID_POWER_CONFIGURATIONS_NODE_MISSING = "6E7941C5D3EE4b5fBB15AC7F83E14199";
		[Obsolete("v5.2.000 - no longer used")]
		public const string INVALID_PREPAY_MAXIMUM_POWER_LEVEL_DURATION = "27838EF59CF140d3953B05080D999328";
		[Obsolete("v5.2.000 - no longer used")]
		public const string INVALID_PREPAY_MAXIMUM_POWER_LEVEL_DURATION_NODE_MISSING = "5C8B9433317845c3ABA292D616F04621";
		[Obsolete("v5.2.000 - no longer used")]
		public const string INVALID_PREPAY_MAXIMUM_POWER_NODE_MISSING = "0F0ADB596EF34e68A27CC99C46A71F80";
		[Obsolete("v5.2.000 - no longer used")]
		public const string INVALID_PREPAY_POWER_CONFIGURATION_NODE_MISSING = "C8D71D7CBA86469dAB22E72B98BAB0FD";
		[Obsolete("v5.2.000 - no longer used")]
		public const string INVALID_PRIMARY_MAXIMUM_POWER_LEVEL_DURATION = "BF209D6C05DA4e2394FBDFCB36B23CBA";
		[Obsolete("v5.2.000 - no longer used")]
		public const string INVALID_PRIMARY_MAXIMUM_POWER_LEVEL_DURATION_NODE_MISSING = "454C8A945BBA488d90F2A2ABE884D46A";
		[Obsolete("v5.2.000 - no longer used")]
		public const string INVALID_PRIMARY_MAXIMUM_POWER_NODE_MISSING = "767111BBD9194cf09F47498CBE551E49";
		[Obsolete("v5.2.000 - no longer used")]
		public const string INVALID_PRIMARY_POWER_CONFIGURATION_NODE_MISSING = "D4C0D4E44CFC4e899E0D8DD8F68EE331";
		[Obsolete("v5.3 - no replacement, no longer used")]
		public const string INVALID_CONTROLLER_CONFIGURATION_FILE_TYPE = "47f62e56b6ac41939eba537b78e3c774";
		[Obsolete("v5.3 - no replacement, no longer used")]
		public const string CONTROLLER_CONFIGURATION_FILE_XSD_VALIDATION_FAILURE = "7eb05d5c8f9d41cca41da09c9ebb7b0a";
		[Obsolete("v5.3 - no replacement, no longer used")]
		public const string DUPLICATE_CONTROLLER_CONFIGURATION_FILE_TYPE = "7659634a0cd846b29ba4817f1a508d14";
		[Obsolete("v5.3 - no replacement, no longer used")]
		public const string UNSUPPORTED_CONTROLLER_CONFIGURATION_VERSION = "3bd9491b21ec43da9735f2410cc2fd97";
        ////////////////////////////////////////////////////////////////////////////////////////////
	}


	public class MBusAutoDiscoveryStatusTypes
	{
		public const string ENABLED = "ce8efc7530964e9a917bc70407af21af";
		public const string DISABLED = "4672466240864adeaa7d16008551e272";
	}

	// Contains the list of MessageLog statuses
	public class MessageLogStatus
	{
		public const string ENABLED = "2f3ab4b458764083b84b2fca15049604";
		public const string DISABLED = "4b2859f9ba4243f88fbe6a6d64f69616";
	}

	// List of standard options that apply to several API's
	public class StandardAPIOptions
	{
		public const string YES = "5af8a532523c4c6e9f9344c0827391ee";
		public const string NO = "fe6a84d285d74e06bc52413ffb81151a";
		public const string DISCONNECT = "7d6b389f36044740bc0e1babb6340b4f";
		public const string CONNECT = "30bd98ee3bf641798418ee0222d235b6";
		public const string EXACT = "de334f6f1654471d99e18df1b7c29fdd";
		public const string INEXACT = "78c2a568a6a04a9e9ccf08329043657f";
        public const string SUCCESS = "e19cc05385004100ac7dbde7baa2a2ef";
        public const string FAILURE = "1bf17985fcfb44f19f00799677b526bf";
	}

	// List of settings in the solution
	public class SettingTypes
	{
		public const string ARCHIVESETTING = "1cb53121e2bd4c50b78568ca6cba980c";
		public const string WAN_CONFIGURATION = "ef34587793674fafab8c2d4f77c873a3";
		public const string SOLUTIONSETTING = "beb7933008fa44c590692991de18fa86";
        public const string OPERATIONAL_PROFILE = "0d25d10d84264c12b1071510495e4172";

		////////////////////////////////////////////////////////////////////////////////////////
		//The following are deprecated - the version in which they were deprecated and the    //
		//constant replacing them (if any) is listed to the right of the constant.            //
		////////////////////////////////////////////////////////////////////////////////////////

		[Obsolete("v2.1 - replaced by WAN_CONFIGURATION")]
		public const string COMMUNICATIONSETTING = "ef34587793674fafab8c2d4f77c873a3";	//v2.1 - replaced by WAN_CONFIGURATION

		[Obsolete("v2.1 - replaced by WAN_CONFIGURATION_ROUTE")]
		public const string COMMUNICATIONROUTE = "b3ad8aa480f240538db0df10eaf004c7";	//v2.1 - replaced by WAN_CONFIGURATION_ROUTE

        [Obsolete("v4 - not replaced")]
        public const string WAN_CONFIGURATION_ROUTE = "b3ad8aa480f240538db0df10eaf004c7"; //v4 - not replaced
        ////////////////////////////////////////////////////////////////////////////////////////
	}

	// List of solution settings in the solution
	public class SolutionSettingTypes
	{
		public const string API_KEY_TIMEOUT_PERIOD = "6DB13F06AE454d7cB054061A250B76A2";
		public const string HIERARCHY_PATH_DELIMITER = "7ADD9A1F555E4f879E2F0A8C6204932A";
		public const string DAILY_SCHEDULE_CALCULATION_TIME = "eea43a3a2bf14490a7726d20aaee8a7f";
		public const string HIERARCHY_WILDCARD = "10c13e93eb2340e8ae1f52a903a33e7b";
		public const string DC1000_ADAPTER_SERVER_URL = "4cf38c4953034ce79ca6fabe98cd1fca";
		public const string DC1000_MAX_CONNECTION_INACTIVITY_TIMEOUT = "66688870F9554ee1A3227B0DBDD06E7C";
		public const string DC1000_DCXP_COMMAND_TIMEOUT = "AAE6926A528D4220822ABF88D8FC2651";
		public const string EVENT_RECEIVER_URL = "7a3405d3ebe2486491cff11bcb7a622d";
		public const string EVENT_RECEIVER_NAMESPACE = "a0aa36cb53704f01940ea070933b70ff";
		public const string DC1000_FTP_TIMEOUT = "7eed92692f8a4b038b61ef374bc09882";
		public const string ORPHAN_CHECK_TIME_ALWAYS_ON_IP_CONNECTED = "88c4045ad843454d9692fc4bf394999c";
		public const string ORPHAN_CHECK_TIME_ALWAYS_ON_IP_PENDING = "4d525a491b6247598d7c44181b63e6ae";
		public const string DC1000_FTP_RESPONSE_TIMEOUT = "F4CE0256CF374a7d82195941A6A17213";
		public const string MINIMUM_RECHECK_QUEUE = "7b2527dbfdce4bc59bb842113e69eb1b";
		public const string MAXIMUM_RECHECK_QUEUE = "6fbf9f7bbe9f4df1a46be2ce89295442";
		public const string SCHEDULE_DELTA_LOAD_PROFILE_NOW = "2D0BB098EA4B4d0a9F08EA9606D1996D";
		public const string DEFAULT_DEVICE_DOWN_LIMIT = "9608981a58474e5c99a1a0cb84085fb2";
		public const string USE_WAN_COMPRESSION = "F82C9628C46440abAB87A8FB547B4D68";
		public const string DC1000_MINIMUM_WAN_COMPRESSIBLE_BYTES = "65a05337822341c39bbf136da3fcb69d";
		public const string ARCHIVE_ROW_LIMIT = "e1dec9f41d9240a5908caf27ecf93a18";
        public const string COMMAND_HISTORY_ORPHAN_CHECK_ROW_LIMIT = "259ef87c4d5d4f26bd656bdb6e469d39";
		public const string DISCONNECT_ON_DCXP_TIMEOUT = "fce47d7588ce4076a45c3a33f26da190";
		public const string MBUS_ON_DEMAND_BILLING_TIMEOUT_PERIOD = "352BB96D62134e9fA6625FDEFDF79F88";
        public const string MEP_ON_DEMAND_DATA_TIMEOUT_PERIOD = "7e61f2559f0744289efbb77707232c26";
		public const string SERVER_PERFORMANCE_LOG_INTERVAL = "8bddff448c924d1d8144a0ca17864553";
		public const string DAYS_UNTIL_STOP_MODE = "ba90116e638f49669fa5f34ea19efb77";
		public const string FTP_ENCRYPTED_DIRECTORY_LISTING_POLL_DELAY = "3e23b7baff254b61b5f9c76019cfe921";
		public const string PROXY_SERVER_HOST_NAME = "05205dbf9e00421c9b66b685b95c1d7d";
		public const string PROXY_SERVER_PORT = "e77cbe864d054075971b7e8f2a396852";
        public const string PROXY_SERVER_USERNAME = "4d9c362a58d9411aaca631c886ed7dca";
        public const string PROXY_SERVER_PASSWORD = "e17ec8fc151b43828291258e6045848d";
		public const string TASK_TIMEOUT_ROW_LIMIT = "7c5a5ff39c3f436680edef291c422c7c";
		public const string FORWARD_DEPRECATED_DC1000_EVENT = "52a32c7f85c945eca5d5ea5e8e0ec9ec";
        public const string DELTA_LOAD_PROFILE_PROCESS_MODE = "6941e02df6bc403d901af18444614d5b";
        public const string DELTA_LOAD_PROFILE_PROCESS_BLOCK_LIMIT = "95c4faa2a1d9473f9b14ff7139deeb3a";
		public const string GATEWAY_SYNCHRONIZATION_DELAY = "f47e95cc74234c93a8449dba0aa4fd22";
        public const string LOAD_PROFILE_ACCUMULATOR_RESULT_VALUE = "198d738f150340628e46dcbc34ba0b98";
        public const string EXCLUDE_INVALID_LOAD_PROFILE_INTERVAL_DATA = "39a486807a404117bf855fdec656965c";
		public const string TIME_WAIT_CONNECT_RETRY_ALWAYS_ON_IP = "e1712659569f4cefa429566ecaf8da8a";
		public const string READ_DEVICE_LOCAL_DATE_TIME = "a0af9e54378043aea9a7512c5434089d";
        public const string DEFAULT_MAXIMUM_BATCH_SIZE = "951cdc8da44943fa8f7d8e3798bb6ac5";
		public const string RETRIEVE_LIST_MAXIMUM_COUNT_DEFAULT = "22947bfd4d59430e8e5486a886af7aae";
        public const string GATEWAY_AND_DEVICE_MANAGER_INDIVIDUAL_RESULT_REMOVAL = "6314b564fd1644e9b826a1bac807b978";
        public const string MAXIMUM_GATEWAY_INITIATED_CONNECTIONS = "76f305b044064545aa6f4e56746098e4";
        public const string DEVICE_REACHABILITY_TIMEOUT = "e28dd0a66198442593bdbb457e25408f";

        public const string GENERAL_MESSAGE_CACHE_SIZE = "92dbc396ce1b4fef915ef354ec52cbb6";
        public const string TASK_MESSAGE_CACHE_SIZE = "9ddd08e79ac543bfa8bc573831997d4f";
        public const string MESSAGE_CACHE_REMOVAL_FREQUENCY = "16648caa36704eb89da416e66d65b089";
        public const string MESSAGE_CACHE_REMOVAL_CRITERIA = "6cdc625ef62f49cba57c302ffa6bfc5d";

        //ATM
        public const string ATM_GATEWAY_BATCH_COUNT = "6c05668fd9e64bf48a3be6cdef59143d";
        public const string ATM_CREATE_CONNECTIONS = "2b52c6c531e2489eb437c02d94a484a9";
        public const string ATM_DISCOVERED_DATA_DEFAULT_COLLECTION_TIME = "87aaf58fc6054617894202c70f6ff825";
        public const string ATM_DEVICE_BATCH_COUNT = "f5f1a2551c514ac2846b266268cc422f";
        public const string ATM_DEVICE_ASSIGNMENT_BATCH_COUNT = "f24757f595ce471a8fc371e1cb497098";
        public const string ATM_ASSIGNMENT_FACTOR = "43a4792e6555490597b69d9706a37689";
        public const string ATM_DISCOVERED_DATA_MAXIMUM_AGE = "944f668b223f4b8db9d72044b4734b5f";
        public const string ATM_COMMISSION_TIMEOUT = "d2d3b607ff274b4182015166efe9cc6f";
        public const string ATM_MAXIMUM_ASSIGNMENT_ATTEMPTS = "246e491a3b5641f7abfcac5a9da4ca58";
        public const string ATM_MAXIMUM_SAME_GATEWAY_ASSIGNMENT_ATTEMPTS = "79622935f4054a039e6eb3d7bc29e03d";
        public const string ATM_COMMISSION_DATA_ACCEPTABLE_REPEATER_COUNT = "295891c575c2476f9a7fab8d53e7a047";
        public const string ATM_COMMISSION_DATA_ACCEPTABLE_CARRIER_MARGIN = "6b319cdec372464a9dfa321a4240e179";
        public const string ATM_COMMISSION_DATA_ACCEPTABLE_SIGNAL_STRENGTH = "1ce21fd296ca4b7fb6cb5e9e06628ba1";
		public const string ATM_DEVICE_ASSIGNMENT_RANK_BATCH_DELETE_COUNT = "3e95f128a04f4110949293effe209c97";
		public const string ATM_FINAL_ASSIGNMENT_TIMEOUT_PERIOD_START_MODE = "013cddce9cb342f2aad1f4d7367bf38b";
		public const string ATM_NO_ACCEPTABLE_DATA_CONCENTRATOR_MODE = "4b7daded43f94a079a6eac281c3ea4cb";

        public const string COMMISSION_PROCESS_MODE = "71ffc76603694ff7ba81080fbd50b7a0";

		public const string EVENT_MANAGER_INDIVIDUAL_EVENT_HISTORY_REMOVAL = "0d2ce23d6a6842fda3bde1e228582e6e";
		public const string USE_RESULT_COMPRESSION = "20c80eedbadf4a9ca6f2ce5744c713ba";

        public const string DC_ADAPTER_COMMUNICATION_COMPLETION_DELAY = "4d88ce272af8411cb07373dc7a5d54a2";
        public const string GATEWAY_REPLACEMENT_INFORMATION_MINIMUM_AGE = "afb64d1f35e342ff81e5fa74492542bb";
        public const string GATEWAY_REPLACEMENT_INFORMATION_MAXIMUM_AGE = "1125aa190f514ae5872983d4d8eed1f6";
        public const string GATEWAY_REPLACEMENT_INFORMATION_COLLECTION_MINIMUM_METER_COUNT = "c35c63fe44be4597bb6564ef6a08870d";
		public const string DEFAULT_MAXIMUM_IDENTITY_RELEASE_SIZE = "c07e392eb4c74ecf872b8b90e0f72189";
        public const string READ_DEVICE_LOAD_STATUS_DATA_CONCENTRATOR_DELAY = "4d4390767a3e440d955062bc3f64cc20";
        public const string PERFORM_COMMAND_ON_GROUP_COMMAND_HISTORY_DETAIL_MODE = "b3b04bba498e456abee091efd3305081";
		public const string CACHE_REFRESH_INTERVAL = "1a3cb2746ec7454480c0d7983f16b858";
		public const string PHASE_INACCURACY_EVENT_THRESHOLD_VALUE = "5f412ef0e18347fcb9c14084dc0d5548";

        public const string GATEWAY_TIME_SYNCHRONIZATION_NOTIFICATION_THRESHOLD = "fb906a7147644092bd97dc7d5cd768bb";
		public const string DC_ADAPTER_INTERNAL_PIPE_REQUEST_RETRY_COUNT = "eced0bcbd029456b9505f58c734daaa4";
		public const string DC_ADAPTER_INTERNAL_PIPE_REQUEST_RETRY_DELAY = "f38f70fa92fe41e29166cfa46e5de142";
		public const string SYSTEM_CONFIGURATION_FILE_UPDATE_DELAY = "224b3c46eceb43d588f556e26bfad2ae";
		public const string MAXIMUM_SSH_LOGIN_RETRY_ATTEMPTS = "bcba0f7c29da4ce9b72774ceb332506e";

		public const string CONVERSION_COLLECTION_PERIOD = "e756089592f84670833b3687ecfc3b37";
		public const string ENGINE_MESSAGE_POLLING_DELAY = "208e89ba99ee48318802208086ef71e7";

        public const string MAXIMUM_RESULT_DATA_SIZE = "5e431fe8d7284b428c54292c58a276ed";


        public const string COMMUNICATION_HISTORY_ORPHAN_CHECK_ROW_LIMIT = "7cb4f1b9679d41618af7d7dad2890139";
		public const string NES_SYSTEM_SOFTWARE_ENGINE_CONVERSION_MAXIMUM_BATCH_SIZE = "4de2b8b1baa240c196387336296e07ad";

		//v5.1
		public const string READ_DEVICE_MEMORY_CONFIGURATION_DATA_CONCENTRATOR_DELAY = "db93f2f24cf742f79850d65f418d6e20";
		public const string LOG4NET_CONFIGURATION = "23a6929cb902432aa94ad28a1db6cba5";

		//v5.2
		public const string CIM_SOURCE_STRING = "dbfff030e9154310aebc604e05900613";
		public const string CIM_REQUEST_TIMEOUT_SECONDS = "3a816b8dba98452589c06760b343a447";
		public const string ALARM_FILTERS = "3d91658c6b46405c9cac3321b3293c25";
        public const string DEVICE_EVENT_LOG_HISTORY_EVENT_NUMBERS = "8b01f3244005409f85f9205089f850d0";
        public const string WATT_HOUR_RETURN_TYPE = "4f3f1a7660bf4846a8c7046ba68c7438";

		//v5.3
        public const string UNBALANCED_VOLTAGE_RANGES = "f18c6be0b6d74bbebba65e083d4765da";
		public const string VALIDATE_HELLO_MESSAGE_SIGNATURE = "9437a1d5da024c11992ea8325f178fa2";
        public const string LVGM_AUTOMATIC_READ_MINIMUM_AGE = "8f4e5f77155944f48608f9a88e7b151b";
        public const string LVGM_AUTOMATIC_READ_MAXIMUM_AGE = "4225c09c02304a9b86182e5fd22c080b";
        public const string LVGM_AUTOMATIC_READ_TIME = "fb7307031e5f4a30b4651f7ee77221c4";


        ////////////////////////////////////////////////////////////////////////////////////////
		//The following are deprecated - the version in which they were deprecated and the    //
		//constant replacing them (if any) is listed to the right of the constant.            //
		////////////////////////////////////////////////////////////////////////////////////////

		[Obsolete("v2.1 - replaced by EVENT_SOAP_CALL_URL")]
		public const string EVENT_SOAP_CALL_ADDRESS = "7a3405d3ebe2486491cff11bcb7a622d";	//v2.1 - replaced by EVENT_SOAP_CALL_URL

		[Obsolete("v2.0 - replaced by DC1000_RESOURCE_READ_COUNT")]
		public const string DC1000_ADAPTER_NON_URGENT_READ_COUNT = "72b14bb18d25435eb12b11ef7a87bb08"; //v2.0 - replaced by DC1000_RESOURCE_READ_COUNT

		[Obsolete("v2.1 - replaced by API_KEY_TIMEOUT_PERIOD")]
		public const string SECURITYKEY_INACTIVITY_LIFESPAN = "6DB13F06AE454d7cB054061A250B76A2";	//v2.1 - replaced by API_KEY_TIMEOUT_PERIOD

		[Obsolete("v2.1 - replaced by HIERARCHY_PATH_DELIMITER")]
		public const string HIERARCHY_DELIMITER = "7ADD9A1F555E4f879E2F0A8C6204932A";	//v2.1 - replaced by HIERARCHY_PATH_DELIMITER

		[Obsolete("v2.1 - no longer used")]
		public const string EMAIL_SENTBY_ADDRESS = "752896D3EA534a6f8814366C97C8EE55";	//v2.1 - no longer used

		[Obsolete("v2.1 - no longer used")]
		public const string SMTP_SERVER_ADDRESS = "E8890EEF580C44fe8557A2E1F481F91D";	//v2.1 - no longer used

		[Obsolete("v2.1 - replaced by AGGREGATION_SOAP_CALL_TIMEOUT_PERIOD")]
		public const string SOAP_CALL_TIMEOUT_PERIOD = "7E9C00D0C8664d3eA4C3E5500A21F6C0";	//v2.1 - replaced by AGGREGATION_SOAP_CALL_TIMEOUT_PERIOD

		[Obsolete("v2.1 - replaced by TCPIP_SOCKET_MESSAGE_TIMEOUT_PERIOD")]
		public const string TCPIP_SOCKET_TIMEOUT_PEROID = "94417108619144a2881510CC5E4A8458";	//v2.1 - replaced by TCPIP_SOCKET_MESSAGE_TIMEOUT_PERIOD

		[Obsolete("v2.1 - replaced by ILON100_DATALOG_MAX_UPLOAD_SESSION_TIMEOUT")]
		public const string DATALOG_UPLOAD_SESSION_MAX_LIFETIME = "09AC43CA44A044b188384AE033913ED1";	//v2.1 - replaced by ILON100_DATALOG_MAX_UPLOAD_SESSION_TIMEOUT

		[Obsolete("v2.1 - replaced by DATALOG_SOAP_READ_COUNT")]
		public const string DATALOG_SOAP_RETRIEVE_COUNT = "ca2bd557f2ab46aca620f986326157b4";	//v2.1 - replaced by DATALOG_SOAP_READ_COUNT

		[Obsolete("v2.1 - replaced by SUMMARY_KEY_DATA_POINT_IDS")]
		public const string SUMMARY_KEY_DATAPOINTIDS = "81cc4f8a18e64b308d0f0dd258cb1d21";	//v2.1 - replaced by SUMMARY_KEY_DATA_POINT_IDS

		[Obsolete("replaced by TIME_WAIT_DEPENDENT_TASK_NOT_FOUND")]
		public const string TIME_WAIT_DEPENDENCY_NOT_FOUND = "38bbc28186104981a359b3c18e7e6a2a";	//replaced by TIME_WAIT_DEPENDENT_TASK_NOT_FOUND

		[Obsolete("v2.1 - replaced by DC1000_MAX_CONNECTION_INACTIVITY_TIMEOUT")]
		public const string DC1000_DISASTERTIMEOUT = "66688870F9554ee1A3227B0DBDD06E7C";	//v2.1 - replaced by DC1000_MAX_CONNECTION_INACTIVITY_TIMEOUT

		[Obsolete("v2.1 - replaced by DC1000_DCXP_COMMAND_TIMEOUT")]
		public const string DC1000_RESPONSETIMEOUT = "AAE6926A528D4220822ABF88D8FC2651";	//v2.1 - replaced by DC1000_DCXP_COMMAND_TIMEOUT

		[Obsolete("v2.1 - replaced by DC1000_MAX_OUTSTANDING_DCXP_COMMANDS")]
		public const string DC1000_MAX_OUTSTANDING = "FEC89CF21289495eA455F428327784CA";	//v2.1 - replaced by DC1000_MAX_OUTSTANDING_DCXP_COMMANDS

		[Obsolete("v2.1 - replaced by DC1000_FTP_TIMEOUT")]
		public const string DC1000_FTPTIMEOUT = "7eed92692f8a4b038b61ef374bc09882";	//v2.1 - replaced by DC1000_FTP_TIMEOUT

		[Obsolete("v2.1 - replaced by CISCO_ACCESS_SERVER_AUTHENTICATION_ENGINE_SERVER_SECRET")]
		public const string CISCO_ACCESS_SERVER_AUTHENTICATION_ENGINE_SECRET = "BC79C2A6AA5D4046A6E18A82E0F64C66";	//v2.1 - replaced by CISCO_ACCESS_SERVER_AUTHENTICATION_ENGINE_SERVER_SECRET

		[Obsolete("v2.1 - replaced by DC1000_REFUSAL_PORT")]
		public const string DC_REFUSAL_PORT = "ecfc4369cfd946729e48b41087e7bc29";	//v2.1 - replaced by DC1000_REFUSAL_PORT

		[Obsolete("v2.1 - replaced by CISCO_ACCESS_SERVER_LAST_DATE_TIME_CONNECTION_ATTEMPT")]
		public const string CISCO_ACCESS_SERVER_LAST_CONNECTION_ATTEMPT_DATE_TIME = "392749bc81a94576ac65a1c32db9de27";	//v2.1 - replaced by CISCO_ACCESS_SERVER_LAST_DATE_TIME_CONNECTION_ATTEMPT

		[Obsolete("v2.1 - replaced by TIME_WAIT_CONNECT_RETRY_ALWAYS_ON_IP")]
		public const string TIME_WAIT_CONNECT_RETRY_ALWAYSONIP = "e1712659569f4cefa429566ecaf8da8a";	//v2.1 - replaced by TIME_WAIT_CONNECT_RETRY_ALWAYS_ON_IP

		[Obsolete("v2.1 - no longer used")]
		public const string MAXIMUM_TIME_CONNECT_CISCO_ACCESS_SERVER = "b10dffa5c7bc4ecea09d782a13096066";	//v2.1 - no longer used

		[Obsolete("v2.2 - no longer used")]
        public const string APP_LVL_AUTH_CHALLENGE_DATA = "187cd1eaed4842a1ac4fcd4238170753";	//v2.2 - no longer used

		[Obsolete("v2.2 - no longer used")]
		public const string SESSION_SEQUENCE_NUMBER = "03d1326b4d654436905d0c2fa74efd81";	//v2.2 - no longer used

		[Obsolete("v2.2 - no longer used")]
		public const string FTP_SEQUENCE_NUMBER = "fdbacd47f1214b88bd98a6d964c830ef";	//v2.2 - no longer used

		[Obsolete("v2.2 - replaced by DC1000_ADAPTER_SERVER_URL")]
		public const string DC1000_ADAPTER_LOCATION = "4cf38c4953034ce79ca6fabe98cd1fca";	//v2.2 - replaced by DC1000_ADAPTER_SERVER_URL

		[Obsolete("v2.2 - replaced by EVENT_RECEIVER_URL")]
		public const string EVENT_SOAP_CALL_URL = "7a3405d3ebe2486491cff11bcb7a622d";	//v2.2 - replaced by EVENT_RECEIVER_URL

		[Obsolete("v2.2 - replaced by EVENT_RECEIVER_NAMESPACE")]
		public const string EVENT_SOAP_CALL_NAMESPACE = "a0aa36cb53704f01940ea070933b70ff";	//v2.2 - replaced by EVENT_RECEIVER_NAMESPACE

		[Obsolete("v2.2 - repaced by WINDOWS_RAS_PHONEBOOK")]
		public const string WINDOWS_RAS_PHONE_BOOK = "D51D39D27E154fd39CD76995EB7E01C0";	//v2.2 - repaced by WINDOWS_RAS_PHONEBOOK

		[Obsolete("v2.3 - no longer used")]
		public const string LAST_METER_HANDLE_USED = "43e75bb5db724684a2e0b7b1c71c5ba9";	//v2.3 - no longer used

		[Obsolete("v2.3 - no longer used")]
		public const string LAST_PANORAMIX_DOMAIN_ID_USED = "cf2dc39053a44d3da35f3feb6bc0a1f8";	//v2.3 - no longer used

		[Obsolete("v2.3 - no longer used")]
		public const string CISCO_ACCESS_SERVER_LAST_DATE_TIME_CONNECTION_ATTEMPT = "392749bc81a94576ac65a1c32db9de27";	//v2.3 - no longer used

		[Obsolete("v2.3 - no longer used")]
		public const string REGISTRATION = "a064392b33a84dcb8a1b1e1fb2f5174d";	//v2.3 - no longer used

		[Obsolete("v3.0 - replaced by READ_DEVICE_LOCAL_DATE_TIME")]
		public const string READ_METER_LOCAL_DATE_TIME = "a0af9e54378043aea9a7512c5434089d";	//v3.0 - replaced by READ_DEVICE_LOCAL_DATE_TIME

        [Obsolete("v4 - no longer used")]
        public const string CISCO_ACCESS_SERVER_AUTHENTICATION_ENGINE_SERVER_SECRET = "BC79C2A6AA5D4046A6E18A82E0F64C66"; // v4 - no longer used

        [Obsolete("v4 - no longer used")]
        public const string CISCO_ACCESS_SERVER_DIALER_GROUP_NAME = "EC8ABF2C85ED4fd8B1CF5D2465DFC4A4"; // v4 - no longer used

        [Obsolete("v4 - no longer used")]
        public const string ORPHAN_CHECK_TIME_CISCO_ACCESS_SERVER_ADAPTER_CONNECTED = "0839026f00b4490b89f7a3987c8c7e3d"; // v4 - no longer used

        [Obsolete("v4 - no longer used")]
        public const string ORPHAN_CHECK_TIME_CISCO_ACCESS_SERVER_ADAPTER_PENDING = "753956dcc6c84a07b4f38acc2c013613"; // v4 - no longer used

        [Obsolete("v4 - no longer used")]
        public const string TIME_WAIT_CONNECT_RETRY_CISCO_ACCESS_SERVER_ADAPTER = "2ad853ac737a441597ff4750b1012632"; // v4 - no longer used

        [Obsolete("v4 - no longer used")]
        public const string CISCO_ACCESS_SERVER_CONNECTION_DELAY = "e4078ed3809f4cf595f978eeb7c7cb98"; // v4 - no longer used

        [Obsolete("v4 - no longer used")]
        public const string DC1000_REFUSAL_PORT = "ecfc4369cfd946729e48b41087e7bc29"; // v4 - no longer used

        [Obsolete("v4.1 - no longer used")]
        public const string WINDOWS_RAS_PHONEBOOK = "D51D39D27E154fd39CD76995EB7E01C0";

        [Obsolete("v4.1 - no longer used")]
        public const string ORPHAN_CHECK_TIME_RAS_CONNECTED = "55f6a3f38d9240a5a422760b23ec32a2";

        [Obsolete("v4.1 - no longer used")]
        public const string ORPHAN_CHECK_TIME_RAS_PENDING = "ca968778d96a44799df4b03e475e1e91";

        [Obsolete("v4.1 - no longer used")]
        public const string TIME_WAIT_CONNECT_RETRY_RAS = "54EC511FE20E4bffBE055922087B354B";

        [Obsolete("v4.1 - no longer used")]
        public const string DISCONNECT_DELAY_RAS = "95a4b77e7eca4947a4796648ab231812";

		[Obsolete("v4.1 - no longer used")]
		public const string DC1000_RESOURCE_READ_COUNT = "72b14bb18d25435eb12b11ef7a87bb08";

		[Obsolete("v4.1 - no longer used")]
		public const string DC1000_DEVICE_RESOURCE_READ_COUNT = "feb8698552474dceb2fbd71f6f0b7560";

		[Obsolete("v4.1 - no longer used")]
		public const string DC1000_MAX_OUTSTANDING_DCXP_COMMANDS = "FEC89CF21289495eA455F428327784CA";

		[Obsolete("5.0 - replaced by SYSTEM_CONFIGURATION_FILE_UPDATE_DELAY")]
		public const string DATAMANAGER_INIT_UPDATE_DELAY = "224b3c46eceb43d588f556e26bfad2ae";

        [Obsolete("5.0 - no longer used")]
        public const string ILON100_DATALOG_MAX_UPLOAD_SESSION_TIMEOUT = "09AC43CA44A044b188384AE033913ED1";

        [Obsolete("5.0 - no longer used")]
        public const string ILON100_WEB_SERVICE_LOCATION = "9f2a946206ff47dbbf37047ac6b074e4";

        [Obsolete("5.0 - no longer used")]
        public const string ILON100_WEB_SERVICE_PORT = "43e382a7a2754960a6156daccb328f82";

        [Obsolete("5.1 - no longer used")]
        public const string AGGREGATION_SQL_QUERY_TIMEOUT_PERIOD = "984CCB5060724e57AA6E4161985F69E4";

        [Obsolete("5.1 - no longer used")]
        public const string DATALOG_SOAP_READ_COUNT = "ca2bd557f2ab46aca620f986326157b4";

        [Obsolete("5.1 - no longer used")]
        public const string SOLUTION_NAME = "DDB91D53C6044f4eA617D2C274B39B89";

        [Obsolete("5.1 - no longer used")]
        public const string SUMMARY_AGGREGATION_CALCULATION_HOURS = "8FB08B60BD594ac29963C56A24FD71AA";

        [Obsolete("5.1 - no longer used")]
        public const string SUMMARY_GATEWAYS_HOURS = "EF6115D7EDCA43ac8221DF076FE9CD44";

        [Obsolete("5.1 - no longer used")]
        public const string SUMMARY_KEY_DATA_POINT_IDS = "81cc4f8a18e64b308d0f0dd258cb1d21";

        [Obsolete("5.1 - no longer used")]
        public const string TCPIP_SOCKET_MESSAGE_TIMEOUT_PERIOD = "94417108619144a2881510CC5E4A8458";

        [Obsolete("5.1 - no longer used")]
        public const string TIME_WAIT_DEPENDENT_TASK_NOT_FOUND = "38bbc28186104981a359b3c18e7e6a2a";

        [Obsolete("5.1 - no longer used")]
        public const string AGGREGATION_SOAP_CALL_TIMEOUT_PERIOD = "7E9C00D0C8664d3eA4C3E5500A21F6C0";

		[Obsolete("v5.3 - no replacement, no longer used")]
		public const string GATEWAY_REPLACEMENT_DEFAULT_CONTROLLER_CONFIGURATION_FILE_TYPES = "a1251956dd874f78bdda4978b72d3989";

		[Obsolete("v5.3 - no replacement, no longer used")]
		public const string HISTORICAL_GATEWAY_CONTROLLER_CONFIGURATION_FILE_TYPE_LIMIT = "c4ac487a642b4d1a9bd791ac0ca8a24e";

        ////////////////////////////////////////////////////////////////////////////////////////
	}

	// List of types of solution settings supported
	public class SolutionSettingValueTypes
	{
		public const string SETTING_NUMERIC = "129ce55698fa498cb9209becf83e2bcc";
		public const string SETTING_STRING = "72a077b7c1fc441cbe96552634fb2dbe";
		public const string SETTING_EMAIL = "fa7402aaf48d465091a60f16e13dce1b";
		public const string ENCRYPTED_STRING = "eb59d1351cbf4560a9cec2ccac802aca";
	}

	public class DeviceDisconnectPriorityLevel
	{
		public const int LOW = 0;
		public const int HIGH = 1;
	}

	// List of the statuses for a device
	public class DeviceStatus
	{
		public const string ENABLED = "F33C5299FCC14a6198230E1CA7B97AED";
		public const string ADD_PENDING = "5E96BEFC1BFD4c6cA42CE2F9BF86FA37";
		public const string REMOVE_PENDING = "34c9333f91984faf9369aed0fb8cf09d";
        public const string COMMISSIONED = "5139ad9f8e5c444b9c7815d0aa987655";
        public const string ADD_FAILED = "3d6d78d870324100bb8dd4e7ef51d5fd";

        ////////////////////////////////////////////////////////////////////////////////////////
        //The following are deprecated - the version in which they were deprecated and the    //
        //constant replacing them (if any) is listed to the right of the constant.            //
        ////////////////////////////////////////////////////////////////////////////////////////

        [Obsolete("v4 - no longer used")]
        public const string FIRMWARE_UPDATE_PENDING = "6BF14C06561E4300923AB74877EB324A";
	}

	// List of the status for Gateway Communication History
	public class GatewayCommunicationHistoryStatus
	{
        public const string ERROR = "6814825A626A46aaAA7F6AE8252AAA08";
        public const string SOAP_FAILED = "2603FA0E27DD401fAB42FBBBB4281072";
        public const string CONNECTED = "7E58E836F4A24535BEE97404B4E519B0";
        public const string PENDING = "fc3f906abbe04110bd760e32fa7e5994";
        public const string COMPLETE = "020f3a24af6f4f30bb7446e0147e7438";
        public const string TIMED_OUT = "E7B8217D9A5742C4A2787AABF4073DF0";
	}

	// List of the statuses for Gateways
	public class GatewayStatus
	{
        public const string ENABLED = "6F9EFDF055C04031947FAE0C65814C78";
		public const string DISABLED = "D1F878A4F5594dd0886B31C9CDAF62CC";

        ////////////////////////////////////////////////////////////////////////////////////////
        //The following are deprecated - the version in which they were deprecated and the    //
        //constant replacing them (if any) is listed to the right of the constant.            //
        ////////////////////////////////////////////////////////////////////////////////////////

        [Obsolete("v4 - no longer used")]
        public const string FIRMWARE_UPDATE_PENDING = "45B20F36413F4f57BE474C19C3054ABA";
	}


	//List of the Gateways_iLON100 Retrieval types
	public class DataLogRetrievalMethodTypeID
	{
		public const string FTP_THEN_SOAP = "51F3232443974c1c9316EA4B8AB2D8F2";
        public const string SOAP_ONLY = "049D074B92F1417b9407F840722E3839";
	}

	// List of statuses for Schedule Assignments
	public class ScheduleAssignmentStatus
	{
		public const string ENABLED = "D256F7755E044cdf832631C3BF026CF4";
		public const string DISABLED = "4F70DCA609D34e099D0310BBA025C381";
	}

	// List of statuses for Schedules
	public class ScheduleStatus
	{
		public const string ENABLED = "1621F0AB77604f8c923A553AAC2076BD";
		public const string DISABLED = "C2E734072C574d08A67FABB6E5D000A0";
	}

	// List of schedule recurrences
	public class ScheduleRecurrenceTypes
	{
		public const string MINUTE = "C46C46455C77431aBE570936306DC4A5";
		public const string HOUR = "E606F3057B68493bB39B2BC27592BDAF";
		public const string DAY = "86206BCE754B414eBAF7BFC2900667DC";
		public const string WEEK = "319DD42D25A1405a94B3DADDE34CABC1";
		public const string MONTH = "E8E946937A464c1b81F4E2C441E9A107";
		public const string YEAR = "1C583DD1E1C244528D30714A8144F532";
		public const string NONE = "082BC98D245E44aa9FB821E49503D20F";
	}

	// List of archive setting expired interval types
	public class ArchiveSettingsExpiredIntervalTypes
	{
		public const string MINUTE = "0083AF706B93468c96C066F0CE5869C3";
		public const string HOUR = "794BE94A059D4bce8DC93AEE88E9AE79";
		public const string DAY = "477AE70FB6984f658187575572696A07";
	}

	// List of schedule timeout recurrences
	public class ScheduleTimeoutIntervalTypes
	{
		public const string MINUTE = "BC661FDB62074409A2B33E96BC8CBD7C";
		public const string HOUR = "5D5E0B604162464d8ED018691BB64CA6";
		public const string DAY = "4478D5FAC7684b6fBF022C9B8908F51E";
	}

	// List of types of scheduled tasks that are pending
	public class ScheduledTasksPendingStatus
	{
		public const string AVAILABLE = "B9DFAF8E5A3B4cd098F33422AED35734";
		public const string PROCESSING = "6A71A54D26B64efb8DED405B8925D385";
		public const string WAITING = "1D186DA2DB58484697F1A87A360D8083";
        public const string PAUSED = "f99912a32e344496a7750e1614084e26";
	}

	// List of types of Gateways
	public class GatewayTypes
	{
		public const string DC1000 = "7428ddbc573941f683c28212f8a0a746";
	}

	public class NotSupportedGatewayTypes
	{
		public const string ILON100 = "f4ff86c4263c433ca8352b965985b9f0";
	}

	// List of reserved ID's in the solution
	public class ReservedIDs
	{
		public const string UNASSIGNED_HIERARCHY_LEVEL_MEMBER_ID = "00f2a36c09c244ac87e90f7f0e83878e";
		public const string UNASSIGNED_ATTRIBUTE_ID = "7a934e71dc574795a12577db22ee86d3";
	}

	// List of options for searching hierarchies
	public class HierarchySearchOptions
	{
		//To get all entities in the selected level member only
		public const string EQUAL = "c0413dbb7e0b4d15a3a09f7e0efda93d";
		//To get all entities in the selected level members and all its children
		public const string EQUALORUNDER = "549515bbb3644027a7a56ef4d352bedd";
	}

	// List of types of data point sources
	public class DataPointSourceTypeID
	{
		public const string RAW = "FD240DE7307B4f9eBFAB29EAB5AB9E0E";
		public const string AGGREGATE = "5281F3BAE4B54b569AFF27B43421007A";
	}

	// List of types of restrictions for data points
	public class DataPointRestrictionTypes
	{
		public const string ONLY_DATAPOINTS_WITH_VALUES = "A00DEFC923084f6fAF177F31E181AA93";
		public const string ALL_DATAPOINTS = "E0E720CB95EA46c49463316A6FED4705";
	}

	// List of types of expressions
	public class ExpressionTypes
	{
		public const string ACTUAL = "7F5955E03D09451485047DE7870716B5";
		public const string INTENT = "7AC5339ADEF1406987F7CB9427F6C4B9";
		public const string DEPENDENCY = "1E3AEE3CAE0743748FD923C682353FF3";
	}

	// List of options for deleting HierarchyLevelMembers
	public class HierarchyLevelMemberDeleteTypes
	{
		public const string AUTO_REASSIGN_CHILDREN = "04235c93ce3e4bbabd93a0fddcea730a";
		public const string DELETE_DESCENDENTS = "0c0f514eaf824097881899b12ded5fca";
		public const string MOVE_CHILDREN_TO_NEW_PARENT = "46e53e63e29d4d86b671ec733322f329";
	}

	public class DataPointCalculationTypes
	{
		public const string IGNORE_DEPENDENCIES = "A22ABBCD0F2940988E1CD0E1181A07B5";
		public const string WAIT_ON_DEPENDENCIES = "C10F8A4502BF43789229673853707948";
		public const string FAIL_ON_DEPENDENCIES = "C24C9CFC98004ca69F8FFB3F240BE149";
	}

	// List of user authentication types supported
	public class UserAuthenticationTypes
	{
		public const string DEFAULT = "C8D75848B3A9441f8B0165EF75554C0D";
	}

	public class TaskPriorities
	{
		public const int LOW = 10;
		public const int MEDIUM = 5;
		public const int MEDIUM_TO_MEDIUM_HIGH = 4;
		public const int MEDIUM_HIGH = 3;
		public const int HIGH = 1;
	}

    public class ImportDataTypes
    {
        public const string ECHELON_CREATED_IMPORT_FILE = "e9c20760feda448fb27947baaa3dcd95";
        public const string NES_SYSTEM_SOFTWARE_EXPORT_RESULT = "dbfaa690e6904f7e82f273cb2d8bce12";
		public const string PROVISIONING_TOOL_CREATED_DATA_CONCENTRATOR_FILE = "634ed91bf3c645028b1b612bfe9d2e62";
    }

    public class ImportProcessingCompletionStatusTypes
    {
        public const string SUCCESS = "b544a3914d3944f2a1e5948991456032";
        public const string PARTIAL_SUCCESS = "e80ec5f617de45dba3c76798e97ea647";
        public const string FAILURE = "9c9f8d6864d54279a3c410b84258bf5e";
        public const string UNEXPECTED_FAILURE = "b6af6360170c4aecbe16d93dd226b1b4";
    }

	//List of gateway communication types
	public class GatewayCommunicationTypes
	{
		public const string ALWAYS_ON_IP = "3c8f6832eceb41deb0834c707af9ad3e";
	}

	public class NotSupportedGatewayCommunicationTypes
	{
		public const string CISCO_ACCESS_SERVER = "51013344C14245dc8E010B3132DE6DEC";
		public const string PPP_WINDOWS_RAS = "1b7d87c1405d4620b9538754b86867c2";
	}

	//List of gateway template types
	public class GatewayTemplateTypes
	{
		public const string TEMPLATE_ONLY = "aacfbb5418924690958b826cede639c7";
		public const string GATEWAY_AND_TEMPLATE = "afc0a772dac647faa4db9c541a38de4f";
		public const string GATEWAY_ONLY = "d9d8596d0af04b3083822a4bf6d9be43";
	}

	public class IDTypes
	{
		public const string GATEWAY_ID = "444b729f4d7a4b5e90f5296e7c62ed78";
		public const string TRANSFORMER_ID = "0aadee3706ce479a9fbb769b91ee7cf9";
		public const string NEURON_ID = "85a93675602b49a5a3ecbc7b66a513db";
		public const string DEVICE_ID = "7bf34c6d91f64ff4904411b72874ddf9";
		public const string SERIAL_NUMBER = "8448b06de2f34e838914d50021053ee8";
        public const string NAME = "f76e5d708edd464593bb031f646990b6";
	}

	//List of device types
	public class DeviceTypes
	{
		public const string METER = "fdfac94660b04fdcbdfc399cbb2c743d";
		public const string MBUS = "ec40abc46a524f478f67f0823e38a558";
        public const string MEP = "0b664d3e5a12405f9417ed20e025a2cd";

		//public const string ILON100 = "7e31e58fac69427caa8305bb40158c17";
	}

	public class EntityTypes
	{
		public const string DATAPOINT = "2f3e88220d0f4deb9de60e545e80d414";
		public const string DEVICE = "da18b9cdd06e4d2ea31439625431b775";
		public const string EVENT = "5cf615c6be054f0f890f743c64a42d85";
		public const string GATEWAY = "6a66c9e80bc64b1e918c65c1787c1e3c";
		public const string ENGINE = "dbe17830518a48e18904322e8d22506f";
		public const string SETTING = "dbf2f9b39b4e4dd9b504fe5d5a4196d9";
		public const string SCHEDULE_ASSIGNMENT = "E3F076689EB84094B6A06FC71EF81214";
		public const string SCHEDULE = "8800D8DB1841441cB15A3FD1F3D24B52";
        public const string SYSTEM = "977b7e2785f049889321fc07646339e9";
    }

	public class GatewayCommunicationRequestTypes
	{
		public const string SERVER_INITIATED_HIGH_PRIORITY = "8262edfc771547a2ad4467e97b2c5479";
		public const string SERVER_INITIATED_NORMAL_PRIORITY = "ebcec8adecb142c48fb3951818db1fc4";
		public const string GATEWAY_INITIATED = "a6fbe085fba047a3ad7a1cfcbcd86227";
	}

	public class CommandHistoryStatus
	{
		public const string SUCCESS = "b43637dc8ddb487aabbcf9752556762f";
		public const string FAILURE = "28b70caf8360420982f4d1ebac66a39c";
		public const string WAITING = "3dc39216a0a64a19974826f3fec44368";
		public const string DELETED = "a796f572761a4ea6b3a6cab751a4e8e7";
		public const string IN_PROGRESS = "7f31e3d98cfb478f8155d3851053a607";
		public const string CANCELLED = "FA74ABC8031F4ffb9F0C37A9720BB249";
        public const string ORPHANED = "45e31fee41cb48d6b615fd99935db48b";
	}

	public class GatewayDataAvailableTypes
	{
		public const string DISCOVERED_DEVICES = "44d5344152694179a78c482e6d81ca7e";
        public const string EVENTS = "2496a6cbfcb24269ab21ffc1e311a372";
        public const string COLLECTED_DATA = "bcdc43b6050f4a76ae3fc852bc06e46e";
        public const string COMMAND_RESULTS = "3223bcc8485843268c2e92dc63b23652";
	}

	public class DeviceCommands
	{
		public const string CONNECT_LOAD = "8ff7f8145166477a86afebd1052682ab";
		public const string DISCONNECT_LOAD = "55c9a443124e42308681eb8ebe17c067";
		public const string READ_FULL_LOAD_PROFILE = "c060ae65f0a24a74a3aa783e4820b8ad";
		public const string READ_POWER_QUALITY = "efc57978bcd6481db4df5e267340248d";
		public const string READ_BILLING_DATA_ON_DEMAND = "876d72f4ae384434a87b61c095ca9037";
		public const string READ_SELF_BILLING_DATA = "06790b8c487c41e5a9a2466309fee4c3";
		public const string READ_SECONDARY_BILLING_REGISTERS = "141da4844e5e44d8a1961506d25ddf28";
		public const string CONNECT_CONTROL_RELAY = "971dc6e881a14317a5ae4936fdf2734f";
		public const string DISCONNECT_CONTROL_RELAY = "5137fd076f6c4be69da39c1928d1251d";
		public const string SET_LOAD_PROFILE_CONFIGURATION = "ce7242bd9b7a4824a3e4ab390b1de400";
        public const string READ_DELTA_LOAD_PROFILE = "640998221D7545a1ABED17D67008831E";
		public const string READ_CONTINUOUS_DELTA_LOAD_PROFILE = "7114127AD12B4854A7487B6A617866B0";
		public const string UPDATE_FIRMWARE = "4DFE4EF6120144fcBB472986848D3C25";
		public const string READ_DISPLAY_CONFIGURATION = "A125838270E94e70AAF8AD5997219F66";
		public const string SET_DISPLAY_CONFIGURATION = "ADCBF99F87E04c4bBA1D8B268892728F";
		public const string SET_ALARM_DISPLAY_CONFIGURATION = "2582A29F0CE3480590FC9E3A58E9B739";
		public const string READ_PULSE_INPUT_CONFIGURATION = "125875c4075d4673840cf3e85140eaae";
		public const string READ_INSTANTANEOUS_POWER = "929174F4A6864c00853CA07C25F21A93";
		public const string READ_CONTROL_RELAY = "a1b3ecb71d4c4eaa914b8cb1b4e4809a";
		public const string READ_LOAD_STATUS = "00ee66bae6ec4e6e94c474031f9ad171";
		public const string READ_FIRMWARE_VERSION = "38bf1957518e4b85888634a62534f6e6";
		public const string SET_PULSE_INPUT_CONFIGURATION = "928b63e248874c0d8992f80189f90fe6";
		public const string READ_ACTIVE_TOU_CALENDAR = "851007C70FF344799DE53FA72FCC2FFD";
		public const string READ_PENDING_TOU_CALENDAR = "9169C4DC6417453cA8A5C439537AE038";
		public const string SET_PENDING_TOU_CALENDAR = "93C5CEDA5BE444d0B277E3CB77C64426";

		// V2.1 commands
		public const string READ_DOWN_LIMIT = "7546196b682e409e8929cc099ab8764a";
		public const string SET_DOWN_LIMIT = "3850dc63144846cab9cd4f0478c69c49";
		public const string SET_DATE_TIME = "bfdd8bdc648247febeb8876ce5aaf184";
		public const string READ_HISTORICAL_BILLING_DATA = "37c6c1b2c957439d9b9ef2bf7473e747";

		// v2.2 Commands
		public const string READ_ALARM_POLLING_RATE = "4360a6993e2a480dae781f9e7830f367";
		public const string SET_ALARM_POLLING_RATE = "9720799680614c4abf305eeabc06be94";
		public const string READ_BILLING_SCHEDULE = "3E838D9FD4AE42318D1C4F2C2C571C1F";
		public const string SET_BILLING_SCHEDULE = "0F5CD7D9B7E145828AD50174D7BE1D87";

		public const string READ_STATISTICS = "39d7f2bbce2443cebafc664a169d3163";
		public const string READ_AUTO_DISCOVERY_CONFIGURATION = "fb4bc6d5429846778601aa7b8c40fcb2";
		public const string SET_AUTO_DISCOVERY_CONFIGURATION = "46c63c1745594e118138e1eb9a7f7a47";
		public const string READ_EVENT_LOG = "3514e14b1e9c465cb7a69a329a47db3d";
		public const string READ_UTILITY_INFORMATION = "ffb823d7cbb7481388be415b8604ba7c";
		public const string SET_UTILITY_INFORMATION = "a8a9bbd9b2cd43b0814bbc64b0e59208";

		// V2.3 Commands
		public const string ADD_PREPAY_CREDIT = "3A7BB83EDB714856B9D3532D4BC5485C";
		public const string CLEAR_PREPAY_CREDIT = "B8485CC3630B40a99142D80581E2C1E4";
		public const string READ_PREPAY_CONFIGURATION = "652E05E87D864a488CAF502C6718DEF0";
		public const string SET_PREPAY_CONFIGURATION = "CA90E67E311A4c95891D7CF8A9F4AB18";
		public const string READ_TIME_ZONE_CONFIGURATION = "229f94ce3bdb47da98288a08acaf1a50";
		public const string SET_TIME_ZONE_CONFIGURATION = "345649807dc04c758ca4a795d03f98ee";
		public const string READ_PREPAY_CREDIT = "da5e63c3e2934be48ffd3da30d8600c6";

		//V3.0 Commands
		public const string READ_DISCONNECT_CONFIGURATION = "834f15a5532f4cfc97fca3b295a7cf82";
		public const string SET_DISCONNECT_CONFIGURATION = "d06a616f944d4e6db8e477c865819c8c";
		public const string WRITE_DATA = "792be923a6eb4984b82be9332a729f03";
        public const string READ_CONTINUOUS_EVENT_LOG_CONFIGURATION = "b00a410c225746e58ba27fe300fa709b";
        public const string SET_CONTINUOUS_EVENT_LOG_CONFIGURATION = "da6c0e20693f42c69da29ab7d7ec75b5";
        public const string READ_STOP_MODE_CONFIGURATION = "ca7cc3e8f3e545efb1cc38ccf563b7b2";
        public const string SET_STOP_MODE_CONFIGURATION = "69ab75debd4d4505b1a67a7647444cfa";
        public const string READ_POWER_QUALITY_CONFIGURATION = "df5e4eb3e46547e889613eb21103aa75";
        public const string SET_POWER_QUALITY_CONFIGURATION = "68a0dee787044a98a62318b2e5b4cdc6";
        public const string RESET_POWER_QUALITY_STATISTICS = "55d5cde1efa147f795326cf1a4d72c87";
        public const string READ_SELF_READ_RETRIEVAL_CONFIGURATION = "f543e76f3c38479f9ef6b2916050c032";
        public const string SET_SELF_READ_RETRIEVAL_CONFIGURATION = "98f0cebef4e64feaa2b6a3cc74c0ded3";
	    public const string ADD_ONE_TIME_READ_REQUEST = "4badb2180fbd4680abc7c0fc588863a1";
        public const string CLEAR_ONE_TIME_READ_REQUESTS = "30b43c78d8f1407fb970dc47c3a8146e";
        public const string READ_HISTORICAL_ONE_TIME_READ_DATA = "9c7ec36006c140b7a551a8624a8a4807";
        public const string READ_ONE_TIME_READ_REQUESTS = "c7c86583f7564329878f8895018cdec3";
        public const string RESET_DEMAND_VALUES = "13ab688e67174948931c8ab43b5a7509";
		public const string READ_CONTINUOUS_DELTA_LOAD_PROFILE_COLLECTION_CONFIGURATION = "3684d3368e9548058137ea90ea3dafd1";
		public const string SET_CONTINUOUS_DELTA_LOAD_PROFILE_COLLECTION_CONFIGURATION = "9cbd0f851b154af1baa356ec16252b59";
        public const string READ_DEMAND_CONFIGURATION = "31775176C9404d9b9510D2C8A6238872";
        public const string SET_DEMAND_CONFIGURATION = "5990FE6CFBC8499cA7038EA70BB6A0DC";
        public const string READ_LOAD_PROFILE_CONFIGURATION = "6a3f040e71d74803a9dd2e161c7424cc";
		public const string READ_LOAD_PROFILE_ON_DEMAND = "60e3b79064f84e74952e929e782b94ae";
        public const string READ_EVENT_LOG_CONFIGURATION = "a5412d516e7b460d85f1b7d45214125f";
        public const string SET_EVENT_LOG_CONFIGURATION = "dd7b915d3c9b4a02a67f7fd4a7f1a8cc";

        //V4 Commands
        public const string READ_ATM_CONFIGURATION = "d67255060c524f12ac30005b82a81a20";
        public const string SET_ATM_CONFIGURATION = "434ec5d054d24366a3b807a0397417a1";
		public const string READ_READ_SCHEDULE = "ebbaf22b74d84751a9acde75e5d4cd33";
		public const string SET_READ_SCHEDULE = "721147cd4e564481ad3da505c239a835";
        public const string READ_DATA_ON_DEMAND = "7f8b420e24864dcdb671fe3728a8d318";
        public const string READ_PROVISIONING_IDENTIFIERS = "19906644fe7740fc959fb606a9ba0903";
        public const string SET_PROVISIONING_IDENTIFIERS = "6fe33cb297654cb1a5949e9383dd2b9a";

		//V4.1 Commands
		public const string SET_CONTROL_RELAY_CONFIGURATION = "1160f35a4c004f6b808e3f94ffde3757";
		public const string READ_CONTROL_RELAY_CONFIGURATION = "3851673b37b04667b931a5059d186fe5";
        public const string READ_DATA_RECORD_HEADER_CONFIGURATION = "84f40927f66f4c4d9b4ecfa3aa0c79d2";
        public const string SET_DATA_RECORD_HEADER_CONFIGURATION = "34d1818107b046febf3df98f5c682502";
        public const string READ_DATA_RECORD_HEADER_MAPPING = "4792cb5b7e744a0ab4298841f006aa69";
        public const string SET_DATA_RECORD_HEADER_MAPPING = "6b9415c21f3a4c3a84320adf5a4e3cf4";
        public const string READ_TIER_CONTROL_CONFIGURATION = "57345ddff93a458e974db9e24cfc575f";
        public const string SET_TIER_CONTROL_CONFIGURATION = "6944ca88fc404177880e8a4c0aa40872";
		public const string SEND_FILE = "5349ada980d9400bb9d49828542ac1bd";

        //V5 Commands
        public const string READ_LOCAL_DATA_ACCESS_CONFIGURATION = "9dadd1a3cf6d4e4d8dd83d8fc0a95f90";
        public const string SET_LOCAL_DATA_ACCESS_CONFIGURATION = "2fffc80354ef48168481d4895313d77d";
		public const string SET_MEASUREMENT_RATIO_CONFIGURATION = "444f07a00d5045b3928f1024a6bc3e16";
		public const string READ_MEASUREMENT_RATIO_CONFIGURATION = "122dc2270d6c4440a180033a098096a5";
        public const string SET_BROADCAST_GROUP_MEMBERSHIP_CONFIGURATION = "6a0e935770c74daf977ab512dbd505bf";
        public const string READ_BROADCAST_GROUP_MEMBERSHIP_CONFIGURATION = "d99a4107f50c43139466eed77742ad96";

		//V5.1 Commands
		public const string READ_MEMORY_CONFIGURATION = "f2bb3117f61043ada0f1de25a3ecbf71";
		public const string SET_MEMORY_CONFIGURATION = "9625740077a94b2e999f9c1d9d1d60a1";
		public const string READ_LOAD_PROFILE_DATA_SET_COLLECTION_CONFIGURATION = "b392c69291fe4b26b57650bef6862bae";
		public const string SET_LOAD_PROFILE_DATA_SET_COLLECTION_CONFIGURATION = "293e0ca895a641f8a6c0d7f0cb6b8b3d";
		public const string READ_GATEWAY_SETTINGS = "c875d001dfdf4783845b749a6f607d9e";
		public const string SET_GATEWAY_SETTINGS = "6f82e4e446a341c6828590f2d590969e";
		public const string READ_MCN_CONFIGURATION = "b0d8507ed48c4e9691b7663c7d1f06f2";
		public const string SET_MCN_CONFIGURATION = "42c97c79399345ebba57886187780c36";

        //V5.2 Commands
        public const string READ_POWER_STATUS = "9D615237F67A4A97AD0C2E7CDE0053E7";
		public const string READ_ALARM_SETTINGS = "d9c1e84268244abfa5fa8437783e9ab9";
		public const string SET_ALARM_SETTINGS = "9776004c74eb43f0817105b9943df132";
		public const string READ_DISCONNECT_THRESHOLDS_CONFIGURATION = "bc269392cb7743a0b70da1cd7f702b03";
		public const string SET_DISCONNECT_THRESHOLDS_CONFIGURATION = "29cd80efcf8349f68fec4011c0174414";

		//v5.3 Commands
		public const string READ_TAMPER_CONFIGURATION = "be543769163442c6bbfdd01db510b2d2";
		public const string SET_TAMPER_CONFIGURATION = "0ebddc0af4af4928aa15a5ab62dcb8cd";
        public const string READ_MEP_CONFIGURATION = "9a80ca106c9747eb8e606da3aa3e52d0";
        public const string SET_MEP_CONFIGURATION = "ba1b6bbe2a884c2aba33ac1c14fc4310";
		public const string SET_KEY = "76f6162c876449f082c59b8dae3af5d7";
		public const string READ_REGISTER_CONFIGURATION = "9fdb073b722c4073b1a814ac9020b4b4";
		public const string SET_REGISTER_CONFIGURATION = "301d2a92c7e04dc2a32001f160e3ac00";
        public const string READ_OPTICAL_PORT_CONFIGURATION = "4b149183509247c488d1837189041bad";
        public const string SET_OPTICAL_PORT_CONFIGURATION = "5888af1d6acf4e44b5bfabeff00117f4";

		////////////////////////////////////////////////////////////////////////////////////////
		//The following are deprecated - the version in which they were deprecated and the    //
		//constant replacing them (if any) is listed to the right of the constant.            //
		////////////////////////////////////////////////////////////////////////////////////////

		[Obsolete("v2.1 - use CONNECT_LOAD")]
		public const string REMOTE_METER_CONNECT = "8ff7f8145166477a86afebd1052682ab"; //v2.1 - use CONNECT_LOAD

		[Obsolete("v2.1 - use DISCONNECT_LOAD")]
		public const string REMOTE_METER_DISCONNECT = "55c9a443124e42308681eb8ebe17c067"; //v2.1 - use DISCONNECT_LOAD

		[Obsolete("v2.1 - use READ_FULL_LOAD_PROFILE")]
		public const string READ_METER_LOAD_PROFILE = "c060ae65f0a24a74a3aa783e4820b8ad"; //v2.1 - use READ_FULL_LOAD_PROFILE

		[Obsolete("v2.1 - use READ_BILLING_DATA_ON_DEMAND")]
		public const string READ_BILLING_DATA = "876d72f4ae384434a87b61c095ca9037"; //v2.1 - use READ_BILLING_DATA_ON_DEMAND

		[Obsolete("v2.1 - use SET_TOU_SEASON_SCHEDULES")]
		public const string SET_SEASON_SCHEDULES = "95B45DEEE1CC4f0dB945D439E9C245DF"; //v2.1 - use SET_TOU_SEASON_SCHEDULES

		[Obsolete("v2.1 - use SET_TOU_DAY_SCHEDULES")]
		public const string SET_DAY_SCHEDULES = "EDB47BBCDB5F44989CA460F736232D05"; //v2.1 - use SET_TOU_DAY_SCHEDULES

		[Obsolete("v2.1 - use READ_TOU_DAY_SCHEDULES")]
		public const string RETRIEVE_DAY_SCHEDULES = "36C6E601E4A94c21B819647ACFE5D6DA"; //v2.1 - use READ_TOU_DAY_SCHEDULES

		[Obsolete("v2.1 - use READ_TOU_SEASON_SCHEDULES")]
		public const string RETRIEVE_SEASON_SCHEDULES = "6BE158081CEC4444B1B519BAE6AB53BB"; //v2.1 - use READ_TOU_SEASON_SCHEDULES

		[Obsolete("v2.1 - use READ_TOU_RECURRING_DATES")]
		public const string RETRIEVE_TOU_RECURRING_DATES = "27E730D4120D483fB18E961A032EE9A2";//v2.1 - use READ_TOU_RECURRING_DATES

		[Obsolete("v2.1 - use SET_PRIMARY_MAXIMUM_POWER_STATUS")]
		public const string ENABLE_MAXIMUM_POWER = "8abfe06564bc4543b04198c9c792238a"; //v2.1 - use SET_PRIMARY_MAXIMUM_POWER_STATUS

		[Obsolete("v2.1 - use READ_SELF_BILLING_DATA")]
		public const string PERFORM_SELF_BILLING_READ = "06790b8c487c41e5a9a2466309fee4c3";	//v2.1 - use READ_SELF_BILLING_DATA

		[Obsolete("v2.1 - use CONNECT_CONTROL_RELAY")]
		public const string CLOSE_SECONDARY_CONTROL_OUTPUT = "971dc6e881a14317a5ae4936fdf2734f"; //v2.1 - use CONNECT_CONTROL_RELAY

		[Obsolete("v2.1 - use DISCONNECT_CONTROL_RELAY")]
		public const string OPEN_SECONDARY_CONTROL_OUTPUT = "5137fd076f6c4be69da39c1928d1251d";	//v2.1 - use DISCONNECT_CONTROL_RELAY

		[Obsolete("v2.1 - use SET_CONTROL_RELAY_TIERS_STATUS")]
		public const string ENABLE_SECONDARY_CONTROL_OUTPUT_TIERS = "44101289ac4749d4924fde9ffbda0edb";	//v2.1 - use SET_CONTROL_RELAY_TIERS_STATUS

		[Obsolete("v2.1 - use SET_CONTROL_RELAY_TIERS")]
		public const string SET_SECONDARY_CONTROL_OUTPUT_TIERS = "a47d6f51a1744654a06212116d05d3bb"; //v2.1 - use SET_CONTROL_RELAY_TIERS

		[Obsolete("v2.1 - use READ_POWER_QUALITY_OUTAGE_DURATION_THRESHOLD")]
		public const string READ_POWER_QUALITY_TIME_THRESHOLD = "D93F4FBEB72A44bdA2D7506B15DED8E2"; //v2.1 - use READ_POWER_QUALITY_OUTAGE_DURATION_THRESHOLD

		[Obsolete("v2.1 - use READ_POWER_QUALITY_SAG_VOLTAGE_PERCENT_THRESHOLD")]
		public const string READ_POWER_QUALITY_SAG_THRESHOLD = "8DFFC5DA18B54f90B18909D3186F5189"; //v2.1 - use READ_POWER_QUALITY_SAG_VOLTAGE_PERCENT_THRESHOLD

		[Obsolete("v2.1 - use READ_POWER_QUALITY_SURGE_VOLTAGE_PERCENT_THRESHOLD")]
		public const string READ_POWER_QUALITY_SURGE_THRESHOLD = "1F6A974893314fe0BC160A268A032F50"; //v2.1 - use READ_POWER_QUALITY_SURGE_VOLTAGE_PERCENT_THRESHOLD

		[Obsolete("v2.1 - use READ_POWER_QUALITY_OVERCURRENT_PERCENT_THRESHOLD")]
		public const string READ_POWER_QUALITY_OVERCURRENT_THRESHOLD = "34613E88E4254ea9AE1C516B10C97B7F"; //v2.1 - use READ_POWER_QUALITY_OVERCURRENT_PERCENT_THRESHOLD

		[Obsolete("v2.1 - use SET_POWER_QUALITY_OUTAGE_DURATION_THRESHOLD")]
		public const string SET_POWER_QUALITY_TIME_THRESHOLD = "6AC87E7BFFD34f7c853DDC28B41E93FB"; //v2.1 - use SET_POWER_QUALITY_OUTAGE_DURATION_THRESHOLD

		[Obsolete("v2.1 - use SET_POWER_QUALITY_SAG_VOLTAGE_PERCENT_THRESHOLD")]
		public const string SET_POWER_QUALITY_SAG_THRESHOLD = "96C130FE709F4598B58F92A69F3823F7"; //v2.1 - use SET_POWER_QUALITY_SAG_VOLTAGE_PERCENT_THRESHOLD

		[Obsolete("v2.1 - use SET_POWER_QUALITY_SURGE_VOLTAGE_PERCENT_THRESHOLD")]
		public const string SET_POWER_QUALITY_SURGE_THRESHOLD = "7F9B565530E84a1b87680B6CDC037E48"; //v2.1 - use SET_POWER_QUALITY_SURGE_VOLTAGE_PERCENT_THRESHOLD

		[Obsolete("v2.1 - use SET_POWER_QUALITY_OVERCURRENT_PERCENT_THRESHOLD")]
		public const string SET_POWER_QUALITY_OVERCURRENT_THRESHOLD = "42559B73860C440d8F5970F2BFF3D5D3"; //v2.1 - use SET_POWER_QUALITY_OVERCURRENT_PERCENT_THRESHOLD

		[Obsolete("v2.1 - use UPDATE_FIRMWARE")]
		public const string UPDATE_METER_FIRMWARE = "4DFE4EF6120144fcBB472986848D3C25"; //v2.1 - use UPDATE_FIRMWARE

		[Obsolete("v2.1 - use READ_DISPLAY_CONFIGURATION")]
		public const string READ_METER_DISPLAY_CONFIGURATION = "A125838270E94e70AAF8AD5997219F66";	//v2.1 - use READ_DISPLAY_CONFIGURATION

		[Obsolete("v2.1 - use SET_DISPLAY_CONFIGURATION")]
		public const string SET_METER_DISPLAY_CONFIGURATION = "ADCBF99F87E04c4bBA1D8B268892728F"; //v2.1 - use SET_DISPLAY_CONFIGURATION

		[Obsolete("v2.1 - use READ_PRIMARY_MAXIMUM_POWER_STATUS")]
		public const string READ_MAXIMUM_POWER_ENABLE = "F5A1A390BEF244cc8271A54D88AB2310"; //v2.1 - use READ_PRIMARY_MAXIMUM_POWER_STATUS

		[Obsolete("v2.1 - use READ_CONTROL_RELAY")]
		public const string READ_SECONDARY_CONTROL_OUTPUT_RELAY = "a1b3ecb71d4c4eaa914b8cb1b4e4809a"; //v2.1 - use READ_CONTROL_RELAY

		[Obsolete("v2.1 - use READ_LOAD_STATUS")]
		public const string READ_PRIMARY_CONTROL_OUTPUT = "00ee66bae6ec4e6e94c474031f9ad171"; //v2.1 - use READ_LOAD_STATUS

		[Obsolete("v2.1 - use READ_FIRMWARE_VERSION")]
		public const string READ_METER_FIRMWARE_VERSION = "38bf1957518e4b85888634a62534f6e6"; //v2.1 - use READ_FIRMWARE_VERSION

		[Obsolete("v2.1 - no longer used")]
		public const string GET_LOAD_PROFILES = "38A0EAD083E5489a9A8A1E50F6F84D4F"; //v2.1 - no longer used

		[Obsolete("v2.2 - not replaced as this command was not implemented yet in any version")]
		public const string READ_PREPAY_MAXIMUM_POWER_STATUS = "696bf1f6eeee44d2831ff41f09ed097f"; //v2.2 - not replaced as this command was not implemented yet in any version

		[Obsolete("v2.2 - not replaced as this command was not implemented yet in any version")]
		public const string SET_PREPAY_MAXIMUM_POWER_STATUS = "474045f96d1f4b9fb0e79e7ec9b454e5"; //v2.2 - not replaced as this command was not implemented yet in any version

		[Obsolete("v2.2 - not replaced as this command was not implemented yet in any version")]
		public const string READ_PREPAY_MAXIMUM_POWER_LEVEL = "d26849dfc53d4d208cdc64e46be42a45"; //v2.2 - not replaced as this command was not implemented yet in any version

		[Obsolete("v2.2 - not replaced as this command was not implemented yet in any version")]
		public const string SET_PREPAY_MAXIMUM_POWER_LEVEL = "3c69304af03f48bfa2d82f9fbcea9ce8"; //v2.2 - not replaced as this command was not implemented yet in any version

		[Obsolete("v2.3 - use SET_PRIMARY_MAXIMUM_POWER_LEVEL ")]
		public const string SET_MAXIMUM_POWER_LEVEL = "6a84f553420d412c932ac9945753d8e0"; //v2.3 - use SET_PRIMARY_MAXIMUM_POWER_LEVEL

		[Obsolete("v2.3 - use READ_PRIMARY_MAXIMUM_POWER_LEVEL ")]
		public const string READ_MAXIMUM_POWER_LEVEL = "cf9681d07e294b4195736362e7de80e0"; //v2.3 - use READ_PRIMARY_MAXIMUM_POWER_LEVEL

		[Obsolete("v2.3 - use SET_PRIMARY_MAXIMUM_POWER_STATUS ")]
		public const string SET_MAXIMUM_POWER_STATUS = "8abfe06564bc4543b04198c9c792238a"; //v2.3 - use SET_PRIMARY_MAXIMUM_POWER_STATUS

		[Obsolete("v2.3 - use READ_PRIMARY_MAXIMUM_POWER_STATUS ")]
		public const string READ_MAXIMUM_POWER_STATUS = "F5A1A390BEF244cc8271A54D88AB2310"; //v2.3 - use READ_PRIMARY_MAXIMUM_POWER_STATUS

        [Obsolete("v3.0 - use SET_PENDING_TOU_CALENDAR")]
        public const string SET_TOU_SEASON_SCHEDULES = "95B45DEEE1CC4f0dB945D439E9C245DF"; //v3.0 - use SET_PENDING_TOU_CALENDAR

        [Obsolete("v3.0 - use SET_PENDING_TOU_CALENDAR")]
        public const string SET_TOU_DAY_SCHEDULES = "EDB47BBCDB5F44989CA460F736232D05"; //v3.0 - use SET_PENDING_TOU_CALENDAR

        [Obsolete("v3.0 - use SET_PENDING_TOU_CALENDAR")]
        public const string SET_TOU_RECURRING_DATES = "CDBB8E08358F4488A2869D123976F86B"; //v3.0 - use SET_PENDING_TOU_CALENDAR

        [Obsolete("v3.0 - use READ_ACTIVE_TOU_CALENDAR")]
        public const string READ_TOU_DAY_SCHEDULES = "36C6E601E4A94c21B819647ACFE5D6DA"; //v3.0 - use READ_ACTIVE_TOU_CALENDAR

        [Obsolete("v3.0 - use READ_ACTIVE_TOU_CALENDAR")]
        public const string READ_TOU_SEASON_SCHEDULES = "6BE158081CEC4444B1B519BAE6AB53BB"; //v3.0 - use READ_ACTIVE_TOU_CALENDAR

        [Obsolete("v3.0 - use READ_ACTIVE_TOU_CALENDAR")]
        public const string READ_TOU_RECURRING_DATES = "27E730D4120D483fB18E961A032EE9A2"; //v3.0 - use READ_ACTIVE_TOU_CALENDAR

        [Obsolete("v3.0 - use READ_POWER_QUALITY_CONFIGURATION")]
        public const string READ_POWER_QUALITY_OUTAGE_DURATION_THRESHOLD = "D93F4FBEB72A44bdA2D7506B15DED8E2"; //v3.0 - use READ_POWER_QUALITY_CONFIGURATION

        [Obsolete("v3.0 - use READ_POWER_QUALITY_CONFIGURATION")]
        public const string READ_POWER_QUALITY_SAG_SURGE_DURATION_THRESHOLD = "91868BA3ABEE4f01A8DFCE625A6EBF22"; //v3.0 - use READ_POWER_QUALITY_CONFIGURATION

        [Obsolete("v3.0 - use READ_POWER_QUALITY_CONFIGURATION")]
        public const string READ_POWER_QUALITY_SAG_VOLTAGE_PERCENT_THRESHOLD = "8DFFC5DA18B54f90B18909D3186F5189"; //v3.0 - use READ_POWER_QUALITY_CONFIGURATION

        [Obsolete("v3.0 - use READ_POWER_QUALITY_CONFIGURATION")]
        public const string READ_POWER_QUALITY_SURGE_VOLTAGE_PERCENT_THRESHOLD = "1F6A974893314fe0BC160A268A032F50"; //v3.0 - use READ_POWER_QUALITY_CONFIGURATION

        [Obsolete("v3.0 - use READ_POWER_QUALITY_CONFIGURATION")]
        public const string READ_POWER_QUALITY_OVERCURRENT_PERCENT_THRESHOLD = "34613E88E4254ea9AE1C516B10C97B7F"; //v3.0 - use READ_POWER_QUALITY_CONFIGURATION

        [Obsolete("v3.0 - use SET_POWER_QUALITY_CONFIGURATION")]
        public const string SET_POWER_QUALITY_OUTAGE_DURATION_THRESHOLD = "6AC87E7BFFD34f7c853DDC28B41E93FB"; //v3.0 - use SET_POWER_QUALITY_CONFIGURATION

        [Obsolete("v3.0 - use SET_POWER_QUALITY_CONFIGURATION")]
        public const string SET_POWER_QUALITY_SAG_SURGE_DURATION_THRESHOLD = "C8D241398E8D4e1fB17928746402AD85"; //v3.0 - use SET_POWER_QUALITY_CONFIGURATION

        [Obsolete("v3.0 - use SET_POWER_QUALITY_CONFIGURATION")]
        public const string SET_POWER_QUALITY_SAG_VOLTAGE_PERCENT_THRESHOLD = "96C130FE709F4598B58F92A69F3823F7"; //v3.0 - use SET_POWER_QUALITY_CONFIGURATION

        [Obsolete("v3.0 - use SET_POWER_QUALITY_CONFIGURATION")]
        public const string SET_POWER_QUALITY_SURGE_VOLTAGE_PERCENT_THRESHOLD = "7F9B565530E84a1b87680B6CDC037E48"; //v3.0 - use SET_POWER_QUALITY_CONFIGURATION

        [Obsolete("v3.0 - use SET_POWER_QUALITY_CONFIGURATION")]
        public const string SET_POWER_QUALITY_OVERCURRENT_PERCENT_THRESHOLD = "42559B73860C440d8F5970F2BFF3D5D3"; //v3.0 - use SET_POWER_QUALITY_CONFIGURATION

        [Obsolete("v3.0 - use READ_INTERNAL_EXPANSION_MODULE_CARD_CONFIGURATION")]
        public const string READ_MEP_CARD_CONFIGURATION = "0f6583b94dfa442689e71e08b2956470"; //v3.0 - use READ_INTERNAL_EXPANSION_MODULE_CARD_CONFIGURATION

		[Obsolete("v4.1 - has been superseded by the SET_CONTROL_RELAY_CONFIGURATION command")]
		public const string SET_CONTROL_RELAY_TIERS_STATUS = "44101289ac4749d4924fde9ffbda0edb";

		[Obsolete("v4.1 - has been superseded by the SET_CONTROL_RELAY_CONFIGURATION command")]
		public const string SET_CONTROL_RELAY_TIERS = "a47d6f51a1744654a06212116d05d3bb";

		[Obsolete("v5.0 - no longer used")]
		public const string READ_INTERNAL_EXPANSION_MODULE_CARD_CONFIGURATION = "0f6583b94dfa442689e71e08b2956470"; //v5.0 - no replacement, no longer used

		[Obsolete("v5.2.000 - use SET_DISCONNECT_THRESHOLDS_CONFIGURATION")]
		public const string SET_PRIMARY_MAXIMUM_POWER_LEVEL = "6a84f553420d412c932ac9945753d8e0";
		[Obsolete("v5.2.000 - use SET_DISCONNECT_THRESHOLDS_CONFIGURATION")]
		public const string SET_MAXIMUM_POWER_LEVEL_DURATION = "c8a34bc2193e47baab32fdc809bec8a8";
		[Obsolete("v5.2.000 - use READ_DISCONNECT_THRESHOLDS_CONFIGURATION")]
		public const string READ_PRIMARY_MAXIMUM_POWER_LEVEL = "cf9681d07e294b4195736362e7de80e0";
		[Obsolete("v5.2.000 - use READ_DISCONNECT_THRESHOLDS_CONFIGURATION")]
		public const string READ_MAXIMUM_POWER_LEVEL_DURATION = "bc6ef513d5f3440baf6f25dc1405a93c";
		[Obsolete("v5.2.000 - use SET_DISCONNECT_THRESHOLDS_CONFIGURATION")]
		public const string SET_PRIMARY_MAXIMUM_POWER_STATUS = "8abfe06564bc4543b04198c9c792238a";
		[Obsolete("v5.2.000 - use READ_DISCONNECT_THRESHOLDS_CONFIGURATION")]
		public const string READ_PRIMARY_MAXIMUM_POWER_STATUS = "F5A1A390BEF244cc8271A54D88AB2310";
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	}

	public class GatewayCommands
	{
		public const string UPDATE_FIRMWARE = "23c147b71a91412488147ac6213bf7e3";
		public const string SET_PPP_USERNAME = "c45d90fb3cb943878db6fe5a4c5158be";
		public const string SET_PPP_PASSWORD = "2bb14b677e0f421f9162908f08271661";
		public const string READ_GATEWAY_TO_SERVER_IP_ADDRESS = "09897dc31b504a34835cf68ce5933a04";
		public const string SET_GATEWAY_TO_SERVER_IP_ADDRESS = "38dfce8925934314a7d4b17409779917";
		public const string READ_STATISTICS = "d60e2a94ab694a5c82c69868c1dd7c38";
        public const string SET_TOTAL_ENERGY_STATUS = "de31bda71ed74c60a8b14b5612520bb8";
		public const string READ_FIRMWARE_VERSION = "b6722ae4913148b6bf41c02b7ffec72a";

		// V 2.0
		public const string BROADCAST_DISCONNECT_CONTROL_RELAY = "b7f1b9801f6542fe807b573cc688dc81";
		public const string BROADCAST_CONNECT_CONTROL_RELAY = "00d34196af4a4e5198ff9b280d0e40a0";
		public const string BROADCAST_SET_PRIMARY_MAXIMUM_POWER_STATUS = "18e8884fbbe649ca8e628d2dd7009fed";

		// V 2.1
		public const string READ_REPEATER_PATHS = "3553acb885b9472fb3d28cc4d3172f9b";
		public const string REBOOT = "7e9fab7db6de4fc6827a023fdb0679ab";
		public const string DELETE_WAN_CONFIGURATION = "a2995515fd444506a55f4e4f823103e5";

		//For ATM Function Scheduling
		public const string SET_PROCESS_CONFIGURATION = "0b72299e12db498182d168858fb4e305";
		public const string READ_PROCESS_CONFIGURATION = "7ecdec9d1e0b4c1ebccbc378f95b8336";

        public const string READ_DISCOVERED_DEVICES = "a78acc8d74404a23b213a9ae64145927"; // Read Gateway Discovered Devices
		public const string READ_WAN_CONFIGURATION = "59149781e9b34c16b2cff5440078beff";
		public const string SET_WAN_CONFIGURATION = "346A2B7AF85D49b6A9FCBEFB46282B30";

		//Starting with v2.2:
		public const string SET_DEVICE_LIMIT = "532554d783be46f48b5c6360cb59ee42";
		public const string READ_DEVICE_LIMIT = "4ef8338f94c044f381a91207a4adb237";

        //Starting with v3.0
        public const string READ_EVENT_CONFIGURATION = "1da9c2f3a735478da7d8f4dd07af7d5e";
        public const string SET_EVENT_CONFIGURATION = "58c43ccec55b4937bc6f319ea9e32102";
        public const string READ_POWER_LINE_CARRIER_COMMUNICATION_CONFIGURATION = "ab184acb08a94278b8e623e4ed2bbda0";
        public const string SET_POWER_LINE_CARRIER_COMMUNICATION_CONFIGURATION = "08d87dbeb03349f2a1b0cf679187aafc";

        //Starting with v4
        public const string READ_ATM_CONFIGURATION = "51653506a0c9488caae56c3f1fc225d5";
        public const string SET_ATM_CONFIGURATION = "e33ba06a4510411e9fa90096a0ecbd26";
        public const string ASSIGN_OPERATIONAL_PROFILE = "61d06a345d69442aa6fa67781184497f";

        //Version 4.1 commands
        public const string COLLECT_REPLACEMENT_INFORMATION = "00c1ac363ab4431c8203209fd5c2bf4f";

        //Version 5.0 commands
        public const string READ_LOCAL_DATA_ACCESS_CONFIGURATION = "ab83d7572d8b4af396808659ba67d987";
        public const string SET_LOCAL_DATA_ACCESS_CONFIGURATION = "485ec92e8c2f4bbebf6d483226fee75a";

        public const string READ_FEATURE_ACTIVATION = "296d3849dfc74cf4afa201a3debd2916";
        public const string SET_FEATURE_ACTIVATION = "40716ef9d07549f6ac1eb10d196ace1a";

		public const string READ_FTP_CONFIGURATION = "39b66840e0d742c1b060f63f7041c4df";
		public const string SET_FTP_CONFIGURATION = "782b8a8f388d4d8f9de72882cdc03c60";

        public const string BROADCAST_WRITE_DEVICE_DATA = "057973db7f724fadacc8b44ccf28befa";
		public const string SEND_FILE = "ac46bdba803f4ea6b693866bdefd2d0b";



		public const string READ_GPS_COORDINATES = "24cdaf6105b44604a65449dcc9f306f4";
		public const string READ_SETTINGS = "8df59b8e11a5407da457aebdb015f7ab";
		public const string SET_SETTINGS = "fea95fbc504f4004bb2c8eb6afedd922";

        ////////////////////////////////////////////////////////////////////////////////////////
        // Version 5.3 Commands
        public const string READ_LVGM = "b124846f08de47bdb7f7ecb0d9a55917";
        public const string READ_LVGM_CONFIGURATION = "6ca02159f6e84117b9c98988ed1671e7";
        public const string SET_LVGM_CONFIGURATION = "04ebb7f6e1c6400db7c1b05c0da84ba3";
		public const string SET_PHASE_CONFIGURATION = "f45394e231514eba8c9f56b5a16d648f";
		public const string READ_PHASE_INFORMATION = "a0cce92cc37043f08f50d61ca24378e8";
		public const string READ_WAN_STATUS = "a7f10d5c259049a4bbe48862383b4e7d";
		public const string SET_KEY = "6ba4f26fe81c496a8273c0dfd0a1e107";

		////////////////////////////////////////////////////////////////////////////////////////
		//The following are deprecated - the version in which they were deprecated and the    //
		//constant replacing them (if any) is listed to the right of the constant.            //
		////////////////////////////////////////////////////////////////////////////////////////

		[Obsolete("v1.2 - use SET_WAN_PHONE_NUMBER")]
		public const string CHANGE_GATEWAY_PHONE_NUMBER = "c990d4e48b5d4001b7b6413c7e7ba25e"; //v1.2 - use SET_WAN_PHONE_NUMBER

		[Obsolete("v2.1 - use UPDATE_FIRMWARE")]
		public const string UPDATE_GATEWAY_FIRMWARE = "23c147b71a91412488147ac6213bf7e3"; //v2.1 - use UPDATE_FIRMWARE

		[Obsolete("v2.1 - use SET_PPP_USERNAME")]
		public const string CHANGE_PPP_LOGIN = "c45d90fb3cb943878db6fe5a4c5158be"; //v2.1 - use SET_PPP_USERNAME

		[Obsolete("v2.1 - use SET_PPP_PASSWORD")]
		public const string CHANGE_PPP_PASSWORD = "2bb14b677e0f421f9162908f08271661"; //v2.1 - use SET_PPP_PASSWORD

		[Obsolete("v2.1 - use SET_IP_ADDRESS")]
		public const string SET_SERVER_TO_GATEWAY_IP_ADDRESS = "4d854628421046919135dbc1379dc5b2"; //v2.1 - use SET_IP_ADDRESS

		[Obsolete("v2.1 - use READ_STATISTICS")]
		public const string RETRIEVE_GATEWAY_PERFORMANCE_STATISTICS = "d60e2a94ab694a5c82c69868c1dd7c38"; //v2.1 - use READ_STATISTICS

		[Obsolete("v2.1 - use SET_TOTAL_ENERGY_STATUS")]
        public const string ENABLE_TOTAL_ENERGY = "de31bda71ed74c60a8b14b5612520bb8";	//v2.1 - use SET_TOTAL_ENERGY_STATUS

		[Obsolete("v2.1 - use SET_WAN_PHONE_NUMBER")]
		public const string SET_SERVER_TO_GATEWAY_PHONE_NUMBER = "c990d4e48b5d4001b7b6413c7e7ba25e"; //v2.1 - use SET_WAN_PHONE_NUMBER

		[Obsolete("v2.1 - use READ_FIRMWARE_VERSION")]
		public const string READ_GATEWAY_FIRMWARE_VERSION = "b6722ae4913148b6bf41c02b7ffec72a";	//v2.1 - use READ_FIRMWARE_VERSION

		[Obsolete("v2.1 - use BROADCAST_DISCONNECT_CONTROL_RELAY")]
		public const string BROADCAST_OPEN_SECONDARY_CONTROL_OUTPUT = "b7f1b9801f6542fe807b573cc688dc81"; //v2.1 - use BROADCAST_DISCONNECT_CONTROL_RELAY

		[Obsolete("v2.1 - use BROADCAST_CONNECT_CONTROL_RELAY")]
		public const string BROADCAST_CLOSE_SECONDARY_CONTROL_OUTPUT = "00d34196af4a4e5198ff9b280d0e40a0"; //v2.1 - use BROADCAST_CONNECT_CONTROL_RELAY

		[Obsolete("v2.1 - use BROADCAST_SET_PRIMARY_MAXIMUM_POWER_STATUS")]
		public const string BROADCAST_MAXIMUM_POWER_LEVEL_ENABLE = "18e8884fbbe649ca8e628d2dd7009fed"; //v2.1 - use BROADCAST_SET_PRIMARY_MAXIMUM_POWER_STATUS

		[Obsolete("v2.3 - use SET_WAN_PHONE_NUMBER")]
		public const string SET_PHONE_NUMBER = "c990d4e48b5d4001b7b6413c7e7ba25e";	//v2.3 - use SET_WAN_PHONE_NUMBER

		[Obsolete("v2.3 - use BROADCAST_SET_PRIMARY_MAXIMUM_POWER_STATUS")]
		public const string BROADCAST_SET_MAXIMUM_POWER_LEVEL_STATUS = "18e8884fbbe649ca8e628d2dd7009fed"; //v2.3 - use BROADCAST_SET_PRIMARY_MAXIMUM_POWER_STATUS

        [Obsolete("v3.0 - not replaced")]
        public const string READ_GATEWAY_TO_SERVER_MODEM_INIT_STRING = "d4f1d34bffef48b89bc21aaeb5fb9c25";  //v3.0 - not replaced

        [Obsolete("v3.0 - not replaced")]
        public const string READ_GATEWAY_TO_SERVER_MODEM_TYPE = "56fc08b83b76416495ed68e48a265438"; //v3.0 - not replaced

        [Obsolete("v3.0 - not replaced")]
        public const string READ_GATEWAY_TO_SERVER_PHONE_NUMBER_1 = "1434818290d04270a108d7e15e29f911"; //v3.0 - not replaced

        [Obsolete("v3.0 - not replaced")]
        public const string READ_GATEWAY_TO_SERVER_PHONE_NUMBER_2 = "2f010587b2714d3892acc38f5b35c41f"; //v3.0 - not replaced

        [Obsolete("v3.0 - not replaced")]
        public const string SET_IP_ADDRESS = "4d854628421046919135dbc1379dc5b2";    //v3.0 - not replaced

        [Obsolete("v3.0 - not replaced")]
        public const string SET_SECURITY_OPTIONS = "9cf6934e44bf42ca9589a2d98045df70";  //v3.0 - not replaced

        [Obsolete("v3.0 - not replaced")]
        public const string SET_GATEWAY_TO_SERVER_MODEM_INIT_STRING = "d257f4ae90354cf2a70186c7a79f6eea";   //v3.0 - not replaced

        [Obsolete("v3.0 - not replaced")]
        public const string SET_GATEWAY_TO_SERVER_MODEM_TYPE = "3e42ca2578f44ad5a038867b0bb11a17";  //v3.0 - not replaced

        [Obsolete("v3.0 - not replaced")]
        public const string SET_GATEWAY_TO_SERVER_PHONE_NUMBER_1 = "ffaea45d1072471fbdd0d5ed1aa0b2f8";  //v3.0 - not replaced

        [Obsolete("v3.0 - not replaced")]
        public const string SET_GATEWAY_TO_SERVER_PHONE_NUMBER_2 = "ffeba0d6cabd4dbb8630fcaaf8d82e9a";  //v3.0 - not replaced

        [Obsolete("v4.1 - not replaced")]
        public const string SET_WAN_PHONE_NUMBER = "c990d4e48b5d4001b7b6413c7e7ba25e";

		[Obsolete("v5.3 - no replacement, no longer used")]
		public const string READ_CONTROLLER_CONFIGURATION = "b51190f6ab1c4c96a95a4ac8834bee81";

		[Obsolete("v5.3 - no replacement, no longer used")]
		public const string SET_CONTROLLER_CONFIGURATION = "e02870da0522423993406aeea1bf01fe";
		////////////////////////////////////////////////////////////////////////////////////////
	}

	public class DeviceEvents
	{
        public const string ADD_SUCCESS = "bffeafeef0964980926e5e0fc81cb55a";
        public const string ADD_FAILURE = "e895295532644b899216a2d647da6eb8";
        public const string MOVE_SUCCESS = "03894AB5B80C4530AE39C256C8EAF735";
        public const string MOVE_FAILURE = "297F815EFC044713AEF36D994194D296";
        public const string MOVE_CANCELLED = "751EABB3A118478e8225FD22CD805ED1";
        public const string ADD_CANCELLED = "90B7406A272A40b8A454282A7EF171A8";
        public const string REMOVE_SUCCESS = "fb1eb87c9def4190a121aaa2995627f5";
        public const string REMOVE_FAILURE = "5da8b450b7dd4cb2a982539934a4a2c6";
        public const string END_OF_BILLING_CYCLE_BILLING_DATA_AVAILABLE = "3b29e905f9764169a999cfc8ad527d2e";
        public const string READ_BILLING_DATA_ON_DEMAND_COMMAND_COMPLETE = "f309efb1263c4706ac8c1e3a18ec9da9";
        public const string READ_FULL_LOAD_PROFILE_COMMAND_COMPLETE = "f33490cf1e484c4d817d27b125b1031f";
        public const string READ_POWER_QUALITY_COMMAND_COMPLETE = "10becfacb599459cacc920cb4fc6bce5";
        public const string CONNECT_LOAD_COMMAND_COMPLETE = "4bea21adc4474f5fa7f9e412b74d0d11";
        public const string DISCONNECT_LOAD_COMMAND_COMPLETE = "416517694b9a482f87c5b81be3fee8fa";
        public const string READ_SELF_BILLING_DATA_COMMAND_COMPLETE = "0a87ae5fbef54616a74faa3a653250ba";
        public const string READ_SECONDARY_BILLING_REGISTERS_COMMAND_COMPLETE = "63b3fe0b77e841259d412763b349e2ff";
		public const string DISCONNECT_CONTROL_RELAY_COMMAND_COMPLETE = "a1a15cdcb98546fb96c6564a288b930e";
		public const string CONNECT_CONTROL_RELAY_COMMAND_COMPLETE = "d5e307213a2f4452870ffd12ab1bf937";
		public const string SET_LOAD_PROFILE_CONFIGURATION_COMMAND_COMPLETE = "18e64ad80a2e4f3691b1065b40c6d3e4";
        public const string READ_DELTA_LOAD_PROFILE_COMMAND_COMPLETE = "8C55269E57A44edf88609794260AD857";
		public const string READ_CONTINUOUS_DELTA_LOAD_PROFILE_COMMAND_COMPLETE = "AAC59D59669C4e7f9AC4E63782BD9F98";
		public const string UPDATE_FIRMWARE_COMMAND_COMPLETE = "4da60cb36956422086892e0baa077ac3";
		public const string CONTINUOUS_DELTA_LOAD_PROFILE_DATA_AVAILABLE = "E73928E49A194259A6C9BA5C987CF3B7";

        //New in v2.0
		public const string READ_PULSE_INPUT_CONFIGURATION_COMMAND_COMPLETE = "6dc87cd602ff46fa920a072ee584e5e4";
		public const string READ_INSTANTANEOUS_POWER_COMMAND_COMPLETE = "A5DB38E87B554ebe82058B4EB38CB58C";
		public const string READ_CONTROL_RELAY_COMMAND_COMPLETE = "8895461b1adf4aa6ad538c22aec447ff";
		public const string READ_LOAD_STATUS_COMMAND_COMPLETE = "9cd46282124b434499f7e02bf6d5a8d8";
		public const string READ_FIRMWARE_VERSION_COMMAND_COMPLETE = "194325c249b94c28a2125797d5acdf2f";
		public const string SET_PULSE_INPUT_CONFIGURATION_COMMAND_COMPLETE = "1e2a6435c4d0467ea01b1e7868708b92";
		public const string READ_ACTIVE_TOU_CALENDAR_COMMAND_COMPLETE = "AC8EB7183720417fA5083824F5336C39";
		public const string READ_PENDING_TOU_CALENDAR_COMMAND_COMPLETE = "71887A7C756C42bdA83ACAB49FC099D8";
		public const string SET_PENDING_TOU_CALENDAR_COMMAND_COMPLETE = "07660D8DEB4F427c9B4FF53E4E83DD41";
		public const string READ_DISPLAY_CONFIGURATION_COMMAND_COMPLETE = "737FFF081E4D47199D1350B354B43573";
		public const string SET_DISPLAY_CONFIGURATION_COMMAND_COMPLETE = "AF23E940CC134d45A0C6501CA4D44B0C";
		public const string SET_ALARM_DISPLAY_CONFIGURATION_COMMAND_COMPLETE = "6D0A1C1D0C2E45e5BF8C87A383090EE4";

		//new in v2.1
		public const string READ_DOWN_LIMIT_COMMAND_COMPLETE = "2f10429d091a4923963f2f1fc427fb3e";
		public const string SET_DOWN_LIMIT_COMMAND_COMPLETE = "90cd985936a44233892b2f779171a849";
		public const string SET_DATE_TIME_COMMAND_COMPLETE = "74d6144653f548e1aeeb70b0cc6a7eef";
		public const string READ_HISTORICAL_BILLING_DATA_COMMAND_COMPLETE = "4277c41e1a4445b385f00821bd0e1cce";

		public const string TEMP_DOWN = "84965a6c5ca6447c8983271372d4dad9";
		public const string PHASE_INACCURACY = "648c506071df4f1984f8d5626226a86a";
        public const string PHASE_ROTATION = "a152064cf7f54c6290cc9c4e5e209ddd"; //MeterAlarms.PHASE_ROTATION;
        public const string PREPAY_CREDIT_EXHAUSTED = "a225ba7c5a894589be61aeef58592e9b"; //MeterAlarms.PREPAY_CREDIT_EXHAUSTED;
        public const string PREPAY_WARNING_ACKNOWLEDGEMENT = "dce1d1c4935f40b3a07cf417f41b21ce"; //MeterAlarms.PREPAY_WARNING_ACKNOWLEDGEMENT;

		//New in v2.2
        public const string READ_ALARM_POLLING_RATE_COMMAND_COMPLETE = "51e41cd6f3344f2aac0cc70204e5fdd5";
		public const string SET_ALARM_POLLING_RATE_COMMAND_COMPLETE = "fa06b45f85c947e4b5c41aebee2adc00";
        public const string READ_BILLING_SCHEDULE_COMMAND_COMPLETE = "2E8E13CD32E746d988FC0EB53A20B23E";
		public const string SET_BILLING_SCHEDULE_COMMAND_COMPLETE = "C00896213B55412f8710DEA77D6F369E";

        public const string INTERNAL_EXPANSION_MODULE_CARD_INSTALLATION_OR_REMOVAL = "581b5ac07e474811890becb076a8ffeb"; //MeterAlarms.MEP_CARD_INSTALLATION_OR_REMOVAL;
		public const string AUTO_DISCOVERY = "faaf691349c54630af1f91f1275cc97a";
        public const string MBUS_BILLING_READ_OVERFLOW_OCCURRED = "81f436cdb03a42478cab94e128b17986"; //MBusAlarms.BILLING_READ_OVERFLOW_OCCURRED;
        public const string MBUS_FAILED_COMMUNICATIONS_ON_READ = "3f3a1abce12d4ad78e5095186e685445"; //MBusAlarms.FAILED_COMMUNICATIONS_ON_READ;
        public const string MBUS_BILLING_READ_SERIAL_NUMBER_MISMATCH = "2d019ae40d7842a7839b837748369590"; //MBusAlarms.BILLING_READ_SERIAL_NUMBER_MISMATCH;
		public const string MBUS_ALARMS = "aebf13a8eabe451a892de6bf67c1263e"; //previously MBUS_DEVICE_ALARMS
		public const string UNEXPECTED_END_OF_CYCLE_BILLING_DATA_AVAILABLE = "8aed0a9a60a9461f954fba8341c53eb9";
		public const string READ_STATISTICS_COMMAND_COMPLETE = "933d0223e4e7464bb911b81791336773";
		public const string READ_AUTO_DISCOVERY_CONFIGURATION_COMMAND_COMPLETE = "153ebbb9029f4a2e81808e99ecbd1432";
		public const string SET_AUTO_DISCOVERY_CONFIGURATION_COMMAND_COMPLETE = "9a2909fcd21645eeacd2dcfb63c5f24c";
		public const string READ_EVENT_LOG_COMMAND_COMPLETE = "63fa52aad137434c91b8f957c01a13d3";
		public const string EVENT_LOG_PENDING_OVERFLOW = "a0b97660e3544be7beb0f84bb3e8be44";
		public const string READ_UTILITY_INFORMATION_COMMAND_COMPLETE = "0982e6400da0461984b37db55e6c6b30";
		public const string SET_UTILITY_INFORMATION_COMMAND_COMPLETE = "1a7da8013994477681b5ef8cff7d0711";

		// V2.3 Device Events
		public const string ADD_PREPAY_CREDIT_COMMAND_COMPLETE = "431CD8BB28B1462c946BD70AC24F0B4C";
		public const string CLEAR_PREPAY_CREDIT_COMMAND_COMPLETE = "DB3A53F6467C4fc2972BF6BB043EC0DA";
		public const string READ_PREPAY_CONFIGURATION_COMMAND_COMPLETE = "9B1734FEDF4D4cdf8F522B5256167816";
		public const string SET_PREPAY_CONFIGURATION_COMMAND_COMPLETE = "84BD2180D04D4c7c94BC2F4095545F87";
		public const string READ_TIME_ZONE_CONFIGURATION_COMMAND_COMPLETE = "3cdf51589c65472ab7bbd9fb76cb6469";
		public const string SET_TIME_ZONE_CONFIGURATION_COMMAND_COMPLETE = "a5ec927c2e0d4132984ede2ed280eebc";
		public const string READ_PREPAY_CREDIT_COMMAND_COMPLETE = "6df8c7e3885346879d806e27a6f225de";
		public const string AUTO_TEST_POINT_STATUS = "0c5d543d093849fab30dc0d16326a4c5";

		// V3.0 Device Events
        public const string LOAD_PROFILE_OVERFLOW = "3acc6522fcb342748773009d412cd0a2"; //MeterAlarms.LOAD_PROFILE_OVERFLOW;
        public const string LOAD_DISCONNECT_STATUS_CHANGE = "8c61b51eeaad414cae2e8a40c758debc"; //MeterAlarms.LOAD_DISCONNECT_STATUS_CHANGE;
        public const string CONTROL_RELAY_STATUS_CHANGE = "aec6c8c2237e469aa0d1081f18486354"; //MeterAlarms.CONTROL_RELAY_STATUS_CHANGE;
        public const string LOAD_PROFILE_BACKFILL_FAILED = "a3c4c5e34c62484aaba533c16e5b7b0c"; //MeterAlarms.LOAD_PROFILE_BACKFILL_FAILED;
        public const string POWER_QUALITY_EVENT_DETECTED = "5830f4de931a434dadd080c011756ae5"; //MeterAlarms.POWER_QUALITY_EVENT_DETECTED;
        public const string UNREAD_EVENT_LOG_ENTRY_EXISTS = "811eb4f6f85e4870bc86dfe9285d6a67"; //MeterAlarms.UNREAD_EVENT_LOG_ENTRY_EXISTS;
		public const string READ_DISCONNECT_CONFIGURATION_COMMAND_COMPLETE = "82b138d172574c15894649524567121f";
		public const string SET_DISCONNECT_CONFIGURATION_COMMAND_COMPLETE = "549f7795a11042abb8f6d735404061b1";
		public const string WRITE_DATA_COMMAND_COMPLETE = "6ea358d702a147ce9dc1cb0446a615cb";
        public const string READ_CONTINUOUS_EVENT_LOG_CONFIGURATION_COMMAND_COMPLETE = "c65a04f331c3442a9e1d610cb61ee1aa";
        public const string SET_CONTINUOUS_EVENT_LOG_CONFIGURATION_COMMAND_COMPLETE = "0a5398b133584d45a6189992f3b3ffa5";
        public const string BILLING_INTERFACE_MISMATCH = "7159AA6727D941cf9F0E856EC5F6D022";
        public const string READ_STOP_MODE_CONFIGURATION_COMMAND_COMPLETE = "798da63001ef41b8b1848c26d8f3515b";
        public const string SET_STOP_MODE_CONFIGURATION_COMMAND_COMPLETE = "8516b5ddaaab437cb3931541acff009a";
        public const string EVENT_LOG_DATA_AVAILABLE = "7821bd4d6f4c4f079ec0c662a36f5933";
        public const string READ_POWER_QUALITY_CONFIGURATION_COMMAND_COMPLETE = "fcb9a271f4d144e1a28b90f0aedfedec";
        public const string SET_POWER_QUALITY_CONFIGURATION_COMMAND_COMPLETE = "3c3ca351fe054dba9ffabe24042c3aed";
        public const string RESET_POWER_QUALITY_STATISTICS_COMMAND_COMPLETE = "9ddb8a0fddc240e9bd48f0e86258be06";
        public const string READ_SELF_READ_RETRIEVAL_CONFIGURATION_COMMAND_COMPLETE = "a5ffdc09398c4e84b66e97d9ecd31ab8";
        public const string SET_SELF_READ_RETRIEVAL_CONFIGURATION_COMMAND_COMPLETE = "392da58b4fd84d5fa8cb013edeb517cd";
        public const string ADD_ONE_TIME_READ_REQUEST_COMMAND_COMPLETE = "2f8565fb143b4061a58ea23697223cd3";
        public const string CLEAR_ONE_TIME_READ_REQUESTS_COMMAND_COMPLETE = "e57f6b94596b48a4851aa56878a02325";
        public const string READ_HISTORICAL_ONE_TIME_READ_DATA_COMMAND_COMPLETE = "e726cabf27ac43ffa01f2acc8da90698";
        public const string ONE_TIME_READ_DATA_AVAILABLE = "68ce4375b84a4d3baa8ca5ae2f596ff3";
        public const string UNEXPECTED_ONE_TIME_READ_DATA_AVAILABLE = "54a76f264f7849d5a8b819eb02ce68a5";
        public const string READ_ONE_TIME_READ_REQUESTS_COMMAND_COMPLETE = "daf68045f7274037bc648638115517cc";
        public const string CONTINUOUS_DELTA_LOAD_PROFILE_COLLECTION_STOPPED = "d98a7d4099e14908835435a83afae9e9";
        public const string READ_EVENT_LOG_CONFIGURATION_COMMAND_COMPLETE = "8dc650d90ec44df493a0f8271f51b8c5";
        public const string SET_EVENT_LOG_CONFIGURATION_COMMAND_COMPLETE = "c3849da8d18c4c78b1a167795778a13d";

        public const string CONFIGURATION_ERROR = "8ca7f8c35d2247a397c49c9f33d668a0"; //MeterAlarms.CONFIGURATION_ERROR;
        public const string SYSTEM_RESET = "21b3b465b3e64bce952133f43f706c5c"; //MeterAlarms.SYSTEM_RESET;
        public const string RAM_FAILURE = "e5a5b0d1f1e44129a98fddaf6ce4398a"; //MeterAlarms.RAM_FAILURE;
        public const string ROM_FAILURE = "79c97a9c3bb147f7a21e921d43d51eb3"; //MeterAlarms.ROM_FAILURE;
        public const string NON_VOLATILE_MEMORY_FAILURE = "82a33447b5ab434bb4e578eec104bcea"; //MeterAlarms.NON_VOLATILE_MEMORY_FAILURE;
        public const string CLOCK_ERROR = "f316273e6f374e0086710af9d101054f"; //MeterAlarms.CLOCK_ERROR;
        public const string MEASUREMENT_ERROR = "41e0b86652164cd4a539b226cea18719"; //MeterAlarms.MEASUREMENT_ERROR;
        public const string LOW_BATTERY = "4ea1f5f2583f4017b8c449d6423123ca"; //MeterAlarms.LOW_BATTERY;
        public const string POWER_FAILURE = "b02fb601b98a47ce8de32dbb157fb2a2"; //MeterAlarms.POWER_FAILURE;
        public const string TAMPER_DETECTED = "6f37d5a7280b405288fdb174c5a1af52"; //MeterAlarms.TAMPER_DETECTED;
        public const string REVERSE_ENERGY = "729ba6910acb4426a0a0706005a58976"; //MeterAlarms.REVERSE_ENERGY;
        public const string DATA_BACKUP_INCOMPLETE = "0ef90b3b76b94967b0e623386472e25c"; //MeterAlarms.DATA_BACKUP_INCOMPLETE;
        public const string DISCONNECT_SWITCH_ERROR = "75fbf631eb7947d29138327c11291ff1"; //MeterAlarms.DISCONNECT_SWITCH_ERROR;
        public const string PHASE_LOSS = "da41430263f2482c83180c7ec6ca5282"; //MeterAlarms.PHASE_LOSS;
        public const string PHASE_INVERSION = "bc648feb5e324de9ae4578bc89a8d557"; //MeterAlarms.PHASE_INVERSION;
        public const string PLC_COMMUNICATION_FAILURE = "88af05a6ce154fe5a4da71b92b1c6f88"; //MeterAlarms.PLC_COMMUNICATION_FAILURE;
        public const string GENERAL_ERROR = "2dd6e07c1bcc4d78bd4a8014f3fe21b2"; //MeterAlarms.GENERAL_ERROR;
        public const string INVALID_PASSWORD = "39320b04e50d41f5ac68391dbd4d0131"; //MeterAlarms.INVALID_PASSWORD_OVER_OPTICAL;
        public const string REMOTE_COMMUNICATIONS_INACTIVE = "782db533d360401189e5f08b7f895d80"; //MeterAlarms.REMOTE_COMMUNICATIONS_INACTIVE;
        public const string CURRENT_ON_MISSING_OR_UNUSED_PHASE = "a7f07be6c8414b79b7b1a3ebbfe60ee2"; //MeterAlarms.CURRENT_ON_MISSING_OR_UNUSED_PHASE;
        public const string PULSE_INPUT_1_TAMPER = "e00f616458684d26b4882b5e33af693c"; //MeterAlarms.PULSE_INPUT_1_TAMPER;
        public const string PULSE_INPUT_2_TAMPER = "0618bed8c9dc4be3941f0207c699632e"; //MeterAlarms.PULSE_INPUT_2_TAMPER;
        public const string MAGNETIC_TAMPER = "b3de1c9ae0c4433b8a5150d9f3c1a554"; //MeterAlarms.MAGNETIC_TAMPER
        public const string ACCESS_LOCKOUT_OVERRIDE = "53bfdc0c8b9341688a520134b1098b26"; //MeterAlarms.ACCESS_LOCKOUT_OVERRIDE

        public const string UNEXPECTED_EVENT_LOG_DATA_AVAILABLE = "16df53a75ee946de86da64e0447b4443";
		public const string READ_CONTINUOUS_DELTA_LOAD_PROFILE_COLLECTION_CONFIGURATION_COMMAND_COMPLETE = "b5df64484e86499aa7fc88edb6bbc0db";
		public const string SET_CONTINUOUS_DELTA_LOAD_PROFILE_COLLECTION_CONFIGURATION_COMMAND_COMPLETE = "305a50a3cb1440f59a9ceb6a2e425f65";
        public const string READ_DEMAND_CONFIGURATION_COMMAND_COMPLETE = "44CA1EADA4E84a818B997B24A4383205";
        public const string SET_DEMAND_CONFIGURATION_COMMAND_COMPLETE = "2919767C8BD14d2391695D7F9C4B189C";
		public const string RESET_DEMAND_VALUES_COMMAND_COMPLETE = "6c0467e0b7974374a895620464b1d8ac";
        public const string READ_LOAD_PROFILE_CONFIGURATION_COMMAND_COMPLETE = "6fd59a5075b04f6fa81b3921c82f4d51";
		public const string READ_LOAD_PROFILE_ON_DEMAND_COMMAND_COMPLETE = "cdedc0b2e0fd4048b4364e80412ecd3d";
        public const string DEMAND_RESET_DATA_AVAILABLE = "0264748bfef04dbea9ebbce8542ea2e7";
        public const string UNEXPECTED_DEMAND_RESET_DATA_AVAILABLE = "0d3ade21b557473ab46f4d19e38fd955";
        public const string KEY_ISSUE_DETECTED = "16e4f4d9332a45299cdf37465adef8d2";
        public const string KEY_ISSUE_RESOLVED = "a70985df086245eb96630e56d63371b1";
        public const string KEY_ISSUE_NOT_RESOLVED = "f0b46f4d7b264ac0b919ad7f0cf2add9";

        // v4 Device Events
        public const string READ_ATM_CONFIGURATION_COMMAND_COMPLETE = "5ea7f4b0c44f4945b41c42975d6d6f9d";
        public const string SET_ATM_CONFIGURATION_COMMAND_COMPLETE = "da5865d871af4d50a8d6677541ea368a";
        public const string TOTAL_HARMONIC_DISTORTION_EVENT_DETECTED = "5b107ab0c46b492a9cce040192176261";
        public const string READ_DATA_ON_DEMAND_COMMAND_COMPLETE = "da0c2b374dcb41588b1affb87131afd4";
		public const string MEP_ALARMS = "6b4e13fa1e184e769610ce3ae417e5f8";
		public const string MEP_COMMUNICATIONS_FAILURE = "45cffd598df349c2abb985319123ab66";
		public const string MEP_READ_OVERFLOW_OCCURRED = "873ddf46cf0e4d3c8771fcba4b36b78f";
		public const string MEP_DATA_AVAILABLE = "a307c83832604922be863e67f8d24d33";
        public const string LOAD_SIDE_VOLTAGE_WITH_OPEN_DISCONNECT_SWITCH = "1acb13f94e224d5491fea02233ec41c5";
		public const string READ_READ_SCHEDULE_COMMAND_COMPLETE = "3db749c532964988a971b822287b78d8";
		public const string SET_READ_SCHEDULE_COMMAND_COMPLETE = "c86c69fae7c742038077a92b39191aa2";
		public const string READ_PROVISIONING_IDENTIFIERS_COMMAND_COMPLETE = "6bd79895768f41be8f0393eb7f18b557";
		public const string SET_PROVISIONING_IDENTIFIERS_COMMAND_COMPLETE = "27b59bc60cbd4ce89967af773ef2881e";

        //v4.1 Device Events
        public const string READ_TIER_CONTROL_CONFIGURATION_COMMAND_COMPLETE = "3171484de0d1466da7cbc026211b7806";
        public const string SET_TIER_CONTROL_CONFIGURATION_COMMAND_COMPLETE = "44f0af6a9d7b490ab5f8322e60c9bca8";
        public const string HIGH_FREQUENCY_NOISE_DETECTED = "b9b2d7114d084943be571a6ead9a0c38";
		public const string SET_CONTROL_RELAY_CONFIGURATION_COMMAND_COMPLETE = "b3967f08307b4c41b0a9a16be9806046";
		public const string READ_CONTROL_RELAY_CONFIGURATION_COMMAND_COMPLETE = "91322189a36447d6a0c118d87ea85f8c";
        public const string READ_DATA_RECORD_HEADER_CONFIGURATION_COMMAND_COMPLETE = "7bd7eec56b7e44e7a143710c942f6ae3";
        public const string SET_DATA_RECORD_HEADER_CONFIGURATION_COMMAND_COMPLETE = "b72e76d1be884029bdf7c499910a3e23";
        public const string READ_DATA_RECORD_HEADER_MAPPING_COMMAND_COMPLETE = "a109cef3503c4ab6b0a726a0683918b7";
        public const string SET_DATA_RECORD_HEADER_MAPPING_COMMAND_COMPLETE = "4fa5ab1b3cf84c8b997806473e17d04e";
        public const string GATEWAY_REPLACEMENT_ADD_FAILURE = "279135a3d3c745c284e996ad229e3dfd";
        public const string GATEWAY_REPLACEMENT_ADD_SUCCESS = "1807abe7305f45dfb1abfd4e48278a34";
        public const string GATEWAY_REPLACEMENT_ADD_CANCELLED = "4b6ee4f6206b4054923dc77e5457801f";
		public const string SEND_FILE_COMMAND_COMPLETE = "f42d2671e5574b7a9f907dc725e9c060";

		//v5.0 Device Events
		public const string CONTINUOUS_DELTA_LOAD_PROFILE_DATA_MISSING = "1abc5c55a66d478990df785bad59b4eb";
        public const string READ_LOCAL_DATA_ACCESS_CONFIGURATION_COMMAND_COMPLETE = "0555906f933d4c1f80b5ffc819976ef9";
        public const string SET_LOCAL_DATA_ACCESS_CONFIGURATION_COMMAND_COMPLETE = "2b7700f7c42940a2bea1efff579be88e";
        public const string LOCAL_DATA_COLLECTION_STOPPED = "139dfe49c52c4ccb94ebe834c791fa28";
		public const string SET_MEASUREMENT_RATIO_CONFIGURATION_COMMAND_COMPLETE = "ed0c8699dabb4913be0c0b16ae87d669";
		public const string READ_MEASUREMENT_RATIO_CONFIGURATION_COMMAND_COMPLETE = "80e62bafed214f6ca793902184ce868d";
        public const string SET_BROADCAST_GROUP_MEMBERSHIP_CONFIGURATION_COMMAND_COMPLETE = "33ce834fd5ed4c5d9b0930f4e11a11c2";
        public const string READ_BROADCAST_GROUP_MEMBERSHIP_CONFIGURATION_COMMAND_COMPLETE = "6dccdb4bba814eebb204224fbda0c3f0";
		public const string GATEWAY_REPLACEMENT_DEVICE_ADD_NOT_ATTEMPTED = "83bc59eea5c04853a42f89f39d35129a";

        //v5.1 Device Events
        public const string LOAD_PROFILE_DATA_SET_COLLECTION_STOPPED = "60760484830843fa9fe5038db4252d23";
        public const string DELTA_LOAD_PROFILE_DATA_AVAILABLE_DATA_SET_1 = "e1f82d39635340848ea4de36b0dcfc7b";
        public const string DELTA_LOAD_PROFILE_DATA_AVAILABLE_DATA_SET_2 = "04c112f0062f45fbbb151b84edae0bce";
        public const string DELTA_LOAD_PROFILE_DATA_AVAILABLE_DATA_SET_3 = "e019156c6fc145dda05e63da5f938971";
        public const string DELTA_LOAD_PROFILE_DATA_AVAILABLE_DATA_SET_4 = "62c016409df0491eb5767ca12704aa5a";
        public const string FULL_LOAD_PROFILE_DATA_AVAILABLE_DATA_SET_1 = "2cc6a202ef1a4c05a2cb2929920c0a8d";
        public const string FULL_LOAD_PROFILE_DATA_AVAILABLE_DATA_SET_2 = "53ef697907b84b9d8546dff5e8caa897";
        public const string FULL_LOAD_PROFILE_DATA_AVAILABLE_DATA_SET_3 = "85750bf7873d4fb598947779511560ee";
        public const string FULL_LOAD_PROFILE_DATA_AVAILABLE_DATA_SET_4 = "5496c42fdd144ac7a2cb493913166b51";
        public const string CONTINUOUS_DELTA_LOAD_PROFILE_DATA_AVAILABLE_DATA_SET_1 = DeviceEvents.CONTINUOUS_DELTA_LOAD_PROFILE_DATA_AVAILABLE;
        public const string CONTINUOUS_DELTA_LOAD_PROFILE_DATA_AVAILABLE_DATA_SET_2 = "d2633957b86349f4970607da72d37e8f";
        public const string CONTINUOUS_DELTA_LOAD_PROFILE_DATA_AVAILABLE_DATA_SET_3 = "97199c980e6641319fa559550ea39425";
        public const string CONTINUOUS_DELTA_LOAD_PROFILE_DATA_AVAILABLE_DATA_SET_4 = "f3249c5d7aab41d38f4cd71ddece265a";


		//v5.1 Device Events
		public const string EXTERNAL_VARIABLE_ALARM_DETECTED = "7a3af69c451d46f7a1fd0f67b3e9b883";
		public const string NEUTRAL_BYPASS_DETECTED = "4b69e1145b9e46c0a88ac2e9ee5166cc";
		public const string READ_MEMORY_CONFIGURATION_COMMAND_COMPLETE = "5896d629ee224a34bf79d3a290aac8cf";
		public const string SET_MEMORY_CONFIGURATION_COMMAND_COMPLETE = "6355e115e3a346508d3c8bca4c180702";
        public const string READ_LOAD_PROFILE_DATA_SET_COLLECTION_CONFIGURATION_COMMAND_COMPLETE = "01a8e9d2db124d0c98a080157746bb25";
        public const string SET_LOAD_PROFILE_DATA_SET_COLLECTION_CONFIGURATION_COMMAND_COMPLETE = "f5fd8ff54a8e4deaa3a9537f5cac42ea";
		public const string SET_GATEWAY_SETTINGS_COMMAND_COMPLETE = "b80ef62cbda54c82adbc01345a9e06fc";
		public const string READ_GATEWAY_SETTINGS_COMMAND_COMPLETE = "8af7c4c3f28f4dcaaa350bf663643afe";
		public const string READ_MCN_CONFIGURATION_COMMAND_COMPLETE = "bc8df27b62b0486fae07c50e7cab13b9";
		public const string SET_MCN_CONFIGURATION_COMMAND_COMPLETE = "c42620a9d18e4e6998e3596e8e2a6010";

        //v5.2 Device Events
        public const string READ_POWER_STATUS_COMMAND_COMPLETE = "EA9720BFF3B74C86B848197E99FE3BDD";
		public const string READ_ALARM_SETTINGS_COMMAND_COMPLETE = "25829f3dce3444d69b7511598c002f04";
		public const string SET_ALARM_SETTINGS_COMMAND_COMPLETE = "c6e447293cac4a21b7cc843cceb7bb1c";
		public const string INVALID_PASSWORD_VIA_OPTICAL = "4fa1fbca7ecf435b8b123b1f50959f0c";
		public const string INVALID_PASSWORD_VIA_PLC = "bbc9befec60e447193f62e41ab0fa0fb";
		public const string UNBALANCED_VOLTAGE_DETECTED = "52b806ffc91848048c71be568bfbd976";
		public const string READ_DISCONNECT_THRESHOLDS_CONFIGURATION_COMMAND_COMPLETE = "b1605153a86142729a93f535632553f8";
		public const string SET_DISCONNECT_THRESHOLDS_CONFIGURATION_COMMAND_COMPLETE = "222b4e1323c9418495f0d1e6969c8c74";

		//v5.3 Device Events
		public const string READ_DEVICE_TAMPER_CONFIGURATION_COMMAND_COMPLETE = "6c506e73c80f45419d3ffefa886219b3";
		public const string SET_DEVICE_TAMPER_CONFIGURATION_COMMAND_COMPLETE = "3484c17fbdfa4400b39f4db170194732";
        public const string READ_MEP_CONFIGURATION_COMMAND_COMPLETE = "7079cddf664749bcbf16ed9c2e7e9912";
        public const string SET_MEP_CONFIGURATION_COMMAND_COMPLETE = "e10ad2f542e6436f86ad15fd6e09d3d4";
		public const string SET_KEY_COMMAND_COMPLETE = "b25a349d06f847b695769d31ef747d45";
		public const string KEY_CHANGED = "6abe933cac434959ab0f0d2e6597a4dc";
		public const string READ_DEVICE_REGISTER_CONFIGURATION_COMMAND_COMPLETE = "84306bb2434142b1aa2cb1434a2ffa74";
		public const string SET_DEVICE_REGISTER_CONFIGURATION_COMMAND_COMPLETE = "853d5f35c7584c959278c30bdfdad329";
		public const string COMMISSION_STATUS_CHANGED = "2978ab4ff8194751b627c2e56998c582";
        public const string SET_OPTICAL_PORT_CONFIGURATION_COMMAND_COMPLETE = "23fa5d4d2ea54901910d1b6ecf2c59f8";
        public const string READ_OPTICAL_PORT_CONFIGURATION_COMMAND_COMPLETE = "c909ceb106584105a0b32366896224a2";


		////////////////////////////////////////////////////////////////////////////////////////
		//The following are deprecated - the version in which they were deprecated and the    //
		//constant replacing them (if any) is listed to the right of the constant.            //
		////////////////////////////////////////////////////////////////////////////////////////

        [Obsolete("V2.1 - use SET_CONTROL_RELAY_CONFIGURATION_COMMAND_COMPLETE")]
        public const string SET_CONTROL_RELAY_TIERS_COMMAND_COMPLETE = "ca3a8bb6fbc14db9971f2824b0cbbbf2";

        [Obsolete("V2.1 - use SET_CONTROL_RELAY_CONFIGURATION_COMMAND_COMPLETE")]
        public const string SET_CONTROL_RELAY_TIERS_STATUS_COMMAND_COMPLETE = "9469ea5f72fe451c97a7f58690d57ebe";

        [Obsolete("v2.2 - not replaced since this event won't occur in any released version")]
		public const string READ_PREPAY_MAXIMUM_POWER_STATUS_COMMAND_COMPLETE = "a1bb59286c5d4747885e33b4cebb59fd"; //v2.2 - not replaced since this event won't occur in any released version

		[Obsolete("v2.2 - not replaced since this event won't occur in any released version")]
		public const string SET_PREPAY_MAXIMUM_POWER_STATUS_COMMAND_COMPLETE = "8dc4416aec64455f818899855adfd318"; //v2.2 - not replaced since this event won't occur in any released version

		[Obsolete("v2.2 - not replaced since this event won't occur in any released version")]
		public const string READ_PREPAY_MAXIMUM_POWER_COMMAND_COMPLETE = "b45bc0a6c43e40a4b427b0f9ae67070e"; //v2.2 - not replaced since this event won't occur in any released version

		[Obsolete("v2.2 - not replaced since this event won't occur in any released version")]
		public const string SET_PREPAY_MAXIMUM_POWER_COMMAND_COMPLETE = "c70f0449741c4c158426f6a059fc4f9b"; //v2.2 - not replaced since this event won't occur in any released version

		[Obsolete("v2.3 - use SET_PRIMARY_MAXIMUM_POWER_LEVEL_COMMAND_COMPLETE")]
        public const string SET_MAXIMUM_POWER_LEVEL_COMMAND_COMPLETE = "93af3eebb36742a89d6a12cd9e7274b8"; //v2.3 - use SET_PRIMARY_MAXIMUM_POWER_LEVEL_COMMAND_COMPLETE

		[Obsolete("v2.3 - use READ_PRIMARY_MAXIMUM_POWER_LEVEL_COMMAND_COMPLETE")]
		public const string READ_MAXIMUM_POWER_LEVEL_COMMAND_COMPLETE = "164fb5c6de3546eba985de5d71e984a9"; //v2.3 - use READ_PRIMARY_MAXIMUM_POWER_LEVEL_COMMAND_COMPLETE

		[Obsolete("v2.3 - use SET_PRIMARY_MAXIMUM_POWER_STATUS_COMMAND_COMPLETE")]
        public const string SET_MAXIMUM_POWER_STATUS_COMMAND_COMPLETE = "2bbb57575b8d49e980f1837e9682cc75"; //v2.3 - use SET_PRIMARY_MAXIMUM_POWER_STATUS_COMMAND_COMPLETE

		[Obsolete("v2.3 - use READ_PRIMARY_MAXIMUM_POWER_STATUS_COMMAND_COMPLETE")]
		public const string READ_MAXIMUM_POWER_STATUS_COMMAND_COMPLETE = "BBE31E1ECE9A44cc91BC66BA9AD89DEA"; //v2.3 - use READ_PRIMARY_MAXIMUM_POWER_STATUS_COMMAND_COMPLETE

        [Obsolete("v3.0 - use SET_PENDING_TOU_CALENDAR_COMMAND_COMPLETE")]
        public const string SET_TOU_SEASON_SCHEDULES_COMMAND_COMPLETE = "8F11B342841D404a91883626FF3D2704"; //v3.0 - use SET_PENDING_TOU_CALENDAR_COMMAND_COMPLETE

        [Obsolete("v3.0 - use SET_PENDING_TOU_CALENDAR_COMMAND_COMPLETE")]
        public const string SET_TOU_RECURRING_DATES_COMMAND_COMPLETE = "F039C7FDC7204c31A576C9AFC24BD951"; //v3.0 - use SET_PENDING_TOU_CALENDAR_COMMAND_COMPLETE

        [Obsolete("v3.0 - use SET_PENDING_TOU_CALENDAR_COMMAND_COMPLETE")]
        public const string SET_TOU_DAY_SCHEDULES_COMMAND_COMPLETE = "B5FDEFDF88E348f0824E329778BA78EF"; //v3.0 - use SET_PENDING_TOU_CALENDAR_COMMAND_COMPLETE

        [Obsolete("v3.0 - use READ_ACTIVE_TOU_CALENDAR_COMMAND_COMPLETE")]
        public const string READ_TOU_DAY_SCHEDULES_COMMAND_COMPLETE = "270827C2F082486f81916DE63876D4AE"; //v3.0 - use READ_ACTIVE_TOU_CALENDAR_COMMAND_COMPLETE

        [Obsolete("v3.0 - use READ_ACTIVE_TOU_CALENDAR_COMMAND_COMPLETE")]
        public const string READ_TOU_SEASON_SCHEDULES_COMMAND_COMPLETE = "9A9A5B023D5C41da9AF0506B271E9CF7"; //v3.0 - use READ_ACTIVE_TOU_CALENDAR_COMMAND_COMPLETE

        [Obsolete("v3.0 - use READ_ACTIVE_TOU_CALENDAR_COMMAND_COMPLETE")]
        public const string READ_TOU_RECURRING_DATES_COMMAND_COMPLETE = "1AD9795CD5524895AD9B013570CA2ED9"; //v3.0 - use READ_ACTIVE_TOU_CALENDAR_COMMAND_COMPLETE

        [Obsolete("v3.0 - use READ_POWER_QUALITY_CONFIGURATION_COMMAND_COMPLETE")]
        public const string READ_POWER_QUALITY_OUTAGE_DURATION_THRESHOLD_COMMAND_COMPLETE = "DF520B4557734f9e93120044480063A2"; //v3.0 - use READ_POWER_QUALITY_CONFIGURATION_COMMAND_COMPLETE

        [Obsolete("v3.0 - use READ_POWER_QUALITY_CONFIGURATION_COMMAND_COMPLETE")]
        public const string READ_POWER_QUALITY_SAG_SURGE_DURATION_THRESHOLD_COMMAND_COMPLETE = "2F24531525494369B2C2D75DD7ACF695"; //v3.0 - use READ_POWER_QUALITY_CONFIGURATION_COMMAND_COMPLETE

        [Obsolete("v3.0 - use READ_POWER_QUALITY_CONFIGURATION_COMMAND_COMPLETE")]
        public const string READ_POWER_QUALITY_SAG_VOLTAGE_PERCENT_THRESHOLD_COMMAND_COMPLETE = "CD02921B850748a18A463D24685FA3D6"; //v3.0 - use READ_POWER_QUALITY_CONFIGURATION_COMMAND_COMPLETE

        [Obsolete("v3.0 - use READ_POWER_QUALITY_CONFIGURATION_COMMAND_COMPLETE")]
        public const string READ_POWER_QUALITY_SURGE_VOLTAGE_PERCENT_THRESHOLD_COMMAND_COMPLETE = "AF1653E211064c4c821DC5F547D6337F"; //v3.0 - use READ_POWER_QUALITY_CONFIGURATION_COMMAND_COMPLETE

        [Obsolete("v3.0 - use READ_POWER_QUALITY_CONFIGURATION_COMMAND_COMPLETE")]
        public const string READ_POWER_QUALITY_OVERCURRENT_PERCENT_THRESHOLD_COMMAND_COMPLETE = "50A432F5323B4c1cA0B0F064791BA45C"; //v3.0 - use READ_POWER_QUALITY_CONFIGURATION_COMMAND_COMPLETE

        [Obsolete("v3.0 - use SET_POWER_QUALITY_CONFIGURATION_COMMAND_COMPLETE")]
        public const string SET_POWER_QUALITY_OUTAGE_DURATION_THRESHOLD_COMMAND_COMPLETE = "A2CE552EAD2C4a3cBA84B54CCAAD5AC1"; //v3.0 - use SET_POWER_QUALITY_CONFIGURATION_COMMAND_COMPLETE

        [Obsolete("v3.0 - use SET_POWER_QUALITY_CONFIGURATION_COMMAND_COMPLETE")]
        public const string SET_POWER_QUALITY_SAG_SURGE_DURATION_THRESHOLD_COMMAND_COMPLETE = "9554BBE2CD9F464dA9A4F038B794B0F9"; //v3.0 - use SET_POWER_QUALITY_CONFIGURATION_COMMAND_COMPLETE

        [Obsolete("v3.0 - use SET_POWER_QUALITY_CONFIGURATION_COMMAND_COMPLETE")]
        public const string SET_POWER_QUALITY_SAG_VOLTAGE_PERCENT_THRESHOLD_COMMAND_COMPLETE = "9AD8E6F84A1A40ccB5344A0D56B46776"; //v3.0 - use SET_POWER_QUALITY_CONFIGURATION_COMMAND_COMPLETE

        [Obsolete("v3.0 - use SET_POWER_QUALITY_CONFIGURATION_COMMAND_COMPLETE")]
        public const string SET_POWER_QUALITY_SURGE_VOLTAGE_PERCENT_THRESHOLD_COMMAND_COMPLETE = "F3DFBF4C5BE340a0BE795431B0EC375A"; //v3.0 - use SET_POWER_QUALITY_CONFIGURATION_COMMAND_COMPLETE

        [Obsolete("v3.0 - use SET_POWER_QUALITY_CONFIGURATION_COMMAND_COMPLETE")]
        public const string SET_POWER_QUALITY_OVERCURRENT_PERCENT_THRESHOLD_COMMAND_COMPLETE = "EF4DAD2B3E264e048D95718055531DA1"; //v3.0 - use SET_POWER_QUALITY_CONFIGURATION_COMMAND_COMPLETE

        [Obsolete("v3.0 - use MBUS_ALARMS")]
        public const string MBUS_DEVICE_ALARMS = "aebf13a8eabe451a892de6bf67c1263e"; //v3.0 - use MBUS_ALARMS

        [Obsolete("v3.0 - use READ_INTERNAL_EXPANSION_MODULE_CARD_CONFIGURATION_COMMAND_COMPLETE")]
        public const string READ_MEP_CARD_CONFIGURATION_COMMAND_COMPLETE = "51fbdf80ca674156aff61f1fc5d67cea"; //v3.0 - use READ_INTERNAL_EXPANSION_MODULE_CARD_CONFIGURATION_COMMAND_COMPLETE

        [Obsolete("v3.0 - use INTERNAL_EXPANSION_MODULE_CARD_INSTALLATION_OR_REMOVAL")]
        public const string MEP_CARD_INSTALLATION_OR_REMOVAL = "581b5ac07e474811890becb076a8ffeb"; //v3.0 - use INTERNAL_EXPANSION_MODULE_CARD_INSTALLATION_OR_REMOVAL

        [Obsolete("v3.0 - use AUTO_DISCOVERY")]
        public const string MEP_AUTO_DISCOVERY = "faaf691349c54630af1f91f1275cc97a"; //v3.0 - use AUTO_DISCOVERY

        [Obsolete("v3.0 - use READ_POWER_QUALITY_COMMAND_COMPLETE")]
        public const string POWER_QUALITY_COMMAND_COMPLETE = "10becfacb599459cacc920cb4fc6bce5"; //v4 - use READ_POWER_QUALITY_COMMAND_COMPLETE

        [Obsolete("v4.1 - use CONTROL_RELAY_STATUS_CHANGE")]
        public const string CONTROL_RELAY_ACTIVATED = "aec6c8c2237e469aa0d1081f18486354"; //v4 - use CONTROL_RELAY_STATUS_CHANGE;

		[Obsolete("v5.0 - not replaced")]
		public const string READ_INTERNAL_EXPANSION_MODULE_CARD_CONFIGURATION_COMMAND_COMPLETE = "51fbdf80ca674156aff61f1fc5d67cea"; //v5.0 - not replaced

        [Obsolete("v5.1 - no replacement")]
        public const string UNEXPECTED_DELTA_LOAD_PROFILE_AVAILABLE = "a50cb1ae1a5a4c81a5c0f428164d1fda"; //v5.1 - no longer used

        [Obsolete("v5.1 - no replacement")]
        public const string UNEXPECTED_FULL_LOAD_PROFILE_AVAILABLE = "e29cf712f35e4bc8b183ad3fffd0a869"; //v5.1 - no longer used

		[Obsolete("v5.2 - use INAVLID_PASSWORD")]
		public const string INVALID_PASSWORD_OVER_OPTICAL = DeviceEvents.INVALID_PASSWORD;

		[Obsolete("v5.2.000 - use SET_DISCONNECT_THRESHOLDS_CONFIGURATION_COMMAND_COMPLETE")]
        public const string SET_PRIMARY_MAXIMUM_POWER_LEVEL_COMMAND_COMPLETE = "93af3eebb36742a89d6a12cd9e7274b8";
		[Obsolete("v5.2.000 - use SET_DISCONNECT_THRESHOLDS_CONFIGURATION_COMMAND_COMPLETE")]
		public const string SET_MAXIMUM_POWER_LEVEL_DURATION_COMMAND_COMPLETE = "95e559c12e914ce6be7d199ad9f63c69";
		[Obsolete("v5.2.000 - use READ_DISCONNECT_THRESHOLDS_CONFIGURATION_COMMAND_COMPLETE")]
		public const string READ_PRIMARY_MAXIMUM_POWER_LEVEL_COMMAND_COMPLETE = "164fb5c6de3546eba985de5d71e984a9";
		[Obsolete("v5.2.000 - use READ_DISCONNECT_THRESHOLDS_CONFIGURATION_COMMAND_COMPLETE")]
		public const string READ_MAXIMUM_POWER_LEVEL_DURATION_COMMAND_COMPLETE = "439b97c9dfdc499289128038ec83985e";
		[Obsolete("v5.2.000 - use SET_DISCONNECT_THRESHOLDS_CONFIGURATION_COMMAND_COMPLETE")]
        public const string SET_PRIMARY_MAXIMUM_POWER_STATUS_COMMAND_COMPLETE = "2bbb57575b8d49e980f1837e9682cc75";
		[Obsolete("v5.2.000 - use READ_DISCONNECT_THRESHOLDS_CONFIGURATION_COMMAND_COMPLETE")]
		public const string READ_PRIMARY_MAXIMUM_POWER_STATUS_COMMAND_COMPLETE = "BBE31E1ECE9A44cc91BC66BA9AD89DEA";
        ////////////////////////////////////////////////////////////////////////////////////////
	}

	public class GatewayEvents
	{
		public const string CONNECTION_ESTABLISHED = "16e7a62c458c48189d4ce6249264512d";
        public const string CONNECTION_RELEASED = "cb9678d369b547d8bf4ee51e6df81455";
		public const string TOTAL_ENERGY_DATA_AVAILABLE = "a0e09a0454e04a89bceb469ef18b2348";
        public const string SET_PPP_USERNAME_COMMAND_COMPLETE = "BCE91DAD62804a4e8824E7C3433A269E";
        public const string SET_PPP_PASSWORD_COMMAND_COMPLETE = "5B4D3E50144D41f68EA1EAFC4EC32F21";
        public const string UPDATE_FIRMWARE_COMMAND_COMPLETE = "B2F41EB2832744a3AB03B2C1DFD48082";
        public const string READ_GATEWAY_TO_SERVER_IP_ADDRESS_COMMAND_COMPLETE = "2dd2f06283424a749fa4eeead0d54771";
        public const string SET_GATEWAY_TO_SERVER_IP_ADDRESS_COMMAND_COMPLETE = "bcc89beebfc54943abae9ee1fd51b696";
		public const string READ_STATISTICS_COMMAND_COMPLETE = "cd091b7129aa410e987d00ee4a5e9f94";
		public const string SET_TOTAL_ENERGY_STATUS_COMMAND_COMPLETE = "7da4d95936df4d5b8951828c562ce7b9";

		//new in v2.0
		public const string BROADCAST_DISCONNECT_CONTROL_RELAY_COMMAND_COMPLETE = "232cb0c5b7b74916b84e559d4f4c0f1f";
		public const string BROADCAST_CONNECT_CONTROL_RELAY_COMMAND_COMPLETE = "7a056e9329c2425aba609006727f34f9";
		public const string BROADCAST_SET_PRIMARY_MAXIMUM_POWER_STATUS_COMMAND_COMPLETE = "73fe67b9e6a74b58a3c2e7ee28aece07";
		public const string READ_FIRMWARE_VERSION_COMMAND_COMPLETE = "022d4e1cbdaa4890b509030354d5f67b";
		public const string GENERAL_FAILURE_DATA_STILL_AVAILABLE = "ba19e6ab3fec45abac093bb7035ebbc1";

		//New in v2.1
		public const string READ_REPEATER_PATHS_COMMAND_COMPLETE = "4505d8b112754f2e8f4731313836116d";
		public const string REBOOT_COMMAND_COMPLETE = "8b2aa26e527b49afa48134cc4115f8d3";
		public const string DELETE_WAN_CONFIGURATION_COMMAND_COMPLETE = "03f9796464f94bb499145b04491267d3";
		public const string CONNECTION_FAILURE = "d95cfb52576d4f8f83b37dfff53bffa3";

		//New in v2.2
		public const string INVALID_IP_ADDRESS = "306af8ec1a0f4d08a58d9d6b7af6497c";

		//New in V2.3
        public const string POWER_LINE_CARRIER_MESH_NETWORK_STATUS_DETECTED = "3def0023c2db41cb9d50785d215fc2f4";

		//For Read and Set Gateway process COnfiguration
		public const string READ_PROCESS_CONFIGURATION_COMMAND_COMPLETE = "9eede710db664a8a9a01e5f00cdf72b8";
		public const string SET_PROCESS_CONFIGURATION_COMMAND_COMPLETE = "b31068b8bd6a418d8771080613d70b05";

		//For ReadGatewayDiscoveredDevices
		public const string READ_DISCOVERED_DEVICES_COMMAND_COMPLETE = "0f6dc335fe914305ba859b2d35e58433";

		//For ATM Event Processing
        public const string NEW_DISCOVERED_DEVICES = "397d0d21d5a8484d8e96ad9d60fe1699";
		public const string NEW_ORPHANED_DEVICES = "a12caef6cafc4c09beb3a1cfcf44db9e";
        public const string NEW_DISCOVERED_GATEWAYS = "f25572b686014c729ab457c5e7c95434";

		//For Gateway WAN Configuration
		public const string READ_WAN_CONFIGURATION_COMMAND_COMPLETE = "eeefd82d143c4bd598a55eb933a81ac6";
		public const string WAN_CONFIGURATION_MISMATCH = "31a263a35ada458484b0ad5d5e6fc54a";
		public const string SET_WAN_CONFIGURATION_COMMAND_COMPLETE = "F27911244AA741899A548521C41D46A3";

		//New DC-1000 Events in 2.1
		public const string CAPACITY_CHANGED = "24f9be63b4d1419395abd829779287c4";
		public const string WAN_FAILURE = "78469a992dbd4323b93b2ab6626e88b6";
		public const string WAN_SWITCH = "ff3888ccda0f4f1696f2107e225ba4aa";
		public const string UNANSWERED_CALL_ALERT = "2f6fc88d7b084da8ab2acd6f8bb6c434";

		public const string SET_DEVICE_LIMIT_COMMAND_COMPLETE = "d457b5109ee04427b4f96164f8f544c1";
		public const string READ_DEVICE_LIMIT_COMMAND_COMPLETE = "3bd1689a40a942c1ba2a9c1c6be29fb1";

		public const string UNKNOWN_CONNECTION = "0d68e424019e41dcbdd75677728b798c";
		public const string IP_ADDRESS_CHANGED = "72dcafb9a1df45ae8beec1d1cfa7285f";

		//New in 3.0
		public const string FLASH_LOW = "e48d0268333045aebc33515df2f2fab2";
		public const string RESOURCE_EXHAUSTION = "265ee17fc1c848c695c71646067d7b03";
        public const string READ_EVENT_CONFIGURATION_COMMAND_COMPLETE = "95ae757ce207413bbf307555cec98241";
        public const string SET_EVENT_CONFIGURATION_COMMAND_COMPLETE = "5bee9d2ba5af4e36a21e3c3dbc12575b";

        public const string HARDWARE_FAILURE = "63976eb22a434affbe7556c1e0f7a86f";
        public const string SOFTWARE_FAILURE = "d9500617d3f64aa7a91d331482f83d33";
        public const string DEVICE_COMMUNICATION_STATUS = "041cb4c4d7ac432c97328afbe7ceb616";
        public const string RECEIVED_DEVICE_CAUSE_CODE = "a1a66ac0e54941b087cec156c5fdd71e";
        public const string DETECTED_SEGMENT_DOWN = "d5daa7f085d84b35b0a2014aa59c6c5f";
        public const string UNPLANNED_REBOOT = "444a0b86e34b4187bb46c405375c62cb";
        public const string DEVICE_COMMUNICATION_FAILURE = "56a291c7b8ab4f08b47632db655e54dc";
        public const string SERVICE_TOOL_CONNECTED = "aa312203d1cc49399e2aeeae4252fe4b";
        public const string DETECTED_DEVICE_PHASE_CHANGE = "617788753a1c48e58eb900b7fb9627fe";
        public const string DETECTED_DEVICE_CLOCK_ERROR = "5cf7eddfe37d46bd8683b57d65d499c4";
        public const string DETECTED_DEVICE_PHASE_INVERSION = "33ebafd7f4544234b87bc15ea5628529";
        public const string PLANNED_REBOOT = "20326b9da4494adb99bfea70dfbdd033";
        public const string SECURITY_EXCEPTION = "f29bbfde79e040289f24868e855d44d3";
        public const string WAN_EXCEPTION = "30674f7aacf042fab7deaca897bd0495";
        public const string DETECTED_DEVICE_INVALID_DATA_AVAILABLE = "4379f9e9404d469fa91318b60eb7e64d";
        public const string PHASE_OUTAGE = "62cc50f695a8482096a3c561f1bf5f21";

        public const string READ_POWER_LINE_CARRIER_COMMUNICATION_CONFIGURATION_COMMAND_COMPLETE = "117c08a07dbd40109138cb7f20ae0df5";
        public const string SET_POWER_LINE_CARRIER_COMMUNICATION_CONFIGURATION_COMMAND_COMPLETE = "441bf6a882e0417aac1396caf2454421";

        public const string CAUSE_CODE = "21fc4890e57e4edcbf27ba9450054af8";
        public const string UPDATE_FIRMWARE_COMPLETE_POST_PROCESSING_FAILURE = "997cf8177e5a4729938e2d5ffffe5783";
		public const string POWER_RESTORATION_DATA = "2f1f76db6c39472687fcbf679cf0c6dc";
        public const string INITIAL_COMMUNICATION_FAILURE = "92827859834c4cbb822c69cc6ec527ac";

        //New in v4
        public const string READ_ATM_CONFIGURATION_COMMAND_COMPLETE = "88e9c07580ad4a8fa30970318a1e288a";
        public const string SET_ATM_CONFIGURATION_COMMAND_COMPLETE = "73703ee2f5b743f28bfad124d6508a01";
        public const string ASSIGN_OPERATIONAL_PROFILE_COMMAND_COMPLETE = "e86b1b53413249a8b5a27c38a7766024";
        public const string OPERATIONAL_PROFILE_CHANGED = "09fd0d66cea34568b880750771078c81";

        //V4.1
        public const string COLLECT_REPLACEMENT_INFORMATION_COMMAND_COMPLETE = "057345db02e04bf3b4df3ac21b290898";

        public const string TIME_SYNCHRONIZATION_NOTIFICATION_THRESHOLD_EXCEEDED = "ee6c860e458749bba9ff2ee8ca980e0a";

        //V5
        public const string READ_LOCAL_DATA_ACCESS_CONFIGURATION_COMMAND_COMPLETE = "8240b2a4c7c74d69bc0922ccd0630ef1";
        public const string SET_LOCAL_DATA_ACCESS_CONFIGURATION_COMMAND_COMPLETE = "ddcbb18600b445e3a5577e4119c3d520";
		public const string LOCAL_DEVICE_CHANGE = "1cb114f970094162832b613ff6d6acd5";
		public const string SSH_CONNECTION_FAILURE = "f73126dae9ab4c708acc7e038477398b";

        public const string READ_FEATURE_ACTIVATION_COMMAND_COMPLETE = "eabf02c6dcfa48af91af842fa80dfca4";
        public const string SET_FEATURE_ACTIVATION_COMMAND_COMPLETE = "ba00f4bd150a487c8334dd6246fdb91f";
		public const string COMMUNICATION_SESSION_FAILURES = "60d63459023f467a852f97fd060bbf00";
		public const string UNEXPECTED_FTP_PORT_CHANGE = "055c12a6e7b4442f8c3aeb66320c4898";
		public const string READ_FTP_CONFIGURATION_COMMAND_COMPLETE = "92aee98d2f784650a7ee574f23c01b30";
		public const string SET_FTP_CONFIGURATION_COMMAND_COMPLETE = "e3c87ed3f0ea4fac83bca4188ba2f06b";
        public const string TEMPERATURE_ALERT = "f362e5e8b216478e8c396b2f88a7e6c3";
        public const string CONFIGURATION_ERROR = "1c64c75bf8364c8fb9b992008f49bdf6";
        public const string DETECTED_DEVICE_BROWN_OUT = "4d4597bdd7274387b69667d6df65b723";
        public const string MODEM_STATUS_UPDATE = "ad3f5cea0306459282293ff2e34ba7db";
        public const string EXTERNAL_EVENT_0 = "e85ae3ed45ae4915a501cdfd015a0b26";
        public const string BROADCAST_WRITE_DEVICE_DATA_COMMAND_COMPLETE = "183934bba41047e6bee125ab866dabeb";
		public const string SEND_FILE_COMMAND_COMPLETE = "e6e4b6fe25304357adc3234ee57eeefa";
		public const string SEND_FILE_COMPLETE_POST_PROCESSING_FAILURE = "0d79771324c748379448f645ab005a2f";
		public const string APPLICATION_DATA_AVAILABLE_0 = "72a3dc267b7d4a068f4a6f524c66937b";
		public const string APPLICATION_DATA_AVAILABLE_1 = "c11f2860fc3e4d97a0174f7b9354a0d6";
		public const string APPLICATION_DATA_AVAILABLE_2 = "4625563353c241ee962da524ed0c28e6";
		public const string APPLICATION_DATA_AVAILABLE_3 = "3f09005f659f486f8ce355a5fec7f038";
		public const string APPLICATION_DATA_AVAILABLE_4 = "1981a173437f4a1bad9b38486709bbde";
		public const string APPLICATION_DATA_AVAILABLE_5 = "099b67ed557941dab3e643556caa2dcf";
		public const string APPLICATION_DATA_AVAILABLE_6 = "834527d6b1704dec8b7915ca3eb170a7";
		public const string APPLICATION_DATA_AVAILABLE_7 = "0bbde5f522d04743b3ec0becd22556f9";
		public const string APPLICATION_DATA_AVAILABLE_8 = "faa49ff6d9bd4d059cbc96495aa099b5";
		public const string APPLICATION_DATA_AVAILABLE_9 = "d35f2a3c52df4e13b4e59d5e4a97e0d7";
		public const string APPLICATION_DATA_AVAILABLE_10 = "984dae7176d248d7b881f35788ac519f";
		public const string APPLICATION_DATA_AVAILABLE_11 = "b2655782f54948e3b1fbfe7b33fc11cc";
		public const string APPLICATION_DATA_AVAILABLE_12 = "2658f17b663d4baa852ca144baf3bd77";
		public const string APPLICATION_DATA_AVAILABLE_13 = "8ac65c6139fa4ec1a004e115c9cff0c1";
		public const string APPLICATION_DATA_AVAILABLE_14 = "048894357b394416a6b760b5016cb892";

		public const string READ_GPS_COORDINATES_COMMAND_COMPLETE = "1577972e3f10494ba109f96a7c3905b9";
		public const string GPS_COORDINATES_REPORTED = "05b37d1a4f7d4f43894f8c9ef5af8fd2";

		public const string CLIENT_PROCESS_RESTART_OCCURRED = "33766e58e8274040a6698acc35262da8";
		public const string CONFIGURATION_STATE_CHANGE_REPORTED = "0f1853ae54754790b299799948bf1747";
		public const string CRITICAL_MESSAGE_LOG_REPORTED = "7ffc716a01b64094b693f6754a519648";
		public const string WIRELESS_MAC_ID_LOGIN_FAILURE = "da3ecbc7af2742fab3c1023a4f80e7de";
		public const string WIRELESS_PROTECTED_ACCESS_LOGIN_FAILURE = "5b67aabdeff5432f937f52191ca11095";
		public const string COMMUNICATION_PROTOCOL_SECURITY_LOGIN_FAILURE = "0e3a3735db0c4c579060343f44fc610c";
		public const string COMMUNICATION_PROTOCOL_SECURITY_LOCKOUT = "105dac72bf8f48a69ce62e750b19cc8f";


		//V5.1
		public const string TAMPER_DETECTED = "9b5763de06ee4540a5e502e05026d7e5";
        public const string DETECTED_INVALID_DEVICE_LOAD_PROFILE_DATA_SET_COLLECTION_REQUESTED = "90a3ac15a8314a2eaa1298ab598d231a";
		public const string SET_SETTINGS_COMMAND_COMPLETE = "752759d822354e00868dc8bb0b600640";
		public const string READ_SETTINGS_COMMAND_COMPLETE = "f1b10d309e4b457e90144cca98ed9dda";


        //V5.3
        public const string READ_LVGM_COMMAND_COMPLETE = "51f373a3227c492194c1bb3fb885f1ad";
        public const string READ_LVGM_CONFIGURATION_COMMAND_COMPLETE = "8b652863da8c4d2dac74a1d3770aceb5";
        public const string SET_LVGM_CONFIGURATION_COMMAND_COMPLETE = "32f2a8d08d8b46e985699ccf9613aff3";
		public const string READ_PHASE_INFORMATION_COMMAND_COMPLETE = "bebdfb252c1b4ccbae319b9701a39eaf";
		public const string SET_PHASE_INFORMATION_COMMAND_COMPLETE = "f79baee27f2b496fa05b5063a92b6203";
		public const string HARDWARE_CONFIGURATION_CHANGE = "a5ac8d60f1124aa58003ee9124127652";
		public const string READ_WAN_STATUS_COMMAND_COMPLETE = "61e4db67b09a4064b565fd88908d3000";
		public const string SET_GATEWAY_KEY_COMMAND_COMPLETE = "dbb14d8f6a73495b878b67ef98233b33";

		////////////////////////////////////////////////////////////////////////////////////////
		//The following are deprecated - the version in which they were deprecated and the    //
		//constant replacing them (if any) is listed to the right of the constant.            //
		////////////////////////////////////////////////////////////////////////////////////////

		[Obsolete("v2.3 - use SET_WAN_PHONE_NUMBER_COMMAND_COMPLETE")]
		public const string SET_PHONE_NUMBER_COMMAND_COMPLETE = "338D71EE6D224a10AEA6142A9A3131E0";	//v2.3 - use SET_WAN_PHONE_NUMBER_COMMAND_COMPLETE

		[Obsolete("v2.3 - use BROADCAST_SET_PRIMARY_MAXIMUM_POWER_STATUS_COMMAND_COMPLETE")]
		public const string BROADCAST_SET_MAXIMUM_POWER_LEVEL_STATUS_COMMAND_COMPLETE = "73fe67b9e6a74b58a3c2e7ee28aece07"; //v2.3 - use BROADCAST_SET_PRIMARY_MAXIMUM_POWER_STATUS_COMMAND_COMPLETE

        [Obsolete("v3.0 - no longer used")]
        public const string READ_GATEWAY_TO_SERVER_MODEM_INIT_STRING_COMMAND_COMPLETE = "acf0ea51d03d4f2f9220ceffc82ced3f"; //v3.0 - no longer used

        [Obsolete("v3.0 - no longer used")]
        public const string READ_GATEWAY_TO_SERVER_MODEM_TYPE_COMMAND_COMPLETE = "469eef112d9c43f79d0debbceb4240ac";    //v3.0 - no longer used

        [Obsolete("v3.0 - no longer used")]
        public const string READ_GATEWAY_TO_SERVER_PHONE_NUMBER_1_COMMAND_COMPLETE = "c63e377e9c0b4459b03f0afd60892004";    //v3.0 - no longer used

        [Obsolete("v3.0 - no longer used")]
        public const string READ_GATEWAY_TO_SERVER_PHONE_NUMBER_2_COMMAND_COMPLETE = "1640aabde50f4121bf8939bb3f0b7640";    //v3.0 - no longer used

        [Obsolete("v3.0 - no longer used")]
        public const string SET_IP_ADDRESS_COMMAND_COMPLETE = "c452c6f1b95840b88a9a8f6ae1b72e52";   //v3.0 - no longer used

        [Obsolete("v3.0 - no longer used")]
        public const string SET_SECURITY_OPTIONS_COMMAND_COMPLETE = "35a1980a0f574936b10d53df9bcf0a17"; //v3.0 - no longer used

        [Obsolete("v3.0 - no longer used")]
        public const string SET_GATEWAY_TO_SERVER_MODEM_INIT_STRING_COMMAND_COMPLETE = "8ed0a80aa9774f578479c0e6b5d2da89";  //v3.0 - no longer used

        [Obsolete("v3.0 - no longer used")]
        public const string SET_GATEWAY_TO_SERVER_MODEM_TYPE_COMMAND_COMPLETE = "518a7ae6cf3f487690fbecb284426909"; //v3.0 - no longer used

        [Obsolete("v3.0 - no longer used")]
        public const string SET_GATEWAY_TO_SERVER_PHONE_NUMBER_1_COMMAND_COMPLETE = "0a6d540769554399aaa1022df954e6ee"; //v3.0 - no longer used

        [Obsolete("v3.0 - no longer used")]
        public const string SET_GATEWAY_TO_SERVER_PHONE_NUMBER_2_COMMAND_COMPLETE = "0e71c1e80add4e049e239d49d23654cd"; //v3.0 - no longer used

        [Obsolete("v4.1 - no longer used")]
        public const string SET_WAN_PHONE_NUMBER_COMMAND_COMPLETE = "338D71EE6D224a10AEA6142A9A3131E0";

        [Obsolete("v5.0 - use POWER_LINE_CARRIER_MESH_NETWORK_STATUS_DETECTED")]
        public const string MESH_STATUS_DETECTED = "3def0023c2db41cb9d50785d215fc2f4"; //This is for a Power Line Carrier (PLC) mesh.

		[Obsolete("v5.3 - no replacement, no longer used")]
		public const string READ_CONTROLLER_CONFIGURATION_COMMAND_COMPLETE = "a95a746c99534c2f9c6213978fc9f4ab";
		[Obsolete("v5.3 - no replacement, no longer used")]
		public const string SET_CONTROLLER_CONFIGURATION_COMMAND_COMPLETE = "8de5a5612fdd4deba107d065c9c7ea5b";
		[Obsolete("v5.3 - no replacement, no longer used")]
		public const string CONTROLLER_CONFIGURATION_VERSION_CHANGED = "69b24ad06cf34f748fc27294e4e9dfdd";
		[Obsolete("v5.3 - no replacement, no longer used")]
		public const string CONFIGURATION_STATE_CHANGED = "7903b807bbbd43beb7fefa3b7a7c5482";
		[Obsolete("v5.3 - no replacement, no longer used")]
		public const string REPLACEMENT_CONTROLLER_CONFIGURATION_UPDATED = "076ab334c8e34f50a1d08d83baab6cd2";
		[Obsolete("v5.3 - no replacement, no longer used")]
		public const string NODE_TO_NODE_NETWORK_MINIMUM_PEER_COUNT_THRESHOLD_EXCEEDED = "497fe0ee04624ee59c3b7dced336a747";
		[Obsolete("v5.3 - no replacement, no longer used")]
		public const string BACKUP_POWER_THRESHOLD_EXCEEDED = "9f81db5430f5453d915623a841574485";
		[Obsolete("v5.3 - no replacement, no longer used")]
		public const string WIRELESS_ACCESS_POINT_INTRUSION_LOCKOUT_OCCURRED = "b6aab0864e0844aaa9f08b8bd1c2c075";
		////////////////////////////////////////////////////////////////////////////////////////
	}

	public class SystemEvents
	{
        public const string COMMAND_FAILURE = "7bd38a557c4641d7bebe59edbb328064";
		public const string ENGINE_STARTED = "a89ab0bab7344d26bc4236a0d2c309e1";
		public const string ENGINE_STOPPED = "11d73ac3f2bb4093b447ba68f5266d8b";
		public const string SECURITY_ALERT = "3745625CAFA24ad989472607AB0A28E0";
		public const string SERVER_ERROR = "e3501923ef634306b0c5592ff53b2f00";

        //New in v4
        public const string ATM_PROCESS_DETECTED_INVALID_GATEWAY_VERSION = "c63352f1392746ab9e54bf08578bdfbe";
        public const string ATM_PROCESS_FAILED_TO_ASSIGN_DEVICE = "f05b056396dc4c3c9d5b8fd1a92c88f6";

        //New in v4.1
        public const string UNSUPPORTED_VERSION_DETECTED = "2f41214ffaae4465bf284e27149e0918";

        //New in v5.0
        public const string COMMAND_CANCELLED = "556179039e894456add05f3b68d59f5c";
		public const string ARCHIVE_PROCESSING_COMPLETE = "4f6a8808f3a04a6e9b95f0f3be62a7f3";
		public const string ORPHANED_COMMAND_HISTORY_PROCESSING_COMPLETE = "dd5676368e7045bd8452fe7e1b3c81ad";
        public const string IMPORT_PROCESSING_COMPLETE = "49c2f36864d145d7b3c5088734e66d1d";
        public const string EXPORT_PROCESSING_COMPLETE = "f955079405e64249841ba566e9f9643f";
		public const string COMMANDS_QUEUED = "fde7ca5fe2ac4099a57be8f858ff7890";

        ////////////////////////////////////////////////////////////////////////////////////////
		//The following are deprecated - the version in which they were deprecated and the    //
		//constant replacing them (if any) is listed to the right of the constant.            //
		////////////////////////////////////////////////////////////////////////////////////////

		[Obsolete("v2.2 - use GatewayEvents.IP_ADDRESS_CHANGED")]
		public const string GATEWAY_IP_ADDRESS_CHANGED = "72dcafb9a1df45ae8beec1d1cfa7285f";	//v2.2 - use GatewayEvents.IP_ADDRESS_CHANGED

		[Obsolete("v3.0 - no longer used")]
		public const string RECOVER_ENGINES_COMMAND_COMPLETE = "8b41562cb08649ffa2bb780bba38bca6";	//v3.0 - no longer used

        [Obsolete("v3.0 - no longer used, replaced by individual GatewayEvents & DeviceEvents constants")]
        public const string DC1000_EVENT = "748af8ef281c4428b0e3f8cc2d8fe0cf"; //v3.0 - no longer used, replaced by individual GatewayEvents & DeviceEvents constants
		////////////////////////////////////////////////////////////////////////////////////////
	}

    public class DC1000Resources
    {
        public const string NM_NODE = "36a5f19a66bc46f4b13964367ad1021d";
        public const string DCX_RESOURCE_STATUS = "d4a0d220db26496db1b702833240b7f8";
        public const string DCX_NODE_STATUS = "396b6b8fb33b48978115235add3c34dc";
        public const string DCX_DEVICE_CONFIG = "82e64ac83ebc4cc8935ffd41408d3e25";
        public const string DCX_DEVICE_STATUS = "91e0ec8d1f4a45e3a70e22f0a6bc7fc5";
        public const string DCX_DEVICE_UPDATE = "86e15389b2664fca8728bc55d19d8eba";
        public const string DCX_SEGMENT_CONFIG = "a784e1ab989640a9ae186540344805ce";
        public const string DCX_SEGMENT_STATUS = "3616bc805f8b465aa1a987389b8fbfef";
        public const string DCX_EVENT_CONFIG = "7205a16eaac348648565a9924ef17f02";
        public const string DCX_EVENT = "eaf730f1a6064e4aa1117892be28ad57";
        public const string DCX_FUNCTION_CONFIG = "b8ec04b9d0db4735b836018fcb9e7cc0";
        public const string DCX_FUNCTION_STATUS = "e54ec57778654403b909a34dd2f71f96";
        public const string DCX_OPERATION = "fc64e88b608c4220be1297f2f3d34cfd";
        public const string DCX_OPERATION_STATUS = "988c0212a4224febb38d0348b3606354";
        public const string DCX_RESULT = "ac826f9a39ea4d1481f533ae46fcce48";
        public const string DCX_MESSAGE = "ae87304e6fd74bb4976ca484c4b0017b";
        public const string DCX_TIMEZONE_CONFIG = "165deff7510a4c03bfce7886581b5269";
        public const string DCX_TIMEZONE_STATUS = "e4924d221c884e98ae26e91eafd8d889";
        public const string DCX_DATA = "e719104a17a6487ab4f9e5b044f9274f";
        public const string DCX_MEP_DEVICE_CONFIG = "f1c70f26857d478fa865c25c4ff1563a";
        public const string DCX_MEP_DEVICE_STATUS = "123cbd87def44a37bf94189ae1850ee8";
        public const string DCX_DISCOVERED_DEVICE = "dfd18367d5394c96a5f2e8092505ea4d";
        public const string DCX_WAN_CONFIG = "77f826bf5b2547d881032452729be48a";
        public const string DCX_WAN_STATUS = "f4c55e21e3f543069d8045811ed974a3";
        public const string DCX_IDR = "5c9e378d83ce4550836662d8c0b84e47";
        public const string DCX_MEP_UPDATE = "c19063e452e145d1bda27f380b34506e";
        public const string DCX_DEVICE_PROGRAM_ID = "41903df9b6014b969e4cbbfcef591837";
        public const string DCX_DEVICE_UPDATE_STATUS = "79b789c2d0d949c4b50056ccd073e466";
        public const string DCX_MEP_UPDATE_STATUS = "f37725d31ea74aedb5cbdd2965f3e1cd";
		public const string DCX_LOCAL_DATA_GROUP = "dc90568eb2b64cb89325492a1ff2af12";
		public const string DCX_LOCAL_DATA_CONFIG = "9512cfcdbfa74fd6b4a7a92570e94c33";
		public const string DCX_LOCAL_DATA_STATUS = "e7de9e6df8aa4ec68de3374aede69adc";
		public const string DCX_LOCAL_EVENT = "b4a54be26bb74014912f19d56699b204";
        public const string DCX_DC_UPGRADE_CONFIG = "f4d54e26b36e40c9a7ce7a65d9711bff";
        public const string DCX_DC_UPGRADE_STATUS = "cc3bb970777f4881a7e0927ee12112f4";
    }

	// Contains the list of Process Configuration Status Types
	public class ProcessConfigurationStatus
	{
		public const int ENABLED = 0;
		public const int DISABLED = 1;
	}

	public class ApplicationLevelAuthenticationStatusTypes
	{
		public const string ENABLED = "dadc479ad4f646eb9028e9f3f0a75bbb";
		public const string DISABLED = "e8681b9c315d4ef6bb06370684867c1c";
	}

	public class GatewayEncryptionStatusTypes
	{
		public const string ENABLED = "33168a0ef643440fab780af1541f9664";
		public const string DISABLED = "cca636daca974106a9cef938f48cfa5d";
	}

	public class GatewayWANConfigurationStatusTypes
	{
		public const string ENABLED = "6994d3fecb6243eb8e65e1f3801b4538";
		public const string DISABLED = "4c0662cc8ba04698914e6e9c6e8521d4";
	}

	public class GatewayOutboundCHAPStatusTypes
	{
		public const string ENABLED = "b310ae6f5520447aafc26fccd984c800";
		public const string DISABLED = "6f9abc0646b140d88903dc7f4c0036bb";
	}

	public class GatewayInboundCHAPStatusTypes
	{
		public const string ENABLED = "848d52e083b1422997e1524712e883ba";
		public const string DISABLED = "8769e901729c47b7bf48a63b3c2a8c24";
	}

	public class GatewayPAPStatusTypes
	{
		public const string ENABLED = "6183031e351e46c8bcda3274aba9235c";
		public const string DISABLED = "8716bb2a316945e486da007b528fbc12";
	}

	public class GatewayPortSpeedTypes
	{
		public const string SLOW = "a14b7e23fa2e44e2b15020e09c92b55a";
		public const string FAST = "2262d756893248f0a1f0b4f8632de95b";
	}

	public class GatewayModemCommandTypes
	{
		public const string AT_COMMANDS = "f415d58490b9419b8a572cae8cabce32";
		public const string DIRECT_CONNECTION = "b6aa5e29421749b5bdf6203abc7d8dca";
	}

	public class GatewayModemConnectTypes
	{
		public const string TEMPORARY = "bac326ec2c974c34838511c8b23354de";
		public const string PERMANENT = "8cf551f390794a0781cf51db5c1bded1";
	}

	public class GatewayPPPConnectionTypes
	{
		public const string NO_RECONNECT = "4b9a3ed202244468a5e7308c7d1c4509";
		public const string RECONNECT = "847e0a6e585c4fbdaba9ebb77d5816b4";
	}

    public class GatewayPowerLineCarrierCommunicationStatusTypes
    {
        public const string ENABLED = "fc7b60db48374cf8b1d0f602fb03fe7a";
        public const string DISABLED = "80685663e1cc4926958642f4817b862b";
    }

	public class GatewayModemAnswerTypes
	{
		public const string AUTO_ANSWER = "265884817e204449aec532f0356c3c07";
		public const string OUTBOUND_ONLY = "f6361f6d5af840289054f16a9592d09a";
	}

	public class GatewayAuthenticationTypes
	{
		public const string PPP = "93f28d87529e40879394577f0b5f2662";
		public const string ISP = "9a85f829955d41bc9ccc87f727c314a0";
	}

    public class PowerQualityStatisticTypes
    {
        public const string MINIMUM_AND_MAXIMUM_FREQUENCIES = "af11482536bb4074a9aa283e99df2351";
    }

    public class LocalDataAccessConfiguredOnDC
    {
        public const int YES = 1;
        public const int NO = 0;
    }

    public class LocalDataCollectionStoppedReasonTypes
    {
        public const string GATEWAY_VERSION_NOT_SUPPORTED = "78888657abf14b3f861d0633f2b9a913";
        public const string SET_LOCAL_DATA_ACCESS_CONFIGURATION_COMMAND_FAILED = "86927a30b4db4e04b6dc85376f09b4c0";
    }

	public class ProcessConfigurationID
	{
		public const int DISCOVER_DEVICES = 18;
		public const int PRIORITY_FIND_ORPHANS = 19;
        public const int CONTINUOUS_EVENT_LOG = 23;
        public const int PRIORITY_CONTINUOUS_EVENT_LOG = 24;
		public const int LOW_VOLTAGE_GRID_MAP = 48;
	}

	// Commands available to a TaskProcessor
	public class TaskProcessorCommands
	{
		public const string START_ENGINE = "ec1ef82ec7ec4704b117bf3315cc89ca";
		public const string STOP_ENGINE = "338b167a563540a495e38492dc586bfc";

		////////////////////////////////////////////////////////////////////////////////////////
		//The following are deprecated - the version in which they were deprecated and the    //
		//constant replacing them (if any) is listed to the right of the constant.            //
		////////////////////////////////////////////////////////////////////////////////////////

		[Obsolete("v3.0 - no longer used")]
		public const string RECOVER = "c100d8f599454e308d0a60352c5fc4e5";	//v3.0 - no longer used
	}

	public class PhaseTypeID
	{
		public const string UNKNOWN = "0e05b04fb4a245269e0fd5a7fee43dbf";
		public const string L1 = "1e5f6731bee04b559e2f16f11a0ce681";
		public const string L2 = "e1b4d3d6de9d477a9c72e7aa5cf639bd";
		public const string L3 = "e736139e387d4905a2f9cca4c9b9af05";
		public const string L1_L2_L3 = "5c90f6ce54ec4e69945299788995d455";
	}

	public class MeterLonTalkKeyStatus
	{
		public const string PENDING = "dae97ad3037d479c84213122e322c187";
		public const string LAST_KNOWN_GOOD = "4fd8350bf13844549eae72b0540f66cb";
	}

    public class OneTimeReadRequestTypes
    {
        public const string METER = "908a1deaec7047baacf6f4bf279308e7";
        public const string ALL_CHILD_DEVICES = "e302f863c2eb440189a8afa320af58d1";
    }

	public class DeviceLoadVoltageStatusTypes
	{
		public const string PRESENT = "ff22a845507f467ca65555d52927b94c";
		public const string NOT_PRESENT = "a8b06b115d704a64b586b19fdd1cc8b4";
		public const string UNKNOWN = "2349e315a1bc400b97f873901550d593";
	}

	public class DeviceManualDisconnectStatus
	{
		public const string ENABLED = "912d149e67574cb1b48481214e7a802e";
		public const string DISABLED = "c5302ea9f7cd4c84b343c29e512c2f38";
	}

	public class DeviceRemoteReconnectStatus
	{
		public const string ENABLED = "bc02c5c78bb348adb6c7f2f60cb46a0e";
		public const string DISABLED = "dd6c5e2310384684ac5957c5f4df8a68";
	}

    public class MBusAndMEPAlarmStatusTypes
    {
        public const string ENABLED = "028d0349cd1048b593d5fbbaf00bb24f";
        public const string DISABLED = "7350b651489f4b0d825090e373113fcb";
    }

	public class DeviceDisconnectSwitchLEDStatus
	{
		public const string ENABLED = "42725613b1464ff6b1700365f33fc087";
		public const string DISABLED = "32d83c18b5f5470895c01f9a839f1fda";
	}

	public class DeviceDisconnectSwitchLEDBehavior
	{
		public const string ALWAYS_ON = "2f10ae0a80c0430b878e68d630744d65";
		public const string ALWAYS_OFF = "a0960b9630c74357a1cb751064516a3d";
		public const string BLINK = "5fa15224a9944fbf8492d8dbbe469e68";
	}

    public class DeviceDisconnectReconnectWithLSVStatusTypes
    {
        public const string ENABLED = "71acc2b0421a4234a55275164a0e7aa7";
        public const string DISABLED = "fefa2628c86c43a7a8d94e22b8d00781";
    }

    public class DeviceDisconnectReconnectWithLSStatusTypes
    {
        public const string ENABLED = "d62117edd86f466c9b0e197d2c629aab";
        public const string DISABLED = "38268324b86f4c37901d8b28efbd7625";
    }

    public class DeviceDisconnectLockedOpenStatusTypes
    {
        public const string LOCKED_OPEN = "a472d4d067c6476cb3c2bcc439534e92";
        public const string UNLOCKED = "f9037f88427d4b29948e50a6b195f5c6";
        public const string UNKNOWN = "9ec0acc2aff44df4a6f94a38e087c236";
    }

    public class DeviceDisconnectFeedbackStatusTypes
    {
        public const int OPEN = 0;
        public const int CLOSED = 1;
        public const int AMBIGUOUS = 2;
        public const int ERROR = 3;
        public const int UNKNOWN = -1;
    }

	public class InitialCommunicationTypes
	{
		public const string COMPLETE = "7ffc3f8959344fbcbb5f65239630216d";
		public const string NOT_COMPLETE = "59ca036643f24386bbac495255d992bb";
	}

	public class EventDeliveryTypes
	{
		public const string SOAP = "3c3d5784292a4e3b9224fcda703a6348";
		public const string NONE = "f5d30980f6794d9299a4153aa01316d8";
	}

	public class EventDeliveryStatuses
	{
		public const string FAILURE = "183a3c523dc6454da08774f5c2ec8c4e";
		public const string SUCCESS = "5f0392101a404c828d7ed6a81a90afff";
	}

	public class EventDefinitionStatuses
	{
		public const string ENABLED = "9a59ae3e83854a4fbbebde54dc9fe31b";
		public const string DISABLED = "28dc3a83582b4272a08c3ec438b099d1";
	}

	public class MaximumPowerEnableTypes
	{
		public const string BROADCAST_ENABLE = "4f24e3216ab245cb8c217bb6c8ec5493";
		public const string BROADCAST_DISABLE = "90774857012f4c22af5bb3e9c393f1d3";
		public const string ENABLED = "A7F520A5FB1441e5847942045E546202";
		public const string DISABLED = "78B0E3B009B54a96A21F61D6770F09F2";
	}

	public class MeterDisplayCategoryTypes
	{
		public const string SELF_READ_DATE_TIME = "6713EDAE75A04799A6FC3E25E3C81531";
		public const string PRESENT_VALUE_INFORMATION = "7853F025C50144b189B5DEF3CBDA412B";
		public const string CURRENT_DATE_TIME = "22DCB2B2C5AF440b87447B2D2ADED564";
		public const string SELF_READ_INFORMATION = "DB22E435D9CC412990B8F69BE5CBB599";
		public const string CURRENT_TOU_CALENDAR_ID = "69D60DF737764855832D4DB936A5AE9F";
		public const string FIRMWARE_VERSION_NUMBER = "B4C494AEC77F4fa2B73829A67D8EE88E";
		public const string PREPAY_CREDIT_REMAINING = "065B922548874fc2B5CCB7BB447DAE8A";

        //V3.0
        public const string ONE_TIME_READ_DATA = "a42266b1393448b090f63c98a4f13b6d";
        public const string HISTORICAL_DEMAND_RESET_DATA = "5621b27e98984554be5e68ed12cc9c94";
        public const string NUMBER_OF_DEMAND_RESETS = "0d88b73b99774def89a106523cb44bf1";
        public const string ONE_TIME_READ_MAXIMUM_DEMAND_DATE_TIME = "ec363e63f8e942d395b404b4a93148e8";
        public const string ONE_TIME_READ_DATE_TIME = "0a17bf40508140d4971a9bc792587e11";
        public const string HISTORICAL_DEMAND_RESET_MAXIMUM_DEMAND_DATE_TIME = "4220dbdb80404a12a03876bf36a937af";
        public const string HISTORICAL_DEMAND_RESET_DATE_TIME = "f334cc85957048deb86ddd5b46c990e3";
        public const string SELF_READ_MAXIMUM_DEMAND_DATE_TIME = "3e875ca368d7430a8bdb3a79c38bf9e3";
        public const string PRESENT_VALUE_MAXIMUM_DEMAND_DATE_TIME = "55925d281dd9482aafc151bb2da8935d";
		public const string DISCONNECT_MAXIMUM_POWER_LIMIT = "24656e84a7524ec1987f0cc8d6c37052";

		//V4.1
		public const string CONTROL_RELAY_ID = "47f083c9dd1846d8b9e5dbedf0e5ee84";
		public const string CT_RATIO_MULTIPLIER = "9d7bd67a0cab4fa58d19d39c57a003dd";
		public const string VT_RATIO_MULTIPLIER = "1fc2ef2f566f40a386385ee3593e8af2";
		public const string CT_RATIO_ACTUAL = "88c1b5da2fcf48e9a87f5d44f03aaac7";

        //v5.0
        public const string DEMAND_SUB_INTERVAL = "6adfa403767542ff94fa2fc40d0f915a";
        public const string DEMAND_INTERVAL = "ec1df94263e24fb4ae719720adfa3e7b";
        public const string LOAD_PROFILE_INTERVAL_TIME = "4994ed3957914f2698f9c9f70ec184bf";

		//5.3
		public const string FIRMWARE_CRC = "6635d48856f146b9a8d698eebf8897f4";
	}

	public class InformationReturnTypes
	{
		public const string DETAIL = "1441a464525d43659c02b3d608175be2";
		public const string GENERAL = "3294f665bc644359bfd15824d2efe29c";
	}

	public class DeviceResultTypes
	{
		public const string END_OF_BILLING_CYCLE_BILLING_DATA = "d357ee7048cd4983a3ee896f0e49c562";
		public const string FULL_LOAD_PROFILE = "60381e83d07945649a86c193cdc40d6b";
		public const string BILLING_DATA_ON_DEMAND = "67a59c9ce2d64e98a8fe0bf34a0d8267";
		public const string POWER_QUALITY = "56f2cb69a8a44247be7a537b754f72af";
		public const string SELF_BILLING_DATA = "ad85befc6cc448bf9863c5cb62007561";
		public const string SECONDARY_BILLING_REGISTERS = "7cf449f68be04523b3fccef2a7781e4e";
		public const string DELTA_LOAD_PROFILE = "1707758FC0134fc187B105D50BF33723";
		public const string DISPLAY_CONFIGURATION = "5FB2472783C745dc8B110BD8E116A99E";
		public const string PULSE_INPUT_CONFIGURATION = "5ddd67c34b0a419e8b7204d16c308e92";
		public const string INSTANTANEOUS_POWER = "6072CB933A0840cbA8FEE91456A5F1CB";
		public const string CONTROL_RELAY = "3b295e082b3d43d2bd2d3908ae7fe164";
		public const string LOAD_STATUS = "65621819be064241bee46d5a38a67f15";
		public const string FIRMWARE_VERSION = "5bc2f8cfdbc846148e517a2d9514248e";
		public const string ACTIVE_TOU_CALENDAR = "b98a6c0ff148489fb4443f654f848bf6";
		public const string PENDING_TOU_CALENDAR = "d57d9409bdbb425b8ab239c04120da78";
		public const string DOWN_LIMIT = "690c5eb6f780485bba43f94c916245c2";
		public const string ALARM_POLLING_RATE = "418fded6134b4ddcbb2333ba6f58e97c";
		public const string BILLING_SCHEDULE = "ED775679030446568E37EFA97927B8D3";
		public const string HISTORICAL_BILLING_DATA = "43bc7004769945399a1a1f645ba09af4";

		public const string STATISTICS = "8978b6ab31ca4a409623a3c977eb0bff"; //For Read Device Statistics command
		public const string AUTO_DISCOVERY_CONFIGURATION = "b6914d3d1b2c4ceaa2091178846f6da0"; //For Read Device Auto Discovery Configuration command
		public const string EVENT_LOG = "413761a463d5411db19c3754c983d49a";
		public const string UTILITY_INFORMATION = "4e16942686b74ce1a8907828aea9779c";
        //V2.3 Results
		public const string PREPAY_CONFIGURATION = "8BD9240D590C4613A3A0DE265501879F";
		public const string TIME_ZONE_CONFIGURATION = "e5bc9ce182594fa4b342615cc690bdc5";
		public const string PREPAY_CREDIT = "66bcaa97329b40b2ae1fade1f97a3de1";

		//V3.0 Results
		public const string DISCONNECT_CONFIGURATION = "f44fe58324794bbd9c165aea96f290bb";
        public const string CONTINUOUS_EVENT_LOG_CONFIGURATION = "df828e0b4a1f43b6afe0423e17741dd6";
        public const string STOP_MODE_CONFIGURATION = "84a4b62523544d8dba2d297cdc7ad400";
        public const string POWER_QUALITY_CONFIGURATION = "9048f7199a1246dab0e7d314fdd2fd39";
        public const string SELF_READ_RETRIEVAL_CONFIGURATION = "7df5217325eb4ae7bb41e6fbd032ef05";
        public const string HISTORICAL_ONE_TIME_READ_DATA = "188693996ab741e18c32444b8f64297f";
        public const string ONE_TIME_READ_DATA = "4240c1e43ba54d9589395e1498857781";
        public const string ONE_TIME_READ_REQUESTS = "e00b9373af304612862441466c6fa9e1";
		public const string CONTINUOUS_DELTA_LOAD_PROFILE_COLLECTION_CONFIGURATION = "312a50cfb2e845bdad6e5b4afc619cc1";
        public const string EVENT_LOG_CONFIGURATION = "f331906b237444cdac579b2300aa2c00";
        public const string DEMAND_CONFIGURATION = "59F5F6D63A4247bb86CE0F164333FEE9";
        public const string LOAD_PROFILE_CONFIGURATION = "bdaa2093616849e3a4c71c6ccfc686f4";
		public const string LOAD_PROFILE_ON_DEMAND = "e728794e88884896860d82637c52799f";
        public const string DEMAND_RESET_DATA = "67278becab6c45a98fa418ca5e18df8b";

        //v4 Results
        public const string ATM_CONFIGURATION = "a3c7b6814ba94383b05f14e13c129456";
        public const string PROVISIONING_IDENTIFIERS = "405047a4c3f84bfd9da22cab771727cf";
		public const string READ_SCHEDULE = "33525ee0fb11467f954177fa91a97dfb";
        public const string DATA_ON_DEMAND = "0b5adc78d83844f6b1fc8b07752e8341";
		public const string MEP_DATA = "99d74efcf04e41549def71e6b134cb94";

        //v4.1 Results
        public const string TIER_CONTROL_CONFIGURATION = "e61034c6714e4e05b2aecd938bd35658";
        public const string DATA_RECORD_HEADER_CONFIGURATION = "22281c0b09a348f1b177169e2e06e34c";
		public const string CONTROL_RELAY_CONFIGURATION = "1ad0dc16eec34c7b86b8404cf9557864";
        public const string DATA_RECORD_HEADER_MAPPING = "17dcdae34a584b2ea64556ab50624e3e";

        //v5.0 Results
        public const string LOCAL_DATA_ACCESS_CONFIGURATION = "98383d268aa24986a204c5934a6f8ee9";

		//v5.0 Results
		public const string MEASUREMENT_RATIO_CONFIGURATION = "75454835b76b4b1b95781628a94f5daa";
        public const string BROADCAST_GROUP_MEMBERSHIP_CONFIGURATION = "2d4b294b300147c09ff835b04792ad5b";

        //v5.1 Results
        public const string LOAD_PROFILE_ON_DEMAND_DATA_SET_1 = DeviceResultTypes.LOAD_PROFILE_ON_DEMAND;
        public const string LOAD_PROFILE_ON_DEMAND_DATA_SET_2 = "7ab0fb7ed82446e6b66dc384221975b5";
        public const string LOAD_PROFILE_ON_DEMAND_DATA_SET_3 = "4cde865aa7874977b7b1132c73bef3db";
        public const string LOAD_PROFILE_ON_DEMAND_DATA_SET_4 = "0a4a59ddcfe549b19fb16c7c28a570e4";
        public const string DELTA_LOAD_PROFILE_DATA_SET_1 = DeviceResultTypes.DELTA_LOAD_PROFILE;
        public const string DELTA_LOAD_PROFILE_DATA_SET_2 = "9bccdbfcc7e54e73b90fdcc77441f072";
        public const string DELTA_LOAD_PROFILE_DATA_SET_3 = "0e2477937c004ae3924913d40cabb99b";
        public const string DELTA_LOAD_PROFILE_DATA_SET_4 = "a22afd66839247e0846e26c6e50fd5e4";
        public const string FULL_LOAD_PROFILE_DATA_SET_1 = DeviceResultTypes.FULL_LOAD_PROFILE;
        public const string FULL_LOAD_PROFILE_DATA_SET_2 = "ac1b7ac6ad754b2f8355694538c25c77";
        public const string FULL_LOAD_PROFILE_DATA_SET_3 = "417ed3d410ce4739bbf16ea22749e819";
        public const string FULL_LOAD_PROFILE_DATA_SET_4 = "be54b23c8ab44b99854624f3157ee96e";

		//v5.1 Results
		public const string MEMORY_CONFIGURATION = "c80f7077d19347eebd8bf613115974f5";
		public const string LOAD_PROFILE_DATA_SET_COLLECTION_CONFIGURATION = "e6630e760cf046f9bf9a51b648ad632a";
		public const string MCN_CONFIGURATION = "c87c44cb730e47bb8ef88901895ad6b6";

        //v5.2 Results
        public const string POWER_STATUS = "31C8909A295F445C87C19E5C41748B34";
		public const string ALARM_SETTINGS = "5eec5133b2a145428c6d58146e172a7a";
		public const string DISCONNECT_THRESHOLDS_CONFIGURATION = "993315c77e0548db832a3d01abe16cb5";

		// v5.3 Results
		public const string TAMPER_CONFIGURATION = "756be7f5e4ca444184e225f88220e104";
        public const string MEP_CONFIGURATION = "57fba30497cf423983023d49a6fbf008";
		public const string REGISTER_CONFIGURATION = "39e56fd49aad47bb9ca538168c32ff31";
        public const string OPTICAL_PORT_CONFIGURATION = "d5c9b0e4b8d240fcb5ac55c2e72d4e84";

		////////////////////////////////////////////////////////////////////////////////////////
		//The following are deprecated - the version in which they were deprecated and the    //
		//constant replacing them (if any) is listed to the right of the constant.            //
		////////////////////////////////////////////////////////////////////////////////////////

		[Obsolete("v2.2 - not replaced since this no longer can be returned")]
		public const string PREPAY_MAXIMUM_POWER_STATUS = "64b4504113c24ada8f06b23775ee5f5f"; //v2.2 - not replaced since this no longer can be returned

		[Obsolete("v2.2 - not replaced since this no longer can be returned")]
		public const string PREPAY_MAXIMUM_POWER_LEVEL = "f12e58ca90d94fa1b24793e2244b50b7"; //v2.2 - not replaced since this no longer can be returned

		[Obsolete("v2.3 - use PRIMARY_MAXIMUM_POWER_STATUS")]
		public const string MAXIMUM_POWER_STATUS = "C1340AE2C9B74d088D5B206CB8C8A5C2"; //v2.3 - use PRIMARY_MAXIMUM_POWER_STATUS

        [Obsolete("v3.0 - use ACTIVE_TOU_CALENDAR")]
        public const string TOU_SEASON_SCHEDULES = "C2F525891E70439d847AF31F77C09385"; //v3.0 - use ACTIVE_TOU_CALENDAR

        [Obsolete("v3.0 - use ACTIVE_TOU_CALENDAR")]
        public const string TOU_DAY_SCHEDULES = "A3DD474A61324fafB0BE2695DB7FC2A4"; //v3.0 - use ACTIVE_TOU_CALENDAR

        [Obsolete("v3.0 - use ACTIVE_TOU_CALENDAR")]
        public const string TOU_RECURRING_DATES = "37D73FDB453241ffA26B5D4BDFF0A33E"; //v3.0 - use ACTIVE_TOU_CALENDAR

        [Obsolete("v3.0 - use POWER_QUALITY_CONFIGURATION")]
        public const string POWER_QUALITY_OUTAGE_DURATION_THRESHOLD = "0A92EE53D0674ce0B7DA0A00B8FC40DB"; //v3.0 - use POWER_QUALITY_CONFIGURATION

        [Obsolete("v3.0 - use POWER_QUALITY_CONFIGURATION")]
        public const string POWER_QUALITY_SAG_SURGE_DURATION_THRESHOLD = "00BBBD2D5B7B4c198B7B3E464C233A4F"; //v3.0 - use POWER_QUALITY_CONFIGURATION

        [Obsolete("v3.0 - use POWER_QUALITY_CONFIGURATION")]
        public const string POWER_QUALITY_SAG_VOLTAGE_PERCENT_THRESHOLD = "70724FDA9D074cffA060101AA67BCED5"; //v3.0 - use POWER_QUALITY_CONFIGURATION

        [Obsolete("v3.0 - use POWER_QUALITY_CONFIGURATION")]
        public const string POWER_QUALITY_SURGE_VOLTAGE_PERCENT_THRESHOLD = "C1E8C1157D7A4f13874119B3811A0CDC"; //v3.0 - use POWER_QUALITY_CONFIGURATION

        [Obsolete("v3.0 - use POWER_QUALITY_CONFIGURATION")]
        public const string POWER_QUALITY_OVERCURRENT_PERCENT_THRESHOLD = "EFC5B52A066B4f2f82EB5364AE9E24C8"; //v3.0 - use POWER_QUALITY_CONFIGURATION

        [Obsolete("v3.0 - use INTERNAL_EXPANSION_MODULE_CARD_CONFIGURATION")]
        public const string MEP_CARD_CONFIGURATION = "96022cc99e80465a84ace39acead1860"; //v3.0 - use INTERNAL_EXPANSION_MODULE_CARD_CONFIGURATION

		[Obsolete("v5.0 - not replaced")]
		public const string INTERNAL_EXPANSION_MODULE_CARD_CONFIGURATION = "96022cc99e80465a84ace39acead1860"; //v5.0 - not replaced

		[Obsolete("v5.2.000 - use DISCONNECT_THRESHOLDS_CONFIGURATION")]
		public const string MAXIMUM_POWER_LEVEL_DURATIONS = "2B7F31C9E9AE4b778E93BAB84725A6C6";
		[Obsolete("v5.2.000 - use DISCONNECT_THRESHOLDS_CONFIGURATION")]
		public const string PRIMARY_MAXIMUM_POWER_STATUS = "C1340AE2C9B74d088D5B206CB8C8A5C2";

        ////////////////////////////////////////////////////////////////////////////////////////
	}

	public class GatewayResultTypes
	{
		public const string STATISTICS = "3a19bf68e06e4879a0cccc0754dae661";
		public const string TOTAL_ENERGY_DATA = "4bdd989403aa4c1691fce3c104bf09c3";
		public const string FIRMWARE_VERSION = "5cc432d671ba465ebb6bd990b6901605";
		public const string REPEATER_PATHS = "220e1da59e534c82ac57ca095b5db969";
		public const string PROCESS_CONFIGURATION = "9331484664784a3ca4fdb62a0de86c28";
		public const string DISCOVERED_DEVICES = "ad2bbabf67e549ec8de11e31592313ba";
		public const string WAN_CONFIGURATIONS = "8efc087116114b91a528ba1ea6691566";
		public const string DEVICE_LIMIT = "39ca7c607ed7427995d0853fbbe74998";
        public const string EVENT_CONFIGURATION = "1f7040968dcd4a3680747899ffd6071a";
        public const string POWER_LINE_CARRIER_COMMUNICATION_CONFIGURATION = "0351d2c67cd24b65a3b79bcfbead39c1";

        //v4 Results
        public const string ATM_CONFIGURATION = "b256b1ed2b9842d2b39c0358212e5f18";

        //v5 Results
        public const string LOCAL_DATA_ACCESS_CONFIGURATION = "417df905868448348ab0a75c6b2cd10c";
        public const string FEATURE_ACTIVATION = "2ef4464961124b94a35a088ede0ba5b7";
		public const string FTP_CONFIGURATION = "9f8d208d2492424ba80fb34808ddafa4";

        public const string APPLICATION_DATA_0 = "15c8d2e0edcd487db38047fa42b84e0d";
        public const string APPLICATION_DATA_1 = "8465c666f473419e8537f47d14dc34ec";
        public const string APPLICATION_DATA_2 = "d13f6dece4794440b6d99befa24c5e5d";
        public const string APPLICATION_DATA_3 = "d7feef54f62b4441a4fcf921805aeeaf";
        public const string APPLICATION_DATA_4 = "d4544f2b6c794875b1aa24a8eb836f5c";
        public const string APPLICATION_DATA_5 = "f13aa171d9174aeebb2b20254b5c7d3f";
        public const string APPLICATION_DATA_6 = "6e478bd8220c42f5b36d48780660e0fd";
        public const string APPLICATION_DATA_7 = "79ba4884080141b097bcd890d0f2084c";
        public const string APPLICATION_DATA_8 = "bf7bdaedd00e43078585ec9c26805f93";
        public const string APPLICATION_DATA_9 = "0997349d3c974ea78a925fffd01f8137";
		public const string APPLICATION_DATA_10 = "4b3409c88d114bffbf3c2d4b9231de68";
		public const string APPLICATION_DATA_11 = "8be38cf46a5d4d788448a878e6334f3f";
		public const string APPLICATION_DATA_12 = "c54312d430f44d6e8d88451813489676";
		public const string APPLICATION_DATA_13 = "e5936de55b534049b9cdf826362f15d6";
		public const string APPLICATION_DATA_14 = "88e2af53997d4df3a102300f5b555f54";

		public const string GPS_COORDINATES = "acb8fe5123f84960bfcd6db60c2acac7";

		// V5.3 Results
		public const string LVGM = "35e26dd68e7544949075cd1d45caa353";
		public const string LVGM_CONFIGURATION = "6be3aed642b44b76818476294e2a1937";
		public const string PHASE_INFORMATION = "8586a865bf4b4d349cff10956dad71ff";
		public const string WAN_STATUS = "5adcdb3990a94f1a8053b238ec8f3a1d";

		////////////////////////////////////////////////////////////////////////////////////////
		//The following are deprecated - the version in which they were deprecated and the    //
		//constant replacing them (if any) is listed to the right of the constant.            //
		////////////////////////////////////////////////////////////////////////////////////////

		[Obsolete("v2.1 - Use STATISTICS")]
		public const string PERFORMANCE_STATISTICS = "3a19bf68e06e4879a0cccc0754dae661";	//v2.1 - Use STATISTICS
		[Obsolete("v5.3 - no replacement, no longer used")]
		public const string CONTROLLER_CONFIGURATION = "560cfca8777f41ab9f367a263b910140";
		////////////////////////////////////////////////////////////////////////////////////////
	}

    public class SystemResultTypes
    {
        public const string NES_SYSTEM_SOFTWARE_EXPORT = "9426946d4ae64792a770d7c14a60f38e";
    }

    public class RepeaterTypes
	{
		public const string DISCOVERED_DEVICE = "8eb085070912454295b35e0740eddd96";
		public const string EXISTING_DEVICE = "e7bf5c60c98f48f5b39c025106cd1ff4";
		public const string GATEWAY = "2851897aec124b189e91b136aec679c2";
	}

	public class DiscoveredTypes
	{
		public const string ORPHANED_DEVICE = "f6676a4ae64d475f9a3153da9d847027";
		public const string NEW_DEVICE = "ef4af3d51f074efc898b6a5a7a368346";
	}

	public class EventStateTypes
	{
        public const string OCCURRED = "71c92fac407f4e25bd77e444899400c9";
        public const string RESTORED = "8a6130176ee84aff84e2f7249e92d69c";
	}

	public class CommandFailureTypes
	{
		public const string DC1000NACK = "2873e7cb9fd9421ab66af67e1e7783fd";
		public const string GENERAL_FAILURE = "401a8956d93348058007bf5736c3a261";
        public const string TIMED_OUT = "7ec1f45a3f204670b03b02714ea15bbf";
        public const string DC1000_COMMAND_TIMED_OUT = "d509192f4b2e4d9e848a6472060e91cb";
		public const string DECRYPTION_FAILURE = "2bd0e075546546f6ad24c699ab251034";
        public const string DECOMPRESSION_FAILURE = "e33f0b2337f546ca827fbb7d5cbbce00";
		public const string CONVERSION = "363b7350890f4fe09744df3558be9ed4";
        public const string DEVICE_REMOVED_FROM_GATEWAY = "058ca9aac3c24726a8ecb9c1f9b898a9";
	}

    public class FileTypes
	{
		public const string MEP = "76a065ba24b445d6aafc167e8ab474e8";
		public const string GATEWAY = "56c943bc8ac04669b700c6b3a2a424fe";
	}

	public class FirmwareTypes
	{
		public const string DC1000_FIRMWARE = "d2c2a55650c44c0eaf1597f29f9647b7";
		public const string METER_FIRMWARE = "175CAA2B5F804f3095A2242D63A4DBAD";
	}

	public class TaskProcessorCommandFailureReasons
	{
		public const string INVALID_LOCAL_TASK_MANAGER_ID = "51D0838322E7452d9C8D682F69BE7B83";
		public const string ENGINE_ALREADY_RUNNING = "ECF1941DA5E54ea68645183F471A4CC7";
		public const string INVALID_TASK_PROCESSOR_ID = "885537BA1DE741a5AC86C082BC74C890";
		public const string FAILURE_START_ENGINE = "28D3BB102D8E401aA85F857214AE9BC5";
		public const string ENGINE_NOT_RUNNING = "BC2F7392200F45af9E7F280ACCF37EAF";
		public const string FAILURE_STOP_ENGINE = "438CCC58E9B84fa29C3791FD0C2764F9";
	}

	public class SecurityAlertTypes
	{
		public const string TACACS_PLUS_AUTHENTICATION_FAILURE = "42432283DD554d788AADAE3ED770029E";
		public const string API_AUTHENTICATION_FAILURE = "cc9d7f6f5fbd46b7b2ffa69b9b4f16ae";
		public const string GATEWAY_AUTHENTICATION_FAILURE = "05f1e769ca0d43b2940910b7d34b11b1";
	}

	public class ScheduleTypes
	{
		public const string GATEWAY_NORMAL_CONNECT = "e56ed175268d4d4e85d37cce8aae7e31";
		public const string ARCHIVE = "49f3c2a29e2e4bba957d3d528e347cb0";
		public const string ORPHAN_CHECK = "4f5ed07cb355480699d23f48dcdb5cdc";
		public const string AGGREGATION = "2e4b9bcb6348411ea07c42bf9e9c0c9e";
		public const string TASK_TIMEOUT = "45c1a77ca40a40eb8d2cd5a8a508c131";
		public const string ATM_GATEWAY_PROCESS = "50ca81b849c341ab9f33e91ec1d66a8a";
		public const string ATM_DEVICE_PROCESS = "7fd6a73171c345d8be154ce48049671d";
        public const string COMMAND_HISTORY_ORPHAN_CHECK = "b3290441fe5d40c9bfc5a6ca9b5e123e";
	}

	public class MeterFirmwareDownloadStatus
	{
		public const string SUCCESS = "97E5A2ED2EE744928FFE6136EABA98AC";
		public const string FAILURE = "57B9E3C731414446A8F7F2645D4E92DF";
	}

	public class DataPointValueSortTypes
	{
		//sort order = expected date time, actual date time milliseconds, data point ID
		public const string EXPECTED_DATE_TIME_SORT_1 = "159d3c3adc414917b9174c4724b211d8";
		//sort order = data point ID, expected date time, actual date time milliseconds
		public const string DATA_POINT_ID_SORT_1 = "fb8e282785774d91a7a620c8bb8bf4f9";
	}

	public class SortOrderTypes
	{
		public const string ASCENDING = "be3c378cf94d4c0882eb1ba141992f20";
		public const string DESCENDING = "cd6c6ac034334622b4a553a6fccadeb2";
	}

	public class PulseInputChannelStatus
	{
		public const string ACTIVE = "4f0771915d5247de9aefbd882835e211";
		public const string INACTIVE = "f80e4a8ab88f4caca8f50aba19d63dd7";
	}

	public class PulseInputChannelIdleState
	{
		public const string LOW = "25efd3bbfb4248048b29d4e80ddc469c";
		public const string HIGH = "49f02294e79b49f39e65091e35493150";
	}

	public class ControlRelayStatus
	{
        public const string BROADCAST_OPEN = "d7ff0b7cdb6d491c90afd700cda4971b";
        public const string BROADCAST_CLOSED = "15080352c0264373a97501ba9e40517f";
        public const string OPEN = "6eebd8aceb784b1f826a6cdfaf4f69ae";
        public const string CLOSED = "f8e560fe460142f996e8374a890161b4";
	}

	public class TOUCalendarTypes
	{
		public const string ACTIVE = "bb8ef5a8233245588a047b77d0ad2a38";
		public const string PENDING = "6a0b211245e44a5f8d443dbf012956da";
	}

	public class RepeaterPathStatus
	{
		public const string SUCCESS = "b3442ec53a644323a64ac7c8a6712eea";
		public const string INVALID_LENGTH = "76159b841ca14454a53fcd1b16132b38";
		public const string REPEATER_LOOP = "ea1194168e7844a89bce8792969dd10a";
		public const string UNDETERMINED = "c49ad93bbc9c4bb3bdb0707dec1a8481";
		public const string INVALID_REPEATER = "ae2c0b72ba9f4413a09e605a3d3d4846";
	}

	public class AddMeterFailureTypes
	{
        public const string GENERAL_FAILURE = "15583ad9e2374760b3f2df089b966c0b";
        public const string WRONG_KEY = "51d30c3026e7457d9a65e3a6eeabff7b";
		public const string INVALID_NEURON_STATE = "ab48409924b14e1c88f56f57bdfc5107";
		public const string NASCENT = "394950942cb745f4a2a36243c5c6f7d9";
        public const string INTERFACE_INCOMPATIBILITY = "314bdeb6169b4988b8151020b405ff99";
        public const string UNKNOWN_VERSION = "a8864a63b5394575bf5fcc5621f1f6d3";
        public const string ATM_COMMISSION_DATA_NOT_ACCEPTABLE = "3957cbcb134647bc948885451d53c08f";
        public const string MEP_DEVICE_NOT_SUPPORTED = "a871c4ca2a8047b184c24e65a4340055";
        public const string DEMAND_NOT_SUPPORTED = "f3eea9c997e4457aa5d1b307f603f45e";
        public const string INVALID_DEVICE = "f0289741c18f44e7b840a4f021ea0c4c";
        public const string INVALID_ADD_METER_COMMAND_STATE = "3634341fd53b4824810f3687d593a573";
        public const string PROCESS_AUTHENTICATION_KEY_FAILURE = "cbd3f6259782448da45dfe8fe899e29f";
        public const string DUPLICATE_NEURON_ID = "aeb854f065aa475292012f3353b3c13d";
		public const string UPDATE_SUBNET_NODE_FAILURE = "52eccb59b7ca48a1a753ecfa54178f06";
		public const string ATM_NOT_SUPPORTED = "8a39036ad0904d96a7b2d7ddb9ca59b5";
		public const string FAILURE_PRIOR_TO_CREATE_DEVICE = "3e81f5ff724b44478bfc8795aeaed826";
		public const string TASK_DATA_VALIDATION_FAILURE = "15f62a4365d44587b37a0ce6dd579c1d";
		public const string PHYSICALLY_ATTACHED_METER_NOT_MOVED = "ea9904d2d75d4085a9bc615e0998c65e";
		public const string INSERT_OR_UPDATE_ADRESSING_INFO_FAILURE = "344da82b4c2d43569cb25d70f1aa04b8";
	}

	public class GrammarStatusTypes
	{
		public const string SUCCESS = "4a5f1225092c49de81f043ed9413f462";
		public const string SYNTAX_ERROR = "b321d320dd8a41569af3946634a5754d";
		public const string TYPE_MISMATCH_ERROR = "6d5a95405a95459594ffffb4e5b0104c";
		public const string DIVIDE_BY_ZERO_ERROR = "acb2a003b0b64b1b9580f2495955b8c1";
		public const string MATH_ERROR = "93009bfe6e31430da648f48538395fd9";
		public const string SQL_ERROR = "824b239eef9f407aaeb755f65fbee79e";
		public const string FUNCTION_ERROR = "1f90d1cc187d41009961d7f679a1c294";
		public const string SOAP_ERROR = "181b384c327a44549bd9dbb121d8d209";
		public const string UNKNOWN_ERROR = "6a9615ad625d4b62a15814bcae314c63";
		public const string PROCESSING = "cc4481d44274443d9714de7de60513c5";
		public const string TASK_WAIT = "fe6cad8132d94111b5a956e1d8fcaa0e";
		public const string TASK_FAIL = "d5b58778df414d468c6389c735396900";
		public const string TASK_TIMEOUT = "18FBAC82858046f3AC1037A45BC00DB0";
	}

	public class BillingDataStructureTypes
	{
		public const string VARIABLE = "de8df181afad466e8816b32fdf2d1e4b";
		public const string FIXED = "08b17d6af9a546e8a1b7a12f8eff288b";
	}

	public class MBusDeviceTypes
	{
		//The M-Bus device types supported by NES System Software
		public const int GAS = 3;
		public const int HEAT = 4;
		public const int HOT_WATER = 21;
		public const int WATER = 7;
	}

	public class TokenTypes
	{
		public const string FUNCTION = "4114DF84A18043fbBCB4A61FFDA7D479";
		public const string SOAP = "69F8AD561E1147ca802324B86CBCDC8E";
		public const string SQL = "70850501EDC74bc39FD95E8B715D55F9";
		public const string DATAPOINT_PARAMETER = "4AD86AFBF2154a50B62E4FAE23919320";
		public const string EXPRESSION = "7F18F19D8B214f5eBDF2E7F1AB7BE4E7";
	}

	public class IPAddressTypes
	{
		public const string STATIC = "3492178071224eedbe1268a80b354ddd";
		public const string DYNAMIC = "435555d42e56498db3d20e0dbd7ac961";
	}

	public class PingGatewayStatusTypes
	{
		public const string ENABLED = "637f2d8189c64d91a40b2d7296ec2b0f";
		public const string DISABLED = "b28660246ee64fdead9b1c53b6f53a07";
	}

	public class MBusStatusTypes
	{
        public const string ENABLED = "42317b88d6d448c591cc3620e4b399d3";
        public const string DISABLED = "74fe799136ad44719c942ee63880f7bf";
	}

	public class InternalCommands
	{
		public const string REMOVE_DEVICE = "b2e4b7691acf4b2daae5808818ff87d4";
		public const string ADD_METER = "5536a36488624690a52dbb1a8ca469ca";
		public const string MOVE_DEVICE_ADD = "7CF97C0997E543198AF4D3C777D69A61";
		public const string MOVE_DEVICE_REMOVE = "92668107364343ffBC056C8D53CAD378";
		public const string CONNECT = "bb496629589e4bf59aa83f393042a98c";
		public const string DISCONNECT = "367d83df9ab74e9d8ea8cd21a7919ac2";
        public const string GATEWAY_REPLACEMENT_ADD_DEVICE = "23b2a124012e4caf91cb3f4138154424";

		////////////////////////////////////////////////////////////////////////////////////////
		//The following are deprecated - the version in which they were deprecated and the    //
		//constant replacing them (if any) is listed to the right of the constant.            //
		////////////////////////////////////////////////////////////////////////////////////////

		[Obsolete("v2.2 - use REMOVE_DEVICE")]
		public const string REMOVE_METER = "b2e4b7691acf4b2daae5808818ff87d4";    //v2.2 - use REMOVE_DEVICE

		[Obsolete("v2.1 - no longer used      ")]
		public const string METER_STATUS = "5d00b50350924f1993021bd97ccaba64";	//v2.1 - no longer used

		[Obsolete("v2.1 - no longer used")]
		public const string CLEAR_METER_ALARMS = "f1a123b084324d148a35972cd1181187";	//v2.1 - no longer used

		[Obsolete("v2.1 - no longer used")]
		public const string READ_EVENTS = "5c434edd1bc54c25ae4eca27c4c27a7e";	//v2.1 - no longer used

		[Obsolete("v2.1 - no longer used")]
		public const string READ_RESULTS = "9f2e0bc96e0a417ab9cf240944c085ab";	//v2.1 - no longer used

		[Obsolete("v2.1 - no longer used")]
		public const string READ_DCX_DATA_RESOURCE = "01fd87fdbe5f4517a50c49ec612479ce";	//v2.1 - no longer used

		[Obsolete("v2.1 - no longer used")]
		public const string TIME_SYNC = "fcb91eea9630431a8cdf27b07fa54b54";	//v2.1 - no longer used

		[Obsolete("v2.1 - no longer used")]
		public const string INITIAL_COMMUNICATION = "772660C3ED094b548A8F05F5A10A1236";	//v2.1 - no longer used

		[Obsolete("v2.1 - no longer used")]
		public const string CREATE_ACCOUNT = "5e3117ba59874854bdd2b5dc5913dc5d";	//v2.1 - no longer used

		[Obsolete("v2.1 - no longer used")]
		public const string DELETE_ACCOUNT = "924465babccb46af9079420b8a9207e7";	//v2.1 - no longer used

		[Obsolete("v2.1 - no longer used")]
		public const string TERMINATE_SESSION = "00aba36789d647eba3b60e62db1486a1";	//v2.1 - no longer used

		[Obsolete("v2.1 - no longer used")]
		public const string AUTHENTICATE = "b18a56c164c24d059710ced501f6ce36";	//v2.1 - no longer used

		[Obsolete("v2.1 - no longer used")]
		public const string READ_METER_FIRMWARE_AND_BOOTROM_VERSIONS = "7c895be303b241c9871bc4671cd64255";	//v2.1 - no longer used

		[Obsolete("v2.1 - no longer used")]
		public const string ENABLE_METER = "b8255ff459604e48bb0a89537f87e8ce";	//v2.1 - no longer used

		[Obsolete("v2.1 - no longer used")]
		public const string RETRIEVE_METER_SOFTWARE_VERSION = "D540611DF14F45a6AA141F4A4471A6A6";	//v2.1 - no longer used

		[Obsolete("v2.1 - no longer used")]
		public const string RETRIEVE_METER_FIRMWARE_VERSION = "6BCA5DA454154fdbBE9D933F8AC32DEF";	//v2.1 - no longer used

		[Obsolete("v2.1 - no longer used")]
		public const string GET_GATEWAY_VERSION = "D410B8BE717F43749E012E2F276C4F51";	//v2.1 - no longer used

		[Obsolete("v2.1 - no longer used")]
		public const string DELETE_DCX_EVENTS = "ae5565c2b01a446d85362aedc31f46cb";	//v2.1 - no longer used

		[Obsolete("v2.1 - no longer used")]
		public const string DELETE_DCX_RESULTS = "68bda33aad2149cf8995183d578bb07e";	//v2.1 - no longer used

		[Obsolete("v2.1 - no longer used")]
		public const string DELETE_DCX_DATA_RESOURCE = "fdd0c687f67b435099755dbdef1ee59d";	//v2.1 - no longer used

		[Obsolete("v2.1 - no longer used")]
		public const string SET_STOP_MODE = "598080cfd80544e2a8f54bca7075252e";	//v2.1 - no longer used

		[Obsolete("v2.1 - no longer used")]
		public const string CLEAR_EVENT_LOG_PENDING_OVERFLOW_ALARM = "17ce1d88cf78470580565dcca36640ad";	//v2.1 - no longer used
		////////////////////////////////////////////////////////////////////////////////////////
	}

	public class DataOrderTypes
	{
        public const string MSB_FIRST = "a0acddee89ef4ef9baa2ca507faf11ad";
		public const string LSB_FIRST = "e20bcc56866146f2bfc0e83f7aef70b2";
	}

	public class KeyAvailabilityTypes
	{
		public const string KEY_AVAILABLE_TO_DECRYPT_DATA = "993559bc23d84ba4951770ef131c5043";
		public const string NO_KEY_AVAILABLE_TO_DECRYPT_DATA = "1fa69a08f49b4701804354fb57076551";
	}

	public class MBusSecurityStatusTypes
	{
		public const string PASSED = "bbceb6d86d5d4b64954f4fa2e833fd51";
		public const string FAILED = "d95545c359174f73ae98c10905330fae";
	}

	public class MBusBillingScheduleFrequencyTypes
	{
		public const string HOURLY = "07794DC86EAE44d595D3D089B37759E3";
		public const string DAILY = "908761AF6EAA4b61AD55138FC52B6EF9";
		public const string WEEKLY = "65D8EAEF659E4a6889FDDBA22F20F2B5";
		public const string MONTHLY = "878D491B5AC948cbA2871EBC61CDB37A";
		public const string NEVER = "31fb72cb886248649530f0ad1009c72a";
	}

	public class ConnectionFailureTypes
	{
        public const string SOCKET_CONNECT_ERROR = "da940a38bb2843a39fa9446be13d16ba";
        public const string DCXP_TIMEOUT = "092ad8db3ae84e76b3a32c7c92f3bed2";
        public const string INVALID_GATEWAY_VERSION = "78d16dea04c345dfb83ac65b3cb8f7af";
        public const string GENERAL_FAILURE = "aafb988d858b43a28ff138316d8b9a04";
        public const string FTP_SOCKET_CONNECT_ERROR = "aa8b96b428f149e3b18d97eced73a922";
        public const string DC1000_ADAPTER_SERVICE_CONNECTION_ERROR = "1d22a735900e42bcbc0841294b94fea1";
	    public const string GATEWAY_INITIATED_CONNECTION_UNAVAILABLE = "13ccd70252b14ec3afa612cd11f8d482";
        public const string SECURITY_FAILURE = "a46dd392a5c847389fcd720c28d23e01";
    }

	public class MBusAuthenticationTypes
	{
		public const string PASSED = "1687d9c9bb6246f9a95823483640659f";
		public const string ID_FAILURE = "0075a0ddeb6f4be7ab9d81a05f5e6ea5";
		public const string DATE_FAILURE = "41c58e7fa72b41c3856dd69cb99ef5fb";
		public const string PASSED_WITH_ALTERNATE_DATE = "b8f1e12cfd3b49b285fd21fc123d8637";
	}
	public class PerformanceLogTypes
	{
		public const string SERVER = "e99171ad6e694b61aa342b3f9517b0f7";
		public const string DC1000_ADAPTER = "363e10ad751e4a8fb69d0245ef74b0c8";
	}
	public class CommunicationFailureStatusTypes
	{
		public const string FAILURE = "2C095BAF40844945AD460A3B5C84E3F8";
		public const string NO_FAILURE = "47CBD5BBF3F14bf9A7B6C6BB284F62F3";
	}

	public class ServerToGatewayProtocolTypes
	{
		public const string TCP = "044d52791fbf427eab4482c188bf2d37";
		public const string FTP = "7d4a47546e4f4a4090d97da789c9f40e";
	}

	public class DC1000ResourceEntryPriorities
	{
		public const string NORMAL = "0245574809e9419ab834497ca5e7a1e4";
		public const string HIGH = "e9507e718cf64754b2e8a3fe0dd6e132";
	}

	public class AgentTypes
	{
		public const string DEVICE = "314B72A03CDC40ad95A625CB0602182F";
		public const string UNKNOWN_DEVICE = "903D59A945604ec8ADE822C0EDC976C0";
		public const string GATEWAY = "191F595B65E248749BCDDDAD35106E8C";
	}

	public class PrepayAddCreditOptionTypes
	{
		public const string ADD_PREPAY_CREDIT_ONLY = "1D9860F62F304615BA3AE9CB7FC0B2C1";
		public const string ADD_PREPAY_EMERGENCY_CREDIT_ONLY = "70D5E5497A744f7b90038088C2597C71";
		public const string ADD_PREPAY_EMERGENCY_CREDIT_THEN_PREPAY_CREDIT = "98079381CD724f32B5CD8FD898AE6EBC";
	}

	public class PrepayClearCreditOptionTypes
	{
		public const string CLEAR_PREPAY_CREDIT_ONLY = "6365823F41B84992B52DD1DF958A6D5F";
		public const string CLEAR_PREPAY_EMERGENCY_CREDIT_ONLY = "D5DC7740A29A4bf5A49DFE172F877C8B";
		public const string CLEAR_PREPAY_CREDIT_AND_PREPAY_EMERGENCY_CREDIT = "BFBAD7349E9A456aBCDDCE0D0F60A53C";
	}

	public class PrepayStatusTypes
	{
		public const string ENABLED = "A3C2AD09987941b6A379763A97E4B540";
		public const string DISABLED = "059C0FA1653B495c834738A7A215321C";
	}

	public class PrepayReversePowerDeductionStatusTypes
	{
		public const string ENABLED = "4388337E206C4552B7C3DF857EFB11E6";
		public const string DISABLED = "5C7410D887034eef9B6F14CF44739D0E";
	}

	public class PrepayAudibleAlarmStatusTypes
	{
		public const string ENABLED = "F5045FD794EA46bbB295B26E0F72955B";
		public const string DISABLED = "8913D939B4824b88A4950AF798BED820";
	}

	public class PrepayMaximumPowerStatusTypeID
	{
        public const string ENABLED = "deb4566e07d147b48d3700f49f6e34b2";
        public const string DISABLED = "f19abc0c5b924234b99a0a10de7ec812";
	}

	public class PrepayEmergencyCreditStatusTypes
	{
		public const string ENABLED = "C60B74ED2D9E430c95B445964BD916AB";
		public const string DISABLED = "48DB020F29134130B3F14214E8191B53";
	}

	public class PrepayCreditTypes
	{
		public const string REGULAR = "75ac11b6ead14c32ae8fac863bc00dd9";
		public const string EMERGENCY = "8b69a66966bf47d5aa644cfca7d992e1";
	}

	public class DeviceTaskTypes
	{
		public const string READ_FULL_LOAD_PROFILE = "ffedafe3328c45f3ad01a6fe01290d84";
		public const string CONNECT_LOAD = "9b076bc9903444899ff2734889aa9086";
        public const string DISCONNECT_LOAD = "2fab533d860f4cbe9bf85f418e36093a";
        public const string READ_BILLING_DATA_ON_DEMAND = "6eee1c4551484aa68384b84ef265b088";
		public const string READ_SELF_BILLING_DATA = "a92907a371774a748829de52f5ca45bc";
		public const string READ_SECONDARY_BILLING_REGISTERS = "70818b636c55499f80c2b6028e3861ff";
		public const string DISCONNECT_CONTROL_RELAY = "a0bfeb73f357479f8bf18c33588f2141";
		public const string CONNECT_CONTROL_RELAY = "8d684c709dde4ee08c06a574ce62a501";
		public const string SET_CONTROL_RELAY_TIERS_STATUS = "bffd077bbe6a4d028610bd6617497197";
		public const string SET_CONTROL_RELAY_TIERS = "ca4c2d1dde0e46e6b54f4b5647875e00";
        public const string SET_LOAD_PROFILE_CONFIGURATION = "d614b22684cf45d6bc50363b9a6f245e";
		public const string UPDATE_FIRMWARE = "dd2aa43bd23e4855946785e535e610f0";
		public const string READ_DISPLAY_CONFIGURATION = "07A8F21239464ab4875C835085B9F495";
		public const string SET_DISPLAY_CONFIGURATION = "8B66865C7FC54a3aBBAD08738768BC11";
		public const string SET_ALARM_DISPLAY_CONFIGURATION = "1C2C078AE4B147ddB9CA07F214D3CA0C";
		public const string READ_POWER_QUALITY = "88d02253387e4a789cf721580e0578ed";
		public const string READ_PULSE_INPUT_CONFIGURATION = "1170e680261f4d75a72e9ca74ae0d7d2";
		public const string READ_INSTANTANEOUS_POWER = "6659D10F0962447cBD07E169B65D8001";
		public const string READ_CONTROL_RELAY = "e44f1020a7a14d679d596f5c655bffcb";
		public const string READ_LOAD_STATUS = "73809a395bbc447a87673bde6739c952";
		public const string READ_FIRMWARE_VERSION = "ca1ca2cd30444b4a84a0245e9c86f8d2";
		public const string SET_PULSE_INPUT_CONFIGURATION = "edf915ca40ca44cfbbdd667603138967";
		public const string READ_ACTIVE_TOU_CALENDAR = "0511085D4DC9442c966106842AB7A6C9";
		public const string READ_PENDING_TOU_CALENDAR = "1537F6D9BC3C43a697960DB3A5365A35";
		public const string SET_PENDING_TOU_CALENDAR = "4AE8A9D89305472490D20336FF25AC7E";
		public const string READ_DOWN_LIMIT = "a9d73ac2abc94ebdbee8731284aebf45";
		public const string SET_DOWN_LIMIT = "bb45fc6e85c241808a43c770927a3f5b";
		public const string SET_DATE_TIME = "89c51f87d84847f5a1ea986b7d67638b";
		public const string READ_DELTA_LOAD_PROFILE = "dc126b9a6224447b901b42687d79f1f2";
		public const string READ_CONTINUOUS_DELTA_LOAD_PROFILE = "4b6c5b0f93a6427687c8f6d418dd4676";

		public const string READ_STATISTICS = "92ee7e0e0a00414dadb1a199280ec70c";
		public const string READ_EVENT_LOG = "b1548023071d4fba814a53000a8e5689";
		public const string READ_ALARM_POLLING_RATE = "f6a27f0049e94f4aa7f944be84699648";
		public const string SET_ALARM_POLLING_RATE = "92df0f15a4a24429aa566984ee457639";
		public const string READ_BILLING_SCHEDULE = "78BC0D4C6E684dc3A3AE8242909E114A";
		public const string SET_BILLING_SCHEDULE = "AE7B6DC406FD40c28CADAF95A74AA7FE";
		public const string READ_HISTORICAL_BILLING_DATA = "1ff26ee6f4a649eca54d5ce3ee3c780c";
		public const string READ_AUTO_DISCOVERY_CONFIGURATION = "cadecfc3478d49cca08c6433f7e8a425";
		public const string SET_AUTO_DISCOVERY_CONFIGURATION = "01de8bb8cfb440a4b292c42708f7cd55";
		public const string READ_UTILITY_INFORMATION = "d39e9a3a80c0414d85a4bd758091def7";
		public const string SET_UTILITY_INFORMATION = "b07031d448e740cf90479f6752a9da8e";
        public const string READ_STOP_MODE_CONFIGURATION = "7c255b1784dd42d6930d9f4970df80c5";
        public const string SET_STOP_MODE_CONFIGURATION = "39f58111cf594884bb67c8aa333c3fd1";
        public const string READ_POWER_QUALITY_CONFIGURATION = "cc0addddcd854678b7f654953817a628";
        public const string SET_POWER_QUALITY_CONFIGURATION = "7bd4fa2e47914e98bb129d5f8ba15497";
		// V2.3 Tasks
		public const string ADD_PREPAY_CREDIT = "43BD199A540B4f5d98F538C3C149D995";
		public const string CLEAR_PREPAY_CREDIT = "74012F0E198F4f399ED988C8A21C3BBC";
		public const string READ_PREPAY_CONFIGURATION = "2669D04D2A5046b18F25710C35C38BC5";
		public const string SET_PREPAY_CONFIGURATION = "950D0039DE214b42A2888CE294F78ED4";
		public const string READ_TIME_ZONE_CONFIGURATION = "166fda8672174213823a41c5c77fc9aa";
		public const string SET_TIME_ZONE_CONFIGURATION = "b40fdef551a84e17b4116fd17e9c059b";
		public const string READ_PREPAY_CREDIT = "031210f70bec420e8faccea7ba8affa1";

		//V3.0 Tasks
        public const string READ_DISCONNECT_CONFIGURATION = "657729a2940749d8ab81307198ed6023";
		public const string SET_DISCONNECT_CONFIGURATION = "b5e8f513482d4fbea41bb8c9929e23de";
		public const string WRITE_DATA = "372bb7b95e53482382b2b180ca82d3eb";
        public const string READ_CONTINUOUS_EVENT_LOG_CONFIGURATION = "2d4568bdfe174b408e4c075c8c71ad92";
        public const string SET_CONTINUOUS_EVENT_LOG_CONFIGURATION = "0745621775e6482588fa932fddff2d79";
        public const string RESET_POWER_QUALITY_STATISTICS = "bbcc10acdcd547a0bbc088a42f7c0a9b";
        public const string READ_SELF_READ_RETRIEVAL_CONFIGURATION = "7fbfa5ffa1c94df08ca454c318835754";
        public const string SET_SELF_READ_RETRIEVAL_CONFIGURATION = "3a03cd794d9c4541973a5d444205ba68";
        public const string ADD_ONE_TIME_READ_REQUEST = "d58dc3851df4479db099f7f739adf283";
        public const string CLEAR_ONE_TIME_READ_REQUESTS = "a15f9dd1845d436284a0e86b78fccab4";
        public const string READ_HISTORICAL_ONE_TIME_READ_DATA = "82f4d9b912dd4e6c98ba06f507e71767";
        public const string READ_ONE_TIME_READ_REQUESTS = "c2450066cc5d4b0b91b60865a92e8885";
        public const string RESET_DEMAND_VALUES = "62be409665c74be0b2c937fb6816cbd3";
		public const string READ_CONTINUOUS_DELTA_LOAD_PROFILE_COLLECTION_CONFIGURATION = "ac5c9b5eb594468ba4b3e35084879b5a";
		public const string SET_CONTINUOUS_DELTA_LOAD_PROFILE_COLLECTION_CONFIGURATION = "6261e65aa66f48c0b9dcac607c089e44";
        public const string READ_DEMAND_CONFIGURATION = "7754241F46494a4fAFAA8938DF796F2D";
        public const string SET_DEMAND_CONFIGURATION = "2E23978B2C184573AE1A7B04B9B6ADDE";
        public const string READ_LOAD_PROFILE_CONFIGURATION = "7ca2a1b225a140a6b48235220fa836e0";
		public const string READ_LOAD_PROFILE_ON_DEMAND = "1de872bffd1549c694b1572aeb598b74";
        public const string READ_EVENT_LOG_CONFIGURATION = "621fd0b23e4848f09200ea9db1033f15";
        public const string SET_EVENT_LOG_CONFIGURATION = "ffd71bedd7cc40d0aadf118fb9ea4453";

		// v4 Tasks
		public const string READ_READ_SCHEDULE = "7700f4b734b446b59618d9b74ac43038";
		public const string SET_READ_SCHEDULE = "f0d148e8644147b3b94fbc5580b586bc";
        public const string READ_DATA_ON_DEMAND = "0849851de24d444492a866329957d73c";
        public const string READ_PROVISIONING_IDENTIFIERS = "0db6d1b48fbd44ceb9e4a10ff27bd1e9";
        public const string SET_PROVISIONING_IDENTIFIERS = "edc92e406d524d5fa7813e129f6df0f0";

        //v4.1 Tasks
        public const string READ_TIER_CONTROL_CONFIGURATION = "efd79173e085414dadc69ffa7e4e80c4";
        public const string SET_TIER_CONTROL_CONFIGURATION = "c120dbe78a1b4bae9e9887f79f197dbe";
		public const string READ_CONTROL_RELAY_CONFIGURATION = "5ff09d29ea824724b67ed9be3b6adf3c";
		public const string SET_CONTROL_RELAY_CONFIGURATION = "5fc2724d3cd446ca8626c12b18e34180";
        public const string READ_DATA_RECORD_HEADER_CONFIGURATION = "8a1e982fd76a4baca9c5397eece8cc4d";
        public const string SET_DATA_RECORD_HEADER_CONFIGURATION = "8f1e54012419418dac5f38526d63d89d";
        public const string READ_DATA_RECORD_HEADER_MAPPING = "53d8bf853afd49deb7a7f3bc256437b3";
        public const string SET_DATA_RECORD_HEADER_MAPPING = "b875724a72d544c3a47dcaec9b33344a";
		public const string SEND_FILE = "e4361942090a41dc9b8dab95965552db";

        //v5.0 Tasks
        public const string READ_LOCAL_DATA_ACCESS_CONFIGURATION = "bbf69f1218d04d34869f2c666596c98d";
        public const string SET_LOCAL_DATA_ACCESS_CONFIGURATION = "384551dc797a4ea28670fcef29b1edda";
		public const string SET_MEASUREMENT_RATIO_CONFIGURATION = "803715cf776a469f84e44a95c608903c";
		public const string READ_MEASUREMENT_RATIO_CONFIGURATION = "e95f10ba46ae4f7786a04e1e598bccee";
        public const string SET_BROADCAST_GROUP_MEMBERSHIP_CONFIGURATION = "1741af41cb8648c18864194da2d8e8e4";
        public const string READ_BROADCAST_GROUP_MEMBERSHIP_CONFIGURATION = "c431411079694ec7be814dd7c6fc4e4b";

		//v5.1 Tasks
		public const string READ_MEMORY_CONFIGURATION = "ef48fc1eb7754437bfc62031f08da43e";
		public const string SET_MEMORY_CONFIGURATION = "10afd3dad34d4b69ad7efa290e9adfc4";
        public const string READ_LOAD_PROFILE_DATA_SET_COLLECTION_CONFIGURATION = "c6790d3557d04c61a932ed23be0fc091";
        public const string SET_LOAD_PROFILE_DATA_SET_COLLECTION_CONFIGURATION = "43b44a3c137b4b1f81eb72090d5aa22a";
		public const string SET_GATEWAY_SETTINGS = "6812c7d3d79d45ffb619fe9cb04a92f2";
		public const string READ_GATEWAY_SETTINGS = "fe829a47d2594a10a4c0438dc70f21a7";
		public const string READ_MCN_CONFIGURATION = "30c6ff352a954e34b37bedb6f18a9545";
		public const string SET_MCN_CONFIGURATION = "6c01590b65f443f9bc7dd7674f36cd18";

        //v5.2 Tasks
        public const string READ_POWER_STATUS = "E0797C3BA5A34F619263E0878BC9C58D";
		public const string READ_ALARM_SETTINGS = "eaf95311b1a64c1ab05dfa6074a9326d";
		public const string SET_ALARM_SETTINGS = "e1dedfe99d38496a805db01d48de67c7";
		public const string READ_DISCONNECT_THRESHOLDS_CONFIGURATION = "a4f264460ca1427394a6120194e64e57";
		public const string SET_DISCONNECT_THRESHOLDS_CONFIGURATION = "7e63c8582322490cb03d3907ae7970ec";
		public const string READ_TAMPER_CONFIGURATION = "0bec3a23556443ff9bc4376756b91336";
		public const string SET_TAMPER_CONFIGURATION = "55b65e6a65d1417ea1d7f3dfc0351832";

        //v5.3 Tasks
        public const string READ_MEP_CONFIGURATION = "df4a4e4b33034b13a0996e4ce1dd53bd";
        public const string SET_MEP_CONFIGURATION = "aed8ae10850e423cb8952228da12020f";
		public const string SET_KEY = "56d79160b10d4395abfe2ac8da29d2a8";
		public const string READ_DEVICE_REGISTER_CONFIGURATION = "7639a8ca17a74d86b5f641cb0c2761bc";
		public const string SET_DEVICE_REGISTER_CONFIGURATION = "6e84dd4af8184d149ea1de065a41fe63";
        public const string SET_DEVICE_OPTICAL_PORT_CONFIGURATION = "1c2a5ac00f2b467b9e0e3bb23ae05b34";
        public const string READ_DEVICE_OPTICAL_PORT_CONFIGURATION = "33caabc590d64afaaa8a869aca4eb4c0";

		////////////////////////////////////////////////////////////////////////////////////////
		//The following are deprecated - the version in which they were deprecated and the    //
		//constant replacing them (if any) is listed to the right of the constant.            //
		////////////////////////////////////////////////////////////////////////////////////////

		[Obsolete("v2.3 - use SET_PRIMARY_MAXIMUM_POWER_LEVEL")]
        public const string SET_MAXIMUM_POWER_LEVEL = "e507e37bb1534d1d98b686efab3fcd2f"; //v2.3 - use SET_PRIMARY_MAXIMUM_POWER_LEVEL

		[Obsolete("v2.3 - use READ_PRIMARY_MAXIMUM_POWER_LEVEL")]
		public const string READ_MAXIMUM_POWER_LEVEL = "53dab6299f8548d1b4252955ae452347"; //v2.3 - use READ_PRIMARY_MAXIMUM_POWER_LEVEL

		[Obsolete("v2.3 - use SET_PRIMARY_MAXIMUM_POWER_STATUS")]
		public const string SET_MAXIMUM_POWER_STATUS = "6c0dd22755ca4ac69ee3ca7708f8ffdb"; //v2.3 - use SET_PRIMARY_MAXIMUM_POWER_STATUS

		[Obsolete("v2.3 - use READ_PRIMARY_MAXIMUM_POWER_STATUS")]
		public const string READ_MAXIMUM_POWER_STATUS = "59C52D3F7CF545b2A11F48A45B04FA2C"; //v2.3 - use READ_PRIMARY_MAXIMUM_POWER_STATUS

        [Obsolete("v3.0 - use SET_PENDING_TOU_CALENDAR")]
        public const string SET_TOU_SEASON_SCHEDULES = "018DAF857332403e8EEDAF88AAD7F796"; //v3.0 - use SET_PENDING_TOU_CALENDAR

        [Obsolete("v3.0 - use SET_PENDING_TOU_CALENDAR")]
        public const string SET_TOU_DAY_SCHEDULES = "E7F9B97C3CFE4b09A94A103F2DED1067"; //v3.0 - use SET_PENDING_TOU_CALENDAR

        [Obsolete("v3.0 - use SET_PENDING_TOU_CALENDAR")]
        public const string SET_TOU_RECURRING_DATES = "FCDED048A6BF4b8a8FEA2004F40D8D23"; //v3.0 - use SET_PENDING_TOU_CALENDAR

        [Obsolete("v3.0 - use READ_ACTIVE_TOU_CALENDAR")]
        public const string READ_TOU_DAY_SCHEDULES = "3080861AAAA1493781605E11B5F6C24F"; //v3.0 - use READ_ACTIVE_TOU_CALENDAR

        [Obsolete("v3.0 - use READ_ACTIVE_TOU_CALENDAR")]
        public const string READ_TOU_SEASON_SCHEDULES = "B32962D9E0234f25A61963B03002E66D"; //v3.0 - use READ_ACTIVE_TOU_CALENDAR

        [Obsolete("v3.0 - use READ_ACTIVE_TOU_CALENDAR")]
        public const string READ_TOU_RECURRING_DATES = "B438B71FD3624ab994D596B29DAD6F84"; //v3.0 - use READ_ACTIVE_TOU_CALENDAR

        [Obsolete("v3.0 use READ_POWER_QUALITY_CONFIGURATION")]
        public const string READ_POWER_QUALITY_OUTAGE_DURATION_THRESHOLD = "261A278CCE224ffeB08051F0E1641365"; //v3.0 use READ_POWER_QUALITY_CONFIGURATION

        [Obsolete("v3.0 use READ_POWER_QUALITY_CONFIGURATION")]
        public const string READ_POWER_QUALITY_SAG_SURGE_DURATION_THRESHOLD = "08BAE067222749448FCCADDE09B74A8F"; //v3.0 use READ_POWER_QUALITY_CONFIGURATION

        [Obsolete("v3.0 use READ_POWER_QUALITY_CONFIGURATION")]
        public const string READ_POWER_QUALITY_SAG_VOLTAGE_PERCENT_THRESHOLD = "DCA6CABC85C8458387EEEA8A1FFF40DB"; //v3.0 use READ_POWER_QUALITY_CONFIGURATION

        [Obsolete("v3.0 use READ_POWER_QUALITY_CONFIGURATION")]
        public const string READ_POWER_QUALITY_SURGE_VOLTAGE_PERCENT_THRESHOLD = "00EEE598B2AC4934A501778C40EDC6BC"; //v3.0 use READ_POWER_QUALITY_CONFIGURATION

        [Obsolete("v3.0 use READ_POWER_QUALITY_CONFIGURATION")]
        public const string READ_POWER_QUALITY_OVERCURRENT_PERCENT_THRESHOLD = "475316B0A63A4f0d9EBA5E8BF98789B2"; //v3.0 use READ_POWER_QUALITY_CONFIGURATION

        [Obsolete("v3.0 use SET_POWER_QUALITY_CONFIGURATION")]
        public const string SET_POWER_QUALITY_OUTAGE_DURATION_THRESHOLD = "91407B2D6D1B48a1BA4713B094A5949F"; //v3.0 use SET_POWER_QUALITY_CONFIGURATION

        [Obsolete("v3.0 use SET_POWER_QUALITY_CONFIGURATION")]
        public const string SET_POWER_QUALITY_SAG_SURGE_DURATION_THRESHOLD = "7A6BD5F1966848a6AF91D9E7DECEE25F"; //v3.0 use SET_POWER_QUALITY_CONFIGURATION

        [Obsolete("v3.0 use SET_POWER_QUALITY_CONFIGURATION")]
        public const string SET_POWER_QUALITY_SAG_VOLTAGE_PERCENT_THRESHOLD = "A76182A992FD4932B2973DB880AD8585"; //v3.0 use SET_POWER_QUALITY_CONFIGURATION

        [Obsolete("v3.0 use SET_POWER_QUALITY_CONFIGURATION")]
        public const string SET_POWER_QUALITY_SURGE_VOLTAGE_PERCENT_THRESHOLD = "494F9D8827044730B9786B74B82B94C4"; //v3.0 use SET_POWER_QUALITY_CONFIGURATION

        [Obsolete("v3.0 use SET_POWER_QUALITY_CONFIGURATION")]
        public const string SET_POWER_QUALITY_OVERCURRENT_PERCENT_THRESHOLD = "2A5395280E8F4a1fAEDF3CFAA2E918D9"; //v3.0 use SET_POWER_QUALITY_CONFIGURATION

        [Obsolete("v3.0 - use READ_INTERNAL_EXPANSION_MODULE_CARD_CONFIGURATION")]
        public const string READ_MEP_CARD_CONFIGURATION = "c44d0adfd2444207848b6a35a624a997"; //v3.0 - use READ_INTERNAL_EXPANSION_MODULE_CARD_CONFIGURATION

		[Obsolete("v5.0 - not replaced")]
		public const string READ_INTERNAL_EXPANSION_MODULE_CARD_CONFIGURATION = "c44d0adfd2444207848b6a35a624a997"; //v5.0 - not replaced

		[Obsolete("v5.2.000 - use SET_DISCONNECT_THRESHOLDS_CONFIGURATION")]
        public const string SET_PRIMARY_MAXIMUM_POWER_LEVEL = "e507e37bb1534d1d98b686efab3fcd2f";
		[Obsolete("v5.2.000 - use SET_DISCONNECT_THRESHOLDS_CONFIGURATION")]
		public const string SET_MAXIMUM_POWER_LEVEL_DURATION = "c847892e95264216be97cba84388bf0a";
		[Obsolete("v5.2.000 - use READ_DISCONNECT_THRESHOLDS_CONFIGURATION")]
		public const string READ_PRIMARY_MAXIMUM_POWER_LEVEL = "53dab6299f8548d1b4252955ae452347";
		[Obsolete("v5.2.000 - use READ_DISCONNECT_THRESHOLDS_CONFIGURATION")]
		public const string READ_MAXIMUM_POWER_LEVEL_DURATION = "b1bd1d54ed5e4b3782b45a31d3dd9b10";
		[Obsolete("v5.2.000 - use SET_DISCONNECT_THRESHOLDS_CONFIGURATION")]
		public const string SET_PRIMARY_MAXIMUM_POWER_STATUS = "6c0dd22755ca4ac69ee3ca7708f8ffdb";
		[Obsolete("v5.2.000 - use READ_DISCONNECT_THRESHOLDS_CONFIGURATION")]
		public const string READ_PRIMARY_MAXIMUM_POWER_STATUS = "59C52D3F7CF545b2A11F48A45B04FA2C";
        ////////////////////////////////////////////////////////////////////////////////////////
	}

	public class GatewayTaskTypes
	{
		public const string SET_WAN_PHONE_NUMBER = "4eafe7b8e63c4ca39a1dafa921db9595";
        public const string UPDATE_FIRMWARE = "5797529de55d4598bdbb1565ef64c862";
		public const string SET_PPP_USERNAME = "929AEE135E1B4ef3A32DFECC83977724";
		public const string SET_PPP_PASSWORD = "5F7274D13AC143cc86B8DC20FC2ED491";
		public const string READ_GATEWAY_TO_SERVER_MODEM_INIT_STRING = "50328816894c43d79271e6b950ffc4b5";
		public const string READ_GATEWAY_TO_SERVER_MODEM_TYPE = "1e6470dd541d4363984a28e1a0ad3bb6";
		public const string READ_GATEWAY_TO_SERVER_PHONE_NUMBER_1 = "8b9beba68b3944c7948c0a883cbf445b";
		public const string READ_GATEWAY_TO_SERVER_PHONE_NUMBER_2 = "0b40b4c0c0dc41689e3eeb4b1e8b71d2";
		public const string READ_GATEWAY_TO_SERVER_IP_ADDRESS = "91c7803e15944c999ca4410755e74a53";
		public const string SET_GATEWAY_TO_SERVER_IP_ADDRESS = "d64100d65024434bbdff2be23d7d5c64";
		public const string SET_IP_ADDRESS = "0f12e4bcf7854b218c44747555a46784";
		public const string SET_GATEWAY_TO_SERVER_PHONE_NUMBER_1 = "dbfde8dd7dd943ceb42c0781a6b571ab";
		public const string SET_GATEWAY_TO_SERVER_PHONE_NUMBER_2 = "8f54808f5af543499fab4f99c1147964";
		public const string SET_GATEWAY_TO_SERVER_MODEM_TYPE = "969d665c8da34995999d6102c0d9c548";
		public const string SET_GATEWAY_TO_SERVER_MODEM_INIT_STRING = "dab6e63d5a254565a8f83d7ff0a4ca85";
		public const string READ_STATISTICS = "0b31c2f49a4e467fb20f9241153507ea";
        public const string SET_TOTAL_ENERGY_STATUS = "c2ee73bee9b847fb803ddac8a4d51b1e";
		public const string SET_SECURITY_OPTIONS = "60e8a8beeb314ce7b5409c4aa693eeae";
		public const string BROADCAST_DISCONNECT_CONTROL_RELAY = "8836af9905b448b1a2b30dbeb5e052fe";
		public const string BROADCAST_CONNECT_CONTROL_RELAY = "d186b04ff2b14172abaa3f2a1a6c788e";
		public const string BROADCAST_SET_PRIMARY_MAXIMUM_POWER_STATUS = "4b69b9e8e6f5401e8b3224649e264c14";
		public const string READ_FIRMWARE_VERSION = "9d4b5062406b4e37bbfa1e70e9fbd318";
		public const string READ_REPEATER_PATHS = "15357b6ee3ec46b082e2da8d3c296134";
		public const string REBOOT = "89e6c8b9963d482b8d87cdf39fa1910c";
		public const string DELETE_WAN_CONFIGURATION = "ee384c36513448e689f30a26a443ced5";
		public const string SET_DEVICE_LIMIT = "3c25bdd7fb2e4f88bf96a9132d766a28";
		public const string READ_DEVICE_LIMIT = "632c4e86aaa84595a07f6f06e8563c9b";
		public const string SET_PROCESS_CONFIGURATION = "d9d04314b4c447408c4569723777ef60";
		public const string READ_PROCESS_CONFIGURATION = "ad3a490bd4934f239056a25cdd609324";
		public const string READ_DISCOVERED_DEVICES = "7700d7a39b24481dab4990980bb4e0da";
        public const string READ_WAN_CONFIGURATION = "9340c39dac934866aa9c9b459bdf9baf";
		public const string SET_WAN_CONFIGURATION = "50B7EE5AB227474e9C18361ABEEE6FC6";
        public const string READ_POWER_LINE_CARRIER_COMMUNICATION_CONFIGURATION = "a4172f4546f445cca197cc55940aeace";
        public const string SET_POWER_LINE_CARRIER_COMMUNICATION_CONFIGURATION = "3104390fa1e945d0b692ccd54c0b4d2b";
        public const string READ_EVENT_CONFIGURATION = "dfe24f8326ec42c49faaf5203cc55bab";
        public const string SET_EVENT_CONFIGURATION = "a1bd92749b1e4ea5a45db327aa3e75cd";
        public const string ASSIGN_OPERATIONAL_PROFILE = "fe0576c13a3f49b7b54221daef48349d";
        public const string COLLECT_REPLACEMENT_INFORMATION = "dbf8f38ab1ff46e98daa9f89c81e9e48";
        public const string READ_LOCAL_DATA_ACCESS_CONFIGURATION = "4ac76e711e864df8a83237833793ee2e";
        public const string SET_LOCAL_DATA_ACCESS_CONFIGURATION = "09406cd643ae41c99914f5e8be891ec8";
        public const string READ_FEATURE_ACTIVATION = "353d71caf3064d2692ff377fbbf9e972";
        public const string SET_FEATURE_ACTIVATION = "19f41c53f85d4dccbf58eebc1efd4b2a";

		//v5.0
		public const string READ_FTP_CONFIGURATION = "c85b3c175d9a492cad27356aa6d46232";
		public const string SET_FTP_CONFIGURATION = "af197dff830d4da19e1efe8e89f09d7e";
        public const string BROADCAST_WRITE_DEVICE_DATA = "f185e549d176401dba15bbf9f27b95a5";
		public const string SEND_FILE = "98757b8361094c39a7e4342d3ca3b5de";
		public const string UPDATE_VIA_RESOURCE = "8a07342580114e4689ddf43fd6c7e8dd";

		public const string READ_GPS_COORDINATES = "1372d6a1864b4241a590ea258c19b5fb";

		//V5.1
		public const string SET_SETTINGS = "859de26a0b514d2bb6b03a2d92e472d5";
		public const string READ_SETTINGS = "cc460d0493934ac3b7497b2bfac03b57";

        //V5.3
        public const string READ_LVGM = "6711c4c95edf40d3bd379a2a61efe672";
        public const string SET_LVGM_CONFIGURATION = "77e38c313161447fb0618c338f22bf49";
        public const string READ_LVGM_CONFIGURATION = "92403cd8ea2045eeb24d81df31f059ff";
		public const string READ_GATEWAY_PHASE_INFORMATION = "02234e4ba18743ffb8e0e0ec19d6e035";
		public const string SET_GATEWAY_PHASE_CONFIGURATION = "7209085876eb4f0e8a8541fa2ba691a6";
		public const string READ_WAN_STATUS = "181d1625ffa84b198828dc0b7e995466";
		public const string SET_GATEWAY_KEY = "01f8c3875612428889167036a4a70d14";

		////////////////////////////////////////////////////////////////////////////////////////
		//The following are deprecated - the version in which they were deprecated and the    //
		//constant replacing them (if any) is listed to the right of the constant.            //
		////////////////////////////////////////////////////////////////////////////////////////

		[Obsolete("v2.3 - use BROADCAST_SET_PRIMARY_MAXIMUM_POWER_STATUS")]
		public const string BROADCAST_SET_MAXIMUM_POWER_LEVEL_STATUS = "4b69b9e8e6f5401e8b3224649e264c14";  //v2.3 - use BROADCAST_SET_PRIMARY_MAXIMUM_POWER_STATUS
		[Obsolete("v5.3 - no replacement, no longer used")]
		public const string READ_CONTROLLER_CONFIGURATION = "6a4f0107e3d34d5894e672dec6be1f49";
		[Obsolete("v5.3 - no replacement, no longer used")]
		public const string SET_CONTROLLER_CONFIGURATION = "a974360d67b8457ab3880059ebb2d037";
		////////////////////////////////////////////////////////////////////////////////////////
	}

	public class InternalTaskTypes
	{
		public const string SYNCHRONIZE_MEP_DEVICES = "f57256e111c641ada28f2f2bd6844e05";
		public const string SCHEDULE = "548D93B15FAE46f1BBE0D8DB3E677D98";
		public const string RECALCULATION = "E441F39AF80D45c994A652BCEFD2D5A7";
		public const string DATAPOINT = "E86ACFED1B934f8bB884B2367204DB65";
		public const string AGGREGATION = "35659D71EC254cabB7F60A8C07811F60";
		public const string ILON100ADAPTER = "B4D8506E56374612A82DBFAB71842F2C";
		public const string SCHEDULE_DELETED = "88a5a66d40484482956dca475671bd2c";
		public const string SCHEDULE_DISABLED = "73de098b579b48edb0e0b8fe5f4e9b15";
		public const string SCHEDULE_ENABLED = "313bf499ce134fda9c73404be963c05f";
		public const string SCHEDULE_UPDATE = "361c871367834e279248f6bc613498c3";
		public const string SCHEDULE_PROCESS_HEARTBEAT_FAILURE = "637d46a7059a4abb8b18112465ce0c61";
		public const string SCHEDULE_ASSIGNMENT_CREATED = "ea5e58d936e34325baf93f600c7766f9";
		public const string SCHEDULE_ASSIGNMENT_REMOVED = "7f76c1dc81e145ea9feb4c8d8b95cf8a";
		public const string SCHEDULE_OBJECT_DELETED = "8B50020541D84358A09CEAF24EE2DF53";
		public const string SCHEDULE_OBJECT_ENABLED = "7cafcfdd77734b1f8295fb482912a5c6";
		public const string SCHEDULE_OBJECT_DISABLED = "ddd6ce3366d844d0acd03e1447669d29";
		public const string RECALCULATION_STARTED = "df6ee83b167f42b5bb155d334e0fe3a7";
		public const string RECALCULATION_COMPLETED = "093ea585774e49838c656eec62ca5663";
		public const string RECALCULATION_STOPPED = "fcd87ee72cc743628eb4ad6ecbf70796";
		public const string ARCHIVE = "9084f2f2617e44e582a067466c27008d";
		public const string ADD_METER_DEVICE = "af84239ee5594bd1b425544ec6769f52";
		public const string MOVE_METER_DEVICE_ADD = "EF5EBA9624DB4b4e89CFE5CD4A136D7C";
		public const string REMOVE_METER_DEVICE = "27d8944e70bc479ab84543036ad2707b";
		public const string MOVE_METER_DEVICE_REMOVE = "14A9A96DCA2C42be90C087A66401F03C";
		public const string EVENT = "e5ed464a3ce64930845eb69df383be87";
		public const string CONNECT_GATEWAY = "c0eca309cb544ebb8fa6f3b7befb4f91";
		public const string DISCONNECT_GATEWAY = "654bbb9e99e142a69bfee55399680434";
		public const string CHECK_FOR_ORPHANED_CONNECTIONS = "4e33ea19ae754711aff6f1792fafe8d1";
		public const string READ_METER_DEVICE_STATUS = "bb08706eb0274ac392047d67f3064fa8";
        public const string READ_GATEWAY_VERSION = "035C0D9EA8CB4d6499D98BEF0CDE81E3";
		public const string SET_STOP_MODE = "abf544b3bc624adca066eba88369038a";
		public const string SET_LOG_EVENTS_CONFIGURATION = "010FD66BA5C84b508952D5B5761CA21B";
		public const string REMOVE_MBUS_DEVICE = "1eee13291f804e6692f7c64d252abd74";
		public const string CLEAR_EVENT_LOG_PENDING_OVERFLOW_ALARM = "9e335d44b6bd44218ac722a8191e6ca6";
		public const string SYNCHRONIZE_METER_DEVICE_ALARMS = "5e9b7609a8c941488d281ddaeffb19bd";
		public const string READ_NEW_DISCOVERED_METER_DEVICES = "c600ac0fd58f42328ffaa4ee7774f5ae";
		public const string TASK_TIMEOUT = "d67f265555e843e5aca2706fde0f0048";
		public const string RECOMMISSION = "25b0269d50a44f6b8dc9dedab21b0ca5";
        public const string ATM_DC_BATCH = "0e0065881aa74b17a2c0a520b5deb962";
        public const string ATM_METER_BATCH = "20b28a83bcbc4e39926c68094694753a";
        public const string ATM_METER_ASSIGNMENT_BATCH = "9c250cb1f28d4266951148b30592a5c7";
        public const string ATM_FINALIZE_METER_ASSIGNMENTS = "09335d0ed8d547bc8dc64a08314d4321";
        public const string ATM_START_METER_PROCESS = "cc5077341de744f0870691564d3d7cb8";
        public const string ATM_START_DC_PROCESS = "14564b9ef932492b99ac36464997d421";
        public const string ATM_ENQUEUE_ASSIGNMENT_CONNECTS = "9007680f2bb64e8f95a4ba3a90580990";
        public const string ATM_READ_GATEWAY_DISCOVERED_DEVICES = "9042df91847a4ec9a300f6ae00c8b8a3";
        public const string REMOVE_MEP_DEVICE = "83b7ab85cfb04348a1df5a01f1e890cc";
		public const string SYNCHRONIZE_MEP_AND_MBUS_DEVICE_ALARMS = "0eb93548f5c7449e930d55f08955bbe8";
        public const string GATEWAY_REPLACEMENT_ADD_DEVICE = "b6b7ced2d6ec461e98a8fec4c92e60d2";
        public const string COMMAND_HISTORY_ORPHAN_CHECK = "b0d36b83d9d74b7fbba54b5d2302fa2b";
        public const string IMPORT_KEYS = "3eca2e648fa047aca642420fa15275ab";
        public const string ENCRYPT_EXISTING_GATEWAY_LOGIN = "1872701ed94c415f8cedced23f821fa1";
        public const string EXPORT_DATA = "894b254a40c54ce2b98f01f1c4376827";
        public const string IMPORT_NES_SYSTEM_SOFTWARE_EXPORT_RESULT = "87abf15341c445fdbd4c0945bd8e60e8";
		public const string IMPORT_PROVISIONING_TOOL_CREATED_DATA_CONCENTRATOR_FILE = "15bb1e5448784623a866ecb0645e9927";
		public const string UPDATE_DEVICE_DOMAIN_INFORMATION = "a28016c70a28479ebcde67f472e9984f";
        public const string SYNCHRONIZE_DEVICE_LOAD_PROFILE_COLLECTION_CONFIGURATION = "559160fa8cad475ca3c6883b58e88a8e";
        public const string SYNCHRONIZE_GATEWAY_COMPATIBILITY_SETTING = "5ccf0d5d57a24431a39e6920577e765a";
        public const string DETERMINE_DEVICE_PRIMARY_LOAD_PROFILE_DATA_SETS = "aaa4885e200d42d49ada5949578d04a4";
        public const string SYNCHRONIZE_GATEWAY_COMPATIBILITY_SETTINGS = "938f22e579fd479ea740809afd02feae";
		public const string DATABASE_WORK = "4bd12e66aad14dcbb06e6c589ce92f29";
		public const string STORE_COMMISSION_HISTORY_SIGNAL_QUALITY_DATA = "42eef5fe7cee4f3b8585c16ee8b99cfb";
        public const string CIM_Meter_Reading = "0595dd4a76cd4cc198e8d18a7f459193";
		public const string PROCESS_DEVICE_EVENT_LOG_HISTORY = "f6f426b86d964729b887395df3170170";
		public const string PROCESS_DEPRECATED_MAX_POWER_DATA = "8ce32d36d8ad440a9fb49ae835e23853";

        //DEPRECATED -- The following InternalTaskTypes are deprecated
		[Obsolete("v3.0 - no replacement")]
		public const string CLEAR_METER_DEVICE_ALARMS = "8dc9a6a52cfa42d685430be251c8670a";

		[Obsolete("v3.0 - no replacement")]
		public const string CLEAR_MBUS_DEVICE_ALARMS = "55031cf93b894e738712d0b1628d4ed6";

		[Obsolete("v4.0 - use SYNCHRONIZE_MEP_AND_MBUS_DEVICE_ALARMS")]
		public const string SYNCHRONIZE_MBUS_DEVICE_ALARMS = "0eb93548f5c7449e930d55f08955bbe8";

        [Obsolete("v4.0- no replacement")]
        public const string READ_METER_DEVICE_FIRMWARE_AND_BOOTROM_VERSIONS = "e252b767c42a41a69e5c6ef3c57ef5df";

        [Obsolete("v4.0 - no replacement")]
        public const string READ_METER_DEVICE_SOFTWARE_VERSION = "DE798DD0A022417f9C3A5BB6A05FCBF6";

        [Obsolete("v4.0 - no replacement")]
        public const string READ_METER_DEVICE_FIRMWARE_VERSION = "940E43D713594c45AEF877087535D7E6";

        [Obsolete("v4.0 - no replacement")]
        public const string UPDATE_METER_FIRMWARE_TASK_TIMEOUT_PROCESSING = "97a33a4657ed4a549e5ecedc59d5a03c";

        [Obsolete("v4.1 - no replacement")]
        public const string CREATE_PPP_ACCOUNT = "C728EA59DE474bee987E9252D9EB113D";

        [Obsolete("v4.1 - no replacement")]
        public const string DELETE_PPP_ACCOUNT = "8CB7034AB826495888BC6163442249EC";

		[Obsolete("v5.0 - no replacement")]
		public const string SCHEDULE_PROCESS_SHUTDOWN = "1680D1D9D30F472cB1C47CE49F3EB28A";
	}

    public class EngineIPAddressAssignmentTypeID
	{
		public const string USER_ASSIGNED = "2c5067c5abc2476fa417dc2e40a68bb5";
		public const string SERVER_ASSIGNED = "a7aa2e4af6924525b2ad156d5f6ea894";
	}

	public class DeviceTestPointStatusTypes
	{
		public const string NOT_A_TEST_POINT = "fd2c785c0d6b4cc380ac630267d65b85";
		public const string AUTOMATIC_TEST_POINT = "baf84a58ee1347d1858c242248fee59d";
		public const string CONFIRMED_TEST_POINT = "f33eb2a356744d5ea70e09d4939def7d";
		public const string REMOVING_TEST_POINT = "465f506122bc43aab25d15e0453ccead";
        public const string UNKNOWN = "919d57cfd6f740b7a7baf605f80dd95c";
	}

    public class DeviceTestPointStatusCodes
    {
        public const int NOT_A_TEST_POINT = 0;
        public const int AUTOMATIC_TEST_POINT = 1;
        public const int CONFIRMED_TEST_POINT = 2;
        public const int REMOVING_TEST_POINT = 3;
    }

	public class GatewayMeshDetectionStatusTypes
	{
		public const string DETECTED_MESH = "7adbd0d225fa48b2a38403f75c193853";
		public const string DETECTED_NON_MESH = "c44f21d8e8824c86975c5fde82fe1f69";
        public const string UNKNOWN = "18b6d899c9984156954c45be2f31881d";
	}

    public class DCMeshDetectionStatusCodes
    {
        public const int DETECTED_NON_MESH = 0;
        public const int DETECTED_MESH = 1;
    }

	public class ReadTOUCalendarOptionTypes
	{
		public const string READ_ENTIRE_CALENDAR = "10DBB92E49AA4ed08A2C2C57BF1A24F1";
		public const string READ_CALENDAR_ID_ONLY = "F93EBDD5AFCA41629CE2688FDE6EEC95";
	}

	public class ConnectRequestSourceTypes
	{
		public const string CORE_SERVICES_API = "598eaa8f38b84a5d8a91ecece29d08fd";
        public const string SCHEDULED_COMMUNICATION = "80516e311e204596975123e8567a58ab";
		public const string GATEWAY = "fc00670f7e7e4b35a2a61983b1e5a1ee";
        public const string ATM_ENGINE = "506ae5181e1b4aa89c78c85a2dbc9592";
	}

	public class TestTcpIpPortStatusTypes
	{
		public const string ENABLED = "37dc09364a0f48a79d4470999bcaa93d";
		public const string DISABLED = "765fef3d066347099b6f0468303ce20b";
	}

    public class PassiveFtpIpAddressSourceTypes
    {
        public const string FTP_REPLY_IP_ADDRESS = "4dbac487e3744ba58f135a3223f7f64f";
        public const string SERVER_ROUTABLE_IP_ADDRESS = "a41300f1defd4b4da496122c314aeaba";
    }

	public class EventHistorySortByTypes
	{
		public const string EVENT_DATE_TIME = "0154d281c5ad4bd087ced7c44785ae49";
	}

	public class CommandHistorySortByTypes
	{
		public const string REQUEST_DATE_TIME = "01a61b55e8bc443db746346916301b67";
	}

    public class RetrieveResultSortByTypes
    {
        public const string RESULT_DATE_TIME = "790eb4b869504eb38a85c3ac9b305ceb";
    }

    public class GatewayRetrieveCommunicationHistorySortByTypes
	{
		public const string REQUESTED_DATE_TIME = "14f4195789e6452897ed704036adb587";
	}

	public class FileRetrieveListSortByTypes
	{
		public const string FILE_NAME = "5facb5fed745497cb3f00731d3dd2dba";
	}

    public class GatewayResources
    {
        public const byte DC_NODE_STATUS = 0x82;
        public const byte DC_DEVICE_STATUS = 0x84;
        public const byte DC_MEP_DEVICE_STATUS = 0x94;
        public const byte DC_WAN_STATUS = 0x97;
        public const byte DC_FUNCTION_STATUS = 0x8B;
    }

    public class ProcessedStatusTypes
    {
        public const string PROCESSED = "d433acf3ffa14ba291e9420f9633ba9c";
        public const string NOT_PROCESSED = "a8232a0cdd1b466281907d4240bec547";
    }

    public class ContinuousEventLogConfigurationStatusTypes
    {
        public const string ENABLED = "4136d666bffa4d58a19751d49a921c04";
        public const string DISABLED = "eb07455582dc4931af66acccce98de16";
    }

    public class ContinuousEventLogConfigurationPriorityLevelTypes
    {
        public const string CONTINUOUS_EVENT_LOG = "eb896d1d65e6481cb5d13304e1fe3ee1";
        public const string PRIORITY_CONTINUOUS_EVENT_LOG = "01cab46d123843e08167b797566c4a5b";
    }

    public class InterfaceReadStatusTypes
    {
        public const string PENDING = "F8C93741AC4649fa8EFDB12D22ECC920";
        public const string COMPLETE = "064672BFBB554bc0BECF983C7CA28EC1";
        public const string UNKNOWN = "53B8F4EB4C5E487e9661B85568170950";
    }

    public class BillingInterfaceMismatchTypes
    {
        public const string MATCH = "08B104E6641A462bBE1ECB8E29EFFCEA";
        public const string MISMATCH = "645677F2D5AE4d3280FA1632F0A0882B";
        public const string UNKNOWN = "CF1021170A8F45b6B55555F03FDCFED7";
    }

    public class MeterAlarmStatusTypes
    {
        public const string URGENT_EVENT = "b632849f0ced409ca99afd6a85308ef8";
        public const string NON_URGENT_EVENT = "ff38e2a7e97841689bca57fb3b577190";
        public const string NO_EVENT = "0e43f12720674744a994c9ab972b0d11";
    }

    public class MeterAlarmContinuousEventLogStatusTypes
    {
        public const string EVENT_LOG_READ_ON_OCCURRENCE = "d17db5bc2cc34e85b9984adbc6c7ede8";
        public const string NO_EVENT_LOG_READ_ON_OCCURRENCE = "c504cc32b39a4191b9f044292cc05fed";
    }

    public class GatewayEventStatusTypes
    {
        public const string ENABLED = "aafcb88e7b224ca1888c5ee6873727ce";
        public const string DISABLED = "b9f8a1e9685b4d76b20e4925a6cd4d7a";
    }

    public class GatewayEventOnClearStatusTypes
    {
        public const string ENABLED = "b9cde5d8a751440ba6f93c5f520b0322";
        public const string DISABLED = "3932475fffd14cb98afce178921922b9";
    }

    public class DCEvents
    {
        public const string HARDWARE_FAILURE = "63976eb22a434affbe7556c1e0f7a86f";
        public const string SOFTWARE_FAILURE = "d9500617d3f64aa7a91d331482f83d33";
        public const string DEVICE_COMMUNICATION_STATUS = "041cb4c4d7ac432c97328afbe7ceb616";
        public const string RECEIVED_DEVICE_CAUSE_CODE = "a1a66ac0e54941b087cec156c5fdd71e";
        public const string DEVICE_ALARM = "0e0caa59280f47f7a9b4eb3f6e52c224";
        public const string DETECTED_SEGMENT_DOWN = "d5daa7f085d84b35b0a2014aa59c6c5f";
        public const string FUNCTION_DONE = "4a6739dde7b94692924f20db4dc6b488";
        public const string RESOURCE_EXHAUSTION = "265ee17fc1c848c695c71646067d7b03";
        public const string UNPLANNED_REBOOT = "444a0b86e34b4187bb46c405375c62cb";
        public const string DEVICE_COMMUNICATION_FAILURE = "56a291c7b8ab4f08b47632db655e54dc";
        public const string SERVICE_TOOL_CONNECTED = "aa312203d1cc49399e2aeeae4252fe4b";
        public const string DETECTED_DEVICE_PHASE_CHANGE = "617788753a1c48e58eb900b7fb9627fe";
        public const string DETECTED_DEVICE_CLOCK_ERROR = "5cf7eddfe37d46bd8683b57d65d499c4";
        public const string DETECTED_DEVICE_PHASE_INVERSION = "33ebafd7f4544234b87bc15ea5628529";
        public const string PLANNED_REBOOT = "20326b9da4494adb99bfea70dfbdd033";
        public const string FLASH_LOW = "e48d0268333045aebc33515df2f2fab2";
        public const string SECURITY_EXCEPTION = "f29bbfde79e040289f24868e855d44d3";
        public const string DETECTED_AUTO_DISCOVERED_MBUS_DEVICE = "faaf691349c54630af1f91f1275cc97a";
        public const string MBUS_DEVICE_ALARM = "0f70c44476624907b8ed80d7893146af";
        public const string DETECTED_DEVICE_TEMP_DOWN = "84965a6c5ca6447c8983271372d4dad9";
        public const string AUTO_TEST_POINT_STATUS = "0c5d543d093849fab30dc0d16326a4c5";
        public const string MESH_STATUS_DETECTED = "3def0023c2db41cb9d50785d215fc2f4";
        public const string DEVICE_NON_URGENT_ALARM = "2f774facbeac40e183505fb42e025633";
        public const string WAN_FAILURE = "78469a992dbd4323b93b2ab6626e88b6";
        public const string WAN_SWITCH = "ff3888ccda0f4f1696f2107e225ba4aa";
        public const string UNANSWERED_CALL_ALERT = "2f6fc88d7b084da8ab2acd6f8bb6c434";
        public const string CAPACITY_CHANGED = "24f9be63b4d1419395abd829779287c4";
        public const string DETECTED_DEVICE_PHASE_INACCURACY = "648c506071df4f1984f8d5626226a86a";
        public const string WAN_EXCEPTION = "30674f7aacf042fab7deaca897bd0495";
        public const string DETECTED_DEVICE_INVALID_DATA_AVAILABLE = "4379f9e9404d469fa91318b60eb7e64d";
        public const string PHASE_OUTAGE = "62cc50f695a8482096a3c561f1bf5f21";
        public const string TEMPERATURE_ALERT = "f362e5e8b216478e8c396b2f88a7e6c3";
        public const string CONFIGURATION_ERROR = "1c64c75bf8364c8fb9b992008f49bdf6";
        public const string DETECTED_DEVICE_BROWN_OUT = "4d4597bdd7274387b69667d6df65b723";
        public const string MODEM_STATUS_UPDATE = "ad3f5cea0306459282293ff2e34ba7db";
        public const string EXTERNAL_EVENT_0 = "e85ae3ed45ae4915a501cdfd015a0b26";
		public const string GPS_COORDINATES_REPORTED = "05b37d1a4f7d4f43894f8c9ef5af8fd2";
		public const string CLIENT_PROCESS_RESTART_OCCURRED = "33766e58e8274040a6698acc35262da8";
		public const string CONFIGURATION_STATE_CHANGE_REPORTED = "0f1853ae54754790b299799948bf1747";
		public const string CRITICAL_MESSAGE_LOG_REPORTED = "7ffc716a01b64094b693f6754a519648";
		public const string COMMUNICATION_PROTOCOL_SECURITY_LOGIN_FAILURE = "0e3a3735db0c4c579060343f44fc610c";
		public const string COMMUNICATION_PROTOCOL_SECURITY_LOCKOUT = "105dac72bf8f48a69ce62e750b19cc8f";
		public const string WIRELESS_MAC_ID_LOGIN_FAILURE = "da3ecbc7af2742fab3c1023a4f80e7de";
		public const string WIRELESS_PROTECTED_ACCESS_LOGIN_FAILURE = "5b67aabdeff5432f937f52191ca11095";
		public const string TAMPER_DETECTED = "9b5763de06ee4540a5e502e05026d7e5";
        public const string DETECTED_INVALID_DEVICE_LOAD_PROFILE_DATA_SET_COLLECTION_REQUESTED = "90a3ac15a8314a2eaa1298ab598d231a";
		public const string PHASE_ROTATION = "a152064cf7f54c6290cc9c4e5e209ddd";
		public const string COMMISSION_STATUS_CHANGED = "2978ab4ff8194751b627c2e56998c582";

		[Obsolete("v5.3 - no replacement, no longer used")]
		public const string NODE_TO_NODE_NETWORK_MINIMUM_PEER_COUNT_THRESHOLD_EXCEEDED = "497fe0ee04624ee59c3b7dced336a747";
		[Obsolete("v5.3 - no replacement, no longer used")]
		public const string BACKUP_POWER_THRESHOLD_EXCEEDED = "9f81db5430f5453d915623a841574485";
		[Obsolete("v5.3 - no replacement, no longer used")]
		public const string WIRELESS_ACCESS_POINT_INTRUSION_LOCKOUT_OCCURRED = "b6aab0864e0844aaa9f08b8bd1c2c075";
    }

    public class MeterAlarms
    {
        public const string CONFIGURATION_ERROR = "8ca7f8c35d2247a397c49c9f33d668a0";
        public const string SYSTEM_RESET = "21b3b465b3e64bce952133f43f706c5c";
        public const string RAM_FAILURE = "e5a5b0d1f1e44129a98fddaf6ce4398a";
        public const string ROM_FAILURE = "79c97a9c3bb147f7a21e921d43d51eb3";
        public const string NON_VOLATILE_MEMORY_FAILURE = "82a33447b5ab434bb4e578eec104bcea";
        public const string CLOCK_ERROR = "f316273e6f374e0086710af9d101054f";
        public const string MEASUREMENT_ERROR = "41e0b86652164cd4a539b226cea18719";
        public const string LOW_BATTERY = "4ea1f5f2583f4017b8c449d6423123ca";
        public const string POWER_FAILURE = "b02fb601b98a47ce8de32dbb157fb2a2";
        public const string TAMPER_DETECTED = "6f37d5a7280b405288fdb174c5a1af52";
        public const string REVERSE_ENERGY = "729ba6910acb4426a0a0706005a58976";
        public const string DATA_BACKUP_INCOMPLETE = "0ef90b3b76b94967b0e623386472e25c";
        public const string DISCONNECT_SWITCH_ERROR = "75fbf631eb7947d29138327c11291ff1";
        public const string LOAD_PROFILE_OVERFLOW = "3acc6522fcb342748773009d412cd0a2";
        public const string LOAD_DISCONNECT_STATUS_CHANGE = "8c61b51eeaad414cae2e8a40c758debc";
        public const string CONTROL_RELAY_STATUS_CHANGE = "aec6c8c2237e469aa0d1081f18486354";
        public const string PHASE_LOSS = "da41430263f2482c83180c7ec6ca5282";
        public const string PHASE_INVERSION = "bc648feb5e324de9ae4578bc89a8d557";
        public const string PLC_COMMUNICATION_FAILURE = "88af05a6ce154fe5a4da71b92b1c6f88";
        public const string GENERAL_ERROR = "2dd6e07c1bcc4d78bd4a8014f3fe21b2";
        public const string INVALID_PASSWORD_OVER_OPTICAL = "39320b04e50d41f5ac68391dbd4d0131";
        public const string REMOTE_COMMUNICATIONS_INACTIVE = "782db533d360401189e5f08b7f895d80";
        public const string CURRENT_ON_MISSING_OR_UNUSED_PHASE = "a7f07be6c8414b79b7b1a3ebbfe60ee2";
        public const string PULSE_INPUT_1_TAMPER = "e00f616458684d26b4882b5e33af693c";
        public const string PULSE_INPUT_2_TAMPER = "0618bed8c9dc4be3941f0207c699632e";
        public const string LOAD_PROFILE_BACKFILL_FAILED = "a3c4c5e34c62484aaba533c16e5b7b0c";
        public const string INTERNAL_EXPANSION_MODULE_CARD_INSTALLATION_OR_REMOVAL = "581b5ac07e474811890becb076a8ffeb";
        public const string PHASE_ROTATION = "a152064cf7f54c6290cc9c4e5e209ddd";
        public const string PREPAY_CREDIT_EXHAUSTED = "a225ba7c5a894589be61aeef58592e9b";
        public const string PREPAY_WARNING_ACKNOWLEDGEMENT = "dce1d1c4935f40b3a07cf417f41b21ce";
        public const string EVENT_LOG_PENDING_OVERFLOW = "a0b97660e3544be7beb0f84bb3e8be44";
        public const string POWER_QUALITY_EVENT_DETECTED = "5830f4de931a434dadd080c011756ae5";
        public const string UNREAD_EVENT_LOG_ENTRY_EXISTS = "811eb4f6f85e4870bc86dfe9285d6a67";
        public const string MAGNETIC_TAMPER = "b3de1c9ae0c4433b8a5150d9f3c1a554";
        public const string ACCESS_LOCKOUT_OVERRIDE = "53bfdc0c8b9341688a520134b1098b26";
        public const string LOAD_SIDE_VOLTAGE_WITH_OPEN_DISCONNECT_SWITCH = "1acb13f94e224d5491fea02233ec41c5";
        public const string TOTAL_HARMONIC_DISTORTION_EVENT_DETECTED = "5b107ab0c46b492a9cce040192176261";
        public const string HIGH_FREQUENCY_NOISE_DETECTED = "b9b2d7114d084943be571a6ead9a0c38";
		public const string EXTERNAL_VARIABLE_ALARM_DETECTED = "c101b452c1d94c37b5109bae0c254ba9";
		public const string NEUTRAL_BYPASS_DETECTED = "4b69e1145b9e46c0a88ac2e9ee5166cc";
        public const string UNBALANCED_VOLTAGE_DETECTED = "52b806ffc91848048c71be568bfbd976";
		public const string KEY_CHANGED = "6abe933cac434959ab0f0d2e6597a4dc";

        ////////////////////////////////////////////////////////////////////////////////////////
        //The following are deprecated - the version in which they were deprecated and the    //
        //constant replacing them (if any) is listed to the right of the constant.            //
        ////////////////////////////////////////////////////////////////////////////////////////

        [Obsolete("v4.1 - use CONTROL_RELAY_STATUS_CHANGE")]
        public const string CONTROL_RELAY_ACTIVATED = "aec6c8c2237e469aa0d1081f18486354";
    }

	public class MBusAndMEPAlarms
    {
        public const string DEVICE_ALARM_REPORTED = "2aefb2504737408eb7faecbaeaddb70b";
        public const string BILLING_READ_OVERFLOW_OCCURRED = "6756f25cd81948fa896b95818df979ed";
        public const string FAILED_COMMUNICATIONS_ON_READ = "458118b43afa430c8cc51021a3f38ca4";
        public const string BILLING_READ_SERIAL_NUMBER_MISMATCH = "159209907c714597b7cc7baad15ee6ee";
    }

    public class DataConcentratorHardwareFailureCodes
    {
        public const UInt16 DRAM = 1;
        public const UInt16 FLASH = 8;
        public const UInt16 SRAM = 16;
        public const UInt16 RTC = 32;
        public const UInt16 NEURON = 256;
        public const UInt16 CLOCK = 512;
        public const UInt16 PHASE = 1024;
    }

    public class DataConcentratorSoftwareFailureCodes
    {
        public const UInt16 NONE = 0;
        public const UInt16 BOOT_LOOP = 1;
        public const UInt16 FILE_ERROR = 2;
        public const UInt16 DCX_UPDATE = 3;
        public const UInt16 RTC_NOT_SET = 4;
        public const UInt16 UPDATE_IMAGE_CRC = 5;
        public const UInt16 RTC_BATTERY_LOW = 6;
        public const UInt16 INVALID_PPP_USERNAME_OR_PASSWORD = 7;
        public const UInt16 INTERNAL_REPEATING_LISTS_ERROR = 8;
        public const UInt16 SRAM_CHECKSUM_ERROR = 9;
        public const UInt16 IMPROPER_SHUTDOWN = 10;
        public const UInt16 EXTENDED_DATA_PROPERTY_ERROR = 11;
    }

    //The integer constants in this class are mapped to the GUID
    //constants in the GatewayToDeviceCommunicationStatusTypes class
    public class DataConcentratorDeviceCommunicationStatusCodes
    {
        public const byte UP = 0;
        public const byte DOWN = 1;
        public const byte NO_AGENT = 2;
        public const byte CONFIRMED_DOWN = 3;
        public const byte NASCENT = 4;
        public const byte WRONG_KEY = 5;
        public const byte INVALID_NEURON_STATE = 6;
        public const byte UNKNOWN = 7;
        public const byte CONFIRMED_NO_AGENT = 8;
    }

    //The GUID constants in this class are mapped to the integer
    //constants in the DataConcentratorDeviceCommunicationStatusCodes class
    public class GatewayToDeviceCommunicationStatusTypes
    {
        // ------------METER type specific---------------
        public const string UNKNOWN = "f4183c2b23d7458a9e78529ae9c3cc27";
        public const string UP = "c4a83cbbb8ac4794ab59562bc4b88a32";
        public const string DOWN = "587080ce53fb4a95ba6dc51838d410c9";
        public const string CONFIRMED_DOWN = "af5fdf7f4c644936b131e3ac3d9d6926";
        public const string NO_AGENT = "3680be224f2745e2a132c651d22666dd";
        public const string NASCENT = "198044101bde44c483325d3a5803e407";
        public const string WRONG_KEY = "f0b97e6646c8494c964d972c19a634ad";
        public const string INVALID_NEURON_STATE = "fd345d84467d4841b87f7ea17d5e11d2";
        public const string CONFIRMED_NO_AGENT = "bc0067045e3b4a06b826c62c102cb703";
        // -----------------------------------------
    }

    public class DeviceNackCodes
    {
        public const byte OK = 0;
        public const byte ERROR = 1;
        public const byte SERVICE_NOT_SUPPORTED = 2;
        public const byte INSUFFICIENT_SECURITY_CLEARANCE = 3;
        public const byte OPERATION_NOT_POSSIBLE = 4;
        public const byte INAPPROPRIATE_ACTION = 5;
        public const byte BUSY = 6;
        public const byte INVALID_SERVICE_SEQUENCE_STATE = 10;
        public const byte DIGEST_ERROR = 11;
        public const byte SEQUENCE_NUMBER_ERROR = 12;
        public const byte INCOMPATIBLE_ERROR = 30;
        public const byte INTERFACE_CHANGE = 31;
        public const byte PROC_COMPLETED = 100;
        public const byte PROC_ACCEPTED = 101;
        public const byte PROC_INVALID_PARAM = 102;
        public const byte PROC_CONFLICT_SETUP = 103;
        public const byte PROC_TIMING_CONSTRAINT = 104;
        public const byte PROC_NO_AUTHORIZATION = 105;
        public const byte PROC_UNRECOGNIZED = 106;
    }

    public class DataConcentratorResourceExhaustionCodes
    {
        public const byte DCXP_EVENT = 137;
        public const byte DCXP_RESULT = 142;
        public const byte DCXP_DATA = 146;
        public const byte DCX_MEP_DEVICE = 147;
    }

    public class DataConcentratorRebootCodes
    {
        public const byte POWER_UP = 0;
        public const byte PANIC_RESET = 1;
        public const byte CACHE_FLUSH_FAIL = 2;
        public const byte CLOCK_WRAPAROUND = 3;
        public const byte EXTERNAL_REQUEST = 4;
        public const byte LOCAL_REQUEST = 5;
        public const byte BOOT_API_RESET = 6;
        public const byte NO_MEMORY = 7;
        public const byte WATCHDOG = 8;
        public const byte REASON_OUT_OF_RANGE = 9;
        public const byte MODEM_FAILURE = 10;
        public const byte UNKNOWN_EXCEPTION = 11;
        public const byte FATAL_TAMPER = 12;
        public const byte BUFFER_EXHAUSTION = 13;
        public const byte WAN_INACTIVE = 14;
    }

    public class DataConcentratorCauseCodes
	{
        public const byte NO_ERROR = 0;
        public const byte UNKNOWN = 1;
        public const byte RESOURCE_NOT_FOUND = 2;
        public const byte NOT_IMPLEMENTED = 3;
        public const byte INVALID_PARAMETER = 4;
        public const byte OPERATION_FAILED = 5;
        public const byte INTERNAL_FAILURE = 6;
        public const byte OUT_OF_RANGE = 7;
        public const byte INVALID_LENGTH = 8;
        public const byte PROPERTY_NOT_FOUND = 9;
        public const byte OPERATION_NOT_ALLOWED = 10;
        public const byte REPEAT_CHAIN_TOO_LONG = 11;
        public const byte LIMIT_EXCEEDED = 12;
        public const byte RESOURCE_DISABLED = 13;
        public const byte PREVIOUS_SEGMENT_NOT_FOUND = 14;
        public const byte SEGMENT_LOOP = 15;
        public const byte DUPLICATE_NODE_ADDRESS = 16;
        public const byte SEGMENT_NOT_FOUND = 17;
        public const byte RESOURCE_IN_USE = 18;
        public const byte DEVICE_NOT_DEFINED = 19;
        public const byte MESSAGE_SET_NOT_FOUND = 20;
        public const byte DEVICE_DID_NOT_RESPOND = 21;
        public const byte FUNCTION_ABORTED = 22;
        public const byte OPERATION_EXPIRED = 23;
        public const byte INVALID_SEGMENT = 24;
        public const byte INCUFFICIENT_RESOURCES = 25;
        public const byte INVALID_DATETIME = 26;
        public const byte IMAGE_NOT_FOUND = 27;
        public const byte IMAGE_CRC_FAILURE = 28;
        public const byte TOO_MANY_TEST_POINTS = 29;
        public const byte DEVICE_PROCEDURE_FAIL = 30;
        public const byte TARGET_DISABLED = 32;
        public const byte ADDRESS_ERROR = 33;
        public const byte DEVICE_NOT_REACHABLE = 34;
        public const byte AUTH_RESPONSE_FAILURE = 35;
        public const byte RESPONSE_NOT_AUTHENTIC = 36;
        public const byte INVALID_RESPONSE = 37;
        public const byte TARGET_DOES_NOT_ANSWER_AGENT = 38;
        public const byte TARGET_OFFLINE = 39;
        public const byte REPEATER_1_FAILURE = 40;
        public const byte REPEATER_2_FAILURE = 41;
        public const byte REPEATER_3_FAILURE = 42;
        public const byte REPEATER_4_FAILURE = 43;
        public const byte REPEATER_5_FAILURE = 44;
        public const byte REPEATER_6_FAILURE = 45;
        public const byte REPEATER_7_FAILURE = 46;
        public const byte REPEATER_8_FAILURE = 47;
        public const byte PHASE_NOT_MEASURABLE = 48;
        public const byte INVALID_STATE = 49;
        public const byte GENERIC_RESPONSE_FAILURE = 50;
        public const byte INVALID_HANDLE = 51;
        public const byte INVALID_STATE_FOR_ONLINE = 52;
        public const byte INVALID_DEVICE_TYPE_FOR_OPERATION = 53;
        public const byte AGENT_HAD_NO_BUFFER_FOR_RESPONSE = 54;
        public const byte APPLICATION_AUTHENTICATION_FAILURE = 55;
        public const byte DCXP_SOCKET_NOT_SECURED = 56;
        public const byte DCX_NOT_READY_TO_HANDLE_REQUEST = 57;
        public const byte COMPRESSION_FAILURE = 58;
        public const byte DATA_OVERFLOW = 59;
        public const byte INVALID_DATATYPE = 60;
        public const byte DUPLICATE_PROPERTY = 61;
        public const byte TRANSACTION_NUMBER_MISMATCH = 62;
        public const byte TRANSACTION_NUMBER_INTEGRITY = 63;
        public const byte MEP_DEVICE_NOT_FOUND = 64;
        public const byte OPERATION_INTERRUPTION = 65;
        public const byte MIXED_ENCRYPTION = 66;
        public const byte WRONG_KEY = 67;
        public const byte CAPABILITY_REQUIRED = 68;
        public const byte DECRYPTION_FAILURE = 69;
        public const byte OPERATION_REJECTED = 70;
        public const byte NOT_MODIFIABLE = 71;
        public const byte PHASE_ABORT = 72;
        public const byte WAN_CONFIG_ERROR = 73;
        public const byte UNEXPECTED_DEVICE_RESET = 74;
        public const byte MODEL_NUMBER_MISMATCH = 75;
        public const byte FIRMWARE_VERSION_NUMBER_MISMATCH = 76;
        public const byte DEVICE_IS_READ_WRITE_PROTECTED = 77;
        public const byte DEVICE_APP_CHECKSUM_VERIFICATION_FAILED = 78;
        public const byte DEVICE_DATA_ERROR = 79;
        public const byte IDI_MISMATCH = 80;
        public const byte NOT_EXECUTED = 81;
        public const byte RESOURCE_DATA_ERROR = 82;
        public const byte DORMANT_STATE = 83;
        public const byte UNABLE_TO_INSTALL_UPGRADE = 84;
        public const byte INVALID_IMAGE = 85;
        public const byte UPGRADE_SCRIPT_ERROR = 86;
        public const byte IMAGE_SWITCH_FAILED = 87;
        public const byte UNDEFINED_DATA_GROUP = 88;
        public const byte UNDEFINED_DATA_SOURCE = 89;
        public const byte CO_PROCESSOR_IMAGE_LOADING_ERROR = 90;
        public const byte LONTALK_NETWORK_INTERFACE_CURRENTLY_DISABLED = 91;
        public const byte DEVICE_RESPONSE_CODE_UNKNOWN = 100;
        public const byte DEVICE_REQUEST_REJECTED = 101;
        public const byte DEVICE_SERVICE_NOT_SUPPORTED = 102;
        public const byte DEVICE_SECURITY_FAILURE = 103;
        public const byte DEVICE_OPERATION_NOT_POSSIBLE = 104;
        public const byte DEVICE_ACTION_INAPPROPRIATE = 105;
        public const byte DEVICE_BUSY = 106;
        public const byte DEVICE_DATA_NOT_READY = 107;
        public const byte DEVICE_DATA_LOCKED = 108;
        public const byte DEVICE_RENEGOTIATE_REQUEST = 109;
        public const byte DEVICE_INVALID_SERVICE_SEQUENCE_STATE = 110;
        public const byte DEVICE_COULD_NOT_AUTHENTICATE_REQUEST = 111;
        public const byte DEVICE_INVALID_AUTHENTICATION_SEQUENCE_NUMBER = 112;
        public const byte DEVICE_PROCEDURE_ACCEPTED_BUT_NOT_COMPLETE = 113;
        public const byte DEVICE_PROCEDURE_INVALID_PARAMETER = 114;
        public const byte DEVICE_PROCEDURE_CONFIGURATION_CONFLICT = 115;
        public const byte DEVICE_PROCEDURE_TIMING_CONSTRAINT = 116;
        public const byte DEVICE_PROCEDURE_NOT_AUTHORIZED = 117;
        public const byte DEVICE_PROCEDURE_ID_INVALID = 118;
        public const byte DEVICE_ON_DEMAND_REQUEST_TABLE_FULL = 119;
        public const byte DEVICE_UNKNOWN_PROC_RESPONSE = 120;
        public const byte DEVICE_ICA_NACK = 121;
        public const byte DEVICE_INCOMPATIBLE_REQUEST = 122;
        public const byte DEVICE_INCOMPATIBLE_REQUEST_2 = 145;
    }

    public class DataConcentratorSecurityExceptionCodes
    {
        public const byte NONE = 0;
        public const byte ST_CONSOLE_PASSWORD_FAILURE = 1;
        public const byte ST_PPP_CHAP_FAILURE = 2;
        public const byte WAN_PPP_CHAP_FAILURE = 3;
        public const byte WAN_APP_AUTH_FAILURE = 4;
        public const byte WAN_SSL_FAILURE = 5;
        public const byte LOCAL_SSL_FAILURE = 6;
        public const byte DCXP_SESSION_LOGIN_TIMEOUT = 7;
        public const byte DCXP_SESSION_CAPABILITY_UNRECOGNIZED_BY_PEER = 8;
        public const byte DCXP_SESSION_CAPABILITY_NOT_AGREED_BY_PEER = 9;
    }

    public class SelfReadRetrievalStatusTypes
    {
        public const string INCLUDE = "a4d4c080d4534005a467a4be7f33a022";
        public const string EXCLUDE = "a1143747c8c147ea8757912435731a17";
    }

    public class ContinuousDeltaLoadProfileCollectionStatusTypes
    {
        public const string DISABLED = "cd511bfffee44595809881b91797fe9a";
        public const string ENABLED = "58df98e571ac45789165e3147a1c8a8d";
    }

    public class ContinuousDeltaLoadProfileCollectionStoppedReasonTypes
    {

        public const string GATEWAY_CONTINUOUS_MODE_NOT_SUPPORTED = "3a0c503044cd4ed0b7f1a2a64e9e9014";
        public const string SYSTEM_CONTINUOUS_MODE_NOT_SUPPORTED = "288ec5177876429aa6782177e3fcfe22";
        public const string GATEWAY_LOAD_PROFILE_DATA_SET_NOT_SUPPORTED = "92ea2ee832a6460ea95e3399d7a26bf6";

        ////////////////////////////////////////////////////////////////////////////////////////
        //The following are deprecated - the version in which they were deprecated and the    //
        //constant replacing them (if any) is listed to the right of the constant.            //
        ////////////////////////////////////////////////////////////////////////////////////////

        [Obsolete("v5.0.01 - no replacement")]
        public const string FAILED_ENABLE_DEVICE_CONTINUOUS_DELTA_LOAD_PROFILE_STATUS = "16f3f43e12e9463cac42b24bcbc2ced3";
    }

    public class CumulativeDemandStatusTypes
    {
        public const string ENABLED = "6D87FF6A892A4434B9B0BC2AC926818B";
        public const string DISABLED = "DCE74898E62D47958F8DA37653C6AB76";
    }

    public class ContinuousCumulativeDemandStatusTypes
    {
        public const string ENABLED = "D3EA8F3A288246e48166BD28B7915B16";
        public const string DISABLED = "66A38719B8E547a58D04264162980756";
    }

    public class PresentDemandCalculationTypes
    {
        public const string USE_ENTIRE_INTERVAL_LENGTH = "CD3155FF4E604e1fAC0F40FFE7C66A41";
        public const string USE_TIME_SINCE_LAST_END_OF_INTERVAL = "8538D3FB783F447e99E92225331C5864";
    }

    public class DemandConfigurationFailureTypes
    {
        public const string GENERAL_FAILURE = "2AD0B6F1B32044a8975093052B14B944";
        public const string INSUFFICIENT_MEMORY = "E94F66816B204551978E9DB4D9A1D5A2";
        public const string DEMAND_NOT_ACTIVATED = "164F4471B76D4594A8C5BD294FDB4720";
    }

    public class PhaseStatusTypes
    {
        public const string POWERED = "8017744a58e3445ea13c9a5aaffe4997";
        public const string NOT_POWERED = "de0c5cd435e84b82a85de4c5cb5f9879";
    }

    public class LoadProfileConfigurationAnsiComplianceStatusTypes
    {
        public const string COMPLIANT = "1c4ea8cb81464b6cbd34f4d2a3351986";
        public const string NOT_COMPLIANT = "ff85255975e843d5b89e5f0911697291";
    }

    public class LoadProfileConfigurationSkippedIntervalValuesTypes
    {
        public const string INSTANTANEOUS_VALUES_SET_TO_ZERO = "ea97d041ee7e4bcea9e72ab2a925eda0";
        public const string INSTANTANEOUS_VALUES_FROM_PREVIOUS_INTERVAL = "68e7d87378e24f90b9b08535a8cd0471";
    }

    public class DemandConfigurationStatusTypes
    {
        public const string KNOWN_CONFIGURATION = "afed364af1704b26bbccdbd78ae24b97";
        public const string UNKNOWN_CONFIGURATION = "7424080f71894d98bad59757a18d32e1";
    }

	public class DeviceKeyCategories
	{
		public const string METER_OEM = "46ca5ac9e87e4296bc8f87c14e0604d3";
		public const string METER_DEMAND_ACTIVATION = "bee42657ada54779b29bfe4629d21abc";
	}

	public class GatewayKeyCategories
	{
		public const string DATA_CONCENTRATOR_DEVICE_LIMIT = "9d43acc1166e4b8f85ac94a43a6bad5d";
		public const string DATA_CONCENTRATOR_OEM = "aa0d78fbdaa0457ba4d106f37c3cbf35";
        public const string DATA_CONCENTRATOR_FEATURE = "4de3195ab82f4a3cbfcb665fce06af7f";
	}

	public class DataContentReturnTypes
	{
		public const string COUNT_ONLY = "d4282122c0904a5a92dbe5bd9c7a5164";
		public const string LIST_AND_COUNT = "c6ce34a3b0f54e45a106b5b429858b73";
		public const string LIST_ONLY = "55e700c3acd84c53a65f10a496249af7";
	}

    public class LogicalRestrictionTypes
	{
		public const string OR = "5a610ce3b4904ce6b5b8d10929d8a706";
		public const string AND = "2bcd3f55ff1b40a1a638890a8fba13fd";
	}

    public class GatewayRetrieveListSortByTypes
    {
        public const string GATEWAY_NAME = "5252644cfa9640d0a41c5c008223f989";
        public const string LAST_SERVER_INITIATED_HIGH_PRIORITY_COMMUNICATION_DATE_TIME = "6c8f3674fb1146bbb8f05c5af5678984";
        public const string LAST_SERVER_INITIATED_NORMAL_PRIORITY_COMMUNICATION_DATE_TIME = "f7ec9881805a484390ba6db35edd2d9b";
        public const string LAST_GATEWAY_INITIATED_COMMUNICATION_DATE_TIME = "c3835fb3813e4a3c8d3dc7ca9432e9f4";
    }

    public class ValueMatchTypes
    {
        public const string EQUALS = "6d7a4195d4ce44ccbd7f5bd6f41fddeb";
        public const string CONTAINS = "83ed1f09b4ec493eaddb6482fa1e1f42";
    }

    public class GatewayAtmModeTypes
    {
        public const string AUTOMATIC_ASSIGNMENT = "08bf032488404c6881ed76f157c22c9c";
        public const string MANUAL_ASSIGNMENT = "372f9c0d5fce4aaf8104cb2fb7ad6086";
    }

    public class DeviceAtmModeTypes
    {
        public const string AUTOMATIC_ASSIGNMENT = "ee79dbcf1b3d4e83866e4d6e94f2735a";
        public const string MANUAL_ASSIGNMENT = "7448fe44ed07453c88df16e824b4babc";
    }

	public class EventTypes
	{
		public const string GATEWAY = "8006884b27c8456db8544db5b57c8167";
		public const string DEVICE = "634f74915323405bac754e25971ed061";
        public const string SYSTEM = "2b4a334dc7104645bddeec9c44090cde";
	}

    public class DeviceRetrieveListSortByTypes
    {
        public const string DEVICE_NAME = "e0ee83acc7a842149bd2b3e71fe77871";
        public const string GATEWAY_NAME = "5da8c5036b464e9e95028308b2b8743d";
        public const string LAST_DELTA_LOAD_PROFILE_RESULT_RECEIVED_DATE_TIME = "de8272e46653498f8ddc6ba3e9a548de";
        public const string LAST_END_OF_CYCLE_BILLING_DATA_RESULT_RECEIVED_DATE_TIME = "736bca483c3b42919b3d70307e0ddf0b";
    }

    public class DemandActivatedValues
    {
        public const int UNKNOWN = -1;
        public const int NOT_ACTIVATED = 0;
        public const int ACTIVATED = 1;
    }

    public class DemandConfiguredValues
    {
        public const int UNKNOWN = -1;
        public const int NOT_CONFIGURED = 0;
        public const int CONFIGURED = 1;
    }

	public class GatewayEventRetrieveTypes
	{
		public const string GATEWAY_ONLY = "685193ef9a90416bb7868b6a5621e64c";
		public const string GATEWAY_AND_DEVICE = "ad76f5bd02ad4fa680d44676b5250b24";
	}

	public class HierarchyRetrieveTypes
	{
		public const string NOT_ASSIGNED = "ec599a1b4ce04229b5db3b87fe644f87";
	}

	public class AttributeRetrieveTypes
	{
		public const string NOT_ASSIGNED = "3b5e583a3176440fb41a51d31109666f";
	}

    public class CommandCancelledTypes
    {
        public const string DUPLICATE_SET_LOCAL_DATA_ACCESS_CONFIGURATION_TASK = "0f07776ae054438c97b1bc1b60e0b390";
    }

    public class CommandFailureReasonTypes
    {
        public const string INTERFACE_UNAVAILABLE = "29f99e44f223430fa942182d302f1e77";
		public const string CONFIGURATION_ERROR = "80dd089b36ea41238fc6461862a5299c";
    }

    public class CommissionProcessModes
    {
        public const int SCHEDULE = 0;
        public const int IMMEDIATE = 1;
    }

    public class DataConcentratorLoadProfileSourceTypes
    {
        public const string FILE = "4922fe80ac664a60ac20d68e1e5f351c";
        public const string DATA_RESOURCE = "539273fa3ee84412a75d49b565c512d7";
    }

	public class MepReadScheduleFrequencyTypes
	{
		public const string HOURLY = "d97190529c6d4d76afed03e25983a329";
		public const string DAILY = "a012eb366b6b4f74b3c89e493d6202ac";
		public const string WEEKLY = "e6d892adb80e4028bef694275fff4e5e";
		public const string MONTHLY = "d8cad4be3abb42a38124c5e45389515f";
		public const string NEVER = "16aa64a1cd1a45e7bdc904dd6bf4b87b";
	}

    public class DataUrgencyTypes
    {
        public const string URGENT = "3c82154f793f4c2cbe9e8bf494c686af";
        public const string NON_URGENT = "3b17d4df879c492aa58bdc213f861a15";
    }

	public enum MeterKeyTypes : byte
	{
		OPTICAL = 0
	}

	public enum GatewayKeyTypes : byte
	{
		PLC = 0
	}

	public enum PowerLineCarrierModes : byte
	{
		EXTERNAL = 0,
		INTERNAL = 1
	}

	public enum PowerLineCarrierModeTransitions : byte
	{
		SYNCHRONIZED = 0,
		IMMEDIATE = 1
	}

	public enum WANAdapterStatus : byte
	{
		ON = 0,
		OFF = 1
	}

    public class THDCalculationMethods
    {
        public const int OPTION_1 = 0;
        public const int OPTION_2 = 1;
    }

	public class AtmActions
	{
		public const byte AUTOMATIC_ASSIGNMENT = 0;
		public const byte ASSIGNED = 1;
		public const byte UNASSIGNED = 2;
		public const byte FINAL_PLACEMENT = 3;
		public const byte MANUAL_ASSIGNMENT = 4;
		public const byte NO_ACTION = 5;
	}

	public class AtmAssignmentFailureCodes
	{
		public const byte OVERALL_DEVICE_ATM_PROCESS_TIMEOUT_EXPIRED = 0;
		public const byte NO_CANDIDATE_GATEWAY_FOUND = 1;
		public const byte ASSIGNMNET_TO_CANDIDATE_GATEWAY_FAILED = 2;
	}

	public enum AtmUnassignReasonCodes : byte
	{
		AUTO_ASSIGNMENT = 0,
		COMMISSION_DATA_COLLECTED = 1,
		ADD_FAILED = 2,
		COMMISSION_TIMEOUT = 3,
		FINAL_TIMEOUT = 4
	}

	public enum AtmManualAssignmentReasonCodes : byte
	{
		MAX_ASSIGNMENTS_NO_CANDIDATE_GATEWAY = 0,
		FINAL_TIMEOUT = 1,
		FINAL_ASSIGNMENT_FAILURE = 2,
		ACCEPTABLE_GATEWAY_FOUND = 3,
		SET_DEVICE_ATM_CONFIGURATION = 4
	}

	public enum AtmNoActionReasonCodes : byte
	{
		NO_DISCOVERY_DATA = 0,
		DISCOVERY_DATA_INVALID = 1,
		COMMISSION_ATTEMPT_IN_PROGRESS = 2
	}

	public enum AtmFinalPlacementReasonCodes : byte
	{
		MAX_ASSIGNMENTS_REACHED = 0,
		NO_NEW_DISCOVERY_DATA = 1
	}

	public enum AtmDiscoveryDataValidityCodes : byte
	{
		VALID = 0,
		TOO_OLD = 1,
		COMMISSION_ALREADY_COLLECTED = 2,
		MAX_ASSIGNMENTS_REACHED = 3
	}

    public class ConsumptionBasedControlStatus
    {
        public const int ENABLED = 1;
        public const int DISABLED = 0;
    }

    public class PowerThresholdSource
    {
        public const int FORWARD_PLUS_REVERSE_POWER = 0;
        public const int FORWARD_MINUS_REVERSE_POWER = 2;
    }

    public class DataRecordHeaderEntryTypes
    {
        public const int NOT_USED = 0;
        public const int MBUS_BASE = 1;
        public const int MEP_BASE = 2;
        public const int EXTENSION = 3;
    }

	public class ControlRelayTierStatus
	{
		public const byte DISABLED = 0;
		public const byte ENABLED = 1;
	}

	public class ControlRelayState
	{
		public const byte DISCONNECT = 0;
		public const byte CONNECT = 1;
	}

	public class ControlRelayRandomizationCommands
	{
		public const byte UNICAST = 0;
		public const byte BROADCAST = 1;
		public const byte TIER_BASED = 2;
		public const byte TIME_BASED = 3;
	}


    public class GatewayWANDataSignalsStatus
    {
        public const int ENABLED = 1;
        public const int DISABLED = 0;
    }

    public class GatewayWANInterfaceClass
    {
        public const int PPP = 0;
        public const int NDIS = 1;
    }

    public class GatewayWANSIMMethod
    {
        public const int AUTO = 0;
        public const int INTERNAL = 1;
        public const int EXTERNAL = 2;
    }

    public class GatewayWANAntennaMethod
    {
        public const int AUTO = 0;
        public const int INTERNAL = 1;
        public const int EXTERNAL = 2;
    }

    public class GatewayWANOTAUpdateMethod
    {
        public const int NO_OTA = 0;
        public const int OTASP = 1;
    }

    public class GatewayWANUpdateAction
    {
        public const int NONE = 0;
        public const int OTA_ACTIVATE = 1;
        public const int OTA_PRLUPDATE = 2;
        public const int FILE_ACTIVATE = 4;
        public const int FILE_PRLUPDATE = 5;
    }

	public class ControlRelayRandomizationStatus
	{
		public const int ENABLED = 1;
		public const int DISABLED = 0;
	}

	public class ControlRelayTimeModes
	{
		public const byte DISABLED = 0;
		public const byte DAILY = 1;
		public const byte WEEKDAY_WEEKEND = 2;
		public const byte SEASON = 3;
		public const byte SEASON_WEEKDAY_WEEKEND = 4;
	}

	public class ControlRelayDailySchedule
	{
		public const byte SUNDAY = 0;
		public const byte MONDAY = 1;
		public const byte TUESDAY = 2;
		public const byte WEDNESDAY = 3;
		public const byte THURSDAY = 4;
		public const byte FRIDAY = 5;
		public const byte SATURDAY = 6;
	}

	public class ControlRelayWeekdayWeekendSchedule
	{
		public const byte WEEKDAY = 0;
		public const byte WEEKEND = 1;
	}

	public class ControlRelaySeasonSchedule
	{
		public const byte SEASON0 = 0;
		public const byte SEASON1 = 1;
		public const byte SEASON2 = 2;
		public const byte SEASON3 = 3;
	}

	public class ControlRelaySeasonWeekdayWeekendSchedule
	{
		public const byte SEASON0_WEEKDAY = 0;
		public const byte SEASON0_WEEKEND = 1;
		public const byte SEASON1_WEEKDAY = 2;
		public const byte SEASON1_WEEKEND = 3;
		public const byte SEASON2_WEEKDAY = 4;
		public const byte SEASON2_WEEKEND = 5;
		public const byte SEASON3_WEEKDAY = 6;
		public const byte SEASON3_WEEKEND = 7;
	}

    public class ReplacedDC
    {
        public const int NO = 0;
        public const int YES = 1;
    }

    public class DisplayListStatus
    {
        public const int ENABLED = 0;
        public const int DISABLED = 1;
    }

    public class DisplayListTimeFormats
    {
        public const int UTC = 0;
        public const int LOCAL_TIME = 1;
    }

    public class DisplayListExtendedStatusDisplayChannelNumbers
    {
        public const int FIRST_TWO_CHARACTERS = 0;
        public const int ALL_FOUR_CHARACTERS = 1;
    }

    public class LoadProfileConfigurationTypes
    {
        public const string ENTIRE = "f3c39daca476445da26c0a87edb273c8";
        public const string DISPLAY_ONLY = "dcf92d62c15e441eb9ed90e8b7ac18f8";
    }

    public class LoadProfileConfigurationReadTypes
    {
        public const string FULL_CONFIGURATION = "d069197179f54d729eb236cefb312341";
        public const string CONFIGURATION_ID_ONLY = "a7ebc40ba89a443d97a87b24da96ba13";
    }

	public class SystemTaskTypes
	{
        public const string DATABASE_UPDATE = "adb7dd5841454a999a761ec7959473dd";
        public const string MESSAGE_LOG_UPDATE = "c43d48c44fdd4bd0bebde71aea08feda";
        public const string START_ENGINES = "759d846438654139b26215d863f49a20";
        public const string STOP_ENGINES = "46fa44185b8749cbb740e00792f3a75e";
        public const string ENGINE_STATE = "63ea5aeb384747fdbb12b500080582ad";
	}


	public enum SshConnectionFailureReasons : int
	{
		UNKNOWN = 0,
		NO_CREDENTIALS = 1,
		SERVER_UNAVAILABLE = 2,
		UNKNOWN_SSH_FINGERPRINT = 3,
		AUTHENTICATION_FAILED = 4
	}

    public class TrustedSourceTypes
    {
        public const string NES_SYSTEM_SOFTWARE = "b0af0793c61840e38f4b6f2d0b873eb5";
        public const string PROVISIONING_TOOL = "f82320c84301479b92aa31b19d05c006";
    }

    public class GatewayExportDataTypes
    {
        public const string CREDENTIALS = "7e2d22862d224ce9b0f8d77197ce1454";
    }

    public class DeviceExportDataTypes
    {
        public const string AUTHENTICATION_KEY_INFORMATION = "a694d08d0548484ca8c19d8c9c830ba1";
    }

    public class ExportProcessingCompletionStatusTypes
    {
        public const string SUCCESS = "080c26e2587b476cb3434c3da0144d2a";
        public const string FAILURE = "7dce9262326642eba18fcc1d66263de7";
    }

    public class ImportFailureReasonTypes
    {
        public const string REQUEST_TIMED_OUT = "b6c0e4bf98e046ac94d950e4a0ddc408";
        public const string UNEXPECTED_ERROR = "49aae33d03d14ed5ad65437b1e47cbb8";
		public const string UNSUPPORTED_ENCRYPTION_VERSION = "544b232e8a26445c91a6fad77355b3b3";
		public const string INVALID_DECRYPTOR_ECDH_PUBLIC_KEY = "191ded354ead4c1aa54dc03eea4cb25c";
		public const string INVALID_ENCRYPTOR_ECDH_PUBLIC_KEY = "8863eb9ab2d34d9f927b469089acff58";
		public const string ENCRYPTOR_ECDSA_PUBLIC_KEY_NOT_TRUSTED = "5cad3568ab6942bdaa54049edc602a3a";
		public const string INVALID_ENCRYPTOR_ECDSA_PUBLIC_KEY_SOURCE_TYPE_ID = "9215be0ef76644fbbd5a8a36c4afee40";
		public const string INVALID_DIGITAL_SIGNATURE = "9e3618d4beef4abd94332bfe3b009c15";
		public const string ORPHANED = "9b6522a78cdb41fcadf4dc701cf9daad";
		public const string UNSUPPORTED_DATA_FORMAT = "51d2d44c2ce0422aa1fdded100fd471a";
		public const string UNSUPPORTED_GATEWAY_VERSION = "f1bf6af69c124003abe44cef3b716071";
		public const string INVALID_NEURONID_SERIAL_NUMBER_COMBINATION = "2aaaa5c90fe34b29979dd799c4944e03";
		public const string ERROR_PROCESSING_DATA = "b5686e1573494ae7b88eeefa177433ac";
		public const string UNSUPPORTED_PLATFORM = "71affe0c755e40d4889080e0c559919b";
	}

	public class ExportFailureReasonTypes
	{
		public const string INVALID_ORIGINATION_PUBLIC_KEY = "db678281ea8944c68cee7d5118390ab0";
		public const string REQUEST_TIMED_OUT = "cdfbae65534f411ca9d5f2b89ce349ec";
		public const string ORPHANED = "c7f588df8d2d4ab4b4f7f34885e5ea25";
		public const string UNEXPECTED_ERROR = "446e2f1b0415475691f4ab647346d904";
		public const string UNSUPPORTED_PLATFORM = "94072bec60cd487e940989a3732b9fa0";
	}

	public class ConnectFailureReasons
	{
		public const string TASK_TIMED_OUT = "97b8cc108be849788505883112bccbad";
		public const string DATABASE = "8d222f27bf5c47dfa10880c5a197737e";
		public const string DC_ADAPTER_MESSAGE = "4684950565784ad2a7a60b36c75602a8";
		public const string HIGH_PRIORITY_FIRST_PING_OR_CONNECT_ATTEMPT = "ba194a030c8347e39505fd116a2f34e6";
		public const string ALL_WANS_ATTEMPTED = "1e917228ff0a4fc68c47e10a298cb133";
	}

    public class GroupMembershipActions
    {
        public const int ADD = 0;
        public const int REMOVE = 1;
    }

	public class DataCollectionTypes
	{
		public const string IMPORTED_FROM_PROVISIONING_TOOL_CREATED_FILE = "a9b91760980b45cd91ce6d74021b938f";
	}

	public class AsymmetricKeyTypes
	{
		public const string ECDH_P521 = "4990f759ff4241cba3563ce832edeb90";
		public const string ECDSA_P521 = "172b33e79f3945afb78d1c08e98fd4ab";
	}


    public class AutoApplyStatusTypes
    {
        public const string SUCCESS = "67dfd9dd671745218c104ca09acf3cb0";
        public const string PARTIAL_SUCCESS = "526c23ac8fb24df6992543f6be688ddb";
        public const string FAILURE = "8e9c409d12a94d9f961cd02692c1979e";
        public const string PENDING = "0f95c99f51654a5caec28ec5dfe3e7b3";
    }

    public class Actions
    {
        public const int SYNCHRONIZE_DEVICE_LOAD_PROFILE_COLLECTION_CONFIGURATION = 1;
        public const int SYNCHRONIZE_GATEWAY_COMPATIBILITY_SETTING = 2;
    }

    public class LoadProfileDataSetCollectionConfigurationTypes
    {
        public const int FULL = 0;
        public const int DELTA = 1;
    }

    public class DeviceLoadProfileDataSets
    {
        public const int DATA_SET_1 = 1;
        public const int DATA_SET_2 = 2;
        public const int DATA_SET_3 = 3;
        public const int DATA_SET_4 = 4;
    }

    public class LoadProfileDataSetCollectionConfigurationStatus
    {
        public const int DISABLE = 0;
        public const int ENABLE = 1;
    }

    public class DeviceMemoryTypes
    {
        public const string LOAD_PROFILE_DATA_SET_1 = "ef199c26711544988be770f4f4947f83";
        public const string LOAD_PROFILE_DATA_SET_2 = "bee619f5376848a5ac6e0b8678a61146";
        public const string LOAD_PROFILE_DATA_SET_3 = "6cb6f1b2d346456ead185a123f696bf5";
        public const string LOAD_PROFILE_DATA_SET_4 = "81a8dd56fe3e4116bbbc6f004d6e870b";
    }

    public class ResultOptions
    {
        public const int LOAD_PROFILE_ON_DEMAND_PRIMARY_DATA_SET = 0;
        public const int FULL_LOAD_PROFILE_PRIMARY_DATA_SET = 1;
        public const int DELTA_LOAD_PROFILE_PRIMARY_DATA_SET = 2;
        public const int CONTINUOUS_DELTA_LOAD_PROFILE_PRIMARY_DATA_SET = 3;
    }

	/// <summary>
	/// Used in GatewayManager.Register() and GatewayManager.Update() to provide options for
	/// validating the certificate from the ECN's SOAP interface.
	/// </summary>
	public class EssCredentialsBypassSslTypes
	{
		public const string ALWAYS_BYPASS = "d64573908e2e42dcb34d1e43c49a4c6e";
		public const string ALLOW = "0c77ab6a3c1e471a82c090ca00a146b5";
		public const string NEVER_BYPASS = "1cc35b79392d4a5f93e6cbe737d81562";
	}

    public class AtmProcessTypes
    {
        public const int METER = 0;
    }

    public class BatchTypes
    {
        public const string DEVICE_RESULTS = "87362b2ab39243c4a9d8283a019aca7f";
        public const string EVENT_HISTORY = "15d61a80c9484a859d4df7786cfebf63";
        public const string GATEWAY_RESULTS = "07d74f9da160493f8374ad50d83b2d25";
		public const string COMMAND_HISTORY_INTERNAL_CIM = "5b6f521fe93c4819bba626ede191b3ee";
    }

    public class PropertyDataTypes
    {
        public const string NUMERIC = "dba4124e5e264414967e0021e3f5d54d";
        public const string STRING = "b508bf89d45b4f06bf38150ab0af1356";
        public const string XML = "c1526595b0424c45b9396e29fd750a15";
    }

    public class MeterEnabledByteValues
    {
        public const int UNKNOWN = -1;
        public const int DISABLE = 0;
        public const int ENABLE = 1;
    }

    public class GatewayProperties
    {
        public const string PROCESS_CONFIGURATION = "2c68a437ba754ca0ae18de54b7c6249c";
        public const string DISCOVERED_DEVICES_LAST_HANDLE_READ = "603bc068b58b4d0dac960d056df82b69";
        public const string ENCRYPTION_CONFIGURATION = "a5f846d7d68c4656b1c5e15df4571671";
        public const string UNANSWERED_CALL_IN_PROGRESS = "909de0733c884a0dba09073834704edb";
        public const string DEVICE_LIMIT = "c522018ca6c34da6a7e4e75d5da3cd3a";
        public const string STATISTICS = "ffb5d500416244b8ab257eea28440c69";
        public const string POWER_LINE_CARRIER_COMMUNICATION_CONFIGURATION = "257b695d46a541439d064aaa6ba6156d";
    }

    public class DeviceProperties
    {
        public const string METER_DISPLAY_CONFIGURATION = "95F6DE654504475fB5873CAD721D04B2";
        public const string PULSE_INPUT_CONFIGURATION = "963a74876f32451db97b7647ab2a2134";
        public const string PENDING_TOU_CALENDAR = "e545fea5718545e0966aafcb7050dd69";
        public const string ACTIVE_TOU_CALENDAR = "85f3edf275794caa8f7caf5727b83e13";
        public const string LAST_KNOWN_CDLP_TRACKING_ID = "819dac36aa3649d098a4c8c82503f493";
        public const string DOWN_LIMIT = "06eaee4eb69d4d0b8a8af69442ace82b";
        public const string COMMISSION_STATUS_TYPE_ID = "7380E5B9836C4b17A1CAF83FA4D23D4F";
        public const string MBUS_DATA_ORDER_TYPE_ID = "1d806e12b1c44639ad1155c9dbfd7b0c";
        public const string MBUS_BILLING_DATA_STRUCTURE_TYPE_ID = "1df973aeb3a649ad99f98049226bcbbf";
        public const string MBUS_HANDLE = "23d69e35a7a244c7ba7ba7b8b502ff03";
        public const string MBUS_MANUFACTURER_ID = "874a7b17827f4a41aa5d61837ea32535";
        public const string MBUS_VERSION = "645df1235aa140988a6cc6b0dc1179e3";
        public const string ALARM_POLLING_RATE = "1bc2a5093a58485aa72830b1ee781505";
        public const string BILLING_SCHEDULE = "6AFD5F8B17824db5ADA034938A27347C";
        public const string TRANSACTION_TABLE_REQUEST_ID = "8cccecf0fd0649f8a991c99e83529cfa";
        public const string MBUS_HANDLE_HISTORY = "34580eb76bec461d8c5e045b386bc810";
        public const string AUTO_DISCOVERY_CONFIGURATION = "857245bd949e41b4bf60afc4ff3e2836";
        public const string UTILITY_INFORMATION = "8fc449ab92274724b4cd6452bbcaadec";
        public const string TRANSACTION_NUMBER = "54cb1b31d4dc43779de19feedde94e2e";
        public const string STOP_MODE_CONFIGURATION = "e0e3eacd838c463db6322a8464234fbe";
        public const string POWER_QUALITY_CONFIGURATION = "85fdfa7cdbc5444ea33f0a6ab819428d";

        //V2.3 Device Properties
        public const string ADD_PREPAY_CREDIT_SEQUENCE_NUMBER = "2FFAD8D4546A48fdBAC618A9D102C339";
        public const string PREPAY_CONFIGURATION = "462FD9011D524d67A5CA654654768F74";
        public const string TIME_ZONE_CONFIGURATION = "b779e06d6e654cc1ac7e1059b55abd3c";

        //V3.0 Device Properties
        public const string DISCONNECT_CONFIGURATION = "0acc90fce4b847fc98461d8e2d628b06";
        public const string CONTINUOUS_EVENT_LOG_CONFIGURATION = "8d1680f11992437a8540f6a9ed19fe75";
        public const string OPTICAL_PORT_CONFIGURATION = "83e79165fe814b30922ba95d5f0b385f";

        [Obsolete("v5.1")]
        public const string POWER_QUALITY_OUTAGE_DURATION_THRESHOLD = "E692D4F7B7D446ddA240F80CAF1E8D4A";
        [Obsolete("v5.1")]
        public const string POWER_QUALITY_TIME_THRESHOLD = "BD395FA876664e26A33EE1A8AA9FE597";
        [Obsolete("v5.1")]
        public const string POWER_QUALITY_SAG_SURGE_DURATION_THRESHOLD = "8CF7494D55E649cf9FECC520B8557A17";
        [Obsolete("v5.1")]
        public const string POWER_QUALITY_SURGE_VOLTAGE_PERCENT_THRESHOLD = "27CB0ECC12724759877609E886BFC4B0";
        [Obsolete("v5.1")]
        public const string POWER_QUALITY_SAG_VOLTAGE_PERCENT_THRESHOLD = "08ED81276FAC495786FBBC7F74D51641";
        [Obsolete("v5.1")]
        public const string POWER_QUALITY_OVERCURRENT_PERCENT_THRESHOLD = "D07BCADBBFFF4b6dAFE29C55152FAD81";
        [Obsolete("v5.1")]
        public const string POWER_QUALITY_SAG_SURGE_DURATION_THRESHOLD_SECONDS = "CE0D44B6C7AE4c75A124795F8B3CCB83";
        [Obsolete("v5.1")]
        public const string PRIMARY_MAXIMUM_POWER_LEVEL_DURATION = "BEB98CFCEF294453AF95D41268853E35";
        [Obsolete("v5.1")]
        public const string MAXIMUM_POWER_LEVEL_DURATION = "EAA32AA9F9BC472fB4A4235F6D72A8A7";
		[Obsolete("5.2.000")]
		public const string PRIMARY_POWER_CONFIGURATION = "25EBD8AA631C4ba69D15C347933E400A";
    }

    public class CommissionStatusType
    {
        public const string COMMISSION_NOT_COMPLETE = "EAA43532233745f484594602EC8AF482";
        public const string COMMISSION_COMPLETE = "37B67FFCDB0744ea924F2DA64451DAC6";
    }

    public class CommandTypes
    {
        public const string DEVICE = "c5f8f6e49de24d039de5a6960485f694";
        public const string GATEWAY = "0c68d260aa61407f86eb67dd32806342";
        public const string ENGINE = "e6c2ce4b215344959f23fd27e5be338e";
        public const string INTERNAL = "97f2157ab7014fbcbd0e1b770d5abd1e";
    }

    // Contains the list of events used in the solution
    public class EventIDs
    {
        public const string HIERARCHY_MEMBER_DELETED = "3B7564C75BB74d538FB695065B03748B";
        public const string ATTRIBUTE_DELETED = "A19ECF846E4D4280B5100BC095C95A9F";
        public const string ATTRIBUTE_MEMBER_DELETED = "617874DABEC9496d9D4077F59004EB8E";
        public const string HIERARCHY_LEVEL_DELETED = "71FF3AACA4A04a828373745F3CD04948";
        public const string HIERARCHY_DELETED = "5EC22DD435FC41fe98DB9DB9BA9A381B";
        public const string DATA_POINT_DELETED = "E566FFD3026F416bB3CA07D1C866913A";
        public const string DATA_POINT_CREATED = "EF39FB82FB574246BE3E68482635606A";
        public const string DATA_POINT_UPDATED = "46CF86CFB48B4a98BD6238BA5999DF5C";
        public const string DEVICE_DELETED_KEEP_DATA_POINTS = "638C9315494F4543AE235EBE00F73081";
        public const string DEVICE_DELETED_DELETE_DATA_POINTS = "C7027B2B70D241dd836E52504687C9B7";
        public const string FUNCTION_CALL_DELETED = "E0E0CFA8FCD94e168694F56CF275C176";
        public const string SOAP_CALL_DELETED = "324CE8FC4ECB475fA0A5B2DB9299951D";
        public const string TASK_TIMEOUT = "F25CD601CB114f51A9D7A7A54B36377F";
        public const string SCHEDULE_DELETED = "0693a7443d884d2480c24f6997835956";
    }

    public class InternalServiceReturnCodes
    {
        public const string DUPLICATE_PROCESS_CONFIGURATION_ID = "d664805ec24249cd94343e62cd90b398";
        public const string INVALID_LOG_EVENTS = "5978a2f2867c46eebcc749438ad9e743";
        public const string INVALID_URGENT_HOLD = "8768757a712c42e4a5850ad6f257ca8a";
        public const string INVALID_TRACE = "b0f7f3a17e0d4c35a50d5cddbb354bc6";
        public const string INVALID_FLAGS = "e01563c3b12f498e8897a7e0c5419a16";
        public const string INVALID_LIMIT = "3df0cac22f564476b038d197845abb7e";
        public const string INVALID_CONTROL = "f945e7f918364b72b20ca178ca88dd1b";

        public const string INVALID_NEM_GATEWAY_VERSION_ID = "43252804ee8146e9b1a8e2247936c6f3";
    }

    public class LoadProfileUploadTypes
    {
        public const string UPLOADING = "a05159404aa74fa9a62c816c75c0c148";
        public const string NOT_UPLOADING = "134e78c61a0d4d809c9018381830beb9";
    }

    public class MeterDomainIDStatusTypes
    {
        public const string PENDING = "74d756c7eba54c5395ccc9249c975e37";
        public const string LAST_KNOWN_GOOD = "104ec1d3885146fa8d719266a03cc080";
    }

    public class MeterSourceTypes
    {
        public const string DEMAND = "464714C8CA4F4649B4C0191D0ABF6639";
        public const string COINCIDENT = "E376C71AF8A74971AF49A3ACF4EB349F";
        public const string LOAD_PROFILE = "19309bde9689472185a5835d90b8e293";
        public const string MAPPED_SOURCE = "4306aa832ae24c719899006d1952a7f6";
    }

    public class PendingChangeEntityTypes
    {
        public const string GATEWAY_WAN_CONFIGURATION = "CC1668A6DAB94af080111553103B6B9E";
    }

    public class PendingChangeTypes
    {
        public const string SERVER_TO_GATEWAY_IP_ADDRESS = "97cec7e09bf64d96b0dab27a6d679dd0";
        public const string PPP_LOGIN = "D94E0622FE2A4d79B566437A81461825";
        public const string GATEWAY_WAN_CONFIGURATION_NAME = "A786E657CA2644a5AFD1CD46BD5B1FBC";
        public const string GATEWAY_WAN_CONFIGURATION_STATUS = "A34BC51E54E74a0dBB9464C131678042";
    }

    public class InternalEventTypes
    {
        public const string ALL = "4d0af91e811e4f6cbbddc0784abb0cb8";
    }

	public class ClearPrepayCreditsBeforeAddOptionTypes
	{
		public const string PREPAY_ONLY = "3994637182eb45eb823a850625c38a16";
		public const string EMERGENCY_ONLY = "35064344511d48c4a2db2fe95593f36e";
		public const string PREPAY_AND_EMERGENCY = "3a7ec74053c844d4a6b24dd471831498";
	}

	public class DisconnectControlTypes
	{
		public const int MAX_POWER = 0;
		public const int CURRENT = 1;
	}

	public class DisconnectTripValueSelects
	{
		public const int FORWARD_PLUS_REVERSE_POWER = 0;
		public const int FORWARD_POWER = 1;
		public const int FORWARD_MINUS_REVERSE = 2;
	}

	public class CurrentControlTypes
	{
		public const int SINGLE_PHASE = 0;
		public const int ALL_ACTIVE_PHASES = 1;
	}

	public class MeterDisplayListTypes
	{
		public const string PRIMARY = "d603e3991caa4ccf82a7b2d36e09db62";
		public const string SECONDARY = "9f18b97585d941169ac4b5eaa20aaf49";
	}

    public enum NegativePrepayCreditStatus : uint
    {
        DISABLED = 0,
        ENABLED = 1,
        GRACE_PERIOD = 2
    }

	public enum ValidateHelloMessageSignature : int
	{
		None = 0,
		DcOnly = 1,
		All = 2,
	}

	public enum TamperPowerQualityEventQualificationID : int
	{
		PHASE_LOSS = 0,
		CURREONT_ON_VOLTAGE = 1,
		SUBSEQUENT_POWER_OUTAGE = 2,
		PRECEDING_OR_COINCIDENT_POWER_OUTAGE = 3
	}

	public class AveragePowerConfigurationTypes
	{
		public const string PENDING = "4c1e9970ecd64667af55fc458ed77252";
		public const string ACTIVE = "27f46fa7e8cb4313984a7a228150fff5";
	}

	public enum CommissionStatus : byte
	{
		PENDING = 0,
		FAILED = 1,
		SUCCESS = 2
	}

	#region Deprecated Classes
	////////////////////////////////////////////////////////////////////////////////////////
	//The following classes are deprecated												  //
	////////////////////////////////////////////////////////////////////////////////////////

	//The RetrieveByParameterIDTypes class is deprecated starting with v2.1 -
	//use the IDTypes class in its place
	//List of ID types used by RetrieveByParameter API methods
	[Obsolete("v2.1 - use the IDTypes class")]
	public class RetrieveByParameterIDTypes
	{
		public const string GATEWAY_ID = "444b729f4d7a4b5e90f5296e7c62ed78";
		public const string TRANSFORMER_ID = "0aadee3706ce479a9fbb769b91ee7cf9";
		public const string NEURON_ID = "85a93675602b49a5a3ecbc7b66a513db";
		public const string DEVICE_ID = "7bf34c6d91f64ff4904411b72874ddf9";
		public const string SERIAL_NUMBER = "8448b06de2f34e838914d50021053ee8";
	}

	//Starting with v2.1, the following class has been deprecated and replaced by DeviceEvents,
	//GatewayEvents and SystemEvents classes. All new event definitions will be added to
	//one of those three classes.
	//The constant that replaces each is listed to the right
	//		DED = DeviceEventDefinitionID
	//		GED = GatewayEventDefinitionID
	//		SED = SystemEventDefinitionID
	[Obsolete("v2.1 - use DeviceEvents, GatewayEvents, or SystemEvents classes")]
	public class EventDefinitionID
	{
        public const string COMMAND_FAILURE = "7bd38a557c4641d7bebe59edbb328064"; //SED.COMMAND_FAILURE
        public const string END_OF_BILLING_CYCLE_BILLING_DATA_AVAILABLE = "3b29e905f9764169a999cfc8ad527d2e"; //SED.END_OF_BILLING_CYCLE_BILLING_DATA_AVAILABLE
        public const string ON_DEMAND_BILLING_DATA_READ_COMMAND_COMPLETE = "f309efb1263c4706ac8c1e3a18ec9da9"; //DED.READ_BILLING_DATA_ON_DEMAND_COMMAND_COMPLETE
        public const string DC1000_EVENT = "748af8ef281c4428b0e3f8cc2d8fe0cf"; //SED.DC1000_EVENT
        public const string REQUEST_METER_LOAD_PROFILE_COMMAND_COMPLETE = "f33490cf1e484c4d817d27b125b1031f"; //DED.READ_FULL_LOAD_PROFILE_COMMAND_COMPLETE
        public const string POWER_QUALITY_COMMAND_COMPLETE = "10becfacb599459cacc920cb4fc6bce5"; //DED.POWER_QUALITY_COMMAND_COMPLETE
        public const string CONNECTION_ESTABLISHED = "16e7a62c458c48189d4ce6249264512d"; //SED.CONNECTION_ESTABLISHED
        public const string CONNECTION_RELEASED = "cb9678d369b547d8bf4ee51e6df81455"; //SED.CONNECTION_RELEASED
        public const string ADD_METER_SUCCESS = "bffeafeef0964980926e5e0fc81cb55a"; //DED.ADD_SUCCESS
        public const string ADD_METER_FAILURE = "e895295532644b899216a2d647da6eb8"; //DED.ADD_FAILURE
        public const string REMOVE_METER_SUCCESS = "fb1eb87c9def4190a121aaa2995627f5"; //SED.REMOVE_SUCCESS
        public const string REMOVE_METER_FAILURE = "5da8b450b7dd4cb2a982539934a4a2c6"; //DED.REMOVE_FAILURE
        public const string UPDATE_GATEWAY_LOGIN_COMMAND_COMPLETE = "BCE91DAD62804a4e8824E7C3433A269E"; //GED.SET_PPP_USERNAME_COMMAND_COMPLETE
        public const string UPDATE_GATEWAY_PASSWORD_COMMAND_COMPLETE = "5B4D3E50144D41f68EA1EAFC4EC32F21"; //GED.SET_PPP_PASSWORD_COMMAND_COMPLETE
        public const string REMOTE_METER_CONNECT_COMMAND_COMPLETE = "4bea21adc4474f5fa7f9e412b74d0d11"; //DED.CONNECT_LOAD_COMMAND_COMPLETE
        public const string REMOTE_METER_DISCONNECT_COMMAND_COMPLETE = "416517694b9a482f87c5b81be3fee8fa"; //DED.DISCONNECT_LOAD_COMMAND_COMPLETE
        public const string SET_MAXIMUM_POWER_LEVEL_COMMAND_COMPLETE = "93af3eebb36742a89d6a12cd9e7274b8"; //DED.SET_PRIMARY_MAXIMUM_POWER_LEVEL_COMMAND_COMPLETE
		public const string SET_MAXIMUM_POWER_LEVEL_DURATION_COMMAND_COMPLETE = "95e559c12e914ce6be7d199ad9f63c69";//DED.SET_MAXIMUM_POWER_LEVEL_DURATION_COMMAND_COMPLETE
        public const string SET_SEASON_SCHEDULES_COMMAND_COMPLETE = "8F11B342841D404a91883626FF3D2704"; //DED.SET_TOU_SEASON_SCHEDULES_COMMAND_COMPLETE
        public const string SET_TOU_RECURRING_DATES_COMMAND_COMPLETE = "F039C7FDC7204c31A576C9AFC24BD951"; //DED.SET_TOU_RECURRING_DATES_COMMAND_COMPLETE
        public const string SET_DAY_SCHEDULES_COMMAND_COMPLETE = "B5FDEFDF88E348f0824E329778BA78EF"; //DED.SET_TOU_DAY_SCHEDULES_COMMAND_COMPLETE
        public const string RETRIEVE_DAY_SCHEDULES_COMMAND_COMPLETE = "270827C2F082486f81916DE63876D4AE"; //DED.READ_TOU_DAY_SCHEDULES_COMMAND_COMPLETE
        public const string RETRIEVE_SEASON_SCHEDULES_COMMAND_COMPLETE = "9A9A5B023D5C41da9AF0506B271E9CF7"; //DED.READ_TOU_SEASON_SCHEDULES_COMMAND_COMPLETE
        public const string RETRIEVE_TOU_RECURRING_DATES_COMMAND_COMPLETE = "1AD9795CD5524895AD9B013570CA2ED9"; //DED.READ_TOU_RECURRING_DATES_COMMAND_COMPLETE
        public const string ENGINE_STARTED = "a89ab0bab7344d26bc4236a0d2c309e1"; //SED.ENGINE_STARTED
        public const string ENGINE_STOPPED = "11d73ac3f2bb4093b447ba68f5266d8b"; //SED.ENGINE_STOPPED
		public const string READ_MAXIMUM_POWER_LEVEL_COMMAND_COMPLETE = "164fb5c6de3546eba985de5d71e984a9"; //DED.READ_PRIMARY_MAXIMUM_POWER_LEVEL_COMMAND_COMPLETE
		public const string READ_MAXIMUM_POWER_LEVEL_DURATION_COMMAND_COMPLETE = "439b97c9dfdc499289128038ec83985e"; //DED.READ_MAXIMUM_POWER_LEVEL_DURATION_COMMAND_COMPLETE
        public const string DC_FIRMWARE_UPDATE_COMMAND_COMPLETE = "B2F41EB2832744a3AB03B2C1DFD48082"; //GED.UPDATE_FIRMWARE_COMMAND_COMPLETE
        public const string ENABLE_MAXIMUM_POWER_COMMAND_COMPLETE = "2bbb57575b8d49e980f1837e9682cc75"; //DED.SET_PRIMARY_MAXIMUM_POWER_STATUS_COMMAND_COMPLETE
        public const string PERFORM_SELF_BILLING_READ_COMMAND_COMPLETE = "0a87ae5fbef54616a74faa3a653250ba"; //DED.READ_SELF_BILLING_DATA_COMMAND_COMPLETE
        public const string READ_SECONDARY_BILLING_REGISTERS_COMMAND_COMPLETE = "63b3fe0b77e841259d412763b349e2ff"; //DED.READ_SECONDARY_BILLING_REGISTERS_COMMAND_COMPLETE
		public const string OPEN_SECONDARY_CONTROL_OUTPUT_COMMAND_COMPLETE = "a1a15cdcb98546fb96c6564a288b930e"; //DED.DISCONNECT_CONTROL_RELAY_COMMAND_COMPLETE
		public const string CLOSE_SECONDARY_CONTROL_OUTPUT_COMMAND_COMPLETE = "d5e307213a2f4452870ffd12ab1bf937"; //DED.CONNECT_CONTROL_RELAY_COMMAND_COMPLETE
		public const string ENABLE_SECONDARY_CONTROL_OUTPUT_TIERS_COMMAND_COMPLETE = "9469ea5f72fe451c97a7f58690d57ebe"; //DED.SET_CONTROL_RELAY_TIERS_STATUS_COMMAND_COMPLETE
		public const string SET_SECONDARY_CONTROL_OUTPUT_TIERS_COMMAND_COMPLETE = "ca3a8bb6fbc14db9971f2824b0cbbbf2"; //DED.SET_CONTROL_RELAY_TIERS_COMMAND_COMPLETE
		public const string SET_LOAD_PROFILE_CONFIGURATION_COMMAND_COMPLETE = "18e64ad80a2e4f3691b1065b40c6d3e4"; //DED.SET_LOAD_PROFILE_CONFIGURATION_COMMAND_COMPLETE
        public const string READ_GATEWAY_TO_SERVER_MODEM_INIT_STRING_COMMAND_COMPLETE = "acf0ea51d03d4f2f9220ceffc82ced3f"; //GED.READ_GATEWAY_TO_SERVER_MODEM_INIT_STRING_COMMAND_COMPLETE
        public const string READ_GATEWAY_TO_SERVER_MODEM_TYPE_COMMAND_COMPLETE = "469eef112d9c43f79d0debbceb4240ac"; //GED.READ_GATEWAY_TO_SERVER_MODEM_TYPE_COMMAND_COMPLETE
        public const string READ_GATEWAY_TO_SERVER_PHONE_NUMBER_1_COMMAND_COMPLETE = "c63e377e9c0b4459b03f0afd60892004"; //GED.READ_GATEWAY_TO_SERVER_PHONE_NUMBER_1_COMMAND_COMPLETE
        public const string READ_GATEWAY_TO_SERVER_PHONE_NUMBER_2_COMMAND_COMPLETE = "1640aabde50f4121bf8939bb3f0b7640"; //GED.READ_GATEWAY_TO_SERVER_PHONE_NUMBER_2_COMMAND_COMPLETE
        public const string READ_GATEWAY_TO_SERVER_IP_ADDRESS_COMMAND_COMPLETE = "2dd2f06283424a749fa4eeead0d54771"; //GED.READ_GATEWAY_TO_SERVER_IP_ADDRESS_COMMAND_COMPLETE
        public const string SET_GATEWAY_TO_SERVER_IP_ADDRESS_COMMAND_COMPLETE = "bcc89beebfc54943abae9ee1fd51b696"; //GED.SET_GATEWAY_TO_SERVER_IP_ADDRESS_COMMAND_COMPLETE
        public const string SET_SERVER_TO_GATEWAY_IP_ADDRESS_COMMAND_COMPLETE = "c452c6f1b95840b88a9a8f6ae1b72e52"; //GED.SET_IP_ADDRESS_COMMAND_COMPLETE
        public const string SET_GATEWAY_TO_SERVER_PHONE_NUMBER_1_COMMAND_COMPLETE = "0a6d540769554399aaa1022df954e6ee"; //GED.SET_GATEWAY_TO_SERVER_PHONE_NUMBER_1_COMMAND_COMPLETE
        public const string SET_GATEWAY_TO_SERVER_PHONE_NUMBER_2_COMMAND_COMPLETE = "0e71c1e80add4e049e239d49d23654cd"; //GED.SET_GATEWAY_TO_SERVER_PHONE_NUMBER_2_COMMAND_COMPLETE
        public const string SET_GATEWAY_TO_SERVER_MODEM_TYPE_COMMAND_COMPLETE = "518a7ae6cf3f487690fbecb284426909"; //GED.SET_GATEWAY_TO_SERVER_MODEM_TYPE_COMMAND_COMPLETE
        public const string SET_GATEWAY_TO_SERVER_MODEM_INIT_STRING_COMMAND_COMPLETE = "8ed0a80aa9774f578479c0e6b5d2da89"; //GED.SET_GATEWAY_TO_SERVER_MODEM_INIT_STRING_COMMAND_COMPLETE
		public const string READ_POWER_QUALITY_TIME_THRESHOLD_COMMAND_COMPLETE = "DF520B4557734f9e93120044480063A2"; //DED.READ_POWER_QUALITY_OUTAGE_DURATION_THRESHOLD_COMMAND_COMPLETE
		public const string READ_POWER_QUALITY_SAG_THRESHOLD_COMMAND_COMPLETE = "CD02921B850748a18A463D24685FA3D6"; //DED.READ_POWER_QUALITY_SAG_VOLTAGE_PERCENT_THRESHOLD_COMMAND_COMPLETE
		public const string READ_POWER_QUALITY_SURGE_THRESHOLD_COMMAND_COMPLETE = "AF1653E211064c4c821DC5F547D6337F"; //DED.READ_POWER_QUALITY_SURGE_VOLTAGE_PERCENT_THRESHOLD_COMMAND_COMPLETE
		public const string READ_POWER_QUALITY_OVERCURRENT_THRESHOLD_COMMAND_COMPLETE = "50A432F5323B4c1cA0B0F064791BA45C"; //DED.READ_POWER_QUALITY_OVERCURRENT_PERCENT_THRESHOLD_COMMAND_COMPLETE
		public const string SET_POWER_QUALITY_TIME_THRESHOLD_COMMAND_COMPLETE = "A2CE552EAD2C4a3cBA84B54CCAAD5AC1"; //DED.SET_POWER_QUALITY_OUTAGE_DURATION_THRESHOLD_COMMAND_COMPLETE
		public const string SET_POWER_QUALITY_SAG_THRESHOLD_COMMAND_COMPLETE = "9AD8E6F84A1A40ccB5344A0D56B46776"; //DED.SET_POWER_QUALITY_SAG_VOLTAGE_PERCENT_THRESHOLD_COMMAND_COMPLETE
		public const string SET_POWER_QUALITY_SURGE_THRESHOLD_COMMAND_COMPLETE = "F3DFBF4C5BE340a0BE795431B0EC375A"; //DED.SET_POWER_QUALITY_SURGE_VOLTAGE_PERCENT_THRESHOLD_COMMAND_COMPLETE
		public const string SET_POWER_QUALITY_OVERCURRENT_THRESHOLD_COMMAND_COMPLETE = "EF4DAD2B3E264e048D95718055531DA1"; //DED.SET_POWER_QUALITY_OVERCURRENT_PERCENT_THRESHOLD_COMMAND_COMPLETE
		public const string RETRIEVE_GATEWAY_PERFORMANCE_STATISTICS_COMMAND_COMPLETE = "cd091b7129aa410e987d00ee4a5e9f94"; //GED.READ_STATISTICS_COMMAND_COMPLETE
        public const string READ_DELTA_LOAD_PROFILE_COMMAND_COMPLETE = "8C55269E57A44edf88609794260AD857"; //DED.READ_DELTA_LOAD_PROFILE_COMMAND_COMPLETE
		public const string READ_CONTINUOUS_DELTA_LOAD_PROFILE_COMMAND_COMPLETE = "AAC59D59669C4e7f9AC4E63782BD9F98"; //DED.READ_CONTINUOUS_DELTA_LOAD_PROFILE_COMMAND_COMPLETE
		public const string CONTINUOUS_DELTA_LOAD_PROFILE_DATA_AVAILABLE = "E73928E49A194259A6C9BA5C987CF3B7"; //DED.CONTINUOUS_DELTA_LOAD_PROFILE_DATA_AVAILABLE
		public const string SECURITY_ALERT = "3745625CAFA24ad989472607AB0A28E0"; //SED.SECURITY_ALERT
		public const string UPDATE_METER_FIRMWARE_COMMAND_COMPLETE = "4da60cb36956422086892e0baa077ac3"; //DED.UPDATE_FIRMWARE_COMMAND_COMPLETE
		public const string ENABLE_TOTAL_ENERGY_COMMAND_COMPLETE = "7da4d95936df4d5b8951828c562ce7b9"; //GED.SET_TOTAL_ENERGY_STATUS_COMMAND_COMPLETE
		public const string TOTAL_ENERGY_DATA_AVAILABLE = "a0e09a0454e04a89bceb469ef18b2348"; //SED.TOTAL_ENERGY_DATA_AVAILABLE
		public const string SERVER_ERROR_EVENT = "e3501923ef634306b0c5592ff53b2f00"; //SED.SERVER_ERROR
		public const string SET_SERVER_TO_GATEWAY_PHONE_NUMBER_COMMAND_COMPLETE = "338D71EE6D224a10AEA6142A9A3131E0"; //GED.SET_WAN_PHONE_NUMBER_COMMAND_COMPLETE
		public const string SET_SECURITY_OPTIONS_COMMAND_COMPLETE = "35a1980a0f574936b10d53df9bcf0a17"; //GED.SET_SECURITY_OPTIONS_COMMAND_COMPLETE
		public const string GATEWAY_IP_ADDRESS_CHANGED = "72dcafb9a1df45ae8beec1d1cfa7285f"; //SED.GATEWAY_IP_ADDRESS_CHANGED

		//V2
		public const string READ_PULSE_INPUT_CONFIGURATION_COMMAND_COMPLETE = "6dc87cd602ff46fa920a072ee584e5e4"; //DED.READ_PULSE_INPUT_CONFIGURATION_COMMAND_COMPLETE
		public const string READ_INSTANTANEOUS_POWER_COMMAND_COMPLETE = "A5DB38E87B554ebe82058B4EB38CB58C"; //DED.READ_INSTANTANEOUS_POWER_COMMAND_COMPLETE
		public const string READ_SECONDARY_CONTROL_OUTPUT_RELAY_COMMAND_COMPLETE = "8895461b1adf4aa6ad538c22aec447ff"; //DED.READ_CONTROL_RELAY_COMMAND_COMPLETE
		public const string READ_PRIMARY_CONTROL_OUTPUT_COMMAND_COMPLETE = "9cd46282124b434499f7e02bf6d5a8d8"; //DED.READ_LOAD_STATUS_COMMAND_COMPLETE
		public const string BROADCAST_OPEN_SECONDARY_CONTROL_OUTPUT_COMMAND_COMPLETE = "232cb0c5b7b74916b84e559d4f4c0f1f"; //GED.BROADCAST_DISCONNECT_CONTROL_RELAY_COMMAND_COMPLETE
		public const string BROADCAST_CLOSE_SECONDARY_CONTROL_OUTPUT_COMMAND_COMPLETE = "7a056e9329c2425aba609006727f34f9"; //GED.BROADCAST_CONNECT_CONTROL_RELAY_COMMAND_COMPLETE
		public const string BROADCAST_MAXIMUM_POWER_LEVEL_ENABLE_COMMAND_COMPLETE = "73fe67b9e6a74b58a3c2e7ee28aece07"; //GED.BROADCAST_SET_PRIMARY_MAXIMUM_POWER_STATUS_COMMAND_COMPLETE
		public const string READ_GATEWAY_FIRMWARE_VERSION_COMMAND_COMPLETE = "022d4e1cbdaa4890b509030354d5f67b"; //GED.READ_FIRMWARE_VERSION_COMMAND_COMPLET
		public const string READ_METER_FIRMWARE_VERSION_COMMAND_COMPLETE = "194325c249b94c28a2125797d5acdf2f"; //DED.READ_FIRMWARE_VERSION_COMMAND_COMPLETE
		public const string SET_PULSE_INPUT_CONFIGURATION_COMMAND_COMPLETE = "1e2a6435c4d0467ea01b1e7868708b92"; //DED.SET_PULSE_INPUT_CONFIGURATION_COMMAND_COMPLETE
		public const string READ_ACTIVE_TOU_CALENDAR_COMMAND_COMPLETE = "AC8EB7183720417fA5083824F5336C39"; //DED.READ_ACTIVE_TOU_CALENDAR_COMMAND_COMPLETE
		public const string READ_PENDING_TOU_CALENDAR_COMMAND_COMPLETE = "71887A7C756C42bdA83ACAB49FC099D8"; //DED.READ_PENDING_TOU_CALENDAR_COMMAND_COMPLETE
		public const string SET_PENDING_TOU_CALENDAR_COMMAND_COMPLETE = "07660D8DEB4F427c9B4FF53E4E83DD41"; //DED.SET_PENDING_TOU_CALENDAR_COMMAND_COMPLETE
		public const string READ_METER_DISPLAY_CONFIGURATION_COMMAND_COMPLETE = "737FFF081E4D47199D1350B354B43573"; //DED.READ_DISPLAY_CONFIGURATION_COMMAND_COMPLETE
		public const string SET_METER_DISPLAY_CONFIGURATION_COMMAND_COMPLETE = "AF23E940CC134d45A0C6501CA4D44B0C"; //DED.SET_DISPLAY_CONFIGURATION_COMMAND_COMPLETE
		public const string SET_ALARM_DISPLAY_CONFIGURATION_COMMAND_COMPLETE = "6D0A1C1D0C2E45e5BF8C87A383090EE4"; //DED.SET_ALARM_DISPLAY_CONFIGURATION_COMMAND_COMPLETE
		public const string READ_MAXIMUM_POWER_ENABLE_COMMAND_COMPLETE = "BBE31E1ECE9A44cc91BC66BA9AD89DEA"; //DED.READ_PRIMARY_MAXIMUM_POWER_STATUS_COMMAND_COMPLETE
		public const string GENERAL_FAILURE_DATA_STILL_AVAILABLE = "ba19e6ab3fec45abac093bb7035ebbc1"; //GED.GENERAL_FAILURE_DATA_STILL_AVAILABLE

		//The following is deprecated:
		public const string UPDATE_GATEWAY_PHONENUMBER_COMMAND_COMPLETE = "338D71EE6D224a10AEA6142A9A3131E0";  // Use GED.SET_WAN_PHONE_NUMBER_COMMAND_COMPLETE
	}

	//Starting with v2.1, the following class has been deprecated and replaced by DeviceResultTypes and
	//GatewayResultTypes classes. All new results will be added to one of those two classes.
	//The constant that replaces each is listed to the right
	//		DRT = DeviceResultTypes
	//		GRT = GatewayResultTypes
	[Obsolete("v2.1 - use DeviceResultTypes or GatewayResultTypes classes")]
	public class ResultTypes
	{
		public const string END_OF_BILLING_CYCLE_BILLING_DATA = "d357ee7048cd4983a3ee896f0e49c562"; //DRT.END_OF_BILLING_CYCLE_BILLING_DATA
		public const string LOAD_PROFILE = "60381e83d07945649a86c193cdc40d6b";	//DRT.FULL_LOAD_PROFILE
		public const string ON_DEMAND_BILLING_DATA = "67a59c9ce2d64e98a8fe0bf34a0d8267";	//DRT.BILLING_DATA_ON_DEMAND
		public const string POWER_QUALITY_INFORMATION = "56f2cb69a8a44247be7a537b754f72af";	//DRT.POWER_QUALITY
		public const string SEASON_SCHEDULES = "C2F525891E70439d847AF31F77C09385";	//DRT.TOU_SEASON_SCHEDULES
		public const string DAY_SCHEDULES = "A3DD474A61324fafB0BE2695DB7FC2A4";	//DRT.TOU_DAY_SCHEDULES
		public const string TOU_RECURRING_DATES = "37D73FDB453241ffA26B5D4BDFF0A33E";	//DRT.TOU_RECURRING_DATES
		public const string SELF_BILLING_READ_DATA = "ad85befc6cc448bf9863c5cb62007561";	//DRT.SELF_BILLING_DATA
		public const string SECONDARY_BILLING_REGISTERS_DATA = "7cf449f68be04523b3fccef2a7781e4e";	//DRT.SECONDARY_BILLING_REGISTERS
		public const string GATEWAY_PERFORMANCE_STATISTICS_DATA = "3a19bf68e06e4879a0cccc0754dae661";	//GRT.STATISTICS
		public const string DELTA_LOAD_PROFILE = "1707758FC0134fc187B105D50BF33723";	//DRT.DELTA_LOAD_PROFILE
		public const string TOTAL_ENERGY_DATA = "4bdd989403aa4c1691fce3c104bf09c3";	//GRT.TOTAL_ENERGY_DATA
		public const string METER_DISPLAY_CONFIGURATION = "5FB2472783C745dc8B110BD8E116A99E";	//DRT.DISPLAY_CONFIGURATION
		public const string MAXIMUM_POWER_ENABLE = "C1340AE2C9B74d088D5B206CB8C8A5C2";	//DRT.PRIMARY_MAXIMUM_POWER_STATUS
		public const string PULSE_INPUT_CONFIGURATION = "5ddd67c34b0a419e8b7204d16c308e92";	//DRT.PULSE_INPUT_CONFIGURATION
		public const string INSTANTANEOUS_POWER = "6072CB933A0840cbA8FEE91456A5F1CB";	//DRT.INSTANTANEOUS_POWER
		public const string SECONDARY_CONTROL_OUPUT_RELAY_STATUS = "3b295e082b3d43d2bd2d3908ae7fe164";	//DRT.CONTROL_RELAY
		public const string PRIMARY_CONTROL_OUTPUT_STATUS = "65621819be064241bee46d5a38a67f15";	//DRT.LOAD_STATUS
		public const string GATEWAY_FIRMWARE_VERSION = "5cc432d671ba465ebb6bd990b6901605";	//GRT.FIRMWARE_VERSION
		public const string METER_FIRMWARE_VERSION = "5bc2f8cfdbc846148e517a2d9514248e";	//DRT.FIRMWARE_VERSION
		public const string ACTIVE_TOU_CALENDAR = "b98a6c0ff148489fb4443f654f848bf6";	//DRT.ACTIVE_TOU_CALENDAR
		public const string PENDING_TOU_CALENDAR = "d57d9409bdbb425b8ab239c04120da78";	//DRT.PENDING_TOU_CALENDAR
	}

	// The following class of constants is deprecated and replaced by
	// the class GatewayToDeviceCommunicationStatusTypes
	[Obsolete("use the GatewayToDeviceCommunicationStatusTypes class")]
	public class DC1000DeviceStates
	{
		public const string UP = "c4a83cbbb8ac4794ab59562bc4b88a32";
		public const string DOWN = "587080ce53fb4a95ba6dc51838d410c9";
		public const string CONFIRMED_DOWN = "af5fdf7f4c644936b131e3ac3d9d6926";
		public const string NO_AGENT = "3680be224f2745e2a132c651d22666dd";
		public const string NASCENT = "198044101bde44c483325d3a5803e407";
	}

	//The SecondaryControlOutputRelayStatus class is deprecated and replaced by ControlRelayStatus class
	[Obsolete("use ControlRelayStatus class")]
	public class SecondaryControlOutputRelayStatus
	{
        public const string BROADCAST_OPEN = "d7ff0b7cdb6d491c90afd700cda4971b"; // replaced by ControlRelayStatus.BROADCAST_OPEN
        public const string BROADCAST_CLOSED = "15080352c0264373a97501ba9e40517f"; // replaced by ControlRelayStatus.BROADCAST_CLOSED
        public const string OPEN = "6eebd8aceb784b1f826a6cdfaf4f69ae"; // replaced by ControlRelayStatus.OPEN
        public const string CLOSED = "f8e560fe460142f996e8374a890161b4"; // replaced by ControlRelayStatus.CLOSED
	}

	//The following class of constants is deprecated and replaced by MeterLonTalkKeyStatus
	[Obsolete("use the MeterLonTalkKeyStatus class")]
	public class MeterAuthenticationKeyStatus
	{
		public const string PENDING = "dae97ad3037d479c84213122e322c187";
		public const string LAST_KNOWN_GOOD = "4fd8350bf13844549eae72b0540f66cb";
	}

	//Starting with v2.3, the following class has been deprecated and replaced by DeviceTaskTypes,
	//GatewayTaskTypes and InternalTaskTypes classes. All new event definitions will be added to
	//one of those three classes.
	//The constant that replaces each is listed to the right
	[Obsolete("v2.3 - use the DeviceTaskTypes, GatewayTaskTypes, or InternalTaskTypes class")]
    public class TaskProcessorTypeTaskTypes
	{
		public const string SCHEDULES = "548D93B15FAE46f1BBE0D8DB3E677D98";	//InternalTaskTypes.SCHEDULE
		public const string RECALC = "E441F39AF80D45c994A652BCEFD2D5A7";	//InternalTaskTypes.RECALCULATION
		public const string DATAPOINT = "E86ACFED1B934f8bB884B2367204DB65";	//InternalTaskTypes.DATAPOINT
		public const string AGGREGATION = "35659D71EC254cabB7F60A8C07811F60";	//InternalTaskTypes.AGGREGATION
		public const string ILON100ADAPTER = "B4D8506E56374612A82DBFAB71842F2C";	//InternalTaskTypes.ILON100ADAPTER
		public const string SCHEDULE_DELETE = "88a5a66d40484482956dca475671bd2c";	//InternalTaskTypes.SCHEDULE_DELETED
		public const string SCHEDULE_DISABLE = "73de098b579b48edb0e0b8fe5f4e9b15";	//InternalTaskTypes.SCHEDULE_DISABLED
		public const string SCHEDULE_ENABLE = "313bf499ce134fda9c73404be963c05f";	//InternalTaskTypes.SCHEDULE_ENABLED
		public const string SCHEDULE_UPDATE = "361c871367834e279248f6bc613498c3";	//InternalTaskTypes.SCHEDULE_UPDATE
		public const string SCHEDULE_PROCESS_HEARTBEAT_FAILURE = "637d46a7059a4abb8b18112465ce0c61";	//InternalTaskTypes.SCHEDULE_PROCESS_HEARTBEAT_FAILURE
		public const string SCHEDULE_PROCESS_SHUTDOWN = "1680D1D9D30F472cB1C47CE49F3EB28A";	//InternalTaskTypes.SCHEDULE_PROCESS_SHUTDOWN
		public const string SCHEDULE_ASSIGNMENT_CREATED = "ea5e58d936e34325baf93f600c7766f9";	//InternalTaskTypes.SCHEDULE_ASSIGNMENT_CREATED
		public const string SCHEDULE_ASSIGNMENT_REMOVED = "7f76c1dc81e145ea9feb4c8d8b95cf8a";	//InternalTaskTypes.SCHEDULE_ASSIGNMENT_REMOVED
		public const string SCHEDULE_OBJECT_DELETED = "8B50020541D84358A09CEAF24EE2DF53";	//InternalTaskTypes.SCHEDULE_OBJECT_DELETED
		public const string SCHEDULE_OBJECT_ENABLED = "7cafcfdd77734b1f8295fb482912a5c6";	//InternalTaskTypes.SCHEDULE_OBJECT_ENABLED
		public const string SCHEDULE_OBJECT_DISABLED = "ddd6ce3366d844d0acd03e1447669d29";	//InternalTaskTypes.SCHEDULE_OBJECT_DISABLED
		public const string RECALCULATION_STARTED = "df6ee83b167f42b5bb155d334e0fe3a7";	//InternalTaskTypes.RECALCULATION_STARTED
		public const string RECALCULATION_COMPLETE = "093ea585774e49838c656eec62ca5663";	//InternalTaskTypes.RECALCULATION_COMPLETED
		public const string RECALCULATION_STOPPED = "fcd87ee72cc743628eb4ad6ecbf70796";	//InternalTaskTypes.RECALCULATION_STOPPED
		public const string ARCHIVE = "9084f2f2617e44e582a067466c27008d";	//InternalTaskTypes.ARCHIVE
		public const string ADD_METER = "af84239ee5594bd1b425544ec6769f52";	//InternalTaskTypes.ADD_METER_DEVICE
		public const string MOVE_DEVICE_ADD = "EF5EBA9624DB4b4e89CFE5CD4A136D7C";	//InternalTaskTypes.MOVE_METER_DEVICE_ADD
		public const string REMOVE_METER = "27d8944e70bc479ab84543036ad2707b";	//InternalTaskTypes.REMOVE_METER_DEVICE
		public const string MOVE_DEVICE_REMOVE = "14A9A96DCA2C42be90C087A66401F03C";	//InternalTaskTypes.MOVE_METER_DEVICE_REMOVE
		public const string EVENT = "e5ed464a3ce64930845eb69df383be87";	//InternalTaskTypes.EVENT
		public const string READ_METER_FIRMWARE_AND_BOOTROM_VERSIONS = "e252b767c42a41a69e5c6ef3c57ef5df";	//InternalTaskTypes.READ_METER_DEVICE_FIRMWARE_AND_BOOTROM_VERSIONS
		// Communication Adapters
		public const string CONNECT = "c0eca309cb544ebb8fa6f3b7befb4f91";	//InternalTaskTypes.CONNECT_GATEWAY
		public const string DISCONNECT = "654bbb9e99e142a69bfee55399680434";	//InternalTaskTypes.DISCONNECT_GATEWAY
        public const string CREATE_ENTRY = "C728EA59DE474bee987E9252D9EB113D";	//InternalTaskTypes.CREATE_PPP_ACCOUNT
        public const string DELETE_ENTRY = "8CB7034AB826495888BC6163442249EC";	//InternalTaskTypes.DELETE_PPP_ACCOUNT
		public const string UPDATE_ENTRY_PHONE_NUMBER = "4eafe7b8e63c4ca39a1dafa921db9595";	//GatewayTaskTypes.SET_WAN_PHONE_NUMBER
		public const string CHECK_FOR_ORPHAN_CONNECTIONS = "4e33ea19ae754711aff6f1792fafe8d1";	//InternalTaskTypes.CHECK_FOR_ORPHANED_CONNECTIONS

		// DC 1000 Adapter
		#pragma warning disable 618
        public const string REQUEST_METER_LOAD_PROFILE = REQUEST_METER_FULL_LOAD_PROFILE;	//DeviceTaskTypes.READ_FULL_LOAD_PROFILE
		#pragma warning restore
        public const string REMOTE_METER_CONNECT = "9b076bc9903444899ff2734889aa9086";	//DeviceTaskTypes.CONNECT_LOAD
        public const string REMOTE_METER_DISCONNECT = "2fab533d860f4cbe9bf85f418e36093a";	//DeviceTaskTypes.DISCONNECT_LOAD
        public const string READ_BILLING_DATA_ON_DEMAND = "6eee1c4551484aa68384b84ef265b088";	//DeviceTaskTypes.READ_BILLING_DATA_ON_DEMAND
        public const string UPDATE_GATEWAY_FIRMWARE = "5797529de55d4598bdbb1565ef64c862";	//GatewayTaskTypes.UPDATE_FIRMWARE
        public const string SET_MAXIMUM_POWER_LEVEL = "e507e37bb1534d1d98b686efab3fcd2f";	//DeviceTaskTypes.SET_PRIMARY_MAXIMUM_POWER_LEVEL
		public const string SET_MAXIMUM_POWER_LEVEL_DURATION = "c847892e95264216be97cba84388bf0a";	//DeviceTaskTypes.SET_MAXIMUM_POWER_LEVEL_DURATION
        public const string CHANGE_PPP_LOGIN = "929AEE135E1B4ef3A32DFECC83977724";	//GatewayTaskTypes.SET_PPP_USERNAME
        public const string CHANGE_PPP_PASSWORD = "5F7274D13AC143cc86B8DC20FC2ED491";	//GatewayTaskTypes.SET_PPP_PASSWORD
        public const string READ_MAXIMUM_POWER_LEVEL = "53dab6299f8548d1b4252955ae452347";	//DeviceTaskTypes.READ_PRIMARY_MAXIMUM_POWER_LEVEL
		public const string READ_MAXIMUM_POWER_LEVEL_DURATION = "b1bd1d54ed5e4b3782b45a31d3dd9b10";	//DeviceTaskTypes.READ_MAXIMUM_POWER_LEVEL_DURATION
        public const string SET_SEASON_SCHEDULES = "018DAF857332403e8EEDAF88AAD7F796";	//DeviceTaskTypes.SET_TOU_SEASON_SCHEDULES
        public const string SET_DAY_SCHEDULES = "E7F9B97C3CFE4b09A94A103F2DED1067";	//DeviceTaskTypes.SET_TOU_DAY_SCHEDULES
        public const string SET_TOU_RECURRING_DATES = "FCDED048A6BF4b8a8FEA2004F40D8D23";	//DeviceTaskTypes.SET_TOU_RECURRING_DATES
        public const string RETRIEVE_DAY_SCHEDULES = "3080861AAAA1493781605E11B5F6C24F";	//DeviceTaskTypes.READ_TOU_DAY_SCHEDULES
        public const string RETRIEVE_SEASON_SCHEDULES = "B32962D9E0234f25A61963B03002E66D";	//DeviceTaskTypes.READ_TOU_SEASON_SCHEDULES
        public const string RETRIEVE_TOU_RECURRING_DATES = "B438B71FD3624ab994D596B29DAD6F84";	//DeviceTaskTypes.READ_TOU_RECURRING_DATES
        public const string ENABLE_MAXIMUM_POWER = "6c0dd22755ca4ac69ee3ca7708f8ffdb";	//DeviceTaskTypes.SET_PRIMARY_MAXIMUM_POWER_STATUS
        public const string PERFORM_SELF_BILLING_READ = "a92907a371774a748829de52f5ca45bc";	//DeviceTaskTypes.READ_SELF_BILLING_DATA
		public const string READ_SECONDARY_BILLING_REGISTERS = "70818b636c55499f80c2b6028e3861ff";	//DeviceTaskTypes.READ_SECONDARY_BILLING_REGISTERS
        public const string OPEN_SECONDARY_CONTROL_OUTPUT = "a0bfeb73f357479f8bf18c33588f2141";	//DeviceTaskTypes.DISCONNECT_CONTROL_RELAY
        public const string CLOSE_SECONDARY_CONTROL_OUTPUT = "8d684c709dde4ee08c06a574ce62a501";	//DeviceTaskTypes.CONNECT_CONTROL_RELAY
		public const string ENABLE_SECONDARY_CONTROL_OUTPUT_TIERS = "bffd077bbe6a4d028610bd6617497197";	//DeviceTaskTypes.SET_CONTROL_RELAY_TIERS_STATUS
		public const string SET_SECONDARY_CONTROL_OUTPUT_TIERS = "ca4c2d1dde0e46e6b54f4b5647875e00";	//DeviceTaskTypes.SET_CONTROL_RELAY_TIERS
        public const string SET_LOAD_PROFILE_CONFIGURATION = "d614b22684cf45d6bc50363b9a6f245e";	//DeviceTaskTypes.SET_LOAD_PROFILE_CONFIGURATION
        public const string READ_GATEWAY_TO_SERVER_MODEM_INIT_STRING = "50328816894c43d79271e6b950ffc4b5";	//GatewayTaskTypes.READ_GATEWAY_TO_SERVER_MODEM_INIT_STRING
        public const string READ_GATEWAY_TO_SERVER_MODEM_TYPE = "1e6470dd541d4363984a28e1a0ad3bb6";	//GatewayTaskTypes.READ_GATEWAY_TO_SERVER_MODEM_TYPE
        public const string READ_GATEWAY_TO_SERVER_PHONE_NUMBER_1 = "8b9beba68b3944c7948c0a883cbf445b";	//GatewayTaskTypes.READ_GATEWAY_TO_SERVER_PHONE_NUMBER_1
        public const string READ_GATEWAY_TO_SERVER_PHONE_NUMBER_2 = "0b40b4c0c0dc41689e3eeb4b1e8b71d2";	//GatewayTaskTypes.READ_GATEWAY_TO_SERVER_PHONE_NUMBER_2
        public const string READ_GATEWAY_TO_SERVER_IP_ADDRESS = "91c7803e15944c999ca4410755e74a53";	//GatewayTaskTypes.READ_GATEWAY_TO_SERVER_IP_ADDRESS
        public const string SET_GATEWAY_TO_SERVER_IP_ADDRESS = "d64100d65024434bbdff2be23d7d5c64";	//GatewayTaskTypes.SET_GATEWAY_TO_SERVER_IP_ADDRESS
        public const string SET_SERVER_TO_GATEWAY_IP_ADDRESS = "0f12e4bcf7854b218c44747555a46784";	//GatewayTaskTypes.SET_IP_ADDRESS
        public const string SET_GATEWAY_TO_SERVER_PHONE_NUMBER_1 = "dbfde8dd7dd943ceb42c0781a6b571ab";	//GatewayTaskTypes.SET_GATEWAY_TO_SERVER_PHONE_NUMBER_1
        public const string SET_GATEWAY_TO_SERVER_PHONE_NUMBER_2 = "8f54808f5af543499fab4f99c1147964";	//GatewayTaskTypes.SET_GATEWAY_TO_SERVER_PHONE_NUMBER_2
        public const string SET_GATEWAY_TO_SERVER_MODEM_TYPE = "969d665c8da34995999d6102c0d9c548";	//GatewayTaskTypes.SET_GATEWAY_TO_SERVER_MODEM_TYPE
        public const string SET_GATEWAY_TO_SERVER_MODEM_INIT_STRING = "dab6e63d5a254565a8f83d7ff0a4ca85";	//GatewayTaskTypes.SET_GATEWAY_TO_SERVER_MODEM_INIT_STRING
		public const string READ_GATEWAY_STATISTICS = "0b31c2f49a4e467fb20f9241153507ea";	//GatewayTaskTypes.READ_STATISTICS
        public const string ENABLE_TOTAL_ENERGY = "c2ee73bee9b847fb803ddac8a4d51b1e";	//GatewayTaskTypes.SET_TOTAL_ENERGY_STATUS
        public const string SET_SECURITY_OPTIONS = "60e8a8beeb314ce7b5409c4aa693eeae";	//GatewayTaskTypes.SET_SECURITY_OPTIONS
        public const string UPDATE_METER_FIRMWARE = "dd2aa43bd23e4855946785e535e610f0";	//DeviceTaskTypes.UPDATE_FIRMWARE
		public const string READ_METER_DISPLAY_CONFIGURATION = "07A8F21239464ab4875C835085B9F495";	//DeviceTaskTypes.READ_DISPLAY_CONFIGURATION
		public const string SET_METER_DISPLAY_CONFIGURATION = "8B66865C7FC54a3aBBAD08738768BC11";	//DeviceTaskTypes.SET_DISPLAY_CONFIGURATION
		public const string SET_ALARM_DISPLAY_CONFIGURATION = "1C2C078AE4B147ddB9CA07F214D3CA0C";	//DeviceTaskTypes.SET_ALARM_DISPLAY_CONFIGURATION
		public const string READ_MAXIMUM_POWER_ENABLE = "59C52D3F7CF545b2A11F48A45B04FA2C";	//DeviceTaskTypes.READ_PRIMARY_MAXIMUM_POWER_STATUS
		public const string READ_METER_STATUS = "bb08706eb0274ac392047d67f3064fa8";	//InternalTaskTypes.READ_METER_DEVICE_STATUS
		// Power Quality
        public const string READ_POWER_QUALITY_DATA = "88d02253387e4a789cf721580e0578ed";	//DeviceTaskTypes.READ_POWER_QUALITY

		//V2 commands
		public const string READ_PULSE_INPUT_CONFIGURATION = "1170e680261f4d75a72e9ca74ae0d7d2";	//DeviceTaskTypes.READ_PULSE_INPUT_CONFIGURATION
		public const string READ_INSTANTANEOUS_POWER = "6659D10F0962447cBD07E169B65D8001";	//DeviceTaskTypes.READ_INSTANTANEOUS_POWER
		public const string READ_SECONDARY_CONTROL_OUTPUT_RELAY = "e44f1020a7a14d679d596f5c655bffcb";	//DeviceTaskTypes.READ_CONTROL_RELAY
		public const string READ_PRIMARY_CONTROL_OUTPUT = "73809a395bbc447a87673bde6739c952";	//DeviceTaskTypes.READ_LOAD_STATUS
		public const string BROADCAST_OPEN_SECONDARY_CONTROL_OUTPUT = "8836af9905b448b1a2b30dbeb5e052fe";	//GatewayTaskTypes.BROADCAST_DISCONNECT_CONTROL_RELAY
		public const string BROADCAST_CLOSE_SECONDARY_CONTROL_OUTPUT = "d186b04ff2b14172abaa3f2a1a6c788e";	//GatewayTaskTypes.BROADCAST_CONNECT_CONTROL_RELAY
		public const string BROADCAST_MAXIMUM_POWER_LEVEL_ENABLE = "4b69b9e8e6f5401e8b3224649e264c14";	//GatewayTaskTypes.BROADCAST_SET_PRIMARY_MAXIMUM_POWER_STATUS
		public const string READ_GATEWAY_FIRMWARE_VERSION = "9d4b5062406b4e37bbfa1e70e9fbd318";	//GatewayTaskTypes.READ_FIRMWARE_VERSION
		public const string READ_METER_FIRMWARE_VERSION = "ca1ca2cd30444b4a84a0245e9c86f8d2";	//DeviceTaskTypes.READ_FIRMWARE_VERSION
        public const string GET_GATEWAY_VERSION = "035C0D9EA8CB4d6499D98BEF0CDE81E3";	//InternalTaskTypes.READ_GATEWAY_VERSION
		public const string RETRIEVE_METER_SOFTWARE_VERSION = "DE798DD0A022417f9C3A5BB6A05FCBF6";	//InternalTaskTypes.READ_METER_DEVICE_SOFTWARE_VERSION
		public const string RETRIEVE_METER_FIRMWARE_VERSION = "940E43D713594c45AEF877087535D7E6";	//InternalTaskTypes.READ_METER_DEVICE_FIRMWARE_VERSION
		public const string SET_PULSE_INPUT_CONFIGURATION = "edf915ca40ca44cfbbdd667603138967";	//DeviceTaskTypes.SET_PULSE_INPUT_CONFIGURATION
		public const string READ_ACTIVE_TOU_CALENDAR = "0511085D4DC9442c966106842AB7A6C9";	//DeviceTaskTypes.READ_ACTIVE_TOU_CALENDAR
		public const string READ_PENDING_TOU_CALENDAR = "1537F6D9BC3C43a697960DB3A5365A35";	//DeviceTaskTypes.READ_PENDING_TOU_CALENDAR
		public const string SET_PENDING_TOU_CALENDAR = "4AE8A9D89305472490D20336FF25AC7E";	//DeviceTaskTypes.SET_PENDING_TOU_CALENDAR
		public const string SET_STOP_MODE = "abf544b3bc624adca066eba88369038a";	//InternalTaskTypes.SET_STOP_MODE

		//V2.1 Tasks
		public const string READ_DEVICE_DOWN_LIMIT = "a9d73ac2abc94ebdbee8731284aebf45";	//DeviceTaskTypes.READ_DOWN_LIMIT
		public const string SET_DEVICE_DOWN_LIMIT = "bb45fc6e85c241808a43c770927a3f5b";	//DeviceTaskTypes.SET_DOWN_LIMIT
		public const string SET_DEVICE_DATE_TIME = "89c51f87d84847f5a1ea986b7d67638b";	//DeviceTaskTypes.SET_DATE_TIME
		public const string READ_REPEATER_PATHS = "15357b6ee3ec46b082e2da8d3c296134";	//GatewayTaskTypes.READ_REPEATER_PATHS
		public const string CLEAR_METER_ALARMS = "8dc9a6a52cfa42d685430be251c8670a";	//InternalTaskTypes.CLEAR_METER_DEVICE_ALARMS
		public const string REBOOT_GATEWAY = "89e6c8b9963d482b8d87cdf39fa1910c";	//GatewayTaskTypes.REBOOT
        public const string REQUEST_METER_FULL_LOAD_PROFILE = "ffedafe3328c45f3ad01a6fe01290d84";	//DeviceTaskTypes.READ_FULL_LOAD_PROFILE
		public const string REQUEST_METER_DELTA_LOAD_PROFILE = "dc126b9a6224447b901b42687d79f1f2";	//DeviceTaskTypes.READ_DELTA_LOAD_PROFILE
		public const string REQUEST_METER_CONTINUOUS_DELTA_LOAD_PROFILE = "4b6c5b0f93a6427687c8f6d418dd4676";	//DeviceTaskTypes.READ_CONTINUOUS_DELTA_LOAD_PROFILE
		public const string SET_LOG_EVENTS_CONFIGURATION = "010FD66BA5C84b508952D5B5761CA21B";	//InternalTaskTypes.SET_LOG_EVENTS_CONFIGURATION
		public const string REMOVE_MBUS = "1eee13291f804e6692f7c64d252abd74";	//InternalTaskTypes.REMOVE_MBUS_DEVICE
		public const string DELETE_GATEWAY_WAN_CONFIGURATION = "ee384c36513448e689f30a26a443ced5";	//GatewayTaskTypes.DELETE_WAN_CONFIGURATION

		//v2.2 Tasks
		public const string READ_MEP_CARD_CONFIGURATION = "c44d0adfd2444207848b6a35a624a997";	//DeviceTaskTypes.READ_MEP_CARD_CONFIGURATION
		public const string SET_GATEWAY_DEVICE_LIMIT = "3c25bdd7fb2e4f88bf96a9132d766a28";	//GatewayTaskTypes.SET_DEVICE_LIMIT
		public const string READ_GATEWAY_DEVICE_LIMIT = "632c4e86aaa84595a07f6f06e8563c9b";	//GatewayTaskTypes.READ_DEVICE_LIMIT
		public const string CLEAR_MBUS_ALARMS = "55031cf93b894e738712d0b1628d4ed6";	//InternalTaskTypes.CLEAR_MBUS_DEVICE_ALARMS
		public const string READ_DEVICE_STATISTICS = "92ee7e0e0a00414dadb1a199280ec70c";	//DeviceTaskTypes.READ_STATISTICS
		public const string READ_DEVICE_EVENT_LOG = "b1548023071d4fba814a53000a8e5689";	//DeviceTaskTypes.READ_EVENT_LOG
		public const string CLEAR_EVENT_LOG_PENDING_OVERFLOW_ALARM = "9e335d44b6bd44218ac722a8191e6ca6";	//InternalTaskTypes.CLEAR_EVENT_LOG_PENDING_OVERFLOW_ALARM
		public const string SYNCHRONIZE_DEVICE_ALARMS = "5e9b7609a8c941488d281ddaeffb19bd";	//InternalTaskTypes.SYNCHRONIZE_METER_DEVICE_ALARMS
		public const string SYNCHRONIZE_MBUS_DEVICE_ALARMS = "0eb93548f5c7449e930d55f08955bbe8";	//InternalTaskTypes.SYNCHRONIZE_MBUS_DEVICE_ALARMS

        //v3.0 Tasks

		//ATM Function Scheduling
		public const string SET_GATEWAY_PROCESS_CONFIGURATION = "d9d04314b4c447408c4569723777ef60";	//GatewayTaskTypes.SET_PROCESS_CONFIGURATION
		public const string READ_GATEWAY_PROCESS_CONFIGURATION = "ad3a490bd4934f239056a25cdd609324";	//GatewayTaskTypes.READ_PROCESS_CONFIGURATION

		//Read Gateway Discovered Devices
		public const string READ_GATEWAY_DISCOVERED_DEVICES = "7700d7a39b24481dab4990980bb4e0da";	//GatewayTaskTypes.READ_DISCOVERED_DEVICES
		public const string READ_NEW_DISCOVERED_DEVICES = "c600ac0fd58f42328ffaa4ee7774f5ae";	//InternalTaskTypes.READ_NEW_DISCOVERED_METER_DEVICES

		// Read/Set M-Bus alarm polling rate
		public const string READ_DEVICE_ALARM_POLLING_RATE = "f6a27f0049e94f4aa7f944be84699648";	//DeviceTaskTypes.READ_ALARM_POLLING_RATE
		public const string SET_DEVICE_ALARM_POLLING_RATE = "92df0f15a4a24429aa566984ee457639";	//DeviceTaskTypes.SET_ALARM_POLLING_RATE

		// Read/Set M-Bus billing schedule
		public const string READ_DEVICE_BILLING_SCHEDULE = "78BC0D4C6E684dc3A3AE8242909E114A";	//DeviceTaskTypes.READ_BILLING_SCHEDULE
		public const string SET_DEVICE_BILLING_SCHEDULE = "AE7B6DC406FD40c28CADAF95A74AA7FE";	//DeviceTaskTypes.SET_BILLING_SCHEDULE

		// Read Device Historical Billing Data
		public const string READ_DEVICE_HISTORICAL_BILLING_DATA = "1ff26ee6f4a649eca54d5ce3ee3c780c";	//DeviceTaskTypes.READ_HISTORICAL_BILLING_DATA

		//Gateway WAN Configuration
        public const string READ_GATEWAY_WAN_CONFIGURATION = "9340c39dac934866aa9c9b459bdf9baf";	//GatewayTaskTypes.READ_WAN_CONFIGURATION
		public const string SET_GATEWAY_WAN_CONFIGURATION = "50B7EE5AB227474e9C18361ABEEE6FC6";	//GatewayTaskTypes.SET_WAN_CONFIGURATION

		//Read/Set Device Auto Discovery Configuration
		public const string READ_DEVICE_AUTO_DISCOVERY_CONFIGURATION = "cadecfc3478d49cca08c6433f7e8a425";	//DeviceTaskTypes.READ_AUTO_DISCOVERY_CONFIGURATION
		public const string SET_DEVICE_AUTO_DISCOVERY_CONFIGURATION = "01de8bb8cfb440a4b292c42708f7cd55";	//DeviceTaskTypes.SET_AUTO_DISCOVERY_CONFIGURATION

		//Read/Set Device Utility Information
		public const string READ_DEVICE_UTILITY_INFORMATION = "d39e9a3a80c0414d85a4bd758091def7";	//DeviceTaskTypes.READ_UTILITY_INFORMATION
		public const string SET_DEVICE_UTILITY_INFORMATION = "b07031d448e740cf90479f6752a9da8e";	//DeviceTaskTypes.SET_UTILITY_INFORMATION

        //Read/Set Device Power Quality Configuration
        public const string READ_DEVICE_POWER_QUALITY_CONFIGURATION = "cc0addddcd854678b7f654953817a628";   //DeviceTaskTypes.READ_POWER_QUALITY_CONFIGURATION
        public const string SET_DEVICE_POWER_QUALITY_CONFIGURATION = "7bd4fa2e47914e98bb129d5f8ba15497";     //DeviceTaskTypes.SET_POWER_QUALITY_CONFIGURATION

		//Used for system timeout task
		public const string TASK_TIMEOUT = "d67f265555e843e5aca2706fde0f0048";		//InternalTaskTypes.TASK_TIMEOUT

        ////////////////////////////////////////////////////////////////////////////////////////
		//The following are deprecated - the version in which they were deprecated and the    //
		//constant replacing them (if any) is listed to the right of the constant.            //
		////////////////////////////////////////////////////////////////////////////////////////

		[Obsolete("v2.1 - use GatewayTaskTypes.READ_STATISTICS")]
		public const string RETRIEVE_GATEWAY_PERFORMANCE_STATISTICS = "0b31c2f49a4e467fb20f9241153507ea"; //v2.1 - use GatewayTaskTypes.READ_STATISTICS

		[Obsolete("v2.1 - use DeviceTaskTypes.READ_POWER_QUALITY_OUTAGE_DURATION_THRESHOLD")]
		public const string READ_POWER_QUALITY_TIME_THRESHOLD = "261A278CCE224ffeB08051F0E1641365"; //v2.1 - use DeviceTaskTypes.READ_POWER_QUALITY_OUTAGE_DURATION_THRESHOLD

		[Obsolete("v2.1 - use DeviceTaskTypes.READ_POWER_QUALITY_SAG_VOLTAGE_PERCENT_THRESHOLD")]
		public const string READ_POWER_QUALITY_SAG_THRESHOLD = "DCA6CABC85C8458387EEEA8A1FFF40DB"; //v2.1 - use DeviceTaskTypes.READ_POWER_QUALITY_SAG_VOLTAGE_PERCENT_THRESHOLD

		[Obsolete("v2.1 - use DeviceTaskTypes.READ_POWER_QUALITY_SURGE_VOLTAGE_PERCENT_THRESHOLD")]
		public const string READ_POWER_QUALITY_SURGE_THRESHOLD = "00EEE598B2AC4934A501778C40EDC6BC"; //v2.1 - use DeviceTaskTypes.READ_POWER_QUALITY_SURGE_VOLTAGE_PERCENT_THRESHOLD

		[Obsolete("v2.1 - use DeviceTaskTypes.READ_POWER_QUALITY_OVERCURRENT_PERCENT_THRESHOLD")]
		public const string READ_POWER_QUALITY_OVERCURRENT_THRESHOLD = "475316B0A63A4f0d9EBA5E8BF98789B2"; //v2.1 - use DeviceTaskTypes.READ_POWER_QUALITY_OVERCURRENT_PERCENT_THRESHOLD

		[Obsolete("v2.1 - use DeviceTaskTypes.SET_POWER_QUALITY_OUTAGE_DURATION_THRESHOLD")]
		public const string SET_POWER_QUALITY_TIME_THRESHOLD = "91407B2D6D1B48a1BA4713B094A5949F"; //v2.1 - use DeviceTaskTypes.SET_POWER_QUALITY_OUTAGE_DURATION_THRESHOLD

		[Obsolete("v2.1 - use DeviceTaskTypes.SET_POWER_QUALITY_SAG_VOLTAGE_PERCENT_THRESHOLD")]
		public const string SET_POWER_QUALITY_SAG_THRESHOLD = "A76182A992FD4932B2973DB880AD8585"; //v2.1 - use DeviceTaskTypes.SET_POWER_QUALITY_SAG_VOLTAGE_PERCENT_THRESHOLD

		[Obsolete("v2.1 - use DeviceTaskTypes.SET_POWER_QUALITY_SURGE_VOLTAGE_PERCENT_THRESHOLD")]
		public const string SET_POWER_QUALITY_SURGE_THRESHOLD = "494F9D8827044730B9786B74B82B94C4"; //v2.1 - use DeviceTaskTypes.SET_POWER_QUALITY_SURGE_VOLTAGE_PERCENT_THRESHOLD

		[Obsolete("v2.1 - use DeviceTaskTypes.SET_POWER_QUALITY_OVERCURRENT_PERCENT_THRESHOLD")]
		public const string SET_POWER_QUALITY_OVERCURRENT_THRESHOLD = "2A5395280E8F4a1fAEDF3CFAA2E918D9"; //v2.1 - use DeviceTaskTypes.SET_POWER_QUALITY_OVERCURRENT_PERCENT_THRESHOLD


		[Obsolete("v2.1 - a task of this type no longer exists")]
		public const string ENABLE_METER = "b6d3a13e7d034b11be859943e6ae9387";  //v2.1 - a task of this type no longer exists

		[Obsolete("v2.2 - a task of this type no longer exists")]
		public const string ENABLE_MBUS_DEVICES = "f093b806d6884baaa39b08682a0847b5";	//v2.2 - a task of this type no longer exists

		[Obsolete("v2.2 - not replaced as this task type was not implemented in any version")]
        public const string READ_DEVICE_PREPAY_MAXIMUM_POWER_STATUS = "598d24405eec4a94b575388080f067cd"; //v2.2 - not replaced as this task type was not implemented in any version

		[Obsolete("v2.2 - not replaced as this task type was not implemented in any version")]
		public const string SET_DEVICE_PREPAY_MAXIMUM_POWER_STATUS = "29f68b0428e24849bf689219b8b1a172"; //v2.2 - not replaced as this task type was not implemented in any version

		[Obsolete("v2.2 - not replaced as this task type was not implemented in any version")]
		public const string READ_DEVICE_PREPAY_MAXIMUM_POWER_LEVEL = "bbff1fd404e243be979338f9cdffb27e"; //v2.2 - not replaced as this task type was not implemented in any version

		[Obsolete("v2.2 - not replaced as this task type was not implemented in any version")]
		public const string SET_DEVICE_PREPAY_MAXIMUM_POWER_LEVEL = "ff2948952118428aa5e93220e7edd6f1"; //v2.2 - not replaced as this task type was not implemented in any version


        [Obsolete("v3.0 - use READ_POWER_QUALITY_CONFIGURATION")]
        public const string READ_POWER_QUALITY_OUTAGE_DURATION_THRESHOLD = "261A278CCE224ffeB08051F0E1641365";	//v3.0 - use READ_POWER_QUALITY_CONFIGURATION

        [Obsolete("v3.0 - use READ_POWER_QUALITY_CONFIGURATION")]
        public const string READ_POWER_QUALITY_SAG_SURGE_DURATION_THRESHOLD = "08BAE067222749448FCCADDE09B74A8F";	//v3.0 - use READ_POWER_QUALITY_CONFIGURATION

        [Obsolete("v3.0 - use READ_POWER_QUALITY_CONFIGURATION")]
        public const string READ_POWER_QUALITY_SAG_VOLTAGE_PERCENT_THRESHOLD = "DCA6CABC85C8458387EEEA8A1FFF40DB";	//v3.0 - use READ_POWER_QUALITY_CONFIGURATION

        [Obsolete("v3.0 - use READ_POWER_QUALITY_CONFIGURATION")]
        public const string READ_POWER_QUALITY_SURGE_VOLTAGE_PERCENT_THRESHOLD = "00EEE598B2AC4934A501778C40EDC6BC";	//v3.0 - use READ_POWER_QUALITY_CONFIGURATION

        [Obsolete("v3.0 - use READ_POWER_QUALITY_CONFIGURATION")]
        public const string READ_POWER_QUALITY_OVERCURRENT_PERCENT_THRESHOLD = "475316B0A63A4f0d9EBA5E8BF98789B2";	//v3.0 - use READ_POWER_QUALITY_CONFIGURATION

        [Obsolete("v3.0 - use SET_POWER_QUALITY_CONFIGURATION")]
        public const string SET_POWER_QUALITY_OUTAGE_DURATION_THRESHOLD = "91407B2D6D1B48a1BA4713B094A5949F";	//v3.0 - use SET_POWER_QUALITY_CONFIGURATION

        [Obsolete("v3.0 - use SET_POWER_QUALITY_CONFIGURATION")]
        public const string SET_POWER_QUALITY_SAG_SURGE_DURATION_THRESHOLD = "7A6BD5F1966848a6AF91D9E7DECEE25F";	//v3.0 - use SET_POWER_QUALITY_CONFIGURATION

        [Obsolete("v3.0 - use SET_POWER_QUALITY_CONFIGURATION")]
        public const string SET_POWER_QUALITY_SAG_VOLTAGE_PERCENT_THRESHOLD = "A76182A992FD4932B2973DB880AD8585";	//v3.0 - use SET_POWER_QUALITY_CONFIGURATION

        [Obsolete("v3.0 - use SET_POWER_QUALITY_CONFIGURATION")]
        public const string SET_POWER_QUALITY_SURGE_VOLTAGE_PERCENT_THRESHOLD = "494F9D8827044730B9786B74B82B94C4";	//v3.0 - use SET_POWER_QUALITY_CONFIGURATION

        [Obsolete("v3.0 - use SET_POWER_QUALITY_CONFIGURATION")]
        public const string SET_POWER_QUALITY_OVERCURRENT_PERCENT_THRESHOLD = "2A5395280E8F4a1fAEDF3CFAA2E918D9";	//v3.0 - use SET_POWER_QUALITY_CONFIGURATION
		///////////////////////////////////////////////////////////////////////////////////////////
	}

	//Starting with v2.3, the following class has been deprecated. It was not previously used.
	[Obsolete("v2.3 - class deprecated")]
	public class CommissionStatusTypes
	{
		public const string COMMISSION_NOT_COMPLETE = "C6FF1B4DD3BC4e29A43A09C6483EDBDA";
		public const string INITIAL_COMMISSION_COMPLETE = "87A4CE6351E54974B4FE216261C89940";
		public const string MULTIPLE_COMMISSIONS_COMPLETE = "9BC3BD5429E4498089D7DC1F52B7CFA9";
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //Starting with v3.0, the following class has been deprecated. Individual DeviceEvents are used instead.
	[Obsolete("v3.0 - DeviceEvents are used instead")]
	public class DC1000EventTypes
    {
        public const string HARDWARE_FAILURE = "e47ae463688245fca552ddfabeeda394";
        public const string SOFTWARE_FAILURE = "f847423354bc48e39ca3c3b82d5a10ba";
        public const string PLANNED_REBOOT = "98ac824aac3f41c9a242b4a2c27dc523";
        public const string UNPLANNED_REBOOT = "deb8a63238d746c1af343740f2ff0b19";
        public const string DEVICE_ALARM = "baf6e2cee374487fb2eb43a234c47f53";
        public const string DEVICE_CLOCK_ERROR = "049eadf50b674e59b0aecb1247ea48a3";
        public const string DC_TO_METER_COMMUNICATION_STATUS = "a1d05a2738ca4723b2043a79cb4926a6";
        public const string DEVICE_NACK = "07a8e71726af45159c725051c0a748b6";
        public const string DEVICE_PHASE_CHANGE = "3c5eca8e3f1349668537959906b391c7";
        public const string DEVICE_PHASE_INVERSION = "e4b21f8155fa45cba984a68149ea8234";
        public const string SEGMENT_DOWN = "fe2dba2e51c3434f87f422c12ca965cb";
        public const string RESOURCE_EXHAUSTION = "40c6b322ec094ceda9ad99a491a7bbff";
        public const string COMMUNICATION_FAILURE = "b61349547e5a4c74bbe2e124632da3a0";
        public const string SERVICE_TOOL_CONNECT = "8bb8b2ebc5ae4b1b988f89e28f0a6651";
        public const string FLASH_LOW = "90b388e5bbfa402c83f07c52a0d318d3";
        public const string FUNCTION_DONE = "d995608f764c4eed8c47852c30579eb6";
        public const string SECURITY_EXCEPTION = "9dd32d8f2fcc470ea7b0413fefe1da5c";
        public const string CA_CERTIFICATE_EXPIRATION = "f366851d89a340f8a9840f5c14395ee0";
        public const string DC_CERTIFICATE_EXPIRATION = "cb1615cb8f9945c3ab0c50ff2b253014";
        public const string RSA_KEY_GENERATED = "4f32c12b91854d4aa1d45a08ae75e8dd";

        ////////////////////////////////////////////////////////////////////////////////////////
        //The following are deprecated - the version in which they were deprecated and the    //
        //constant replacing them (if any) is listed to the right of the constant.            //
        ////////////////////////////////////////////////////////////////////////////////////////

        [Obsolete("v2.0 - use DC_TO_METER_COMMUNICATION_STATUS")]
        public const string DEVICE_DOWN = "a1d05a2738ca4723b2043a79cb4926a6";	//v2.0 - use DC_TO_METER_COMMUNICATION_STATUS
        ////////////////////////////////////////////////////////////////////////////////////////
    }

    //Starting with v3.0, the following class has been deprecated. Individual DeviceEvents are used instead, which contain the same values.
	[Obsolete("v3.0 - DeviceEvents are used instead")]
	public class DC1000DeviceAlarms
    {
        public const string CONFIGURATION_ERROR = "8ca7f8c35d2247a397c49c9f33d668a0"; //MeterAlarms.CONFIGURATION_ERROR;
        public const string SELF_CHECK_ERROR = "21b3b465b3e64bce952133f43f706c5c"; //MeterAlarms.SYSTEM_RESET;
        public const string RAM_FAILURE = "e5a5b0d1f1e44129a98fddaf6ce4398a"; //MeterAlarms.RAM_FAILURE;
        public const string NON_VOLATILE_MEMORY_FAILURE = "82a33447b5ab434bb4e578eec104bcea"; //MeterAlarms.NON_VOLATILE_MEMORY_FAILURE;
        public const string CLOCK_ERROR = "f316273e6f374e0086710af9d101054f"; //MeterAlarms.CLOCK_ERROR;
        public const string MEASUREMENT_ERROR = "41e0b86652164cd4a539b226cea18719"; //MeterAlarms.MEASUREMENT_ERROR;
        public const string LOW_BATTERY = "4ea1f5f2583f4017b8c449d6423123ca"; //MeterAlarms.LOW_BATTERY;
        public const string POWER_FAILURE = "b02fb601b98a47ce8de32dbb157fb2a2"; //MeterAlarms.POWER_FAILURE;
        public const string TAMPER_DETECT = "6f37d5a7280b405288fdb174c5a1af52"; //MeterAlarms.TAMPER_DETECTED;
        public const string ROM_FAILURE = "79c97a9c3bb147f7a21e921d43d51eb3"; //MeterAlarms.ROM_FAILURE;
        public const string CURRENT_FLOW_DETECTED_ON_LOW_VOLTAGE_PHASE = "a7f07be6c8414b79b7b1a3ebbfe60ee2"; //MeterAlarms.CURRENT_ON_MISSING_OR_UNUSED_PHASE;
        public const string PLC_CONFIGURATION_FAILURE = "88af05a6ce154fe5a4da71b92b1c6f88"; //MeterAlarms.PLC_COMMUNICATION_FAILURE;
        public const string INVALID_PASSWORD_ENTERED_OVER_OPTICAL_PORT = "39320b04e50d41f5ac68391dbd4d0131"; //MeterAlarms.INVALID_PASSWORD_OVER_OPTICAL;
        public const string REVERSE_ENERGY = "729ba6910acb4426a0a0706005a58976"; //MeterAlarms.REVERSE_ENERGY;
        public const string SAVE_ALL_ABORTED = "0ef90b3b76b94967b0e623386472e25c"; //MeterAlarms.DATA_BACKUP_INCOMPLETE;
        public const string DISCONNECT_ADC_ERROR = "75fbf631eb7947d29138327c11291ff1"; //MeterAlarms.DISCONNECT_SWITCH_ERROR;
        public const string GENERAL_ERROR = "2dd6e07c1bcc4d78bd4a8014f3fe21b2"; //MeterAlarms.GENERAL_ERROR;
        public const string REMOTE_COMMUNICATIONS_INACTIVE = "782db533d360401189e5f08b7f895d80"; //MeterAlarms.REMOTE_COMMUNICATIONS_INACTIVE;
        public const string PULSE_INPUT_CHANNEL_1_TAMPER = "e00f616458684d26b4882b5e33af693c"; //MeterAlarms.PULSE_INPUT_1_TAMPER;
        public const string PULSE_INPUT_CHANNEL_2_TAMPER = "0618bed8c9dc4be3941f0207c699632e"; //MeterAlarms.PULSE_INPUT_2_TAMPER;
        public const string PHASE_LOSS_DETECTED = "da41430263f2482c83180c7ec6ca5282"; //MeterAlarms.PHASE_LOSS;
        public const string PHASE_INVERSION = "bc648feb5e324de9ae4578bc89a8d557"; //MeterAlarms.PHASE_INVERSION;

        ////////////////////////////////////////////////////////////////////////////////////////
        //The following are deprecated - the version in which they were deprecated and the    //
        //constant replacing them (if any) is listed to the right of the constant.            //
        ////////////////////////////////////////////////////////////////////////////////////////

        [Obsolete("v2.1 - no longer used")]
        public const string PRIMARY_BREAKER_SHUTOFF = "cca2df7b0bd54b61afeb42cdddd23cad"; //v2.1 - no longer used
        ///////////////////////////////////////////////////////////////////////////////////////
    }

    //Starting with v3.0, the following class has been deprecated. Replaced by DataConcentratorRebootCodes
	[Obsolete("v3.0 - use the DataConcentratorRebootCodes class instead")]
	public class DC1000RebootCauses
    {
        public const string POWER_UP = "b13d8506f1de4e6e8d9dc64180f74505";
        public const string PANIC_RESET = "d708a5b736b04571bdff9d1ec168bccd";
        public const string CACHE_FLUSH_FAILURE = "ae2a15e81ba94ac59e87df93ec5be145";
        public const string CLOCK_WRAPAROUND = "411e00e2b07e48b8b4ec3d9ee5c92b38";
        public const string EXTERNAL_REQUEST = "8656b3a5d3c44f34bf61f08238e90815";
        public const string LOCAL_REQUEST = "7c71772af47845579de0fa9f6fdbacef";
        public const string BOOT_API_RESET = "5b9e5d27cf4a47a6be04d94bab143f4e";
        public const string NO_MEMORY = "d1b8de3ce66742a494b3c987a10bf0d9";
        public const string WATCHDOG = "55662d889f4543deb10ed7ce221a8b0c";
        public const string REASON_OUT_OF_RANGE = "5066441d40d54fa9be56bfc47ecbe6ee";
        public const string MODEM_INITIALIZATION = "fb7c3e5f1e85465d8204c1bdc47cd189";
        public const string UNKNOWN_EXCEPTION = "f9846a7806724db8944f6f417e84f9c0";
        public const string FATAL_TAMPER = "326f491f8a8741b4a41672898630cb5a";
        public const string BUFFER_EXHAUSTION = "7468ccbce24c405194ac93c8c2cf582a";
        public const string WAN_INACTIVE = "9a5256eae68d48e694a1126d47c001c5";
    }

    //Starting with v3.0, the following class has been deprecated. Replaced by DataConcentratorSecurityExceptionCodes
	[Obsolete("v3.0 - use the DataConcentratorSecurityExceptionCodes class instead")]
	public class DC1000SecurityExceptions
    {
        public const string NONE = "b8686056929643738bfe0f77dadf755e";
        public const string ST_CONSOLE_PASSWORD_FAILURE = "8f8879ab79a54fb6b47d69e181d60d78";
        public const string ST_PPP_CHAP_FAILURE = "ec30817dab1740a0b201bf9667f0c394";
        public const string WAN_PPP_CHAP_FAILURE = "4729f2d9cc8d413aa9ee96ae1b4706e5";
        public const string WAN_APPLICATION_AUTH_FAILURE = "92440929f37347b78a02b5aa96d51fb4";
        public const string WAN_SSL_FAILURE = "9cc8846087594119b26b8e2d126345df";
        public const string LOCAL_SSL_FAILURE = "6dcf3a4591824046accafbbd0002e3b2";
        public const string NES_SECURITY_FAILURE = "490197ec69414958b287ae20144ecd33";
        public const string PEER_DCXP_SESSION_CAPABILITY_NOT_RECOGNIZED = "59a625ea315e4f05b531d28171b21983";
        public const string PEER_DISAGREED_ON_DCXP_SESSION_CAPABILITY_REQUIRED = "a9cd9883d44f46e989d20290a3d3dfd6";
    }

    //Starting with v3.0, the following class has been deprecated.
	[Obsolete("v3.0 - deprecated")]
	public class DC1000RsaKeyStates
    {
        public const string NO_RSA_KEYS_PENDING = "f19c279741f2458ca30e8624cebf8554";
        public const string GENERATING_RSA_KEY = "f38aaf3bf84348bbaa7e5ad6b1d9a773";
        public const string RSA_KEY_GENERATED = "4202e082ffdc40a597ad3fde2c03aaf9";
        public const string ACTIVATING_RSA_KEY_AND_CERTIFICATES = "0569534a911d43898458baa3d8f3ed05";
        public const string RSA_KEY_AND_CERTIFICATES_ACTIVE = "830b26c7e3694a2aa4e8ef514cbdd2a4";

        ////////////////////////////////////////////////////////////////////////////////////////
        //The following are deprecated - the version in which they were deprecated and the    //
        //constant replacing them (if any) is listed to the right of the constant.            //
        ////////////////////////////////////////////////////////////////////////////////////////

        [Obsolete("v2.1 - use RSA_KEY_GENERATED")]
        public const string RSA_KEY_GENERTATED = "4202e082ffdc40a597ad3fde2c03aaf9";	//v2.1 - use RSA_KEY_GENERATED
        ////////////////////////////////////////////////////////////////////////////////////////
    }

    //Starting with v3.0, the following class has been deprecated.
    [Obsolete("v3.0 - deprecated")]
    public class DeviceServiceStatus
    {
        public const string CONNECTED = "9d9867a0f3844b50a3a221c2a94c2567";
        public const string DISCONNECTED = "1a3adf724b5b4b029f0d83939ab93e67";
    }

    //Starting with v3.0, the following class has been deprecated. Replaced by DataConcentratorHardwareFailureCodes
	[Obsolete("v3.0 - use the DataConcentratorHardwareFailureCodes class instead")]
	public class DC1000HardwareDiagnosticCodes
    {
        public const string DRAM_FAILURE = "4b64107457be4180af3bcd97c89d29f7";
        public const string FLASH_FAILURE = "aea543c7493643b0b4c57d7b50469abe";
        public const string SRAM_FAILURE = "d6920a13175f410cbdd7d678e202ede3";
        public const string RTC_FAILURE = "019298bda6e447e4a9192d61531dcf96";
        public const string NEURON_FAILURE = "5fcfbdc2fd0f4177aa7b9f37adfd4f7e";
        public const string CLOCK_FAILURE = "9bec8186d8c949c0a7cc5f6bbfad7fc1";
        public const string PHASE_FAILURE = "cb6204fd63ba42e4937efd7b9ebfd7a1";
    }

    //Starting with v3.0, the following class has been deprecated. Replaced by DataConcentratorSoftwareFailureCodes
	[Obsolete("v3.0 - use the DataConcentratorSoftwareFailureCodes class instead")]
	public class DC1000SoftwareDiagnosticCodes
    {
        public const string NONE = "004ca744a09d4733a9d18a428e75967b";
        public const string BOOT_LOOP = "cf8b328502b5412ebfdcf2e32b41afe1";
        public const string FILE_ERROR = "f5f9e9d769ba4c44a144d0d6465c7b54";
        public const string RTC_NOT_SET = "56e19e8d77e54773afd46a5152104bf1";
        public const string RTC_BATTERY_LOW = "0bd750aed582498782259756e3d819dd";
        public const string INVALID_PPP_USERNAME_OR_PASSWORD = "5f51f415e4464721b0134987d2f0e9ec";
        public const string UPDATE_FAILURE = "20827241468b40ec92fa1b21b9afe29e";
        public const string UPDATE_IMAGE_CRC_FAILURE = "589f193efb2d4ae39ec95c4efb02b2e0";
        public const string INTERNAL_REPEATING_LISTS_ERROR = "82fd0b512090464b8aca2821675ddcbe";
        public const string SRAM_CHECKSUM_ERROR = "35644bb0461e417096af462c058b3361";
        public const string IMPROPER_SHUTDOWN = "bea62789de07498d8acf9ea179b5d855";
        public const string EXTENDED_DATA_PROPERTY_ERROR = "31a73c89d8724c0dac3a20b158b78faa";
    }

    //Starting with v3.0, the following class has been deprecated. Replaced by DeviceNackCodes
	[Obsolete("v3.0 - use the DeviceNackCodes class instead")]
	public class DC1000DeviceNackCodes
    {
        public const string SERVICE_NOT_SUPPORTED = "3b06b5ad902642d4ad5d7637f1bf1fb2";
        public const string OPERATION_NOT_POSSIBLE = "2bdfd376f3f24f02b4185f9f85d3bb1e";
        public const string INAPPROPRIATE_ACTION_REQUESTED = "595d74df2b3f427495e819a1eee59f26";
        public const string DEVICE_BUSY = "832aaf3fa21d44468d50c8caa7c23a77";
        public const string DIGEST_ERROR = "4ac31c661b35436e84668abb03c474bf";
        public const string SEQUENCE_NUMBER_ERROR = "af18c500715d4e30b9d77e472adcf575";

        public const string PROCEDURE_NOT_COMPLETED = "6e204365ef9c4f13a8128735992a8fbf";
        public const string INVALID_PARAMETER_PROCEDURE_IGNORED = "de4e05b8c0b64948b4185b77a2d42bd0";
        public const string DEVICE_SETUP_CONFLICT_PROCEDURE_IGNORED = "20136ea064d54945a1ec20b63540d22a";
        public const string TIMING_CONSTRAINT_PROCEDURE_IGNORED = "6d40fae46ffd4a1f8136bdf4e29c0c21";
        public const string NO_AUTHORIZATION_PROCEDURE_IGNORED = "903fae11f8254d08ae8c809bddd41a16";
        public const string PROCEDURE_UNRECOGNIZED = "9f88e6fab35d4715989f0c8cda4b5d58";
    }

    //Starting with v3.0, the following class has been deprecated. Replaced by DataConcentratorCauseCodes
	[Obsolete("v3.0 - use the DataConcentratorCauseCodes class instead")]
	public class DC1000NackCauseCodes
    {
        public const string UNKNOWN_CODE = "7bede9d036d645708c254902e43cb85d"; //1
        public const string RESOURCE_NOT_FOUND = "2ca9c48665d845e8a29fb44f422055d4"; //2
        public const string NOT_IMPLEMENTED = "e08347ba4e66489c89de83b0bfb61bb6"; //3
        public const string INVALID_PARAMETER = "a4719031032c4c89833760f6dfca554f"; //4
        public const string OPERATION_FAILED = "8a0fe7f515584b35a954ff2a4d85a194"; //5
        public const string INTERNAL_FAILURE = "7fbc6eb80e7a4773a47be53aae83b722"; //6
        public const string OUT_OF_RANGE = "3a3030aa4d85406dae9d2d4b5cf2da72"; //7
        public const string INVALID_LENGTH = "44bd8e06b1cc4a8a8635e3be52b0278f"; //8
        public const string PROPERTY_NOT_FOUND = "773f7e89b3024ff9a9bc54d861732aed"; //9
        public const string OPERATION_NOT_ALLOWED = "108d4460f16249a68ba04b57e3a3d476"; //10
        public const string REPEAT_CHAIN_TOO_LONG = "f7c9fc8d7fdb48acaa34fad7a296fc3a"; //11
        public const string LIMIT_EXCEEDED = "0ce0c89985744d0c96355a1a78151765"; //12
        public const string RESOURCE_DISABLED = "8e9d68d762f24ae19616aa961373753a"; //13
        public const string PREVIOUS_SEGMENT_NOT_FOUND = "6f2087e3bb3a4b93845c3d3b006e715d"; //14
        public const string SEGMENT_LOOP = "3959d9a2df3949b79f886cc7e22211d1"; //15
        public const string DUPLICATE_SUBNET_NODE_ADDRESS = "0f19631536e34503b6bd87fa7738d76e"; //16
        public const string SEGMENT_NOT_FOUND = "8ecaf37e9f2848c0a500da1ba9b04e00"; //17
        public const string RESOURCE_IN_USE = "0122c07e6de9477db10e222b5f5a6ee8"; //18
        public const string DEVICE_NOT_DEFINED = "5b02f42be0e54120967e237d5773d60d"; //19
        public const string MESSAGE_SET_NOT_FOUND = "8225d882f2154ddda545e0fad314649f"; //20
        public const string DEVICE_NO_RESPONSE = "5ccfd4c78cc4486685069818c9f90a03"; //21
        public const string FUNCTION_ABORTED = "f0e399264ff44fbc9304f643621ff1a4"; //22

        public const string OPERATION_EXPIRED = "ffc44b7cca3549b8886c03880564cbcb"; //23
        public const string INVALID_SEGMENT = "055cd2fe5ec144c29ee11f2a6378d85c"; //24
        public const string INSUFFICIENT_RESOURCES = "299593d7d6ce4383850aee0533f8ca1a"; //25
        public const string INVALID_DATE_TIME = "5f61bdd1b0154f838bb5af7d83aad616"; //26
        public const string IMAGE_NOT_FOUND = "1e6dc2ba35c04b448323e83c401a0ade"; //27
        public const string IMAGE_CRC_FAILURE = "e3889119c0bf48b7b3f8c4e0b5059df5"; //28
        public const string TOO_MANY_TEST_POINTS_FOR_SEGMENT_PHASE = "49a71a8e2b45452e8691036c04609de3"; //29
        public const string DEVICE_PROCEDURE_FAILURE = "912b0781d191457d96173c20f309e1ba"; //30

        public const string TARGET_DISABLED = "d5c93d880bd344fc8526742c262e9b51"; //32
        public const string ADDRESS_ERROR = "bcd6895af8f04fe68f63939db43b8abf"; //33
        public const string DEVICE_NOT_REACHABLE = "52487c5461414eaf913ec728ba8218c1"; //34
        public const string AUTHENTICATED_RESPONSE_FAILURE = "e3482665545c448281312048d31cbb66"; //35
        public const string RESPONSE_NOT_AUTHENTIC = "55b768b9ee434dae830fd1bea97d0020"; //36
        public const string RESPONSE_INVALID = "8596c218b86348be854ffa1ce577519a"; //37
        public const string TARGET_NOT_ANSWERING_AGENT = "335d532ad9fb4935b318a0b0d81f6ccf"; //38
        public const string TARGET_OFFLINE = "25ce1cbe78744757824fde35b1aa001b"; //39
        public const string REPEATER1_FAILURE = "8c8d4339487e41378cef725c13124513"; //40
        public const string REPEATER2_FAILURE = "e24d78f565db4be99d39a4ddfe945ce9"; //41
        public const string REPEATER3_FAILURE = "c48a77afb7ca408989a1baffae4ef0c9"; //42
        public const string REPEATER4_FAILURE = "289766e2bc4a4785ae1894a8e5965677"; //43
        public const string REPEATER5_FAILURE = "12294b1f597c4c939e487964d22a8c13"; //44
        public const string REPEATER6_FAILURE = "1352c037bac24145b69d4262ccf33740"; //45
        public const string REPEATER7_FAILURE = "f2b7bdd6a77945909092a98158883179"; //46
        public const string REPEATER8_FAILURE = "04c3847ff44c4a63b0516e789070fb16"; //47
        public const string PHASE_NOT_MEASURABLE = "afc90b175f9f41678a53c46271b713e8"; //48
        public const string INVALID_STATE = "8418b3f062754f0eb4614e37bb3b8041"; //49
        public const string GENERIC_RESPONSE_FAILURE = "b4d17ed741f94a628e3c91a351485cb5"; //50
        public const string INVALID_HANDLE = "7a709e175ac241198b8ee33b22cee2bd"; //51
        public const string INVALID_STATE_FOR_ONLINE = "14ca9c087a8c499e8abe88115fb47635"; //52
        public const string INVALID_DEVICE_TYPE_FOR_OPERATION = "0bfddc37ed2947b98a06355c5a46aa87"; //53
        public const string AGENT_HAD_NO_BUFFER_FOR_RESPONSE = "e5039e51c7e94c6d9fe8e8130609cffb"; //54
        public const string APPLICATION_AUTHENTICATION_FAILURE = "472b59afec734ddfa7e81d15db42e122"; //55
        public const string DCXP_SOCKET_NOT_SECURED = "c70766aa40c140548988481526d1619a"; //56
        public const string DCX_NOT_READY_TO_HANDLE_REQUEST = "a0754514f9c14f8fb4c19d77c324ac4d"; //57
        public const string COMPRESSION_FAILURE = "c961e7e5094a4eafa1aabbf4518d48cf"; //58
        public const string DATA_OVERFLOW = "1f55c680a9e14c85a5c05a8b67743ec5"; //59
        public const string INVALID_DATATYPE = "ce73203b971e42ccb6bf314df29c6c35"; //60
        public const string DUPLICATE_PROPERTY = "84c47d934e164db8989f61a569865206"; //61
        public const string TRANSACTION_NUMBER_MISMATCH = "06575edeebbe48f4be8077f7c8e898f8"; //62
        public const string TRANSACTION_NUMBER_INTEGRITY = "0a7f6f8940a44402adfd91cfcab96e99"; //63
        public const string MEP_DEVICE_NOT_FOUND = "6bb8ac1862c449be9ea146616cf08896"; //64
        public const string OPERATION_INTERRUPTED = "c090d89ae37e484192a22ea16bf951e5"; //65
        public const string MIXED_ENCRYPTION = "eb0a1702284c4872beae04c569df6e04"; //66
        public const string WRONG_KEY = "34b87b9bb7e04a03b0b8a97464a43fde"; //67
        public const string CAPABILITY_REQUIRED = "875464591544435d9e677655982f231f"; //68
        public const string DECRYPTION_FAILURE = "af8db2b857954c7bac965fb8fb60372e"; //69
        public const string OPERATION_REJECTED = "04af3fbcc9d64a32a9cee9fbf6348055"; //70
        public const string NOT_MODIFIABLE = "69514fc919a54b298ccbc7cd0365f7c9"; //71
        public const string PHASE_ABORT = "2fedd5d37bc94b1a988c324889e199c2"; //72
        public const string WAN_CONFIG_ERROR = "17273d9be3574e38995c4a7864e0f87f"; //73
        public const string UNEXPECTED_DEVICE_RESET = "e18103dfab6941e68bb0c045d510feb4"; //74
        public const string MODEL_NUMBER_MISMATCH = "e6c13fb7b7ed4b8eb1a1f6cfa5551254"; //75
        public const string FIRMWARE_VERSION_NUMBER_MISMATCH = "beae616b92ff456eafaf5175fd28e157"; //76
        public const string DEVICE_IS_READ_WRITE_PROTECTED = "e04f64eb58804038af97b325224b3890"; //77
        public const string DEVICE_APP_CHECKSUM_VERIFICATION_FAILED = "ac4b50e0b9b0402b8b812274456462de"; //78
        public const string DEVICE_DATA_ERROR = "4df79567ab4d4d54aa790ae7472a16bb"; //79
        public const string IDI_MISMATCH = "0CC1AF25EEC04b79851A21017118F473"; //80
        public const string NOT_EXECUTED = "9587485b22e441d2ab3c35807c564581"; //81
        public const string RESOURCE_DATA_ERROR = "575d8dbd208d417dadf5c643d06ebda6"; //82
        public const string DEVICE_RESPONSE_CODE_UNKNOWN = "736313e4a781496598ede74340dadacd"; //100
        public const string DEVICE_REQUEST_REJECTED = "37e9919f3435457889c9cdca9d97cb0b"; //101
        public const string DEVICE_SERVICE_NOT_SUPPORTED = "f374d7adb35f4e4f9b8204d8ea998e7a"; //102
        public const string DEVICE_SECURITY_FAILURE = "16c04c74266c4312b4dbf6c2ff2c00ef"; //103
        public const string DEVICE_OPERATION_NOT_POSSIBLE = "0be77170579648f7b30433c884ce2235"; //104
        public const string DEVICE_ACTION_INAPPROPRIATE = "ccfc02e2c0544e33a17554a9d183bc4a"; //105
        public const string DEVICE_BUSY = "c43acd10253e4b4ea7f3fcb16247df1f"; //106
        public const string DEVICE_DATA_NOT_READY = "2027621a942a4cbcb818e7a4fd8d5899"; //107
        public const string DEVICE_DATA_LOCKED = "b969c4b328764ee2902f909c64eb7f49"; //108
        public const string DEVICE_RENEGOTIATE_REQUEST = "723d1545fe4b4eefb184d17b496235f6"; //109
        public const string DEVICE_INVALID_SERVICE_SEQUENCE_STATE = "2bd216169b9f49b0805e24db949ab6d0"; //110
        public const string DEVICE_COULD_NOT_AUTHENTICATE_REQUEST = "e15e1a81a44e425cb8a913d951cf3348"; //111
        public const string DEVICE_INVALID_AUTHENTICATION_SEQUENCE_NUMBER = "27d23394e031411da029bcdd40d107c4"; //112
        public const string DEVICE_PROCEDURE_ACCEPTED_BUT_NOT_COMPLETE = "64c9e4f9a2144116883a58ca38b8963f"; //113
        public const string DEVICE_PROCEDURE_INVALID_PARAMETER = "2e8ea53c80aa43b6ab15c16bfe3cf89f"; //114
        public const string DEVICE_PROCEDURE_CONFIGURATION_CONFLICT = "939a4b28014d4a108a548c016df067e3"; //115
        public const string DEVICE_PROCEDURE_TIMING_CONSTRAINT = "b1589efd4707427098b22019122a809e"; //116
        public const string DEVICE_PROCEDURE_NOT_AUTHORIZED = "de604d7bbe6647bd9736afc9da12de0c"; //117
        public const string DEVICE_PROCEDURE_ID_INVALID = "3920dd5e02b44503ab4f49e3fa927912"; //118
        public const string DEVICE_ON_DEMAND_REQUEST_TABLE_FULL = "0ff6e66d18804c65b9dc33725b47e756"; //119
        public const string DEVICE_UNKNOWN_PROC_RESPONSE = "39fe4a236fe0431b9665c92952368018"; //120
        public const string DEVICE_ICA_NACK = "f2c7c8dd29a64665bdb93edb2718f26e"; //121
        public const string DEVICE_INCOMPATIBLE_REQUEST = "ab99aee6f4374448a41419cdad67dc32"; //122
    }

    //Starting with v3.0, the following class has been deprecated. It is replaced by Constants.MBusAlarms.
	[Obsolete("v3.0 - use the MBusAlarms class instead")]
	public class MeterDetectedMBusAlarms
    {
        public const string BILLING_READ_OVERFLOW_OCCURRED = "81f436cdb03a42478cab94e128b17986";
        public const string FAILED_COMMUNICATIONS_ON_READ = "3f3a1abce12d4ad78e5095186e685445";
        public const string BILLING_READ_SERIAL_NUMBER_MISMATCH = "2d019ae40d7842a7839b837748369590";
    }

    //Starting with v4, the following class has been deprecated.
    [Obsolete("v4 - Deprecated")]
    public class MBusAlarmStatusTypes
    {
        public const string EVENT = "ea5aaee6bda24d5a8d9f7d919d3977eb";
        public const string NO_EVENT = "829c28ccb7024af6a67a035fd6c5413c";
    }

    //Starting with v4, the following class has been deprecated.
    [Obsolete("v4 - Deprecated")]
    public class MBusAlarms
    {
        public const string BILLING_READ_OVERFLOW_OCCURRED = "81f436cdb03a42478cab94e128b17986";
        public const string FAILED_COMMUNICATIONS_ON_READ = "3f3a1abce12d4ad78e5095186e685445";
        public const string BILLING_READ_SERIAL_NUMBER_MISMATCH = "2d019ae40d7842a7839b837748369590";
    }

    // List of types of time zone Daylight Savings Time supported - deprecated starting in v5
    [Obsolete("v5 - Deprecated")]
    public class TimeZoneDstTypes
    {
        public const string NO_DST = "3798357b8ff8450daa66d54c717e1624";
        public const string UNKNOWN_DST = "8aa83546e7d548f2b760fe1036bc666c";
        public const string US_DST = "7a8fe89cbf7c4803a042b4671a767b19";
        public const string EUROPEAN_DST = "e853364bb7274cf6ae62ee60f3fe8afe";
    }

	[Obsolete("v5.2.000 - Deprecated")]
	public class MaximumPowerTypes
	{
		public const string PRIMARY = "ECDC678900904377959DEFDA361DB929";
		public const string PREPAY = "C90A51D93AD14770A95CB78A2A58731A";
        public const string BOTH_PRIMARY_AND_PREPAY = "A5766D4A9F744f6988307E95FBE03B14";
	}

	[Obsolete("v5.3 - no replacement, no longer used")]
    public class WirelessMeshControllerConfigurationOptions
    {
        public const int PRESERVE_PREVIOUS_IP_ADDRESSES_AND_PORT_FORWARDING_MAPPINGS = 0;
        public const int DO_NOT_PRESERVE_PREVIOUS_IP_ADDRESSES_AND_PORT_FORWARDING_MAPPINGS = 1;
    }
	///////////////////////////////////////////////////////////////////////////////////////
	#endregion
}
