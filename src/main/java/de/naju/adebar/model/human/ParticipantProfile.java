package de.naju.adebar.model.human;

import java.time.LocalDate;
import java.time.Period;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Transient;
import org.springframework.util.Assert;

/**
 * Every camp participant has to fill a registration form. The corresponding data will be collected
 * in this profile.
 *
 * @author Rico Bergmann
 */
@Entity(name = "participant")
public class ParticipantProfile {

  @EmbeddedId
  @Column(name = "personId")
  private PersonId personId;

  @Column(name = "gender")
  private Gender gender;

  @Column(name = "dateOfBirth")
  private LocalDate dateOfBirth;

  @Column(name = "eatingHabit")
  private String eatingHabits;

  @Column(name = "healthImpairment")
  private String healthImpairments;

  @Embedded
  @AttributeOverrides({@AttributeOverride(name = "membershipNumber",
      column = @Column(name = "nabuMembershipNumber"))})
  private NabuMembership nabuMembership;

  @Column(name = "remarks", length = 512)
  private String remarks;

  /**
   * Copy constructor
   *
   * @param other the profile to copy
   */
  public ParticipantProfile(ParticipantProfile other) {
    this.personId = new PersonId(other.personId);
    this.gender = other.gender;
    this.dateOfBirth = other.dateOfBirth;
    this.eatingHabits = other.eatingHabits;
    this.healthImpairments = other.healthImpairments;
    this.nabuMembership = other.nabuMembership;
  }

  /**
   * Each participant profile has to be created in terms of an existing person.
   *
   * @param person the person to create the profile for
   */
  ParticipantProfile(Person person) {
    Assert.notNull(person, "Id may not be null");
    this.personId = person.getId();
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
  }

  /**
   * Default constructor for JPA's sake
   */
  @SuppressWarnings("unused")
  private ParticipantProfile() {}

  /**
   * @return the ID of the person to whom this profile belongs.
   */
  public PersonId getPersonId() {
    return personId;
  }

  /**
   * @return the person's gender. May be {@code null}.
   */
  public Gender getGender() {
    return gender;
  }

  /**
   * @return the person's date of birth. May be {@code null}.
   */
  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  /**
   * @return the person's eating habit (i.e. vegetarian and the like as well as food-related
   *         allergies). May be {@code null}.
   */
  public String getEatingHabits() {
    return eatingHabits;
  }

  /**
   * @return the person's health impairments (mainly non-food-related allergies like hayfever). May
   *         be {@code null}.
   */
  public String getHealthImpairments() {
    return healthImpairments;
  }

  /**
   * @return information regarding the person's membership in the NABU. May be {@code null} if the
   *         person is not a NABU member.
   */
  public NabuMembership getNabuMembership() {
    return nabuMembership;
  }

  /**
   * @return additional remarks such as swimming permission or other information. May be
   *         {@code null}.
   */
  public String getRemarks() {
    return remarks;
  }

  /**
   * @return {@code true} if the person has a date of birth specified and {@code false} otherwise
   */
  public boolean hasDateOfBirth() {
    return dateOfBirth != null;
  }

  /**
   * @return the age of the person
   * @throws IllegalStateException if the person has no date of birth specified
   */
  public int calculateAge() {
    if (!hasDateOfBirth()) {
      throw new IllegalStateException("No date of birth specified");
    }
    Period age = Period.between(dateOfBirth, LocalDate.now());
    return age.getYears();
  }

  /**
   * @return {@code true} if the person is a NABU member and {@code false} otherwise
   */
  @Transient
  public boolean isNabuMember() {
    return nabuMembership != null;
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
    ParticipantProfile updatedProfile = new ParticipantProfile(this);
    updatedProfile.setGender(gender);
    updatedProfile.setDateOfBirth(dateOfBirth);
    updatedProfile.setEatingHabits(eatingHabits);
    updatedProfile.setHealthImpairments(healthImpairments);
    return updatedProfile;
  }

  /**
   * Updates participation info
   *
   * @param gender the new gender
   * @return the updated profile information
   */
  public ParticipantProfile updateGender(Gender gender) {
    ParticipantProfile updatedProfile = new ParticipantProfile(this);
    updatedProfile.setGender(gender);
    return updatedProfile;
  }

  /**
   * Updates participation info
   *
   * @param dateOfBirth the new date of birth
   * @return the updated profile information
   */
  public ParticipantProfile updateDateOfBirth(LocalDate dateOfBirth) {
    ParticipantProfile updatedProfile = new ParticipantProfile(this);
    updatedProfile.setDateOfBirth(dateOfBirth);
    return updatedProfile;
  }

  /**
   * Updates the participation-related remarks
   *
   * @param remarks the remarks
   * @return the new profile information
   */
  public ParticipantProfile updateRemarks(String remarks) {
    ParticipantProfile updatedProfile = new ParticipantProfile(this);
    updatedProfile.setRemarks(remarks);
    return updatedProfile;
  }

  /**
   * Updates the NABU membership information
   *
   * @param nabuMembership the new information
   * @return the new profile
   */
  public ParticipantProfile updateNabuMembership(NabuMembership nabuMembership) {
    ParticipantProfile updatedProfile = new ParticipantProfile(this);
    updatedProfile.setNabuMembership(nabuMembership);
    return updatedProfile;
  }

  /**
   * @param gender the person's gender. May be {@code null}.
   */
  protected void setGender(Gender gender) {
    this.gender = gender;
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
   * @param eatingHabits the person's eating habit (i.e. vegetarian and the like as well as
   *        food-related allergies). May be {@code null}.
   */
  protected void setEatingHabits(String eatingHabits) {
    this.eatingHabits = eatingHabits;
  }

  /**
   * @param healthImpairments the person's health impairments (mainly non-food-related allergies
   *        like hayfever). May be {@code null}.
   */
  protected void setHealthImpairments(String healthImpairments) {
    this.healthImpairments = healthImpairments;
  }

  /**
   *
   * @param nabuMembership information regarding the person's membership in the NABU. May be
   *        {@code null} if the person is not a NABU member.
   */
  protected void setNabuMembership(NabuMembership nabuMembership) {
    this.nabuMembership = nabuMembership;
  }

  /**
   * @param remarks additional remarks such as swimming permission or other information. May be
   *        {@code null}.
   */
  protected void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  /**
   * @param personId the ID of the person to whom this profile belongs.
   */
  @SuppressWarnings("unused")
  private void setPersonId(PersonId personId) {
    this.personId = personId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

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
