package de.naju.adebar.web.model.events.participation.table.columns;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.persons.ParticipantProfile;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.web.model.events.ParticipantsTable;

/**
 * Formatter used in the {@link ParticipantsTable} to display the participants' eating habits.
 *
 * @author Rico Bergmann
 */
@Service
public class EatingHabitsColumnFormatter implements TableColumnFormatter {

  /*
   * (non-Javadoc)
   *
   * @see
   * de.naju.adebar.web.model.events.participation.table.columns.TableColumnFormatter#isApplicable(
   * de.naju.adebar.model.events.Event)
   */
  @Override
  public boolean isApplicable(Event event) {
    // the participant's eating habits may always be displayed
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
    Assert.state(participant.isParticipant(), "Person is no partcipant: " + participant);
    final ParticipantProfile profile = participant.getParticipantProfile();
    return profile.hasEatingHabits() ? profile.getEatingHabits() : "";
  }

}
