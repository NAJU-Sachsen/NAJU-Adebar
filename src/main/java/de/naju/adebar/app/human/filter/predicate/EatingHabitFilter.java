package de.naju.adebar.app.human.filter.predicate;

import com.querydsl.core.BooleanBuilder;
import de.naju.adebar.model.human.QPerson;

public class EatingHabitFilter implements PersonFilter {

  private final QPerson person = QPerson.person;
  private String eatingHabit;

  public EatingHabitFilter(String eatingHabit) {
    this.eatingHabit = eatingHabit;
  }

  @Override
  public BooleanBuilder filter(BooleanBuilder input) {
    return input.and(person.participantProfile.eatingHabits.containsIgnoreCase(eatingHabit));
  }

}
