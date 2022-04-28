package za.co.spsi.mdms.common.db.utility;

import za.co.spsi.mdms.common.dao.MeterResultData;
import za.co.spsi.mdms.common.dao.MeterResultDataArray;
import za.co.spsi.toolkit.db.DataSourceDB;
import za.co.spsi.toolkit.db.View;
import za.co.spsi.toolkit.io.IOUtil;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static za.co.spsi.mdms.common.dao.MeterResultData.getNonNull;
import static za.co.spsi.mdms.common.db.utility.IceMeterConsDistrView.DIST_METHOD.PERC;
import static za.co.spsi.mdms.common.db.utility.IceMeterConsDistrView.DIST_METHOD.SQM;

/**
 * Created by johan on 2017/03/30.
 */
public class IceMeterConsDistrView extends View<IceMeterConsDistrView> {

    private static String METER_CONS_DISTR_VIEW_SQL;

    public enum DIST_METHOD {
        FACTOR,PERC,SQM,VAR
    }

    public enum OP_TYPE {
        ADD,SUBTRACT,DISTRIBUTE
    }

    static {
        try {
            METER_CONS_DISTR_VIEW_SQL = new String(IOUtil.readFully(Thread.currentThread().getContextClassLoader().getResourceAsStream("sql/ice_meter_cons_distr_view.sql")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public IceMeter meter = new IceMeter();
    public IceMeterRegister meterRegister = new IceMeterRegister();
    public IceMeterConsDistr iceMeterConsDistr = new IceMeterConsDistr();
    public IceMeterConsDistrLine iceMeterConsDistrLine = new IceMeterConsDistrLine();
    public IceProperty p1 = new IceProperty();
    public IceProperty p2 = new IceProperty();

    public IceMeterConsDistrView() {
        p1.setName("p1");
        p2.setName("p2");
        setSql(METER_CONS_DISTR_VIEW_SQL);
        aliasNames();
    }

    @Override
    public DataSourceDB<IceMeterConsDistrView> getDataSource(Connection dbcon) {
        DataSourceDB<IceMeterConsDistrView> ds = super.getDataSource(dbcon);
        ds.setKeepHistory(true);
        return ds;
    }

    public static class Container extends ArrayList<IceMeterConsDistrView> {

        public boolean equalsView(IceMeterConsDistrView view) {
            return !isEmpty() && get(0).iceMeterConsDistr.ice_meterconsdistr_id.get().equals(view.iceMeterConsDistr.ice_meterconsdistr_id.get());
        }

        public boolean containsM(String vm) {
            return stream().filter(v -> vm.equals(v.meter.iceMeterNumber.get()) && !v.iceMeterConsDistrLine.isVM()).findAny().isPresent();
        }

        public boolean containsMeterN(String mN) {
            return stream().filter(v -> mN.equals(v.meter.iceMeterNumber.get())).findAny().isPresent();
        }

        public boolean containsVM(String vm) {
            return stream().filter(v -> vm.equals(v.meter.iceMeterNumber.get()) && v.iceMeterConsDistrLine.isVM()).findAny().isPresent();
        }

        private void update(IceMeterConsDistrView v, MeterResultData reading,Map<String,MeterResultDataArray> meterData) {
            if (meterData.containsKey(v.meter.iceMeterNumber.get()) && meterData.get(v.meter.iceMeterNumber.get()) != null) {
                Optional<MeterResultData> result = meterData.get(v.meter.iceMeterNumber.get()).stream().filter(r -> r.getEntryTime().equals(reading.getEntryTime())).findFirst();
                if (result.isPresent()) {
                    MeterResultData.refFields.stream().forEach(f -> {
                        try {
                            if (f.get(result.get()) != null && f.getType().equals(Double.class)) {
                                if (PERC.name().equals(v.iceMeterConsDistr.ice_distributionmethod.get())) {
                                    f.set(reading, getNonNull((Double) f.get(reading)) + getNonNull((Double) f.get(reading)) * (v.iceMeterConsDistrLine.ice_distributionvalue.get() / 100.0));
                                } else if (SQM.name().equals(v.iceMeterConsDistr.ice_distributionmethod.get()) &&
                                        getNonNull(v.p1.iceOccupiedOrCoveredArea.get()) > 0 && getNonNull(v.p2.iceOccupiedOrCoveredArea.get()) > 0) {
                                    // assume that we use the occupied area to perform this calc
                                    f.set(reading, getNonNull((Double) f.get(reading)) + getNonNull((Double) f.get(reading)) * (getNonNull(v.p2.iceOccupiedOrCoveredArea.get()) / getNonNull(v.p1.iceOccupiedOrCoveredArea.get())));
                                }
                            }
                        } catch (IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }
        }

        public void update(MeterResultData reading,Map<String,MeterResultDataArray> meterData) {
            stream().forEach(c -> update(c,reading,meterData));
        }

        /**
         *
         * @return all the non vm's
         */
        public List<String> getMeters() {
            return stream().filter(p -> !p.iceMeterConsDistrLine.isVM()).map(f -> f.meter.iceMeterNumber.get()).distinct().
                    collect(Collectors.toCollection(ArrayList::new));
        }

        public List<String> getVMRegisters(String vm) {
            return stream().filter(p -> p.meter.iceMeterNumber.get().equals(vm)).map(f -> f.meterRegister.meterRegister.get()).distinct().
                    collect(Collectors.toCollection(ArrayList::new));
        }

        public List<String> getVM() {
            return stream().filter(v -> v.iceMeterConsDistrLine.isVM()).map(l -> l.meter.iceMeterNumber.get()).collect(Collectors.toCollection(ArrayList::new));
        }
    }

    public static class ContainerList extends ArrayList<Container> {

        public void add(IceMeterConsDistrView view) {
            if (isEmpty() || !get(size()-1).equalsView(view)) {
                add(new Container());
            }
            get(size()-1).add(view);
        }

        public ContainerList filterOnMeterN(String vm) {
            return stream().filter(c -> c.containsMeterN(vm)).collect(Collectors.toCollection(ContainerList::new));
        }

        public List<String> getVMRegisters() {
            return stream().map(p -> p.getMeters()).flatMap(List::stream).collect(Collectors.toList());
        }

        public List<String> getMeters() {
            return stream().map(p -> p.getMeters()).flatMap(List::stream).collect(Collectors.toList());
        }

        public List<String> getVM() {
            return stream().map(c -> c.getVM()).flatMap(List::stream).collect(Collectors.toList());
        }

    }

    public static ContainerList getGroups(Connection connection) {
        ContainerList list = new ContainerList();
        for (IceMeterConsDistrView v : new IceMeterConsDistrView().getDataSource(connection)) {
            list.add(v);
        }
        return list;
    }

    public static void main(String args[]) throws Exception {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        Connection connection = DriverManager.getConnection("jdbc:oracle:thin:@//dboradev.spsi.co.za:1521/dev","ICEUTILOPECPROD","ICEUTILOPECPROD");

        ContainerList list = getGroups(connection);
        ContainerList l1 = list.filterOnMeterN("VM-C/A--Unit, UnitNo: Ground and 1st Floor D, Wend");
        List<String> meters = l1.getMeters();
        // get the last meter reading from the VM

        ContainerList ls = list.stream().filter(c -> c.get(0).meter.iceMeterNumber.get().startsWith("ELON")).collect(Collectors.toCollection(ContainerList::new));
        System.out.print(list);
    }

}