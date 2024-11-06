package org.nucleus8583.oim.util;

import net.sf.cglib.core.ClassEmitter;
import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.Constants;
import net.sf.cglib.core.EmitUtils;
import net.sf.cglib.core.Signature;

import org.nucleus8583.oim.MessageEntity;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Type;

public class MessageEntityEmitter extends ClassEmitter {
	
	private static final Type SUPER_TYPE = Type.getType(MessageEntity.class);
	
	private static final Type OBJECT_TYPE = Type.getType(Object.class);
	
	private static final Signature NEW_BEAN_INSTANCE = new Signature("newBeanInstance", OBJECT_TYPE, Constants.TYPES_EMPTY);
	
	public MessageEntityEmitter(ClassVisitor v, String className, Class<?> beanClass) {
		super(v);
		
		begin_class(Constants.V1_2, Constants.ACC_PUBLIC, className, SUPER_TYPE, null, Constants.SOURCE_FILE);
		
		EmitUtils.null_constructor(this);
		
		newBeanInstance(beanClass);
		
		end_class();
	}
	
	private void newBeanInstance(Class<?> beanClass) {
		// override method: protected Object newBeanInstance()
		Type type = Type.getType(beanClass);
		
        CodeEmitter e = begin_method(Constants.ACC_PROTECTED, NEW_BEAN_INSTANCE, Constants.TYPES_EMPTY);
        e.new_instance(type);
        e.dup();
        e.invoke_constructor(type);
        e.return_value();
        e.end_method();
	}
}
