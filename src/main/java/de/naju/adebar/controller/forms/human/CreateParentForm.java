package de.naju.adebar.controller.forms.human;

/**
 * Model POJO for new parents. The fields are set by Thymeleaf when the associated form is
 * submitted.
 *
 * @author Rico Bergmann
 */
public class CreateParentForm extends PersonForm {

  private String phoneNumber;
  private boolean useChildAddress;

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
