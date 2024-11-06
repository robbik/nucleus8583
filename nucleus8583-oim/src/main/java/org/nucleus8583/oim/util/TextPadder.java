package org.nucleus8583.oim.util;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;

import org.nucleus8583.oim.field.Alignment;

public class TextPadder {

	private char padWith;

	private char align;

	private int length;

	private char[] padder;

	private char[] emptyValue;
	
	public TextPadder() {
		// do nothing
	}
	
	public TextPadder(TextPadder o) {
		padWith = o.padWith;
		align = o.align;
		
		length = o.length;
		
		padder = o.padder;
		emptyValue = o.emptyValue;
	}

	public void setPadWith(char padWith) {
		this.padWith = padWith;
	}

	public void setAlign(Alignment align) {
		this.align = align.symbolicValue();
	}

	public Alignment getAlign() {
		return Alignment.enumValueOf(align);
	}

	public void setLength(int length) {
		this.length = length;
	}

	public void setEmptyValue(char[] emptyValue) {
		this.emptyValue = emptyValue;
	}
	
	public char[] getEmptyValue() {
		return emptyValue;
	}

	public void initialize() {
		padder = new char[length];
		Arrays.fill(padder, padWith);
	}

	public void pad(Writer out, String value, int valueLength) throws IOException {
		if (valueLength == 0) {
			out.write(padder, 0, length);
		} else if (valueLength == length) {
			out.write(value, 0, valueLength);
		} else {
			switch (align) {
			case 'l':
            case 'L':
            	out.write(value, 0, valueLength);
            	out.write(padder, 0, length - valueLength);

				break;
            case 'r':
            case 'R':
            	out.write(padder, 0, length - valueLength);
            	out.write(value, 0, valueLength);

				break;
			default: // NONE
				out.write(value, 0, valueLength);
				out.write(padder, 0, length - valueLength);

				break;
			}
		}
	}

	public char[] unpad(char[] value, int length) throws IOException {
		char[] result;
		int resultLength;

		switch (align) {
		case 'l':
			resultLength = 0;

			for (int i = length - 1; i >= 0; --i) {
				if (value[i] != padWith) {
					resultLength = i + 1;
					break;
				}
			}

			if (resultLength == 0) {
				result = emptyValue;
			} else if (resultLength == length) {
				result = value;
			} else {
				result = new char[resultLength];
				System.arraycopy(value, 0, result, 0, resultLength);
			}

			break;
		case 'r':
			int padLength = length;

			for (int i = 0; i < length; ++i) {
				if (value[i] != padWith) {
					padLength = i;
					break;
				}
			}

			if (padLength == 0) {
				result = value;
			} else if (padLength == length) {
				result = emptyValue;
			} else {
				resultLength = length - padLength;

				result = new char[resultLength];
				System.arraycopy(value, padLength, result, 0, resultLength);
			}

			break;
		default: // NONE, UNTRIMMED_LEFT, UNTRIMMED_RIGHT
			result = value;
			break;
		}

		return result;
	}
}
