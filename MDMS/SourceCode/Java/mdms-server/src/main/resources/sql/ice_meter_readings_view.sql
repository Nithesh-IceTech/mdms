SELECT * FROM ICE_MeterReadings
  left join ICE_METERREADINGLISTITEMS_V on
  LEFT JOIN ICE_METERREADINGLIST ON ICE_METERREADINGLIST.ICE_METERREADINGLIST_ID = ICE_MeterReadings.ICE_MeterReadingList_ID
  LEFT JOIN ICE_Entity_Hierarchy hierar ON ICE_MeterReadings.ICE_Meter_ID = hierar.ICE_Meter_ID
  LEFT JOIN ICE_Property ON hierar.ICE_Property_ID = ICE_Property.ICE_Property_ID
  LEFT JOIN ICE_METERREADINGS_HIST_INV_V
    ON ICE_METERREADINGS_HIST_INV_V.latest_meterreadings_id = ICE_MeterReadings.ICE_METERREADINGS_ID
  LEFT JOIN ICE_METER_REGISTER ON ICE_METER_REGISTER.ICE_METER_REGISTER_ID = ICE_MeterReadings.ICE_METER_REGISTER_ID
  LEFT JOIN ICE_METER ON ICE_METER_REGISTER.ICE_METER_ID = ICE_METER.ICE_METER_ID
  LEFT JOIN C_UOM ON C_UOM.C_UOM_ID = ICE_METER_REGISTER.C_UOM_ID
WHERE ICE_MeterReadings.ICE_MeterReadingList_ID IN (SELECT ICE_MeterReadingList_ID
                                                    FROM ICE_MeterReadingList
                                                    WHERE ICE_MeterReadingListStatus = 'AP') AND
      ICE_METERREADINGS_HIST_INV_V.current_meterreadingdate IS NOT NULL

