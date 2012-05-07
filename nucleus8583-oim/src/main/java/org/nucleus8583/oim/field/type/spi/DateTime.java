package org.nucleus8583.oim.field.type.spi;

import java.io.Reader;
import java.io.Writer;
import java.text.SimpleDateFormat;

import org.nucleus8583.oim.field.type.Type;

public class DateTime extends Text {
	
	protected SimpleDateFormat sdf;
	
	protected String pattern;
	
	public DateTime() {
		super();
	}
	
	public DateTime(DateTime o) {
		super(o);
		
		this.pattern = o.pattern;
	}
	
	public void setPattern(String pattern) {
		this.sdf = new SimpleDateFormat(pattern);
		
		if (length <= 0) {
			setLength(pattern.length());
		}
	}
	
	public void initialize() throws Exception {
		super.initialize();
		
		if (pattern != null) {
			sdf = new SimpleDateFormat(pattern);
		}
	}
	
	public Object read(Reader in) throws Exception {
		String value = (String) super.read(in);
		
		synchronized (sdf) {
			return sdf.parse(value);
		}
	}

	public void write(Writer out, Object o) throws Exception {
		String value;
		
		if (o == null) {
			value = "";
		} else {
			synchronized (sdf) {
				value = sdf.format(o);
			}
		}

		super.write(out, value);
	}

	public Type clone() {
		return new DateTime(this);
	}
}
