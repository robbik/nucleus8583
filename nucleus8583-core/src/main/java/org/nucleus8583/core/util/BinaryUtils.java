package org.nucleus8583.core.util;

public abstract class BinaryUtils {

    private static final char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    public static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        int len = bytes.length;

        for (int i = 0; i < len; ++i) {
            sb.append(HEX[(bytes[i] & 0xF0) >> 4]);
            sb.append(HEX[bytes[i] & 0x0F]);
        }

        return sb.toString();
    }
}
