package de.naju.adebar.app.search.persons.interpreters;

import com.querydsl.core.BooleanBuilder;
import de.naju.adebar.app.search.persons.PersonQueryInterpreter;
import de.naju.adebar.model.core.Email;
import de.naju.adebar.model.persons.QPerson;
import de.naju.adebar.util.Validation;
import java.util.Optional;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Interpreter for email addresses. Only matches on queries which are an email address. The {@link
 * GenericPersonQueryInterpreter} will be used as fallback.
 *
 * @author Rico Bergmann
 */
@Service
public class EmailQueryInterpreter implements PersonQueryInterpreter {

  private static final QPerson person = QPerson.person;

  private final GenericPersonQueryInterpreter genericQueryInterpreter;

  /**
   * Full constructor
   *
   * @param genericQueryInterpreter the generic interpreter to use
   */
  public EmailQueryInterpreter(GenericPersonQueryInterpreter genericQueryInterpreter) {
    Assert.notNull(genericQueryInterpreter, "genericQueryInterpreter may not be null");
    this.genericQueryInterpreter = genericQueryInterpreter;
  }

  @Override
  public boolean mayInterpret(@NonNull String query) {
    return Validation.isEmail(query);
  }

  @Override
  public BooleanBuilder interpret(@NonNull String query) {
    assertMayInterpret(query);

    BooleanBuilder predicate = new BooleanBuilder();

    // better save than sorry - check if the person actually has an email before comparing it
    predicate.and(person.email.isNotNull().and(person.email.eq(Email.of(query))));
    return predicate;
  }

  @Override
  public Optional<PersonQueryInterpreter> getFallback() {
    return Optional.of(genericQueryInterpreter);
  }

}
