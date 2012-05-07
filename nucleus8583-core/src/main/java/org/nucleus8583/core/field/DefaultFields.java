package org.nucleus8583.core.field;

import org.nucleus8583.core.field.spi.AsciiText;

public abstract class DefaultFields {

	public static final Field FIELD_0;

	static {
		AsciiText asciiText = new AsciiText();
		asciiText.setLength(4);
		asciiText.setAlignment(Alignment.NONE);
		asciiText.setPadWith(" ");
		asciiText.setEmptyValue("");

		try {
			asciiText.initialize();
		} catch (Exception e) {
			throw new Error(e);
		}

		FIELD_0 = new Field(0, asciiText);
	}
}
