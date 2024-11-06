package org.nucleus8583.oim.util;

import net.sf.cglib.core.ClassEmitter;
import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.Constants;
import net.sf.cglib.core.Signature;
import net.sf.cglib.core.TypeUtils;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Type;

public abstract class BeanAccessorEmitter extends ClassEmitter {
	
	protected static final Type INTERFACE_TYPE = Type.getType(BeanAccessorIntf.class);
	
	protected static final Type OBJECT_TYPE = Type.getType(Object.class);
	
	protected static final Type STRING_TYPE = Type.getType(String.class);
	
	protected static final Type EXCEPTION_TYPE = Type.getType(IllegalArgumentException.class);
	
	protected static final Signature EXCEPTION_CTOR = new Signature("<init>", Type.VOID_TYPE, new Type[] { STRING_TYPE });
	
	protected static final Signature EQUALS = TypeUtils.parseSignature("boolean equals(java.lang.Object)");
	
	protected static final Signature GET = new Signature("get", OBJECT_TYPE, new Type[] { STRING_TYPE });
	
	protected static final Signature SET = new Signature("set", Type.VOID_TYPE, new Type[] { STRING_TYPE, OBJECT_TYPE });
	
	protected final Type beanType;
	
	protected final ClassAccessor ca;
	
	protected BeanAccessorEmitter(ClassVisitor v, String className, Class<?> beanClass) {
		super(v);
		
		beanType = Type.getType(beanClass);
		
		ca = ClassAccessor.getInstance(beanClass);
		
		begin_class(Constants.V1_2, Constants.ACC_PUBLIC, className, OBJECT_TYPE, new Type[] { INTERFACE_TYPE }, Constants.SOURCE_FILE);
		
		emit_constructor();
		
		emit_get_bean();
		
		emit_get();
		
		emit_set();
		
		end_class();
	}
	
	protected void emit_constructor() {
		declare_field(Constants.ACC_PRIVATE | Constants.ACC_FINAL, "bean", beanType, null);
		
		Signature sig = new Signature("<init>", Type.VOID_TYPE, new Type[] { beanType });
		
		// public BeanAccessor(T arg0) {
        CodeEmitter e = begin_method(Constants.ACC_PUBLIC, sig, Constants.TYPES_EMPTY);
        
        // super();
        e.load_this();
        e.super_invoke_constructor();
        
        // this.bean = arg0;
        e.load_this();
        e.load_arg(0);
        e.putfield("bean");
        
        // }
        e.return_value();
        e.end_method();
	}
	
	protected void emit_get_bean() {
		Signature sig = new Signature("getBean", beanType, Constants.TYPES_EMPTY);
		
		// public T getBean() {
        CodeEmitter e = begin_method(Constants.ACC_PUBLIC, sig, Constants.TYPES_EMPTY);
        
        // return this.bean;
        e.load_this();
        e.getfield("bean");
        e.return_value();
        
        // }
        e.end_method();
	}
	
	protected abstract void emit_get();
	
	protected abstract void emit_set();
}
