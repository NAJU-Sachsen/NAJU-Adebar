package de.naju.adebar.model.human;

import java.time.LocalDate;
import java.time.Period;
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
  private NabuMembership nabuMembership;
  @Column(name = "remarks", length = 512)
  private String remarks;

  // constructors

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
   * Default constructor for JPA's sake
   */
  @SuppressWarnings("unused")
  private ParticipantProfile() {}

  // getter and setter

  /**
   * @return the ID of the person to whom this profile belongs.
   */
  public PersonId getPersonId() {
    return personId;
  }

  /**
   * @param personId the ID of the person to whom this profile belongs.
   */
  @SuppressWarnings("unused")
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
  public void setGender(Gender gender) {
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
  public void setDateOfBirth(LocalDate dateOfBirth) {
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
  public void setEatingHabits(String eatingHabits) {
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
  public void setHealthImpairments(String healthImpairments) {
    this.healthImpairments = healthImpairments;
  }

  /**
   * @return information regarding the person's membership in the NABU. May be {@code null} if the
   *         person is not a NABU member.
   */
  public NabuMembership getNabuMembership() {
    return nabuMembership;
  }

  /**
   * @param nabuMembership information regarding the person's membership in the NABU. May be {@code
   * null} if the person is not a NABU member.
   */
  public void setNabuMembership(NabuMembership nabuMembership) {
    this.nabuMembership = nabuMembership;
  }

  /**
   * @return additional remarks such as swimming permission or other information. May be {@code
   * null}.
   */
  public String getRemarks() {
    return remarks;
  }

  /**
   * @param remarks additional remarks such as swimming permission or other information. May be
   *        {@code null}.
   */
  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  // normal methods

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

  // overridden from Object

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
