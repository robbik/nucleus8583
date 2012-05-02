package rk.commons.beans.factory.support;

public abstract class FactoryBean<T> {

	private T singleton;

	public FactoryBean() {
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
