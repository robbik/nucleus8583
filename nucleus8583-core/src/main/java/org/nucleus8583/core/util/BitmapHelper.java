package org.nucleus8583.core.util;

import java.util.Arrays;

public abstract class BitmapHelper {

    private static int bytesIndex(int bitIndex) {
        return bitIndex >> 3;
    }

    public static byte[] create(int nbits) {
        return new byte[bytesIndex(nbits - 1) + 1];
    }

    public static void set(byte[] bytes, int bitIndex) {
        bytes[bytesIndex(bitIndex)] |= (128 >> (bitIndex & 0x07));
    }

    public static boolean get(byte[] bytes, int bitIndex) {
        return (bytes[bytesIndex(bitIndex)] & (128 >> (bitIndex & 0x07))) != 0;
    }

    public static void clear(byte[] bytes, int bitIndex) {
        bytes[bytesIndex(bitIndex)] &= ~(128 >> (bitIndex & 0x07));
    }

    public static void clear(byte[] bytes) {
        Arrays.fill(bytes, (byte) 0);
    }

    public static boolean isEmpty(byte[] bytes) {
        for (int i = bytes.length - 1; i >= 0; --i) {
            if (bytes[i] != 0) {
                return false;
            }
        }

        return true;
    }

    public static int realBytesInUse(byte[] bytes) {
        for (int i = bytes.length - 1; i >= 0; --i) {
            if (bytes[i] != 0) {
                return i + 1;
            }
        }

        return 0;
    }
}
