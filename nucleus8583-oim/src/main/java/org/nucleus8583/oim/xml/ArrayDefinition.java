package org.nucleus8583.oim.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "array", namespace = "http://www.nucleus8583.org/schema/nucleus8583-oim")
@XmlAccessorType(XmlAccessType.FIELD)
public class ArrayDefinition {

	@XmlElement(name = "name")
	private String name;

	@XmlElementRefs({ @XmlElementRef(type = BasicDefinition.class),
			@XmlElementRef(type = ConstantDefinition.class),
			@XmlElementRef(type = SubDefinition.class),
			@XmlElementRef(type = IgnoreDefinition.class),
			@XmlElementRef(type = ArrayDefinition.class),
			@XmlElementRef(type = ArrayLengthDefinition.class)})
	@XmlMixed
	private List<Object> contents;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Object> getContents() {
		return contents;
	}

	public void setContents(List<Object> contents) {
		this.contents = contents;
	}
}
