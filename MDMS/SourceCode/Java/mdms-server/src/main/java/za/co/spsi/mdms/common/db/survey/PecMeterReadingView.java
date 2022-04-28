package za.co.spsi.mdms.common.db.survey;

import za.co.spsi.toolkit.db.View;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.io.IOUtil;
import za.co.spsi.toolkit.util.Assert;
import za.co.spsi.toolkit.util.StringUtils;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.OptionalInt;
import java.util.stream.IntStream;

/**
 * Created by johan on 2017/03/31.
 */
public class PecMeterReadingView extends View<PecMeterReadingView> {

    private static String PEC_METER_READINGS_VIEW_SQL;

    static {
        try {
            PEC_METER_READINGS_VIEW_SQL = new String(IOUtil.readFully(Thread.currentThread().getContextClassLoader().getResourceAsStream("sql/pec_meter_readings_that_has_readings_view.sql")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public PecMeterReadingView init(String meterReadingListID) {
        setSql(PEC_METER_READINGS_VIEW_SQL, meterReadingListID);
        //aliasNames();
        return this;
    }

    public PecMeterReadingEntity pecMeterReading = new PecMeterReadingEntity();
    public PecMeterEntity pecMeter = new PecMeterEntity();
    public PecMeterRegisterEntity pecMeterRegister = new PecMeterRegisterEntity();
    public PecUtilityMeterReadingListEntity pecUtilityMeterReadingList = new PecUtilityMeterReadingListEntity();

    public Field<String> listReferenceId = pecUtilityMeterReadingList.reference_id;
    public Field<String> readingReferenceId = pecMeterReading.reference_id;
    public Field<String> meterReference = pecMeter.meterReference;
    public Field<Double> reading = pecMeterReading.reading;
    public Field<Double> smartReading = pecMeterReading.smartReading;
    public Field<Timestamp> readingDate = pecMeterReading.readingDate;
    public Field<Integer> noReadingReasonCd = pecMeterReading.noReadingReasonCd;

    public PecMeterReadingView() {
        aliasNames();
    }

    public boolean isTouSmartMeterView( PecMeterReadingView view ) {
        boolean isTou = !StringUtils.isEmpty(view.pecMeterRegister.timeOfUseName != null ?
                view.pecMeterRegister.timeOfUseName.get() : null);
        boolean isSmartMeter = view.pecMeter != null ? view.pecMeter.isSmartMeter() : false;
        return isTou && isSmartMeter;
    }

    public PecMeterReadingView merge(PecMeterReadingView view) {

        if( isTouSmartMeterView( view ) ) {
            // IED-3131: If TOU View, consolidate Standard, Peak and Off-peak Usages
            this.smartReading.set(this.smartReading.getNonNull() + view.smartReading.getNonNull());
            this.reading.set(this.pecMeterReading.prevReading1.getNonNull() + this.smartReading.getNonNull());
        } else {
            // Take the max value of the two
            Assert.isTrue(view.readingReferenceId.get().equals(readingReferenceId.get()), "Reading reference mismatch %s %s"
                    , readingReferenceId.toString(), view.readingReferenceId.toString());
            this.reading.set(Math.max(this.reading.getNonNull(),view.reading.getNonNull()));
            this.smartReading.set(Math.max(this.smartReading.getNonNull(), view.smartReading.getNonNull()));
        }

        return this;
    }

    public PecMeterReadingViewList getGroupsByReadingReference(javax.sql.DataSource dataSource) {
        PecMeterReadingViewList list = new PecMeterReadingViewList();
        for (PecMeterReadingView view : getAsList(dataSource)) {
            list.addGrouped(view);
        }
        return list;
    }

    public static class PecMeterReadingViewList extends ArrayList<PecMeterReadingView> {

        public PecMeterReadingViewList() {

        }

        public boolean isTouSmartMeterView( PecMeterReadingView view ) {
            boolean isTou = !StringUtils.isEmpty(view.pecMeterRegister.timeOfUseName != null ?
                    view.pecMeterRegister.timeOfUseName.get() : null);
            boolean isSmartMeter = view.pecMeter != null ? view.pecMeter.isSmartMeter() : false;
            return isTou && isSmartMeter;
        }

        public OptionalInt getIndex(PecMeterReadingView view) {

            return IntStream.range(0, size())
                        .filter(i -> get(i).readingReferenceId.get().equals(view.readingReferenceId.get()))
                        .findAny();
        }

        public boolean containsView(PecMeterReadingView view) {
            return getIndex(view).isPresent();
        }

        public void addGrouped(PecMeterReadingView view) {
            if (containsView(view)) {
                get(getIndex(view).getAsInt()).merge(view);
            } else {
                add(view);
            }
        }

    }

}
