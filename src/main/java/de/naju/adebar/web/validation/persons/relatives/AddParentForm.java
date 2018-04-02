package de.naju.adebar.web.validation.persons.relatives;

import de.naju.adebar.model.core.Email;
import de.naju.adebar.model.core.PhoneNumber;
import de.naju.adebar.model.persons.Person;
import de.naju.adebar.web.validation.NewOrExistingEntityForm;

/**
 * POJO representation of an 'add parent' form. Each parent form should be created in terms of an
 * existing person (a.k.a. "the child"). Some of the child's data may actually be reused - such as
 * the last name or the address. Furthermore the parent may either be an existing person, or be
 * created "from scratch".
 *
 * @author Rico Bergmann
 */
public class AddParentForm implements NewOrExistingEntityForm {

  private Person existingPerson;
  private String newFirstName;
  private String newLastName;
  private String newEmail;
  private String newPrivatePhone;
  private String newWorkPhone;
  private String newLandlinePhone;
  private boolean newRetainAddress;

  /**
   * Full constructor
   *
   * @param person the person the parent is created for
   */
  public AddParentForm(Person person) {
    this.newLastName = person.getLastName();
    this.newEmail = person.hasEmail() ? person.getEmail().getValue() : null;
    this.newPrivatePhone = person.hasPhoneNumber() ? person.getPhoneNumber().getValue() : null;
  }

  /**
   * Default constructor
   */
  public AddParentForm() {}

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
   * @return the new person's email. We may not return an {@link Email} instance here, as the email
   *     may still need to be validated and hence may be invalid.
   */
  public String getNewEmail() {
    return newEmail;
  }

  /**
   * @param newEmail the new person's email
   */
  public void setNewEmail(String newEmail) {
    this.newEmail = newEmail;
  }

  /**
   * @return whether the form contains data for an email address (although this address may
   *     potentially be invalid)
   */
  public boolean hasNewEmail() {
    return newEmail != null && !newEmail.isEmpty();
  }

  /**
   * @return the new person's phone number. We may not return an {@link PhoneNumber} instance here,
   *     as the phone number may still need to be validated and hence may be invalid.
   */
  public String getNewPrivatePhone() {
    return newPrivatePhone;
  }

  /**
   * @param newPhone the new person's phone number
   */
  public void setNewPrivatePhone(String newPhone) {
    this.newPrivatePhone = newPhone;
  }

  /**
   * @return whether the form contains data for a phone number (although this may potentially be
   *     invalid)
   */
  public boolean hasNewPrivatePhone() {
    return newPrivatePhone != null && !newPrivatePhone.isEmpty();
  }

  /**
   * @return the new parent's phone number at work
   */
  public String getNewWorkPhone() {
    return newWorkPhone;
  }

  /**
   * @param newWorkPhone the new parent's phone number at work
   */
  public void setNewWorkPhone(String newWorkPhone) {
    this.newWorkPhone = newWorkPhone;
  }

  /**
   * @return whether the parent has a special phone number at work
   */
  public boolean hasNewWorkPhone() {
    return newWorkPhone != null && !newWorkPhone.isEmpty();
  }

  /**
   * @return the new parent's phone number at home
   */
  public String getNewLandlinePhone() {
    return newLandlinePhone;
  }

  /**
   * @param newLandlinePhone the new parent's phone number at home
   */
  public void setNewLandlinePhone(String newLandlinePhone) {
    this.newLandlinePhone = newLandlinePhone;
  }

  /**
   * @return whether the parent has a special phone number at home
   */
  public boolean hasNewLandlinePhone() {
    return newLandlinePhone != null && !newLandlinePhone.isEmpty();
  }

  /**
   * @return whether the parent should re-use the address of its child
   */
  public boolean isNewRetainAddress() {
    return newRetainAddress;
  }

  /**
   * @param newRetainAddress whether the parent should re-use the address of its child
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
    return "AddParentForm [existingPerson=" + existingPerson + ", newFirstName=" + newFirstName
        + ", newLastName=" + newLastName + ", newEmail=" + newEmail + ", newPrivatePhone="
        + newPrivatePhone + ", newWorkPhone=" + newWorkPhone + ", newLandlinePhone="
        + newLandlinePhone + ", newRetainAddress=" + newRetainAddress + "]";
  }

}
