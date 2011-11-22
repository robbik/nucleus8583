package org.nucleus8583.core.logging;

public class CommonsLogger implements Logger {

	private org.apache.commons.logging.Log logger;

	public CommonsLogger(Class<?> clazz) {
		logger = org.apache.commons.logging.LogFactory.getLog(clazz);
	}

	public CommonsLogger(String name) {
		logger = org.apache.commons.logging.LogFactory.getLog(name);
	}

	public void info(String msg) {
		logger.info(msg);
	}

	public void warning(String msg) {
		logger.warn(msg);
	}

	public void warning(String msg, Throwable t) {
		logger.warn(msg, t);
	}
}
