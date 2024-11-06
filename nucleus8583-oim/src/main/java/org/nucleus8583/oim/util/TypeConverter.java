package org.nucleus8583.oim.util;

import java.math.BigDecimal;
import java.math.BigInteger;

public abstract class TypeConverter {
	
	public static int convertToInt(Object o) throws Exception {
		int value;
		
		if (o instanceof Byte) {
			value = ((Byte) o).intValue();
		} else if (o instanceof Short) {
			value = ((Short) o).intValue();
		} else if (o instanceof Long) {
			value = ((Long) o).intValue();
		} else if (o instanceof Float) {
			value = ((Float) o).intValue();
		} else if (o instanceof Double) {
			value = ((Double) o).intValue();
		} else if (o instanceof BigInteger) {
			value = ((BigInteger) o).intValue();
		} else if (o instanceof BigDecimal) {
			value = ((BigDecimal) o).intValue();
		} else if (o instanceof String) {
			value = Integer.parseInt((String) o);
		} else if (byte.class.isInstance(o)) {
			value = byte.class.cast(o).intValue();
		} else if (short.class.isInstance(o)) {
			value = short.class.cast(o).intValue();
		} else if (long.class.isInstance(o)) {
			value = long.class.cast(o).intValue();
		} else if (float.class.isInstance(o)) {
			value = float.class.cast(o).intValue();
		} else if (double.class.isInstance(o)) {
			value = double.class.cast(o).intValue();
		} else {
			throw new UnsupportedOperationException("unsupported conversion for type " + o.getClass());
		}
		
		return value;
	}

	public static BigDecimal convertToBigDecimal(Object o) throws Exception {
		BigDecimal value;
		
		if (o instanceof Byte) {
			value = BigDecimal.valueOf(((Byte) o).longValue());
		} else if (o instanceof Short) {
			value = BigDecimal.valueOf(((Short) o).longValue());
		} else if (o instanceof Integer) {
			value = BigDecimal.valueOf(((Integer) o).longValue());
		} else if (o instanceof Long) {
			value = BigDecimal.valueOf(((Long) o).longValue());
		} else if (o instanceof Float) {
			value = BigDecimal.valueOf(((Float) o).doubleValue());
		} else if (o instanceof Double) {
			value = BigDecimal.valueOf(((Double) o).doubleValue());
		} else if (o instanceof BigInteger) {
			value = new BigDecimal((BigInteger) o);
		} else if (o instanceof String) {
			value = new BigDecimal((String) o);
		} else if (byte.class.isInstance(o)) {
			value = BigDecimal.valueOf(byte.class.cast(o).longValue());
		} else if (short.class.isInstance(o)) {
			value = BigDecimal.valueOf(short.class.cast(o).longValue());
		} else if (int.class.isInstance(o)) {
			value = BigDecimal.valueOf(int.class.cast(o).longValue());
		} else if (long.class.isInstance(o)) {
			value = BigDecimal.valueOf(long.class.cast(o).longValue());
		} else if (float.class.isInstance(o)) {
			value = BigDecimal.valueOf(float.class.cast(o).doubleValue());
		} else if (double.class.isInstance(o)) {
			value = BigDecimal.valueOf(double.class.cast(o).doubleValue());
		} else {
			throw new UnsupportedOperationException("unsupported conversion for type " + o.getClass());
		}
		
		return value;
	}
}
