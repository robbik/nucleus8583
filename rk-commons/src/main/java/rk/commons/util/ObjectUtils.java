package rk.commons.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import rk.commons.ioc.factory.ObjectInstantiationException;
import rk.commons.ioc.factory.config.ObjectDefinition;
import rk.commons.ioc.factory.support.ObjectDefinitionValueResolver;
import rk.commons.ioc.factory.type.converter.TypeConverterResolver;

public abstract class ObjectUtils {

	public static String getObjectQName(String packageName, String objectName) {
		if (objectName == null) {
			objectName = "";
		} else {
			objectName = objectName.trim();
		}

		if (StringUtils.hasText(packageName)) {
			return packageName.trim().concat(":").concat(objectName);
		}

		return objectName;
	}

	public static String getPackageName(String objectQName) {
		if (objectQName == null) {
			objectQName = "";
		} else {
			objectQName = objectQName.trim();
		}

		if (StringUtils.hasText(objectQName)) {
			int lddi = objectQName.lastIndexOf(':');

			if (lddi < 0) {
				return "";
			} else {
				return objectQName.substring(0, lddi);
			}
		} else {
			return "";
		}
	}

	public static String applyDefaultPackageName(String packageName,
			String objectQName) {
		if (objectQName == null) {
			objectQName = "";
		} else {
			objectQName = objectQName.trim();
		}

		if (!StringUtils.hasText(packageName)) {
			return objectQName;
		}

		if (StringUtils.hasText(objectQName)) {
			int lddi = objectQName.lastIndexOf(':');

			if (lddi < 0) {
				return packageName.concat(":").concat(objectQName);
			} else {
				return objectQName;
			}
		} else {
			return packageName.concat(":");
		}
	}

	public static String applyPackageName(String packageName, String objectQName) {
		if (objectQName == null) {
			objectQName = "";
		} else {
			objectQName = objectQName.trim();
		}

		if (StringUtils.hasText(packageName)) {
			packageName = packageName.concat(":");
		} else {
			packageName = "";
		}

		if (StringUtils.hasText(objectQName)) {
			int lddi = objectQName.lastIndexOf(':');

			if (lddi < 0) {
				return packageName.concat(objectQName);
			} else {
				return packageName.concat(objectQName.substring(lddi + 1));
			}
		} else {
			return packageName;
		}
	}

	public static String generateRandomObjectQName(String packageName,
			String objectName) {
		return getObjectQName(packageName, objectName).concat("__").concat(
				StringUtils.valueOf(UUID.randomUUID()));
	}

	public static String generateRandomObjectQName(String packageName,
			ObjectDefinition definition) {

		String className;

		if (StringUtils.hasText(definition.getObjectClassName())) {
			className = definition.getObjectClassName();
		} else if (definition.getObjectClass() != null) {
			className = definition.getObjectClass().getName();
		} else {
			className = "(anonymous)";
		}

		int lastDotIndex = className.lastIndexOf('.');

		if (lastDotIndex >= 0) {
			className = className.substring(lastDotIndex + 1);
		}

		return generateRandomObjectQName(packageName, className);
	}
	
	public static void applyPropertyValues(String objectQName, Object object,
			Map<String, Object> propertyValues) {
		
		applyPropertyValues(objectQName, object, propertyValues, null, null);
	}
	
	public static void applyPropertyValues(String objectQName, Object object,
			Map<String, Object> propertyValues,
			TypeConverterResolver typeConverterResolver) {
		
		applyPropertyValues(objectQName, object, propertyValues, null, typeConverterResolver);
	}

	public static void applyPropertyValues(String objectQName, Object object,
			Map<String, Object> propertyValues,
			ObjectDefinitionValueResolver valueResolver,
			TypeConverterResolver typeConverterResolver) {

		Class<?> objectType = object.getClass();
		
		for (Map.Entry<String, Object> entry : propertyValues.entrySet()) {
			String name = entry.getKey();
			
			Object value;
			
			if (valueResolver == null) {
				value = entry.getValue();
			} else {
				value = valueResolver.resolveValueIfNecessary(entry.getValue());
			}
			
			applyPropertyValue(objectQName, object, objectType, name, value, typeConverterResolver);
		}
	}
	
	public static void applyPropertyValue(String objectQName, Object object, Class<?> objectType,
			String name,
			Object value,
			TypeConverterResolver typeConverterResolver) {

		try {
			if (name.contains(".")) {
				int dot = name.indexOf('.');
				
				String subname = name.substring(0, dot);
				String methodName = "get" + Character.toUpperCase(subname.charAt(0)) + subname.substring(1);
				
				Method getter = ReflectionUtils.findPublicMethod(objectType, methodName);
				if (getter == null) {
					throw new NoSuchMethodException(objectType + "." + methodName + "()");
				}
				
				Object nextBean = getter.invoke(objectType);
				
				applyPropertyValue(objectQName, nextBean, nextBean.getClass(), name.substring(dot + 1), value, typeConverterResolver);
				return;
			}
			
			String methodName = "set" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
			Class<?> valueType = value.getClass();
			
			Method setter = ReflectionUtils.findPublicMethod(objectType, methodName, valueType);
			
			if ((setter == null) && (typeConverterResolver != null)) {
				setter = ReflectionUtils.findConvertablePublicMethod(objectType, methodName,
						typeConverterResolver, valueType);
				
				if (setter != null) {
					value = typeConverterResolver.resolve(valueType,
							setter.getParameterTypes()[0]).convert(value);
				}
			}
			
			if (setter == null) {
				Class<?> primitive = ObjectUtils.getPrimitiveType(valueType);
				
				if (primitive != null) {
					setter = ReflectionUtils.findPublicMethod(objectType, methodName, primitive);
				}
			}
			
			if (setter == null) {
				throw new NoSuchMethodException(objectType + "." + methodName + "(" + valueType + ")");
			}

			setter.invoke(object, value);
		} catch (Throwable t) {
			throw new ObjectInstantiationException(objectQName, t);
		}
	}

	public static boolean isArray(Object a) {
		return a.getClass().getName().startsWith("[");
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
	
	public static Class<?> getPrimitiveType(Class<?> type) {
		if (Integer.class.equals(type)) {
			return int.class;
		} else if (Long.class.equals(type)) {
			return long.class;
		} else if (Short.class.equals(type)) {
			return short.class;
		} else if (Byte.class.equals(type)) {
			return byte.class;
		} else if (Float.class.equals(type)) {
			return float.class;
		} else if (Double.class.equals(type)) {
			return double.class;
		} else if (Void.class.equals(type)) {
			return void.class;
		} else {
			return null;
		}
	}

	public static <T> T coalesce(T... values) {
		for (int i = 0, len = values.length; i < len; ++i) {
			T value = values[i];

			if (value != null) {
				return value;
			}
		}

		return null;
	}

	private static byte[] readBytes(ObjectInputStream in) throws IOException {
		int length = in.readInt();

		byte[] bytes = new byte[length];
		in.readFully(bytes, 0, length);

		return bytes;
	}

	private static void writeBytes(byte[] bytes, ObjectOutputStream out)
			throws IOException {
		out.writeInt(bytes.length);
		out.write(bytes);
	}

	public static void writeBytes(Object o, ObjectOutputStream out)
			throws IOException {
		if (o instanceof byte[]) {
			out.write('b');
			out.write(']');
			writeBytes((byte[]) o, out);
		} else if (o instanceof String) {
			out.write('S');
			out.write(' ');
			writeBytes(((String) o).getBytes("UTF-8"), out);
		} else if (o instanceof char[]) {
			out.write('c');
			out.write(']');
			writeBytes(String.valueOf((char[]) o).getBytes("UTF-8"), out);
		} else if (byte.class.isInstance(o)) {
			out.write('b');
			out.write(' ');
			out.writeByte(byte.class.cast(o));
		} else if (short.class.isInstance(o)) {
			out.write('s');
			out.write(' ');
			out.writeShort(short.class.cast(o));
		} else if (char.class.isInstance(o)) {
			out.write('c');
			out.write(' ');
			out.writeChar(char.class.cast(o));
		} else if (int.class.isInstance(o)) {
			out.write('i');
			out.write(' ');
			out.writeInt(int.class.cast(o));
		} else if (long.class.isInstance(o)) {
			out.write('l');
			out.write(' ');
			out.writeLong(long.class.cast(o));
		} else if (Byte.class.isInstance(o)) {
			out.write('B');
			out.write(' ');
			out.writeByte(Byte.class.cast(o).byteValue());
		} else if (Short.class.isInstance(o)) {
			out.write('S');
			out.write(' ');
			out.writeShort(Short.class.cast(o).shortValue());
		} else if (Character.class.isInstance(o)) {
			out.write('C');
			out.write(' ');
			out.writeChar(Character.class.cast(o).charValue());
		} else if (Integer.class.isInstance(o)) {
			out.write('I');
			out.write(' ');
			out.writeInt(Integer.class.cast(o).intValue());
		} else if (Long.class.isInstance(o)) {
			out.write('L');
			out.write(' ');
			out.writeLong(Long.class.cast(o).longValue());
		} else {
			out.write('J');
			out.write('O');
			out.writeObject(o);
		}
	}

	public static Object readFromBytes(ObjectInputStream in)
			throws IOException, ClassNotFoundException {

		char h1 = (char) in.readUnsignedByte();
		char h2 = (char) in.readUnsignedByte();

		if ((h1 == 'b') && (h2 == ']')) {
			return readBytes(in);
		}

		if ((h1 == 'c') && (h2 == ']')) {
			return new String(readBytes(in), "UTF-8").toCharArray();
		}
		if ((h1 == 'S') && (h2 == ' ')) {
			return new String(readBytes(in), "UTF-8");
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

		if ((h1 == 'B') && (h2 == ' ')) {
			return Byte.valueOf(in.readByte());
		}
		if ((h1 == 'S') && (h2 == ' ')) {
			return Short.valueOf(in.readShort());
		}
		if ((h1 == 'C') && (h2 == ' ')) {
			return Character.valueOf(in.readChar());
		}
		if ((h1 == 'I') && (h2 == ' ')) {
			return Integer.valueOf(in.readInt());
		}
		if ((h1 == 'L') && (h2 == ' ')) {
			return Long.valueOf(in.readLong());
		}

		if ((h1 == 'J') && (h2 == 'O')) {
			return in.readObject();
		}

		throw new IOException("unknown content " + h1 + h2);
	}

	public static byte[] toBytes(Object o) {
		if (o == null) {
			return null;
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {
			ObjectOutputStream oos = new ObjectOutputStream(out);
			writeBytes(o, oos);

			oos.flush();
		} catch (Throwable t) {
			// do nothing
		}

		return out.toByteArray();
	}

	public static Object fromBytes(byte[] bytes) {
		if (bytes == null) {
			return null;
		}

		try {
			ObjectInputStream ois = new ObjectInputStream(
					new ByteArrayInputStream(bytes));

			return readFromBytes(ois);
		} catch (Throwable t) {
			return null;
		}
	}
}
