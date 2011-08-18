package org.nucleus8583.core.xml;

import java.util.List;

public class MessageDefinition {

    private String encoding;

    private List<FieldDefinition> fields;

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public List<FieldDefinition> getFields() {
        return fields;
    }

    public void setFields(List<FieldDefinition> fields) {
        this.fields = fields;
    }
}
