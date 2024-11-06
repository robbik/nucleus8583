package org.nucleus8583.core.config;

import org.nucleus8583.core.type.Alignment;
import org.nucleus8583.core.type.spi.AsciiText;

public final class DefaultFields {

	public static final Field FIELD_0 = new Field(0, new AsciiText.Builder()
			.withLength(4)
			.withAlignment(Alignment.NONE)
			.withPadWith(" ")
			.withEmptyValue("")
			.build());

	private DefaultFields() {
		// do nothing
	}
}
