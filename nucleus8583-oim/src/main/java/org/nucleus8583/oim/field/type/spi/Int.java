package org.nucleus8583.oim.field.type.spi;

import java.io.Reader;
import java.io.Writer;

import org.nucleus8583.oim.field.Alignment;
import org.nucleus8583.oim.field.type.Type;
import org.nucleus8583.oim.util.TypeConverter;

public class Int extends Text {
	
	public Int() {
		padder.setAlign(Alignment.TRIMMED_RIGHT);
		padder.setPadWith('0');
	}
	
	public Int(Int o) {
		super(o);
	}
	
	public Object read(Reader in) throws Exception {
		Integer ival;
		
		try {
			ival = Integer.valueOf((String) super.read(in));
		} catch (Throwable t) {
			return null;
		}
		
		return ival;
	}
	
	public void write(Writer out, Object o) throws Exception {
		String str;
		
		if (o == null) {
			str = "0";
		} else if (int.class.isInstance(o)) {
			str = String.valueOf(int.class.cast(o));
		} else if (o instanceof Integer) {
			str = ((Integer) o).toString();
		} else {
			str = String.valueOf(TypeConverter.convertToInt(o));
		}
		
		super.write(out, str);
	}
	
	public Type clone() {
		return new Int(this);
	}
}
