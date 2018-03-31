package de.naju.adebar.app.search.persons.interpreters;

import de.naju.adebar.app.search.persons.PersonQueryInterpreter;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * The {@code StretchingNameQueryInterpreter} works if a name consists of a composed first name and
 * a composed last name. It will only use the first part of the first name and the last part of the
 * last name and should produce less results then the fallback {@link
 * GenericPersonQueryInterpreter}.
 *
 * @author Rico Bergmann
 */
@Service
public class StretchingNameQueryInterpreter extends AbstractNameQueryInterpreter {

  private static final String NAME_REGEX = "^(?<firstName>[a-zA-ZöÖüÜäÄß]+) (([a-zA-ZöÖüÜäÄß]+(\\s*-?\\s*[a-zA-ZöÖüÜäÄß])?)+\\s)*(?<lastName>[a-zA-ZöÖüÜäÄß]+)$";

  private final GenericPersonQueryInterpreter genericInterpreter;

  public StretchingNameQueryInterpreter(GenericPersonQueryInterpreter genericInterpreter) {
    Assert.notNull(genericInterpreter, "genericInterpreter may not be null");
    this.genericInterpreter = genericInterpreter;
  }

  @Override
  protected String getNameRegex() {
    return NAME_REGEX;
  }

  @Override
  public Optional<PersonQueryInterpreter> getFallback() {
    return Optional.of(genericInterpreter);
  }
}
