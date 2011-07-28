package org.nucleus8583.oim.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "iso-message", namespace = "http://www.nucleus8583.org/schema/nucleus8583-oim")
@XmlAccessorType(XmlAccessType.FIELD)
public class Iso8583MessageDefinition {

	@XmlElement(name = "id")
	private String id;
	
	@XmlElementRef
	private List<IsoFieldDefinition> fields;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<IsoFieldDefinition> getFields() {
		return fields;
	}

	public void setFields(List<IsoFieldDefinition> fields) {
		this.fields = fields;
	}
}
