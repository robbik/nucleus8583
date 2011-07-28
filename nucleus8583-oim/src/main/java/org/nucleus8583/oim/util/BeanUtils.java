package org.nucleus8583.oim.util;

import java.lang.reflect.Field;
import java.util.Map;

public abstract class BeanUtils {
	
	public static void collectFields(Class<?> _class, Map<String, Field> fields) {
		Class<?> current = _class;

		while ((current != null) && (current != Object.class)) {
			Field[] arrFields = current.getDeclaredFields();

			for (int i = arrFields.length - 1; i >= 0; --i) {
				Field f = arrFields[i];
				f.setAccessible(true);

				fields.put(f.getName(), f);
			}

			current = current.getSuperclass();
		}
	}
}
