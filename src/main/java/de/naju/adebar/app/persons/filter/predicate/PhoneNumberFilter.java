package de.naju.adebar.app.persons.filter.predicate;

import com.querydsl.core.BooleanBuilder;
import de.naju.adebar.model.core.PhoneNumber;
import de.naju.adebar.model.persons.QPerson;

public class PhoneNumberFilter implements PersonFilter {

  private static final QPerson person = QPerson.person;
  private PhoneNumber phoneNumber;

  public PhoneNumberFilter(PhoneNumber phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  /*
   * (non-Javadoc)
   *
   * @see de.naju.adebar.app.filter.AbstractFilter#filter(java.lang.Object)
   */
  @Override
  public BooleanBuilder filter(BooleanBuilder input) {
    return input.and(person.phoneNumber.eq(phoneNumber)
        .or(person.parent.isTrue().and(person.parentProfile.workPhone.eq(phoneNumber)))
        .or(person.parent.isTrue().and(person.parentProfile.landlinePhone.eq(phoneNumber))));
  }

}
