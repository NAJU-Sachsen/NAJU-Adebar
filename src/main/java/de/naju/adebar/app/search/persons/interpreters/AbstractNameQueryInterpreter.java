package de.naju.adebar.app.search.persons.interpreters;

import com.querydsl.core.BooleanBuilder;
import de.naju.adebar.app.search.persons.PersonQueryInterpreter;
import de.naju.adebar.model.persons.QPerson;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

/**
 * Base class for the {@link NameQueryInterpreter} and the {@link ShiftedNameQueryInterpreter}. Both
 * interpreters share the same logic and just differ in the regular expression they use. Therefore
 * this class provides the functionality whereas the implementations provide their fallbacks and
 * respective regex.
 *
 * @author Rico Bergmann
 */
abstract class AbstractNameQueryInterpreter implements PersonQueryInterpreter {

  protected static final Logger log = LoggerFactory.getLogger(NameQueryInterpreter.class);
  protected static final QPerson person = QPerson.person;

  protected abstract String getNameRegex();

  @Override
  public boolean mayInterpret(@NonNull String query) {
    return query.matches(getNameRegex());
  }

  @Override
  public BooleanBuilder interpret(@NonNull String query) {
    Pattern namePatter = Pattern.compile(getNameRegex());
    Matcher queryMatcher = namePatter.matcher(query);

    if (!queryMatcher.matches()) {
      throw new IllegalArgumentException("Not a name query: " + query);
    }

    String firstName = queryMatcher.group("firstName");
    String lastName = queryMatcher.group("lastName");

    log.debug("querying for first name '{}' and last name '{}'", firstName, lastName);

    return generatePredicateFor(firstName, lastName);
  }

  /**
   * Constructs a predicate for the given first name and last name
   *
   * @param firstName the first name
   * @param lastName the last name
   * @return the predicate
   */
  @NonNull
  private BooleanBuilder generatePredicateFor(@NonNull String firstName, @NonNull String lastName) {
    BooleanBuilder predicate = new BooleanBuilder();
    predicate.and(person.firstName.containsIgnoreCase(firstName));
    predicate.and(person.lastName.containsIgnoreCase(lastName));
    return predicate;
  }

}
