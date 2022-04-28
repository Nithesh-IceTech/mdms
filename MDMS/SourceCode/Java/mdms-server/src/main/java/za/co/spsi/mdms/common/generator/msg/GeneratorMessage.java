package za.co.spsi.mdms.common.generator.msg;

import java.sql.Timestamp;

public interface GeneratorMessage<T extends GeneratorMessage> {

    public T init(String source);

    public default boolean match(String source) {
        return false;
    }

    public default Timestamp getReceiveTime(Timestamp received) {
        return received;
    }

    public String getSerialNo();
}
