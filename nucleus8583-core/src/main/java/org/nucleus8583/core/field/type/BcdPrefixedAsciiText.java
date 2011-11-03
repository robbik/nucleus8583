package org.nucleus8583.core.field.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.nucleus8583.core.util.AsciiPadder;
import org.nucleus8583.core.util.BcdPrefixer;
import org.nucleus8583.core.xml.FieldAlignments;
import org.nucleus8583.core.xml.FieldDefinition;

public abstract class BcdPrefixedAsciiText extends FieldType {
    private static final long serialVersionUID = -5615324004502124085L;

    private BcdPrefixer prefixer;

    private AsciiPadder padder;

    private int maxLength;

    private String emptyValue;

    public BcdPrefixedAsciiText(FieldDefinition def, FieldAlignments defaultAlign, String defaultPadWith,
            String defaultEmptyValue, int prefixLength, int maxLength) {
        super(def, defaultAlign, defaultPadWith, defaultEmptyValue);

        this.maxLength = maxLength;

        if (def.getEmptyValue() == null) {
            if (defaultEmptyValue == null) {
                emptyValue = "";
            } else {
                emptyValue = defaultEmptyValue;
            }
        } else {
            emptyValue = def.getEmptyValue();
        }

        prefixer = new BcdPrefixer(prefixLength);

        padder = new AsciiPadder();
    }

    @Override
    public boolean isBinary() {
        return false;
    }

    @Override
    public String readString(InputStream in) throws IOException {
        int vlen = prefixer.readUint(in);
        if (vlen == 0) {
            return emptyValue;
        }

        char[] cbuf = new char[vlen];
        padder.read(in, cbuf, 0, vlen);

        return new String(cbuf);
    }

    @Override
    public void write(OutputStream out, String value) throws IOException {
        int vlen = value.length();
        if (vlen > maxLength) {
            throw new IllegalArgumentException("value of field #" + id + " is too long, expected 0-" + maxLength
                    + " but actual is " + vlen);
        }

        prefixer.writeUint(out, vlen);
        padder.write(out, value, 0, vlen);
    }
}
