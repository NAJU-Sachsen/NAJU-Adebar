package de.naju.adebar.infrastructure.thymeleaf;

import java.util.StringJoiner;

public class IterableFormatter {
  private static final String DEFAULT_DELIMITER = ";";

  public String join(Iterable<?> it) {
    return join(it, DEFAULT_DELIMITER);
  }

  public String join(Iterable<?> it, String delim) {
    StringJoiner sj = new StringJoiner(delim);
    it.forEach(elem -> sj.add(elem.toString()));
    return sj.toString();
  }

}
