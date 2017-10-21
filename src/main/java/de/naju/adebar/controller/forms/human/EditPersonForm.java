package de.naju.adebar.controller.forms.human;

import javax.validation.constraints.NotNull;

/**
 * Model POJO for person data. The fields are set by Thymeleaf when the associated form is
 * submitted.
 *
 * @author Rico Bergmann
 */
public class EditPersonForm {

  public final static String DATE_FORMAT = "dd.MM.yyyy";

  // general data
  @NotNull
  private String firstName;
  @NotNull
  private String lastName;
  private String email;
  private String phoneNumber;
  private String street;
  private String zip;
  private String city;

  // participant data
  private boolean participant;
  private String gender;
  private String dateOfBirth;
  private String eatingHabit;
  private String healthImpairments;
  private String remarks;
  private boolean nabuMember;
  private String nabuNumber;

  public EditPersonForm() {}

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    if (email != null && !email.isEmpty()) {
      return email;
    }
    return null;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getZip() {
    return zip;
  }

  public void setZip(String zip) {
    this.zip = zip;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

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

  public boolean isNabuMember() {
    return nabuMember;
  }

  public void setNabuMember(boolean nabuMember) {
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
    return "EditPersonForm{" + "firstName='" + firstName + '\'' + ", lastName='" + lastName + '\''
        + ", email='" + email + '\'' + ", phoneNumber='" + phoneNumber + '\'' + ", street='"
        + street + '\'' + ", zip='" + zip + '\'' + ", city='" + city + '\'' + ", participant="
        + participant + ", gender='" + gender + '\'' + ", dateOfBirth='" + dateOfBirth + '\''
        + ", eatingHabit='" + eatingHabit + '\'' + ", healthImpairments='" + healthImpairments
        + '\'' + ", nabuMember=" + nabuMember + ", nabuNumber='" + nabuNumber + '\'' + '}';
  }
}
