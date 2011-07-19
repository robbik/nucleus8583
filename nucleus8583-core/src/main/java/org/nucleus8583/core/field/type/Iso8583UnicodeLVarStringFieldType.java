package org.nucleus8583.core.field.type;

import org.nucleus8583.core.xml.Iso8583FieldAlignments;
import org.nucleus8583.core.xml.Iso8583FieldDefinition;

public final class Iso8583UnicodeLVarStringFieldType extends Iso8583UnicodeVarStringFieldType {
	private static final long serialVersionUID = -5615324004502124085L;

	public Iso8583UnicodeLVarStringFieldType(Iso8583FieldDefinition def, Iso8583FieldAlignments defaultAlign,
			String defaultPadWith, String defaultEmptyValue) {
		super(def, defaultAlign, defaultPadWith, defaultEmptyValue, 1, 9);
	}
}
