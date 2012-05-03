package rk.commons.ioc.factory;

public class ObjectInstantiationException extends RuntimeException {

	private static final long serialVersionUID = -3589899663174222479L;

	private final String objectQName;

	public ObjectInstantiationException(String objectQName) {
		super("object " + objectQName + " cannot be instantiated");

		this.objectQName = objectQName;
	}

	public ObjectInstantiationException(String objectQName, Throwable cause) {
		super("object " + objectQName + " cannot be instantiated", cause);

		this.objectQName = objectQName;
	}

	public String getObjectQName() {
		return objectQName;
	}
}
