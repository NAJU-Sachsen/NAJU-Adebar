package de.naju.adebar.app.search.persons;

import com.querydsl.core.BooleanBuilder;
import de.naju.adebar.app.search.QueryInterpreter;
import de.naju.adebar.app.search.QueryInterpreterHierarchy;
import de.naju.adebar.app.search.persons.interpreters.EmailQueryInterpreter;
import de.naju.adebar.app.search.persons.interpreters.FirstNameOrLastNameQueryInterpreter;
import de.naju.adebar.app.search.persons.interpreters.GenericPersonQueryInterpreter;
import de.naju.adebar.app.search.persons.interpreters.NameQueryInterpreter;
import de.naju.adebar.app.search.persons.interpreters.PhoneNumberQueryInterpreter;
import de.naju.adebar.util.Assert2;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

/**
 * The interpreter hierarchy of the {@link PersonQueryInterpreter PersonQueryInterpreters}.
 *
 * @author Rico Bergmann
 */
@Service
public class PersonQueryInterpreterHierarchy implements QueryInterpreterHierarchy<BooleanBuilder> {

  private static final int INTERPRETER_COUNT = 5;

  private final List<QueryInterpreter<BooleanBuilder>> interpreters;

  /**
   * The constructor will take care of inflating the hierarchy.
   *
   * @param genericInterpreter the generic interpreter
   * @param nameInterpreter the interpreter for full names
   * @param firstNameOrLastNameInterpreter the interpreter for first name or last name only
   * @param phoneInterpreter the interpreter for phone numbers
   * @param emailInterpreter the interpreter for email addresses
   */
  public PersonQueryInterpreterHierarchy(GenericPersonQueryInterpreter genericInterpreter,
      NameQueryInterpreter nameInterpreter,
      FirstNameOrLastNameQueryInterpreter firstNameOrLastNameInterpreter,
      PhoneNumberQueryInterpreter phoneInterpreter, EmailQueryInterpreter emailInterpreter) {
    Assert2.noNullArguments("No argument may be null", genericInterpreter, nameInterpreter,
        firstNameOrLastNameInterpreter, phoneInterpreter, emailInterpreter);

    this.interpreters = new ArrayList<>(INTERPRETER_COUNT);

    /*
     * The order in which the interpreters are added will be the order in which they should be
     * applied.
     */
    interpreters.add(phoneInterpreter);
    interpreters.add(emailInterpreter);

    /*
     * The name interpreter MUST be added before the first name/last name one as a complete name
     * is a "special case" of just a first name or last name.
     */
    interpreters.add(nameInterpreter);
    interpreters.add(firstNameOrLastNameInterpreter);

    /*
     * The generic interpreter MUST be the last one, as it will match every query
     */
    interpreters.add(genericInterpreter);
  }

  @Override
  public Optional<PersonQueryInterpreter> getInterpreterFor(@NonNull String query) {

    /*
     * Just search for the first best interpreter (which is based on the order the interpreters
     * where added to the list)
     */

    for (QueryInterpreter<BooleanBuilder> interpreter : interpreters) {
      if (interpreter.mayInterpret(query)) {
        return Optional.of((PersonQueryInterpreter) interpreter);
      }
    }
    return Optional.empty();
  }

  @Override
  @NonNull
  public Iterator<QueryInterpreter<BooleanBuilder>> iterator() {
    return interpreters.iterator();
  }

}
