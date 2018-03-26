package de.naju.adebar.web.validation.persons.participant;

import java.time.LocalDate;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import de.naju.adebar.model.persons.details.Gender;
import de.naju.adebar.model.persons.details.NabuMembershipInformation;
import de.naju.adebar.model.persons.details.NabuMembershipInformation.MembershipStatus;
import de.naju.adebar.web.validation.AbstractForm;
import de.naju.adebar.web.validation.persons.EditPersonForm;

/**
 * POJO representation of the participant information inside an {@link EditPersonForm}
 *
 * @author Rico Bergmann
 */
public class EditParticipantForm implements AbstractForm {

  /**
   * The maximum number of characters in the textarea-fields
   */
  public static final int MAX_STRING_LENGTH = 511;

  private Gender gender;

  // this is the default pattern for dates on the web
  // see https://developer.mozilla.org/en-US/docs/Web/HTML/Element/input/date#Value
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate dateOfBirth;

  @Length(max = 511)
  private String eatingHabits;

  @Length(max = 511)
  private String healthImpairments;

  private NabuMembershipInformation.MembershipStatus nabuMember;
  private String nabuMembershipNumber;

  @Length(max = 511)
  private String remarks;

  /**
   * Full constructor. Every field may be {@code null}.
   *
   * @param gender the participant's gender
   * @param dateOfBirth the participant's date of birth
   * @param eatingHabits the participant's eating habits
   * @param healthImpairments the participant's health impairments
   * @param nabuMembership the participant's NABU membership status
   * @param remarks remarks about the participant
   */
  public EditParticipantForm(Gender gender, //
      LocalDate dateOfBirth, //
      String eatingHabits, //
      String healthImpairments, //
      NabuMembershipInformation nabuMembership, //
      String remarks) {

    this.gender = gender;
    this.dateOfBirth = dateOfBirth;
    this.eatingHabits = eatingHabits;
    this.healthImpairments = healthImpairments;

    if (nabuMembership != null) {
      this.nabuMember = nabuMembership.getStatus();
      // getMembershipNumber will throw an exception if the participant is no NABU member
      this.nabuMembershipNumber = (nabuMember == MembershipStatus.IS_MEMBER) //
          ? nabuMembership.getMembershipNumber() //
          : null;
    } else {
      nabuMember = MembershipStatus.UNKNOWN;
    }

    this.remarks = remarks;
  }

  /**
   * Default constructor
   */
  public EditParticipantForm() {}

  /**
   * @return the participant's gender
   */
  public Gender getGender() {
    return gender;
  }

  /**
   * @param gender the participant's gender
   */
  public void setGender(Gender gender) {
    this.gender = gender;
  }

  /**
   * @return the participant's date of birth
   */
  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  /**
   * @param dateOfBirth the participant's date of birth
   */
  public void setDateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  /**
   * @return the participant's eating habits
   */
  public String getEatingHabits() {
    return eatingHabits;
  }

  /**
   * @param eatingHabits the participant's eating habits
   */
  public void setEatingHabits(String eatingHabits) {
    this.eatingHabits = eatingHabits;
  }

  /**
   * @return the participant's health impairments
   */
  public String getHealthImpairments() {
    return healthImpairments;
  }

  /**
   * @param healthImpairments the participant's health impairments
   */
  public void setHealthImpairments(String healthImpairments) {
    this.healthImpairments = healthImpairments;
  }

  /**
   * @return the participant's NABU membership status
   */
  public MembershipStatus getNabuMember() {
    return nabuMember;
  }

  /**
   * @param nabuMember the participant's NABU membership status
   */
  public void setNabuMember(MembershipStatus nabuMember) {
    this.nabuMember = nabuMember;
  }

  /**
   * @return the participant's NABU membership number
   */
  public String getNabuMembershipNumber() {
    return nabuMembershipNumber;
  }

  /**
   * @param nabuMembershipNumber the participant's NABU membership number. May be {@code null} if
   *        the participant is no NABU member.
   */
  public void setNabuMembershipNumber(String nabuMembershipNumber) {
    this.nabuMembershipNumber = nabuMembershipNumber;
  }

  /**
   * @return remarks about the participant
   */
  public String getRemarks() {
    return remarks;
  }

  /**
   * @param remarks remarks about the participant
   */
  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  /**
   * @return whether the form contains information about the person's gender
   */
  public boolean hasGender() {
    return gender != null;
  }

  /**
   * @return whether the form contains information about the person's date of birth
   */
  public boolean hasDateOfBirth() {
    return dateOfBirth != null;
  }

  /**
   * @return whether the form contains information about the person's eating habits
   */
  public boolean hasEatingHabits() {
    return eatingHabits != null && !eatingHabits.isEmpty();
  }

  /**
   * @return whether the form contains information about health impairments of the person
   */
  public boolean hasHealthImpairments() {
    return healthImpairments != null && !healthImpairments.isEmpty();
  }

  /**
   * @return whether the form contains information about the person's nabu membership
   */
  public boolean hasNabuMembershipInformation() {
    return nabuMember != null;
  }

  /**
   * @return whether the person contains additional remarks
   */
  public boolean hasRemarks() {
    return remarks != null && !remarks.isEmpty();
  }

  @Override
  public boolean hasData() {
    return hasGender() || hasDateOfBirth() || hasEatingHabits() || hasHealthImpairments()
        || hasNabuMembershipInformation() || hasRemarks();
  }

  @Override
  public String toString() {
    return "EditParticipantForm [" + "gender=" + gender + ", dateOfBirth=" + dateOfBirth
        + ", eatingHabits='" + eatingHabits + '\'' + ", healthImpairments='" + healthImpairments
        + '\'' + ", nabuMember=" + nabuMember + ", nabuMembershipNumber='" + nabuMembershipNumber
        + '\'' + ", remarks='" + remarks + '\'' + ']';
  }
}
