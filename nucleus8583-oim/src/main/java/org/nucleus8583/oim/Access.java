package org.nucleus8583.oim;

public enum Access {

	FIELD,
	PROPERTY,
	VIRTUAL;
	
	public static Access enumValueOf(String s) {
		if ("FIELD".equalsIgnoreCase(s)) {
			return FIELD;
		} else if ("PROPERTY".equalsIgnoreCase(s)) {
			return PROPERTY;
		} else {
			return VIRTUAL;
		}
	}
}
