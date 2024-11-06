package org.nucleus8583.core.type;

public enum Alignment {
    TRIMMED_LEFT, TRIMMED_RIGHT, NONE, UNTRIMMED_LEFT, UNTRIMMED_RIGHT;

    public char symbolicValue() {
        return switch (this) {
            case TRIMMED_LEFT -> 'l';
            case TRIMMED_RIGHT -> 'r';
            case UNTRIMMED_LEFT -> 'L';
            case UNTRIMMED_RIGHT -> 'R';
            default -> 'n';
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case TRIMMED_LEFT -> "left";
            case TRIMMED_RIGHT -> "right";
            case UNTRIMMED_LEFT -> "uleft";
            case UNTRIMMED_RIGHT -> "uright";
            case NONE -> "none";
            default -> null;
        };
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
