package org.nucleus8583.oim.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "import", namespace = "http://www.nucleus8583.org/schema/nucleus8583-oim")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportDefinition {

	@XmlElement(name = "uri", required = true)
	private String uri;

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
}
