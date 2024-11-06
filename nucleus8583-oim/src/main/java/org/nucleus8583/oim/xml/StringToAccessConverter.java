package org.nucleus8583.oim.xml;

import org.nucleus8583.oim.Access;

import rk.commons.inject.factory.type.converter.TypeConverter;

public class StringToAccessConverter implements TypeConverter {

	public Object convert(Object from) {
		return Access.enumValueOf((String) from);
	}
}
