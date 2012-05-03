package rk.commons.ioc.factory;

import java.util.List;

import rk.commons.ioc.factory.config.ObjectDefinition;

public interface IocObjectFactory {
	
	boolean containsObject(String objectQName);

	Object getObject(String objectQName);

	<T> List<T> getObjectsOfType(Class<T> type);

	String[] getObjectQNames();
	
	Object createObject(ObjectDefinition definition);
	
	void destroy();
}
