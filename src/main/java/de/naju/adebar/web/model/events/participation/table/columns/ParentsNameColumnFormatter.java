package de.naju.adebar.web.model.events.participation.table.columns;

import java.util.StringJoiner;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.web.model.events.participation.table.ParticipantsTable;

/**
 * Formatter used in the {@link ParticipantsTable} to display the parents' names.
 *
 * @author Rico Bergmann
 */
@Service
public class ParentsNameColumnFormatter implements TableColumnFormatter {

  private static final String COMPLETE_NAME_DELIM = ", ";
  private static final String FIRST_NAME_ONLY_DELIM = ", ";
  private static final String FIRST_NAMES_AND_LAST_NAME_DELIM = " + ";
  private static final String FIRST_NAME_LAST_NAME_SEPARATOR = " ";

  /*
   * (non-Javadoc)
   *
   * @see
   * de.naju.adebar.web.model.events.participation.table.columns.TableColumnFormatter#isApplicable(
   * de.naju.adebar.model.events.Event)
   */
  @Override
  public boolean isApplicable(Event event) {
    // a participant's parents may always be displayed
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
    if (!participant.hasParents()) {
      return "";
    }

    Streamable<Person> parents = Streamable.of(participant.getParents());

    String firstParentLastName = parents.stream() //
        .findFirst() //
        .map(Person::getLastName) //
        .orElseThrow(AssertionError::new);

    boolean allParentsShareLastName = parents.stream() //
        .map(Person::getLastName) //
        .allMatch(ln -> ln.equals(firstParentLastName));

    boolean parentsLastNameMatchesParticipant =
        allParentsShareLastName && firstParentLastName.equals(participant.getLastName());

    if (parentsLastNameMatchesParticipant) {
      StringJoiner parentsFirstNameJoiner = new StringJoiner(FIRST_NAME_ONLY_DELIM);
      parents.stream().map(Person::getFirstName).forEach(parentsFirstNameJoiner::add);
      return parentsFirstNameJoiner.toString();
    } else if (allParentsShareLastName) {
      StringJoiner parentsFirstNameJoiner = new StringJoiner(FIRST_NAMES_AND_LAST_NAME_DELIM);
      parents.stream().map(Person::getFirstName).forEach(parentsFirstNameJoiner::add);
      return parentsFirstNameJoiner.toString() + FIRST_NAME_LAST_NAME_SEPARATOR
          + firstParentLastName;
    } else {
      StringJoiner parentsNameJoiner = new StringJoiner(COMPLETE_NAME_DELIM);
      parents.stream() //
          .map(p -> p.getFirstName() + FIRST_NAME_LAST_NAME_SEPARATOR + p.getLastName()) //
          .forEach(parentsNameJoiner::add);
      return parentsNameJoiner.toString();
    }
  }

}
