package org.nucleus8583.oim.accessor;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.nucleus8583.oim.util.BeanUtils;
import org.nucleus8583.oim.util.FastStringReader;
import org.nucleus8583.oim.util.StringUtils;

public final class DataStructureComponent extends BaseComponent implements
		HasFieldsComponent {
	private final int no;

	private final String name;

	private final Field field;

	private final Class<?> _class;

	private final Map<String, Field> fields;

	private final BaseComponent[] childComponents;

	private final int childrenCount;

	public DataStructureComponent(HasFieldsComponent parent, int no,
			String className, String name, BaseComponent[] childComponents,
			boolean ignoreField) {

		this.no = no;

		if (StringUtils.hasText(className)) {
			try {
				_class = Class.forName(className, true, Thread.currentThread()
						.getContextClassLoader());
			} catch (ClassNotFoundException e) {
				throw new IllegalArgumentException("unable to find class "
						+ className);
			}

			fields = new HashMap<String, Field>();
			BeanUtils.collectFields(_class, fields);
		} else {
			_class = null;
			fields = parent.getFields();
		}

		this.name = name;

		if (ignoreField || (name == null)) {
			field = null;
		} else {
			field = fields.get(name);
		}

		this.childComponents = childComponents;
		childrenCount = childComponents == null ? 0 : childComponents.length;
	}

	public int getNo() {
		return no;
	}

	public String getName() {
		return name;
	}

	public boolean hasField() {
		return field != null;
	}

	@Override
	public boolean isBinary() {
		return false;
	}

	@Override
	public void encode(StringBuilder sb, Object pojo,
			Map<String, Object> session) {
		if (field != null) {
			try {
				pojo = field.get(pojo);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}

		for (int i = 0; i < childrenCount; ++i) {
			childComponents[i].encode(sb, pojo, session);
		}
	}

	@Override
	public String encodeToString(Object pojo, Map<String, Object> session) {
		StringBuilder sb = new StringBuilder();
		encode(sb, pojo, session);

		return sb.toString();
	}

	@Override
	public void decode(Reader reader, Object pojo, Map<String, Object> session)
			throws IOException {
		if ((field != null) && (_class != null)) {
			Object newpojo;

			try {
				newpojo = _class.newInstance();
				field.set(pojo, newpojo);
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}

			pojo = newpojo;
		}

		for (int i = 0; i < childrenCount; ++i) {
			childComponents[i].decode(reader, pojo, session);
		}
	}

	@Override
	public Object decode(Reader reader, Map<String, Object> session)
			throws IOException {
		if (_class == null) {
			return null;
		}

		Object pojo;

		try {
			pojo = _class.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}

		for (int i = 0; i < childrenCount; ++i) {
			childComponents[i].decode(reader, pojo, session);
		}

		return pojo;
	}

	@Override
	public void decode(String value, Object pojo, Map<String, Object> session) {
		if ((field != null) && (_class != null)) {
			Object newpojo;

			try {
				newpojo = _class.newInstance();
				field.set(pojo, newpojo);
			} catch (InstantiationException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}

			pojo = newpojo;
		}

		Reader reader = new FastStringReader(value);

		try {
			for (int i = 0; i < childrenCount; ++i) {
				childComponents[i].decode(reader, pojo, session);
			}
		} catch (IOException e) {
			throw new RuntimeException("internal error", e);
		}
	}

	@Override
	public Object decode(String value, Map<String, Object> session) {
		if (_class == null) {
			return null;
		}

		Reader reader = new FastStringReader(value);
		Object pojo;

		try {
			pojo = _class.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}

		try {
			for (int i = 0; i < childrenCount; ++i) {
				childComponents[i].decode(reader, pojo, session);
			}
		} catch (IOException e) {
			throw new RuntimeException("internal error", e);
		}

		return pojo;
	}

	public Field getField(String name) {
		return fields.get(name);
	}

	public Map<String, Field> getFields() {
		return fields;
	}
}
