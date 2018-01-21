package de.naju.adebar.app.persons.filter.stream;

import de.naju.adebar.app.filter.DateFilterType;
import de.naju.adebar.model.persons.Person;
import java.time.LocalDate;
import java.util.stream.Stream;

/**
 * Filter for persons depending on their date of birth
 * 
 * @author Rico Bergmann
 */
public class DateOfBirthFilter implements PersonFilter {

  private LocalDate dob;
  private DateFilterType matchType;

  /**
   * @param dob the date of birth to use as an offset
   * @param matchType the way to treat the offset
   * @see DateFilterType
   */
  public DateOfBirthFilter(LocalDate dob, DateFilterType matchType) {
    this.dob = dob;
    this.matchType = matchType;
  }

  @Override
  public Stream<Person> filter(Stream<Person> personStream) {
    personStream = personStream.filter(Person::isParticipant);
    personStream = personStream.filter(p -> p.getParticipantProfile().hasDateOfBirth());
    return personStream
        .filter(p -> matchType.matching(dob, p.getParticipantProfile().getDateOfBirth()));
  }
}
