package de.naju.adebar.app.persons.filter.predicate;

import com.querydsl.core.BooleanBuilder;
import de.naju.adebar.model.core.Email;
import de.naju.adebar.model.persons.QPerson;

public class EmailFilter implements PersonFilter {

  private static final QPerson person = QPerson.person;
  private Email email;

  public EmailFilter(Email email) {
    this.email = email;
  }

  @Override
  public BooleanBuilder filter(BooleanBuilder input) {
    return input.and(person.email.eq(email));
  }

}
