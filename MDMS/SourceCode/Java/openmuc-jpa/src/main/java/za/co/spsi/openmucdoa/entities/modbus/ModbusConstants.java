package za.co.spsi.openmucdoa.entities.modbus;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class ModbusConstants {

    public enum DataType
    {
        BOOLEAN, INT16, UINT16, INT32, UINT32, LONG, FLOAT, DOUBLE, BYTEARRAY;
    }

    public enum ChannelType
    {
        DOUBLE, FLOAT, LONG, INTEGER, SHORT, BYTE, BOOLEAN, BYTE_ARRAY, STRING;
    }

    public enum PrimaryTable
    {
        COILS, DISCRETE_INPUTS, INPUT_REGISTERS, HOLDING_REGISTERS;
    }

    public ModbusConstants() {

    }

}
