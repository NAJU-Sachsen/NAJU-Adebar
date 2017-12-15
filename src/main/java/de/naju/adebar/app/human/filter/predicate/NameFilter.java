package de.naju.adebar.app.human.filter.predicate;

import com.querydsl.core.BooleanBuilder;
import de.naju.adebar.model.human.QPerson;

public class NameFilter implements PersonFilter {

  private final QPerson person = QPerson.person;
  private String firstName;
  private String lastName;

  public NameFilter(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  @Override
  public BooleanBuilder filter(BooleanBuilder input) {
    if (firstName != null && !firstName.isEmpty()) {
      input.and(person.firstName.containsIgnoreCase(firstName));
    }

    if (lastName != null && !lastName.isEmpty()) {
      input.and(person.lastName.containsIgnoreCase(lastName));
    }

    return input;
  }

}
