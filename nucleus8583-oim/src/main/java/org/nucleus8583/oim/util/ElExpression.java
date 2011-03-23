package org.nucleus8583.oim.util;

import java.util.Collections;
import java.util.Map;

import ognl.Ognl;
import ognl.OgnlContext;

import org.mvel2.MVEL;

public class ElExpression {
	private static final int EL_TYPE_MVEL = 1;

	private static final int EL_TYPE_MVEL_CONST = 2;

	private static final int EL_TYPE_OGNL = 3;

	private static final int EL_TYPE_OGNL_CONST = 4;

	private static final int EL_TYPE_LITERAL = 5;

	private static final Object EMPTY = new Object();

	private final String expression;

	private final Object compiled;

	private final int type;

	public static Object eval(String expression, Map<String, Object> vars) {
		return new ElExpression(expression).eval(vars);
	}

	public static Object eval(String expression) {
		return new ElExpression(expression).eval(Collections
				.<String, Object> emptyMap());
	}

	public ElExpression(String expression) {
		if (expression.startsWith("mvel:const:")) {
			this.expression = expression.substring(11);

			try {
				compiled = MVEL.eval(this.expression);
			} catch (RuntimeException e) {
				throw new RuntimeException(
						"unable to evaluate mvel expression " + this.expression,
						e);
			}

			type = EL_TYPE_MVEL_CONST;
		} else if (expression.startsWith("mvel:")) {
			this.expression = expression.substring(5);

			try {
				compiled = MVEL.compileExpression(this.expression);
			} catch (RuntimeException e) {
				throw new RuntimeException("unable to compile mvel expression "
						+ this.expression, e);
			}

			type = EL_TYPE_MVEL;
		} else if (expression.startsWith("ognl:const:")) {
			this.expression = expression.substring(11);

			try {
				compiled = Ognl.getValue(this.expression, new Object());
			} catch (Throwable e) {
				throw new RuntimeException(
						"unable to evaluate ognl expression " + this.expression,
						e);
			}

			type = EL_TYPE_OGNL_CONST;
		} else if (expression.startsWith("ognl:")) {
			this.expression = expression.substring(5);

			try {
				compiled = Ognl.compileExpression(new OgnlContext(), EMPTY,
						this.expression);
			} catch (Throwable e) {
				throw new RuntimeException("unable to compile ognl expression "
						+ this.expression, e);
			}

			type = EL_TYPE_OGNL;
		} else if (expression.startsWith("literal:")) {
			this.expression = expression.substring(8);

			compiled = null;
			type = EL_TYPE_LITERAL;
		} else {
			this.expression = expression;

			compiled = null;
			type = EL_TYPE_LITERAL;
		}
	}

	public Object eval(Map<String, Object> vars) {
		switch (type) {
		case EL_TYPE_MVEL_CONST:
			return compiled;
		case EL_TYPE_MVEL:
			try {
				return MVEL.executeExpression(compiled, vars);
			} catch (Throwable e) {
				throw new RuntimeException(
						"unable to evaluate compiled mvel expression "
								+ expression, e);
			}
		case EL_TYPE_OGNL_CONST:
			return compiled;
		case EL_TYPE_OGNL:
			try {
				return Ognl.getValue(compiled, vars);
			} catch (Throwable e) {
				throw new RuntimeException(
						"unable to evaluate compiled ognl expression "
								+ expression, e);
			}
		case EL_TYPE_LITERAL:
			return expression;
		default:
			return null;
		}
	}

	public Object eval(Object root) {
		switch (type) {
		case EL_TYPE_MVEL_CONST:
			return compiled;
		case EL_TYPE_MVEL:
			try {
				return MVEL.executeExpression(compiled, root);
			} catch (Throwable e) {
				throw new RuntimeException(
						"unable to evaluate compiled mvel expression "
								+ expression, e);
			}
		case EL_TYPE_OGNL_CONST:
			return compiled;
		case EL_TYPE_OGNL:
			try {
				return Ognl.getValue(compiled, root);
			} catch (Throwable e) {
				throw new RuntimeException(
						"unable to evaluate compiled ognl expression "
								+ expression, e);
			}
		case EL_TYPE_LITERAL:
			return expression;
		default:
			return null;
		}
	}

	public boolean isConstant() {
		switch (type) {
		case EL_TYPE_MVEL_CONST:
			return true;
		case EL_TYPE_MVEL:
			return false;
		case EL_TYPE_OGNL_CONST:
			return true;
		case EL_TYPE_OGNL:
			return false;
		case EL_TYPE_LITERAL:
			return true;
		default:
			return false;
		}
	}
}
