package za.co.spsi.mdms.common.db.utility;

import lombok.Data;

import java.util.Date;

/**
 * Created by Arno Combrinck 2021-06-07
 */
@Data
public class IceTimeOfUseHistRow {

    public Integer mPriceListId;

    public Integer mPriceListVersionId;

    public String plvName;

    public Date plvValidFrom;

    public Date startTime;

    public Date endTime;

    public String dowName;

}
