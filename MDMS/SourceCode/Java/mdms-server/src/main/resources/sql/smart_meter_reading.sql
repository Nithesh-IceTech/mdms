SELECT
  sum(TOTAL_KWHP_diff),
  sum(_FIELD__diff),
  max(TOTAL_KWHP),
  max(_FIELD_)

FROM (
  SELECT
    CASE WHEN entry_time > ? AND entry_time <= ?
      THEN TOTAL_KWHP - LAG(TOTAL_KWHP, 1, TOTAL_KWHP)
      OVER (
        ORDER BY entry_time )
    ELSE 0 END AS TOTAL_KWHP_diff,
    CASE WHEN entry_time > ? AND entry_time <= ?
      THEN _FIELD_ - LAG(_FIELD_, 1, _FIELD_)
      OVER (
        ORDER BY entry_time )
    ELSE 0 END AS _FIELD__diff,

    CASE WHEN entry_time > ? AND entry_time <= ?
      THEN TOTAL_KWHP
    ELSE 0 END AS TOTAL_KWHP,
    CASE WHEN entry_time > ? AND entry_time <= ?
      THEN _FIELD_
    ELSE 0 END AS _FIELD_

  FROM METER_READING
  WHERE
    _METER_ID_FIELD_ = ? AND
    ENTRY_DAY >= ? AND ENTRY_DAY <= ? AND
    (_TOU_FLTER_ OR (
      (to_char(entry_time, 'HH24:MI') >= '_FROM_TOU_' AND to_char(entry_time, 'HH24:MI') < '_TO_TOU_' OR
       (to_char(entry_time, 'HH24:MI') >= '_FROM_TOU_' OR to_char(entry_time, 'HH24:MI') < '_TO_TOU_')
      )
    )
    )
)