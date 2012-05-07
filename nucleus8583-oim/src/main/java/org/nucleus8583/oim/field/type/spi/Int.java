package org.nucleus8583.oim.field.type.spi;

import java.io.Reader;
import java.io.Writer;

import org.nucleus8583.oim.field.Alignment;
import org.nucleus8583.oim.field.type.Type;

public class Int extends Text {
	
	private static final Integer ZERO = Integer.valueOf(0);

	protected int precision;

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
		Integer ival;
		
		if (o == null) {
			ival = ZERO;
		} else {
			ival = (Integer) o;
		}
		
		super.write(out, ival.toString());
	}
	
	public Type clone() {
		return new Int(this);
	}
}
