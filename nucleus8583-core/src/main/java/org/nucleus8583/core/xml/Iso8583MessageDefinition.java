package org.nucleus8583.core.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "iso-message", namespace = "http://www.nucleus8583.org/schema/iso-message")
@XmlAccessorType(XmlAccessType.FIELD)
public class Iso8583MessageDefinition {

	@XmlAttribute(name = "encoding", required = true)
	private String encoding;

	@XmlElementRef
	private List<Iso8583FieldDefinition> fields;

	public String getEncoding() {
		return encoding;
	}

	public List<Iso8583FieldDefinition> getFields() {
		return fields;
	}
}
