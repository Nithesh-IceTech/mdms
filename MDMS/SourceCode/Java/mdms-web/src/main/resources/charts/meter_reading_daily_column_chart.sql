SELECT
  _LEGEND_ as LEGEND,
  to_char(ENTRY_TIME, 'YYYY/MM/DD') as X,
  COUNT(*)   as Y
FROM METER_READING
where ENTRY_DAY >= '_DATE_START_' and ENTRY_DAY < '_DATE_END_'
and _METER_FOREIGN_KEY_ is not null
GROUP BY to_char(ENTRY_TIMe, 'YYYY/MM/DD')
order by X asc