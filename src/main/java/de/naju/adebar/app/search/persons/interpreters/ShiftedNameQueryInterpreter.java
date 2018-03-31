package de.naju.adebar.app.search.persons.interpreters;

import de.naju.adebar.app.search.persons.PersonQueryInterpreter;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * The {@code ShiftedNameQueryInterpreter} works just like the normal {@link NameQueryInterpreter}
 * but supports composed first names with a single last name instead.
 * <p>
 * The only case not covered by both interpreters is a composed first name with a composed last
 * name. As this case will be extremely rare, the fallback {@link StretchingNameQueryInterpreter}
 * has to be used for this.
 *
 * @author Rico Bergmann
 */
@Service
public class ShiftedNameQueryInterpreter extends AbstractNameQueryInterpreter {

  private static final String NAME_REGEX =
      "^(?<firstName>([a-zA-ZöÖüÜäÄß]+(\\s*-?\\s*[a-zA-ZöÖüÜäÄß])?)+) (?<lastName>(von |zu )?([a-zA-ZöÖüÜäÄß]+((\\s?-\\s?)?[a-zA-ZöÖüÜäÄß]+)?)+)$";

  private final StretchingNameQueryInterpreter stretchingInterpreter;

  public ShiftedNameQueryInterpreter(StretchingNameQueryInterpreter stretchingInterpreter) {
    Assert.notNull(stretchingInterpreter, "stretchingInterpreter may not be null");
    this.stretchingInterpreter = stretchingInterpreter;
  }

  @Override
  public Optional<PersonQueryInterpreter> getFallback() {
    return Optional.of(stretchingInterpreter);
  }

  @Override
  protected String getNameRegex() {
    return NAME_REGEX;
  }

}
