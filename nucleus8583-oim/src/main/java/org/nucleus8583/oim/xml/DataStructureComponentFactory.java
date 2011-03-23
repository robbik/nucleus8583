package org.nucleus8583.oim.xml;

import java.util.regex.Pattern;

import org.nucleus8583.oim.component.BaseComponent;
import org.nucleus8583.oim.component.BasicComponent;
import org.nucleus8583.oim.component.DataStructureComponent;
import org.nucleus8583.oim.component.HasFieldsComponent;
import org.nucleus8583.oim.component.TransientComponent;
import org.nucleus8583.oim.util.StringUtil;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class DataStructureComponentFactory {
	private static final Pattern digits = Pattern.compile("^[0-9]+$");

	public static DataStructureComponent parse(Iso8583MessagesFactory factory,
			HasFieldsComponent parent, boolean ignoreField, Element node) {

		String s_no = node.getAttribute("no");
		String s_name = node.getAttribute("name");
		String s_className = node.getAttribute("class");

		// no
		int no;

		if (StringUtil.hasText(s_no)) {
			s_no = s_no.trim();

			if (!digits.matcher(s_no).matches()) {
				throw new IllegalArgumentException(
						"no value must be numeric only");
			}

			no = Integer.parseInt(s_no, 10);
		} else {
			no = -1;
		}

		// name
		String name;

		if (StringUtil.hasText(s_name)) {
			name = s_name.trim();
		} else {
			name = null;
		}

		// class
		String className;

		if (StringUtil.hasText(s_className)) {
			className = s_className.trim();
		} else {
			if (parent == null) {
				throw new IllegalArgumentException("class is required");
			}

			className = null;
		}

		// child components
		NodeList children = node.getChildNodes();
		int count = children.getLength();

		int realCount = 0;
		for (int i = 0; i < count; ++i) {
			if (Element.class.isInstance(children.item(i))) {
				++realCount;
			}
		}

		BaseComponent[] childComponents = new BaseComponent[realCount];

		DataStructureComponent instance = new DataStructureComponent(parent,
				no, className, name, childComponents, ignoreField);
		boolean nextIgnoreField = !instance.hasField();

		for (int i = 0, j = 0; i < count; ++i) {
			Node child = children.item(i);
			if (!Element.class.isInstance(child)) {
				continue;
			}

			Element el = (Element) child;
			String tagName = el.getTagName();

			if ("skip".equals(tagName)) {
				childComponents[j] = SkipComponentFactory.parse(el);
			} else if ("basic".equals(tagName)) {
				childComponents[j] = validate(BasicComponentFactory.parse(
						factory, instance, el));
			} else if ("transient".equals(tagName)) {
				childComponents[j] = validate(TransientComponentFactory.parse(
						factory, el));
			} else if ("data-structure".equals(tagName)) {
				childComponents[j] = DataStructureComponentFactory.parse(
						factory, instance, nextIgnoreField, el);
			} else if ("list".equals(tagName)) {
				childComponents[j] = CollectionComponentFactory.parse(factory,
						instance, el);
			} else {
				throw new IllegalArgumentException("unsupported tag " + tagName);
			}

			++j;
		}

		return instance;
	}

	private static BasicComponent validate(BasicComponent component) {
		if (component.isBinary()) {
			throw new RuntimeException(
					"data-structure cannot contains binary value");
		}

		return component;
	}

	private static TransientComponent validate(TransientComponent component) {
		if (component.isBinary()) {
			throw new RuntimeException(
					"data-structure cannot contains binary value");
		}

		return component;
	}

}
