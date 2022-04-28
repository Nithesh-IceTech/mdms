package za.co.spsi.toolkit.entity;

import za.co.spsi.toolkit.io.IOUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

/**
 * Created by jaspervdbijl on 2017/01/05.
 */
public class FieldZip extends Field<byte[]> {

    public FieldZip(Entity entity) {
        super(entity);
    }

    public byte[] getInflated() {
        try {
            if (super.get() != null) {
                try (ByteArrayInputStream bis = new ByteArrayInputStream(super.get())) {
                    try (InflaterInputStream fis = new InflaterInputStream(bis)) {
                        return IOUtil.readFully(fis);
                    }
                }
            }
            return null;
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }
    public String getInflatedString() {
        try {
            if (super.get() != null) {
                return new String(getInflated(), "UTF-8");
            } else {
                return null;
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public Entity setAndDeflate(byte data[]) {
        try {
            if (data != null) {
                try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
                    try (DeflaterOutputStream dos = new DeflaterOutputStream(bos)) {
                        dos.write(data);
                    }
                    super.set(bos.toByteArray());
                }
            } else {
                super.set(data);
            }
            return getEntity();
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

}