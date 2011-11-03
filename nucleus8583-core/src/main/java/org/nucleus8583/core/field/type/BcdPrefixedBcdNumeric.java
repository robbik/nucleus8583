package org.nucleus8583.core.field.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.nucleus8583.core.util.BcdPadder;
import org.nucleus8583.core.util.BcdPrefixer;
import org.nucleus8583.core.util.StringUtils;
import org.nucleus8583.core.xml.FieldAlignments;
import org.nucleus8583.core.xml.FieldDefinition;

public abstract class BcdPrefixedBcdNumeric extends FieldType {
    private static final long serialVersionUID = -5615324004502124085L;

    private BcdPrefixer prefixer;

    private BcdPadder padder;

    private int maxLength;

    private String emptyValue;

    public BcdPrefixedBcdNumeric(FieldDefinition def, FieldAlignments defaultAlign, String defaultPadWith,
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

        padder = new BcdPadder();

        if (def.getAlign() == null) {
            if (defaultAlign == null) {
                throw new IllegalArgumentException("alignment required");
            }

            padder.setAlign(defaultAlign);
        } else {
            padder.setAlign(def.getAlign());
        }

        if (padder.getAlign() == FieldAlignments.NONE) {
            padder.setPadWith('0');
        } else {
            if (StringUtils.isEmpty(def.getPadWith())) {
                if (StringUtils.isEmpty(defaultPadWith)) {
                    throw new IllegalArgumentException("pad-with required");
                }

                padder.setPadWith(defaultPadWith.charAt(0));
            } else {
                padder.setPadWith(def.getPadWith().charAt(0));
            }
        }
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
        padder.read(in, cbuf, vlen);

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
        padder.write(out, value, vlen);
    }
}
