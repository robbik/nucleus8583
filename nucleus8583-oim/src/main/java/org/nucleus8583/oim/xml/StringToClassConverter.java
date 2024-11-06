package org.nucleus8583.oim.xml;

import rk.commons.inject.factory.type.converter.TypeConverter;
import rk.commons.loader.ResourceLoader;

public class StringToClassConverter implements TypeConverter {
	
	private final ResourceLoader loader;
	
	public StringToClassConverter(ResourceLoader loader) {
		this.loader = loader;
	}

	public Object convert(Object from) {
		try {
			return loader.loadClass((String) from);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("class " + from + " cannot be found in classpath");
		}
	}
}
