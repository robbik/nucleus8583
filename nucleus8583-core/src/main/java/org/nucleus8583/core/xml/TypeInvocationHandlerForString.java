package org.nucleus8583.core.xml;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.nucleus8583.core.Message;
import org.nucleus8583.core.MessageSerializer;
import org.nucleus8583.core.field.Type;

public class TypeInvocationHandlerForString implements InvocationHandler {
	
	private final Type<?> proxied;
	
	private final MessageSerializer serializer;
	
	private final Method readMethod;
	
	private final Method writeMethod;
	
	public TypeInvocationHandlerForString(Type<?> proxied, MessageSerializer serializer) {
		this.proxied = proxied;
		this.serializer = serializer;
		
		try {
			readMethod = Type.class.getMethod("read", InputStream.class);
		} catch (NoSuchMethodException e) {
			throw new AssertionError(e);
		}
		
		try {
			writeMethod = Type.class.getMethod("write", OutputStream.class,
					(Class<?>) Type.class.getTypeParameters()[0].getBounds()[0]);
		} catch (Throwable e) {
			throw new AssertionError(e);
		}
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (method.equals(readMethod)) {
			String data = (String) method.invoke(proxied, args[0]);
			
			Message out = new Message();
			serializer.read(data.getBytes(), out);
			
			return out;
		} else if (method.equals(writeMethod)) {
			byte[] bytes = serializer.write((Message) args[1]);
			
			return method.invoke(proxied, args[0], new String(bytes));
		} else {
			return method.invoke(proxied, args);
		}
	}
}
