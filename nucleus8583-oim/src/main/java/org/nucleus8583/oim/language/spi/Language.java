package org.nucleus8583.oim.language.spi;

public interface Language {
	CompiledExpression compile(String expression) throws Exception;
}
