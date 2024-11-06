package org.nucleus8583.oim.field.type.spi;

import java.io.Reader;
import java.io.Writer;
import java.util.Map;

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
	
	@Override
	public Object read(Reader in, Map<String, Object> tmp) throws Exception {
		Integer ival;
		
		try {
			ival = Integer.valueOf((String) super.read(in, tmp));
		} catch (Throwable t) {
			return null;
		}
		
		return ival;
	}
	
	@Override
	public void write(Writer out, Object o, Map<String, Object> tmp) throws Exception {
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
		
		super.write(out, str, tmp);
	}
	
	@Override
	public Type clone() {
		return new Int(this);
	}
}
