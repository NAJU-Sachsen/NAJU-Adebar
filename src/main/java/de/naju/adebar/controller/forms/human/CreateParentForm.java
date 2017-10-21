package de.naju.adebar.controller.forms.human;

/**
 * Model POJO for new parents. The fields are set by Thymeleaf when the associated form is
 * submitted.
 *
 * @author Rico Bergmann
 */
public class CreateParentForm {

  private String firstName;
  private String lastName;
  private String email;
  private String phoneNumber;
  private boolean useChildAddress;

  public CreateParentForm() {}

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

  public boolean isUseChildAddress() {
    return useChildAddress;
  }

  public void setUseChildAddress(boolean useChildAddress) {
    this.useChildAddress = useChildAddress;
  }


}
