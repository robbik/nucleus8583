package org.nucleus8583.core.field;

import java.util.Comparator;

public class Field {

	public static final Comparator<Field> COMPARATOR_ASC = new Comparator<Field>() {

		public int compare(Field a, Field b) {
			return a.no - b.no;
		}
	};

	private final int no;

	private final Type<?> type;
	
	public Field(int no, Type<?> type) {
		this.no = no;
		this.type = type;
	}
	
	public int getNo() {
		return no;
	}

	public Type<?> getType() {
		return type;
	}
}
