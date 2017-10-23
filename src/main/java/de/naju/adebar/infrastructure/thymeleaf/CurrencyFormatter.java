package de.naju.adebar.infrastructure.thymeleaf;

import javax.money.Monetary;
import javax.money.MonetaryAmount;

public class CurrencyFormatter {
	
	public String format(MonetaryAmount amount) {
		if (amount.getCurrency() != Monetary.getCurrency("EUR")) {
			return amount.getNumber().toString() + "€";
		}
		return amount.toString();
	}
	
}
