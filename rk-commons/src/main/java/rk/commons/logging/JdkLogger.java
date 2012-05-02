package rk.commons.logging;

import java.util.logging.Level;

public class JdkLogger implements Logger {
	
	private java.util.logging.Logger logger;
	
	public JdkLogger(Class<?> clazz) {
		this(clazz.getName());
	}
	
	public JdkLogger(String name) {
		logger = java.util.logging.Logger.getLogger(name);
	}

	public boolean isTraceEnabled() {
		return logger.isLoggable(Level.FINER);
	}

	public void trace(String msg) {
		logger.finer(msg);
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

	public void error(String msg) {
		logger.severe(msg);
	}

	public void error(String msg, Throwable t) {
		logger.log(Level.SEVERE, msg, t);
	}
}
