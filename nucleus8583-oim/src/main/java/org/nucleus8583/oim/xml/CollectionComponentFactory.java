package org.nucleus8583.oim.xml;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

import org.nucleus8583.oim.component.ArrayComponent;
import org.nucleus8583.oim.component.BaseComponent;
import org.nucleus8583.oim.component.DataStructureComponent;
import org.nucleus8583.oim.component.HasFieldsComponent;
import org.nucleus8583.oim.component.ListComponent;
import org.nucleus8583.oim.util.StringUtil;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public abstract class CollectionComponentFactory {
	private static final Pattern digits = Pattern.compile("^[0-9]+$");

	public static BaseComponent parse(Iso8583MessagesFactory factory,
			HasFieldsComponent parent, Element node) {
		String s_no = node.getAttribute("no");
		String s_name = node.getAttribute("name");
		String s_size = node.getAttribute("size");
		String s_dsRef = node.getAttribute("data-structure-ref");

		// no
		int no;

		if (StringUtil.hasText(s_no, true)) {
			s_no = s_no.trim();

			if (!digits.matcher(s_no).matches()) {
				throw new IllegalArgumentException("no must be numeric only");
			}

			no = Integer.parseInt(s_no, 10);
		} else {
			no = -1;
		}

		// name
		String name;

		if (!StringUtil.hasText(s_name)) {
			throw new IllegalArgumentException("name is required");
		}

		name = s_name.trim();

		// size
		String size;

		if (StringUtil.hasText(s_size)) {
			s_size = s_size.trim();

			if (!s_size.startsWith("transient:")
					&& !s_size.startsWith("basic:")) {
				throw new IllegalArgumentException(
						"prefix 'transient:' or 'basic:' required");
			}

			size = s_size;
		} else {
			size = null;
		}

		// append
		boolean append = "true".equalsIgnoreCase(node.getAttribute("append"));

		// data-structure
		DataStructureComponent dscomp;

		if (StringUtil.hasText(s_dsRef)) {
			dscomp = factory.findDataStructure(s_dsRef.trim());

			if (dscomp == null) {
				throw new IllegalArgumentException(
						"unable to find data-structure " + s_dsRef);
			}
		} else {
			NodeList children = node.getElementsByTagName("data-structure");
			int count = children.getLength();

			if (count == 0) {
				throw new IllegalArgumentException("data-structure is required");
			}

			dscomp = DataStructureComponentFactory.parse(factory, parent, true,
					(Element) children.item(0));
		}

		Field field = parent.getField(name);
		if (field.getType().isArray()) {
			return new ArrayComponent(parent, no, name, size, dscomp);
		}

		return new ListComponent(parent, no, name, size, append, dscomp);
	}
}
