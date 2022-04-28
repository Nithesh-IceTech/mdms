    _FIELD__diff,
    FIRST_VALUE(entry_time) IGNORE NULLS OVER (PARTITION BY groupHolder ORDER BY _FIELD__diff asc) as _FIELD__MIN_DT,
    FIRST_VALUE(entry_time) IGNORE NULLS OVER (PARTITION BY groupHolder ORDER BY _FIELD__diff desc) as _FIELD__MAX_DT,
    _FIELD_
