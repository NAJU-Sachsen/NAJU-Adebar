package de.naju.adebar.app.events.filter;

import de.naju.adebar.app.filter.MatchType;
import de.naju.adebar.model.core.Address;
import de.naju.adebar.model.events.Event;
import java.util.stream.Stream;

/**
 * Filter for events based on where they take place
 *
 * @author Rico Bergmann
 */
public class AddressFilter implements EventFilter {

  private Address address;
  private MatchType matchType;

  /**
   * @param address the address to base the filter on
   * @param matchType how restrictive the filter is
   */
  public AddressFilter(Address address, MatchType matchType) {
    if (matchType == MatchType.AT_LEAST) {
      throw new IllegalArgumentException(matchType.toString() + " not allowed here!");
    }
    this.address = address;
    this.matchType = matchType;
  }

  @Override
  public Stream<Event> filter(Stream<Event> input) {
    input = input.filter(event -> definedAddress(event.getPlace()));
    switch (matchType) {
      case EXACT:
        return input.filter(event -> event.getPlace().equals(address));
      case IF_DEFINED:
        if (stringIsSet(address.getStreet())) {
          input = input.filter(event -> event.getPlace().getStreet().equals(address.getStreet()));
        }
        if (stringIsSet(address.getZip())) {
          input = input.filter(event -> event.getPlace().getZip().equals(address.getZip()));
        }
        if (stringIsSet(address.getCity())) {
          input = input.filter(event -> event.getPlace().getCity().equals(address.getCity()));
        }
        if (stringIsSet(address.getAdditionalInfo())) {
          input = input.filter(
              event -> event.getPlace().getAdditionalInfo().equals(address.getAdditionalInfo()));
        }
        return input;
      default:
        assert false : "Should not have match type: " + matchType;
    }
    return null;
  }

  /**
   * @param address the address to check
   * @return {@code true} if at least one field of the address is defined, {@code false} otherwise
   */
  protected boolean definedAddress(Address address) {
    if (address == null) {
      return false;
    } else if (stringIsSet(address.getStreet())) {
      return true;
    } else if (stringIsSet(address.getZip())) {
      return true;
    } else if (stringIsSet(address.getCity())) {
      return true;
    }
    return false;
  }

  /**
   * @param str the string to check
   * @return {@code true} if the string is neither {@code null} nor empty
   */
  protected boolean stringIsSet(String str) {
    return str != null && !str.isEmpty();
  }
}
