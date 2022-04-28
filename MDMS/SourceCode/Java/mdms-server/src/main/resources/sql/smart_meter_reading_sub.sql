
select _SELECT_
from (

  SELECT
   _LAG_

  FROM (
    SELECT
      '1' as groupHolder,
      entry_time,
      _CASE_

    FROM METER_READING
    WHERE
      _METER_ID_FIELD_ = '_METER_ID_VALUE_' AND
      ENTRY_DAY >= _START_DAY_ AND ENTRY_DAY <= _END_DAY_
      AND (_TOU_FLTER_ OR (
        (to_char(entry_time, 'HH24:MI') >= '_FROM_TOU_' AND to_char(entry_time, 'HH24:MI') < '_TO_TOU_')
      )
    )
    order by entry_time asc
    )
  where
    TO_CHAR(entry_time,'YYYY/MM/DD HH24:MI') > '_START_TIME_' AND TO_CHAR(entry_time,'YYYY/MM/DD HH24:MI') <= '_END_TIME_'
)