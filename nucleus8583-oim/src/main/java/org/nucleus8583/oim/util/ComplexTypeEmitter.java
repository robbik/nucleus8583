package org.nucleus8583.oim.util;

import net.sf.cglib.core.ClassEmitter;
import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.Constants;
import net.sf.cglib.core.EmitUtils;
import net.sf.cglib.core.Signature;

import org.nucleus8583.oim.field.type.ComplexType;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Type;

public class ComplexTypeEmitter extends ClassEmitter {
	
	private static final Type SUPER_TYPE = Type.getType(ComplexType.class);
	
	private static final Type OBJECT_TYPE = Type.getType(Object.class);
	
	private static final Type CLONE_NOT_SUPPORTED_EXCEPTION_TYPE = Type.getType(CloneNotSupportedException.class);
	
	private static final Signature NEW_BEAN_INSTANCE = new Signature("newBeanInstance", OBJECT_TYPE, Constants.TYPES_EMPTY);
	
	private static final Signature COPY_CTOR = new Signature("<init>", Type.VOID_TYPE, new Type[] { SUPER_TYPE });
	
	private static final Signature OBJECT_CLONE = new Signature("clone", OBJECT_TYPE, Constants.TYPES_EMPTY);
	
	private static final Signature SUPER_CLONE = new Signature("clone", SUPER_TYPE, Constants.TYPES_EMPTY);
	
	public ComplexTypeEmitter(ClassVisitor v, String className, Class<?> beanClass) {
		super(v);
		
		begin_class(Constants.V1_2, Constants.ACC_PUBLIC, className, SUPER_TYPE, null, Constants.SOURCE_FILE);
		
		EmitUtils.null_constructor(this);
		
		newBeanInstance(beanClass);
		cloneable();
		
		end_class();
	}
	
	private void cloneable() {
		// copy constructor
        CodeEmitter e = begin_method(Constants.ACC_PUBLIC, COPY_CTOR, Constants.TYPES_EMPTY);
        e.load_this();
        e.load_arg(0);
        e.super_invoke_constructor(COPY_CTOR);
        e.return_value();
        e.end_method();
        
        // override method: public ComplexType bridge synthetic clone()
        e = begin_method(Constants.ACC_PUBLIC, SUPER_CLONE, Constants.TYPES_EMPTY);
        e.new_instance_this();
        e.dup();
        e.load_this();
        e.invoke_constructor_this(COPY_CTOR);
        e.return_value();
        e.end_method();
        
        // override method: public bridge synthetic Object clone() throws CloneNotSupportedException
        e = begin_method(Constants.ACC_PUBLIC | Constants.ACC_BRIDGE | Constants.ACC_SYNTHETIC,
        		OBJECT_CLONE, new Type[] { CLONE_NOT_SUPPORTED_EXCEPTION_TYPE });

        e.load_this();
        e.invoke_virtual_this(SUPER_CLONE);
        e.return_value();
        e.end_method();
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
