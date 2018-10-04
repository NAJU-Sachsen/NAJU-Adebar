package de.naju.adebar.web.model.events.participation.table.columns;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.events.RegistrationInfo;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.web.model.events.ParticipantsTable;

/**
 * Formatter used in the {@link ParticipantsTable} to display whether participants have payed the
 * participation fee.
 *
 * @author Rico Bergmann
 */
@Service
public class ParticipationFeePayedColumnFormatter implements TableColumnFormatter {

  /*
   * (non-Javadoc)
   *
   * @see
   * de.naju.adebar.web.model.events.participation.table.columns.TableColumnFormatter#isApplicable(
   * de.naju.adebar.model.events.Event)
   */
  @Override
  public boolean isApplicable(Event event) {
    return event.getParticipationInfo().hasParticipationFee();
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

    if (registrationInfo.isParticipationFeePayed()) {
      return "glyph.ok";
    } else {
      return "glyph.remove";
    }

  }

  /*
   * (non-Javadoc)
   *
   * @see
   * de.naju.adebar.web.model.events.participation.table.columns.TableColumnFormatter#usesHtml()
   */
  @Override
  public boolean usesHtml() {
    // the formatter should display a checked arrow if the form has been sent
    return true;
  }

}
