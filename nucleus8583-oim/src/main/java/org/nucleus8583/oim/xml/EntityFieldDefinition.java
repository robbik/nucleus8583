package org.nucleus8583.oim.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "field", namespace = "http://www.nucleus8583.org/schema/nucleus8583-oim")
@XmlAccessorType(XmlAccessType.FIELD)
public class EntityFieldDefinition {

    @XmlAttribute(name = "id", required = true)
    private String id;

    @XmlAttribute(name = "binary", required = true)
    private boolean binary;

    @XmlElementRefs( { @XmlElementRef(type = BasicDefinition.class), @XmlElementRef(type = VarBasicDefinition.class),
            @XmlElementRef(type = ConstantDefinition.class), @XmlElementRef(type = EntityFieldDefinition.class),
            @XmlElementRef(type = SubDefinition.class), @XmlElementRef(type = IgnoreDefinition.class),
            @XmlElementRef(type = ArrayDefinition.class), @XmlElementRef(type = ArrayLengthDefinition.class) })
    private List<Object> contents;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isBinary() {
        return binary;
    }

    public void setBinary(boolean binary) {
        this.binary = binary;
    }

    public List<Object> getContents() {
        return contents;
    }

    public void setContents(List<Object> contents) {
        this.contents = contents;
    }

    public void validate() {
        int iid;

        try {
            iid = Integer.parseInt(id);
        } catch (RuntimeException ex) {
            throw new IllegalArgumentException("id should be a number between 0-200");
        }

        if ((iid < 0) || (iid > 200)) {
            throw new IllegalArgumentException("id should be a number between 0-200");
        }

        if (contents != null) {
            if (contents.isEmpty()) {
                contents = null;
            }
        }
    }
}
