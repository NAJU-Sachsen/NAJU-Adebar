package de.naju.adebar.controller.forms.human;

import de.naju.adebar.controller.forms.AddressForm;

/**
 * Model POJO for person data. Only contains some general data - for most use-cases a subclass
 * should be used. The fields are set by Thymeleaf when the associated form is submitted.
 *
 * @author Rico Bergmann
 */
public class PersonForm extends AddressForm {

  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;

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

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

}
