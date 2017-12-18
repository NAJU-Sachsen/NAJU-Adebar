package de.naju.adebar.app.human.filter.predicate;

import com.querydsl.core.BooleanBuilder;
import de.naju.adebar.model.human.QPerson;

public class EmailFilter implements PersonFilter {

  private static final QPerson person = QPerson.person;
  private String email;

  public EmailFilter(String email) {
    this.email = email;
  }

  @Override
  public BooleanBuilder filter(BooleanBuilder input) {
    return input.and(person.email.eq(email));
  }

}
