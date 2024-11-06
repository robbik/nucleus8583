package org.nucleus8583.core.util;

public final class BinaryHelper {

    private static final char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    private BinaryHelper() {
        // do nothing
    }

    public static int hex2int(char ichar) {
        return switch (ichar) {
            case '0' ->
                // 0000
                    0;
            case '1' ->
                // 0001
                    1;
            case '2' ->
                // 0010
                    2;
            case '3' ->
                // 0011
                    3;
            case '4' ->
                // 0100
                    4;
            case '5' ->
                // 0101
                    5;
            case '6' ->
                // 0110
                    6;
            case '7' ->
                // 0111
                    7;
            case '8' ->
                // 1000
                    8;
            case '9' ->
                // 1001
                    9;
            case 'A' ->
                // 1010
                    10;
            case 'B' ->
                // 1011
                    11;
            case 'C' ->
                // 1100
                    12;
            case 'D' ->
                // 1101
                    13;
            case 'E' ->
                // 1110
                    14;
            case 'F' ->
                // 1111
                    15;
            default -> throw new IllegalArgumentException("character " + ichar + " is not numeric");
        };

    }

    public static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        int len = bytes.length;

        for (int i = 0; i < len; ++i) {
            sb.append(HEX[(bytes[i] & 0xF0) >> 4]);
            sb.append(HEX[bytes[i] & 0x0F]);
        }

        return sb.toString();
    }

    public static byte[] toBytes(String hex) {
        byte[] b = new byte[hex.length() >> 1];

        for (int i = 0, j = 0; i < b.length; ++i, j += 2) {
            b[i] = (byte) (((hex2int(hex.charAt(j)) << 4) | hex2int(hex.charAt(j + 1))) & 0xFF);
        }

        return b;
    }
}
