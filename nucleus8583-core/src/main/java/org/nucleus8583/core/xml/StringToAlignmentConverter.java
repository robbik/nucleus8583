package org.nucleus8583.core.xml;

import org.nucleus8583.core.field.Alignment;

import rk.commons.inject.factory.type.converter.TypeConverter;

public class StringToAlignmentConverter implements TypeConverter {

	public Object convert(Object from) {
		return Alignment.enumValueOf((String) from);
	}
}
