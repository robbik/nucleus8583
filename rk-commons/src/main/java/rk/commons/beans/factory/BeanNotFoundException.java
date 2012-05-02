package rk.commons.beans.factory;

public class BeanNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -3589899663174222479L;

	private final String beanQName;

	public BeanNotFoundException(String beanQName) {
		super("bean " + beanQName + " cannot be found");

		this.beanQName = beanQName;
	}

	public BeanNotFoundException(String beanQName, Throwable cause) {
		super("bean " + beanQName + " cannot be found", cause);

		this.beanQName = beanQName;
	}

	public String getBeanQName() {
		return beanQName;
	}
}
