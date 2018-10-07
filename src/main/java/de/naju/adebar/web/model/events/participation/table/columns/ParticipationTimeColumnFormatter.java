package de.naju.adebar.web.model.events.participation.table.columns;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import de.naju.adebar.infrastructure.thymeleaf.TimeFormatter;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.events.RegistrationInfo;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.web.model.events.participation.table.ParticipantsTable;

/**
 * Formatter used in the {@link ParticipantsTable} to display the participants' participation time.
 *
 * @author Rico Bergmann
 */
@Service
public class ParticipationTimeColumnFormatter implements TableColumnFormatter {

  private final TimeFormatter timeFormatter = new TimeFormatter();
  private final MessageSource messageSource;

  /**
   * Constructs a new formatter.
   *
   * @param messageSource which contains default messages if a date of birth is not set. Must not be
   *          {@code null}.
   */
  public ParticipationTimeColumnFormatter(MessageSource messageSource) {
    Assert.notNull(messageSource, "MessageSource may not be null");
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
    return event.getParticipationInfo().supportsFlexibleParticipationTimes();
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

    final RegistrationInfo registrationInfo =
        event.getParticipantsList().getParticipationDetailsFor(participant);
    Assert.state(registrationInfo != null,
        String.format("%s does not participate in %s", participant, event));

    return registrationInfo.hasParticipationTime() //
        ? timeFormatter.formatTimeSpan(event.getStartTime(),
            registrationInfo.getParticipationTime()) //
        : messageSource.getMessage("field.not-set", new Object[] {},
            LocaleContextHolder.getLocale());
  }

}
