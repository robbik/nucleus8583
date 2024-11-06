package org.nucleus8583.oim.util;

import static org.junit.Assert.assertEquals;
import net.sf.cglib.core.ReflectUtils;

import org.junit.Test;
import org.objectweb.asm.ClassWriter;

public class BeanFieldAccessorEmitterTest {
	
	private Object newInstance(String className, Object bean) throws Exception {
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		
		Class<?> beanClass = bean.getClass();
		
		new BeanFieldAccessorEmitter(cw, className, beanClass);
		
		Class<?> _class = ReflectUtils.defineClass(className, cw.toByteArray(), BeanFieldAccessorEmitter.class.getClassLoader());
		
		return _class.getConstructor(beanClass).newInstance(bean);
	}

	@Test
	public void testGet() throws Exception {
		BeanAccessorIntf ac = (BeanAccessorIntf) newInstance("Z$1", new A());
		
		assertEquals("x", ac.get("x"));
		assertEquals("y", ac.get("y"));
	}
}
