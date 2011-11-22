package org.nucleus8583.core.logging;

public class NoopLogger implements Logger {
	
	public static final Logger INSTANCE = new NoopLogger();
	
	private NoopLogger() {
		// do nothing
	}

	public void info(String msg) {
		// do nothing
	}

	public void warning(String msg) {
		// do nothing
	}

	public void warning(String msg, Throwable t) {
		// do nothing
	}
}
