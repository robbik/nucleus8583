package rk.commons.ioc.factory.type.converter.spi;

import rk.commons.ioc.factory.type.converter.TypeConverter;

public class StringToDoubleConverter implements TypeConverter {
	
	public Object convert(Object from) {
		return Double.parseDouble((String) from);
	}
}
