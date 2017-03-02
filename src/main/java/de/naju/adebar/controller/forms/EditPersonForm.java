package de.naju.adebar.controller.forms;

/**
 * Model POJO for person data. The fields are set by Thymeleaf when the associated form is submitted.
 * @author Rico Bergmann
 */
public class EditPersonForm {
    public static String DATE_FORMAT = "dd.MM.yyyy";

    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private String dateOfBirth;
    private String eatingHabit;
    private String healthImpairments;
    private String street;
    private String zip;
    private String city;
    private boolean nabuMember;
    private String nabuNumber;


    public EditPersonForm() {}

    public EditPersonForm(String firstName, String lastName, String email, String gender, String dateOfBirth, String eatingHabit,
                          String healthImpairments, String street, String zip, String city, boolean nabuMember, String nabuNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.eatingHabit = eatingHabit;
        this.healthImpairments = healthImpairments;
        this.street = street;
        this.zip = zip;
        this.city = city;
        this.nabuMember = nabuMember;
        this.nabuNumber = nabuNumber;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public boolean hasDateOfBirth() {
        return dateOfBirth != null && !dateOfBirth.isEmpty();
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

    @Override
    public String toString() {
        return "EditPersonForm{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", eatingHabit='" + eatingHabit + '\'' +
                ", healthImpairments='" + healthImpairments + '\'' +
                ", street='" + street + '\'' +
                ", zip='" + zip + '\'' +
                ", city='" + city + '\'' +
                ", nabuMember=" + nabuMember +
                ", nabuNumber='" + nabuNumber + '\'' +
                '}';
    }
}
