package rk.commons.ioc.factory.config;

public class ObjectReference {

	private final String objectQName;

	public ObjectReference(String objectQName) {
		this.objectQName = objectQName;
	}

	public String getObjectQName() {
		return objectQName;
	}
}
