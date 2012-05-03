package rk.commons.ioc.factory.support;

import rk.commons.ioc.factory.config.ObjectDefinition;
import rk.commons.ioc.factory.config.ObjectReference;

public class ObjectDefinitionBuilder {

	protected ObjectDefinition definition;

	public ObjectDefinitionBuilder() {
		definition = new ObjectDefinition();
	}

	public void setObjectQName(String objectQName) {
		definition.setObjectQName(objectQName);
	}

	public void setObjectClassName(String objectClassName) {
		definition.setObjectClassName(objectClassName);
	}

	public void setObjectClass(Class<?> objectClass) {
		definition.setObjectClass(objectClass);
	}
	
	public void addPropertyValue(String name, Object value) {
		definition.getPropertyValues().put(name, value);
	}

	public void addPropertyReference(String name, String objectQName) {
		addPropertyValue(name, new ObjectReference(objectQName));
	}

	public void setInitMethod(String initMethod) {
		definition.setInitMethod(initMethod);
	}

	public void setDestroyMethod(String destroyMethod) {
		definition.setDestroyMethod(destroyMethod);
	}

	public void setExtends(String objectQName) {
		definition.setExtends(objectQName);
	}

	public ObjectDefinition createObjectDefinition() {
		return definition;
	}
}
