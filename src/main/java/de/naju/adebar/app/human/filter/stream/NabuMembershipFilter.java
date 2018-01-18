package de.naju.adebar.app.human.filter.stream;

import de.naju.adebar.app.filter.FilterType;
import de.naju.adebar.model.persons.Person;
import java.util.stream.Stream;

/**
 * @author Rico Bergmann
 */
public class NabuMembershipFilter implements PersonFilter {
  private FilterType filterType;
  private String membershipNumber;

  public NabuMembershipFilter(FilterType filterType) {
    this.filterType = filterType;
  }

  public NabuMembershipFilter(String membershipNumber) {
    this.membershipNumber = membershipNumber;
  }

  @Override
  public Stream<Person> filter(Stream<Person> personStream) {
    personStream = personStream.filter(Person::isParticipant);
    if (membershipNumber != null) {
      Stream<Person> nabuMembers =
          personStream.filter(p -> p.getParticipantProfile().isNabuMember());
      return nabuMembers.filter(p -> p.getParticipantProfile().getNabuMembership()
          .getMembershipNumber().equals(membershipNumber));
    } else {
      return personStream
          .filter(p -> filterType.matching(p.getParticipantProfile().isNabuMember(), true));
    }
  }
}
