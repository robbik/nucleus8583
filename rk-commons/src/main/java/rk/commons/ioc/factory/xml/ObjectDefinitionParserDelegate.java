package rk.commons.ioc.factory.xml;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import rk.commons.ioc.factory.config.ObjectDefinition;
import rk.commons.ioc.factory.support.ObjectDefinitionRegistry;
import rk.commons.loader.ResourceLoader;
import rk.commons.util.ObjectUtils;
import rk.commons.util.StringUtils;

public class ObjectDefinitionParserDelegate {

	public static final String DEFAULT_OBJECT_PACKAGE_NAME = null;
	
	private final ResourceLoader resourceLoader;

	private NamespaceHandlerResolver resolver;

	private ObjectDefinitionRegistry registry;

	private String packageName;
	
	public ObjectDefinitionParserDelegate(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
	
	public ResourceLoader getResourceLoader() {
		return resourceLoader;
	}

	public void setNamespaceHandlerResolver(NamespaceHandlerResolver resolver) {
		this.resolver = resolver;
	}

	public void setObjectDefinitionRegistry(ObjectDefinitionRegistry registry) {
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

		ObjectDefinition definition = handler.parse(element, this);
		String objectQName = definition.getObjectQName();
		
		if (!StringUtils.hasText(objectQName)) {
			while (registry.containsObjectDefinition(objectQName = ObjectUtils
					.generateRandomObjectQName(packageName, definition))) {
				// do nothing
			}

			definition.setObjectQName(objectQName);
		} else {
			definition.setObjectQName(ObjectUtils.applyDefaultPackageName(packageName, objectQName));
		}

		registry.registerObjectDefinition(definition);

		return definition;
	}
}
