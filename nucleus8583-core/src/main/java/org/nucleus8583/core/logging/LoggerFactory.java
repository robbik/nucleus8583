package org.nucleus8583.core.logging;

public abstract class LoggerFactory {

	private static final String[] loggers = {
			"org.nucleus8583.core.logging.Slf4jLogger",
			"org.nucleus8583.core.logging.CommonsLogger",
			"org.nucleus8583.core.logging.JdkLogger" };

	public static Logger getLogger(Class<?> clazz) {
		for (int i = 0, length = loggers.length; i < length; ++i) {
			try {
				return (Logger) Class.forName(loggers[i])
						.getConstructor(Class.class).newInstance(clazz);
			} catch (Throwable t) {
				// do nothing
			}
		}

		return NoopLogger.INSTANCE;
	}

	public static Logger getLogger(String name) {
		for (int i = 0, length = loggers.length; i < length; ++i) {
			try {
				return (Logger) Class.forName(loggers[i])
						.getConstructor(String.class).newInstance(name);
			} catch (Throwable t) {
				// do nothing
			}
		}

		return NoopLogger.INSTANCE;
	}
}
