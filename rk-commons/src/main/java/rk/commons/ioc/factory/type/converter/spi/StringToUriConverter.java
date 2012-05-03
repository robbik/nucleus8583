package rk.commons.ioc.factory.type.converter.spi;

import java.net.URI;

import rk.commons.ioc.factory.type.converter.TypeConverter;

public class StringToUriConverter implements TypeConverter {
	
	public static final Class<?> FROM = String.class;

	public static final Class<?> TO = URI.class;

	public Object convert(Object from) {
		return URI.create((String) from);
	}
}
