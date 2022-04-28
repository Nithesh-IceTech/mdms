package za.co.spsi.mdms.kamstrup.services.meter;

import za.co.spsi.mdms.io.kamstrup.RestHelper;
import za.co.spsi.mdms.kamstrup.db.KamstrupMeterEntity;
import za.co.spsi.mdms.kamstrup.services.PagingService;
import za.co.spsi.mdms.kamstrup.services.meter.domain.Meter;
import za.co.spsi.mdms.kamstrup.services.meter.domain.MeterDetail;
import za.co.spsi.mdms.kamstrup.services.meter.domain.Meters;
import za.co.spsi.mdms.kamstrup.services.meter.domain.Profile;
import za.co.spsi.toolkit.db.DataSourceDB;

import javax.annotation.Resource;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import java.util.List;
import java.util.logging.Logger;

import static za.co.spsi.mdms.kamstrup.db.KamstrupMeterEntity.findMeterByRef;


/**
 * Created by jaspervdb on 2016/10/12.
 */
@Dependent
public class KamstrupMeterService {

    public static final Logger TAG = Logger.getLogger(KamstrupMeterService.class.getName());

    @Inject
    private PagingService<Meters> pagingService;

    public PagingService<Meters> getPagingService() {
        return pagingService;
    }

    @Resource(mappedName = "java:/jdbc/mdms")
    private javax.sql.DataSource dataSource;

    @Inject
    private RestHelper restHelper;

    private int syncAllCnt = 0;

    public List<Meters> getMeters() {
        List<Meters> meters = pagingService.getObjects("meters", Meters.class);
        return meters;
    }

    public MeterDetail getDetail(Meter meter) {
        return pagingService.get(MeterDetail.class, meter.ref);
    }

    public Profile getProfile(MeterDetail detail) {
        return pagingService.get(Profile.class, detail.profileRef.ref);
    }

    public void updateMeter(MeterDao meter, boolean reSync,boolean updateRegisters) {
        KamstrupMeterEntity meterEntity = findMeterByRef(dataSource,meter.getMeter().ref);
        if (meterEntity == null || reSync) {
            meterEntity = meterEntity == null ? new KamstrupMeterEntity() : meterEntity;

            KamstrupMeterEntity clone = ((KamstrupMeterEntity) meterEntity.clone()).init(meter.getDetail(pagingService));
            if (meterEntity.meterId.get() == null || !clone.equalsEntity(meterEntity)) {
                meterEntity = DataSourceDB.set(dataSource, clone);
                if (updateRegisters) {
                    meterEntity.updateRegisters(dataSource, meter.getRegisters(pagingService));
                }
            }

        }
    }

    public void updateMeters(List<Meters> meterList, boolean reSync,boolean updateRegisters) {
        MeterDaoList meters = new MeterDaoList(meterList).getActive();
        meters.stream().forEach(m -> updateMeter(m, reSync,updateRegisters));
    }

    public void updateMeters(boolean reSync,boolean updateRegisters) {
        updateMeters(getMeters(), reSync,updateRegisters);
    }

}
