package org.nucleus8583.core.field.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.nucleus8583.core.util.Base16Padder;
import org.nucleus8583.core.util.StringUtils;
import org.nucleus8583.core.xml.FieldAlignments;
import org.nucleus8583.core.xml.FieldDefinition;

public class Base16Binary extends FieldType {
    private static final long serialVersionUID = -5615324004502124085L;

    private int length;

    private Base16Padder padder;

    public Base16Binary(FieldDefinition def, FieldAlignments defaultAlign, String defaultPadWith,
            String defaultEmptyValue) {
        super(def, defaultAlign, defaultPadWith, defaultEmptyValue);

        if (def.getLength() <= 0) {
            throw new IllegalArgumentException("length must be greater than zero");
        }

        padder = new Base16Padder();
        padder.setLength(def.getLength());

        length = def.getLength();

        if (def.getAlign() == null) {
            if (defaultAlign == null) {
                throw new IllegalArgumentException("alignment required");
            }

            padder.setAlign(defaultAlign);
        } else {
            padder.setAlign(def.getAlign());
        }

        if (padder.getAlign() == FieldAlignments.NONE) {
            padder.setPadWith((byte) 0);
        } else {
            if (StringUtils.isEmpty(def.getPadWith())) {
                if (StringUtils.isEmpty(defaultPadWith)) {
                    throw new IllegalArgumentException("pad-with required");
                }

                padder.setPadWith(defaultPadWith);
            } else {
                padder.setPadWith(def.getPadWith());
            }
        }

        if (def.getEmptyValue() == null) {
            if (defaultEmptyValue == null) {
                padder.setEmptyValue(new byte[0]);
            } else {
                padder.setEmptyValue(defaultEmptyValue);
            }
        } else {
            padder.setEmptyValue(def.getEmptyValue());
        }

        padder.initialize();
    }

    @Override
    public boolean isBinary() {
        return true;
    }

    @Override
    public byte[] readBinary(InputStream in) throws IOException {
        return padder.unpad(in);
    }

    @Override
    public void read(InputStream in, byte[] value, int off, int len) throws IOException {
        padder.unpad(in, value, off, len);
    }

    @Override
    public void write(OutputStream out, byte[] value) throws IOException {
        int vlen = value.length;
        if (vlen != length) {
            throw new IllegalArgumentException("value length of field #" + id + " is not equals to " + length
                    + ", actual is " + vlen);
        }

        padder.pad(out, value, 0, vlen);
    }

    @Override
    public void write(OutputStream out, byte[] value, int off, int len) throws IOException {
        padder.pad(out, value, off, len);
    }
}
