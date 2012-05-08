package org.nucleus8583.oim.xml;

import org.nucleus8583.oim.expression.ExpressionHandlerResolver;
import org.nucleus8583.oim.field.spi.Expression;

public class ExpressionFactory extends BasicFactory {

	private ExpressionHandlerResolver resolver;

	private String language;

	private String expression;

	public void setResolver(ExpressionHandlerResolver resolver) {
		this.resolver = resolver;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	protected Expression createInstance() {
		Expression exp;
		
		try {
			exp = resolver.parse(language, expression);
		} catch (Exception e) {
			throw new IllegalArgumentException("unable to parse expression '"
					+ expression + "' for language " + language, e);
		}

		exp.setNo(no);
		exp.setType(reInitializeType());

		return exp;
	}
}
