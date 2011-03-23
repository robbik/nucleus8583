package org.nucleus8583.oim.xml;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.nucleus8583.oim.converter.TypeConverter;
import org.nucleus8583.oim.util.ElExpression;
import org.nucleus8583.oim.util.StringUtil;
import org.w3c.dom.Element;

public abstract class TypeConverterFactory {
	private static final Map<Class<?>, String> primitives;

	static {
		primitives = new HashMap<Class<?>, String>();

		primitives.put(Byte.TYPE, "byte");
		primitives.put(Byte.class, "byte");

		primitives.put(Short.TYPE, "short");
		primitives.put(Short.class, "short");

		primitives.put(Integer.TYPE, "int");
		primitives.put(Integer.class, "int");

		primitives.put(Long.TYPE, "long");
		primitives.put(Long.class, "long");

		primitives.put(Float.TYPE, "float");
		primitives.put(Float.class, "float");

		primitives.put(Double.TYPE, "double");
		primitives.put(Double.class, "double");

		primitives.put(Void.TYPE, "void");
		primitives.put(Void.class, "void");

		primitives.put(Character.TYPE, "char");
		primitives.put(Character.class, "char");
	}

	private static boolean primitiveEquals(Class<?> a, Class<?> b) {
		String pa = primitives.get(a);
		String pb = primitives.get(b);

		if ((pa == null) || (pb == null)) {
			return false;
		}

		return pa.equals(pb);
	}

	private static boolean arrayEquals(Class<?>[] expected, Class<?>[] actual,
			boolean skipNull, boolean canBeInherited) {
		if (expected == actual) {
			return true;
		}

		if (expected == null) {
			return actual == null;
		}
		if (actual == null) {
			return expected == null;
		}

		if (expected.length != actual.length) {
			return false;
		}

		Class<?> exp, act;

		for (int i = expected.length - 1; i >= 0; --i) {
			exp = expected[i];
			act = actual[i];

			if ((exp == null) && (act != null) && !skipNull) {
				return false;
			}
			if ((exp != null) && (act == null) && !skipNull) {
				return false;
			}

			if ((exp != null) && (act != null)) {
				if (!primitiveEquals(exp, act)) {
					if (canBeInherited) {
						if (!act.isAssignableFrom(exp)) {
							return false;
						}
					} else {
						if (!act.equals(exp)) {
							return false;
						}
					}
				}
			}
		}

		return true;
	}

	public static TypeConverter parse(Element node) {
		String name = node.getAttribute("name");
		String className = node.getAttribute("class");
		String ctorArguments = node.getAttribute("constructor-args");

		// name
		if (!StringUtil.hasText(name)) {
			throw new IllegalArgumentException("name required");
		}

		name = name.trim();

		// class
		if (!StringUtil.hasText(className)) {
			throw new IllegalArgumentException("class name required");
		}

		// constructor-args
		Object[] args = null;

		if (StringUtil.hasText(ctorArguments)) {
			args = (Object[]) ElExpression.eval(ctorArguments);
		}

		// create instance
		Class<?> _class;

		try {
			_class = Class.forName(className, true, Thread.currentThread()
					.getContextClassLoader());
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("unable to find class " + className);
		}

		Object instance;

		if ((args == null) || (args.length == 0)) {
			try {
				instance = _class.newInstance();
			} catch (InstantiationException e) {
				throw new RuntimeException("unable to instantiate class "
						+ className + " using default constructor", e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException("unable to instantiate class "
						+ className + " using default constructor", e);
			}
		} else {
			Class<?>[] argsTypes = new Class<?>[args.length];

			for (int i = args.length - 1; i >= 0; --i) {
				Object obj = args[i];

				if (obj == null) {
					argsTypes[i] = null;
				} else {
					argsTypes[i] = obj.getClass();
				}
			}

			Constructor<?>[] ctors = _class.getConstructors();
			instance = null;

			for (int i = ctors.length - 1; i >= 0; --i) {
				Constructor<?> ctor = ctors[i];

				if (arrayEquals(argsTypes, ctor.getParameterTypes(), true, true)) {
					try {
						instance = ctor.newInstance(args);
					} catch (IllegalArgumentException e) {
						throw new RuntimeException(
								"unable to instantiate class " + className
										+ " using matched constructor", e);
					} catch (InstantiationException e) {
						throw new RuntimeException(
								"unable to instantiate class " + className
										+ " using matched constructor", e);
					} catch (IllegalAccessException e) {
						throw new RuntimeException(
								"unable to instantiate class " + className
										+ " using matched constructor", e);
					} catch (InvocationTargetException e) {
						throw new RuntimeException(
								"unable to instantiate class " + className
										+ " using matched constructor", e);
					}
					break;
				}

				if (instance == null) {
					throw new RuntimeException(
							"no matched constructor found in class "
									+ className);
				}
			}
		}

		if (!TypeConverter.class.isInstance(instance)) {
			throw new ClassCastException(className + " is not a converter");
		}

		TypeConverter converter = (TypeConverter) instance;
		converter.setName(name);

		return converter;
	}
}
