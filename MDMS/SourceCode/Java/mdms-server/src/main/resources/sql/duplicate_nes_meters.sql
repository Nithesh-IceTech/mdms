SELECT DISTINCT (SERIAL_N)
  FROM nes_meter
  WHERE SERIAL_N IS NOT NULL AND SERIAL_N IN (
    SELECT SERIAL_N
    FROM nes_meter
    GROUP BY SERIAL_N
    HAVING count(*) > 1
  )