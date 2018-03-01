package de.naju.adebar.app.persons.filter.predicate;

import com.querydsl.core.BooleanBuilder;
import de.naju.adebar.model.persons.QPerson;

public class PhoneNumberFilter implements PersonFilter {

  private static final QPerson person = QPerson.person;
  private String phoneNumber;

  public PhoneNumberFilter(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.naju.adebar.app.filter.AbstractFilter#filter(java.lang.Object)
   */
  @Override
  public BooleanBuilder filter(BooleanBuilder input) {
    return input.and(person.phoneNumber.value.eq(phoneNumber));
  }
}
