package org.nucleus8583.oim.field.type.spi;

import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.nucleus8583.oim.field.Alignment;
import org.nucleus8583.oim.field.type.Type;
import org.nucleus8583.oim.util.TypeConverter;

public class SignedNumeric extends Text {
	
	protected int precision;
	
	protected RoundingMode roundingMode;
	
	protected boolean signFirst;
	
	protected String zero;

	public SignedNumeric() {
		padder.setAlign(Alignment.TRIMMED_RIGHT);
		padder.setPadWith('0');
		
		precision = 0;
		roundingMode = RoundingMode.UNNECESSARY;
		
		signFirst = false;
		
		zero = BigDecimal.ZERO.toPlainString();
	}
	
	public SignedNumeric(SignedNumeric o) {
		super(o);
		
		precision = o.precision;
		roundingMode = o.roundingMode;
		
		signFirst = o.signFirst;

		zero = BigDecimal.ZERO.movePointRight(precision).toPlainString();
	}

	public void setLength(int length) {
		super.setLength(length - 1);
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
	
	public void setSignFirst(boolean signFirst) {
		this.signFirst = signFirst;
	}

	public Object read(Reader in) throws Exception {
		String str;
		int strlen;
		
		int signum;
		
		if (signFirst) {
			int cc = in.read();
			if (cc < 0) {
				str = null;
				strlen = 0;
				
				signum = 0;
			} else {
				str = (String) super.read(in);
				strlen = str.length();
				
				if (cc == '-') {
					signum = -1;
				} else {
					signum = 1;
				}
			}
		} else {
			str = (String) super.read(in);
			strlen = str.length();
			
			int cc = in.read();
			if (cc < 0) {
				if (strlen == 0) {
					signum = 0;
				} else {
					--strlen;
					
					cc = str.charAt(strlen);
					str = str.substring(0, strlen);
					
					if (cc == '-') {
						signum = -1;
					} else {
						signum = 1;
					}
				}
			} else {
				if (cc == '-') {
					signum = -1;
				} else {
					signum = 1;
				}
			}
		}
		
		BigDecimal bd;
		
		switch (strlen) {
		case 0:
			bd = BigDecimal.ZERO;
			break;
		case 1:
			bd = BigDecimal.ZERO;
			break;
		default:
			try {
				bd = new BigDecimal(str).movePointLeft(precision);
				
				if (signum < 0) {
					bd = bd.negate();
				}
			} catch (Throwable t) {
				bd = null;
			}
			
			break;
		}
		
		return bd;
	}

	public void write(Writer out, Object o) throws Exception {
		String str;
		char signchar;
		
		if (o == null) {
			str = zero;
			signchar = '+';
		} else {
			BigDecimal bd;

			if (o instanceof BigDecimal) {
				bd = (BigDecimal) o;
			} else {
				bd = TypeConverter.convertToBigDecimal(o);
			}
			
			bd = bd.setScale(precision, roundingMode).movePointRight(precision);
			
			if (bd.compareTo(BigDecimal.ZERO) >= 0) {
				signchar = '+';
			} else {
				bd = bd.negate();
				signchar = '-';
			}
			
			str = bd.toPlainString();
		}
		
		if (signFirst) {
			out.write(signchar);
			super.write(out, str);
		} else {
			super.write(out, str);
			out.write(signchar);
		}
	}
	
	public Type clone() {
		return new SignedNumeric(this);
	}
}
