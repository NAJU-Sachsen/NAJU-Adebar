package de.naju.adebar.app.persons.filter.predicate;

import com.querydsl.core.BooleanBuilder;
import de.naju.adebar.app.filter.FilterType;
import de.naju.adebar.model.persons.QPerson;
import de.naju.adebar.model.persons.details.Gender;

public class GenderFilter implements PersonFilter {

  private static final QPerson person = QPerson.person;
  private Gender gender;
  private FilterType filterType;

  public GenderFilter(Gender gender, FilterType filterType) {
    this.gender = gender;
    this.filterType = filterType;
  }

  @Override
  public BooleanBuilder filter(BooleanBuilder input) {
    switch (filterType) {
      case ENFORCE:
        return input.and(person.participantProfile.gender.eq(gender));
      case IGNORE:
        return input.andNot(person.participantProfile.gender.eq(gender));
      default:
        throw new AssertionError(filterType);
    }
  }

}
