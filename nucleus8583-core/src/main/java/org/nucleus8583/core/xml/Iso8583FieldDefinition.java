package org.nucleus8583.core.xml;

import java.lang.reflect.Constructor;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.nucleus8583.core.Iso8583Binary;
import org.nucleus8583.core.Iso8583Field;
import org.nucleus8583.core.Iso8583String;
import org.nucleus8583.core.util.FieldTypes;

@XmlRootElement(name = "iso-field", namespace = "http://www.nucleus8583.org/schema/iso-message")
@XmlAccessorType(XmlAccessType.FIELD)
public class Iso8583FieldDefinition {

    public static final Iso8583FieldDefinition FIELD_0;

    public static final Iso8583FieldDefinition FIELD_1;

    public static final Iso8583FieldDefinition FIELD_65;

    static {
        FIELD_0 = new Iso8583FieldDefinition();
        FIELD_0.id = 0;
        FIELD_0.type = "an";
        FIELD_0.lcount = 0;
        FIELD_0.length = 4;
        FIELD_0.align = Iso8583FieldAlignments.NONE;
        FIELD_0.padWith = " ";
        FIELD_0.emptyValue = "";

        FIELD_1 = new Iso8583FieldDefinition();
        FIELD_1.id = 1;
        FIELD_1.type = "b";
        FIELD_1.lcount = 0;
        FIELD_1.length = 16;
        FIELD_1.align = Iso8583FieldAlignments.NONE;
        FIELD_1.padWith = null;
        FIELD_1.emptyValue = null;

        FIELD_65 = new Iso8583FieldDefinition();
        FIELD_65.id = 65;
        FIELD_65.type = "b";
        FIELD_65.lcount = 0;
        FIELD_65.length = 8;
        FIELD_65.align = Iso8583FieldAlignments.NONE;
        FIELD_65.padWith = null;
        FIELD_65.emptyValue = null;
    }

    @XmlAttribute(name = "id", required = true)
	private int id;

	@XmlAttribute(name = "type", required = true)
	private String type;

	@XmlTransient
	private int lcount;

	@XmlAttribute(name = "length")
	private int length;

	@XmlAttribute(name = "align")
	private Iso8583FieldAlignments align;

	@XmlAttribute(name = "pad-with")
	private String padWith;

	@XmlAttribute(name = "empty-value")
	private String emptyValue;

	public Iso8583FieldDefinition() {
		lcount = 0;
		length = 0;

		align = Iso8583FieldAlignments.NONE;
		padWith = " ";

		emptyValue = "";
	}

	private void initLength() {
		type = type.trim();
		if (type.endsWith(".....")) {
			type = type.substring(0, type.length() - 5).trim();

			length = 99999;
			lcount = 5;
		} else if (type.endsWith("....")) {
			type = type.substring(0, type.length() - 4).trim();

			length = 9999;
			lcount = 4;
		} else if (type.endsWith("...")) {
			type = type.substring(0, type.length() - 3).trim();

			length = 999;
			lcount = 3;
		} else if (type.endsWith("..")) {
			type = type.substring(0, type.length() - 2).trim();

			length = 99;
			lcount = 2;
		} else if (type.endsWith(".")) {
			type = type.substring(0, type.length() - 1).trim();

			length = 9;
			lcount = 1;
		} else {
			if (length <= 0) {
				throw new IllegalArgumentException(
						"length must greater than 0, given " + length
								+ " in field with id = " + id);
			}

			lcount = 0;
		}
	}
	
	public int getId() {
		return id;
	}

	public Iso8583Field createField() {
		initLength();

		if ("a".equalsIgnoreCase(type)) {
			return new Iso8583String(id, lcount, length,
					Iso8583FieldAlignments.LEFT.symbolicValue(), ' ', "");
		}

		if ("n".equalsIgnoreCase(type)) {
			return new Iso8583String(id, lcount, length,
					Iso8583FieldAlignments.RIGHT.symbolicValue(), '0', "0");
		}

		if ("s".equalsIgnoreCase(type)) {
			return new Iso8583String(id, lcount, length,
					Iso8583FieldAlignments.LEFT.symbolicValue(), ' ', "");
		}

		if ("an".equalsIgnoreCase(type)) {
			return new Iso8583String(id, lcount, length,
					Iso8583FieldAlignments.LEFT.symbolicValue(), ' ', "");
		}

		if ("as".equalsIgnoreCase(type)) {
			return new Iso8583String(id, lcount, length,
					Iso8583FieldAlignments.LEFT.symbolicValue(), ' ', "");
		}

		if ("ns".equalsIgnoreCase(type)) {
			return new Iso8583String(id, lcount, length,
					Iso8583FieldAlignments.LEFT.symbolicValue(), ' ', "");
		}

		if ("ans".equalsIgnoreCase(type)) {
			return new Iso8583String(id, lcount, length,
					Iso8583FieldAlignments.LEFT.symbolicValue(), ' ', "");
		}

		if ("b".equalsIgnoreCase(type)) {
			return new Iso8583Binary(id, length);
		}

		if ("custom".equalsIgnoreCase(type)) {
			char cpadWith = ' ';
			if (padWith.length() > 0) {
				cpadWith = padWith.charAt(0);
			}

			return new Iso8583String(id, lcount, length, align.symbolicValue(),
					cpadWith, emptyValue);
		}

		Constructor<?> ctor = FieldTypes.getConstructor(type);
		if (ctor != null) {
			char cpadWith = ' ';
			if (padWith.length() > 0) {
				cpadWith = padWith.charAt(0);
			}

			try {
				return (Iso8583Field) ctor.newInstance(id, lcount, length,
						align.symbolicValue(), cpadWith, emptyValue);
			} catch (Throwable t) {
				throw new RuntimeException("unable to instantiate field type "
						+ type, t);
			}
		}

		throw new RuntimeException("unknown type " + type);
	}
}
