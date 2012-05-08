package org.nucleus8583.oim.expression.spi;

import java.util.Collections;

import ognl.Ognl;
import ognl.OgnlContext;

import org.nucleus8583.oim.expression.ExpressionHandler;
import org.nucleus8583.oim.field.spi.Expression;

public class OgnlExpressionHandler implements ExpressionHandler {
	
	public Expression parse(String expression) throws Exception {
		return new OgnlExpression(Ognl.compileExpression(new OgnlContext(), (Object) Collections.EMPTY_MAP, expression));
	}
}
