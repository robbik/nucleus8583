package org.nucleus8583.oim.expression.spi;

import ognl.Ognl;

import org.nucleus8583.oim.field.spi.Expression;

public class OgnlExpression extends Expression {
	
	private final Object tree;
	
	public OgnlExpression(Object tree) {
		this.tree = tree;
	}

	public Object eval(Object object) throws Exception {
		return Ognl.getValue(tree, object);
	}
}
