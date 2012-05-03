package rk.commons.ioc.factory.support;

public abstract class ObjectFactory<T> {

	private T singleton;

	public ObjectFactory() {
		singleton = null;
	}

	public T getObject() {
		if (singleton == null) {
			singleton = createInstance();
		}

		return singleton;
	}

	protected abstract T createInstance();
}
