package de.naju.adebar.app.persons.filter.stream;

import java.util.stream.Stream;
import de.naju.adebar.app.filter.FilterType;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.details.NabuMembershipInformation;

/**
 * @author Rico Bergmann
 * @deprecated The filter is not adjusted to the {@link NabuMembershipInformation} capturing whether
 *             a person is NABU member or not. It may be supported again some time in the future but
 *             as of now it should not be used any more.
 */
@Deprecated
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
