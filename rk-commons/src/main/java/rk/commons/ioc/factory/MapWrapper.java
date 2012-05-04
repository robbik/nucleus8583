package rk.commons.ioc.factory;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MapWrapper implements Map<String, Object> {
	
	private final IocObjectFactory factory;
	
	public MapWrapper(IocObjectFactory factory) {
		this.factory = factory;
	}

	public void clear() {
		throw new UnsupportedOperationException();
	}

	public boolean containsKey(Object key) {
		return factory.containsObject((String) key);
	}

	public boolean containsValue(Object value) {
		throw new UnsupportedOperationException();
	}

	public Set<Map.Entry<String, Object>> entrySet() {
		return factory.getObjectsOfType(Object.class).entrySet();
	}

	public Object get(Object key) {
		return factory.getObject((String) key);
	}

	public boolean isEmpty() {
		return factory.getObjectQNames().isEmpty();
	}

	public Set<String> keySet() {
		return factory.getObjectQNames();
	}

	public Object put(String key, Object value) {
		throw new UnsupportedOperationException();
	}

	public void putAll(Map<? extends String, ? extends Object> m) {
		throw new UnsupportedOperationException();
	}

	public Object remove(Object key) {
		throw new UnsupportedOperationException();
	}

	public int size() {
		return factory.getObjectQNames().size();
	}

	public Collection<Object> values() {
		return factory.getObjectsOfType(Object.class).values();
	}
	
	@Override
	public String toString() {
		return factory.getObjectsOfType(Object.class).toString();
	}
}
