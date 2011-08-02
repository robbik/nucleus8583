package org.nucleus8583.oim.accessor;

import java.lang.reflect.Field;
import java.util.Map;

public interface HasFieldsComponent {
	Field getField(String name);

	Map<String, Field> getFields();
}
