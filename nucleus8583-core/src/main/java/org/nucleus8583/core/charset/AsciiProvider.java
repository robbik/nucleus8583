package org.nucleus8583.core.charset;

public class AsciiProvider implements CharsetProvider {

	private final AsciiDecoder decoder;

	private final AsciiEncoder encoder;

	public AsciiProvider() {
		decoder = new AsciiDecoder();
		encoder = new AsciiEncoder();
	}

	public CharsetEncoder getEncoder() {
		return encoder;
	}

	public CharsetDecoder getDecoder() {
		return decoder;
	}
}
