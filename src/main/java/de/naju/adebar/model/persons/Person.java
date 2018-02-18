package de.naju.adebar.model.persons;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Transient;
import org.hibernate.validator.constraints.Email;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;
import org.springframework.util.Assert;
import de.naju.adebar.model.Address;
import de.naju.adebar.model.PhoneNumber;
import de.naju.adebar.model.events.Event;
import de.naju.adebar.util.Validation;

/**
 * Abstraction of a person. No matter of its concrete role (camp participant, activist, ...) some
 * data always needs to be tracked. This will be handled here.
 *
 * In terms of DDD a Person acts as an aggregate-root for a number of profiles.
 *
 * @author Rico Bergmann
 * @see ParticipantProfile
 * @see ActivistProfile
 * @see ReferentProfile
 */
@Entity(name = "person")
public class Person {

  private static final String FOR_PERSON_MSG = "For person ";
  private static final int MAX_PARENT_PROFILES = 2;

  @Transient
  private final Collection<AbstractPersonRelatedEvent> domainEvents = new ArrayList<>();

  @EmbeddedId
  @Column(name = "id")
  private PersonId id;

  @Column(name = "firstName")
  private String firstName;

  @Column(name = "lastName")
  private String lastName;

  @Column(name = "email")
  @Email
  private String email;

  @Embedded
  private PhoneNumber phoneNumber;

  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "street", column = @Column(name = "addressStreet")),
      @AttributeOverride(name = "zip", column = @Column(name = "addressZip")),
      @AttributeOverride(name = "city", column = @Column(name = "addressCity")),
      @AttributeOverride(name = "additionalInfo", column = @Column(name = "addressHints"))})
  private Address address;

  @Column(name = "participant")
  private boolean participant;

  @Column(name = "activist")
  private boolean activist;

  @Column(name = "referent")
  private boolean referent;

  @PrimaryKeyJoinColumn
  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private ParticipantProfile participantProfile;

  @PrimaryKeyJoinColumn
  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private ActivistProfile activistProfile;

  @PrimaryKeyJoinColumn
  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private ReferentProfile referentProfile;

  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinTable(name = "parents", joinColumns = @JoinColumn(name = "child"),
      inverseJoinColumns = @JoinColumn(name = "parent"))
  private List<Person> parentProfiles;

  @Column(name = "archived")
  private boolean archived;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "eventParticipants", //
      joinColumns = @JoinColumn(name = "participant"), //
      inverseJoinColumns = @JoinColumn(name = "eventId"))
  private List<Event> participatingEvents;

  /**
   * Full constructor to create new Person instances. However to create a new object from outside
   * the package, the {@link PersonFactory} should be used.
   *
   * @param id the person's unique ID. Used as PK within the database
   * @param firstName the person's first name. May not be empty.
   * @param lastName the person's last name. May not be empty.
   * @param email the person's email address. Must be a valid email address or {@code null}
   * @throws IllegalArgumentException if any of the parameters constraints are violated.
   * @see PersonFactory
   */
  Person(PersonId id, String firstName, String lastName, String email) {
    Object[] params = {id, firstName, lastName};
    Assert.noNullElements(params, "No argument may be null: " + Arrays.toString(params));
    Assert.hasText(firstName, "First name may not be null nor empty, but was: " + firstName);
    Assert.hasText(lastName, "Last name may not be null nor empty, but was: " + lastName);

    if (email != null && !email.isEmpty()) {
      Assert.isTrue(Validation.isEmail(email), "Not a valid email address: " + email);
    }

    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;

    this.parentProfiles = new ArrayList<>(MAX_PARENT_PROFILES);

    this.archived = false;
  }

  /**
   * Default constructor for JPA's sake
   */
  Person() {}

  /**
   * @return the person's unique ID.
   */
  public PersonId getId() {
    return id;
  }

  /**
   * @param id the person's unique ID
   */
  @SuppressWarnings("unused")
  private void setId(PersonId id) {
    this.id = id;
  }

  /**
   * @return the person's first name
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * @param firstName the person's first name
   * @throws IllegalArgumentException if the new name is empty or {@code null}
   */
  protected void setFirstName(String firstName) {
    Assert.hasText(firstName, "First name may not be null nor empty, but was: " + firstName);
    this.firstName = firstName;
  }

  /**
   * @return the person's last name
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * @param lastName the person's last name
   * @throws IllegalArgumentException if the new name is empty or {@code null}
   */
  protected void setLastName(String lastName) {
    Assert.hasText(lastName, "Last name may not be null nor empty, but was: " + lastName);
    this.lastName = lastName;
  }

  /**
   * @return the person's email address
   */
  public String getEmail() {
    return email;
  }

  /**
   * @param email the person's email address, may be {@code null @throws IllegalArgumentException if
   * the email is not valid, i.e. does not match the email regex (Existence of the address is not
   * checked)
   */
  protected void setEmail(String email) {
    if (email != null) {
      Assert.isTrue(Validation.isEmail(email), "Not a valid email address: " + email);
    }
    this.email = email;
  }

  /**
   * @return the person's phone number. May be {@code null}.
   */
  public PhoneNumber getPhoneNumber() {
    return phoneNumber;
  }

  /**
   * @param phoneNumber the person's phone number. May be {@code null}.
   */
  protected void setPhoneNumber(PhoneNumber phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  /**
   * @return the person's address. May be {@code null}.
   */
  public Address getAddress() {
    return address;
  }

  /**
   * @param address the person's address. May be {@code null}.
   */
  protected void setAddress(Address address) {
    this.address = address;
  }

  /**
   * @return {@code true} if the person is a camp participant, or {@code false} otherwise
   */
  public boolean isParticipant() {
    return participant;
  }

  /**
   * Setter just for JPA's sake. Private to enforce consistency with the state of the
   * {@link #participantProfile}
   *
   * @param participant whether the person is a participant
   */
  @SuppressWarnings("unused")
  private void setParticipant(boolean participant) {
    this.participant = participant;
  }

  /**
   * @return the participant profile of the person. If it is not a camp participant, this will
   *         return {@code null}.
   */
  public ParticipantProfile getParticipantProfile() {
    return participantProfile;
  }

  /**
   * @param participantProfile the participant profile of the person. May be {@code null} if the
   *        person is not a camp participant.
   */
  protected void setParticipantProfile(ParticipantProfile participantProfile) {
    this.participant = participantProfile != null;
    this.participantProfile = participantProfile;
    participantProfile.provideRelatedPerson(this);
  }

  /**
   * @return {@code true} if the person is an activist, or {@code false} otherwise
   */
  public boolean isActivist() {
    return activist;
  }

  /**
   * Setter just for JPA's sake. Private to enforce consistency with the state of the
   * {@link #activistProfile}
   *
   * @param participant whether the person is a participant
   */
  @SuppressWarnings("unused")
  private void setActivist(boolean activist) {
    this.activist = activist;
  }

  /**
   * @return activist-related information about the person. If it is not an activist, this will
   *         return {@code null}.
   */
  public ActivistProfile getActivistProfile() {
    return activistProfile;
  }

  /**
   * @param activistProfile the activist profile of the person. May be {@code null} if the person is
   *        not an activist.
   */
  protected void setActivistProfile(ActivistProfile activistProfile) {
    this.activist = activistProfile != null;
    this.activistProfile = activistProfile;
    activistProfile.provideRelatedPerson(this);
  }

  /**
   * @return {@code true} if the person is a referent, or {@code false} otherwise
   */
  public boolean isReferent() {
    return referent;
  }

  /**
   * Setter just for JPA's sake. Private to enforce consistency with the state of the
   * {@link #referentProfile}
   *
   * @param participant whether the person is a participant
   */
  @SuppressWarnings("unused")
  private void setReferent(boolean referent) {
    this.referent = referent;
  }

  /**
   * @return referent-related information about the person. If it is not a referent, this will
   *         return {@code null}.
   */
  public ReferentProfile getReferentProfile() {
    return referentProfile;
  }

  /**
   * @param referentProfile the referent profile of the person. May be {@code null} if the person is
   *        not a referent.
   */
  protected void setReferentProfile(ReferentProfile referentProfile) {
    this.referent = referentProfile != null;
    this.referentProfile = referentProfile;
    referentProfile.provideRelatedPerson(this);
  }

  /**
   * @return the person's parents
   */
  public Iterable<Person> getParentProfiles() {
    return parentProfiles;
  }

  /**
   * @param parentProfiles the person's parents
   */
  protected void setParentProfiles(List<Person> parentProfiles) {
    if (parentProfiles == null) {
      this.parentProfiles = new ArrayList<>(MAX_PARENT_PROFILES);
    } else {
      this.parentProfiles = parentProfiles;
    }
  }

  /**
   * @return whether the person is still to be used
   */
  public boolean isArchived() {
    return archived;
  }

  /**
   * @param archived whether the person is still to be used
   */
  @SuppressWarnings("unused")
  private void setArchived(boolean archived) {
    this.archived = archived;
  }

  /**
   * @return all the events this person attended as participant
   */
  public Collection<Event> getParticipatingEvents() {
    return Collections.unmodifiableList(participatingEvents);
  }

  /**
   * @param participatingEvents all the events this person attended as participant
   */
  @SuppressWarnings("unused")
  private void setParticipatingEvents(List<Event> participatingEvents) {
    Assert.notNull(participatingEvents, "Events may not be null");
    this.participatingEvents = participatingEvents;
  }

  /**
   * @return the person's name, which basically is {@code firstName + " " + lastName}
   */
  @Transient
  public String getName() {
    return firstName + " " + lastName;
  }

  /**
   * @return {@code true} if an email address is set, {@code false} otherwise
   */
  public boolean hasEmail() {
    return email != null;
  }

  /**
   * @return whether the phone number is set
   */
  public boolean hasPhoneNumber() {
    return phoneNumber != null;
  }

  /**
   * @return whether the address is set
   */
  public boolean hasAddress() {
    return address != null;
  }

  /**
   * @return {@code true} if a parent is registered for this person, {@code false} otherwise
   */
  public boolean hasParents() {
    return !parentProfiles.isEmpty();
  }

  /**
   * @return {@code true} if another parent profile may be connected to this person, {@code false}
   *         otherwise
   */
  public boolean parentProfileMayBeConnected() {
    return parentProfiles.size() < MAX_PARENT_PROFILES;
  }

  /**
   * Updates the personal information.
   * <p>
   * Once a person instance has been created, it is immutable. Therefore a copy is being created and
   * returned.
   *
   * @param firstName the new first name
   * @param lastName the new last name
   * @param email the new email
   * @param phoneNumber the new phone number
   * @return the updated person
   */
  public Person updateInformation(String firstName, String lastName, String email,
      PhoneNumber phoneNumber) {
    setFirstName(firstName);
    setLastName(lastName);
    setEmail(email);
    setPhoneNumber(phoneNumber);

    if (!updateEventWasRegistered()) {
      registerEvent(PersonDataUpdatedEvent.forPerson(this));
    }
    return this;
  }

  /**
   * Updates the personal information.
   * <p>
   * Once a person instance has been created, it is immutable. Therefore a copy is being created and
   * returned.
   *
   * @param firstName the new first name
   * @return the updated person
   */
  public Person updateFirstName(String firstName) {
    setFirstName(firstName);

    if (!updateEventWasRegistered()) {
      registerEvent(PersonDataUpdatedEvent.forPerson(this));
    }
    return this;
  }

  /**
   * Updates the personal information.
   * <p>
   * Once a person instance has been created, it is immutable. Therefore a copy is being created and
   * returned.
   *
   * @param lastName the new last name
   * @return the updated person
   */
  public Person updateLastName(String lastName) {
    setLastName(lastName);

    if (!updateEventWasRegistered()) {
      registerEvent(PersonDataUpdatedEvent.forPerson(this));
    }
    return this;
  }

  /**
   * Updates the personal information.
   * <p>
   * Once a person instance has been created, it is immutable. Therefore a copy is being created and
   * returned.
   *
   * @param email the new email address
   * @return the updated person
   */
  public Person updateEmail(String email) {
    setEmail(email);

    if (!updateEventWasRegistered()) {
      registerEvent(PersonDataUpdatedEvent.forPerson(this));
    }
    return this;
  }

  /**
   * Updates the personal information.
   * <p>
   * Once a person instance has been created, it is immutable. Therefore a copy is being created and
   * returned.
   *
   * @param phoneNumber the new phone number
   * @return the updated person
   */
  public Person updatePhoneNumber(PhoneNumber phoneNumber) {
    setPhoneNumber(phoneNumber);

    if (!updateEventWasRegistered()) {
      registerEvent(PersonDataUpdatedEvent.forPerson(this));
    }
    return this;
  }

  /**
   * Updates the personal information.
   * <p>
   * Once a person instance has been created, it is immutable. Therefore a copy is being created and
   * returned.
   *
   * @param address the new address
   * @return the updated person
   */
  public Person updateAddress(Address address) {
    setAddress(address);

    if (!updateEventWasRegistered()) {
      registerEvent(PersonDataUpdatedEvent.forPerson(this));
    }
    return this;
  }

  /**
   * Turns the person into a camp participant.
   * <p>
   * Be sure to save the person in order to persist the profile.
   *
   * @return the person's new participant profile
   * @throws IllegalStateException if the person already is a camp participant
   */
  public ParticipantProfile makeParticipant() {
    if (isParticipant()) {
      throw new IllegalStateException("Person already is a participant");
    }
    this.participantProfile = new ParticipantProfile(this);
    this.participant = true;
    return participantProfile;
  }

  /**
   * Turns the person into a camp participant, initializing its profile right away.
   * <p>
   * Be sure to save the person in order to persist the profile.
   *
   * @param gender the person's gender
   * @param dateOfBirth the person's date of birth
   * @param eatingHabits the person's eating habits
   * @param healthImpairments the person's health impairments
   * @return the person's new participant profile
   * @throws IllegalStateException if the person already is a camp participant
   */
  public ParticipantProfile makeParticipant(Gender gender, LocalDate dateOfBirth,
      String eatingHabits, String healthImpairments) {
    if (isParticipant()) {
      throw new IllegalStateException("Person already is a participant");
    }
    this.participantProfile =
        new ParticipantProfile(this, gender, dateOfBirth, eatingHabits, healthImpairments);
    this.participant = true;
    return participantProfile;
  }

  /**
   * Turns the person into an activist.
   * <p>
   * Be sure to save the person in order to persist the profile.
   *
   * @return the new activist profile, containing all activist related data
   * @throws IllegalStateException if the person already is an activist
   */
  public ActivistProfile makeActivist() {
    if (isActivist()) {
      throw new IllegalStateException("Person already is an activist");
    }
    this.activistProfile = new ActivistProfile(this);
    this.activist = true;

    registerEvent(NewActivistRegisteredEvent.forPerson(this));
    return activistProfile;
  }

  /**
   * Turns the person into an activist, initializing its profile right away.
   * <p>
   * Be sure to save the person in order to persist the profile.
   *
   * @param juleica the person's juleica, may be {@code null} if there is none
   * @return the new activist profile
   * @throws IllegalStateException if the person already is an activist
   */
  public ActivistProfile makeActivist(JuleicaCard juleica) {
    if (isActivist()) {
      throw new IllegalStateException("Person already is an activist");
    }
    this.activistProfile = new ActivistProfile(this, juleica);
    this.activist = true;

    registerEvent(NewActivistRegisteredEvent.forPerson(this));
    return activistProfile;
  }

  /**
   * Turns the person into a referent.
   * <p>
   * Be sure to save the person in order to persist the profile.
   *
   * @return the new referent profile, containing all referent related data.
   * @throws IllegalStateException if the person already is a referent
   */
  public ReferentProfile makeReferent() {
    if (isReferent()) {
      throw new IllegalStateException("Person already is a referent");
    }
    this.referentProfile = new ReferentProfile(this);
    this.referent = true;

    registerEvent(NewReferentRegisteredEvent.forPerson(this));
    return referentProfile;
  }

  /**
   * Turns the person into a referent.
   * <p>
   * Be sure to save the person in order to persist the profile.
   *
   * @param qualifications the person's qualifications
   * @return the new referent profile
   * @throws IllegalStateException if the person already is a referent
   */
  public ReferentProfile makeReferent(Collection<Qualification> qualifications) {
    if (isReferent()) {
      throw new IllegalStateException("Person already is a referent");
    }
    this.referentProfile = new ReferentProfile(this, qualifications);
    this.referent = true;

    registerEvent(NewReferentRegisteredEvent.forPerson(this));
    return referentProfile;
  }

  /**
   * Marks the person as "archived" - it should not be modified any further. However this is not
   * being enforced.
   *
   * @return the archived person
   */
  public Person archive() {
    if (archived) {
      throw new IllegalStateException("Person is already archived " + this);
    }
    anonymiseProfile();
    anonymiseAddress();
    archived = true;

    registerEvent(PersonArchivedEvent.forPerson(this));
    return this;
  }

  /**
   * Adds a person as parent to the current person
   *
   * @param parent the parent
   * @return the person with the new parent
   * @throws IllegalStateException if the person already has two parents
   * @throws ExistingParentException if the parent is already known
   * @throws ImpossibleKinshipRelationException if parent and child are the same person. No cycle
   *         checks in the relationship graph are being performed yet.
   */
  public Person connectParentProfile(Person parent) {
    if (!parentProfileMayBeConnected()) {
      throw new IllegalStateException("No more parent profile may be connected");
    } else if (parentProfiles.contains(parent)) {
      throw new ExistingParentException(String.format("Parent: %s; child: %s", parent, this));
    } else if (this.equals(parent)) {
      throw new ImpossibleKinshipRelationException(FOR_PERSON_MSG + this);
    }

    parentProfiles.add(parent);

    if (!updateEventWasRegistered()) {
      registerEvent(PersonDataUpdatedEvent.forPerson(this));
    }
    return this;
  }

  /**
   * Removes a parent - brutal.
   *
   * @param parent the former parent
   * @return the person without its parent
   * @throws IllegalArgumentException if the given parent is not known to be one
   */
  public Person disconnectParentProfile(Person parent) {
    if (parentProfiles.remove(parent)) {
      throw new IllegalArgumentException("No connection with parent " + parent);
    }

    parentProfiles.remove(parent);

    if (!updateEventWasRegistered()) {
      registerEvent(PersonDataUpdatedEvent.forPerson(this));
    }
    return this;
  }

  /**
   * @param event saves an event for publishing
   */
  void registerEvent(AbstractPersonRelatedEvent event) {
    domainEvents.add(event);
  }

  /**
   * @return whether at least one {@link PersonDataUpdatedEvent} was registered since the last
   *         {@link PersonRepository#save(Entity)} operation
   */
  boolean updateEventWasRegistered() {
    return hasRegisteredEventOf(PersonDataUpdatedEvent.class);
  }

  /**
   * @param clazz the event type to check for
   * @return whether the aggregate has at least one event of the given class registered
   */
  boolean hasRegisteredEventOf(Class<? extends AbstractPersonRelatedEvent> clazz) {
    return domainEvents.stream().anyMatch(e -> e.getClass().equals(clazz));
  }

  /**
   * @return all events that where saved for publishing. This will include at most one
   *         {@link PersonDataUpdatedEvent} and one {@link PersonArchivedEvent}
   */
  @DomainEvents
  Collection<AbstractPersonRelatedEvent> getModificationEvents() {
    return Collections.unmodifiableCollection(domainEvents);
  }

  /**
   * Marks all events as "has been published" should always be called after the result of
   * {@link #getModificationEvents()} has been processed
   */
  @AfterDomainEventPublication
  void clearEvents() {
    domainEvents.clear();
  }

  /**
   * Removes all data that could potentially identify this person
   */
  private void anonymiseProfile() {
    this.firstName = "";
    this.email = null;
  }

  /**
   * Removes all data that would make an address unique - which in this case is just its street
   */
  private void anonymiseAddress() {
    this.address = new Address("", address.getZip(), address.getCity());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Person person = (Person) o;

    return id.equals(person.id);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public String toString() {
    return "Person{" + "id=" + id + ", firstName='" + firstName + '\'' + ", lastName='" + lastName
        + '\'' + ", email='" + email + '\'' + '}';
  }
}
