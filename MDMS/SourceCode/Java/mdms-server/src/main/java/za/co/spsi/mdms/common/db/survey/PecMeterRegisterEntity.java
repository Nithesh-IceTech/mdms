/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package za.co.spsi.mdms.common.db.survey;

import za.co.spsi.mdms.common.dao.ano.MeterRegister;
import za.co.spsi.mdms.common.db.MeterReadingEntity;
import za.co.spsi.mdms.common.db.utility.IceApprovedMeterReadingsView;
import za.co.spsi.mdms.common.db.utility.IceTimeOfUseView;
import za.co.spsi.toolkit.crud.db.fields.UserIdField;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.EntityRef;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.ForeignKey;
import za.co.spsi.toolkit.db.ano.Id;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.db.fields.Index;
import za.co.spsi.toolkit.entity.Exportable;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.entity.ano.Audit;
import za.co.spsi.toolkit.util.Assert;

import java.sql.Connection;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.DayOfWeek.*;
import static za.co.spsi.mdms.common.db.MeterReadingEntity.METER_ENTRY_FIELDS;
import static za.co.spsi.toolkit.util.Util.getLocalDateTime;

/**
 * @author jaspervdb
 */

@Table(version = 2)
@Audit
public class PecMeterRegisterEntity extends EntityDB {

    private static MeterReadingEntity READING_ENTITY = new MeterReadingEntity();

    @Id(uuid = true, name = "MR_PK")
    @Column(name = "METER_REGISTER_ID", size = 50)
    public Field<String> meterRegisterId = new Field<>(this);

    @Column(name = "METER_ID", size = 50)
    @ForeignKey(table = PecMeterEntity.class, name = "MRREG_METER_ID", onDeleteAction = ForeignKey.Action.Cascade)
    public Field<String> meterId = new Field<>(this);

    @Column(name = "METER_READING_LIST_ID", size = 50)
    @ForeignKey(table = PecMeterReadingListEntity.class, name = "MRR_METER_READING_LIST_ID", onDeleteAction = ForeignKey.Action.Cascade)
    public Field<String> meterReadingListId = new Field<>(this);

    @Column(name = "UTILITY_METER_READING_LIST_ID", size = 50)
    @ForeignKey(table = PecUtilityMeterReadingListEntity.class, name = "MRRU_METER_READING_LIST_ID", onDeleteAction = ForeignKey.Action.SetNull)
    public Field<String> utilityMeterReadingListId = new Field<>(this);

    @Column(name = "AGENCY_ID", size = 8)
    public Field<Integer> agencyId = new Field<>(this);

    @Column(name = "USER_ID", size = 50)
    public UserIdField userId = new UserIdField(this);

    @Column(name = "METER_REGISTER_TYPE_CD", size = 10)
    public Field<String> meterRegisterTypeCd = new Field<>(this);

    public Field<Integer> digits = new Field<>(this);

    @Column(name = "AUTO_RESET_CD", size = 8)
    public Field<String> autoResetCd = new Field<>(this);

    @Column(name = "AUTO_RESET_DAY_CD", size = 8)
    public Field<String> autoResetDayCd = new Field<>(this);

    @Column(name = "READING_TYPE_CD", size = 8)
    public Field<String> readingTypeCd = new Field<>(this);

    @Column(name = "UNIT_OF_MEASURE_CD", size = 8)
    public Field<String> unitOfMeasureCd = new Field<>(this);

    public Field<String> description = new Field<>(this);

    @Column(name = "LOGGER_ID")
    public Field<String> loggerId = new Field<>(this);

    @Column(name = "REGISTER_ID")
    public Field<String> registerId = new Field<>(this);

    @Column(name = "LOAD_PROFILE_NUMBER")
    public Field<Integer> loadProfileNumber = new Field<>(this);

    @Column(name = "CHANNEL_ID")
    public Field<Integer> channelId = new Field<>(this);

    @Column(name = "ACTIVE", size = 3)
    public Field<String> activeCd = new Field<>(this);

    @Column(name = "METER_SEQ")
    public Field<Integer> meterSeq = new Field<>(this);

    // TIME OF USE VALUES

    @Column(name = "TOU_NAME")
    public Field<String> timeOfUseName = new Field<>(this);

    @Column(name = "TOU_START_TIME")
    public Field<Time> timeOfUseStartTime = new Field<>(this);

    @Column(name = "TOU_END_TIME")
    public Field<Time> timeOfUseEndTime = new Field<>(this);

    @Column(name = "TOU_DAY_OF_WEEK_VALUE")
    public Field<Integer> timeOfUseDayOfWeekValue = new Field<>(this);

    @Column(name = "TOU_DAY_OF_WEEK_NAME")
    public Field<String> timeOfUseDayOfWeekName = new Field<>(this);

    @Column(name = "DATETRX")
    public Field<java.sql.Date> dateRx = new Field<>(this);

    public Field<String> reference_id = new Field<>(this);

    @Exportable(name = "pecMeter", forceExport = true)
    public EntityRef<PecMeterEntity> meter = new EntityRef<>(meterId, this);

    @Exportable(name = "pecMeterReading", forceExport = true)
    public EntityRef<PecMeterReadingEntity> meterReading = new EntityRef<>(this);

    public EntityRef<PecMeterReadingListEntity> list = new EntityRef<>(meterReadingListId, this);

    public Index idxMRL = new Index("PEC_REG_MRL",this,meterReadingListId);
    public Index idxMS = new Index("PEC_REG_MS",this,meterSeq);
    public Index idxMe = new Index("PEC_REG_ME",this,meterId);

    public PecMeterRegisterEntity() {
        super("PEC_METER_REGISTER");
    }

    public PecMeterRegisterEntity initRegister(IceApprovedMeterReadingsView view) {
        reference_id.set(view.register.meterRegisterId.get());
        meterRegisterTypeCd.set(view.register.meterRegisterTypeId.get());
        digits.set(view.register.meterDigits.get());
        autoResetCd.set("N".equals(view.register.meterAutoReset.get()) ? "2" : "1");
        autoResetDayCd.set(view.register.autoResetDay.get());
        readingTypeCd.set(view.register.meterRegisterReadingType.get());
        unitOfMeasureCd.set(view.iceOUM.oumId.get());
        description.set(view.register.description.get());
        loggerId.set(view.register.loggerId.get());
        meterSeq.set(view.approved.meterseq.get());
        registerId.set(view.register.meterRegister.get());
        loadProfileNumber.set(view.register.profileNo.get());
        channelId.set(view.register.meterChannelId.get());
        activeCd.set("N".equals(view.register.isActive.get()) ? "2" : "1");

        timeOfUseName.set(view.priceVersionTimeOfUse.name.get());
        timeOfUseStartTime.set(view.priceVersionTimeOfUse.startTime.get());
        timeOfUseEndTime.set(view.priceVersionTimeOfUse.endTime.get());
        timeOfUseDayOfWeekName.set(view.iceDayOfWeek.name.get());
        if (view.iceDayOfWeek.value.get() != null) {
            Assert.isTrue(isNum(view.iceDayOfWeek.value.get()), ""
                    , (e) -> new RuntimeException(String.format("Day of week value %s is not a number", view.iceDayOfWeek.name.get())));
            timeOfUseDayOfWeekValue.set(Integer.parseInt(view.iceDayOfWeek.value.get()));
        }
        dateRx.set(view.priceVersionTimeOfUse.dateTrx.get());
        return this;
    }

    public LocalTime getTouFrom() {
        return timeOfUseStartTime.get() != null ? getLocalDateTime(timeOfUseStartTime.get()).toLocalTime() : null;
    }

    public LocalTime getTouTo() {
        return timeOfUseEndTime.get() != null ? getLocalDateTime(timeOfUseEndTime.get()).toLocalTime() : null;
    }

    /**
     * map 01	Sunday to java DayOfWeek
     *
     * @return
     */
    public List<DayOfWeek> getTouDayOfWeek() {
        return "Weekdays".equalsIgnoreCase(timeOfUseDayOfWeekName.get()) ?
                Arrays.asList(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY)
                : timeOfUseDayOfWeekName.get() != null
                ? Arrays.asList(DayOfWeek.valueOf(timeOfUseDayOfWeekName.get().toUpperCase()))
                : null;
    }

    public static boolean isNum(String num) {
        try {
            Integer.parseInt(num);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public boolean hasTimeOfUse() {
        return timeOfUseStartTime.get() != null && timeOfUseEndTime.get() != null
                || getTouDayOfWeek() != null || dateRx.get() != null;
    }

    public boolean isKva() {
        return registerId.get().matches("1.1.9.6.[0,1,2].255");
    }

    public boolean inTimeOfUse(LocalTime time) {
        return hasTimeOfUse() ?
                IceTimeOfUseView.inTimeOfUse(timeOfUseStartTime.get().toLocalTime(), timeOfUseEndTime.get().toLocalTime(), time) : true;
    }

    public static PecMeterRegisterEntity create(Connection connection, PecMeterEntity meter, PecUtilityMeterReadingListEntity list,
                                                PecMeterReadingListEntity meterList, IceApprovedMeterReadingsView view) {
        PecMeterRegisterEntity register = new PecMeterRegisterEntity();
        register.initRegister(view);
        register.meterId.set(meter.meterId.get());
        register.meterReadingListId.set(meterList.meterReadingListId.get());
        register.utilityMeterReadingListId.set(list.utilityMeterReadingListId.get());
        DataSourceDB.set(connection, register);
        return register;
    }

    private LocalTime getTouStart() {
        return timeOfUseStartTime.get() == null ? LocalTime.of(0, 0, 0) : timeOfUseStartTime.get().toLocalTime();
    }

    private LocalTime getTouEnd() {
        return timeOfUseEndTime.get() == null ? LocalTime.of(23, 59, 59) : timeOfUseEndTime.get().toLocalTime();
    }

    public static String getMappedName(String registerId) {
        List<String> fields =
                METER_ENTRY_FIELDS.stream()
                        .filter(f -> f.getAnnotation(MeterRegister.class) != null
                                && Arrays.stream(f.getAnnotation(MeterRegister.class).value()).anyMatch(s -> s.equals(registerId)))
                        .map(f -> f.getName())
                        .collect(Collectors.toCollection(ArrayList::new));
        Assert.isTrue(!fields.isEmpty(), "Could not find mapping for register %s", registerId);
        return fields.get(0);
    }

    private boolean isTField() {
        return getMappedName().startsWith("t1") || getMappedName().startsWith("t2");
    }

    private boolean isMaxField() {
        return getMappedName().toLowerCase().indexOf("max") != -1;
    }

    public String getMappedName() {
        return getMappedName(registerId.get() == null ? "1.1.1.8.0.255" : registerId.get());
    }

    public boolean isMaxReading() {
        return getMappedName(registerId.get() == null ? "1.1.1.8.0.255" : registerId.get()).contains("Max")
                && !isKva();
    }

    // IED-4974: MDMS Backend (Modem-Based) Generator Testing
    public boolean isGridRegister() { // T1 Register
        return registerId.get().matches("1.1.[1,2,3,4].8.1.255");
    }

    // IED-4974: MDMS Backend (Modem-Based) Generator Testing
    public boolean isGeneratorRegister() { // T2 Register
        return registerId.get().matches("1.1.[1,2,3,4].8.2.255");
    }

    public String getRegisterId() {
        return this.registerId.get();
    }

}
