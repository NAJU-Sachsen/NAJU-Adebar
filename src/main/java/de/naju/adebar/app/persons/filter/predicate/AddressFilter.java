package de.naju.adebar.app.persons.filter.predicate;

import com.querydsl.core.BooleanBuilder;
import de.naju.adebar.app.filter.MatchType;
import de.naju.adebar.model.core.Address;
import de.naju.adebar.model.persons.QPerson;

public class AddressFilter implements PersonFilter {

  private static final QPerson person = QPerson.person;
  private Address address;
  private MatchType matchType;

  public AddressFilter(Address address, MatchType matchType) {
    if (matchType == MatchType.AT_LEAST) {
      throw new IllegalArgumentException(matchType.toString() + " not allowed here!");
    }
    this.address = address;
    this.matchType = matchType;
  }

  @Override
  public BooleanBuilder filter(BooleanBuilder input) {
    switch (matchType) {
      case EXACT:
        fillExact(input);
        return input;
      case IF_DEFINED:
        if (!address.getCity().isEmpty()) {
          fillCityCriteria(input);
        }
        if (!address.getStreet().isEmpty()) {
          fillStreetCriteria(input);
        }
        if (!address.getZip().isEmpty()) {
          fillZipCriteria(input);
        }
        if (!address.getAdditionalInfo().isEmpty()) {
          fillAdditionalInfoCriteria(input);
        }
        return input;
      default:
        assert false : "Should not have match type: " + matchType;
    }
    return null;
  }

  private void fillExact(BooleanBuilder predicate) {
    fillCityCriteria(predicate);
    fillStreetCriteria(predicate);
    fillZipCriteria(predicate);
    fillAdditionalInfoCriteria(predicate);
  }

  private void fillCityCriteria(BooleanBuilder predicate) {
    predicate.and(person.address.city.containsIgnoreCase(address.getCity()));
  }

  private void fillStreetCriteria(BooleanBuilder predicate) {
    predicate.and(person.address.street.containsIgnoreCase(address.getStreet()));
  }

  private void fillZipCriteria(BooleanBuilder predicate) {
    predicate.and(person.address.zip.eq(address.getZip()));
  }

  private void fillAdditionalInfoCriteria(BooleanBuilder predicate) {
    predicate.and(person.address.additionalInfo.containsIgnoreCase(address.getAdditionalInfo()));
  }

}
