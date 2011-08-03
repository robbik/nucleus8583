package org.nucleus8583.core.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "iso-field", namespace = "http://www.nucleus8583.org/schema/iso-message")
@XmlAccessorType(XmlAccessType.FIELD)
public class FieldDefinition {
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
