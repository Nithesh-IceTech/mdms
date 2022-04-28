package za.co.spsi.mdms.web.gui.layout;

import za.co.spsi.toolkit.db.drivers.Driver;
import za.co.spsi.toolkit.db.drivers.DriverFactory;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MeterFilterHelper {

    public static DateTimeFormatter DAY_FORMAT = DateTimeFormatter.ofPattern("yyMMdd");

    public static List<String[]> getFilters(String tableName, String fieldName) {
        Driver driver = DriverFactory.getDriver();
        Boolean isOracle = driver.getClass().getName().contains("Oracle");
        String subQueryAlias = isOracle ? "" : "meter_exists";
        List<String[]> values = new ArrayList<>();
        values.add(new String[]{"Comms today. Yes", "Communicated today",
                String.format("select * from kamstrup_meter where exists " +
                                "(select * from meter_reading where kam_meter_id = kamstrup_meter.meter_id and entry_day = %s) ",
                        LocalDate.now().format(DAY_FORMAT) )
        });
        values.add(new String[]{"Comms today. No", "Did not communicate today",
                String.format("select * from kamstrup_meter where not exists " +
                                "(select * from meter_reading where kam_meter_id = kamstrup_meter.meter_id and entry_day = %s) ",
                        LocalDate.now().format(DAY_FORMAT) )
        });
        values.add(new String[]{"Comms yesterday. Yes", "Communicated yesterday",
                String.format("select * from kamstrup_meter where exists " +
                                "(select * from meter_reading where kam_meter_id = kamstrup_meter.meter_id and entry_day = %s) ",
                        LocalDate.now().minusDays(1).format(DAY_FORMAT) )
        });
        values.add(new String[]{"Comms yesterday. No", "Did not communicate yesterday",
                String.format("select * from kamstrup_meter where not exists " +
                                "(select * from meter_reading where kam_meter_id = kamstrup_meter.meter_id and entry_day = %s) ",
                        LocalDate.now().minusDays(1).format(DAY_FORMAT) )
        });
        values.add(new String[]{"Comms this week. Yes", "Communicated this week",
                String.format("select * from kamstrup_meter where exists " +
                                "(select * from meter_reading where kam_meter_id = kamstrup_meter.meter_id and entry_day >= %s and entry_day < %s ) ",
                        LocalDate.now().with(DayOfWeek.MONDAY).format(DAY_FORMAT),
                        LocalDate.now().with(DayOfWeek.MONDAY).plusDays(7).format(DAY_FORMAT) )
        });
        values.add(new String[]{"Comms this week. No", "Did not communicate this week",
                String.format("select * from kamstrup_meter where not exists " +
                                "(select * from meter_reading where kam_meter_id = kamstrup_meter.meter_id and entry_day >= %s and entry_day < %s ) ",
                        LocalDate.now().with(DayOfWeek.MONDAY).format(DAY_FORMAT),
                        LocalDate.now().with(DayOfWeek.MONDAY).plusDays(7).format(DAY_FORMAT) )
        });
        values.add(new String[]{"Comms this month. Yes", "Communicated this month",
                String.format("select * from kamstrup_meter where exists " +
                                "(select * from meter_reading where kam_meter_id = kamstrup_meter.meter_id and entry_day >= %s and entry_day < %s) ",
                        LocalDate.now().withDayOfMonth(1).format(DAY_FORMAT),
                        LocalDate.now().withDayOfMonth(1).plusMonths(1).format(DAY_FORMAT) )
        });
        values.add(new String[]{"Comms this month. No", "Did not communicate this month",
                String.format("select * from kamstrup_meter where not exists " +
                                "(select * from meter_reading where kam_meter_id = kamstrup_meter.meter_id and entry_day >= %s and entry_day < %s) ",
                        LocalDate.now().withDayOfMonth(1).format(DAY_FORMAT),
                        LocalDate.now().withDayOfMonth(1).plusMonths(1).format(DAY_FORMAT) )
        });
        values.add(new String[]{"Comms this year. Yes", "Communicated this year",
                String.format("select * from kamstrup_meter where not exists " +
                                "(select * from meter_reading where kam_meter_id = kamstrup_meter.meter_id and entry_day >= %s and entry_day < %s) ",
                        LocalDate.now().withDayOfYear(1).format(DAY_FORMAT),
                        LocalDate.now().withDayOfYear(1).plusYears(1).format(DAY_FORMAT) )
        });
        values.add(new String[]{"Comms this year. No", "Did not communicate this year",
                String.format("select * from kamstrup_meter where not exists " +
                                "(select * from meter_reading where kam_meter_id = kamstrup_meter.meter_id and entry_day >= %s and entry_day < %s) ",
                        LocalDate.now().withDayOfYear(1).format(DAY_FORMAT),
                        LocalDate.now().withDayOfYear(1).plusYears(1).format(DAY_FORMAT) )
        });
        for (String[] v : values) {
            v[2] = v[2].replace("kamstrup_meter", tableName).replace("kam_meter_id", fieldName);
        }
        return values;
    }

    public String[][] getFilters() {
        List<String[]> values= MeterFilterHelper.getFilters("kamstrup_meter","kam_meter_id");
        values.add(0,new String[]{"Unallocated", "Not allocated to any group","select * from kamstrup_meter where kamstrup_meter.group_id is null"});
        return values.toArray(new String[][]{});
    }

    public static void main(String args[]) throws Exception {
        new MeterFilterHelper().getFilters();
    }
}
