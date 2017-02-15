package de.naju.adebar.model.human;

import java.beans.Transient;
import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.util.Assert;

import de.naju.adebar.util.Validation;

/**
 * Abstraction of a normal camp participant or a person that is otherwise of interest for our database.
 * As this class is primarily targeted at camp participants, it features things such as eating habits (e.g. vegetarian)
 * and health impairments (e.g. asthma, hay fever...)
 * @author Rico Bergmann
 */
@Entity
public class Person {

    @EmbeddedId private PersonId id;

    private String firstName, lastName;
    private String email;
    private Gender gender;
    private Address address;
    private LocalDate dateOfBirth;
    private String eatingHabit;
    private String healthImpairments;

    // constructors

    /**
     * Full constructor to be used from outside
     * @param firstName the person's first name
     * @param lastName the person's last name
     * @param email the person's email
     * @param gender the person's gender
     * @param address the person's address, may be {@code null}
     * @param dateOfBirth the person's date of birth, may be {@code null}
     * @throws IllegalArgumentException if any of the parameters is {@code null}, the email is not valid, the
     * date of birth lies in the future or names are empty
     */
    public Person(String firstName, String lastName, String email, Gender gender, Address address, LocalDate dateOfBirth) {
        Object[] params = {firstName, lastName, email, gender};
        Assert.noNullElements(params, "Parameters may not be null!");
        Assert.hasText(firstName, "First name may not be empty, but was: " + firstName);
        Assert.hasText(lastName, "Last name may not be empty, but was: " + lastName);
        Assert.isTrue(Validation.isEmail(email), "Must provide a valid email address, but was: " + email);
        if (dateOfBirth != null) {
            Assert.isTrue(dateOfBirth.isBefore(LocalDate.now()), "A date of birth must be in the past, but was " + dateOfBirth);
        }
        this.id = null;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.eatingHabit = "";
        this.healthImpairments = "";
    }

    /**
     * Full constructor to be used within the package. Therefore package-local..
     * @param id the id (=primary key) of the person
     * @param firstName the person's first name
     * @param lastName the person's last name
     * @param email the person's email
     * @param gender the person's gender
     * @param address the person's address, may be {@code null}
     * @param dateOfBirth the person's date of birth, may be {@code null}
     * @throws IllegalArgumentException if any of the parameters is {@code null}, the email is not valid, the
     * date of birth lies in the future or names are empty
     */
    Person(PersonId id, String firstName, String lastName, String email, Gender gender, Address address, LocalDate dateOfBirth) {
        Object[] params = {id, firstName, lastName, email, gender};
        Assert.noNullElements(params, "Parameters may not be null!");
        Assert.hasText(firstName, "First name may not be empty, but was: " + firstName);
        Assert.hasText(lastName, "Last name may not be empty, but was: " + lastName);
        Assert.isTrue(Validation.isEmail(email), "Must provide a valid email address, but was: " + email);
        if (dateOfBirth != null) {
            Assert.isTrue(dateOfBirth.isBefore(LocalDate.now()),
                    "A date of birth must be in the past, but was " + dateOfBirth);
        }
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.gender = gender;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.eatingHabit = "";
        this.healthImpairments = "";
    }

    /**
     * default constructor for JPA's sake
     */
    protected Person() {

    }

    // getter

    /**
     * @return the person's id (=primary key)
     */
    public PersonId getId() {
        return id;
    }

    /**
     * @return the person's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @return the person's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @return the person's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return the person's gender
     */
    public Gender getGender() {
        return gender;
    }

    /**
     * @return the person's address, may be {@code null}
     */
    public Address getAddress() {
        return address;
    }

    /**
     * @return the person's date of birth, may be {@code null}
     */
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * @return the person's eating habit
     */
    public String getEatingHabit() {
        return eatingHabit;
    }

    public String getHealthImpairments() {
        return healthImpairments;
    }

    // setter

    /**
     * @param firstName the first name to set
     * @throws IllegalArgumentException if the first name is {@code null} or empty
     */
    public void setFirstName(String firstName) {
        Assert.hasText(firstName, "First name may not be empty but was: " + firstName);
        this.firstName = firstName;
    }

    /**
     * @param lastName the last name to set
     * @throws IllegalArgumentException if the last name is {@code null} or empty
     */
    public void setLastName(String lastName) {
        Assert.hasText(lastName, "Last name may not be empty but was: " + lastName);
        this.lastName = lastName;
    }

    /**
     * @param email the email to set
     * @throws IllegalArgumentException if the email address is not valid or {@code null}
     */
    public void setEmail(String email) {
        Assert.notNull(email, "Email may not be null!");
        if (!Validation.isEmail(email)) {
            throw new IllegalArgumentException("Must provide a valid email address, but was: " + email);
        }
        this.email = email;
    }

    /**
     * @param gender the gender to set
     * @throws IllegalArgumentException if the gender is {@code null}
     */
    public void setGender(Gender gender) {
        Assert.notNull(gender, "Gender may not be null!");
        this.gender = gender;
    }

    /**
     * @param address the address to set, may be {@code null}
     */
    public void setAddress(Address address) {
        this.address = address;
    }

    /**
     * @param dateOfBirth the date of birth to set, may be {@code null}
     * @throws IllegalArgumentException if the date of birth lies in the future
     */
    public void setDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth != null) {
            Assert.isTrue(dateOfBirth.isBefore(LocalDate.now()),
                    "Date of birth must be in the past, but was: " + dateOfBirth);
        }
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * @param eatingHabit the eating habit (e.g. vegetarian, lactose intolerance, ...) to set, may be empty
     * @throws IllegalArgumentException if the eating habit is {@code null}
     */
    public void setEatingHabit(String eatingHabit) {
        Assert.notNull(eatingHabit, "Eating habit may not be null!");
        this.eatingHabit = eatingHabit;
    }

    /**
     * @param healthImpairments the health impairments to set (e.g. asthma, hay fever...), may be empty
     * @throws IllegalArgumentException if the health impairments is {@code null}
     */
    public void setHealthImpairments(String healthImpairments) {
        Assert.notNull(healthImpairments, "Health impairments may not be null (but hopefully empty :-) )!");
        this.healthImpairments = healthImpairments;
    }

    /**
     * Updates the newsletter's id.
     * <p>
     * As the id will be used as primary key in the database, it should not be changed by the user by any means.
     * Only JPA should access this method, which is why {@code setId()} was made {@code protected}.
     * </p>
     *
     * @param id the newsletter's new id
     * @throws IllegalArgumentException if the id is {@code null}
     */
    void setId(PersonId id) {
        Assert.notNull(id, "PersonID may not be null!");
        this.id = id;
    }

    // "advanced" getter

    /**
     * @return the subscriber's name, which basically is {@code firstName + " " + lastName}
     */
    @Transient
    public String getName() {
        return firstName + " " + lastName;
    }

    /**
     * @return {@code true} if the person has an id set. Important, as instances of the classes may be created from
     * outside the package and will therefore not have the id set
     */
    public boolean hasId() {
        return id != null;
    }

    /**
     * @return {@code true} if the person's date of birth is set
     */
    public boolean hasDateOfBirth() {
        return dateOfBirth != null;
    }

    // overridden from Object

    @Override
    public int hashCode() {
        int result = firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + email.hashCode();
        result = 31 * result + gender.hashCode();
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (dateOfBirth != null ? dateOfBirth.hashCode() : 0);
        result = 31 * result + (eatingHabit != null ? eatingHabit.hashCode() : 0);
        result = 31 * result + (healthImpairments != null ? healthImpairments.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (!firstName.equals(person.firstName)) return false;
        if (!lastName.equals(person.lastName)) return false;
        if (!email.equals(person.email)) return false;
        if (gender != person.gender) return false;
        if (address != null ? !address.equals(person.address) : person.address != null) return false;
        if (dateOfBirth != null ? !dateOfBirth.equals(person.dateOfBirth) : person.dateOfBirth != null) return false;
        if (eatingHabit != null ? !eatingHabit.equals(person.eatingHabit) : person.eatingHabit != null) return false;
        return healthImpairments != null ? healthImpairments.equals(person.healthImpairments) : person.healthImpairments == null;
    }

    @Override
    public String toString() {
        return String.format("Person [firstName=%s, lastName=%s, email=%s, dob=%s]", firstName, lastName, email, dateOfBirth);
    }
}
