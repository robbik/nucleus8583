package org.nucleus8583.oim.expression.spi;

import org.nucleus8583.oim.expression.ExpressionHandler;
import org.nucleus8583.oim.field.spi.Expression;

public class LiteralExpressionHandler implements ExpressionHandler {
	
	public Expression parse(String expression) throws Exception {
		return new LiteralExpression(expression);
	}
}
