package za.co.spsi.mdms.common.generator.msg;

import za.co.spsi.toolkit.entity.Entity;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.util.StringList;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/*
02/08/2017-09:53:56
Sig=98%  AT=0
FW=7.47A
SN=170101-30183
PWR=On  BT=14.3V
IN=0
OUT=0
 */
public class StatusMsg extends Entity implements GeneratorMessage<StatusMsg> {

    public static final String DATE_FORMAT = "dd/MM/yyyy-HH:mm:ss";
    private static SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

    public Field<Timestamp> dateTime = new Field<>(this);
    public Field<String> gsmSignal = new Field<>(this);
    public Field<String> at = new Field<>(this);
    public Field<String> firmwareVersion = new Field<>(this);
    public Field<String> serialNo = new Field<>(this);
    public Field<String> mainsPower = new Field<>(this);
    public Field<String> batteryPwer = new Field<>(this);
    public Field<String> input = new Field<>(this);
    public Field<String> output = new Field<>(this);

    public StatusMsg() {
        dateTime.setDateFormat(DATE_FORMAT);
    }


    public Timestamp getReceiveTime() throws ParseException {
        return dateTime.get();
    }

    public boolean isOn() {
        return input.get().equalsIgnoreCase("1");
    }

    public static StatusMsg get(String source) {
        return new StatusMsg().init(source);
    }

    @Override
    public StatusMsg init(String source) {
        AtomicInteger idx = new AtomicInteger(0);
        Arrays.stream(source.split("  ")).forEach(s -> getFields().get(idx.getAndIncrement()).setSerial(
                s.indexOf("=")!=-1?s.substring(s.indexOf("=")+1):s));
        return this;
    }

    public boolean match(String source) {
        StringList sl = new StringList().readLines(source);
        if (sl.size() > 0) {
            try {
                dateFormat.parse(sl.get(0));
                return true;
            } catch (ParseException e) {
            }
        }
        return false;
    }

    public Timestamp getReceiveTime(Timestamp received) {
        return dateTime.get();
    }

    @Override
    public String getSerialNo() {
        return serialNo.get();
    }

    public static void main(String args[]) throws Exception {
        System.out.println(new StatusMsg().init("25/08/2017-16:07:05  Sig=100%  AT=0  FW=7.47A  SN=170101-30183  PWR=On  BT=14.3V  IN=1  OUT=0  ").isOn());

    }

}