package org.nucleus8583.oim;

import java.util.ArrayList;
import java.util.List;

import org.nucleus8583.core.Message;
import org.nucleus8583.oim.field.Alignment;
import org.nucleus8583.oim.util.BeanAccessor;
import org.nucleus8583.oim.xml.StringToAccessConverter;
import org.nucleus8583.oim.xml.StringToAlignmentConverter;
import org.nucleus8583.oim.xml.StringToClassConverter;

import rk.commons.inject.context.XmlContext;
import rk.commons.inject.factory.ObjectFactory;
import rk.commons.inject.factory.SingletonObjectFactory;
import rk.commons.inject.factory.type.converter.TypeConverterResolver;
import rk.commons.loader.ResourceLoader;

public class MessageManager {

    private static final String NAMESPACE_URI = "http://www.nucleus8583.org/schema/nucleus8583-oim";

    private static final String NAMESPACE_HANDLER = "classpath:META-INF/nucleus8583/nucleus8583-oim.handlers";

    private static final String NAMESPACE_SCHEMA = "classpath:META-INF/nucleus8583/nucleus8583-oim.schemas";
	
	private XmlContext context;
	
	private ObjectFactory factory;

    /**
     * create a new instance of {@link MessageManager} using given
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
	public MessageManager(String... locations) {
		ResourceLoader resourceLoader = new ResourceLoader();
		
		SingletonObjectFactory factory = new SingletonObjectFactory(resourceLoader);
		TypeConverterResolver tcr = factory.getTypeConverterResolver();

		tcr.register(String.class, Alignment.class, new StringToAlignmentConverter());
		tcr.register(String.class, Access.class, new StringToAccessConverter());
		tcr.register(String.class, Class.class, new StringToClassConverter(resourceLoader));
		
		context = new XmlContext();
		
		context.setResourceLoader(resourceLoader);
		
		context.setXmlDefaultNamespace(NAMESPACE_URI);
		
		context.setNamespaceHandlerPath(NAMESPACE_HANDLER);
		context.setNamespaceSchemaPath(NAMESPACE_SCHEMA);
		
		context.setObjectFactory(factory);
		context.setObjectDefinitionRegistry(factory);
		
		boolean defTypesFound = false;

		for (int i = 0, n = locations.length; i < n; ++i) {
			if ("classpath:META-INF/nucleus8583/nucleus8583-oim.xml".equals(locations[i])) {
				defTypesFound = true;
				break;
			}
		}
		
		if (!defTypesFound) {
			List<String> newLocations = new ArrayList<String>();
			
			newLocations.add("classpath:META-INF/nucleus8583/nucleus8583-oim.xml");
			
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
	
	public void write(Message isoMsg, String name, Object object) throws Exception {
		((MessageEntity) factory.getObject(name)).write(isoMsg, object);
	}
	
	public Object read(Message isoMsg, String name) throws Exception {
		return ((MessageEntity) factory.getObject(name)).read(isoMsg);
	}
	
	public BeanAccessor readAsBeanAccessor(Message isoMsg, String name) throws Exception {
		return ((MessageEntity) factory.getObject(name)).readAsBeanAccessor(isoMsg);
	}
}
