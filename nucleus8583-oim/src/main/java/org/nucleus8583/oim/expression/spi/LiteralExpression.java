package org.nucleus8583.oim.expression.spi;

import java.util.Map;

import org.nucleus8583.oim.field.spi.Expression;

public class LiteralExpression extends Expression {
	
	private final String expression;
	
	public LiteralExpression(String expression) {
		this.expression = expression;
	}

	public Object eval(Map<String, Object> root) throws Exception {
		return expression;
	}
}
