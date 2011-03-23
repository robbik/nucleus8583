package org.nucleus8583.oim.language.spi;

public interface CompiledExpression {
	Object eval(Object root) throws Exception;

	<T> T eval(Object root, Class<? extends T> resultType) throws Exception;
}
