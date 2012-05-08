package org.nucleus8583.oim.xml;

import org.nucleus8583.oim.field.Field;
import org.nucleus8583.oim.field.spi.ListCount;

public class ListCountFactory extends BasicFactory {

	protected Field createInstance() {
		ListCount lc = new ListCount();
		lc.setNo(no);
		lc.setName(name);
		lc.setType(reInitializeType());
		
		lc.initialize();
		
		return lc;
	}
}
