package rk.commons.logging;

public class CommonsLogger implements Logger {

	private org.apache.commons.logging.Log logger;

	public CommonsLogger(Class<?> clazz) {
		logger = org.apache.commons.logging.LogFactory.getLog(clazz);
	}

	public CommonsLogger(String name) {
		logger = org.apache.commons.logging.LogFactory.getLog(name);
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
