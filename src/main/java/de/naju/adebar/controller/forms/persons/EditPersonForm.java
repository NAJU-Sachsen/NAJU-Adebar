package de.naju.adebar.controller.forms.persons;

import de.naju.adebar.model.persons.NabuMembershipInformation.MembershipStatus;

/**
 * Model POJO for person data. The fields are set by Thymeleaf when the associated form is
 * submitted.
 *
 * @author Rico Bergmann
 */
public class EditPersonForm extends PersonForm {

  public static final String DATE_FORMAT = "dd.MM.yyyy";

  // participant data
  private boolean participant;
  private String gender;
  private String dateOfBirth;
  private String eatingHabit;
  private String healthImpairments;
  private String remarks;
  private MembershipStatus nabuMember;
  private String nabuNumber;

  public boolean isParticipant() {
    return participant;
  }

  public void setParticipant(boolean participant) {
    this.participant = participant;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(String dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public String getEatingHabit() {
    return eatingHabit;
  }

  public void setEatingHabit(String eatingHabit) {
    this.eatingHabit = eatingHabit;
  }

  public String getHealthImpairments() {
    return healthImpairments;
  }

  public void setHealthImpairments(String healthImpairments) {
    this.healthImpairments = healthImpairments;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public MembershipStatus getNabuMember() {
    return nabuMember;
  }

  public void setNabuMember(MembershipStatus nabuMember) {
    this.nabuMember = nabuMember;
  }

  public String getNabuNumber() {
    return nabuNumber;
  }

  public void setNabuNumber(String nabuNumber) {
    this.nabuNumber = nabuNumber;
  }

  public boolean hasDateOfBirth() {
    return dateOfBirth != null && !dateOfBirth.isEmpty();
  }

  @Override
  public String toString() {
    return "EditPersonForm{" + "firstName='" + getFirstName() + '\'' + ", lastName='"
        + getLastName() + '\'' + ", email='" + getEmail() + '\'' + ", street='" + getStreet() + '\''
        + ", zip='" + getZip() + '\'' + ", city='" + getCity() + '\'' + ", participant="
        + participant + ", gender='" + gender + '\'' + ", dateOfBirth='" + dateOfBirth + '\''
        + ", eatingHabit='" + eatingHabit + '\'' + ", healthImpairments='" + healthImpairments
        + '\'' + ", nabuMember=" + nabuMember + ", nabuNumber='" + nabuNumber + '\'' + '}';
  }
}
