package org.nucleus8583.oim.component;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Map;

import org.nucleus8583.oim.util.FastStringReader;

public final class ArrayComponent extends BaseComponent {
	private final int no;

	private final String name;

	private final Field field;

	private final Class<?> componentType;

	private final Field sizeBasic;

	private final String sizeTransient;

	private final DataStructureComponent dataStructure;

	public ArrayComponent(HasFieldsComponent parent, int no, String name,
			String size, DataStructureComponent dscomp) {
		this.no = no;
		this.name = name;

		field = parent.getField(name);
		componentType = field.getType().getComponentType();

		if (size == null) {
			sizeBasic = null;
			sizeTransient = null;
		} else if (size.startsWith("transient:")) {
			sizeBasic = null;
			sizeTransient = size.substring(10);
		} else {
			// basic:
			size = size.substring(6);

			sizeBasic = parent.getField(size.substring(10));
			if (sizeBasic == null) {
				throw new IllegalArgumentException("field " + size
						+ " cannot be found");
			}
			sizeTransient = null;
		}

		dataStructure = dscomp;
	}

	public int getNo() {
		return no;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean isBinary() {
		return false;
	}

	@Override
	public void encode(StringBuilder sb, Object pojo,
			Map<String, Object> session) {
		Object array;

		try {
			array = field.get(pojo);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}

		if (array == null) {
			return;
		}

		int count;

		if (sizeTransient != null) {
			count = ((Integer) session.get(sizeTransient)).intValue();
		} else if (sizeBasic != null) {
			try {
				count = sizeBasic.getInt(pojo);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		} else {
			count = Array.getLength(array);
		}

		for (int i = 0; i < count; ++i) {
			dataStructure.encode(sb, Array.get(array, i), session);
		}
	}

	@Override
	public String encodeToString(Object pojo, Map<String, Object> session) {
		Object array;

		try {
			array = field.get(pojo);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}

		if (array == null) {
			return null;
		}

		StringBuilder sb = new StringBuilder();
		int count;

		if (sizeTransient != null) {
			count = ((Integer) session.get(sizeTransient)).intValue();
		} else if (sizeBasic != null) {
			try {
				count = sizeBasic.getInt(pojo);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		} else {
			count = Array.getLength(array);
		}

		for (int i = 0; i < count; ++i) {
			dataStructure.encode(sb, Array.get(array, i), session);
		}

		return sb.toString();
	}

	@Override
	public void decode(String value, Object pojo, Map<String, Object> session) {
		Reader reader = new FastStringReader(value);
		int count;

		if (sizeTransient != null) {
			count = ((Integer) session.get(sizeTransient)).intValue();
		} else if (sizeBasic != null) {
			try {
				count = sizeBasic.getInt(pojo);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		} else {
			throw new IllegalStateException("please set size attribute value");
		}

		Object array = Array.newInstance(componentType, count);
		try {
			for (int i = 0; i < count; ++i) {
				Array.set(array, i, dataStructure.decode(reader, session));
			}
		} catch (IOException ex) {
			throw new RuntimeException("internal error", ex);
		}

		try {
			field.set(pojo, array);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void decode(Reader reader, Object pojo, Map<String, Object> session)
			throws IOException {
		int count;

		if (sizeTransient != null) {
			count = ((Integer) session.get(sizeTransient)).intValue();
		} else if (sizeBasic != null) {
			try {
				count = sizeBasic.getInt(pojo);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		} else {
			throw new IllegalStateException("please set size attribute value");
		}

		Object array = Array.newInstance(componentType, count);
		for (int i = 0; i < count; ++i) {
			Array.set(array, i, dataStructure.decode(reader, session));
		}

		try {
			field.set(pojo, array);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
}
