package za.co.spsi.mdms.common.db.utility;

import za.co.spsi.toolkit.db.EntityDB;
import za.co.spsi.toolkit.db.ano.Column;
import za.co.spsi.toolkit.db.ano.Table;
import za.co.spsi.toolkit.entity.Field;

import java.sql.Timestamp;

/**
 * Created by jaspervdbijl on 2017/03/29.
 */
public class IceMeterReadingList extends EntityDB {

    @Column(name = "ICE_METERREADINGLIST_ID")
    public Field<Integer> meterReadingListId = new Field<>(this);

    @Column(name = "ICE_READINGLISTNOTES")
    public Field<String> notes = new Field<>(this);

    @Column(name = "NAME")
    public Field<String> name = new Field<>(this);

    @Column(name = "ICE_METER_READING_DATE")
    public Field<Timestamp> meterReadingDate = new Field<>(this);

    @Column(name = "ICE_CYCLESTARTDATE")
    public Field<Timestamp> cycleStartDate = new Field<>(this);

    @Column(name = "ICE_MeterReadingListStatus")
    public Field<String> readingListStatus = new Field<>(this);

    public IceMeterReadingList() {
        super("ICE_METERREADINGLIST");
    }

    @Table(version = 0)
    public static class TempCopy extends EntityDB {

        @Column(name = "ICE_METERREADINGLIST_ID")
        public Field<Integer> meterReadingListId = new Field<>(this);

        public TempCopy() {
            super("pec_meter_reading_list_copy");
        }

        public TempCopy(String name) {
            super(name);
        }
    }

    @Table(version = 0)
    public static class Delete extends EntityDB {

        @Column(name = "METER_READING_LIST_ID", size = 50)
        public Field<String> meterReadingListId = new Field<>(this);

        public Delete() {
            super("pec_meter_reading_list_delete");
        }

    }
}

/*

INSERT INTO MZICEUTILODEV.ICE_METERREADINGLIST (AD_CLIENT_ID, AD_ORG_ID, CREATED, CREATEDBY, ICE_METERREADINGLIST_ID, ICE_METERREADINGLIST_UU,
ICE_READINGLISTNOTES, ICE_READINGSEQ, ISACTIVE, NAME, UPDATED, UPDATEDBY, VALUE, ICE_METER_READING_DATE, ICE_METERREADING_ROUTE_ID, ICE_METERREADINGLISTSTATUS, ACCEPT, CONTACTNAME, ICE_BUSINESS_TELEPHONE_NUMBER, ICE_NUMBEROFDAYS, ICE_CYCLESTARTDATE) VALUES (1000007, 2010159, TO_DATE('2017-02-17 13:22:36', 'YYYY-MM-DD HH24:MI:SS'), 1000010, 1000000, 'e2132b32-622b-4dda-8f88-a5e21b962ff2', null, null, 'Y', 'MRL - 28/02/2017', TO_DATE('2017-03-24 12:04:20', 'YYYY-MM-DD HH24:MI:SS'), 1000010, '1000000', TO_DATE('2017-02-28 00:00:00', 'YYYY-MM-DD HH24:MI:SS'), 1000000, 'IP', null, 'ASD', '2233444222', 27, TO_DATE('2017-03-01 00:00:00', 'YYYY-MM-DD HH24:MI:SS'));

 */
