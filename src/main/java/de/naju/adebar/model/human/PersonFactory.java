package de.naju.adebar.model.human;

import java.util.Iterator;
import org.springframework.stereotype.Service;

/**
 * Factory to create {@link Person} instances. Using the Builder pattern.
 *
 * @author Rico Bergmann
 * @see <a href="https://en.wikipedia.org/wiki/Builder_pattern">Builder pattern</a>
 */
@Service
public class PersonFactory {

  private Iterator<PersonId> idGenerator;

  // constructors

  /**
   * Constructor supplying a custom ID generator
   *
   * @param idGenerator the generator to use
   */
  public PersonFactory(Iterator<PersonId> idGenerator) {
    this.idGenerator = idGenerator;
  }

  /**
   * Default constructor
   */
  public PersonFactory() {
    idGenerator = new PersonId();
  }

  // normal methods

  /**
   * Starts the process of building a new {@link Person} instance
   *
   * @param firstName the person's first name
   * @param lastName the person's last name
   * @param email the person's email
   * @return the builder object to continue the creation
   */
  public PersonBuilder buildNew(String firstName, String lastName, String email) {
    return new PersonBuilder(firstName, lastName, email);
  }

  // inner classes

  /**
   * The builder implementation that takes care of the construction of a new {@link Person}
   * instance.
   */
  public class PersonBuilder {

    private Person person;

    /**
     * Full constructor
     *
     * @param firstName the person's first name
     * @param lastName the person's last name
     * @param email the person's email
     */
    public PersonBuilder(String firstName, String lastName, String email) {
      PersonId id = idGenerator.next();
      person = new Person(id, firstName, lastName, email);
    }

    /**
     * Turns the person under construction into a participant
     *
     * @return the builder to use for further calls
     */
    public PersonBuilder makeParticipant() {
      person.makeParticipant();
      return this;
    }

    /**
     * Turns the person under construction into an activist
     *
     * @return the builder to use for further calls
     */
    public PersonBuilder makeActivist() {
      person.makeActivist();
      return this;
    }

    /**
     * Turns the person under construction into a referent
     *
     * @return the builder to use for further calls
     */
    public PersonBuilder makeReferent() {
      person.makeReferent();
      return this;
    }

    /**
     * Transacts the creation
     *
     * @return the new {@link Person} instance
     */
    public Person create() {
      return person;
    }
  }
}
