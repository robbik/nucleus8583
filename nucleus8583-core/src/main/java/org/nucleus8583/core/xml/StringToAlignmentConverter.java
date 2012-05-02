package org.nucleus8583.core.xml;

import rk.commons.beans.factory.type.converter.TypeConverter;

public class StringToAlignmentConverter implements TypeConverter {
	
	public static final Class<?> FROM = String.class;

	public static final Class<?> TO = Alignment.class;

	public Object convert(Object from) {
		return Alignment.enumValueOf((String) from);
	}
}
