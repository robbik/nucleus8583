package org.nucleus8583.oim.xml;

import java.util.List;

import net.sf.cglib.core.ReflectUtils;

import org.nucleus8583.oim.Access;
import org.nucleus8583.oim.MessageEntity;
import org.nucleus8583.oim.field.Field;
import org.nucleus8583.oim.util.MessageEntityEmitter;
import org.objectweb.asm.ClassWriter;

import rk.commons.inject.annotation.Inject;
import rk.commons.inject.factory.support.FactoryObject;
import rk.commons.loader.ResourceLoader;

public class MessageEntityFactory extends FactoryObject<MessageEntity> {
	
	private static final ClassLoader DEFAULT_CLASSLOADER = MessageEntityFactory.class.getClassLoader();
	
	@Inject
	private ResourceLoader loader;
	
	private String name;
	
	private Class<?> beanClass;
	
	private Access access;
	
	private List<Field> fields;
	
	private Class<?> gc;
	
	public void setName(String name) {
		this.name = name;
	}

	public void setBeanClass(Class<?> beanClass) {
		this.beanClass = beanClass;
	}
	
	public void setAccess(Access access) {
		this.access = access;
	}

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }
    
	protected synchronized MessageEntity createInstance() {
		if (gc == null) {
			generateClass();
		}
		
		MessageEntity obj;
		
		try {
			obj = (MessageEntity) gc.newInstance();
		} catch (Exception e) {
			throw new UnsupportedOperationException(e);
		}
		
		obj.setAccess(access);
		
		obj.setFields(fields);
		
		return obj;
	}
	
	private void generateClass() {
		String className = MessageEntity.class.getName() + "$$$" + name;
		
		gc = loader.tryLoadClass(className);
		
		if (gc == null) {
			ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			
			// emit class
			new MessageEntityEmitter(cw, className, beanClass);
			
			// defined new class
			try {
				gc = ReflectUtils.defineClass(className, cw.toByteArray(), DEFAULT_CLASSLOADER);
			} catch (Exception e) {
				throw new RuntimeException("unable to create class " + className + " for message " + name, e);
			}
		}
	}
}
