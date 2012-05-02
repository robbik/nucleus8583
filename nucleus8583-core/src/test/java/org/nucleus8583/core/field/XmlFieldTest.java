package org.nucleus8583.core.field;

import org.junit.Test;
import org.nucleus8583.core.XmlContext;

public class XmlFieldTest {

	@Test
	public void testLoadXml() throws Exception {
		new XmlContext("classpath:META-INF/nucleus8583/nucleus8583-types.xml");
	}
}
