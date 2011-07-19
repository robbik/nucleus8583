package org.nucleus8583.core;

import java.io.InputStream;

import org.w3c.dom.Node;

/**
 * Entry point to create {@link Iso8583Message}. Creating this class requires
 * configuration.
 *
 * @author Robbi Kurniawan
 * @deprecated please use {@link Iso8583MessageSerializer} instead
 */
@Deprecated
public final class Iso8583MessageFactory {
	private final Iso8583MessageSerializer wrapped;

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
		wrapped = new Iso8583MessageSerializer(location);
	}

	/**
	 * create a new instance of {@link Iso8583MessageFactory} using given
	 * configuration
	 *
	 * @param in
	 *            input stream
	 */
	public Iso8583MessageFactory(InputStream in) {
		wrapped = new Iso8583MessageSerializer(in);
	}

	/**
	 * create a new instance of {@link Iso8583MessageFactory} using given
	 * configuration
	 *
	 * @param node
	 *            an DOM node where the entire XML start from
	 */
	public Iso8583MessageFactory(Node node) {
		wrapped = new Iso8583MessageSerializer(node);
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
		Iso8583Message instance = new Iso8583Message();
		instance.attach(this);

		return instance;
	}

	/**
	 * retrieve encoding used by this instance
	 *
	 * @return used encoding
	 */
	public String getEncoding() {
		return wrapped.getEncoding();
	}

	/**
	 * DO NOT use this method directly
	 */
	Iso8583MessageSerializer getWrappedObject() {
		return wrapped;
	}
}
