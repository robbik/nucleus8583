package org.nucleus8583.core.logging;

import org.slf4j.LoggerFactory;

public class Slf4jLogger implements Logger {

	private org.slf4j.Logger logger;

	public Slf4jLogger(Class<?> clazz) {
		logger = LoggerFactory.getLogger(clazz);
	}

	public Slf4jLogger(String name) {
		logger = LoggerFactory.getLogger(name);
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
