package org.nucleus8583.oim.metadata;

import java.math.BigDecimal;
import java.util.Date;

@SuppressWarnings("unused")
public class Ipm1 {
	private String cardNumber = "31254";

	private String stan = "2";

	private Date transactionDate = new Date();

	private Date transactionTime = new Date();

	private BigDecimal charge = BigDecimal.valueOf(1500);

	private String rrn = "1762745214";

	private String responseCode = "";

	private Ipm1Detail[] details;

	public Ipm1() {
		// details = new Vector<Ipm1Detail>();
		// details.add(new Ipm1Detail());

		details = new Ipm1Detail[] { new Ipm1Detail() };
	}

	public Ipm1Detail[] getDetails() {
		return details;
	}
}
