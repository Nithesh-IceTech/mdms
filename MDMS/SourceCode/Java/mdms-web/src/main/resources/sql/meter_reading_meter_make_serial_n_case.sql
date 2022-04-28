case
         when KAM_METER_ID is not null then 'KAMSTRUP'
         when ELS_METER_ID is not null then 'ELSTER'
         when NES_METER_ID is not null then 'ECHELON'
         else GENERIC_METER.METER_TYPE end as METER_MAKE,
       case
         when KAM_METER_ID is not null then KAMSTRUP_METER.SERIAL_N
         when ELS_METER_ID is not null then ELSTER_METER.SERIAL_N
         when NES_METER_ID is not null then NES_METER.SERIAL_N
         else GENERIC_METER.METER_SERIAL_N end as METER_SERIAL_N