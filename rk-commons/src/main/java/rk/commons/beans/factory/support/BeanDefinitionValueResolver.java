package rk.commons.beans.factory.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import rk.commons.beans.factory.BeanFactory;
import rk.commons.beans.factory.config.BeanDefinition;
import rk.commons.beans.factory.config.RuntimeBeanDefinition;

public class BeanDefinitionValueResolver {

	private final BeanFactory beanFactory;

	public BeanDefinitionValueResolver(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}

	public Object resolveValueIfNecessary(Object value) {
		Object c;
		
		if (value == null) {
			c = null;
		} else if (value instanceof BeanDefinition) {
			c = resolveValueIfNecessary(beanFactory.createBean((BeanDefinition) value));
		} else if (value instanceof RuntimeBeanDefinition) {
			c = resolveValueIfNecessary(beanFactory.getBean(((RuntimeBeanDefinition) value).getBeanQName()));
		} else if (value instanceof ManagedArray) {
			ManagedArray<?> t = (ManagedArray<?>) value;

			Object[] ct = new Object[t.length()];
			for (int i = 0, n = ct.length; i < n; ++i) {
				ct[i] = resolveValueIfNecessary(t.get(i));
			}

			c = ct;
		} else if (value instanceof Object[]) {
			Object[] t = (Object[]) value;
			
			Object[] ct = new Object[t.length];
			for (int i = 0, n = ct.length; i < n; ++i) {
				ct[i] = resolveValueIfNecessary(t[i]);
			}

			c = ct;
		} else if (value instanceof ManagedList) {
			ManagedList<?> t = (ManagedList<?>) value;

			List<Object> ct = new ArrayList<Object>(t.size());
			for (int i = 0, n = t.size(); i < n; ++i) {
				ct.add(resolveValueIfNecessary(t.get(i)));
			}
			
			c = ct;
		} else if (value instanceof ManagedMap) {
			ManagedMap<?, ?> t = (ManagedMap<?, ?>) value;

			Map<Object, Object> ct = new HashMap<Object, Object>();
			for (Map.Entry<?, ?> entry : t.entrySet()) {
				ct.put(resolveValueIfNecessary(entry.getKey()), resolveValueIfNecessary(entry.getValue()));
			}
			
			c = ct;
		} else if (value instanceof ManagedSet) {
			ManagedSet<?> t = (ManagedSet<?>) value;

			Set<Object> ct = new HashSet<Object>();
			for (Object e : t) {
				ct.add(resolveValueIfNecessary(e));
			}
			
			c = ct;
		} else if (value instanceof FactoryBean) {
			c = ((FactoryBean<?>) value).getObject();
		} else {
			c = value;
		}

		return c;
	}
}
