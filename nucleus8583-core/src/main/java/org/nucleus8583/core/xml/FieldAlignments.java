package org.nucleus8583.core.xml;

public enum FieldAlignments {
    LEFT, RIGHT, NONE;

    public char symbolicValue() {
        if (this == LEFT) {
            return 'l';
        }
        if (this == RIGHT) {
            return 'r';
        }
        return 'n';
    }

    public static FieldAlignments enumValueOf(String str) {
        if ("left".equalsIgnoreCase(str)) {
            return LEFT;
        }

        if ("right".equalsIgnoreCase(str)) {
            return RIGHT;
        }

        if ("none".equalsIgnoreCase(str)) {
            return NONE;
        }

        return null;
    }
}
