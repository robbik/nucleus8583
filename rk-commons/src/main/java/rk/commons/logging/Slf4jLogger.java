package rk.commons.logging;

import org.slf4j.LoggerFactory;

public class Slf4jLogger implements Logger {

	private org.slf4j.Logger logger;

	public Slf4jLogger(Class<?> clazz) {
		logger = LoggerFactory.getLogger(clazz);
	}

	public Slf4jLogger(String name) {
		logger = LoggerFactory.getLogger(name);
	}

	public boolean isTraceEnabled() {
		return logger.isTraceEnabled();
	}

	public void trace(String msg) {
		logger.trace(msg);
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

	public void error(String msg) {
		logger.error(msg);
	}

	public void error(String msg, Throwable t) {
		logger.error(msg, t);
	}
}
