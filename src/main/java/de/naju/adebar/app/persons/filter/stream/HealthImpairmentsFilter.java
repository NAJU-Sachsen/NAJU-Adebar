package de.naju.adebar.app.persons.filter.stream;

import de.naju.adebar.model.persons.Person;
import java.util.stream.Stream;

/**
 * Filter for persons depending on health impairments. The filter is'fuzzy' - i.e. the health
 * impairments do not need to be exactly equal, but the person's data need to contain the health
 * impairment specified
 * 
 * @author Rico Bergmann
 */
public class HealthImpairmentsFilter implements PersonFilter {
  private String healthImpairments;

  public HealthImpairmentsFilter(String healthImpairments) {
    this.healthImpairments = healthImpairments;
  }

  @Override
  public Stream<Person> filter(Stream<Person> personStream) {
    personStream = personStream.filter(Person::isParticipant);
    return personStream
        .filter(p -> p.getParticipantProfile().getHealthImpairments().contains(healthImpairments));
  }
}
