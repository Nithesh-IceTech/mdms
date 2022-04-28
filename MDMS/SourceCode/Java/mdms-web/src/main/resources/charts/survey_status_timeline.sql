SELECT
  statusDate,
  DESCRIPTION,
  sum(amount)
FROM (
  SELECT
    to_date(to_char(ENTITY_STATUS_CHANGE_D, 'MM/DD/YYYY'), 'MM/DD/YYYY') AS statusDate,
    lookups.DESCRIPTION,
    count(*)                                                             AS amount
  FROM ADVERTISING
    LEFT JOIN lookups ON lookups.LANG = '_LANG_' AND lookups.agency_id = '_AGENCY_ID_' AND LOOKUPS.LOOKUP_DEF = 'ENTITYSTAT' AND
                         LOOKUPS.code = ENTITY_STATUS_CD
  WHERE ENTITY_STATUS_CHANGE_D > to_date('2000-01-01', 'YYYY-MM-DD') and ADVERTISING.agency_id = '_AGENCY_ID_'
  GROUP BY lookups.DESCRIPTION, to_date(to_char(ENTITY_STATUS_CHANGE_D, 'MM/DD/YYYY'), 'MM/DD/YYYY')

  UNION ALL

  SELECT
    to_date(to_char(ENTITY_STATUS_CHANGE_D, 'MM/DD/YYYY'), 'MM/DD/YYYY') AS statusDate,
    lookups.DESCRIPTION,
    count(*)                                                             AS amount
  FROM BUSINESS
    LEFT JOIN lookups ON lookups.LANG = '_LANG_' AND lookups.agency_id = '_AGENCY_ID_' AND LOOKUPS.LOOKUP_DEF = 'ENTITYSTAT' AND
                         LOOKUPS.code = ENTITY_STATUS_CD
  WHERE ENTITY_STATUS_CHANGE_D > to_date('2000-01-01', 'YYYY-MM-DD') and BUSINESS.agency_id = '_AGENCY_ID_'
  GROUP BY lookups.DESCRIPTION, to_date(to_char(ENTITY_STATUS_CHANGE_D, 'MM/DD/YYYY'), 'MM/DD/YYYY')

  UNION ALL

  SELECT
    to_date(to_char(ENTITY_STATUS_CHANGE_D, 'MM/DD/YYYY'), 'MM/DD/YYYY') AS statusDate,
    lookups.DESCRIPTION,
    count(*)                                                             AS amount
  FROM PROPERTY
    LEFT JOIN lookups ON lookups.LANG = '_LANG_' AND lookups.agency_id = '_AGENCY_ID_' AND LOOKUPS.LOOKUP_DEF = 'ENTITYSTAT' AND
                         LOOKUPS.code = ENTITY_STATUS_CD
  WHERE ENTITY_STATUS_CHANGE_D > to_date('2000-01-01', 'YYYY-MM-DD') and PROPERTY.agency_id = '_AGENCY_ID_'
  GROUP BY lookups.DESCRIPTION, to_date(to_char(ENTITY_STATUS_CHANGE_D, 'MM/DD/YYYY'), 'MM/DD/YYYY')
)
group by DESCRIPTION,statusDate
order by statusDate