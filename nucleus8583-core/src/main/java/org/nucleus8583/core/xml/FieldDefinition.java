package org.nucleus8583.core.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "iso-field", namespace = "http://www.nucleus8583.org/schema/iso-message")
@XmlAccessorType(XmlAccessType.FIELD)
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

	@XmlAttribute(name = "id", required = true)
	private int id;

	@XmlAttribute(name = "type", required = true)
	private String type;

	@XmlAttribute(name = "length")
	private int length;

	@XmlAttribute(name = "align")
	private FieldAlignments align;

	@XmlAttribute(name = "pad-with")
	private String padWith;

	@XmlAttribute(name = "empty-value")
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

	public String getType() {
		return type;
	}

	public int getLength() {
		return length;
	}

	public FieldAlignments getAlign() {
		return align;
	}

	public String getPadWith() {
		return padWith;
	}

	public String getEmptyValue() {
		return emptyValue;
	}
}
