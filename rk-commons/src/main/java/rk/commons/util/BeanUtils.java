package rk.commons.util;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.UUID;

import rk.commons.beans.factory.BeanInstantiationException;
import rk.commons.beans.factory.config.BeanDefinition;
import rk.commons.beans.factory.support.BeanDefinitionValueResolver;
import rk.commons.beans.factory.type.converter.TypeConverterResolver;

public abstract class BeanUtils {

	public static String getBeanQName(String packageName, String beanName) {
		if (beanName == null) {
			beanName = "";
		} else {
			beanName = beanName.trim();
		}

		if (StringUtils.hasText(packageName)) {
			return packageName.trim().concat(":").concat(beanName);
		}

		return beanName;
	}

	public static String applyDefaultPackageName(String packageName,
			String beanQName) {
		if (beanQName == null) {
			beanQName = "";
		} else {
			beanQName = beanQName.trim();
		}

		if (!StringUtils.hasText(packageName)) {
			return beanQName;
		}

		if (StringUtils.hasText(beanQName)) {
			int lddi = beanQName.lastIndexOf(':');

			if (lddi < 0) {
				return packageName.concat(":").concat(beanQName);
			} else {
				return beanQName;
			}
		} else {
			return packageName.concat(":");
		}
	}

	public static String applyPackageName(String packageName, String beanQName) {
		if (beanQName == null) {
			beanQName = "";
		} else {
			beanQName = beanQName.trim();
		}

		if (StringUtils.hasText(packageName)) {
			packageName = packageName.concat(":");
		} else {
			packageName = "";
		}

		if (StringUtils.hasText(beanQName)) {
			int lddi = beanQName.lastIndexOf(':');

			if (lddi < 0) {
				return packageName.concat(beanQName);
			} else {
				return packageName.concat(beanQName.substring(lddi + 1));
			}
		} else {
			return packageName;
		}
	}

	public static String generateRandomBeanQName(String packageName,
			String beanName) {
		return getBeanQName(packageName, beanName).concat("__").concat(
				StringUtils.valueOf(UUID.randomUUID()));
	}

	public static String generateRandomBeanQName(String packageName,
			BeanDefinition definition) {

		String className;

		if (StringUtils.hasText(definition.getBeanClassName())) {
			className = definition.getBeanClassName();
		} else if (definition.getBeanClass() != null) {
			className = definition.getBeanClass().getName();
		} else {
			className = "(anonymous)";
		}

		int lastDotIndex = className.lastIndexOf('.');

		if (lastDotIndex >= 0) {
			className = className.substring(lastDotIndex + 1);
		}

		return generateRandomBeanQName(packageName, className);
	}
	
	public static void applyPropertyValues(String beanQName, Object bean, Map<String, Object> propertyValues) {
		applyPropertyValues(beanQName, bean, propertyValues, null, null);
	}
	
	public static void applyPropertyValues(String beanQName, Object bean, Map<String, Object> propertyValues,
			TypeConverterResolver typeConverterResolver) {
		applyPropertyValues(beanQName, bean, propertyValues, null, typeConverterResolver);
	}

	public static void applyPropertyValues(String beanQName, Object bean, Map<String, Object> propertyValues,
			BeanDefinitionValueResolver valueResolver, TypeConverterResolver typeConverterResolver) {

		Class<?> beanType = bean.getClass();
		
		for (Map.Entry<String, Object> entry : propertyValues.entrySet()) {
			String name = entry.getKey();
			
			Object value;
			
			if (valueResolver == null) {
				value = entry.getValue();
			} else {
				value = valueResolver.resolveValueIfNecessary(entry.getValue());
			}
			
			applyPropertyValue(beanQName, bean, beanType, name, value, typeConverterResolver);
		}
	}
	
	public static void applyPropertyValue(String beanQName, Object bean, Class<?> beanType, String name, Object value,
			TypeConverterResolver typeConverterResolver) {

		try {
			if (name.contains(".")) {
				int dot = name.indexOf('.');
				
				String subname = name.substring(0, dot);
				String methodName = "get" + Character.toUpperCase(subname.charAt(0)) + subname.substring(1);
				
				Method getter = ReflectionUtils.findPublicMethod(beanType, methodName);
				if (getter == null) {
					throw new NoSuchMethodException(beanType + "." + methodName + "()");
				}
				
				Object nextBean = getter.invoke(beanType);
				
				applyPropertyValue(beanQName, nextBean, nextBean.getClass(), name.substring(dot + 1), value, typeConverterResolver);
				return;
			}
			
			String methodName = "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
			
			Method setter = ReflectionUtils.findPublicMethod(beanType, methodName, value.getClass());
			
			if (setter == null) {
				if (value instanceof String) {
					if (typeConverterResolver != null) {
						setter = ReflectionUtils.findStringConvertablePublicMethod(beanType, methodName, typeConverterResolver);
						
						if (setter != null) {
							value = typeConverterResolver.resolve(String.class, setter.getParameterTypes()[0]).convert(value);
						}
					}
				} else {
					Class<?> primitive = ObjectUtils.getPrimitiveType(value.getClass());
					
					if (primitive != null) {
						setter = ReflectionUtils.findPublicMethod(beanType, methodName, primitive);
					}
				}
				
				if (setter == null) {
					throw new NoSuchMethodException(beanType + "." + methodName + "(" + value.getClass() + ")");
				}
			}
			
			setter.invoke(bean, value);
		} catch (Throwable t) {
			throw new BeanInstantiationException(beanQName, t);
		}
	}
}
