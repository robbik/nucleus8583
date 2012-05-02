package rk.commons.logging;

public abstract class LoggerFactory {

	private static final String[] loggers = {
			"rk.commons.logging.Slf4jLogger",
			"rk.commons.logging.CommonsLogger",
			"rk.commons.logging.JdkLogger" };

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
