package org.nucleus8583.oim.expression.spi;

import org.mvel2.MVEL;
import org.nucleus8583.oim.field.spi.Expression;

public class Mvel2Expression extends Expression {
	
	private final Object compiled;
	
	public Mvel2Expression(Object compiled) {
		this.compiled = compiled;
	}

	public Object eval(Object object) throws Exception {
		return MVEL.executeExpression(compiled, object);
	}
}
