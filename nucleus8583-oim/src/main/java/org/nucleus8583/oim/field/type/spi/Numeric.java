package org.nucleus8583.oim.field.type.spi;

import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

import org.nucleus8583.oim.field.Alignment;
import org.nucleus8583.oim.field.type.Type;
import org.nucleus8583.oim.util.TypeConverter;

public class Numeric extends Text {

	protected int precision;
	
	protected RoundingMode roundingMode;
	
	protected String zero;

	public Numeric() {
		padder.setAlign(Alignment.TRIMMED_RIGHT);
		padder.setPadWith('0');

		precision = 0;
		roundingMode = RoundingMode.UNNECESSARY;
		
		zero = BigDecimal.ZERO.toPlainString();
	}
	
	public Numeric(Numeric o) {
		super(o);
		
		precision = o.precision;
		roundingMode = o.roundingMode;

		zero = BigDecimal.ZERO.movePointRight(precision).toPlainString();
	}
	
	public void setPrecision(int precision) {
		if (precision < 0) {
			throw new IllegalArgumentException("precision must greater or equals than zero");
		}
		
		this.precision = precision;
		zero = BigDecimal.ZERO.movePointRight(precision).toPlainString();
	}
	
	public void setRoundingMode(String roundingMode) {
		if (roundingMode == null) {
			throw new NullPointerException("roundingMode");
		}

		try {
			this.roundingMode = RoundingMode.valueOf(roundingMode);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("invalid rounding mode " + roundingMode);
		}
	}
	
	public void setRoundingMode(RoundingMode roundingMode) {
		if (roundingMode == null) {
			throw new NullPointerException("roundingMode");
		}

		this.roundingMode = roundingMode;
	}

	@Override
	public Object read(Reader in, Map<String, Object> tmp) throws Exception {
		BigDecimal bd;
		
		try {
			bd = new BigDecimal((String) super.read(in, tmp));
		} catch (Throwable t) {
			return null;
		}
		
		return bd.movePointLeft(precision);
	}

	@Override
	public void write(Writer out, Object o, Map<String, Object> tmp) throws Exception {
		String str;
		
		if (o == null) {
			str = zero;
		} else {
			BigDecimal bd;
			
			if (o instanceof BigDecimal) {
				bd = (BigDecimal) o;
			} else {
				bd = TypeConverter.convertToBigDecimal(o);
			}
			
			bd = bd.setScale(precision, roundingMode);
			str = bd.movePointRight(precision).toPlainString();
		}
		
		super.write(out, str, tmp);
	}
	
	@Override
	public Type clone() {
		return new Numeric(this);
	}
}
