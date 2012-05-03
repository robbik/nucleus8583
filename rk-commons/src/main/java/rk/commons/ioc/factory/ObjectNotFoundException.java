package rk.commons.ioc.factory;

public class ObjectNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -3589899663174222479L;

	private final String objectQName;

	public ObjectNotFoundException(String objectQName) {
		super("object " + objectQName + " cannot be found");

		this.objectQName = objectQName;
	}

	public ObjectNotFoundException(String objectQName, Throwable cause) {
		super("object " + objectQName + " cannot be found", cause);

		this.objectQName = objectQName;
	}

	public String getObjectQName() {
		return objectQName;
	}
}
