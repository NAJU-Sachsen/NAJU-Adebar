package de.naju.adebar.app.search.persons.interpreters;

import com.querydsl.core.BooleanBuilder;
import de.naju.adebar.app.search.persons.PersonQueryInterpreter;
import de.naju.adebar.model.core.PhoneNumber;
import de.naju.adebar.model.persons.QPerson;
import de.naju.adebar.util.Validation;
import java.util.Optional;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Interpreter for phone numbers. Only matches on queries which are phone number. The {@link
 * GenericPersonQueryInterpreter} will be used as fallback.
 *
 * @author Rico Bergmann
 */
@Service
public class PhoneNumberQueryInterpreter implements PersonQueryInterpreter {

  private static final QPerson person = QPerson.person;

  private final GenericPersonQueryInterpreter genericInterpreter;

  public PhoneNumberQueryInterpreter(GenericPersonQueryInterpreter genericInterpreter) {
    Assert.notNull(genericInterpreter, "genericInterpreter may not be null");
    this.genericInterpreter = genericInterpreter;
  }

  @Override
  public boolean mayInterpret(@NonNull String query) {
    return Validation.isPhoneNumber(query);
  }

  @Override
  public BooleanBuilder interpret(@NonNull String query) {
    assertMayInterpret(query);

    BooleanBuilder predicate = new BooleanBuilder();
    predicate.and(person.phoneNumber.isNotNull().and(person.phoneNumber.eq(PhoneNumber.of(query))));
    return predicate;
  }

  @Override
  public Optional<PersonQueryInterpreter> getFallback() {
    return Optional.of(genericInterpreter);
  }

}
