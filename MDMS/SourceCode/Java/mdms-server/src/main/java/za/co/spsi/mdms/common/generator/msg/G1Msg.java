package za.co.spsi.mdms.common.generator.msg;

import za.co.spsi.toolkit.entity.Entity;
import za.co.spsi.toolkit.entity.Field;
import za.co.spsi.toolkit.util.Assert;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// G1 On 170101-30183 7.47A 12.6 1 86 243 00/00/0000 00:03:40
// G1 On \# \f \b \p \s \a \d \t

public class G1Msg extends Entity implements GeneratorMessage<G1Msg> {

    private static SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public Field<String> header = new Field<>(this);
    public Field<String> status = new Field<>(this);
    public Field<String> serialNo = new Field<>(this);
    public Field<String> firmwareVersion = new Field<>(this);
    public Field<Double> voltage = new Field<>(this);
    public Field<Double> mainsPower = new Field<>(this);
    public Field<Integer> gsmSignal = new Field<>(this);
    public Field<Integer> analogInput = new Field<>(this);
    public Field<String> date = new Field<>(this);
    public Field<String> time = new Field<>(this);

    public boolean isValidDate() {
        return date.get() != null && date.get().indexOf("/") != -1 &&
                date.get().substring(date.get().lastIndexOf("/") + 1).compareTo("2017") >= 0;
    }

    public Timestamp getReceiveTime() throws ParseException {
        return new Timestamp(format.parse(date.get() + " " + time.get()).getTime());
    }

    public boolean isOn() {
        Assert.isTrue("on".equalsIgnoreCase(status.get()) || "off".equalsIgnoreCase(status.get()), "Unexpected state " + status.get());
        return "on".equalsIgnoreCase(status.get());
    }

    // G1 On 170101-30183 7.47A 11.5 1 76 243 00/00/0000 01:45:07&
    public static G1Msg get(String source) {
        G1Msg msg = new G1Msg();
        return msg.init(source);
    }

    @Override
    public G1Msg init(String source) {
        List<String> split = new ArrayList<>();
        Collections.addAll(split, source.split(" "));
        Assert.isTrue(split.size() == getFields().size(), "Invalid format " + source);
        getFields().stream().forEach(f -> f.setSerial(split.remove(0)));
        return this;
    }

    public boolean match(String source) {
        return source.startsWith("G1");
    }

    public Timestamp getReceiveTime(Timestamp received) {
        try {
            return isValidDate()?getReceiveTime():received;
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getSerialNo() {
        return serialNo.get();
    }

    public static void main(String args[]) throws Exception {
        G1Msg g1 = get("G1 On 170101-30183 7.47A 14.3 1 100 243 22/08/2017 14:01:46");

        System.out.println(g1.isValidDate() + " "+ g1.getReceiveTime(new Timestamp(System.currentTimeMillis())));
    }

}