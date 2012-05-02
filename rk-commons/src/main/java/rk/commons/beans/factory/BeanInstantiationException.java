package rk.commons.beans.factory;

public class BeanInstantiationException extends RuntimeException {

	private static final long serialVersionUID = -3589899663174222479L;

	private final String beanQName;

	public BeanInstantiationException(String beanQName) {
		super("bean " + beanQName + " cannot be instantiated");

		this.beanQName = beanQName;
	}

	public BeanInstantiationException(String beanQName, Throwable cause) {
		super("bean " + beanQName + " cannot be instantiated", cause);

		this.beanQName = beanQName;
	}

	public String getBeanQName() {
		return beanQName;
	}
}
