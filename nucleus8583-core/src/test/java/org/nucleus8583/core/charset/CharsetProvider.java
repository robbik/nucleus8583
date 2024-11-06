package org.nucleus8583.core.charset;

public interface CharsetProvider {
	CharsetDecoder getDecoder();

	CharsetEncoder getEncoder();
}
