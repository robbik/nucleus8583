package rk.commons.beans.factory.support;

public class ManagedArray<E> {

	private final E[] values;

	@SuppressWarnings("unchecked")
	public ManagedArray(int size) {
		values = (E[]) new Object[size];
	}

	public void set(int index, E value) {
		values[index] = value;
	}

	public E get(int index) {
		return values[index];
	}

	public int length() {
		return values.length;
	}
}
