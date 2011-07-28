package org.nucleus8583.oim.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "basic", namespace = "http://www.nucleus8583.org/schema/nucleus8583-oim")
@XmlAccessorType(XmlAccessType.FIELD)
public class BasicDefinition {
	
	@XmlElement(name = "name", required = true)
	private String name;
	
	@XmlElement(name = "type", required = true)
	private String type;
	
	@XmlElement(name = "length", required = true)
	private int length;
	
	@XmlElement(name = "align", defaultValue = "none")
	private String align;
	
	@XmlElement(name = "pad-with", defaultValue = " ")
	private String padWith;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public String getPadWith() {
		return padWith;
	}

	public void setPadWith(String padWith) {
		this.padWith = padWith;
	}
}
