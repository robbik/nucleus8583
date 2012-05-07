package org.nucleus8583.core.field;

public enum Alignment {
    TRIMMED_LEFT, TRIMMED_RIGHT, NONE, UNTRIMMED_LEFT, UNTRIMMED_RIGHT;

    public char symbolicValue() {
        switch (this) {
        case TRIMMED_LEFT:
            return 'l';
        case TRIMMED_RIGHT:
            return 'r';
        case UNTRIMMED_LEFT:
            return 'L';
        case UNTRIMMED_RIGHT:
            return 'R';
        default:
            return 'n';
        }
    }

    @Override
    public String toString() {
        switch (this) {
        case TRIMMED_LEFT:
            return "left";
        case TRIMMED_RIGHT:
            return "right";
        case UNTRIMMED_LEFT:
            return "uleft";
        case UNTRIMMED_RIGHT:
            return "uright";
        case NONE:
            return "none";
        default:
            return null;
        }
    }

    public static Alignment enumValueOf(String str) {
        if ("left".equalsIgnoreCase(str) || "l".equalsIgnoreCase(str)) {
            return TRIMMED_LEFT;
        }

        if ("right".equalsIgnoreCase(str) || "r".equalsIgnoreCase(str)) {
            return TRIMMED_RIGHT;
        }

        if ("none".equalsIgnoreCase(str) || "n".equalsIgnoreCase(str)) {
            return NONE;
        }

        if ("uleft".equalsIgnoreCase(str) || "ul".equalsIgnoreCase(str)) {
            return UNTRIMMED_LEFT;
        }

        if ("uright".equalsIgnoreCase(str) || "ur".equalsIgnoreCase(str)) {
            return UNTRIMMED_RIGHT;
        }

        return null;
    }

    public static Alignment enumValueOf(char symbolicValue) {
        switch (symbolicValue) {
        case 'l':
            return TRIMMED_LEFT;
        case 'r':
            return TRIMMED_RIGHT;
        case 'L':
            return UNTRIMMED_LEFT;
        case 'R':
            return UNTRIMMED_RIGHT;
        default:
            return NONE;
        }
    }
}
