package org.nucleus8583.oim.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;

import net.sf.cglib.core.ReflectUtils;

import org.junit.Test;
import org.nucleus8583.oim.field.type.ComplexType;
import org.objectweb.asm.ClassWriter;

public class ComplexTypeEmitterTest {
	
	private Object newInstance(String className, Class<?> beanClass) throws Exception {
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		
		new ComplexTypeEmitter(cw, className, beanClass);
		
		return ReflectUtils.defineClass(className, cw.toByteArray(), ComplexTypeEmitter.class.getClassLoader()).newInstance();
	}

	@Test
	public void testHashMap() throws Exception {
		ComplexType ct = (ComplexType) newInstance("org.nucleus8583.oim.field.type.ComplexType$java.util.HashMap", HashMap.class);
		
		Object bean = ct.newBeanInstance();
		
		assertNotNull(bean);
		assertEquals(bean.getClass(), HashMap.class);
		
		System.out.println(bean);
	}
}
