package org.nucleus8583.oim.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "oim-context", namespace = "http://www.nucleus8583.org/schema/nucleus8583-oim")
@XmlAccessorType(XmlAccessType.FIELD)
public class ContextDefinition {

	@XmlElementRef
	private List<EntityDefinition> messages;

	public List<EntityDefinition> getMessages() {
		return messages;
	}

	public void setMessages(List<EntityDefinition> messages) {
		this.messages = messages;
	}
}
