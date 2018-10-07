package de.naju.adebar.web.model.events.participation.table.columns;

import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.web.model.events.participation.table.ParticipantsTable;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Formatter used in the {@link ParticipantsTable} to display whether participant may leave from the
 * event on their own (or whether they have to be picked up by some relative).
 *
 * @author Rico Bergmann
 */
@Service
public class MayGoHomeSinglyColumnFormatter implements TableColumnFormatter {

  private final MessageSource messageSource;

  /**
   * Constructs a new formatter.
   *
   * @param messageSource which contains default messages if a date of birth is not set. Must
   *     not be {@code null}.
   */
  public MayGoHomeSinglyColumnFormatter(MessageSource messageSource) {
    Assert.notNull(messageSource, "MessageSource may not be null");
    this.messageSource = messageSource;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.naju.adebar.web.model.events.participation.table.columns.TableColumnFormatter#isApplicable(
   * de.naju.adebar.model.events.Event)
   */
  @Override
  public boolean isApplicable(Event event) {
    // leave options may always be displayed
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
    Assert.notNull(participant, "Participant may not be null");
    Assert.notNull(event, "Event may not be null");
    assertIsApplicable(event);

    return event.getParticipantsList().getParticipationDetailsFor(participant).mayGoHomeSingly() //
        ? messageSource.getMessage("yes", new Object[]{}, LocaleContextHolder.getLocale()) //
        : messageSource.getMessage("no", new Object[]{}, LocaleContextHolder.getLocale());
  }

}
