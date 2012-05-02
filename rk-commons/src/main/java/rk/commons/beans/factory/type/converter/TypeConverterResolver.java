package rk.commons.beans.factory.type.converter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import rk.commons.beans.factory.type.converter.spi.StringToIntConverter;
import rk.commons.beans.factory.type.converter.spi.StringToLongConverter;
import rk.commons.util.ObjectUtils;

public class TypeConverterResolver {
	
	private Map<Tupple, TypeConverter> map;
	
	public TypeConverterResolver() {
		map = Collections.synchronizedMap(new HashMap<Tupple, TypeConverter>());
		
		// register embedded type converters
		register(StringToIntConverter.FROM, StringToIntConverter.TO, new StringToIntConverter());
		register(StringToLongConverter.FROM, StringToLongConverter.TO, new StringToLongConverter());
	}
	
	public void register(Class<?> from, Class<?> to, TypeConverter converter) {
		map.put(new Tupple(from, to), converter);
	}
	
	public TypeConverter unregister(Class<?> from, Class<?> to) {
		return map.remove(new Tupple(from, to));
	}
	
	public TypeConverter resolve(Class<?> from, Class<?> to) {
		return map.get(new Tupple(from, to));
	}
	
	private static class Tupple {
		final Class<?> a;
		
		final Class<?> b;
		
		Tupple(Class<?> a, Class<?> b) {
			this.a = a;
			this.b = b;
		}
		
		@Override
		public int hashCode() {
			return a.hashCode() ^ b.hashCode();
		}
		
		public boolean equals(Object o) {
			if (o == null) {
				return false;
			}
			
			if (o == this) {
				return true;
			}
			
			if (o instanceof Tupple) {
				Tupple t = (Tupple) o;
				
				return ObjectUtils.equals(a, t.a) && ObjectUtils.equals(b, t.b);
			} else {
				return false;
			}
		}
	}
}
