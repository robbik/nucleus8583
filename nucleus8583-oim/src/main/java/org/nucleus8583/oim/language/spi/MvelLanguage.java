package org.nucleus8583.oim.language.spi;

import org.mvel2.MVEL;

public class MvelLanguage implements Language {

	public CompiledExpression compile(String expression) throws Exception {
		return new MvelCompiledExpression(expression);
	}

	private static class MvelCompiledExpression implements CompiledExpression {
		private Object compiled;

		public MvelCompiledExpression(String expression) {
			compiled = MVEL.compileExpression(expression);
		}

		public Object eval(Object root) throws Exception {
			return MVEL.executeExpression(compiled, root);
		}

		public <T> T eval(Object root, Class<? extends T> resultType)
				throws Exception {
			return MVEL.executeExpression(compiled, root, resultType);
		}
	}
}
