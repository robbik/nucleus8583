package org.nucleus8583.oim.expression.spi;

import ognl.Ognl;

import org.nucleus8583.oim.expression.ExpressionHandler;
import org.nucleus8583.oim.field.spi.Expression;

public class OgnlExpressionHandler implements ExpressionHandler {
	
	public Expression parse(String expression) throws Exception {
		return new OgnlExpression(Ognl.parseExpression(expression));
	}
}
