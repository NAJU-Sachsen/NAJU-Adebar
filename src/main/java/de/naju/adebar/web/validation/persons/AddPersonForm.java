package de.naju.adebar.web.validation.persons;

import de.naju.adebar.model.core.Email;
import de.naju.adebar.model.core.PhoneNumber;
import de.naju.adebar.web.validation.core.AddressForm;
import de.naju.adebar.web.validation.persons.activist.AddActivistForm;
import de.naju.adebar.web.validation.persons.participant.AddParticipantForm;
import de.naju.adebar.web.validation.persons.referent.AddReferentForm;
import javax.validation.constraints.NotEmpty;

/**
 * POJO representation of the data inside the 'add person' form. The form is structured into "sub
 * forms" which have their own POJOs backing them up
 *
 * @author Rico Bergmann
 * @see AddParticipantForm
 * @see AddActivistForm
 * @see AddReferentForm
 * @see AddressForm
 */
public class AddPersonForm {

  @NotEmpty
  private String firstName;

  @NotEmpty
  private String lastName;

  private String email;
  private String phone;
  private AddressForm address;
  private boolean activist;
  private AddActivistForm activistForm;
  private boolean participant;
  private AddParticipantForm participantForm;
  private boolean referent;
  private AddReferentForm referentForm;

  /**
   * Full constructor
   *
   * @param firstName the person's first name
   * @param lastName the person's last name
   * @param email the person's email address
   * @param phone the person's phone number
   * @param address the person's address
   */
  public AddPersonForm(String firstName, String lastName, Email email,
      PhoneNumber phone, AddressForm address) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email.getValue();
    this.phone = phone.getValue();
    this.address = address;
  }

  /**
   * Default constructor
   */
  public AddPersonForm() {
  }

  /**
   * @return the person's first name
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * @param firstName the person's first name
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
   * @param lastName the person's last name
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * @return the person's email address
   */
  public String getEmail() {
    return email;
  }

  /**
   * @param email the person's email address
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * @return the person's phone number
   */
  public String getPhone() {
    return phone;
  }

  /**
   * @param phone the person's phone number
   */
  public void setPhone(String phone) {
    this.phone = phone;
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
   * @return whether the person is an activist
   */
  public boolean isActivist() {
    return activist;
  }

  /**
   * @param activist whether the person is an activist
   */
  public void setActivist(boolean activist) {
    this.activist = activist;
  }

  /**
   * @return the activist-related data
   */
  public AddActivistForm getActivistForm() {
    return activistForm;
  }

  /**
   * @param activistForm the activist-related data
   */
  public void setActivistForm(AddActivistForm activistForm) {
    this.activistForm = activistForm;
  }

  /**
   * @return whether the person is a participant
   */
  public boolean isParticipant() {
    return participant;
  }

  /**
   * @param participant whether the person is a participant
   */
  public void setParticipant(boolean participant) {
    this.participant = participant;
  }

  /**
   * @return the participation-related data
   */
  public AddParticipantForm getParticipantForm() {
    return participantForm;
  }

  /**
   * @param participantForm the participation-related data
   */
  public void setParticipantForm(AddParticipantForm participantForm) {
    this.participantForm = participantForm;
  }

  /**
   * @return whether the person is a referent
   */
  public boolean isReferent() {
    return referent;
  }

  /**
   * @param referent whether the person is a referent
   */
  public void setReferent(boolean referent) {
    this.referent = referent;
  }

  /**
   * @return the referent-related data
   */
  public AddReferentForm getReferentForm() {
    return referentForm;
  }

  /**
   * @param referentForm the referent-related data
   */
  public void setReferentForm(AddReferentForm referentForm) {
    this.referentForm = referentForm;
  }

  @Override
  public String toString() {
    return "AddPersonForm [" +
        "firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", email=" + email +
        ", phone=" + phone +
        ", address=" + address +
        ", activist=" + activist +
        ", activistForm=" + activistForm +
        ", participant=" + participant +
        ", participantForm=" + participantForm +
        ", referent=" + referent +
        ", referentForm=" + referentForm +
        ']';
  }

}
