package de.naju.adebar.app.persons.filter.predicate;

import com.querydsl.core.BooleanBuilder;
import de.naju.adebar.model.persons.QPerson;
import de.naju.adebar.model.persons.details.NabuMembershipInformation.MembershipStatus;

public class NabuMembershipFilter implements PersonFilter {

  private static final QPerson person = QPerson.person;
  private PersonFilter filterImpl;

  public NabuMembershipFilter(String membershipNumber) {
    this.filterImpl = new NabuMembershipNumberFilter(membershipNumber);
  }

  public NabuMembershipFilter(MembershipStatus membershipStatus) {
    this.filterImpl = new GeneralNabuMembershipFilter(membershipStatus);
  }

  @Override
  public BooleanBuilder filter(BooleanBuilder input) {
    return filterImpl.filter(input);
  }

  private class GeneralNabuMembershipFilter implements PersonFilter {
    private MembershipStatus membershipStatus;

    public GeneralNabuMembershipFilter(MembershipStatus membershipStatus) {
      this.membershipStatus = membershipStatus;
    }

    @Override
    public BooleanBuilder filter(BooleanBuilder input) {
      switch (membershipStatus) {
        case IS_MEMBER:
          return input.and(person.participantProfile.nabuMembership.nabuMember.isTrue());
        case NO_MEMBER:
          return input.and(person.participantProfile.nabuMembership.nabuMember.isFalse());
        case UNKNOWN:
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
