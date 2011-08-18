package org.nucleus8583.core.xml;

public class FieldDefinition {

    public static final FieldDefinition FIELD_0;

    public static final FieldDefinition FIELD_1;

    public static final FieldDefinition FIELD_65;

    static {
        FIELD_0 = new FieldDefinition();
        FieldDefinition.FIELD_0.id = 0;
        FieldDefinition.FIELD_0.type = "an";
        FieldDefinition.FIELD_0.length = 4;
        FieldDefinition.FIELD_0.align = FieldAlignments.NONE;
        FieldDefinition.FIELD_0.padWith = " ";
        FieldDefinition.FIELD_0.emptyValue = "";

        FIELD_1 = new FieldDefinition();
        FieldDefinition.FIELD_1.id = 1;
        FieldDefinition.FIELD_1.type = "bitmap";
        FieldDefinition.FIELD_1.length = 16;
        FieldDefinition.FIELD_1.align = FieldAlignments.NONE;
        FieldDefinition.FIELD_1.padWith = null;
        FieldDefinition.FIELD_1.emptyValue = null;

        FIELD_65 = new FieldDefinition();
        FieldDefinition.FIELD_65.id = 65;
        FieldDefinition.FIELD_65.type = "bitmap";
        FieldDefinition.FIELD_65.length = 8;
        FieldDefinition.FIELD_65.align = FieldAlignments.NONE;
        FieldDefinition.FIELD_65.padWith = null;
        FieldDefinition.FIELD_65.emptyValue = null;
    }

    private int id;

    private String type;

    private int length;

    private FieldAlignments align;

    private String padWith;

    private String emptyValue;

    public FieldDefinition() {
        length = 0;

        align = null;
        padWith = null;

        emptyValue = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public FieldAlignments getAlign() {
        return align;
    }

    public void setAlign(FieldAlignments align) {
        this.align = align;
    }

    public String getPadWith() {
        return padWith;
    }

    public void setPadWith(String padWith) {
        this.padWith = padWith;
    }

    public String getEmptyValue() {
        return emptyValue;
    }

    public void setEmptyValue(String emptyValue) {
        this.emptyValue = emptyValue;
    }
}
