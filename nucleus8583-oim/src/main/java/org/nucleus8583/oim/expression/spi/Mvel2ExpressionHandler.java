package org.nucleus8583.oim.expression.spi;

import org.mvel2.MVEL;
import org.nucleus8583.oim.expression.ExpressionHandler;
import org.nucleus8583.oim.field.spi.Expression;

public class Mvel2ExpressionHandler implements ExpressionHandler {
	
	public Expression parse(String expression) throws Exception {
		return new Mvel2Expression(MVEL.compileExpression(expression));
	}
}
