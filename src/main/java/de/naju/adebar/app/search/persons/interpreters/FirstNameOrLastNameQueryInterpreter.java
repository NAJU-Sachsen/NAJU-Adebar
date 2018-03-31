package de.naju.adebar.app.search.persons.interpreters;

import com.querydsl.core.BooleanBuilder;
import de.naju.adebar.app.search.persons.PersonQueryInterpreter;
import de.naju.adebar.model.persons.QPerson;
import java.util.Optional;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

/**
 * Interpreter for first name or last name - but not a complete name.
 * <p>
 * The interpreter will produce a predicate which matches if the first name or the last name of a
 * person match the query. It supports special german characters (umlauts ä, ö, ü and ß), composed
 * names such as "Jeremy Pascal" as well as names separated by a hyphen such as
 * "Glockenstein-Schnabelstädt".
 *
 * @author Rico Bergmann
 */
@Service
public class FirstNameOrLastNameQueryInterpreter implements PersonQueryInterpreter {

  private static final String FIRST_NAME_LAST_NAME_REGEX = "^([a-zA-ZöÖüÜäÄß]+(\\s*(-\\s*)?[a-zA-ZöÖüÜäÄß]+)?)+$";
  private static final QPerson person = QPerson.person;

  @Override
  public boolean mayInterpret(@NonNull String query) {
    return query.matches(FIRST_NAME_LAST_NAME_REGEX);
  }

  @Override
  public BooleanBuilder interpret(@NonNull String query) {
    assertMayInterpret(query);

    BooleanBuilder predicate = new BooleanBuilder();
    predicate.and(person.firstName.containsIgnoreCase(query) //
        .or(person.lastName.containsIgnoreCase(query)));
    return predicate;
  }

  @Override
  public Optional<PersonQueryInterpreter> getFallback() {
    return Optional.of(new GenericPersonQueryInterpreter());
  }

}
