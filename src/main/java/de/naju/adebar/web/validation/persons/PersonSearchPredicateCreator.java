package de.naju.adebar.web.validation.persons;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import de.naju.adebar.model.core.Email;
import de.naju.adebar.model.core.PhoneNumber;
import de.naju.adebar.model.persons.PersonId;
import de.naju.adebar.model.persons.QPerson;
import de.naju.adebar.util.Validation;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

/**
 * Service to create database query predicates for searches
 *
 * @author Rico Bergmann
 * @deprecated the {@link de.naju.adebar.app.search.persons.PersonSearchServer} should be used
 *     instead
 */
@Service
@Deprecated
public class PersonSearchPredicateCreator {

  private static final QPerson person = QPerson.person;

  private static final String FIRST_NAME_LAST_NAME_REGEX =
      "^(?<firstName>([a-zA-ZöÖüÜäÄß]+(\\w*-?\\w*[a-zA-ZöÖüÜäÄß])?)+) (?<lastName>(von |zu )?([a-zA-ZöÖüÜäÄß]+(\\s*(-\\s*)?[a-zA-ZöÖüÜäÄß]+)?)+)$";
  private static final String NAME_REGEX = "^([a-zA-ZöÖüÜäÄß]+(\\s*(-\\s*)?[a-zA-ZöÖüÜäÄß]+)?)+$";

  private final List<PredicateCreator> predicateCreators;

  public PersonSearchPredicateCreator() {
    /*
     * Don't use the generic creator here it will be used lastly. Also use the NamePredicateCreator
     * before the FirstNameOrLastNamePredicateCreator as the former is a special case of the latter.
     */
    this.predicateCreators = Arrays.asList( //
        new NamePredicateCreator(), //
        new FirstNameOrLastNamePredicateCreator(), //
        new EmailPredicateCreator(), //
        new PhoneNumberPredicateCreator());
  }

  /**
   * The predicate will filter for all persons whose name, address (city), phone number or email is
   * like the query specified. It will also try to infer, what the query "means" by matching it
   * against patterns for name, phone number and email.
   *
   * @param query the criteria
   * @return the predicate
   */
  public Predicate createPredicate(String query) {
    return createPredicate(query, null);
  }

  /**
   * Basically works the same as {@link #createPredicate(String)} but excludes the person with the
   * given ID from results.
   *
   * @param query the cirteria
   * @param personId the person to exclude
   * @return the predicate
   */
  public Predicate createPredicate(@NonNull String query, PersonId personId) {
    BooleanBuilder predicate = null;
    boolean matched = false;

    for (PredicateCreator creator : predicateCreators) {
      if (creator.isApplicable(query)) {
        matched = true;
        predicate = creator.generatePredicate(query);
        break;
      }
    }

    if (!matched) {
      predicate = new GenericPredicateCreator().generatePredicate(query);
    }

    if (personId != null) {
      predicate.and(person.id.ne(personId));
    }

    return predicate;
  }


  /**
   * Service to perform the actual query creation
   *
   * @author Rico Bergmann
   */
  private interface PredicateCreator {

    /**
     * Creates the predicate
     *
     * @param query the query
     * @return the predicate
     *
     * @throws IllegalStateException if the generator is not applicable for the given type of
     *     query
     */
    BooleanBuilder generatePredicate(String query);

    /**
     * Checks, whether the generator may generate a predicate from the given query. This must not
     * always be the case, e.g. when the query is expected to be formatted in a certain way.
     *
     * @param query the query to check
     * @return whether the generator works for the query
     */
    boolean isApplicable(String query);

    /**
     * Asserts that the given query is applicable for the query
     *
     * @param query the query
     * @throws IllegalStateException if the query is not applicable
     */
    default void assertApplicable(String query) {
      if (!isApplicable(query)) {
        throw new IllegalArgumentException(String.format("%s is not applicable for query '%s'",
            this.getClass().getSimpleName(), query));
      }
    }

  }

  /**
   * Generates a predicate which will match if either first name, last name, email, phone number or
   * city match the query. The query will be split into single words and each part will be matched
   * separately. This Creator is always applicable.
   *
   * @author Rico Bergmann
   */
  private class GenericPredicateCreator implements PredicateCreator {

    /*
     * (non-Javadoc)
     *
     * @see de.naju.adebar.web.validation.persons.PersonSearchPredicateCreator.PredicateCreator#
     * generatePredicate(java.lang.String)
     */
    @Override
    public BooleanBuilder generatePredicate(String query) {
      BooleanBuilder predicate = new BooleanBuilder();
      String[] args = query.split(" ");

      for (String arg : args) {
        predicate.or(person.firstName.containsIgnoreCase(arg));
        predicate.or(person.lastName.containsIgnoreCase(arg));
        predicate.or(person.email.isNotNull().and(person.email.value.containsIgnoreCase(arg)));
        predicate.or(
            person.phoneNumber.isNotNull().and(person.phoneNumber.value.containsIgnoreCase(arg)));
        predicate
            .or(person.address.city.isNotNull().and(person.address.city.equalsIgnoreCase(arg)));
      }

      return predicate;
    }

    /*
     * (non-Javadoc)
     *
     * @see de.naju.adebar.web.validation.persons.PersonSearchPredicateCreator.PredicateCreator#
     * isApplicable(java.lang.String)
     */
    @Override
    public boolean isApplicable(String query) {
      // the generic creator is always applicable
      return true;
    }

  }

  /**
   * Generates a query which will match on the person's complete name. Only applicable when the
   * query consists of at least two words with no non-alphabetic characters.
   *
   * @author Rico Bergmann
   */
  private class NamePredicateCreator implements PredicateCreator {

    /*
     * (non-Javadoc)
     *
     * @see de.naju.adebar.web.validation.persons.PersonSearchPredicateCreator.PredicateCreator#
     * generatePredicate(java.lang.String)
     */
    @Override
    public BooleanBuilder generatePredicate(String query) {
      assertApplicable(query);
      BooleanBuilder predicate = new BooleanBuilder();
      Pattern pattern = Pattern.compile(FIRST_NAME_LAST_NAME_REGEX);
      Matcher matcher = pattern.matcher(query);

      String firstName = matcher.group("firstName");
      String lastName = matcher.group("lastName");
      predicate.and(person.firstName.containsIgnoreCase(firstName));
      predicate.and(person.lastName.containsIgnoreCase(lastName));
      return predicate;
    }

    /*
     * (non-Javadoc)
     *
     * @see de.naju.adebar.web.validation.persons.PersonSearchPredicateCreator.PredicateCreator#
     * isApplicable(java.lang.String)
     */
    @Override
    public boolean isApplicable(String query) {
      return query.matches(FIRST_NAME_LAST_NAME_REGEX);
    }

  }

  /**
   * Generates a query which will match on the person's first name or last name.
   *
   * @author Rico Bergmann
   */
  private class FirstNameOrLastNamePredicateCreator implements PredicateCreator {

    /*
     * (non-Javadoc)
     *
     * @see de.naju.adebar.web.validation.persons.PersonSearchPredicateCreator.PredicateCreator#
     * generatePredicate(java.lang.String)
     */
    @Override
    public BooleanBuilder generatePredicate(String query) {
      assertApplicable(query);
      BooleanBuilder predicate = new BooleanBuilder();
      predicate.and( //
          person.firstName.containsIgnoreCase(query) //
              .or(person.lastName.containsIgnoreCase(query)));
      return predicate;
    }

    /*
     * (non-Javadoc)
     *
     * @see de.naju.adebar.web.validation.persons.PersonSearchPredicateCreator.PredicateCreator#
     * isApplicable(java.lang.String)
     */
    @Override
    public boolean isApplicable(String query) {
      return query.matches(NAME_REGEX);
    }

  }

  /**
   * Generates a query which will match on the person's email. Only applicable if the query is an
   * email address.
   *
   * @author Rico Bergmann
   */
  private class EmailPredicateCreator implements PredicateCreator {

    /*
     * (non-Javadoc)
     *
     * @see de.naju.adebar.web.validation.persons.PersonSearchPredicateCreator.PredicateCreator#
     * generatePredicate(java.lang.String)
     */
    @Override
    public BooleanBuilder generatePredicate(String query) {
      assertApplicable(query);
      BooleanBuilder predicate = new BooleanBuilder();
      predicate.and(person.email.isNotNull().and(person.email.eq(Email.of(query))));
      return predicate;
    }

    /*
     * (non-Javadoc)
     *
     * @see de.naju.adebar.web.validation.persons.PersonSearchPredicateCreator.PredicateCreator#
     * isApplicable(java.lang.String)
     */
    @Override
    public boolean isApplicable(String query) {
      return Validation.isEmail(query);
    }

  }

  /**
   * Generates a query which will match on the person's phone number. Only applicable if the query
   * is a phone number.
   *
   * @author Rico Bergmann
   */
  private class PhoneNumberPredicateCreator implements PredicateCreator {

    /*
     * (non-Javadoc)
     *
     * @see de.naju.adebar.web.validation.persons.PersonSearchPredicateCreator.PredicateCreator#
     * generatePredicate(java.lang.String)
     */
    @Override
    public BooleanBuilder generatePredicate(String query) {
      assertApplicable(query);
      BooleanBuilder predicate = new BooleanBuilder();
      predicate
          .and(person.phoneNumber.isNotNull().and(person.phoneNumber.eq(PhoneNumber.of(query))));
      return predicate;
    }

    /*
     * (non-Javadoc)
     *
     * @see de.naju.adebar.web.validation.persons.PersonSearchPredicateCreator.PredicateCreator#
     * isApplicable(java.lang.String)
     */
    @Override
    public boolean isApplicable(String query) {
      return Validation.isPhoneNumber(query);
    }

  }

}
