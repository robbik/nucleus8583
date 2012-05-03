package rk.commons.ioc.factory.type.converter;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import rk.commons.ioc.factory.type.converter.spi.StringToBooleanConverter;
import rk.commons.ioc.factory.type.converter.spi.StringToDoubleConverter;
import rk.commons.ioc.factory.type.converter.spi.StringToFloatConverter;
import rk.commons.ioc.factory.type.converter.spi.StringToIntConverter;
import rk.commons.ioc.factory.type.converter.spi.StringToLongConverter;
import rk.commons.ioc.factory.type.converter.spi.StringToUriConverter;
import rk.commons.util.ObjectUtils;

public class TypeConverterResolver {
	
	private Map<Tupple, TypeConverter> map;
	
	public TypeConverterResolver() {
		map = Collections.synchronizedMap(new HashMap<Tupple, TypeConverter>());
		
		// register embedded type converters
		register(String.class, int.class, new StringToIntConverter());
		register(String.class, long.class, new StringToLongConverter());
		register(String.class, boolean.class, new StringToBooleanConverter());
		register(String.class, float.class, new StringToFloatConverter());
		register(String.class, double.class, new StringToDoubleConverter());

		register(String.class, URI.class, new StringToUriConverter());
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
