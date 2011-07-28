package org.nucleus8583.oim.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.nucleus8583.oim.util.StringUtils;

@XmlRootElement(name = "ignore", namespace = "http://www.nucleus8583.org/schema/nucleus8583-oim")
@XmlAccessorType(XmlAccessType.FIELD)
public class IgnoreDefinition {

	@XmlElement(name = "fill-with", defaultValue = " ")
	private String fillWith;

	@XmlElement(name = "length", required = true)
	private int length;

	public String getFillWith() {
		return fillWith;
	}

	public void setFillWith(String fillWith) {
		this.fillWith = fillWith;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
	
	public void validate() throws Exception {
		if (!StringUtils.hasText(fillWith, true)) {
			throw new IllegalArgumentException("fill-with must not be empty");
		}
	}
}
