package org.nucleus8583.oim.language.spi;

import ognl.Ognl;
import ognl.OgnlContext;


public class OgnlLanguage implements Language {
	private static final Object EMPTY = new Object();

	public CompiledExpression compile(String expression) throws Exception {
		return new OgnlCompiledExpression(expression);
	}

	private static class OgnlCompiledExpression implements CompiledExpression {
		private Object compiled;

		public OgnlCompiledExpression(String expression) throws Exception {
			compiled = Ognl.compileExpression(new OgnlContext(), EMPTY,
					expression);
		}

		public Object eval(Object root) throws Exception {
			return Ognl.getValue(compiled, root);
		}

		public <T> T eval(Object root, Class<? extends T> resultType)
				throws Exception {
			return resultType.cast(Ognl.getValue(compiled, root, resultType));
		}
	}
}
