package org.nucleus8583.core.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.annotation.Annotation;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.function.Function;

public final class ObjectHelper {

	private ObjectHelper() {
		// do nothing
	}

	public static boolean equals(byte[] a, byte[] b) {
		if (a == b) {
			return true;
		}

		if ((a == null) || (b == null)) {
			return false;
		}

		return Arrays.equals(a, b);
	}

	public static boolean equals(Object a, Object b) {
		if (a == b) {
			return true;
		}

		if ((a == null) || (b == null)) {
			return false;
		}

		return a.equals(b);
	}

	private static byte[] readBytes(ObjectInputStream in) throws IOException {
		int length = in.readInt();

		byte[] bytes = new byte[length];
		in.readFully(bytes, 0, length);

		return bytes;
	}

	private static void writeBytes(byte[] bytes, ObjectOutputStream out) throws IOException {
		out.writeInt(bytes.length);
		out.write(bytes);
	}

	public static void writeObject(Object o, ObjectOutputStream out) throws IOException {
		if (o instanceof byte[]) {
			out.write('b');
			out.write(']');
			writeBytes((byte[]) o, out);
		} else if (o instanceof char[]) {
			out.write('c');
			out.write(']');
			writeBytes(String.valueOf((char[]) o).getBytes(StandardCharsets.UTF_8), out);
		} else if (boolean.class.isInstance(o)) {
			out.write('b');
			out.write('o');
			out.writeBoolean((boolean) o);
		} else if (byte.class.isInstance(o)) {
			out.write('b');
			out.write(' ');
			out.writeByte((byte) o);
		} else if (short.class.isInstance(o)) {
			out.write('s');
			out.write(' ');
			out.writeShort((short) o);
		} else if (char.class.isInstance(o)) {
			out.write('c');
			out.write(' ');
			out.writeChar((char) o);
		} else if (int.class.isInstance(o)) {
			out.write('i');
			out.write(' ');
			out.writeInt(int.class.cast(o));
		} else if (long.class.isInstance(o)) {
			out.write('l');
			out.write(' ');
			out.writeLong((long) o);
		} else if (float.class.isInstance(o)) {
			out.write('f');
			out.write(' ');
			out.writeFloat((float) o);
		} else if (double.class.isInstance(o)) {
			out.write('d');
			out.write(' ');
			out.writeDouble((double) o);
		} else if (o instanceof Boolean) {
			out.write('B');
			out.write('O');
			out.writeBoolean((Boolean) o);
		} else if (o instanceof Byte) {
			out.write('B');
			out.write(' ');
			out.writeByte((Byte) o);
		} else if (o instanceof Short) {
			out.write('S');
			out.write(' ');
			out.writeShort((Short) o);
		} else if (o instanceof Character) {
			out.write('C');
			out.write(' ');
			out.writeChar((Character) o);
		} else if (o instanceof Integer) {
			out.write('I');
			out.write(' ');
			out.writeInt((Integer) o);
		} else if (o instanceof Long) {
			out.write('L');
			out.write(' ');
			out.writeLong((Long) o);
		} else if (o instanceof Float) {
			out.write('F');
			out.write(' ');
			out.writeFloat((Float) o);
		} else if (o instanceof Double) {
			out.write('D');
			out.write(' ');
			out.writeDouble((Double) o);
		} else if (o instanceof String) {
			out.write('T');
			out.write(' ');
			out.writeUTF((String) o);
		} else {
			out.write('J');
			out.write('O');
			out.writeObject(o);
		}
	}

	public static Object readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		char h1 = (char) in.readUnsignedByte();
		char h2 = (char) in.readUnsignedByte();

		if ((h1 == 'b') && (h2 == ']')) {
			return readBytes(in);
		}
		if ((h1 == 'c') && (h2 == ']')) {
			return new String(readBytes(in), StandardCharsets.UTF_8).toCharArray();
		}

		if ((h1 == 'b') && (h2 == 'o')) {
			return in.readBoolean();
		}
		if ((h1 == 'b') && (h2 == ' ')) {
			return in.readByte();
		}
		if ((h1 == 's') && (h2 == ' ')) {
			return in.readShort();
		}
		if ((h1 == 'c') && (h2 == ' ')) {
			return in.readChar();
		}
		if ((h1 == 'i') && (h2 == ' ')) {
			return in.readInt();
		}
		if ((h1 == 'l') && (h2 == ' ')) {
			return in.readLong();
		}
		if ((h1 == 'f') && (h2 == ' ')) {
			return in.readFloat();
		}
		if ((h1 == 'd') && (h2 == ' ')) {
			return in.readDouble();
		}

		if ((h1 == 'B') && (h2 == 'O')) {
			return in.readBoolean();
		}
		if ((h1 == 'B') && (h2 == ' ')) {
			return in.readByte();
		}
		if ((h1 == 'S') && (h2 == ' ')) {
			return in.readShort();
		}
		if ((h1 == 'C') && (h2 == ' ')) {
			return in.readChar();
		}
		if ((h1 == 'I') && (h2 == ' ')) {
			return in.readInt();
		}
		if ((h1 == 'L') && (h2 == ' ')) {
			return in.readLong();
		}
		if ((h1 == 'F') && (h2 == ' ')) {
			return in.readFloat();
		}
		if ((h1 == 'D') && (h2 == ' ')) {
			return in.readDouble();
		}

		if ((h1 == 'T') && (h2 == ' ')) {
			return in.readUTF();
		}
		if ((h1 == 'J') && (h2 == 'O')) {
			return in.readObject();
		}

		throw new IOException("unknown content " + h1 + h2);
	}

	public static <T, R> R ifNotNull(T value, Function<? super T, ? extends R> dst) {
		if (value == null) {
			return null;
		} else {
			return dst.apply(value);
		}
	}

	public static <T extends Annotation> T getAnnotation(Class<?> clazz, Class<T> annotationType) {
		if (!clazz.isAnnotationPresent(annotationType)) {
			return null;
		}

		return clazz.getAnnotation(annotationType);
	}

	public static Properties asProperties(Map<String, String> properties) {
		final Properties result = new Properties();

		if ((properties != null) && !properties.isEmpty()) {
            result.putAll(properties);
		}

		return result;
	}

	public static int asInt(Object o, int defaultValue) {
		if (o == null) {
			return defaultValue;
		}

		if (o instanceof Integer) {
			return (Integer) o;
		} else if (o instanceof Number) {
			return ((Number) o).intValue();
		} else if (o instanceof String) {
			return StringHelper.asInt((String) o, defaultValue);
		} else {
			return defaultValue;
		}
	}
}
