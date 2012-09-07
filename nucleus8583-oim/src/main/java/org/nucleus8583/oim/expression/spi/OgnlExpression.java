package org.nucleus8583.oim.expression.spi;

import java.util.Map;

import ognl.Ognl;

import org.nucleus8583.oim.field.spi.Expression;

public class OgnlExpression extends Expression {
	
	private final Object tree;
	
	public OgnlExpression(Object tree) {
		this.tree = tree;
	}

	public Object eval(Map<String, Object> root, Map<String, Object> tmp) throws Exception {
		return Ognl.getValue(tree, root);
	}
}
