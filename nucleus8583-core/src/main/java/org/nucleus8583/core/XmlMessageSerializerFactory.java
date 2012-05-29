package org.nucleus8583.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.nucleus8583.core.field.Alignment;
import org.nucleus8583.core.xml.StringToAlignmentConverter;

import rk.commons.inject.context.XmlContext;
import rk.commons.inject.factory.ObjectFactory;
import rk.commons.inject.factory.SingletonObjectFactory;
import rk.commons.loader.ResourceLoader;

public class XmlMessageSerializerFactory {

    private static final String NAMESPACE_URI = "http://www.nucleus8583.org/schema/nucleus8583";

    private static final String NAMESPACE_HANDLER = "classpath:META-INF/nucleus8583/nucleus8583.handlers";

    private static final String NAMESPACE_SCHEMA = "classpath:META-INF/nucleus8583/nucleus8583.schemas";
	
	private XmlContext context;
	
	private ObjectFactory factory;

    /**
     * create a new instance of {@link XmlMessageSerializerFactory} using given
     * configuration.
     *
     * For example, if you want to load "nucleus8583.xml" from "META-INF"
     * located in classpath, the location should be
     * <code>classpath:META-INF/nucleus8583.xml</code>.
     *
     * If you want to load "nucleus8583.xml" from "conf" directory, the location
     * should be <code>file:conf/nucleus8583.xml</code> or just
     * <code>conf/nucleus8583.xml</code>.
     *
     * @param location
     *            configuration location (in URI)
     */
	public XmlMessageSerializerFactory(String... locations) {
		ResourceLoader resourceLoader = new ResourceLoader();
		
		SingletonObjectFactory factory = new SingletonObjectFactory(resourceLoader);

		factory.getTypeConverterResolver().register(
				String.class, Alignment.class, new StringToAlignmentConverter());
		
		context = new XmlContext();
		
		context.setResourceLoader(resourceLoader);
		
		context.setXmlDefaultNamespace(NAMESPACE_URI);
		
		context.setNamespaceHandlerPath(NAMESPACE_HANDLER);
		context.setNamespaceSchemaPath(NAMESPACE_SCHEMA);
		
		context.setObjectFactory(factory);
		context.setObjectDefinitionRegistry(factory);
		
		boolean defTypesFound = false;

		for (int i = 0, n = locations.length; i < n; ++i) {
			if ("classpath:META-INF/nucleus8583/nucleus8583-types.xml".equals(locations[i])) {
				defTypesFound = true;
				break;
			}
		}
		
		if (!defTypesFound) {
			List<String> newLocations = new ArrayList<String>();
			
			newLocations.add("classpath:META-INF/nucleus8583/nucleus8583-types.xml");
			
			for (int i = 0, n = locations.length; i < n; ++i) {
				newLocations.add(locations[i]);
			}
			
			locations = newLocations.toArray(new String[0]);
			newLocations.clear();
		}
		
		context.setLocations(locations);

		context.refresh(false);
		
		this.factory = factory;
	}

	public MessageSerializer getMessageSerializer(String name) {
		return (MessageSerializer) factory.getObject(name);
	}

	public MessageSerializer getMessageSerializer() {
		Collection<MessageSerializer> list = factory.getObjectsOfType(MessageSerializer.class).values();
		
		if (list.isEmpty()) {
			throw new IllegalArgumentException("no message serializer defined");
		} else if (list.size() > 1) {
			throw new IllegalArgumentException("more than one message serializers defined");
		}
		
		return list.iterator().next();
	}
}
