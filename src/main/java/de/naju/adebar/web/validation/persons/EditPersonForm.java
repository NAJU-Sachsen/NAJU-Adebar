package de.naju.adebar.web.validation.persons;

import de.naju.adebar.model.core.Email;
import de.naju.adebar.model.core.PhoneNumber;
import de.naju.adebar.web.validation.core.AddressForm;
import de.naju.adebar.web.validation.persons.participant.EditParticipantForm;
import de.naju.adebar.web.validation.persons.relatives.EditParentProfileForm;

/**
 * POJO representation of the edit person form
 *
 * @author Rico Bergmann
 */
public class EditPersonForm {

  private String firstName;

  private String lastName;

  private String email;
  private String phoneNumber;
  private AddressForm address;
  private boolean participant;

  private EditParticipantForm participantForm;
  private EditParentProfileForm parentForm;

  /**
   * Full constructor
   *
   * @param firstName the person's first name. May not be empty.
   * @param lastName the person's last name. May not be empty.
   * @param email the person's email
   * @param phoneNumber the person's phone number
   * @param address the person's address
   * @param participantForm the person's participant form. May be {@code null} if the person is
   *     no participant
   */
  public EditPersonForm(String firstName, String lastName, Email email, PhoneNumber phoneNumber,
      AddressForm address, EditParticipantForm participantForm, EditParentProfileForm parentForm) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email != null ? email.getValue() : null;
    this.phoneNumber = phoneNumber != null ? phoneNumber.getValue() : null;
    this.address = address;
    this.participant = participantForm != null;
    this.participantForm = participantForm;
    this.parentForm = parentForm;
  }

  /**
   * Default constructor
   */
  public EditPersonForm() {}

  /**
   * @return the person's first name
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * @param firstName the person's first name. May not be empty.
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * @return the person's last name
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * @param lastName the person's last name. May not be empty.
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * @return the person's email
   */
  public String getEmail() {
    return email;
  }

  /**
   * @param email the person's email
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * @return whether the form contains an email address (which may however be invalid)
   */
  public boolean hasEmail() {
    return email != null && !email.isEmpty();
  }

  /**
   * @return the person's phone number
   */
  public String getPhoneNumber() {
    return phoneNumber;
  }

  /**
   * @param phoneNumber the person's phone number
   */
  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  /**
   * @return whether the form contains a phone number (which may however be invalid)
   */
  public boolean hasPhoneNUmber() {
    return phoneNumber != null && !phoneNumber.isEmpty();
  }

  /**
   * @return the person's address
   */
  public AddressForm getAddress() {
    return address;
  }

  /**
   * @param address the person's address
   */
  public void setAddress(AddressForm address) {
    this.address = address;
  }

  /**
   * @return whether the person is a camp participant
   */
  public boolean isParticipant() {
    return participant;
  }

  /**
   * @param participant whether the person is a camp participant
   */
  public void setParticipant(boolean participant) {
    this.participant = participant;
  }

  /**
   * @return whether the person form contains participant information
   */
  public boolean hasParticipantForm() {
    return participantForm != null;
  }

  /**
   * @return the person's participant form. May be {@code null} if the person is no participant
   */
  public EditParticipantForm getParticipantForm() {
    return participantForm;
  }

  /**
   * @param participantForm the person's participant form. May be {@code null} if the person is
   *     no participant
   */
  public void setParticipantForm(EditParticipantForm participantForm) {
    this.participantForm = participantForm;
  }

  /**
   * @return whether the form contains parent-related information
   */
  public boolean hasParentForm() {
    return parentForm != null;
  }

  /**
   * @return the person's parent form. May be {@code null} if the person is no parent of nobody.
   */
  public EditParentProfileForm getParentForm() {
    return parentForm;
  }

  /**
   * @param parentForm the person's parent form. May be {@code null} if the person is no
   *     parent.
   */
  public void setParentForm(EditParentProfileForm parentForm) {
    this.parentForm = parentForm;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "EditPersonForm [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
        + ", phoneNumber=" + phoneNumber + ", address=" + address + ", participant=" + participant
        + ", participantForm=" + participantForm + ", parentForm=" + parentForm + "]";
  }

}
