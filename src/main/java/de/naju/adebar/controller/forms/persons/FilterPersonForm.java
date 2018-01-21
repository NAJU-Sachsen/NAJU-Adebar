package de.naju.adebar.controller.forms.persons;

import java.util.List;

/**
 * Model POJO for filters. The fields are set by Thymeleaf when the associated form is submitted.
 *
 * @author Rico Bergmann
 */
public class FilterPersonForm extends PersonForm {

  public static final String DATE_FORMAT = "dd.MM.yyyy";

  private String genderFilterType;
  private String gender;

  private String dobFilterType;
  private String dob;

  private String eatingHabit;
  private String healthImpairments;

  private String activistFilterType;
  private String activistJuleicaFilterType;
  private String activistJuleicaExpiryFilterType;
  private String juleicaExpiryDate;

  private String referentsFilterType;
  private List<String> referentQualifications;

  private String nabuMembershipFilterType;
  private String nabuMembershipNumber;

  private String participatedEventName;

  private boolean returnParents;

  public String getGenderFilterType() {
    return genderFilterType;
  }

  public void setGenderFilterType(String genderFilterType) {
    this.genderFilterType = genderFilterType;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getDobFilterType() {
    return dobFilterType;
  }

  public void setDobFilterType(String dobFilterType) {
    this.dobFilterType = dobFilterType;
  }

  public String getDob() {
    return dob;
  }

  public void setDob(String dob) {
    this.dob = dob;
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

  public String getActivistFilterType() {
    return activistFilterType;
  }

  public void setActivistFilterType(String activistFilterType) {
    this.activistFilterType = activistFilterType;
  }

  public String getActivistJuleicaFilterType() {
    return activistJuleicaFilterType;
  }

  public void setActivistJuleicaFilterType(String activistJuleicaFilterType) {
    this.activistJuleicaFilterType = activistJuleicaFilterType;
  }

  public String getActivistJuleicaExpiryFilterType() {
    return activistJuleicaExpiryFilterType;
  }

  public void setActivistJuleicaExpiryFilterType(String activistJuleicaExpiryFilterType) {
    this.activistJuleicaExpiryFilterType = activistJuleicaExpiryFilterType;
  }

  public String getJuleicaExpiryDate() {
    return juleicaExpiryDate;
  }

  public void setJuleicaExpiryDate(String juleicaExpiryDate) {
    this.juleicaExpiryDate = juleicaExpiryDate;
  }

  public String getReferentsFilterType() {
    return referentsFilterType;
  }

  public void setReferentsFilterType(String referentsFilterType) {
    this.referentsFilterType = referentsFilterType;
  }

  public List<String> getReferentQualifications() {
    return referentQualifications;
  }

  public void setReferentQualifications(List<String> referentQualifications) {
    this.referentQualifications = referentQualifications;
  }

  public String getNabuMembershipFilterType() {
    return nabuMembershipFilterType;
  }

  public void setNabuMembershipFilterType(String nabuMembershipFilterType) {
    this.nabuMembershipFilterType = nabuMembershipFilterType;
  }

  public String getNabuMembershipNumber() {
    return nabuMembershipNumber;
  }

  public void setNabuMembershipNumber(String nabuMembershipNumber) {
    this.nabuMembershipNumber = nabuMembershipNumber;
  }

  public String getParticipatedEventName() {
    return participatedEventName;
  }

  public void setParticipatedEventName(String participatedEventName) {
    this.participatedEventName = participatedEventName;
  }

  public boolean isReturnParents() {
    return returnParents;
  }

  public void setReturnParents(boolean returnParents) {
    this.returnParents = returnParents;
  }

  @Override
  public String toString() {
    return "FilterPersonForm{" + "firstName='" + getFirstName() + '\'' + ", lastName='"
        + getLastName() + '\'' + ", email='" + getEmail() + '\'' + ", genderFilterType='"
        + genderFilterType + '\'' + ", gender='" + gender + '\'' + ", dobFilterType='"
        + dobFilterType + '\'' + ", dob='" + dob + '\'' + ", eatingHabit='" + eatingHabit + '\''
        + ", healthImpairments='" + healthImpairments + '\'' + ", street='" + getStreet() + '\''
        + ", zip='" + getZip() + '\'' + ", city='" + getCity() + '\'' + ", activistFilterType='"
        + activistFilterType + '\'' + ", activistJuleicaFilterType='" + activistJuleicaFilterType
        + '\'' + ", activistJuleicaExpiryFilterType='" + activistJuleicaExpiryFilterType + '\''
        + ", juleicaExpiryDate='" + juleicaExpiryDate + '\'' + ", referentsFilterType='"
        + referentsFilterType + '\'' + ", referentQualifications=" + referentQualifications
        + ", nabuMembershipFilterType='" + nabuMembershipFilterType + '\''
        + ", nabuMembershipNumber='" + nabuMembershipNumber + '\'' + '}';
  }
}
