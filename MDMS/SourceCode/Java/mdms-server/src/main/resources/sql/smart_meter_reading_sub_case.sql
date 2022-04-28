      CASE WHEN TO_CHAR(entry_time,'YYYY/MM/DD HH24:MI') > '_START_TIME_' AND TO_CHAR(entry_time,'YYYY/MM/DD HH24:MI') <= '_END_TIME_' AND
      ENTRY_DAY - LAG(ENTRY_DAY, 1, ENTRY_DAY) OVER (ORDER BY entry_time ) = 0 AND GEN_TX_ID IS NULL
        THEN _FIELD_ - LAG(_FIELD_, 1, _FIELD_)
        OVER (
          ORDER BY entry_time )
      WHEN TO_CHAR(entry_time,'YYYY/MM/DD HH24:MI') > '_START_TIME_' AND TO_CHAR(entry_time,'YYYY/MM/DD HH24:MI') <= '_END_TIME_' AND
      ENTRY_DAY - LAG(ENTRY_DAY, 1, ENTRY_DAY) OVER (ORDER BY entry_time ) = 0 AND GEN_TX_ID IS NOT NULL
        THEN _FIELD_ - LAG(_FIELDLAG_, 1, _FIELDLAG_)
        OVER (
          ORDER BY entry_time )
      ELSE 0 END      AS _FIELD__diff,

      CASE WHEN TO_CHAR(entry_time,'YYYY/MM/DD HH24:MI') > '_START_TIME_' AND TO_CHAR(entry_time,'YYYY/MM/DD HH24:MI') <= '_END_TIME_'
        THEN _FIELD_
      ELSE 0 END      AS _FIELD_
