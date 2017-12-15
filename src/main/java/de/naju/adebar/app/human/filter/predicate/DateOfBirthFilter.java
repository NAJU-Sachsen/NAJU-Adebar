package de.naju.adebar.app.human.filter.predicate;

import java.time.LocalDate;
import com.querydsl.core.BooleanBuilder;
import de.naju.adebar.app.filter.DateFilterType;
import de.naju.adebar.model.human.QPerson;

public class DateOfBirthFilter implements PersonFilter {

  private final QPerson person = QPerson.person;
  private LocalDate dob;
  private DateFilterType filterType;

  public DateOfBirthFilter(LocalDate dob, DateFilterType filterType) {
    this.dob = dob;
    this.filterType = filterType;
  }

  @Override
  public BooleanBuilder filter(BooleanBuilder input) {
    switch (filterType) {
      case AFTER:
        return input.and(person.participantProfile.dateOfBirth.after(dob));
      case EXACT:
        return input.and(person.participantProfile.dateOfBirth.eq(dob));
      case BEFORE:
        return input.and(person.participantProfile.dateOfBirth.before(dob));
      default:
        throw new AssertionError(filterType);
    }
  }
}
