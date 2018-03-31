package de.naju.adebar.app.search.persons.interpreters;

import com.querydsl.core.BooleanBuilder;
import de.naju.adebar.app.search.persons.PersonQueryInterpreter;
import de.naju.adebar.model.persons.QPerson;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

/**
 * The most general {@link PersonQueryInterpreter} available. It will match every query and does not
 * provide a fallback. The {@code GenericPersonQueryInterpreter} will match if either the person's
 * first name, last name, phone number, email address or the city the person lives in match at least
 * part of the query. To achieve this, the query will be split into single words and compared word
 * by word.
 *
 * @author Rico Bergmann
 */
@Service
public class GenericPersonQueryInterpreter implements PersonQueryInterpreter {

  private static final QPerson person = QPerson.person;

  @Override
  public boolean mayInterpret(@NonNull String query) {
    // the generic interpreter is always applicable
    return true;
  }

  @Override
  public BooleanBuilder interpret(@NonNull String query) {
    assertMayInterpret(query);
    BooleanBuilder predicate = new BooleanBuilder();
    BooleanBuilder subPredicate = new BooleanBuilder();

    for (String queryPart : query.split(" ")) {
      subPredicate.or(person.firstName.containsIgnoreCase(queryPart)
          .or(person.lastName.containsIgnoreCase(queryPart))
          .or(person.email.isNotNull().and(person.email.value.eq(queryPart)))
          .or(person.phoneNumber.isNotNull().and(person.phoneNumber.value.eq(queryPart)))
          .or(person.address.isNotNull().and(person.address.city.eq(queryPart))));
    }

    predicate.and(subPredicate);
    return predicate;
  }

}
