package org.nucleus8583.core;

import java.util.List;

import org.nucleus8583.core.xml.StringToAlignmentConverter;

import rk.commons.beans.context.XmlBeanContext;
import rk.commons.beans.factory.BeanFactory;
import rk.commons.beans.factory.SingletonBeanFactory;
import rk.commons.loader.ResourceLoader;

public class XmlContext {

    private static final String NAMESPACE_URI = "http://www.nucleus8583.org/schema/nucleus8583";

    private static final String NAMESPACE_HANDLER = "classpath:META-INF/nucleus8583/nucleus8583.handlers";

    private static final String NAMESPACE_SCHEMA = "classpath:META-INF/nucleus8583/nucleus8583.schemas";
	
	private XmlBeanContext context;
	
	private BeanFactory beanFactory;

    /**
     * create a new instance of {@link MessageSerializer} using given
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
	public XmlContext(String... locations) {
		ResourceLoader resourceLoader = new ResourceLoader();
		
		SingletonBeanFactory beanFactory = new SingletonBeanFactory(resourceLoader);

		beanFactory.getTypeConverterResolver().register(
				StringToAlignmentConverter.FROM, StringToAlignmentConverter.TO, new StringToAlignmentConverter());
		
		context = new XmlBeanContext();
		
		context.setResourceLoader(resourceLoader);
		
		context.setXmlDefaultNamespace(NAMESPACE_URI);
		
		context.setNamespaceHandlerPath(NAMESPACE_HANDLER);
		context.setNamespaceSchemaPath(NAMESPACE_SCHEMA);
		
		context.setBeanFactory(beanFactory);
		context.setBeanDefinitionRegistry(beanFactory);
		
		context.setLocations(locations);
		
		context.refresh(false);
		
		this.beanFactory = beanFactory;
	}

	public MessageSerializer getMessageSerializer(String name) {
		return (MessageSerializer) beanFactory.getBean(name);
	}

	public MessageSerializer getMessageSerializer() {
		List<MessageSerializer> list = beanFactory.getBeansOfType(MessageSerializer.class);
		
		if (list.isEmpty()) {
			throw new IllegalArgumentException("no message serializer defined");
		} else if (list.size() > 1) {
			throw new IllegalArgumentException("more than one message serializers defined");
		}
		
		return list.get(0);
	}
}
