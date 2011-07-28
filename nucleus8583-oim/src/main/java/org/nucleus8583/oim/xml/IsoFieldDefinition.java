package org.nucleus8583.oim.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "iso-field", namespace = "http://www.nucleus8583.org/schema/nucleus8583-oim")
@XmlAccessorType(XmlAccessType.FIELD)
public class IsoFieldDefinition {

	@XmlElement(name = "id", required = true)
	private String id;

	@XmlElementRefs({ @XmlElementRef(type = BasicDefinition.class),
			@XmlElementRef(type = ConstantDefinition.class),
			@XmlElementRef(type = IsoFieldDefinition.class),
			@XmlElementRef(type = SubDefinition.class),
			@XmlElementRef(type = IgnoreDefinition.class),
			@XmlElementRef(type = ArrayDefinition.class),
			@XmlElementRef(type = ArrayLengthDefinition.class) })
	@XmlMixed
	private List<Object> contents;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Object> getContents() {
		return contents;
	}

	public void setContents(List<Object> contents) {
		this.contents = contents;
	}
}
