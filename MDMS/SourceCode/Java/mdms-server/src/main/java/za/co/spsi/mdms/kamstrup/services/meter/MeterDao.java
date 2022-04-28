package za.co.spsi.mdms.kamstrup.services.meter;

import za.co.spsi.mdms.kamstrup.services.PagingService;
import za.co.spsi.mdms.kamstrup.services.meter.domain.Meter;
import za.co.spsi.mdms.kamstrup.services.meter.domain.MeterDetail;
import za.co.spsi.mdms.kamstrup.services.meter.domain.Profile;
import za.co.spsi.mdms.kamstrup.services.meter.domain.Register;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by jaspervdb on 2016/10/13.
 */
public class MeterDao {


    private Meter meter;
    private MeterDetail detail;
    private List<MeterRegister> registers;

    public MeterDao(Meter meter) {
        this.meter = meter;
    }

    public Meter getMeter() {
        return meter;
    }

    public MeterDetail getDetail(PagingService pagingService) {
        if (detail == null) {
            detail = (MeterDetail) pagingService.get(MeterDetail.class,meter.ref);
        }
        return detail;
    }

    public List<MeterRegister> getRegisters(PagingService pagingService) {
        if (registers == null) {
            registers = new ArrayList<>();
            Profile profile = (Profile) pagingService.get(Profile.class,
                    getDetail(pagingService).profileRef.ref);
            for (Register register : profile.registers.registers) {
                registers.add(new MeterRegister(register,profile));
            }
        }
        return registers;
    }

    public static class MeterRegister {
        private Register register;
        private Profile profile;
        private boolean autoColllect = false;

        public MeterRegister(Register register,Profile profile) {
            this.register = register;
            this.profile = profile;
            if (profile.autoCollection != null && profile.autoCollection.registers != null &&
                    profile.autoCollection.registers.registers != null) {
                Arrays.asList(profile.autoCollection.registers.registers).stream().forEach(
                        r -> autoColllect = autoColllect || r.id.equals(register.id));
            }
        }

        public Register getRegister() {
            return register;
        }

        public boolean isAutoColllect() {
            return autoColllect;
        }

    }
}
