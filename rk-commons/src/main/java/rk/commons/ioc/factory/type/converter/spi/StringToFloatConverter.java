package rk.commons.ioc.factory.type.converter.spi;

import rk.commons.ioc.factory.type.converter.TypeConverter;

public class StringToFloatConverter implements TypeConverter {
	
	public Object convert(Object from) {
		return Float.parseFloat((String) from);
	}
}
