package de.naju.adebar.app.persons.filter.stream;

import de.naju.adebar.model.persons.Person;
import java.util.stream.Stream;

/**
 * Filter for persons depending on their eating habit. The filter is'fuzzy' - i.e. the eating habits
 * do not need to be exactly equal, but the person's data need to contain the eating habit specified
 * 
 * @author Rico Bergmann
 */
public class EatingHabitFilter implements PersonFilter {
  private String eatingHabit;

  public EatingHabitFilter(String eatingHabit) {
    this.eatingHabit = eatingHabit;
  }

  @Override
  public Stream<Person> filter(Stream<Person> personStream) {
    personStream = personStream.filter(Person::isParticipant);
    return personStream
        .filter(p -> p.getParticipantProfile().getEatingHabits().contains(eatingHabit));
  }
}
