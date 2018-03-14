package de.naju.adebar.web.validation.persons;

import de.naju.adebar.model.Email;
import de.naju.adebar.model.PhoneNumber;
import de.naju.adebar.web.validation.core.AddressForm;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * POJO representation of the edit person form
 *
 * @author Rico Bergmann
 */
public class EditPersonForm {

  @NotEmpty
  private String firstName;

  @NotEmpty
  private String lastName;

  private Email email;
  private PhoneNumber phoneNumber;
  private AddressForm address;
  private boolean participant;

  private EditParticipantForm participantForm;

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
      AddressForm address, EditParticipantForm participantForm) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.address = address;
    this.participant = participantForm != null;
    this.participantForm = participantForm;
  }

  /**
   * Default constructor
   */
  public EditPersonForm() {
  }

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
  public Email getEmail() {
    return email;
  }

  /**
   * @param email the person's email
   */
  public void setEmail(Email email) {
    this.email = email;
  }

  /**
   * @return the person's phone number
   */
  public PhoneNumber getPhoneNumber() {
    return phoneNumber;
  }

  /**
   * @param phoneNumber the person's phone number
   */
  public void setPhoneNumber(PhoneNumber phoneNumber) {
    this.phoneNumber = phoneNumber;
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

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "EditPersonForm [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
        + ", phoneNumber=" + phoneNumber + ", address=" + address + ", participant=" + participant
        + ", participantForm=" + participantForm + "]";
  }

}
