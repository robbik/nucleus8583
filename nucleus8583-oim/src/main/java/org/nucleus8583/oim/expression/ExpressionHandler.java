package org.nucleus8583.oim.expression;

import org.nucleus8583.oim.field.spi.Expression;

public interface ExpressionHandler {

	Expression parse(String expression) throws Exception;
}
