package de.naju.adebar.app.persons.filter.stream;

import de.naju.adebar.app.filter.MatchType;
import de.naju.adebar.model.core.Address;
import de.naju.adebar.model.persons.Person;
import java.util.stream.Stream;

/**
 * Filter for persons depending on their addresses.
 *
 * @author Rico Bergmann
 */
public class AddressFilter implements PersonFilter {

  private Address address;
  private MatchType matchType;

  /**
   * @param address the address to query for
   * @param matchType the way the address should be treated, i.e. whether the address has to
   *     match exactly or whether only specified fields should be considered
   * @throws IllegalArgumentException if {@code matchType \u2209 {EXACT, IF_DEFINED}}
   */
  public AddressFilter(Address address, MatchType matchType) {
    if (matchType == MatchType.AT_LEAST) {
      throw new IllegalArgumentException(matchType.toString() + " not allowed here!");
    }
    this.address = address;
    this.matchType = matchType;
  }

  @Override
  public Stream<Person> filter(Stream<Person> personStream) {
    switch (matchType) {
      case EXACT:
        return personStream.filter(p -> p.getAddress().equals(address));
      case IF_DEFINED:
        if (!address.getCity().isEmpty()) {
          personStream =
              personStream.filter(p -> p.getAddress().getCity().equals(address.getCity()));
        }
        if (!address.getStreet().isEmpty()) {
          personStream =
              personStream.filter(p -> p.getAddress().getStreet().equals(address.getStreet()));
        }
        if (!address.getZip().isEmpty()) {
          personStream = personStream.filter(p -> p.getAddress().getZip().equals(address.getZip()));
        }
        if (!address.getAdditionalInfo().isEmpty()) {
          personStream = personStream
              .filter(p -> p.getAddress().getAdditionalInfo().equals(address.getAdditionalInfo()));
        }
        return personStream;
      default:
        assert false : "Should not have match type: " + matchType;

    }
    return null;
  }
}
