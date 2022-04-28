package za.co.spsi.mdms.common.db.utility;

import lombok.Data;

import java.util.Date;

@Data
public class IceTOURow {

        public Integer ICE_TARRIFFSCHEDULE_ID;

        public Integer AD_CLIENT_ID;

        public Integer AD_ORG_ID;

        public String SCHEDULENAME;

        public Date VALID_FROM;

        public String TIMEOFUSE;

        public String DAYOFWEEK;

        public Date STARTTIME;

        public Date ENDTIME;

}
