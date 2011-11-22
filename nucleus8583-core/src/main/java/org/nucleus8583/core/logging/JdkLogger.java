package org.nucleus8583.core.logging;

import java.util.logging.Level;

public class JdkLogger implements Logger {
	
	private java.util.logging.Logger logger;
	
	public JdkLogger(Class<?> clazz) {
		this(clazz.getName());
	}
	
	public JdkLogger(String name) {
		logger = java.util.logging.Logger.getLogger(name);
	}

	public void info(String msg) {
		logger.info(msg);
	}

	public void warning(String msg) {
		logger.warning(msg);
	}
	
	public void warning(String msg, Throwable t) {
		logger.log(Level.WARNING, msg, t);
	}
}
