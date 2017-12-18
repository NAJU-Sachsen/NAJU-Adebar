package de.naju.adebar.controller.forms;

/**
 * Model POJO for addresses. For most use-cases a subclass should be used. The fields are set by
 * Thymeleaf when the associated form is submitted.
 *
 * @author Rico Bergmann
 */
public class AddressForm {

  private String street;
  private String zip;
  private String city;

  public AddressForm(String street, String zip, String city) {
    this.street = street;
    this.zip = zip;
    this.city = city;
  }

  public AddressForm() {}

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getZip() {
    return zip;
  }

  public void setZip(String zip) {
    this.zip = zip;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

}
