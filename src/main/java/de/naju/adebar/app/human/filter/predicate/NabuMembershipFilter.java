package de.naju.adebar.app.human.filter.predicate;

import com.querydsl.core.BooleanBuilder;
import de.naju.adebar.app.filter.FilterType;
import de.naju.adebar.model.human.QPerson;

public class NabuMembershipFilter implements PersonFilter {

  private static final QPerson person = QPerson.person;
  private PersonFilter filterImpl;

  public NabuMembershipFilter(String membershipNumber) {
    this.filterImpl = new NabuMembershipNumberFilter(membershipNumber);
  }

  public NabuMembershipFilter(FilterType filterType) {
    this.filterImpl = new GeneralNabuMembershipFilter(filterType);
  }

  @Override
  public BooleanBuilder filter(BooleanBuilder input) {
    return filterImpl.filter(input);
  }

  private class GeneralNabuMembershipFilter implements PersonFilter {
    private FilterType filterType;

    public GeneralNabuMembershipFilter(FilterType filterType) {
      this.filterType = filterType;
    }

    @Override
    public BooleanBuilder filter(BooleanBuilder input) {
      switch (filterType) {
        case ENFORCE:
          return input.and(person.participantProfile.nabuMembership.isNotNull());
        case IGNORE:
          return input.and(person.participantProfile.nabuMembership.isNull());
        default:
          throw new AssertionError(filterImpl);
      }
    }
  }

  private class NabuMembershipNumberFilter implements PersonFilter {
    private String membershipNumber;

    public NabuMembershipNumberFilter(String membershipNumber) {
      this.membershipNumber = membershipNumber;
    }

    @Override
    public BooleanBuilder filter(BooleanBuilder input) {
      return input.and(person.participantProfile.nabuMembership.membershipNumber
          .containsIgnoreCase(membershipNumber));
    }
  }

}
