package org.nucleus8583.core.field.type;

import org.nucleus8583.core.xml.FieldAlignments;
import org.nucleus8583.core.xml.FieldDefinition;

public class AsciiLAsciiText extends AsciiPrefixedAsciiText {

	private static final long serialVersionUID = 6562019703061572067L;

	public AsciiLAsciiText(FieldDefinition def, FieldAlignments defaultAlign,
			String defaultPadWith, String defaultEmptyValue) {
		super(def, defaultAlign, defaultPadWith, defaultEmptyValue, 1, 9);
	}
}
