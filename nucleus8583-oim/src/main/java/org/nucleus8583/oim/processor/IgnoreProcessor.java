package org.nucleus8583.oim.processor;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;

import org.nucleus8583.oim.xml.IgnoreDefinition;

public final class IgnoreProcessor implements Processor {
	private final String filler;

	private final int length;

	public IgnoreProcessor(IgnoreDefinition def) {
		
		
		this.filler = filler;
		this.length = length;
	}

	private void skip(Reader reader, int length) throws IOException {
		int crem = length;
		int dcrem;

		while (crem > 0) {
			dcrem = (int) reader.skip(length);
			if (dcrem <= 0) {
				throw new EOFException();
			}

			crem -= dcrem;
		}
	}

	@Override
	public boolean isBinary() {
		return false;
	}

	@Override
	public void encode(StringBuilder sb, Object pojo,
			Map<String, Object> session) {
		sb.append(filler);
	}

	@Override
	public String encodeToString(Object pojo, Map<String, Object> session) {
		return filler;
	}

	@Override
	public void decode(String value, Object pojo, Map<String, Object> session) {
		// do nothing
	}

	@Override
	public void decode(Reader reader, Object pojo, Map<String, Object> session)
			throws IOException {
		skip(reader, length);
	}
}
