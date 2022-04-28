package za.co.spsi.toolkit.entity;

import za.co.spsi.toolkit.entity.ano.Bit;
import za.co.spsi.toolkit.util.Assert;

import java.math.BigInteger;
import java.util.Collections;

/**
 * Created by jaspervdbijl on 2017/01/26.
 */
public class EntityBit extends Entity {

    public EntityBit() {
        super(EntityBit.class.getName());
    }

    public EntityBit(String uniqueName) {
        super(uniqueName);
    }

    public EntityBit(String uniqueName, byte data[]) {
        this(uniqueName);
        this.setSerializedAsBit(data);
    }

    public int getSize(Field field) {
        Bit bit = get(field).getAnnotation(Bit.class);
        Assert.isTrue(bit != null || field.getType().equals(Boolean.class), "Field %s must be annotated with Bit", field.getName());
        return bit != null ? bit.size() : 1;
    }

    public void reverse() {
        Collections.reverse(getFields());
    }

    /*
     * validate the field lengths
     */
    public void checkFields() throws IllegalAccessException {
        for (Field field : getFields()) {
            if (getSize(field) == 0
                    || field.getType().equals(Boolean.class) && getSize(field) != 1
                    || field.getType().equals(Byte.class) && getSize(field) > 8
                    || field.getType().equals(Integer.class) && getSize(field) > 16
                    || field.getType().equals(Long.class) && getSize(field) > 32) {
                throw new RuntimeException("Incorrect field length for [" + field.getName() + "] for type [" + field.getType() + "]. SIze [" + getSize(field) + "]");
            }
        }
    }

    /*
     * @return formats the given string builder to required length
     */
    private static void format(StringBuilder sb, int length) {
        if (sb.length() > length) {
            sb.delete(0, sb.length() - length);
        }
        for (; sb.length() < length; sb.insert(0, "0")) {
        }
    }

    /*
     * @retrun formats the given string to required length
     */
    private static String format(String sb, int length) {
        if (sb.length() > length) {
            sb = sb.substring(sb.length() - length);
        }
        for (; sb.length() < length; sb = "0" + sb) {
        }
        return sb;
    }

    /*
     * @return convert the field value to a binary bit string
     */
    public static StringBuilder getAsBinary(EntityBit entity, Field field) {
        StringBuilder builder = new StringBuilder();
        if (field.get() != null) {
            if (field.getType().equals(Long.class)) {
                builder.append(Long.toBinaryString((Long) field.get()));
            } else if (field.getType().equals(Integer.class)) {
                builder.append(Integer.toBinaryString((Integer) field.get()));
            } else if (field.getType().equals(Byte.class)) {
                builder.append(Integer.toBinaryString((Byte) field.get()));
            } else if (field.getType().equals(Boolean.class)) {
                builder.append((Boolean) field.getNonNull() ? "1" : "0");
            } else if (field.getType().equals(String.class) || field.getType().equals(Character.class)) {
                for (char c : field.getSerial().toCharArray()) {
                    builder.append(format(Integer.toBinaryString(c), 8));
                }
            } else if (field.getType().equals(byte[].class)) {
                for (byte b : (byte[]) field.get()) {
                    builder.append(format(Integer.toBinaryString(b), 8));
                }
                format(builder, entity.getSize(field));
            } else {
                throw new UnsupportedOperationException("Type [" + field.getType() + "] not supported for field " + field.getName());
            }
            // ensure field fits
            if (builder.length() <= entity.getSize(field)) {
                format(builder, entity.getSize(field));
                return builder;
            } else {
                throw new RuntimeException("Field [" + field.getName() + "] value [" + field.getSerial() + "] did not fit. Size " + entity.getSize(field));

            }
        } else {
            throw new RuntimeException("Value of field[" + field.getName() + "] is not allowed to be null");
        }
    }

    public void setAsBinary(Field field, String value) {
        if (field.getType().equals(Byte.class)) {
            field.set(Byte.parseByte(value, 2));
        } else if (field.getType().equals(byte[].class)) {
            StringBuilder builder = new StringBuilder();
            builder.append(value);
            format(builder, getSize(field) % 8 == 0 ? (getSize(field) / 8) * 8 : (getSize(field) / 8 + 1) * 8);
            byte data[] = new byte[builder.length() / 8];
            for (int i = 0; i < builder.length() / 8; i++) {
                data[i] = (byte) (Integer.parseInt(builder.substring(i * 8, i * 8 + 8), 2) & 0xff);
            }
            field.set(data);
        } else if (field.getType().equals(Integer.class)) {
            field.set(Integer.parseInt(value, 2));
        } else if (field.getType().equals(Long.class)) {
            field.set(Long.parseLong(value, 2));
        } else if (field.getType().equals(Boolean.class)) {
            field.set(value.equals("1"));
        } else if (field.getType().equals(String.class) | field.getType().equals(Character.class)) {
            // must be text
            field.setSerial(value);
        } else {
            throw new UnsupportedOperationException("Type [" + field.getType() + "] not supported for field " + field.getName());
        }
    }

    public byte[] getSerialAsBit(boolean reverse) {
        if (this.getFields().size() > 0) {
            StringBuilder builder = new StringBuilder();
            for (Field field : this.getFields()) {
                builder.append(getAsBinary(this, field));
            }
            if (reverse) {
                builder.reverse();
            }
            if (builder.length() % 8 == 0) {
                byte buffer[] = new byte[builder.length() / 8];
                for (int i = 0; i < buffer.length; i++) {
                    buffer[i] = (byte) Integer.parseInt(builder.substring(i * 8, i * 8 + 8), 2);
                }
                return buffer;
            } else {
                throw new RuntimeException("Malformed Bit dao. Total length % 8 != 0. Length " + builder.length());
            }
        } else {
            return new byte[]{};
        }
    }

    public boolean setSerializedAsBit(byte buffer[]) {
        return setSerializedAsBit(buffer, false);
    }

    public boolean setSerializedAsBit(byte buffer[], boolean reverse) {
        StringBuilder builder = new StringBuilder();
        for (byte b : buffer) {
            builder.append(format(Integer.toBinaryString(b & 0xff), 8));
        }
        if (reverse) {
            builder.reverse();
        }
        int size = 0;
        for (Field field : this.getFields()) {
            if (getSize(field) > 0 && builder.length() >= size + getSize(field)) {
                try {
                    setAsBinary(field, builder.substring(size, size + getSize(field)));
                    size += getSize(field);
                } catch (Exception ex) {
                    throw new RuntimeException("Error on field " + field.getName(), ex);
                }
            }
        }
        return false;
    }

    public boolean setSerializedAsLong(Long value) {
        return setSerializedAsBit(new BigInteger("" + value).toByteArray(), true);
    }

    public long getSerializedAsLong() {
        return new BigInteger(getSerialAsBit(true)).longValue();
    }

}