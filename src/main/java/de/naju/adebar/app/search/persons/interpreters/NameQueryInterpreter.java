package de.naju.adebar.app.search.persons.interpreters;

import de.naju.adebar.app.search.persons.PersonQueryInterpreter;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Interpreter for complete names
 * <p>
 * The interpreter will produce a predicate which matches if the first name and the last name match
 * those specified in the query. Therefore the interpreter will try to infer what first name and
 * last name are. This will fail if the query consists of a composed first name and a single last
 * name, such as "Jeremy Pascal Glockenstein". In this case the fallbacks have to be used.
 * <p>
 * The interpreter supports special german characters (umlauts ä, ö, ü and ß), last names with
 * special name affixes like "von Schnabelstädt" or "zu Glockenstein" and composed last names.
 *
 * @author Rico Bergmann
 * @see ShiftedNameQueryInterpreter
 */
@Service
public class NameQueryInterpreter extends AbstractNameQueryInterpreter {

  private static final String NAME_REGEX =
      "^(?<firstName>[a-zA-ZöÖüÜäÄß]+) (?<lastName>(von |zu )?([a-zA-ZöÖüÜäÄß]+(\\s*(-\\s*)?[a-zA-ZöÖüÜäÄß]+)?)+)$";

  private final ShiftedNameQueryInterpreter shiftedInterpreter;

  /**
   * Full constructor
   *
   * @param shiftedInterpreter the fallback interpreter
   */
  public NameQueryInterpreter(ShiftedNameQueryInterpreter shiftedInterpreter) {
    Assert.notNull(shiftedInterpreter, "shiftedInterpreter may not be null");
    this.shiftedInterpreter = shiftedInterpreter;
  }

  @Override
  public Optional<PersonQueryInterpreter> getFallback() {
    return Optional.of(shiftedInterpreter);
  }

  @Override
  protected String getNameRegex() {
    return NAME_REGEX;
  }

}
