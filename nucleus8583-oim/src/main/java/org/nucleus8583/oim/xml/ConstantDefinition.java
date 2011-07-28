package org.nucleus8583.oim.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "constant", namespace = "http://www.nucleus8583.org/schema/nucleus8583-oim")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConstantDefinition {
	
	@XmlElement(name = "type", required = true)
	private String type;
	
	@XmlElementRef
	@XmlMixed
	private LanguageDefinition language;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public LanguageDefinition getLanguage() {
		return language;
	}

	public void setLanguage(LanguageDefinition language) {
		this.language = language;
	}
}
