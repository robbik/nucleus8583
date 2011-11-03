package org.nucleus8583.core.charset;

public class Utf8Provider implements CharsetProvider {

	private final Utf8Decoder decoder;

	private final Utf8Encoder encoder;

	public Utf8Provider() {
		decoder = new Utf8Decoder();
		encoder = new Utf8Encoder();
	}

	public CharsetEncoder getEncoder() {
		return encoder;
	}

	public CharsetDecoder getDecoder() {
		return decoder;
	}
}
