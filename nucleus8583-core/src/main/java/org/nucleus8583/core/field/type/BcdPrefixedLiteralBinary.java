package org.nucleus8583.core.field.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.nucleus8583.core.util.BcdPrefixer;
import org.nucleus8583.core.util.LiteralBinaryPadder;
import org.nucleus8583.core.xml.FieldAlignments;
import org.nucleus8583.core.xml.FieldDefinition;

public abstract class BcdPrefixedLiteralBinary extends FieldType {
    private static final long serialVersionUID = -5615324004502124085L;

    private BcdPrefixer prefixer;

    private LiteralBinaryPadder padder;

    private int maxLength;

    public BcdPrefixedLiteralBinary(FieldDefinition def, FieldAlignments defaultAlign, String defaultPadWith,
            String defaultEmptyValue, int prefixLength, int maxLength) {
        super(def, defaultAlign, defaultPadWith, defaultEmptyValue);

        this.maxLength = maxLength;

        prefixer = new BcdPrefixer(prefixLength);

        padder = new LiteralBinaryPadder();
        padder.setLength(maxLength);

        if (def.getAlign() == null) {
            if (defaultAlign == null) {
                throw new IllegalArgumentException("alignment required");
            }

            padder.setAlign(defaultAlign);
        } else {
            padder.setAlign(def.getAlign());
        }

        padder.setPadWith((byte) 0);

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
        int vlen = prefixer.readUint(in);
        if (vlen == 0) {
            return padder.getEmptyValue();
        }

        byte[] bbuf = new byte[vlen];
        padder.read(in, bbuf, 0, vlen);

        return bbuf;
    }

    @Override
    public void write(OutputStream out, byte[] value) throws IOException {
        int vlen = value.length;
        if (vlen > maxLength) {
            throw new IllegalArgumentException("value of field #" + id
                    + " is too long, expected " + maxLength + " but actual is "
                    + vlen);
        }

        prefixer.writeUint(out, vlen);
        padder.write(out, value, 0, vlen);
    }
}
