package org.nucleus8583.oim.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javassist.Modifier;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

public class ClassAccessor {
	
	private static Map<Class<?>, ClassAccessor> cache;
	
	static {
		cache = new HashMap<Class<?>, ClassAccessor>();
	}

	Map<String, Field> fields;
	
	Map<String, FastMethod> getters;
	
	Map<String, FastMethod> setters;
	
	private ClassAccessor(Class<?> _class) {
		fields = new HashMap<String, Field>();
		
		getters = new HashMap<String, FastMethod>();
		setters = new HashMap<String, FastMethod>();
		
		FastClass fc = FastClass.create(_class);
		
		Class<?> i = _class; while ((i = gatherFields(i)) != null) ;
		
		FastClass j = fc; while ((j = gatherGetters(j)) != null) ;

		j = fc; while ((j = gatherSetters(j)) != null) ;

	}

	public static ClassAccessor getInstance(Class<?> _class) {
		ClassAccessor ce;
		
		synchronized (cache) {
			ce = cache.get(_class);
			
			if (ce == null) {
				ce = new ClassAccessor(_class);
				cache.put(_class, ce);
			}
		}
		
		return ce;
	}
	
	private Class<?> gatherFields(Class<?> _class) {
		Field[] fs = _class.getDeclaredFields();
		
		for (int i = 0, n = fs.length; i < n; ++i) {
			Field f = fs[i];
			
			// make sure it is not a static field
			if (Modifier.isStatic(f.getModifiers())) {
				continue;
			}
			
			String name = f.getName();
			
			if (!fields.containsKey(name)) {
				try {
					f.setAccessible(true);
				} catch (SecurityException e) {
					// do nothing
				}
				
				if (f.isAccessible()) {
					fields.put(name, f);
				}
			}
		}
		
		if (Object.class.equals(_class)) {
			return null;
		}
		
		return _class.getSuperclass();
	}
	
	private FastClass gatherGetters(FastClass fc) {
		Class<?> jc = fc.getJavaClass();
		
		Method[] ms = jc.getDeclaredMethods();
		
		for (int i = 0, n = ms.length; i < n; ++i) {
			Method m = ms[i];
			
			// make sure it is public method (non-static)
			int mod = m.getModifiers();
			
			if (Modifier.isStatic(mod) || !Modifier.isPublic(mod)) {
				continue;
			}
			
			// make sure its name begins with "get" and not "get"
			String name = m.getName();
			
			if (!name.startsWith("get") || "get".equals(name)) {
				continue;
			}

			// make sure it has no arguments
			if (m.getParameterTypes().length > 0) {
				continue;
			}
			
			// make sure it has only one return type and it is not a "void"
			Class<?> returnType = m.getReturnType();
			if ((returnType == null) || void.class.equals(returnType) || Void.class.equals(returnType)) {
				continue;
			}
			
			// rename
			name = name.substring(3);
			
			int namelen = name.length();
			if (namelen == 1) {
				name = name.toLowerCase();
			} else {
				char firstchar = name.charAt(0);
				char secondchar = name.charAt(1);
				
				if (Character.isUpperCase(firstchar) && Character.isUpperCase(secondchar)) {
					// do nothing
				} else {
					name = String.valueOf(Character.toLowerCase(firstchar)).concat(name.substring(1));
				}
			}
			
			if (!getters.containsKey(name)) {
				try {
					m.setAccessible(true);
				} catch (SecurityException e) {
					// do nothing
				}
				
				if (m.isAccessible()) {
					getters.put(name, fc.getMethod(m));
				}
			}
		}
		
		if (Object.class.equals(jc)) {
			return null;
		}
		
		return FastClass.create(jc.getSuperclass());
	}
	
	private FastClass gatherSetters(FastClass fc) {
		Class<?> jc = fc.getJavaClass();
		
		Method[] ms = jc.getDeclaredMethods();
		
		for (int i = 0, n = ms.length; i < n; ++i) {
			Method m = ms[i];
			
			// make sure it is public method (non-static)
			int mod = m.getModifiers();
			
			if (Modifier.isStatic(mod) || !Modifier.isPublic(mod)) {
				continue;
			}
			
			// make sure its name begins with "set" and not "set"
			String name = m.getName();
			
			if (!name.startsWith("set") || "set".equals(name)) {
				continue;
			}

			// make sure it has only one argument
			Class<?>[] paramTypes = m.getParameterTypes();
			if (paramTypes.length != 1) {
				continue;
			}
			
			// and it is not a "void" type
			if (void.class.equals(paramTypes[0]) || Void.class.equals(paramTypes[0])) {
				continue;
			}
			
			// make sure it has no return type or only a "void" returnType
			Class<?> returnType = m.getReturnType();
			if ((returnType != null) && !void.class.equals(returnType) && !Void.class.equals(returnType)) {
				continue;
			}
			
			// rename
			name = name.substring(3);
			
			int namelen = name.length();
			if (namelen == 1) {
				name = name.toLowerCase();
			} else {
				char firstchar = name.charAt(0);
				char secondchar = name.charAt(1);
				
				if (Character.isUpperCase(firstchar) && Character.isUpperCase(secondchar)) {
					// do nothing
				} else {
					name = String.valueOf(Character.toLowerCase(firstchar)).concat(name.substring(1));
				}
			}
			
			if (!setters.containsKey(name)) {
				try {
					m.setAccessible(true);
				} catch (SecurityException e) {
					// do nothing
				}
				
				if (m.isAccessible()) {
					setters.put(name, fc.getMethod(m));
				}
			}
		}
		
		if (Object.class.equals(jc)) {
			return null;
		}
		
		return FastClass.create(jc.getSuperclass());
	}
}
