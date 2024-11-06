package org.nucleus8583.externaltest.osgi;

import org.nucleus8583.core.MessageSerializer;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

    public void start(BundleContext context) throws Exception {
        MessageSerializer iso87ascii = new MessageSerializer("classpath:META-INF/nucleus8583/packagers/iso87ascii.xml");
    }

    public void stop(BundleContext context) throws Exception {
        // do nothing
    }
}
