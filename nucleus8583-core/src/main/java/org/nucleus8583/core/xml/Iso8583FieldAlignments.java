package org.nucleus8583.core.xml;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlEnum(String.class)
public enum Iso8583FieldAlignments {
	@XmlEnumValue("left")
	LEFT, @XmlEnumValue("right")
	RIGHT, @XmlEnumValue("none")
	NONE;

	public char symbolicValue() {
		if (this == LEFT) {
			return 'l';
		}
		if (this == RIGHT) {
			return 'r';
		}
		return 'n';
	}
}
