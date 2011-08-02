package org.nucleus8583.oim.xml;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "entity", namespace = "http://www.nucleus8583.org/schema/nucleus8583-oim")
@XmlAccessorType(XmlAccessType.FIELD)
public class EntityDefinition {

	@XmlAttribute(name = "id", required = true)
	private String id;

    @XmlAttribute(name = "class", required = true)
    private String className;

	@XmlElementRef
	private List<EntityFieldDefinition> fields;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<EntityFieldDefinition> getFields() {
		return fields;
	}

	public void setFields(List<EntityFieldDefinition> fields) {
		this.fields = fields;
	}

	public void validate() {
	    for (char cc : id.toCharArray()) {
	        if (!Character.isUnicodeIdentifierPart(cc)) {
	            throw new IllegalArgumentException("invalid id " + id);
	        }
	    }

        for (char cc : className.toCharArray()) {
            if (!Character.isJavaIdentifierPart(cc)) {
                throw new IllegalArgumentException("invalid class name " + className);
            }
        }

        if (fields == null) {
            fields = new ArrayList<EntityFieldDefinition>();
        }

        for (EntityFieldDefinition efd : fields) {
            efd.validate();
        }
	}
}
