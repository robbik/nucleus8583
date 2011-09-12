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

    @Override
    public String toString() {
        if (this == LEFT) {
            return "left";
        }

        if (this == RIGHT) {
            return "right";
        }

        if (this == NONE) {
            return "none";
        }

        return null;
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
