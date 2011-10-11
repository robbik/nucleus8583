package org.nucleus8583.core.field.type;

import org.nucleus8583.core.xml.FieldAlignments;
import org.nucleus8583.core.xml.FieldDefinition;

public class AsciiLLLBase16Binary extends AsciiPrefixedBase16Binary {

	private static final long serialVersionUID = 6562019703061572067L;

	public AsciiLLLBase16Binary(FieldDefinition def,
			FieldAlignments defaultAlign, String defaultPadWith,
			String defaultEmptyValue) {
		super(def, defaultAlign, defaultPadWith, defaultEmptyValue, 3, 999);
	}
}
