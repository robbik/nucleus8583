package org.nucleus8583.oim.util;

import java.lang.reflect.Field;

import net.sf.cglib.core.CodeEmitter;
import net.sf.cglib.core.Constants;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;

public class BeanFieldAccessorEmitter extends BeanAccessorEmitter {

	public BeanFieldAccessorEmitter(ClassVisitor v, String className, Class<?> beanClass) {
		super(v, className, beanClass);
	}
	
	protected void emit_get() {
		// public Object get(String)
		CodeEmitter e = begin_method(Constants.ACC_PUBLIC, GET, Constants.TYPES_EMPTY);
		
		Label endIf = null;
		
		for (Field f : ca.fields.values()) {
			endIf = emit_get(e, f);
			
			e.mark(endIf);
		}
		
		emit_get_error(e, endIf);
		
		// }
		e.end_method();
	}
	
	protected void emit_set() {
		// public void set(String, Object)
		CodeEmitter e = begin_method(Constants.ACC_PUBLIC, SET, Constants.TYPES_EMPTY);
		
		Label endIf = e.make_label();
		Label elseIf = null;
		
		for (Field f : ca.fields.values()) {
			elseIf = emit_set(e, f, endIf);
			
			e.mark(elseIf);
		}
		
		emit_set_error(e, elseIf, endIf);
		
		// }
		e.end_method();
	}
	
	private Label emit_get(CodeEmitter e, Field f) {
		Label endIf = e.make_label();
		
		String fname = f.getName();
		
		// if ("<XX>".equals(arg0)) {
		e.push(fname);
		e.load_arg(0);
		e.invoke_virtual(STRING_TYPE, EQUALS);
		e.if_jump(Constants.IFEQ, endIf);
		
		// return bean.XX;
		e.load_this();
		e.getfield("bean");
		e.getfield(Type.getType(f.getDeclaringClass()), fname, Type.getType(f.getType()));
		e.return_value();
		// }
		
		return endIf;
	}
	
	private void emit_get_error(CodeEmitter e, Label endIf) {
		if (endIf != null) {
			e.mark(endIf);
		}
		
		e.new_instance(EXCEPTION_TYPE);
		e.dup();
		
		e.load_arg(0);
		e.invoke_constructor(EXCEPTION_TYPE, EXCEPTION_CTOR);
		
		e.athrow();
	}
	
	private void emit_set_error(CodeEmitter e, Label elseIf, Label endIf) {
		if (elseIf != null) {
			e.mark(elseIf);
		}
		
		e.new_instance(EXCEPTION_TYPE);
		e.dup();
		
		e.load_arg(0);
		e.invoke_constructor(EXCEPTION_TYPE, EXCEPTION_CTOR);
		
		e.athrow();
		
		if (elseIf != null) {
			e.mark(endIf);
			e.return_value();
		}
	}
	
	private Label emit_set(CodeEmitter e, Field f, Label endIf) {
		Label elseIf = e.make_label();
		
		String fname = f.getName();
		Type ftype = Type.getType(f.getType());
		
		// if ("<XX>".equals(arg0)) {
		e.push(fname);
		e.load_arg(0);
		e.invoke_virtual(STRING_TYPE, EQUALS);
		e.if_jump(Constants.IFEQ, elseIf);
		
		// bean.XX = value;
		e.load_this();
		e.getfield("bean");
		
		e.load_arg(1);
		e.checkcast(ftype);
		
		e.putfield(Type.getType(f.getDeclaringClass()), fname, ftype);
		
		e.goTo(endIf);
		// }
		
		return elseIf;
	}
}
