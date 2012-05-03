package rk.commons.ioc.factory.type.converter.spi;

import rk.commons.ioc.factory.type.converter.TypeConverter;

public class StringToIntConverter implements TypeConverter {
	
	public static final Class<?> FROM = String.class;
	
	public static final Class<?> TO = int.class;
	
	public Object convert(Object from) {
		return Integer.parseInt((String) from);
	}
}
