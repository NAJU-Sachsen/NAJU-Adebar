package de.naju.adebar.web.model.events.participation.table.columns;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.persons.ParticipantProfile;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.web.model.events.ParticipantsTable;

/**
 * Formatter used in the {@link ParticipantsTable} to display the date of birth of the participants.
 *
 * @author Rico Bergmann
 */
@Service
public class DateOfBirthColumnFormatter implements TableColumnFormatter {

  // we do not need to worry about l10n yet
  private static final DateTimeFormatter DATE_FORMAT =
      DateTimeFormatter.ofPattern("dd.MM.yyy", Locale.GERMAN);

  private final MessageSource messageSource;

  /**
   * Constructs a new formatter.
   *
   * @param messageSource which contains default messages if a date of birth is not set. Must not be
   *          {@code null}.
   */
  public DateOfBirthColumnFormatter(MessageSource messageSource) {
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
    // the participant's date of birth may always be formatted (even if it is unknown)
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
    Assert.state(participant.isParticipant(), "Person is no participant: " + participant);
    final ParticipantProfile profile = participant.getParticipantProfile();
    if (!profile.hasDateOfBirth()) {
      return messageSource.getMessage("field.unknown", new Object[] {},
          LocaleContextHolder.getLocale());
    }
    return profile.getDateOfBirth().format(DATE_FORMAT);
  }

}
