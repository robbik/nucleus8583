package org.nucleus8583.oim.field.type.spi;

import java.io.Reader;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.nucleus8583.oim.field.Alignment;
import org.nucleus8583.oim.field.type.Type;

public class SignedNumeric extends Text {

	private static final long serialVersionUID = -5615324004502124085L;
	
	protected int precision;
	
	protected RoundingMode roundingMode;
	
	protected boolean signFirst;

	public SignedNumeric() {
		padder.setAlign(Alignment.TRIMMED_RIGHT);
		padder.setPadWith('0');
		
		precision = 0;
		roundingMode = RoundingMode.CEILING;
		
		signFirst = false;
	}
	
	public SignedNumeric(SignedNumeric o) {
		super(o);
		
		precision = o.precision;
		roundingMode = o.roundingMode;
		
		signFirst = o.signFirst;
	}

	public void setLength(int length) {
		super.setLength(length - 1);
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
		BigDecimal bd = (BigDecimal) o;
		char signchar;
		
		if (bd == null) {
			bd = BigDecimal.ZERO;
			signchar = '+';
		} else {
			bd = bd.setScale(precision, roundingMode).movePointRight(precision);
			
			if (bd.compareTo(BigDecimal.ZERO) >= 0) {
				signchar = '+';
			} else {
				bd = bd.negate();
				signchar = '-';
			}
		}
		
		if (signFirst) {
			out.write(signchar);
			super.write(out, bd.toPlainString());
		} else {
			super.write(out, bd.toPlainString());
			out.write(signchar);
		}
	}
	
	public Type clone() {
		return new SignedNumeric(this);
	}
}
