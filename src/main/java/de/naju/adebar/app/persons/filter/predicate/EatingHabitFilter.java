package de.naju.adebar.app.persons.filter.predicate;

import com.querydsl.core.BooleanBuilder;
import de.naju.adebar.model.persons.QPerson;

public class EatingHabitFilter implements PersonFilter {

  private static final QPerson person = QPerson.person;
  private String eatingHabit;

  public EatingHabitFilter(String eatingHabit) {
    this.eatingHabit = eatingHabit;
  }

  @Override
  public BooleanBuilder filter(BooleanBuilder input) {
    return input.and(person.participantProfile.eatingHabits.containsIgnoreCase(eatingHabit));
  }

}
