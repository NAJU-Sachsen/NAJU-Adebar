package de.naju.adebar.infrastructure.thymeleaf;

import com.google.common.collect.Sets;
import de.naju.adebar.model.core.Email;
import java.util.Set;
import java.util.StringJoiner;

public class EmailFormatter {

  private static final String MAILTO_PREFIX = "mailto:";
  private static final String ADDRESS_DELIMITER = ";";
  private static final String EVENT_DEFAULT_EMAIL = "event@naju-sachsen.de";
  private static final String MAILTO_BCC_PREFIX = "mailto:" + EVENT_DEFAULT_EMAIL + "?bcc=";

  public String mailto(Iterable<Email> addresses) {
    Set<Email> noDuplicates = Sets.newHashSet(addresses);
    noDuplicates.remove(null);
    StringJoiner joiner = new StringJoiner(ADDRESS_DELIMITER, MAILTO_PREFIX, "");
    return stringifyEmailsWithJoiner(joiner, noDuplicates);
  }

  public String mailtoBcc(Iterable<Email> addresses) {
    Set<Email> noDuplicates = Sets.newHashSet(addresses);
    noDuplicates.remove(null);
    StringJoiner joiner = new StringJoiner(ADDRESS_DELIMITER, MAILTO_BCC_PREFIX, "");
    return stringifyEmailsWithJoiner(joiner, noDuplicates);
  }

  private String stringifyEmailsWithJoiner(StringJoiner joiner, Iterable<Email> addresses) {
    addresses.forEach(email -> joiner.add(email.getValue()));
    return joiner.toString();
  }

}
