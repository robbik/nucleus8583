package org.nucleus8583.oim.component;

import java.io.IOException;
import java.io.Reader;
import java.util.BitSet;
import java.util.Map;

public abstract class BaseComponent {
	public boolean isBinary() {
		throw new UnsupportedOperationException();
	}

	public void decode(Reader reader, Object pojo, Map<String, Object> session)
			throws IOException {
		throw new UnsupportedOperationException();
	}

	public Object decode(Reader reader, Map<String, Object> session)
			throws IOException {
		throw new UnsupportedOperationException();
	}

	public void decode(String value, Object pojo, Map<String, Object> session) {
		throw new UnsupportedOperationException();
	}

	public Object decode(String value, Map<String, Object> session) {
		throw new UnsupportedOperationException();
	}

	public void decode(BitSet value, Object pojo, Map<String, Object> session) {
		throw new UnsupportedOperationException();
	}

	public BitSet decode(BitSet value, Map<String, Object> session) {
		throw new UnsupportedOperationException();
	}

	public void encode(StringBuilder sb, Object pojo,
			Map<String, Object> session) {
		throw new UnsupportedOperationException();
	}

	public String encodeToString(Object pojo, Map<String, Object> session) {
		StringBuilder sb = new StringBuilder();
		encode(sb, pojo, session);

		return sb.toString();
	}

	public BitSet encodeToBinary(Object pojo, Map<String, Object> session) {
		throw new UnsupportedOperationException();
	}
}
