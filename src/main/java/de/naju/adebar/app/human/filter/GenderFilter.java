package de.naju.adebar.app.human.filter;

import de.naju.adebar.app.filter.streams.FilterType;
import de.naju.adebar.model.human.Gender;
import de.naju.adebar.model.human.Person;
import java.util.stream.Stream;

/**
 * Filter for persons of a specific gender
 * 
 * @author Rico Bergmann
 */
public class GenderFilter implements PersonFilter {
  private Gender gender;
  private FilterType filterType;

  public GenderFilter(Gender gender, FilterType filterType) {
    this.gender = gender;
    this.filterType = filterType;
  }

  @Override
  public Stream<Person> filter(Stream<Person> personStream) {
    personStream = personStream.filter(Person::isParticipant);
    return personStream
        .filter(p -> filterType.matching(p.getParticipantProfile().getGender(), gender));
  }
}
