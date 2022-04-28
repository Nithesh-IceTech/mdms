SELECT
  'KAMSTRUP' as LEGEND,
  to_char(ENTRY_TIME, 'YYYY/MM') as X,
  COUNT(*)   as Y
FROM METER_READING
where ENTRY_DAY >= to_char(add_months(sysdate,-12), 'yyMMDD')
and KAM_METER_ID is not null
GROUP BY to_char(ENTRY_TIME, 'YYYY/MM')
UNION
SELECT
  'NES' as LEGEND,
  to_char(ENTRY_TIME, 'YYYY/MM') as X,
  COUNT(*)   as Y
FROM METER_READING
where ENTRY_DAY >= to_char(add_months(sysdate,-12), 'yyMMDD')
and NES_METER_ID is not null
GROUP BY to_char(ENTRY_TIME, 'YYYY/MM')
order by X asc