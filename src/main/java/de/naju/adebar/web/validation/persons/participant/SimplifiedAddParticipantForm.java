package de.naju.adebar.web.validation.persons.participant;

import de.naju.adebar.model.persons.Person;
import de.naju.adebar.model.persons.details.Gender;
import de.naju.adebar.web.validation.NewOrExistingEntityForm;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * POJO representation of an 'add participant form' which offers to either use an existing
 * participant or create a new one using a reduced set of fields. Each such instance should be
 * created in terms of another existing person. Each person created through this form will be a
 * participant by default.
 *
 * @author Rico Bergmann
 */
public class SimplifiedAddParticipantForm implements NewOrExistingEntityForm {

  private Person existingPerson;
  private String newFirstName;
  private String newLastName;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate newDateOfBirth;

  private Gender newGender;

  private boolean newRetainAddress;

  /**
   * Full constructor. The person's last name as well as its date of birth will be reused
   *
   * @param person the person the form should be created for
   */
  public SimplifiedAddParticipantForm(Person person) {
    this.newLastName = person.getLastName();
    this.newDateOfBirth = person.isParticipant() //
        ? person.getParticipantProfile().getDateOfBirth() //
        : null;
  }

  /**
   * Default constructor
   */
  public SimplifiedAddParticipantForm() {
  }

  /**
   * @return the existing person
   */
  public Person getExistingPerson() {
    return existingPerson;
  }

  /**
   * @param existingPerson the existing person
   */
  public void setExistingPerson(Person existingPerson) {
    this.existingPerson = existingPerson;
  }

  /**
   * @return the new person's first name
   */
  public String getNewFirstName() {
    return newFirstName;
  }

  /**
   * @param newFirstName the new person's first name
   */
  public void setNewFirstName(String newFirstName) {
    this.newFirstName = newFirstName;
  }

  /**
   * @return the new person's last name
   */
  public String getNewLastName() {
    return newLastName;
  }

  /**
   * @param newLastName the new person's last name
   */
  public void setNewLastName(String newLastName) {
    this.newLastName = newLastName;
  }

  /**
   * @return the new person's date of birth
   */
  public LocalDate getNewDateOfBirth() {
    return newDateOfBirth;
  }

  /**
   * @param newDateOfBirth the new person's date of birth
   */
  public void setNewDateOfBirth(LocalDate newDateOfBirth) {
    this.newDateOfBirth = newDateOfBirth;
  }

  /**
   * @return the new person's gender
   */
  public Gender getNewGender() {
    return newGender;
  }

  /**
   * @param newGender the new person's gender
   */
  public void setNewGender(Gender newGender) {
    this.newGender = newGender;
  }

  /**
   * @return whether the new person should reuse the address of some other existing person (which
   *     may depend on the context)
   */
  public boolean isNewRetainAddress() {
    return newRetainAddress;
  }

  /**
   * @param newRetainAddress whether the new person should reuse the address of some other
   *     existing person
   */
  public void setNewRetainAddress(boolean newRetainAddress) {
    this.newRetainAddress = newRetainAddress;
  }

  @Override
  public SubmittedData getFormStatus() {
    return existingPerson == null ? SubmittedData.NEW_ENTITY : SubmittedData.EXISTING_ENTITY;
  }

  @Override
  public String toString() {
    return "SimplifiedAddParticipantForm [" +
        "existingPerson=" + existingPerson +
        ", newFirstName='" + newFirstName + '\'' +
        ", newLastName='" + newLastName + '\'' +
        ", newDateOfBirth=" + newDateOfBirth +
        ", newGender=" + newGender +
        ", newRetainAddress=" + newRetainAddress +
        ']';
  }

}
