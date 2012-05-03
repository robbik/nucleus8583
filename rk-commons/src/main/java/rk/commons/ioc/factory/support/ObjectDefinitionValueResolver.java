package rk.commons.ioc.factory.support;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rk.commons.ioc.factory.IocObjectFactory;
import rk.commons.ioc.factory.config.ObjectDefinition;
import rk.commons.ioc.factory.config.ObjectReference;

public class ObjectDefinitionValueResolver {

	private final IocObjectFactory beanFactory;

	public ObjectDefinitionValueResolver(IocObjectFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	public Object resolveValueIfNecessary(Object value) {
		Object c;
		
		if (value == null) {
			c = null;
		} else if (value instanceof ObjectDefinition) {
			c = resolveValueIfNecessary(beanFactory.createObject((ObjectDefinition) value));
		} else if (value instanceof ObjectReference) {
			c = resolveValueIfNecessary(beanFactory.getObject(((ObjectReference) value).getObjectQName()));
		} else if (value instanceof Object[]) {
			Object[] t = (Object[]) value;
			
			Object[] ct = (Object[]) Array.newInstance(value.getClass().getComponentType(), t.length);
			boolean changed = false;
			
			for (int i = 0, n = ct.length; i < n; ++i) {
				ct[i] = resolveValueIfNecessary(t[i]);
				
				if (ct[i] != t[i]) {
					changed = true;
				}
			}

			if (changed) {
				c = ct;
			} else {
				c = value;
			}
		} else if (value instanceof List<?>) {
			List<?> t = (List<?>) value;

			List<Object> ct = new ArrayList<Object>(t.size());
			boolean changed = false;
			
			for (int i = 0, n = t.size(); i < n; ++i) {
				Object e = t.get(i);
				Object ne = resolveValueIfNecessary(e);
				
				ct.add(ne);
				
				if (ne != e) {
					changed = true;
				}
			}

			if (changed) {
				c = ct;
			} else {
				c = value;
			}
		} else if (value instanceof Map<?, ?>) {
			Map<?, ?> t = (Map<?, ?>) value;

			Map<Object, Object> ct = new HashMap<Object, Object>();
			boolean changed = false;
			
			for (Map.Entry<?, ?> entry : t.entrySet()) {
				Object nkey = resolveValueIfNecessary(entry.getKey());
				Object nvalue = resolveValueIfNecessary(entry.getValue()); 
				
				ct.put(nkey, nvalue);
				
				if ((nkey != entry.getKey()) || (nvalue != entry.getValue())) {
					changed = true;
				}
			}

			if (changed) {
				c = ct;
			} else {
				c = value;
			}
		} else if (value instanceof Set<?>) {
			Set<?> t = (Set<?>) value;

			Set<Object> ct = new HashSet<Object>();
			boolean changed = false;
			
			for (Object e : t) {
				Object ne = resolveValueIfNecessary(e);
				
				ct.add(ne);
				
				if (ne != e) {
					changed = true;
				}
			}

			if (changed) {
				c = ct;
			} else {
				c = value;
			}
		} else if (value instanceof ObjectFactory) {
			c = resolveValueIfNecessary(((ObjectFactory<?>) value).getObject());
		} else {
			c = value;
		}

		return c;
	}
}
