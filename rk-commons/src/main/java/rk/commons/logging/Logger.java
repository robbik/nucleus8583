package rk.commons.logging;

public interface Logger {
	
	boolean isTraceEnabled();
	
	void trace(String msg);

	void info(String msg);
	
	void warning(String msg);
	
	void warning(String msg, Throwable t);
	
	void error(String msg);
	
	void error(String msg, Throwable t);
}
