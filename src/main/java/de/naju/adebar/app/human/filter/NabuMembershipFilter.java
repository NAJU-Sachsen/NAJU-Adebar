package de.naju.adebar.app.human.filter;

import de.naju.adebar.app.filter.FilterType;
import de.naju.adebar.model.human.Person;

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
        if (membershipNumber != null) {
            Stream<Person> nabuMembers = personStream.filter(p -> p.getNabuMembership().isNabuMember());
            return nabuMembers.filter(p -> p.getNabuMembership().getMembershipNumber().equals(membershipNumber));
        } else {
            return personStream.filter(p -> filterType.matching(p.getNabuMembership().isNabuMember(), true));
        }
    }
}
