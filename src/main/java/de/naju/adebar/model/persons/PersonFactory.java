package de.naju.adebar.model.persons;

import de.naju.adebar.model.core.Address;
import de.naju.adebar.model.core.Email;
import de.naju.adebar.model.core.PhoneNumber;
import de.naju.adebar.model.persons.details.Gender;
import de.naju.adebar.model.persons.details.JuleicaCard;
import de.naju.adebar.model.persons.details.NabuMembershipInformation;
import de.naju.adebar.model.persons.qualifications.Qualification;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * Factory to create {@link Person} instances. The factory just sets up everything to delegate the
 * creation-process itself to a Builder.
 *
 * <p>
 * Beware that the implementation of the builder is actually not stateless: <br> When performing a
 * call such as
 *
 * <pre>
 * {@code
 *  newBuilder = builder.setAddress(anAddress);
 * }
 * </pre>
 *
 * {@code newBuilder == builder} will hold true afterwards. <br> The fact that a builder-instance is
 * returned after each call is just for a more convenient usage.
 * <p>
 * Also note that calling a {@code makeXYZ()} more than once may lead to undefined behavior whereas
 * calling {@code specifyXYZ()} will usually override the first call.
 *
 * @author Rico Bergmann
 * @see <a href="https://en.wikipedia.org/wiki/Builder_pattern">Builder pattern</a>
 */
@Service
public class PersonFactory {

  private Iterator<PersonId> idGenerator;

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

  /**
   * Starts the process of building a new {@link Person} instance
   *
   * @param firstName the person's first name
   * @param lastName the person's last name
   * @param email the person's email
   * @return the builder object to continue the creation
   */
  public PersonBuilder buildNew(String firstName, String lastName, Email email) {
    return new PersonBuilder(firstName, lastName, email);
  }

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
    public PersonBuilder(String firstName, String lastName, Email email) {
      PersonId id = idGenerator.next();
      person = new Person(id, firstName, lastName, email);
      person.setAddress(new Address());
    }

    /**
     * Sets the address of the person
     *
     * @param street the street
     * @param zip the zip
     * @param city the city
     * @return the builder to use for further calls
     */
    public PersonBuilder specifyAddress(String street, String zip, String city) {
      person.setAddress(Address.of(street, zip, city));
      return this;
    }

    /**
     * Sets the address of the person
     *
     * @param address the address
     * @return the builder to use for further calls
     */
    public PersonBuilder specifyAddress(Address address) {
      person.setAddress(address);
      return this;
    }

    /**
     * Sets the person's phone number
     *
     * @param phoneNumber the number
     * @return the builder to use for further calls
     */
    public PersonBuilder specifyPhoneNumber(PhoneNumber phoneNumber) {
      person.setPhoneNumber(phoneNumber);
      return this;
    }

    /**
     * Turns the person under construction into a participant
     *
     * @return the builder to use for further calls
     */
    public ParticipantBuilder makeParticipant() {
      person.makeParticipant();
      return new ParticipantBuilder(this);
    }

    /**
     * Turns the person under construction into an activist
     *
     * @return the builder to use for further calls
     */
    public ActivistBuilder makeActivist() {
      person.makeActivist();
      return new ActivistBuilder(this);
    }

    /**
     * Turns the person under construction into a referent
     *
     * @return the builder to use for further calls
     */
    public ReferentBuilder makeReferent() {
      person.makeReferent();
      return new ReferentBuilder(this);
    }

    /**
     * Makes the person parent of somebody
     *
     * @return the builder to use for further calls
     */
    public ParentBuilder makeParent() {
      person.makeParent();
      return new ParentBuilder(this);
    }

    /**
     * Transacts the creation
     *
     * @return the new {@link Person} instance
     */
    public Person create() {
      return person;
    }

    /**
     * @return the person as far as it has been already constructed
     */
    protected Person currentBuild() {
      return person;
    }
  }

  /**
   * Base class for more specific builders which will take care of certain aspects of the person.
   * Through it's fluent interface the construction may return to the actual {@link PersonBuilder}
   * at any time.
   *
   * @see <a href="https://en.wikipedia.org/wiki/Fluent_interface">About Fluent interfaces</a>
   */
  public abstract class AbstractProfileBuilder {

    protected PersonBuilder parentBuilder;

    /**
     * @param parentBuilder the builder which started the person's creation. This one is
     *     especially important to get back to the caller.
     * @throws IllegalArgumentException if the {@code parentBuilder} is null
     */
    protected AbstractProfileBuilder(PersonBuilder parentBuilder) {
      Assert.notNull(parentBuilder, "Parent may not be null");
      this.parentBuilder = parentBuilder;
    }

    /**
     * Marks the profile creation as completed and switches back to the original builder
     *
     * @return the original builder
     */
    public PersonBuilder done() {
      return parentBuilder;
    }

    /**
     * Marks the profile creation as done and terminates the whole creation right away.
     *
     * @return the person
     *
     * @see PersonBuilder#create()
     */
    public Person create() {
      return done().create();
    }
  }

  /**
   * Builder for the person's {@link ParticipantProfile}
   */
  public class ParticipantBuilder extends AbstractProfileBuilder {

    /**
     * @param parentBuilder the delegating builder
     */
    private ParticipantBuilder(PersonBuilder parentBuilder) {
      super(parentBuilder);
    }

    /**
     * Sets the gender of the person
     *
     * @param gender the gender
     * @return the builder to use for further calls
     */
    public ParticipantBuilder specifyGender(Gender gender) {
      parentBuilder.currentBuild().getParticipantProfile().setGender(gender);
      return this;
    }

    /**
     * Sets the person's date of birth
     *
     * @param dateOfBirth the date of birth
     * @return the builder to use for further calls
     */
    public ParticipantBuilder specifyDateOfBirth(LocalDate dateOfBirth) {
      parentBuilder.currentBuild().getParticipantProfile().setDateOfBirth(dateOfBirth);
      return this;
    }

    /**
     * Sets the person's eating habits
     *
     * @param eatingHabits the eating habits
     * @return the builder to use for further calls
     */
    public ParticipantBuilder specifyEatingHabits(String eatingHabits) {
      parentBuilder.currentBuild().getParticipantProfile().setEatingHabits(eatingHabits);
      return this;
    }

    /**
     * Sets the person's health impairments
     *
     * @param healthImpairments the health impairments
     * @return the builder to use for further calls
     */
    public ParticipantBuilder specifyHealthImpairments(String healthImpairments) {
      parentBuilder.currentBuild().getParticipantProfile().setHealthImpairments(healthImpairments);
      return this;
    }

    /**
     * Sets the person's NABU membership information
     *
     * @param nabuMembership the information
     * @return the builder to use for further calls
     */
    public ParticipantBuilder specifyNabuMembership(NabuMembershipInformation nabuMembership) {
      parentBuilder.currentBuild().getParticipantProfile().setNabuMembership(nabuMembership);
      return this;
    }

    /**
     * Adds special remarks to the profile
     *
     * @param remarks the profile
     * @return the builder to use for further calls
     */
    public ParticipantBuilder addRemarks(String remarks) {
      parentBuilder.currentBuild().getParticipantProfile().setRemarks(remarks);
      return this;
    }

  }

  /**
   * Builder for the person's {@link ActivistProfile}
   */
  public class ActivistBuilder extends AbstractProfileBuilder {

    /**
     * @param parentBuilder the delegating builder
     */
    private ActivistBuilder(PersonBuilder parentBuilder) {
      super(parentBuilder);
    }

    /**
     * Sets the person's Juleica card
     *
     * @param juleica the card
     * @return the builder to use for further calls
     */
    public ActivistBuilder specifyJuleicaCard(JuleicaCard juleica) {
      parentBuilder.currentBuild().getActivistProfile().setJuleicaCard(juleica);
      return this;
    }

  }

  /**
   * Builder for the person's {@link ReferentProfile}
   */
  public class ReferentBuilder extends AbstractProfileBuilder {

    /**
     * @param parentBuilder the delegating builder
     */
    private ReferentBuilder(PersonBuilder parentBuilder) {
      super(parentBuilder);
    }

    /**
     * Sets the person's qualifications
     *
     * @param qualifications the qualifications
     * @return the builder to use for further calls
     */
    public ReferentBuilder specifyQualifications(List<Qualification> qualifications) {
      parentBuilder.currentBuild().getReferentProfile().setQualifications(qualifications);
      return this;
    }

  }

  /**
   * Builder for the person's {@link ParentProfile}
   */
  public class ParentBuilder extends AbstractProfileBuilder {

    /**
     * @param delegatingBuilder the delegating builder
     */
    private ParentBuilder(PersonBuilder delegatingBuilder) {
      super(delegatingBuilder);
    }

    /**
     * Sets the parent's phone number at work
     *
     * @param workPhone the phone number
     * @return the builder to use for further calls
     */
    public ParentBuilder specifyWorkPhone(PhoneNumber workPhone) {
      parentBuilder.currentBuild().getParentProfile().setWorkPhone(workPhone);
      return this;
    }

    /**
     * Sets the parent's phone number at home
     *
     * @param landlinePhone the phone number
     * @return the builder to use for further calls
     */
    public ParentBuilder specifyLandlinePhone(PhoneNumber landlinePhone) {
      parentBuilder.currentBuild().getParentProfile().setLandlinePhone(landlinePhone);
      return this;
    }

  }
}
