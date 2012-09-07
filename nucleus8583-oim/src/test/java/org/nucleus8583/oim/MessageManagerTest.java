package org.nucleus8583.oim;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.nucleus8583.core.Message;

public class MessageManagerTest {
	
	private static final SimpleDateFormat sdfMMddHHmmss = new SimpleDateFormat("MMddHHmmss");
	
	private static final SimpleDateFormat sdfHHmmss = new SimpleDateFormat("HHmmss");
	
	private static final SimpleDateFormat sdfMMdd = new SimpleDateFormat("MMdd");
	
	private MessageManager manager;
	
	private Map<String, Object> map1;
	
	private Message isoMsg1;
	
	private void initialize1(Date dt) throws Exception {
		String strMMddHHmmss = sdfMMddHHmmss.format(dt);
		String strHHmmss = sdfHHmmss.format(dt);
		String strMMdd = sdfMMdd.format(dt);
		
		map1 = new HashMap<String, Object>();
		map1.put("cardNumber", "9939");
		map1.put("transmissionDateTime", sdfMMddHHmmss.parse(strMMddHHmmss));
		map1.put("stan", 37);
		map1.put("transactionTime", sdfHHmmss.parse(strHHmmss));
		map1.put("transactionDate", sdfMMdd.parse(strMMdd));
		
		map1.put("charge", new BigDecimal(-10).setScale(2, RoundingMode.CEILING));
		map1.put("rrn", "9987");
		map1.put("responseCode", "00");
		
		List<Map<String, Object>> details = new ArrayList<Map<String,Object>>();
		details.add(Collections.singletonMap("z", (Object) "z1"));
		details.add(Collections.singletonMap("z", (Object) "z2"));
		details.add(Collections.singletonMap("z", (Object) "z3"));
		
		map1.put("details", details);
		
		// map1.put("transient:details____count", details.size());

		isoMsg1 = new Message();
		isoMsg1.set(0, "0110");
		isoMsg1.set(2, "9939");
		isoMsg1.set(7, strMMddHHmmss);
		isoMsg1.set(11, "37");
		isoMsg1.set(12, strHHmmss);
		isoMsg1.set(13, strMMdd);
		isoMsg1.set(29, "C0001000-");
		isoMsg1.set(37, "9987");
		isoMsg1.set(39, "00");
		isoMsg1.set(48, "03z1      z2      z3      ");
	}

	@Before
	public void initialize() throws Exception {
		manager = new MessageManager("classpath:META-INF/oim-sample.xml");
		
		Date dt = new Date();

		initialize1(dt);
	}

	@Test
	public void testPersist() throws Exception {
		Message isoMsg = new Message();
		
		manager.persist(isoMsg, "msg1", map1);
		
		assertEquals(isoMsg1, isoMsg);
	}

	@Test
	public void testLoad() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		
		manager.load(isoMsg1, "msg1_rev", map);
		
		assertEquals(map1, map);
	}
}
