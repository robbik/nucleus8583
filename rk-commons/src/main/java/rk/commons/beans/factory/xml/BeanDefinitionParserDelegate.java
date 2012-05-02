package rk.commons.beans.factory.xml;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import rk.commons.beans.factory.config.BeanDefinition;
import rk.commons.beans.factory.support.BeanDefinitionRegistry;
import rk.commons.loader.ResourceLoader;
import rk.commons.util.BeanUtils;
import rk.commons.util.StringUtils;

public class BeanDefinitionParserDelegate {

	public static final String DEFAULT_BEAN_PACKAGE_NAME = null;
	
	private final ResourceLoader resourceLoader;

	private NamespaceHandlerResolver resolver;

	private BeanDefinitionRegistry registry;

	private String packageName;
	
	public BeanDefinitionParserDelegate(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
	
	public ResourceLoader getResourceLoader() {
		return resourceLoader;
	}

	public void setNamespaceHandlerResolver(NamespaceHandlerResolver resolver) {
		this.resolver = resolver;
	}

	public void setBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
		this.registry = registry;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getNamespaceURI(Node node) {
		return node.getNamespaceURI();
	}

	public String getLocalName(Node node) {
		return node.getLocalName();
	}

	public Object parse(Element element) {
		String namespaceURI = getNamespaceURI(element);

		NamespaceHandler handler = resolver.resolve(namespaceURI);

		BeanDefinition definition = handler.parse(element, this);
		String beanQName = definition.getBeanQName();
		
		if (!StringUtils.hasText(beanQName)) {
			while (registry.containsBeanDefinition(beanQName = BeanUtils
					.generateRandomBeanQName(packageName, definition))) {
				// do nothing
			}

			definition.setBeanQName(beanQName);
		} else if (registry.containsBeanDefinition(beanQName)) {
			throw new IllegalArgumentException("bean '"
					+ definition.getBeanQName() + "' already registered");
		} else {
			definition.setBeanQName(BeanUtils.applyDefaultPackageName(
					packageName, beanQName));
		}

		registry.registerBeanDefinition(definition);

		return definition;
	}
}
