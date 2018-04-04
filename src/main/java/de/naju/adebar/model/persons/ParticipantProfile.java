package de.naju.adebar.model.persons;

import java.time.LocalDate;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Transient;
import org.springframework.util.Assert;
import de.naju.adebar.documentation.ddd.BusinessRule;
import de.naju.adebar.documentation.infrastructure.JpaOnly;
import de.naju.adebar.model.core.Age;
import de.naju.adebar.model.persons.details.Gender;
import de.naju.adebar.model.persons.details.NabuMembershipInformation;
import de.naju.adebar.model.persons.events.PersonDataUpdatedEvent;
import de.naju.adebar.model.persons.exceptions.DateOfBirthIsRequiredForMinorsException;

/**
 * Every camp participant has to fill a registration form. The corresponding data will be collected
 * in this profile.
 *
 * @author Rico Bergmann
 */
@Entity(name = "participant")
public class ParticipantProfile extends AbstractProfile {

  public static final int LEGAL_AGE = 18;

  @EmbeddedId
  @Column(name = "personId")
  private PersonId personId;

  @Column(name = "gender")
  private Gender gender;

  @Column(name = "dateOfBirth")
  private LocalDate dateOfBirth;

  @Column(name = "eatingHabit", length = 511)
  private String eatingHabits;

  @Column(name = "healthImpairment", length = 511)
  private String healthImpairments;

  @Embedded
  @AttributeOverrides({@AttributeOverride(name = "membershipNumber",
      column = @Column(name = "nabuMembershipNumber"))})
  private NabuMembershipInformation nabuMembership;

  @Column(name = "remarks", length = 511)
  private String remarks;

  /**
   * Each participant profile has to be created in terms of an existing person.
   *
   * @param person the person to create the profile for
   */
  ParticipantProfile(Person person) {
    Assert.notNull(person, "Id may not be null");
    this.personId = person.getId();
    provideRelatedPerson(person);
  }

  /**
   * Convenience constructor to initialize a new profile right away.
   *
   * @param person the person to create the person for
   * @param gender the person's gender
   * @param dateOfBirth the person's date of birth
   * @param eatingHabits the person's eating habits
   * @param healthImpairments the person's health impairments
   */
  ParticipantProfile(Person person, Gender gender, LocalDate dateOfBirth, String eatingHabits,
      String healthImpairments) {
    Assert.notNull(person, "Id may not be null");
    Assert.isTrue(dateOfBirth == null || dateOfBirth.isBefore(LocalDate.now()),
        "Date of birth must be past!");
    this.personId = person.getId();
    this.gender = gender;
    this.dateOfBirth = dateOfBirth;
    this.eatingHabits = eatingHabits;
    this.healthImpairments = healthImpairments;
    provideRelatedPerson(person);
  }

  /**
   * Default constructor for JPA's sake
   */
  @JpaOnly
  private ParticipantProfile() {}

  /**
   * @return the ID of the person to whom this profile belongs.
   */
  public PersonId getPersonId() {
    return personId;
  }

  /**
   * @param personId the ID of the person to whom this profile belongs.
   */
  @JpaOnly
  private void setPersonId(PersonId personId) {
    this.personId = personId;
  }

  /**
   * @return the person's gender. May be {@code null}.
   */
  public Gender getGender() {
    return gender;
  }

  /**
   * @param gender the person's gender. May be {@code null}.
   */
  protected void setGender(Gender gender) {
    this.gender = gender;
  }

  /**
   * @return the person's date of birth. May be {@code null}.
   */
  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  /**
   * @param dateOfBirth the person's date of birth. May be {@code null}.
   * @throws IllegalArgumentException if the date of birth is not past
   */
  protected void setDateOfBirth(LocalDate dateOfBirth) {
    if (dateOfBirth != null) {
      Assert.isTrue(dateOfBirth.isBefore(LocalDate.now()), "Date of birth must be past!");
    }
    this.dateOfBirth = dateOfBirth;
  }

  /**
   * @return the person's eating habit (i.e. vegetarian and the like as well as food-related
   *         allergies). May be {@code null}.
   */
  public String getEatingHabits() {
    return eatingHabits;
  }

  /**
   * @param eatingHabits the person's eating habit (i.e. vegetarian and the like as well as
   *        food-related allergies). May be {@code null}.
   */
  protected void setEatingHabits(String eatingHabits) {
    this.eatingHabits = eatingHabits;
  }

  /**
   * @return the person's health impairments (mainly non-food-related allergies like hayfever). May
   *         be {@code null}.
   */
  public String getHealthImpairments() {
    return healthImpairments;
  }

  /**
   * @param healthImpairments the person's health impairments (mainly non-food-related allergies
   *        like hayfever). May be {@code null}.
   */
  protected void setHealthImpairments(String healthImpairments) {
    this.healthImpairments = healthImpairments;
  }

  /**
   * @return information regarding the person's membership in the NABU. May be {@code null} if the
   *         person is not a NABU member.
   */
  public NabuMembershipInformation getNabuMembership() {
    return nabuMembership;
  }

  /**
   * @param nabuMembership information regarding the person's membership in the NABU.
   */
  protected void setNabuMembership(NabuMembershipInformation nabuMembership) {
    this.nabuMembership = nabuMembership;
  }

  /**
   * @return additional remarks such as swimming permission or other information. May be {@code
   *     null}.
   */
  public String getRemarks() {
    return remarks;
  }

  /**
   * @param remarks additional remarks such as swimming permission or other information. May be
   *        {@code null}.
   */
  protected void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  /**
   * @return {@code true} if the person has a date of birth specified and {@code false} otherwise
   */
  public boolean hasDateOfBirth() {
    return dateOfBirth != null;
  }

  /**
   * @return the age of the person
   *
   * @throws IllegalStateException if the person has no date of birth specified
   */
  public Age calculateAge() {
    if (!hasDateOfBirth()) {
      throw new IllegalStateException("No date of birth specified");
    }
    return Age.forDateOfBirth(dateOfBirth);
  }

  /**
   * @return {@code true} if the person is a NABU member and {@code false} otherwise
   */
  @Transient
  public boolean isNabuMember() {
    if (isNabuMembershipUnknown()) {
      return false;
    }
    return nabuMembership.isNabuMember();
  }

  /**
   * @return {@code true} if it is unknown, whether the person is a NABU member
   */
  public boolean isNabuMembershipUnknown() {
    return nabuMembership == null;
  }

  /**
   * Updates the participation information
   *
   * @param gender the new gender
   * @param dateOfBirth the new date of birth <small>(does it change?)</small>
   * @param eatingHabits new eating habits <small>- it's vegan isn't it?</small>
   * @param healthImpairments (hopefully less) health impairments
   * @return the new profile information
   */
  public ParticipantProfile updateProfile(Gender gender, LocalDate dateOfBirth, String eatingHabits,
      String healthImpairments) {
    assertDateOfBirthIsSetForMinors(dateOfBirth, gender);

    setGender(gender);
    setDateOfBirth(dateOfBirth);
    setEatingHabits(eatingHabits);
    setHealthImpairments(healthImpairments);

    getRelatedPerson().ifPresent( //
        person -> registerEventIfPossible(PersonDataUpdatedEvent.forPerson(person)));

    return this;
  }

  /**
   * Updates participation info
   *
   * @param gender the new gender
   * @return the updated profile information
   */
  public ParticipantProfile updateGender(Gender gender) {
    assertDateOfBirthIsSetForMinors(this.dateOfBirth, gender);

    setGender(gender);

    getRelatedPerson().ifPresent( //
        person -> registerEventIfPossible(PersonDataUpdatedEvent.forPerson(person)));

    return this;
  }

  /**
   * Updates participation info
   *
   * @param dateOfBirth the new date of birth
   * @return the updated profile information
   */
  public ParticipantProfile updateDateOfBirth(LocalDate dateOfBirth) {
    assertDateOfBirthIsSetForMinors(dateOfBirth, this.gender);

    setDateOfBirth(dateOfBirth);

    getRelatedPerson().ifPresent( //
        person -> registerEventIfPossible(PersonDataUpdatedEvent.forPerson(person)));

    return this;
  }

  /**
   * Updates the participation-related remarks
   *
   * @param remarks the remarks
   * @return the new profile information
   */
  public ParticipantProfile updateRemarks(String remarks) {
    setRemarks(remarks);
    return this;
  }

  /**
   * Updates the NABU membership information
   *
   * @param nabuMembership the new information
   * @return the new profile
   */
  public ParticipantProfile updateNabuMembership(NabuMembershipInformation nabuMembership) {
    setNabuMembership(nabuMembership);

    getRelatedPerson().ifPresent( //
        person -> registerEventIfPossible(PersonDataUpdatedEvent.forPerson(person)));

    return this;
  }

  /**
   * Checks whether the person associated to this participant profile is under-aged, i.e. younger
   * than {@link #LEGAL_AGE}. <b>If no date of birth is set, {@code false} will be returned</b>
   *
   * @return whether the person is younger than the legal age
   */
  public boolean isOfMinorAge() {
    if (!hasDateOfBirth()) {
      return false;
    }

    return personWithBirthdayIsMinor(dateOfBirth);
  }

  /**
   * Checks whether a person born on {@code date} is of minor age.
   *
   * @param date the date of birth to check
   * @return whether the date describes an under-aged person
   */
  private boolean personWithBirthdayIsMinor(LocalDate date) {
    return !Age.forDateOfBirth(date).isOfLegalAge();
  }

  /**
   * @param dateOfBirth the date of birth to check
   * @param gender the gender to check
   * @throws DateOfBirthIsRequiredForMinorsException if the person is under-aged and no gender was
   *         given
   */
  @BusinessRule
  private void assertDateOfBirthIsSetForMinors(LocalDate dateOfBirth, Gender gender) {
    if (dateOfBirth != null && personWithBirthdayIsMinor(dateOfBirth) && gender == null) {
      throw new DateOfBirthIsRequiredForMinorsException(personId);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ParticipantProfile profile = (ParticipantProfile) o;

    return personId.equals(profile.personId);
  }

  @Override
  public int hashCode() {
    return personId.hashCode();
  }

  @Override
  public String toString() {
    return "ParticipantProfile{" + "personId=" + personId + ", gender=" + gender + ", dateOfBirth="
        + dateOfBirth + ", eatingHabits='" + eatingHabits + '\'' + ", healthImpairments='"
        + healthImpairments + '\'' + ", nabuMembership=" + nabuMembership + ", remarks='" + remarks
        + '\'' + '}';
  }
}
