package org.nucleus8583.oim;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nucleus8583.Iso8583Message;
import org.nucleus8583.oim.processor.Iso8583MessageComponent;
import org.nucleus8583.oim.xml.Iso8583MessagesFactory;

public final class Iso8583MessageManager {
	private final Iso8583MessagesFactory xmlFactory;

	private final Map<String, Iso8583MessageComponent> messageComponents;

	public Iso8583MessageManager(List<String> pathname) throws IOException {
		this(pathname.toArray(new String[0]));
	}

	public Iso8583MessageManager(String... locations) throws IOException {
		xmlFactory = new Iso8583MessagesFactory(locations);
		messageComponents = new HashMap<String, Iso8583MessageComponent>();

		for (Map.Entry<String, Iso8583MessageComponent> entry : xmlFactory
				.getMessages().entrySet()) {
			messageComponents.put(entry.getKey(), entry.getValue());
		}
	}

	private Iso8583MessageComponent findMessageComponent(String messageName) {
		Iso8583MessageComponent found = messageComponents.get(messageName);
		if (found == null) {
			throw new RuntimeException("unable to find message " + messageName);
		}

		return found;
	}

	public Object convert(String messageName, Iso8583Message msg) {
		return findMessageComponent(messageName).decode(msg,
				new HashMap<String, Object>());
	}

	/**
	 * convert from java plain object <code>obj</code> to ISO-8583 message.
	 * 
	 * @param messageName
	 *            message name
	 * @param obj
	 *            plain old java object
	 * @param msg
	 *            ISO-8583 message
	 */
	public void convert(String messageName, Object obj, Iso8583Message msg) {
		findMessageComponent(messageName).encode(msg, obj,
				new HashMap<String, Object>());
	}
}
