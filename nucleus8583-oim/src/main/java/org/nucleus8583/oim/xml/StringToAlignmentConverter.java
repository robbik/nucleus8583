package org.nucleus8583.oim.xml;

import org.nucleus8583.oim.field.Alignment;

import rk.commons.ioc.factory.type.converter.TypeConverter;

public class StringToAlignmentConverter implements TypeConverter {

	public Object convert(Object from) {
		return Alignment.enumValueOf((String) from);
	}
}
