package org.nucleus8583.oim.field.type.spi;

import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.nucleus8583.oim.field.Alignment;
import org.nucleus8583.oim.field.type.Type;

public class Numeric extends Text {

	private static final long serialVersionUID = -5615324004502124085L;
	
	protected int precision;
	
	protected RoundingMode roundingMode;

	public Numeric() {
		padder.setAlign(Alignment.TRIMMED_RIGHT);
		padder.setPadWith('0');
	}
	
	public Numeric(Numeric o) {
		super(o);
		
		precision = o.precision;
	}
	
	public void setPrecision(int precision) {
		if (precision < 0) {
			throw new IllegalArgumentException("precision must greater or equals than zero");
		}
		
		this.precision = precision;
	}
	
	public void setRoundingMode(String roundingMode) {
		this.roundingMode = RoundingMode.valueOf(roundingMode);
	}
	
	public void setRoundingMode(RoundingMode roundingMode) {
		this.roundingMode = roundingMode;
	}

	public Object read(Reader in) throws Exception {
		BigDecimal bd;
		
		try {
			bd = new BigDecimal((String) super.read(in));
		} catch (Throwable t) {
			return null;
		}
		
		return bd.movePointLeft(precision);
	}

	public void write(Writer out, Object o) throws Exception {
		BigDecimal bd = (BigDecimal) o;
		
		if (bd == null) {
			bd = BigDecimal.ZERO;
		} else {
			bd = bd.setScale(precision, roundingMode);
		}
		
		super.write(out, bd.movePointRight(precision).toPlainString());
	}
	
	public Type clone() {
		return new Numeric(this);
	}
}
