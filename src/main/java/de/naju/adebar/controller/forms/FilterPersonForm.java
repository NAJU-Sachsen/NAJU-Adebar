package de.naju.adebar.controller.forms;

import java.util.List;

/**
 * Model POJO for filters. The fields are set by Thymeleaf when the associated form is submitted.
 * @author Rico Bergmann
 */
public class FilterPersonForm {
    public static String DATE_FORMAT = "dd.MM.yyyy";

    private String firstName, lastName;
    private String email;
    private String genderFilterType, gender;
    private String dobFilterType, dob;
    private String eatingHabit, healthImpairments;
    private String street, zip, city;
    private String activistFilterType, activistJuleicaFilterType;
    private String activistJuleicaExpiryFilterType, juleicaExpiryDate;
    private String referentsFilterType;
    private List<String> referentQualifications;

    public FilterPersonForm() {

    }

    public static String getDateFormat() {
        return DATE_FORMAT;
    }

    public static void setDateFormat(String dateFormat) {
        DATE_FORMAT = dateFormat;
    }

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
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

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

    @Override
    public String toString() {
        return "FilterPersonForm{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", genderFilterType='" + genderFilterType + '\'' +
                ", gender='" + gender + '\'' +
                ", dobFilterType='" + dobFilterType + '\'' +
                ", dob='" + dob + '\'' +
                ", eatingHabit='" + eatingHabit + '\'' +
                ", healthImpairments='" + healthImpairments + '\'' +
                ", street='" + street + '\'' +
                ", zip='" + zip + '\'' +
                ", city='" + city + '\'' +
                ", activistFilterType='" + activistFilterType + '\'' +
                ", activistJuleicaFilterType='" + activistJuleicaFilterType + '\'' +
                ", activistJuleicaExpiryFilterType='" + activistJuleicaExpiryFilterType + '\'' +
                ", juleicaExpiryDate='" + juleicaExpiryDate + '\'' +
                ", referentsFilterType='" + referentsFilterType + '\'' +
                ", referentQualifications=" + referentQualifications +
                '}';
    }
}
