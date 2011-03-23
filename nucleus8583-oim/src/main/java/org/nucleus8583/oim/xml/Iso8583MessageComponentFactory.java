package org.nucleus8583.oim.xml;

import org.nucleus8583.oim.component.ArrayComponent;
import org.nucleus8583.oim.component.BaseComponent;
import org.nucleus8583.oim.component.BasicComponent;
import org.nucleus8583.oim.component.DataStructureComponent;
import org.nucleus8583.oim.component.Iso8583MessageComponent;
import org.nucleus8583.oim.component.ListComponent;
import org.nucleus8583.oim.component.TransientComponent;
import org.nucleus8583.oim.util.StringUtil;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class Iso8583MessageComponentFactory {
	private static BasicComponent validate(BasicComponent comp) {
		if (comp.getNo() < 0) {
			throw new IllegalArgumentException(
					"no attribute is required for basic " + comp.getName());
		}

		return comp;
	}

	private static TransientComponent validate(TransientComponent comp) {
		if (comp.getNo() < 0) {
			throw new IllegalArgumentException(
					"no attribute is required for transient " + comp.getName());
		}

		return comp;
	}

	private static DataStructureComponent validate(DataStructureComponent comp) {
		if (comp.getNo() < 0) {
			throw new IllegalArgumentException(
					"no attribute is required for data structure "
							+ comp.getName());
		}

		return comp;
	}

	private static BaseComponent validate(BaseComponent base) {
		if (ArrayComponent.class.isInstance(base)) {
			ArrayComponent comp = (ArrayComponent) base;

			if (comp.getNo() < 0) {
				throw new IllegalArgumentException(
						"no attribute is required for list " + comp.getName());
			}
		} else if (ListComponent.class.isInstance(base)) {
			ListComponent comp = (ListComponent) base;

			if (comp.getNo() < 0) {
				throw new IllegalArgumentException(
						"no attribute is required for list " + comp.getName());
			}
		}

		return base;
	}

	public static Iso8583MessageComponent parse(Iso8583MessagesFactory factory,
			Element node) {
		String s_name = node.getAttribute("name");
		String s_className = node.getAttribute("class");

		// name
		String name;

		if (StringUtil.nullOrEmpty(s_name, true)) {
			throw new IllegalArgumentException("name is required");
		}

		name = s_name.trim();

		// class
		Class<?> _class;

		if (StringUtil.nullOrEmpty(s_className, true)) {
			throw new IllegalArgumentException("class is required");
		}

		s_className = s_className.trim();

		try {
			_class = Class.forName(s_className, true, Thread.currentThread()
					.getContextClassLoader());
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("unable to find class " + s_className);
		}

		// children
		NodeList children = node.getChildNodes();
		int count = children.getLength();

		int realCount = 0;
		for (int i = 0; i < count; ++i) {
			Node child = children.item(i);

			if (!Element.class.isInstance(child)) {
				continue;
			}

			Element el = (Element) child;
			String elTagName = el.getTagName();

			if ("basic".equals(elTagName)) {
				++realCount;
			} else if ("transient".equals(elTagName)) {
				++realCount;
			} else if ("data-structure".equals(elTagName)) {
				++realCount;
			} else if ("list".equals(elTagName)) {
				++realCount;
			}
		}

		if (realCount == 0) {
			throw new NullPointerException(
					"at least one child component required");
		}

		BaseComponent[] isoFields = new BaseComponent[realCount];
		int[] isoNumbers = new int[realCount];

		// create instance
		Iso8583MessageComponent instance = new Iso8583MessageComponent(name,
				_class, isoFields, isoNumbers, realCount);

		for (int i = 0, j = 0; i < count; ++i) {
			Node child = children.item(i);
			if (!Element.class.isInstance(child)) {
				continue;
			}

			Element el = (Element) child;
			String elTagName = el.getTagName();

			if ("basic".equals(elTagName)) {
				BasicComponent comp = validate(BasicComponentFactory.parse(
						factory, instance, el));

				isoFields[j] = comp;
				isoNumbers[j++] = comp.getNo();
			} else if ("transient".equals(elTagName)) {
				TransientComponent comp = validate(TransientComponentFactory
						.parse(factory, el));

				isoFields[j] = comp;
				isoNumbers[j++] = comp.getNo();
			} else if ("data-structure".equals(elTagName)) {
				DataStructureComponent comp = validate(DataStructureComponentFactory
						.parse(factory, instance, false, el));

				isoFields[j] = comp;
				isoNumbers[j++] = comp.getNo();
			} else if ("list".equals(elTagName)) {
				BaseComponent comp = validate(CollectionComponentFactory.parse(
						factory, instance, el));

				isoFields[j] = comp;

				if (ArrayComponent.class.isInstance(comp)) {
					isoNumbers[j++] = ((ArrayComponent) comp).getNo();
				} else {
					isoNumbers[j++] = ((ListComponent) comp).getNo();
				}
			}
		}

		return instance;
	}
}
