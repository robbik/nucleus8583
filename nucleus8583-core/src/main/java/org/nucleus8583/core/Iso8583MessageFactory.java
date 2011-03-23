package org.nucleus8583.core;

import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;

import javax.xml.bind.JAXBContext;

import org.nucleus8583.core.charset.spi.CharsetProvider;
import org.nucleus8583.core.util.ResourceUtils;
import org.nucleus8583.core.xml.Iso8583MessageDefinition;
import org.w3c.dom.Node;

/**
 * Entry point to create {@link Iso8583Message}. Creating this class requires
 * configuration.
 * 
 * @author Robbi Kurniawan
 * 
 */
public final class Iso8583MessageFactory {
	private final Iso8583MessageDefinition definition;

	private static final JAXBContext ctx;

	static {
		try {
			ctx = JAXBContext.newInstance(Iso8583MessageDefinition.class);
		} catch (Exception e) {
			StringWriter w = new StringWriter();
			PrintWriter pw = new PrintWriter(w);

			e.printStackTrace(pw);
			pw.flush();

			throw new InternalError(w.toString());
		}
	}

	/**
	 * create a new instance of {@link Iso8583MessageFactory} using given
	 * configuration.
	 * 
	 * For example, if you want to load "nucleus8583.xml" from "META-INF"
	 * located in classpath, the location should be
	 * <code>classpath:META-INF/nucleus8583.xml</code>.
	 * 
	 * If you want to load "nucleus8583.xml" from "conf" directory, the location
	 * should be <code>file:conf/nucleus8583.xml</code> or just
	 * <code>conf/nucleus8583.xml</code>.
	 * 
	 * @param location
	 *            configuration location (in URI)
	 */
	public Iso8583MessageFactory(String location) {
		URL found = ResourceUtils.getURL(location);
		if (found == null) {
			throw new RuntimeException("unable to find " + location);
		}

		try {
			definition = (Iso8583MessageDefinition) ctx.createUnmarshaller()
					.unmarshal(found);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		definition.createMessageTemplate();
	}

	/**
	 * create a new instance of {@link Iso8583MessageFactory} using given
	 * configuration
	 * 
	 * @param in
	 *            input stream
	 */
	public Iso8583MessageFactory(InputStream in) {
		try {
			definition = (Iso8583MessageDefinition) ctx.createUnmarshaller()
					.unmarshal(in);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		definition.createMessageTemplate();
	}

	/**
	 * create a new instance of {@link Iso8583MessageFactory} using given
	 * configuration
	 * 
	 * @param node
	 *            an DOM node where the entire XML start from
	 */
	public Iso8583MessageFactory(Node node) {
		try {
			definition = (Iso8583MessageDefinition) ctx.createUnmarshaller()
					.unmarshal(node);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		definition.createMessageTemplate();
	}

	/**
	 * same as <code>
	 *     return new Iso8583MessageFactory(location);
	 * </code>
	 * 
	 * @param location
	 * @return a new instance of Iso8583MessageFactory.
	 */
	public static Iso8583MessageFactory create(String location) {
		return new Iso8583MessageFactory(location);
	}

	/**
	 * same as <code>
	 *     return new Iso8583MessageFactory(in);
	 * </code>
	 * 
	 * @param in
	 * @return a new instance of Iso8583MessageFactory.
	 */
	public static Iso8583MessageFactory create(InputStream in) {
		return new Iso8583MessageFactory(in);
	}

	/**
	 * same as <code>
	 *     return new Iso8583MessageFactory(node);
	 * </code>
	 * 
	 * @param node
	 * @return a new instance of Iso8583MessageFactory.
	 */
	public static Iso8583MessageFactory create(Node node) {
		return new Iso8583MessageFactory(node);
	}

	/**
	 * create a new instance of {@link Iso8583Message}
	 * 
	 * @return the new instance of {@link Iso8583Message}
	 */
	public Iso8583Message createMessage() {
		return definition.createMessage();
	}

	/**
	 * retrieve encoding used by this instance
	 * 
	 * @return used encoding
	 */
	public String getEncoding() {
		return definition.getEncoding();
	}

	/**
	 * Retrieve encoding provider used by this factory. <b>You must not use this
	 * method directly.</b>
	 * 
	 * @return the provider
	 */
	public CharsetProvider getCharsetProvider() {
		return definition.getCharsetProvider();
	}

	/**
	 * Retrieve iso8583 fields used by this factory. <b>You must not use this
	 * method directly.</b>
	 * 
	 * @return the fields
	 */
	public Iso8583Field[] getFields() {
		return definition.getFields();
	}

	/**
	 * Retrieve binary marks used by this factory. <b>You must not use this
	 * method directly.</b>
	 * 
	 * @return the binaries
	 */
	public boolean[] getBinaries() {
		return definition.getBinaries();
	}
}
