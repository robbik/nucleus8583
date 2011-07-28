package org.nucleus8583.oim.processor;

import java.lang.reflect.Field;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

import org.nucleus8583.Iso8583Message;
import org.nucleus8583.oim.util.BeanUtils;

public final class Iso8583MessageComponent implements HasFieldsComponent {
	private final String name;

	private final Class<?> _class;

	private final Map<String, Field> fields;

	private final int[] isoFieldsNumber;

	private final BaseComponent[] isoFieldsComponent;

	private final int isoFieldsCount;

	public Iso8583MessageComponent(String name, Class<?> _class,
			BaseComponent[] isoFieldsComponent, int[] isoFieldsNumber,
			int isoFieldsCount) {
		this.name = name;
		this._class = _class;

		this.isoFieldsComponent = isoFieldsComponent;
		this.isoFieldsNumber = isoFieldsNumber;
		this.isoFieldsCount = isoFieldsCount;

		fields = new HashMap<String, Field>();
		BeanUtils.collectFields(_class, fields);
	}

	public void encode(Iso8583Message msg, Object pojo,
			Map<String, Object> session) {
		BaseComponent component;
		int isoNo;

		for (int i = 0; i < isoFieldsCount; ++i) {
			component = isoFieldsComponent[i];
			isoNo = isoFieldsNumber[i];

			if (isoNo == 0) {
				String value = component.encodeToString(pojo, session);
				if (value == null) {
					msg.unsetMti();
				} else {
					msg.setMti(value);
				}
			} else {
				if (component.isBinary()) {
					BitSet value = component.encodeToBinary(pojo, session);
					if (value == null) {
						msg.unsafeUnset(isoNo);
					} else {
						msg.unsafeSet(isoNo, value);
					}
				} else {
					String value = component.encodeToString(pojo, session);
					if (value == null) {
						msg.unsafeUnset(isoNo);
					} else {
						msg.unsafeSet(isoNo, value);
					}
				}
			}
		}
	}

	public void decode(Iso8583Message msg, Object pojo,
			Map<String, Object> session) {
		BaseComponent component;
		int isoNo;

		for (int i = 0; i < isoFieldsCount; ++i) {
			component = isoFieldsComponent[i];
			isoNo = isoFieldsNumber[i];

			if (isoNo == 0) {
				component.decode(msg.getMti(), pojo, session);
			} else {
				if (component.isBinary()) {
					BitSet value = msg.unsafeGetBinary(isoNo);
					if (value != null) {
						component.decode(value, pojo, session);
					}
				} else {
					String value = msg.unsafeGetString(isoNo);
					if (value != null) {
						component.decode(value, pojo, session);
					}
				}
			}
		}
	}

	public Object decode(Iso8583Message msg, Map<String, Object> session) {
		Object pojo;

		try {
			pojo = _class.newInstance();
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}

		decode(msg, pojo, session);
		return pojo;
	}

	public String getName() {
		return name;
	}

	public Field getField(String name) {
		return fields.get(name);
	}

	public Map<String, Field> getFields() {
		return fields;
	}
}
