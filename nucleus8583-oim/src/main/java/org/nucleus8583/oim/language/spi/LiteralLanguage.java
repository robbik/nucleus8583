package org.nucleus8583.oim.language.spi;


public class LiteralLanguage implements Language {

	public CompiledExpression compile(String expression) throws Exception {
		return new LiteralCompiledExpression(expression);
	}

	private static class LiteralCompiledExpression implements
			CompiledExpression {
		private String compiled;

		public LiteralCompiledExpression(String expression) {
			compiled = expression;
		}

		public Object eval(Object root) throws Exception {
			return compiled;
		}

		public <T> T eval(Object root, Class<? extends T> resultType)
				throws Exception {
			Object transformed;

			if (Boolean.class.equals(resultType)) {
				transformed = Boolean.valueOf(compiled);
			} else if (boolean.class.equals(resultType)) {
				transformed = Boolean.parseBoolean(compiled);
			} else if (Integer.class.equals(resultType)) {
				transformed = Integer.valueOf(compiled);
			} else if (int.class.equals(resultType)) {
				transformed = Integer.parseInt(compiled);
			} else if (Long.class.equals(resultType)) {
				transformed = Long.valueOf(compiled);
			} else if (long.class.equals(resultType)) {
				transformed = Long.parseLong(compiled);
			} else if (Float.class.equals(resultType)) {
				transformed = Float.valueOf(compiled);
			} else if (float.class.equals(resultType)) {
				transformed = Float.parseFloat(compiled);
			} else if (Double.class.equals(resultType)) {
				transformed = Double.valueOf(compiled);
			} else if (double.class.equals(resultType)) {
				transformed = Double.parseDouble(compiled);
			} else if (byte[].class.equals(resultType)) {
				transformed = compiled.getBytes();
			} else if (char[].class.equals(resultType)) {
				transformed = compiled.toCharArray();
			} else {
				transformed = compiled;
			}

			return resultType.cast(transformed);
		}
	}
}
