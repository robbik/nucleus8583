package org.nucleus8583.oim.processor;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.nucleus8583.oim.util.FastStringReader;

public final class ListComponent extends BaseComponent {
	private final int no;

	private final String name;

	private final Field field;

	private final Field sizeBasic;

	private final String sizeTransient;

	private final boolean append;

	private final DataStructureComponent dataStructure;

	public ListComponent(HasFieldsComponent parent, int no, String name,
			String size, boolean append, DataStructureComponent dscomp) {
		this.no = no;
		this.name = name;

		field = parent.getField(name);

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

		this.append = append;
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
		List<?> list;

		try {
			list = (List<?>) field.get(pojo);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}

		if (list == null) {
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
			count = list.size();
		}

		for (int i = 0; i < count; ++i) {
			dataStructure.encode(sb, list.get(i), session);
		}
	}

	@Override
	public String encodeToString(Object pojo, Map<String, Object> session) {
		List<?> list;

		try {
			list = (List<?>) field.get(pojo);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}

		if (list == null) {
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
			count = list.size();
		}

		for (int i = 0; i < count; ++i) {
			dataStructure.encode(sb, list.get(i), session);
		}

		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void decode(String value, Object pojo, Map<String, Object> session) {
		List list;

		try {
			if (append) {
				list = (List) field.get(pojo);
				if (list == null) {
					list = new ArrayList<Object>();
					field.set(pojo, list);
				}
			} else {
				list = new ArrayList<Object>();
				field.set(pojo, list);
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}

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

		try {
			for (int i = 0; i < count; ++i) {
				list.add(dataStructure.decode(reader, session));
			}
		} catch (IOException ex) {
			throw new RuntimeException("internal error", ex);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void decode(Reader reader, Object pojo, Map<String, Object> session)
			throws IOException {
		List list;

		try {
			list = (List) field.get(pojo);
			if (append) {
				list = (List) field.get(pojo);
				if (list == null) {
					list = new ArrayList<Object>();
					field.set(pojo, list);
				}
			} else {
				list = new ArrayList<Object>();
				field.set(pojo, list);
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
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
			throw new IllegalStateException("please set size attribute value");
		}

		for (int i = 0; i < count; ++i) {
			list.add(dataStructure.decode(reader, session));
		}
	}
}
