select CASE WHEN type_desc is null THEN 'Not Set'
       ELSE type_desc
       END AS type_desc,
  count(*) as count from _TABLE_NAME_ group by type_desc
