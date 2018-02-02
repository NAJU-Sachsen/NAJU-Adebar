package de.naju.adebar.infrastructure.thymeleaf;

import javax.money.Monetary;
import javax.money.MonetaryAmount;

/**
 * Formatter for instances of {@link MonetaryAmount}
 *
 * @author Rico Bergmann
 */
public class CurrencyFormatter {

  /**
   * @param amount the money to format
   * @return if the money's currency is Euro, then {@code XXX€}, otherwise the {@code toString} will
   *         be used
   */
  public String format(MonetaryAmount amount) {
    if (amount.getCurrency().equals(Monetary.getCurrency("EUR"))) {
      return amount.getNumber().toString() + "€";
    }
    return amount.toString();
  }

}
