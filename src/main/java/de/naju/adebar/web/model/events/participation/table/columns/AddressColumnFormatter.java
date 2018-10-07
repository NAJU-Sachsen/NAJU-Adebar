package de.naju.adebar.web.model.events.participation.table.columns;

import java.util.StringJoiner;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import de.naju.adebar.model.core.Address;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.web.model.events.participation.table.ParticipantsTable;

/**
 * Formatter used in the {@link ParticipantsTable} to display participants' addresses.
 *
 * @author Rico Bergmann
 */
@Service
public class AddressColumnFormatter implements TableColumnFormatter {

  private static final String STREET_DELIM = ", ";
  private static final String ZIP_DELIM = " ";

  private final MessageSource messageSource;

  /**
   * Constructs a new formatter.
   *
   * @param messageSource which contains default messages if addresses are empty. Must not be
   *          {@code null}.
   */
  public AddressColumnFormatter(MessageSource messageSource) {
    Assert.notNull(messageSource, "Message source may not be null");
    this.messageSource = messageSource;
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * de.naju.adebar.web.model.events.participation.table.columns.TableColumnFormatter#isApplicable(
   * de.naju.adebar.model.events.Event)
   */
  @Override
  public boolean isApplicable(Event event) {
    // the participant's address may always be formatted
    return true;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.naju.adebar.web.model.events.participation.table.columns.TableColumnFormatter#
   * formatColumnFor(de.naju.adebar.model.persons.Person, de.naju.adebar.model.events.Event)
   */
  @Override
  public String formatColumnFor(Person participant, Event event) {
    final Address participantAddress = participant.getAddress();

    if (participantAddress.isEmpty()) {
      return messageSource.getMessage("field.unknown", new Object[] {},
          LocaleContextHolder.getLocale());
    }

    /*
     * The resulting String should look like "street, zip city".
     *
     * However some of the fields may be null, which could lead to a misplaced comma and the like if
     * a naive approach is chosen.
     *
     * Therefore we try to join the result from its substrings like this: JOIN(street, JOIN(zip,
     * city)).
     *
     * If any of the fields are not set, the delimiters will not be inserted, ultimately leading to
     * the desired outcome.
     */

    StringJoiner streetAndZipCityJoiner = new StringJoiner(STREET_DELIM);

    // we try to insert the street first
    if (participantAddress.hasStreet()) {
      streetAndZipCityJoiner.add(participantAddress.getStreet());
    }

    // afterwards we try to get zip and city
    StringJoiner zipAndCityJoiner = new StringJoiner(ZIP_DELIM);
    if (participantAddress.hasZip()) {
      zipAndCityJoiner.add(participantAddress.getZip());
    }
    if (participantAddress.hasCity()) {
      zipAndCityJoiner.add(participantAddress.getCity());
    }

    // if zip and city where joined successfully (i.e. at least one field was set), we combine it
    // with the street
    String zipAndCity = zipAndCityJoiner.toString();
    if (!zipAndCity.isEmpty()) {
      streetAndZipCityJoiner.add(zipAndCity);
    }

    return streetAndZipCityJoiner.toString();
  }

}
