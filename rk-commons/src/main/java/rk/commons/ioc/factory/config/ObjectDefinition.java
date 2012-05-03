package rk.commons.ioc.factory.config;

import java.util.HashMap;
import java.util.Map;

import rk.commons.loader.ResourceLoader;
import rk.commons.util.StringUtils;

public class ObjectDefinition {

	protected String objectQName;

	protected String objectClassName;

	protected Class<?> objectClass;
	
	protected Map<String, Object> propertyValues;

	protected String initMethod;

	protected String destroyMethod;

	protected String extendsObjectQName;

	public ObjectDefinition() {
		propertyValues = new HashMap<String, Object>();
	}

	public String getObjectQName() {
		return objectQName;
	}

	public void setObjectQName(String objectQName) {
		this.objectQName = objectQName;
	}

	public String getObjectClassName() {
		return objectClassName;
	}

	public void setObjectClassName(String objectClassName) {
		this.objectClassName = objectClassName;
	}

	public Class<?> getObjectClass() {
		return objectClass;
	}
	
	public void setObjectClass(Class<?> objectClass) {
		this.objectClass = objectClass;
	}
	
	public Map<String, Object> getPropertyValues() {
		return propertyValues;
	}

	public void setPropertyValues(Map<String, Object> propertyValues) {
		this.propertyValues = propertyValues;
	}

	public String getInitMethod() {
		return initMethod;
	}

	public void setInitMethod(String initMethod) {
		this.initMethod = initMethod;
	}

	public String getDestroyMethod() {
		return destroyMethod;
	}

	public void setDestroyMethod(String destroyMethod) {
		this.destroyMethod = destroyMethod;
	}

	public String getExtends() {
		return extendsObjectQName;
	}

	public void setExtends(String extendsObjectQName) {
		this.extendsObjectQName = extendsObjectQName;
	}

	public void resolveClass(ResourceLoader resourceLoader) {
		if (objectClass == null) {
			if (!StringUtils.hasText(objectClassName)) {
				throw new IllegalArgumentException(
						"object class name or object class must be specified for object " + objectQName);
			}

			try {
				objectClass = resourceLoader.loadClass(objectClassName);
			} catch (ClassNotFoundException e) {
				throw new IllegalArgumentException(
						"unable to load object class name '" + objectClassName + "' for object " + objectQName);
			}
		}
	}
}
