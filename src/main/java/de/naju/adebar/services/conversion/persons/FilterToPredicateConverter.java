package de.naju.adebar.services.conversion.persons;

import org.springframework.stereotype.Service;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import de.naju.adebar.model.persons.QPerson;

/**
 * Service to create a predicate for filter information
 *
 * @author Rico Bergmann
 */
@Service
public class FilterToPredicateConverter {

  /**
   * Creates a predicate for a generic person filter. Each parameter which is either {@code null} or
   * empty will be ignored.
   *
   * @param firstName the person's first name
   * @param lastName the person's last name
   * @param city the city the person resides in
   * @return the predicate which describes the given criteria
   */
  public Predicate fromFields(String firstName, String lastName, String city) {
    QPerson person = QPerson.person;
    BooleanBuilder predicate = new BooleanBuilder();

    predicate.and(person.archived.isFalse());
    if (firstName != null && !firstName.isEmpty()) {
      predicate.and(person.firstName.contains(firstName));
    }

    if (lastName != null && !lastName.isEmpty()) {
      predicate.and(person.lastName.contains(lastName));
    }

    if (city != null && !city.isEmpty()) {
      predicate.and(person.address.city.eq(city));
    }

    return predicate;
  }

  /**
   * Creates a predicate for a generic person filter. Each parameter which is either {@code null} or
   * empty will be ignored.
   *
   * @param firstName the person's first name
   * @param lastName the person's last name
   * @param city the city the person resides in
   * @param activist whether the person has to be an activist
   * @param referent whether the person has to be a referent
   * @return the predicate which describes the given criteria
   */
  public Predicate fromFields(String firstName, String lastName, String city, Boolean activist,
      Boolean referent) {
    QPerson person = QPerson.person;
    BooleanBuilder predicate = (BooleanBuilder) fromFields(firstName, lastName, city);

    if (activist != null) {
      predicate.and(person.activist.eq(activist));
    }

    if (referent != null) {
      predicate.and(person.referent.eq(referent));
    }

    return predicate;
  }

  /**
   * Creates a predicate for a persons which are activists. Each parameter which is either
   * {@code null} or empty will be ignored.
   *
   * @param firstName the activist's first name
   * @param lastName the activist's last name
   * @param city the city the activist resides in
   * @return the predicate which describes the given criteria
   */
  public Predicate activistsFromFields(String firstName, String lastName, String city) {
    QPerson person = QPerson.person;
    BooleanBuilder predicate = new BooleanBuilder();

    predicate.and(person.archived.isFalse());
    predicate.and(person.activist.isTrue());

    if (firstName != null && !firstName.isEmpty()) {
      predicate.and(person.firstName.contains(firstName));
    }

    if (lastName != null && !lastName.isEmpty()) {
      predicate.and(person.lastName.contains(lastName));
    }

    if (city != null && !city.isEmpty()) {
      predicate.and(person.address.city.eq(city));
    }

    return predicate;
  }

}
