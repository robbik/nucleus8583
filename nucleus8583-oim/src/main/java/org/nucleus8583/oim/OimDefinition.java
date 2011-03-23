package org.nucleus8583.oim;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "oim")
@XmlAccessorType(XmlAccessType.FIELD)
public class OimDefinition {
	@XmlElementRef
	private List<EntityDefinition> entities;
	
	
}
