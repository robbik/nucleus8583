package org.nucleus8583.core.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "iso-message", namespace = "http://www.nucleus8583.org/schema/iso-message")
@XmlAccessorType(XmlAccessType.FIELD)
public class MessageDefinition {

	@XmlAttribute(name = "encoding", required = true)
	private String encoding;

	@XmlElementRef
	private List<FieldDefinition> fields;

	public String getEncoding() {
		return encoding;
	}

	public List<FieldDefinition> getFields() {
		return fields;
	}
}
