select *
from KAMSTRUP_METER
where CONSUMPTION_TYPE = 'Electricity' and
        LAST_COMMS_D is not null and
    (EXTRACT(MINUTE FROM LAST_COMMS_D) >= _Q1_ and EXTRACT(MINUTE FROM LAST_COMMS_D) < _Q2_ or
     EXTRACT(MINUTE FROM LAST_COMMS_D) >= _Q3_ and EXTRACT(MINUTE FROM LAST_COMMS_D) < _Q4_)