package de.naju.adebar.web.model.events.participation.table.columns;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.events.RegistrationInfo;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.web.model.events.participation.table.ParticipantsTable;

/**
 * Formatter used in the {@link ParticipantsTable} to display additional remarks regarding the
 * participants' registration.
 *
 * @author Rico Bergmann
 */
@Service
public class ParticipationRemarksColumnFormatter implements TableColumnFormatter {

  private final MessageSource messageSource;

  /**
   * Constructs a new formatter.
   *
   * @param messageSource which contains default messages if a date of birth is not set. Must not be
   *          {@code null}.
   */
  public ParticipationRemarksColumnFormatter(MessageSource messageSource) {
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
    // the additional remarks may always be displayed
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

    final RegistrationInfo registrationInfo =
        event.getParticipantsList().getParticipationDetailsFor(participant);
    Assert.state(registrationInfo != null,
        String.format("%s does not participate in %s", participant, event));

    return registrationInfo.hasRemarks() //
        ? registrationInfo.getRemarks() //
        : messageSource.getMessage("field.not-set", new Object[] {},
            LocaleContextHolder.getLocale());
  }

}
