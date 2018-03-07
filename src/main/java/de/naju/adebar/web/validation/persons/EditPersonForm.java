package de.naju.adebar.web.validation.persons;

import org.hibernate.validator.constraints.NotEmpty;
import de.naju.adebar.model.Email;
import de.naju.adebar.model.PhoneNumber;
import de.naju.adebar.web.validation.core.AddressForm;

/**
 * POJO representation of the edit person form
 *
 * @author Rico Bergmann
 *
 */
public class EditPersonForm {

  @NotEmpty
  private String firstName;

  @NotEmpty
  private String lastName;

  private Email email;
  private PhoneNumber phoneNumber;
  private AddressForm address;

  /**
   * Full constructor
   *
   * @param firstName the person's first name. May not be empty.
   * @param lastName the person's last name. May not be empty.
   * @param email the person's email
   * @param phoneNumber the person's phone number
   * @param address the person's address
   */
  public EditPersonForm(String firstName, String lastName, Email email, PhoneNumber phoneNumber,
      AddressForm address) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.address = address;
  }

  /**
   * Default constructor
   */
  public EditPersonForm() {
    this.address = new AddressForm();
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
  public Email getEmail() {
    return email;
  }

  /**
   * @return the person's phone number
   */
  public PhoneNumber getPhoneNumber() {
    return phoneNumber;
  }

  /**
   * @return the person's address
   */
  public AddressForm getAddress() {
    return address;
  }

  /**
   * @param firstName the person's first name. May not be empty.
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * @param lastName the person's last name. May not be empty.
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * @param email the person's email
   */
  public void setEmail(Email email) {
    this.email = email;
  }

  /**
   * @param phoneNumber the person's phone number
   */
  public void setPhoneNumber(PhoneNumber phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  /**
   * @param address the person's address
   */
  public void setAddress(AddressForm address) {
    this.address = address;
  }

  /*
   * (non-Javadoc)
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return "EditPersonForm [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
        + ", phoneNumber=" + phoneNumber + ", address=" + address + "]";
  }

}
