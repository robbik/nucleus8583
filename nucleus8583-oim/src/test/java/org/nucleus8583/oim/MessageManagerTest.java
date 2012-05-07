package org.nucleus8583.oim;

import java.math.BigDecimal;
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
	private MessageManager manager;

	@Before
	public void initialize() throws Exception {
		manager = new MessageManager("classpath:META-INF/oim-sample.xml");
	}

	@Test
	public void testPersist() throws Exception {
		Message isoMsg = new Message();
		
		Date dt = new Date();
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("mti", "0110");
		map.put("cardNumber", "9939");
		map.put("transmissionDateTime", dt);
		map.put("stan", 37);
		map.put("transactionTime", dt);
		map.put("transactionDate", dt);
		map.put("charge", new BigDecimal(10));
		map.put("rrn", "9987");
		map.put("responseCode", "00");
		
		List<Map<String, Object>> details = new ArrayList<Map<String,Object>>();
		details.add(Collections.singletonMap("z", (Object) "z1"));
		details.add(Collections.singletonMap("z", (Object) "z2"));
		details.add(Collections.singletonMap("z", (Object) "z3"));
		
		map.put("details_count", details.size());
		map.put("details", details);
		
		int n = 100000;
		
		long dtstart = System.currentTimeMillis();
		
		for (int i = 0; i < n; ++i) {
			manager.persist(isoMsg, "msg1", map);
		}
		
		long dtend = System.currentTimeMillis();
		
		System.out.println((float) (n * 1000) / (float) (dtend - dtstart));
		
		System.out.println(isoMsg);
	}

	@Test
	public void testLoad() throws Exception {
		Message isoMsg = new Message();
		isoMsg.setMti("0110");
		isoMsg.set(2, "9939");
		isoMsg.set(7, "0507194411");
		isoMsg.set(11, "37");
		isoMsg.set(12, "194411");
		isoMsg.set(13, "0507");
		isoMsg.set(29, "C00001000");
		isoMsg.set(37, "9987");
		isoMsg.set(39, "00");
		isoMsg.set(48, "03z1      z2      z3      ");
		
		Map<String, Object> map = new HashMap<String, Object>();
		int n = 100000;
		
		long dtstart = System.currentTimeMillis();
		
		for (int i = 0; i < n; ++i) {
			map.clear();

			manager.load(isoMsg, "msg1_rev", map);
		}
		
		long dtend = System.currentTimeMillis();
		
		System.out.println((float) (n * 1000) / (float) (dtend - dtstart));
		
		System.out.println(map);
	}
}
