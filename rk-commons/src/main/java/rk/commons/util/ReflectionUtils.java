package rk.commons.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import rk.commons.beans.factory.type.converter.TypeConverterResolver;

public abstract class ReflectionUtils {
	
	public static int diffParameterTypes(Class<?>[] actual, Class<?>[] expected) {
		if (actual.length != expected.length) {
			return Integer.MAX_VALUE;
		}
		
		int diff = 0;

		for (int i = 0, n = actual.length; i < n; ++i) {
			Class<?> ca = actual[i];
			Class<?> ce = expected[i];
			
			if (!ca.equals(ce)) {
				if (!ca.isAssignableFrom(ce)) {
					diff = Integer.MAX_VALUE;
					break;
				}

				int depth = 1;

				Class<?> cesup = ce.getSuperclass();
				while ((cesup != null) && !cesup.equals(ca)) {
					++depth;
					cesup = cesup.getSuperclass();
				}

				diff += depth;
			}
		}
		
		return diff;
	}

	public static Method findPublicMethod(Class<?> _class, String methodName, Class<?>... expectedParamTypes) {
		Method found = null;

		int minDiff = Integer.MAX_VALUE;
		
		while (_class != null) {
			Method[] methods = _class.getDeclaredMethods();

			for (int i = 0, n = methods.length; i < n; ++i) {
				Method method = methods[i];

				if (Modifier.isPublic(method.getModifiers()) && methodName.equals(method.getName())) {
					int diff = diffParameterTypes(method.getParameterTypes(), expectedParamTypes);
					
					if (diff < minDiff) {
						found = method;
						minDiff = diff;
					}
				}
			}
			
			if (_class.equals(Object.class)) {
				_class = null;
			} else {
				_class = _class.getSuperclass();
			}
		}

		return found;
	}

	public static Method findStringConvertablePublicMethod(Class<?> _class, String methodName, TypeConverterResolver resolver) {
		Method found = null;
		
		while (_class != null) {
			Method[] methods = _class.getDeclaredMethods();

			for (int i = 0, n = methods.length; i < n; ++i) {
				Method method = methods[i];

				if (Modifier.isPublic(method.getModifiers()) && methodName.equals(method.getName())) {
					Class<?>[] methodParameterTypes = method.getParameterTypes();
					
					if ((methodParameterTypes != null) && (methodParameterTypes.length == 1)) {
						if (resolver.resolve(String.class, methodParameterTypes[0]) != null) {
							found = method;
							
							break;
						}
					}
				}
			}
			
			if (_class.equals(Object.class)) {
				_class = null;
			} else {
				_class = _class.getSuperclass();
			}
		}

		return found;
	}
}
