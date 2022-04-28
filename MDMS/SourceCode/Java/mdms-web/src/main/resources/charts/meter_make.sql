select * from (
  SELECT 'KAMSTRUP', count(*) as count
  FROM KAMSTRUP_METER
  UNION
  Select 'NES', count(*) as count
  from NES_METER
  )
order by count desc