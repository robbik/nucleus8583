package org.nucleus8583.core.osgi;

import org.nucleus8583.core.field.type.FieldTypes;
import org.nucleus8583.core.xml.FieldDefinition;
import org.nucleus8583.core.xml.MessageDefinitionReader;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Nucleus8583Activator implements BundleActivator {

    public void start(BundleContext context) throws Exception {
        try {
            FieldTypes.getType(new FieldDefinition(0, "an", 0, null, null, ""));
        } catch (Throwable t) {
            // do nothing
        }

        try {
            new MessageDefinitionReader();
        } catch (Throwable t) {
            // do nothing
        }
    }

    public void stop(BundleContext context) throws Exception {
        // do nothing
    }
}
