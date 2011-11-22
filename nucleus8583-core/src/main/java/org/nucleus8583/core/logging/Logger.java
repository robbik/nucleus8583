package org.nucleus8583.core.logging;

public interface Logger {

	void info(String msg);
	
	void warning(String msg);
	
	void warning(String msg, Throwable t);
}
